// define the namespaces
jmaki.namespace("jmaki.widgets.google.search");

jmaki.widgets.google.search.Widget = function(wargs) {
	// Create a search control
	var searchControl = new GSearchControl();
	
	// Add in a full set of searchers
	var localSearch = new GlocalSearch();
	searchControl.addSearcher(localSearch);
	searchControl.addSearcher(new GwebSearch());
	searchControl.addSearcher(new GvideoSearch());
	searchControl.addSearcher(new GblogSearch());
	
	var centerPoint =  'Santa Clara, CA';
	var defaultSearch = 'ajax java';
	
	if (wargs.args) {
	    if (wargs.args.defaultSearch) {
	        defaultSearch = wargs.args.defaultSearch;
	    }

            if (wargs.args.centerPoint) {
                centerPoint = wargs.args.centerPoint;
            }
	}

	// Set the Local Search center point
	localSearch.setCenterPoint(centerPoint);
	
	// Tell the searcher to draw itself and tell it where to attach
	searchControl.draw(document.getElementById(wargs.uuid));
	
	// Execute an inital search
    searchControl.execute(defaultSearch);
}