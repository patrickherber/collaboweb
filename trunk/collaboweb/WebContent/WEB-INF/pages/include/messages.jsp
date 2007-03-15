<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:messagesPresent message="false">
	<div class="errors">
		<html:messages id="error" message="false">
			<bean:write name="error" filter="false" ignore="true"/><br/>
		</html:messages>
	</div>
</logic:messagesPresent>
<logic:messagesPresent message="true">
	<div class="messages">
		<html:messages id="error" message="true">
			<bean:write name="error" filter="false" ignore="true"/><br/>
		</html:messages>
	</div>
</logic:messagesPresent>
