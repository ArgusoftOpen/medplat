package com.argusoft.medplat.fcm.service.impl;

import com.argusoft.medplat.fcm.dao.TechoPushNotificationTypeDao;
import com.argusoft.medplat.fcm.model.TechoPushNotificationType;
import com.argusoft.medplat.fcm.service.TechoPushNotificationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nihar
 * @since 12/10/22 6:01 PM
 */
@Service
@Transactional
public class TechoPushNotificationTypeServiceImpl
        implements TechoPushNotificationTypeService {

    @Autowired
    private TechoPushNotificationTypeDao techoPushNotificationTypeDao;

//    @Autowired
//    private DocumentService documentService;

    @Override
    public void createOrUpdate(TechoPushNotificationType techoPushNotificationType) {
        techoPushNotificationTypeDao.createOrUpdate(techoPushNotificationType);
    }

    @Override
    public List<TechoPushNotificationType> getNotificationTypeList() {
        return techoPushNotificationTypeDao.retrieveAll();
    }

    @Override
    public TechoPushNotificationType getNotificationTypeById(Integer id) {
        return techoPushNotificationTypeDao.retrieveById(id);
    }

//    @Override
//    public File getPushNotificationFile(Integer id) throws FileNotFoundException {
//        if (techoPushNotificationTypeDao.checkFileExists(id)) {
//            return documentService.getFile(Long.parseLong(id.toString()));
//        }
//        return null;
//    }
}
