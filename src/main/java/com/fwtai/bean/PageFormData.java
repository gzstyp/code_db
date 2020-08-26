package com.fwtai.bean;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用的键值对数据结构
 * @作者 田应平
 * @版本 v1.0
 * @提示 表单提交的字段和数据库字段(字段大小写一致)的话，处理表单更为方便
 * @创建时间 2017年1月12日 13:13:05
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class PageFormData extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;

	private HashMap<String,Object> map = null;
	
	/**无参数的构造方法 */
	public PageFormData(){
		map = new HashMap<String,Object>(0);
	}
	
	/**有参数的构造方法,能获取到表单或ajax提交传参数和值或参数不传值的方法 */
	public PageFormData(final HttpServletRequest request){
		map = new HashMap<String,Object>(0);
		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()){
			final String key = paramNames.nextElement();
			final String[] values = request.getParameterValues(key);
			String value = "";
			if(values == null){
				value = "";
			}else {
				for (int i = 0; i < values.length; i++){
					value = values[i] + ",";
				}
				value = value.substring(0,value.length() - 1);
			}
			map.put(key,value);
		}
	}
	
	public final String getString(final String key){
		return get(key) == null ? null : get(key).toString();
	}

	public final Integer getInteger(final String key) throws Exception{
		return get(key) == null ? null : Integer.parseInt(get(key).toString());
	}

	@Override
	public int size(){
		return map.size();
	}

	@Override
	public boolean isEmpty(){
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key){
		return map.containsKey(key);
	}

	@Override
	public Object put(final String key,final Object value){
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m){
		map.putAll(m);
	}

	@Override
	public Object remove(final Object key){
		return map.remove(key);
	}

	@Override
	public void clear(){
		map.clear();
	}

	@Override
	public boolean containsValue(final Object value){
		return map.containsValue(value);
	}

	@Override
	public Object clone(){
		return map.clone();
	}

	@Override
	public Set<String> keySet(){
		return map.keySet();
	}

	@Override
	public Collection<Object> values(){
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet(){
		return map.entrySet();
	}

	@Override
	public Object get(final Object key){
		return map.get(key);
	}
}