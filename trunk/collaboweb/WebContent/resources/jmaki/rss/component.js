 // define the namespaces
if (!jmaki.widgets.jmaki) {
    jmaki.widgets.jmaki = {};
}
jmaki.widgets.jmaki.rss = {};

jmaki.widgets.jmaki.rss.Widget = function(wargs) {
    var _this = this;
    var uuid = wargs.uuid;
    var rssitems;
    var channel;
    var id = 0;
    var link = [];
    var title = [];
    var description = [];
    var opacitysetting = 0.1;
    var fadetimer1;	
	var interval = 175;
	var feed = "https://ajax.dev.java.net/servlets/ProjectRSS?type=news";
	var service = jmaki.webRoot + "/rssprovider";
	var topic = null;
	
	if (typeof wargs.args != 'undefined') {
	  if (typeof wargs.args.interval != 'undefined') {
	      interval = Number(wargs.args.interval);
	  }
	  if (typeof wargs.args.feed != 'undefined') {
	    feed = wargs.args.feed;
	  }
	  if (typeof wargs.args.topic != 'undefined') {
	    topic = wargs.args.topic;
	  }
	}
	
	if (topic == null) {
	    document.getElementById(wargs.uuid + "_item").innerHTML = "Please wait while RSS ticker is being generated."
	}	

    
    function getXHR() {
	    if (window.XMLHttpRequest) {
        	return new XMLHttpRequest();
    	} else if (window.ActiveXObject) {
        	return new ActiveXObject("Microsoft.XMLHTTP");
    	}
    }
    
    var ajax = getXHR();
    getRSSContent();
    
    function getRSSContent(){
        ajax.onreadystatechange = function(){loadRss()};
        var url = service + "?url=" + encodeURIComponent( feed) +  "&format=json&itemIndex=4";
        ajax.open('GET', url, true);
        ajax.send(null);
    }
    
    function loadRss() {
        
        if (ajax.readyState == 4){ 
 
            if (ajax.status == 200){
                channel = eval("(" + ajax.responseText + ")");
                rssItems = channel.channel.item;
                if (topic == null) {
                var tickerItemDiv = document.getElementById(uuid + "_item");
                var tickerDescriptionDiv = document.getElementById(uuid + "_description");
                if (typeof rssItems == 'undefined' || rssItems.length == 0) { 
                    tickerItemDiv.innerHTML = "Sorry, cannot load RSS feed<br />" + response;
                    return;
                }
                
                tickerItemDiv.innerHTML = '<a href="'+rssItems[id].link+'" target="_newFrame">'+rssItems[id].title +'</a>';
                tickerDescriptionDiv.innerHTML  = rssItems[id].date;
                fade("reset"); 
                fadetimer1 = setInterval(function(){fade('up', 'fadetimer1' + uuid)}, interval);   
                id=(id < rssItems.length-1)? id+1 : 0;
                setTimeout(function(){loadRss()}, 7000);
                } else {
                    jmaki.publish(topic, rssItems);
                }
            }
        }
    }
    
    function setOpacity(opacity, id) {
        var target = document.getElementById(id);
        if (typeof target.style.filter != 'undefined') {
            target.style.filter = "alpha(opacity:" + (opacity*100) + ")";
        } else {
            target.style.opacity = opacity;
        }
    }
 
    function fade(type, timerid){
        
        if (type == "reset") {
            opacitysetting=.1;
        }
        
        setOpacity(opacitysetting, uuid + "_item");
        setOpacity(opacitysetting, uuid + "_description");
        
        if (type == "up") {
            opacitysetting += 0.1;
        }
        
        if (opacitysetting >= 1 ) {
            clearInterval(fadetimer1);
        }
    }
}