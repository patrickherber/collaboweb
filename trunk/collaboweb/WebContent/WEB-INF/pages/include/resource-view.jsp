<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

 <c:if test="${resource != null}">
	<app:transform resource="${resource}" viewId="1">
  		<table class="form" width="500">
  			<tr>
  				<td class="label"><bean:message key="spaceView.name"/></td>
  				<td><c:out value="${resource.name}"/></td>
  			</tr>
  			<tr>
  				<td class="label"><bean:message key="spaceView.type"/></td>
  				<td><bean:define id="resourceType" name="resource" property="resourceType"/>
				<app:writeLabel name="resourceType" property="labels"/></td>
  			</tr>
  			<tr>
  				<td class="label"><bean:message key="spaceView.authorName"/></td>
  				<td><c:out value="${resource.authorName}"/></td>
  			</tr>
  			<tr>
  				<td class="label"><bean:message key="spaceView.updateDate"/></td>
  				<td><bean:write name="resource" property="updateDate" format="dd.MM.yyyy HH:mm"/></td>
  			</tr>
  			<tr>
  				<td class="label"><bean:message key="spaceView.createDate"/></td>
  				<td><bean:write name="resource" property="createDate" format="dd.MM.yyyy HH:mm"/></td>
  			</tr>
  		</table>
	</app:transform>
 	<br>
 	<a href="<c:url value="resource.html?resourceId=${resource.resourceId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
	<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.resource"/>','<c:url value="resource-delete.html?resourceId=${resource.resourceId}&amp;parentId=${resource.parentId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
	<c:if test="${ resource.parentId != 0 && resource.parentId != user.community.communityId}">
		<a href="<c:url value="space-view.html?resourceId=${resource.parentId}"/>"><img src="images/up.gif" alt="<bean:message key="spaceView.parent"/>"/></a>
 	</c:if>
	<c:if test="${not(empty(resource.resourceType.views[0].actions))}">
		<form action="execute-action.html" style="margin-left: 10px;display: inline;">
			<select name="actionId">
				<c:forEach var="action" items="${resource.resourceType.views[0].actions}">
					<option value="<c:out value="${action.actionId}"/>"><app:writeLabel name="action" property="labels"/></option>
				</c:forEach>
			</select>
			<input type="hidden" name="resourceId" value="<c:out value="${resource.resourceId}"/>"/>
			<input type="submit" class="button" value="<bean:message key="application.ok"/>"/>
		</form>
	</c:if>
 		
	<c:if test="${param.test == true}">
		<textarea><c:out value="${resource.xml}"/></textarea>
	</c:if>

</c:if>