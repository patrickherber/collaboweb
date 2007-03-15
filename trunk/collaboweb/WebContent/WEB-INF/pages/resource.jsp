<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="resource.pageTitle" /></title>
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
	
	<h1><bean:message key="resource.pageTitle"/></h1>
		
	<jsp:include flush="true" page="include/messages.jsp"/>

	<c:set var="htmlElements" value=""/>
	<html:form action="/resource.html" enctype="multipart/form-data">
		<table width="750" class="form">
			<c:forEach var="attribute" items="${resourceForm.resourceType.attributes}">
				<tr>
					<td class="label"><app:writeLabel name="attribute" property="labels"/></td>
					<td>
						<c:choose>
							<c:when test="${ attribute.calculated }">
								<c:choose>
									<c:when test="${empty(attribute.formatter)}">
										<bean:write name="resourceForm" property="attributeValues(${attribute.attributeId})" />
									</c:when>
									<c:otherwise>
										<bean:define id="formatter" name="attribute" property="formatter"/>
										<bean:write name="resourceForm" property="attributeValues(${attribute.attributeId})" format="<%= pageContext.findAttribute("formatter") +"" %>"/>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${empty(attribute.choices)}">
										<c:choose>
											<c:when test="${ attribute.dataType == 2}">					
												<html:text property="attributeValues(${attribute.attributeId})" onkeypress="return numbersonly(this, event);"/>
											</c:when>
											<c:when test="${ attribute.dataType == 3}">
												<html:text property="attributeValues(${attribute.attributeId})" onkeypress="return numbersonly(this, event, true);" />
											</c:when>
											<c:when test="${ attribute.dataType == 4}">
												<html:text property="attributeValues(${attribute.attributeId})" styleId="attributeValues${attribute.attributeId}" size="10" style="width: 70px" maxlength="10"/>
												<img src="images/calendar.gif" border="0" alt="" id="attributeValues${attribute.attributeId}Img" onclick="if(self.gfPop)gfPop.fPopCalendar(document.getElementById('attributeValues${attribute.attributeId}'));return false;">				
											</c:when>
											<c:when test="${ attribute.dataType == 5}">
												<html:file property="attributeValues(${attribute.attributeId})" size="50"/>
												<logic:present name="resourceForm" property="attributeValues(${attribute.attributeId})">
													<a href="<c:url value="attribute-open.html?attributeId=${attribute.attributeId}"/>&amp;resourceId=${resourceForm.resourceId}"><bean:write name="resourceForm" property="attributeValues(${attribute.attributeId})"/></a>
												</logic:present>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${ attribute.editor == 1}">
														<c:choose>
															<c:when test="${ attribute.editor == 1}"><c:set var="htmlElements" value="${htmlElements},attributeValues(${attribute.attributeId})"/></c:when>
															<c:otherwise><c:set var="htmlElements" value="attributeValues(${attribute.attributeId})"/></c:otherwise>
														</c:choose>
														<html:textarea property="attributeValues(${attribute.attributeId})" style="height:150px;width:500px;"/>
													</c:when>
													<c:when test="${ attribute.editor == 2}">
														<html:textarea property="attributeValues(${attribute.attributeId})" style="height:150px;width:500px;"/>
													</c:when>
													<c:otherwise>
														<html:text property="attributeValues(${attribute.attributeId})" style="width:500px;"/>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
										</c:when>
										<c:otherwise>
											<html:select property="attributeValues(${attribute.attributeId})">
												<c:forTokens var="choice" items="${attribute.choices}" delims=",">
													<html:option value="${choice}"/>
												</c:forTokens>
											</html:select>
										</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td class="label"><bean:message key="resource.visibility"/></td>
				<td>
					<html:select property="visibility">
						<html:option value="0"><bean:message key="resource.visibility.0"/></html:option>
						<html:option value="1"><bean:message key="resource.visibility.1"/></html:option>
						<html:option value="2"><bean:message key="resource.visibility.2"/></html:option>
					</html:select>
				</td>
			</tr>
			<c:if test="${resourceForm.resourceId != 0}">
				<tr>
					<td class="label"><bean:message key="resource.authorName"/></td>
					<td><bean:write name="resourceForm" property="authorName"/></td>
				</tr>
				<tr>
					<td class="label"><bean:message key="resource.createDate"/></td>
					<td><bean:write name="resourceForm" property="createDate" format="dd.MM.yyyy HH:mm"/></td>
				</tr>
				<tr>
					<td class="label"><bean:message key="resource.updateDate"/></td>
					<td><bean:write name="resourceForm" property="updateDate" format="dd.MM.yyyy HH:mm"/></td>
				</tr>
			</c:if>
			<tr>
				<td>
					<html:hidden property="parentId"/>
					<html:hidden property="resourceId"/>
					<html:hidden property="typeId"/>
					<input type="hidden" name="submitted" value="true">
				</td>
				<td>
					<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
					<input type="button" class="button" value="<bean:message key="application.back"/>" onclick="history.go(-1);"/>
				</td>
			</tr>
		</table>
	</html:form>
	<c:if test="${not(empty(htmlElements))}">
		<jsp:include flush="true" page="<%= "include/html-editor.jsp?field="+ pageContext.findAttribute("htmlElements")%>"/>
	</c:if>
	
	<script type="text/javascript">
		var focusControl = document.resourceForm.elements[0];
		if (focusControl != null && focusControl.type != "hidden" && !focusControl.disabled) {
     		focusControl.focus();
		}
	</script>
	
	<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>
