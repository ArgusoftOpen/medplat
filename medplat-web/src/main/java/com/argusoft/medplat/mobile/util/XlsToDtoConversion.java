package com.argusoft.medplat.mobile.util;

import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.mobile.dto.ComponentTagDto;
import com.thoughtworks.xstream.XStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author vaishali
 */
@Service()
public class XlsToDtoConversion {

    public List<ComponentTagDto> xlsConversionMain(String sheetName, String formVersion) {
        String xml = this.generateXML(sheetName, formVersion);
        XStream xStream = new XStream();
        xStream.alias("sewa", SewaTagDto.class);
        xStream.alias("form", FormTagDto.class);
        xStream.alias("component", ComponentTagDto.class);
        xStream.alias("option", OptionTagDto.class);
        xStream.alias("validation", ValidationTagDto.class);
        xStream.alias("formulas", FormulaTagDto.class);
        xStream.useAttributeFor("id", int.class);
        xStream.useAttributeFor("name", String.class);
        xStream.useAttributeFor("key", String.class);
        xStream.useAttributeFor("value", String.class);
        xStream.useAttributeFor("next", String.class);
        xStream.useAttributeFor("subform", String.class);
        xStream.useAttributeFor("formulavalue", String.class);
        xStream.useAttributeFor("method", String.class);
        xStream.useAttributeFor("message", String.class);
        xStream.useAttributeFor("relatedpropertyname", String.class);
        xStream.useAttributeFor("datadump", String.class);
        xStream.useAttributeFor("datadumporder", int.class);
        xStream.useAttributeFor("hint", String.class);
        xStream.useAttributeFor("helpVideoField", String.class);
        xStream.useAttributeFor("listValueFieldValue", String.class);

        xStream.allowTypes(new Class[]{SewaTagDto.class, FormTagDto.class, ComponentTagDto.class,
                OptionTagDto.class, ValidationTagDto.class, FormulaTagDto.class});

        SewaTagDto element = null;
        try {
            xStream.setClassLoader(SewaTagDto.class.getClassLoader());
            element = (SewaTagDto) xStream.fromXML(xml);
            List<ComponentTagDto> components = element.getForm().getComponents();
            ComponentTagDto[] componentTagBeans = new ComponentTagDto[components.size()];

            for (int i = 0; i < components.size(); i++) {
                componentTagBeans[i] = components.get(i);
            }

            components.clear();
            Collections.addAll(components, componentTagBeans);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (element != null && element.getForm() != null) {
            return element.getForm().getComponents();
        } else {
            return Collections.emptyList();
        }
    }

    private String generateXML(String sheetName, String formVersion) {
        try {
            //Initializing the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("sewa");
            document.appendChild(rootElement);
            InputStream input = this.getClass().getResourceAsStream("/xlsFiles/" + formVersion + "/" + sheetName + ".xls");
            HSSFWorkbook workbook = new HSSFWorkbook(input);
            HSSFSheet spreadsheet = workbook.getSheet(sheetName);
            HSSFRow headerRow = spreadsheet.getRow(0);
            StringBuilder xMLDoc = new StringBuilder("<sewa>\n");
            xMLDoc.append("\t<form name = \"").append(convertToXML(sheetName)).append("\">\n\t\t<components>\n");
            for (int rowNo = 1; rowNo <= spreadsheet.getLastRowNum(); rowNo++) {
                HSSFRow row = spreadsheet.getRow(rowNo);
                if (row != null && checkRow(row) && row.getCell(0) != null && row.getCell(0).getNumericCellValue() != 0.0) {
                    xMLDoc.append("\t\t\t<component id=\"").append((int) row.getCell(0).getNumericCellValue()).append("\">\n");
                    for (int cellNo = 0; cellNo < row.getLastCellNum(); cellNo++) {
                        String data = convertToXML(headerRow.getCell(cellNo).getStringCellValue()).toLowerCase();
                        if (data.equalsIgnoreCase("key")) {
                            xMLDoc.append(createOptionsTag(rowNo, cellNo, row, spreadsheet));
                            cellNo = cellNo + 3;
                        } else {
                            if (!data.equalsIgnoreCase("id")) {
                                xMLDoc.append("\t\t\t\t<").append(data).append(">");
                                if (data.equalsIgnoreCase("length") || data.equalsIgnoreCase("page") || data.equalsIgnoreCase("datadumporder")) {
                                    String cellContent;
                                    if (row.getCell(cellNo) != null) {
                                        cellContent = "" + (int) row.getCell(cellNo).getNumericCellValue();
                                    } else {
                                        cellContent = "0";
                                    }
                                    xMLDoc.append(cellContent).append("</").append(data).append(">\n");
                                } else {
                                    if (data.equalsIgnoreCase("mandatorymessage")) {
                                        String cellContent = row.getCell(cellNo) + "";
                                        xMLDoc.append(cellContent.trim()).append("</").append(data).append(">\n");
                                    } else if (data.equalsIgnoreCase("validations")) {
                                        xMLDoc.append(createValidationsTag(cellNo, row));
                                        xMLDoc.append(createValidationsMessageTag(rowNo, cellNo, spreadsheet));
                                        xMLDoc.append("\t\t\t\t</validations>\n");
                                        cellNo++;
                                    } else if (data.equalsIgnoreCase("formulas")) {
                                        xMLDoc.append(createFormulasTag(rowNo, cellNo, row, spreadsheet));
                                    } else {
                                        String cellContent = row.getCell(cellNo) + "";
                                        if (!cellContent.equalsIgnoreCase("null")) {
                                            xMLDoc.append(cellContent.trim()).append("</").append(data).append(">\n");
                                        } else {
                                            xMLDoc.append("</").append(data).append(">\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    xMLDoc.append("\t\t\t\t<row>").append(rowNo).append("</row>\n");
                    xMLDoc.append("\t\t\t</component>\n");
                }
            }
            xMLDoc.append("\t\t</components>\n\t</form>\n");
            xMLDoc.append("</sewa>");
            return xMLDoc.toString();
        } catch (IOException | ParserConfigurationException e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private String convertToXML(String str) {
        return str.replaceAll("[-+.^:, ]", "");
    }

    private String createOptionsTag(int rowNo, int cellNo, HSSFRow row, HSSFSheet spreadsheet) {
        int entryRowNo = rowNo;
        int keyCellNo = cellNo;
        StringBuilder optionsTag = new StringBuilder("\t\t\t\t<options>\n");
        String cellContent;
        if (row.getCell(cellNo) != null) {
            try {
                cellContent = row.getCell(cellNo).getStringCellValue();
            } catch (IllegalStateException e) {
                cellContent = "" + (int) row.getCell(cellNo).getNumericCellValue();
            }
        } else {
            cellContent = "";
        }

        if (cellContent.equalsIgnoreCase("")) {
            rowNo++;
            row = spreadsheet.getRow(rowNo);
        }
        while (row != null && checkRow(row) && (row.getCell(0) == null || row.getCell(0).getNumericCellValue() == 0.0)) {
            try {
                if (row.getCell(cellNo) != null) {
                    cellContent = row.getCell(cellNo).getStringCellValue();
                } else {
                    break;
                }
            } catch (IllegalStateException e) {
                cellContent = "" + (int) row.getCell(cellNo).getNumericCellValue();
            }
            cellNo++;
            if (cellContent.equalsIgnoreCase("")) {
                break;
            }
            optionsTag.append("\t\t\t\t\t<option key=\"").append(cellContent).append("\"");
            try {
                cellContent = row.getCell(cellNo).getStringCellValue();
            } catch (IllegalStateException e) {
                cellContent = "" + (int) row.getCell(cellNo).getNumericCellValue();
            }
            cellNo++;
            optionsTag.append(" value=\"").append(cellContent).append("\"");

            if (row.getCell(cellNo) != null) {
                cellContent = "" + (int) row.getCell(cellNo).getNumericCellValue();
                if (cellContent.equalsIgnoreCase("0")) {
                    cellContent = "";
                }
            } else {
                cellContent = "";
            }
            optionsTag.append(" next=\"").append(cellContent).append("\"");
            cellNo++;
            cellContent = row.getCell(cellNo) + "";
            if (cellContent.trim().startsWith("null")) {
                cellContent = "";
            }
            optionsTag.append(" subform=\"").append(cellContent.trim()).append("\"");
            cellNo++;
            cellContent = row.getCell(cellNo) + "";
            if (cellContent.trim().startsWith("null")) {
                cellContent = "";
            }
            optionsTag.append(" relatedpropertyname=\"").append(cellContent.trim()).append("\"/>\n");
            cellNo = keyCellNo;
            rowNo++;
            row = spreadsheet.getRow(rowNo);
        }
        optionsTag.append("\t\t\t\t</options>\n");
        keyCellNo = keyCellNo + 2;
        row = spreadsheet.getRow(entryRowNo);

        if (row.getCell(keyCellNo) != null) {
            cellContent = "" + (int) row.getCell(keyCellNo).getNumericCellValue();
        } else {
            cellContent = "";
        }

        if (cellContent.equalsIgnoreCase("0")) {
            cellContent = "";
        }
        optionsTag.append("\t\t\t\t<next>").append(cellContent).append("</next>\n");
        keyCellNo++;

        cellContent = row.getCell(keyCellNo) + "";
        if (cellContent.trim().startsWith("null")) {
            cellContent = "";
        }
        optionsTag.append("\t\t\t\t<subform>").append(cellContent.trim()).append("</subform>\n");
        return optionsTag.toString();
    }

    private boolean checkRow(HSSFRow row) {
        for (int cellNo = 0; cellNo < row.getLastCellNum(); cellNo++) {
            HSSFCell cell = row.getCell(cellNo);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return true;
            }
        }
        return false;
    }

    private String createFormulasTag(int rowNo, int cellNo, HSSFRow row, HSSFSheet spreadSheet) {
        StringBuilder formulasTag = new StringBuilder("\n");
        HSSFCell cell = row.getCell(cellNo);
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            String cellContent = row.getCell(cellNo).getStringCellValue();
            String separator = ",";
            String[] contents = checkSeparator(cellContent, separator);
            for (String content : contents) {
                formulasTag.append("\t\t\t\t\t<formulas formulavalue=\"").append(content).append("\"/>\n");
            }
            //for multiple formulas in a sheet
            row = spreadSheet.getRow(++rowNo);
            while (row != null && checkRow(row) && (row.getCell(0) == null || row.getCell(0).getNumericCellValue() == 0.0)) {
                if (row.getCell(cellNo) != null && !row.getCell(cellNo).getStringCellValue().equalsIgnoreCase("")) {
                    formulasTag.append("\t\t\t\t\t<formulas formulavalue=\"").append(row.getCell(cellNo)).append("\"/>\n");
                }
                rowNo++;
                row = spreadSheet.getRow(rowNo);
            }
        }
        formulasTag.append("\t\t\t\t</formulas>\n");
        return formulasTag.toString();
    }

    private String createValidationsTag(int cellNo, HSSFRow row) {
        StringBuilder validationsTag = new StringBuilder("\n");
        HSSFCell cell = row.getCell(cellNo);
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            String cellContent = row.getCell(cellNo).getStringCellValue();
            String separator = ",";
            String[] contents = checkSeparator(cellContent, separator);
            cellNo++;
            if (row.getCell(cellNo) != null) {
                String validationMessage = row.getCell(cellNo).getStringCellValue();
                for (String content : contents) {
                    validationsTag.append("\t\t\t\t\t<validation method=\"").append(content).append("\" message=\"").append(validationMessage).append("\"/>\n");
                }
            } else {
                for (String content : contents) {
                    validationsTag.append("\t\t\t\t\t<validation method=\"").append(content).append("\" message=\"\"/>\n");
                }
            }
        }
        return validationsTag.toString();
    }

    private String[] checkSeparator(String cellContent, String separator) {
        String temp = cellContent;
        int i = 0;
        int index;

        while (temp.contains(separator)) {
            index = temp.indexOf(separator);
            temp = temp.substring(index + 1);
            i++;
        }
        String[] contents = new String[i + 1];
        i = 0;
        while (cellContent.contains(separator)) {
            index = cellContent.indexOf(separator);
            contents[i++] = cellContent.substring(0, index);
            cellContent = cellContent.substring(index + 1);
        }
        contents[i] = cellContent;
        return contents;
    }

    private String createValidationsMessageTag(int rowNo, int cellNo, HSSFSheet spreadsheet) {
        rowNo++;
        HSSFRow row = spreadsheet.getRow(rowNo);
        StringBuilder validationsMessageTag = new StringBuilder();
        while (row != null && checkRow(row) && (row.getCell(0) == null || row.getCell(0).getNumericCellValue() == 0.0)) {
            if (row.getCell(cellNo) != null && !row.getCell(cellNo).getStringCellValue().equalsIgnoreCase("")
                    && row.getCell(cellNo + 1) != null && !row.getCell(cellNo + 1).getStringCellValue().equalsIgnoreCase("")) {
                validationsMessageTag.append("\t\t\t\t\t<validation method=\"").append(row.getCell(cellNo)).append("\" message=\"").append(row.getCell(cellNo + 1)).append("\"/>\n");
            }
            rowNo++;
            row = spreadsheet.getRow(rowNo);
        }
        return validationsMessageTag.toString();
    }
}
