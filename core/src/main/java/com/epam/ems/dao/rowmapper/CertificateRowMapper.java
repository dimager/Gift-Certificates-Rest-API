package com.epam.ems.dao.rowmapper;

import com.epam.ems.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong("id"));
        certificate.setName(rs.getString("name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setDuration(rs.getShort("duration"));
        certificate.setCreatedDateTime(rs.getTimestamp("create_date"));
        certificate.setLastUpdatedDateTime(rs.getTimestamp("last_update_date"));
        return certificate;

    }
}
