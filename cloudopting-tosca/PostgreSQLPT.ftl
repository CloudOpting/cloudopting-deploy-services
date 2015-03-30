class { 'postgresql::server':
      <#if listen_addresses?has_content>listen_addresses => ${listen_addresses},</#if>
      <#if postgres_password?has_content>postgres_password => ${postgres_password},</#if>
      <#if port?has_content>port => ${port},</#if>
    }

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
