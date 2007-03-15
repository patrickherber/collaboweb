// define the namespaces
if (!jmaki.widgets.spry) {
	jmaki.widgets.spry = {};
}
jmaki.widgets.spry.plainAccordion = {};

jmaki.widgets.spry.plainAccordion.Widget = function(wargs) {
    
     var gradient = "BlueAccordionTab";
    
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
    }
    var selectedIndex = 0;
    
    var rows = [];
    // pull in the arguments
    if (wargs.value) {
        
        // convert the rows object into an array
        if (typeof wargs.value.rows == "object") {
            for (var i in wargs.value.rows) {
                var row = [];
                for (var ir in wargs.value.rows[i] ) {  
                    row[ir] = wargs.value.rows[i][ir];
                }
                rows.push(row);
            }
        }
    } else {
        rows = [
        {label: 'Books', content: 'Book content'},
        {label: 'Magazines', content: 'Magazines here'},
        {label: 'Newspaper', content: 'Newspaper content'}
        ];
    }

  if (wargs.args && wargs.args.selectedIndex) {
        selectedIndex = wargs.args.selectedIndex;
  }

  var container = document.getElementById(wargs.uuid);

  function addRow(label, content, url) {
      var _row = document.createElement("div");
      _row.className = "AccordionPanel";
      var _rowTitle = document.createElement("div");
      _rowTitle.className = gradient;
      _rowTitle.appendChild(document.createTextNode(label));
      _row.appendChild(_rowTitle);
      var _rowContent = document.createElement("div");
      _rowContent.className = "AccordionPanelContent";
      _row.appendChild(_rowContent);
      if (typeof content != 'undefined') {
        _rowContent.innerHTML = content;
      } else if (typeof url != 'undefined') {
        var _in = new jmaki.Injector();
        _in.inject({url:url, injectionPoint: _rowContent});
      }
      container.appendChild(_row);
  }

  // if the user used a custom template and no value 
  //use it otherwise use the built in one
  if (!wargs.value && !container.firstChild) {
      for(var i=0; i<rows.length; ++i) {
        var _row = rows[i];
            addRow(_row.label,_row.content, _row.url);
      }
      // set the selected row
      container.setAttribute("tabindex", selectedIndex);
  }

  this.wrapper = new Spry.Widget.Accordion(wargs.uuid,{ defaultPanel: selectedIndex });
 }