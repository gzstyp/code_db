<#ftl encoding="utf-8"/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${nameSpace}">
	
	<!-- 添加数据 -->
	<update id="add" parameterType="HashMap">
		INSERT INTO ${tableName} (
        <trim suffixOverrides=",">
		<#list listData as ld>
		<#if ld_has_next>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name},</if>
		<#else>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name}</if>
		</#if>
		</#list>
        </trim>
		) VALUES (
        <trim suffixOverrides=",">
        <#list listData as ld>
		<#if ld_has_next>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"},</if>
		<#else>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"}</if>
		</#if>
		</#list>
        </trim>
		)
	</update>
	
	<!-- 编辑数据 -->
	<update id="edit" parameterType="HashMap">
		UPDATE ${tableName}
		<trim prefix="SET" suffixOverrides=",">
		<#list listEdit as le>
		<#if le_has_next>
		<if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"},</if>
		<#else>
		<if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"}</if>
		</#if>
		</#list>
		</trim>
		WHERE ${keyId} = ${r"#{"}${keyId}${r"}"}
	</update>
	
	<!-- 行删除 -->
	<delete id="delById" parameterType="String">
        DELETE FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"}
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
			AND ${le.column_name} LIKE '%'||${r"#{"}${le.column_name}${r"}"}||'%'
		</if>
		</#list>
		</trim>
	</sql>

    <!-- 数据列表 -->
    <select id="listData" parameterType="HashMap" resultType="HashMap">
        SELECT
        <#list listData as ld>
        <#if ld_has_next>
            ${ld.column_name},
        <#else>
            ${ld.column_name}
        </#if>
        </#list>
        FROM (
        SELECT
        <#list listData as ld>
            TB.${ld.column_name},
        </#list>
            ROWNUM RN
        FROM (
        SELECT
        <#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
        </#list>
        FROM ${tableName}
        <include refid="sql_where_listData"/>
        <choose>
            <when test="sort != null and order != ''">
                ORDER BY ${r"${sort}"} ${r"${order}"}
            </when>
            <otherwise>
            </otherwise>
        </choose>
        <![CDATA[ ) TB WHERE ROWNUM <= ${r"#{KEYROWS}"}) WHERE RN >= ${r"#{KEYRN}"}]]>
    </select>

    <!-- 数据列表总条数总记录数 -->
    <select id="listTotal" parameterType="HashMap" resultType="Integer">
        SELECT COUNT(${keyId})TOTAL FROM ${tableName}
        <include refid="sql_where_listData"/>
    </select>
	
	<!-- 根据id获取全字段数据 -->
	<select id="queryById" parameterType="String" resultType="pd">
		SELECT
		<#list listEdit as le>
		<#if le_has_next>
			${le.column_name},
		<#else>
			${le.column_name}
		</#if>
		</#list>
		FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"}
	</select>

	<!-- 查询全部数据 -->
	<select id="listAll" parameterType="HashMap" resultType="HashMap">
		SELECT
		<#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
		</#list>
		FROM ${tableName}
		<trim prefix="WHERE" prefixOverrides="AND">
		<#list listData as ld>
		<if test="${ld.column_name} != null and ${ld.column_name} != ''">
			AND ${ld.column_name} LIKE CONCAT('%',${r"#{"}${ld.column_name}${r"}"},'%')
		</if>
		</#list>
		</trim>
	</select>

    <!-- List_String批量更新操作 -->
    <update id="batchUpdateListString" parameterType="ArrayList">
        <foreach collection="list" item="item" index="index" open="begin" close=";end;" separator=";">
            UPDATE ${tableName}
            SET ${keyId} = ${r"#{"}item${r"}"}
            WHERE ${keyId} = ${r"#{"}item${r"}"}
        </foreach>
    </update>

    <!-- List_Map批量更新操作 -->
    <update id="batchUpdateListMap" parameterType="ArrayList">
        begin
        <foreach collection="list" item="item" index="index" separator=";" >
            UPDATE ${tableName}
            <set>
                ${keyId} = ${r"#{item."}${keyId}${r"}"},
            </set>
            WHERE ${keyId} = ${r"#{item."}${keyId}${r"}"}
        </foreach>
        ;end;
    </update>

    <!-- List_Map批量删除操作 -->
    <delete id="batchDeleteListMap" parameterType="ArrayList">
        DELETE FROM ${tableName} WHERE ${keyId} IN
        <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
        ${r"#{"}item${r"."}${keyId}${r"}"}
        </foreach>
    </delete>

    <!-- List_Map批量插入操作 -->
    <update id="batchAddListMap" parameterType="ArrayList">
        INSERT ALL
        <foreach item="item" index="index" collection="list">
            INTO ${tableName}
            <trim prefix="(" suffix=")" prefixOverrides="," suffixOverrides=",">
            <#list listData as le>
                <#if le_has_next>
            <if test="${r"item."}${le.column_name} != null and ${r"item."}${le.column_name} != ''">${le.column_name},</if>
                <#else>
            <if test="${r"item."}${le.column_name} != null and ${r"item."}${le.column_name} != ''">${le.column_name}</if>
                </#if>
            </#list>
            </trim>
            <trim prefix=" VALUES (" suffix=")" prefixOverrides="," suffixOverrides=",">
            <#list listData as le>
                <#if le_has_next>
            <if test="${r"item."}${le.column_name} != null and ${r"item."}${le.column_name} != ''">${r"#{item."}${le.column_name}${r"}"},</if>
                <#else>
            <if test="${r"item."}${le.column_name} != null and ${r"item."}${le.column_name} != ''">${r"#{item."}${le.column_name}${r"}"}</if>
                </#if>
            </#list>
            </trim>
        </foreach>
        SELECT 1 FROM DUAL
    </update>
</mapper>