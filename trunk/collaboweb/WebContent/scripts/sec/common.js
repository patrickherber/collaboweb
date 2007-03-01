function doReset(theForm) {
	for (var i = 0; i < theForm.elements.length; i++) {
		if (theForm.elements[i].type == 'text') {
			theForm.elements[i].value = '';
		} else if (theForm.elements[i].type == 'checkbox') {
			theForm.elements[i].checked = false;
		}
	}
	for (var i = 0; i < theForm.elements.length; i++) {
		if (theForm.elements[i].type == 'text' && !theForm.elements[i].disabled) {
			theForm.elements[i].focus();	
			break;
		}
	}
	return false;
}
function sFindObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=sFindObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function openWindow(url, pageRef, pwidth, pheight, showToolbar) {
	openReturnWindow(url, pageRef, pwidth, pheight, showToolbar);
	return false;
}
function openReturnWindow(url, pageRef, pwidth, pheight, showToolbar) {
	var x = screen.width;
	var y = screen.height;
	var popup=window.open(url, pageRef,'resizable=yes,width='+pwidth+',height='+pheight+',left='+parseInt((x-pwidth)/2)+',top='+parseInt((y-pheight)/2)+',toolbar='+showToolbar+',location=no,status=yes,scrollbars=yes,menubar=no');
	popup.focus();
	return popup;
}
function ask(question, url) {
	if (confirm(question)) {
		location.href = url;
	}
	return false;
}
function formAsk(theForm, question, url) {
	if (confirm(question)) {
		theForm.action=url;
		theForm.submit();
	}
}
function numbersonly(myfield, e, dec) {
	var key;
	var keychar;
	if (window.event)
	   key = window.event.keyCode;
	else if (e)
	   key = e.which;
	else
	   return true;
	keychar = String.fromCharCode(key);
	// control keys
	if ((key==null) || (key==0) || (key==8) ||
	    (key==9) || (key==13) || (key==27) )
	   return true;
	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
	   return true;
	// decimal point jump
	else if (dec && (keychar == "."))
	   return true;
	else
	   return false;
}
function format_number(pnumber,decimals) {
  if (isNaN(pnumber)) { pnumber = 0; }
  if (pnumber=='') { pnumber = 0; }
  var IsNegative=(parseInt(pnumber)<0);
  if(IsNegative) pnumber=-pnumber;
  var snum = new String(pnumber);
  var sec = snum.split('.');
  var whole = parseInt(sec[0]);
  var result = '';
  if(sec.length > 1){
    var dec = new String(sec[1]);
    dec = parseInt(dec)/Math.pow(10,parseInt(dec.length-decimals-1));
	  Math.round(dec);
	  dec = parseInt(dec)/10;
	  if(IsNegative) {
	    var x = 0-dec;
      x = Math.round(x);
	    dec = - x;
	  } else {
      dec = Math.round(dec);
	  }
	/*
	 * If the number was rounded up from 9 to 10, and it was for 1 'decimal'
	 * then we need to add 1 to the 'whole' and set the dec to 0.
	 */
    if(decimals==1 && dec==10) {
      whole+=1;
    dec="0";
    }
    dec = String(whole) + "." + String(dec);
    var dot = dec.indexOf('.');
    if(dot == -1) {
      dec += '.';
      dot = dec.indexOf('.');
    }
    var l=parseInt(dot)+parseInt(decimals);
    while(dec.length <= l) { dec += '0'; }
    result = dec;
  } else {
    dec = new String(whole);
    dec += '.';
    dot = dec.indexOf('.');
	  l=parseInt(dot)+parseInt(decimals);
    while(dec.length <= l) { dec += '0'; }
    result = dec;
  }
  if(IsNegative)result="-"+result;
  return result;
}
function closeWindow() {
	if (window.opener != null) {
		window.opener.focus();
	}
	window.close();
}

function toggleCheckBoxes(toggle) {
	var newValue = (toggle.checked);
	var theForm = toggle.form;
	for (var i = 0; i < theForm.elements.length; ++i) {
		if (theForm.elements[i].type == "checkbox" && 
			!theForm.elements[i].disabled) {
			theForm.elements[i].checked = newValue;
		}
	}
}
function checkAll(theForm) {
	for (var i = 0; i < theForm.elements.length; ++i) {
		if (theForm.elements[i].type == "checkbox") {
			theForm.elements[i].checked = true;//!theForm.elements[i].checked;
			//if (theForm.elements[i].checked) counter++;
		}
	}	
}
function checkNone(theForm) {
	for (var i = 0; i < theForm.elements.length; ++i) {
		if (theForm.elements[i].type == "checkbox") {
			theForm.elements[i].checked = false;
		}
	}	
}
function checkInverse(theForm) {
	for (var i = 0; i < theForm.elements.length; ++i) {
		if (theForm.elements[i].type == "checkbox") {
			theForm.elements[i].checked = !theForm.elements[i].checked;
		}
	}	
}
function isSomethingChecked(theForm) {
	for (var i = 0; i < theForm.elements.length; i++) {
		if (theForm.elements[i].type == "checkbox") {
			if (theForm.elements[i].checked == true) {
				return true;
			}
		}
	}
	return false;
}
function processAction(theForm) {
	if (isSomethingChecked(theForm)) {
		var sel=theForm.selectedAction.options[theForm.selectedAction.selectedIndex].value;
		var index=sel.indexOf('javascript:');
		if (index != -1) {
			return eval(sel.substring(index+1,sel.length));
		} else {
			index=sel.indexOf(',');
			if (index == -1) {
				theForm.action=sel;
				return true;
			} else {
				theForm.action=sel.substring(0,index);
				return confirm(sel.substring(index+1,sel.length).replace('\\',''));
			}	
		}		
	} else {
		return false;
	}
}
function addBookmark(title, url) {
	if (document.all)
		window.external.AddFavorite(url, title);
	else if (window.sidebar)
		window.sidebar.addPanel(title, url, "");
	return false;
}
function setDisplay(obj, value) {
	if (obj != null) {
		if (document.getElementById) { // DOM3 = IE5, NS6
			obj.style.display = value;
		} else {
			if (document.layers) { // Netscape 4
				obj.display = value;
			} else { // IE 4
				obj.style.display = value;
			}
		}
	}
}
function openLink(url) {
	if (url != '') {
		if (url.indexOf("://") == -1) {
			url = "http://" + url;
		}
		window.open(url);
	}
	return false;
}
function getDocumentElement(objectId, ws) {
	if (document.getElementById)
		return ws ? document.getElementById(objectId).style : document.getElementById(objectId);
	if (document.all) 
		return ws ? document.all[objectId].style : document.all[objectId];
	if ((navigator.appName.indexOf('Netscape') != -1)
			&& (parseInt(navigator.appVersion) ==4)) 
		return document.layers[objectId];
}
function showPrompt(message, detail) {
	if (prompt(message, detail)) {
		copyToClipboard(detail);
	}
}
function copyToClipboard(text) {
	if (window.clipboardData) {
		window.clipboardData.setData("Text", text);
   	} else if (window.netscape) { 
   		netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
   		if (!clip) return;
   		var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
		if (!trans) return;
   		trans.addDataFlavor('text/unicode');
		var str = new Object();
		var len = new Object();
		var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
		var copytext=text;   
		str.data=copytext;
		trans.setTransferData("text/unicode",str,copytext.length*2);
		var clipid=Components.interfaces.nsIClipboard;
		if (!clip) return false;   
		clip.setData(trans,null,clipid.kGlobalClipboard);
   }
   return false;
}
function goBack() {
	//var current = location.href;
	//if (current != null && current.lastIndexOf('#') == (current.length - 1)) {
	//	history.go(-2);
	//} else {
	//	history.go(-1);
	//}
	history.go(-1);
	return false;
}