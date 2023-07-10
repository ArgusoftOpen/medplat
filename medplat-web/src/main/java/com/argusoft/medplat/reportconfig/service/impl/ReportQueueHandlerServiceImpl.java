package com.argusoft.medplat.reportconfig.service.impl;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.document.dto.DocumentDto;
import com.argusoft.medplat.document.service.DocumentService;
import com.argusoft.medplat.reportconfig.dao.ReportOfflineDetailsDao;
import com.argusoft.medplat.reportconfig.dto.ReprotExcelDto;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;
import com.argusoft.medplat.reportconfig.service.ReportQueueHandlerService;
import com.argusoft.medplat.reportconfig.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * <p>
 * Implements methods of ReportQueueHandlerService
 * </p>
 *
 * @author sneha
 * @since 12-01-2021 03:52
 */
@Service
public class ReportQueueHandlerServiceImpl implements ReportQueueHandlerService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ReportOfflineDetailsDao reportOfflineDetailsDao;

    private static final Logger log = LoggerFactory.getLogger(ReportQueueHandlerServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Async("reportOfflineTaskExecutor")
    @Transactional
    public void handleReportQueue(ReportOfflineDetails reportOfflineDetails) {
        ReprotExcelDto reprotExcelDto = ImtechoUtil.convertObjectFromStringUsingGson(
                reportOfflineDetails.getReportParameters(), ReprotExcelDto.class);
        try {
            log.info("Report processing start for report id {}", reportOfflineDetails.getReportId());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String contentType = "";
            String fileName = "";
            if (reportOfflineDetails.getFileType().equals(ReportOfflineDetails.FILE_TYPE.PDF)) {
                contentType = "application/pdf";
                fileName = reportOfflineDetails.getId().toString() + "_" + new Date().getTime() + ".pdf";
                byteArrayOutputStream = reportService.downLoadPdfForReport(reportOfflineDetails.getReportId(), null, reprotExcelDto);
            } else if (reportOfflineDetails.getFileType().equals(ReportOfflineDetails.FILE_TYPE.EXCEL)) {
                contentType = "application/vnd.ms-excel";
                fileName = reportOfflineDetails.getId().toString() + "_" + new Date().getTime() + ".xlsx";
                byteArrayOutputStream = reportService.downLoadExcelForReport(reportOfflineDetails.getReportId(), null, reprotExcelDto);
            }
            DocumentDto documentDto = documentService.uploadFile(byteArrayOutputStream.toByteArray(),
                    "TECHO_OFFLINE_REPORTS", false,
                    fileName,
                    contentType);
            reportOfflineDetailsDao.markAsReadyForDownload(reportOfflineDetails.getId(), documentDto.getId());
            log.info("Report processed successfully for report id {}", reportOfflineDetails.getReportId());
        } catch (Exception e) {
            reportOfflineDetailsDao.markAsError(reportOfflineDetails.getId(), e.getMessage());
            log.info("Exception during processing offline report of id {} {}", reportOfflineDetails.getReportId(), e.getMessage());
        }
    }
}
