// define the namespaces
jmaki.namespace("jmaki.widgets.yahoo.calendar");

jmaki.widgets.yahoo.calendar.Widget = function(wargs) {
    var self = this;
	this.wrapper = new YAHOO.widget.Calendar("jmaki.attributes.get('" + wargs.uuid + "')", wargs.uuid);

	if (typeof wargs.value != 'undefined') {
    	var date = new Date(wargs.value);
    	this.wrapper.select(date);
	}

	this.getValue = function() {
    	if (this.wrapper.getSelectedDates().length >0) {
        	return this.wrapper.getSelectedDates()[0];
    	} else {
     		return null;
    	}
	}

	// add a saveState function
	if ( wargs.service) {
	    this.saveState = function() {
	        if (self.getValue() == null) return;
	        // we need to be able to adjust this
	        var url = wargs.service;
	        var _val =  self.getValue().toString();
	        url = url + "?cmd=update";
	        jmaki.doAjax({url: url, method: "post", content: {value : _val}, callback: function(req) {
	                if (req.readyState == 4) {
	                    if (req.status == 200) {
	                        // take some action if needed
	                    }
	            }
	        }});
	    }
	}
	this.wrapper.render();
}