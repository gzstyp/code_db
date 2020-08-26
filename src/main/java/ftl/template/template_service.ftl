<#ftl encoding="utf-8"/>
package com.gzjytc.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzjytc.dao.DaoBase;
import com.gzjytc.util.ConfigFile;
import com.gzjytc.util.PageData;
import com.gzjytc.util.ToolClient;
import com.gzjytc.util.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Service
public class ${className}Service{

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DaoBase dao;

	/**添加*/
	public String add(final HttpServletRequest request)throws Exception{
		final String login_key = ToolClient.loginKey(request);
		final HashMap<String,Object> map = ToolClient.getFormParams(request);
		map.put("${keyId}",ToolString.getIdsChar32());
		return ToolClient.executeRows(dao.execute("${nameSpace}.add",map));
	}
	
	/**编辑*/
	public String edit(final HttpServletRequest request)throws Exception{
		final HashMap<String,Object> map = ToolClient.getFormParams(request);
		return ToolClient.executeRows(dao.execute("${nameSpace}.edit",map));
	}
	
	/**行删除*/
	public String delById(final PageData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"${keyId}"}))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.execute("${nameSpace}.delById",pageFormData.getString("${keyId}")));
	}
	
	/**删除[含批量删除]*/
	public String delIds(final PageData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"ids"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		int rows = 0 ;
		if(listIds != null && listIds.size() > 0){
			rows = dao.executeBatch("${nameSpace}.delIds",listIds);//批量删除
		}
		return ToolClient.executeRows(rows);
	}
	
	/**数据列表*/
	public String listData(PageData pageFormData){
		try{
			pageFormData = ToolClient.datagridPagingOracle(pageFormData);
			final HashMap<String,Object> map = dao.queryForPageData(pageFormData,"${nameSpace}.listData","${nameSpace}.listTotal");
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error("${className}Service的方法listData出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}

    /**根据id查询全字段*/
    public PageData queryById(final String id)throws Exception{
    	return dao.queryForPageData("${nameSpace}.queryById",id);
    }

	/**查询全部数据,页面通过json的code为200时再操作*/
	public String listAll(final PageData pageFormData){
		try{
			return ToolClient.queryJson(dao.queryForListHashMap("${nameSpace}.listAll",pageFormData));
		} catch (Exception e){
			logger.error("${className}Service的方法listAll出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**查询listMap数据,页面通过json的code为200时再操作,即{"code":200,"listData":[{""}]}*/
	public String listMap(final HttpServletRequest request){
		final HashMap<String,Object> params = ToolClient.getFormParams(request);
		try{
			return ToolClient.queryJson(dao.queryForListHashMap("${nameSpace}.listMap",params));
		}catch(Exception e){
			logger.error("${className}Service的方法listMap出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
}