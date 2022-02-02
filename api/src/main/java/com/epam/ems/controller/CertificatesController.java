package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
import com.epam.ems.handler.ValidationHandler;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(value = "/certificates")
public class CertificatesController {
    private final CertificateService certificateService;
    private final ValidationHandler validationHandler;

    @Autowired
    public CertificatesController(CertificateService certificateService, ValidationHandler validationHandler) {
        this.certificateService = certificateService;
        this.validationHandler = validationHandler;
    }

    @GetMapping
    public List<Certificate> getCertificates(@RequestParam(name = "sort", defaultValue = "false") boolean sorted,
                                             @RequestParam(name = "desc", defaultValue = "false") boolean desc,
                                             @RequestParam(name = "tag") Optional<String> tagName,
                                             @RequestParam(name = "filter") Optional<String> pattern,
                                             HttpServletResponse response) {
        List<Certificate> certificates = certificateService.getFilteredSortedCertificates(sorted,desc,tagName,pattern);
        if (certificates.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return certificateService.getFilteredSortedCertificates(sorted,desc,tagName,pattern);

    }


    @GetMapping("{id}")
    public Certificate getCertificate(@PathVariable long id) {
        return certificateService.getCertificate(id);
    }

    @DeleteMapping("{id}")
    public void deleteCertificate(@PathVariable long id) {
        certificateService.deleteCertificate(id);
    }

    @PostMapping
    public Certificate addCertificate(@RequestBody @Valid Certificate certificate, BindingResult bindingResult) {
        validationHandler.handleBindingResult(bindingResult);
        return certificateService.createCertificate(certificate);
    }

    @PutMapping("{id}")
    public Certificate updateCertificate(@PathVariable long id, @RequestBody @Valid Certificate certificate, BindingResult bindingResult) {
        validationHandler.handleBindingResult(bindingResult);
        certificate.setId(id);
        return certificateService.updateCertificate(certificate);
    }

}
