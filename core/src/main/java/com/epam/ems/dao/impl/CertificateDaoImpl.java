package com.epam.ems.dao.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CertificateDaoImpl extends NamedParameterJdbcTemplate implements CertificateDao {

    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id=:id";
    private static final String SQL_SELECT_ALL = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SQL_INSERT = "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String SQL_UPDATE = "UPDATE gift_certificate SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :lastUpdateDate WHERE id = :id;";
    private static final String SQL_DELETE = "DELETE FROM gift_certificate WHERE id = :id";
    private static final String SQL_DELETE_TAGS = "DELETE FROM gift_certificate WHERE id = :id";
    private static final String SQL_SELECT_TAGS_FOR_CERTIFICATE_BY_ID = "SELECT id, name FROM tag LEFT JOIN gift_certificate_has_tag as gsht on tag.id = gsht.tag_id WHERE gsht.gift_certificate_id = :id";

    @Autowired
    public CertificateDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Certificate> getAll() {
        return this.query(SQL_SELECT_ALL, new CertificateRowMapper());
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
        sqlParameterSource.addValue("lastUpdateDate", certificate.getLastUpdatedTime());
        this.update(SQL_UPDATE, sqlParameterSource);
        return getById(certificate.getId());
    }

    @Override
    public Certificate create(Certificate certificate) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        return this.update(SQL_DELETE, sqlParameterSource) == 1;
    }

    @Override
    public List<Tag> getCertificatesTags(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        List<Tag> tags = this.query(SQL_SELECT_TAGS_FOR_CERTIFICATE_BY_ID, sqlParameterSource, new TagRowMapper());
        return tags;
    }
}
