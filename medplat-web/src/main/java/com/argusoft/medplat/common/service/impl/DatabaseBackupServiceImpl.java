
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.DatabaseBackupService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * Implements methods of DatabaseBackupService
 * @author subhash
 * @since 28/08/2020 4:30
 */
@Service
public class DatabaseBackupServiceImpl implements DatabaseBackupService{
    
    private static Logger log = LoggerFactory.getLogger(DatabaseBackupServiceImpl.class);
    
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    /**
     * Returns latest database backup file of given file name
     * @param fileName Name of the backup file
     * @return An instance of File
     */
    public Optional<File> getLatestDatabaseBackupFile(String fileName) {
        String backupPath = getDBBackupPath();
        Optional<File> latestDatabaseOptional;
        if(fileName==null){
            latestDatabaseOptional = getLatestDatabaseBackupFile();
        }else{
            latestDatabaseOptional = Optional.of(new File(backupPath+File.separator+fileName));
        }
        return latestDatabaseOptional;
    }

    /**
     * Returns latest database backup file
     * @return An instance of File
     */
    public Optional<File> getLatestDatabaseBackupFile(){
        String backupPath = getDBBackupPath();
        Path parentFolder = Paths.get(backupPath);
        Optional<File> mostRecentFileOrFolder
                = Arrays
                        .stream(parentFolder.toFile().listFiles())
                        .filter(file->file.getName().endsWith(".gz") || file.getName().endsWith(".zip"))
                        .max(
                                Comparator.comparingLong(File::lastModified));

        if (mostRecentFileOrFolder.isPresent()) {
            return mostRecentFileOrFolder;
        } else {
            log.info("folder is empty!");
            return Optional.empty();
        }
    }

    /**
     * Returns a path of database backup file
     * @return A path string
     */
    private String getDBBackupPath(){
       SystemConfiguration systemConfiguration = systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.TECHO_DB_BACKUP_PATH);
       if(systemConfiguration==null){
           return SystemConstantUtil.DATABASE_PATH_DEFAULT;
       }else{
           return systemConfiguration.getKeyValue();
       }
    }
    
}
