<%@ page import="org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    Include Patterns:<strong><props:displayValue name="<%=TodoBuildRunnerConstants.PARAM_INCLUDE_REGEX%>" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Exclude Patterns:<strong><props:displayValue name="<%=TodoBuildRunnerConstants.PARAM_EXCLUDE_REGEX%>" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Minor level:<strong><props:displayValue name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX%>" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Major level:<strong><props:displayValue name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX%>" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Critical level:<strong><props:displayValue name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX%>" emptyValue="none"/></strong>
</div>
