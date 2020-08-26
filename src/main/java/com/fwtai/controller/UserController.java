package com.fwtai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fwtai.service.UserService;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class UserController{

	@Autowired
	private UserService user;
	
	/**验证账号和密码登录*/
	@RequestMapping("/check")
	@ResponseBody
	public final void check(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(user.check(request),response);
	}

	/**验证账号和密码登录*/
	@RequestMapping("/login")
	@ResponseBody
	public final void login(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(user.login(request),response);
	}
}