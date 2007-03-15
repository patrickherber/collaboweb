/* Copyright 2006 Sun Microsystems, Inc. All rights reserved. You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: http://developer.sun.com/berkeley_license.html
$Id: injector.js,v 1.16 2007/01/03 07:35:12 gmurray71 Exp $ */
if (!jmaki) {
    jmaki = {};
}

jmaki.Injector = function() {
 
  var _uuid = new Date().getMilliseconds();
  var _injector = this;
  var _processing = false;

  var styles = [];
  
  var tasks = [];
  
  this.inject = function(task) {
   // make sure jmaki creates a list of libraries it can not load
    if (!jmaki.blocked) jmaki.initializeBlocked();      
    if (tasks.length == 0 && !_processing) {
        inject(task);
    } else {
        tasks.push(task);
    }
  }

  /**
   * 
   * Load template text aloing with an associated script
   * 
   * Argument p properties are as follows:
   *
   * url :              Not required but used if you want to get the template from
   *                    something other than the injection serlvet. For example if
   *                    you want to load content directly from a a JSP or HTML file.
   * 
   * p.injectionPoint:  Not required. This is the id of an element into. If this is
   *                    not specfied a div will be created under the root node of
   *                    the document and the template will be injected into it.
   *                    Content is injected by setting the innerHTML property
   *                    of an element to the template text.
   */
  function inject(task) {
      _processing = true;
      jmaki.doAjax({
            method:"GET",
            url: task.url,
            asynchronous: false,
            callback: function(req){
               if (req.readyState == 4) {
                   getContent(req.responseText, task);               
               //if no parent is given append to the document root   
               var injectionPoint;
               if (typeof task.injectionPoint == 'string') {
                   injectionPoint = document.getElementById(task.injectionPoint);
                   // wait for the injection point
                   if (!document.getElementById(task.injectionPoint)) {
                       var _t = setInterval(function() {
                           if (document.getElementById(task.injectionPoint)) {
                               clearInterval(_t);
                               injectionPoint = document.getElementById(task.injectionPoint);
                               setTimeout(function(){processTask(injectionPoint,task);},0);                      
                           }
                       }, 25);
                   } else {
                       processTask(injectionPoint, task);             
                   }
                } else {
                    processTask(task.injectionPoint, task);
                }
            }
         }
       });
  }
  
  function processTask(injectionPoint, task) {
      jmaki.clearWidgets(injectionPoint);
      var _id = "injector_" + _uuid;
      var data = task.content + "<div id='" + _id + "'></div>";
      injectionPoint.innerHTML = data;
      // wait for the content to be loaded
      var _t = setInterval(function() {
          if (document.getElementById(_id)) {
              clearInterval(_t);
              try {
                  _injector.loadScripts(task,processNextTask);
              } catch (e) {
                  injectionPoint.innerHTML = "<span style='color:red'>" + e.message + "</span>";
              }
          }
      }, 25);
  }
  
  // pass in a reference to the task
  // start the next task
  function processNextTask() {
      if (tasks.length >0) {
          var _t = tasks.shift();
          inject(_t);
      };
      _processing = false;
  }
  

  /**
   * 
   * Load template text aloing with an associated script
   * 
   * Argument p properties are as follows:
   *
   * url :              Not required but used if you want to get the template from
   *                    something other than the injection serlvet. For example if
   *                    you want to load content directly from a a JSP, JSF call, PHP, or HTML file.
   */
  this.get = function (p) {
      var _data;
       jmaki.doAjax({
            method:"GET",
            url: p.url,
            asynchronous: false,
            callback: function(req){
                _data = getContent(req.responseText);
            }
           }
           );
           return _data;
  }

  /**
   * If were returning an text document remove any script in the
   * the document and add it to the global scope using a time out.
   */
  function getContent(rawContent, _task) {
   
   _task.embeddedScripts = [];
   _task.embeddedStyles = [];
   _task.scriptReferences = [];
   _task.styleReferences = [];
  
    var _t = rawContent;
    var bodyText = "";

    // recursively go through and weed out the scripts
    // TODO: Use some better REGEX processing
    // TODO: Also support single quotes
    var gscripts = document.getElementsByTagName("script");
    var gstyles = document.getElementsByTagName("link");
    while (_t.indexOf("<script") != -1) {
            var realStart = _t.indexOf("<script");
            var scriptSourceStart = _t.indexOf("src=", (realStart));
            var scriptElementEnd = _t.indexOf(">", realStart);
            var end = _t.indexOf("</script>", (realStart)) + "</script>".length;
            if (realStart != -1 && scriptSourceStart != -1) {
                var scriptSourceName;
                var scriptSourceLinkStart= scriptSourceStart + 5;
                var quoteType =  _t.substring(scriptSourceStart + 4, (scriptSourceStart +5))
                var scriptSourceLinkEnd= _t.indexOf("\"", (scriptSourceLinkStart + 1));
              	scriptSourceLinkEnd= _t.indexOf(quoteType, (scriptSourceLinkStart + 1));
                if (scriptSourceStart < scriptElementEnd) {
                    scriptSourceName = _t.substring(scriptSourceLinkStart, scriptSourceLinkEnd);
                    // prevent multiple inclusions of the same script
                    var exists = false;
                    for (var i = 0; i < gscripts.length; i++) {
                        if (typeof gscripts[i].src) {
                            if (gscripts[i].src == scriptSourceName) {
                                exists = true;
                                break;
                            }
                        }
                    }
                    if (!exists) {
                        _task.scriptReferences.push(scriptSourceName);
                    }
                }
            }
           // now remove the script body
           var scriptBodyStart =  scriptElementEnd + 1;
           var sBody = _t.substring(scriptBodyStart, end - "</script>".length);
           if (sBody.length > 0) {
              	_task.embeddedScripts.push(sBody);
           }
           //remove script
           _t = _t.substring(0, realStart) + _t.substring(end, _t.length);
           scriptSourceLinkEnd = -1;
      }
      while (_t.indexOf("<style") != -1) {
           var realStart = _t.indexOf("<style");
           var styleElementEnd = _t.indexOf(">", realStart);
           var end = _t.indexOf("</style>", (realStart)) ;
           var styleBodyStart =  styleElementEnd + 1;
           var sBody = _t.substring(styleBodyStart, end);
           if (sBody.length > 0) {
              _task.embeddedStyles.push(sBody);
           }
           //remove style
           _t = _t.substring(0, realStart) + _t.substring(end + "</style>".length, _t.length);
        }
        // get the links    
        while (_t.indexOf("<link") != -1) {
            var realStart = _t.indexOf("<link");
            var styleSourceStart = _t.indexOf("href=", (realStart));
            var styleElementEnd = _t.indexOf(">", realStart) +1;
            if (realStart != -1 && styleSourceStart != -1) {
                var styletSourceName;
                var styleSourceLinkStart= styleSourceStart + 6;
                var quoteType =  _t.substring(styleSourceStart + 5, (styleSourceStart + 6))                
                var styleSourceLinkEnd= _t.indexOf(quoteType, (styleSourceLinkStart + 1));
                if (styleSourceStart < styleElementEnd) {
                    styleSourceName = _t.substring(styleSourceLinkStart, styleSourceLinkEnd);
	              	var exists = false;
                        for (var i = 0; i < gstyles.length; i++) {
                            if (typeof gstyles[i].src != 'undefined') {
                                if (gstyles[i].src == styleSourceName) {
                                    exists = true;	
                                }
                            }
                        }
		          if (!exists) {
		          	_task.styleReferences.push(styleSourceName);
	    	      }
                }
                //remove style
                _t = _t.substring(0, realStart) + _t.substring(styleElementEnd, _t.length);
            }
        }
        
        var head = document.getElementsByTagName("head")[0];
        
        // inject the links
        for(var loop = 0; loop < _task.styleReferences.length; loop++) {
            var link = document.createElement("link");
            link.href = _task.styleReferences[loop];
            link.type = "text/css";
            link.rel = "stylesheet";
            head.appendChild(link);
        }
        
        var stylesElement;
        if (_task.embeddedStyles.length > 0) {
            stylesElement = document.createElement("style");
            stylesElement.type="text/css";
            var stylesText;
            for(var loop = 0; loop < _task.embeddedStyles.length; loop++) {
                stylesText = stylesText + _task.embeddedStyles[loop];
            }
            if (document.styleSheets[0].cssText) {
               document.styleSheets[0].cssText = document.styleSheets[0].cssText + stylesText;
            } else {
                stylesElement.appendChild(document.createTextNode(stylesText));
                head.appendChild(stylesElement);
            }
        }
        _task.content = _t;
      }
  
      this.loadScripts = function(task, initFunction) {    
          var _loadEmbeded = function() {
              // evaluate the embedded javascripts in the order they were added
              for(var loop = 0; loop < task.embeddedScripts.length; loop++) {
                  var script = task.embeddedScripts[loop];
                  // append to the script a method to call the scriptLoaderCallback
                  eval(script);
                  if (loop == (task.embeddedScripts.length -1)) {
                      if (typeof initFunction != 'undefined') initFunction();
                      return;
                  }
              }
              if (task.embeddedScripts.length == 0 && typeof initFunction != 'undefined') initFunction();
          }
          if (task.scriptReferences.length > 0){
              // load the global scripts before loading the embeded scripts
              return this.addLibraries(task.scriptReferences.reverse(),_loadEmbeded);   
          } else {
              _loadEmbeded();
          }
          return true;
    }
    
    /**
     * Load a set of libraries in order
     */
    this.addLibraries = function(_libs, _cb, _inprocess) {
        if (typeof _inprocess == 'undefined') {
            _inprocess = new jmaki.Map();
        }

        var _lib = _libs[_libs.length-1];
        if (jmaki.blocked) {
            // check the global block list
            for (var b=0; b < jmaki.blocked.length; b++) {
                if (_lib.indexOf(jmaki.blocked[b]) != -1) {
                    throw new Error("Can not load this content because it has a script " + _lib + 
                     " that will overwrite the page. Try moving this text into an iframe or use content that does " +
                     " not contain this script.");
                }
            }
        }
        var _s_uuid = "c_script_" + _libs.length + "_" + _uuid;
        var head = document.getElementsByTagName("head")[0];
        var e = document.createElement("script");
        e.id =  _s_uuid;
        e.type = 'text/javascript';
        // safari doesn't dynamically pick up scripts added to the head
        // and requires the src be set before adding
        if (/WebKit/i.test(navigator.userAgent)) {
            var _t = setInterval(function() {
                var _id = _s_uuid;
                if (/complete/.test(document.readyState)) {
                    clearInterval(_t);
                    loadHandler(_id);
                }
               }, 25);
                e.src = _lib;
                document.body.appendChild(e);
        } else {
            head.appendChild(e);
        }
        var se = document.getElementById(_s_uuid);
        _inprocess.put(_s_uuid,_lib);
        var loadHandler = function (_id) {
            _inprocess.remove(_id);
            var _cbk = _cb;
            if (_libs.length-1 > 0) {
                _libs.pop();
                _injector.addLibraries(_libs, _cb,_inprocess);
             /**  rather than check length check for inprocess **/
            } 
            
            if (_inprocess.keys().length == 0) {
                if (typeof _cb != 'undefined'){
                    var _to = 5;
                    delete _inprocess;
                    setTimeout(function(){_cb();}, _to);
                }
            }
        }
        
        if (/MSIE/i.test(navigator.userAgent)) {
            se.onreadystatechange = function () {
                if (this.readyState == 'loaded') {
                    var _id = _s_uuid;
                    loadHandler(_id);
                }
            }; 
        } 
        
        // For everything but Safari
        if (!/WebKit/i.test(navigator.userAgent)) {   
            if (se.addEventListener) {
                se.addEventListener("load", function(){var _id = _s_uuid;loadHandler(_id)}, true);
            }
            setTimeout(function(){document.getElementById(_s_uuid).src = _lib;}, 0);
        }
        se = null;
        head = null;
        return true;
    }
}

if (!jmaki.injector) {
   jmaki.injector = new jmaki.Injector();
}