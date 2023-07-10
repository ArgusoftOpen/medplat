
package com.argusoft.medplat.common.service;

import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.dto.AnnouncementPushNotificationDto;
import com.argusoft.medplat.exception.ImtechoResponseEntity;
import com.argusoft.medplat.mobile.dto.AnnouncementMobDataBean;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * Define methods for announcement
 * </p>
 *
 * @author smeet
 * @since 27/08/2020 4:30
 */
public interface AnnouncementService {

    /**
     * Create or update an announcement
     *
     * @param announcementMasterDto An instance of AnnouncementMasterDto
     */
    void createOrUpdate(AnnouncementMasterDto announcementMasterDto);

    /**
     * Uploads given file to the server
     *
     * @param file A multi part file to upload
     * @return Name of the file
     */
    String uploadFile(MultipartFile[] file);

    /**
     * Removes a file of given filename from server
     *
     * @param file A filename to be remove
     */
    void removeFile(String file);

    /**
     * Returns an announcement of given id
     *
     * @param id An Id of AnnouncementMasterDto
     * @return An instance of AnnouncementMasterDto
     */
    AnnouncementMasterDto retrieveById(Integer id);

    /**
     * Updates status of an announcement of given id
     *
     * @param id An Id of AnnouncementMasterDto
     */
    void toggleStatusById(Integer id);

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

    /**
     * Checks whether location to be added is already exist in current location or in its child locations or not
     *
     * @param locationIds A list of location id
     * @param toBeAdded   A location to add
     * @return ImtechoResponseEntity with location name string or an exception string if location is already exist
     */
    ImtechoResponseEntity validateAOI(List<Integer> locationIds, Integer toBeAdded);

    List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset) throws ParseException;

    LinkedHashMap<String, Integer> getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId);

    void markAnnouncementAsSeen(Integer announcementId, Integer healthInfraId);

    AnnouncementPushNotificationDto getAnnouncementDetailsForPushNotification(Integer announcementId);
}
