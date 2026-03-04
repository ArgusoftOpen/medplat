/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 *
 * @author ashish
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private static final String NOT_SUPPORTED_MSG="Not supported yet.";

    private ByteArrayOutputStream cachedBytes;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) {
            cacheInputStream();
        }

        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        /* Cache the inputstream in order to read it multiple times. For
     * convenience, I use apache.commons IOUtils
         */
        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    /* An inputstream which reads the cached request body */
    public class CachedServletInputStream extends ServletInputStream {

        private ByteArrayInputStream input;

        public CachedServletInputStream() {
            /* create a new input stream from the cached request body */
            input = new ByteArrayInputStream(cachedBytes.toByteArray());
        }

        @Override
        public int read() throws IOException {
            return input.read();
        }

        @Override
        public boolean isFinished() {
            throw new UnsupportedOperationException(NOT_SUPPORTED_MSG); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isReady() {
            throw new UnsupportedOperationException(NOT_SUPPORTED_MSG); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setReadListener(ReadListener rl) {
            throw new UnsupportedOperationException(NOT_SUPPORTED_MSG); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
