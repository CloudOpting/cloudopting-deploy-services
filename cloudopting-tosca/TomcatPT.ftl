class { 'tomcat': }
class { 'java': 
distribution => 'jdk',}

tomcat::instance { '<#if tomcat?has_content>${tomcat}</#if>':
<#if catalina_base?has_content>catalina_base => ${catalina_base},</#if>
<#if source_url?has_content>source_url => ${source_url},</#if>
}->
tomcat::config::server { '<#if tomcat?has_content>${tomcat}</#if>':
  <#if catalina_base?has_content>catalina_base => ${catalina_base},</#if>
  port          => '8105',
}->
tomcat::config::server::connector { '<#if tomcat?has_content>${tomcat}</#if>-http':
  <#if catalina_base?has_content>catalina_base => ${catalina_base},</#if>
  port                  => '8180',
  protocol              => 'HTTP/1.1',
  additional_attributes => {
    'redirectPort' => '8543'
  },
}->
tomcat::config::server::connector { '<#if tomcat?has_content>${tomcat}</#if>-ajp':
  <#if catalina_base?has_content>catalina_base => ${catalina_base},</#if>
  port                  => '8109',
  protocol              => 'AJP/1.3',
  additional_attributes => {
    'redirectPort' => '8543'
  },
}->
tomcat::service { 'default':
<#if catalina_base?has_content>catalina_base => ${catalina_base},</#if>
}

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
