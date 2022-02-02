package com.epam.ems.dao.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.CertificateTagRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.CertificateTag;
import com.epam.ems.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CertificateDaoImpl extends NamedParameterJdbcTemplate implements CertificateDao {

    private static final int ONE_UPDATED_ROW = 1;
    private static final int NO_DB_ENTRY = 0;
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id=:id";
    private static final String SQL_SELECT_ALL = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SQL_INSERT = "INSERT INTO gift_certificate (name,description,price,duration,create_date,last_update_date) VALUES (:name, :description, :price, :duration, :createDateTime, :lastUpdateDateTime)";
    private static final String SQL_UPDATE = "UPDATE gift_certificate SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :lastUpdateDate WHERE id = :id;";
    private static final String SQL_DELETE = "DELETE FROM gift_certificate WHERE id = :id";
    private static final String SQL_ADD_TAG_TO_CERTIFICATE = "INSERT INTO gift_certificate_has_tag (gift_certificate_id,tag_id) VALUES (:certificateId, :tagId)";
    private static final String SQL_DELETE_TAG_FROM_CERTIFICATE = "DELETE FROM gift_certificate_has_tag WHERE gift_certificate_id = :certificateId AND tag_id = :tagId";
    private static final String SQL_CERTIFICATE_HAS_TAG = "SELECT count(*) FROM gift_certificate_has_tag WHERE gift_certificate_id = :certificateId AND tag_id = :tagId";
    private static final String SQL_SELECT_TAGS_FOR_CERTIFICATE_BY_ID = "SELECT id, name FROM tag LEFT JOIN gift_certificate_has_tag as gsht on tag.id = gsht.tag_id WHERE gsht.gift_certificate_id = :id";
    private static final String SQL_SELECT_IS_CERTIFICATE_EXIST = "SELECT COUNT(*) FROM gift_certificate WHERE id = :id";
    private static final String SQL_SELECT_ALL_CERTIFICATES_ID_WITH_TAGS = "select gchs.gift_certificate_id, t.id, t.name " +
            "from gift_certificate_has_tag as gchs left join tag as t on gchs.tag_id = t.id order by gchs.gift_certificate_id";

    @Autowired
    public CertificateDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Certificate> getAll() {
        List<Certificate> certificates = this.query(SQL_SELECT_ALL, new CertificateRowMapper());
        List<CertificateTag> certificateTagList = this.query(SQL_SELECT_ALL_CERTIFICATES_ID_WITH_TAGS, new CertificateTagRowMapper());
        for (Certificate certificate : certificates) {
            certificate.getTags()
                    .addAll(certificateTagList.stream()
                            .filter(certificateTag -> certificateTag.getCertId()==certificate.getId())
                            .map(CertificateTag::getTag)
                            .collect(Collectors.toList()));
        }
        return certificates;
    }

    @Override
    public Certificate getById(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        Certificate certificate = this.queryForObject(SQL_SELECT_BY_ID, sqlParameterSource, new CertificateRowMapper());
        certificate.getTags().addAll(this.getCertificateTags(id));
        return certificate;
    }

    @Override
    public Certificate update(Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", certificate.getId());
        sqlParameterSource.addValue("name", certificate.getName());
        sqlParameterSource.addValue("description", certificate.getDescription());
        sqlParameterSource.addValue("price", certificate.getPrice());
        sqlParameterSource.addValue("duration", certificate.getDuration());
        sqlParameterSource.addValue("lastUpdateDate", certificate.getLastUpdatedDateTime());
        this.update(SQL_UPDATE, sqlParameterSource);
        return getById(certificate.getId());
    }


    @Override
    public Certificate create(Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", certificate.getName());
        sqlParameterSource.addValue("description", certificate.getDescription());
        sqlParameterSource.addValue("price", certificate.getPrice());
        sqlParameterSource.addValue("duration", certificate.getDuration());
        sqlParameterSource.addValue("createDateTime", certificate.getCreatedDateTime());
        sqlParameterSource.addValue("lastUpdateDateTime", certificate.getLastUpdatedDateTime());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.update(SQL_INSERT, sqlParameterSource, keyHolder);
        certificate.setId(keyHolder.getKey().longValue());
        return certificate;
    }

    @Override
    public boolean delete(long id) {
        if (this.isCertificateExistById(id)) {
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", id);
            return this.update(SQL_DELETE, sqlParameterSource) == ONE_UPDATED_ROW;
        } else {
            return false;
        }
    }

    @Override
    public List<Tag> getCertificateTags(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        List<Tag> tags = this.query(SQL_SELECT_TAGS_FOR_CERTIFICATE_BY_ID, sqlParameterSource, new TagRowMapper());
        return tags;
    }

    @Override
    public boolean addTagToCertificate(Tag tag, Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", certificate.getId());
        sqlParameterSource.addValue("tagId", tag.getId());
        return this.update(SQL_ADD_TAG_TO_CERTIFICATE, sqlParameterSource) == ONE_UPDATED_ROW;
    }

    @Override
    public boolean removeTagFromCertificate(Tag tag, Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", certificate.getId());
        sqlParameterSource.addValue("tagId", tag.getId());
        return this.update(SQL_DELETE_TAG_FROM_CERTIFICATE, sqlParameterSource) == ONE_UPDATED_ROW;
    }

    @Override
    public boolean isCertificateMissingTag(Tag tag, Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", certificate.getId());
        sqlParameterSource.addValue("tagId", tag.getId());
        return this.queryForObject(SQL_CERTIFICATE_HAS_TAG, sqlParameterSource, Integer.class) == NO_DB_ENTRY;
    }

    private boolean isCertificateExistById(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        return this.queryForObject(SQL_SELECT_IS_CERTIFICATE_EXIST, sqlParameterSource, Integer.class) == ONE_UPDATED_ROW;
    }
}
