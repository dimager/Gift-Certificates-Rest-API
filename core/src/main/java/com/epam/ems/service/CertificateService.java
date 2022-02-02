package com.epam.ems.service;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This interface allows to manage certificates.
 */
@Service
public interface CertificateService {

    /**
     * Allows getting list of all {@link Certificate certificates};
     * @return List of {@link Certificate certificates};
     */
    List<Certificate> getAllCertificates();

    /**
     * Allows getting {@link Certificate} entity from DB by id.
     * @param id {@link Certificate} id
     * @return {@link Certificate}
     */
    Certificate getCertificate(long id);

    /**
     * Allows deleting {@link Certificate} by from DB id.
     * @param id {@link Certificate} id
     * @return true - if object is deleted, otherwise - false.
     */
    boolean deleteCertificate(long id);

    /**
     * Allows updating {@link Certificate} entity in DB.
     * @param certificate {@link Certificate} with new data.
     * @return updated {@link Certificate}.
     */
    Certificate updateCertificate(Certificate certificate);

    /**
     * Allows creating {@link Certificate} entity in DB.
     * @param certificate new {@link Certificate}
     * @return tag with generated {@link Certificate} id.
     */
    Certificate createCertificate(Certificate certificate);

    /**
     * Allows  getting certificate`s tags by {@link Certificate} id
     * @param id {@link Certificate} id
     * @return List of certificate`s tags
     */
    List<Tag> getCertificateTags(long id);


    /**
     * Allows getting certificates sorted, filtered by name or description and by contained tag;
     * @param sorted if true - sorting by name.
     * @param desc if true - reverse sorting order.
     * @param tagName Name of tag which should be included in certificate
     * @param pattern pattern used for filtering by name or description
     * @return processed certificates list.
     */
    List<Certificate> getFilteredSortedCertificates(boolean sorted, boolean desc, Optional<String> tagName, Optional<String> pattern);
}
