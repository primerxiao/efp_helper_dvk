<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoQuaName}">

    <resultMap type="${domainQuaName}" id="${baseClassName?uncap_first}List">
        <#list classFields as field>
            <result property="${field.fieldName?uncap_first}" column="${field.dasColumnName}"/>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        ${baseColumnList}
    </sql>

    <insert id="insert" parameterType="${domainQuaName}">
        insert into ${tableName}(<#list classFields as field><#if field_index!=0>,</#if>${field.dasColumnName}</#list>)
        values (<#list classFields as field><#if field_index!=0>,</#if><#noparse>#{</#noparse>${field.fieldName?uncap_first}}</#list>)
    </insert>

    <delete id="deleteByPk" parameterType="${domainQuaName}">
        delete from ${tableName} where <#list classFields as field><#if field.primaryKey><#if field_index!=0> and </#if>${field.fieldName?uncap_first}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}</#if></#list>
    </delete>

    <update id="updateByPk" parameterType="${domainQuaName}">
        update ${tableName}
        <trim prefix="set" suffixOverrides=",">
            <#list classFields as field>
                <if test="${field.fieldName?uncap_first}!=null">${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}},</if>
            </#list>
        </trim>
        where <#list classFields as field><#if field.primaryKey><#if field_index!=0> and </#if>${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}</#if> </#list>
    </update>

    <select id="queryByPk" resultMap="${baseClassName?uncap_first}List" parameterType="${domainQuaName}">
        select
        <include refid="Base_Column_List"/>
        from ${tableName} where  <#list classFields as field><#if field.primaryKey><#if field_index!=0> and </#if>${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}</#if></#list>
    </select>

</mapper>
