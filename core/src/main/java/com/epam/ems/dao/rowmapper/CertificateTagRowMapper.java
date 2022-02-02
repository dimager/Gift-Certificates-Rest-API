package com.epam.ems.dao.rowmapper;

import com.epam.ems.entity.CertificateTag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateTagRowMapper implements RowMapper<CertificateTag> {
    @Override
    public CertificateTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        CertificateTag certificateTag = new CertificateTag();
        certificateTag.setCertId(rs.getLong("gift_certificate_id"));
        certificateTag.setTagId(rs.getLong("id"));
        certificateTag.setTagName(rs.getString("name"));
        return certificateTag;
    }
}
