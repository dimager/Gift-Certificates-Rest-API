package com.epam.ems.dao.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String FIND_BY_ID_EQUALS = "select t from Tag t where t.id = :id";
    private static final String COUNT_ALL_TAGS = "select count(t) from Tag t";
    private static final String FIND_ALL = "select t from Tag t";
    private static final String FIND_BY_NAME_EQUALS = "select t from Tag t where t.name = :name";
    private static final String EXISTS_BY_NAME_EQUALS = "select (count(t) > 0) from Tag t where t.name = :name";
    private static final Logger logger = LogManager.getLogger(TagDaoImpl.class);
    private static final String SQL_SELECT_MOST_POPULAR_TAG =
            "select t.tag_id, t.name, sum(oc.amount) as totaltags, o.user_id, t1.totalcost from tags as t " +
                    "join certificate_tags as ct on t.tag_id = ct.tag_id " +
                    "join order_certificate as oc on ct.certificate_id = oc.certificate_id " +
                    "join orders as o on oc.order_id = o.order_id " +
                    "join ( select user_id, sum(cost) as totalcost from orders group by user_id order by sum(cost) desc limit 1 )" +
                    " as t1 on o.user_id = t1.user_id group by t.tag_id order by totaltags desc limit 1";

    private final EntityManager entityManager;

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (Objects.nonNull(tag)) {
            entityManager.remove(tag);
            return true;
        }
        return false;
    }

    @Override
    public List<Tag> getAll(int limit, int offset) {
        return entityManager.createQuery(FIND_ALL, Tag.class).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    public Tag getById(long id) {
        logger.error(entityManager.getEntityManagerFactory().getMetamodel().getEntities());
        logger.error(entityManager.getEntityGraphs(Tag.class));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(FIND_BY_ID_EQUALS, Tag.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public Long getNumberOfTags() {
        return entityManager.createQuery(COUNT_ALL_TAGS, Long.class).getSingleResult();
    }

    @Override
    public Tag getMostPopularTag() {
        Object[] objects = (Object[]) entityManager.createNativeQuery(SQL_SELECT_MOST_POPULAR_TAG).getSingleResult();
        BigInteger integer = (BigInteger) objects[0];
        return this.getById(integer.longValue());
    }

    @Override
    public Tag update(Tag tag) {
        entityManager.merge(tag);
        entityManager.flush();
        return tag;
    }

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }

    @Override
    public Tag getByName(String name) {
        TypedQuery<Tag> typedQuery = entityManager.createQuery(FIND_BY_NAME_EQUALS, Tag.class);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }

    @Override
    public boolean isTagExistById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Objects.nonNull(tag);
    }

    @Override
    public boolean isTagExistByName(String name) {
        TypedQuery<Boolean> typedQuery = entityManager.createQuery(EXISTS_BY_NAME_EQUALS, Boolean.class);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }
}
