package com.epam.ems.dao;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate>{
    List<Tag> getCertificatesTags(long id);
    boolean addTagToCertificate(Tag tag, Certificate certificate);
    boolean removeTagFromCertificate(Tag tag, Certificate certificate);
    boolean isCertificateMissingTag(Tag tag, Certificate certificate);

    void deleteCertificateRelations(long id);

    List<Certificate> getByPartNameOrDescription(String pattern);
}
