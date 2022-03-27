package com.epam.ems.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/aws")
public class AwsController {
    @GetMapping
    public Map<String, String> getFiles() {
        return System.getenv();
    }
}
