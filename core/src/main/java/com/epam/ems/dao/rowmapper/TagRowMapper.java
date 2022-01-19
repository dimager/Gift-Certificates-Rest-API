package com.epam.ems.dao.rowmapper;

import com.epam.ems.dao.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag>{
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(1));
        tag.setName(rs.getString(2));
        return tag;
    }
}
