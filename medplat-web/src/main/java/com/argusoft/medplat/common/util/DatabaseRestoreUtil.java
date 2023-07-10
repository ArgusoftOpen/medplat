package com.argusoft.medplat.common.util;

import com.argusoft.medplat.exception.ImtechoSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Util class for executing the shell script at fixed location.
 * @author Hiren Morzariya
 */
public class DatabaseRestoreUtil {

    private DatabaseRestoreUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRestoreUtil.class);

      
    /**
     * This method calls for OS Process for executing database restore script
     * @param hostName String database ip address
     * @param port String database port
     * @param dbUserName String database user name
     * @param password String database password
     * @param db String database name
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    
    public static final void restoredb(
            String hostName,
            String port,
            String dbUserName,
            String password,
            String db) throws InterruptedException{
        try {
            long t = System.currentTimeMillis();
            String cmd = "sh database_restore.sh";
            String[] env = new String[6];
            env[0] = "hostname=" + hostName;
            env[1] = "port=" + port;
            env[2] = "username=" + dbUserName;
            env[3] = "database=" + db;
            env[4] = "numberOfJobs=" + "4";
            env[5] = "PGPASSWORD=" + password;

            String s;
            Process p;

            p = Runtime.getRuntime().exec(cmd, env, new File(ConstantUtil.TEST_DB_RESTORE_SCRIPT_PATH));

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = br.readLine()) != null) {
                LOGGER.debug(s);
            }

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                LOGGER.debug(s);
            }
            p.waitFor();
            LOGGER.debug("Process Exit status : {}", p.exitValue());
            p.destroy();
            LOGGER.info("TIME TAEKN TO RESTORE Database : {} {}", (System.currentTimeMillis() - t) / 1000, " seconds");
        } catch (IOException  ex) {
            LOGGER.error(ex.getMessage());
            throw new ImtechoSystemException("Exception ", ex);
            

        }

    }
}
