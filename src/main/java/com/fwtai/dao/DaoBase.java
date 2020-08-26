package com.fwtai.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**Oracle数据源-dao底层操作处理工具类*/
@Repository
public class DaoBase {
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年12月25日 00:44:39
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	 */
	public String queryForString(final String sqlMapId) throws Exception {
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年12月25日 00:44:39
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public String queryForString(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**查询返回List《HashMap<String,String>》*/
	public List<HashMap<String,String>> queryForListHashMapString(final String sqlMapId, final Object objParam) throws Exception {
		return sqlSession.selectList(sqlMapId,objParam);
	}

    /**查询返回List《HashMap<String,Object>》*/
    public List<HashMap<String,Object>> queryForListHashMap(final String sqlMapId, final Object objParam) throws Exception {
        return sqlSession.selectList(sqlMapId,objParam);
    }
}