package com.argusoft.medplat.fcm.service.impl;

import com.argusoft.medplat.common.dto.LocationDto;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationConfigDao;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationLocationDetailDao;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationRoleUserDetailDao;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationConfigDto;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDisplayDto;
import com.argusoft.medplat.fcm.mapper.TechoPushNotificationConfigMapper;
import com.argusoft.medplat.fcm.model.TechoPushNotificationConfig;
import com.argusoft.medplat.fcm.model.TechoPushNotificationLocationDetail;
import com.argusoft.medplat.fcm.model.TechoPushNotificationRoleUserDetail;
import com.argusoft.medplat.fcm.service.TechoPushNotificationConfigService;
import com.argusoft.medplat.timer.dao.TimerEventDao;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.users.dto.RoleHierarchyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author nihar
 * @since 14/10/22 12:57 PM
 */
@Service
@Transactional
public class TechoPushNotificationConfigServiceImpl
        implements TechoPushNotificationConfigService {

    @Autowired
    private TechoPushNotificationConfigDao techoPushNotificationConfigDao;

    @Autowired
    private TechoPushNotificationLocationDetailDao techoPushNotificationLocationDetailDao;

    @Autowired
    private TechoPushNotificationRoleUserDetailDao techoPushNotificationRoleUserDetailDao;

    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;

    @Autowired
    private TimerEventDao timerEventDao;

    @Override
    public void createOrUpdateNotificationConfig(TechoPushNotificationConfigDto dto) {
        Integer id;
        if (dto.getId() == null) {
            dto.setState(TechoPushNotificationConfig.State.ACTIVE);
            dto.setStatus(TechoPushNotificationConfig.Status.NEW);
            id = techoPushNotificationConfigDao
                    .create(TechoPushNotificationConfigMapper
                            .convertPushNotificationConfigDtoTOPushNotificationConfig(dto));
            dto.setId(id);

        } else {
            techoPushNotificationLocationDetailDao.deleteByConfigId(dto.getId());
            techoPushNotificationRoleUserDetailDao.deleteByConfigId(dto.getId());
            TechoPushNotificationConfig techoPushNotificationConfig =
                    techoPushNotificationConfigDao.retrieveById(dto.getId());
            id = dto.getId();
            dto.setCreatedBy(techoPushNotificationConfig.getCreatedBy());
            dto.setCreatedOn(techoPushNotificationConfig.getCreatedOn());
            dto.setState(techoPushNotificationConfig.getState());
            dto.setStatus(techoPushNotificationConfig.getStatus());
            techoPushNotificationConfigDao.merge(TechoPushNotificationConfigMapper.
                    convertPushNotificationConfigDtoTOPushNotificationConfig(dto));
        }
        techoPushNotificationLocationDetailDao
                .createOrUpdateAll(TechoPushNotificationConfigMapper
                        .convertPushNotificationConfigDtoTOPushNotificationLocationList(dto));
        techoPushNotificationRoleUserDetailDao
                .createOrUpdateAll(TechoPushNotificationConfigMapper
                        .convertPushNotificationConfigDtoTOPushNotificationRoleUserDetail(dto));

        TimerEvent timerEvent = timerEventDao.findByRefIdAndType(id, TimerEvent.TYPE.CONFIG_PUSH_NOTIFY);
        if (timerEvent == null) {
            timerEvent = new TimerEvent();
            timerEvent.setRefId(id);
        }
        timerEvent.setProcessed(Boolean.FALSE);
        timerEvent.setStatus(TimerEvent.STATUS.NEW);
        if (dto.getTriggerType().equals(TechoPushNotificationConfig.TRIGGER_TYPE.SCHEDULE_TIME)) {
            timerEvent.setSystemTriggerOn(dto.getDateTime());
        } else if (dto.getTriggerType().equals(TechoPushNotificationConfig.TRIGGER_TYPE.IMMEDIATELY)) {
            timerEvent.setSystemTriggerOn(new Date((Calendar.getInstance().getTimeInMillis())));
        }
        timerEvent.setType(TimerEvent.TYPE.CONFIG_PUSH_NOTIFY);
        timerEventDao.saveOrUpdate(timerEvent);
    }

    @Override
    public List<TechoPushNotificationDisplayDto> getPushNotificationConfigs(BigInteger limit, Integer offset) {
        return techoPushNotificationConfigDao.getNotificationConfig(limit, offset);
    }

    @Override
    public TechoPushNotificationConfigDto getNotificationConfigById(Integer id) {
        TechoPushNotificationConfigDto techoPushNotificationConfigDto;
        TechoPushNotificationConfig techoPushNotificationConfig
                = techoPushNotificationConfigDao.retrieveById(id);
        techoPushNotificationConfigDto = TechoPushNotificationConfigMapper.convertModelToDto(techoPushNotificationConfig);

        List<TechoPushNotificationLocationDetail> techoPushNotificationLocationDetails
                = techoPushNotificationLocationDetailDao.findByNotificationConfigId(id);
        List<LocationDto> locations = new LinkedList<>();
        for (TechoPushNotificationLocationDetail techoPushNotificationLocationDetail
                : techoPushNotificationLocationDetails) {
            LocationDto locationDto = new LocationDto();
            locationDto.setLocationFullName(getLocationString(techoPushNotificationLocationDetail.getLocationId()));
            locationDto.setLocationId(Integer.parseInt(techoPushNotificationLocationDetail.getLocationId().toString()));
            locations.add(locationDto);
        }
        List<TechoPushNotificationRoleUserDetail> roleUserDetails
                = techoPushNotificationRoleUserDetailDao.findByNotificationConfigId(id);
        List<RoleHierarchyDto> roles = new LinkedList<>();
        for (TechoPushNotificationRoleUserDetail roleUserDetail : roleUserDetails) {
            RoleHierarchyDto roleHierarchyDto = new RoleHierarchyDto();
            roleHierarchyDto.setRoleId(roleUserDetail.getRoleId());
            roles.add(roleHierarchyDto);
        }
        techoPushNotificationConfigDto.setLocations(locations);
        techoPushNotificationConfigDto.setRoles(roles);
        return techoPushNotificationConfigDto;
    }

    @Override
    public void toggleNotificationConfigState(Integer id) {
        TechoPushNotificationConfig techoPushNotificationConfig =
                techoPushNotificationConfigDao.retrieveById(id);
        if (techoPushNotificationConfig.getState().equals(TechoPushNotificationConfig.State.ACTIVE)) {
            techoPushNotificationConfig.setState(TechoPushNotificationConfig.State.INACTIVE);
        } else {
            techoPushNotificationConfig.setState(TechoPushNotificationConfig.State.ACTIVE);
        }
        techoPushNotificationConfigDao.merge(techoPushNotificationConfig);
    }

    private String getLocationString(Integer locationId) {
        List<String> parentLocations = locationHierchyCloserDetailDao.retrieveParentLocations(locationId);
        return String.join(",", parentLocations);
    }
}
