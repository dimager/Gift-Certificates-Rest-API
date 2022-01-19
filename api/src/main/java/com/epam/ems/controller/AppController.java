package com.epam.ems.controller;

import com.epam.ems.dao.entity.Certificate;
import com.epam.ems.dao.entity.Tag;
import com.epam.ems.service.CertificateService;
import com.epam.ems.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@ComponentScan("com.epam.ems")
public class AppController {
    private  final TagService tagService;
    private  CertificateService certificateService;

    @Autowired
    public AppController(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/certificates")
    public List<Certificate> getAllCertificates() {
        return certificateService.getAllCertificates();
    }


}
