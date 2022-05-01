package batch.example.spring;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import batch.example.spring.model.User;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
     public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;

	 
	@Bean
	public FlatFileItemReader<User> readerFromCsv(){
		FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
		reader.setResource(new ClassPathResource("user.csv"));
		reader.setLineMapper(new DefaultLineMapper<User>(){
			{
			setLineTokenizer(new DelimitedLineTokenizer(){
				{
					setNames(User.field());
				}
			});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<User>(){
				{
					setTargetType(User.class);
				}
			});
			}
		});
		return reader;
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writeIntoDB(){
		JdbcBatchItemWriter<User> writer= new JdbcBatchItemWriter<User>();
		writer.setDataSource(dataSource);
		writer.setSql("insert into User (id,prenom,nom,age) values(:id,:prenom,:nom,:age)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		return writer;
	}
	
	@Bean
	public Step step(){
	return	stepBuilderFactory.get("step").<User,User>chunk(10).reader(readerFromCsv()).writer(writeIntoDB()).build();
	}
	
	@Bean
	public Job job(){
		return jobBuilderFactory.get("job").flow(step()).end().build();
	}
}
