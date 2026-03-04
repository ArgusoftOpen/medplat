/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config;

import com.argusoft.medplat.common.model.SystemBuildHistory;
import com.argusoft.medplat.common.service.SystemBuildHistoryService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author smeet
 */
@Component
public class SystemBuildSave {

    @Autowired
    private SystemBuildHistoryService systemBuildHistoryService;

//    @Autowired
//    private CronExecutorService cronExecutorService;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
//        cronExecutorService.execute((tenantMaster -> {
            try {
                ResourceBundle serverPropertiesBundle = ResourceBundle.getBundle("server");
                String mavenVersion = serverPropertiesBundle.getString("mavenVersion");
                Date buildDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(serverPropertiesBundle.getString("buildDate"));
                SystemBuildHistory previousSystemBuildHistory = systemBuildHistoryService.retrieveLastBuildHistory();
                if (previousSystemBuildHistory != null) {
                    SystemBuildHistory systemBuildHistory = new SystemBuildHistory();
                    systemBuildHistory.setMavenVersion(mavenVersion);
                    systemBuildHistory.setServerStartDate(new Date());
                    if (buildDate.compareTo(previousSystemBuildHistory.getBuildDate()) == 0) {
                        systemBuildHistory.setBuildDate(previousSystemBuildHistory.getBuildDate());
                        systemBuildHistory.setBuildVersion(previousSystemBuildHistory.getBuildVersion());
                    } else {
                        systemBuildHistory.setBuildDate(buildDate);
                        systemBuildHistory.setBuildVersion(previousSystemBuildHistory.getBuildVersion() + 1);
                    }
                    systemBuildHistoryService.create(systemBuildHistory);
                } else {
                    SystemBuildHistory systemBuildHistory = new SystemBuildHistory();
                    systemBuildHistory.setBuildDate(new Date());
                    systemBuildHistory.setServerStartDate(new Date());
                    systemBuildHistory.setMavenVersion(mavenVersion);
                    systemBuildHistory.setBuildVersion(1);
                    systemBuildHistoryService.create(systemBuildHistory);
                }
                // TO COPY THE IMAGE RESOURCES ON BUILD BASED ON SERVER_IMPLEMENTATION_TYPE IN POM.XML
                final String DESTINATION = ConstantUtil.REPOSITORY_PATH + ConstantUtil.IMPLEMENTATION_TYPE + File.separator;
                URI resource = this.getClass().getResource("").toURI();
                if ("jar".equalsIgnoreCase(resource.getScheme())) {
                    final FileSystem fileSystem = FileSystems.newFileSystem(resource, Collections.<String, String>emptyMap());
                    final String source = File.separator + "BOOT-INF" + File.separator + "classes" + File.separator + ConstantUtil.IMPLEMENTATION_TYPE + File.separator;
                    final Path jarPath = fileSystem.getPath(source);
                    final Path destination = Paths.get(DESTINATION);
                    copyFromJar(jarPath, destination);
                    fileSystem.close();
                } else {
                    File sourceDir = new File("src/main/resources" + File.separator + ConstantUtil.IMPLEMENTATION_TYPE + File.separator);
                    File destDir = new File(DESTINATION);
                    FileUtils.copyDirectory(sourceDir, destDir, true);
                }
            } catch (Exception ex) {
                throw new ImtechoSystemException(ex.getMessage(), ex);
            }

//            }));
    }

    private void copyFromJar(final Path jarPath, final Path target) throws URISyntaxException, IOException {
        Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {
            private Path currentTarget;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                currentTarget = target.resolve(jarPath.relativize(dir).toString());
                Files.createDirectories(currentTarget);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}