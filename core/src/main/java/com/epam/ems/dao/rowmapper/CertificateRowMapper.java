package com.epam.ems.dao.rowmapper;

import com.epam.ems.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong(1));
        certificate.setName(rs.getString(2));
        certificate.setDescription(rs.getString(3));
        certificate.setPrice(rs.getDouble(4));
        certificate.setDuration(rs.getShort(5));
        certificate.setCreatedTime(rs.getTimestamp(6));
        certificate.setLastUpdatedTime(rs.getTimestamp(7));
        return certificate;
    }
}
