package com.mungdori.fallserver.adapter.webapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Container readiness check; not exposed by the frontend proxy. */
@RestController
public class DeploymentHealthApi {
    @GetMapping("/deployment-health")
    public String health() {
        return "ok";
    }
}
