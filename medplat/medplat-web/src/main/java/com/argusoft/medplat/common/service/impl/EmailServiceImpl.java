
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.model.Attachment;
import com.argusoft.medplat.common.model.Email;
import com.argusoft.medplat.common.service.EmailService;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.EmailException;
import com.argusoft.medplat.exception.ImtechoSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements methods of EmailService
 * @author kunjan
 * @since 28/08/2020 4:30
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final String FILENAME = "email.properties";
    List<File> attachmentFiles = new LinkedList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMail(Email email) {
        sendMail(email, getPropertyFileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMail(Email email, String emailPropertyFile) {
        String filepath = getFilepath(emailPropertyFile);
        try {
            if (email.getIsSecure() == null) {
                email.setIsSecure(isEmailSecureOrnot(filepath));
            }
            if (email.getConnectionType().equalsIgnoreCase("DEFAULT")) {
                handleServerAddressAndPortForMail(email,filepath);
                handleUsernameAndPasswordForMail(email,filepath);
            }
            if (email.getFrom() == null) {
                email.setFrom(setFromAddressForMail(filepath));
            }
            if (email.getReply() == null) {
                email.setReply(setReplyForMail(filepath));
            }
            Properties props = new Properties(System.getProperties());   //this is used to set required properties for sending email.
            props.put("mail.smtp.host", email.getServerAddress());
            props.put("mail.smtp.port", email.getPort());
            props.put("mail.debug", "true");

            Session session;
            // Get a mail session if it is secure then get session with authentication else
            // get session without authentication.
            if (Boolean.TRUE.equals(email.getIsSecure())) {
                final String username = email.getUsername();
                final String password = email.getPassword();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.port", email.getPort());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                session = Session.getInstance(props,
                        new javax.mail.Authenticator() {

                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

            } else {
                session = Session.getInstance(props);
            }

            // Define a new mail message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFrom()));
            setOtherMailDetails(email);
            int attachmentLength = email.getAttachment().length;
            setMessageForEmail(email,message);

            if (email.getSubject() == null) {
                email.setSubject("");
            }
            message.setSubject(email.getSubject());

            // Create a message part to represent the body text
            BodyPart messageBodyPart = new MimeBodyPart();

            if (email.getMessageBody() == null) {
                email.setMessageBody("");
            }
            messageBodyPart.setContent(email.getMessageBody(), "text/html");

            //use a MimeMultipart as we need to handle the file attachments
            Multipart multipart = new MimeMultipart();

            //add the message body to the mime message
            multipart.addBodyPart(messageBodyPart);

            // add any file attachments to the message
            if (attachmentLength > 0) {
                addAtachments(email.getAttachment(), multipart);
            }

            // Put all message parts in the message
            message.setContent(multipart);
            // Send the message

            Transport.send(message);
            removeAttachments();
        } catch (Exception addressException) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, addressException);

        }
    }

    /**
     * Set message details for email.
     * @param email Define instance of email.
     * @param message Define instance of message.
     * @throws MessagingException Throws messaging exception.
     */
    private void setMessageForEmail(Email email,Message message) throws MessagingException{
        int lengthOfToAddresses = email.getTo().length;
        int lengthOfCcAddresses = email.getCc().length;
        int lengthOfBccAddresses = email.getBcc().length;
        int lengthOfReplyToAddresses = email.getReply().length;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        InternetAddress[] toAddresses = new InternetAddress[lengthOfToAddresses];
        InternetAddress[] ccAddresses = new InternetAddress[lengthOfCcAddresses];
        InternetAddress[] bccAddresses = new InternetAddress[lengthOfBccAddresses];
        InternetAddress[] replyAddresses = new InternetAddress[lengthOfReplyToAddresses];
        for (String s : email.getTo()) {
            toAddresses[i++] = new InternetAddress(s);
        }
        message.addRecipients(Message.RecipientType.TO, toAddresses);

        if (lengthOfCcAddresses > 0) {
            for (String s : email.getCc()) {
                ccAddresses[j++] = new InternetAddress(s);
            }
            message.addRecipients(Message.RecipientType.CC, ccAddresses);
        }
        if (lengthOfBccAddresses > 0) {
            for (String s : email.getBcc()) {
                bccAddresses[k++] = new InternetAddress(s);
            }
            message.addRecipients(Message.RecipientType.BCC, bccAddresses);
        }

        if (lengthOfReplyToAddresses > 0) {
            for (String s : email.getReply()) {
                replyAddresses[l++] = new InternetAddress(s);
            }
            message.setReplyTo(replyAddresses);
        }
    }

    /**
     * Set sever address and port for mail.
     * @param email Define instance of email.
     * @param filepath Define file path.
     * @throws EmailException Throws email exception.
     */
    private void handleServerAddressAndPortForMail(Email email,String filepath) throws EmailException{
        String msg;
        String serverAddress = this.getPropertyValue("emailServer", filepath);
        if (serverAddress != null) {
            email.setServerAddress(serverAddress);
        } else {
            msg = "server address is not set in property file";
            throw new EmailException(msg);
        }
        String port = this.getPropertyValue("emailPort", filepath);
        if (port != null) {
            email.setPort(port);
        } else {
            msg = "port number is not set in property file";
            throw new EmailException(msg);
        }
    }

    /**
     * Set cc,bcc,attachment for mail.
     * @param email Define instance of email.
     * @throws EmailException Throws email exception.
     */
    private void setOtherMailDetails(Email email) throws EmailException{
        String[] emptyArray = new String[0];  // this is used to set array elements when user does not enter any value for that kind of array.
        Attachment[] emptyAttachments = new Attachment[0];  // this is used to set array elements when user does not enter any value for that kind of array.
        String msg;
        if (email.getTo() == null) {
            msg = "not any to address is specified";
            throw new EmailException(msg);
        }
        if (email.getCc() == null) {
            email.setCc(emptyArray);
        }
        if (email.getBcc() == null) {
            email.setBcc(emptyArray);
        }
        if (email.getAttachment() == null) {
            email.setAttachment(emptyAttachments);
        }
    }

    /**
     * Set username and password details for mail.
     * @param email Define instance of email.
     * @param filepath Define file path.
     * @throws EmailException Throws email exception.
     */
    private void handleUsernameAndPasswordForMail(Email email,String filepath) throws EmailException{
        String msg;
        String username = this.getPropertyValue("emailUsername", filepath);
        if (username != null || !email.getIsSecure()) {
            if (username == null && !email.getIsSecure()) {
                email.setUsername("");
            } else {
                email.setUsername(username);
            }
        } else {
            msg = "username is not set in property file";
            throw new EmailException(msg);
        }
        String password = this.getPropertyValue("emailPassword", filepath);
        if (password != null || !email.getIsSecure()) {
            if (password == null && !email.getIsSecure()) {
                email.setPassword("");
            } else {
                email.setPassword(password);
            }
        } else {
            msg = "password is not set in property file";
            throw new EmailException(msg);
        }
    }

    /**
     * Is email secure or not.
     * @param filepath Define file path.
     * @return Return true/false based on email secure or not.
     * @throws EmailException Throws email exception.
     */
    private boolean isEmailSecureOrnot(String filepath) throws EmailException{
        boolean isSecure;
        String msg;
        String secure = this.getPropertyValue("isSecure", filepath);
        if (secure != null) {
            if (Boolean.TRUE.equals(Boolean.valueOf(secure))) {
                isSecure = true;
            } else if (Boolean.FALSE.equals(Boolean.valueOf(secure))) {
                isSecure = false;
            } else {
                msg = "property file contains invalid value for isSecure";
                throw new EmailException(msg);
            }
        } else {
            msg = "isSecure is not set in property file";
            throw new EmailException(msg);
        }
        return isSecure;
    }

    /**
     * Set from address for mail.
     * @param filepath Define file path.
     * @return Returns from address for mail.
     */
    private String setFromAddressForMail(String filepath){
        String fromAddress = this.getPropertyValue("defaultFrom", filepath);
        return Objects.requireNonNullElse(fromAddress, "");
    }

    /**
     * Set reply for mail.
     * @param filepath Define file path.
     * @return Returns reply details for mail.
     */
    private String[] setReplyForMail(String filepath){
        String[] replyAddresses = this.getPropertyValue("defaultReplyTo", filepath).split(",");
        return Objects.requireNonNullElseGet(replyAddresses, () -> new String[0]);
    }

    /**
     * Returns file name of email properties
     * @return A file name string
     */
    public static String getPropertyFileName() {
        return FILENAME;
    }

    /**
     * Returns path of email properties file
     * @param emailPropertyFile A name of email property file
     * @return A file name string
     */
    public static String getFilepath(String emailPropertyFile) {
        if (new File(emailPropertyFile).exists()) {
            return emailPropertyFile;
        }
        return FILENAME;
    }

    /**
     * Adds an attachments to given multi part file
     * @param attachments A list of Attachment
     * @param multipart An instance of Multipart
     * @throws MessagingException A messing exception
     * @throws IOException If an I/O error occurs when reading or writing
     */
    private void addAtachments(Attachment[] attachments, Multipart multipart)
            throws MessagingException, IOException {
        for (int i = 0; i <= attachments.length - 1; i++) {
            String fileName = attachments[i].getFileName();
            byte[] fileContent = attachments[i].getFileContent();
            File file = new File(fileName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(fileContent);
                fileOutputStream.flush();
            }
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            //use a JAF FileDataSource as it does MIME type detection
            DataSource source = new FileDataSource(file);
            attachmentBodyPart.setDataHandler(new DataHandler(source));

            //assume that the filename you want to send is the same as the
            //actual file name - could alter this to remove the file path
            attachmentBodyPart.setFileName(fileName);

            //add the attachment
            multipart.addBodyPart(attachmentBodyPart);
            attachmentFiles.add(file);
        }
    }

    /**
     * Returns property value from given file
     * @param propertyKey A property key
     * @param propertyFilename A name of property file
     * @return A string of property value
     */
    public String getPropertyValue(String propertyKey, String propertyFilename) {
        String keyVal = "";
        try {
            Properties properties = new Properties();
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(propertyFilename);
            } catch (Exception e) {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(propertyFilename);
            }

            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + propertyFilename + "' not found in the classpath");
            }
            properties.load(inputStream);
            keyVal = properties.getProperty(propertyKey);
        } catch (FileNotFoundException fileNotFoundException) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.INFO, "File not Found Exception in getPropertyValue method");
        } catch (IOException iOException) {
            try {
                throw new EmailException(iOException.getMessage());
            } catch (EmailException emailException) {
                Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.INFO, emailException.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.INFO, "Error in getPropertyValue method.....");
        }
        return keyVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        String emailTemplate = notificationConfigTypeDto.getTemplate();
        String emailSubjectTemplate = notificationConfigTypeDto.getEmailSubject();
        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            if (!queryDataList.containsKey(notificationConfigTypeDto.getBaseDateFieldName())) {
                throw new ImtechoSystemException("email field not found. Event Config Id : " + notificationConfigTypeDto.getId(), 503);
            }
            if (queryDataList.get(notificationConfigTypeDto.getBaseDateFieldName()) != null) {
                String emailIds = queryDataList.get("email").toString();
                if (StringUtils.hasText(emailIds)) {
                    Email email = new Email();
                    email.setTo(emailIds.split(","));
                    email.setSubject(EventFunctionUtil.replaceParameterWithValue(emailSubjectTemplate, notificationConfigTypeDto.getEmailSubjectParameter(), queryDataList));
                    email.setMessageBody(EventFunctionUtil.replaceParameterWithValue(emailTemplate, notificationConfigTypeDto.getTemplateParameter(), queryDataList));
                    email.setConnectionType("DEFAULT");
                    sendMail(email);
                }
            }
        }
    }

    /**
     * Removes an attachment from file
     */
    private void removeAttachments() {
        for (File file : attachmentFiles) {
            file.delete();
        }
    }
}
