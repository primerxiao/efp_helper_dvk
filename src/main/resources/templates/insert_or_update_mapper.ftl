<insert id="insertOrUpdate" parameterType="java.util.List">
   insert into ${tableName}(<#list classFields as field><#if field_index!=0>,</#if>${field.dasColumnName}</#list>)
      values
   <foreach collection="list" item="item" index="index" separator=",">
    <#list classFields as field>
    <#if field_index==0>(</#if><#noparse>#{</#noparse>item.${field.fieldName?uncap_first}}<#if field_has_next>,<#else>)</#if>
    </#list>
   </foreach>
   ON DUPLICATE KEY UPDATE
  <#list classFields as field>
   ${field.dasColumnName} = values(${field.dasColumnName})<#if field_has_next>,</#if>
  </#list>
</insert>