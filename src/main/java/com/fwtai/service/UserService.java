package com.fwtai.service;

import com.fwtai.auth.AuthInterceptor;
import com.fwtai.dao.DaoMysql;
import com.fwtai.dao.DaoSQLite;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService{

	@Autowired
	private DaoMysql mysql;

    @Autowired
    private DaoSQLite sqLite;

	protected final Log logger = LogFactory.getLog(getClass());

	public String login(final HttpServletRequest request){
		final String user = request.getParameter("userName");
		final String pwd = request.getParameter("password");
		final String kid = sqLite.queryString("SELECT kid FROM sys_user WHERE name= ? and pwd = ?",new String[]{user,pwd});
		if(!ToolString.isBlank(kid)){
			request.getSession().setAttribute("login_user",kid);
			return ToolClient.createJson(200,"恭喜,登录成功!");
		}
		return ToolClient.createJson(199,"用户名或密码错误!");
	}

	public String check(HttpServletRequest request){
		final boolean b = AuthInterceptor.checkLogin(request);
		return b ? ToolClient.createJson(200,"已登录") : ToolClient.createJson(199,"未登录");
	}
}