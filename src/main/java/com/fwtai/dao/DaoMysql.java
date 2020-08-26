package com.fwtai.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**Mysql数据源*/
@Repository
public class DaoMysql {

	@Autowired
	@Qualifier("jdbcTemplateMysql")
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> queryForMap(final String sql) throws Exception{
		return  jdbcTemplate.queryForMap(sql);
	}

	public String queryString(final String sql,final Object[] objects){
		Object obj = null;
		try {
			obj = jdbcTemplate.queryForObject(sql,objects,String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return obj == null ? null : obj.toString();
	}
}