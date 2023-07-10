
package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.AnnouncementInfoDetail;
import com.argusoft.medplat.database.common.GenericDao;

/**
 * <p>Defines database method for announcement detail</p>
 *
 * @author smeet
 * @since 31/08/2020 10:30
 */
public interface AnnouncementInfoDetailDao extends GenericDao<AnnouncementInfoDetail, Integer> {

    /**
     * Create or update an announcement info detail
     *
     * @param announcementInfoDetail An instance of AnnouncementInfoDetail
     */
    void createOrUpdate(AnnouncementInfoDetail announcementInfoDetail);

    /**
     * Delete an announcement info detail by id
     *
     * @param id An id of announcementInfoDetail
     */
    void deleteById(Integer id);

    AnnouncementInfoDetail retrieveByAnnouncementId(Integer id);
}
