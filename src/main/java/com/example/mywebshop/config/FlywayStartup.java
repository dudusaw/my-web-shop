package com.example.mywebshop.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.ValidateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayStartup {

    @Autowired
    public void flywayRepair(Flyway flyway) {
        boolean validationSuccessful = flyway.validateWithResult().validationSuccessful;
        if (!validationSuccessful) {
            flyway.repair();
            ValidateResult validateResult = flyway.validateWithResult();
            boolean repairSuccessful = validateResult.validationSuccessful;
            if (!repairSuccessful) {
                throw new IllegalStateException("flyway validation fails: " + validateResult.errorDetails.errorMessage);
            }
        }
    }
}
