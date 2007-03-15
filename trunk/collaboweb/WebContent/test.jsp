<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>photo gallery/javascript photo viewer</title>
</head>
<body>
<center>
<form method="POST" name="rotater">
<script type="text/javascript">
var photos=new Array();
var which=0;
photos[0]="images/add.gif"
photos[1]="images/calendar.gif"
photos[2]="images/delete.gif"
photos[3]="images/detail.gif"
photos[4]="images/edit.gif"
photos[5]="images/lang_de.gif"
photos[6]="images/lang_en.gif"
photos[7]="images/lang_fr.gif"
photos[8]="images/lang_it.gif"

function backward(){
	if (which > 0) {
		which--;
	} else {
		which = photos.length-1;
	}
	document.images.photoslider.src=photos[which]
}

function forward(){
	if (which<photos.length-1){
		which++;
	} else {
		which = 0;
	}
	document.images.photoslider.src=photos[which]
}
</script>
<input type="button" value="&lt;&lt;Back" onClick="backward()">
<input type="button" value="Next&gt;&gt;" onClick="forward()">
<br>
<font size=-1><a href="#" onClick="which=1;backward();return false">Start Over</a></font>
</form>
<center><img SRC="images/add.gif" NAME="photoslider"></center>
</center>

</body>
</html>