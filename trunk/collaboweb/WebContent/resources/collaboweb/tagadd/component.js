// define the namespaces
if (!jmaki.widgets.collaboweb) {
	jmaki.widgets.collaboweb = {};
}
jmaki.widgets.collaboweb.tagadd = {};

jmaki.widgets.collaboweb.tagadd.Widget = function(wargs) {
	sendMessage(wargs.uuid, wargs.service);
}

function sendMessage(tag, uuid, service) {
    var tag = document.getElementById(uuid).value;
    var url = service + "&tag=" + tag;
    jmaki.doAjax({url: url, callback: function(req) {var _req=req; processMessagePost(_req);}});
    return false;
}

function processMessagePost(req) {
    if (req.readyState == 4) {
		alert(4);
    }
}
