package com.vxianjin.gringotts.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.dao.IRepaymentDao;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.risk.dao.IRiskModelOrderDao;
import com.vxianjin.gringotts.risk.dao.IRiskModelScoreDao;
import com.vxianjin.gringotts.risk.dao.IRiskOrdersDao;
import com.vxianjin.gringotts.risk.pojo.*;
import com.vxianjin.gringotts.risk.service.IRiskService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.dao.IUserBankDao;
import com.vxianjin.gringotts.web.dao.IUserContactsDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IAutoRiskService;
import com.vxianjin.gringotts.web.util.autorisk.AutoRiskUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AutoRiskService implements IAutoRiskService {

    private static Logger logger = LoggerFactory.getLogger(AutoRiskService.class);

    @Resource
    private IRiskService riskService;
    @Autowired
    private IUserContactsDao userContactsDao;
    @Autowired
    private IUserDao userDao;
    @Resource
    private IRepaymentDao repaymentDao;
    @Autowired
    @Qualifier("userBankDaoImpl")
    private IUserBankDao userBankDao;
    @Autowired
    private IRiskOrdersDao riskOrdersDao;
    @Resource
    private IBorrowOrderDao borrowOrderDao;
    @Resource
    private IRiskModelScoreDao riskModelScoreDao;
    @Resource
    private IRiskModelOrderDao riskModelOrderDao;

    @Autowired
    private OrderLogComponent orderLogComponent;
    /**
     * 用户执行借款订单的风控信息
     *
     * @param assetBorrowId 订单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void dealRemoteCreditReport(Integer assetBorrowId) {
        logger.info("dealRemoteCreditReport start");
        logger.info("dealRemoteCreditReport assetBorrowId=" + assetBorrowId);
        autoReview(assetBorrowId);
    }

    /**
     * 开始处理dubbo风控信息
     *
     * @param assetBorrowId
     */
    private void autoReview(Integer assetBorrowId) {
        logger.info("autoReview start assetBorrowId=" + assetBorrowId);
        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(assetBorrowId);
        logger.info("autoReview borrowOrder=" + borrowOrder.toString());
        Integer userId = borrowOrder.getUserId();
        CreditUser creditUser = this.bulidRequestAutoRiskParams(userId, assetBorrowId);
        CreditReport creditReport = null;

        if(borrowOrder.getStatus() != 0){
            logger.error("adviceExecute failure borrowId = " + assetBorrowId);
            throw new RuntimeException("adviceExecute failure borrowId = " + assetBorrowId);
        }

        try {
            logger.info("autoReview creditUser=" + creditUser.toString());

            //远程调用dubbo信审信息
            creditReport = riskService.getUserCreditReport(creditUser, -1, false);

            if (creditReport == null) {
                logger.error("dealRemoteCreditReport error creditReport=null assetBorrowId=" + assetBorrowId);
//                BussinessLogUtil.warn("dealRemoteCreditReport error creditReport=null assetBorrowId = " +
//                        assetBorrowId, "dealRemoteCreditReport");
                return;

            }
            logger.info("autoReview identityCard=" + (creditReport != null ? creditReport.getIdentityCard() : "null"));

            //存储风控报文
            saveCreditReport(creditReport, userId, assetBorrowId);

            //存储模型分箱得分
            saveSubScore(creditUser, creditReport, userId, assetBorrowId);

        } catch (Exception e) {
            logger.error("autoReview error assetBorrowId = " + assetBorrowId);
            logger.error(e.getMessage(), e);
//            BussinessLogUtil.error("dealRemoteCreditReport error assetBorrowId = " + assetBorrowId,
//                    "dealRemoteCreditReport", e);
            return;
        }

        try {
            Reason executeReason = getSupplierReson(creditReport, "EXECUTE");

            Map<String, String> items = executeReason.getItems();
            String oldCustomerAdviceString = items.get("oldCustomerAdvice");
            //老用户意见（默认：null）
            Advice oldCustomerAdvice = null;
            if (oldCustomerAdviceString != null) {
                oldCustomerAdvice = Advice.valueOf(oldCustomerAdviceString);
            }

            //硬指标意见
            Advice inflexibleAdvice = Advice.valueOf(items.get("inflexibleAdvice"));

            //模型意见（默认：null）
            Advice modelAdvice = null;
            if (items.get("modelAdvice") != null) {
                modelAdvice = Advice.valueOf(items.get("modelAdvice"));
            }
            //模型信息
            RiskModel riskModel = new Gson().fromJson(items.get("riskModel"), RiskModel.class);
            //模型得分
            Integer totalScore = Integer.parseInt(items.get("totalScore"));

            Advice summaryAdvice = Advice.valueOf(items.get("summaryAdvice"));

            Boolean loanSwitchStatusIsOn = Boolean.valueOf(items.get("loanSwitchStatusIsOn"));

            Advice executeAdvice = Advice.valueOf(items.get("executeAdvice"));

            String inflexibleItems = items.get("inflexibleItems");

            //执行建议
            adviceExecute(assetBorrowId, userId, borrowOrder.getCustomerType(), oldCustomerAdvice, inflexibleAdvice,
                    modelAdvice, summaryAdvice, executeAdvice, riskModel, totalScore, loanSwitchStatusIsOn);

            updateAssetBorrowOrderByAutoRisk(assetBorrowId,inflexibleItems);

            logger.info("autoReview end assetBorrowId=" + assetBorrowId);
        } catch (Exception e) {
//            BussinessLogUtil.error("dealRemoteCreditReport index error borrowId = " + assetBorrowId, "autoReview", e);
            logger.error("dealRemoteCreditReport index error borrowId = " + assetBorrowId, e);
            throw e;
        }

    }

    /**
     * 标记机审订单以及机审相关信息
     * @param assetBorrowId
     * @param inflexibleItems
     */
    public void updateAssetBorrowOrderByAutoRisk(Integer assetBorrowId, String inflexibleItems) {
        BorrowOrder borrowOrder = new BorrowOrder();
        borrowOrder.setAutoExplain(inflexibleItems);
        borrowOrder.setId(assetBorrowId);
        if(StringUtils.isNotBlank(inflexibleItems)){
            borrowOrderDao.updateByPrimaryKeySelective(borrowOrder);
        }
    }

    /**
     * 根据 dubbo返回来的报文存入 risk_orders数据库中
     *
     * @param creditReport
     * @param userId
     * @param assetBorrowId
     */
    private void saveCreditReport(CreditReport creditReport, Integer userId, Integer assetBorrowId) {
        logger.info("saveCreditReport start");
        Gson gson = new Gson();
        String resultJson = gson.toJson(creditReport);
        RiskOrders riskOrders = new RiskOrders();
        riskOrders.setUserId(String.valueOf(userId));
        riskOrders.setOrderNo(UUID.randomUUID().toString());
        riskOrders.setReturnParams(resultJson);
        riskOrders.setOrderType("AUTO_RISK");
        riskOrders.setAssetBorrowId(assetBorrowId);
        RiskOrders riskOrdersSelect = riskOrdersDao.selectCreditReportByBorrowId(assetBorrowId);
        if (riskOrdersSelect != null) {
            riskOrders.setId(riskOrdersSelect.getId());
            int flag = riskOrdersDao.update(riskOrders);
            if (flag == 1) {
                logger.info("update success assetBorrowId = " + assetBorrowId);
            } else {
                logger.info("update fail assetBorrowId = " + assetBorrowId);
            }
        } else {
            int flag = riskOrdersDao.insertCreditReport(riskOrders);
            if (flag == 1) {
                logger.info("store success assetBorrowId = " + assetBorrowId);
            } else {
                logger.info("store fail assetBorrowId = " + assetBorrowId);
            }
        }
    }

    /**
     * 存储评分卡具体分箱得分
     *
     * @param creditReport
     */
    private void saveSubScore(CreditUser creditUser, CreditReport creditReport, Integer userId, Integer assetBorrowId) {
        //1、确保一个订单只有一组riskModelScore  2、确保重复数据补充不会使一笔人工订单包含评分卡信息
        riskModelScoreDao.deleteByBorrowOrderId(assetBorrowId);

        Reason model = getSupplierReson(creditReport, "MODEL");
        if (model != null) {
            RiskModel riskModel = AutoRiskUntil.getArtificialRiskModel();
            if (model.getItems().containsKey("riskModel")) {
                String riskModelString = model.getItems().get("riskModel");
                try {
                    riskModel = new Gson().fromJson(riskModelString, RiskModel.class);
                } catch (JsonSyntaxException e) {
//                    BussinessLogUtil.error("saveSubScore JsonSyntaxException borrowId = " + assetBorrowId, "autoReview", e);
//                    logger.error("saveSubScore JsonSyntaxException borrowId = " + assetBorrowId, e);
                    throw new RuntimeException("saveSubScore JsonSyntaxException borrowId = " + assetBorrowId, e);
                }
            }

            if (model.getItems().containsKey("subScoreMap")) {
                Map<String, Integer> subScoreMap = new Gson().fromJson(model.getItems().get("subScoreMap"),
                        new TypeToken<Map<String, Integer>>() {
                        }.getType());
                List<String> subScoreKeyList = new ArrayList<>();
                subScoreKeyList.addAll(subScoreMap.keySet());
                Map<String, Double> variableValueMap = new Gson().fromJson(model.getItems().get("variableValueMap"),
                        new TypeToken<HashMap<String, Double>>() {
                        }.getType());
                for (String key : subScoreKeyList) {
                    RiskModelScore riskModelScore = new RiskModelScore();
                    riskModelScore.setBorrowOrderId(assetBorrowId);
                    riskModelScore.setUserId(userId);
                    riskModelScore.setModelCode(riskModel.getModelCode());
                    riskModelScore.setVariableVersion(riskModel.getVariableVersion());
                    riskModelScore.setBinningVersion(riskModel.getBinningVersion());
                    riskModelScore.setCutoffVersion(riskModel.getCutoffVersion());
                    riskModelScore.setCanIgnoreVersion(riskModel.getCanIgnoreVersion());
                    riskModelScore.setVariableName(key);
                    riskModelScore.setVariableValue(variableValueMap.get(key));
                    riskModelScore.setVariableScore(subScoreMap.get(key));
                    riskModelScoreDao.insert(riskModelScore);
                }
            }
        }
    }

    /**
     * * 执行建议
     *
     * @param assetBorrowId     订单号
     * @param userId            用户id
     * @param customerType      新老用户标识
     * @param oldCustomerAdvice 老用户意见
     * @param inflexibleAdvice  硬指标建议
     * @param modelAdvice       模型建议
     * @param summaryAdvice     汇总建议
     * @param riskModel         模型参数
     * @param modelScore        模型得分
     */
    public void adviceExecute(Integer assetBorrowId, Integer userId, Integer customerType, Advice oldCustomerAdvice,
                              Advice inflexibleAdvice, Advice modelAdvice, Advice summaryAdvice, Advice executeAdvice, RiskModel riskModel,
                              Integer modelScore, Boolean loanSwitchStatusIsOn) {
        logger.info("adviceExecute start assetBorrowId=" + assetBorrowId);
        BorrowOrder borrowOrderAutoRisk = new BorrowOrder();
        borrowOrderAutoRisk.setId(assetBorrowId);

        Date nowDate = new Date();

        //初审备注信息
        borrowOrderAutoRisk.setVerifyTrialUser("自动化信审，初审完成");
        borrowOrderAutoRisk.setVerifyTrialTime(nowDate);
        borrowOrderAutoRisk.setUpdatedAt(nowDate);

        //默认，初审通过/待复审
        Integer loanStatus = BorrowOrder.STATUS_CSTG;

        if (executeAdvice == Advice.REJECT) {

            if (inflexibleAdvice == Advice.REJECT||oldCustomerAdvice == Advice.REJECT) {
                //-3:初审驳回
                loanStatus = BorrowOrder.STATUS_CSBH;
                //审核失败恢复可借额度
                changeLimitMoney(assetBorrowId);
            } else {
                //-4:复审驳回
                loanStatus = BorrowOrder.STATUS_FSBH;
                //审核失败恢复可借额度
                changeLimitMoney(assetBorrowId);
                //复审备注信息
                borrowOrderAutoRisk.setVerifyReviewUser("自动化信审，复审驳回");
                borrowOrderAutoRisk.setVerifyReviewTime(nowDate);
            }
        } else if (executeAdvice == Advice.REVIEW) {
            //1:初审通过建议人工复审
            loanStatus = BorrowOrder.STATUS_CSTG;
        } else if (executeAdvice == Advice.PASS) {
            //22:放款中
            loanStatus = BorrowOrder.STATUS_FKZ;
            //复审备注信息
            borrowOrderAutoRisk.setVerifyReviewUser("自动化信审，机审放款");
            borrowOrderAutoRisk.setVerifyReviewTime(nowDate);
        }

        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(String.valueOf(userId));
        logModel.setBorrowId(String.valueOf(assetBorrowId));
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setBeforeStatus("0");
        logModel.setAfterStatus(String.valueOf(loanStatus));
        orderLogComponent.addNewOrderLog(logModel);

        //保存评分卡数据
        saveRiskModelOrder(assetBorrowId, userId, customerType, oldCustomerAdvice, inflexibleAdvice, modelAdvice,
                summaryAdvice, executeAdvice, riskModel, modelScore, loanSwitchStatusIsOn);

        //订单最终状态
        borrowOrderAutoRisk.setStatus(loanStatus);

        logger.info("adviceExecute end assetBorrowId=" + assetBorrowId + " status=" + loanStatus);

        //乐观锁，只允许修改订单状态原先为0的借款订单
        int flag = borrowOrderDao.updateByPrimaryKeySelectiveAndStatus(borrowOrderAutoRisk);
        if (flag == 1) {
            logger.info("adviceExecute success borrowId = " + assetBorrowId);
        } else {
            logger.error("adviceExecute failure borrowId = " + assetBorrowId);
            throw new RuntimeException("adviceExecute failure borrowId = " + assetBorrowId);
        }
    }

    /**
     * 存储订单风控基本信息
     *
     * @param assetBorrowId        订单号
     * @param userId               用户id
     * @param customerType         新老用户标识
     * @param oldCustomerAdvice    老用户意见
     * @param inflexibleAdvice     硬指标意见
     * @param modelAdvice          模型意见
     * @param executeAdvice        执行意见
     * @param summaryAdvice        汇总意见
     * @param riskModel            模型信息
     * @param modelScore           模型得分
     * @param loanSwitchStatusIsOn 总放款开关
     */
    private void saveRiskModelOrder(Integer assetBorrowId, Integer userId, Integer customerType,
                                    Advice oldCustomerAdvice, Advice inflexibleAdvice,
                                    Advice modelAdvice, Advice summaryAdvice, Advice executeAdvice, RiskModel riskModel, Integer modelScore,
                                    Boolean loanSwitchStatusIsOn) {
        RiskModelOrder riskModelOrder = new RiskModelOrder();
        riskModelOrder.setBorrowOrderId(assetBorrowId);
        riskModelOrder.setUserId(userId);
        riskModelOrder.setCustomerType(customerType);
        riskModelOrder.setModelCode(riskModel.getModelCode());
        riskModelOrder.setVariableVersion(riskModel.getVariableVersion());
        riskModelOrder.setBinningVersion(riskModel.getBinningVersion());
        riskModelOrder.setCutoffVersion(riskModel.getCutoffVersion());
        riskModelOrder.setCanIgnoreVersion(riskModel.getCanIgnoreVersion());
        riskModelOrder.setCutoff(riskModel.getCutoff());
        riskModelOrder.setReviewUp(riskModel.getReviewUp());
        riskModelOrder.setReviewDown(riskModel.getReviewDown());
        riskModelOrder.setOldCustomerAdvice(oldCustomerAdvice == null ? RiskModelOrder.ADVICE_NULL : RiskModelOrder.ADVICE_TYPE.get(oldCustomerAdvice + ""));
        riskModelOrder.setInflexibleAdvice(RiskModelOrder.ADVICE_TYPE.get(inflexibleAdvice + ""));
        riskModelOrder.setModelAdvice(modelAdvice == null ? RiskModelOrder.ADVICE_NULL : RiskModelOrder.ADVICE_TYPE.get(modelAdvice + ""));
        riskModelOrder.setSummaryAdvice(RiskModelOrder.ADVICE_TYPE.get(summaryAdvice + ""));
        riskModelOrder.setExecuteAdvice(RiskModelOrder.ADVICE_TYPE.get(executeAdvice + ""));
        riskModelOrder.setModelScore(modelScore);
        riskModelOrder.setLoanSwitchStatus(RiskModelOrder.LOAN_SWITCH_TYPE.get(loanSwitchStatusIsOn));
        //确保一个订单只有一组riskModelScore
        riskModelOrderDao.deleteByBorrowOrderId(assetBorrowId);
        riskModelOrderDao.insert(riskModelOrder);
    }

    /**
     * @param creditReport
     * @param supplier     reason提供者
     * @return 根据各自的提供者 返回Reasn
     */
    private static Reason getSupplierReson(CreditReport creditReport, String supplier) {
        if (creditReport == null) {
            return null;
        }
        Set<Reason> allResonSet = creditReport.getReasons();
        Reason resultReason = null;
        for (Reason reason : allResonSet) {
            if (reason.getSupplier().equals(supplier)) {
                resultReason = reason;
                break;
            }
        }
        return resultReason;
    }

    /**
     * 构造dubbo 请求的入参
     * 风控所需的数据支持：
     * 姓名、手机号、身份证、银行卡、业务编码、设备类型、年龄、民族、通讯录、
     * 身份证有效期、是否老用户、逾期天数、是否有运营商数据
     *
     * @param userId
     * @param assetBorrowId
     * @return
     */
    private CreditUser bulidRequestAutoRiskParams(Integer userId, Integer assetBorrowId) {
        CreditUser creditUser = new CreditUser();
        //查找通讯录
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        List<UserContacts> userContactsList = userContactsDao.selectUserContacts(hashMap);
        if (!userContactsList.isEmpty()) {
            Set<String> userContactSet = new HashSet<>();
            for (UserContacts userContacts : userContactsList) {
                userContactSet.add(userContacts.getContactPhone());
            }
            creditUser.setCallSet(userContactSet);

            Map<String, String> userContactMap = new HashMap<>();
            Set<String> strings = new HashSet<>();
            for (UserContacts userContact : userContactsList) {
                if (!strings.contains(userContact.getContactPhone())) {
                    strings.add(userContact.getContactPhone());
                    userContactMap.put(userContact.getContactPhone(), userContact.getContactName());
                }
            }
            creditUser.setPhoneMap(userContactMap);//通讯录
        }

        //查找用户的信息
        User user = userDao.searchByUserid(userId);
        creditUser.setExtUserId(userId);
        creditUser.setIdentityCard(user.getIdNumber());//身份证
        creditUser.setUserPhone(user.getUserPhone());//电话
        creditUser.setUserName(user.getRealname());//姓名
        creditUser.setAge(user.getUserAge());//年龄
        creditUser.setIdcardYears(user.getRace());//身份证有效期
        //第一联系人
        creditUser.setFirstCall(user.getFirstContactPhone());
        //第二联系人
        creditUser.setSecondCall(user.getSecondContactPhone());
        if ("1".equals(user.getCustomerType())) {//是否老用户
            creditUser.setCustomerType(1);
        }
        if ("0".equals(user.getCustomerType())) {
            creditUser.setCustomerType(0);
        }

        //邀请者id
        if(StringUtils.isBlank(user.getInviteUserid()) || "0".equals(user.getInviteUserid())){
            creditUser.setInviteUserid(CreditUser.NATURAL_USER_MARK);
        }else{
            creditUser.setInviteUserid(user.getInviteUserid());
        }

        //推广渠道id
        if(StringUtils.isBlank(user.getUserFrom()) || "0".equals(user.getUserFrom())){
            creditUser.setUserFrom(CreditUser.NATURAL_USER_MARK);
        }else{
            creditUser.setUserFrom(user.getUserFrom());
        }

        if("1".equals(String.valueOf(user.getClientType()))){
            creditUser.setUserInfoEquipment(Equipment.AND.toString());
        }else if("2".equals(String.valueOf(user.getClientType()))){
            creditUser.setUserInfoEquipment(Equipment.IOS.toString());
        }else if("3".equals(String.valueOf(user.getClientType())) || "0".equals(String.valueOf(user.getClientType()))){
            creditUser.setUserInfoEquipment(Equipment.WEB.toString());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        List<Repayment> repaymentList = repaymentDao.findParams(map);
        int tmpLateDay = 0;
        repaymentList.sort((Repayment r1, Repayment r2) -> -r1.getRepaymentTime().compareTo(r2.getRepaymentTime()));
        if (repaymentList.size() > 0) {
            /**
             * 最后一次还款的逾期天数
             */
            tmpLateDay = repaymentList.get(0).getLateDay();
        }
        creditUser.setOverdueDays(tmpLateDay);//逾期天数

        //根据用户id查询银行卡信息
        UserCardInfo userCardInfo = userBankDao.findUserBankCard(userId);
        if (userCardInfo != null) {
            creditUser.setBankCardNo(userCardInfo.getCard_no());//银行卡
        }
        creditUser.setBusiness(PropertiesConfigUtil.get("RISK_BUSINESS"));//业务编码
        creditUser.setBusinessPassword(PropertiesConfigUtil.get("RISK_BUSINESS_PWD"));
        //查询订单的的来源
        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(assetBorrowId);
        creditUser.setRace(user.getUserRace());//民族
        if (borrowOrder != null) {
            String equipMentName = Equipment.AND.toString();
            if (borrowOrder.getClientType().equals("1")) {
                equipMentName = Equipment.AND.toString();
            } else if (borrowOrder.getClientType().equals("2")) {
                equipMentName = Equipment.IOS.toString();
            } else if (borrowOrder.getClientType().equals("3")) {
                equipMentName = Equipment.WEB.toString();
            }
            creditUser.setExtBorrowOrderId(assetBorrowId);
            creditUser.setEquipment(equipMentName);//设备类型
        }
        return creditUser;
    }

    /**
     * 审核不通过，恢复用户额度
     *
     * @param assetBorrowId
     */
    private void changeLimitMoney(Integer assetBorrowId) {

        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(assetBorrowId);

        User user = userDao.searchByUserid(borrowOrder.getUserId());
        User newUser = new User();
        newUser.setId(user.getId());
        //不幂等的操作
        newUser.setAmountAvailable(String.valueOf(Integer.valueOf(user.getAmountAvailable()) + borrowOrder.getMoneyAmount()));
        newUser.setUpdateTime(new Date());
        userDao.updateAmountAvailableByUserId(newUser);
        logger.info("changeLimitMoney userId=" + user.getId() + " 审核失败，用户额度恢复成功！！");
    }
}
