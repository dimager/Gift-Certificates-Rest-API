package com.epam.ems.dao;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate>{
    /**
     * Allows getting certificate`s tags.
     * @param id {@link Certificate} id
     * @return List of certificate`s tags.
     */
    List<Tag> getCertificateTags(long id);

    /**
     * Allows adding {@link Tag} to {@link Certificate}
     * @param tag {@link Tag} which should be added.
     * @param certificate  {@link Certificate} to which this tag should be added.
     * @return true - if tag successfully added
     */
    boolean addTagToCertificate(Tag tag, Certificate certificate);

    /**
     * Allows removing {@link Tag} from {@link Certificate}
     * @param tag {@link Tag} which should be removed.
     * @param certificate  {@link Certificate} from which this tag should be removed.
     * @return true - if tag successfully removed
     */
    boolean removeTagFromCertificate(Tag tag, Certificate certificate);

    /**
     * Allows checking the relation of the {@link Tag} to the {@link Certificate}
     * @param tag {@link Tag}
     * @param certificate {@link Certificate}
     * @return true - if {@link Certificate} include {@link Tag}, otherwise false.
     */
    boolean isCertificateMissingTag(Tag tag, Certificate certificate);

}
