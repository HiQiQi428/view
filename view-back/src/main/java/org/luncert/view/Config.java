package org.luncert.view;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import com.github.pagehelper.PageHelper;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.luncert.springconfigurer.ConfigManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@MapperScan("org.luncert.view.datasource.mysql")
@EnableTransactionManagement
@ComponentScan("org.luncert.view.datasource.neo4j")
@EnableNeo4jRepositories("org.luncert.view.datasource.neo4j")
@EnableWebMvc
@Configuration
public class Config implements WebMvcConfigurer {

    // config manager

    @Bean
    public ConfigManager getConfigManager() throws Exception {
        return new ConfigManager();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    // neo4j

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() throws Exception {
        ConfigManager configManager = getConfigManager();
        return new org.neo4j.ogm.config.Configuration
                    .Builder()
                    .uri(configManager.getString("neo4j:host"))
                    .credentials(configManager.getString("neo4j:username"), configManager.getString("neo4j:password"))
                    .build();
    }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() throws IOException, Exception {
        return new SessionFactory(configuration(), "org.luncert.view.datasource.neo4j");
    }
  
    @Bean
    public Neo4jTransactionManager transactionManager() throws Exception {
       return new Neo4jTransactionManager(getSessionFactory());
    }

    // page helper

	@Bean
	public PageHelper getPageHelper(){
		PageHelper pageHelper=new PageHelper();
		Properties properties=new Properties();
		properties.setProperty("helperDialect", "mysql");
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		//properties.setProperty("params","count=countSql");
		pageHelper.setProperties(properties);
		return pageHelper;
    }
    
    // file upload

    @Bean
    public MultipartConfigElement multipartConfigElement() throws Exception{
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("2MB");
        factory.setMaxRequestSize("1000MB");
        return factory.createMultipartConfig();
    }

}