<%@ page import="org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants" %>
<%@ page import="org.r4d5.teamcity.todo.common.TodoScanResult" %>
<%@ page import="org.r4d5.teamcity.todo.common.TodoLine" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="todoScanResultsReport" type="java.util.ArrayList<org.r4d5.teamcity.todo.common.TodoScanResult>" scope="request"/>

<div>
    <table>
        <thead>
            <tr style="background-color: ghostwhite">
                <th colspan="3" style="text-align: center">File name</th>
            </tr>
        </thead>
        <tbody>
            <% for (TodoScanResult todoScanResult : todoScanResultsReport) { %>
            <%  if (todoScanResult.getTodos().length > 0) { %>
            <tr style="background-color: lightgrey">
                <th colspan="3"><%= todoScanResult.getFilePath() %></th>
            </tr>
            <tr style="background-color: ghostwhite">
                <th>Line</th>
                <th>Todo</th>
                <th>Level</th>
            </tr>
            <%      for (TodoLine todoLine : todoScanResult.getTodos()) { %>
            <tr>
                <td style="text-align: right"><%= todoLine.getLineNumber() %></td>
                <td style="text-align: left"><%= todoLine.getLine() %></td>
                <td style="text-align: center">
                    <% switch (todoLine.getLevel()) {
                        case MINOR: %>
                        <img src="${teamcityPluginResourcesPath}question58.svg" height="16" width="16" border="0">

                    <%        break;
                        case MAJOR: %>
                        <img src="${teamcityPluginResourcesPath}triangle38.svg" height="16" width="16" border="0">
                    <%        break;
                        case CRITICAL: %>
                        <img src="${teamcityPluginResourcesPath}lightning46.svg" height="16" width="16" border="0">
                    <%        break;
                    } %>
                </td>
            </tr>
            <%      } %>
            <%  } %>
            <% } %>
        </tbody>
    </table>

    <div style="font-size: x-small; color: lightgrey; margin-top: 1cm">Icons made by
        <a style="color: darkgrey" href="http://www.flaticon.com/authors/daniel-bruce" title="Daniel Bruce">Daniel Bruce</a>,
        <a style="color: darkgrey" href="http://www.flaticon.com/authors/elegant-themes" title="Elegant Themes">Elegant Themes</a>,
        <a style="color: darkgrey" href="http://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from
        <a style="color: darkgrey" href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>
        are licensed by <a style="color: darkgrey" href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a>
    </div>

</div>

