package com.edigest.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement  //----->  yeh annotation unn sabhi methods ko dhoondega jinpe transaction likha hoga
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.edigest.journalApp.repository") // Add this line
public class JournalApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(JournalApplication.class, args);
		ConfigurableEnvironment environment = context.getEnvironment();
		System.out.println(environment.getActiveProfiles()[0]);
//		System.out.println(environment.getActiveProfiles()[1]);
	}

	@Bean
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}
}

// PlatformTransationManager
// MongoTransactionManager