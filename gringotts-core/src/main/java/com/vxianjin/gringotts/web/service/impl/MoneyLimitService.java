package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.risk.dao.IRiskCreditUserDao;
import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.utils.ConstantRisk;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MoneyLimitService implements IMoneyLimitService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRiskCreditUserDao riskCreditUserDao;

    @Override
    public void dealEd(String userId) {
        logger.info(" MoneyLimitService dealEd start");
        logger.info(" MoneyLimitService dealEd userId = " + userId);
        RiskCreditUser riskCreditUser = null;
        riskCreditUser = riskCreditUserDao.findUserDetail(Integer.parseInt(userId));
        if (null != riskCreditUser) {
            logger.info("riskCreditUser = " + riskCreditUser.toString());
            updateBorrowMoney(riskCreditUser);
        } else {
            logger.info("MoneyLimitService dealEd riskCreditUser = null");
        }
        logger.info(" MoneyLimitService dealEd end");
    }

    private void updateBorrowMoney(RiskCreditUser riskCreditUser2) {
        try {
            RiskCreditUser tmp = new RiskCreditUser();
            tmp.setUserId(riskCreditUser2.getUserId());//用户ID
            tmp.setAssetId(0);//
            //查询某个用户有没有辅助计算额度的记录 表：risk_credit_user user_id=#{userId} and asset_id=#{assetId}
            Integer id = riskCreditUserDao.findOneCal(tmp);
            logger.info("在risk_credit_user中是否有用户数据是，此处的id是否为空" + id);
            if (id == null) {
                // 查询数据库没有已计算记录则插入
                riskCreditUser2.setAssetId(ConstantRisk.NO_ID);
                riskCreditUser2.setLastDays(-1);
                riskCreditUser2.setRiskStatus(5);
                riskCreditUserDao.insertCalMoney(riskCreditUser2);
                id = riskCreditUser2.getId();
            }
        } catch (Exception e) {
            logger.error("updateBorrowMoney error riskCreditUser2=" + riskCreditUser2.toString(), e);
        }
    }


}

