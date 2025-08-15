package com.argusoft.medplat.web.BackendController.repository;

import com.argusoft.medplat.web.BackendController.entity.IndicatorMaster;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class IndicatorMasterRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Integer getQueryResultByIndicatorName(String indicatorName) {
        String sql = "SELECT query_result FROM indicator_master WHERE indicator_name = :indicatorName";
        NativeQuery<Integer> query = (NativeQuery<Integer>) entityManager.createNativeQuery(sql);
        query.setParameter("indicatorName", indicatorName);
        return query.uniqueResult();
    }

    public IndicatorMaster save(IndicatorMaster indicatorMaster) {
        if (indicatorMaster.getId() == null) {
            entityManager.persist(indicatorMaster);
        } else {
            indicatorMaster = entityManager.merge(indicatorMaster);
        }
        return indicatorMaster;
    }

    public List<IndicatorMaster> findAll() {
        String sql = "SELECT * FROM indicator_master";
        NativeQuery<IndicatorMaster> query = (NativeQuery<IndicatorMaster>) entityManager.createNativeQuery(sql, IndicatorMaster.class);
        return query.list();
    }

    public Optional<IndicatorMaster> findById(Integer id) {
        IndicatorMaster entity = entityManager.find(IndicatorMaster.class, id);
        return Optional.ofNullable(entity);
    }
}
