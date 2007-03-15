<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="administration.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h1><bean:message key="administration.pageTitle" /></h1>
	
	<jsp:include flush="true" page="include/messages.jsp"/>

	<ul>
		<li><a href="resource-types.html"><bean:message key="administration.resourceTypes" /></a></li>
		<li><a href="validation-types.html"><bean:message key="administration.validationTypes" /></a></li>
		<li><a href="resource-validation-types.html"><bean:message key="administration.resourceValidationTypes" /></a></li>
		<li><a href="view-types.html"><bean:message key="administration.viewTypes" /></a></li>
		<li><a href="actions.html"><bean:message key="administration.actions" /></a></li>
		<li><a href="relationship-types.html"><bean:message key="administration.relationshipTypes" /></a></li>
	</ul>
   	
	<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>
