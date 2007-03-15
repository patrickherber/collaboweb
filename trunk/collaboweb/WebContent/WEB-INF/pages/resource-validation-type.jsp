<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="resourceValidationType.pageTitle" /></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h2><bean:message key="resourceValidationType.pageTitle"/></h2>
		
	<jsp:include flush="true" page="include/messages.jsp"/>

	<html:form action="/resource-validation-type.html" focus="className">
		<table width="750" class="form">
			<tr>
				<td class="label"><bean:message key="resourceValidationType.className"/></td>
				<td><html:text property="className" style="width: 500px;"/></td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceValidationType.description"/></td>
				<td>
					<img src="images/lang_en.gif" alt=""/>&nbsp;<html:text property="labels(en)" style="width: 300px;"/><br>
					<img src="images/lang_de.gif" alt=""/>&nbsp;<html:text property="labels(de)" style="width: 300px;"/><br>
					<img src="images/lang_fr.gif" alt=""/>&nbsp;<html:text property="labels(fr)" style="width: 300px;"/><br>
					<img src="images/lang_it.gif" alt=""/>&nbsp;<html:text property="labels(it)" style="width: 300px;"/>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceValidationType.message"/></td>
				<td>
					<img src="images/lang_en.gif" alt=""/>&nbsp;<html:text property="messages(en)" style="width: 300px;"/><br>
					<img src="images/lang_de.gif" alt=""/>&nbsp;<html:text property="messages(de)" style="width: 300px;"/><br>
					<img src="images/lang_fr.gif" alt=""/>&nbsp;<html:text property="messages(fr)" style="width: 300px;"/><br>
					<img src="images/lang_it.gif" alt=""/>&nbsp;<html:text property="messages(it)" style="width: 300px;"/>
				</td>
			</tr>
			<tr>
				<td>
					<html:hidden property="communityId"/>
					<html:hidden property="validationTypeId"/>
					<input type="hidden" name="submitted" value="true">
				</td>
				<td>
					<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
					<input type="button" class="button" value="<bean:message key="application.back"/>" onclick="location.href='<c:url value="resource-validation-types.html"/>';"/>
				</td>
			</tr>
		</table>
	</html:form>
	
	<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>