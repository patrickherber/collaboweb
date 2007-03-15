<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="errorPage.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
<jsp:include flush="true" page="include/header.jsp"/>

<% 
	if (exception != null) { 
%>		
	<h1><bean:message key="errorPage.pageTitle"/></h1>
	<bean:message key="errorPage.message"/> 
	<p><b><%= exception %></b></p>
	<p><% exception.printStackTrace(); %></p>
<% } else { %>
	<logic:messagesPresent>
		<div class="error">
			<html:messages id="error">
				<bean:write name="error" filter="false" ignore="true"/><br/>
			</html:messages>
		</div>
	</logic:messagesPresent>				
<% } %>

<jsp:include flush="true" page="include/messages.jsp"/>

<jsp:include flush="true" page="include/footer.jsp"/>
	
</body>
</html>