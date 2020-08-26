package com.fwtai.auth;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Spring boot拦截器
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017-11-26 13:58
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public class AuthInterceptor implements HandlerInterceptor{

    public final String[] allowUrls = {"/","/login","/check","/coreMenu/queryMenu/"};//不拦截的url访问地址

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,Object object, Exception exception) throws Exception {
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object object) throws Exception {
        final String method = request.getMethod();
        if(!method.equals("GET")&&!method.equals("POST")){return false;}
        final String path = request.getRequestURI().replace(request.getContextPath(), "");
        if(allowUrls != null && allowUrls.length >= 1){
            for (String url : allowUrls){
                if(path.equals(url)){
                    return true;
                }
            }
        }
        final boolean login = checkLogin(request);
        if(login){
            return true;
        }else{
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }

    public static final boolean checkLogin(final HttpServletRequest request){
        final HttpSession session = request.getSession(false);
        if(session == null){
            return false;
        }
        final Object login_user = session.getAttribute("login_user");
        return login_user != null;
    }
}