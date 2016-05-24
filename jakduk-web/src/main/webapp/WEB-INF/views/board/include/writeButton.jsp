<%--
  Created by IntelliJ IDEA.
  User: pyohwan
  Date: 16. 5. 14
  Time: 오후 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
    <c:when test="${param.authRole == 'ANNONYMOUS'}">
        <button type="button" class="btn-u btn-brd rounded" onclick="needLogin();"
                tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="board.write"/>'>
            <span aria-hidden="true" class="icon-pencil"></span>
        </button>
    </c:when>
    <c:when test="${param.authRole == 'USER'}">
        <button type="button" class="btn-u btn-brd rounded" onclick="location.href='<c:url value="/board/free/write"/>'"
                tooltip-popup-close-delay='300' uib-tooltip='<spring:message code="board.write"/>'>
            <span aria-hidden="true" class="icon-pencil"></span>
        </button>
    </c:when>
</c:choose>
