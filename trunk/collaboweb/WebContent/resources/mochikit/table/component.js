
// define the namespaces
if (!jmaki.widgets.mochikit) {
	jmaki.widgets.mochikit = {};
}

jmaki.widgets.mochikit.table = {};

jmaki.widgets.mochikit.table.Widget = function(wargs) {
	/*
	The SortableManager:
	
	- Rips out all of the elements with the mochi-example class.
	- Finds the elements with the mochi-template class and saves them for
	  later parsing with "MochiTAL".
	- Finds the anchor tags with the mochi:dataformat attribute and gives them
	  onclick behvaiors to load new data, using their href as the data source.
	  This makes your XML or JSON look like a normal link to a search engine
	  (or javascript-disabled browser).
	- Clones the thead element from the table because it will be replaced on each
	  sort.
	- Sets up a default sort key of "domain_name" and queues a load of the json
	  document.
	
	
	On data load, the SortableManager:
	
	- Parses the table data from the document (columns list, rows list-of-lists)
	  and turns them into a list of [{column:value, ...}] objects for easy sorting
	  and column order stability.
	- Chooses the default (or previous) sort state and triggers a sort request
	
	
	On sort request:
	
	- Replaces the cloned thead element with a copy of it that has the sort
	  indicator (&uarr; or &darr;) for the most recently sorted column (matched up
	  to the first field in the th's mochi_sortcolumn attribute), and attaches
	  onclick, onmousedown, onmouseover, onmouseout behaviors to them. The second
	  field of mochi_sortcolumn attribute is used to perform a non-string sort.
	- Performs the sort on the domains list.  If the second field of
	  mochi_sortcolumn was not "str", then a custom function is used and the
	  results are stored away in a __sort__ key, which is then used to perform the
	  sort (read: shwartzian transform).
	- Calls processMochiTAL on the page, which finds the mochi-template sections 
	  and then looks for mochi_repeat and mochi_content attributes on them, using
	  the data object.
	
	*/
	processMochiTAL = function (dom, data) {
	    /***
	
	        A TAL-esque template attribute language processor,
	        including content replacement and repeat
	
	    ***/
	
	    // nodeType == 1 is an element, we're leaving
	    // text nodes alone.
	    if (dom.nodeType != 1) {
	        return;
	    }
	    var attr;
	    // duplicate this element for each item in the
	    // given list, and then process the duplicated
	    // element again (sans mochi_repeat tag)
	    attr = getAttribute(dom, "mochi_repeat");
	    if (attr) {
	        dom.removeAttribute("mochi_repeat");
	        var parent = dom.parentNode;
	        attr = attr.split(" ");
	        var name = attr[0];
	        var lst = valueForKeyPath(data, attr[1]);
	        if (!lst) {
	            return;
	        }
	        for (var i = 0; i < lst.length; i++) {
	            data[name] = lst[i];
	            var newDOM = dom.cloneNode(true);
	            processMochiTAL(newDOM, data);
	            parent.insertBefore(newDOM, dom);
	        }
	        parent.removeChild(dom);
	        return;
	    }
	    // do content replacement if there's a mochi_content attribute
	    // on the element
	    attr = getAttribute(dom, "mochi_content");
	    if (attr) {
	        dom.removeAttribute("mochi_content");
	        replaceChildNodes(dom, valueForKeyPath(data, attr));
	        return;
	    }
	    // we make a shallow copy of the current list of child nodes
	    // because it *will* change if there's a mochi_repeat in there!
	    var nodes = list(dom.childNodes);
	    for (var i = 0; i < nodes.length; i++) {
	        processMochiTAL(nodes[i], data);
	    }
	};
	
	mouseOverFunc = function () {
	    addElementClass(this, "over");
	};
	
	mouseOutFunc = function () {
	    removeElementClass(this, "over");
	};
	
	ignoreEvent = function (ev) {
	    if (ev && ev.preventDefault) {
	        ev.preventDefault();
	        ev.stopPropagation();
	    } else if (typeof(event) != 'undefined') {
	        event.cancelBubble = false;
	        event.returnValue = false;
	    }
	};
	
	SortTransforms = {
	    "str": operator.identity,
	    "istr": function (s) { return s.toLowerCase(); },
	    "isoDate": isoDate
	};
	
	getAttribute = function (dom, key) {
	    try {
	        return dom.getAttribute(key);
	    } catch (e) {
	        return null;
	    }
	};
	
	datatableFromXMLRequest = function (req) {
	    /***
	
	        This effectively converts domains.xml to the
	        same form as domains.json
	
	    ***/
	    var xml = req.responseXML;
	    var nodes = xml.getElementsByTagName("column");
	    var rval = {"columns": map(scrapeText, nodes)};
	    var rows = [];
	    nodes = xml.getElementsByTagName("row") 
	    for (var i = 0; i < nodes.length; i++) {
	        var cells = nodes[i].getElementsByTagName("cell");
	        rows.push(map(scrapeText, cells));
	    }
	    rval.rows = rows;
	    return rval;
	};
	
	loadFromDataAnchor = function (ev) {
	    ignoreEvent(ev);
	    var format = this.getAttribute("mochi:dataformat");
	    var href = this.href;
	    sortableManager.loadFromURL(format, href);
	};
	
	valueForKeyPath = function (data, keyPath) {
	    var chunks = keyPath.split(".");
	    while (chunks.length && data) {
	        data = data[chunks.shift()];
	    }
	    return data;
	};
	
	
	SortableManager = function () {
	    this.thead = null;
	    this.thead_proto = null;
	    this.tbody = null;
	    this.deferred = null;
	    this.columns = [];
	    this.rows = [];
	    this.templates = [];
	    this.sortState = {};
	    this.data = {};
	    bindMethods(this);
	    this.examples = null;
	    this.oTemplates = null;
	};
	
	SortableManager.prototype = {
	
	    "initialize": function () {
	        // just rip all mochi-examples out of the DOM
	        while (this.examples.length) {
	            swapDOM(this.examples.pop(), null);
	        }
	        // make a template list
	        for (var i = 0; i < this.oTemplates.length; i++) {
	            var template = this.oTemplates[i];
	            var proto = template.cloneNode(true);
	            removeElementClass(proto, "mochi-template");
	            this.templates.push({
	                "template": proto,
	                "node": template
	            });
	        }
	        // set up the data anchors to do loads
	        var anchors = getElementsByTagAndClassName("a", null);
	        for (var i = 0; i < anchors.length; i++) {
	            var node = anchors[i];
	            var format = getAttribute(node, "mochi:dataformat");
	            if (format) {
	                node.onclick = loadFromDataAnchor;
	            }
	        }
	        // to find sort columns
	        this.thead_proto = this.thead.cloneNode(true);
	    },
	
	    "loadFromURL": function (format, url) {
	        var d;
	        if (this.deferred) {
	            this.deferred.cancel();
	        }
	        if (format == "xml") {
	            var req = getXMLHttpRequest();
	            if (req.overrideMimeType) {
	                req.overrideMimeType("text/xml");
	            }
	            req.open("GET", url, true);
	            d = sendXMLHttpRequest(req).addCallback(datatableFromXMLRequest);
	        } else if (format == "json") {
	            d = loadJSONDoc(url);
	        } else {
	            throw new TypeError("format " + repr(format) + " not supported");
	        }
	        // keep track of the current deferred, so that we can cancel it
	        this.deferred = d;
	        var self = this;
	        // on success or error, remove the current deferred because it has
	        // completed, and pass through the result or error
	        d.addBoth(function (res) {
	            self.deferred = null; 
	            log('loadFromURL success');
	            return res;
	        });
	        // on success, tag the result with the format used so we can display
	        // it
	        d.addCallback(function (res) {
	            res.format = format;
	            return res;
	        });
	        // call this.initWithData(data) once it's ready
	        d.addCallback(this.initWithData);
	        // if anything goes wrong, except for a simple cancellation,
	        // then log the error and show the logger
	        d.addErrback(function (err) {
	            if (err instanceof CancelledError) {
	                return;
	            }
	            logError(err);
	            logger.debuggingBookmarklet();
	        });
	        return d;
	    },
	
	    "initWithData": function (data) {
	        /***
	
	            Initialize the SortableManager with a table object
	        
	        ***/
	       
	        // reformat to [{column:value, ...}, ...] style as the domains key
	        var items = [];
	        var rows = data.rows;
	        var colo = data.columns;
	        var cols = [];
	        for (var ci in colo) {
	            cols.push(ci);
	        }
	        for (var i = 0; i < rows.length; i++) {
	            var row = rows[i];
	            var item = {};
	            for (var j = 0; j < cols.length; j++) {
	                item[cols[j]] = row[j];
	            }
	            items.push(item);
	        }
	        data.items = items;
	        this.data = data;
	        //this.instances[wargs.uuid] = data;
	        // perform a sort and display based upon the previous sort state,
	        // defaulting to an ascending sort if this is the first sort
	        var order = this.sortState[this.sortkey];
	        if (typeof(order) == 'undefined') {
	            order = true;
	        }
	        this.drawSortedRows(this.sortkey, order, false);
	
	    },
	
	    "onSortClick": function (name) {
	        /***
	
	            Return a sort function for click events
	
	        ***/
	        // save ourselves from doing a bind
	        var self = this;
	        // on click, flip the last sort order of that column and sort
	        return function () {
	            var order = self.sortState[name];
	            if (typeof(order) == 'undefined') {
	                // if it's never been sorted by this column, sort ascending
	                order = true;
	            } else if (self.sortkey == name) {
	                // if this column was sorted most recently, flip the sort order
	                order = !((typeof(order) == 'undefined') ? false : order);
	            }
	            self.drawSortedRows(name, order, true);
	        };
	    },
	
	    "drawSortedRows": function (key, forward, clicked) {
	        /***
	
	            Draw the new sorted table body, and modify the column headers
	            if appropriate
	
	        ***/
	
	        // save it so we can flip next time
	        this.sortState[key] = forward;
	        this.sortkey = key;
	        var sortstyle;
	
	        // setup the sort columns   
	        var thead = this.thead_proto.cloneNode(true);
	        var cols = thead.getElementsByTagName("th");
	
	        for (var i = 0; i < cols.length; i++) {
	            var col = cols[i];
	            var sortinfo = getAttribute(col, "mochi_sortcolumn").split(" ");
	            var sortkey = sortinfo[0];
	            col.onclick = this.onSortClick(sortkey);
	            col.onmousedown = ignoreEvent;
	            col.onmouseover = mouseOverFunc;
	            col.onmouseout = mouseOutFunc;
	            // if this is the sorted column
	            if (sortkey == key) {
	                sortstyle = sortinfo[1];
	                // \u2193 is down arrow, \u2191 is up arrow
	                // forward sorts mean the rows get bigger going down
	                var arrow = (forward ? "\u2193" : "\u2191");
	                // add the character to the column header
	                col.appendChild(SPAN(null, arrow));
	                if (clicked) {
	                    col.onmouseover();
	                }
	            }
	        }
	        this.thead = swapDOM(this.thead, thead);
	
	        // apply a sort transform to a temporary column named __sort__,
	        // and do the sort based on that column
	        if (!sortstyle) {
	            sortstyle = "str";
	        }
	        var sortfunc = SortTransforms[sortstyle];
	        if (!sortfunc) {
	            throw new TypeError("unsupported sort style " + repr(sortstyle));
	        }
	        //var items = this.data[wargs.uuid].data;
	        var items = this.data.items;
	        for (var i = 0; i < items.length; i++) {
	            var item = items[i];
	            item.__sort__ = sortfunc(item[key]);
	        }
	
	        // perform the sort based on the state given (forward or reverse)
	        var cmp = (forward ? keyComparator : reverseKeyComparator);
	        items.sort(cmp("__sort__"));
	
	        // process every template with the given data
	        // and put the processed templates in the DOM
	        for (var i = 0; i < this.templates.length; i++) {
	            var template = this.templates[i];
	            var dom = template.template.cloneNode(true);
	            processMochiTAL(dom, this.data);
	            template.node = swapDOM(template.node, dom);
	        }
	 
	
	    },
	
	    "load" : function (mrows,mcolumns) {
	    var table = document.createElement("table");
	    table.className = "datagrid";
	    // create the table
	    var columnIds = [];
	    
	    var examples = [];
	    var templates = [];
	
	    var mthead;
	    if (table.createTHead) {
	        mthead = table.createTHead();
	    } else {
	         mthead = document.createElement("thead");
	         table.appendChild(mthead);
	    }
	    this.thead = mthead;
	    this.thead_proto = this.thead.cloneNode(true);
	
	    var tr;
	    if (mthead.createRow) {
	        tr = mmthead.insertRow();	
	    } else {
	        tr = document.createElement("tr");
	        mthead.appendChild(tr);
	    }
	
	    for (var l in mcolumns) {
	        columnIds.push(l);
	        var cell;
	        if (tr.createCell) {
	             cell = tr.createCell();
	        } else {
	            cell = document.createElement("th");
	            tr.appendChild(cell);
	        }
	        cell.setAttribute("mochi_sortcolumn", l + " str");
	        cell.appendChild(document.createTextNode( mcolumns[l]));
	    }
	    // set the default sort key
	    this.sortkey = columnIds[0];
	
	    var tbody;
	    if (table.createTBody) {
	        tbody = table.createTBody();
	    } else {
	        tbody  = document.createElement("tbody");
	        table.appendChild(tbody);
	    }
	
	    tbody.className = "mochi-template";
	    templates.push(tbody);
	    for (var r = 0; r < mrows.length; r++) {
	        var lrow;
	        if (table.insertRow != null) {
	            lrow = table.insertRow(table.rows.length);
	        } else {
	            lrow = document.createElement("tr");
	        }
	        tbody.appendChild(lrow);
	        if (r == 0) { 
	            // place the right mappings into the sort key using the wargs.uuid
	            lrow.setAttribute("mochi_repeat", "item items");
	        } else {
	            lrow.className = "mochi-example";
	            examples.push(lrow);
	        }
	
	        for (var c = 0; c < mrows[r].length; c++) {
	            var cell;
	            if (lrow.insertCell) {
	                cell = lrow.insertCell(lrow.cells.length);
	            } else {
	                 cell = document.createElement("td");
	                 row.appendChild(cell);
	            }
	            if (r == 0) {
	                cell.setAttribute("mochi_content", "item." + columnIds[c]);
	            }
	            cell.appendChild(document.createTextNode(mrows[r][c]));
	        }
	    }
	    container.appendChild(table);
	    this.examples = examples;
	    this.oTemplates = templates;
	     },
	
	  "addRow" : function(b){
	    // add an id for sorting if not defined
	    if (typeof b.Id == "undefined") {      
	        b.Id = count++;
	    }
	    this.data.items.push(b);
	    // redraw every thing
	        var order = this.sortState[this.sortkey];
	        if (typeof(order) == 'undefined') {
	            order = true;
	        }
	        this.drawSortedRows(this.sortkey, order, false);
	  }
	
	};
	
	
	var container = document.getElementById(wargs.uuid);
	var  sortableManager = new SortableManager();
	
	if (typeof wargs.value != "undefined") {
	     var rows = [];
	     // convert the rows object into an array
	     if (typeof wargs.value.rows == "object") {
	        
	        for (var i in wargs.value.rows) {
	            var row = [];
	            for (var ir in wargs.value.rows[i] ) {  
	                row[ir] = wargs.value.rows[i][ir];
	            }
	            rows.push(row);
	        }
	     } else {
	        rows = wargs.value.rows;
	     }
	     sortableManager.load(rows,wargs.value.columns);
	     sortableManager.initialize();
	     sortableManager.initWithData({rows: rows, columns: wargs.value.columns});
	} else if (typeof wargs.service != "undefined") {
	    if (this.deferred) {
	      this.deferred.cancel();
	    }
	
	    var d = loadJSONDoc(wargs.service);
	
	    d.addCallback(function (res) {
	     var rows = res.rows;
	     var columns = res.columns;
	     sortableManager.load(rows,columns);
	     sortableManager.initialize();
	     sortableManager.initWithData(res);
	    });
	} else {
	     var    columns = { "domain_name" : "Domain Name", "create_date":"Create Date", "expiry_date": "Expiry Date", "organization_name":"Organization Name"};
	     var    rows =  [
	            ["json.org", "2000-05-08", "2006-05-08", "Douglas Crockford"],
	            ["mochibot.com", "2005-02-10", "2007-02-10", "Jameson Hsu"],
	            ["pythonmac.org", "2003-09-24", "2006-09-24", "Bob Ippolito"],
	            ["undefined.org", "2000-01-10", "2006-01-10", "Robert J Ippolito"],
	            ["python.org", "1995-03-27", "2007-03-28", "Python Software Foundation"]
	        ];
	     sortableManager.load(rows,columns);
	     sortableManager.initialize();
	     sortableManager.initWithData({rows: rows, columns: columns});
	}
}