package com.argusoft.medplat.web.BackendController.repository;

import com.argusoft.medplat.web.BackendController.entity.DerivedAttributes;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class DerivedAttributesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public DerivedAttributes save(DerivedAttributes derivedAttributes) {
        if (derivedAttributes.getId() == null) {
            entityManager.persist(derivedAttributes);
        } else {
            derivedAttributes = entityManager.merge(derivedAttributes);
        }
        return derivedAttributes;
    }

    public List<DerivedAttributes> findAllByOrderByCreatedOnDesc() {
        String sql = "SELECT * FROM derived_attributes ORDER BY created_on DESC";
        NativeQuery<DerivedAttributes> query = (NativeQuery<DerivedAttributes>) entityManager.createNativeQuery(sql, DerivedAttributes.class);
        return query.list();
    }

    public List<DerivedAttributes> findAll() {
        String sql = "SELECT * FROM derived_attributes";
        NativeQuery<DerivedAttributes> query = (NativeQuery<DerivedAttributes>) entityManager.createNativeQuery(sql, DerivedAttributes.class);
        return query.list();
    }

    public Optional<DerivedAttributes> findById(Integer id) {
        DerivedAttributes entity = entityManager.find(DerivedAttributes.class, id);
        return Optional.ofNullable(entity);
    }
}