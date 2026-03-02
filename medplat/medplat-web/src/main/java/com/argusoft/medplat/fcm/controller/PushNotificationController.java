package com.argusoft.medplat.fcm.controller;
import io.swagger.v3.oas.annotations.tags.Tag;


import com.argusoft.medplat.fcm.dto.TechoPushNotificationConfigDto;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDisplayDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationType;
import com.argusoft.medplat.fcm.service.TechoPushNotificationConfigService;
import com.argusoft.medplat.fcm.service.TechoPushNotificationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/push")
@Tag(name = "Push Notification Controller", description = "")
public class PushNotificationController {
    @Autowired
    private TechoPushNotificationTypeService techoPushNotificationTypeService;

    @Autowired
    private TechoPushNotificationConfigService techoPushNotificationConfigService;

    @PostMapping(value = "")
    public void createOrUpdate(@RequestBody TechoPushNotificationType techoPushNotificationType) {
        techoPushNotificationTypeService.createOrUpdate(techoPushNotificationType);
    }

    @GetMapping(value = "")
    public List<TechoPushNotificationType> getNotificationTypeList() {
        return techoPushNotificationTypeService.getNotificationTypeList();
    }

    @GetMapping(value = "getById")
    public TechoPushNotificationType getNotificationTypeById(@RequestParam("typeId") Integer id) {
        return techoPushNotificationTypeService.getNotificationTypeById(id);
    }

    @PostMapping(value = "manageconfig")
    public void createOrUpdateNotificationConfig(@RequestBody TechoPushNotificationConfigDto techoPushNotificationConfigDto) {
        techoPushNotificationConfigService.
                createOrUpdateNotificationConfig(techoPushNotificationConfigDto);
    }

    @GetMapping(value = "getnotifications")
    public List<TechoPushNotificationDisplayDto> getPushNotificationConfigs(
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") BigInteger limit
    ) {
        return techoPushNotificationConfigService.getPushNotificationConfigs(limit,offset);
    }

    @GetMapping(value = "/getconfigbyid/{id}")
    public TechoPushNotificationConfigDto getNotificationConfigById(@PathVariable Integer id) {
        return techoPushNotificationConfigService.getNotificationConfigById(id);
    }

    @GetMapping(value = "toggleconfigstate/{id}")
    public void toggleNotificationConfigState(@PathVariable Integer id) {
        techoPushNotificationConfigService.toggleNotificationConfigState(id);
    }

//    @GetMapping(value = "file/{id}")
//    public ResponseEntity getPushNotificationFile(@PathVariable() Integer id) throws FileNotFoundException {
//        File file =techoPushNotificationTypeService.getPushNotificationFile(id);
//        if(file !=null){
//            try {
//                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//                HttpHeaders headers = new HttpHeaders();
//                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .contentLength(file.length())
//                        .contentType(MediaType.parseMediaType("application/octet-stream"))
//                        .body(resource);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return ResponseEntity.status(404).build();
//    }
}