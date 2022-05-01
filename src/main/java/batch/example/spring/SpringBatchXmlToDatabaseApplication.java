package batch.example.spring;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.integration.IntegrationDataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBatchXmlToDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchXmlToDatabaseApplication.class, args);
	}

}
