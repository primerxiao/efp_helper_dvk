<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<#assign x = "${generateInfo.baseMoudleName}">
<mapper namespace="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.mapper.${generateInfo.basicClassName}Mapper">

    <resultMap type="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO" id="${generateInfo.basicClassName?uncap_first}List">
        <#list generateInfo.classFields as field>
            <result property="${field.fieldName?uncap_first}" column="${field.dasColumnName}"/>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list generateInfo.classFields as classField>${classField.dasColumnName}<#sep>, </#sep></#list>
    </sql>

    <insert id="insertSingle" parameterType="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO">
        insert into ${generateInfo.dasTable.name}(
            <#list generateInfo.classFields as field>${field.dasColumnName}<#sep>, </#sep></#list>
        )
        values (
            <#list generateInfo.classFields as field><#noparse>#{</#noparse>${field.fieldName?uncap_first}}<#sep>, </#sep></#list>
        )
    </insert>

    <delete id="deleteByPk" parameterType="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO">
        delete from
            ${generateInfo.dasTable.name}
        where
            <#list generateInfo.pkClassFields as field>${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}<#sep> and </#sep></#list>
    </delete>

    <update id="updateByPk" parameterType="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO">
        update
            ${generateInfo.dasTable.name}
        <trim prefix="set" suffixOverrides=",">
            <#list generateInfo.classFields as field>
                <if test="${field.fieldName?uncap_first}!=null">
                    ${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}},
                </if>
            </#list>
        </trim>
        where
            <#list generateInfo.pkClassFields as field>${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}<#sep> and </#sep></#list>
    </update>

    <select id="queryByPk" resultMap="${generateInfo.basicClassName?uncap_first}List" parameterType="com.fdb.a.${simpleBaseModuleNameMethod(x)}.infra.persistence.po.${generateInfo.basicClassName}PO">
        select
        <include refid="Base_Column_List"/>
        from ${generateInfo.dasTable.name}
        where
            <#list generateInfo.pkClassFields as field>${field.dasColumnName}=<#noparse>#{</#noparse>${field.fieldName?uncap_first}}<#sep> and </#sep></#list>
    </select>

</mapper>
