
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.AnnouncementDao;
import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.model.AnnouncementMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.AnnouncementMobDataBean;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * Implements methods of AnnouncementDao
 * </p>
 *
 * @author smeet
 * @since 31/08/2020 4:30
 */
@Repository
public class AnnouncementDaoImpl extends GenericDaoImpl<AnnouncementMaster, Integer> implements AnnouncementDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnouncementMaster retrieveById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<AnnouncementMaster> cq = cb.createQuery(AnnouncementMaster.class);
        Root<AnnouncementMaster> root = cq.from(AnnouncementMaster.class);

        cq.select(root).where(cb.equal(root.get("id"), id));
        return session.createQuery(cq).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnouncementMasterDto> retrieveByCriteria(Integer limit, Integer offset, String orderBy, String order) {
        String query = "select announcement_info_master.id as id"
                + ",announcement_info_master.subject as subject"
                + ",announcement_info_master.default_language as defaultLanguage"
                + ",announcement_info_master.from_date as fromDate"
                + ",announcement_info_master.is_active as isActive"
                + ",announcement_info_master.created_by as createdBy"
                + ",announcement_info_master.created_on as createdOn"
                + ",announcement_info_detail.media_path as mediaPath"
                + ",announcement_info_detail.file_extension as fileExtension"
                + " from announcement_info_master"
                + " left outer join announcement_info_detail"
                + " on announcement_info_master.id=announcement_info_detail.announcement"
                + " ORDER BY " + orderBy + " " + order + " limit " + limit + " offset " + offset;
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<AnnouncementMasterDto> nativeQuery = session.createNativeQuery(query);
        nativeQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("subject", StandardBasicTypes.STRING)
                .addScalar("defaultLanguage", StandardBasicTypes.STRING)
                .addScalar("fromDate", StandardBasicTypes.DATE)
                .addScalar("isActive", StandardBasicTypes.BOOLEAN)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("mediaPath", StandardBasicTypes.STRING)
                .addScalar("fileExtension", StandardBasicTypes.STRING);
        return nativeQuery.setResultTransformer(Transformers.aliasToBean(AnnouncementMasterDto.class)).list();
    }

    @Override
    public List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset) {
        String query = "with announcements as (\n" +
                "\tselect announcement as id,\n" +
                "\thas_seen as has_seen\n" +
                "\tfrom announcement_health_infra_detail\n" +
                "\twhere health_infra_id = :healthInfraId\n" +
                ")select master.id as \"announcementId\",\n" +
                "master.subject as \"subject\",\n" +
                "master.default_language as \"defaultLanguage\",\n" +
                "to_char(master.from_date,'YYYY-MM-DD HH24:MI:SS') as \"fromDate\",\n" +
                "master.is_active as \"isActive\",\n" +
                "detail.media_path as \"fileName\",\n" +
                "detail.file_extension as \"fileExtension\",\n" +
                "announcements.has_seen as \"hasSeen\"\n" +
                "from announcement_info_master master\n" +
                "inner join announcements on master.id = announcements.id\n" +
                "left join announcement_info_detail detail on master.id = detail.announcement\n" +
                "where master.is_active\n" +
                "order by announcements.has_seen asc,master.from_date desc\n" +
                "limit :limit offset :offset";
        SQLQuery<AnnouncementMobDataBean> sqlQuery = getCurrentSession().createSQLQuery(query);
        sqlQuery.setParameter("healthInfraId", healthInfraId)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .addScalar("announcementId", StandardBasicTypes.INTEGER)
                .addScalar("subject", StandardBasicTypes.STRING)
                .addScalar("defaultLanguage", StandardBasicTypes.STRING)
                .addScalar("fromDate", StandardBasicTypes.STRING)
                .addScalar("isActive", StandardBasicTypes.BOOLEAN)
                .addScalar("fileName", StandardBasicTypes.STRING)
                .addScalar("fileExtension", StandardBasicTypes.STRING)
                .addScalar("hasSeen", StandardBasicTypes.BOOLEAN);
        return sqlQuery.setResultTransformer(Transformers.aliasToBean(AnnouncementMobDataBean.class)).list();
    }

    @Override
    public Integer getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId) {
        String query = "select count(1) as \"unreadCount\"\n" +
                "from announcement_health_infra_detail ann_infra\n" +
                "inner join announcement_info_master master on ann_infra.announcement = master.id\n" +
                "and master.is_active\n" +
                "where ann_infra.health_infra_id = :healthInfraId\n" +
                "and ann_infra.has_seen is not true";
        SQLQuery<BigInteger> sqlQuery = getCurrentSession().createSQLQuery(query);
        sqlQuery.setParameter("healthInfraId", healthInfraId);
        BigInteger unreadCount = sqlQuery.getSingleResult();
        return unreadCount.intValue();
    }
}
