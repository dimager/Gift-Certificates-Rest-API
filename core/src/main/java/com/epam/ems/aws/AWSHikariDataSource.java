package com.epam.ems.aws;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.rds.auth.GetIamAuthTokenRequest;
import com.amazonaws.services.rds.auth.RdsIamAuthTokenGenerator;
import com.zaxxer.hikari.HikariDataSource;

public class AWSHikariDataSource extends HikariDataSource {

    private String rdbUsername = System.getenv("RDS_USER");

    private String rdsDBDriver = System.getenv("RDS_DRIVER");

    private String rdsHost = System.getenv("RDS_HOST");

    private int rdsPort = Integer.parseInt(System.getenv("RDS_PORT"));

    private String rdsJdbcUrl = System.getenv("RDS_JDBC_URL");

    private String rdsRegion = System.getenv("RDS_REGION");

    public AWSHikariDataSource() {
        this.setUsername(rdbUsername);
        this.setDriverClassName(rdsDBDriver);
        this.setJdbcUrl(rdsJdbcUrl);
    }

    @Override
    public String getPassword() {
        return this.getToken();
    }

    private String getToken() {
        RdsIamAuthTokenGenerator rdsIamAuthTokenGenerator = RdsIamAuthTokenGenerator.builder()
                .credentials(new InstanceProfileCredentialsProvider(false))
                .region(rdsRegion)
                .build();

        GetIamAuthTokenRequest request = GetIamAuthTokenRequest.builder()
                .hostname(rdsHost)
                .port(rdsPort)
                .userName(rdbUsername)
                .build();
        return rdsIamAuthTokenGenerator.getAuthToken(request);
    }
}
