<%--
  Created by IntelliJ IDEA.
  User: greg
  Date: 10/2/13
  Time: 3:13 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.dtolabs.rundeck.core.plugins.configuration.PropertyScope" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="base"/>
    <meta name="tabpage" content="configure"/>
    <title>Plugins</title>
</head>
<body>
<div class="row">
<div class="col-sm-3">
<g:render template="configNav" model="[selected: 'plugins']"/>
</div>
<div class="col-sm-9">
    <div class="row">
    <div class="col-sm-10">
    <h2>
        Installed and Bundled Plugins
    </h2>
    <div class="text-info">
        <g:markdown>Some plugin behavior can be configured by modifying the `project.properties` or `framework.properties`
        configuration files.</g:markdown>
    </div>
    </div>
    <div class="col-sm-2">
            <g:set var="pluginParams" value="${g.helpLinkParams(campaign: 'getpluginlink')}"/>
            <g:set var="pluginUrl" value="http://rundeck.org/plugins/?${pluginParams}"/>
            <g:set var="pluginLinkUrl"
                   value="${grailsApplication.config?.rundeck?.gui?.pluginLink ?: pluginUrl}"/>
            <a href="${pluginLinkUrl.encodeAsHTML()}" class="btn btn-success ">
                <g:message code="gui.admin.GetPlugins" default="Get Plugins"/>
                <i class="glyphicon glyphicon-arrow-right"></i>
            </a>
    </div>
    </div>
    <div class="row row-space">
    <div class="col-sm-12">
    <div class="panel-group" id="accordion">

<g:each in="${descriptions}" var="plugin">
    <g:set var="serviceName" value="${plugin.key}"/>
    <g:set var="pluginDescList" value="${plugin.value}"/>
    <g:set var="serviceDefaultScope"
           value="${serviceDefaultScopes && serviceDefaultScopes[serviceName] ? serviceDefaultScopes[serviceName] : PropertyScope.Project}"/>
    <g:set var="ukey" value="${g.rkey()}"/>
    <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#${ukey}">
                        <i class="glyphicon glyphicon-chevron-down"></i>
    <g:message code="framework.service.${serviceName}.label.plural" default="${serviceName}"/></a>

                    <g:if test="${pluginDescList.size()>0}">
                        <span class="label label-default">${pluginDescList.size()}</span>
                    </g:if>
                    <g:else>
                    <small>
                        (${pluginDescList.size()})
                    </small>
                    </g:else>
                    <span class="text-muted"><g:message code="framework.service.${serviceName}.description"
                                                        default=""/></span>
                </h4>
            </div>

            <div id="${ukey}" class="panel-collapse collapse">
                <div class="panel-body">
                <div class="panel-group" id="accordion${ukey}">
        <g:each in="${pluginDescList}" var="${pluginDescription}">
            <g:set var="pluginName" value="${pluginDescription.name}"/>
            <g:set var="pluginTitle" value="${pluginDescription.title}"/>
            <g:set var="pluginDesc" value="${pluginDescription.description}"/>


                <g:set var="ukeyx" value="${g.rkey()}"/>
    <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion${ukey}" href="#${ukeyx}">
                        <i class="glyphicon glyphicon-chevron-down"></i>
                    <i class="rdicon icon-small plugin"></i>
                    ${(pluginTitle?:pluginName).encodeAsHTML()}</a>

                    <g:if test="${pluginDesc}">
                        <span class="text-muted">${pluginDesc.encodeAsHTML()}</span>
                    </g:if>
                    <g:if test="${bundledPlugins&& bundledPlugins[serviceName] && bundledPlugins[serviceName].contains(pluginName)}">
                        <span class="label label-default pull-right">bundled</span>
                    </g:if>
                    <g:else>
                        <span class="label label-info pull-right">plugin</span>
                    </g:else>
                </h4>
            </div>

            <div id="${ukeyx}" class="panel-collapse collapse">
                <div class="panel-body">
                    <g:each in="${pluginDescription?.properties}" var="prop">
                        <g:set var="outofscope"
                               value="${prop.scope && !prop.scope.isInstanceLevel() && !prop.scope.isUnspecified()}"/>
                        <g:if test="${true}">
                            <g:render
                                    template="/framework/pluginConfigPropertyFormField"
                                    model="${[prop: prop, prefix: '',
                                            error: null,
                                            values: [:],
                                            fieldname: prop.name,
                                            origfieldname: 'orig.' + prefix + prop.name,
                                            outofscope: true,
                                            outofscopeShown: true,
                                            outofscopeOnly: true,
                                            defaultScope: serviceDefaultScope,
                                            pluginName: pluginName,
                                            serviceName: serviceName,
                                            mapping: pluginDescription.propertiesMapping,
                                            frameworkMapping: pluginDescription.fwkPropertiesMapping,
                                            hideMissingFrameworkMapping:(serviceName in ['NodeExecutor','FileCopier'])
                                    ]}"/>
                        </g:if>
                    </g:each>
                    <g:unless test="${pluginDescription?.properties?.any{!(it.scope==null||it.scope==PropertyScope.Unspecified? serviceDefaultScope.isInstanceLevel(): it.scope.isInstanceLevel())}}">
                        %{--no config properties--}%
                        <p class="text-muted">
                            No configuration
                        </p>
                    </g:unless>
                </div>
            </div>
            </div>
        </g:each>
            </div>
            <g:unless test="${pluginDescList}">
                <p class="text-muted">
        None
                </p>
            </g:unless>
                </div>
            </div>
    </div>
</g:each>
</div>
    </div>
    </div>
</div>
</div>
</body>
</html>
