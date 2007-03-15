// define the namespaces
if (!jmaki.widgets.flickr) {
	jmaki.widgets.flickr = {};
}
jmaki.widgets.flickr.search = {};

jmaki.widgets.flickr.search.Widget = function(wargs) {

	var self = this;
	var topic = "";
	var columns = 5;
	var count = 20;
	
	if (typeof wargs.args != 'undefined') {
	    if  (typeof wargs.args.topic == 'undefined') {
	        topic = "flickrSearch";
	    } else {
	        topic = wargs.args.topic;
	    }
	    if  (typeof wargs.args.columns != 'undefined') {
	        columns = Number(wargs.args.columns);
	    }
	    if  (typeof wargs.args.count != 'undefined') {
	        count = Number(wargs.args.count);
	    }
	}
	
	function flickrSearchListener(photos) {
	     var targetDiv = document.getElementById(wargs.uuid + "_flickrResults");
	    // clear out the children
	    for (var l = targetDiv.childNodes.length -1; l >= 0; l--) {
	        targetDiv.removeChild(targetDiv.childNodes[l]);    
	    }
	
		var row;
	    var length = count;
	
	    if (photos.items.length < count) {
	        length = photos.items.length;
	    }
	
	    for (var i =0; i < length; i++) {
	         if (i % columns == 0) {
	            if (typeof targetDiv.insertRow != 'undefined') {
	                row = targetDiv.insertRow(targetDiv.rows.length);
	            } else {
	                row = document.createElement("tr");
	            	targetDiv.appendChild(row);
	            }
	        }
	        var cell;
	        if (typeof row.insertCell != 'undefined') {
	            cell = row.insertCell(0);
	        } else {
	            cell = document.createElement('td');
	            row.appendChild(cell);
	        }
	        var target = document.createElement("a");
	        target.href =  photos.items[i].url;
	        target.title = photos.items[i].title;
	        target.innerHTML = "<img src='" + photos.items[i].smallURL + "' border='0'>";
	        cell.appendChild(target);
	    }
	}
        var fs = new jmaki.FlickrProxySearch(wargs.service,topic);
        // subscribe the instance listener
        fs.listener = flickrSearchListener;
        this.searchPhotos = function(args) {
            fs.searchPhotos(args);
        }
	// subscribe the instance listener
	jmaki.subscribe(topic, flickrSearchListener);
}