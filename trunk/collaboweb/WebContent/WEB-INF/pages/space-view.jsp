<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title"/> &gt; <bean:message key="spaceView.pageTitle"/></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
	<script type="text/javascript" src="resources/jmaki.js"></script>
	<script type="text/javascript" src="resources/libs/scriptaculous/version1.6.4/prototype.js"></script>
	<script type="text/javascript" src="resources/libs/scriptaculous/version1.6.4/scriptaculous.js"></script>
	<script type="text/javascript" src="resources/libs/scriptaculous/version1.6.4/dragdrop.js"></script>
	<script type="text/javascript" src="resources/libs/scriptaculous/version1.6.4/effects.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/scriptaculous/inplace/component.css"></link>
	<script type="text/javascript" src="resources/scriptaculous/inplace/component.js"></script>
	<script type="text/javascript">
		function executeAction(resourceId) {
			var select = document.getElementById('action'+resourceId);
			if (select != null) {
				var action = select.options[select.selectedIndex].value;
				location.href='<c:url value="execute-action.html"/>?actionId=' + action + '&resourceId=' + resourceId;
			}
		}
	</script>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h1><bean:message key="spaceView.pageTitle"/></h1>
		
	<jsp:include flush="true" page="include/messages.jsp"/>
	
	<c:if test="${resource != null}">
	<table width="100%">
		<tr valign="top">
			<td width="70%">
				<c:if test="${ resource.parentId != 0 && resource.resourceId != userInfo.community.resourceRootId}">
					<jsp:include flush="true" page="include/resource-view.jsp"/>
				</c:if>
			</td>
			<td width="30%">
				<c:if test="${not(empty(supportedChildren))}">
					<form action="<c:url value="resource.html"/>" style="display: inline;">
						<bean:message key="application.new"/>
						&nbsp;
						<select name="typeId">
							<c:forEach var="type" items="${supportedChildren}">
								<option value="<c:out value="${type.typeId}"/>"><app:writeLabel name="type" property="labels"/></option>
							</c:forEach>
						</select>
						<input type="hidden" name="parentId" value="<c:out value="${resource.resourceId}"/>"/>
						&nbsp;
						<input type="submit" class="button" value="<bean:message key="application.ok"/>"/>
					</form>
					&nbsp;		
				</c:if>
				<c:if test="${not(empty(clipboard))}">
					<h2><bean:message key="application.clipboard"/></h2>
					<a href="<c:url value="move.html?resourceId=${resource.resourceId}"/>"><bean:message key="application.move"/></a>
					&nbsp;
					<a href="<c:url value="clipboard-empty.html"/>"><bean:message key="application.clipboardEmpty"/></a>
				</c:if>
			</td>
		</tr>
		<tr valign="top">
			<td width="70%">
				<jsp:include flush="true" page="include/resource-list.jsp"/>			
			</td>
			<td width="30%">
			   	<c:if test="${not(empty(resource.relationships)) || not(empty(clipboard))}">
					<h2><bean:message key="spaceView.relationships"/></h2>
					<c:if test="${not(empty(clipboard))}">
						<form action="<c:url value="relationship-add.html"/>">
							<bean:message key="spaceView.addRelationshipFromClipboard"/>
							&nbsp;
							<select name="relationshipTypeId">
								<c:forEach var="type" items="${relationshipTypes}">
									<option value="<c:out value="${type.relationshipTypeId}"/>"><app:writeLabel name="type" property="labels"/></option>
								</c:forEach>
							</select>
							<input type="hidden" name="resourceId" value="<c:out value="${resource.resourceId}"/>"/>
							&nbsp;
							<input type="submit" class="button" value="<bean:message key="application.ok"/>"/>
						</form>
				 	</c:if>
			   		<c:if test="${not(empty(resource.relationships))}">
						<table width="100%">
							<c:forEach var="item" items="${resource.relationships}">
								<bean:define id="relationshipType" name="item" property="relationshipType"/>
							 	<tr>
									<td width="1%">
										<c:choose>
											<c:when test="${item.outgoing}"><img src="images/next.gif" alt=""/></c:when>
											<c:otherwise><img src="images/previous.gif" alt=""/></c:otherwise>
										</c:choose>
									<td width="68%"><a href="<c:url value="space-view.html?resourceId=${item.relatedResource.resourceId}"/>"><c:out value="${item.relatedResource.name}"/></a></td>
									<td width="30%"><app:writeLabel name="relationshipType" property="labels"/></td>
									<td width="1%">
										<c:choose>
											<c:when test="${item.outgoing}">
												<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.relationship"/>','<c:url value="relationship-delete.html?resourceId=${resource.resourceId}&relationshipTypeId=${item.relationshipType.relationshipTypeId}&referencedId=${item.relatedResource.resourceId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
											</c:when>
											<c:otherwise>
												<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.relationship"/>','<c:url value="relationship-delete.html?resourceId=${item.relatedResource.resourceId}&relationshipTypeId=${item.relationshipType.relationshipTypeId}&referencedId=${resource.resourceId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
											</c:otherwise>
										</c:choose>
									</td>  
								</tr>
								<c:set var="bg" value="${bg * -1}"/>
							</c:forEach>
						</table>
					</c:if>
			   	</c:if>
			   	
				<h2><bean:message key="spaceView.tags"/></h2>
			   	<c:if test="${not(empty(resource.tags))}">
					<div class="tagCloud">
						<c:forEach var="tagCloudEntry" items="${resource.tags}" varStatus="status">
							<c:url var="tagLink" value="/tag-view.html?tag=${tagCloudEntry.tag}" />
							<c:set var="tagClass" value="cloud${tagCloudEntry.scale}" />
							<c:if test="${status.index > 0}">::</c:if>
							<a rel="tag" class="tag ${tagClass}" href="${tagLink}">
								<c:out value="${tagCloudEntry.tag}" />
							</a>
						</c:forEach>
					</div>
			   	</c:if>
			   	<bean:define id="tagLabel"><bean:message key="tag.add"/></bean:define>
			   	<div style="text-align: center;"><a:ajax name="scriptaculous.inplace" value="${tagLabel}" service="/tag-add.html?resourceId=${resource.resourceId}"/></div>
			</td>
		</tr>
	</table>
	</c:if>
   	
	<jsp:include flush="true" page="include/footer.jsp"/>
	
</body>
</html>