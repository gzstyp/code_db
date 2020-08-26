package com.fwtai.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**配置数据源*/
@Configuration
public class DataSourceConfig {

    @Bean(name = "SQLiteDataSource")
    @Qualifier("SQLiteDataSource")
    @ConfigurationProperties(prefix = "datasource.sqlite")
    public DataSource getSQLiteDataSource(){
        return DataSourceBuilder.create().build();
    }

	@Bean(name = "MySQLDataSource")
	@Qualifier("MySQLDataSource")
	@ConfigurationProperties(prefix = "datasource.mysql")
	public DataSource getMySQLDataSource(){
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "OracleDataSource")
	@Qualifier("OracleDataSource")
	@ConfigurationProperties(prefix = "datasource.oracle")
	@Primary
	public DataSource getOracleDataSource(){
		return DataSourceBuilder.create().build();
	}

    @Bean(name = "jdbcTemplateSQLite")
    public JdbcTemplate getJdbcTemplateSQLite(@Qualifier("SQLiteDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

	@Bean(name = "jdbcTemplateMysql")
	public JdbcTemplate getJdbcTemplateMysql(@Qualifier("MySQLDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "jdbcTemplateOracle")
	public JdbcTemplate getJdbcTemplateOracle(@Qualifier("OracleDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}