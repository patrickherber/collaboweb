// define the namespaces
if (!jmaki.widgets.flickr) {
	jmaki.widgets.flickr = {};
}
jmaki.widgets.flickr.word = {};

jmaki.widgets.flickr.word.Widget = function(wargs) {

    var self = this;
    var legalCharacters = "abcdefghjiklmnopqrstuvwxyz";
    var items = {};
    var usedChars = {};
    var pxSize = 25;
    var originalText;
    var repeatCharacters = false;
    var renderred = false;
    
    var blankImage = wargs.widgetDir + "/images/blank.gif";
  
    var pxSize = "25";
    var word = "jmaki";
    var fl;
    var repeatCharacters = false;
	

     this.setWord = function(text, size, repeat) {
        items = {};
        usedChars = {};
        renderred = false;
        originalText = text.toLowerCase();
        
        if (typeof size != 'undefined') {
            pxSize = size;
         }
         if (typeof repeat != 'undefined') {
           repeatCharacters = repeat;
         }
        items.targetCharacters =  getCharacters(text);
        for (var i = 0; i < items.targetCharacters.length; i++) {
            fl.load("oneletter," + items.targetCharacters[i] + items.targetCharacters[i], fCallback);
        }
    }
 
    this.getTargetWord = function() {
        return items.targetCharacters.join();
    }
    
    function fCallback (obj, t) {

        var letter = t.split(',')[1].charAt(0);
        // get info from the JSON object
        items[letter] = [];
        // get the letter count which will be more than one if
        // repeact characters was set
        var count = Number(usedChars[letter]);
        for (var lo = 0; lo < count; lo++) {
            // randomly choose one of the letters
            var l= Math.floor(Math.random()*(obj.items.length));
            var description = obj.items[l].description;     
            var start = description.indexOf("src=") + 10;
            var stop =  description.indexOf("_m.jpg");
            var imageBase = description.substring(start,stop);
            var thumbURL = imageBase + "_s.jpg";
            var name = obj.items[l].title;
            var i = {name: name, url: thumbURL, link: obj.items[l].link};
            items[letter].push(i);
        }
        // check to see all the images are loaded
        if (checkIfDone() && !renderred) {
            renderred = true;
            showImages(items);
        }
    }
    
    // check to see if all the target characters have been loaded.
    function checkIfDone() {
        for (var l = 0; l < items.targetCharacters.length; l++) {
            if (typeof items[items.targetCharacters[l]] == 'undefined') {
                return false;
            }
        }
        return true;
    }
     
    function showImages(items) {
        var targetDiv = document.getElementById(wargs.uuid);
        targetDiv.style.height = "auto";
        targetDiv.innerHTML = "";
        for (var i =0;  i < originalText.length; i++) {
            var node = document.createElement("img");
            var link = document.createElement("a");
            node.style.border = "0px";
            node.style.height = pxSize + "px";
            node.style.width = pxSize + "px";
            // if we are working with a space handle it
            if (originalText.charAt(i) == ' ') {
                node.src = blankImage;
                targetDiv.appendChild(node);
            } else if (typeof items[originalText.charAt(i)] != 'undefined') {
                // take the next available letter off the top if repeatCharacters is on
                // otherwise default to the first one (as there is only one)
                var t;
                if (!repeatCharacters) {
                    if (typeof items[originalText.charAt(i)] != 'undefined') {
                        t = items[originalText.charAt(i)].pop();
                        node.src = t.url;
                        link.href = t.link;
                        link.appendChild(node);
                        targetDiv.appendChild(link);
                    }
                } else {
                  t = items[originalText.charAt(i)][0];
                  node.src = t.url;
                  targetDiv.appendChild(node);  
                }        
            }
        }
    }
    
    function getCharacters(w) {
        var word = w.toLowerCase();
        var characters = [];
        for (var ch=0; ch < word.length; ch++) {
            // skip spaces and only allow legal characters
            if (word.charAt(ch) != ' ' && legalCharacters.indexOf(word.charAt(ch)) != -1) {
                if (repeatCharacters == false) {     
                    if (typeof usedChars[word.charAt(ch)] == 'undefined') {
                        usedChars[word.charAt(ch)] = 1;
                        characters.push(word.charAt(ch));
                    } else {
                      var prev = Number(usedChars[word.charAt(ch)]);
                      // increment the counter so we can load multiple images
                      // for each character
                      usedChars[word.charAt(ch)] = ++prev; 
                    }
                } else {
                    if (typeof usedChars[ch] == 'undefined') {
                        characters.push(word.charAt(ch));
                        usedChars[word.charAt(ch)] = 1;
                    }
                }
            }
        }
        return characters;
    }
    if (typeof wargs.args != "undefined") {
        if (typeof wargs.args.word != "undefined") {
            word = wargs.args.word;
        }
        if (typeof wargs.args.size != "undefined") {
            pxSize = wargs.args.size;
        }
        if (wargs.args.repeatCharacters) {
            repeatCharacters = wargs.args.repeatCharacters;
        }
        if (wargs.args.tags) {
            fl = new jmaki.FlickrLoader(wargs.args.apikey);
        } else {
            fl = new jmaki.FlickrLoader();
        }
       self.setWord(word, pxSize,repeatCharacters); 
    }
}