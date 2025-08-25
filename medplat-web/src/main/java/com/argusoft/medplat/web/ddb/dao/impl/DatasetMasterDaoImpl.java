package com.argusoft.medplat.web.ddb.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.ddb.dao.DatasetMasterDao;
import com.argusoft.medplat.web.ddb.model.DatasetMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class DatasetMasterDaoImpl extends GenericDaoImpl<DatasetMaster, Integer> implements DatasetMasterDao {

    @Override
    public List<DatasetMaster> getAllDatasetMaster() {
        Session session = getCurrentSession();
        TypedQuery<DatasetMaster> query = session.createQuery(
                "SELECT d FROM DatasetMaster d ORDER BY d.id DESC",
                DatasetMaster.class
        );
        return query.getResultList();
    }
}
