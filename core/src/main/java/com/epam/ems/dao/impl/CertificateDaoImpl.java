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
    private final static String SORT_NAME_DESC = "name_desc";
    private final static String SORT_DATE = "date";
    private final static String SORT_DATE_DESC = "date_desc";
    private final static String SORT_NAME_ASC_DATE_ASC = "name_date";
    private final static String SORT_NAME_ASC_DATE_DESC = "name_date_desc";
    private final static String SORT_NAME_DESC_DATE_ASC = "name_desc_date";
    private final static String SORT_NAME_DESC_DATE_DESC = "name_desc_date_desc";
    private final static String UPDATE_CERTIFICATE_SET_IS_ARCHIVED_TRUE = "update Certificate c set c.isArchived = true where c.id = :id";
    private final static String FIND_ALL_CERTIFICATES = "select c from Certificate c where c.isArchived = false";
    private final static String FIND_BY_ID = "select c from Certificate c where c.id = :id and c.isArchived = false";
    private final static String EXISTS_BY_ID = "select (count(c) > 0) from Certificate c where c.id = :id and c.isArchived = false";
    private final static String FIND_CERTIFICATES_BY_TAG_IN = "select c from Certificate c join c.tags t where t in " +
            ":tags and c.isArchived = false group by c.id having count(c.id) = :amount";


    private final EntityManager entityManager;


    @Autowired
    public CertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public boolean delete(long id) {
        Query query = entityManager.createQuery(UPDATE_CERTIFICATE_SET_IS_ARCHIVED_TRUE);
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }


    @Override
    public List<Certificate> getAll(int size, int offset) {
        return entityManager.createQuery(FIND_ALL_CERTIFICATES, Certificate.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Certificate getById(long id) {
        TypedQuery<Certificate> query = entityManager.createQuery(FIND_BY_ID, Certificate.class);
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
        TypedQuery<Boolean> query = entityManager.createQuery(EXISTS_BY_ID, Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Certificate> getCertificatesContainsTags(int size, int offset, Set<Tag> tags) {
        TypedQuery<Certificate> typedQuery = entityManager.createQuery(FIND_CERTIFICATES_BY_TAG_IN, Certificate.class);
        typedQuery.setParameter("tags", tags);
        typedQuery.setParameter("amount", (long) tags.size());
        return typedQuery.setMaxResults(size)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Integer getNumberOCertificatesContainsTags(Set<Tag> tags) {
        TypedQuery<Certificate> typedQuery = entityManager.createQuery(FIND_CERTIFICATES_BY_TAG_IN, Certificate.class);
        typedQuery.setParameter("tags", tags);
        typedQuery.setParameter("amount", (long) tags.size());
        return typedQuery.getResultList().size();
    }

    @Override
    public List<Certificate> getCertificates(int size, int offset, Optional<String> sort,
                                             Optional<String> filterPattern) {

        TypedQuery<Certificate> tq = getCertificatesTypedQuery(sort, filterPattern);
        return tq.setMaxResults(size).setFirstResult(offset).getResultList();
    }


    @Override
    public Integer getCertificatesAmount(Optional<String> sort,
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
                    criteriaQuery.orderBy(builder.asc(root.get("name")));
                    break;
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
