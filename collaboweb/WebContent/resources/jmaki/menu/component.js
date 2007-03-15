 // define the namespaces
if (!jmaki.widgets.jmaki) {
	jmaki.widgets.jmaki = {};
}
jmaki.widgets.jmaki.menu = {};

jmaki.widgets.jmaki.menu.Widget = function(wargs) {
	
    var container = document.getElementById(wargs.uuid);
    var topMenus = [];
    var navMenus = [];
    var menus = [];
    var hideTimer;
    var columns = [];

    // pull in the arguments
    if (wargs.value) {
        columns = wargs.value.columns;
        rows = wargs.value.rows;
        init();
    } else if (wargs.service) {
            jmaki.doAjax({url: wargs.service, callback: function(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
              var data = eval('(' + req.responseText + ')');
              columns = data.columns;
              init();
          }
        }
      }});
    } else {
        columns = [
        {label: 'Some Topic',
            menuItems: [
                {label:'Some Item', url:'1.jsp'},
                {label:'Some Item 2', url:'2.jsp'}
                ]},

        {label: 'Some Other Topic',

            menuItems: [
                {label:'Some Other Item', url:'1.jsp'},
                {label:'Some Other Item 2', url:'2.jsp'}
                ]}
        ];
        init();
    }

    function addStyle(style, nStyle){
       if (style.indexOf(nStyle) != -1) return style;
       if (style.length > 0) style += " ";
       return (style + nStyle);
    }

    function removeStyle(style, oStyle){
        if (style.indexOf(oStyle) == -1) return style;
        var styles = style.split(' ');
        var nStyle = "";
        for (var i = 0; i < styles.length; i++) {
            if (styles[i] != oStyle) nStyle += styles[i] + " ";
        }
        return nStyle;
    }


    function showMenu(e){
        hideMenus();
        var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
        var nStyle = addStyle(src.className, "jmakiMenuTopHover");
        src.className = nStyle;
        var pos = getPosition(topMenus[src.id]);
        navMenus[src.id].style.top = pos.y + container.clientHeight - 2 + "px";
        navMenus[src.id].style.left = pos.x + "px";
        navMenus[src.id].style.display = "block";
    }

    function menuSelect(e){
        hideMenus();
        var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
        var sp = src.id.split("_");
        var url = columns[sp[0]].menuItems[sp[1]].url;
        jmaki.publish('/jmaki/dcontainer', url);
    }

    function hideMenus(){
        for (var _i=0; _i < menus.length;_i++) {
            menus[_i].style.display = "none";
            topMenus[_i].className = removeStyle(topMenus[_i].className, "jmakiMenuTopHover");
        }
    }

    function startHide() {
        hideTimer = setTimeout(hideMenus, 2000);
    }

    function stopHide() {
        if (hideTimer != null)  clearTimeout(hideTimer);
    }

    function getPosition(_e) {
        var pX = 0;
        var pY = 0;
        while (_e.offsetParent) {
                pY += _e.offsetTop;
                pX += _e.offsetLeft;
                _e = _e.offsetParent;
        }
        return {x: pX, y: pY};
    }

    function getPosition(_e){
        var pX = 0;
        var pY = 0;
        if(_e.offsetParent) {
            while(true){
                pY += _e.offsetTop;
                pX += _e.offsetLeft;
                if(_e.offsetParent == null){
                    break;
                }
                _e = _e.offsetParent;
            }
        } else if(_e.y) {
                pY += _e.y;
                pX += _e.x;
        }
        return {x: pX, y: pY};
    }  

    function init() {
        // since we add right to left reverse the ordering
        columns = columns.reverse();
        var selectedIndex = 0;
        var endSpacer = document.createElement("div");
        endSpacer.className = "jmakiMenuTop jmakiMenuEndSpacer";
        container.appendChild(endSpacer);

        for (var i=0; i < columns.length; i++) {
            var menu = document.createElement("div");
            menu.className = "jmakiMenuTop";
            menu.id = i + '';
            var tmi = document.createTextNode(columns[i].label);
            menu.appendChild(tmi);
            menu.onmouseover = showMenu;
            container.appendChild(menu);
            topMenus.push(menu);
            var menuList = document.createElement("div");
            menuList.className = "jmakiMenuContainer";      
            menuList.onmouseout = startHide;
            menuList.onmousemove = stopHide;
            navMenus.push(menuList);
            container.appendChild(menuList);
            menus.push(menuList);
            if (i < columns.length -1) {
                var spacerDiv = document.createElement("div");
                spacerDiv.appendChild(document.createTextNode("|"));
                spacerDiv.className = "jmakiMenuSeparator";
                container.appendChild(spacerDiv);
            }

        }

        if (typeof wargs.args != 'undefined' && 
        typeof wargs.args.selectedIndex != 'undefined') {
            selectedIndex = Number(wargs.args.selectedIndex);
        }
        for(var oi=0; oi<columns.length; ++oi) {
            var mis = columns[oi].menuItems;
            for (var ii=0; ii < mis.length; ii++){
                var mi = document.createElement("div");
                mi.className = "jmakiMenuItem";
                mi.appendChild(document.createTextNode(mis[ii].label));
                mi.id = oi + "_" + ii;
                mi.onclick = menuSelect;
                mi.onmouseout = function(e){
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                    src.className = "jmakiMenuItem";
                };
                mi.onmousemove = function(e){
                    var src = (typeof window.event == 'undefined') ? e.target : window.event.srcElement;
                    src.className = "jmakiMenuItemHover";
                }
                navMenus[oi].appendChild(mi);
            }
        }
    }
}