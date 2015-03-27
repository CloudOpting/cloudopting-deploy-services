<#list modData as module>
mod '<#if module['module']?has_content>${module['module']}</#if>',
  <#if module['git']?has_content>:git => ${module['git']}</#if>

</#list>
