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

    List<Certificate> getCertificatesContainsTags(int size, int offset, Set<Tag> tags);

    Integer getNumberOCertificatesContainsTags(int size, int offset, Set<Tag> tags);

    List<Certificate> getCertificates(int size, int offset, Optional<String> sort,
                                      Optional<String> filterPattern);

    Integer getCertificatesAmount(int size, int offset, Optional<String> sort,
                                  Optional<String> filterPattern);
}
