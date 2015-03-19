node '<#if hostname?has_content>${hostname}</#if>' {

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
}