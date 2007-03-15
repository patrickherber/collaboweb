// define the namespaces
jmaki.namespace("jmaki.widgets.yahoo.tree");

jmaki.widgets.yahoo.tree.Widget = function(wargs) {
    var self = this;
    var topic = "/yahoo/tree";

    this.tree = new YAHOO.widget.TreeView(wargs.uuid);

    // use the default tree found in the widget.json if none is provided
    if (!wargs.value ) {
        var callback;
        // default to the service in the widget.json if a value has not been st
        // and if there is no service
        if (typeof wargs.service == 'undefined') {
            wargs.service = wargs.widgetDir + "/widget.json";
            callback = function(req) {
                if (req.readyState == 4) {
                    var obj = eval("(" + req.responseText + ")");
                    var jTree = obj.value.data;
                    var root = jTree.root;
                    buildTree(root);
                }
            }
           
        } else {
           callback = function(req) {
                if (req.readyState == 4) {
                    var jTree = eval("(" + req.responseText + ")");
                    var root = jTree.root;
                    buildTree(root);
                }
            }        
        }
          var ajax = jmaki.doAjax({url : wargs.service, callback : callback});
    } else if (typeof wargs.value == 'object') {
      if (wargs.value.collapseAnim) {
        this.tree.setCollapseAnim(wargs.value.collapseAnim);
      }
      if (wargs.value.expandAnim) {
        this.tree.setExpandAnim(wargs.value.expandAnim);
      }
      buildTree(wargs.value.root);
    }

    var nodes = [];
    var nodeIndex;

    // now build the tree programtically
    function buildTree(root, parent) {
        var rChildren = false;
        var rExpanded = false;
        if (typeof root.children != 'undefined') rChildren = true;
        if (typeof root.expanded != 'undefined' && (root.expanded == true || root.expanded == "true")) rExpanded = true;
   
        if (typeof parent == 'undefined') {
            parent = self.tree.getRoot();
        }

        // Backwards compatibility -- copy "title" to "label" if needed
        // but we will use "label" henceforth
        if (root.title && !root.label) {
          root.label = root.title;
        }
        // End of backwards compatibility hack

        var rNode = new YAHOO.widget.TextNode(root, parent, rExpanded);

        // wire in onclick handler
        if (typeof root.onclick != 'undefined') {
                var _m = root.onclick;
                _m.title = root.label;
                rNode.toggle =  function(e){_m.event = e;jmaki.publish(topic,_m);};
            } else if (!rChildren) {
                var _m = {};
                _m.title = root.label;
                rNode.toggle = function(e){_m.event = e;jmaki.publish(topic,_m);};
            }
        for (t in root.children) {
            var n = root.children[t];
            var hasChildren = false;
            if (typeof n.children != 'undefined') hasChildren = true;
            var isExpanded = false;
            if (typeof n.expanded  != 'undefined' && n.expanded == "true") isExpanded = true;
            // Backwards compatibility -- copy "title" to "label" (as above)
            if (n.title && !n.label) {
              n.label = n.title;
            }
            // End of backwards compatibility hack
            var lNode = new YAHOO.widget.TextNode(n, rNode, isExpanded);

            if (typeof n.onclick != 'undefined') {
                var _m = n.onclick;
                _m.title = n.label;
                lNode.toggle = function(e){_m.event = e;jmaki.publish(topic,_m);};
            } else if (!hasChildren) {
                var _m = {};
                _m.title = n.label;
                lNode.toggle =  function(e){_m.event = e;jmaki.publish(topic,_m);};
            }

            //  recursively call this function to add children
            if (typeof n.children != 'undefined') {
                for (ts in n.children) {
                    buildTree(n.children[ts], lNode);
                }
            }
            
        }
        self.tree.draw();
   }
}
