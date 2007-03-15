if (typeof jmaki == 'undefined') {
    jmaki = {};
}

if (typeof jmaki.DContainer == 'undefined') {
    jmaki.DContainer = function(uuid, useIFrame, startWidth, startHeight, overflow, topic){
    var _this = this;
    var _container = document.getElementById(uuid);
    _container.style.border = "0px";
    //_container.style.border = "1px green solid";
    if (typeof overflow != 'undefined') {
         _container.style.overflow = overflow;
    }
    var iframe;

    if (useIFrame) {
        // use this technique as creating the iframe programmatically does not allow us to turn the border off
        var iframeTemplate = "<IFRAME ID=" + uuid + "_iframe" + " FRAMEBORDER=0 SCROLLING=NO></IFRAME>"
        _container.innerHTML = iframeTemplate;
        iframe = document.getElementById(uuid + "_iframe");
    }
    
    this.url = null;
 
    var autoSizeH = false;
    var autoSizeW = false;
  
    // default sizes are all based on the width of the container   
    var VIEWPORT_WIDTH;

    if (typeof startWidth != 'undefined') {
        VIEWPORT_WIDTH = Number(startWidth);
        _container.style.width = VIEWPORT_WIDTH + "px";
    } else {
        VIEWPORT_WIDTH = _container.clientWidth;
        autoSizeW = true;
    }

    var VIEWPORT_HEIGHT;

    if (typeof startHeight != 'undefined') {
        VIEWPORT_HEIGHT = Number(startHeight);
    } else {
        VIEWPORT_HEIGHT = _container.clientHeight;
        autoSizeH = true;
    }
     if (VIEWPORT_HEIGHT < 0) VIEWPORT_HEIGHT = 320;
    _container.style.height = VIEWPORT_HEIGHT + "px";
    if (useIFrame) {
        iframe.style.height = VIEWPORT_HEIGHT + "px";
    }

    function getPosition(_e) {
        var pX = 0;
        var pY = 0;

        while (_e.offsetParent) {
                pY += _e.offsetTop;
                pX += _e.offsetLeft;
                _e = _e.offsetParent;
        }
        return {x: pX, y: pY};
    }

    this.resize = function() {
        var pos = getPosition(_container);
        if (autoSizeH || autoSizeW){
              var pos = getPosition(_container);
              if (_container.parentNode.nodeName == "BODY") {
                if (window.innerHeight){
                    VIEWPORT_HEIGHT = window.innerHeight - pos.y -40;
                    VIEWPORT_WIDTH = window.innerWidth - 20;
                } else {
                    var _tNode = _container.parentNode;
                    while(_tNode != null &&
                        (_tNode.clientHeight == 0 ||
                         typeof _tNode.clientWidth == 'undefined')) {
                        _tNode = _tNode.parentNode;
                    }
                    if (_tNode == null) {

                        VIEWPORT_WIDTH = 400;
                    } else {
                        VIEWPORT_WIDTH = _tNode.clientWidth -20;
                        VIEWPORT_HEIGHT = _tNode.clientHeight - pos.y - 15;
                    }
                }
              } else {
                    var _tNode = _container.parentNode;
                    while(_tNode != null &&
                        (_tNode.clientHeight == 0 ||
                         typeof _tNode.clientWidth == 'undefined')) {
                        _tNode = _tNode.parentNode;
                    }
                    if (_tNode == null) {
                        VIEWPORT_WIDTH = 400;
                    } else {
                        VIEWPORT_WIDTH = _tNode.clientWidth -2;
                        VIEWPORT_HEIGHT = _tNode.clientHeight -2;
                    }
                }         
              if (autoSizeH) {
                  if (VIEWPORT_HEIGHT < 0) VIEWPORT_HEIGHT = 320;
                  _container.style.height = VIEWPORT_HEIGHT + "px";
              }
            } else {
                _container.style.width = VIEWPORT_WIDTH + "px";
                _container.style.height = VIEWPORT_HEIGHT + "px";          
            }
            if (VIEWPORT_HEIGHT < 0) {
                VIEWPORT_HEIGHT = 320;
            }
            if (VIEWPORT_WIDTH < 0) {
                VIEWPORT_WIDTH = 500;
            }
        if (useIFrame) {
            iframe.style.height = VIEWPORT_HEIGHT + "px";
            iframe.style.width = VIEWPORT_WIDTH + "px";
        }
    }
    
    this.loadURL = function(_url){
        if (typeof _url != 'undefined') {
            this.url = _url;
            if (useIFrame) {
                iframe.src = _url;
            } else {
                var _i = new jmaki.Injector();
                _i.inject({url:_url, injectionPoint: _container});
            }
        }
    }
    
    this.resize();
    if (this.url != null) {
        this.loadURL(this.url);
    }
     var oldOnResizeEnd = null;
    
        if (window.onresize != null) {
            oldOnResizeEnd = window.onresize;
        }
    window.onresize = function() {
        if (oldOnResizeEnd != null) oldOnResizeEnd();
        _this.resize();
    };   
    // listen for load requests
    jmaki.subscribe(topic, this.loadURL);
  }
}


jmaki.FlickrProxySearch = function(service, topic){

    var target;
    
    if (typeof  topic == 'undfined') {
        topic = "flickrSearch";
    }
	
    this.searchPhotos = function(tags) {
        // build and encode the last URL parameter tags=_target.value
        target = encodeURIComponent("tags=" + tags);
        var url = service + "?key=flickrtagsearch&urlparams=" + target;
        jmaki.doAjax({url: url, callback: function(req) { var _req=req; postProcess(_req);}});
    }
	
    function postProcess(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
                if (req.responseText != '') {
                    var response = eval("(" + req.responseText + ")");
		    		jmaki.publish(topic, response.photos);
                } else {
	                jmaki.publish(topic, []);
	            }
            }
        }
    }
}
/**
*  Insert a script tag in the head of the document which will inter load the flicker photos
*  and call jsonFlickrFeed(obj) with the corresponding object.
*
*/
jmaki.FlickrLoader = function(apiKey) {
    
    this.load = function(tags, callback) {
        if (typeof _globalScope.flickrListeners == 'undefined') {
            _globalScope.flickrListeners = {};
        }
        var listeners = _globalScope.flickrListeners[tags];
        if (typeof listeners == 'undefined') {
            listeners = [];
        }
        listeners.push(callback);
        _globalScope.flickrListeners[tags] = listeners;      
        
        _globalScope.jsonFlickrFeed = function(args) {
            var title = args.title;
            var tagsEnd = title.indexOf(" - Everyone");
            var tagNames = title.substring(0,tagsEnd);
            tagNames = tagNames.replace(/ and /, ',');
            var tListeners = _globalScope.flickrListeners[tagNames];
            if (tListeners != null) {
                for (var i = 0; i < tListeners.length; i++) {
                    tListeners[i](args,tagNames);
                }
                // release the listeners for this tag
                delete _globalScope.flickrListeners[tagNames];
            }
        }
        var s = document.createElement("script");
        var url ="http://www.flickr.com/services/feeds/photos_public.gne?tags=" + tags + "&format=json";
        if (typeof apiKey != 'undefined') {
            url += "appid=" + apiKey;
        }
        s.src = url;
        s.type = "text/javascript";
        s.charset = "utf-8";
        document.body.appendChild(s);      
    }
}

jmaki.DiggLoader = function(targetDiv, topic, count) {
    if (targetDiv == null) return;
    var processDig = function(d) {
        var bodies = jmaki.getElementsByStyle("rss-body",d);
        var items = jmaki.getElementsByStyle("rss-item",d);
        // now go through each item and add it to the body.
        for (var l = 0; l < bodies.length && l < count; l++) {
            var wrapperDiv = document.createElement("div");
            wrapperDiv.className = "diggItem";
            var diggTitle = document.createElement("div");
            diggTitle.className = "diggTitle";
            // clone the link
            var diggLink = document.createElement("a");
            diggLink.className = "diggLink";
            diggLink.href = items[l].firstChild.href;
            diggLink.innerHTML = items[l].firstChild.innerHTML;
            diggLink.title = items[l].firstChild.title;
            diggTitle.appendChild(diggLink);
            wrapperDiv.appendChild(diggTitle);
            var diggBody = document.createElement("div");
            diggBody.className = "diggBody";
            diggBody.innerHTML =  bodies[l].innerHTML;
            wrapperDiv.appendChild(diggBody);     
            targetDiv.appendChild(wrapperDiv);
        }
    }
    
    if (typeof topic == 'undefined') topic = "all";
    if (typeof count == 'undefined') count = "10";
    var div = targetDiv;
    // create an invisible iframe
    var iframe = document.createElement("iframe");
    iframe.style.width = "0px";
    iframe.style.height = "0px";
    iframe.style.border = "0px";
    div.appendChild(iframe);
    // get the iframe document
    var d;
    if (iframe.contentWindow) {
        d = iframe.contentWindow.document;
    } else if (iframe.document) {
        d = iframe.document;
    } else if (iframe.contentDocument) {
        d= iframe.contentDocument;	
    }
    var tcount = 10;
    d.write("<script id='digg-getter' type='text/javascript' src='http://digg.com/diggjs/front/" + topic + "/" + tcount + "'></" + "script>");
    // this is for ie and firefox
    if (/(firefox)/i.test(navigator.userAgent.toLowerCase())) {
        var dScript = d.getElementById("digg-getter");
        // this is for ie and firefox
        dScript.onload= function() {
            processDig(d);
            // cleanup
            iframe.src = "";
            delete iframe;
        }
        // for ie
    } else if (typeof d.attachEvent != 'undefined') {
        var dScript = d.getElementById("digg-getter");
        dScript.attachEvent("onreadystatechange", function(e){
            if(dScript.readyState == "complete"){
                processDig(d);
                // cleanup
                iframe.src = "nowhere";
                delete iframe;
            }
        });
        // genric anything else.
    } else {
        var t = setInterval(function() {
            var root;
            if ((root = jmaki.getElementsByStyle("rss-box",d)) != null) {
                clearInterval(t);
                processDig(d);
                // cleanup
                iframe.src = "";            
                delete iframe;
            }
        }, 250);
        
    }
}




jmaki.Delicious = function() {;
    var uuid;

    this.load = function(id, user, count){
        uuid = id;
        var s = document.createElement("script");
        var url = "http://del.icio.us/feeds/json/"+user+"?count=" + count + "&" + 
                        "callback=jmaki.attributes.get('" + uuid + "').loaded";
        s.src = url;
        s.charset = "utf-8";
        document.body.appendChild(s);
    }
     
     this.loaded  = function(posts) {
         var ul = document.createElement('ul');
         for (var i=0, post; post = posts[i]; i++) {
             var li = document.createElement('li');
             var a = document.createElement('a');
             a.style.marginLeft = '20px';
             var img = document.createElement('img');
             img.style.position = 'absolute';
             img.style.display = 'none';
             img.height = img.width = 16;
             img.src = post.u.split('/').splice(0,3).join('/')+'/favicon.ico'
                 img.onload = showImage(img);
             a.setAttribute('href', post.u);
             a.appendChild(document.createTextNode(post.d));
             li.appendChild(img);
             li.appendChild(a);
             ul.appendChild(li);
         }
         document.getElementById(uuid).appendChild(ul);
     }

    function showImage(img){ return (function(){ img.style.display='inline' }) }
}