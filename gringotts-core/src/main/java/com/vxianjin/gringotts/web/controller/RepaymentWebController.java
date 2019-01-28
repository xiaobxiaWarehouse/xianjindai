package com.vxianjin.gringotts.web.controller;

import com.google.gson.Gson;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.ArrayUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserBankService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenheFail;
import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenhezhong;


/***
 * 还款
 *
 * @author Administrator
 */
@Controller
@RequestMapping("repayment/")
@SuppressWarnings("all")
public class RepaymentWebController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RepaymentWebController.class);
    @Autowired
    private RepaymentService repaymentService;
    @Autowired
    private RepaymentDetailService repaymentDetailService;
    @Autowired
    private IBorrowOrderService borrowOrderService;
    @Autowired
    private IUserBankService userBankService;
    @Autowired
    private IUserService userService;
    @Autowired
    private RenewalRecordService renewalRecordService;
    @Autowired
    private IOutOrdersService outOrdersService;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 还款详情页面
     */
    @RequestMapping("detail")
    public String detail(HttpServletRequest request, Model model, Integer id) throws Exception {
        String errorReturnUrl = request.getParameter("errorReturnUrl");
        String successReturnUrl = request.getParameter("successReturnUrl");
        String rtl = request.getParameter("rtl");
        boolean isTg = false;
        if (StringUtils.isNotBlank(errorReturnUrl)) {
            model.addAttribute("errorReturnUrl", errorReturnUrl);
            isTg = true;
        }
        if (StringUtils.isNotBlank(successReturnUrl)) {
            isTg = true;
            model.addAttribute("successReturnUrl", successReturnUrl);
        }
        if (StringUtils.isNotBlank(rtl)) {
            model.addAttribute("rtl", rtl);
            isTg = true;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // User user = this.loginFrontUser(request);
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);//查找借款订单
        logger.info("borrowOrder = " + bo.toString());
        //UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        UserCardInfo info = null;
        try {

            info = userService.findBankCardByCardNo(bo.getCardNo());
            if (info != null) {
                info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
            } else {
                info = new UserCardInfo();
                info.setBankName("尾号");
                info.setCard_no(bo.getCardNo().substring(bo.getCardNo().length() - 4));
            }
        } catch (Exception e) {
            logger.error("UserCardInfo error:", e);
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag", "0");
        map.put("title", "申请提交成功" + dateFormat.format(bo.getCreatedAt()));
        map.put("body", "申请借款" + (bo.getMoneyAmount() / 100.00) + "元，期限" + bo.getLoanTerm() + BorrowOrder.loanMethed.get(bo.getLoanMethod()) + "，手续费"
                + (bo.getLoanInterests() / 100.00) + "元");
        list.add(map);
        map = new HashMap<String, String>();
        logger.info("borrowOrderId=" + bo.getId() + " status=" + bo.getStatus());
        if (borrowStatusMap_shenhezhong.containsKey(bo.getStatus())) {//待初审(待机审)、初审通过(机审通过)，待复审 显示审核中
            System.out.println("step = 11");
            map.put("tag", "1");
            map.put("title", "审核中");
            map.put("body", "已进入风控审核状态，请您耐心等待");
            list.add(map);
        } else if (borrowStatusMap_shenheFail.containsKey(bo.getStatus())) {//机审拒绝、初审驳回、复审驳回 显示审核不通过
            System.out.println("step = 22");
            map.put("tag", "1");
            map.put("title", "审核不通过");
            // 如果是 机审拒绝、初审驳回 显示初审备注
            if (bo.getStatus().equals(BorrowOrder.STATUS_CSBH)) {
                if (bo.getVerifyTrialUser().equals("机审")) {
                    map.put("body", "信用评分不足");
                } else {
                    map.put("body", bo.getVerifyTrialRemark());
                }

            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FSBH)) {//复审拒绝 显示复审备注
                map.put("body", bo.getVerifyReviewRemark());
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKBH)) {
                map.put("body", bo.getVerifyLoanRemark());
            } else {
                map.put("body", "经审核您不符合借款要求");
            }
            map.put("body", "信用评分不足");
            list.add(map);
        } else {
            map.put("tag", "0");
            map.put("title", "审核通过" + dateFormat.format(bo.getVerifyReviewTime()));
            map.put("body", "恭喜通过风控审核");
            list.add(map);
            map = new HashMap<String, String>();
            if (bo.getStatus().equals(BorrowOrder.STATUS_FSTG)) {
                map.put("tag", "1");
                map.put("title", "打款审核中");
                map.put("body", "已进入打款审核状态，请您耐心等待");
                list.add(map);
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKZ) || bo.getStatus().equals(BorrowOrder.STATUS_FKSB)) {
                if (bo.getPaystatus().equals(BorrowOrder.SUB_SUBMIT)) {
                    map.put("tag", "1");
                    map.put("title", "打款中");
                    map.put("body", "已进入银行处理流程，请您耐心等待");
                    list.add(map);
                } else {
                    map.put("tag", "1");
                    map.put("title", "打款中");
                    map.put("body", "已进入打款流程，请您耐心等待");
                    list.add(map);
                }
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKBH)) {
                map.put("tag", "1");
                map.put("title", "打款审核不通过");
                map.put("body", bo.getVerifyLoanRemark());
                list.add(map);
            } else {
                System.out.println("step  = 4");
                Map<String, Object> mapT = new HashMap<String, Object>();
                mapT.put("assetOrderId", bo.getId());
                Repayment repayment = repaymentService.findOneRepayment(mapT);
                System.out.println("repayment  = " + repayment);
                map.put("tag", "0");
                map.put("title", "打款成功");
                map.put("body", "打款至" + info.getBankName() + "(" + info.getCard_no() + ")");
                list.add(map);
                map = new HashMap<String, String>();
                map.put("tag", "1");
                if (bo.getStatus().equals(BorrowOrder.STATUS_HKZ) || bo.getStatus().equals(BorrowOrder.STATUS_BFHK)) {
                    try {
                        String repayStatus = jedisCluster.get("FUYOU_REPAY_" + repayment.getId());
                        String renewalStatus = jedisCluster.get("FUYOU_RENEWAL_" + repayment.getId());
                        if ("true".equals(repayStatus)) {
                            map.put("title", "还款提交中");
                            model.addAttribute("applying", "true");
                        } else if ("true".equals(renewalStatus)) {
                            map.put("title", "续期提交中");
                            model.addAttribute("applying", "true");
                        } else {
                            map.put("title", DateUtil.daysBetween(new Date(), repayment.getRepaymentTime()) + "天后还款");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    map.put("body", "请于" + dateFormat.format(repayment.getRepaymentTime()) + "前将还款金额存入银行卡中");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_BFHK)) {
                    map.put("title", "部分还款");
                    map.put("body", "请于" + dateFormat.format(repayment.getRepaymentTime()) + "前将还款金额存入银行卡中");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_YYQ)) {
                    map.put("title", "已逾期");
                    map.put("body", "您的借款已逾期，请尽快完成还款操作");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_YHZ)) {
                    map.put("title", "已坏账");
                    map.put("body", "您的借款已坏账，详情请联系客服");
                } else {// if(bo.getStatus().equals(BorrowOrder.STATUS_YHK))
                    map.put("title", "已还款");
                    map.put("body", "恭喜还款成功，又积攒了一份信用");
                }
                list.add(map);
                model.addAttribute("repayment", repayment);
            }
        }
        model.addAttribute("list", list);
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        model.addAttribute("appName", request.getParameter("appName"));
        if (isTg) {
            model.addAttribute("sgd", request.getParameter("sgd"));
            return "repayment/tg/repaymentDetail2";
        }
        return "repayment/repaymentDetail";
    }

    /**
     * 还款方式选择页面
     */
    @RequestMapping("repay-choose")
    public String repayChoose(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment repayment = repaymentService.findOneRepayment(map);
        model.addAttribute("repayment", repayment);
        return "repayment/repaymentChoose";
    }

    /**
     * 还款支付页面（易宝支付）
     */
    @RequestMapping("repay-pay-yeepay")
    public String repayYeepay(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment repayment = repaymentService.findOneRepayment(map);
        Gson gson = new Gson();
        String msg = "";
        if (bo != null) {
            //该用户银行卡列表
            List<UserCardInfo> userBankCardList = userService.findUserBankCardList(bo.getUserId());
            for (UserCardInfo usercard : userBankCardList) {
                String cardNo = usercard.getCard_no();
                usercard.setCard_no(cardNo.substring(cardNo.length() - 4, cardNo.length()));
            }
            String json = "";
            if (ArrayUtil.isNotEmpty(userBankCardList)) {
                json = gson.toJson(userBankCardList);
            }
            model.addAttribute("repayment", repayment);
            model.addAttribute("bankCardList", json);
        } else {
            msg = "不存在借款订单";
            model.addAttribute("message", msg);
        }
        return "repayment/repaymentYeepay";
    }

    /**
     * 还款支付页面（先锋支付）
     */
    @RequestMapping("repay-pay-ucfpay")
    public String repayUcfpay(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment repayment = repaymentService.findOneRepayment(map);
        model.addAttribute("repayment", repayment);
        return "repayment/repaymentUcfpay";
    }

    /**
     * 还款支付页面（支付宝支付）
     */
    @RequestMapping("repay-pay-alipay")
    public String repayAlipay(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment repayment = repaymentService.findOneRepayment(map);
        model.addAttribute("repayment", repayment);
        return "repayment/repaymentAlipay";
    }

    /**
     * 还款方式页面
     */
    @RequestMapping("repay-type")
    public String repayType(HttpServletRequest request, Model model, Integer id) {
        model.addAttribute("id", id);
        return "repayment/repaymentType";
    }

    /*******************************************************************************************/
    //判断是否能续期
    @RequestMapping("renewal-whether")
    public void renewalWhether(HttpServletResponse response, Integer id) {

        if(null == id || "".equals(id)){
            ResponseContent rest = new ResponseContent("-100", "输入信息不合法");
            SpringUtils.renderJson(response, rest);
            return;
        }

        Repayment re = repaymentService.selectByPrimaryKey(id);
        if (re == null) {
            ResponseContent rest = new ResponseContent("-100", "无法获取该笔订单信息");
            SpringUtils.renderJson(response, rest);
            return;
        }
        //最大逾期天数
        String overdue_days = PropertiesConfigUtil.get("OVERDUE_DAYS");

        if (DateUtil.daysBetween(re.getRepaymentTime(), new Date()) > Integer.parseInt(overdue_days)) {
            ResponseContent rest = new ResponseContent("-101", "您已逾期超过"+overdue_days+"天，不能续期，请联系客服或先去还款");
            SpringUtils.renderJson(response, rest);
            return;
        }
        if (re.getRepaymentedAmount() - re.getRepaymentPrincipal() - re.getRepaymentInterest() > 0) {
            ResponseContent rest = new ResponseContent("-102", "您的本金已还完，不能继续申请续期");
            SpringUtils.renderJson(response, rest);
            return;
        }
        ResponseContent rest = new ResponseContent("0", "续期成功");
        SpringUtils.renderJson(response, rest);
    }

    /**
     * 续期方式选择页面
     *
     * @return
     */
    @RequestMapping("renewal-choose")
    public String renewalChoose(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        model.addAttribute("info", info);
        model.addAttribute("bo", bo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment repayment = repaymentService.findOneRepayment(map);
        model.addAttribute("repayment", repayment);
        return "repayment/renewalChoose";
    }

    /**
     * 续期支付页面（先锋支付）
     */
    @RequestMapping("renewal-pay-ucfpay")
    public String renewalUcfpay(HttpServletRequest request, Model model, Integer id) {
        String errorReturnUrl = request.getParameter("errorReturnUrl");
        String successReturnUrl = request.getParameter("successReturnUrl");
        String rtl = request.getParameter("rtl");
        boolean isTg = false;
        if (StringUtils.isNotBlank(errorReturnUrl)) {
            model.addAttribute("errorReturnUrl", errorReturnUrl);
            isTg = true;
        }
        if (StringUtils.isNotBlank(successReturnUrl)) {
            model.addAttribute("successReturnUrl", successReturnUrl);
            isTg = true;
        }
        if (StringUtils.isNotBlank(rtl)) {
            model.addAttribute("rtl", rtl);
            isTg = true;
        }
        Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
        // 续期费
        String renewalFee = keys.get("renewal_fee");

        BorrowOrder bo = borrowOrderService.findOneBorrow(id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment re = repaymentService.findOneRepayment(map);
        // 待还总金额
        Long waitRepay = re.getRepaymentAmount() - re.getRepaymentedAmount();
        // 待还滞纳金
        Long waitLate = Long.parseLong(String.valueOf(re.getPlanLateFee() - re.getTrueLateFee()));
        // 待还本金
        Long waitAmount = waitRepay - waitLate;
        // 服务费
        Integer loanApr = bo.getMoneyAmount() * bo.getApr() / 10000; // 服务费
        Long allCount = waitLate + loanApr + Long.parseLong(renewalFee);
        //用户银行卡信息
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        logger.info("usercardinfo :" + info.getCard_no());
        model.addAttribute("bo", bo);
        model.addAttribute("info", info);
        model.addAttribute("waitAmount", waitAmount);
        model.addAttribute("loanApr", loanApr);
        model.addAttribute("renewalFee", renewalFee);
        model.addAttribute("waitLate", waitLate);
        model.addAttribute("allCount", allCount);
        if (isTg) {
            model.addAttribute("sgd", request.getParameter("sgd"));
            return "repayment/tg/renewalRenewal2";
        }
        return "repayment/renewalUcfpay";
    }

    /**
     * 续期支付页面（易宝支付）
     */
    @RequestMapping("renewal-pay-yeepay")
    public String renewalYeepay(HttpServletRequest request, Model model, Integer id) {
        String errorReturnUrl = request.getParameter("errorReturnUrl");
        String successReturnUrl = request.getParameter("successReturnUrl");
        String rtl = request.getParameter("rtl");
        boolean isTg = false;
        if (StringUtils.isNotBlank(errorReturnUrl)) {
            model.addAttribute("errorReturnUrl", errorReturnUrl);
            isTg = true;
        }
        if (StringUtils.isNotBlank(successReturnUrl)) {
            model.addAttribute("successReturnUrl", successReturnUrl);
            isTg = true;
        }
        if (StringUtils.isNotBlank(rtl)) {
            model.addAttribute("rtl", rtl);
            isTg = true;
        }
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);
        // 续期手续费
        BigDecimal renewalFee = bo.getRenewalPoundage();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment re = repaymentService.findOneRepayment(map);
        // 待还总金额
        Long waitRepay = re.getRepaymentAmount() - re.getRepaymentedAmount();
        // 待还滞纳金
        Long waitLate = Long.parseLong(String.valueOf(re.getPlanLateFee() - re.getTrueLateFee()));
        // 待还本金
        Long waitAmount = waitRepay - waitLate;
        // 续期费
        Integer loanApr = bo.getRenewalFee().intValue();

        Long allCount = waitLate + loanApr + renewalFee.longValue();
        //用户银行卡信息
        UserCardInfo info = userService.findUserBankCard(bo.getUserId());
        info.setCard_no(info.getCard_no().substring(info.getCard_no().length() - 4));
        logger.info("usercardinfo :" + info.getCard_no());
        Gson gson = new Gson();
        //获取银行卡列表
        List<UserCardInfo> userBankCardList = userService.findUserBankCardList(bo.getUserId());
        for (UserCardInfo usercard : userBankCardList) {
            String cardNo = usercard.getCard_no();
            usercard.setCard_no(cardNo.substring(cardNo.length() - 4, cardNo.length()));
        }
        String json = "";
        if (ArrayUtil.isNotEmpty(userBankCardList)) {
            json = gson.toJson(userBankCardList);
        }
        model.addAttribute("bo", bo);
        model.addAttribute("info", info);
        model.addAttribute("waitAmount", waitAmount);
        model.addAttribute("loanApr", loanApr);
        model.addAttribute("renewalFee", renewalFee);
        model.addAttribute("waitLate", waitLate);
        model.addAttribute("allCount", allCount);
        model.addAttribute("bankCardList", json);
        if (isTg) {
            model.addAttribute("sgd", request.getParameter("sgd"));
            return "repayment/tg/renewalRenewal2";
        }
        return "repayment/renewalYeepay";
    }

    /**
     * 续期支付页面（支付宝）
     */
    @RequestMapping("renewal-pay-alipay")
    public String renewalPayAlipay(HttpServletRequest request, Model model, Integer id) {
        BorrowOrder bo = borrowOrderService.findOneBorrow(id);

        // 续期手续费
        BigDecimal renewalFee = bo.getRenewalPoundage();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assetOrderId", bo.getId());
        Repayment re = repaymentService.findOneRepayment(map);
        // 待还总金额
        Long waitRepay = re.getRepaymentAmount() - re.getRepaymentedAmount();
        // 待还滞纳金
        Long waitLate = Long.parseLong(String.valueOf(re.getPlanLateFee() - re.getTrueLateFee()));
        // 待还本金
        Long waitAmount = waitRepay - waitLate;
        // 续期费
        Integer loanApr = bo.getRenewalFee().intValue();

        Long allCount = waitLate + loanApr + renewalFee.longValue();
        //用户银行卡信息
        model.addAttribute("bo", bo);
        model.addAttribute("waitAmount", waitAmount);
        model.addAttribute("loanApr", loanApr);
        model.addAttribute("renewalFee", renewalFee);
        model.addAttribute("waitLate", waitLate);
        model.addAttribute("allCount", allCount);
        return "repayment/renewalAlipay";
    }

    /**
     * 催收减免回调
     */
    @RequestMapping("mitigate/{userId}/{repaymentId}/{money}/{mitigateId}/{sign}")
    public void mitigate(HttpServletResponse response, @PathVariable Integer userId, @PathVariable Integer repaymentId, @PathVariable Long money,
                         @PathVariable String mitigateId, @PathVariable String sign) {
        ResponseContent rest;
        try {
            if (MD5Util.MD5(AESUtil.encrypt("" + userId + repaymentId + money + mitigateId, CollectionConstant.getCollectionSign())).equals(sign)) {
                Repayment re = repaymentService.selectByPrimaryKey(repaymentId);
                //re.setWithholdId(mitigateId);
                //User user = userService.searchByUserid(userId);
                if (money <= 0) {
                    rest = new ResponseContent("-101", "减免失败,减免金额为空");
                } else if (re.getRepaymentedAmount() < (re.getRepaymentPrincipal() + re.getRepaymentInterest())) {
                    rest = new ResponseContent("-101", "减免失败,本金未还清");
                } else if ((re.getRepaymentedAmount() + money) > re.getRepaymentAmount()) {
                    rest = new ResponseContent("-101", "减免失败,减免金额超出剩余应还金额");
                } else {
                    RepaymentDetail detail = new RepaymentDetail();
                    detail.setUserId(userId);
                    detail.setAssetRepaymentId(repaymentId);
                    detail.setTrueRepaymentMoney(money);
                    detail.setCreatedAt(new Date());
                    detail.setRepaymentType(RepaymentDetail.TYPE_OFF_LINE_DEDUCTION);
                    detail.setRemark("催收减免");
                    detail.setUserId(userId);
                    detail.setUpdatedAt(new Date());
                    detail.setStatus(RepaymentDetail.STATUS_SUC);
                    detail.setAssetOrderId(re.getAssetOrderId());
                    repaymentDetailService.insertSelective(detail);
                    repaymentService.repay(re, detail,OrderChangeAction.COLLECTION_JIANMIAN_ACTION.getCode());
                    rest = new ResponseContent("0", "减免成功");
                }
            } else {
                rest = new ResponseContent("-101", "参数有误");
                logger.error("催收减免 param error mitigateId userId=" + userId + " repaymentId=" + repaymentId + " money=" + money + " withholdId="
                        + mitigateId + " sign= " + sign + " mysign=" + CollectionConstant.getCollectionSign());
            }
        } catch (Exception e) {
            rest = new ResponseContent("-100", "系统错误");
            logger.error("", e);
        }
        logger.info("催收减免 end mitigate userId=" + userId + " repaymentId=" + repaymentId + " code=" + rest.getCode());
        SpringUtils.renderJson(response, rest);
    }

    /**
     * 支付宝还款方式页面
     */
    @RequestMapping("repay-type-alipay")
    public String repaymentTypeAlipay(HttpServletRequest request, Model model, Integer id) {
        String userId = request.getParameter("userId");
        User user = userService.searchByUserid(Integer.parseInt(userId));
        model.addAttribute("realname", user.getRealname());
        model.addAttribute("phone", user.getUserPhone());
        model.addAttribute("id", id);

        return "repayment/repaymentTypeAlipay";
    }
}
