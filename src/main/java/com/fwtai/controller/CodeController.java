package com.fwtai.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolFile;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 简单的代码生成
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
@RestController
@RequestMapping("/code")
public final class CodeController{

	@Autowired
	private DaoBase dao;

	@Value("${code_open}")
	private int code_open;

	@Value("${dir_window}")
	private String dir_window;

	@Value("${dir_linux}")
	private String dir_linux;

	/**查询所有的表*/
	@RequestMapping("/queryExistTable")
	@ResponseBody
	public final void queryTables(final HttpServletRequest request,final HttpServletResponse response){
		String tb = null;
		if (code_open == 0){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"代码生成功能已关闭"),response);
			return;
		}
		final String tableName = request.getParameter("table");
		if (ToolString.isBlank(tableName)){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code202,"警告,请输入表名!"),response);
			return;
		}
		try {
			tb = tableName.toUpperCase();
			final String table = dao.queryForString("sys_code.queryExistTable",tb);
			if (ToolString.isBlank(table)){
				final String json = ToolClient.createJson(ConfigFile.code199,"表名不存在,请输入正确的表名!");
				ToolClient.responseJson(json,response);
			}else{
				final String nameSpace = request.getParameter("nameSpace");/*sqlMap的命名空间*/
				final String className = request.getParameter("className");/*类名,首字母大写*/
				if (nameSpace == null || nameSpace.length() <= 0){
					ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入sqlMap的命名空间"),response);
					return;
				}
				if (className == null || className.length() <= 0){
					ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入类名且首字母大写"),response);
					return;
				}
				final String keyId = request.getParameter("keyId");
				if(ToolString.isBlank(keyId)){
					ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写表的主键字段"),response);
					return;
				}
				final List<HashMap<String,String>> listData = dao.queryForListHashMapString("sys_code.queryTable",tb);
				final boolean bl = keyExist(listData,keyId);
				if (!bl){
					ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写正确的表主键字段"),response);
					return;
				}
				final String json = ToolClient.queryJson(table);
				ToolClient.responseJson(json,response);
			}
		} catch (Exception e){
			e.printStackTrace();
			ToolClient.responseException(response);
			return;
		}
	}

	/**生成代码*/
	@RequestMapping("/create")
	@ResponseBody
	public final void create(final HttpServletRequest request,final HttpServletResponse response){
		final PageFormData pfd = new PageFormData(request);
		final String nameSpace = pfd.getString("nameSpace");/*sqlMap的命名空间*/
		final String className = pfd.getString("className");/*类名,首字母大写*/
		if (nameSpace == null || nameSpace.length() <= 0){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入sqlMap的命名空间"),response);
			return;
		}
		if (className == null || className.length() <= 0){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入类名且首字母大写"),response);
			return;
		}
		final String tableName = pfd.getString("table");
		if (ToolString.isBlank(tableName)){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code202,"警告,请输入表名!"),response);
			return;
		}
		final String keyId = pfd.getString("keyId");
		if(ToolString.isBlank(keyId)){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写表的主键字段"),response);
			return;
		}
		final HashMap<String,Object> data = new HashMap<String,Object>(0);	//创建数据模型
		data.put("keyId",keyId.toUpperCase());/*主键id,如果为空则是id,大写*/
		data.put("nameSpace",nameSpace);/*sql映射文件的命名空间及Controller跳转页面名称*/
		data.put("className",className);/*类名,首字母大写,前台输入不是首字母大写的自行修改即可*/
		data.put("tableName",tableName.toUpperCase());/*表名(全大写)*/
		try {
			final List<HashMap<String,String>> listData = dao.queryForListHashMapString("sys_code.queryTable",tableName.toUpperCase());
			final boolean bl = keyExist(listData,keyId);
			if (!bl){
				ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写正确的表主键字段"),response);
				return;
			}
			data.put("listData",listData);/*表名的字段及字段注释*/
			final List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < listData.size();i++){
				list.add(listData.get(i));
			}
			for (int i = 0; i < list.size(); i++){
				final HashMap<String,String> map = list.get(i);
				for(final String key : map.keySet()){
					if(map.get(key).equalsIgnoreCase(keyId)){
						list.remove(i);
						break;
					}
				}
				if(bl)break;
			}
			data.put("listEdit",list);/**编辑的sql*/
		} catch (Exception e){
			e.printStackTrace();
			ToolClient.responseException(response,"抱歉,代码生成出现异常");
			return;
		}
		final String separator = File.separator;
		final String code = "code";
		final String os_dir = ToolString.isLinuxOS() ? dir_linux : dir_window;
		final String dir = os_dir+"/"+code;
		final String ftl_dir = dir+separator;
		ToolFile.delete(new File(dir));//删除文件夹
		ToolFile.delete(new File(os_dir+"/"+code+".zip"));//删除文件
		final String fileDir = os_dir+"/ftl";
		try {
			/*生成controller*/
			output("template_controller.ftl",data,className+"Controller.java",ftl_dir,fileDir);
			/*生成service*/
			output("template_service.ftl",data,className+"Service.java",ftl_dir,fileDir);
			/*生成mybatis xml*/
			output("template_sql.ftl",data,tableName.toLowerCase()+".xml",ftl_dir,fileDir);
			/*生成jsp页面*/
			output("template_list.ftl",data,nameSpace+"_list.jsp",ftl_dir,fileDir);
			output("template_edit.ftl",data,nameSpace+"_edit.jsp",ftl_dir,fileDir);
			final String zipName = dir+".zip";
			final String fileName = code+".zip";//以nameSpace做文件名
			ToolFile.zipCompress(dir,zipName);
			fileDownload(response,zipName,fileName);
		} catch (Exception e){
			ToolClient.responseException(response,"抱歉,代码生成出现异常");
			return;
		}
	}

	/**生成代码*/
	@RequestMapping("/create1")
	@ResponseBody
	public final void create1(final HttpServletRequest request,final HttpServletResponse response){
		final PageFormData pfd = new PageFormData(request);
		final String nameSpace = pfd.getString("nameSpace");/*sqlMap的命名空间*/
		final String className = pfd.getString("className");/*类名,首字母大写*/
		if (nameSpace == null || nameSpace.length() <= 0){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入sqlMap的命名空间"),response);
			return;
		}
		if (className == null || className.length() <= 0){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请输入类名且首字母大写"),response);
			return;
		}
		final String tableName = pfd.getString("table");
		if (ToolString.isBlank(tableName)){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code202,"警告,请输入表名!"),response);
			return;
		}
		final String keyId = pfd.getString("keyId");
		if(ToolString.isBlank(keyId)){
			ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写表的主键字段"),response);
			return;
		}
		final HashMap<String,Object> data = new HashMap<String,Object>(0);	//创建数据模型
		data.put("keyId",keyId.toUpperCase());/*主键id,如果为空则是id,大写*/
		data.put("nameSpace",nameSpace);/*sql映射文件的命名空间及Controller跳转页面名称*/
		data.put("className",className);/*类名,首字母大写,前台输入不是首字母大写的自行修改即可*/
		data.put("tableName",tableName.toUpperCase());/*表名(全大写)*/
		try {
			final List<HashMap<String,String>> listData = dao.queryForListHashMapString("sys_code.queryTable",tableName.toUpperCase());
			final boolean bl = keyExist(listData,keyId);
			if (!bl){
				ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,"请填写正确的表主键字段"),response);
				return;
			}
			data.put("listData",listData);/*表名的字段及字段注释*/
			final List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			for(int i = 0; i < listData.size();i++){
				list.add(listData.get(i));
			}
			for (int i = 0; i < list.size(); i++){
				final HashMap<String,String> map = list.get(i);
				for(final String key : map.keySet()){
					if(map.get(key).equalsIgnoreCase(keyId)){
						list.remove(i);
						break;
					}
				}
				if(bl)break;
			}
			data.put("listEdit",list);/**编辑的sql*/
		} catch (Exception e){
			e.printStackTrace();
			ToolClient.responseException(response,"抱歉,代码生成出现异常");
			return;
		}
		final String separator = File.separator;
		final String base = request.getSession().getServletContext().getRealPath("ftl");
		final String dir = base+separator+"code";//以nameSpace做文件目录
		final String ftl_dir = dir+separator;
		ToolFile.delete(new File(base));//删除文件夹
		ToolFile.delete(new File(dir+".zip"));//删除文件
		final String getPath = getClasses();
		final String fileDir = getPath+"ftl/template";/*以模版ftl文件生成后的文件存放目录,即要压缩的目标目录,web项目的路径,所以是/*/
		try {
			/*生成controller*/
			output("template_controller.ftl",data,className+"Controller.java",ftl_dir,fileDir);
			/*生成service*/
			output("template_service.ftl",data,className+"Service.java",ftl_dir,fileDir);
			/*生成mybatis xml*/
			output("template_sql.ftl",data,tableName.toLowerCase()+".xml",ftl_dir,fileDir);
			/*生成jsp页面*/
			output("template_list.ftl",data,nameSpace+"_list.jsp",ftl_dir,fileDir);
			output("template_edit.ftl",data,nameSpace+"_edit.jsp",ftl_dir,fileDir);
			final String zipName = dir+".zip";
			final String fileName = "code.zip";//以nameSpace做文件名
			ToolFile.zipCompress(dir,zipName);
			fileDownload(response,zipName,fileName);
		} catch (Exception e){
			ToolClient.responseException(response,"抱歉,代码生成出现异常");
			return;
		}
	}

	private final boolean keyExist(final List<HashMap<String,String>> listData,final String keyId){
		boolean bl = false;
		for(int i = 0; i < listData.size(); i++){
			final HashMap<String,String> map = listData.get(i);
			final String key = map.get("column_name");
			if(key.equalsIgnoreCase(keyId)){
				bl = true;
				break;
			}
			if(bl)break;
		}
		return  bl;
	}

	/**获取项目物理路径classes*/
	private final String getClasses(){
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		if(path.indexOf(":") != -1)
			path = path.substring(1);
		return path;
	}

	/**
	 * @param response
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	private final void fileDownload(final HttpServletResponse response,final String filePath,String fileName) throws Exception{
		final byte[] data = toByteArray(filePath);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		final OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}

	private final byte[] toByteArray(final String filePath) throws IOException {
		final File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 输出到输出到文件
	 * @param ftlName   ftl文件名
	 * @param data		传入的map
	 * @param outFile	输出后的文件全部路径目录
	 * @param fileDir	输出前的文件全路径,ftl存放文件目录
	 */
	private final void output(final String ftlName,final HashMap<String,Object> data,final String outFile,final String filePath,final String fileDir) throws Exception{
		final File file = new File(filePath + outFile);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
		final Template template = getTemplate(ftlName,fileDir);
		if (template != null) {
			template.process(data,out);
		}
		out.flush();
		out.close();
	}

	/**
	 * 通过文件名加载模版
	 * @param ftlName
	 */
	private final Template getTemplate(final String ftlName,final String ftlPath) throws Exception{
		try {
			final Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);//通过Freemaker的Configuration读取相应的ftl
			cfg.setEncoding(Locale.CHINA, "utf-8");
			cfg.setDirectoryForTemplateLoading(new File(ftlPath));	//设定去哪里读取相应的ftl模板文件
			return cfg.getTemplate(ftlName);						//在模板文件目录中找到名称为name的文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}