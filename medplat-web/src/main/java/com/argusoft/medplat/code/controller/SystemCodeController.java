package com.argusoft.medplat.code.controller;

import com.argusoft.medplat.code.dto.SystemCodeDto;
import com.argusoft.medplat.code.dto.SystemCodeListDto;
import com.argusoft.medplat.code.mapper.CodeType;
import com.argusoft.medplat.code.mapper.TableType;
import com.argusoft.medplat.code.service.SystemCodeListService;
import com.argusoft.medplat.code.service.SystemCodeService;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.ICDService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Hiren Morzariya
 */
@RestController
@RequestMapping("/api/systemcode")
public class SystemCodeController {

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private SystemCodeListService systemCodeListService;

    @Autowired
    private ICDService icdService;

    @RequestMapping(value = "/icd", method = RequestMethod.GET)
    public String insertJsonDetailsByCodeRange(
            @RequestParam(name="version") String version,
            @RequestParam(name="releaseYear") String releaseYear,
            @RequestParam(name="startingCodeCategory") String startingCodeCategory,
            @RequestParam(name="uptoCodeCategory") String uptoCodeCategory) {
            return icdService.insertJsonDetailsByCodeRange(Integer.parseInt(version), Integer.parseInt(releaseYear), startingCodeCategory, uptoCodeCategory);
    }

    @RequestMapping(value = "/icd/search", method = RequestMethod.GET)
    public List<SystemCodeListDto> getCodeByRefSearch(
            @RequestParam(name="searchNameString") String searchNameString,
            @RequestParam(name="descTypeId", required = false) String descTypeId,
            @RequestParam(name="moduleId", required = false) String moduleId,
            @RequestParam(name="codeType") String codeType) {
            return systemCodeListService.getSystemCodes(searchNameString,descTypeId,moduleId,codeType);
    }

    @RequestMapping(value = "/icd/code", method = RequestMethod.GET)
    public List<SystemCodeListDto> getCodeDetailsByCodeAndType(
            @RequestParam(name="code") String code,
            @RequestParam(name="codeType") String codeType) {
            return systemCodeListService.getSystemCodesByCode(code,codeType);
    }

    @RequestMapping(value = "/uploadcsv", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public void uploadDocument(
            @RequestParam("file") MultipartFile fileParts,
            @RequestParam(name = "codeType", required = true) String codeType) {

        FileOutputStream outputStream = null;

         SystemConfiguration dateFormatSC = systemConfigurationService.retrieveSystemConfigurationByKey("SNOMED_CT_CSV_FILE_DATE_FROMAT");

        try {
            String tempFilePath = System.getProperty("java.io.tmpdir");
            File tempDirectory = new File(tempFilePath);
            boolean isTempDirectoryExists = tempDirectory.exists();
            if (!isTempDirectoryExists) {
                new File(tempFilePath).mkdirs();
            }
            outputStream = new FileOutputStream(new File(tempFilePath, fileParts.getOriginalFilename()));
            outputStream.write(fileParts.getBytes());
            outputStream.close();
            File file = new File(tempFilePath, fileParts.getOriginalFilename());
            fileParts.transferTo(file);

            List<SystemCodeListDto> list = this.validateFile(file.getPath(), codeType.toUpperCase(), dateFormatSC.getKeyValue());
            if (list.isEmpty()) {
                throw new ImtechoSystemException("No Recoed found in file " + file.getPath(), 500);
            }
            systemCodeListService.saveOrUpdate(list);

        } catch (FileNotFoundException ex) {
            throw new ImtechoSystemException("Error while processing file " + fileParts.getName(), 500, ex);
        } catch (CsvValidationException | IOException | ParseException e) {
            throw new ImtechoSystemException("Error while processing file " + fileParts.getName(), 500, e);
        }
    }

    @RequestMapping(value = "/processcsv", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public void processFilePath(
            @RequestParam(name = "filePath", required = true) String filePath,
            @RequestParam(name = "codeType", required = true) String codeType) {
        try {
            SystemConfiguration dateFormat = systemConfigurationService.retrieveSystemConfigurationByKey("SNOMED_CT_CSV_FILE_DATE_FROMAT");

            List<SystemCodeListDto> list = this.validateFile(filePath, codeType, dateFormat.getKeyValue());
            if (list.isEmpty()) {
                throw new ImtechoSystemException("No Recoed found in file " + filePath, 500);
            }
            systemCodeListService.saveOrUpdate(list);
        } catch (CsvValidationException | IOException | ParseException e) {
            e.printStackTrace();
            throw new ImtechoSystemException("Error ehile processing file", 500, e);
        }
    }

    public List<SystemCodeListDto> validateFile(final String filePath, final String codeType, final String dateFormat) throws FileNotFoundException, IOException, CsvValidationException, ParseException {

        List<SystemCodeListDto> dtoList = new ArrayList<>();
        final SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        // skip the header of the csv
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
            // skip the header of the csv
            dtoList = br.lines().skip(1).map((String line) -> {

                String[] record = line.split("\t");// a CSV has comma separated lines
                SystemCodeListDto item = new SystemCodeListDto();
                item.setCodeId(record[0]); //id
                try {
                    item.setEffectiveDate(df.parse(record[1])); // effectiveDate
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                item.setIsActive(record[2].trim().equals("1") ? Boolean.TRUE : Boolean.FALSE);
                item.setPublishedEdition(record[3]);
                item.setCode(record[4]);
                item.setLanguageCode(record[5]);
                item.setDescTypeId(record[6]);
                item.setName(record[7]);
                item.setCodeType(codeType);

                return item;

            }).filter((SystemCodeListDto item) -> {
                return "en".equals(item.getLanguageCode());
                // include only if language code is en
            }).collect(Collectors.toList());
        }
        return dtoList;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void saveOrUpdate(@RequestBody SystemCodeDto dto) {
        if (dto.getCodeType() == null) {
            throw new ImtechoSystemException("codeType is required field", 400);
        }
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ImtechoSystemException("code is required field", 400);
        }
        if (dto.getTableType() == null) {
            throw new ImtechoSystemException("tableType is required field", 400);
        }

        if (dto.getTableId() == null) {
            throw new ImtechoSystemException("tableId is required field", 400);
        }

        systemCodeService.saveOrUpdate(dto);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<SystemCodeDto> getSystemCodesByTypeAndCode(
            @RequestParam(required = false, value = "tableType") TableType tableType,
            @RequestParam(required = false, value = "tableId") String tableId,
            @RequestParam(required = false, value = "codeType") CodeType codeType) {

        if (tableType != null && tableId != null && codeType != null) {
            return systemCodeService.getSystemCodesByTypeAndCode(tableType, tableId, codeType);
        }

        if (tableType != null) {
            return systemCodeService.getSystemCodesByTableType(tableType.name());
        }

        if (codeType != null) {
            return systemCodeService.getSystemCodesByCodeType(codeType.name());
        }

        return systemCodeService.getSystemCodes();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SystemCodeDto getById(@PathVariable("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ImtechoSystemException("Id can not be null", 400);
        }
        return systemCodeService.getById(UUID.fromString(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSystemCode(@PathVariable("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ImtechoSystemException("Id can not be null", 400);
        }
        systemCodeService.deleteSystemCode(UUID.fromString(id));
    }

}
