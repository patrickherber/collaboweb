<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<script language="javascript" type="text/javascript" src="scripts/sec/tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript">
tinyMCE.init({
	<c:if test="${not(empty(sessionScope.language))}">
		language : "<c:out value="${sessionScope.language}"/>",		
	</c:if>
	mode : "exact",
	elements : "<c:out value="${param.field}"/>",
	theme : "advanced",
	force_br_newlines : true,
	force_p_newlines : false,
	plugins : "table,advhr,advimage,advlink,searchreplace,contextmenu,preview",
	theme_advanced_buttons1: "cut,copy,paste,separator,undo,redo,separator,search,replace,separator,bold,italic,underline,separator,forecolor,backcolor,separator,justifyleft,justifycenter,justifyright,justifyfull,separator,code,cleanup,removeformat,preview",
	theme_advanced_buttons2: "fontselect,fontsizeselect,separator,bullist,numlist,separator,link,unlink,image,hr,separator,table,separator",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left"
});
</script>