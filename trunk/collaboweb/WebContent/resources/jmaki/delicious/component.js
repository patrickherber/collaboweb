/* Copyright 2005 Sun Microsystems, Inc. All rights reserved.
   You may not modify, use, reproduce, or distribute this software
   except in compliance with the terms of the License at:
   http://developer.sun.com/berkeley_license.html
   $Id: component.js,v 1.3 2006/12/19 09:35:14 gmurray71 Exp $
*/

// define the namespaces
if (!jmaki.widgets.jmaki) {
    jmaki.widgets.jmaki = {};
}
jmaki.widgets.jmaki.delicious = {};

jmaki.widgets.jmaki.delicious.Widget = function(wargs) {

	var count = 20;
	var tag = "gmurray71";
	
	if (wargs.args) {
	  if (wargs.args.itemCount) {
	    count = wargs.args.itemCount;
	  }
	  if (wargs.args.tag) {
	    tag = wargs.args.tag;
	  }
	
	}
    
	this.wrapper = new jmaki.Delicious();
    this.loaded = this.wrapper.loaded;
	this.wrapper.load(wargs.uuid, tag, count);

}