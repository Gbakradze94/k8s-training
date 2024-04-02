package com.kube.training.resourceservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
@Slf4j
public class ResourceController {
    @GetMapping("test")
    public String getGreeting() {
        return "Hello Friend";
    }
}
