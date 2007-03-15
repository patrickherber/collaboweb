<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="resourceType.pageTitle"/></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
	<jsp:include flush="true" page="include/header.jsp"/>
	
	<h2><bean:message key="resourceType.pageTitle"/></h2>
		
	<jsp:include flush="true" page="include/messages.jsp"/>

	<html:form action="/resource-type.html">
		<table width="750" class="form">
			<tr>
				<td class="label"><bean:message key="resourceType.description"/></td>
				<td>
					<img src="images/lang_en.gif" alt=""/>&nbsp;<html:text property="labels(en)" style="width: 300px;"/><br>
					<img src="images/lang_de.gif" alt=""/>&nbsp;<html:text property="labels(de)" style="width: 300px;"/><br>
					<img src="images/lang_fr.gif" alt=""/>&nbsp;<html:text property="labels(fr)" style="width: 300px;"/><br>
					<img src="images/lang_it.gif" alt=""/>&nbsp;<html:text property="labels(it)" style="width: 300px;"/>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceType.timeView"/></td>
				<td>
					<html:radio property="timeView" value="true"/><bean:message key="application.yes"/>
					<html:radio property="timeView" value="false"/><bean:message key="application.no"/>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceType.attributes"/>
				<c:if test="${resourceTypeForm.typeId != 0}">
				&nbsp;<a href="<c:url value="attribute.html?typeId=${resourceTypeForm.typeId}"/>"><img src="images/add.gif" alt="<bean:message key="application.new"/>"/></a>
				</c:if>
				</td>
				<td>
					<c:if test="${not(empty(resourceTypeForm.attributes))}">
					<table width="100%">
						<c:forEach var="attribute" items="${resourceTypeForm.attributes}">
							<tr>
								<td width="50%"><app:writeLabel name="attribute" property="labels"/></td>
								<td width="35%"><bean:message key="attribute.dataType.${attribute.dataType}"/></td>
   								<td width="15%">
   									<a href="<c:url value="attribute.html?attributeId=${attribute.attributeId}&amp;typeId=${attribute.typeId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
   									<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.attribute"/>','<c:url value="attribute-delete.html?attributeId=${attribute.attributeId}&amp;typeId=${attribute.typeId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
   								</td>
							</tr>
						</c:forEach>
					</table>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceType.validations"/>
					<c:if test="${resourceTypeForm.typeId != 0}">
						&nbsp;<a href="<c:url value="resource-validation.html?typeId=${resourceTypeForm.typeId}"/>"><img src="images/add.gif" alt="<bean:message key="application.new"/>"/></a>
					</c:if>
				</td>
				<td>
					<c:if test="${not(empty(resourceTypeForm.resourceType.resourceValidations))}">
					<table width="100%">
						<c:forEach var="validation" items="${resourceTypeForm.resourceType.resourceValidations}">
							<bean:define id="validationType" name="validation" property="validationType"/>
							<tr>
								<td width="85%"><app:writeLabel name="validationType" property="labels"/></td>
   								<td width="15%">
   									<a href="<c:url value="resource-validation.html?typeId=${resourceTypeForm.typeId}&amp;validationTypeId=${validationType.validationTypeId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
   									<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.resourceValidation"/>','<c:url value="resource-validation-delete.html?typeId=${resourceTypeForm.typeId}&amp;validationTypeId=${validationType.validationTypeId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
   								</td>   								
							</tr>
						</c:forEach>
					</table>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceType.views"/>
					<c:if test="${resourceTypeForm.typeId != 0}">
						&nbsp;<a href="<c:url value="view.html?typeId=${resourceTypeForm.typeId}"/>"><img src="images/add.gif" alt="<bean:message key="application.new"/>"/></a>
					</c:if>
				</td>
				<td>
					<c:if test="${not(empty(resourceTypeForm.resourceType.views))}">
					<table width="100%">
						<c:forEach var="view" items="${resourceTypeForm.resourceType.views}">
							<bean:define id="viewType" name="view" property="viewType"/>
							<tr>
								<td width="50%"><app:writeLabel name="viewType" property="labels"/></td>
								<td width="35%"><c:out value="${viewType.contentType}"/></td>
   								<td width="15%">
   									<a href="<c:url value="view.html?typeId=${resourceTypeForm.typeId}&amp;viewTypeId=${viewType.viewTypeId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
   									<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.view"/>','<c:url value="view-delete.html?typeId=${resourceTypeForm.typeId}&amp;viewTypeId=${viewType.viewTypeId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
   								</td>   								
							</tr>
						</c:forEach>
					</table>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label"><bean:message key="resourceType.supportedChildren"/></td>
				<td>
					<html:select property="supportedChildrenIds" multiple="true" size="6">
						<c:forEach var="type" items="${resourceTypes}">
							<html:option value="${type.typeId}"><app:writeLabel name="type" property="labels"/></html:option>
						</c:forEach>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<html:hidden property="typeId"/>
					<input type="hidden" name="submitted" value="true">
				</td>
				<td>
					<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
					<input type="button" class="button" value="<bean:message key="application.back"/>" onclick="location.href='<c:url value="resource-types.html"/>';"/>
				</td>
			</tr>
		</table>
	</html:form>
	
	<script type="text/javascript">
		var focusControl = document.resourceTypeForm.elements[0];
		if (focusControl != null && focusControl.type != "hidden" && !focusControl.disabled) {
     		focusControl.focus();
		}
	</script>
	
	<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>