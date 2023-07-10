/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Defines fields related to sms</p>
 * @author Harshit
 * @since 26/08/2020 5:30
 */
public class SmsDto {

    private Authentication authentication;
    private List<MessageDetail> messages = new LinkedList<>();

    public SmsDto(String username, String password, String signature,String text, String[] recipients) {
        this.authentication = new Authentication(username, password,signature);
        this.messages.add(new MessageDetail(text, recipients));
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public List<MessageDetail> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDetail> messages) {
        this.messages = messages;
    }

    class Authentication {

        private String username;
        private String password;
        private String signature;

        public Authentication(String username, String password,String signature) {
            this.username = username;
            this.password = password;
            this.signature = signature;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    class MessageDetail {

        private String sender = "TECHOG";
        private String text;
        private String type = "longSMS";
        private String datacoding = "8";
        List<Recipients> recipients;

        public MessageDetail(String text, String[] recipients) {
            this.text = text;
            this.recipients = new LinkedList<>();
            for (String recipient : recipients) {
                this.recipients.add(new Recipients("+91" + recipient));
            }
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Recipients> getRecipients() {
            return recipients;
        }

        public void setRecipients(List<Recipients> recipients) {
            this.recipients = recipients;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDatacoding() {
            return datacoding;
        }

        public void setDatacoding(String datacoding) {
            this.datacoding = datacoding;
        }

    }

    class Recipients {

        private String gsm;

        public Recipients(String gsm) {
            this.gsm = gsm;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

    }

}
