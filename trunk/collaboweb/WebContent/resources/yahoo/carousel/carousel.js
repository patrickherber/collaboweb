/**
 * Copyright (c) 2006, Bill W. Scott
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution 2.5 License. To view a copy 
 * of this license, visit http://creativecommons.org/licenses/by/2.5/ or send a letter to 
 * Creative Commons, 543 Howard Street, 5th Floor, San Francisco, California, 94105, USA.
 *
 * This work was created by Bill Scott (billwscott.com, looksgoodworkswell.com).
 * 
 * The only attribution I require is to keep this notice of copyright & license 
 * in this original source file.
 *
 * Version 0.3.2 - 10.19.2006
 *
 */
YAHOO.namespace("extension");

/**
* @class 
* The carousel class manages a content list (a set of LI elements within an UL list)  that can be displayed horizontally or vertically. The content can be scrolled back and forth  with or without animation. The content can reference static HTML content or the list items can  be created dynamically on-the-fly (with or without Ajax). The navigation and event handling  can be externalized from the class.
* @param {object|string} carouselElementID The element ID (id name or id object) of the DIV that will become a carousel
* @param {object} carouselCfg The configuration object literal containing the configuration that should be set for this module. See configuration documentation for more details.
* @constructor
*/
YAHOO.extension.Carousel = function(carouselElementID, carouselCfg) {
 		this.init(carouselElementID, carouselCfg);
	};

YAHOO.extension.Carousel.prototype = {

	/**
	 * Constant denoting that the carousel size is unbounded (no limits set on scrolling)
	 * @type number
	 */
	UNBOUNDED_SIZE: 1000000,
	
	/**
	 * Initializes the carousel object and all of its local members.
     * @param {object|string} carouselElementID The element ID (id name or id object) 
     * of the DIV that will become a carousel
     * @param {object} carouselCfg The configuration object literal containing the 
     * configuration that should be set for this module. See configuration documentation for more details.
	 */
	init: function(carouselElementID, carouselCfg) {

 		this.carouselElemID = carouselElementID;
 		this.carouselElem = YAHOO.util.Dom.get(carouselElementID);

 		this.prevEnabled = true;
 		this.nextEnabled = true;
 		
 		// Create the config object
 		this.cfg = new YAHOO.util.Config(this);

		/**
		 * orientation property. 
		 * Either "horizontal" or "vertical". Changes carousel from a 
		 * left/right style carousel to a up/down style carousel.
		 */
		this.cfg.addProperty("orientation", { 
				value:"horizontal", 
				suppressEvent:true
		} );		
		
		/**
		 * size property. 
		 * The upper hand for scrolling in the 'next' set of content. 
		 * Set to a large value by default (this means unlimited scrolling.) 
		 */
		this.cfg.addProperty("size", { 
				value:this.UNBOUNDED_SIZE, 
				suppressEvent:true
		} );

		/**
		 * numVisible property. 
		 * The number of items that will be visible.
		 */
		this.cfg.addProperty("numVisible", { 
				value:3, 
				suppressEvent:true
		} );

		/**
		 * firstVisible property. 
		 * Sets which item should be the first visible item in the carousel. Use to set which item will
		 * display as the first element when the carousel is first displayed. After the carousel is created,
		 * you can manipulate which item is the first visible by using the moveTo() or scrollTo() convenience
		 * methods.
		 */
		this.cfg.addProperty("firstVisible", { 
				value:1, 
				suppressEvent:true
		} );

		/**
		 * scrollInc property. 
		 * The number of items to scroll by. Think of this as the page increment.
		 */
		this.cfg.addProperty("scrollInc", { 
				value:3, 
				suppressEvent:true
		} );
		
		/**
		 * animationSpeed property. 
		 * The time (in seconds) it takes to complete the scroll animation. 
		 * If set to 0, animated transitions are turned off and the new page of content is 
		 * moved immdediately into place.
		 */
		this.cfg.addProperty("animationSpeed", { 
				value:0.25, 
				suppressEvent:true
		} );

		/**
		 * animationMethod property. 
		 * The <a href="http://developer.yahoo.com/yui/docs/animation/YAHOO.util.Easing.html">YAHOO.util.Easing</a> 
		 * method.
		 */
		this.cfg.addProperty("animationMethod", { 
				value:  YAHOO.util.Easing.easeOut, 
				suppressEvent:true
		} );
		
		/**
		 * animationCompleteHandler property. 
		 * JavaScript function that is called when the Carousel finishes animation 
		 * after a next or previous nagivation. 
		 * Only invoked if animationSpeed > 0. 
		 * Two parameters are passed: type (set to 'onAnimationComplete') and 
		 * args array (args[0] = direction [either: 'next' or 'previous']).
		 */
		this.cfg.addProperty("animationCompleteHandler", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * autoPlay property. 
		 * Specifies how many milliseconds to periodically auto scroll the content. 
		 * If set to 0 (default) then autoPlay is turned off. 
		 * If the user interacts by clicking left or right navigation, autoPlay is turned off. 
		 * You can restart autoPlay by calling the <em>startAutoPlay()</em>. 
		 * If you externally control navigation (with your own event handlers) 
		 * then you may want to turn off the autoPlay by calling<em>stopAutoPlay()</em>
		 */
		this.cfg.addProperty("autoPlay", { 
				value:0, 
				suppressEvent:true
		} );
		
		/**
		 * wrap property. 
		 * Specifies whether to wrap when at the end of scrolled content. When the end is reached,
		 * the carousel will scroll backwards to the item 1 (the animationSpeed parameter is used to 
		 * determine how quickly it should animate back to the start.)
		 * Ignored if the <em>size</em> attribute is not explicitly set 
		 * (i.e., value equals YAHOO.extension.Carousel.UNBOUNDED_SIZE)
		 */
		this.cfg.addProperty("wrap", { 
				value:false, 
				suppressEvent:true
		} );
		
		/**
		 * navMargin property. 
		 * The margin space for the navigation controls. This is only useful for horizontal carousels 
		 * in which you have embedded navigation controls. 
		 * The <em>navMargin</em> allocates space between the left and right margins 
		 * (each navMargin wide) giving space for the navigation controls.
		 */
		this.cfg.addProperty("navMargin", { 
				value:0, 
				suppressEvent:true
		} );
		
		/**
		 * prevElementID property. 
		 * The element ID (string ID) of the HTML element that will provide the previous navigation control.
		 * If supplied, then events are wired to this control to fire scroll events to move the carousel to
		 * the previous content. You may want to provide your own interaction for controlling the carousel. If
		 * so leave this unset and provide your own event handling mechanism.
		 */
		this.cfg.addProperty("prevElementID", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * nextElementID property. 
		 * The element ID (string ID) of the HTML element that will provide the next navigation control.
		 * If supplied, then events are wired to this control to fire scroll events to move the carousel to
		 * the next content. You may want to provide your own interaction for controlling the carousel. If
		 * so leave this unset and provide your own event handling mechanism.
		 */
		this.cfg.addProperty("nextElementID", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * loadInitHandler property. 
		 * JavaScript function that is called when the Carousel needs to load 
		 * the initial set of visible items. Two parameters are passed: 
		 * type (set to 'onLoadInit') and an argument array (args[0] = start index, args[1] = last index).
		 */
		this.cfg.addProperty("loadInitHandler", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * loadNextHandler property. 
		 * JavaScript function that is called when the Carousel needs to load 
		 * the next set of items (in response to the user navigating to the next set.) 
		 * Two parameters are passed: type (set to 'onLoadInit') and 
		 * args array (args[0] = start index, args[1] = last index).
		 */
		this.cfg.addProperty("loadNextHandler", { 
				value:null, 
				suppressEvent:true
		} );
				
		/**
		 * loadPrevHandler property. 
		 * JavaScript function that is called when the Carousel needs to load 
		 * the previous set of items (in response to the user navigating to the previous set.) 
		 * Two parameters are passed: type (set to 'onLoadInit') and args array 
		 * (args[0] = start index, args[1] = last index).
		 */
		this.cfg.addProperty("loadPrevHandler", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * prevButtonStateHandler property. 
		 * JavaScript function that is called when the enabled state of the 
		 * 'previous' control is changing. The responsibility of 
		 * this method is to enable or disable the 'previous' control. 
		 * Two parameters are passed to this method: <em>type</em> 
		 * (which is set to "onPrevButtonStateChange") and <em>args</em>, 
		 * an array that contains two values. 
		 * The parameter args[0] is a flag denoting whether the 'previous' control 
		 * is being enabled or disabled. The parameter args[1] is the element object 
		 * derived from the <em>prevElementID</em> parameter.
		 * If you do not supply a prevElementID then you will need to track
		 * the elements that you would want to enable/disable while handling the state change.
		 */
		this.cfg.addProperty("prevButtonStateHandler", { 
				value:null, 
				suppressEvent:true
		} );
		
		/**
		 * nextButtonStateHandler property. 
		 * JavaScript function that is called when the enabled state of the 
		 * 'next' control is changing. The responsibility of 
		 * this method is to enable or disable the 'next' control. 
		 * Two parameters are passed to this method: <em>type</em> 
		 * (which is set to "onNextButtonStateChange") and <em>args</em>, 
		 * an array that contains two values. 
		 * The parameter args[0] is a flag denoting whether the 'next' control 
		 * is being enabled or disabled. The parameter args[1] is the element object 
		 * derived from the <em>nextElementID</em> parameter.
		 * If you do not supply a nextElementID then you will need to track
		 * the elements that you would want to enable/disable while handling the state change.
		 */
		this.cfg.addProperty("nextButtonStateHandler", { 
				value:null, 
				suppressEvent:true
		} );
		
		
 		if(carouselCfg) {
 			this.cfg.applyConfig(carouselCfg);
 		}
 		
 		this.numVisible = this.cfg.getProperty("numVisible");
 		this.scrollInc = this.cfg.getProperty("scrollInc");
		this.navMargin = this.cfg.getProperty("navMargin");
 		this.animSpeed = this.cfg.getProperty("animationSpeed");
		this.initHandler = this.cfg.getProperty("loadInitHandler");
		this.animationCompleteHandler = this.cfg.getProperty("animationCompleteHandler");
		this.size = this.cfg.getProperty("size");
		this.wrap = this.cfg.getProperty("wrap");
		this.animationMethod = this.cfg.getProperty("animationMethod");
		this.orientation = this.cfg.getProperty("orientation");
		this.nextElementID = this.cfg.getProperty("nextElementID");
		this.prevElementID = this.cfg.getProperty("prevElementID");
		this.autoPlay = this.cfg.getProperty("autoPlay");
		this.autoPlayTimer = null;
		this.firstVisible = this.cfg.getProperty("firstVisible");
		this.lastVisible = this.firstVisible;
		this.lastPrebuiltIdx = 0;
		this.currSize = 0;
		
		// CSS style classes
 		var carouselListClass = "carousel-list";
 		var carouselClipRegionClass = "carousel-clip-region";
 		var carouselNextClass = "carousel-next";
 		var carouselPrevClass = "carousel-prev";
 		
 		// prefetch elements
 		this.carouselList = YAHOO.util.Dom.getElementsByClassName(carouselListClass, 
												"ul", this.carouselElem)[0];
							
		if(this.nextElementID === null) {
			this.carouselNext = YAHOO.util.Dom.getElementsByClassName(carouselNextClass, 
												"div", this.carouselElem)[0];
		} else {
			this.carouselNext = YAHOO.util.Dom.get(this.nextElementID);
		}

		if(this.nextElementID === null) {
 			this.carouselPrev = YAHOO.util.Dom.getElementsByClassName(carouselPrevClass, 
												"div", this.carouselElem)[0];
		} else {
			this.carouselPrev = YAHOO.util.Dom.get(this.prevElementID);
		}
		
		this.clipReg = YAHOO.util.Dom.getElementsByClassName(carouselClipRegionClass, 
												"div", this.carouselElem)[0];
												
		// add a style class dynamically so that the correct styles get applied for a vertical carousel
		if(this.isVertical()) {
			YAHOO.util.Dom.addClass(this.carouselList, "carousel-vertical");
		}
		
		// initialize the animation objects for next/previous
 		this.scrollNextAnim = new YAHOO.util.Motion(this.carouselList, this.scrollNextParams, 
   								this.animSpeed, this.animationMethod);
 		this.scrollPrevAnim = new YAHOO.util.Motion(this.carouselList, this.scrollPrevParams, 
   								this.animSpeed, this.animationMethod);
		
		// If they supplied a nextElementID then wire an event listener for the click
		if(this._isValidObj(this.carouselNext)) {
			YAHOO.util.Event.addListener(this.carouselNext, "click", this._scrollNext, this);
		} 
		
		// If they supplied a prevElementID then wire an event listener for the click
		if(this._isValidObj(this.carouselPrev)) {
			YAHOO.util.Event.addListener(this.carouselPrev, "click", this._scrollPrev, this);
		}
				
		// Wire up the various event handlers that they might have supplied
		if(this._isValidObj(this.initHandler)) {
			this.loadInitialEvt = new YAHOO.util.CustomEvent("onLoadInit", this);
			this.loadInitialEvt.subscribe(this.initHandler, this);
		}
		this.nextHandler = this.cfg.getProperty("loadNextHandler");
		if(this._isValidObj(this.nextHandler)) {
			this.loadNextEvt = new YAHOO.util.CustomEvent("onLoadNext", this);
			this.loadNextEvt.subscribe(this.nextHandler, this);
		}
		this.prevHandler = this.cfg.getProperty("loadPrevHandler");
		if(this._isValidObj(this.prevHandler)) {
			this.loadPrevEvt = new YAHOO.util.CustomEvent("onLoadPrev", this);
			this.loadPrevEvt.subscribe(this.prevHandler, this);
		}
		if(this._isValidObj(this.animationCompleteHandler)) {
			this.animationCompleteEvt = new YAHOO.util.CustomEvent("onAnimationComplete", this);
			this.animationCompleteEvt.subscribe(this.animationCompleteHandler, this);
		}
		this.prevButtonStateHandler = this.cfg.getProperty("prevButtonStateHandler");
		if(this._isValidObj(this.prevButtonStateHandler)) {
			this.prevButtonStateEvt = new YAHOO.util.CustomEvent("onPrevButtonStateChange", 
							this);
			this.prevButtonStateEvt.subscribe(this.prevButtonStateHandler, this);
		}
		this.nextButtonStateHandler = this.cfg.getProperty("nextButtonStateHandler");
		if(this._isValidObj(this.nextButtonStateHandler)) {
			this.nextButtonStateEvt = new YAHOO.util.CustomEvent("onNextButtonStateChange", this);
			this.nextButtonStateEvt.subscribe(this.nextButtonStateHandler, this);
		}
		
		// Since loading may take some time, wire up a listener to fire when at least the first
		// element actually gets loaded
  		YAHOO.util.Event.onAvailable(this.carouselElemID + "-item-1", this._firstElementIsLoaded, this);
  		
  		// Call the initial loading sequence
		this._loadInitial();	

	},

	// /////////////////// Public API //////////////////////////////////////////

	/**
	 * Clears all items from the list and resets to the carousel to its original initial state.
	 */
	clear: function() {
		this.moveTo(1);
		this._removeChildrenFromNode(this.carouselList);
		this.stopAutoPlay();
		this.firstVisible = 1;
		this.lastVisible = 1;
		this.lastPrebuiltIdx = 0;
		this.currSize = 0;
		this.size = this.cfg.getProperty("size");
	},
	
	/**
	 * Clears all items from the list and calls the loadInitHandler to load new items into the list. 
	 * The carousel size is reset to the original size set during creation.
	 * @param {number}	numVisible	Optional parameter: numVisible. 
	 * If set, the carousel will resize on the reload to show numVisible items.
	 */
	reload: function(numVisible) {
	    if(this._isValidObj(numVisible)) {
	    	this.numVisible = numVisible;
	    }
		this.clear();
		YAHOO.util.Event.onAvailable(this.carouselElemID + "-item-1", this._firstElementIsLoaded, this);  		
		this._loadInitial();
	},

	/**
	 * Clears all items from the list and calls the loadInitHandler to load new items into the list. 
	 * The carousel size is reset to the original size set during creation.
	 * With patch from Dan Hobbs for handling unordered loading.
	 * @param {number}	idx	which item in the list to potentially create. 
	 * If item already exists it will not create a new item.
	 * @param {string}	innerHTML	The innerHTML string to use to create the contents of an LI element.
	 */
	addItem: function(idx, innerHTML) {
        var liElem = this.getCarouselItem(idx);

		// Need to create the li
		if(!this._isValidObj(liElem)) {
			liElem = this._createItem(idx, innerHTML);
			this.carouselList.appendChild(liElem);
			
		} else if(this._isValidObj(liElem.placeholder)) {		
	    	var newLiElem = this._createItem(idx, innerHTML);
			//var oldLiElem = this.carouselList.getElementsByTagName("li")[idx - 1];
			this.carouselList.replaceChild(newLiElem, liElem);
		}
		if(this.isVertical()) {
			YAHOO.util.Dom.setStyle(liElem, "height", liElem.offsetHeight + "px");
		}

	},

	/**
	 * Inserts a new LI item before the index specified. Uses the innerHTML to create the contents of the new LI item
	 * @param {number}	refIdx	which item in the list to insert this item before. 
	 * @param {string}	innerHTML	The innerHTML string to use to create the contents of an LI element.
	 */
	insertBefore: function(refIdx, innerHTML) {
		if(refIdx < 1) {
			refIdx = 1;
		}
		
		var insertionIdx = refIdx - 1;
		
		if(insertionIdx > this.lastPrebuiltIdx) {
			this._prebuildItems(this.lastPrebuiltIdx, refIdx); // is this right?
		}
		
		var liElem = this._insertBeforeItem(refIdx, innerHTML);
		
		// depends on recalculation of this.size above
		if(this.firstVisible > insertionIdx || this.lastVisible < this.size) {
			if(this.nextEnabled === false) {
				this._enableNext();
			}
		}

		return liElem;
	},
	
	/**
	 * Inserts a new LI item after the index specified. Uses the innerHTML to create the contents of the new LI item
	 * @param {number}	refIdx	which item in the list to insert this item after. 
	 * @param {string}	innerHTML	The innerHTML string to use to create the contents of an LI element.
	 */
	insertAfter: function(refIdx, innerHTML) {
	
		if(refIdx > this.size) {
			refIdx = this.size;
		}
		
		var insertionIdx = refIdx + 1;			
		
		// if we are inserting this item past where we have prebuilt items, then
		// prebuild up to this point.
		if(insertionIdx > this.lastPrebuiltIdx) {
			this._prebuildItems(this.lastPrebuiltIdx, insertionIdx+1);
		}

		var liElem = this._insertAfterItem(refIdx, innerHTML);		

		if(insertionIdx > this.size) {
			this.size = insertionIdx;
			if(this.nextEnabled === false) {
				this._enableNext();
			}
		}
		
		// depends on recalculation of this.size above
		if(this.firstVisible > insertionIdx || this.lastVisible < this.size) {
			if(this.nextEnabled === false) {
				this._enableNext();
			}
		}

		return liElem;
	},	

	/**
	 * Simulates a next button event. Causes the carousel to scroll the next set of content into view.
	 */
	scrollNext: function() {
		this._scrollNext(null, this);
		
		// we know the timer has expired.
		this.autoPlayTimer = null;
		if(this.autoPlay !== 0) {
			this.autoPlayTimer = this.startAutoPlay();
		}
	},
	
	/**
	 * Simulates a prev button event. Causes the carousel to scroll the previous set of content into view.
	 */
	scrollPrev: function() {
		this._scrollPrev(null, this);
	},
	
	/**
	 * Scrolls the content to place itemNum as the start item in the view 
	 * (if size is specified, the last element will not scroll past the end.). 
	 * Uses current animation speed & method.
	 * @param {number}	newStart	The item to scroll to. 
	 */
	scrollTo: function(newStart) {
		this._position(newStart, true);
	},

	/**
	 * Moves the content to place itemNum as the start item in the view 
	 * (if size is specified, the last element will not scroll past the end.) 
	 * Ignores animation speed & method; moves directly to the item. 
	 * Note that you can also set the <em>firstVisible</em> property upon initialization 
	 * to get the carousel to start at a position different than 1.	
	 * @param {number}	newStart	The item to move directly to. 
	 */
	moveTo: function(newStart) {
		this._position(newStart, false);
	},

	/**
	 * Starts up autoplay. If autoPlay has been stopped (by calling stopAutoPlay or by user interaction), 
	 * you can start it back up by using this method.
	 * @param {number}	interval	optional parameter that sets the interval 
	 * for auto play the next time that autoplay fires. 
	 */
	startAutoPlay: function(interval) {
		// if interval is passed as arg, then set autoPlay to this interval.
		if(this._isValidObj(interval)) {
			this.autoPlay = interval;
		}
		
		// if we already are playing, then do nothing.
		if(this.autoPlayTimer !== null) {
			return this.autoPlayTimer;
		}
				
		var oThis = this;  
		var autoScroll = function() { oThis.scrollNext(); };
		var timeoutId = setTimeout( autoScroll, this.autoPlay );
		return timeoutId;
	},

	/**
	 * Stops autoplay. Useful for when you want to control what events will stop the autoplay feature. 
	 * Call <em>startAutoPlay()</em> to restart autoplay.
	 */
	stopAutoPlay: function() {
		if (this.autoPlayTimer !== null) {
			clearTimeout(this.autoPlayTimer);
			this.autoPlayTimer = null;
		}
	},
	
	/**
	 * Returns whether the carousel's orientation is set to vertical.
	 */
	isVertical: function() {
		return (this.orientation != "horizontal");
	},
	
	
	/**
	 * Check to see if an element (by index) has been loaded or not. If the item is simply pre-built, but not
	 * loaded this will return false. If the item has not been pre-built it will also return false.
	 * @param {number}	idx	Index of the element to check load status for. 
	 */
	isItemLoaded: function(idx) {
		var liElem = this.getCarouselItem(idx);
		
		// if item exists and is not a placeholder, then it is already loaded.
		if(this._isValidObj(liElem) && !this._isValidObj(liElem.placeholder)) {
			return true;
		}
		
		return false;
	},
	
	/**
	 * Lookup the element object for a carousel list item by index.
	 * @param {number}	idx	Index of the element to lookup. 
	 */
	getCarouselItem: function(idx) {
		var elemName = this.carouselElemID + "-item-" + idx;
 		var liElem = YAHOO.util.Dom.get(elemName);
		return liElem;	
	},
	
	// /////////////////// PRIVATE API //////////////////////////////////////////
	_firstElementIsLoaded: function(me) {
 		var ulKids = me.carouselList.childNodes;
 		var li = null;
		for(var i=0; i<ulKids.length; i++) {
		
			li = ulKids[i];
			if(li.tagName == "LI" || li.tagName == "li") {
				break;
			}
		}
		var liPaddingWidth;
		if(me.isVertical()) {
			liPaddingWidth = parseInt(YAHOO.util.Dom.getStyle(li, "paddingLeft"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "paddingRight"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginLeft"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginRight"),10);
			var liPaddingHeight = parseInt(YAHOO.util.Dom.getStyle(li, "paddingTop"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "paddingBottom"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginTop"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginBottom"),10);
			
			me.scrollAmountPerInc = (li.offsetHeight+liPaddingHeight);
			me.clipReg.style.width = (li.offsetWidth + liPaddingWidth) + "px";
			me.clipReg.style.height = (me.scrollAmountPerInc*me.numVisible) + "px";
			me.carouselElem.style.width = (li.offsetWidth + liPaddingWidth*2) + "px";			

			// if we set the initial start > 1 then this will adjust the scrolled location
			var currY = YAHOO.util.Dom.getY(me.carouselList);	
			YAHOO.util.Dom.setY(me.carouselList, 
							currY - me.scrollAmountPerInc*(me.firstVisible-1));

		} else {
			liPaddingWidth = parseInt(YAHOO.util.Dom.getStyle(li, "paddingLeft"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "paddingRight"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginLeft"),10) + 
						parseInt(YAHOO.util.Dom.getStyle(li, "marginRight"),10);
            // added by gmurray
            // on IE the padding with is not coming in properly
            if (isNaN(liPaddingWidth)) {
                   liPaddingWidth = 0;
            }						
			me.scrollAmountPerInc = (li.offsetWidth+liPaddingWidth);
			me.carouselElem.style.width = ((me.scrollAmountPerInc*me.numVisible)+me.navMargin*2) + "px";
			me.clipReg.style.width = (me.scrollAmountPerInc*me.numVisible)+"px";

			// if we set the initial start > 1 then this will adjust the scrolled location
			var currX = YAHOO.util.Dom.getX(me.carouselList);
			YAHOO.util.Dom.setX(me.carouselList, 
							currX - me.scrollAmountPerInc*(me.firstVisible-1));
		}

		YAHOO.util.Dom.setStyle(me.carouselElem, "visibility", "visible");
	},

	// From Mike Chambers: http://weblogs.macromedia.com/mesh/archives/2006/01/removing_html_e.html
	_removeChildrenFromNode: function(node)
	{
		if(!this._isValidObj(node))
		{
      		return;
		}
   
		var len = node.childNodes.length;
   
		while (node.hasChildNodes())
		{
			node.removeChild(node.firstChild);
		}
	},
	
	_prebuildLiElem: function(idx) {
		var liElem = document.createElement("li");
		liElem.id = this.carouselElemID + "-item-" + idx;
		// this is default flag to know that we're not really loaded yet.
		liElem.placeholder = true;   
		this.carouselList.appendChild(liElem);
		
		this.lastPrebuiltIdx = (idx > this.lastPrebuiltIdx) ? idx : this.lastPrebuiltIdx;
	},
	
	_createItem: function(idx, innerHTML) {
	    var liElem = document.createElement("li");
		liElem.id = this.carouselElemID + "-item-" + idx;
		liElem.innerHTML = innerHTML;
		return liElem;
	},
	
	// idx is the location to insert after
	_insertAfterItem: function(refIdx, innerHTML) {
		return this._insertBeforeItem(refIdx+1, innerHTML);
	},
	
	
	_insertBeforeItem: function(refIdx, innerHTML) {

		var refItem = this.getCarouselItem(refIdx);
		
		if(this.size != this.UNBOUNDED_SIZE) {
			this.size += 1;
		}
				
		for(var i=this.lastPrebuiltIdx; i>=refIdx; i--) {
			var anItem = this.getCarouselItem(i);
			if(this._isValidObj(anItem)) {
				anItem.id = this.carouselElemID + "-item-" + (i+1);
			}
		}

		var liElem = this._createItem(refIdx, innerHTML);
		
		var insertedItem = this.carouselList.insertBefore(liElem, refItem);
		this.lastPrebuiltIdx += 1;
		
		return liElem;
	},
	
	// TEST THIS... think it has to do with prebuild
	insertAfterEnd: function(innerHTML) {
		return this.insertAfter(this.size, innerHTML);
	},
		
	_position: function(newStart, showAnimation) {
		// do we bypass the isAnimated check?
		if(newStart > this.firstVisible) {
			var inc = newStart - this.firstVisible;
			this._scrollNextInc(this, inc, showAnimation);
		} else {
			var dec = this.firstVisible - newStart;
			this._scrollPrevInc(this, dec, showAnimation);
		}
	},
	
	
	// event handler
	_scrollNext: function(e, carousel) {
		if(carousel.scrollNextAnim.isAnimated()) {
			return false; // might be better to set ourself waiting for animation completion and
			// then just do this function. that will allow faster scroll responses.
		}

		// if fired by an event and wrap is set and we are already at end then wrap
		var currEnd = carousel.firstVisible + carousel.numVisible-1;
		if(carousel.wrap && currEnd == carousel.size) {
			var currAnimSpeed = carousel.animSpeed;
			carousel.scrollTo(1);
		} else if(e !== null) { // event fired this so disable autoplay
			carousel.stopAutoPlay();
			carousel._scrollNextInc(carousel, carousel.scrollInc, (carousel.animSpeed !== 0));
		} else {
			carousel._scrollNextInc(carousel, carousel.scrollInc, (carousel.animSpeed !== 0));
		}


	},
	
	// probably no longer need carousel passed in, this should be correct now.
	_scrollNextInc: function(carousel, inc, showAnimation) {

		var currFirstVisible = carousel.firstVisible;
		
		var newEnd = carousel.firstVisible + inc + carousel.numVisible - 1;
		newEnd = (newEnd > carousel.size) ? carousel.size : newEnd;
		var newStart = newEnd - carousel.numVisible + 1;
		inc = newStart - carousel.firstVisible;
		carousel.firstVisible = newStart;

		// if the prev button is disabled and start is now past 1, then enable it
		if((carousel.prevEnabled === false) && (carousel.firstVisible > 1)) {
			carousel._enablePrev();
		}
		// if next is enabled && we are now at the end, then disable
		if((carousel.nextEnabled === true) && (newEnd == carousel.size)) {
			carousel._disableNext();
		}
		
		if(inc > 0) {
			if(carousel._isValidObj(carousel.nextHandler)) {
				carousel.lastVisible = carousel.firstVisible + carousel.numVisible - 1;
				
				carousel.currSize = (carousel.lastVisible > carousel.currSize) ?
											carousel.lastVisible : carousel.currSize;
											
				var alreadyCached = carousel._areAllItemsLoaded(currFirstVisible, 
										carousel.lastVisible);
				carousel.loadNextEvt.fire(carousel.firstVisible, carousel.lastVisible, alreadyCached);
			}
			
			if(showAnimation) {
	 			var nextParams = { points: { by: [-carousel.scrollAmountPerInc*inc, 0] } };
	 			if(carousel.isVertical()) {
	 				nextParams = { points: { by: [0, -carousel.scrollAmountPerInc*inc] } };
	 			}
 		
	 			carousel.scrollNextAnim = new YAHOO.util.Motion(carousel.carouselList, 
	 							nextParams, 
   								carousel.animSpeed, carousel.animationMethod);
				if(carousel._isValidObj(carousel.animationCompleteHandler)) {
					carousel.scrollNextAnim.onComplete.subscribe(this._handleAnimationComplete, [carousel, "next"]);
				}
				carousel.scrollNextAnim.animate();
			} else {
				if(carousel.isVertical()) {
					var currY = YAHOO.util.Dom.getY(carousel.carouselList);
										
					YAHOO.util.Dom.setY(carousel.carouselList, 
								currY - carousel.scrollAmountPerInc*inc);
				} else {
					var currX = YAHOO.util.Dom.getX(carousel.carouselList);
					YAHOO.util.Dom.setX(carousel.carouselList, 
								currX - carousel.scrollAmountPerInc*inc);
				}
			}
			
		}
		
		return false;
	},
	
	_handleAnimationComplete: function(type, args, argList) {
		var carousel = argList[0];
		var direction = argList[1];
		
		carousel.animationCompleteEvt.fire(direction);

		
	},
	
	// If EVERY item is already loaded in the range then return true
	// Also prebuild whatever is not already created.
	_areAllItemsLoaded: function(first, last) {
		var itemsLoaded = true;
		for(var i=first; i<=last; i++) {
			var liElem = this.getCarouselItem(i);
			
			// If the li elem does not exist, then prebuild it in the correct order
			// but still flag as not loaded (just prebuilt the li item.
			if(!this._isValidObj(liElem)) {
				this._prebuildLiElem(i);
				itemsLoaded = false;
			// but if the item exists and is a placeholder, then
			// note that this item is not loaded (only a placeholder)
			} else if(this._isValidObj(liElem.placeholder)) {
				itemsLoaded = false;
			}
		}
		return itemsLoaded;
	}, 
	
	_prebuildItems: function(first, last) {
		for(var i=first; i<=last; i++) {
			var liElem = this.getCarouselItem(i);
			
			// If the li elem does not exist, then prebuild it in the correct order
			// but still flag as not loaded (just prebuilt the li item.
			if(!this._isValidObj(liElem)) {
				this._prebuildLiElem(i);
			}
		}
	}, 

	_scrollPrev: function(e, carousel) {
		if(carousel.scrollPrevAnim.isAnimated()) {
			return false;
		}
		carousel._scrollPrevInc(carousel, carousel.scrollInc, (carousel.animSpeed !== 0));
	},
	
	_scrollPrevInc: function(carousel, dec, showAnimation) {

		var currLastVisible = carousel.lastVisible;
		var newStart = carousel.firstVisible - dec;
		newStart = (newStart <= 1) ? 1 : (newStart);
		var newDec = carousel.firstVisible - newStart;
		carousel.firstVisible = newStart;
		
		// if prev is enabled && we are now at position 1, then disable
		if((carousel.prevEnabled === true) && (carousel.firstVisible == 1)) {
			carousel._disablePrev();
		}
		// if the next button is disabled and end is < size, then enable it
		if((carousel.nextEnabled === false) && 
						((carousel.firstVisible + carousel.numVisible - 1) < carousel.size)) {
			carousel._enableNext();
		}

		// if we are decrementing
		if(newDec > 0) {			
			if(carousel._isValidObj(carousel.prevHandler)) {
				carousel.lastVisible = carousel.firstVisible + carousel.numVisible - 1;

				carousel.currSize = (carousel.lastVisible > carousel.currSize) ?
											carousel.lastVisible : carousel.currSize;

				var alreadyCached = carousel._areAllItemsLoaded(carousel.firstVisible, 
									currLastVisible);
				carousel.loadPrevEvt.fire(carousel.firstVisible, carousel.lastVisible, alreadyCached);
			}

			if(showAnimation) {
	 			var prevParams = { points: { by: [carousel.scrollAmountPerInc*newDec, 0] } };
	 			if(carousel.isVertical()) {
	 				prevParams = { points: { by: [0, carousel.scrollAmountPerInc*newDec] } };
	 			}
 		
	 			carousel.scrollPrevAnim = new YAHOO.util.Motion(carousel.carouselList,
	 							prevParams, 
   								carousel.animSpeed, carousel.animationMethod);
				if(carousel._isValidObj(carousel.animationCompleteHandler)) {
					carousel.scrollPrevAnim.onComplete.subscribe(this._handleAnimationComplete, [carousel, "prev"]);
				}
				carousel.scrollPrevAnim.animate();
			} else {
				if(carousel.isVertical()) {
					var currY = YAHOO.util.Dom.getY(carousel.carouselList);
					YAHOO.util.Dom.setY(carousel.carouselList, currY + 
							carousel.scrollAmountPerInc*newDec);				
				} else {
					var currX = YAHOO.util.Dom.getX(carousel.carouselList);
					YAHOO.util.Dom.setX(carousel.carouselList, currX + 
							carousel.scrollAmountPerInc*newDec);
				}
			}
		}
		
		return false;
	},
	
	/**
	 * _loadInitial looks at firstItemVisible for the start (not necessarily 1)
	 */
	_loadInitial: function() {
		this.lastVisible = this.firstVisible + this.numVisible - 1;

		this.currSize = (this.lastVisible > this.currSize) ?
									this.lastVisible : this.currSize;

		// Since firstItemVisible can be > 1 need to check for disabling either
		// previous or next controls
		if(this.firstVisible == 1) {
			this._disablePrev();
		}
		if(this.lastVisible == this.size) {
			this._disableNext();
		}
		
		// Load from 1 to the last visible
		// The _firstElementIsLoaded method will adjust the scroll position
		// for starts > 1
		if(this._isValidObj(this.initHandler)) {
			var alreadyCached = this._areAllItemsLoaded(1, this.lastVisible);
			this.loadInitialEvt.fire(1, this.lastVisible, alreadyCached);
		}
		
		if(this.autoPlay !== 0) {
			this.autoPlayTimer = this.startAutoPlay();
		}		
    },
		
	_disablePrev: function() {
		this.prevEnabled = false;
		if(this._isValidObj(this.prevButtonStateEvt)) {
			this.prevButtonStateEvt.fire(false, this.carouselPrev);
		}
		if(this._isValidObj(this.carouselPrev)) {
			YAHOO.util.Event.removeListener(this.carouselPrev, "click", this._scrollPrev);
		}
	},
	
	_enablePrev: function() {
		this.prevEnabled = true;
		if(this._isValidObj(this.prevButtonStateEvt)) {
			this.prevButtonStateEvt.fire(true, this.carouselPrev);
		}
		if(this._isValidObj(this.carouselPrev)) {
			YAHOO.util.Event.addListener(this.carouselPrev, "click", this._scrollPrev, this);
		}
	},
		
	_disableNext: function() {
		if(this.wrap) {
			return;
		}
		
		this.nextEnabled = false;
		if(this._isValidObj(this.nextButtonStateEvt)) {
			this.nextButtonStateEvt.fire(false, this.carouselNext);
		}
		if(this._isValidObj(this.carouselNext)) {
			YAHOO.util.Event.removeListener(this.carouselNext, "click", this._scrollNext);
		}
	},
	
	_enableNext: function() {
		this.nextEnabled = true;
		if(this._isValidObj(this.nextButtonStateEvt)) {
			this.nextButtonStateEvt.fire(true, this.carouselNext);
		}
		if(this._isValidObj(this.carouselNext)) {
			YAHOO.util.Event.addListener(this.carouselNext, "click", this._scrollNext, this);
		}
	},
		
	_isValidObj: function(obj) {

		if (null == obj) {
			return false;
		}
		if ("undefined" == typeof(obj) ) {
			return false;
		}
		return true;

	},

	debugMsg: function(msg) 
	{
		var debugArea = YAHOO.util.Dom.get("debug-area");
		if(!this._isValidObj(debugArea)) {
			debugArea = document.createElement("div");
			debugArea.id = "debug-area";
			document.body.appendChild(debugArea);
		}
		debugArea.innerHTML = debugArea.innerHTML + "<br/>" + msg;
	},
	clearDebug: function() 
	{
		var debugArea = document.getElementById("debug-area");
		if(this._isValidObj(debugArea)) {
			debugArea.innerHTML = "";
		}
	}

};