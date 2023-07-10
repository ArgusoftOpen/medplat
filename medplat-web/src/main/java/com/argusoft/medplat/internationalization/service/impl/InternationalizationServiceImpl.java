package com.argusoft.medplat.internationalization.service.impl;

import com.argusoft.medplat.internationalization.dao.InternationalizationLabelDao;
import com.argusoft.medplat.internationalization.model.InternationalizationLabel;
import com.argusoft.medplat.internationalization.service.InternationalizationService;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     Define services for internationalization.
 * </p>
 * @author dhaval
 * @since 25/08/20 4:00 PM
 *
 */
@Service
public class InternationalizationServiceImpl implements InternationalizationService {

    @Autowired
    InternationalizationLabelDao internationalizationLabelDao;

    public static MultiKeyMap<String, String> labelsMultiKeyMap;

    private static String labelsMapLastUpdatedAt;

//    @Autowired
//    @Qualifier(value = "internationalizationLabels")
//    private TenantCacheProvider<MultiKeyMap<String, String>> tenantCacheProviderForLabels;
//
//    @Autowired
//    private CronExecutorService cronExecutorService;

    /**
     * {@inheritDoc}
     */
    @Override
    @PostConstruct
    public void loadAllLanguageLabels() {
//        cronExecutorService.execute(tenantMaster -> {
            MultiKeyMap<String, String> labelsMultiKeyMap = new MultiKeyMap<>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            labelsMapLastUpdatedAt = df.format(new Date());

            List<InternationalizationLabel> labels = internationalizationLabelDao.getAllLanguageLabels();

            if (!CollectionUtils.isEmpty(labels)) {
                for (InternationalizationLabel label : labels) {
                    labelsMultiKeyMap.put(label.getKey(), label.getLanguage(), label.getText());
                }
            }
//            tenantCacheProviderForLabels.put(labelsMultiKeyMap);
//        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelByKeyAndLanguageCode(String key, String languageCode) {
        if (labelsMultiKeyMap.get(key, languageCode) == null) {
            return key;
        }

        return labelsMultiKeyMap.get(key, languageCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLabelsMap() {
        List<InternationalizationLabel> labels = internationalizationLabelDao.getAllLanguageLabelsAfterGivenDate(labelsMapLastUpdatedAt);

        if (!CollectionUtils.isEmpty(labels)) {
            for (InternationalizationLabel label : labels) {
                labelsMultiKeyMap.put(label.getKey(), label.getLanguage(), label.getText());
            }
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        labelsMapLastUpdatedAt = df.format(new Date());
    }

    /**
     * Set last updated at in label's map.
     * @param labelsMapLastUpdatedAt Last updated at.
     */
    public static void setLabelsMapLastUpdatedAt(String labelsMapLastUpdatedAt) {
        InternationalizationServiceImpl.labelsMapLastUpdatedAt = labelsMapLastUpdatedAt;
    }
}
