package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.AnnouncementHealthInfraDetailDao;
import com.argusoft.medplat.common.model.AnnouncementHealthInfraDetail;
import com.argusoft.medplat.common.model.AnnouncementHealthInfraDetailPKey;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnnouncementHealthInfraDetailDaoImpl extends GenericDaoImpl<AnnouncementHealthInfraDetail, Integer> implements AnnouncementHealthInfraDetailDao {
    @Override
    public List<AnnouncementHealthInfraDetail> retrieveById(AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (announcementHealthInfraDetailPKey.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("announcementHealthInfraDetailPKey").get("id"), announcementHealthInfraDetailPKey.getId()));
            }
            if (announcementHealthInfraDetailPKey.getHealthInfraId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("announcementHealthInfraDetailPKey").get("healthInfraId"), announcementHealthInfraDetailPKey.getHealthInfraId()));
            }
            return predicates;
        });
    }
}
