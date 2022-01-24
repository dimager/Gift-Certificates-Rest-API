package com.epam.ems.dao;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate>{
    boolean delete(long id);
    List<Tag> getCertificatesTags(long id);

}
