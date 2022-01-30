package com.epam.ems.dao.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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
    private static final String SQL_DELETE_CERTIFICATE_RELATIONS = "DELETE FROM gift_certificate_has_tag WHERE gift_certificate_id = :certificateId";
    private static final String SQL_SELECT_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION = "select * from gift_certificate where name like :pattern or description like :pattern";

    @Autowired
    public CertificateDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Certificate> getAll() {
        return getCertificates();
    }



    private List<Certificate> getCertificates() {
        List<Certificate> certificates = this.query(SQL_SELECT_ALL, new CertificateRowMapper());
        certificates.forEach(certificate -> certificate.getTags()
                .addAll(getCertificatesTags(certificate.getId())));
        return certificates;
    }

    @Override
    public Certificate getById(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        Certificate certificate = this.queryForObject(SQL_SELECT_BY_ID, sqlParameterSource, new CertificateRowMapper());
        certificate.getTags().addAll(getCertificatesTags(id));
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
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        return this.update(SQL_DELETE, sqlParameterSource) == ONE_UPDATED_ROW;
    }

    @Override
    public List<Tag> getCertificatesTags(long id) {
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
        return this.update(SQL_ADD_TAG_TO_CERTIFICATE,sqlParameterSource) == ONE_UPDATED_ROW;
    }

    @Override
    public boolean removeTagFromCertificate(Tag tag, Certificate certificate) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", certificate.getId());
        sqlParameterSource.addValue("tagId", tag.getId());
        return this.update(SQL_DELETE_TAG_FROM_CERTIFICATE,sqlParameterSource) == ONE_UPDATED_ROW;
    }

    @Override
    public boolean isCertificateMissingTag(Tag tag, Certificate certificate){
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", certificate.getId());
        sqlParameterSource.addValue("tagId", tag.getId());
        return this.queryForObject(SQL_CERTIFICATE_HAS_TAG,sqlParameterSource, Integer.class) == NO_DB_ENTRY;
    }

    @Override
    public void deleteCertificateRelations(long id){
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("certificateId", id);
        this.update(SQL_DELETE_CERTIFICATE_RELATIONS,sqlParameterSource);
    }

    @Override
    public List<Certificate> getByPartNameOrDescription(String pattern){
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%").append(pattern).append("%");
        sqlParameterSource.addValue("pattern", stringBuilder.toString());
        List<Certificate> certificates = this.query(SQL_SELECT_CERTIFICATES_BY_PART_NAME_OR_DESCRIPTION, sqlParameterSource, new CertificateRowMapper());
        certificates.forEach(certificate -> certificate.getTags()
                .addAll(getCertificatesTags(certificate.getId())));
        return certificates;
    }

}
