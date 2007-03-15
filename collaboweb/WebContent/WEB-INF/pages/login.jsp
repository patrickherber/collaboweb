<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="login.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>

<jsp:include flush="true" page="include/header.jsp" />

<h1><bean:message key="login.pageTitle" /></h1>

<jsp:include flush="true" page="include/messages.jsp" />

<html:form action="/login.html" focus="community">
	<table class="form">
		<tr>
			<td class="label"><bean:message key="login.community" /></td>
			<td><html:text property="community" /></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="login.email" /></td>
			<td><html:text property="email" /></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="login.password" /></td>
			<td><html:password property="password" /></td>
		</tr>
		<tr>
			<td><input type="hidden" name="submitted" value="true" /></td>
			<td><html:submit styleClass="button"><bean:message key="login.submit" /></html:submit></td>
		</tr>
	</table>
</html:form>

<jsp:include flush="true" page="include/footer.jsp" />

</body>
</html>
