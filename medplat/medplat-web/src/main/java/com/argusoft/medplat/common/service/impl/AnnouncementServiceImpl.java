
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.AnnouncementDao;
import com.argusoft.medplat.common.dao.AnnouncementHealthInfraDetailDao;
import com.argusoft.medplat.common.dao.AnnouncementInfoDetailDao;
import com.argusoft.medplat.common.dao.AnnouncementLocationDetailDao;
import com.argusoft.medplat.common.dto.AnnouncementMasterDto;
import com.argusoft.medplat.common.dto.AnnouncementPushNotificationDto;
import com.argusoft.medplat.common.dto.LocationDto;
import com.argusoft.medplat.common.mapper.AnnouncementMapper;
import com.argusoft.medplat.common.model.*;
import com.argusoft.medplat.common.service.AnnouncementService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.exception.ImtechoResponseEntity;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.mobile.dto.AnnouncementMobDataBean;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.users.dao.UserHealthInfrastructureDao;
import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements methods of AnnouncementService
 *
 * @author smeet
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final String ANNOUNCEMENT_TEMP = "Announcement_Temp";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    @Autowired
    AnnouncementDao announcementDao;

    @Autowired
    AnnouncementInfoDetailDao announcementInfoDetailDao;

    @Autowired
    AnnouncementLocationDetailDao announcementLocationDetailDao;

    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;

    @Autowired
    private AnnouncementHealthInfraDetailDao announcementHealthInfraDetailDao;

    @Autowired
    private LocationMasterDao locationMasterDao;

    @Autowired
    private TimerEventDao timerEventDao;

    @Autowired
    private UserHealthInfrastructureDao userHealthInfrastructureDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(AnnouncementMasterDto announcementMasterDto) {
        if (announcementMasterDto.getId() != null) {
            AnnouncementMaster announcementMaster = announcementDao.retrieveById(announcementMasterDto.getId());
            announcementMaster.setSubject(announcementMasterDto.getSubject());
            announcementMaster.setFromDate(announcementMasterDto.getFromDate());
            announcementDao.update(announcementMaster);
            AnnouncementLocationDetailPKey announcementLocationDetailPKeyForDeletion = new AnnouncementLocationDetailPKey();
            announcementLocationDetailPKeyForDeletion.setId(announcementMaster.getId());
            List<AnnouncementLocationDetail> announcementLocationDetailListToBeDeleted = announcementLocationDetailDao.retrieveById(announcementLocationDetailPKeyForDeletion);
            announcementLocationDetailDao.deleteAll(announcementLocationDetailListToBeDeleted);
            announcementLocationDetailDao.flush();
            AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey = new AnnouncementHealthInfraDetailPKey();
            announcementHealthInfraDetailPKey.setId(announcementMaster.getId());
            List<AnnouncementHealthInfraDetail> announcementHealthInfraDetailsToBeDeleted = announcementHealthInfraDetailDao.retrieveById(announcementHealthInfraDetailPKey);
            announcementHealthInfraDetailDao.deleteAll(announcementHealthInfraDetailsToBeDeleted);
            announcementHealthInfraDetailDao.flush();
            createAnnouncementLocations(announcementMasterDto, announcementMaster.getId());
            createAnnouncementHealthInfras(announcementMasterDto, announcementMaster.getId());
            if (ConstantUtil.IMPLEMENTATION_TYPE.equals("uttarakhand")) {
                createAnnouncementPushNotification(announcementMaster);
            }
        } else {
            AnnouncementMaster announcementMaster = AnnouncementMapper.convertAnnouncementDtoToMaster(announcementMasterDto);
            Integer announcementId = announcementDao.create(announcementMaster);
            createAnnouncementMedia(announcementMasterDto, announcementId);
            createAnnouncementLocations(announcementMasterDto, announcementId);
            createAnnouncementHealthInfras(announcementMasterDto, announcementId);
            if (ConstantUtil.IMPLEMENTATION_TYPE.equals("uttarakhand")) {
                createAnnouncementPushNotification(announcementMaster);
            }
        }
    }

    private void createAnnouncementPushNotification(AnnouncementMaster announcementMaster) {
        timerEventDao.deleteByRefIdAndType(announcementMaster.getId(), TimerEvent.TYPE.ANNOUNCEMENT_PUSH_NOTIFICATION);
        TimerEvent timerEvent = new TimerEvent();
        timerEvent.setRefId(announcementMaster.getId());
        timerEvent.setProcessed(Boolean.FALSE);
        timerEvent.setStatus(TimerEvent.STATUS.NEW);
        timerEvent.setType(TimerEvent.TYPE.ANNOUNCEMENT_PUSH_NOTIFICATION);
        timerEvent.setSystemTriggerOn(announcementMaster.getFromDate());
        timerEventDao.createUpdate(timerEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnouncementPushNotificationDto getAnnouncementDetailsForPushNotification(Integer announcementId) {
        AnnouncementPushNotificationDto announcementPushNotificationDto = new AnnouncementPushNotificationDto();
        AnnouncementMaster announcementMaster = announcementDao.retrieveById(announcementId);
        AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey = new AnnouncementHealthInfraDetailPKey();
        announcementHealthInfraDetailPKey.setId(announcementId);
        List<AnnouncementHealthInfraDetail> announcementHealthInfraDetails = announcementHealthInfraDetailDao.retrieveById(announcementHealthInfraDetailPKey);
        List<UserHealthInfrastructure> userHealthInfrastructures = new ArrayList<>();
        for (AnnouncementHealthInfraDetail announcementHealthInfraDetail : announcementHealthInfraDetails) {
            List<UserHealthInfrastructure> uh = userHealthInfrastructureDao.retrieveByHealthInfrastructureId(announcementHealthInfraDetail.getAnnouncementHealthInfraDetailPKey().getHealthInfraId());
            userHealthInfrastructures.addAll(uh);
        }
        List<Integer> users = userHealthInfrastructures.stream().map(UserHealthInfrastructure::getUserId).distinct().collect(Collectors.toList());
        announcementPushNotificationDto.setAnnouncementMaster(announcementMaster);
        announcementPushNotificationDto.setUsers(users);
        return announcementPushNotificationDto;
    }

    /**
     * Creates media of an announcement
     *
     * @param announcementMasterDto An instance of AnnouncementMasterDto
     * @param announcementId        ID of AnnouncementMaster
     */
    private void createAnnouncementMedia(AnnouncementMasterDto announcementMasterDto, Integer announcementId) {
        if (announcementMasterDto.getMediaPath() != null) {
            announcementMasterDto.setId(announcementId);
            String oldFilePath = ConstantUtil.REPOSITORY_PATH + ANNOUNCEMENT_TEMP;
            String newFilePath = ConstantUtil.REPOSITORY_PATH + "Announcement";
            File directory = new File(newFilePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = announcementMasterDto.getMediaPath();
            File oldFile = new File(oldFilePath + File.separator + fileName);
            String mediaPath = "Announcement_" + announcementId + announcementMasterDto.getDefaultLanguage() + "." + FilenameUtils.getExtension(announcementMasterDto.getMediaPath());
            boolean response = oldFile.renameTo(new File(newFilePath + File.separator + mediaPath));
            if (response) {
                log.info("File renamed successfully");
            }
            AnnouncementInfoDetailPKey announcementInfoDetailPKey = new AnnouncementInfoDetailPKey();
            announcementInfoDetailPKey.setId(announcementId);
            announcementInfoDetailPKey.setLanguage(announcementMasterDto.getDefaultLanguage());
            AnnouncementInfoDetail announcementInfoDetail = new AnnouncementInfoDetail();
            announcementInfoDetail.setAnnouncementInfoDetailPKey(announcementInfoDetailPKey);
            announcementInfoDetail.setFileExtension(FilenameUtils.getExtension(announcementMasterDto.getMediaPath()));
            announcementInfoDetail.setMediaPath(mediaPath);
            announcementInfoDetailDao.create(announcementInfoDetail);
        }
    }

    /**
     * Creates Announcement Location and Group Entries
     *
     * @param announcementMasterDto An instance of AnnouncementMasterDto
     * @param announcementId        ID of AnnouncementMaster
     */
    private void createAnnouncementLocations(AnnouncementMasterDto announcementMasterDto, Integer announcementId) {
        if (announcementMasterDto.getLocations().isEmpty()) {
            List<LocationMaster> locationMasters = locationMasterDao.retrieveLocationByLevel(1);
            List<LocationDto> locationDtos = new ArrayList<>();
            for (LocationMaster locationMaster : locationMasters) {
                LocationDto locationDto = new LocationDto();
                locationDto.setLocationId(locationMaster.getId());
                locationDto.setType(locationMaster.getType());
                locationDtos.add(locationDto);
            }
            announcementMasterDto.setLocations(locationDtos);
        }
        List<AnnouncementLocationDetail> announcementLocationDetailList = new ArrayList<>();
        for (String announcementFor : announcementMasterDto.getAnnouncementForArray()) {
            for (LocationDto locationDto : announcementMasterDto.getLocations()) {
                AnnouncementLocationDetailPKey announcementLocationDetailPKey = new AnnouncementLocationDetailPKey();
                announcementLocationDetailPKey.setId(announcementId);
                announcementLocationDetailPKey.setLocation(locationDto.getLocationId());
                announcementLocationDetailPKey.setAnnouncementFor(announcementFor);
                AnnouncementLocationDetail announcementLocationDetail = new AnnouncementLocationDetail();
                announcementLocationDetail.setIsActive(Boolean.TRUE);
                announcementLocationDetail.setLocationType(locationDto.getType());
                announcementLocationDetail.setAnnouncementLocationDetailPKey(announcementLocationDetailPKey);
                announcementLocationDetailList.add(announcementLocationDetail);
            }
        }
        announcementLocationDetailDao.createOrUpdateAll(announcementLocationDetailList);
    }

    private void createAnnouncementHealthInfras(AnnouncementMasterDto announcementMasterDto, Integer announcementId) {
        List<AnnouncementHealthInfraDetail> announcementHealthInfraDetails = new ArrayList<>();
        for (Integer healthInfra : announcementMasterDto.getHealthInfras()) {
            AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey = new AnnouncementHealthInfraDetailPKey();
            announcementHealthInfraDetailPKey.setId(announcementId);
            announcementHealthInfraDetailPKey.setHealthInfraId(healthInfra);
            AnnouncementHealthInfraDetail announcementHealthInfraDetail = new AnnouncementHealthInfraDetail();
            announcementHealthInfraDetail.setAnnouncementHealthInfraDetailPKey(announcementHealthInfraDetailPKey);
            announcementHealthInfraDetail.setHasSeen(Boolean.FALSE);
            announcementHealthInfraDetails.add(announcementHealthInfraDetail);
        }
        announcementHealthInfraDetailDao.createOrUpdateAll(announcementHealthInfraDetails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String uploadFile(MultipartFile[] file) {
        String fileName = file[0].getOriginalFilename();
        String filePath = ConstantUtil.REPOSITORY_PATH + ANNOUNCEMENT_TEMP;
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(new File(filePath + File.separator + fileName))) {
            outputStream.write(file[0].getBytes());

        } catch (FileNotFoundException e) {
            Logger.getLogger(AnnouncementServiceImpl.class.getName()).log(Level.SEVERE, "File not found", e);
        } catch (IOException e) {
            Logger.getLogger(AnnouncementServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFile(String file) {
        String filePath = ConstantUtil.REPOSITORY_PATH + ANNOUNCEMENT_TEMP;
        try {
            Path sourcePath = Paths.get(filePath + FileSystems.getDefault().getSeparator() + file);
            Files.delete(sourcePath);
        } catch (Exception e) {
            Logger.getLogger(AnnouncementServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnouncementMasterDto retrieveById(Integer id) {
        AnnouncementMaster announcementMaster = announcementDao.retrieveById(id);
        AnnouncementLocationDetailPKey announcementLocationDetailPKey = new AnnouncementLocationDetailPKey();
        announcementLocationDetailPKey.setId(id);
        AnnouncementInfoDetailPKey announcementInfoDetailPKey = new AnnouncementInfoDetailPKey();
        announcementInfoDetailPKey.setId(id);
        if (announcementMaster != null) {
            AnnouncementMasterDto announcementMasterDto = AnnouncementMapper.convertAnnouncementMasterToDto(announcementMaster);
            List<AnnouncementLocationDetail> announcementLocationDetailList = announcementLocationDetailDao.retrieveById(announcementLocationDetailPKey);
            AnnouncementInfoDetail announcementInfoDetail = announcementInfoDetailDao.retrieveByAnnouncementId(announcementMaster.getId());
            if (announcementInfoDetail != null) {
                announcementMasterDto.setMediaPath(announcementInfoDetail.getMediaPath());
                File mediaFile = new File(ConstantUtil.REPOSITORY_PATH + "Announcement" + File.separator + announcementInfoDetail.getMediaPath());
                announcementMasterDto.setMediaExists(mediaFile.exists());
            }
            List<String> announcementFor = announcementLocationDetailList.stream().map(a -> a.getAnnouncementLocationDetailPKey().getAnnouncementFor()).distinct().collect(Collectors.toList());
            announcementMasterDto.setAnnouncementForArray(announcementFor);
            List<LocationDto> announcementLocationDetailDtoList = AnnouncementMapper.convertLocationDetailToDto(announcementLocationDetailList);
            for (LocationDto location : announcementLocationDetailDtoList) {
                location.setLocationFullName(this.getLocationString(location.getLocationId()));
            }
            announcementMasterDto.setLocations(announcementLocationDetailDtoList);
            AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey = new AnnouncementHealthInfraDetailPKey();
            announcementHealthInfraDetailPKey.setId(id);
            List<AnnouncementHealthInfraDetail> announcementHealthInfraDetails = announcementHealthInfraDetailDao.retrieveById(announcementHealthInfraDetailPKey);
            List<Integer> healthInfras = announcementHealthInfraDetails.stream().map(h -> h.getAnnouncementHealthInfraDetailPKey().getHealthInfraId()).distinct().collect(Collectors.toList());
            announcementMasterDto.setHealthInfras(healthInfras);
            return announcementMasterDto;
        } else {
            throw new ImtechoSystemException("Announcement does not exist with this id : " + id, 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleStatusById(Integer id) {
        AnnouncementMaster announcementMaster = announcementDao.retrieveById(id);
        if (announcementMaster != null) {
            if (Boolean.TRUE.equals(announcementMaster.getIsActive())) {
                announcementMaster.setIsActive(Boolean.FALSE);
                timerEventDao.deleteByRefIdAndType(announcementMaster.getId(), TimerEvent.TYPE.ANNOUNCEMENT_PUSH_NOTIFICATION);
            } else {
                announcementMaster.setIsActive(Boolean.TRUE);
                if (ConstantUtil.IMPLEMENTATION_TYPE.equals("uttarakhand") && announcementMaster.getFromDate().after(new Date())) {
                    createAnnouncementPushNotification(announcementMaster);
                }
            }
            announcementDao.update(announcementMaster);
        } else {
            throw new ImtechoSystemException("Announcement does not exist with this id " + id, 101);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnouncementMasterDto> retrieveByCriteria(Integer limit, Integer offset, String orderBy, String order) {
        return announcementDao.retrieveByCriteria(limit, offset, orderBy, order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImtechoResponseEntity validateAOI(List<Integer> locationIds, Integer toBeAdded) {
        List<Integer> parentLocationIds = locationHierchyCloserDetailDao.retrieveParentLocationIds(toBeAdded);
        List<Integer> childLocationIds = locationHierchyCloserDetailDao.retrieveChildLocationIds(toBeAdded);
        if (!CollectionUtils.isEmpty(locationIds) && (CollectionUtils.containsAny(childLocationIds, locationIds) || CollectionUtils.containsAny(parentLocationIds, locationIds))) {
            return new ImtechoResponseEntity("Duplicacy of area of intervention found. Kindly add another.", 2);
        }
        String locationString = this.getLocationString(toBeAdded);
        if (locationString != null) {
            return new ImtechoResponseEntity(locationString, 1);
        }
        return new ImtechoResponseEntity("There is some issue with server. Please check!", 0);
    }

    @Override
    public List<AnnouncementMobDataBean> getAnnouncementsByHealthInfra(Integer healthInfraId, Integer limit, Integer offset) throws ParseException {
        String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        List<AnnouncementMobDataBean> announcementsByHealthInfra = announcementDao.getAnnouncementsByHealthInfra(healthInfraId, limit, offset);
        for (AnnouncementMobDataBean announcement : announcementsByHealthInfra) {
            Date date = inputFormat.parse(announcement.getFromDate().toString());
            announcement.setPublishedOn(date.getTime());
        }
        return announcementsByHealthInfra;
    }

    @Override
    public LinkedHashMap<String, Integer> getAnnouncementsUnreadCountByHealthInfra(Integer healthInfraId) {
        Integer unreadCount = announcementDao.getAnnouncementsUnreadCountByHealthInfra(healthInfraId);
        LinkedHashMap<String, Integer> response = new LinkedHashMap<>();
        response.put("count", unreadCount);
        return response;
    }

    @Override
    public void markAnnouncementAsSeen(Integer announcementId, Integer healthInfraId) {
        AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey = new AnnouncementHealthInfraDetailPKey();
        announcementHealthInfraDetailPKey.setId(announcementId);
        announcementHealthInfraDetailPKey.setHealthInfraId(healthInfraId);
        List<AnnouncementHealthInfraDetail> announcementHealthInfraDetails = announcementHealthInfraDetailDao.retrieveById(announcementHealthInfraDetailPKey);
        if (announcementHealthInfraDetails != null && announcementHealthInfraDetails.size() == 1) {
            announcementHealthInfraDetails.get(0).setHasSeen(Boolean.TRUE);
            announcementHealthInfraDetailDao.update(announcementHealthInfraDetails.get(0));
        }
    }

    /**
     * Generate comma separated location string of given location id
     *
     * @param location An id of location
     * @return A location string
     */
    public String getLocationString(Integer location) {
        List<String> parentLocations = locationHierchyCloserDetailDao.retrieveParentLocations(location);
        return String.join(",", parentLocations);
    }

}
