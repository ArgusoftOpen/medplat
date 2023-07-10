package com.argusoft.medplat.config;

import com.argusoft.medplat.common.util.ConstantUtil;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;

@Configuration(value = "FlywayInitializerConfiguration")
public class FlywayInitializer {

    @Autowired
    private DataSource dataSource;


    public void migrate() {
        String scriptLocation = "db/migration";

            Flyway flyway =  Flyway.configure()
                    .locations(scriptLocation)
                    .baselineOnMigrate(Boolean.TRUE)
                    .dataSource(dataSource)
                    .validateOnMigrate(false)
                    .outOfOrder(true)
                    .baselineVersion("1").load();
                flyway.migrate();
            }
}
