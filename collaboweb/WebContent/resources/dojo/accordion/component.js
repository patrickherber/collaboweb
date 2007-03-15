dojo.require("dojo.widget.AccordionContainer");

// define the namespaces
jmaki.namespace("jmaki.widgets.dojo.accordion");

jmaki.widgets.dojo.accordion.Widget = function(wargs) {
    var self = this;
    var container = document.getElementById(wargs.uuid);
    var accordion = dojo.widget.createWidget("AccordionContainer", null, container);
    this.rows = [];
    // pull in the arguments
    if (typeof wargs.value != "undefined") {
        self.rows = wargs.value.rows;
        
        init();
    } else if (wargs.service) {
        jmaki.doAjax({url: wargs.service, callback: function(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
              var data = eval('(' + req.responseText + ')');
              self.rows = data.rows;
              init();
          }
        }
      }});
    } else {
        self.rows = [
        {label: 'Books', content: 'Book content'},
        {label: 'Magazines', content: 'Magazines here'},
        {label: 'Newspaper', content: 'Newspaper content'}
        ];
    }
    
    var selectedIndex = 0;
    
    if (wargs.args &&  wargs.args.selectedIndex != 'undefined') {
        selectedIndex = Number(wargs.args.selectedIndex);
    }
    
  function init() {
      for(i=0; i < self.rows.length; ++i) {
          var _row = self.rows[i];
          if (typeof _row.url == 'undefined') {
              var content = dojo.widget.createWidget("ContentPane", {label: self.rows[i].label, selected: i==selectedIndex});
              content.setContent(_row.content);
              accordion.addChild(content);
          } else {
              var _c = dojo.widget.createWidget("ContentPane", {label: _row.label, selected: i==1});
              var _d = document.createElement("div");
              _c.setContent(_d);
              accordion.addChild(_c);
              var _in = new jmaki.Injector();
              _in.inject({url:_row.url, injectionPoint: _d});
          }  
      }
      accordion.onResized();
  }
}