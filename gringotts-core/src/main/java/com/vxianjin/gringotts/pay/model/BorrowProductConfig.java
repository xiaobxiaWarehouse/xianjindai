package com.vxianjin.gringotts.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BorrowProductConfig implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.borrow_amount
     *
     * @mbggenerated
     */
    private BigDecimal borrowAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.total_fee_rate
     *
     * @mbggenerated
     */
    private BigDecimal totalFeeRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.borrow_interest
     *
     * @mbggenerated
     */
    private BigDecimal borrowInterest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.turst_trial
     *
     * @mbggenerated
     */
    private BigDecimal turstTrial;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.platform_licensing
     *
     * @mbggenerated
     */
    private BigDecimal platformLicensing;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.collect_channel_fee
     *
     * @mbggenerated
     */
    private BigDecimal collectChannelFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.account_manager_fee
     *
     * @mbggenerated
     */
    private BigDecimal accountManagerFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.late_fee
     *
     * @mbggenerated
     */
    private BigDecimal lateFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.renewal_fee
     *
     * @mbggenerated
     */
    private BigDecimal renewalFee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.renewal_poundage
     *
     * @mbggenerated
     */
    private BigDecimal renewalPoundage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.borrow_day
     *
     * @mbggenerated
     */
    private Integer borrowDay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.deal_flag
     *
     * @mbggenerated
     */
    private String dealFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column borrow_product_config.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table borrow_product_config
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.id
     *
     * @return the value of borrow_product_config.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.id
     *
     * @param id the value for borrow_product_config.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.borrow_amount
     *
     * @return the value of borrow_product_config.borrow_amount
     *
     * @mbggenerated
     */
    public BigDecimal getBorrowAmount() {
        return borrowAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.borrow_amount
     *
     * @param borrowAmount the value for borrow_product_config.borrow_amount
     *
     * @mbggenerated
     */
    public void setBorrowAmount(BigDecimal borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.total_fee_rate
     *
     * @return the value of borrow_product_config.total_fee_rate
     *
     * @mbggenerated
     */
    public BigDecimal getTotalFeeRate() {
        return totalFeeRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.total_fee_rate
     *
     * @param totalFeeRate the value for borrow_product_config.total_fee_rate
     *
     * @mbggenerated
     */
    public void setTotalFeeRate(BigDecimal totalFeeRate) {
        this.totalFeeRate = totalFeeRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.borrow_interest
     *
     * @return the value of borrow_product_config.borrow_interest
     *
     * @mbggenerated
     */
    public BigDecimal getBorrowInterest() {
        return borrowInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.borrow_interest
     *
     * @param borrowInterest the value for borrow_product_config.borrow_interest
     *
     * @mbggenerated
     */
    public void setBorrowInterest(BigDecimal borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.turst_trial
     *
     * @return the value of borrow_product_config.turst_trial
     *
     * @mbggenerated
     */
    public BigDecimal getTurstTrial() {
        return turstTrial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.turst_trial
     *
     * @param turstTrial the value for borrow_product_config.turst_trial
     *
     * @mbggenerated
     */
    public void setTurstTrial(BigDecimal turstTrial) {
        this.turstTrial = turstTrial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.platform_licensing
     *
     * @return the value of borrow_product_config.platform_licensing
     *
     * @mbggenerated
     */
    public BigDecimal getPlatformLicensing() {
        return platformLicensing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.platform_licensing
     *
     * @param platformLicensing the value for borrow_product_config.platform_licensing
     *
     * @mbggenerated
     */
    public void setPlatformLicensing(BigDecimal platformLicensing) {
        this.platformLicensing = platformLicensing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.collect_channel_fee
     *
     * @return the value of borrow_product_config.collect_channel_fee
     *
     * @mbggenerated
     */
    public BigDecimal getCollectChannelFee() {
        return collectChannelFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.collect_channel_fee
     *
     * @param collectChannelFee the value for borrow_product_config.collect_channel_fee
     *
     * @mbggenerated
     */
    public void setCollectChannelFee(BigDecimal collectChannelFee) {
        this.collectChannelFee = collectChannelFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.account_manager_fee
     *
     * @return the value of borrow_product_config.account_manager_fee
     *
     * @mbggenerated
     */
    public BigDecimal getAccountManagerFee() {
        return accountManagerFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.account_manager_fee
     *
     * @param accountManagerFee the value for borrow_product_config.account_manager_fee
     *
     * @mbggenerated
     */
    public void setAccountManagerFee(BigDecimal accountManagerFee) {
        this.accountManagerFee = accountManagerFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.late_fee
     *
     * @return the value of borrow_product_config.late_fee
     *
     * @mbggenerated
     */
    public BigDecimal getLateFee() {
        return lateFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.late_fee
     *
     * @param lateFee the value for borrow_product_config.late_fee
     *
     * @mbggenerated
     */
    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.renewal_fee
     *
     * @return the value of borrow_product_config.renewal_fee
     *
     * @mbggenerated
     */
    public BigDecimal getRenewalFee() {
        return renewalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.renewal_fee
     *
     * @param renewalFee the value for borrow_product_config.renewal_fee
     *
     * @mbggenerated
     */
    public void setRenewalFee(BigDecimal renewalFee) {
        this.renewalFee = renewalFee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.renewal_poundage
     *
     * @return the value of borrow_product_config.renewal_poundage
     *
     * @mbggenerated
     */
    public BigDecimal getRenewalPoundage() {
        return renewalPoundage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.renewal_poundage
     *
     * @param renewalPoundage the value for borrow_product_config.renewal_poundage
     *
     * @mbggenerated
     */
    public void setRenewalPoundage(BigDecimal renewalPoundage) {
        this.renewalPoundage = renewalPoundage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.borrow_day
     *
     * @return the value of borrow_product_config.borrow_day
     *
     * @mbggenerated
     */
    public Integer getBorrowDay() {
        return borrowDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.borrow_day
     *
     * @param borrowDay the value for borrow_product_config.borrow_day
     *
     * @mbggenerated
     */
    public void setBorrowDay(Integer borrowDay) {
        this.borrowDay = borrowDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.deal_flag
     *
     * @return the value of borrow_product_config.deal_flag
     *
     * @mbggenerated
     */
    public String getDealFlag() {
        return dealFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.deal_flag
     *
     * @param dealFlag the value for borrow_product_config.deal_flag
     *
     * @mbggenerated
     */
    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag == null ? null : dealFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.create_time
     *
     * @return the value of borrow_product_config.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.create_time
     *
     * @param createTime the value for borrow_product_config.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.update_time
     *
     * @return the value of borrow_product_config.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.update_time
     *
     * @param updateTime the value for borrow_product_config.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column borrow_product_config.remark
     *
     * @return the value of borrow_product_config.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column borrow_product_config.remark
     *
     * @param remark the value for borrow_product_config.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table borrow_product_config
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BorrowProductConfig other = (BorrowProductConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBorrowAmount() == null ? other.getBorrowAmount() == null : this.getBorrowAmount().equals(other.getBorrowAmount()))
            && (this.getTotalFeeRate() == null ? other.getTotalFeeRate() == null : this.getTotalFeeRate().equals(other.getTotalFeeRate()))
            && (this.getBorrowInterest() == null ? other.getBorrowInterest() == null : this.getBorrowInterest().equals(other.getBorrowInterest()))
            && (this.getTurstTrial() == null ? other.getTurstTrial() == null : this.getTurstTrial().equals(other.getTurstTrial()))
            && (this.getPlatformLicensing() == null ? other.getPlatformLicensing() == null : this.getPlatformLicensing().equals(other.getPlatformLicensing()))
            && (this.getCollectChannelFee() == null ? other.getCollectChannelFee() == null : this.getCollectChannelFee().equals(other.getCollectChannelFee()))
            && (this.getAccountManagerFee() == null ? other.getAccountManagerFee() == null : this.getAccountManagerFee().equals(other.getAccountManagerFee()))
            && (this.getLateFee() == null ? other.getLateFee() == null : this.getLateFee().equals(other.getLateFee()))
            && (this.getRenewalFee() == null ? other.getRenewalFee() == null : this.getRenewalFee().equals(other.getRenewalFee()))
            && (this.getRenewalPoundage() == null ? other.getRenewalPoundage() == null : this.getRenewalPoundage().equals(other.getRenewalPoundage()))
            && (this.getBorrowDay() == null ? other.getBorrowDay() == null : this.getBorrowDay().equals(other.getBorrowDay()))
            && (this.getDealFlag() == null ? other.getDealFlag() == null : this.getDealFlag().equals(other.getDealFlag()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table borrow_product_config
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBorrowAmount() == null) ? 0 : getBorrowAmount().hashCode());
        result = prime * result + ((getTotalFeeRate() == null) ? 0 : getTotalFeeRate().hashCode());
        result = prime * result + ((getBorrowInterest() == null) ? 0 : getBorrowInterest().hashCode());
        result = prime * result + ((getTurstTrial() == null) ? 0 : getTurstTrial().hashCode());
        result = prime * result + ((getPlatformLicensing() == null) ? 0 : getPlatformLicensing().hashCode());
        result = prime * result + ((getCollectChannelFee() == null) ? 0 : getCollectChannelFee().hashCode());
        result = prime * result + ((getAccountManagerFee() == null) ? 0 : getAccountManagerFee().hashCode());
        result = prime * result + ((getLateFee() == null) ? 0 : getLateFee().hashCode());
        result = prime * result + ((getRenewalFee() == null) ? 0 : getRenewalFee().hashCode());
        result = prime * result + ((getRenewalPoundage() == null) ? 0 : getRenewalPoundage().hashCode());
        result = prime * result + ((getBorrowDay() == null) ? 0 : getBorrowDay().hashCode());
        result = prime * result + ((getDealFlag() == null) ? 0 : getDealFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table borrow_product_config
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", borrowAmount=").append(borrowAmount);
        sb.append(", totalFeeRate=").append(totalFeeRate);
        sb.append(", borrowInterest=").append(borrowInterest);
        sb.append(", turstTrial=").append(turstTrial);
        sb.append(", platformLicensing=").append(platformLicensing);
        sb.append(", collectChannelFee=").append(collectChannelFee);
        sb.append(", accountManagerFee=").append(accountManagerFee);
        sb.append(", lateFee=").append(lateFee);
        sb.append(", renewalFee=").append(renewalFee);
        sb.append(", renewalPoundage=").append(renewalPoundage);
        sb.append(", borrowDay=").append(borrowDay);
        sb.append(", dealFlag=").append(dealFlag);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public BorrowProductConfig() {
    }
}