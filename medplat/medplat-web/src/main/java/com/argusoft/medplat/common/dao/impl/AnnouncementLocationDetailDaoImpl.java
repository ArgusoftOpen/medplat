
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.AnnouncementLocationDetailDao;
import com.argusoft.medplat.common.model.AnnouncementLocationDetail;
import com.argusoft.medplat.common.model.AnnouncementLocationDetailPKey;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Implements method of AnnouncementLocationDetailDao
 * @author smeet
 * @since 31/08/2020 4:30
 */
@Repository
public class AnnouncementLocationDetailDaoImpl extends GenericDaoImpl<AnnouncementLocationDetail, Integer> implements AnnouncementLocationDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnouncementLocationDetail> retrieveById(AnnouncementLocationDetailPKey announcementLocationDetailPKey) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<AnnouncementLocationDetail> cq = cb.createQuery(AnnouncementLocationDetail.class);
        Root<AnnouncementLocationDetail> root = cq.from(AnnouncementLocationDetail.class);

        cq.select(root).where(cb.equal(root.get("announcementLocationDetailPKey").get("id"), announcementLocationDetailPKey.getId()));
        return session.createQuery(cq).list();
    }
}
