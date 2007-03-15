/* Copyright 2005 Sun Microsystems, Inc. All rights reserved. You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: http://developer.sun.com/berkeley_license.html
$Id: component.js,v 1.3 2006/12/19 09:35:14 gmurray71 Exp $
*/
// define the namespaces
if (!jmaki.widgets.jmaki) {
    jmaki.widgets.jmaki = {};
}
jmaki.widgets.jmaki.digg = {};

jmaki.widgets.jmaki.digg.Widget = function(wargs) {

	var count = 10;
	var diggTopic = "all";
	
	if (wargs.args) {
	   if (wargs.args.itemCount) {
	       count = wargs.args.itemCount;
	   }
	   if (wargs.args.diggTopic) {
	       diggTopic = wargs.args.diggTopic;
	   }
	}
	
	var targetDiv = document.getElementById(wargs.uuid);
	var d = new jmaki.DiggLoader(targetDiv, diggTopic, count);
}
