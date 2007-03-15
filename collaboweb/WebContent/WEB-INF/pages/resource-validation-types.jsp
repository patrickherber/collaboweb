<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="resourceValidationTypes.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
<jsp:include flush="true" page="include/header.jsp"/>

<h2><bean:message key="resourceValidationTypes.pageTitle"/></h2>

<jsp:include flush="true" page="include/messages.jsp"/>

<c:if test="${ not(empty(list)) }">
	<table class="list">
		<thead>
			<tr>
				<th width="45%"><bean:message key="resourceValidationTypes.description"/></th>
				<th width="50%"><bean:message key="resourceValidationTypes.className"/></th>
				<th width="5%"></th>
			</tr>
		</thead>
		<tbody>
			<c:set var="bg" value="-1"/>
			<c:forEach var="item" items="${list}">
		 		<tr class="bg<c:out value="${bg}"/>">
					<td><app:writeLabel name="item" property="labels"/></td>
					<td><c:out value="${item.className}"/></td>
					<td>
						<c:if test="${item.communityId == userInfo.communityId}">
							<a href="<c:url value="/resource-validation-type.html?validationTypeId=${item.validationTypeId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
 							<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.resourceValidationType"/>','<c:url value="/resource-validation-type-delete.html?validationTypeId=${item.validationTypeId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
						</c:if>
   					</td>
				</tr>
				<c:set var="bg" value="${bg * -1}"/>
			</c:forEach>
		</tbody>
	</table>	
</c:if>
<br>

<a href="<c:url value="/resource-validation-type.html"/>"><img src="images/add.gif" alt="<bean:message key="application.new"/>"/></a>  	

<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>
