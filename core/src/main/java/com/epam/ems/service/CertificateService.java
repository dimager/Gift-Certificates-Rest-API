package com.epam.ems.service;

import com.epam.ems.entity.Certificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This interface allows managing certificates.
 */
@Service
public interface CertificateService {

    /**
     * Allows getting list of all {@link Certificate certificates}, could be sorted, filtered by name, description, tag name;
     *
     * @param sort    type of sorting.
     * @param tags    Names of tags which should be included in certificate
     * @param pattern pattern used for filtering by name or description
     * @param link link to controller
     * @param page number of page
     * @param size page size
     * @return List of {@link Certificate certificates};
     */
    CollectionModel<Certificate> getFilteredSortedCertificates(Optional<String> sort,
                                                               Optional<String[]> tags,
                                                               Optional<String> pattern,
                                                               int page,
                                                               int size,
                                                               WebMvcLinkBuilder link);

    /**
     * Allows getting {@link Certificate} entity from DB by id.
     *
     * @param id {@link Certificate} id
     * @return {@link Certificate}
     */
    Certificate getCertificate(long id);

    /**
     * Allows deleting {@link Certificate} by from DB id.
     *
     * @param id {@link Certificate} id
     * @return true - if object is deleted, otherwise - false.
     */
    boolean deleteCertificate(long id);

    /**
     * Allows updating {@link Certificate} entity in DB.
     *
     * @param certificate {@link Certificate} with new data.
     * @return updated {@link Certificate}.
     */
    Certificate updateCertificate(Certificate certificate);

    /**
     * Allows creating {@link Certificate} entity in DB.
     *
     * @param certificate new {@link Certificate}
     * @return tag with generated {@link Certificate} id.
     */
    Certificate createCertificate(Certificate certificate);


    /**
     * Allows update certificate duration
     *
     * @param id certificate id
     * @param durationOnly certificate data
     * @return true if certificate was updated
     */
    boolean updateDuration(long id, Certificate durationOnly);

    boolean couldBeImageDeleted(String imageHash);

    boolean isCertificateExistById(long id);
}
