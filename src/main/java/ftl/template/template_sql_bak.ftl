<#ftl encoding="utf-8"/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${nameSpace}">
	
	<!-- 添加数据 -->
	<update id="add" parameterType="HashMap">
		INSERT INTO ${tableName} (
		<#list listData as ld>
		<#if ld_has_next>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name},</if>
		<#else>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name}</if>
		</#if>
		</#list>
		) VALUES (
		<#list listData as ld>
		<#if ld_has_next>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''"><![CDATA[ ${r"#{"}${ld.column_name}${r"}"},]]></if>
		<#else>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''"><![CDATA[ ${r"#{"}${ld.column_name}${r"}"}]]></if>
		</#if>
		</#list>
		)
	</update>
	
	<!-- 编辑数据 -->
	<update id="edit" parameterType="HashMap">
		<![CDATA[ UPDATE ${tableName} ]]>
		<trim prefix="SET" suffixOverrides=",">
		<#list listEdit as le>
		<#if le_has_next>
		<if test="${le.column_name} != null and ${le.column_name} != ''"><![CDATA[ ${le.column_name} = ${r"#{"}${le.column_name}${r"}"},]]></if>
		<#else>
		<if test="${le.column_name} != null and ${le.column_name} != ''"><![CDATA[ ${le.column_name} = ${r"#{"}${le.column_name}${r"}"} ]]></if>
		</#if>
		</#list>
		</trim>
		<![CDATA[ WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]>
	</update>
	
	<!-- 行删除 -->
	<delete id="delById" parameterType="String">
		<![CDATA[ DELETE FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]> 
	</delete>
	
	<!-- 删除|批量删除-->
	<delete id="delIds">
		DELETE FROM ${tableName} WHERE 
			${keyId} IN
		<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
                 ${r"#{item}"}
		</foreach>
	</delete>
	
	<!-- listData条件查询 -->
	<sql id="sql_where_listData">
		<trim prefix="WHERE" prefixOverrides="AND">
		<#list listEdit as le>
		<if test="${le.column_name} != null and ${le.column_name} != ''">
			<![CDATA[ AND ${le.column_name} LIKE '%'||${r"#{"}${le.column_name}${r"}"}||'%' ]]>
		</if>
		</#list>
		</trim>
	</sql>

    <!-- 数据列表 -->
    <select id="listData"  parameterType="HashMap" resultType="HashMap">
        <![CDATA[
        SELECT
        *
        FROM
        (SELECT  TB.*,
        ROWNUM RN
        FROM
        (
        SELECT
        <#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
        </#list>
        FROM ${tableName} ]]>
        <include refid="sql_where_listData"/>
        <![CDATA[ ) TB WHERE ROWNUM <= ${r"#{KEYROWS}"}) WHERE RN >= ${r"#{KEYRN}"} ]]>
    </select>

    <!-- 数据列表总条数总记录数 -->
    <select id="listTotal"  parameterType="HashMap" resultType="Integer">
        <![CDATA[ SELECT COUNT(${keyId})TOTAL FROM ${tableName} ]]>
        <include refid="sql_where_listData"/>
    </select>
	
	<!-- 根据id获取全字段数据 -->
	<select id="queryById" parameterType="String" resultType="HashMap">
		<![CDATA[
		SELECT 
		<#list listEdit as le>
		<#if le_has_next>
			${le.column_name},
		<#else>
			${le.column_name}
		</#if>
		</#list>
		FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} ]]>
	</select>

	<!-- 查询全部数据 -->
	<select id="listAll" parameterType="HashMap" resultType="HashMap">
		<![CDATA[ 
		SELECT
		<#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
		</#list>
		FROM ${tableName} ]]>
		<trim prefix="WHERE" prefixOverrides="AND">
		<#list listData as ld>
		<if test="${ld.column_name} != null and ${ld.column_name} != ''">
			<![CDATA[ AND ${ld.column_name} LIKE CONCAT('%',${r"#{"}${ld.column_name}${r"}"},'%') ]]>
		</if>
		</#list>
		</trim>
	</select>
</mapper>