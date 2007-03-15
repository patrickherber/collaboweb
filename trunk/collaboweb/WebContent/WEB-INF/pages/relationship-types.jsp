<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="relationshipTypes.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
<jsp:include flush="true" page="include/header.jsp"/>

<h2><bean:message key="relationshipTypes.pageTitle" /></h2>

<jsp:include flush="true" page="include/messages.jsp"/>

<table width="500" class="list">
	<thead>
		<tr>
			<th width="20%"><img src="images/lang_en.gif" alt=""/></th>
			<th width="20%"><img src="images/lang_de.gif" alt=""/></th>
			<th width="20%"><img src="images/lang_fr.gif" alt=""/></th>
			<th width="20%"><img src="images/lang_it.gif" alt=""/></th>
			<th width="20%"></th>
		</tr>
	</thead>
	<tbody>
		<c:set var="bg" value="-1"/>
		<html:form action="/relationship-type.html" focus="labels(en)">
	 		<tr class="bg<c:out value="${bg}"/>">
				<td><html:text property="labels(en)"/></td>
				<td><html:text property="labels(de)"/></td>
				<td><html:text property="labels(fr)"/></td>
				<td><html:text property="labels(it)"/></td>
				<td>
					<input type="hidden" name="submitted" value="true"/>
					<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
				</td>
			</tr>
		</html:form>
		<c:set var="bg" value="${bg * -1}"/>
		<c:forEach var="item" items="${list}">
			<c:choose>
				<c:when test="${item.communityId == userInfo.communityId}">
					<html:form action="/relationship-type.html">
				 		<tr class="bg<c:out value="${bg}"/>">
							<td><html:text name="item" property="labels(en)"/></td>
							<td><html:text name="item" property="labels(de)"/></td>
							<td><html:text name="item" property="labels(fr)"/></td>
							<td><html:text name="item" property="labels(it)"/></td>
							<td>
								<html:hidden name="item" property="communityId"/>
								<html:hidden name="item" property="relationshipTypeId"/>
								<input type="hidden" name="submitted" value="true"/>
								<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
								<input type="button" class="button" onclick="return ask('<bean:message key="deleteQuestion.relationshipType"/>','<c:url value="relationship-type-delete.html?relationshipTypeId=${item.relationshipTypeId}"/>');" value="<bean:message key="application.delete"/>">
		   					</td>
						</tr>
					</html:form>
				</c:when>
				<c:otherwise>
					<tr class="bg<c:out value="${bg}"/>">
						<td><c:out value="${item.labels['en']}"/></td>
						<td><c:out value="${item.labels['de']}"/></td>
						<td><c:out value="${item.labels['fr']}"/></td>
						<td><c:out value="${item.labels['it']}"/></td>
						<td></td>
					</tr>
				</c:otherwise>
			</c:choose>
			<c:set var="bg" value="${bg * -1}"/>
		</c:forEach>
	</tbody>
</table>

<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>
