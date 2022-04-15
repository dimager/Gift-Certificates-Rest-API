package com.epam.ems.controller;

import com.epam.ems.aws.S3Service;
import com.epam.ems.dto.CertificateDTO;
import com.epam.ems.dto.converter.DtoConverter;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping(value = "/certificates")
public class CertificatesController {
    private final CertificateService certificateService;
    private final S3Service s3service;
    private final DtoConverter dtoConverter;

    @Autowired
    public CertificatesController(CertificateService certificateService, S3Service s3service, DtoConverter dtoConverter) {
        this.certificateService = certificateService;
        this.s3service = s3service;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Allows getting list of certificates with extra parameters.
     *
     * @param sort    - type og sort (name, name_desc, date, date_desc, name_date, name_date_desc, name_desc_date, name_desc_date_desc)
     * @param tags    - get certificates with tags
     * @param pattern - filter certificates by pattern in name or description
     * @param size    - page size
     * @param page    - number of page
     * @return Certificates with links and page navigation.
     */
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


    /**
     * Allows getting certificate  by id.
     *
     * @param id - certificate id
     * @return certificate
     */
    @GetMapping("{id}")
    public Certificate getCertificate(@PathVariable long id) {
        Certificate certificate = certificateService.getCertificate(id);
        this.createLinks(certificate);
        return certificate;
    }

    @GetMapping("{id}/image")
    public ResponseEntity<String> getImage(@PathVariable long id) {
        return ResponseEntity.ok(s3service.getImageBase64(id));
    }

    @PreAuthorize("hasAuthority('image:write')")
    @PutMapping("{id}/image")
    public ResponseEntity setImage(@PathVariable long id, @RequestPart MultipartFile multipartFile) {
        s3service.uploadImage(id, multipartFile);
        return ResponseEntity.ok().build();
    }

    /**
     * Allows deleting certificate by id.
     *
     * @param id certificate id
     * @return true or false
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('certificate:write')")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable long id) {
        if (certificateService.deleteCertificate(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Allows adding certificate.
     *
     * @param certificateDTO certificate data
     * @return created certificate with id
     */
    @PostMapping
    @PreAuthorize("hasAuthority('certificate:write')")
    public Certificate addCertificate(@RequestBody @Valid CertificateDTO certificateDTO) {
        Certificate certificate = certificateService.createCertificate(dtoConverter.convertToEntity(certificateDTO));
        this.createLinks(certificate);
        return certificate;
    }

    /**
     * Allows updating certificate
     *
     * @param id             certificateid
     * @param certificateDTO certificate parameters
     * @return updated certificate
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('certificate:write')")
    public Certificate updateCertificate(@PathVariable long id, @RequestBody @Valid CertificateDTO certificateDTO) {
        Certificate certificate = dtoConverter.convertToEntity(certificateDTO);
        certificate.setId(id);
        certificate = certificateService.updateCertificate(certificate);
        this.createLinks(certificate);
        return certificate;
    }

    /**
     * Allows updating certificate`s duration
     *
     * @param id             certificateid
     * @param certificateDTO certificate parameters
     * @return updated certificate
     */
    @PatchMapping(path = "{id}")
    @PreAuthorize("hasAuthority('certificate:write')")
    public Certificate updateCertificateDuration(@PathVariable long id, @RequestBody CertificateDTO certificateDTO) {
        certificateService.updateDuration(id, dtoConverter.convertToEntity(certificateDTO));
        Certificate certificate = certificateService.getCertificate(id);
        this.createLinks(certificate);
        return certificate;
    }


    private void createLinks(Certificate certificate) {
        certificate.add(linkTo(methodOn(CertificatesController.class).getCertificate(certificate.getId())).withSelfRel());
        certificate.add(linkTo(methodOn(CertificatesController.class).getImage(certificate.getId())).withRel("image"));
        for (Tag tag : certificate.getTags()) {
            if (!tag.hasLink("self")) {
                tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
            }
            if (!tag.hasLink("Certificates")) {
                tag.add(Link.of(linkTo(CertificatesController.class)
                                .toUriComponentsBuilder().queryParam("tags", tag.getName()).build().toString())
                        .withRel("Certificates"));
            }
        }
    }


}


