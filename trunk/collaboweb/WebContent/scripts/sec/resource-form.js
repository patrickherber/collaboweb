function removeOptions(sel) {
	for (var i = 0; i < sel.options.length; i++) {
		if (sel.options[i].selected) {
			sel.options[sel.selectedIndex] = null;
			i--;
		}
	}
	return false;
}

function selectAllOptions(theForm) {
	if (theForm != null) {
		selectOptions(theForm.newUsers);
		selectOptions(theForm.notificationGroupList);
		selectOptions(theForm.notificationUserList);
		selectOptions(theForm.approvalGroupList);
		selectOptions(theForm.approvalUserList);
	}
}

function selectOptions(sel) {
	if (sel != null) {
		for (var i = 0; i < sel.options.length; i++) {
			sel.options[i].selected = true;
		}
	}
}

function showDesc() {
	return showHide(true, false, false, false, false, false);
}

function showRights() {
	return showHide(false, false, true, false, false, false);
}

function showNotification() {
	return showHide(false, false, false, true, false, false);
}

function showAttributes() {
	return showHide(false, true, false, false, false, false);
}

function showInvitees() {
	return showHide(false, false, false, false, true, false);
}

function showApproval() {
	return showHide(false, false, false, false, false, true);
}

function showHide(desc, attributes, rights, notification, invitees, approval) {
	if (document.getElementById) { // DOM3 = IE5, NS6
		document.getElementById('formDesc').style.display = ((desc) ? '' : 'none');
		document.getElementById('tabDesc').className = ((desc) ? 'tab-sel' : '');
		document.getElementById('formAttributes').style.display = ((attributes) ? '' : 'none');
		document.getElementById('tabAttributes').className = ((attributes) ? 'tab-sel' : '');
		if (document.getElementById('formRights') != null) {
			document.getElementById('formRights').style.display = ((rights) ? '' : 'none');
			document.getElementById('tabRights').className = ((rights) ? 'tab-sel' : '');
		}
		if (document.getElementById('formNotification') != null) {
			document.getElementById('formNotification').style.display = ((notification) ? '' : 'none');
			document.getElementById('tabNotification').className = ((notification) ? 'tab-sel' : '');
		}
		if (document.getElementById('formInvitees') != null) {
			document.getElementById('formInvitees').style.display = ((invitees) ? '' : 'none');
			document.getElementById('tabInvitees').className = ((invitees) ? 'tab-sel' : '');
		}
		if (document.getElementById('formApproval') != null) {
			document.getElementById('formApproval').style.display = ((approval) ? '' : 'none');
			document.getElementById('tabApproval').className = ((approval) ? 'tab-sel' : '');
		}
	} else {
		if (document.layers) { // Netscape 4
			document.formDesc.display = ((desc) ? '' : 'none');
			document.tabDesc.className = ((desc) ? 'tab-sel' : '');
			document.formAttributes.display = ((attributes) ? '' : 'none');
			document.tabAttributes.className = ((attributes) ? 'tab-sel' : '');
			if (document.formRights != null) {
				document.formRights.display = ((rights) ? '' : 'none');
				document.tabRights.className = ((rights) ? 'tab-sel' : '');
			}
			if (document.formNotification != null) {
				document.formNotification.display = ((notification) ? '' : 'none');
				document.tabNotification.className = ((notification) ? 'tab-sel' : '');
			}
			if (document.formInvitees != null) {
				document.formInvitees.display = ((invitees) ? '' : 'none');
				document.tabInvitees.className = ((invitees) ? 'tab-sel' : '');
			}
			if (document.formApproval != null) {
				document.formApproval.display = ((approval) ? '' : 'none');
				document.tabApproval.className = ((approval) ? 'tab-sel' : '');
			}
		} else { // IE 4
			document.all.formDesc.style.display = ((desc) ? '' : 'none');
			document.all.tabDesc.className = ((desc) ? 'tab-sel' : '');
			document.all.formAttributes.style.display = ((attributes) ? '' : 'none');
			document.all.tabAttributes.className = ((attributes) ? 'tab-sel' : '');
			if (document.all.formRights != null) {
				document.all.formRights.style.display = ((rights) ? '' : 'none');
				document.all.tabRights.className = ((rights) ? 'tab-sel' : '');
			}
			if (document.all.formNotification != null) {
				document.all.formNotification.style.display = ((notification) ? '' : 'none');
				document.all.tabNotification.className = ((notification) ? 'tab-sel' : '');
			}
			if (document.all.formInvitees != null) {
				document.all.formInvitees.style.display = ((invitees) ? '' : 'none');
				document.all.tabInvitees.className = ((invitees) ? 'tab-sel' : '');
			}
			if (document.all.formApproval != null) {
				document.all.formApproval.style.display = ((approval) ? '' : 'none');
				document.all.tabApproval.className = ((approval) ? 'tab-sel' : '');
			}
		}
	}
	return false;
}

function changeTitleName(title) {
	var el = document.getElementById('titleName');
	if (el != null) {
		el.innerHTML = title;
	}
}

function removeRowFromTable(tableId, button) {
	var tbl = document.getElementById(tableId);
	for (var i = 1; i < tbl.rows.length; ++i) { 
		if (tbl.rows[i].cells[3].firstChild == button) {
			tbl.deleteRow(i);
			break;
		}
	}
	return false;
}

function switchChecked(el, checkName) {
	var other = FindObj(checkName);
	if (other != null) {
		other.checked = !el.checked;
	}
}

function addAttachment(btn, number) {
	var cell = document.getElementById('attachmentTd');
	if (cell != null) {
		var br = document.createElement('br');
		cell.appendChild(br);
		br = document.createElement('br');
		cell.appendChild(br);
		var el = document.createElement('input');
		el.type = 'file';
		el.name = 'attachment('+number+')';
		el.size = '78';
		cell.appendChild(el);
		cell.appendChild(document.createTextNode(' '));
		var button = document.createElement('input');
		button.type = 'button';
		button.value = btn.value;
		button.className = 'button';
		button.onclick = function () {
			addAttachment(button, number+1);
		};
		cell.removeChild(btn);
		cell.appendChild(button);
	}
}
