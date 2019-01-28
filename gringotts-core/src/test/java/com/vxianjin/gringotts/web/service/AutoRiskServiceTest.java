package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.AbstractServiceTest;
import com.vxianjin.gringotts.web.service.impl.AutoRiskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoRiskServiceTest extends AbstractServiceTest {

    @Autowired
    private AutoRiskService autoRiskService;

    @Test
    public void dealRemoteCreditReport() {
        autoRiskService.dealRemoteCreditReport(773793);
    }

}