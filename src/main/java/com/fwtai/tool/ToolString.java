package com.fwtai.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fwtai.bean.PageFormData;

/**
 * 字符串的工具类|含json对象或json数组解析
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2015年11月27日 18:25:12
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolString implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 转码
	 * @param object
	 * @return
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2015-9-6 下午5:23:06 
	 * @QQ号码 444141300
	 * @官网 http://www.blidian.com
	*/
	public final static String transCoding(final Object object){
		if(object != null){
			try {
				return new String(object.toString().getBytes("ISO8859-1"),"UTF-8");
			} catch (Exception e){
				return null;
			}
		}
		return null;
	}
	
	/**生成32位唯一的字符串*/
	public final static String getIdsChar32(){
		return UUID.randomUUID().toString().replace("-", "");//生成32位唯一的字符串
	}

    /**
	 * 验证是否为空,为空时返回true,否则返回false,含obj是否为 _单独的下划线特殊字符,是则返回true,否则返回false
	 * @作者: 田应平
	 * @主页 www.fwtai.com
	 * @创建日期 2016年8月18日 17:34:24
	*/
	public final static boolean isBlank(final Object obj){
		if(obj == null)return true;
		final String temp = String.valueOf(obj);
		if(temp.toLowerCase().equals("null"))return true;
		final String key = obj.toString().replaceAll("\\s*", "");
		if(key.equals("") || key.length() <= 0 )return true;
		if(key.length() == 1 && key.equals("_"))return true;
		if(obj instanceof List<?>){
			final List<?> list = (List<?>) obj;
            return list == null || list.size() <= 0;
        }
		if(obj instanceof Map<?,?>){
			final Map<?,?> map = (Map<?,?>) obj;
            return map == null || map.size() <= 0;
        }
		if(obj instanceof String[]){
			boolean flag = false;
			final String[] require = (String[])obj;
			for(int i = 0; i < require.length; i++){
				if(require[i] == null || require[i].length() == 0 || require[i].replaceAll("\\s*", "").length() == 0){
					flag = true;
					break;
				}
			}
			return flag;
		}
		if(obj instanceof HashMap<?,?>){
			final HashMap<?, ?> hashMap = (HashMap<?,?>) obj;
            return hashMap == null || hashMap.size() <= 0;
        }
		if(obj instanceof PageFormData){
			final PageFormData hashMap = (PageFormData)obj;
            return hashMap == null || hashMap.size() <= 0;
        }
		if(obj instanceof JSONObject){
			final JSONObject json = (JSONObject)obj;
			return json.isEmpty();
		}
		return false;
	}
	
	/**如果为空则赋值为""*/
	public final static String setBlank(final Object obj){
		if (isBlank(obj))return "";
		return String.valueOf(obj);
	}
	
	/**如果为空则赋值为defaultValue*/
	public final static String setBlank(final Object obj,final String defaultValue){
		if (isBlank(obj))return defaultValue;
		return String.valueOf(obj);
	}
	
	/**
	 * 去除空格,如果为空则返回null,若有且只有_也只返回null否则去除前后空格后返回
	 * @param obj
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2015-8-18 下午2:03:25 
	 * @QQ号码 444141300
	 * @注释 \s 匹配任何空白字符，包括空格、制表符、换页符,* 匹配前面的子表达式零次或多次。
	 * @官网 http://www.fwtai.com
	*/
	public final static String wipeSpace(final Object obj){
		if(isBlank(obj))return null;
		final String key = obj.toString().replaceAll("\\s*", "");
		if(key.length() == 1 && key.equals("_"))return null;
		return key;
	}
	
	/**
	 * 是否大于指定长度,若大于返回true否则返回false
	 * @param value
	 * @param length
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-12-22 上午3:25:17 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean isTooLong(final String value,final int length){
        return value.length() > length;
    }
	
	/**
	 * 验证输入值是否是正整数,是正整数返回true,否则返回false
	 * @param value
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-12-9 下午6:11:36 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static boolean checkNumber(final String value){
		String reg = "^\\d*[1-9]\\d*$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(value); 
		return m.matches(); 
	}
	
	/**
	 * 生成时间格式:yyyy-MM-dd HH:mm:ss
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月10日 09:35:02 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//设置日期格式
	}
	
	/**
	 * ids字符串数组转换为list< String >
	 * @param ids 格式如:12958292,12951500,12977780,12997129
	 * @param splitFlag 以 splitFlag 为拆分标识
	 * @作者 田应平
	 * @返回值类型 List< String >
	 * @创建时间 2016年1月21日 12:20:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static ArrayList<String> keysToList(final String ids,String splitFlag){
		if(ToolString.isBlank(ids))return null;
		final ArrayList<String> list = new ArrayList<String>();
		if (isBlank(splitFlag)){
			splitFlag = ",";
		}
		final String[] arr = ids.split(splitFlag);
		if(arr.length == 0)return null;
		for(int i = 0; i < arr.length; i++){
			list.add(arr[i]);
		}
		return list;
	}
	
	/**
	 * ids字符串数组转换为String
	 * @param ids 格式如:12958292,12951500,12977780,12997129
	 * @param splitFlag 以 splitFlag 为拆分标识
	 * @作者 田应平
	 * @返回值类型 List< String >
	 * @创建时间 2017年1月10日 09:34:24
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String[] keysToArarry(final String ids,String splitFlag){
		if(ToolString.isBlank(ids))return null;
		if (isBlank(splitFlag)){
			splitFlag = ",";
		}
		final String[] arr = ids.split(splitFlag);
		if(arr.length == 0)return null;
		return arr;
	}
	
	/**
	 * 根据完整文件路径删除文件
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年1月10日 09:34:49 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean deleFileByRealPath(final String filePath){
		//删除附件
		if(!isBlank(filePath) && filePath.indexOf(".") != -1){
			try {
				File f = new File(filePath);
				if(f.exists()){
					f.delete();
				}
				return true ;
			} catch (Exception e){
			}
		}
		return false;
	}
	
	/**
	 * 删除图片
	 * @param request
	 * @param webImageUrl url相对路径,如group_images/1445610757864195059.jpg
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015年11月13日 13:13:08 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean deleFileByWebPath(final HttpServletRequest request,final String webImageUrl){
		final String sys = File.separator;
		final String projectRealPath = request.getSession().getServletContext().getRealPath(sys);
		final String realImagePath = projectRealPath + webImageUrl;
		try {
			File f = new File(realImagePath);
			if(f.exists()){
				f.delete();
			}
			return true ;
		} catch (Exception e){
			return false ;
		}
	}

	/**
	 * html网页的img替换规则
	 * @param html
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月23日 17:33:21
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 * @示例 html = html.replaceAll("src=\"/upimg","src=\""+url+"/upimg");
	*/
	public final static String htmlReplace(final String html,final String flagOld,final String flagNew){
		return html.replaceAll(flagOld,flagNew);
	}
	
	/**
	 * 判断一个字符串是否是纯数字字符串
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年3月8日 上午8:23:11
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static boolean isNumberStr(final String str){
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
	
	/**获取文件的后缀名|扩展名*/
	public final static String getFileExt(final String str){
		return str.substring(str.lastIndexOf('.')+1,str.length());
	}
	
	/**验证指定的key字符串是否在以逗号,隔开的字符串里,存在返回true,为false时说明不存在,用法:if(!ToolString.existKey("xls,xlsx",key)){}*/
	public final static boolean existKey(final String keys,final String key){
		final String[] array = keys.split(",");
		boolean b = false;
		for(int i = 0; i < array.length; i++){
			if(array[i].equalsIgnoreCase(key)){
				b = true;
				break;
			}
		}
		return b;
	}
	
	/**获取文件的文件名,不含扩展名后缀名*/
	public final static String getFileName(final String str){
		return str.substring(str.lastIndexOf('/')+1,str.lastIndexOf('.'));
	}
	
	/**获取文件的文件名,含扩展名后缀名*/
	public final static String getFileNameExt(final String str){
		return str.substring(str.lastIndexOf('/')+1);
	}

	/**
     * <strong style='color:#f69;'>解析json对象字符串,HashMap里的key全是小写</strong>
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2017年3月7日 20:42:05
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
	public final static HashMap<String,String> parseJsonObject(final Object json){
        final HashMap<String, String> jsonMap = new HashMap<String, String>();
        if(isBlank(json)) return jsonMap;
        try {
            final JSONObject jsonObject = JSONObject.parseObject(json.toString());
            for (String key : jsonObject.keySet()){
                jsonMap.put(key.toLowerCase(),(jsonObject.get(key) == null || jsonObject.get(key).toString().length() <= 0) ? "" : String.valueOf(jsonObject.get(key)));
            }
            return jsonMap;
        } catch (Exception e){
            return jsonMap;
        }
    }

    /**
     * <strong style='color:#f69;'>解析json数组字符串,ArrayList里的HashMap的key全是小写</strong>
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2017年3月7日 20:42:12
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
	public final static ArrayList<HashMap<String,String>> parseJsonArray(final Object jsonArray){
		final ArrayList<HashMap<String, String>> listResult = new ArrayList<HashMap<String, String>>();
        if(isBlank(jsonArray)) return listResult;
        try {
        	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();//初始化,以免出现空指针异常
            list = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<HashMap<String, String>>>(){});
            if(list != null && list.size() > 0){
                for (int i = 0; i < list.size(); i++){
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    for (String key : list.get(i).keySet()){
                        hashMap.put(key.toLowerCase(),(list.get(i).get(key) == null || list.get(i).get(key).toString().length() <= 0) ? "" : list.get(i).get(key).toString());
                    }
                    listResult.add(hashMap);
                }
                return listResult;
            } else {
                return listResult;
            }
        } catch (Exception e){
            return listResult;
        }
    }
    
    /**
     * <strong style='color:#f69;'>将json字符串解析为一个 JavaBean 对象</strong>
     * @param cls 转换的目标对象
     * @用法 调用时写 StringTools.jsonToObject(json, javaBean.class);
     * @作者 田应平
     * @返回值类型 T
     * @创建时间 2017年3月7日 下午9:12:19
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static <T> T parseJsonToBean(final String json,final Class<T> cls){
		T t = null;
		try {
			t = JSON.parseObject(json,cls);
		} catch (Exception e){
			e.printStackTrace();
		}
		return t;
	}
    
    /**
     * <strong style='color:#f69;'>将json数组字符串 解析成为一个 List< JavaBean ></strong>
     * @param cls 转换的目标对象
     * @用法 如果是实体类则在调用时写 jsonToListObject(json, javaBean.class);或 jsonToListObject(json,String.class);
     * @作者 田应平
     * @返回值类型 List< T >
     * @创建时间 2017年3月7日 21:15:00
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static <T> List<T> parseJsonToListBean(final String jsonArray,final Class<T> cls){
		List<T> list = new ArrayList<T>();//初始化,以免出现空指针异常
		try {
			list = JSON.parseArray(jsonArray,cls);
		} catch (Exception e){
		}
		return list;
	}
    
    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
    */ 
    public final static boolean checkChinese(String chinese) { 
        String regex = "^[\u4E00-\u9FA5]+$"; 
        return Pattern.matches(regex,chinese); 
    }

	/**
	 * 判断运行系统是linux还是windows,是linux返回true,否则返回false
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月23日 15:50:19
	 */
	public static boolean isLinuxOS(){
		final String os = System.getProperties().getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("linux") > -1)return true;
		return false;
	}
}