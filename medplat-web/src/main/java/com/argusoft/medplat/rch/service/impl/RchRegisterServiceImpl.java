/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.internationalization.service.InternationalizationService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.query.service.ReportQueryMasterService;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.service.RchRegisterService;
import com.argusoft.medplat.reportconfig.service.ReportService;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.HtmlTagWorker;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.styledxmlparser.node.IElementNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 * Define services for rch register.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
@Transactional
@Service
public class RchRegisterServiceImpl implements RchRegisterService {

    @Autowired
    TableService tableService;

    @Autowired
    ReportQueryMasterService reportQueryService;

    @Autowired
    QueryMasterService queryMasterService;

    @Autowired
    ReportService reportService;

    @Autowired
    LocationMasterDao locationMasterDao;

    @Autowired
    UserDao userDao;

    @Autowired
    InternationalizationService internationalizationService;

    /**
     * Define footer for pdf.
     */
    protected static class FooterLogos {
        Map<String, Paragraph> logosMap;
        Image gogImage;
        Image nhmImage;
        Image techoImage;

        FooterLogos() {
            String imageDirectoryPath = ConstantUtil.REPOSITORY_PATH + ConstantUtil.IMPLEMENTATION_TYPE + File.separator + "images" + File.separator;
            String gogImgPath = imageDirectoryPath + "govt_logo.png";
            String nhmImgPath = imageDirectoryPath + "nhm_logo.png";
            String techoImgPath = imageDirectoryPath + "web_logo.png";
            Paragraph gogImgPara = new Paragraph().setFontSize(10);
            Paragraph nhmImgPara = new Paragraph().setFontSize(10);
            Paragraph techoImgPara = new Paragraph().setFontSize(10);
            try {
                gogImage = new Image(ImageDataFactory.create(gogImgPath));
                gogImage.scaleToFit(12, 24);

                nhmImage = new Image(ImageDataFactory.create(nhmImgPath));
                nhmImage.scaleToFit(30, 30);

                techoImage = new Image(ImageDataFactory.create(techoImgPath));
                techoImage.scaleToFit(24, 30);

                gogImgPara.add(gogImage);
                nhmImgPara.add(nhmImage);
                techoImgPara.add(techoImage);

                logosMap = new HashMap<>();
                logosMap.put("gog", gogImgPara);
                logosMap.put("nhm", nhmImgPara);
                logosMap.put("techo", techoImgPara);
            } catch (MalformedURLException e) {
                Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }

        public Paragraph getLogoByName(String logoName) {
            return logosMap.get(logoName);
        }
    }

    // page X of Y
    protected class PageXofY implements IEventHandler {

        protected com.itextpdf.kernel.pdf.xobject.PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 30;
        protected float y = 25;
        protected float space = 4.5f;
        protected float descent = 3;
        protected String loggedInUserName;
        FooterLogos fl = new FooterLogos();

        public PageXofY(String loggedInUserName) {
            placeholder = new com.itextpdf.kernel.pdf.xobject.PdfFormXObject(
                    new Rectangle(0, 0, side, side));
            this.loggedInUserName = loggedInUserName;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
            int pageNumber = pdf.getPageNumber(page);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);

            canvas.showTextAligned(fl.getLogoByName("gog"), 493, 808, TextAlignment.LEFT);
            canvas.showTextAligned(fl.getLogoByName("nhm"), 510, 809, TextAlignment.LEFT);
            canvas.showTextAligned(fl.getLogoByName("techo"), 543, 810, TextAlignment.LEFT);

            Paragraph p = new Paragraph().setFontSize(10).add("Generated by ").add(loggedInUserName)
                    .add(" on ")
                    .add(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
            canvas.showTextAligned(p, 270, 10, TextAlignment.RIGHT);

            Paragraph pageNo = new Paragraph().setFontSize(10).add("Page ").add(String.valueOf(pageNumber));
            canvas.showTextAligned(pageNo, 510, 10, TextAlignment.LEFT);

            pdfCanvas.release();
        }
    }

    protected class CustomTagWorkerFactory extends DefaultTagWorkerFactory {

        @Override
        public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
            if (TagConstants.HTML.equals(tag.name())) {
                return new ZeroMarginHtmlTagWorker(tag, context);
            }
            return null;
        }
    }

    protected class ZeroMarginHtmlTagWorker extends HtmlTagWorker {

        public ZeroMarginHtmlTagWorker(IElementNode element, ProcessorContext context) {
            super(element, context);
            com.itextpdf.layout.Document doc = (com.itextpdf.layout.Document) getElementResult();
            doc.setMargins(37, 30, 13, 70);
        }
    }

    private ByteArrayOutputStream generatePDF(List<LinkedHashMap<String, Object>> dataList, Integer locationId, String type, Object fromDate,
                                              Object toDate, String userName, String languageCode)
            throws IOException, InterruptedException, ParseException {

        StringBuilder sb = new StringBuilder(
                "<!DOCTYPE html> <html>  <head>  <meta charset=\"UTF-8\">  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no\">"
                        + "<style> "
                        // ncd-print.css
                        + "body {"
                        + "    padding: 0;"
                        + "    margin: 0;"
                        + "}"
                        + " "
                        + ".main-title {"
                        + "    text-align: center;"
                        + "    width: 100%;"
                        + "}"
                        + " "
                        + ".w-50 {"
                        + "    width: 49%;"
                        + "    display: inline-block;"
                        + "    vertical-align: top;"
                        + "}"
                        + " "
                        + ".w-33 {"
                        + "    width: 33%;"
                        + "    display: inline-block;"
                        + "    vertical-align: top;"
                        + "}"
                        + " "
                        + ".w-100 {"
                        + "    width: 100%;"
                        + "    border-top: 1px solid #ebebeb;"
                        + "    margin-top: 10px;"
                        + "}"
                        + " "
                        + ".mb-15 {"
                        + "    margin-bottom: 15px;"
                        + "}"
                        + " "
                        + ".mt-15 {"
                        + "    margin-top: 15px;"
                        + "}"
                        + " "
                        + ".br-1 {"
                        + "    border-right: 1px solid #ebebeb;"
                        + "}"
                        + " "
                        + "table {"
                        + "    display: table;"
                        + "    width: 100%;"
                        + "    border-collapse: collapse;"
                        + "}"
                        + " "
                        + "table tr th {"
                        + "    background: #ebebeb;"
                        + "}"
                        + " "
                        + "table tr td,"
                        + "table tr th {"
                        + "    border: 1px solid #ebebeb;"
                        + "    padding: 5px;"
                        + "    text-align: left;"
                        + "}"
                        + " "
                        + " "
                        + "/* @media print{@page {size: landscape}} */"
                        + " "
                        + " "
                        + "/*Bootstrap grid system modified for print / Report */"
                        + " "
                        + "*,"
                        + "*::before,"
                        + "*::after {"
                        + "    box-sizing: border-box;"
                        + "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";"
                        + "}"
                        + " "
                        + "html {"
                        + "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";"
                        + "    line-height: 1.15;"
                        + "    -webkit-text-size-adjust: 100%;"
                        + "    -ms-text-size-adjust: 100%;"
                        + "    -ms-overflow-style: scrollbar;"
                        + "    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);"
                        + "}"
                        + " "
                        + "body {"
                        + "    margin: 0;"
                        + "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";"
                        + "    font-size: 1rem;"
                        + "    font-weight: 400;"
                        + "    line-height: 1.5;"
                        + "    color: #212529;"
                        + "    text-align: left;"
                        + "    background-color: #fff;"
                        + "    box-sizing: border-box;"
                        + "}"
                        + " "
                        + "h1,"
                        + "h2,"
                        + "h3,"
                        + "h4,"
                        + "h5,"
                        + "h6, .cust-header {"
                        + "    margin-top: 0;"
                        + "    margin-bottom: 0.5rem;"
                        + "}"
                        + " "
                        + "p {"
                        + "    margin-top: 0;"
                        + "    margin-bottom: 1rem;"
                        + "}"
                        + " "
                        + "  .cust-header { font-weight : bold !important; }"
                        + ".row {"
                        + "    /* display: -ms-flexbox; display: flex; -ms-flex-wrap: wrap; flex-wrap: wrap; */"
                        + "    margin-right: -15px;"
                        + "    margin-left: -15px;"
                        + "}"
                        + " "
                        + ".index-title {"
                        + "    text-align: center;"
                        + "}"
                        + " "
                        + ".col-1,"
                        + ".col-2,"
                        + ".col-3,"
                        + ".col-4,"
                        + ".col-5,"
                        + ".col-6,"
                        + ".col-7,"
                        + ".col-8,"
                        + ".col-9,"
                        + ".col-10,"
                        + ".col-11,"
                        + ".col-12,"
                        + ".col,"
                        + ".col-auto,"
                        + ".col-sm-1,"
                        + ".col-sm-2,"
                        + ".col-sm-3,"
                        + ".col-sm-4,"
                        + ".col-sm-5,"
                        + ".col-sm-6,"
                        + ".col-sm-7,"
                        + ".col-sm-8,"
                        + ".col-sm-9,"
                        + ".col-sm-10,"
                        + ".col-sm-11,"
                        + ".col-sm-12,"
                        + ".col-sm,"
                        + ".col-sm-auto,"
                        + ".col-md-1,"
                        + ".col-md-2,"
                        + ".col-md-3,"
                        + ".col-md-4,"
                        + ".col-md-5,"
                        + ".col-md-6,"
                        + ".col-md-7,"
                        + ".col-md-8,"
                        + ".col-md-9,"
                        + ".col-md-10,"
                        + ".col-md-11,"
                        + ".col-md-12,"
                        + ".col-md,"
                        + ".col-md-auto,"
                        + ".col-lg-1,"
                        + ".col-lg-2,"
                        + ".col-lg-3,"
                        + ".col-lg-4,"
                        + ".col-lg-5,"
                        + ".col-lg-6,"
                        + ".col-lg-7,"
                        + ".col-lg-8,"
                        + ".col-lg-9,"
                        + ".col-lg-10,"
                        + ".col-lg-11,"
                        + ".col-lg-12,"
                        + ".col-lg,"
                        + ".col-lg-auto,"
                        + ".col-xl-1,"
                        + ".col-xl-2,"
                        + ".col-xl-3,"
                        + ".col-xl-4,"
                        + ".col-xl-5,"
                        + ".col-xl-6,"
                        + ".col-xl-7,"
                        + ".col-xl-8,"
                        + ".col-xl-9,"
                        + ".col-xl-10,"
                        + ".col-xl-11,"
                        + ".col-xl-12,"
                        + ".col-xl,"
                        + ".col-xl-auto {"
                        + "    position: relative;"
                        + "    width: 100%;"
                        + "    min-height: 1px;"
                        + "    padding-right: 7px;"
                        + "    padding-left: 7px;"
                        + "    float: left;"
                        + "    box-sizing: border-box;"
                        + "}"
                        + " "
                        + ".col {"
                        + "    max-width: 100%;"
                        + "}"
                        + " "
                        + ".col-1 {"
                        + "    max-width: 8.333333%;"
                        + "}"
                        + " "
                        + ".col-2 {"
                        + "    max-width: 16.666667%;"
                        + "}"
                        + " "
                        + ".col-3 {"
                        + "    max-width: 25%;"
                        + "}"
                        + " "
                        + ".col-4 {"
                        + "    max-width: 33.333333%;"
                        + "}"
                        + " "
                        + ".col-5 {"
                        + "    max-width: 41.666667%;"
                        + "}"
                        + " "
                        + ".col-6 {"
                        + "    max-width: 50%;"
                        + "}"
                        + " "
                        + ".col-7 {"
                        + "    max-width: 58.333333%;"
                        + "}"
                        + " "
                        + ".col-8 {"
                        + "    max-width: 66.666667%;"
                        + "}"
                        + " "
                        + ".col-9 {"
                        + "    max-width: 75%;"
                        + "}"
                        + " "
                        + ".col-10 {"
                        + "    max-width: 83.333333%;"
                        + "}"
                        + " "
                        + ".col-11 {"
                        + "    max-width: 91.666667%;"
                        + "}"
                        + " "
                        + ".col-12 {"
                        + "    max-width: 100%;"
                        + "}"
                        + " "
                        + ".clearfix {"
                        + "    display: block;"
                        + "    clear: both;"
                        + "    content: \"\";"
                        + "}"
                        + " "
                        + ".cst-hr,"
                        + ".cst-hr .light {"
                        + "    display: block;"
                        + "    width: 100%;"
                        + "    clear: both;"
                        + "}"
                        + " "
                        + ".cst-hr:after,"
                        + ".cst-hr .light:after {"
                        + "    display: block;"
                        + "    clear: both;"
                        + "    content: \"\";"
                        + "    width: 100%;"
                        + "}"
                        + " "
                        + ".print-page {"
                        + "    background: #ffffff;"
                        + "    -webkit-print-color-adjust: exact;"
                        + "    font-size: 11px;"
                        + "    line-height: 20px;"
                        + "}"
                        + " "
                        + ".print-wrapper {"
                        + "    padding: 10px;"
                        + "}"
                        + " "
                        + ".main-title {"
                        + "    font-size: 32px;"
                        + "    padding: 100px 0;"
                        + "    text-align: center;"
                        + "}"
                        + " "
                        + ".sub-title {"
                        + "    font-size: 14px;"
                        + "    margin: 0;"
                        + "}"
                        + " "
                        + ".sub-title.small {"
                        + "    font-size: 12px;"
                        + "    margin: 0;"
                        + "}"
                        + " "
                        + ".sub-title.border-bottom {"
                        + "    display: block;"
                        + "    margin-bottom: 10px;"
                        + "}"
                        + " "
                        + ".box-header {"
                        + "    background: #ebebeb !important;"
                        + "    padding: 5px 10px;"
                        + "    border: 1px solid #ebebeb;"
                        + "    -webkit-print-color-adjust: exact;"
                        + "}"
                        + " "
                        + ".box-body {"
                        + "    border: 1px solid #ebebeb;"
                        + "    border-top: 0;"
                        + "    padding: 10px;"
                        + "}"
                        + " "
                        + ".info ul {"
                        + "    display: table;"
                        + "    width: 100%;"
                        + "    box-sizing: border-box;"
                        + "}"
                        + " "
                        + ".info ul li {"
                        + "    display: table-row;"
                        + "    width: 100%;"
                        + "    float: none;"
                        + "    clear: both;"
                        + "}"
                        + " "
                        + ".info label {"
                        + "    font-weight: 500;"
                        + "    display: block;"
                        + "    margin-bottom: 5px;"
                        + "}"
                        + " "
                        + ".info label span.info_type {"
                        + "    font-size: 12px;"
                        + "    width: 150px;"
                        + "    display: inline-block;"
                        + "    vertical-align: top;"
                        + "}"
                        + " "
                        + ".info label span.data {"
                        + "    font-weight: normal;"
                        + "    width: 150px;"
                        + "    display: inline-block;"
                        + "    vertical-align: top;"
                        + "}"
                        + " "
                        + ".nopadding {"
                        + "    padding: 0px;"
                        + "    margin: 0;"
                        + "}"
                        + " "
                        + ".info ul {"
                        + "    list-style-type: none;"
                        + "}"
                        + " "
                        + ".info ul .data {"
                        + "    font-size: 12px;"
                        + "}"
                        + " "
                        + ".info ul .data.block {"
                        + "    font-weight: bold !important;"
                        + "    width: 150px;"
                        + "    display: block;"
                        + "}"
                        + " "
                        + ".row-wrapper {"
                        + "    display: flex;"
                        + "    flex-wrap: wrap;"
                        + "}"
                        + " "
                        + ".row-wrapper .w-33 {}"
                        + " "
                        + ".row-wrapper .w-50 {}"
                        + " "
                        + "tbody {"
                        + "    background-color: white;"
                        + "}"
                        + " "
                        + ".cst-hr {"
                        + "    box-shadow: none;"
                        + "    outline: none;"
                        + "    border: 0;"
                        + "    border-bottom: 1px solid #e4e4e4;"
                        + "    padding: 10px;"
                        + "}"
                        + " "
                        + ".cst-hr.light {"
                        + "    border-bottom: 1px solid #ebebeb;"
                        + "}"
                        + " "
                        + "@media print {"
                        + "    .cst-table table th {"
                        + "        background-color: #ebebeb !important;"
                        + "    }"
                        + "}"
                        + " "
                        + ".info label span.info_type {"
                        + "    display: block;"
                        + "    width: 50%;"
                        + "    float: left;"
                        // + " padding-right: 2%;"
                        + "}"
                        + " "
                        + ".info label span.data {"
                        + "    display: block;"
                        + "    width: 48%;"
                        + "    float: left;"
                        + "font-weight: bold !important;"
                        + "}"
                        + " "
                        + ".searchparam_style {"
                        + " font-size : 25px;"
                        + " padding-right: 10px;"
                        + " padding-left: 10px;"
                        + " text-align: center;"
                        + "}"
                        + " "
                        + ".pl-10 { "
                        + "padding-left: 10px;"
                        + "}"
                        + "@media print {"
                        + "    @page {"
                        + "        size: landscape"
                        + "    }"
                        + "}"
                        + " .fixed-footer{"
                        + "        bottom: 0;"
                        + "    }"
                        + "    .container{"
                        + "        width: 80%;"
                        + "        margin: 0 auto; /* Center the DIV horizontally */"
                        + "    }"
                        + " .h1-clone {"
                        + "    font-size: 14px;"
                        + "    font-weight: bold;} "
                        + " .h1-display { display: inline-block;}</style> </head>   <body> ");

        if (RchConstants.RCH_MOTHER_SERVICE.equalsIgnoreCase(type)) {
            int g = 0;
            for (LinkedHashMap<String, Object> data : dataList) {
                g++;
                sb.append("<div class=\"box mb-15\">  <div class=\"clearfix\"></div> <div class=\"box-header\"> <div class=\"sub-title h1-clone\">"
                        + internationalizationService.getLabelByKeyAndLanguageCode("Name_web",
                        languageCode)
                        + " : ");
                sb.append("<h1 class=\"sub-title h1-display\" style=\"width: 90%;\"><span style=\"opacity: 0; width: 0; display: inline-block;\">" + g + ") </span>" + data.get("member_name")).append(data.get("unique_health_id") != null ? ", "
                        + internationalizationService.getLabelByKeyAndLanguageCode(
                        "TeCHOID_web", languageCode)
                        + " : " + data.get("unique_health_id") : "");
                sb.append("</h1></div></div> <div class=\"box-body col-12\"> <div class=\"row\"> <div class=\"sub-title, cust-header  pl-10\">"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "GENERALINFORMATION_web", languageCode)
                                + "</div><div class=\"col-6\"><table class=\"sub-title\"><tbody>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("TeCHOID_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.UNIQUE_HEALTH_ID) != null
                                ? data.get(RchConstants.UNIQUE_HEALTH_ID)
                                : "N/A")
                        .append("</td></tr>");
                sb.append("<tr><td>" + internationalizationService.getLabelByKeyAndLanguageCode(
                                "ANCRegistrationDate_web", languageCode) + ":</td><td>")
                        .append(data.get(RchConstants.REG_SERVICE_DATE) != null
                                ? data.get(RchConstants.REG_SERVICE_DATE)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("FamilyId_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("family_id")).append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("PregnantWomanName_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEMBER_NAME) != null
                                ? data.get(RchConstants.MEMBER_NAME)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("HusbandName_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.HUSBAND_NAME) != null
                                ? data.get(RchConstants.HUSBAND_NAME)
                                : "N/A")
                        .append("</td> </tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("P.W.Age(D.O.B.)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.AGE_DURING_DELIVERY) != null
                                ? data.get(RchConstants.AGE_DURING_DELIVERY)
                                : "N/A")
                        .append(" years ")
                        .append(data.get(RchConstants.BIRTH_DATE) != null
                                ? "(" + data.get(RchConstants.BIRTH_DATE) + ")"
                                : "")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("BloodGroupofP.W._web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BLOOD_GROUP) != null
                                ? data.get(RchConstants.BLOOD_GROUP)
                                : "N/A")
                        .append("</td> </tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Address_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ADDRESS) != null
                                ? data.get(RchConstants.ADDRESS)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("MobileNumber_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MOBILE_NUMBER) != null
                                ? data.get(RchConstants.MOBILE_NUMBER)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Religion/Caste_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.RELIGION) != null
                                ? data.get(RchConstants.RELIGION)
                                : "N/A")
                        .append(data.get(RchConstants.CASTE) != null
                                ? "/" + data.get(RchConstants.CASTE)
                                : "")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("BPL/APL_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BPL_FLAG) != null
                                ? data.get(RchConstants.BPL_FLAG)
                                : "N/A")
                        .append("</td></tr> </tbody> </table> </div>")
                        .append("<div class=\"col-6\"><table class=\"sub-title\"><tbody>")
                        // .append("</td> </tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("LMP_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.LMP_DATE) != null
                                ? data.get(RchConstants.LMP_DATE)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "PregnancyWeekNumberattheTimeofRegistration_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PREGNANCY_WEEK_NUMBER) != null
                                ? data.get(RchConstants.PREGNANCY_WEEK_NUMBER)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Registeredwithin12WeeksofPregnancy_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.TWELVE_PREGNANCY_WEEK_NUMBER) != null
                                ? data.get(RchConstants.TWELVE_PREGNANCY_WEEK_NUMBER)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "ExpectedDeliveryDate_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.EDD) != null ? data.get(RchConstants.EDD)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "ExpectedPlaceofDelivery_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.EXPECTED_DELIVERY_PLACE) != null
                                ? data.get(RchConstants.EXPECTED_DELIVERY_PLACE)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "PreviousComplication_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PREVIOUS_COMPLICATION) != null
                                ? data.get(RchConstants.PREVIOUS_COMPLICATION)
                                : "N/A")
                        .append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("JSYBeneficiary_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("jsy_beneficiary")).append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("KPSYBeneficiary_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("kpsy_beneficiary")).append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("IAYBeneficiary_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("iay_beneficiary")).append("</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("MarriageYear_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("year_of_wedding"))
                        .append("</td></tr></tbody></table></div></div>")
                        .append("<div class=\"row\"><div class=\"col-6\"><table class=\"sub-title\"><thead><tr><td colspan=\"2\"><U>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "PreviousPregnancyDetails_web",
                                        languageCode)
                                + "</U>");
                List<LinkedHashMap<String, Object>> previousPregnancyDetailsJsonList = this
                        .extractPreviousPregnancyDetailsJsonListFromResult(data);

                sb.append("</td></tr></thead><tbody>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("TotalPregnancy_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(!previousPregnancyDetailsJsonList.isEmpty()
                                ? previousPregnancyDetailsJsonList.size()
                                : "No Data Available");

                if (!previousPregnancyDetailsJsonList.isEmpty()) {
                    for (LinkedHashMap<String, Object> previousPregnancyDetailsJson : previousPregnancyDetailsJsonList) {
                        sb.append("</td></tr><tr><td>" + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Complicationin"
                                                        + RchConstants.NUBER_TO_STRING_FORMATE_VALUE.get(previousPregnancyDetailsJsonList.indexOf(previousPregnancyDetailsJson) + 1)
                                                        + "Pregnancy_web", languageCode)
                                        + ":</td><td>")
                                .append(previousPregnancyDetailsJson.get(
                                        RchConstants.PREG_COMPLICATION) != null
                                        ? previousPregnancyDetailsJson
                                        .get(RchConstants.PREG_COMPLICATION)
                                        : "N/A")
                                .append("</td></tr><tr><td>"
                                        + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Resultof"
                                                        + RchConstants.NUBER_TO_STRING_FORMATE_VALUE.get(previousPregnancyDetailsJsonList.indexOf(previousPregnancyDetailsJson) + 1)
                                                        + "Pregnancy_web",
                                                languageCode)
                                        + ":</td><td>")
                                .append(previousPregnancyDetailsJson
                                        .get(RchConstants.PREG_GENDER) != null
                                        ? previousPregnancyDetailsJson
                                        .get(RchConstants.PREG_GENDER)
                                        : "N/A");
                    }
                }

                sb.append("</td></tr></tbody></table></div></div>");
                String preDeliveryCareString = (String) data.get("pre_delivery_care_json");
                List<LinkedHashMap<String, Object>> preDeliveryCareJsonList = new ArrayList<>();
                if (preDeliveryCareString != null) {
                    List fromJson = new Gson().fromJson(preDeliveryCareString, List.class);
                    List<String> stringJsonList = new ArrayList<>();
                    fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));
                    stringJsonList.forEach(stringJson ->
                            preDeliveryCareJsonList.add(new Gson().fromJson(stringJson,
                                    new TypeToken<LinkedHashMap<String, Object>>() {

                                    }.getType()))
                    );
                }

                // ANC Visit
                if (!CollectionUtils.isEmpty(preDeliveryCareJsonList)) {
                    sb.append("<hr class=\"cst-hr\">")
                            .append("<div class=\"cust-header sub-title border-bottom\">")
                            .append(internationalizationService.getLabelByKeyAndLanguageCode("ANCVisit_web", languageCode))
                            .append("</div>");

                    LinkedHashMap<String, Object> firstMap;
                    LinkedHashMap<String, Object> secondMap;
                    LinkedHashMap<String, Object> thirdMap;

                    String lastHbgmValue = "";
                    String appendArrow;

                    for (int i = 0; i < preDeliveryCareJsonList.size(); i += 3) {
                        firstMap = preDeliveryCareJsonList.get(i);

                        if (preDeliveryCareJsonList.size() > (i + 2)) {
                            secondMap = preDeliveryCareJsonList.get(i + 1);
                            thirdMap = preDeliveryCareJsonList.get(i + 2);
                        } else if (preDeliveryCareJsonList.size() > (i + 1)) {
                            secondMap = preDeliveryCareJsonList.get(i + 1);
                            thirdMap = null;
                        } else {
                            secondMap = null;
                            thirdMap = null;
                        }

                        if (i != 0) {
                            sb.append("<hr class=\"cst-hr\">");
                        }

                        sb.append("<div class=\"row\">").append("<div class=\"col-12\">")
                                .append("<table class=\"sub-title\">").append("<tbody>")
                                .append("<tr>").append("<td>")
                                .append("<div class=\"cust-header sub-title\">")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "ExaminationDetails_web",
                                                languageCode)
                                        + ":")
                                .append("</div>").append("</td>").append("<td>")
                                .append("<div class=\"cust-header sub-title\">")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Visit_web",
                                                languageCode)
                                        + " - " + (i + 1))
                                .append("</div>").append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append("<div class=\"cust-header sub-title\">")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "Visit_web",
                                                    languageCode)
                                            + " - " + (i + 2))
                                    .append("</div>").append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append("<div class=\"cust-header sub-title\">")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "Visit_web",
                                                    languageCode)
                                            + " - " + (i + 3))
                                    .append("</div>").append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Date_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.INSPECTION_DATE) != null
                                        ? firstMap.get(RchConstants.INSPECTION_DATE)
                                        : "N/A")
                                .append(" (" + Double.valueOf(firstMap
                                        .get(RchConstants.PREGNANCY_WEEK)
                                        .toString()).intValue() + " weeks)")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.INSPECTION_DATE) != null
                                            ? secondMap.get(RchConstants.INSPECTION_DATE)
                                            : "N/A")
                                    .append(" (" + Double.valueOf(secondMap.get(
                                                    RchConstants.PREGNANCY_WEEK)
                                            .toString()).intValue()
                                            + " weeks)")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.INSPECTION_DATE) != null ? thirdMap
                                            .get(RchConstants.INSPECTION_DATE)
                                            : "N/A")
                                    .append(" (" + Double.valueOf(thirdMap.get(
                                                    RchConstants.PREGNANCY_WEEK)
                                            .toString()).intValue()
                                            + " weeks)")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "PlaceName_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.INSPECTION_PLACE) != null
                                        ? firstMap.get(RchConstants.INSPECTION_PLACE)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.INSPECTION_PLACE) != null
                                            ? secondMap.get(RchConstants.INSPECTION_PLACE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.INSPECTION_PLACE) != null
                                            ? thirdMap.get(RchConstants.INSPECTION_PLACE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "PlaceType_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.INSPECTION_PLACE_TYPE) != null
                                        ? firstMap.get(RchConstants.INSPECTION_PLACE_TYPE)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.INSPECTION_PLACE_TYPE) != null
                                            ? secondMap.get(RchConstants.INSPECTION_PLACE_TYPE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.INSPECTION_PLACE_TYPE) != null
                                            ? thirdMap.get(RchConstants.INSPECTION_PLACE_TYPE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "PregnancyWeek_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.PREGNANCY_WEEK) != null
                                        ? Double.valueOf(
                                                firstMap.get(RchConstants.PREGNANCY_WEEK)
                                                        .toString())
                                        .intValue()
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.PREGNANCY_WEEK) != null
                                            ? Double.valueOf(secondMap.get(
                                                            RchConstants.PREGNANCY_WEEK)
                                                    .toString())
                                            .intValue()
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.PREGNANCY_WEEK) != null
                                            ? Double.valueOf(thirdMap.get(
                                                            RchConstants.PREGNANCY_WEEK)
                                                    .toString())
                                            .intValue()
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "P.W.Weight_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.PREG_WOMEN_WEIGHT) != null
                                        ? firstMap.get(RchConstants.PREG_WOMEN_WEIGHT)
                                        + " Kgs"
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.PREG_WOMEN_WEIGHT) != null
                                            ? secondMap.get(RchConstants.PREG_WOMEN_WEIGHT)
                                            + " Kgs"
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.PREG_WOMEN_WEIGHT) != null
                                            ? thirdMap.get(RchConstants.PREG_WOMEN_WEIGHT)
                                            + " Kgs"
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Previouspregnancycomplication_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        "previous_pregnancy_complication") != null
                                        ? firstMap.get("previous_pregnancy_complication")
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get("previous_pregnancy_complication") != null
                                            ? secondMap.get("previous_pregnancy_complication")
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get("previous_pregnancy_complication") != null
                                            ? thirdMap.get("previous_pregnancy_complication")
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "HIVTest_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.HIV_TEST) != null
                                        ? firstMap.get(RchConstants.HIV_TEST)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.HIV_TEST) != null ? secondMap
                                            .get(RchConstants.HIV_TEST)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.HIV_TEST) != null ? thirdMap
                                            .get(RchConstants.HIV_TEST)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "VDRLTest_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.VDRL_TEST) != null
                                        ? firstMap.get(RchConstants.VDRL_TEST)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.VDRL_TEST) != null ? secondMap
                                            .get(RchConstants.VDRL_TEST)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.VDRL_TEST) != null ? thirdMap
                                            .get(RchConstants.VDRL_TEST)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "IsCorticoSteroidgiven_web",
                                                languageCode)
                                        + "?:")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("is_cortico_steroid_given") != null
                                        ? firstMap.get("is_cortico_steroid_given")
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            "is_cortico_steroid_given") != null ? secondMap
                                            .get("is_cortico_steroid_given")
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            "is_cortico_steroid_given") != null ? thirdMap
                                            .get("is_cortico_steroid_given")
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "SystolicBP_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.SYSTOLIC_BP) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.SYSTOLIC_BP))
                                        ? firstMap.get(RchConstants.SYSTOLIC_BP)
                                        + " mmHg"
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.SYSTOLIC_BP) != null
                                            && !("N/A").equals(secondMap
                                            .get(RchConstants.SYSTOLIC_BP))
                                            ? secondMap.get(RchConstants.SYSTOLIC_BP)
                                            + " mmHg"
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.SYSTOLIC_BP) != null
                                            && !("N/A").equals(thirdMap
                                            .get(RchConstants.SYSTOLIC_BP))
                                            ? thirdMap.get(RchConstants.SYSTOLIC_BP)
                                            + " mmHg"
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "DiastolicBP_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.DIASTOLIC_BP) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.DIASTOLIC_BP))
                                        ? firstMap.get(RchConstants.DIASTOLIC_BP)
                                        + " mmHg"
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.DIASTOLIC_BP) != null
                                            && !("N/A").equals(secondMap
                                            .get(RchConstants.DIASTOLIC_BP))
                                            ? secondMap.get(RchConstants.DIASTOLIC_BP)
                                            + " mmHg"
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.DIASTOLIC_BP) != null
                                            && !("N/A").equals(thirdMap
                                            .get(RchConstants.DIASTOLIC_BP))
                                            ? thirdMap.get(RchConstants.DIASTOLIC_BP)
                                            + " mmHg"
                                            : "N/A")
                                    .append("</td>");
                        }
                        // Haemoglobin Count => increase/decrease logic
                        if (!lastHbgmValue.isEmpty() && firstMap
                                .get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                            if (Double.valueOf(lastHbgmValue).equals(Double.valueOf(
                                    firstMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                            .toString()))) {
                                appendArrow = "";
                            } else if (Double.parseDouble(lastHbgmValue) > Double.parseDouble(
                                    firstMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                            .toString())) {
                                appendArrow = " <span>&#8595;</span>";
                            } else {
                                appendArrow = " <span>&#8593;</span>";
                            }
                        } else {
                            appendArrow = "";
                        }
                        if (firstMap.get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                            lastHbgmValue = firstMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                    .toString();
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Hbgm_web",
                                                languageCode)
                                        + "%:")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.HAEMOGLOBIN_COUNT) != null
                                        && !("N/A").equals(firstMap.get(
                                                RchConstants.HAEMOGLOBIN_COUNT)
                                        .toString()) ? firstMap
                                        .get(RchConstants.HAEMOGLOBIN_COUNT)
                                        : "N/A")
                                .append(appendArrow).append("</td>");
                        if (secondMap != null) {
                            // Haemoglobin Count => Increase/Decrease Logic
                            if (!lastHbgmValue.isEmpty() && secondMap
                                    .get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                                if (Double.valueOf(lastHbgmValue).equals(Double.valueOf(
                                        secondMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                                .toString()))) {
                                    appendArrow = "";
                                } else if (Double.parseDouble(lastHbgmValue) > Double
                                        .parseDouble(secondMap.get(
                                                        RchConstants.HAEMOGLOBIN_COUNT)
                                                .toString())) {
                                    appendArrow = " <span>&#8595;</span>";
                                } else {
                                    appendArrow = " <span>&#8593;</span>";
                                }
                            } else {
                                appendArrow = "";
                            }
                            if (secondMap.get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                                lastHbgmValue = secondMap
                                        .get(RchConstants.HAEMOGLOBIN_COUNT)
                                        .toString();
                            }
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.HAEMOGLOBIN_COUNT) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.HAEMOGLOBIN_COUNT))
                                            ? secondMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                            : "N/A")
                                    .append(appendArrow).append("</td>");
                        }

                        if (thirdMap != null) {
                            // Haemoglobin Count => Increase/Decrease Logic
                            if (!lastHbgmValue.isEmpty() && thirdMap
                                    .get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                                if (Double.valueOf(lastHbgmValue).equals(Double.valueOf(
                                        thirdMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                                .toString()))) {
                                    appendArrow = "";
                                } else if (Double.parseDouble(lastHbgmValue) > Double
                                        .parseDouble(thirdMap.get(
                                                        RchConstants.HAEMOGLOBIN_COUNT)
                                                .toString())) {
                                    appendArrow = " <span>&#8595;</span>";
                                } else {
                                    appendArrow = " <span>&#8593;</span>";
                                }
                            } else {
                                appendArrow = "";
                            }
                            if (thirdMap.get(RchConstants.HAEMOGLOBIN_COUNT) != null) {
                                lastHbgmValue = thirdMap
                                        .get(RchConstants.HAEMOGLOBIN_COUNT)
                                        .toString();
                            }
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.HAEMOGLOBIN_COUNT) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.HAEMOGLOBIN_COUNT))
                                            ? thirdMap.get(RchConstants.HAEMOGLOBIN_COUNT)
                                            : "N/A")
                                    .append(appendArrow).append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "FolicAcidTablets_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FALIC_ACID_TABLETS) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FALIC_ACID_TABLETS))
                                        ? firstMap.get(RchConstants.FALIC_ACID_TABLETS)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.FALIC_ACID_TABLETS) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FALIC_ACID_TABLETS))
                                            ? secondMap.get(RchConstants.FALIC_ACID_TABLETS)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.FALIC_ACID_TABLETS) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FALIC_ACID_TABLETS))
                                            ? thirdMap.get(RchConstants.FALIC_ACID_TABLETS)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "IFATablets_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.IFA_TABLET) != null
                                        ? firstMap.get(RchConstants.IFA_TABLET)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.IFA_TABLET) != null ? secondMap
                                            .get(RchConstants.IFA_TABLET)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.IFA_TABLET) != null ? secondMap
                                            .get(RchConstants.IFA_TABLET)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "UrineTest_web",
                                                languageCode)
                                        + " - "
                                        + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Albumin_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.URINE_ALBUMIN) == null
                                        ? "N/A"
                                        : firstMap.get(RchConstants.URINE_ALBUMIN)
                                        .equals("0") ? "NIL"
                                        : (String) firstMap
                                        .get(RchConstants.URINE_ALBUMIN))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.URINE_ALBUMIN) == null
                                            ? "N/A"
                                            : secondMap.get(RchConstants.URINE_ALBUMIN)
                                            .equals("0") ? "NIL"
                                            : (String) secondMap
                                            .get(RchConstants.URINE_ALBUMIN))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(
                                            thirdMap.get(RchConstants.URINE_ALBUMIN) == null
                                                    ? "N/A"
                                                    : thirdMap.get(RchConstants.URINE_ALBUMIN)
                                                    .equals("0") ? "NIL"
                                                    : (String) thirdMap
                                                    .get(RchConstants.URINE_ALBUMIN))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "UrineTest_web",
                                                languageCode)
                                        + " - "
                                        + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Sugar_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.URINE_SUGAR) == null
                                        ? "N/A"
                                        : firstMap.get(RchConstants.URINE_SUGAR)
                                        .equals("0") ? "NIL"
                                        : (String) firstMap
                                        .get(RchConstants.URINE_SUGAR))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(
                                            secondMap.get(RchConstants.URINE_SUGAR) == null
                                                    ? "N/A"
                                                    : secondMap.get(RchConstants.URINE_SUGAR)
                                                    .equals("0") ? "NIL"
                                                    : (String) secondMap
                                                    .get(RchConstants.URINE_SUGAR))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(
                                            thirdMap.get(RchConstants.URINE_SUGAR) == null
                                                    ? "N/A"
                                                    : thirdMap.get(RchConstants.URINE_SUGAR)
                                                    .equals("0") ? "NIL"
                                                    : (String) thirdMap
                                                    .get(RchConstants.URINE_SUGAR))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "BloodSugar_web",
                                                languageCode)
                                        + " - "
                                        + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "BeforeLunch_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL))
                                        ? firstMap.get(RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL)
                                        + " mg/dl"
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL))
                                            ? secondMap.get(RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL)
                                            + " mg/dl"
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL))
                                            ? thirdMap.get(RchConstants.SUGAR_TEST_BEFORE_FOOD_VAL)
                                            + " mg/dl"
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "BloodSugar_web",
                                                languageCode)
                                        + " - "
                                        + internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "AfterLunch_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.SUGAR_TEST_AFTER_FOOD_VAL) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.SUGAR_TEST_AFTER_FOOD_VAL))
                                        ? firstMap.get(RchConstants.SUGAR_TEST_AFTER_FOOD_VAL)
                                        + " mg/dl"
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.SUGAR_TEST_AFTER_FOOD_VAL) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.SUGAR_TEST_AFTER_FOOD_VAL))
                                            ? secondMap.get(RchConstants.SUGAR_TEST_AFTER_FOOD_VAL)
                                            + " mg/dl"
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.SUGAR_TEST_AFTER_FOOD_VAL) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.SUGAR_TEST_AFTER_FOOD_VAL))
                                            ? thirdMap.get(RchConstants.SUGAR_TEST_AFTER_FOOD_VAL)
                                            + " mg/dl"
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "T.T.Dose_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.TT1) == null
                                        ? (firstMap.get(RchConstants.TT2) == null
                                        ? (firstMap.get(RchConstants.TT_BOOSTER) == null
                                        ? "N/A"
                                        : "TT Booster (" + firstMap
                                        .get(RchConstants.TT_BOOSTER)
                                        + ")")
                                        : "TT 2 (" + firstMap
                                        .get(RchConstants.TT2)
                                        + ")")
                                        : "TT 1 (" + firstMap.get(
                                        RchConstants.TT1) + ")")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(RchConstants.TT1) == null
                                            ? (secondMap.get(RchConstants.TT2) == null
                                            ? (secondMap.get(
                                            RchConstants.TT_BOOSTER) == null
                                            ? "N/A"
                                            : "TT Booster (" + secondMap
                                            .get(RchConstants.TT_BOOSTER)
                                            + ")")
                                            : "TT 2 (" + secondMap.get(
                                            RchConstants.TT2)
                                            + ")")
                                            : "TT 1 (" + secondMap.get(RchConstants.TT1)
                                            + ")")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(RchConstants.TT1) == null
                                            ? (thirdMap.get(RchConstants.TT2) == null
                                            ? (thirdMap.get(RchConstants.TT_BOOSTER) == null
                                            ? "N/A"
                                            : "TT Booster (" + thirdMap
                                            .get(RchConstants.TT_BOOSTER)
                                            + ")")
                                            : "TT 2 (" + thirdMap.get(
                                            RchConstants.TT2)
                                            + ")")
                                            : "TT 1 (" + thirdMap.get(RchConstants.TT1)
                                            + ")")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "FoetalHeight(cm)_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.FOETAL_HEIGHT) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FOETAL_HEIGHT))
                                        ? firstMap.get(RchConstants.FOETAL_HEIGHT)
                                        + " cm"
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.FOETAL_HEIGHT) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FOETAL_HEIGHT))
                                            ? secondMap.get(RchConstants.FOETAL_HEIGHT)
                                            + " cm"
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.FOETAL_HEIGHT) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FOETAL_HEIGHT))
                                            ? thirdMap.get(RchConstants.FOETAL_HEIGHT)
                                            + " cm"
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "FoetalHeartSound_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FOETAL_HEART_SOUND) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FOETAL_HEART_SOUND))
                                        ? firstMap.get(RchConstants.FOETAL_HEART_SOUND)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.FOETAL_HEART_SOUND) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FOETAL_HEART_SOUND))
                                            ? secondMap.get(RchConstants.FOETAL_HEART_SOUND)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.FOETAL_HEART_SOUND) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FOETAL_HEART_SOUND))
                                            ? thirdMap.get(RchConstants.FOETAL_HEART_SOUND)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "FoetalPosition_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FOETAL_POSITION) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FOETAL_POSITION))
                                        ? firstMap.get(RchConstants.FOETAL_POSITION)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.FOETAL_POSITION) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FOETAL_POSITION))
                                            ? secondMap.get(RchConstants.FOETAL_POSITION)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.FOETAL_POSITION) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FOETAL_POSITION))
                                            ? thirdMap.get(RchConstants.FOETAL_POSITION)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "FoetalMovement_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FOETAL_MOVEMENT) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FOETAL_MOVEMENT))
                                        ? firstMap.get(RchConstants.FOETAL_MOVEMENT)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.FOETAL_MOVEMENT) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FOETAL_MOVEMENT))
                                            ? secondMap.get(RchConstants.FOETAL_MOVEMENT)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.FOETAL_MOVEMENT) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FOETAL_MOVEMENT))
                                            ? thirdMap.get(RchConstants.FOETAL_MOVEMENT)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "ReferFacility_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.REFERRAL_DONE) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.REFERRAL_DONE))
                                        ? firstMap.get(RchConstants.REFERRAL_DONE)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.REFERRAL_DONE) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.REFERRAL_DONE))
                                            ? secondMap.get(RchConstants.REFERRAL_DONE)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.REFERRAL_DONE) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.REFERRAL_DONE))
                                            ? thirdMap.get(RchConstants.REFERRAL_DONE)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "AnyContraceptiveMethodPostPregnancy_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FAMILY_PLANNING_METHOD) != null
                                        && !("N/A").equals(firstMap.get(
                                        RchConstants.FAMILY_PLANNING_METHOD))
                                        ? firstMap.get(RchConstants.FAMILY_PLANNING_METHOD)
                                        : "Not Applicable")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD) != null
                                            && !("N/A").equals(secondMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD))
                                            ? secondMap.get(RchConstants.FAMILY_PLANNING_METHOD)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD) != null
                                            && !("N/A").equals(thirdMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD))
                                            ? thirdMap.get(RchConstants.FAMILY_PLANNING_METHOD)
                                            : "Not Applicable")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "IsMotherAlive?_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.ALIVE_FLAG))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(RchConstants.ALIVE_FLAG))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(RchConstants.ALIVE_FLAG))
                                    .append("</td>");
                        }
                        sb.append("</tr>");
                        if (firstMap.get(RchConstants.ALIVE_FLAG).equals("No")
                                || (secondMap != null && secondMap
                                .get(RchConstants.ALIVE_FLAG)
                                .equals("No"))
                                || (thirdMap != null
                                && thirdMap.get(RchConstants.ALIVE_FLAG)
                                .equals("No"))) {
                            sb.append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DeathDate_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.DEATH_DATE) != null
                                            ? firstMap.get(RchConstants.DEATH_DATE)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap
                                                .get(RchConstants.DEATH_DATE) != null
                                                ? secondMap.get(RchConstants.DEATH_DATE)
                                                : "No")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap
                                                .get(RchConstants.DEATH_DATE) != null
                                                ? thirdMap.get(RchConstants.DEATH_DATE)
                                                : "No")
                                        .append("</td>");
                            }
                            sb.append("</tr>").append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DeathPlace_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.DEATH_PLACE) != null
                                            ? firstMap.get(RchConstants.DEATH_PLACE)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap
                                                .get(RchConstants.DEATH_PLACE) != null
                                                ? secondMap.get(RchConstants.DEATH_PLACE)
                                                : "No")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap
                                                .get(RchConstants.DEATH_PLACE) != null
                                                ? thirdMap.get(RchConstants.DEATH_PLACE)
                                                : "No")
                                        .append("</td>");
                            }
                            sb.append("</tr>").append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DeathReason_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.DEATH_REASON) != null
                                            ? firstMap.get(RchConstants.DEATH_REASON)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap
                                                .get(RchConstants.DEATH_REASON) != null
                                                ? secondMap.get(RchConstants.DEATH_REASON)
                                                : "No")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap
                                                .get(RchConstants.DEATH_REASON) != null
                                                ? thirdMap.get(RchConstants.DEATH_REASON)
                                                : "No")
                                        .append("</td>");
                            }
                            sb.append("</tr>");
                        }
                        sb.append("<tr>").append("<td>").append(internationalizationService
                                        .getLabelByKeyAndLanguageCode("DangerSigns_web",
                                                languageCode)
                                        + ":").append("</td>").append("<td>")
                                .append(firstMap.get("anc_danger_signs") != null
                                        ? firstMap.get("anc_danger_signs")
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get("anc_danger_signs") != null ? secondMap
                                            .get("anc_danger_signs")
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get("anc_danger_signs") != null ? thirdMap.get(
                                            "anc_danger_signs") : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "AlbendanzoleGiven_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("albendazole_given"))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get("albendazole_given"))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get("albendazole_given"))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "HBsAgTest_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("hbsag_test")).append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get("hbsag_test"))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get("hbsag_test"))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "SickleCellTest_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("sickle_cell_test"))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get("sickle_cell_test"))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get("sickle_cell_test"))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "CalciumTabletsGiven_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("calcium_tablets_given"))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get("calcium_tablets_given"))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get("calcium_tablets_given"))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "IsHighRiskCase_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get("is_high_risk_case"))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get("is_high_risk_case"))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get("is_high_risk_case"))
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("</tbody>").append("</table>")
                                .append("</div>").append("</div >");
                    }
                }

                // Delivery Registration.
                String deliveryResultString = (String) data.get("delivery_result_json");
                List<LinkedHashMap<String, Object>> deliveryResultJsonList = new ArrayList<>();
                if (deliveryResultString != null) {
                    List fromJson = new Gson().fromJson(deliveryResultString, List.class);
                    List<String> stringJsonList = new ArrayList<>();
                    fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));
                    stringJsonList.forEach(stringJson ->
                            deliveryResultJsonList.add(new Gson().fromJson(stringJson,
                                    new TypeToken<LinkedHashMap<String, Object>>() {

                                    }.getType()))
                    );
                }
                if (!CollectionUtils.isEmpty(deliveryResultJsonList)) {
                    sb.append("<hr class=\"cst-hr\"><div class=\"cust-header sub-title border-bottom\">"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "DeliveryRegistration_web", languageCode)
                            + "</div>" + "<div class=\"clearfix\"></div>");
                    LinkedHashMap<String, Object> map;
                    for (int i = 0; i < deliveryResultJsonList.size() + 1; i += 3) {
                        sb.append("<div class=\"row\">");
                        if (i == 0) {
                            map = deliveryResultJsonList.get(i);
                            sb.append("<div class=\"col-4\"><table class=\"sub-title\"><tbody>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DateandTimeofDelivery_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.DATE_OF_DELIVERY) != null
                                            ? map.get(RchConstants.DATE_OF_DELIVERY)
                                            : "N/A")
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "PlaceNameofDelivery_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.DELIVERY_PLACE) != null
                                            ? map.get(RchConstants.DELIVERY_PLACE)
                                            : "N/A")
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "PlaceTypeofDelivery_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.DELIVERY_PLACE_TYPE) != null
                                            ? map.get(RchConstants.DELIVERY_PLACE_TYPE)
                                            : "N/A")
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DeliveryConductedBy_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.DELIVERY_DONE_BY) != null
                                            ? map.get(RchConstants.DELIVERY_DONE_BY)
                                            : "N/A")
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "TypeofDelivery_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.TYPE_OF_DELIVERY) != null
                                            ? map.get(RchConstants.TYPE_OF_DELIVERY)
                                            : "N/A")
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DateandTimeofDischarge_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get(RchConstants.DISCHARGE_DATE) != null
                                            ? map.get(RchConstants.DISCHARGE_DATE)
                                            : "N/A")
                                    .append("</td></tr>");
                            if (map.get("is_abortion_done").equals("true")) {
                                sb.append("<tr><td>" + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "Abortionplace_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.ABORTION_PLACE) != null
                                                ? map.get(RchConstants.ABORTION_PLACE)
                                                : "N/A")
                                        .append("</td></tr>");
                            }
                            sb.append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "HasDeliveryHappened_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get("has_delivery_happened"))
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "PregnancyOutcome_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get("pregnancy_outcome"))
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "IsMotherAlive_web",
                                                    languageCode)
                                            + "?:</td><td>")
                                    .append(map.get("mother_alive"))
                                    .append("</td></tr>")
                                    .append("<tr><td>" + internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "IsHighRiskCase_web",
                                                    languageCode)
                                            + ":</td><td>")
                                    .append(map.get("is_high_risk_case"))
                                    .append("</td></tr>");
                            sb.append("</tbody></table></div>");
                        }
                        for (int j = i; j < (i + 3); j++) {
                            if (j == 0) {
                                continue;
                            }
                            if (j < deliveryResultJsonList.size() + 1) {
                                map = deliveryResultJsonList.get(j - 1);
                                sb.append("<div class=\"col-4\"><table class=\"sub-title\"><thead>")
                                        .append("<tr><td colspan=\"2\">")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        RchConstants.NUBER_TO_STRING_FORMATE_VALUE
                                                                .get(j)
                                                                + "_web",
                                                        languageCode))
                                        .append(" " + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "InfantInformation_web",
                                                        languageCode)
                                                + ":</td></tr></thead>")
                                        .append("<tbody><tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "IsInfantAlive?_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.IS_ALIVE_DEAD) != null
                                                ? map.get(RchConstants.IS_ALIVE_DEAD)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "FullTerm_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.WAS_PREMATURE) != null
                                                ? map.get(RchConstants.WAS_PREMATURE)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "InfantGender_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.GENDER) != null
                                                ? map.get(RchConstants.GENDER)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "InfantCried_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.BABY_CRIED_AT_BIRTH) != null
                                                ? map.get(RchConstants.BABY_CRIED_AT_BIRTH)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "IsReferralDone?_web",
                                                        languageCode)
                                                + "</td><td>")
                                        .append(map.get("is_referral_done") != null
                                                ? map.get("is_referral_done")
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "CongenitalDeformities_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get("child_congential_deformity") != null
                                                ? map.get("child_congential_deformity")
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "BirthWeight_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.BIRTH_WEIGHT) != null
                                                ? map.get(RchConstants.BIRTH_WEIGHT)
                                                + " Kgs"
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "InfantBreastfeedingStartedin1HourofBirth?_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.BREAST_FEEDING_IN_ONE_HOUR) != null
                                                ? map.get(RchConstants.BREAST_FEEDING_IN_ONE_HOUR)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "OPV_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.OPV_GIVEN) != null
                                                ? map.get(RchConstants.OPV_GIVEN)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "BCG_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.BCG_GIVEN) != null
                                                ? map.get(RchConstants.BCG_GIVEN)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "HEPB_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.HEP_B_GIVEN) != null
                                                ? map.get(RchConstants.HEP_B_GIVEN)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("<tr><td>"
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "VITK_web",
                                                        languageCode)
                                                + ":</td><td>")
                                        .append(map.get(RchConstants.VIT_K_GIVEN) != null
                                                ? map.get(RchConstants.VIT_K_GIVEN)
                                                : "N/A")
                                        .append("</td></tr>")
                                        .append("</tbody></table></div>");
                            } else {
                                sb.append("<div class=\"col-4\"><div></div></div>");
                            }
                        }
                        sb.append("</div>");
                    }
                }

                // Pnc visit.
                String pncVisitString = (String) data.get("pnc_visit_json");
                List<LinkedHashMap<String, Object>> pncVisitJsonList = new ArrayList<>();
                if (pncVisitString != null) {
                    List fromJson = new Gson().fromJson(pncVisitString, List.class);
                    List<String> stringJsonList = new ArrayList<>();
                    fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

                    stringJsonList.forEach(stringJson ->
                            pncVisitJsonList.add(new Gson().fromJson(stringJson,
                                    new TypeToken<LinkedHashMap<String, Object>>() {

                                    }.getType()))
                    );
                }
                if (!CollectionUtils.isEmpty(pncVisitJsonList)) {
                    sb.append("<hr class=\"cst-hr\"><div class=\"cust-header sub-title border-bottom\" style=\"word-break: keep-all;\">"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "PNCVisit_web", languageCode)
                            + "</div>" + "<hr class=\"cst-hr\" style=\"padding:0px;\">");

                    LinkedHashMap<String, Object> firstMap;
                    LinkedHashMap<String, Object> secondMap;
                    LinkedHashMap<String, Object> thirdMap;

                    for (int i = 0; i < pncVisitJsonList.size(); i += 3) {

                        firstMap = pncVisitJsonList.get(i);
                        if (pncVisitJsonList.size() > (i + 2)) {
                            secondMap = pncVisitJsonList.get(i + 1);
                            thirdMap = pncVisitJsonList.get(i + 2);
                        } else if (pncVisitJsonList.size() > (i + 1)) {
                            secondMap = pncVisitJsonList.get(i + 1);
                            thirdMap = null;
                        } else {
                            secondMap = null;
                            thirdMap = null;
                        }

                        sb.append("<div class=\"row\">").append("<div class=\"col-12\">")
                                .append("<table class=\"sub-title\">").append("<tbody>")
                                .append("<tr>").append("<td colspan=\"4\">")
                                .append("<div class=\"cust-header sub-title\">")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "MotherDetails_web",
                                                languageCode)
                                        + ":")
                                .append("</div>").append("</td>").append("</tr>")
                                .append("<tr>").append("<td>")
                                .append("<div class=\"cust-header sub-title\">")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Details_web",
                                                languageCode)
                                        + ":")
                                .append("</div").append("</td>").append("<td>")
                                .append("<div class=\"cust-header sub-title\">")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "Visit_web",
                                                languageCode)
                                        + " - " + (i + 1))
                                .append(" (" + firstMap.get(RchConstants.SERVICE_DATE)
                                        + ")")
                                .append("</div>").append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append("<div class=\"cust-header sub-title\">")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "Visit_web",
                                                    languageCode)
                                            + " - " + (i + 2))
                                    .append(" (" + secondMap
                                            .get(RchConstants.SERVICE_DATE)
                                            + ")")
                                    .append("</div>").append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append("<div class=\"cust-header sub-title\">")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "Visit_web",
                                                    languageCode)
                                            + " - " + (i + 3))
                                    .append(" (" + thirdMap
                                            .get(RchConstants.SERVICE_DATE)
                                            + ")")
                                    .append("</div>").append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "ServiceDate_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.SERVICE_DATE) != null
                                        ? firstMap.get(RchConstants.SERVICE_DATE)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.SERVICE_DATE) != null ? secondMap
                                            .get(RchConstants.SERVICE_DATE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.SERVICE_DATE) != null ? thirdMap
                                            .get(RchConstants.SERVICE_DATE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "NumberofIFATabletsGiven_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.IFA_TABLETS_GIVEN) != null
                                        ? firstMap.get(RchConstants.IFA_TABLETS_GIVEN)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.IFA_TABLETS_GIVEN) != null
                                            ? secondMap.get(RchConstants.IFA_TABLETS_GIVEN)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.IFA_TABLETS_GIVEN) != null
                                            ? thirdMap.get(RchConstants.IFA_TABLETS_GIVEN)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "AnyComplication_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.OTHER_DANGER_SIGN) != null
                                        ? firstMap.get(RchConstants.OTHER_DANGER_SIGN)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap
                                            .get(RchConstants.OTHER_DANGER_SIGN) != null
                                            ? secondMap.get(RchConstants.OTHER_DANGER_SIGN)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap
                                            .get(RchConstants.OTHER_DANGER_SIGN) != null
                                            ? thirdMap.get(RchConstants.OTHER_DANGER_SIGN)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "ReferforAdvancedTreatment_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.REFERRAL_PLACE) != null
                                        ? firstMap.get(RchConstants.REFERRAL_PLACE)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.REFERRAL_PLACE) != null ? secondMap
                                            .get(RchConstants.REFERRAL_PLACE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.REFERRAL_PLACE) != null ? thirdMap
                                            .get(RchConstants.REFERRAL_PLACE)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "AnyContraceptiveUsed_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(
                                        RchConstants.FAMILY_PLANNING_METHOD_PNC) != null
                                        ? firstMap.get(RchConstants.FAMILY_PLANNING_METHOD_PNC)
                                        : "N/A")
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(secondMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD_PNC) != null
                                            ? secondMap.get(RchConstants.FAMILY_PLANNING_METHOD_PNC)
                                            : "N/A")
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(thirdMap.get(
                                            RchConstants.FAMILY_PLANNING_METHOD_PNC) != null
                                            ? thirdMap.get(RchConstants.FAMILY_PLANNING_METHOD_PNC)
                                            : "N/A")
                                    .append("</td>");
                        }
                        sb.append("</tr>").append("<tr>").append("<td>")
                                .append(internationalizationService
                                        .getLabelByKeyAndLanguageCode(
                                                "IsMotherAlive?_web",
                                                languageCode)
                                        + ":")
                                .append("</td>").append("<td>")
                                .append(firstMap.get(RchConstants.IS_ALIVE_PNC_MOTHER))
                                .append("</td>");
                        if (secondMap != null) {
                            sb.append("<td>").append(
                                            secondMap.get(RchConstants.IS_ALIVE_PNC_MOTHER))
                                    .append("</td>");
                        }
                        if (thirdMap != null) {
                            sb.append("<td>").append(
                                            thirdMap.get(RchConstants.IS_ALIVE_PNC_MOTHER))
                                    .append("</td>");
                        }
                        sb.append("</tr>");
                        if (firstMap.get(RchConstants.IS_ALIVE_PNC_MOTHER).equals("No")
                                || (secondMap != null && secondMap
                                .get(RchConstants.IS_ALIVE_PNC_MOTHER)
                                .equals("No"))
                                || (thirdMap != null && thirdMap
                                .get(RchConstants.IS_ALIVE_PNC_MOTHER)
                                .equals("No"))) {
                            sb.append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "DateofDeath_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.DEATH_DATE_PNC_MOTHER) != null
                                            ? firstMap.get(RchConstants.DEATH_DATE_PNC_MOTHER)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap.get(
                                                RchConstants.DEATH_DATE_PNC_MOTHER) != null
                                                ? secondMap.get(RchConstants.DEATH_DATE_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap.get(
                                                RchConstants.DEATH_DATE_PNC_MOTHER) != null
                                                ? thirdMap.get(RchConstants.DEATH_DATE_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            sb.append("</tr>").append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "ReasonofDeath_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.DEATH_REASON_PNC_MOTHER) != null
                                            ? firstMap.get(RchConstants.DEATH_REASON_PNC_MOTHER)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap.get(
                                                RchConstants.DEATH_REASON_PNC_MOTHER) != null
                                                ? secondMap.get(RchConstants.DEATH_REASON_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap.get(
                                                RchConstants.DEATH_REASON_PNC_MOTHER) != null
                                                ? thirdMap.get(RchConstants.DEATH_REASON_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            sb.append("</tr>").append("<tr>").append("<td>")
                                    .append(internationalizationService
                                            .getLabelByKeyAndLanguageCode(
                                                    "PlaceofDeath_web",
                                                    languageCode)
                                            + ":")
                                    .append("</td>").append("<td>")
                                    .append(firstMap.get(
                                            RchConstants.PLACE_OF_DEATH_PNC_MOTHER) != null
                                            ? firstMap.get(RchConstants.PLACE_OF_DEATH_PNC_MOTHER)
                                            : "N/A")
                                    .append("</td>");
                            if (secondMap != null) {
                                sb.append("<td>").append(secondMap.get(
                                                RchConstants.PLACE_OF_DEATH_PNC_MOTHER) != null
                                                ? secondMap.get(RchConstants.PLACE_OF_DEATH_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            if (thirdMap != null) {
                                sb.append("<td>").append(thirdMap.get(
                                                RchConstants.PLACE_OF_DEATH_PNC_MOTHER) != null
                                                ? thirdMap.get(RchConstants.PLACE_OF_DEATH_PNC_MOTHER)
                                                : "N/A")
                                        .append("</td>");
                            }
                            sb.append("</tr>");
                        }
                        sb.append("</tbody>").append("</table>").append("</div>");

                        sb.append("</div>");
                    }

                    List<String> allPNCChildStringList = pncVisitJsonList.stream()
                            .map(e -> (String) e.get("child_pnc_dto"))
                            .collect(Collectors.toList());
                    List<LinkedHashMap<String, Object>> allPNCChildJsonList = new ArrayList<>();
                    List<String> allPNCChildJsonStringList = new ArrayList<>();
                    for (String s : allPNCChildStringList) {
                        if (s != null) {
                            List fromJson = new Gson().fromJson(
                                    s, List.class);
                            fromJson.forEach(jsonObj ->
                                    allPNCChildJsonStringList
                                            .add(new Gson().toJson(jsonObj))
                            );
                        }
                    }
                    if (!CollectionUtils.isEmpty(allPNCChildJsonStringList)) {
                        allPNCChildJsonStringList.forEach(stringJson ->
                                allPNCChildJsonList.add(new Gson().fromJson(stringJson,
                                        new TypeToken<LinkedHashMap<String, Object>>() {
                                        }.getType()))
                        );
                    }

                    Map<Object, List<LinkedHashMap<String, Object>>> allPNCChildVisits = allPNCChildJsonList
                            .stream()
                            .collect(Collectors.groupingBy(
                                    e -> e.get("child_id") == null ? "-1"
                                            : e.get("child_id")));

                    int j = 0;
                    for (List<LinkedHashMap<String, Object>> childPNCList : allPNCChildVisits
                            .values()) {
                        j++;

                        if (childPNCList != null && !CollectionUtils.isEmpty(childPNCList)) {
                            for (int k = 0; k < childPNCList.size(); k += 3) {
                                firstMap = childPNCList.get(k);
                                if (childPNCList.size() > (k + 2)) {
                                    secondMap = childPNCList.get(k + 1);
                                    thirdMap = childPNCList.get(k + 2);
                                } else if (childPNCList.size() > (k + 1)) {
                                    secondMap = childPNCList.get(k + 1);
                                    thirdMap = null;
                                } else {
                                    secondMap = null;
                                    thirdMap = null;
                                }

                                sb.append("<div class=\"row\">")
                                        .append("<div class=\"col-12\">")
                                        .append("<table class=\"sub-title\">")
                                        .append("<tbody>").append("<tr>")
                                        .append("<td colspan=\"4\">")
                                        .append("<div class=\"cust-header sub-title\">")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        RchConstants.NUBER_TO_STRING_FORMATE_VALUE
                                                                .get(j)
                                                                + "_web",
                                                        languageCode)
                                                + " "
                                                + internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "InfantDetails_web",
                                                        languageCode)
                                                + ":")
                                        .append("</div>").append("</td>")
                                        .append("</tr>").append("<tr>")
                                        .append("<td>")
                                        .append("<div class=\"cust-header sub-title\">")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "Details_web",
                                                        languageCode)
                                                + ":")
                                        .append("</div").append("</td>")
                                        .append("<td>")
                                        .append("<div class=\"cust-header sub-title\">")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "Visit_web",
                                                        languageCode)
                                                + " - " + (k + 1))
                                        .append(" (" + firstMap.get(
                                                "service_date") + ")")
                                        .append("</div>").append("</td>");
                                if (secondMap != null) {
                                    sb.append("<td>").append(
                                                    "<div class=\"cust-header sub-title\">")
                                            .append(internationalizationService
                                                    .getLabelByKeyAndLanguageCode(
                                                            "Visit_web",
                                                            languageCode)
                                                    + " - "
                                                    + (k + 2))
                                            .append(" (" + secondMap.get(
                                                    "service_date")
                                                    + ")")
                                            .append("</div>")
                                            .append("</td>");
                                }
                                if (thirdMap != null) {
                                    sb.append("<td>").append(
                                                    "<div class=\"cust-header sub-title\">")
                                            .append(internationalizationService
                                                    .getLabelByKeyAndLanguageCode(
                                                            "Visit_web",
                                                            languageCode)
                                                    + " - "
                                                    + (k + 3))
                                            .append(" (" + thirdMap.get(
                                                    "service_date")
                                                    + ")")
                                            .append("</div>")
                                            .append("</td>");
                                }
                                sb.append("</tr>").append("<tr>").append("<td>")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "MemberName_web",
                                                        languageCode)
                                                + ":")
                                        .append("</td>").append("<td>")
                                        .append(firstMap.get("child_name"))
                                        .append("</td>");
                                if (secondMap != null) {
                                    sb.append("<td>")
                                            .append(secondMap.get(
                                                    "child_name"))
                                            .append("</td>");
                                }
                                if (thirdMap != null) {
                                    sb.append("<td>")
                                            .append(thirdMap.get(
                                                    "child_name"))
                                            .append("</td>");
                                }
                                sb.append("</tr>").append("<tr>").append("<td>")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "Weight(inkg)_web",
                                                        languageCode)
                                                + ":")
                                        .append("</td>").append("<td>")
                                        .append(firstMap.get(
                                                RchConstants.CHILD_WEIGHT) != null
                                                ? firstMap.get(RchConstants.CHILD_WEIGHT)
                                                + " Kgs"
                                                : "N/A")
                                        .append("</td>");
                                if (secondMap != null) {
                                    sb.append("<td>").append(secondMap.get(
                                                    RchConstants.CHILD_WEIGHT) != null
                                                    ? secondMap.get(RchConstants.CHILD_WEIGHT)
                                                    + " Kgs"
                                                    : "N/A")
                                            .append("</td>");
                                }
                                if (thirdMap != null) {
                                    sb.append("<td>").append(thirdMap.get(
                                                    RchConstants.CHILD_WEIGHT) != null
                                                    ? thirdMap.get(RchConstants.CHILD_WEIGHT)
                                                    + " Kgs"
                                                    : "N/A")
                                            .append("</td>");
                                }
                                sb.append("</tr>").append("<tr>").append("<td>")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "ReferforAdvancedTreatment_web",
                                                        languageCode)
                                                + ":")
                                        .append("</td>").append("<td>")
                                        .append(firstMap.get(
                                                RchConstants.CHILD_REFERRAL_DONE) != null
                                                ? firstMap.get(RchConstants.CHILD_REFERRAL_DONE)
                                                : "N/A")
                                        .append("</td>");
                                if (secondMap != null) {
                                    sb.append("<td>").append(secondMap.get(
                                                    RchConstants.CHILD_REFERRAL_DONE) != null
                                                    ? secondMap.get(RchConstants.CHILD_REFERRAL_DONE)
                                                    : "N/A")
                                            .append("</td>");
                                }
                                if (thirdMap != null) {
                                    sb.append("<td>").append(thirdMap.get(
                                                    RchConstants.CHILD_REFERRAL_DONE) != null
                                                    ? thirdMap.get(RchConstants.CHILD_REFERRAL_DONE)
                                                    : "N/A")
                                            .append("</td>");
                                }
                                sb.append("</tr>").append("<tr>").append("<td>")
                                        .append(internationalizationService
                                                .getLabelByKeyAndLanguageCode(
                                                        "IsInfantAlive?_web",
                                                        languageCode)
                                                + ":")
                                        .append("</td>").append("<td>")
                                        .append(firstMap.get(
                                                RchConstants.IS_ALIVE_PNC_CHILD))
                                        .append("</td>");
                                if (secondMap != null) {
                                    sb.append("<td>").append(secondMap.get(
                                                    RchConstants.IS_ALIVE_PNC_CHILD))
                                            .append("</td>");
                                }
                                if (thirdMap != null) {
                                    sb.append("<td>").append(thirdMap.get(
                                                    RchConstants.IS_ALIVE_PNC_CHILD))
                                            .append("</td>");
                                }
                                sb.append("</tr>");
                                if (firstMap.get(RchConstants.IS_ALIVE_PNC_CHILD)
                                        .equals("No")
                                        || (secondMap != null && secondMap.get(
                                                RchConstants.IS_ALIVE_PNC_CHILD)
                                        .equals("No"))
                                        || (thirdMap != null && thirdMap.get(
                                                RchConstants.IS_ALIVE_PNC_CHILD)
                                        .equals("No"))) {
                                    sb.append("<tr>").append("<td>").append(
                                                    internationalizationService
                                                            .getLabelByKeyAndLanguageCode(
                                                                    "DateofDeath_web",
                                                                    languageCode)
                                                            + ":")
                                            .append("</td>").append("<td>")
                                            .append(firstMap.get(
                                                    RchConstants.DEATH_DATE_PNC_CHILD) != null
                                                    ? firstMap.get(RchConstants.DEATH_DATE_PNC_CHILD)
                                                    : "N/A")
                                            .append("</td>");
                                    if (secondMap != null) {
                                        sb.append("<td>").append(secondMap.get(
                                                        RchConstants.DEATH_DATE_PNC_CHILD) != null
                                                        ? secondMap.get(RchConstants.DEATH_DATE_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    if (thirdMap != null) {
                                        sb.append("<td>").append(thirdMap.get(
                                                        RchConstants.DEATH_DATE_PNC_CHILD) != null
                                                        ? thirdMap.get(RchConstants.DEATH_DATE_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    sb.append("</tr>").append("<tr>").append("<td>")
                                            .append(internationalizationService
                                                    .getLabelByKeyAndLanguageCode(
                                                            "ReasonofDeath_web",
                                                            languageCode)
                                                    + ":")
                                            .append("</td>").append("<td>")
                                            .append(firstMap.get(
                                                    RchConstants.DEATH_REASON_PNC_CHILD) != null
                                                    ? firstMap.get(RchConstants.DEATH_REASON_PNC_CHILD)
                                                    : "N/A")
                                            .append("</td>");
                                    if (secondMap != null) {
                                        sb.append("<td>").append(secondMap.get(
                                                        RchConstants.DEATH_REASON_PNC_CHILD) != null
                                                        ? secondMap.get(RchConstants.DEATH_REASON_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    if (thirdMap != null) {
                                        sb.append("<td>").append(thirdMap.get(
                                                        RchConstants.DEATH_REASON_PNC_CHILD) != null
                                                        ? thirdMap.get(RchConstants.DEATH_REASON_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    sb.append("</tr>").append("<tr>").append("<td>")
                                            .append(internationalizationService
                                                    .getLabelByKeyAndLanguageCode(
                                                            "PlaceofDeath_web",
                                                            languageCode)
                                                    + ":")
                                            .append("</td>").append("<td>")
                                            .append(firstMap.get(
                                                    RchConstants.PLACE_OF_DEATH_PNC_CHILD) != null
                                                    ? firstMap.get(RchConstants.PLACE_OF_DEATH_PNC_CHILD)
                                                    : "N/A")
                                            .append("</td>");
                                    if (secondMap != null) {
                                        sb.append("<td>").append(secondMap.get(
                                                        RchConstants.PLACE_OF_DEATH_PNC_CHILD) != null
                                                        ? secondMap.get(RchConstants.PLACE_OF_DEATH_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    if (thirdMap != null) {
                                        sb.append("<td>").append(thirdMap.get(
                                                        RchConstants.PLACE_OF_DEATH_PNC_CHILD) != null
                                                        ? thirdMap.get(RchConstants.PLACE_OF_DEATH_PNC_CHILD)
                                                        : "N/A")
                                                .append("</td>");
                                    }
                                    sb.append("</tr>");
                                }
                                sb.append("</tbody>").append("</table>")
                                        .append("</div>").append("</div>");
                            }
                        }
                    }
                }
                if (g < dataList.size()) {
                    sb.append("</div> <div class=\"clearfix\"></div></div><p style=\"page-break-before: always\"></p>");
                } else {
                    sb.append("</div>");
                }
            }
        } else if (RchConstants.RCH_CHILD_SERVICE.equalsIgnoreCase(type)) {
            for (int i = 0; i < dataList.size(); i++) {
                LinkedHashMap<String, Object> data = dataList.get(i);
                sb.append("<div class=\"box mb-15\">  <div class=\"clearfix\"></div> <div class=\"box-header\"> <div class=\"sub-title h1-clone\">"
                        + internationalizationService.getLabelByKeyAndLanguageCode("Name_web",
                        languageCode)
                        + " : ");
                sb.append("<h1 class=\"sub-title h1-display\" style=\"width: 90%;\"><span style=\"opacity: 0; width: 0; display: inline-block;\">" + (i + 1) + ") </span>" + data.get("member_name")).append(data.get("child_unique_health_id") != null
                        ? ", " + internationalizationService.getLabelByKeyAndLanguageCode(
                        "TeCHOID_web", languageCode) + " : "
                        + data.get("child_unique_health_id")
                        : "");
                // General Information
                sb.append("</h1></div></div><div class=\"box-body col-12\" style=\"/* padding: 0; */\"><div class=\"cust-header sub-title border-bottom\">"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "GENERALINFORMATION_web", languageCode)
                                + "</div><div class=\"row\"><div class=\"col-6\"><table class=\"sub-title\"><tbody>"
                                + "<tr><td>"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "ChildName_web", languageCode)
                                + ":</td><td>").append(data.get(RchConstants.MEMBER_NAME))
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Gender_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.GENDER) != null
                                ? data.get(RchConstants.GENDER)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("BirthWeight_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.WEIGHT) != null
                                ? data.get(RchConstants.WEIGHT) + " Kgs"
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Address_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ADDRESS) != null
                                ? data.get(RchConstants.ADDRESS)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Lastgivenchildservicedate_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.LAST_GIVEN_CHILD_SERVICE_DATE) != null
                                ? data.get(RchConstants.LAST_GIVEN_CHILD_SERVICE_DATE)
                                : "N/A")
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-6\"><table class=\"sub-title\">"
                                + "<tbody><tr><td>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Mothername_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MOTHER_NAME) != null
                                ? data.get(RchConstants.MOTHER_NAME) + " (" + data.get(
                                RchConstants.MOTHER_UNIQUE_HEALTH_ID)
                                + ")"
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("BirthDate_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BIRTH_DATE) != null
                                ? data.get(RchConstants.BIRTH_DATE)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("BirthPlace_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BIRTH_LOCATION) != null
                                ? data.get(RchConstants.BIRTH_LOCATION)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Religion/Caste_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.RELIGION) != null
                                ? data.get(RchConstants.RELIGION)
                                : "N/A")
                        .append(data.get(RchConstants.CASTE) != null
                                ? " / " + data.get(RchConstants.CASTE)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("FamilyId_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("family_id") != null ? data.get("family_id") : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Height_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("height"))
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("HavePedalEdema_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("have_pedal_edema"))
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("MidArmCircumference_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get("mid_arm_circumference"))
                        .append("</td></tr></tbody></table></div></div><div class=\"col-12 nopadding\">");

                // vaccination
                sb.append("<div class=\"cust-header sub-title border-bottom\">"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "VACCINATIONDETAILS_web", languageCode)
                                + "</div></div>"
                                + "<div class=\"row\"><div class=\"col-4\"><table class=\"sub-title\"><tbody>"
                                + "<tr><td>"
                                + internationalizationService.getLabelByKeyAndLanguageCode("BCG_web",
                                languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BCG) != null ? data.get(RchConstants.BCG)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("OPV1_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.OPV_1) != null
                                ? data.get(RchConstants.OPV_1)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Pentavalent1_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PENTAVALENT_1) != null
                                ? data.get(RchConstants.PENTAVALENT_1)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("RotaVirus1_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ROTA_VIRUS_1) != null
                                ? data.get(RchConstants.ROTA_VIRUS_1)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("FIPV1_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.F_IPV_1) != null
                                ? data.get(RchConstants.F_IPV_1)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("OPV2_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.OPV_2) != null
                                ? data.get(RchConstants.OPV_2)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Pentavalent2_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PENTAVALENT_2) != null
                                ? data.get(RchConstants.PENTAVALENT_2)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("RotaVirus2_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ROTA_VIRUS_2) != null
                                ? data.get(RchConstants.ROTA_VIRUS_2)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("OPV3_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.OPV_3) != null
                                ? data.get(RchConstants.OPV_3)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Pentavalent3_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PENTAVALENT_3) != null
                                ? data.get(RchConstants.PENTAVALENT_3)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("RotaVirus3_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ROTA_VIRUS_3) != null
                                ? data.get(RchConstants.ROTA_VIRUS_3)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("FIPV2_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.F_IPV_2) != null
                                ? data.get(RchConstants.F_IPV_2)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Measles1/MeaslesRubella1_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEASLES) != null
                                ? data.get(RchConstants.MEASLES)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "FullyImmunizedinTwelveMonths?_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.FULLY_IMMUN_12_MNTH) != null
                                ? data.get(RchConstants.FULLY_IMMUN_12_MNTH)
                                : "N/A")
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-4\"><table class=\"sub-title\"><tbody>"
                                + "<tr><td>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "OPVBooster_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.OPV_BOOSTER) != null
                                ? data.get(RchConstants.OPV_BOOSTER)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("DPTBooster1_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.DPT_BOOSTER) != null
                                ? data.get(RchConstants.DPT_BOOSTER)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("DPTBooster2(5yrs)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.DPT_BOOSTER) != null
                                ? data.get(RchConstants.DPT_BOOSTER)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("TD(10yrs)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append("N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("TD(16yrs)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append("N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Measles2/MeaslesRubella2_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEASLES_2) != null
                                ? data.get(RchConstants.MEASLES_2)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "FullyImmunizedinTwoYears?_web",
                                        languageCode)
                                + "</td><td>")
                        .append(data.get(RchConstants.FULLY_IMMUN_2_YR) != null
                                ? data.get(RchConstants.FULLY_IMMUN_2_YR)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "BreastfeduntilSixMonths_web",
                                        languageCode)
                                + "</td><td>")
                        .append(data.get(RchConstants.IS_BREASTFED_TILL_SIX_MONTH) != null
                                ? data.get(RchConstants.IS_BREASTFED_TILL_SIX_MONTH)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "ComplementaryFeedingStartPeriod_web",
                                        languageCode)
                                + "</td><td>")
                        .append(data.get(
                                RchConstants.COMPLEMENTARY_FEEDING_START_PERIOD) != null
                                ? data.get(RchConstants.COMPLEMENTARY_FEEDING_START_PERIOD)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("IfaSyrupGiven_web",
                                        languageCode)
                                + "</td><td>")
                        .append(data.get("ifa_syrup_given"))
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-4\">" + "<table class=\"sub-title\"><tbody>"
                                + "<tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "VitaminADose_web",
                                        languageCode)
                                + "</u>:</td></tr>");
                List<String> vitaminADoseDateList = new ArrayList<>();
                String vitaminADoseString = (String) data.get(RchConstants.VIT_A_DOSE);
                if (vitaminADoseString != null) {
                    if (vitaminADoseString.contains(",")) {
                        vitaminADoseDateList = Arrays.asList(vitaminADoseString.split(","));
                    } else {
                        vitaminADoseDateList.add(vitaminADoseString);
                    }
                }
                if (!CollectionUtils.isEmpty(vitaminADoseDateList)) {
                    for (String obj : vitaminADoseDateList) {
                        sb.append("<tr><td>");
                        sb.append(internationalizationService.getLabelByKeyAndLanguageCode(
                                RchConstants.NUBER_TO_STRING_FORMATE_VALUE.get(
                                        vitaminADoseDateList.indexOf(obj) + 1)
                                        + "_web",
                                languageCode)
                                + " "
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Dose_web",
                                        languageCode));
                        sb.append(":</td><td>");
                        sb.append(obj);
                        sb.append("</td></tr>");
                    }
                } else {
                    sb.append("<tr><td colspan=\"2\">"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "NoDataAvailable_web", languageCode)
                            + "</td></tr>");
                }
                sb.append("</tbody></table></div></div>");

                // AEFI OF VACCINATION
                sb.append("<h6></h6><div class=\"row\"><div class=\"col-4\"><h6></h6>"
                                + "<table class=\"sub-title\"><tbody>")
                        .append("<tr><td colspan=\"2\"><u>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("DPTBooster_web",
                                        languageCode)
                                + "</u>:</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("ChildWeight_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BSTR_CHILD_WEIGHT) != null
                                ? data.get(RchConstants.BSTR_CHILD_WEIGHT) + " Kgs"
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("IsDiarrhea_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.IS_DIARRHEA) != null
                                ? data.get(RchConstants.IS_DIARRHEA)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("IsPnuemonia_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.PHUEMONIA) != null
                                ? data.get(RchConstants.PHUEMONIA)
                                : "N/A")
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-4\"><h6></h6>"
                                + "<table class=\"sub-title\"><tbody>")
                        .append("<tr><td colspan=\"2\"><u>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("DPTBooster2_web",
                                        languageCode)
                                + "</u>:</td></tr>")
                        .append("<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("AdverseEffect_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ADVERSE_EFFECT) != null
                                ? data.get(RchConstants.ADVERSE_EFFECT)
                                : "N/A")
                        .append("</td></tr>" + "<tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("VaccinationDetails_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.VACC_DETAIL) != null
                                ? data.get(RchConstants.VACC_DETAIL)
                                : "N/A")
                        .append("</td></tr>" + "</tbody></table></div></div>");
                if ((i + 1) < dataList.size()) {
                    sb.append("</div> <div class=\"clearfix\"></div></div><p style=\"page-break-before: always\"></p>");
                }
            }
        } else if (RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE.equalsIgnoreCase(type)) {
            int g = 0;
            for (LinkedHashMap<String, Object> data : dataList) {
                g++;
                sb.append("<div class=\"box mb-15\">  <div class=\"clearfix\"></div> <div class=\"box-header\">"
                        + "                <div class=\"sub-title h1-clone\">"
                        + internationalizationService.getLabelByKeyAndLanguageCode("Name_web",
                        languageCode)
                        + " : ");
                sb.append("<h1 class=\"sub-title h1-display\" style=\"width: 90%;\"><span style=\"opacity: 0; width: 0; display: inline-block;\">" + g + ") </span>" + data.get("member_name")).append(data.get("unique_health_id") != null ? ", "
                        + internationalizationService.getLabelByKeyAndLanguageCode(
                        "TeCHOID_web", languageCode)
                        + " : " + data.get("unique_health_id") : "");
                sb.append("</h1></div></div><div class=\"box-body col-12\"><div class=\"cust-header sub-title border-bottom\">"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "GENERALINFORMATION_web", languageCode)
                                + "</div>"
                                + "<div class=\"row\"><div class=\"col-4\"><table class=\"sub-title\"><thead>"
                                + "<tr><td colspan=\"2\"><u>"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "MemberInformation_web", languageCode)
                                + "</u></td></tr></thead><tbody>" + "<tr><td>"
                                + internationalizationService.getLabelByKeyAndLanguageCode(
                                "RegistrationDate_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.REGISTRATION_DATE) != null
                                ? data.get(RchConstants.REGISTRATION_DATE)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("MemberName_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEMBER_NAME) != null
                                ? data.get(RchConstants.MEMBER_NAME)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Age(atpresent)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEMBER_CURRENT_AGE) != null
                                ? Double.valueOf(data
                                .get(RchConstants.MEMBER_CURRENT_AGE)
                                .toString()).intValue() + " years"
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Age(atthetimeofmarriage)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEMBER_MRG_AGE) != null
                                ? Double.valueOf(data.get(RchConstants.MEMBER_MRG_AGE)
                                .toString()).intValue() + " years"
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("EligibleCoupleDate_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ELIGIBLE_COUPLE_DATE) != null
                                ? data.get(RchConstants.ELIGIBLE_COUPLE_DATE)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "AadharNumberavailable_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.MEMBER_AADHAR_NUMBER_AVAILABLE) != null
                                ? data.get(RchConstants.MEMBER_AADHAR_NUMBER_AVAILABLE)
                                : "No")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "BankAccountNumberavailable_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(
                                RchConstants.MEMBER_BANK_ACCOUNT_NUMBER_AVAILABLE) != null
                                ? data.get(RchConstants.MEMBER_BANK_ACCOUNT_NUMBER_AVAILABLE)
                                : "No")
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-4\"><table class=\"sub-title\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "HusbandInformation_web",
                                        languageCode)
                                + "</u></td></tr></thead>")
                        .append("<tbody><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("HusbandName_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.HUSBAND_NAME) != null
                                ? data.get(RchConstants.HUSBAND_NAME)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Age(atpresent)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.HUSBAND_CURRENT_AGE) != null
                                ? Double.valueOf(data
                                .get(RchConstants.HUSBAND_CURRENT_AGE)
                                .toString()).intValue() + " years"
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Age(atthetimeofmarriage)_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.HUSBAND_MRG_AGE) != null
                                ? Double.valueOf(data.get(RchConstants.HUSBAND_MRG_AGE)
                                .toString()).intValue() + " years"
                                : "N/A")
                        .append("</td></tr></tbody></table>")
                        .append("<table class=\"sub-title\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "FamilyInformation_web",
                                        languageCode)
                                + "</u></td></tr></thead><tbody><tr><td>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Address_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.ADDRESS) != null
                                ? data.get(RchConstants.ADDRESS)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Religion/Caste_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.RELIGION) != null
                                ? data.get(RchConstants.RELIGION)
                                : "N/A")
                        .append(data.get(RchConstants.CASTE) != null
                                ? " / " + data.get(RchConstants.CASTE)
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("APL/BPL_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.BPL_APL) != null
                                ? data.get(RchConstants.BPL_APL)
                                : "N/A")
                        .append("</td></tr></tbody></table></div>")
                        .append("<div class=\"col-4\"><table class=\"sub-title\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "TotalNumberofChildBirths_web",
                                        languageCode)
                                + "</u></td></tr></thead>")
                        .append("<tbody><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Male_web", languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.TOTAL_GIVEN_MALE_BIRTH) != null
                                ? data.get(RchConstants.TOTAL_GIVEN_MALE_BIRTH)
                                : "0")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Female_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.TOTAL_GIVEN_FEMALE_BIRTH) != null
                                ? data.get(RchConstants.TOTAL_GIVEN_FEMALE_BIRTH)
                                : "0")
                        .append("</td></tr></tbody></table>")
                        .append("<table class=\"sub-title\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "LiveChildrenCount_web",
                                        languageCode)
                                + "</u></td></tr></thead><tr><td>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "Male_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.LIVE_MALE_BIRTH) != null
                                ? data.get(RchConstants.LIVE_MALE_BIRTH)
                                : "0")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Female_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.LIVE_FEMALE_BIRTH) != null
                                ? data.get(RchConstants.LIVE_FEMALE_BIRTH)
                                : "0")
                        .append("</td></tr></tbody></table>")
                        .append("<table class=\"sub-title\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td colspan=\"2\"><u>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode(
                                        "YoungestChildInformation_web",
                                        languageCode)
                                + "</u></td></tr></thead><tbody><tr><td>"
                                + internationalizationService
                                .getLabelByKeyAndLanguageCode("Age_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.SML_CHILD_AGE) != null
                                ? Double.valueOf(data.get(RchConstants.SML_CHILD_AGE)
                                .toString()).intValue() + " years"
                                : "N/A")
                        .append("</td></tr><tr><td>" + internationalizationService
                                .getLabelByKeyAndLanguageCode("Gender_web",
                                        languageCode)
                                + ":</td><td>")
                        .append(data.get(RchConstants.SML_CHILD_GENDER) != null
                                ? data.get(RchConstants.SML_CHILD_GENDER)
                                : "N/A")
                        .append("</td></tr></tbody></table></div></div>");

                // LMP VISIT INFO
                List<LinkedHashMap<String, Object>> lmpVisitInfoList = new ArrayList<>();
                String lmpVisitInfoString = (String) data.get(RchConstants.LMP_VISIT_INFO);
                if (lmpVisitInfoString != null) {

                    List fromJson = new Gson().fromJson(lmpVisitInfoString, List.class);
                    List<String> stringJsonList = new ArrayList<>();
                    fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

                    stringJsonList.forEach(stringJson ->
                            lmpVisitInfoList.add(new Gson().fromJson(stringJson,
                                    new TypeToken<LinkedHashMap<String, Object>>() {

                                    }.getType()))
                    );
                }
                if (!CollectionUtils.isEmpty(lmpVisitInfoList)) {
                    sb.append("<div class=\"row\"><div class=\"col-12\"><div class=\"cust-header sub-title border-bottom\">"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "LMPVISITINFORMATION_web", languageCode)
                            + "</div>");
                    sb.append("<table class=\"sub-title\"><thead><tr>" + "<td><u>"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "VisitNo._web", languageCode)
                            + "</u></td>" + "<td><u>"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "Date_web", languageCode)
                            + "</u></td>" + "<td><u>"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "Contraceptionmethod_web", languageCode)
                            + "</u></td>" + "<td><u>"
                            + internationalizationService.getLabelByKeyAndLanguageCode(
                            "IsPregnant_web", languageCode)
                            + "</u></td>" + "</tr></thead><tbody>");
                    for (int i = 0; i < lmpVisitInfoList.size(); i++) {
                        LinkedHashMap<String, Object> record = lmpVisitInfoList.get(i);
                        sb.append("<tr> <td>" + (i + 1) + "</td> <td>"
                                + record.get(RchConstants.DATE) + "</td> <td>");
                        sb.append(record.get(RchConstants.CONTRACEPTION_METHOD) != null
                                ? record.get(RchConstants.CONTRACEPTION_METHOD)
                                : "N/A");
                        sb.append("</td> <td>" + record.get("is_pregnant") + "</td>");
                        sb.append("</tr>");
                    }
                    sb.append("</tbody></table></div></div> <div class=\"clearfix\"></div></div>");
                }
                sb.append("</div>");
                if (g < dataList.size()) {
                    sb.append("<p style=\"page-break-before: always\"></p>");
                }
            }
        }
        sb.append("<div class=\"clearfix\"></div></div>");

        // Appendix of abbreviation and full form list
        sb.append("<p style=\"page-break-before: always\"></p><div class=\"index-title\"><div class=\"clearfix\"></div><h1>Appendix</h1></div>"
                + "<div class=\"box mb-15\"><div class=\"clearfix\"></div><div><table class=\"sub-title\">"
                + "<thead><tr><th style=\"width: 60px;\">Sr. no.</th><th>Abbreviation</th><th>Full form</th></tr></thead>"
                + "<tbody>");

        Map<String, String> CURRENT_APPENDIX_MAP = new LinkedHashMap<>();
        if (RchConstants.RCH_MOTHER_SERVICE.equalsIgnoreCase(type)) {
            CURRENT_APPENDIX_MAP = RchConstants.MOTHER_SERVICE_APPENDIX;
        } else if (RchConstants.RCH_CHILD_SERVICE.equalsIgnoreCase(type)) {
            CURRENT_APPENDIX_MAP = RchConstants.CHILD_SERVICE_APPENDIX;
        } else if (RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE.equalsIgnoreCase(type)) {
            CURRENT_APPENDIX_MAP = RchConstants.ELIGIBLE_COUPLE_SERVICE_APPENDIX;
        }

        int i = 0;
        for (Map.Entry<String, String> entry : CURRENT_APPENDIX_MAP.entrySet()) {
            i++;
            sb.append("<tr>" + "<td>" + i + "</td>" + "<td>" + entry.getKey() + "</td>" + "<td>"
                    + entry.getValue() + "</td>" + "</tr>");
        }

        sb.append("</tbody></table></div></div>");
        sb.append("</body></html>");

        return this.generatePdfExtraContent(sb.toString(), userName, type,
                locationId, fromDate, toDate, languageCode);
    }

    private ByteArrayOutputStream generatePdfExtraContent(String mainHtml, String userName, String type,
                                                          Integer locationId, Object fromDate, Object toDate, String languageCode)
            throws IOException, InterruptedException, ParseException {

        Pdf pdf = new Pdf();
        // pdf.addPageFromString(new
        pdf.addPageFromString(mainHtml);
        // Add a Table of Contents
        pdf.addToc();
        pdf.addTocParam(new Param("--toc-header-text", "Index"));

        // The `wkhtmltopdf` shell command accepts different types of options such as
        // global, page, headers and footers, and toc. Please see `wkhtmltopdf -H` for a
        // full explanation.
        // All options are passed as array, for example:

        String typeValue = null;
        if (RchConstants.RCH_MOTHER_SERVICE.equalsIgnoreCase(type)) {
            typeValue = "MotherService_web";
        } else if (RchConstants.RCH_CHILD_SERVICE.equalsIgnoreCase(type)) {
            typeValue = "ChildService_web";
        } else if (RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE.equalsIgnoreCase(type)) {
            typeValue = "EligibleCoupleService_web";
        }

        String coverHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "\n"
                + "<head>\n"
                + "    <meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n"
                + "    "
                + "    <style>\n"
                + "        body {\n"
                + "            margin-top: 10%;\n"
                + "            text-align: center;\n"
                + "            font-family: 'Tinos', serif;\n"
                + "            font-weight: bold;\n"
                + "            font-size: xx-large;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<h1>\n"
                + internationalizationService.getLabelByKeyAndLanguageCode("Reproductive&ChildHealthCareRegister_web", languageCode)
                + "</h1>\n"
                + "<br>\n"
                + "<h2>"
                + internationalizationService.getLabelByKeyAndLanguageCode(typeValue, languageCode)
                + "</h2>\n"
                + "<br>\n"
                + "<br>\n"
                + "<h2 style=\"text-decoration: underline; font-weight: normal;\">"
                + internationalizationService.getLabelByKeyAndLanguageCode("SEARCHPARAMETERS_web", languageCode)
                + " :</h2>\n"
                + "<br>\n"
                + "<br>\n"
                + "<h3> "
                + internationalizationService.getLabelByKeyAndLanguageCode("Location_web", languageCode)
                + " : <span  style=\"font-weight: normal;\">"
                + locationMasterDao.retrieveLocationFullNameById(locationId)
                + " </span></h3>\n"
                + "<h3> "
                + internationalizationService.getLabelByKeyAndLanguageCode("DateRange_web", languageCode)
                + " : <span  style=\"font-weight: normal;\">"
                + new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("MM/dd/yyyy").parse(fromDate.toString()))
                + " - "
                + new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("MM/dd/yyyy").parse(toDate.toString()))
                + " </span></h3>\n"
                + "</body>\n"
                + "\n"
                + "</html>";
        Path coverPath = Files.createTempFile("rch_register_report_cover", ".html");
        Files.write(coverPath, coverHtml.getBytes());

        String imageDirectoryPath = ConstantUtil.REPOSITORY_PATH + ConstantUtil.IMPLEMENTATION_TYPE + File.separator + "images" + File.separator;
        String gogImgPath = imageDirectoryPath + "govt_logo.png";
        String nhmImgPath = imageDirectoryPath + "nhm_logo.png";
        String techoImgPath = imageDirectoryPath + "web_logo.png";

        String headerHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + " <head>\n"
                + "   <h4 style=\"text-align:right;\">\n"
                + "   <img src=\"file://" + gogImgPath + "\" height=\"42\" width=\"25\">\n"
                + "   <img src=\"file://" + nhmImgPath + "\" height=\"35\" width=\"42\">\n"
                + "   <img src=\"file://" + techoImgPath + "\" height=\"30\" width=\"42\">\n"
                + "   </h4>\n"
                + "  </head>\n"
                + "</html>";
        Path headerPath = Files.createTempFile("rch_register_report_header", ".html");
        Files.write(headerPath, headerHtml.getBytes());
        String footerHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "\n"
                + "<head>\n"
                + "  <meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n"
                + "  <script>\n"
                + "    function subst() {\n"
                + "      var vars = {};\n"
                + "      var x = window.location.search.substring(1).split('&');\n"
                + "      for (var i in x) { var z = x[i].split('=', 2); vars[z[0]] = unescape(z[1]); }\n"
                + "      var x = ['frompage', 'topage', 'page', 'webpage', 'section', 'subsection', 'subsubsection'];\n"
                + "      for (var i in x) {\n"
                + "        var y = document.getElementsByClassName(x[i]);\n"
                + "        for (var j = 0; j < y.length; ++j) y[j].textContent = vars[x[i]];\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "\n"
                + "<body style=\"border:0; margin: 0;\" onload=\"subst()\">\n"
                + "  <table style=\"width: 100%\">\n"
                + "    <tr>\n"
                + "      <td>Generated by " + userName + " on\n"
                + "        <script>\n"
                + "          const current_datetime = new Date();\n"
                + "          const date = current_datetime.getDate() < 10 ? '0' + current_datetime.getDate() : current_datetime.getDate();\n"
                + "          const month = current_datetime.getMonth() < 9 ? '0' + (current_datetime.getMonth() + 1) : (current_datetime.getMonth() + 1);\n"
                + "          const year = current_datetime.getFullYear();\n"
                + "          const hours = current_datetime.getHours() < 10 ? '0' + current_datetime.getHours() : current_datetime.getHours();\n"
                + "          const minutes = current_datetime.getMinutes() < 10 ? '0' + current_datetime.getMinutes() : current_datetime.getMinutes();\n"
                + "          const seconds = current_datetime.getSeconds() < 10 ? '0' + current_datetime.getSeconds() : current_datetime.getSeconds();\n"
                + "\n"
                + "          document.write(date + \"-\" + month + \"-\" + year + \" \" + hours + \":\" + minutes + \":\" + seconds);\n"
                + "\n"
                + "        </script>\n"
                + "      </td>\n"
                + "      <td style=\"text-align:right\">\n"
                + "        Page <span class=\"page\"></span> of <span class=\"topage\"></span>\n"
                + "      </td>\n"
                + "    </tr>\n"
                + "  </table>\n"
                + "</body>\n"
                + "\n"
                + "</html>";
        Path footerPath = Files.createTempFile("rch_register_report_footer", ".html");
        Files.write(footerPath, footerHtml.getBytes());
        pdf.addParam(new Param("--enable-javascript"), new Param("--enable-local-file-access"),
                new Param("--header-html", headerPath.toString()),
                new Param("--header-spacing", "3"), new Param("--footer-html", footerPath.toString()),
                new Param("--footer-spacing", "3"), new Param("cover", coverPath.toString()),
                new Param("--exclude-from-outline"));
        // Save the PDF
        Date currentDate = new Date();
        String filePath = "rch_register_report_" + currentDate.getDate() + "_" + currentDate.getMonth() + "_"
                + currentDate.getYear() + "_" + currentDate.getTime() + ".pdf";
        Path tempOutputFile = Files.createTempFile(filePath, ".pdf");
        File file = pdf.saveAsDirect(tempOutputFile.toString());

        FileInputStream fis = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        try {
            fis = new FileInputStream(file);
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                outputStream.write(buf, 0, readNum); // no doubt here is 0
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (Objects.nonNull(fis)) {
                fis.close();
            }
        }


        Files.deleteIfExists(headerPath);
        Files.deleteIfExists(footerPath);
        Files.deleteIfExists(coverPath);

        return outputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteArrayOutputStream downLoadPdf(QueryDto queryDto)
            throws IOException, InterruptedException, ParseException {
        ByteArrayOutputStream fileOPStream = null;
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
        List<LinkedHashMap<String, Object>> resultOfQueryList = resultQueryDto.get(0).getResult();

        List<LinkedHashMap<String, Object>> memberServiceDetailedResultOjbectList = new ArrayList<>();
        String selectedServiceType = (String) queryDto.getParameters().get(RchConstants.SELECTED_SERVICE_TYPE);
        String languageCode = (String) queryDto.getParameters().get(RchConstants.SELECTED_LANGUAGE_CODE);
        Integer loggedInUserId = ((Number) queryDto.getParameters().get("loggedInUserId")).intValue();
        UserMaster user = userDao.retrieveById(loggedInUserId);
        String userName = user.getFirstName() + " " + user.getLastName();

        if (selectedServiceType.equals(RchConstants.RCH_MOTHER_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get("ref_id"));

                QueryDto motherServiceDetailedQueryDto = new QueryDto();
                motherServiceDetailedQueryDto.setCode("get_rch_register_mother_service_detailed_info");
                LinkedHashMap<String, Object> motherServiceDetailedQueryParameters = new LinkedHashMap<>();
                motherServiceDetailedQueryParameters.put(RchConstants.PREGNANCY_REGISTRATION_ID, refId);
                motherServiceDetailedQueryDto.setParameters(motherServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(motherServiceDetailedQueryDto);
                List<QueryDto> motherServiceDetailedQueryDtoList = queryMasterService
                        .executeQuery(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> motherServiceDetailedQueryResult = motherServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(motherServiceDetailedQueryResult);

            });

            fileOPStream = this.generatePDF(memberServiceDetailedResultOjbectList,
                    ((Number) queryDto.getParameters().get(RchConstants.LOCATION_ID)).intValue(),
                    RchConstants.RCH_MOTHER_SERVICE,
                    queryDto.getParameters().get(RchConstants.FROM_DATE),
                    queryDto.getParameters().get(RchConstants.TO_DATE), userName, languageCode);
        }

        if (selectedServiceType.equals(RchConstants.RCH_CHILD_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get(RchConstants.MEMBER_ID));

                QueryDto childServiceDetailedQueryDto = new QueryDto();
                childServiceDetailedQueryDto.setCode("get_rch_register_child_service_detailed_info");
                LinkedHashMap<String, Object> childServiceDetailedQueryParameters = new LinkedHashMap<>();
                childServiceDetailedQueryParameters.put(RchConstants.MEMBER_ID, refId);
                childServiceDetailedQueryDto.setParameters(childServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(childServiceDetailedQueryDto);
                List<QueryDto> childServiceDetailedQueryDtoList = queryMasterService
                        .execute(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> childServiceDetailedQueryResult = childServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(childServiceDetailedQueryResult);

            });

            fileOPStream = this.generatePDF(memberServiceDetailedResultOjbectList,
                    ((Number) queryDto.getParameters().get(RchConstants.LOCATION_ID)).intValue(),
                    RchConstants.RCH_CHILD_SERVICE, queryDto.getParameters().get(RchConstants.FROM_DATE),
                    queryDto.getParameters().get(RchConstants.TO_DATE), userName, languageCode);

        }

        if (selectedServiceType.equals(RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get(RchConstants.MEMBER_ID));

                QueryDto eligibleCoupleServiceDetailedQueryDto = new QueryDto();
                eligibleCoupleServiceDetailedQueryDto
                        .setCode("get_rch_register_eligible_couple_service_detailed_info");
                LinkedHashMap<String, Object> eligibleCoupleServiceDetailedQueryParameters = new LinkedHashMap<>();
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.MEMBER_ID, refId);
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.TO_DATE,
                        queryDto.getParameters().get(RchConstants.TO_DATE));
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.FROM_DATE,
                        queryDto.getParameters().get(RchConstants.FROM_DATE));
                eligibleCoupleServiceDetailedQueryDto
                        .setParameters(eligibleCoupleServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(eligibleCoupleServiceDetailedQueryDto);
                List<QueryDto> eligibleCoupleServiceDetailedQueryDtoList = queryMasterService
                        .executeQuery(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> eligibleCoupleServiceDetailedQueryResult = eligibleCoupleServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(eligibleCoupleServiceDetailedQueryResult);
            });

            fileOPStream = this.generatePDF(memberServiceDetailedResultOjbectList,
                    ((Number) queryDto.getParameters().get(RchConstants.LOCATION_ID)).intValue(),
                    RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE,
                    queryDto.getParameters().get(RchConstants.FROM_DATE),
                    queryDto.getParameters().get(RchConstants.TO_DATE), userName, languageCode);

        }

        return fileOPStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteArrayOutputStream downloadExcel(QueryDto queryDto) {

        ByteArrayOutputStream fileOPStream;
        List<LinkedHashMap<String, Object>> memberServiceDetailedResultOjbectList = this
                .getLocationWiseMemberList(queryDto);

        String selectedServiceType = (String) queryDto.getParameters().get(RchConstants.SELECTED_SERVICE_TYPE);
        fileOPStream = this.downLoadExcelByType(memberServiceDetailedResultOjbectList, selectedServiceType);

        return fileOPStream;
    }

    /**
     * Retrieves location wise member list.
     *
     * @param queryDto Query code to fetch member list.
     * @return Returns list of member details.
     */
    public List<LinkedHashMap<String, Object>> getLocationWiseMemberList(QueryDto queryDto) {

        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
        List<LinkedHashMap<String, Object>> resultOfQueryList = resultQueryDto.get(0).getResult();

        List<LinkedHashMap<String, Object>> memberServiceDetailedResultOjbectList = new ArrayList<>();
        String selectedServiceType = (String) queryDto.getParameters().get(RchConstants.SELECTED_SERVICE_TYPE);

        if (selectedServiceType.equals(RchConstants.RCH_MOTHER_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get("ref_id"));

                QueryDto motherServiceDetailedQueryDto = new QueryDto();
                motherServiceDetailedQueryDto.setCode("get_rch_register_mother_service_detailed_info");
                LinkedHashMap<String, Object> motherServiceDetailedQueryParameters = new LinkedHashMap<>();
                motherServiceDetailedQueryParameters.put(RchConstants.PREGNANCY_REGISTRATION_ID, refId);
                motherServiceDetailedQueryDto.setParameters(motherServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(motherServiceDetailedQueryDto);
                List<QueryDto> motherServiceDetailedQueryDtoList = queryMasterService
                        .executeQuery(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> motherServiceDetailedQueryResult = motherServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(motherServiceDetailedQueryResult);

            });

        }

        if (selectedServiceType.equals(RchConstants.RCH_CHILD_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get(RchConstants.MEMBER_ID));

                QueryDto childServiceDetailedQueryDto = new QueryDto();
                childServiceDetailedQueryDto.setCode("get_rch_register_child_service_detailed_info");
                LinkedHashMap<String, Object> childServiceDetailedQueryParameters = new LinkedHashMap<>();
                childServiceDetailedQueryParameters.put(RchConstants.MEMBER_ID, refId);
                childServiceDetailedQueryDto.setParameters(childServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(childServiceDetailedQueryDto);
                List<QueryDto> childServiceDetailedQueryDtoList = queryMasterService
                        .executeQuery(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> childServiceDetailedQueryResult = childServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(childServiceDetailedQueryResult);

            });

        }

        if (selectedServiceType.equals(RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE)) {

            resultOfQueryList.forEach(resultOfQuery -> {

                Integer refId = ((Integer) resultOfQuery.get(RchConstants.MEMBER_ID));

                QueryDto eligibleCoupleServiceDetailedQueryDto = new QueryDto();
                eligibleCoupleServiceDetailedQueryDto
                        .setCode("get_rch_register_eligible_couple_service_detailed_info");
                LinkedHashMap<String, Object> eligibleCoupleServiceDetailedQueryParameters = new LinkedHashMap<>();
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.MEMBER_ID, refId);
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.TO_DATE,
                        queryDto.getParameters().get(RchConstants.TO_DATE));
                eligibleCoupleServiceDetailedQueryParameters.put(RchConstants.FROM_DATE,
                        queryDto.getParameters().get(RchConstants.FROM_DATE));
                eligibleCoupleServiceDetailedQueryDto
                        .setParameters(eligibleCoupleServiceDetailedQueryParameters);
                List<QueryDto> queryDtosMemberDerailInfo = new LinkedList<>();
                queryDtosMemberDerailInfo.add(eligibleCoupleServiceDetailedQueryDto);
                List<QueryDto> eligibleCoupleServiceDetailedQueryDtoList = queryMasterService
                        .executeQuery(queryDtosMemberDerailInfo, true);
                LinkedHashMap<String, Object> eligibleCoupleServiceDetailedQueryResult = eligibleCoupleServiceDetailedQueryDtoList
                        .get(0).getResult().get(0);
                memberServiceDetailedResultOjbectList.add(eligibleCoupleServiceDetailedQueryResult);

            });

        }

        return memberServiceDetailedResultOjbectList;

    }

    /**
     * Write vitamin A details in excel sheet.
     *
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writeVitaminAInfoInExcelSheet(LinkedHashMap<String, Object> memberServiceDetailedResultOjbect,
                                               Row rowNumber, Metadata metadata) {
        String vitaminADoseArray = (String) memberServiceDetailedResultOjbect.get(RchConstants.VIT_A_DOSE);
        if (vitaminADoseArray != null) {
            String[] visitList = vitaminADoseArray.split(",");

            for (String visit : visitList) {
                Cell cellNumber = rowNumber.createCell(metadata.cellnum++);
                writeValueInCell(cellNumber, visit);

            }
        }
    }

    /**
     * Write lmp visit details in excel sheet.
     *
     * @param data      Member service details.
     * @param rowNumber Row number.
     * @param metadata  Meta data for pdf.
     */
    private void writeLMPVisitDataInExcel(LinkedHashMap<String, Object> data, Row rowNumber, Metadata metadata) {
        List<LinkedHashMap<String, Object>> lmpVisitInfoList = extractLMPVistInfoListFromResult(data);

        lmpVisitInfoList.forEach(lmpVisitInfo -> {
            String cellValue = null;

            String visitDay = (String) lmpVisitInfo.get(RchConstants.DATE);
            if (visitDay != null) {
                cellValue = visitDay;
            }

            String contraceptionMethod = (String) lmpVisitInfo.get(RchConstants.CONTRACEPTION_METHOD);
            if (contraceptionMethod != null) {
                cellValue = cellValue + "(" + contraceptionMethod + ")";
            }

            Cell cellNumber = rowNumber.createCell(metadata.cellnum++);
            writeValueInCell(cellNumber, cellValue);
        });

    }

    /**
     * Define metadata for pdf.
     */
    class Metadata {

        private int index = 1;
        private int rownum = 0;
        private int cellnum = 0;
        public int temprownum = 0;
        private int tempParentCellNum = 0;
        private int tempChildCellNum = 0;
        private int currentRowNum = 0;
        private int maxRowNumInOneRecord = 0;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteArrayOutputStream downLoadExcelByType(
            List<LinkedHashMap<String, Object>> memberServiceDetailedResultOjbectList,
            String selectedServiceType) {

        Metadata metadata = new Metadata();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
        style.setFont(font);

        metadata.index = 1;
        metadata.rownum = 0;
        metadata.cellnum = 0;
        metadata.temprownum = 0;
        metadata.tempParentCellNum = 0;
        metadata.tempChildCellNum = 0;
        metadata.currentRowNum = 0;
        metadata.maxRowNumInOneRecord = 0;

        switch (selectedServiceType) {
            case RchConstants.RCH_MOTHER_SERVICE: {

                Row titleNameRow = sheet.createRow(metadata.rownum++);

                RchConstants.RCH_REGISTER_MOTHER_SERVICE_TITLE.forEach((title, length) -> {
                    Cell titleNameCell = titleNameRow.createCell(metadata.cellnum);
                    titleNameCell.setCellValue(title);
                    titleNameCell.setCellStyle(style);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, metadata.cellnum,
                            metadata.cellnum + length - 1));
                    metadata.cellnum = metadata.cellnum + length;

                });

                Row subTitleRow = sheet.createRow(metadata.rownum++);

                metadata.cellnum = 0;

                // wriring the title of mother service
                writeDataTitleInExcel(RchConstants.RCH_REGISTER_MOTHER_SERVICE_KEY_LIST, subTitleRow, metadata,
                        style);
                writeDataTitleInExcel(RchConstants.PNC_VISIT_MOTHER_KEY, subTitleRow, metadata, style);
                writeDataTitleInExcel(RchConstants.PNC_VISIT_CHILD_KEY, subTitleRow, metadata, style);

                metadata.currentRowNum = metadata.rownum;

                memberServiceDetailedResultOjbectList.forEach(memberServiceDetailedResultOjbect -> {
                    metadata.rownum = metadata.currentRowNum;

                    metadata.cellnum = 0;
                    Row rowNumber = sheet.createRow(metadata.rownum);
                    // general information
                    writeGeneralInformationForMotherServiceInExcel(memberServiceDetailedResultOjbect,
                            rowNumber, metadata);

                    metadata.rownum = metadata.currentRowNum;
                    // predelivery care information
                    writePreviousPregnancyDetailsInfoForMotherServiceInExcel(sheet,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);

                    metadata.rownum = metadata.currentRowNum;
                    // predelivery care information
                    writePreDeliveryCareInfoForMotherServiceInExcel(sheet,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);

                    metadata.rownum = metadata.currentRowNum;
                    // delivery result informaiton
                    writeDeliveryResultInfoForMotherServiceInExcel(sheet, memberServiceDetailedResultOjbect,
                            rowNumber, metadata);

                    metadata.rownum = metadata.currentRowNum;
                    // pnc mother and child visit information
                    writePncVisitInfoMotherServiceInExcel(sheet, memberServiceDetailedResultOjbect,
                            rowNumber, metadata);

                    metadata.currentRowNum = metadata.maxRowNumInOneRecord + 2;

                });
                break;
            }
            case RchConstants.RCH_CHILD_SERVICE: {

                Row titleNameRow = sheet.createRow(metadata.rownum++);

                RchConstants.RCH_REGISTER_CHILD_SERVICE_TITLE.forEach((title, length) -> {
                    Cell titleNameCell = titleNameRow.createCell(metadata.cellnum);
                    titleNameCell.setCellValue(title);
                    titleNameCell.setCellStyle(style);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, metadata.cellnum,
                            metadata.cellnum + length - 1));
                    metadata.cellnum = metadata.cellnum + length;

                });

                Row subTitleRow = sheet.createRow(metadata.rownum++);

                metadata.cellnum = 0;

                writeDataTitleInExcel(RchConstants.RCH_REGISTER_CHILD_SERVICE_KEY_LIST, subTitleRow, metadata,
                        style);

                metadata.currentRowNum = metadata.rownum;

                memberServiceDetailedResultOjbectList.forEach(memberServiceDetailedResultOjbect -> {
                    metadata.rownum = metadata.currentRowNum;

                    metadata.cellnum = 0;
                    Row rowNumber = sheet.createRow(metadata.rownum);

                    // general information
                    writeDataValueInExcel(RchConstants.CHILD_GENERAL_INFORMATION_ONE,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);
                    writeDataValueInExcel(RchConstants.CHILD_GENERAL_INFORMATION_TWO,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);

                    // vaccination information
                    writeDataValueInExcel(RchConstants.VACCINATION_ONE, memberServiceDetailedResultOjbect,
                            rowNumber, metadata);
                    writeDataValueInExcel(RchConstants.VACCINATION_TWO, memberServiceDetailedResultOjbect,
                            rowNumber, metadata);

                    metadata.rownum = metadata.currentRowNum;
                    writeDataValueInExcel(RchConstants.SIDE_EEFECT_OF_VACC_ONE,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);
                    writeDataValueInExcel(RchConstants.SIDE_EEFECT_OF_VACC_TWO,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);

                    writeVitaminAInfoInExcelSheet(memberServiceDetailedResultOjbect, rowNumber, metadata);

                    metadata.currentRowNum = metadata.currentRowNum + 1;

                });

                break;
            }
            case RchConstants.RCH_ELIGIBLE_COUPLE_SERVICE: {

                Row titleNameRow = sheet.createRow(metadata.rownum++);

                RchConstants.RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.forEach((title, length) -> {
                    Cell titleNameCell = titleNameRow.createCell(metadata.cellnum);
                    titleNameCell.setCellValue(title);
                    titleNameCell.setCellStyle(style);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, metadata.cellnum,
                            metadata.cellnum + length - 1));
                    metadata.cellnum = metadata.cellnum + length;

                });

                Row subTitleRow = sheet.createRow(metadata.rownum++);

                metadata.cellnum = 0;

                writeDataTitleInExcel(RchConstants.RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST, subTitleRow,
                        metadata, style);

                metadata.currentRowNum = metadata.rownum;

                memberServiceDetailedResultOjbectList.forEach(memberServiceDetailedResultOjbect -> {
                    metadata.rownum = metadata.currentRowNum;

                    metadata.cellnum = 0;
                    Row rowNumber = sheet.createRow(metadata.rownum);

                    // general information
                    writeDataValueInExcel(
                            RchConstants.RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST,
                            memberServiceDetailedResultOjbect, rowNumber, metadata);

                    // lmp visit information
                    writeLMPVisitDataInExcel(memberServiceDetailedResultOjbect, rowNumber, metadata);
                    metadata.currentRowNum = metadata.currentRowNum + 1;

                });

                break;
            }
        }

        // Write the workbook in file system
        return generateExcelFile(workbook);
    }


    private ByteArrayOutputStream generateExcelFile(XSSFWorkbook workbook) {

        ByteArrayOutputStream excelByteArrayOPStream = new ByteArrayOutputStream();

        try {
            workbook.write(excelByteArrayOPStream);

        } catch (Exception e) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return excelByteArrayOPStream;
    }

    /**
     * Write general information for mother service details in excel sheet.
     *
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writeGeneralInformationForMotherServiceInExcel(
            LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
            Metadata metadata) {

        writeDataValueInExcel(RchConstants.GENERAL_INFORMATION_KEYS, memberServiceDetailedResultOjbect,
                rowNumber, metadata);

    }

    /**
     * Write previous pregnancy details info for mother service in excel sheet.
     *
     * @param sheet                             Instance of sheet in which data will be write.
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writePreviousPregnancyDetailsInfoForMotherServiceInExcel(XSSFSheet sheet,
                                                                          LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
                                                                          Metadata metadata) {
        List<LinkedHashMap<String, Object>> previousPregnancyDetailsJsonList = this
                .extractPreviousPregnancyDetailsJsonListFromResult(memberServiceDetailedResultOjbect);
        if (previousPregnancyDetailsJsonList.isEmpty()) {
            metadata.cellnum += RchConstants.PREVIOUS_PREGNANCY_DETAILS_KEYS.size();
        }
        previousPregnancyDetailsJsonList.forEach(previousPregnancyDetailsJson -> {
            if (previousPregnancyDetailsJsonList.indexOf(previousPregnancyDetailsJson) == 0) {
                metadata.tempParentCellNum = metadata.cellnum;

                writeDataValueInExcel(RchConstants.PREVIOUS_PREGNANCY_DETAILS_KEYS,
                        previousPregnancyDetailsJson, rowNumber, metadata);

            } else {

                metadata.cellnum = metadata.tempParentCellNum;

                Row existingRowNumberForMultipleRecord = sheet.getRow(++metadata.rownum);
                if (existingRowNumberForMultipleRecord != null) {

                    writeDataValueInExcel(RchConstants.PREVIOUS_PREGNANCY_DETAILS_KEYS,
                            previousPregnancyDetailsJson,
                            existingRowNumberForMultipleRecord, metadata);
                    metadata.rownum++;

                } else {

                    Row rowNumberForMultipleRecord = sheet.createRow(metadata.rownum);
                    writeDataValueInExcel(RchConstants.PREVIOUS_PREGNANCY_DETAILS_KEYS,
                            previousPregnancyDetailsJson, rowNumberForMultipleRecord,
                            metadata);
                }

            }

        });

        if (metadata.maxRowNumInOneRecord < metadata.rownum) {
            metadata.maxRowNumInOneRecord = metadata.rownum;
        }
    }

    /**
     * Write pre delivery care info for mother service in excel sheet.
     *
     * @param sheet                             Instance of sheet in which data will be write.
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writePreDeliveryCareInfoForMotherServiceInExcel(XSSFSheet sheet,
                                                                 LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
                                                                 Metadata metadata) {

        List<LinkedHashMap<String, Object>> preDeliveryCareJsonList = this
                .extractPreDeliveryCareJsonListFromResult(memberServiceDetailedResultOjbect);
        if (preDeliveryCareJsonList.isEmpty()) {
            metadata.cellnum += RchConstants.PRE_DELIVERY_CARE_INFO_KEY.size();
        }
        preDeliveryCareJsonList.forEach(preDeliveryCare -> {
            if (preDeliveryCareJsonList.indexOf(preDeliveryCare) == 0) {
                metadata.tempParentCellNum = metadata.cellnum;

                writeDataValueInExcel(RchConstants.PRE_DELIVERY_CARE_INFO_KEY, preDeliveryCare,
                        rowNumber, metadata);

            } else {

                metadata.cellnum = metadata.tempParentCellNum;

                Row existingRowNumberForMultipleRecord = sheet.getRow(++metadata.rownum);
                if (existingRowNumberForMultipleRecord != null) {

                    writeDataValueInExcel(RchConstants.PRE_DELIVERY_CARE_INFO_KEY, preDeliveryCare,
                            existingRowNumberForMultipleRecord, metadata);

                } else {

                    Row rowNumberForMultipleRecord = sheet.createRow(metadata.rownum);
                    writeDataValueInExcel(RchConstants.PRE_DELIVERY_CARE_INFO_KEY, preDeliveryCare,
                            rowNumberForMultipleRecord, metadata);
                }

            }

        });

        if (metadata.maxRowNumInOneRecord < metadata.rownum) {
            metadata.maxRowNumInOneRecord = metadata.rownum;
        }

    }

    /**
     * Write delivery result info for mother service in excel sheet.
     *
     * @param sheet                             Instance of sheet in which data will be write.
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writeDeliveryResultInfoForMotherServiceInExcel(XSSFSheet sheet,
                                                                LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
                                                                Metadata metadata) {
        List<LinkedHashMap<String, Object>> deliveryResultJsonList = this
                .extractDeliveryResultJsonListFromResult(memberServiceDetailedResultOjbect);

        if (deliveryResultJsonList.isEmpty()) {
            metadata.cellnum = metadata.cellnum + RchConstants.DELIVERY_RESULT_BASIC_INFO_KEY.size()
                    + RchConstants.DELIVERY_RESULT_INFANT_INFO_KEY.size();
        }

        deliveryResultJsonList.forEach(deliveryResultJson -> {

            if (deliveryResultJsonList.indexOf(deliveryResultJson) == 0) {
                writeDataValueInExcel(RchConstants.DELIVERY_RESULT_BASIC_INFO_KEY, deliveryResultJson,
                        rowNumber, metadata);
                metadata.tempChildCellNum = metadata.cellnum;
                writeDataValueInExcel(RchConstants.DELIVERY_RESULT_INFANT_INFO_KEY, deliveryResultJson,
                        rowNumber, metadata);
            } else {

                metadata.cellnum = metadata.tempChildCellNum;

                Row existingRowNumberForMultipleRecord = sheet.getRow(++metadata.rownum);
                if (existingRowNumberForMultipleRecord != null) {
                    writeDataValueInExcel(RchConstants.DELIVERY_RESULT_INFANT_INFO_KEY,
                            deliveryResultJson, existingRowNumberForMultipleRecord,
                            metadata);

                } else {

                    Row rowNumberForMultipleRecord = sheet.createRow(metadata.rownum);
                    writeDataValueInExcel(RchConstants.DELIVERY_RESULT_INFANT_INFO_KEY,
                            deliveryResultJson, rowNumberForMultipleRecord, metadata);

                }

            }

        });

        if (metadata.maxRowNumInOneRecord < metadata.rownum) {
            metadata.maxRowNumInOneRecord = metadata.rownum;
        }
    }

    /**
     * Write pnc visit info for mother service in excel sheet.
     *
     * @param sheet                             Instance of sheet in which data will be write.
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Meta data for pdf.
     */
    private void writePncVisitInfoMotherServiceInExcel(final XSSFSheet sheet,
                                                       LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
                                                       Metadata metadata) {
        List<LinkedHashMap<String, Object>> pncVisitJsonList = this
                .extractPNCVisitJsonListFromResult(memberServiceDetailedResultOjbect);
        metadata.tempParentCellNum = metadata.cellnum;
        if (pncVisitJsonList.isEmpty()) {
            metadata.cellnum = metadata.cellnum + RchConstants.PNC_VISIT_MOTHER_KEY.size()
                    + RchConstants.PNC_VISIT_CHILD_KEY.size();
        }

        pncVisitJsonList.forEach(pncVisitJson -> {

            if (pncVisitJsonList.indexOf(pncVisitJson) == 0) {

                metadata.cellnum = metadata.tempParentCellNum;
                writeDataValueInExcel(RchConstants.PNC_VISIT_MOTHER_KEY, pncVisitJson, rowNumber,
                        metadata);
                metadata.tempChildCellNum = metadata.cellnum;
                List<LinkedHashMap<String, Object>> pncChildVisitJsonList = this
                        .extractPNCChildVisitJsonListFromResult(pncVisitJson);

                writePncChildVisitInfoInExcel(sheet, pncChildVisitJsonList, rowNumber, metadata);
            } else {

                metadata.cellnum = metadata.tempParentCellNum;

                Row existingRowNumberForMultipleRecord = sheet.getRow(++metadata.rownum);
                if (existingRowNumberForMultipleRecord != null) {
                    writeDataValueInExcel(RchConstants.PNC_VISIT_MOTHER_KEY, pncVisitJson,
                            existingRowNumberForMultipleRecord, metadata);
                    metadata.tempChildCellNum = metadata.cellnum;
                    List<LinkedHashMap<String, Object>> pncChildVisitJsonList = this
                            .extractPNCChildVisitJsonListFromResult(pncVisitJson);

                    writePncChildVisitInfoInExcel(sheet, pncChildVisitJsonList,
                            existingRowNumberForMultipleRecord, metadata);
                } else {

                    Row rowNumberForMultipleRecord = sheet.createRow(metadata.rownum);
                    writeDataValueInExcel(RchConstants.PNC_VISIT_MOTHER_KEY, pncVisitJson,
                            rowNumberForMultipleRecord, metadata);
                    metadata.tempChildCellNum = metadata.cellnum;
                    List<LinkedHashMap<String, Object>> pncChildVisitJsonList = this
                            .extractPNCChildVisitJsonListFromResult(pncVisitJson);

                    writePncChildVisitInfoInExcel(sheet, pncChildVisitJsonList,
                            rowNumberForMultipleRecord, metadata);
                }

            }
        });

        if (metadata.maxRowNumInOneRecord < metadata.rownum) {
            metadata.maxRowNumInOneRecord = metadata.rownum;
        }
    }

    /**
     * Write pnc child visit info for mother service in excel sheet.
     *
     * @param sheet                 Instance of sheet in which data will be write.
     * @param pncChildVisitJsonList Pnc child visit service.
     * @param rowNumber             Row number.
     * @param metadata              Meta data for pdf.
     */
    private void writePncChildVisitInfoInExcel(XSSFSheet sheet,
                                               List<LinkedHashMap<String, Object>> pncChildVisitJsonList, Row rowNumber, Metadata metadata) {
        metadata.tempChildCellNum = metadata.cellnum;
        if (pncChildVisitJsonList.isEmpty()) {
            metadata.cellnum = metadata.cellnum + RchConstants.PNC_VISIT_MOTHER_KEY.size()
                    + RchConstants.PNC_VISIT_CHILD_KEY.size();
        }

        pncChildVisitJsonList.forEach(pncChildVisitJson -> {

            if (pncChildVisitJsonList.indexOf(pncChildVisitJson) == 0) {

                metadata.cellnum = metadata.tempChildCellNum;
                writeDataValueInExcel(RchConstants.PNC_VISIT_CHILD_KEY, pncChildVisitJson, rowNumber,
                        metadata);

            } else {

                metadata.cellnum = metadata.tempChildCellNum;
                Row pchChildRow = sheet.getRow(++metadata.rownum);
                if (pchChildRow != null) {
                    writeDataValueInExcel(RchConstants.PNC_VISIT_CHILD_KEY, pncChildVisitJson,
                            pchChildRow, metadata);

                } else {

                    Row rowNumberForMultipleRecord = sheet.createRow(++metadata.rownum);
                    writeDataValueInExcel(RchConstants.PNC_VISIT_CHILD_KEY, pncChildVisitJson,
                            rowNumberForMultipleRecord, metadata);
                }
            }
        });

        if (metadata.maxRowNumInOneRecord < metadata.rownum) {
            metadata.maxRowNumInOneRecord = metadata.rownum;
        }
    }

    /**
     * Extract pre delivery care list into result.
     *
     * @param resultOfQuery Result query.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractPreDeliveryCareJsonListFromResult(
            Map<String, Object> resultOfQuery) {
        String preDeliveryCareString = (String) resultOfQuery.get("pre_delivery_care_json");
        List<LinkedHashMap<String, Object>> preDeliveryCareJsonList = new ArrayList<>();
        if (preDeliveryCareString != null) {
            List fromJson = new Gson().fromJson(preDeliveryCareString, List.class);
            List<String> stringJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

            stringJsonList.forEach(stringJson ->
                    preDeliveryCareJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {

                            }.getType()))
            );
        }

        return preDeliveryCareJsonList;
    }

    /**
     * Extract previous pregnancy details list into result.
     *
     * @param resultOfQuery Result query.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractPreviousPregnancyDetailsJsonListFromResult(
            Map<String, Object> resultOfQuery) {
        String previousPregnancyDetailsString = (String) resultOfQuery.get("previous_pregnancy_details_json");
        List<LinkedHashMap<String, Object>> previousPregnancyDetailsJsonList = new ArrayList<>();
        if (previousPregnancyDetailsString != null) {
            List fromJson = new Gson().fromJson(previousPregnancyDetailsString, List.class);
            List<String> stringJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

            stringJsonList.forEach(stringJson ->
                    previousPregnancyDetailsJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {

                            }.getType()))
            );
        }

        return previousPregnancyDetailsJsonList;
    }

    /**
     * Extract delivery result details list into result.
     *
     * @param resultOfQuery Result query.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractDeliveryResultJsonListFromResult(
            Map<String, Object> resultOfQuery) {
        String deliveryResultString = (String) resultOfQuery.get("delivery_result_json");
        List<LinkedHashMap<String, Object>> deliveryResultJsonList = new ArrayList<>();
        if (deliveryResultString != null) {
            List fromJson = new Gson().fromJson(deliveryResultString, List.class);
            List<String> stringJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

            stringJsonList.forEach(stringJson ->
                    deliveryResultJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {

                            }.getType()))
            );
        }

        return deliveryResultJsonList;
    }

    /**
     * Extract pnc visit details list into result.
     *
     * @param resultOfQuery Result query.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractPNCVisitJsonListFromResult(
            Map<String, Object> resultOfQuery) {
        String pncVisitString = (String) resultOfQuery.get("pnc_visit_json");
        List<LinkedHashMap<String, Object>> pncVisitJsonList = new ArrayList<>();
        if (pncVisitString != null) {
            List fromJson = new Gson().fromJson(pncVisitString, List.class);
            List<String> stringJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringJsonList.add(new Gson().toJson(jsonObj)));

            stringJsonList.forEach(stringJson ->
                    pncVisitJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {

                            }.getType()))
            );
        }

        return pncVisitJsonList;
    }

    /**
     * Extract pnc child visit details list into result.
     *
     * @param pncVisitJson Pnc visit json.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractPNCChildVisitJsonListFromResult(
            Map<String, Object> pncVisitJson) {
        String pncChildVisitString = (String) pncVisitJson.get("child_pnc_dto");
        List<LinkedHashMap<String, Object>> pncChildVisitJsonList = new ArrayList<>();
        if (pncChildVisitString != null) {

            List fromJson = new Gson().fromJson(pncChildVisitString, List.class);
            List<String> stringChildJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringChildJsonList.add(new Gson().toJson(jsonObj)));

            stringChildJsonList.forEach(stringJson ->
                    pncChildVisitJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {
                            }.getType()))
            );
        }

        return pncChildVisitJsonList;
    }

    /**
     * Extract lmp visit details list into result.
     *
     * @param result Result query.
     * @return Returns result.
     */
    public List<LinkedHashMap<String, Object>> extractLMPVistInfoListFromResult(
            Map<String, Object> result) {
        String lmpVisitString = (String) result.get(RchConstants.LMP_VISIT_INFO);
        List<LinkedHashMap<String, Object>> lmpVisitJsonList = new ArrayList<>();
        if (lmpVisitString != null) {

            List fromJson = new Gson().fromJson(lmpVisitString, List.class);
            List<String> stringChildJsonList = new ArrayList<>();
            fromJson.forEach(jsonObj -> stringChildJsonList.add(new Gson().toJson(jsonObj)));

            stringChildJsonList.forEach(stringJson ->
                    lmpVisitJsonList.add(new Gson().fromJson(stringJson,
                            new TypeToken<LinkedHashMap<String, Object>>() {
                            }.getType()))
            );
        }

        return lmpVisitJsonList;
    }

    /**
     * Write data value into the cell of sheet
     *
     * @param cellNumber Cell of sheet.
     * @param value      Value will be write in the cell.
     */
    private void writeValueInCell(Cell cellNumber, Object value) {
        SimpleDateFormat dateFomate = new SimpleDateFormat("dd-MM-yyyy");
        if (value != null) {
            if (value instanceof String) {
                cellNumber.setCellValue((String) value);
            } else if (value instanceof Date) {
                cellNumber.setCellValue(dateFomate.format(value));
            } else if (value instanceof java.sql.Date) {
                cellNumber.setCellValue(dateFomate.format((java.sql.Date) value));
            } else if (value instanceof Boolean) {
                cellNumber.setCellValue(value.toString());
            } else if (value instanceof Integer) {
                cellNumber.setCellValue(((Integer) value));
            } else if (value instanceof Float) {
                cellNumber.setCellValue((Float) value);
            } else if (value instanceof java.math.BigInteger) {
                cellNumber.setCellValue(((BigInteger) value).intValue());
            } else if (value instanceof java.math.BigDecimal) {
                cellNumber.setCellValue(((BigDecimal) value).intValue());
            } else if (value instanceof Double) {
                cellNumber.setCellValue(((Double) value));
            }
        } else {
            cellNumber.setCellValue("N/A");
        }

    }

    /**
     * Write data value into the excel
     *
     * @param keyset                            Set of keys.
     * @param memberServiceDetailedResultOjbect Member service details.
     * @param rowNumber                         Row number.
     * @param metadata                          Metadata for pdf.
     */
    private void writeDataValueInExcel(Map<String, String> keyset,
                                       LinkedHashMap<String, Object> memberServiceDetailedResultOjbect, Row rowNumber,
                                       Metadata metadata) {

        keyset.forEach((key, keyValue) -> {
            if (key.equals(RchConstants.SR_NO)) {
                Cell srNoCell = rowNumber.createCell(metadata.cellnum++);
                writeValueInCell(srNoCell, metadata.index++);
            } else {
                Cell cellNumber = rowNumber.createCell(metadata.cellnum++);
                Object value = memberServiceDetailedResultOjbect.get(key);
                writeValueInCell(cellNumber, value);
            }

        });

    }

    /**
     * Write data value into the excel
     *
     * @param keyset   Set of keys.
     * @param style    Style of cell.
     * @param metadata Metadata for pdf.
     */
    private void writeDataTitleInExcel(Map<String, String> keyset, Row row, Metadata metadata, CellStyle style) {

        keyset.forEach((key, keyValue) -> {
            Cell subTitleCell = row.createCell(metadata.cellnum++);
            subTitleCell.setCellValue(keyValue);
            subTitleCell.setCellStyle(style);
        });

    }

}
