package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.model.AnnouncementMaster;
import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.mobile.dto.AnnouncementMobDataBean;

import java.util.List;

/**
 * <p>
 * Defines database method for announcement
 * </p>
 *
 * @author smeet
 * @since 31/08/2020 10:30
 */
public interface AnnouncementDao extends GenericDao<AnnouncementMaster, Integer> {

    /**
     * Create or update an announcement
     *
     * @param announcement An instance of AnnouncementMaster
     */
    void createOrUpdate(AnnouncementMaster announcement);

    /**
     * Returns an announcement of given id
     *
     * @param id An Id of AnnouncementMaster
     * @return An instance of AnnouncementMaster
     */
    AnnouncementMaster retrieveById(Integer id);

    /**
     * Returns a list of announcement based on given criteria
     *
     * @param limit   Number of announcement to be return
     * @param offset  Value for offset
     * @param orderBy Field name for order by
     * @param order   Type of order i.e. asc, desc
     * @return A list of announcement
     */
    List<AnnouncementMasterDto> retrieveByCriteria(Integer limit, Integer offset, String orderBy, String order);

    List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset);

    Integer getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId);
}
