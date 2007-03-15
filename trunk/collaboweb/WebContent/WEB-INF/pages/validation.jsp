<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="validation.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h2><bean:message key="validation.pageTitle" /></h2>
		
	<jsp:include flush="true" page="include/messages.jsp"/>

	<html:form action="/validation.html" focus="validationTypeId">
		<table width="750" class="form">
			<tr>
				<td class="label"><bean:message key="validation.validationType" /></td>
				<td>
					<html:select property="validationTypeId">
						<c:forEach var="item" items="${validationTypes}">
							<html:option value="${item.validationTypeId}"><app:writeLabel name="item" property="labels"/></html:option>
						</c:forEach>
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="validation.parameter" /></td>
				<td><html:text property="parameter"/></td>
			</tr>
			<tr>
				<td>
					<html:hidden property="attributeId"/>
					<input type="hidden" name="submitted" value="true">
				</td>
				<td>
					<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
					<input type="button" class="button" value="<bean:message key="application.back"/>" onclick="location.href='<c:url value="attribute.html?attributeId=${validationForm.attributeId}"/>';"/>
				</td>
			</tr>
		</table>
	</html:form>
	
	<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>