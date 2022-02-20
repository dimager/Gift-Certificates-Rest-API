package com.epam.ems.dao.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    private final static String SORT_NAME = "name";
    private final static String SORT_NAME_DESC = "name_desc";
    private final static String SORT_DATE = "date";
    private final static String SORT_DATE_DESC = "date_desc";
    private final static String SORT_NAME_ASC_DATE_ASC = "name_date";
    private final static String SORT_NAME_ASC_DATE_DESC = "name_date_desc";
    private final static String SORT_NAME_DESC_DATE_ASC = "name_desc_date";
    private final static String SORT_NAME_DESC_DATE_DESC = "name_desc_date_desc";
    private static final String MSG_SORT_TYPE_NOT_FOUND = "30406;Sort type was not found";

    private EntityManager entityManager;


    @Autowired
    public CertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public boolean delete(long id) {
        Query query = entityManager.createNamedQuery("Certificate.updateIsArchivedById");
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }


    @Override
    public List<Certificate> getAll(int limit, int offset) {
        return entityManager.createNamedQuery("Certificate.findByIsArchivedFalse", Certificate.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Certificate getById(long id) {
        TypedQuery<Certificate> query = entityManager.createNamedQuery("Certificate.findByIdEqualsAndIsArchivedFalse", Certificate.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Certificate update(Certificate certificate) {
        entityManager.merge(certificate);
        entityManager.flush();
        return entityManager.find(Certificate.class, certificate.getId());
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        return entityManager.find(Certificate.class, certificate.getId());
    }

    @Override
    public boolean isCertificateExistById(long id) {
        TypedQuery<Boolean> query = entityManager.createNamedQuery("Certificate.existsByIdEqualsAndIsArchivedFalse", Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Certificate> getCertificatesContainsTags(int size, int offset, Set<Tag> tags) {
        TypedQuery<Certificate> typedQuery = entityManager.createNamedQuery("Certificate.findByTagsIn", Certificate.class);
        typedQuery.setParameter("tags", tags);
        typedQuery.setParameter("amount", (long) tags.size());
        return typedQuery.setMaxResults(size)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Integer getNumberOCertificatesContainsTags(int size, int offset, Set<Tag> tags) {
        TypedQuery<Certificate> typedQuery = entityManager.createNamedQuery("Certificate.findByTagsIn", Certificate.class);
        typedQuery.setParameter("tags", tags);
        typedQuery.setParameter("amount", (long) tags.size());
        return typedQuery.getResultList().size();
    }

    @Override
    public List<Certificate> getCertificates(int size, int offset, Optional<String> sort,
                                             Optional<String> filterPattern) {

        TypedQuery<Certificate> tq = getCertificatesTypedQuery(sort, filterPattern);
        return tq.getResultList();
    }


    @Override
    public Integer getCertificatesAmount(int size, int offset, Optional<String> sort,
                                         Optional<String> filterPattern) {

        TypedQuery<Certificate> tq = getCertificatesTypedQuery(sort, filterPattern);
        return tq.getResultList().size();
    }

    private TypedQuery<Certificate> getCertificatesTypedQuery(Optional<String> sort, Optional<String> filterPattern) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = builder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root);
        criteriaQuery.where(builder.isFalse(root.get("isArchived")));

        if (sort.isPresent()) {
            switch (sort.get().toLowerCase(Locale.ROOT)) {
                case SORT_NAME:
                    criteriaQuery.orderBy(builder.asc(root.get("name")));
                    break;
                case SORT_NAME_DESC:
                    criteriaQuery.orderBy(builder.desc(root.get("name")));
                    break;
                case SORT_DATE:
                    criteriaQuery.orderBy(builder.asc(root.get("createdDateTime")));
                    break;
                case SORT_DATE_DESC:
                    criteriaQuery.orderBy(builder.desc(root.get("createdDateTime")));
                    break;
                case SORT_NAME_ASC_DATE_ASC:
                    criteriaQuery.orderBy(builder.asc(root.get("name")), builder.asc(root.get("createdDateTime")));
                    break;
                case SORT_NAME_ASC_DATE_DESC:
                    criteriaQuery.orderBy(builder.asc(root.get("name")), builder.desc(root.get("createdDateTime")));
                    break;
                case SORT_NAME_DESC_DATE_ASC:
                    criteriaQuery.orderBy(builder.desc(root.get("name")), builder.asc(root.get("createdDateTime")));
                    break;
                case SORT_NAME_DESC_DATE_DESC:
                    criteriaQuery.orderBy(builder.desc(root.get("name")), builder.desc(root.get("createdDateTime")));
                    break;
                default:
                    throw new ServiceException(HttpStatus.NOT_FOUND, MSG_SORT_TYPE_NOT_FOUND);
            }
        }

        TypedQuery<Certificate> tq = entityManager.createQuery(criteriaQuery);

        if (filterPattern.isPresent()) {
            criteriaQuery.where(
                    builder.and(builder.isFalse(root.get("isArchived")),
                            builder.or(builder.like(root.get("name"), builder.parameter(String.class, "pattern")),
                                    builder.like(root.get("description"), builder.parameter(String.class, "pattern")))));
            StringBuilder pattern = new StringBuilder();
            pattern.append("%").append(filterPattern.get()).append("%");
            tq = entityManager.createQuery(criteriaQuery);
            tq.setParameter("pattern", pattern.toString());
        }
        return tq;
    }
}
