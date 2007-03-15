<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<c:if test="${not(empty(list))}">
  	<app:transformList resource="${resource}" list="${list}" viewId="2">	
		<form action="#" method="post">
			<table width="70%" class="list">
		   		<tr>
					<th width="1%"></th>
					<th width="37%"><bean:message key="spaceView.name"/></th>
					<th width="10%"><bean:message key="spaceView.type"/></th>
					<th width="12%"><bean:message key="spaceView.updateDate"/></th>
					<th width="20%"></th>
					<th width="4%"></th>
					<th width="15%"></th>
					<th width="1%" style="text-align: center;"><input type="checkbox" name="toggle" value="" onclick="toggleCheckBoxes(this);" title="<bean:message key="application.selectAll"/>"></th>
				</tr>
				<c:set var="bg" value="-1"/>
				<c:forEach var="item" items="${list}">
					<bean:define id="resourceType" name="item" property="resourceType"/>
				 	<tr class="bg<c:out value="${bg}"/>">
						<td style="text-align: center;"><a href="<c:url value="space-view.html?resourceId=${item.resourceId}"/>"><img src="images/detail.gif" alt="<bean:message key="application.detail"/>"/></a></td>
						<app:transform resource="${item}" viewId="3">
							<td><c:out value="${item.name}"/></td>
						</app:transform>
				   		<td><app:writeLabel name="resourceType" property="labels"/></td>
						<td><bean:write name="item" property="updateDate" format="dd.MM.yyyy HH:mm"/></td>
			   			<td style="text-align: right;">
			   				<app:hasActionsForView resource="${item}" viewTypeId="3">
					   			<select id="action<c:out value="${item.resourceId}"/>">
					   				<app:forEachViewAction resource="${item}" viewTypeId="3" id="action">
				   						<option value="<c:out value="${action.actionId}"/>"><app:writeLabel name="action" property="labels"/></option>
				   					</app:forEachViewAction>
				   				</select>
				   				<input type="button" class="button" value="<bean:message key="application.ok"/>" onclick="executeAction(<c:out value="${item.resourceId}"/>);"/>
					   		</app:hasActionsForView>
				   		</td>
						<td>
							<a href="<c:url value="resource.html?resourceId=${item.resourceId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
							<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.resource"/>','<c:url value="resource-delete.html?resourceId=${item.resourceId}&amp;parentId=${item.parentId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
						</td>  
						<td><a:ajax name="scriptaculous.inplace" value="Add tag" service="/tag-add.html?resourceId=${item.resourceId}"/></td>
						<td align="center"><input type="checkbox" name="resourceId" value="<c:out value="${item.resourceId}"/>"/></td>
					</tr>
					<c:set var="bg" value="${bg * -1}"/>
				</c:forEach>
				<tr class="actions">
					<td colspan="10" align="right">
						<select name="selectedAction">
							<option value=""><bean:message key="application.actionSelection"/></option>
							<option value="<c:url value="clipboard-add.html"/>"><bean:message key="application.clipboardAddTo"/></option>
						</select>&nbsp;
						<input type="submit" class="button" onclick="return processAction(this.form);" value="<bean:message key="application.ok"/>">
					</td>
				</tr>
			</table>
		</form>
	</app:transformList>
</c:if>