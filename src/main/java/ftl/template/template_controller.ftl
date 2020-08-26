<#ftl encoding="utf-8"/>
package com.dwlai.controller.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzjytc.util.PageData;
import com.gzjytc.util.ToolString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.gzjytc.service.${className}Service;
import com.gzjytc.util.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Controller
@RequestMapping("/${nameSpace}")
public final class ${className}Controller extends BaseController{

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private ${className}Service service;
	
	/**跳转到目标页面*/
    @RequestMapping("/page")
    public ModelAndView editPage() {
        final ModelAndView mv = getModelAndView();
        final PageData pd = getPageData();
        if (!ToolString.isBlank(pd.getString("GUID"))){
            try {
                final PageData pageData = service.queryById(pd.getString("${keyId}"));
                mv.addObject("${keyId}", pd.getString("${keyId}"));
                if (pageData == null) {
                    mv.addObject("key_msg","编辑的数据已不存在");
                    mv.setViewName("tips");
                    return mv;
                } else {
                    mv.addObject("pd",pageData);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mv.addObject("key_msg","系统出现异常");
                mv.setViewName("tips");
                return mv;
            }finally {}
        }
        mv.setViewName("" + getPageData().getString("page"));//此处的""按路径填写,如在目录plasma下则填写的是plasma/
        return mv;
    }
	
	/**添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.add(request),response);
		} catch (Exception e){
			logger.error(getClass(),e);
            ToolClient.exceptionJson(response);
		}
	}
	
	/**编辑*/
	@RequestMapping("/edit")
	@ResponseBody
	public final void edit(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.edit(request),response);
		} catch (Exception e){
			logger.error(getClass(),e);
            ToolClient.exceptionJson(response);
		}
	}
	
	/**行删除*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delById(getPageData()),response);
		} catch (Exception e){
			logger.error(getClass(),e);
            ToolClient.exceptionJson(response);
		}
	}
	
	/**删除|批量删除*/
	@RequestMapping("/delIds")
	@ResponseBody
	public final void delIds(final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delIds(getPageData()),response);
		} catch (Exception e){
			logger.error(getClass(),e);
            ToolClient.exceptionJson(response);
		}
	}
	
	/**数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletResponse response){
		ToolClient.responseJson(service.listData(getPageData()),response);
	}
}