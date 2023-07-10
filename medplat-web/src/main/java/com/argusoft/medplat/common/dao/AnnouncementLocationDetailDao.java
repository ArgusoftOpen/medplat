
package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.AnnouncementLocationDetail;
import com.argusoft.medplat.common.model.AnnouncementLocationDetailPKey;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.List;

/**
 * <p>Defines database method for announcement location detail</p>
 * @author smeet
 * @since 31/08/2020 10:30
 */
public interface AnnouncementLocationDetailDao extends GenericDao<AnnouncementLocationDetail, Integer> {

     /**
      * Create or update an announcement location detail
      * @param announcementLocationDetail An instance of AnnouncementLocationDetail
      */
     void createOrUpdate(AnnouncementLocationDetail announcementLocationDetail);

     /**
      * Returns a list of AnnouncementLocationDetail of given announcement location detail
      * @param announcementLocationDetailPKey An instacne of AnnouncementLocationDetailPKey
      * @return A list of AnnouncementLocationDetail
      */
     List<AnnouncementLocationDetail> retrieveById(AnnouncementLocationDetailPKey announcementLocationDetailPKey);
    
}
