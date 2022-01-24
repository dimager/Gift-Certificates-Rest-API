package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CertificatesController {
    private final CertificateService certificateService;

    @Autowired
    public CertificatesController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/certificates")
    public List<Certificate> getAllCertificates() {
        return certificateService.getAllCertificates();
    }

    @GetMapping("/certificates/{id}")
    public Certificate getCertificate(@PathVariable long id) {
        return certificateService.getCertificate(id);
    }

    @DeleteMapping("/certificates/{id}")
    public void deleteCertificate(@PathVariable long id) {
        certificateService.deleteCertificate(id);
    }

    @PostMapping("/certificates")
    public Certificate addCertificate(@RequestBody Certificate certificate) {
        return certificateService.createCertificate(certificate);
    }

    @PutMapping("/certificates/{id}")
    public Certificate updateCertificate(@PathVariable long id, @RequestBody Certificate certificate) {
        certificate.setId(id);
        return certificateService.updateCertificate(certificate);
    }
}
