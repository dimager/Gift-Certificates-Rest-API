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

    private static final int ONE_UPDATED_ROW = 1;
    private static final String SQL_SELECT_BY_ID = "SELECT id, name FROM tag where id=:id";
    private static final String SQL_SELECT_IS_TAG_EXIST_BY_NAME = "SELECT COUNT(*) FROM tag where name like :name";
    private static final String SQL_SELECT_IS_TAG_EXIST_BY_ID = "SELECT COUNT(*) FROM tag where id = :id";
    private static final String SQL_SELECT_BY_NAME = "SELECT id, name FROM tag where name like :name";
    private static final String SQL_SELECT_ALL = "SELECT id, name FROM tag";
    private static final String SQL_UPDATE = "UPDATE tag SET name = :name WHERE id = :id";
    private static final String SQL_INSERT = "INSERT INTO tag (name) VALUES (:name)";
    private static final String SQL_DELETE = "DELETE FROM tag WHERE id = :id";


    @Autowired
    public TagDaoImpl(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public boolean delete(long id) {
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", id);
            return this.update(SQL_DELETE, sqlParameterSource) == ONE_UPDATED_ROW;
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
        this.update(SQL_INSERT, sqlParameterSource, keyHolder);
        tag.setId(keyHolder.getKey().longValue());
        return tag;
    }

    @Override
    public Tag getByName(String name) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", name);
        return this.queryForObject(SQL_SELECT_BY_NAME, sqlParameterSource, new TagRowMapper());
    }

    @Override
    public boolean isTagExistById(long id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        return this.queryForObject(SQL_SELECT_IS_TAG_EXIST_BY_ID, sqlParameterSource, Integer.class) == ONE_UPDATED_ROW;
    }

    @Override
    public boolean isTagExistByName(String name) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", name);
        return this.queryForObject(SQL_SELECT_IS_TAG_EXIST_BY_NAME, sqlParameterSource, Integer.class) == ONE_UPDATED_ROW;
    }

}
