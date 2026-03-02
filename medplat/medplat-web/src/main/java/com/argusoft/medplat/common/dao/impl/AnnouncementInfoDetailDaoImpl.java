
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.AnnouncementInfoDetailDao;
import com.argusoft.medplat.common.model.AnnouncementInfoDetail;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implements methods of AnnouncementInfoDetailDao
 * </p>
 *
 * @author smeet
 * @since 31/08/2020 4:30
 */
@Repository
public class AnnouncementInfoDetailDaoImpl extends GenericDaoImpl<AnnouncementInfoDetail, Integer> implements AnnouncementInfoDetailDao {

    @Override
    public AnnouncementInfoDetail retrieveByAnnouncementId(Integer id) {
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("announcementInfoDetailPKey").get("id"), id));
            return predicates;
        });
    }
}
