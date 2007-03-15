<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="tag.pageTitle"/></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h1><bean:message key="tag.pageTitle"/>
		<c:if test="${not(empty(tags))}">
			:
			<c:set var="tagParam" value=""/>
			<c:forEach var="tag" items="${tags}" varStatus="status">
				<c:if test="${status.index > 0}">::</c:if>
				<a rel="tag" href="?${tagParam}&tag=${tag}"><c:out value="${tag}" /></a>
				<c:set var="tagParam" value="${tagParam}&tag=${tag}"/>
			</c:forEach>
		</c:if>
	</h1>
		
	<jsp:include flush="true" page="include/messages.jsp"/>
	
	<c:if test="${not(empty(tagCloud))}">
		<div class="tagCloud">
			<c:forEach var="tagCloudEntry" items="${tagCloud}" varStatus="status">
				<c:choose>
					<c:when test="${tags != null}">
						<c:url var="tagLink" value="/tag-view.html?${tagString}tag=${tagCloudEntry.tag}" />
					</c:when>
					<c:otherwise>
						<c:url var="tagLink" value="/tag-view.html?tag=${tagCloudEntry.tag}" />
					</c:otherwise>
				</c:choose>
				<c:set var="tagClass" value="cloud${tagCloudEntry.scale}" />
				<c:if test="${status.index > 0}">::</c:if>
				<a rel="tag" class="tag ${tagClass}" href="${tagLink}"><c:out value="${tagCloudEntry.tag}" /></a>
			</c:forEach>
		</div>
	</c:if>
   	
   	<c:if test="${not(empty(list))}">
   		<table class="list">
   			<thead>
   				<tr>
	   				<th width="35%"><bean:message key="tag.name"/></th>
	   				<th width="20%"><bean:message key="tag.type"/></th>
	   				<th width="20%"><bean:message key="tag.authorName"/></th>
	   				<th width="15%"><bean:message key="tag.updateDate"/></th>
	   				<th width="10%"></th>
	   				<th></th>
   				</tr>
   			</thead>
   			<tbody>
				<c:set var="bg" value="-1"/>
   				<c:forEach var="item" items="${list}">
					<bean:define id="resourceType" name="item" property="resourceType"/>
	 				<tr class="bg<c:out value="${bg}"/>">
   						<td><a href="<c:url value="space-view.html?resourceId=${item.resourceId}"/>"><c:out value="${item.name}"/></a></td>
   						<td><app:writeLabel name="resourceType" property="labels"/></td>
   						<td><bean:write name="item" property="authorName"/></td>
   						<td><bean:write name="item" property="updateDate" format="dd.MM.yyyy HH:mm"/></td>
   						<td>
   							<a href="<c:url value="resource.html?resourceId=${item.resourceId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
							<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.resource"/>','<c:url value="resource-delete.html?resourceId=${item.resourceId}&parentId=${item.parentId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
   						</td>   						
   					</tr>
					<c:set var="bg" value="${bg * -1}"/>
   				</c:forEach>
   			</tbody>
   		</table>
   	</c:if>

	<jsp:include flush="true" page="include/footer.jsp"/>
	
</body>
</html>