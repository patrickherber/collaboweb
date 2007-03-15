<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!doctype HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html locale="true">
<head>
	<title><bean:message key="application.title" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>

<jsp:include flush="true" page="include/header.jsp"/>
	
<h1><bean:message key="errors.pageNotFound.title"/></h1>

<bean:message key="errors.pageNotFound.text" arg0="<%= request.getContextPath() + '/' + response.encodeURL("welcome.html") %>"/>
	
<jsp:include page="include/footer.jsp"/>

</body>
</html:html>
