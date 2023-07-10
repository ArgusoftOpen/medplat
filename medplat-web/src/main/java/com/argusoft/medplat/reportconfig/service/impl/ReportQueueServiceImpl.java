package com.argusoft.medplat.reportconfig.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.document.dao.DocumentDao;
import com.argusoft.medplat.document.dao.DocumentModuleMasterDao;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.model.DocumentModuleMaster;
import com.argusoft.medplat.document.service.DocumentService;
import com.argusoft.medplat.reportconfig.dao.ReportOfflineDetailsDao;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;
import com.argusoft.medplat.reportconfig.service.ReportQueueHandlerService;
import com.argusoft.medplat.reportconfig.service.ReportQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

/**
 * <p>
 * Implements methods of ReportQueueService
 * </p>
 *
 * @author sneha
 * @since 11-01-2021 02:16
 */
@Service
public class ReportQueueServiceImpl extends Thread implements ReportQueueService {

    private static final Logger log = LoggerFactory.getLogger(ReportQueueServiceImpl.class);

//    private AppTenantMaster appTenantMaster;
    private static final long SLEEP_TIME = 200L;
    private static final long EVENT_TIME_THRESHOLD = 60 * 1000 / SLEEP_TIME;
    private long loopCounter = -1;

    private final List<ReportOfflineDetails> reportQueue = new LinkedList<>();

    @Autowired
    @Qualifier("reportOfflineTaskExecutor")
    private ThreadPoolTaskExecutor threadPollTaskExecutor;

    @Autowired
    private ReportOfflineDetailsDao reportOfflineDetailsDao;

    @Autowired
    @Lazy
    private ReportQueueHandlerService reportQueueHandlerService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentModuleMasterDao documentModuleMasterDao;

    @Autowired
    private DocumentDao documentDao;

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void startReportThread(AppTenantMaster tenantMaster) {
//        this.appTenantMaster = tenantMaster;
//        this.start();
//    }
    @Override
    public void startReportThread( ) {
//        this.appTenantMaster = tenantMaster;
        this.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void run() {
//        TenantThreadLocal.setTenant(this.appTenantMaster);
        boolean isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(SLEEP_TIME);
                loopCounter++;
            } catch (InterruptedException ex) {
                log.info("Thread Stop ::");
                Thread.currentThread().interrupt();
            }
            try {
                List<ReportOfflineDetails> fetchedReportQueues = null;
                if (loopCounter % EVENT_TIME_THRESHOLD == 0) {
                    long toTime = new Date().getTime() + 60000;
                    int limit = threadPollTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity();
                    fetchedReportQueues = retrieveReportQueue(new Date(toTime), limit);
                    loopCounter = 0;
                }
                List<ReportOfflineDetails> reportQueueToBeHandled = synchronizedReport(fetchedReportQueues);
                markAsProcessed(reportQueueToBeHandled);
            } catch (Exception e) {
                log.error(":: Report Queue Exception ::", e);
            }
        }
//        TenantThreadLocal.clear();
    }

    private List<ReportOfflineDetails> synchronizedReport(List<ReportOfflineDetails> fetchedReportQueues) {
        List<ReportOfflineDetails> reportQueueToBeHandled = new LinkedList<>();
        synchronized (reportQueue) {
            if (fetchedReportQueues != null) {
                reportQueue.addAll(fetchedReportQueues);
            }
            if (!CollectionUtils.isEmpty(reportQueue)) {
                reportQueueToBeHandled.addAll(reportQueue);
                reportQueue.clear();
            }
        }
        return reportQueueToBeHandled;
    }

    /**
     * Returns a list of report queue from given date
     *
     * @param toTime A date time to fetch report
     * @param limit  A limit value
     * @return A list of ReportOfflineDetails
     */
    @Transactional
    public List<ReportOfflineDetails> retrieveReportQueue(Date toTime, Integer limit) {
        List<ReportOfflineDetails> queues = reportOfflineDetailsDao.retrieveOfflineReport(toTime, limit);
        if (!CollectionUtils.isEmpty(queues)) {
            return queues;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Start processing report
     *
     * @param reportQueueToBeHandled A list of ReportOfflineDetails
     */
    @Transactional
    public void markAsProcessed(List<ReportOfflineDetails> reportQueueToBeHandled) {
        for (ReportOfflineDetails reportOfflineDetails : reportQueueToBeHandled) {
            try {
                reportOfflineDetailsDao.markAsProcessed(reportOfflineDetails.getId());
                reportQueueHandlerService.handleReportQueue(reportOfflineDetails);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatusFromProcessedToNewOnServerStartup() {
        reportOfflineDetailsDao.updateStatusFromProcessedToNewOnServerStartup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOldReports(Integer numberOfDays) {
        Date currentDate = new Date();
        Date toTime = new Date(currentDate.getTime() - Duration.ofDays(numberOfDays).toMillis());
        List<ReportOfflineDetails> reportOfflineDetailsList = reportOfflineDetailsDao.retrieveOfflineReportByDate(toTime);
        reportOfflineDetailsList.forEach(reportOfflineDetails -> {
            // Delete report file
            Long fileId = reportOfflineDetails.getFileId();
            if (Objects.nonNull(fileId)) {
                DocumentDto documentDto = documentService.retrieveDocumentById(fileId);
                DocumentModuleMaster documentModuleMaster = documentModuleMasterDao.retrieveById(documentDto.getModuleId());
                try {
                    Files.delete(Path.of(ConstantUtil.REPOSITORY_PATH + File.separator + documentModuleMaster.getBasePath() + File.separator + documentDto.getFileName()));
                    documentDao.deleteById(documentDto.getId());
                } catch (Exception e) {
                   log.error("Exception during removing old reports : {}", e.getMessage());
                }
            }
            // update report status
            reportOfflineDetails.setFileId(null);
            reportOfflineDetails.setState(ReportOfflineDetails.STATE.INACTIVE);
            reportOfflineDetails.setStatus(ReportOfflineDetails.STATUS.ARCHIVED);
            reportOfflineDetailsDao.update(reportOfflineDetails);
        });
    }

}
