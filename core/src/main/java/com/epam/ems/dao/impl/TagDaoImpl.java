package com.epam.ems.dao.impl;

import com.epam.ems.dao.JdbcTemplateFactory;
import com.epam.ems.dao.TagDao;
import com.epam.ems.dao.rowmapper.TagRowMapper;
import com.epam.ems.dao.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan("com.epam.ems")
@PropertySource("sql_queries.properties")
public class TagDaoImpl implements TagDao {

    private final JdbcTemplateFactory JdbcTemplateFactory;

    @Value("${sql.select_all_tags}")
    private String sqlFindAllTags;

    @Autowired
    public TagDaoImpl(JdbcTemplateFactory JdbcTemplateFactory) {
        this.JdbcTemplateFactory = JdbcTemplateFactory;
    }

    @Override
    public List<Tag> getAllTags (){
        JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate();
        List<Tag> list = jdbcTemplate.query(sqlFindAllTags, new TagRowMapper());
        return  list;
    }

}
