package com.epam.ems.dao;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface CertificateDao extends BaseDao<Certificate> {
    /**
     * Allows checking {@link Certificate} existence.
     *
     * @param id {@link Certificate} id.
     * @return true - if certificate is found, otherwise false.
     */
    boolean isCertificateExistById(long id);

    /**
     * Allows getting list of certificates with tags
     *
     * @param size   page size
     * @param offset offset
     * @param tags   tags for filter
     * @return list of certificates
     */
    List<Certificate> getCertificatesContainsTags(int size, int offset, Set<Tag> tags);


    /**
     * Allows getting number of certificates with tags
     *
     * @param tags list of tags
     * @return number of certificates
     */
    Integer getNumberOCertificatesContainsTags(Set<Tag> tags);

    Boolean couldImageBeDeleted(String imageHash);

    /**
     * Allows getting list of certificates with sort and filter by name or description
     *
     * @param size          page size
     * @param offset        offset
     * @param sort          sort type
     * @param filterPattern filter pattern
     * @return list of certificates
     */
    List<Certificate> getCertificates(int size, int offset, Optional<String> sort,
                                      Optional<String> filterPattern);

    /**
     * Allows getting number of certificates with sort and filter by name or description
     *
     * @param sort sort type
     * @param filterPattern filter pattern
     * @return number of certificates
     */
    Integer getCertificatesAmount(Optional<String> sort,
                                  Optional<String> filterPattern);
}
