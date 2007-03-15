// define the namespaces
jmaki.namespace("jmaki.widgets.spry.accordion");

jmaki.widgets.spry.accordion.Widget = function(wargs) {
    
    var gradient = "AquaAccordionTab";
    var rows = [];
    var selectedIndex = 0;
    var autoHeight = true;
    
    var container = document.getElementById(wargs.uuid);     
    
    // load the gradient background if provided
    if (wargs.args && wargs.args.gradient) {
        if (wargs.args.gradient == 'aqua') {
            gradient = "AquaAccordionTab";
        } else  if (wargs.args.gradient == 'blue') {
            gradient = "BlueAccordionTab";
        } else  if (wargs.args.gradient == 'green') {
            gradient = "GreenAccordionTab";
        } else  if (wargs.args.gradient == 'gray') {
            gradient = "GrayAccordionTab";
        }
        if (wargs.autoHeight) {
            autoHeight = wargs.autoHeight;
        }
    }
    
    // pull in the arguments
    if (wargs.value) {
        rows = wargs.value.rows;
        init();
    } else if (wargs.service) {
            jmaki.doAjax({url: wargs.service, callback: function(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
              var data = eval('(' + req.responseText + ')');
              rows = data.rows;
              init();
          }
        }
      }});
    } else {
        rows = [
        {label: 'Books', content: 'Book content'},
        {label: 'Magazines', content: 'Magazines here'},
        {label: 'Newspaper', content: 'Newspaper content'}
        ];
        init();
    }

  if (wargs.args && wargs.args.selectedIndex) {
        selectedIndex = wargs.args.selectedIndex;
  }
    
    function addRow(label, content,_height, url) {
        var _row = document.createElement("div");
        _row.className = "AccordionPanel";
        var _rowTitle = document.createElement("div");
        _rowTitle.className = gradient;
        _rowTitle.appendChild(document.createTextNode(label));
        _row.appendChild(_rowTitle);
        var _rowContent = document.createElement("div");
        _rowContent.className = "AccordionPanelContent";
        if (autoHeight && typeof _height != "undefined") _rowContent.style.height = _height + "px";
        _row.appendChild(_rowContent);
        if (typeof content != 'undefined') {
            _rowContent.innerHTML = content;
        } else if (typeof url != 'undefined') {
            var _in = new jmaki.Injector();
            _in.inject({url:url, injectionPoint: _rowContent});
        }
        container.appendChild(_row);
    }
    
    function init() {
        // if the user used a custom template and no value 
        //use it otherwise use the built in one
        if (rows && !container.firstChild) {      
            // calculate the height
            var ch = container.clientHeight;
            if (ch != 0) contentHeight = ch - ((rows.length + 1) * 30);            
            for(var i=0; i<rows.length; ++i) {
                var _row = rows[i];
                addRow(_row.label,_row.content, contentHeight, _row.url);
            }
        } else if (typeof gradient != 'undefined'){
            // set the gradient on the template if a gradient
            jmaki.replaceStyleClass(wargs.uuid, 'AccordionPanelTab', gradient);	
        }
        this.wrapper = new Spry.Widget.Accordion(wargs.uuid,{ defaultPanel: selectedIndex });
    }
}