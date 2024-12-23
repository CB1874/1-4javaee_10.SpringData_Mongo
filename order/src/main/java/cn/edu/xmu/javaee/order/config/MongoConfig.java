//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.order.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.host1}")
    private String host1;

    @Value("${spring.data.mongodb.host2}")
    private String host2;

    @Value("${spring.data.mongodb.host3}")
    private String host3;

    @Value("${spring.data.mongodb.port1}")
    private Integer port1;

    @Value("${spring.data.mongodb.port3}")
    private Integer port3;

    @Value("${spring.data.mongodb.port2}")
    private Integer port2;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @Override
    public MongoClient mongoClient() {
        MongoCredential credential = MongoCredential.createCredential(
                username,
                database,
                password.toCharArray()
        );

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(
                                        new ServerAddress(host1, port1),
                                        new ServerAddress(host2, port2),
                                        new ServerAddress(host3, port3)
                                )))
                        .readPreference(ReadPreference.secondaryPreferred()) // 优先从节点
                        .build());
        return mongoClient;
    }
}
