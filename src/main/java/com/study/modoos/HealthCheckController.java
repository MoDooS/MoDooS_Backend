package com.study.modoos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/health-check")
public class HealthCheckController {
    @GetMapping
    public Long heathCheck() {
        return System.currentTimeMillis();
    }
}
