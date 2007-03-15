// define the namespaces
jmaki.namespace("jmaki.widgets.yahoo.carousel");

jmaki.widgets.yahoo.carousel.Widget = function(wargs) {

	jmaki.loadScript(wargs.widgetDir + "/carousel.js");

	var uuid = wargs.uuid;
	
	var self = this;
	// default to the tag "theKt"
	var tags = "thekt";
	var fl;
	var wrap = false;
	
	if (wargs.args) {
	     if (wargs.args.wrap) {
	        tags = wargs.args.wrap;
	    }
	    if (wargs.args.tags) {
	        tags = wargs.args.tags;
	    }
	    if (wargs.args.tags) {
	        fl = new jmaki.FlickrLoader(wargs.args.apikey);
	    }
	    if (typeof wargs.args.background != "undefined") {
	        document.getElementById(wargs.uuid).style.background  = wargs.args.background;
	    }
	}
	
	if (!fl) {
	    fl = new jmaki.FlickrLoader();
	}

	function fCallback (flickrPhotos) {
	    // get info from the JSON object
	    var fi = [];
	    
	    for (var l=0; l < flickrPhotos.items.length; l++) {
	        var itemId = "flickr_" + l;
	        var description = flickrPhotos.items[l].description;            
	        var start = description.indexOf("src=") + 10;
	        var stop =  description.indexOf("_m.jpg");
	        var imageBase = description.substring(start,stop);
	        var thumbURL = imageBase + "_m.jpg";
	        var imageURL = imageBase + ".jpg";
	        description = "Author: " + flickrPhotos.items[l].author + " tags:" + flickrPhotos.items[l].tags;
	        var price = 0;
	        var name = flickrPhotos.items[l].title;
	        var mitem = {id:itemId , name: name, thumbnailURL: thumbURL, imageURL: imageURL, description: description};
	        fi.push(mitem);
	        
	    }
	    // call the callback with the flickr items
	    self.initItems(fi);
	    self.pageLoad();
	}
		

    var lastRan = -1;
    var fList = [];
    var _wrapper = this;
    

    var fmtItem = function(imgUrl, url, title) {

        var innerHTML = 
          '<a href="' + 
  		url + 
  		'"><img src="' + 
  		imgUrl +
		'" width="' +
		75 +
		'" height="' +
		75+
		'"/>' + 
  		title + 
  		'</a>';
      
        return innerHTML;
        
    }

    var loadInitialItems = function(type, args) {
        var start = args[0];
        var last = args[1]; 

        load(this, start, last);
    }


    this.initItems = function(_items) {
        fList = _items;
    }
    
    this.loadNextItems = function(type, args) {	
        var start = args[0];
        var last = args[1]; 
        var alreadyCached = args[2];
        if(!alreadyCached) {
            load(this, start, last);
        }
    }

    this.loadPrevItems = function(type, args) {
        var start = args[0];
        var last = args[1]; 
        var alreadyCached = args[2];
        
        if(!alreadyCached && last < fList.length -4) {
            load(this, start, last);
        }
    }     

    var load = function(c,start, last) {
        for(var _i=start;_i<=last;_i++) {
            if (typeof fList[_i] != 'undefined' ) {
                c.addItem(_i, fmtItem(fList[_i].thumbnailURL, fList[_i].imageURL,  fList[_i].name));
            }
        }
    }

    this.handlePrevButtonState = function(type, args) {

        var enabling = args[0];
        var leftImage = args[1];
        if(enabling) {
            leftImage.className = "prev-arrow-enabled ";	
        } else {
            leftImage.className = "prev-arrow-disabled";
        }
        
    }
    
    this.pageLoad = function() {
       var carousel = new YAHOO.extension.Carousel(uuid, 
            {
                numVisible:        4,
                size:             fList.length,
                animationSpeed:   .25,
                scrollInc:         3,
                navMargin:         40,
                prevElementID:     uuid + "_prev-arrow",
                nextElementID:     uuid + "_next-arrow",
                loadInitHandler:   function(type, args) {
                    var start = args[0];
                    var last = args[1]; 
                    var alreadyCached = args[2];
                    
                    if(!alreadyCached && last < fList.length -4) {
                        load(this, start, last);
                    }
                },
                loadNextHandler:   self.loadNextItems,
                loadPrevHandler:   self.loadPrevItems,
                prevButtonStateHandler:   self.handlePrevButtonState,
                wrap: wrap
            }
        );
    }
    fl.load(tags.toLowerCase(), fCallback);
}