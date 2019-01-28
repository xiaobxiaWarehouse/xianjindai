package com.vxianjin.gringotts.web.util.qiniu;

import com.qiniu.common.Zone;

/**
 * 七牛机房枚举
 *
 * @author wangyudong
 */
public enum QnZone {
    //0-华东，1-华北，2-华南，na0-北美
    ZONE0(Zone.zone0()), ZONE1(Zone.zone1()), ZONE2(Zone.zone2()), ZONENA0(Zone.zoneNa0());

    private Zone zone;

    QnZone(Zone zone) {
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }


}
