package com.vxianjin.gringotts.pay.model;

import java.io.Serializable;
import java.util.Date;

public class MqInfo implements Serializable {

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.mq_id
     *
     * @mbggenerated
     */
    private String mqId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.mq_topic
     *
     * @mbggenerated
     */
    private String mqTopic;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.mq_tag
     *
     * @mbggenerated
     */
    private String mqTag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.mq_context
     *
     * @mbggenerated
     */
    private String mqContext;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.mq_status
     *
     * @mbggenerated
     */
    private String mqStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.call_back_time
     *
     * @mbggenerated
     */
    private Date callBackTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mq_info.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table mq_info
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.mq_id
     *
     * @return the value of mq_info.mq_id
     *
     * @mbggenerated
     */
    public String getMqId() {
        return mqId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.mq_id
     *
     * @param mqId the value for mq_info.mq_id
     *
     * @mbggenerated
     */
    public void setMqId(String mqId) {
        this.mqId = mqId == null ? null : mqId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.mq_topic
     *
     * @return the value of mq_info.mq_topic
     *
     * @mbggenerated
     */
    public String getMqTopic() {
        return mqTopic;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.mq_topic
     *
     * @param mqTopic the value for mq_info.mq_topic
     *
     * @mbggenerated
     */
    public void setMqTopic(String mqTopic) {
        this.mqTopic = mqTopic == null ? null : mqTopic.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.mq_tag
     *
     * @return the value of mq_info.mq_tag
     *
     * @mbggenerated
     */
    public String getMqTag() {
        return mqTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.mq_tag
     *
     * @param mqTag the value for mq_info.mq_tag
     *
     * @mbggenerated
     */
    public void setMqTag(String mqTag) {
        this.mqTag = mqTag == null ? null : mqTag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.mq_context
     *
     * @return the value of mq_info.mq_context
     *
     * @mbggenerated
     */
    public String getMqContext() {
        return mqContext;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.mq_context
     *
     * @param mqContext the value for mq_info.mq_context
     *
     * @mbggenerated
     */
    public void setMqContext(String mqContext) {
        this.mqContext = mqContext == null ? null : mqContext.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.mq_status
     *
     * @return the value of mq_info.mq_status
     *
     * @mbggenerated
     */
    public String getMqStatus() {
        return mqStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.mq_status
     *
     * @param mqStatus the value for mq_info.mq_status
     *
     * @mbggenerated
     */
    public void setMqStatus(String mqStatus) {
        this.mqStatus = mqStatus == null ? null : mqStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.create_time
     *
     * @return the value of mq_info.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.create_time
     *
     * @param createTime the value for mq_info.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.call_back_time
     *
     * @return the value of mq_info.call_back_time
     *
     * @mbggenerated
     */
    public Date getCallBackTime() {
        return callBackTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.call_back_time
     *
     * @param callBackTime the value for mq_info.call_back_time
     *
     * @mbggenerated
     */
    public void setCallBackTime(Date callBackTime) {
        this.callBackTime = callBackTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mq_info.update_time
     *
     * @return the value of mq_info.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mq_info.update_time
     *
     * @param updateTime the value for mq_info.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_info
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
        MqInfo other = (MqInfo) that;
        return (this.getMqId() == null ? other.getMqId() == null : this.getMqId().equals(other.getMqId()))
            && (this.getMqTopic() == null ? other.getMqTopic() == null : this.getMqTopic().equals(other.getMqTopic()))
            && (this.getMqTag() == null ? other.getMqTag() == null : this.getMqTag().equals(other.getMqTag()))
            && (this.getMqContext() == null ? other.getMqContext() == null : this.getMqContext().equals(other.getMqContext()))
            && (this.getMqStatus() == null ? other.getMqStatus() == null : this.getMqStatus().equals(other.getMqStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCallBackTime() == null ? other.getCallBackTime() == null : this.getCallBackTime().equals(other.getCallBackTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_info
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMqId() == null) ? 0 : getMqId().hashCode());
        result = prime * result + ((getMqTopic() == null) ? 0 : getMqTopic().hashCode());
        result = prime * result + ((getMqTag() == null) ? 0 : getMqTag().hashCode());
        result = prime * result + ((getMqContext() == null) ? 0 : getMqContext().hashCode());
        result = prime * result + ((getMqStatus() == null) ? 0 : getMqStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCallBackTime() == null) ? 0 : getCallBackTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mq_info
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mqId=").append(mqId);
        sb.append(", mqTopic=").append(mqTopic);
        sb.append(", mqTag=").append(mqTag);
        sb.append(", mqContext=").append(mqContext);
        sb.append(", mqStatus=").append(mqStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", callBackTime=").append(callBackTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public MqInfo() {
    }

    public MqInfo(String mqId, String mqTopic, String mqTag, String mqContext) {
        this.mqId = mqId;
        this.mqTopic = mqTopic;
        this.mqTag = mqTag;
        this.mqContext = mqContext;
    }
}