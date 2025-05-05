/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.reportconfig.dto.ReportConfigDto;
import com.argusoft.medplat.reportconfig.dto.ReprotExcelDto;
import com.argusoft.medplat.reportconfig.mapper.ReportMapper;
import com.argusoft.medplat.reportconfig.model.ReportMaster;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;
import com.argusoft.medplat.reportconfig.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * <p>
 * Define APIs for report.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */


@RestController
@RequestMapping("/api/report")
@Tag(name = "Report Controller", description = "")
public class ReportController {

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    TableService tableService;

    @Autowired
    ReportService reportService;

    /**
     * Save dynamic configuration of report.
     * @param reportConfigDto Report configuration dto.
     */
    @PostMapping(value = "/configdynamicdetail")
    public void saveDynamicReportConfiguration(
            @RequestBody ReportConfigDto reportConfigDto){
        if (reportConfigDto.getUuid() == null) {
            reportConfigDto.setUuid(UUID.randomUUID());
        }
        reportService.saveDynamicReport(reportConfigDto, false);
    }

    /**
     * Retrieves dynamic report.
     * @param reportConfigDto Report config details.
     * @param request Instance of HttpServletRequest.
     * @param httpServletResponse Instance of HttpServletResponse.
     * @return Returns report config details.
     */
    @PostMapping(value = "/getdynamicreport")
    public ReportConfigDto getDynamicReport(
            @RequestBody ReportConfigDto reportConfigDto,
            HttpServletRequest request,
            HttpServletResponse httpServletResponse) {
        return reportService.getDynamicReport(reportConfigDto);
    }

    /**
     * Retrieves dynamic report details by report id and report code.
     * @param reportId Report id.
     * @param reportCode Report code.
     * @param fetchQueryOptions Fetch query option.
     * @return Returns dynamic report details.
     */
    @GetMapping(value = "/dynamicdetail")
    public ReportConfigDto getDynamicReportDetailById(
            @RequestParam(value = "id", required = false) Integer reportId,
            @RequestParam(value = "code", required = false) String reportCode,
            @RequestParam(value = "fetchQueryOptions", required = false) Boolean fetchQueryOptions
    ) {
        return reportService.getDynamicReportDetailByIdOrCode(reportId, reportCode, fetchQueryOptions);
    }

    /**
     * Retrieves report details by id.
     * @param reportId Report id.
     * @param fetchQueryOptions Fetch query option.
     * @return Returns report details.
     */
    @GetMapping(value = "/detail")
    public ReportConfigDto getReportDetailById(
            @RequestParam(value = "id") Integer reportId,
            @RequestParam(value = "fetchQueryOptions", required = false) Boolean fetchQueryOptions
    ) {

        ReportMaster reportMaster = reportService.retrieveReportMasterByIdOrCode(reportId, null, true);
        if (reportMaster != null) {
            ReportConfigDto reportConfigDto = ReportMapper.getReportConfigDto(reportMaster);
            if (fetchQueryOptions != null && fetchQueryOptions.equals(Boolean.TRUE) && reportConfigDto != null && !CollectionUtils.isEmpty(reportConfigDto.getParameterDtos())) {
                reportConfigDto.getParameterDtos().stream().filter(reportParameterConfigDto -> (reportParameterConfigDto.getIsQuery() != null && reportParameterConfigDto.getIsQuery().equals(Boolean.TRUE))).forEach(reportParameterConfigDto -> {
                    String query = reportParameterConfigDto.getQuery();
                    query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                    reportParameterConfigDto.setOptionsByQuery(tableService.executeQueryForCombo(query, reportMaster.getReportName()));
                });
            }
            return reportConfigDto;
        } else {
            throw new ImtechoUserException("Report not found", 0);
        }
    }

    /**
     * Retrieves list of records by report name, parent group id, sub group id.
     * @param reportName Report name.
     * @param offset The number of data to skip before starting to fetch details.
     * @param limit The number of data need to fetch.
     * @param parentGroupId Parent group id.
     * @param subGroupId Sub group id.
     * @param menuType Type of menu.
     * @param sortBy Sort by field name.
     * @param sortOn Sort on field name.
     * @param response Instance of HttpServletResponse.
     * @return Returns list of reports details.
     */
    @GetMapping(value = "/getreportlist")
    public List<ReportConfigDto> getReportDetails(
            @RequestParam(value = "reportName", required = false) String reportName,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "parentGroupId", required = false) Integer parentGroupId,
            @RequestParam(value = "subGroupId", required = false) Integer subGroupId,
            @RequestParam(value = "menuType", required = false) String menuType,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOn", required = false) String sortOn,
            HttpServletResponse response
    ) {
        List<ReportMaster> reportMaster = reportService.retrieveAllActiveReportMasters(reportName, offset, limit, Boolean.TRUE, Boolean.TRUE, parentGroupId, subGroupId, user.getId().toString(), menuType, sortBy, sortOn);
        return ReportMapper.getReportMasterDtoList(reportMaster);
    }

    /**
     * Delete report by report id.
     * @param id Report id.
     */
    @DeleteMapping(value = "/deletereport")
    public void getReportDetails(@RequestParam(value = "id") Integer id) {
        reportService.deleteReportById(id);
    }

    /**
     * Execute query.
     * @param query Query string.
     * @return Returns result.
     */
    @PostMapping(value = "/executeQuery")
    public List<Map<String, Object>> executeQuery(@RequestBody String query) {
        return tableService.executeQueryForCombo(query, null);
    }

    /**
     * Retrieves data by query id.
     * @param id Query id.
     * @param parameterMap Map of parameters.
     * @return Returns map of result.
     */
    @PostMapping(value = "/retrievedatabyqueryid/{id}")
    public List<LinkedHashMap<String, Object>> retrieveDataByQueryId(@PathVariable Integer id, @RequestBody Map<String, String> parameterMap) {
        return reportService.retrieveDataByQueryId(id, parameterMap);

    }

    /**
     * Retrieves data by query uuid.
     * @param uuid Query uuid.
     * @param parameterMap Map of parameters.
     * @return Returns map of result.
     */
    @PostMapping(value = "/retrievedatabyqueryuuid/{uuid}")
    public List<LinkedHashMap<String, Object>> retrieveDataByQueryUUID(@PathVariable UUID uuid, @RequestBody Map<String, String> parameterMap) {
        return reportService.retrieveDataByQueryUUID(uuid, parameterMap);
    }

    /**
     * Retrieves combo data by query id.
     * @param id Query id.
     * @param parameterMap Map of parameters.
     * @return Returns map of result.
     */
    @PostMapping(value = "/retrievecombodatabyqueryid/{id}")
    public List<Map<String, Object>> retrieveComboDataByQueryId(@PathVariable Integer id, @RequestBody Map<String, String> parameterMap) {
        return reportService.retrieveComboDataByQueryId(id, parameterMap);

    }

    /**
     * Retrieves combo data by query uuid.
     * @param uuid Query uuid.
     * @param parameterMap Map of parameters.
     * @return Returns map of result.
     */
    @PostMapping(value = "/retrievecombodatabyqueryuuid/{uuid}")
    public List<Map<String, Object>> retrieveComboDataByQueryUUID(@PathVariable UUID uuid, @RequestBody Map<String, String> parameterMap) {
        return reportService.retrieveComboDataByQueryUUID(uuid, parameterMap);
    }

    /**
     * Download excel file.
     * @param id Report id.
     * @param reportExcelDto Report excel details.
     * @return Returns excel file of report.
     * @throws IOException If an I/O error occurs when reading or writing.
     */
    @PostMapping(value = "/downloadexcel/{uuid}", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<byte[]> downLoadExcelForReport(@PathVariable UUID uuid, @RequestBody ReprotExcelDto reportExcelDto) throws IOException {

        ByteArrayOutputStream excelOPStream = reportService.downLoadExcelForReport(null, uuid, reportExcelDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        byte[] data = excelOPStream.toByteArray();
        headers.setContentDispositionFormData("attachment", "test.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);

    }

    @RequestMapping(value = "/downloadpdf/{uuid}", method = {RequestMethod.POST})
    public ResponseEntity<byte[]> downLoadPdfForReport(@PathVariable UUID uuid, @RequestBody ReprotExcelDto reportExcelDto) throws IOException, InterruptedException {
        ByteArrayOutputStream pdfOPStream = reportService.downLoadPdfForReport(null, uuid, reportExcelDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        byte[] data = pdfOPStream.toByteArray();
        headers.setContentDispositionFormData("attachment", "test.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    /**
     * Stores report offline request to database
     *
     * @param id A report id
     * @param reportExcelDto An instance of ReprotExcelDto
     */
    @PostMapping(value = "/offline-report/{id}")
    public void offlineReportRequest(@PathVariable Integer id,
                                     @RequestBody ReprotExcelDto reportExcelDto) {
        reportService.offlineReportRequest(id,reportExcelDto);
    }

    /**
     * Returns a list of ReportOfflineDetails of given user id
     * @param userId A user id
     * @return A list of ReportOfflineDetails
     */
    @GetMapping(value = "/retrieve-offline-reports/{userId}")
    public List<ReportOfflineDetails> retrieveOfflineReportsByUserId(@PathVariable Integer userId){
        return reportService.retrieveOfflineReportsByUserId(userId);
    }

    /**
     * Returns a ReportOfflineDetails of given id
     *
     * @param id A offline report id
     * @return An instance of ReportOfflineDetails
     */
    @GetMapping(value = "/retrieve-offline-report/{id}")
    public ReportOfflineDetails retrieveOfflineReportById(@PathVariable Integer id) {
        return reportService.retrieveOfflineReportById(id);
    }
}