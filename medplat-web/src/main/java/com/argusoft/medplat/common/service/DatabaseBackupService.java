package com.argusoft.medplat.common.service;

import java.io.File;
import java.util.Optional;

/**
 * <p>
 *     Define methods for database backup
 * </p>
 * @author subhash
 * @since 27/08/2020 4:30
 */
public interface DatabaseBackupService {
    /**
     * Returns latest database backup file
     * @return A backup file
     */
     Optional<File> getLatestDatabaseBackupFile();

    /**
     * Returns latest database backup file
     * @param fileName Name of the backup file
     * @return A backup file
     */
     Optional<File> getLatestDatabaseBackupFile(String fileName);
}
