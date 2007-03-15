<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="app" uri="/WEB-INF/app.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><bean:message key="application.title" /> &gt; <bean:message key="attribute.pageTitle"/></title>
	<jsp:include flush="true" page="include/common-head.jsp"/>
</head>
<body>
	
<jsp:include flush="true" page="include/header.jsp"/>

<h2><bean:message key="attribute.pageTitle"/></h2>
	
<jsp:include flush="true" page="include/messages.jsp"/>

<html:form action="/attribute.html" focus="identifier">
	<table width="750" class="form">
		<tr>
			<td class="label"><bean:message key="attribute.identifier"/></td>
			<td><html:text property="identifier" /></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.description"/></td>
			<td>
				<img src="images/lang_en.gif" alt=""/>&nbsp;<html:text property="labels(en)" style="width: 300px;"/><br>
				<img src="images/lang_de.gif" alt=""/>&nbsp;<html:text property="labels(de)" style="width: 300px;"/><br>
				<img src="images/lang_fr.gif" alt=""/>&nbsp;<html:text property="labels(fr)" style="width: 300px;"/><br>
				<img src="images/lang_it.gif" alt=""/>&nbsp;<html:text property="labels(it)" style="width: 300px;"/>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.dataType"/></td>
			<td>
				<html:select property="dataType">
					<html:option value="1"><bean:message key="attribute.dataType.1"/></html:option>
					<html:option value="2"><bean:message key="attribute.dataType.2"/></html:option>
					<html:option value="3"><bean:message key="attribute.dataType.3"/></html:option>
					<html:option value="4"><bean:message key="attribute.dataType.4"/></html:option>
					<html:option value="5"><bean:message key="attribute.dataType.5"/></html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.choices"/></td>
			<td><html:text property="choices" style="width: 300px;"/></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.defaultValue"/></td>
			<td><html:text property="defaultValue" style="width: 300px;"/></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.calculated"/></td>
			<td>
				<html:radio property="calculated" value="true"><bean:message key="application.yes"/></html:radio>
				<html:radio property="calculated" value="false"><bean:message key="application.no"/></html:radio>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.customEditor"/></td>
			<td>
				<html:select property="editor">
					<html:option value="0">&nbsp;</html:option>
					<html:option value="1"><bean:message key="attribute.customEditor.1"/></html:option>
					<html:option value="2"><bean:message key="attribute.customEditor.2"/></html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.formatter"/></td>
			<td><html:text property="formatter" /></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.formOrder"/></td>
			<td><html:text property="formOrder" /></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.loadInList"/></td>
			<td>
				<html:radio property="loadInList" value="true"><bean:message key="application.yes"/></html:radio>
				<html:radio property="loadInList" value="false"><bean:message key="application.no"/></html:radio>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.searchFieldType"/></td>
			<td>
				<html:radio property="searchFieldType" value="1"><bean:message key="application.yes"/></html:radio>
				<html:radio property="searchFieldType" value="0"><bean:message key="application.no"/></html:radio>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.namePart"/></td>
			<td>
				<html:radio property="namePart" value="true"><bean:message key="application.yes"/></html:radio>
				<html:radio property="namePart" value="false"><bean:message key="application.no"/></html:radio>
			</td>
		</tr>
		<tr>
			<td class="label"><bean:message key="attribute.validations"/>
				<c:if test="${attributeForm.attributeId != 0}">
					&nbsp;<a href="<c:url value="validation.html?attributeId=${attributeForm.attributeId}"/>"><img src="images/add.gif" alt="<bean:message key="application.new"/>"/></a>
				</c:if>
			</td>
			<td>
				<table width="100%">
					<c:forEach var="validation" items="${attributeForm.validations}">
						<bean:define id="validationType" name="validation" property="validationType"/>
						<tr>
							<td width="55%"><app:writeLabel name="validationType" property="labels"/></td>
							<td width="30%"><c:out value="${validation.parameter}"/></td>
							<td width="15%">
								<a href="<c:url value="validation.html?attributeId=${attributeForm.attributeId}&amp;validationTypeId=${validation.validationTypeId}"/>"><img src="images/edit.gif" alt="<bean:message key="application.edit"/>"/></a>
								<a href="javascript:void(0);" onclick="return ask('<bean:message key="deleteQuestion.validation"/>','<c:url value="validation-delete.html?attributeId=${attributeForm.attributeId}&amp;validationTypeId=${validation.validationTypeId}"/>');" title="<bean:message key="application.delete"/>"><img src="images/delete.gif" alt="<bean:message key="application.delete"/>"/></a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<html:hidden property="attributeId"/>
				<html:hidden property="typeId"/>
				<input type="hidden" name="submitted" value="true">
			</td>
			<td>
				<html:submit styleClass="button"><bean:message key="application.save"/></html:submit>
				<input type="button" class="button" value="<bean:message key="application.back"/>" onclick="location.href='<c:url value="resource-type.html?typeId=${attributeForm.typeId}"/>';"/>
			</td>
		</tr>
	</table>
</html:form>

<jsp:include flush="true" page="include/footer.jsp"/>

</body>
</html>