<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="time.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h1><bean:message key="time.pageTitle" /></h1>
		
	<jsp:include flush="true" page="include/messages.jsp"/>

	<jsp:include flush="true" page="include/footer.jsp"/>
	
</body>
</html>