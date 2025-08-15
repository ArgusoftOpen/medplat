package com.argusoft.medplat.web.BackendController.repository;

import com.argusoft.medplat.web.BackendController.entity.DatasetMaster;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class DatasetMasterRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public DatasetMaster save(DatasetMaster datasetMaster) {
        if (datasetMaster.getId() == null) {
            entityManager.persist(datasetMaster);
        } else {
            datasetMaster = entityManager.merge(datasetMaster);
        }
        return datasetMaster;
    }

    public List<DatasetMaster> findAllByOrderByIdDesc() {
        String sql = "SELECT * FROM dataset_master ORDER BY id DESC";
        NativeQuery<DatasetMaster> query = (NativeQuery<DatasetMaster>) entityManager.createNativeQuery(sql, DatasetMaster.class);
        return query.list();
    }

    public List<DatasetMaster> findAll() {
        String sql = "SELECT * FROM dataset_master";
        NativeQuery<DatasetMaster> query = (NativeQuery<DatasetMaster>) entityManager.createNativeQuery(sql, DatasetMaster.class);
        return query.list();
    }

    public Optional<DatasetMaster> findById(Integer id) {
        DatasetMaster entity = entityManager.find(DatasetMaster.class, id);
        return Optional.ofNullable(entity);
    }
}