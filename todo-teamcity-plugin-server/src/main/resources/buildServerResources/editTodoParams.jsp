<%@ page import="org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<l:settingsGroup title="Source filter">
    <tr>
        <th><label for="org.r4d5.teamcity.todo.include">Include Patterns:<l:star/></label></th>
        <td><props:multilineProperty name="<%=TodoBuildRunnerConstants.PARAM_INCLUDE_REGEX%>" className="longField" cols="40" rows="4" expanded="true" linkTitle="Enter include patterns" />
            <span class="smallNote">Newline separated include patterns, like "dir/**/*.{java,cs}"</span><span class="error" id="error_org.r4d5.teamcity.todo.include"></span>
        </td>
    </tr>

    <tr>
        <th><label for="org.r4d5.teamcity.todo.exclude">Exclude Patterns:</label></th>
        <td><props:multilineProperty name="<%=TodoBuildRunnerConstants.PARAM_EXCLUDE_REGEX%>" className="longField" cols="40" rows="4" expanded="true" linkTitle="Enter exclude patterns" />
            <span class="smallNote">Newline separated exclude patterns, like "dir/**/*.{xml,config}"</span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Todo level filter">
    <tr>
        <th><label for="org.r4d5.teamcity.todo.minor">Minor level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX%>" className="longField" cols="40" rows="4" expanded="true" linkTitle="Enter minor level regexes" />
            <span class="smallNote">Newline separated regular expressions for todos with a minor level.</span><span class="error" id="error_org.r4d5.teamcity.todo.minor"></span>
        </td>
    </tr>
    <tr>
        <th><label for="org.r4d5.teamcity.todo.major">Major level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX%>" className="longField" cols="40" rows="4" expanded="true" linkTitle="Enter major level regexes" />
            <span class="smallNote">Newline separated regular expressions for todos with a major level.</span><span class="error" id="error_org.r4d5.teamcity.todo.major"></span>
        </td>
    </tr>
    <tr>
        <th><label for="org.r4d5.teamcity.todo.critical">Critical level:<l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=TodoBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX%>" className="longField" cols="40" rows="4" expanded="true" linkTitle="Enter critical level regexes" />
            <span class="smallNote">Newline separated regular expressions for todos with a critical level.</span><span class="error" id="error_org.r4d5.teamcity.todo.critical"></span>
        </td>
    </tr>
</l:settingsGroup>
