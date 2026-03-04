package com.argusoft.medplat.notification.service.impl;

import com.argusoft.medplat.common.service.SystemConfigSyncService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.notification.dao.EscalationLevelMasterDao;
import com.argusoft.medplat.notification.dao.NotificationTypeMasterDao;
import com.argusoft.medplat.notification.dto.EscalationLevelMasterDto;
import com.argusoft.medplat.notification.dto.NotificationTypeMasterDto;
import com.argusoft.medplat.notification.mapper.EscalationLevelMasterMapper;
import com.argusoft.medplat.notification.mapper.NotificationTypeMasterMapper;
import com.argusoft.medplat.notification.model.EscalationLevelMaster;
import com.argusoft.medplat.notification.model.NotificationTypeMaster;
import com.argusoft.medplat.notification.service.NotificationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * Define services for notification master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class NotificationMasterServiceImpl implements NotificationMasterService {

    @Autowired
    private NotificationTypeMasterDao notificationMasterDao;

    @Autowired
    private EscalationLevelMasterDao escalationLevelMasterDao;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private SystemConfigSyncService systemConfigSyncService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(NotificationTypeMasterDto notificationMasterDto, boolean isMetodCallBySyncFunction) {
        NotificationTypeMaster notificationMaster = handleNotificationTypeMaster(isMetodCallBySyncFunction, notificationMasterDto);

        NotificationTypeMaster notificationMasterNew = NotificationTypeMasterMapper.getNotificationMasterEntity(notificationMasterDto, notificationMaster, isMetodCallBySyncFunction);


        if (notificationMasterNew.getUuid() == null) {
            notificationMasterNew.setUuid(UUID.randomUUID());
        }

        if (isMetodCallBySyncFunction) {
            notificationMasterNew.setCreatedBy(-1);
        }

        notificationMasterDao.createOrUpdate(notificationMasterNew);

        for (EscalationLevelMasterDto escalationLevelMasterDto : notificationMasterDto.getEscalationLevels()) {
            EscalationLevelMaster escalationLevelMaster = handleEscalationLevelMaster(escalationLevelMasterDto);
            escalationLevelMaster = EscalationLevelMasterMapper.convertEscalationLevelMasterDtoToEscalationLevelMaster(escalationLevelMasterDto, escalationLevelMaster);
            escalationLevelMaster.setNotificationTypeId(notificationMasterNew.getId());

            if (escalationLevelMaster.getUuid() == null) {
                escalationLevelMaster.setUuid(UUID.randomUUID());
            }

            if (escalationLevelMasterDto.getIsFromOtherServer() != null) {
                escalationLevelMaster.setCreatedBy(-1);
            }

            escalationLevelMasterDao.createOrUpdate(escalationLevelMaster);
        }
        // retrieve fresh object from db and save for sync so other server can read the latest boject
        if (!isMetodCallBySyncFunction && Objects.nonNull(notificationMaster)) {
            // save only if call is from NON sync call .(if it is direct save call)
            NotificationTypeMasterDto dto = this.retrieveById(notificationMaster.getId());
            List<EscalationLevelMaster> escalationLevelMasters = escalationLevelMasterDao.retrieveByNotificationId(notificationMaster.getId());
            dto.setEscalationLevels(EscalationLevelMasterMapper.convertEscalationLevelMasterListToEscalationLevelMasterDtoList(escalationLevelMasters));
            systemConfigSyncService.createOrUpdate(dto, ConstantUtil.SYNC_NOTIFICATION);
        }
    }

    /**
     * Handle notification type master details.
     *
     * @param isMetodCallBySyncFunction Is method called by sync function.
     * @param notificationMasterDto     Notification master details.
     * @return Returns notification type master details.
     */
    private NotificationTypeMaster handleNotificationTypeMaster(Boolean isMetodCallBySyncFunction,
                                                                NotificationTypeMasterDto notificationMasterDto) {
        NotificationTypeMaster notificationMaster = null;

        if (Boolean.FALSE.equals(isMetodCallBySyncFunction)) {
            if (notificationMasterDto.getId() != null) {
                notificationMaster = notificationMasterDao.retrieveById(notificationMasterDto.getId());
            }
        } else {
            if (notificationMasterDto.getUuid() != null) {
                NotificationTypeMaster temp = notificationMasterDao.retrieveByUUID(notificationMasterDto.getUuid());
                if (temp != null) {
                    notificationMaster = temp;
                }
            }
        }
        return notificationMaster;
    }

    /**
     * Handle escalation level master details.
     *
     * @param escalationLevelMasterDto Escalation level master details.
     * @return Returns escalation level master.
     */
    private EscalationLevelMaster handleEscalationLevelMaster(EscalationLevelMasterDto escalationLevelMasterDto) {
        EscalationLevelMaster escalationLevelMaster = null;

//          this variable is set at ServerManagementServiceImpl to differentiate call from sync method
        if (escalationLevelMasterDto.getIsFromOtherServer() == null) {
            if (escalationLevelMasterDto.getId() != null) {
                escalationLevelMaster = escalationLevelMasterDao.retrieveById(escalationLevelMasterDto.getId());
            }
        } else {
            if (escalationLevelMasterDto.getUuid() != null) {
                EscalationLevelMaster t = escalationLevelMasterDao.retrieveByUUID(escalationLevelMasterDto.getUuid());
                if (t != null) {
                    escalationLevelMaster = t;
                }
            }
        }
        return escalationLevelMaster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeMasterDto retrieveById(Integer id) {
        NotificationTypeMaster notificationMaster = notificationMasterDao.retrieveById(id);
        return NotificationTypeMasterMapper.getNotificationMasterDto(notificationMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationTypeMasterDto> retrieveAll(Boolean isActive) {
        List<NotificationTypeMaster> notificationMasters = notificationMasterDao.retrieveAll(isActive);
        List<NotificationTypeMasterDto> notificationMasterDtos = NotificationTypeMasterMapper.getNotificationMasterDtoList(notificationMasters);
        for (NotificationTypeMasterDto notificationTypeMasterDto : notificationMasterDtos) {
            List<EscalationLevelMaster> escalationLevelMasters = escalationLevelMasterDao.retrieveByNotificationId(notificationTypeMasterDto.getId());
            notificationTypeMasterDto.setEscalationLevels(EscalationLevelMasterMapper.convertEscalationLevelMasterListToEscalationLevelMasterDtoList(escalationLevelMasters));
        }
        return notificationMasterDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(Integer id, Boolean isActive) {
        NotificationTypeMaster notificationMaster = notificationMasterDao.retrieveById(id);
        if (Boolean.TRUE.equals(isActive)) {
            notificationMaster.setState(NotificationTypeMaster.State.ACTIVE);
        } else {
            notificationMaster.setState(NotificationTypeMaster.State.INACTIVE);
        }
        notificationMasterDao.merge(notificationMaster);
        NotificationTypeMasterDto notificationMasterDto = NotificationTypeMasterMapper.getNotificationMasterDto(notificationMaster);
        systemConfigSyncService.createOrUpdate(notificationMasterDto, ConstantUtil.SYNC_NOTIFICATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationTypeMaster retrieveByCode(String code) {
        return notificationMasterDao.retrieveByCode(code);
    }

}
