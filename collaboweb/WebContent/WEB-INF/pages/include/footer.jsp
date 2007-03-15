<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>

</div>

<div id="footer">
	<c:if test="${ userInfo != null }">
		<c:out value="${ userInfo.fullName }"/>&nbsp;
		<html:link page="/logout.html"><bean:message key="application.logout"/></html:link>
	</c:if>
</div>