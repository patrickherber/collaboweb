<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="search.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	<c:choose>
		<c:when test="${sessionScope.language == 'de'}">
			<bean:define id="popupJs" value="normal_de"/>
		</c:when>
		<c:when test="${sessionScope.language == 'fr'}">
			<bean:define id="popupJs" value="normal_fr"/>
		</c:when>
		<c:when test="${sessionScope.language == 'it'}">
			<bean:define id="popupJs" value="normal_it"/>
		</c:when>
		<c:otherwise>
			<bean:define id="popupJs" value="normal_en"/>
		</c:otherwise>
	</c:choose>	
	<iframe width=174 height=189 name="gToday:<bean:write name="popupJs"/>:agenda.js" id="gToday:<bean:write name="popupJs"/>:agenda.js" src="scripts/sec/cal_pop/ipopeng.htm" scrolling="no" frameborder="0" style="border: 0px;visibility:visible; z-index:999; position:absolute; top:-500px; left:-500px;"></iframe>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h1><bean:message key="search.pageTitle" /></h1>
		
	<jsp:include flush="true" page="include/messages.jsp"/>
	
	<html:form action="search.html">
		<table class="form">
			<tr>
				<td><bean:message key="search.quick" /></td>
				<td><html:text property="quickSearch" style="width: 200px;"/></td>
			</tr>
			<tr>
				<td><bean:message key="search.name" /></td>
				<td><html:text property="name" style="width: 200px;"/></td>
			</tr>
			<tr>
				<td><bean:message key="search.typeId" /></td>
				<td><html:select property="typeId">
						<option value="0">&nbsp;</option>
						<c:forEach var="type" items="${resourceTypes}">
							<c:choose>
								<c:when test="${type.typeId == searchForm.typeId}">
									<option  value="<c:out value="${type.typeId}"/>" selected="selected"><app:writeLabel name="type" property="labels"/></option>
								</c:when>
								<c:otherwise>
									<option  value="<c:out value="${type.typeId}"/>"><app:writeLabel name="type" property="labels"/></option>							
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><bean:message key="search.free" /></td>
				<td><html:text property="free" style="width: 200px;"/></td>
			</tr>
			<tr>
				<td><bean:message key="search.updateDate" /></td>
				<td>
					<html:select property="updateDateComparator">
						<html:option value="-1">&lt;</html:option>
						<html:option value="0">=</html:option>
						<html:option value="1">&gt;</html:option>
					</html:select>
					<html:text property="updateDate" styleId="updateDate" size="10" style="width: 70px" maxlength="10"/>
					<img src="images/calendar.gif" border="0" alt="" id="updateDateImg" onclick="if(self.gfPop)gfPop.fPopCalendar(document.getElementById('updateDate'));return false;">
				</td>
			</tr>
			<tr>
				<td><input type="hidden" name="submitted" value="true"/></td>
				<td><html:submit styleClass="button"><bean:message key="application.search"/></html:submit></td>
			</tr>
		</table>
	</html:form>
	
	<c:if test="${not(empty(list))}">
		<table class="list">
			<tr>
				<th width="20%"><bean:message key="search.name" /></th>
				<th width="20%"><bean:message key="search.typeId" /></th>
				<th width="20%"><bean:message key="search.updateDate" /></th>
				<th width="20%"><bean:message key="search.author" /></th>
				<th width="5%"></th>
			</tr>
			<c:set var="bg" value="-1"/>
			<c:forEach var="item" items="${list}">
		 		<tr class="bg<c:out value="${bg}"/>">
					<td><a href="<c:url value="space-view.html?resourceId=${item.resourceId}"/>"><c:out value="${item.name}"/></a></td>
					<td>
						<c:forEach var="type" items="${resourceTypes}">
							<c:if test="${item.typeId == type.typeId}">
								<app:writeLabel name="type" property="labels"/>
							</c:if>
						</c:forEach>
					</td>
					<td><bean:write name="item" property="modified" format="dd.MM.yyyy HH:mm"/></td>
					<td><c:out value="${item.authorName}"/></td>
					<td><bean:write name="item" property="score" format="0.00%"/></td>
				</tr>
				<c:set var="bg" value="${bg * -1}"/>
			</c:forEach>
		</table>
	</c:if>
	

	<jsp:include flush="true" page="include/footer.jsp"/>
	
</body>
</html>