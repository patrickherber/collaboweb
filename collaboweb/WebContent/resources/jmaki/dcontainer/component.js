/* Copyright 2005 Sun Microsystems, Inc. All rights reserved. You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: http://developer.sun.com/berkeley_license.html
$Id: component.js,v 1.5 2006/12/19 09:35:13 gmurray71 Exp $
*/

// define the namespaces
if (!jmaki.widgets.jmaki) {
    jmaki.widgets.jmaki = {};
}
jmaki.widgets.jmaki.dcontainer = {};

jmaki.widgets.jmaki.dcontainer.Widget = function(wargs) {
    var self = this;
    this.url;
    this.uuid = wargs.uuid;
    var startHeight;
    var startWidth;
    var overflow;
    var useIframe = false;
    // subscribe to this topic for url update requests
    var topic = "/jmaki/dcontainer";    

    if (typeof wargs.args != 'undefined'){
        if (typeof wargs.args.height != 'undefined'){
            startHeight = wargs.args.height;
        }
        if (typeof wargs.args.width != 'undefined'){
            startWidth = wargs.args.width;
        }   
        if (typeof wargs.args.overflow != 'undefined'){
            overflow = wargs.args.overflow;
        }   
        if (typeof wargs.args.iframe != 'undefined'){
            useIframe = (wargs.args.iframe == 'true' || wargs.args.iframe);
        }
        if (typeof wargs.args.topic != 'undefined') {
            topic = wargs.args.topic;
        }
    }
    this.dcontainer = new jmaki.DContainer(self.uuid,
                                            useIframe,
                                            startWidth,
                                            startHeight,
                                    overflow, topic);
    if (typeof wargs.args != 'undefined') {
        if (typeof wargs.args.url != 'undefined') {
            this.url = wargs.args.url;
            this.dcontainer.loadURL(this.url);
        }    
    }
}