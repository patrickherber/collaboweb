<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div id="header">

<div id="title"><bean:message key="application.title"/></div>

<div id="menu">
	<c:choose>
		<c:when test="${ userInfo != null }">
			<html:link href="welcome.html"><bean:message key="header.home"/></html:link>
			<c:if test="${userInfo.communityId != 0}">
				<html:link href="space-view.html"><bean:message key="header.spaces"/></html:link>
				<html:link href="time-view.html"><bean:message key="header.time"/></html:link>
				<html:link href="tag-view.html"><bean:message key="header.tag"/></html:link>
				<html:link href="search.html"><bean:message key="header.search"/></html:link>
				<html:link href="people.html"><bean:message key="header.people"/></html:link>
			</c:if>
			<html:link href="administration.html"><bean:message key="header.administration"/></html:link>
		</c:when>
		<c:otherwise>
			<html:link href="home.html"><bean:message key="header.home"/></html:link>
			<html:link href="login.html"><bean:message key="header.login"/></html:link>
		</c:otherwise>
	</c:choose>
</div>

<div id="languages">
	<a href="change-language.html?language=en"><img src="images/lang_en.gif" alt="EN"/></a>
	<a href="change-language.html?language=de"><img src="images/lang_de.gif" alt="DE"/></a>
	<a href="change-language.html?language=fr"><img src="images/lang_fr.gif" alt="FR"/></a>
	<a href="change-language.html?language=it"><img src="images/lang_it.gif" alt="IT"/></a>
</div>

</div>

<div id="content">