// define the namespaces
if (typeof jmaki.widgets.flickr == 'undefined') {
	jmaki.widgets.flickr = {};
}
jmaki.widgets.flickr.favorites = {};

jmaki.widgets.flickr.favorites.Widget = function(wargs) {
    var self = this;
    var items = [];
    var pxSize = 50;
    var count = 8;
    var tags = "";
    var vertical = false;
    var columns = 2;

    if (typeof wargs.args != "undefined") {
        if (typeof wargs.args.tag != "undefined") {
            tags = wargs.args.tag;
        }
        if (typeof wargs.args.tags != "undefined") {
            tags = wargs.args.tags;
        }
        if (typeof wargs.args.size != "undefined") {
            pxSize = new Number(wargs.args.size);
        }

        if (typeof wargs.args.count != "undefined"){
           count = new Number(wargs.args.count);
        }

        if (typeof wargs.args.columns != "undefined"){
           columns = new Number(wargs.args.columns);
        }

        if (typeof wargs.args.apikey != "undefined") {
            fl = new jmaki.FlickrLoader(wargs.args.apikey);
        } else {
            fl = new jmaki.FlickrLoader();
        }
    }
	  
    function fCallback (obj, t) {

        for (var l = 0;  l < obj.items.length && l < count; l++) {

            var description = obj.items[l].description;     
            var start = description.indexOf("src=") + 10;
            var stop =  description.indexOf("_m.jpg");
            var imageBase = description.substring(start,stop);
            var thumbURL = imageBase + "_s.jpg";
            var murl = imageBase + "_m.jpg";
            var name = obj.items[l].title;
            var i = {name: name, url: thumbURL, murl: murl, link: obj.items[l].link};
            items.push(i);
        }
        showImages(items);
    }
      
    function showImages(items) {
        var targetDiv = document.getElementById(wargs.uuid);
        targetDiv.style.height = "auto";
        targetDiv.innerHTML = "";
        var i = 0;
        var line = document.createElement("div");
        targetDiv.appendChild(line);
        while (i < items.length) {
                       
            var node = document.createElement("img");
            var link = document.createElement("a");
            node.style.border = "0px";

            var  t = items[i];
            if (pxSize > 50) {
                node.src = t.murl;
            } else {
                node.src = t.url;
            }
            if (pxSize != 50) {
                node.style.height = pxSize +  "px";
                node.style.width = pxSize + "px";
            }
            link.href = t.link;
            link.appendChild(node);
            line.appendChild(link);
            targetDiv.appendChild(line);
            i++;
            if (i % columns == 0) {
                line = document.createElement("div");
                targetDiv.appendChild(line);
            }
        }
    }
    fl.load(tags,fCallback);
}