package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.CertificateDurationOnly;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping(value = "/certificates")
public class CertificatesController {
    private final CertificateService certificateService;

    @Autowired
    public CertificatesController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public CollectionModel<Certificate> getCertificates(@RequestParam(name = "sort") Optional<String> sort,
                                                        @RequestParam(name = "tags") Optional<String[]> tags,
                                                        @RequestParam(name = "filter") Optional<String> pattern,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "page", defaultValue = "1") int page) {
        CollectionModel<Certificate> certificates = certificateService
                .getFilteredSortedCertificates(sort, tags, pattern, page, size, linkTo(CertificatesController.class));
        for (Certificate certificate : certificates.getContent()) {
            this.createLinks(certificate);
        }
        return certificates;
    }

    private void createLinks(Certificate certificate) {
        certificate.add(linkTo(methodOn(CertificatesController.class).getCertificate(certificate.getId())).withSelfRel());
        for (Tag tag : certificate.getTags()) {
            if (!tag.hasLink("self")) {
                tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
            }
            if (!tag.hasLink("Certificates")) {
                String[] tagName = {tag.getName()};
                tag.add(Link.of(linkTo(CertificatesController.class)
                                .toUriComponentsBuilder().queryParam("tags", tag.getName()).build().toString())
                        .withRel("Certificates"));
            }
        }
    }

    @GetMapping("{id}")
    public Certificate getCertificate(@PathVariable long id) {
        Certificate certificate = certificateService.getCertificate(id);
        this.createLinks(certificate);
        return certificate;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable long id) {
        if (certificateService.deleteCertificate(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Certificate addCertificate(@RequestBody @Valid Certificate certificate) {
        certificate = certificateService.createCertificate(certificate);
        this.createLinks(certificate);
        return certificate;
    }

    @PutMapping("{id}")
    public Certificate updateCertificate(@PathVariable long id, @RequestBody @Valid Certificate certificate) {
        certificate.setId(id);
        certificate = certificateService.updateCertificate(certificate);
        this.createLinks(certificate);
        return certificate;
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<CertificateDurationOnly> updateCertificate(@PathVariable long id, @RequestBody @Valid CertificateDurationOnly durationOnly) {
        if (certificateService.updateDuration(id, durationOnly)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


