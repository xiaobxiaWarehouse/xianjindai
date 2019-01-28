package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;

public class InfoNotice implements Serializable {

    private static final long serialVersionUID = 6647082263417178702L;
    private Integer id;
    private String notContent;
    private String linkUrl;
    private String notSelect;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotContent() {
        return notContent;
    }

    public void setNotContent(String notContent) {
        this.notContent = notContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getNotSelect() {
        return notSelect;
    }

    public void setNotSelect(String notSelect) {
        this.notSelect = notSelect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InfoNotice [id=" + id + ", linkUrl=" + linkUrl
                + ", notContent=" + notContent + ", notSelect=" + notSelect
                + ", status=" + status + "]";
    }

}
