// define the namespaces
jmaki.namespace("jmaki.widgets.yahoo.tabbedview");

jmaki.widgets.yahoo.tabbedview.Widget = function(wargs) {

    var topic = "/yahoo/tabbedview";
    var tabs = [];

    if (typeof wargs.value != "undefined") {
            
            // convert the tabs object into an array
            if (typeof wargs.value.tabs == "object") {
                for (var i in wargs.value.tabs) {
                    var row = [];
                    for (var ir in wargs.value.tabs[i] ) {  
                        row[ir] = wargs.value.tabs[i][ir];
                    }
                    tabs.push(row);
                }
            }
        } else {
            // default some data;
            tabs = [
            {label: 'Tab 0', content: 'This is the default data provided for a tabbed view in the component.js file.' +
             ' To customize the tabs set the value of the widget to an object containing an object with a tabs property containg' +
             ' an array of tabs. For example: {tabs:[{label:\'My Tab\', content: \'Some Content\'},{label:\'My Tab 2\', content: \'Tab 2 Content\'} ]}' +
             '. You can also include content from any URL within your domain. Example: {tabs:[{label:\'My Tab\', url: \'URL_TO_TAB_CONTENT\'} ]}'},
             {label: 'Tab 1', content: 'Tab 1 content'},
             {label: 'Tab 2', content: 'Tab 2 content'}
             ];    
        }
        var tabView = new YAHOO.widget.TabView(wargs.uuid);
        
        for(var i=0; i<tabs.length; ++i) {
            var _row = tabs[i];
            if (typeof _row.url == 'undefined') {
                var _r = new YAHOO.widget.Tab({
                    label: _row.label,
                    content: _row.content,
                    active: (i == 0)
                });
                tabView.addTab(_r);
            } else {
                var divId = wargs.uuid + "_tab" + i;
                // calcualte height here
                var content = ("<div style='height:360px' id='" + divId +"'>Loading..." + "</div>");
                
                var _tv = tabView;
                var _r = new YAHOO.widget.Tab({
                    label: _row.label,
                    content: content,
                    active: (i == 0)
                });
                _tv.addTab(_r);
                jmaki.injector.inject({url:_row.url, injectionPoint: divId});
            }
    }
}