 /**
* @class Chart
* @author InfoSoft Global (P) Ltd. www.InfoSoftGlobal.com
* @version 3.0
*
* Copyright (C) InfoSoft Global Pvt. Ltd.
*
* Chart class is the super class for a FusionCharts chart from
* which individual chart classes inherit. The chart class is
* responsible for a lot of features inherited by child classes.
* Chart class also bunches code that is used by all other charts
* so as to avoid code duplication.
*/
//Utilities
import com.fusioncharts.is.helper.Utils;
//Log class
import com.fusioncharts.is.helper.Logger;
//Enumeration class
import com.fusioncharts.is.helper.FCEnum;
//String extension
import com.fusioncharts.is.extensions.StringExt;
//Color Extension
import com.fusioncharts.is.extensions.ColorExt;
//Math Extension
import com.fusioncharts.is.extensions.MathExt;
//Drawing Extension
import com.fusioncharts.is.extensions.DrawingExt;
//Custom Error Object
import com.fusioncharts.is.helper.FCError;
//Tool Tip
import com.fusioncharts.is.helper.ToolTip;
//Style Managers
import com.fusioncharts.is.core.StyleObject;
import com.fusioncharts.is.core.StyleManager;
//Anotation Mananger
import com.fusioncharts.is.helper.AnnotationManager;
//Macro Class
import com.fusioncharts.is.helper.Macros;
//Depth Manager
import com.fusioncharts.is.helper.DepthManager;
//Class to help as saving as image
import com.fusioncharts.is.helper.BitmapSave;
import com.fusioncharts.is.helper.FCProgressBar;
//Delegate
import mx.utils.Delegate;
//Event Dispatcher
import mx.events.EventDispatcher;
//External Interface - to expose methods via JavaScript
import flash.external.ExternalInterface;
//Class Definition
class com.fusioncharts.is.core.Chart{
	//Instance properties
	//Version
	private var _version:String = "3.0.2";
	//XML data object for the chart.
	private var xmlData:XML;
	//Array and Enumeration listing charts objects
	//arrObjects array would store the list of chart
	//objects as string. The motive is to retrieve this
	//string information to be added to log.
	public var arrObjects:Array;
	//Object Enumeration stores the above array elements
	//(chart objects) as enumeration, so that we can refer
	//to each chart element as a numeric value.
	public var objects:FCEnum;
	//Object to store chart elements
	private var elements:Object;
	//Object to store chart parameters
	//All attributes retrieved from XML will be stored in
	//params object.
	private var params:Object;
	//Object to store chart configuration
	//Any calculation done by our code will be stored in
	//config object. Or, if we over-ride any param values
	//we store in config.
	private var config:Object;
	//DepthManager instance. The DepthManager class helps us
	//allot and retrieve depths of various objects in the chart.
	private var dm:DepthManager;
	//Movie clip in which the entire chart will be built.
	//If chart is not being loaded into another Flash movie,
	//parentMC is set as _root (as we need only 1 chart per
	//movie timeline).
	private var parentMC:MovieClip;
	//Movie clip reference for actual chart MC
	//All chart objects (movie clips) would be rendered as
	//sub-movie clips of this movie clip.
	private var cMC:MovieClip;	
	//Movie clip reference for log MC. The logger elements
	//are contained as a part of this movie clip. Even if the
	//movie is not in debug mode, we create at least the
	//parent log movie clip.
	private var logMC:MovieClip;
	//Movie clip reference for tool tip. We created a separate
	//tool tip movie clip because of two reasons. One, tool tip
	//always appears above the chart. So, we've created tool tip
	//movie clip at a depth greater than that of cMC(chart movie
	//clip). Secondly, the tool tip is not an integral part of
	//chart - it's a helper class.
	private var ttMC:MovieClip;
	//Tool Tip Object. This object is common to all charts.
	//Whenever we need to show/hide tool tips, we called methods
	//of this class.
	private var tTip:ToolTip;
	//Movie clip reference for text box which will be used to determine
	//text width, height for various text fields. During calculation
	//of points (width/height) for chart, we need to simulate various
	//text fields so that we come to know their exact width/height.
	//Based on that, we accomodate other elements on chart. This
	//movie clip is the container for that test text field movie clip.
	//This text field never shows on the chart canvas.
	private var tfTestMC:MovieClip;
	//Co-ordinates for generating test TF
	//We put it outside stage so that it is never visible.
	private var testTFX:Number = - 2000;
	private var testTFY:Number = - 2000;
	//Embedded Font
	//Denotes which font is embedded as a part of the chart. If you're
	//loading the chart in your movie, you need to embed the same font
	//face (plain - not bold - not italics) in your movie, to enable
	//rotated labels. Else, the rotated labels won't show up at all.
	private var _embeddedFont:String = "Verdana";
	//Reference to logger class instance.
	private var lgr;
	//Depth in parent movie clip in which we've to create chart
	//This is useful when you are loading this chart class as a part
	//of your Flash movie, as then you can create various charts at
	//various depths of a single movie clip. In case of single chart
	//(non-load), this is set to 3 (as 1 and 2 are reserved for global
	//progress bar and global application text).
	private var depth:Number;
	//Width & Height of chart in pixels. If the chart is in exactFit
	//mode, the width and height remains the same as that of original
	//document (.fla). However, everything is scaled in proportion.
	//In case of noScale, these variables assume the width and height
	//provided either by chart constructor (when loading chart in your
	//flash movie) or HTML page.
	private var width:Number, height:Number;
	//X and Y Position of top left of chart. When loading the chart in
	//your flash movie, you might want to shift the chart to particular
	//position. These x and y denote that shift.
	private var x:Number, y:Number;
	//Debug mode - Flag whether the chart is in debug mode. It's passed
	//from the HTML page as OBJECT/EMBED variable debugMode=1/0.
	private var debugMode:Boolean;
	//Counter to store timeElapsed. The chart animates sequentially.
	//e.g., the background comes first, then canvas, then div lines.
	//So, we internally need to keep a track of time passed, so that
	//we can call next what to render.
	private var timeElapsed:Number = 0;
	//Language for application messages. By default, we show application
	//messages in English. However, if you need to define your application
	//messages, you can do so in com\fusioncharts\includes\AppMessages.as
	//This value is passed from HTML page as OBJECT/EMBED variable.
	private var lang:String;
	//Scale mode - noScale or exactFit.
	//This value is passed from HTML page as OBJECT/EMBED variable.
	private var scaleMode:String;
	//Is Online Mode. If the chart is working online, we avoid caching
	//of data. Else, we cache data.
	private var isOnline:Boolean;
	//Style Manager object. The style manager object handles the style
	//quotient (FONT, BLUR, BEVEL, GLOW, SHADOW, ANIMATION) of different
	//elements of chart.
	private var styleM:StyleManager;
	//Reference to annotation Manager for this chart
	private var am:AnnotationManager;
	//Macros container. Macros help the user define pre-defined dynamic
	//values in XML for setting animation position.
	private var macro:Macros;	
	//Store a short name reference for Utils.getFirstValue function
	//and Utils.getFirstNumber function
	//As we'll be using this function a lot.
	private var getFV:Function;
	private var getFN:Function;
	private var toBoolean:Function;
	//Short name for ColorExt.formatHexColor function
	private var formatColor:Function;
	//Short name for Utils.createText function
	private var createText:Function;
	//Error handler. We've a custom error object to represent
	//any chart error. All such errors get logged and none are visible
	//to end user, to make their experience smooth.
	var e:FCError;
	//Whether to register chart with JS
	private var registerWithJS:Boolean;
	//DOM Id
	private var DOMId:String;
	//Flag to indicate whether we've conveyed the chart rendering event
	//to JavaScript and loader Flash
	private var renderEventConveyed:Boolean = false;
	//Text field to hold application messages.
	private var tfAppMsg:TextField;
	/**
	* Constructor method for chart. Here, we store the
	* properties of the chart from constructor parameters
	* in instance variables.
	* @param	targetMC		Parent movie clip reference in which
	*							we'll create chart movie clips
	* @param	depth			Depth inside parent movie clip in which
	*							we'll create chart movie clips
	* @param	width			Width of chart
	* @param	height			Height of chart
	* @param	x				x Position of chart
	* @param	y				y Position of chart
	* @param	debugMode		Boolean value indicating whether the chart
	*							is in debug mode.
	* @param	registerWithJS	Whether to register the chart with JavaScript.
	* @param	DOMId			DOM Id of the chart.
	* @param	lang			2 Letter ISO code for the language of application
	*							messages
	* @param	scaleMode		Scale mode of the movie - noScale or exactFit
	*/
	function Chart(targetMC:MovieClip, depth:Number, width:Number, height:Number, x:Number, y:Number, debugMode:Boolean, registerWithJS:Boolean, DOMId:String, lang:String, scaleMode:String){
		//Get the reference to Utils.getFirstValue
		this.getFV = Utils.getFirstValue;
		//Get the reference to getFirstNumber
		this.getFN = Utils.getFirstNumber;
		//Get reference to toBoolean function
		this.toBoolean = Utils.toBoolean;
		//Get reference to ColorExt.formatHexColor
		this.formatColor = ColorExt.formatHexColor;
		//Get reference to Utils.createText
		this.createText = Utils.createText;
		//Store properties in instance variables
		this.parentMC = targetMC;
		this.depth = depth;
		this.width = width;
		this.height = height;
		this.x = getFN (x, 0);
		this.y = getFN (y, 0);
		this.debugMode = getFV (debugMode, false);
		this.registerWithJS = getFV(registerWithJS, false);
		this.DOMId = getFV(DOMId, "");
		this.lang = getFV (lang, "EN");
		this.scaleMode = getFV (scaleMode, "noScale");		
		//Create an empty enumeration for list of objects
		//We'll feed values to this from setChartObjects() method.
		this.objects = new FCEnum();
		//Initialize parameter storage object
		this.params = new Object();
		//Initialize chart configuration storage object
		this.config = new Object();
		//Object to store chart rendering intervals
		this.config.intervals = new Object();
		//Elements object to store the various elements of chart.
		this.elements = new Object();
		//Initialize style manager
		this.styleM = new StyleManager(this, this.objects);
		//Initialize Macros
		this.macro = new Macros();
		//Initialize Depth Manager
		this.dm = new DepthManager(0);
		// ----- CREATE REQUIRED MOVIE CLIPS NOW -----//
		//Create the chart movie clip container
		this.cMC = parentMC.createEmptyMovieClip ("Chart", depth + 1);
		//Re-position the chart Movie clip to required x and y position
		this.cMC._x = this.x;
		this.cMC._y = this.y;
		//Create movie clip for tool tip
		this.ttMC = parentMC.createEmptyMovieClip ("ToolTip", depth + 4);
		//Initialize tool tip by setting co-ordinates and span area
		this.tTip = new ToolTip (this.ttMC, this.x, this.y, this.width, this.height, 8);
		//Tool tip has been created - so initialize Annotation Manager		
		this.am = new AnnotationManager(this, this.objects, this.styleM, this.macro, this.tTip, this.width, this.height, this.registerWithJS);		
		//Text-field test movie clip
		this.tfTestMC = parentMC.createEmptyMovieClip ("TestTF", depth + 5);		
		//Create the movie clip holder for log
		this.logMC = parentMC.createEmptyMovieClip ("Log", depth + 3);
		//Re-position the log Movie clip to required x and y position
		this.logMC._x = this.x;
		this.logMC._y = this.y;
		//Create the log instance
		this.lgr = new Logger (logMC, this.width, this.height);
		if (this.debugMode){
			/**
			* If the chart is in debug mode, we:
			* - Add parameters to the chart.
			* - Show log.
			*/
			//Log the chart parameters
			this.log ("Info", "Chart loaded and initialized.", Logger.LEVEL.INFO);
			this.log ("Initial Width", String (this.width) , Logger.LEVEL.INFO);
			this.log ("Initial Height", String (this.height) , Logger.LEVEL.INFO);
			this.log ("Scale Mode", this.scaleMode, Logger.LEVEL.INFO);
			this.log ("Debug Mode", (this.debugMode == true) ? "Yes":"No", Logger.LEVEL.INFO);
			this.log ("Application Message Language", this.lang, Logger.LEVEL.INFO);
			//Now show the log
			lgr.show();
		}
		//Expose image saving functionality to JS. 
		if (this.registerWithJS==true && ExternalInterface.available){
			ExternalInterface.addCallback("saveAsImage",this, saveAsImage);
		}
		//Expose print chart method
		//Expose the methods to JavaScript using ExternalInterface		
		if (this.registerWithJS==true && ExternalInterface.available){
			ExternalInterface.addCallback("print", this, printChart);
		}
		//Initialize EventDispatcher to implement the event handling functions
		mx.events.EventDispatcher.initialize(this);
	}
	//These functions are defined in the class to prevent
	//the compiler from throwing an error when they are called
	//They are not implemented until EventDispatcher.initalize(this)
	//overwrites them.
	public function dispatchEvent() {
	}
	public function addEventListener() {
	}
	public function removeEventListener() {
	}
	public function dispatchQueue() {
	}
	//----------- DATA RELATED AND PARSING METHODS ----------------//
	/**
	* setXMLData helps set the XML data for the chart. The XML data
	* is acquired from external source. Like, if you load the chart
	* in your movie, you need to create/load the XML data and handle
	* the loading events (etc.). Finally, when the proper XML is loaded,
	* you need to pass it to Chart class. When FusionCharts is loaded
	* in HTML, the .fla file loads the XML and displays loading progress
	* bar and text. Finally, when loaded, errors are checked for, and if
	* ok, the XML is passed to chart for further processing and rendering.
	*	@param	objXML	XML Object containing the XML Data
	*	@return		Nothing.
	*/
	public function setXMLData(objXML:XML):Void {
		//If the XML data has no child nodes, we display error
		if (!objXML.hasChildNodes()){
			//Show "No data to display" error
			tfAppMsg = this.renderAppMessage(_global.getAppMessage("NODATA", this.lang));
			//Add a message to the log.
			this.log("ERROR", "No data to display. There isn't any node/element in the XML document. Please check if your dataURL is properly URL Encoded or, if XML has been correctly embedded in case of dataXML.", Logger.LEVEL.ERROR);
		} else {
			//We've data.
			//Store the XML data in class
			this.xmlData = new XML();
			this.xmlData = objXML;
			//Show rendering chart message
			tfAppMsg = this.renderAppMessage (_global.getAppMessage("RENDERINGCHART", this.lang));
			//If the chart is in debug mode, then add XML data to log
			if (this.debugMode){
				var strXML:String = this.xmlData.toString();
				//First we need to convert < and > in XML to &lt; and &gt;
				//As our logger textbox is HTML enabled.
				strXML = StringExt.replace (strXML, "<", "&lt;");
				strXML = StringExt.replace (strXML, ">", "&gt;");
				//Also convert carriage returns to <BR> for better viewing.
				strXML = StringExt.replace (strXML, "/r", "<BR>");
				this.log ("XML Data", strXML, Logger.LEVEL.CODE);
			}
		}
	}
	
	//----------- CORE FUNCTIONAL METHODS ----------//
	/**
	* setChartObjects method stores the list of chart objects
	* in local arrObjects array and objects enumeration.
	*	@return				Nothing
	*/
	private function setChartObjects():Void{
		//Copy array to instance variable
		//Iterate through the list of objects and add it to the enumeration
		var i:Number;
		for (i=0; i<this.arrObjects.length; i ++){
			this.objects.addItem(this.arrObjects[i]);
		}		
		//Now, if the chart is in debug mode, add the list to log		
		if (this.debugMode){
			var strChartObjects:String="";
			for (i=0; i<this.arrObjects.length; i++){
				strChartObjects += "<LI>" + this.arrObjects[i] + "</LI>";
			}
			this.log("Chart Objects", strChartObjects, Logger.LEVEL.INFO);
		}
	}
	/**
	* setToolTipParam method sets the parameter for tool tip.
	*/
	private function setToolTipParam():Void{
		//Get the style object for tool tip
		var tTipStyleObj:Object = this.styleM.getTextStyle (this.objects.TOOLTIP);
		this.tTip.setParams (tTipStyleObj.font, tTipStyleObj.size, tTipStyleObj.color, tTipStyleObj.bgColor, tTipStyleObj.borderColor, tTipStyleObj.isHTML, false);
	}
	/**
	 * setupAnnotationMC method creates the annotation container movie clips
	 * and conveys to the annotation class.
	*/
	private function setupAnnotationMC():Void{
		//Create movie clips for annotations-below and annotations abobe in the allotted depth.
		var annBelowMC:MovieClip = this.cMC.createEmptyMovieClip("AnnotationsBelow",this.dm.getDepth("ANNOTATIONBELOW"));
		var annAboveMC:MovieClip = this.cMC.createEmptyMovieClip("AnnotationsAbove",this.dm.getDepth("ANNOTATIONABOVE"));
		//Convey it to annotation manager instance
		this.am.setMC(annBelowMC, annAboveMC);
	}
	/**
	 * renderAnnotationBelow method is called when annotations below the chart are to
	 * be rendered.
	*/
	private function renderAnnotationBelow():Void{
		this.am.render(true);
		//Clear Interval
		clearInterval(this.config.intervals.annotationsBelow);
	}
	/**
	 * renderAnnotationAbove method is called when annotations above the chart are to 
	 * be rendered.	 
	*/
	private function renderAnnotationAbove():Void{
		this.am.render(false);
		//Clear Interval
		clearInterval(this.config.intervals.annotationsAbove);
	}
	/**
	 * exposeChartRendered method is called when the chart has rendered. 
	 * Here, we expose the event to JS (if required) & also dispatch a
	 * event (so that, if other movies are loading this chart, they can listen).
	*/
	private function exposeChartRendered():Void{
		//Proceed, if we've not already conveyed the event
		if (this.renderEventConveyed==false){
			//Expose event to JS
			if (this.registerWithJS==true &&  ExternalInterface.available){
				ExternalInterface.call("FC_Rendered", this.DOMId);
			}
			//Dispatch an event to loader class
			this.dispatchEvent({type:"rendered", target:this});
			//Update flag that we've conveyed both rendered events now.
			this.renderEventConveyed = true;
		}
		//Clear calling interval
		clearInterval(this.config.intervals.renderedEvent);
	}
	/**
	* log method records a message to the chart's logger. We record
	* messages in the logger, only if the chart is in debug mode to
	* save memory
	*	@param	strTitle	Title of messsage
	*	@param	strMsg		Message to be logged
	*	@param	intLevel	Numeric level of message - a value from
	*						Logger.LEVEL Enumeration
	*/
	public function log(strTitle:String, strMsg:String, intLevel:Number):Void{
		if (debugMode){
			lgr.record (strTitle, strMsg, intLevel);
		}
	}
	/**
	 * returnAbtMenuItem method returns a context menu item that reads
	 * "About FusionCharts".
	*/
	private function returnAbtMenuItem():ContextMenuItem{
		//Create a about context menu item
		var aboutCMI:ContextMenuItem = new ContextMenuItem ("About FusionCharts", Delegate.create (this, openAboutFusionCharts));
		aboutCMI.separatorBefore = true;		
		return aboutCMI;
	}
	/**
	 * returnImageSaveMenuItem method returns the context menu item to save image.
	*/
	private function returnImageSaveMenuItem():ContextMenuItem{
		//Create the context menu item
		var saveCMI : ContextMenuItem = new ContextMenuItem ("Save as Image", Delegate.create (this, saveAsImage));
		saveCMI.separatorBefore = true;		
		return saveCMI;
	}
	/**
	 * openAboutFusionCharts is the handler for "About FusionCharts"
	 * context menu item
	*/
	private function openAboutFusionCharts():Void{
		//Open FusionCharts website
		getURL("http://www.fusioncharts.com/","_blank");
	}
	/**
	 * saveAsImage method saves the chart as an image.
	*/
	private function saveAsImage():Void{
		//Here, we do the following:
		//1. Create an instance of BitmapSave to capture the chart's image.
		//2. Define listener objects to track progress of it.
		//3. Send data to the defined server side script.
		//RLE Compression is done while capturing the bitmap data.
		
		//Reference to this class
		var classRef = this;
		
		//Create listener object for capture.
		var cList:Object = new Object();		

		//Progress bar positioning and dimension
		var PBWidth:Number = (this.width > 200) ? 150 : (this.width - 25);
		var PBStartX:Number = this.x + this.width/2 - PBWidth/2;
		var PBStartY:Number = this.y + this.height/2 - 15;

		//Create a progress bar to show capturing pixels.
		var imgSaveBg:MovieClip;
		//Text field to show percent complete for image saving progress bar
		var tfImageSavePB:TextField;
		//Create the empty movie clips
		imgSaveBg = this.parentMC.createEmptyMovieClip("ImgSaveBg", this.depth+5);
		//Create a black overlay rectangle
		imgSaveBg.beginFill(0x000000,20);
		imgSaveBg.moveTo(this.x, this.y);
		imgSaveBg.lineTo(this.x + this.width, this.y);
		imgSaveBg.lineTo(this.x + this.width, this.y + this.height);
		imgSaveBg.lineTo(this.x, this.y + this.height);
		imgSaveBg.lineTo(this.x, this.y);
		
		//Another white rectangle in center
		var pad:Number = 20;
		imgSaveBg.beginFill(0xFFFFFF,100);
		imgSaveBg.lineStyle(1, 0xE2E2E2, 100);
		imgSaveBg.moveTo(PBStartX - pad, PBStartY - pad);
		imgSaveBg.lineTo(PBStartX  + PBWidth + pad, PBStartY - pad);
		imgSaveBg.lineTo(PBStartX  + PBWidth + pad, PBStartY + 40 + pad);
		imgSaveBg.lineTo(PBStartX  - pad , PBStartY + 40 + pad);
		imgSaveBg.lineTo(PBStartX - pad, PBStartY - pad);
		
		//Capture mouse event from everything otherwise underneath
		imgSaveBg.useHandCursor = false;
		imgSaveBg.onRollOver = function(){
		}
		
		//Instantiate the progress bar
		var imgSPB:FCProgressBar = new FCProgressBar(this.parentMC, this.depth+6, 0, 100, PBStartX, PBStartY, PBWidth, 15, this.params.imageSaveDialogColor, this.params.imageSaveDialogColor, 1);
		
		//Create the text
		tfImageSavePB = Utils.createText (false, "Capturing data", this.parentMC, this.depth+7, this.x + this.width/2, this.y + this.height/2 + 15, null, {align:"center", vAlign:"bottom", bold:false, italic:false, underline:false, font:"Verdana", size:10, color:this.params.imageSaveDialogFontColor, isHTML:true, leftMargin:0, letterSpacing:0, bgColor:"", borderColor:""}, false, 0, 0).tf;
		var tFormat:TextFormat = tfImageSavePB.getTextFormat();
		
		//Event to detect when capturing is complete.
		cList.onCaptureComplete = function(eventObj:Object){
			//Remove all progress bar related movie clips
			imgSPB.destroy();
			tfImageSavePB.removeTextField();
			imgSaveBg.removeMovieClip();			
			//Capturing is complete. Send it to server side script.			
			classRef.sendImageData(eventObj.out);			
		}
		
		//Event to detect progress of capturing
		cList.onProgress = function(eventObj:Object){
			//Update the text field
			tfImageSavePB.htmlText = "<font face='Verdana' size='10' color='#" + this.params.imageSaveDialogFontColor + "'>Capturing data: " + eventObj.percentDone + "%</font>";
			tfImageSavePB.setTextFormat(tFormat);
			//Set the value of progress bar
			imgSPB.setValue(eventObj.percentDone);
		}

		//Create an instance of BitmapSave 
		var bmpS:BitmapSave = new BitmapSave(this.cMC,this.x,this.y,this.width,this.height,0xffffff);		
		
		//Before we start capturing, we need to make sure that none of the movie clips
		//are cached as bitmap. So run a function that does this job.
		if(!this.cMC.skipBmpCacheCheck){
			var arrCache:Array = this.setPreSaving(this.cMC);
		}
		
		//Capture the bitmap now.
		bmpS.capture();
		
		//Now that the bitmap is captured, we need to set the cache property to original
		if(!this.cMC.skipBmpCacheCheck){
			this.resetPostSaving(this.cMC, arrCache)
		}
		
		//Add the event listeners
		bmpS.addEventListener("onCaptureComplete", cList);
		bmpS.addEventListener("onProgress", cList);
		
		return;	
	}	
	/**
	 * This method sets the bitmap caching of all objects in the chart
	 * so as to avoid freezing of interface.
	*/
	private function setPreSaving(mc:MovieClip):Array{
		//Get the list of filters.
		var arrMcFilters:Array = new Array()
		//Iterate through each movie clip
		for(var i in mc){
			//Work only if it's a movie clip.
			if(mc[i] instanceof MovieClip){
				//Store the filters for this MC
				arrMcFilters[i] = new Array();
				arrMcFilters[i]['filters'] = mc[i].filters;
				mc[i].filters = [];
				//Store the cache property
				arrMcFilters[i]['cache'] = mc[i].cacheAsBitmap;
				mc[i].cacheAsBitmap = false;
				//Store children
				arrMcFilters[i]['children'] = arguments.callee(mc[i]);
			}
		}
		//Return the array
		return arrMcFilters;
	}
	/**
	 * This method restores the bitmap caching state of all the objects
	 * in the chart, once capturing is done.
	*/
	private function resetPostSaving(mc:MovieClip, arrMcFilters:Array){
		for(var i in arrMcFilters){			
			mc[i].filters = arrMcFilters[i]['filters'];;
			mc[i].cacheAsBitmap = arrMcFilters[i]['cache']
			arguments.callee(mc[i],arrMcFilters[i]['children']);
		}
	}
	
	/**
	 * sendImageData actually sends the image data to the server.
	*/
	private function sendImageData(strData:String):Void{
		//Create a loadvars object
		var l:LoadVars = new LoadVars();		
		//Set width and height
		l.width = this.width;
		l.height = this.height;
		//Color to ignore
		l.bgcolor = "FFFFFF";
		l.data = strData;
		//Send the data to server side script.
		l.send(this.params.imageSaveURL, "_self", "POST");
		//Delete loadvars after sending data
		delete l;
	}
	/**
	* printChart method prints the chart.
	*/
	public function printChart():Void{
		//Create a Print Job Instance
		var PrintQueue = new PrintJob();
		//Show the Print box.
		var PrintStart:Boolean = PrintQueue.start();
		//If User has selected Ok, set the parameters.
		if (PrintStart){
			//Add the chart MC to the print job with the required dimensions
			//If the chart width/height is bigger than page width/height, we need to scale.
			if (this.width>PrintQueue.pageWidth || this.height>PrintQueue.pageHeight){				
				//Scale on the lower factor
				var factor:Number = Math.min((PrintQueue.pageWidth/this.width),(PrintQueue.pageHeight/this.height));
				//Scale the movie clip to fit paper size 
				this.cMC._xScale = factor*100;
				this.cMC._yScale = factor*100;
			}
			//Add the chart to printer queue
			PrintQueue.addPage (this.cMC, {xMin:0, xMax:this.width, yMin:0, yMax:this.height}, {printAsBitmap:true});
			//Send the page for printing
			PrintQueue.send();
			//Re-scale back to normal form (as the printing is over).
			this.cMC._xScale = this.cMC._yScale = 100;
		}		
		delete PrintQueue;
	}
	/**
	* reInit method re-initializes the chart. This method is basically called
	* when the user changes chart data through JavaScript. In that case, we need
	* to re-initialize the chart, set new XML data and again render.
	* Order of calling is chart.remove() --> chart.reInit();
	*/
	public function reInit():Void{
		//Re-initialize params and config object
		this.params = new Object();
		this.config = new Object();
		//Re-init objects too (as annotation ids might have been added)
		this.objects = new FCEnum();
		//Re-init chart elements
		this.elements = new Object();
		//Re-feed the original chart objects.
		this.setChartObjects();
		//Object to store chart rendering intervals
		this.config.intervals = new Object();
		//Re-create an empty chart movie clip
		this.cMC = parentMC.createEmptyMovieClip ("Chart", depth + 1);
		//Re-position the chart Movie clip to required x and y position
		this.cMC._x = this.x;
		this.cMC._y = this.y;
		//Reset the style manager
		this.styleM = new StyleManager(this, this.objects);
		//Reset macros
		this.macro = new Macros();
		//Re-create the annotation manager
		this.am = new AnnotationManager(this, this.objects, this.styleM, this.macro, this.tTip, this.width, this.height, this.registerWithJS);
		//Reset depth manager
		this.dm.clear();
		this.dm.setStartDepth (0);
		//Set timeElapsed to 0
		this.timeElapsed = 0;
	}
	/**
	* remove method removes the chart by clearing the chart movie clip
	* and removing any listeners. However, the logger still stays on.
	* To remove the logger too, you need to call destroy method of chart.
	*/
	public function remove():Void {
		//Remove all the intervals (which might not have been cleared)
		//from this.config.intervals
		var item:Object;
		for (item in this.config.intervals){
			//Clear interval
			clearInterval(this.config.intervals[item]);
		}
		//Remove all annotations
		this.am.destroy();
		//Remove application message (if any)
		this.removeAppMessage(this.tfAppMsg);
		//Remove the chart movie clip
		cMC.removeMovieClip();
		//Hide tool tip
		tTip.hide();
	}
	/**
	* destroy method destroys the chart by removing the chart movie clip,
	* logger movie clip, and removing any listeners.
	*/
	public function destroy():Void{
		//Remove the chart first
		this.remove();
		//Remove the chart movie clip
		cMC.removeMovieClip();
		//Destroy the logger
		this.lgr.destroy();
		//Destroy tool tip
		this.tTip.destroy();
		//Remove tool tip movie clip
		this.ttMC.removeMovieClip();
		//Remove test text field movie clip
		this.tfTestMC.removeMovieClip();
		//Remove logger movie clip
		this.logMC.removeMovieClip();		
	}
	// -------------------- UTILITY METHODS --------------------//	
	/**
	* returnDataAsElement method returns the data passed to this
	* method as an Element Object. We store each chart element as an
	* obejct to unify the various properties.
	*	@param	x		Start X of the element
	*	@param	y		Start Y of the element
	*	@param	w		Width of the element
	*	@param	h		Height of the element
	*	@return		Object representing the element
	*/
	private function returnDataAsElement (x:Number, y:Number, w:Number, h:Number):Object{
		//Create new object
		var element:Object = new Object();
		element.x = x;
		element.y = y;
		element.w = w;
		element.h = h;
		//Calculate and store toX and toY
		element.toX = x + w;
		element.toY = y + h;
		//Return
		return element;
	}		
	/**
	* renderAppMessage method helps display an application message to
	* end user.
	* @param	strMessage	Message to be displayed
	* @return				Reference to the text field created
	*/
	private function renderAppMessage (strMessage:String):TextField {
		return _global.createBasicText (strMessage, this.parentMC, depth, this.x + (this.width / 2) , this.y + (this.height / 2) , "Verdana", 10, "666666", "center", "bottom");
	}
	/**
	* removeAppMessage method removes the displayed application message
	* @param	tf	Text Field reference to the message
	*/
	private function removeAppMessage (tf:TextField){
		tf.removeTextField();
	}
	// --------------------- VISUAL RENDERING METHODS ------------------//
	/**
	* drawBackground method renders the chart background. The background
	* cant be solid color or gradient. All charts have a backround. So, we've
	* defined drawBackground in Chart class itself, so that sub classes can
	* directly access it (as it's common).
	*	@return		Nothing
	*/
	private function drawBackground():Void{
		//Create a new movie clip container for background
		var bgMC = this.cMC.createEmptyMovieClip("Background", this.dm.getDepth("BACKGROUND"));
		//Parse the color, alpha and ratio array
		var bgColor:Array = ColorExt.parseColorList(this.params.bgColor);
		var bgAlpha:Array = ColorExt.parseAlphaList(this.params.bgAlpha, bgColor.length);
		var bgRatio:Array = ColorExt.parseRatioList(this.params.bgRatio, bgColor.length);
		//Move to (-w/2, 0); - 0,0 registration point at center (x,y)
		bgMC.moveTo(-(this.width/2) , -(this.height / 2));
		//Create matrix object
		var matrix:Object = {matrixType:"box", w:this.width, h:this.height, x:- (this.width / 2) , y:- (this.height / 2) , r:MathExt.toRadians (this.params.bgAngle)};
		//If border is to be shown
		if (this.params.showBorder){
			bgMC.lineStyle (this.params.borderThickness, parseInt (this.params.borderColor, 16) , this.params.borderAlpha);
		}
		//Border thickness half
		var bth:Number = this.params.borderThickness/2;
		//Start the fill.
		bgMC.beginGradientFill ("linear", bgColor, bgAlpha, bgRatio, matrix);
		//Draw the rectangle with center registration point
		bgMC.lineTo(this.width/2-bth, -(this.height/2)+bth);
		bgMC.lineTo(this.width/2-bth, this.height/2-bth);
		bgMC.lineTo(-(this.width/2)+ bth, this.height/2-bth);
		bgMC.lineTo(-(this.width/2)+ bth, -(this.height/2)+bth);
		//Set the x and y position
		bgMC._x = this.width/2;
		bgMC._y = this.height/2;
		//End Fill
		bgMC.endFill();
		//Apply animation
		if (this.params.animation){
			this.styleM.applyAnimation(bgMC, this.objects.BACKGROUND, this.macro, bgMC._x, - this.width / 2, bgMC._y, - this.height / 2, 100, 100, 100, null);
		}
		//Apply filters
		this.styleM.applyFilters(bgMC, this.objects.BACKGROUND);
	}
	/**
	* loadBgSWF method loads the background .swf file (if required)
	*/
	private function loadBgSWF():Void{
		//We load the BG SWF only if it has been specified and it doesn't contain any colon characters
		//(to disallow XSS attacks)
		if (this.params.bgSWF != ""){
			if (this.params.bgSWF.indexOf(":")==-1 && this.params.bgSWF.indexOf("%3A")==-1){				
				//Create a movie clip container
				var bgSWFMC:MovieClip = this.cMC.createEmptyMovieClip ("BgSWF", this.dm.getDepth("BGSWF"));
				//Load the clip
				bgSWFMC.loadMovie (this.params.bgSWF);
				//Set alpha
				bgSWFMC._alpha = this.params.bgSWFAlpha;
			}else{
				this.log ("bgSWF not loaded", "The bgSWF path contains special characters like colon, which can be potentially dangerous in XSS attacks. As such, FusionCharts has not loaded the bgSWF. If you've specified the absolute path for bgSWF URL, we recommend specifying relative path under the same domain.", Logger.LEVEL.ERROR);
			}
		}
	}
	/**
	* drawClickURLHandler method draws the rectangle over the chart
	* that responds to click URLs. It draws only if clickURL has been
	* defined for the chart.
	*/
	private function drawClickURLHandler():Void{
		//Check if it needs to be created
		if (this.params.clickURL != ""){
			//Create a new movie clip container for background
			var clickMC = this.cMC.createEmptyMovieClip("ClickURLHandler", this.dm.getDepth ("CLICKURLHANDLER"));
			clickMC.moveTo (0, 0);
			//Set fill with 0 alpha
			clickMC.beginFill (0xffffff, 0);
			//Draw the rectangle
			clickMC.lineTo (this.width, 0);
			clickMC.lineTo (this.width, this.height);
			clickMC.lineTo (0, this.height);
			clickMC.lineTo (0, 0);
			//End Fill
			clickMC.endFill();
			clickMC.useHandCursor = true;
			//Set click handler
			var strLink:String = this.params.clickURL;
			var chartRef:Chart = this;
			clickMC.onMouseDown = function(){
				chartRef.invokeLink (strLink);
			}
			clickMC.onRollOver = function(){
				//Empty function just to show hand cursor
				//Necessary, else, it wouldn't show hand cursors
				//for areas that have other links on the chart.
			}
		}
	}
	/**
	 * invokeLink method of the chart is a re-direction function
	 * to Utils.invokeLink.
	 *	@param	strLink		Link to be invoked.
	*/
	private function invokeLink(strLink:String):Void{
		Utils.invokeLink(strLink, this);
	}
	// -------------- APIs for external consumption ------------------//
	/**
	 * isAnimated method indicates whether the chart is in animation mode.
	 *	@return		Whether the chart is in animation mode?
	*/
	public function isAnimated():Boolean{
		return this.params.animation;
	}
}
