// define the namespaces
jmaki.namespace("jmaki.widgets.yahoo.geocoder");

jmaki.widgets.yahoo.geocoder.Widget = function(wargs) {
	
    var topic = "/yahoo/geocoder";
			
    var uuid = wargs.uuid;
    var service = wargs.service;
    if (wargs.args && wargs.args.topic) {
		topic = wargs.args.topic;
	}
    var location;
        
    this.getCoordinates = function() {
        location = encodeURIComponent(document.getElementById(uuid + "_location").value);
        var encodedLocation = encodeURIComponent("location=" + location);        
        var url = service + "?key=yahoogeocoder&urlparams=" + encodedLocation;        
        jmaki.doAjax({url: url, callback: function(req) { var _req=req; postProcess(_req);}});
    }

    function postProcess(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
                var response = eval("(" + req.responseText + ")");
                jmaki.publish(topic, response.coordinates);  
            }
        }
    }
}
