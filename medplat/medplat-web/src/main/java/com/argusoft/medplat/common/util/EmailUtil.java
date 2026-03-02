/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.util;

import com.argusoft.medplat.common.model.Attachment;
import com.argusoft.medplat.common.model.Email;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.EmailService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.service.impl.EmailServiceImpl;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.course.dto.LmsMobileEventDto;
import com.argusoft.medplat.mobile.dto.ParsedRecordBean;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;


/**
 * An util class for email
 * @author kunjan
 * @since 31/08/2020 4:30
 */
@Service
public class EmailUtil {

    @Autowired
    private SystemConfigurationService systemConfigurationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImtechoSecurityUser securityUser;

    private static final String HTML_CLOSE_TAG="</html>";
    private static final String H3_CLOSE_TAG="</h3>";
    private static final String BODY_CLOSE_TAG="</body>";
    private static final String DEFAULT="DEFAULT";
    private static final String HORIZONTAL_RULE="<hr style='border-bottom:solid 1px #9e0709; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>";

    /**
     * Sends an email of exception
     * @param t An instance of exception
     * @param parsedRecordBean An instance of ParsedRecordBean
     */
    public void sendExceptionEmail(Exception t, ParsedRecordBean parsedRecordBean) {

        if (ConstantUtil.SERVER_TYPE != null && (ConstantUtil.SERVER_TYPE.equals("DEV"))) {
            return;
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = writer.toString();

        Email email = new Email();
        String[] sendToList;
        String mailId = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SEND_TO;
        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SEND_TO_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailId = systemConfiguration.getKeyValue();
            sendToList = mailId.split(",");
        } else {
            sendToList = new String[]{mailId};
        }

        email.setTo(sendToList);
        String mailSubject = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SUBJECT;
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SUBJECT_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailSubject = systemConfiguration.getKeyValue();
        }

        email.setSubject(mailSubject);
        StringBuilder msg = new StringBuilder();
        appendHeader(msg);
        msg.append(ConstantUtil.DEFAULT_WS_EXCEPTION_MAIL_MESSAGE);
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_MESSAGE_WS_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            msg.append(" - ").append(systemConfiguration.getKeyValue());
        }

        msg.append(H3_CLOSE_TAG);
        msg.append(HORIZONTAL_RULE);
        if (parsedRecordBean != null) {
            msg.append(parsedRecordBean);
        } else {
            msg.append("Web service exception");
        }
        msg.append(HORIZONTAL_RULE);
        msg.append("<br/><div>With Best Wishes, <br/>Team TeCHO.</div>");
        msg.append(BODY_CLOSE_TAG);
        msg.append(HTML_CLOSE_TAG);

        email.setMessageBody(msg.toString());

        Attachment attachment1 = new Attachment();
        attachment1.setFileContent(s.getBytes());
        attachment1.setFileName("EXCEPTION_ " + new Date() + ".txt");
        email.setAttachment(new Attachment[]{attachment1});
        email.setConnectionType(DEFAULT);

        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }

        emailService.sendMail(email);
    }

    /**
     * Sends an email of given message
     * @param message A message value
     */
    public void sendEmail(String message) {

        if (ConstantUtil.SERVER_TYPE != null && ConstantUtil.SERVER_TYPE.equals("DEV")) {
            return;
        }

        Email email = new Email();
        String[] sendToList;
        String mailId = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SEND_TO;
        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SEND_TO_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailId = systemConfiguration.getKeyValue();
            sendToList = mailId.split(",");
        } else {
            sendToList = new String[]{mailId};
        }

        email.setTo(sendToList);
        email.setSubject(message);
        StringBuilder msg = new StringBuilder();
        appendHeader(msg);
        msg.append(ConstantUtil.DEFAULT_WS_MAIL_MESSAGE);

        msg.append(H3_CLOSE_TAG);
        msg.append(HORIZONTAL_RULE);
        msg.append(message);
        msg.append(HORIZONTAL_RULE);
        msg.append("<br/><div>With Best Wishes, <br/>Team Sewa.</div>");
        msg.append(BODY_CLOSE_TAG);
        msg.append(HTML_CLOSE_TAG);

        email.setMessageBody(msg.toString());
        email.setConnectionType(DEFAULT);
        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }

        emailService.sendMail(email);
    }

    /**
     * Sends an email of given parameter
     * @param sendTo A list of sender
     * @param subject A value of subject
     * @param messageBody A value of message body
     */
    public void sendEmail(String[] sendTo, String subject, String messageBody) {
        if (ConstantUtil.SERVER_TYPE != null && ConstantUtil.SERVER_TYPE.equals("DEV")) {
            return;
        }
        Email email = new Email();
        email.setTo(sendTo);
        email.setSubject(subject);
        email.setMessageBody(messageBody);
        email.setConnectionType(DEFAULT);
        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }
        emailService.sendMail(email);
    }

    /**
     * Sends an email of given exception
     * @param t An instance of Exception
     * @param parsedRecordBean An instance of ParsedRecordBean
     * @param request An instance of HttpRequest
     * @param message A value of message
     */
    public void sendExceptionEmail(Exception t, ParsedRecordBean parsedRecordBean, HttpServletRequest request, String message) {
        if (ConstantUtil.SERVER_TYPE != null && ConstantUtil.SERVER_TYPE.equals("DEV")) {
            return;
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = message + writer.toString();

        Email email = new Email();
        String[] sendToList;
        String mailId = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SEND_TO;
        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SEND_TO_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailId = systemConfiguration.getKeyValue();
            sendToList = mailId.split(",");
        } else {
            sendToList = new String[]{mailId};
        }

        email.setTo(sendToList);
        String mailSubject = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SUBJECT;
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SUBJECT_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailSubject = systemConfiguration.getKeyValue();
        }

        email.setSubject(mailSubject);
        StringBuilder msg = new StringBuilder();
        appendHeader(msg);
        msg.append(ConstantUtil.DEFAULT_WS_EXCEPTION_MAIL_MESSAGE);
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_MESSAGE_WS_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            msg.append(" - ").append(systemConfiguration.getKeyValue());
        }

        msg.append(H3_CLOSE_TAG);
        msg.append(HORIZONTAL_RULE);
        if (parsedRecordBean != null) {
            msg.append(parsedRecordBean);
        } else {
            msg.append("Web service exception");

        }

        if (request.getRequestURL() != null) {
            msg.append("<br/><div>URL: ").append(request.getRequestURL().toString()).append("</div>");
        }
        if (securityUser != null && securityUser.getUserName() != null) {
            msg.append("<br/><div>User Name: ").append(securityUser.getUserName()).append("</div>");
        }

        msg.append(HORIZONTAL_RULE);
        msg.append("<br/><div>With Best Wishes, <br/>Team Techo.</div>");
        msg.append(BODY_CLOSE_TAG);
        msg.append(HTML_CLOSE_TAG);

        email.setMessageBody(msg.toString());

        Attachment attachment1 = new Attachment();
        attachment1.setFileContent(s.getBytes());
        attachment1.setFileName("EXCEPTION_ " + new Date() + ".txt");
        email.setAttachment(new Attachment[]{attachment1});
        email.setConnectionType(DEFAULT);

        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }
        emailService.sendMail(email);
    }

    /**
     * Append header to email message
     * @param msg A message to add header
     */
    private void appendHeader(StringBuilder msg){
        msg.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
        msg.append("<head>");
        msg.append("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' />");
        msg.append("<title>Sewa Service Exception</title>");
        msg.append("</head>");
        msg.append("<body>");
        msg.append("<div style='margin:0 auto; font-family: Arial, Helvetica, sans-serif; font-size:12px;' > <hr style='border-bottom:solid 1px #9e0709; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
        msg.append("<h3>");
    }

    public void sendLmsEventExceptionEmail(Exception t, LmsMobileEventDto lmsMobileEventDto) {

        if (ConstantUtil.SERVER_TYPE != null && (ConstantUtil.SERVER_TYPE.equals("DEV"))) {
            return;
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        String s = writer.toString();

        Email email = new Email();
        String sendToList[] = null;
        String mailId = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SEND_TO;
        SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SEND_TO_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailId = systemConfiguration.getKeyValue();
            sendToList = mailId.split(",");
        } else {
            sendToList = new String[]{mailId};
        }

        email.setTo(sendToList);
        String mailSubject = ConstantUtil.DEFAULT_EXCEPTION_MAIL_SUBJECT;
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_SUBJECT_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            mailSubject = systemConfiguration.getKeyValue();
        }

        email.setSubject(mailSubject);
        StringBuilder msg = new StringBuilder();
        msg.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
        msg.append("<head>");
        msg.append("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' />");
        msg.append("<title>Sewa Service Exception</title>");
        msg.append("</head>");
        msg.append("<body>");
        msg.append("<div style='margin:0 auto; font-family: Arial, Helvetica, sans-serif; font-size:12px;' > <hr style='border-bottom:solid 1px #9e0709; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
        msg.append("<h3>");
        msg.append(ConstantUtil.DEFAULT_WS_EXCEPTION_MAIL_MESSAGE);
        systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.EXCEPTION_MAIL_MESSAGE_WS_TECHO);

        if (systemConfiguration != null && systemConfiguration.getKeyValue() != null) {
            msg.append(" - ").append(systemConfiguration.getKeyValue());
        }

        msg.append("</h3>");
        msg.append("<hr style='border-bottom:solid 1px #9e0709; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
        if (lmsMobileEventDto != null) {
            msg.append(new Gson().toJson(lmsMobileEventDto));
        } else {
            msg.append("Web service exception");
        }
        msg.append("<hr style='border-bottom:solid 1px #9e0709; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
        msg.append("<br/><div>With Best Wishes, <br/>Team TeCHO.</div>");
        msg.append("</body>");
        msg.append("</html>");

        email.setMessageBody(msg.toString());

        Attachment attachment1 = new Attachment();
        attachment1.setFileContent(s.getBytes());
        attachment1.setFileName("EXCEPTION_ " + new Date() + ".txt");
        email.setAttachment(new Attachment[]{attachment1});
        email.setConnectionType("DEFAULT");

        if (emailService == null) {
            emailService = new EmailServiceImpl();
        }

        emailService.sendMail(email);
    }


}