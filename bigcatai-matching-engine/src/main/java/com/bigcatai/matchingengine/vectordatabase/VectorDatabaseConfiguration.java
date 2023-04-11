package com.bigcatai.matchingengine.vectordatabase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="bigcatai.vector-database.config")
public class VectorDatabaseConfiguration {

    private String dataFilePath;
    
    private String redisPort;
    
    private String redisHost;
    
    private VectorDatabaseType type;

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public VectorDatabaseType getType() {
        return type;
    }

    public void setType(VectorDatabaseType type) {
        this.type = type;
    }
    
}
