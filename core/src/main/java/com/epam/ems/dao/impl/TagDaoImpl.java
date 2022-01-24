package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.rowmapper.TagRowMapper;
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
public class TagDaoImpl extends NamedParameterJdbcTemplate implements TagDao {

    private final String SQL_SELECT_BY_ID = "SELECT id, name FROM tag where id=:id";
    private final String SQL_SELECT_ALL = "SELECT id, name FROM tag";
    private final String SQL_UPDATE = "UPDATE tag SET name = :name WHERE id = :id";
    private final String SQL_INSERT = "INSERT INTO tag (name) VALUES (:name)";

    @Autowired
    public TagDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Tag> getAll() {
        return this.query(SQL_SELECT_ALL, new TagRowMapper());
    }

    @Override
    public Tag getById(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        return this.queryForObject(SQL_SELECT_BY_ID, sqlParameterSource, new TagRowMapper());
    }

    @Override
    public Tag update(Tag tag) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", tag.getName());
        sqlParameterSource.addValue("id", tag.getId());
        this.update(SQL_UPDATE, sqlParameterSource);
        return this.queryForObject(SQL_SELECT_BY_ID, sqlParameterSource, new TagRowMapper());
    }

    @Override
    public Tag create(Tag tag) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", tag.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        tag.setId(keyHolder.getKey().longValue());
        return tag;
    }

}
