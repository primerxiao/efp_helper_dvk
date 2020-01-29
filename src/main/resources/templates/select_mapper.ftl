<#function dashedToCamel(s)>
   <#return s
   ?replace('(^_+)|(_+$)', '', 'r')
   ?replace('\\_+(\\w)?', ' $1', 'r')
   ?replace('([A-Z])', ' $1', 'r')
   ?capitalize
   ?replace(' ' , '')
   ?uncap_first
   >
</#function>
<select id="${generateJava.currentMethodName}" resultMap="${dashedToCamel(dasTable.name)}List">
   SELECT
   <include refid="Base_Column_List"/>
   FROM ${dasTable.name} where
   <#list selectDasColumns as field>
      ${field.name}=<#noparse>#{</#noparse>${dashedToCamel(field.name)}}<#if field_has_next> and </#if>
   </#list>
</select>