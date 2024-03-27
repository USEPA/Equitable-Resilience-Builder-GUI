var map = L.map("map");
map.setView([39.368, -99.409], 4);

var options = { attribution: "(c) OpenStreetMap Contributors" };

var layer = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', options)
layer.addTo(map);

var countyLayer;
var type = "single";
var facilitiesToAdd;
var leafletInteractiveArray;
var coloredSVGPathsArray = [];
var preSelectedIndexes = [];
var leafletInteractiveCollection;
var indexOfCountyWithContextMenu;

var data1 = data1;
var data2 = data2;

addKMLLayerToMap();
addGeocoderToMap();
addActionToSVGPathSelection();
checkForPreselectedIndexesInList();

map.on('zoomend', function(e) {
	writeToConsoleForJavaToRead("Zoom", map.getZoom());
	writeToConsoleForJavaToRead("Center:Lat", map.getCenter().lat);
	writeToConsoleForJavaToRead("Center:Lng", map.getCenter().lng);
});

document.getElementById("map").addEventListener('click', function (event){
	var longStringOfContent = getPopupInformation();
	if(longStringOfContent !== ""){
		var countyName = parsePopupContentForCountyName(longStringOfContent);
		var geoId = parsePopupContentForGeoId(longStringOfContent);
		
		writeToConsoleForJavaToRead("CountyName", countyName);
		writeToConsoleForJavaToRead("GEOID", geoId);
	}
	writeToConsoleForJavaToRead("Zoom", map.getZoom());
	writeToConsoleForJavaToRead("Center:Lat", map.getCenter().lat);
	writeToConsoleForJavaToRead("Center:Lng", map.getCenter().lng);
});

function getPopupInformation(){
	var longStringOfContent = "";
	var popup = document.getElementsByClassName("leaflet-popup-content");
	if (popup.length > 0){
	  if(popup.length == 1){
		longStringOfContent = popup[0].innerText;
	  }
	  if(popup.length ==2){
		longStringOfContent = popup[1].innerText;
	  }
	}
	return longStringOfContent;
}	

function checkForPreselectedIndexesInList(){
		if(preSelectedIndexes.length > 0){
			leafletInteractiveCollection = document.getElementsByClassName("leaflet-interactive");
			leafletInteractiveArray = Array.prototype.slice.call(leafletInteractiveCollection);
				for(var j =0; j < preSelectedIndexes.length; j++){
					var index = preSelectedIndexes[j];
					svgPathClicked(leafletInteractiveArray[index], index);
				}
		}
}

function addActionToSVGPathSelection(){
	leafletInteractiveCollection = document.getElementsByClassName("leaflet-interactive");
	leafletInteractiveArray = Array.prototype.slice.call(leafletInteractiveCollection);
	for(var i =0; i < leafletInteractiveArray.length; i++){
		var svgPath = leafletInteractiveArray[i];
			
		//Add on click to color the svgPath
		svgPath.onclick = (function(i) {return function() {
			var svgPath = leafletInteractiveArray[i];
			svgPathClicked(svgPath, i)
		};})(i);
		
		//Add on mouse over to show tornado diagram
		svgPath.onmouseover = (function(i) {return function() {
			var svgPath = leafletInteractiveArray[i];
			svgPathMouseOver(svgPath, i);
		};})(i);
		
		svgPath.onmouseout = (function(i) {return function() {
			var svgPath = leafletInteractiveArray[i];
			svgPathMouseOut(svgPath, i);
		};})(i);	
		
		(function(i){
			svgPath.addEventListener('contextmenu', function(event){
				event.preventDefault();
				svgPathRightClicked(i, event);
				},false);
		})(i);
	}
}

function svgPathClicked(svgPath, i){
	turnOffContextMenu();
	var fillColor = svgPath.attributes.fill.value;
	if(fillColor == "#9ecae1"){
		if(type == "single"){
			changeColoredSVGPathsBackToOriginalColor();
			coloredSVGPathsArray = [];
		}else if (type == "multi"){
			if(coloredSVGPathsArray.length == 2){
				resetSVGPath(coloredSVGPathsArray[1]);
				coloredSVGPathsArray.splice(1, 1);
			}
		}
		svgPath.setAttribute("fill", "b80000");
		coloredSVGPathsArray.push(svgPath);
		svgPath.isColored = true;
		writeToConsoleForJavaToRead("Index", i);
	}
    return svgPath;
}

function resetSVGPath(svgPath){
	svgPath.setAttribute("fill", "#9ecae1");
	svgPath.setAttribute("isColored", false);
}

function svgPathMouseOver(svgPath, i){
	if(svgPath.isColored == true){
		var closeButton = document.getElementsByClassName("leaflet-popup-close-button");
		if(closeButton[0] !== undefined){
			closeButton[0].click();
		}
		clearPopupChildren();
		var svgIndex = coloredSVGPathsArray.indexOf(svgPath);
		console.log("Mouse over = " + preSelectedIndexes[svgIndex]);
		if(svgIndex == 0){ createTornado(data1);}
		if(svgIndex == 1){ createTornado(data2);}
		var p = document.getElementById("popup");
		p.style.display = "block";
	}
}

function svgPathRightClicked(i, event){
	var svgPath = leafletInteractiveArray[i];
	if(svgPath.isColored == true){
		var closeButton = document.getElementsByClassName("leaflet-popup-close-button");
		if(closeButton[0] !== undefined){
			closeButton[0].click();
		}
		if(isDSTDataOn(data1)){
			indexOfCountyWithContextMenu = i;
			turnOnContextMenu(event, svgPath);
		}
	}
}

function turnOnContextMenu(event, svgPath){
	const contextMenu = document.getElementById("context-menu");
	const { clientX: mouseX, clientY: mouseY } = event;
	const { normalizedX, normalizedY } = normalizePozition(mouseX, mouseY, svgPath);
	contextMenu.style.top = `${normalizedY}px`;
	contextMenu.style.left = `${normalizedX}px`;
	contextMenu.classList.add("visible");
	contextMenu.addEventListener('mouseleave', e => {
		turnOffContextMenu();
	});
}

function turnOffContextMenu(){
	const contextMenu = document.getElementById("context-menu");
	contextMenu.classList.remove("visible");
}

function svgPathMouseOut(svgPath, i){
	if(svgPath.isColored == true){
		var p = document.getElementById("popup");
		p.style.display = "none";
		var svgIndex = coloredSVGPathsArray.indexOf(svgPath);
		console.log("Mouse out = " + preSelectedIndexes[svgIndex]);
	}
}

function pinGraphClicked(){
	turnOffContextMenu();
	writeToConsoleForJavaToRead("Pin graph", indexOfCountyWithContextMenu);
}

function reloadPageClicked(){
	turnOffContextMenu();
	window.location.reload();
}

function changeColoredSVGPathsBackToOriginalColor(){
	for(var i =0; i < coloredSVGPathsArray.length; i++){
		coloredSVGPathsArray[i].setAttribute("fill", "#9ecae1");
		coloredSVGPathsArray[i].setAttribute("isColored", false);
	}	
}	

function clearPopupChildren(){
	var p = document.getElementById("popup");
	while (p.firstChild) {
        p.removeChild(p.firstChild);
    }
}

function parsePopupContentForGeoId(stringToParse){
	var result;
	var match = stringToParse.match(/(GEOID)\t([0-9]*)\n/);
	result = match[2];
	return result;
}

function parsePopupContentForCountyName(stringToParse){
	var result;
	var match = stringToParse.match(/(NAME)\t([A-Za-z\s\W]*)\n/);
	result = match[2];
	return result;
}

function parsePopupContentForCountyFP(stringToParse){
	var result;
	var match = stringToParse.match(/(COUNTYFP)([0-9\s\W]*)\n/);
	result = match[2];
	return result;
}

function addGeocoderToMap(){
	L.Control.geocoder().addTo(map);
}

function addKMLLayerToMap(){
	//Read all facilities layers to be added to the map
	facilitiesToAdd = addToMap[0].facilities;
	facilitiesToAdd = facilitiesToAdd.split(",");
	for(var i =0; i < facilitiesToAdd.length; i++){
		var facilityType = facilitiesToAdd[i].trim();
		var xmlString = kml[0][facilityType];
		var xmlDoc = jQuery.parseXML(xmlString);
	
		//Add layer to map
		var kmlLayer = new L.KML(xmlDoc, {async: true});
		if(facilityType == "county"){
			countyLayer = kmlLayer;
			countyLayer.addEventListener('click', function (event){
				writeToConsoleForJavaToRead("County Lat", event.latlng.lat);
				writeToConsoleForJavaToRead("County Lng", event.latlng.lng);
			});
		}
		map.addLayer(kmlLayer);
	}
}  

function createTornado(data){
	if(isDSTDataOn(data)){
		for (var i in data) {
			var chart = tornadoChart(data)
			d3.select("#popup")
				.datum(data[i])
				.call(chart);
		}
	}else{
		var p = document.getElementById("popup");
		p.style.display = "none";
	}
}

function isDSTDataOn(data){
	if(data.LCIA.length > 0){
		return true;
	}else{
		return false;
	}
}
		
function writeToConsoleForJavaToRead(label, value){
	console.log(label + " = " + value);
}	

function tornadoChart(data) {

	var margin = {top: 20, right: 30, bottom: 40, left: 100},
    width = 325 - margin.left - margin.right,
    height = 225 - margin.top - margin.bottom;

	var x = d3.scale.linear()
		.range([0, width]);

	var y = d3.scale.ordinal()
		.rangeRoundBands([0, height], 0.1);

	var xAxis = d3.svg.axis()
		.scale(x)
		.orient("bottom")
		.ticks(7)

	var yAxis = d3.svg.axis()
		.scale(y)
		.orient("left")
		.tickSize(0)
	
	var svg = d3.select("#popup").append("svg")
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.style("background", "#FFFFFF")
		.append("g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	function chart(selection) {
		selection.each(function(data) {
				x.domain(d3.extent(data, function(d) { return d.value; })).nice();
				y.domain(data.map(function(d) { return d.label; }));

				var minInteractions = Math.min.apply(Math, data.map(function(o){return o.value;}))
				yAxis.tickPadding(Math.abs(x(minInteractions) - x(0)) + 10);

				var bar = svg.selectAll(".bar")
					.data(data)

				bar.enter().append("rect")
					.attr("class", function(d) { return "bar bar--" + (d.value < 0 ? "negative" : "positive"); })
					.attr("x", function(d) { return x(Math.min(0, d.value)); })
					.attr("y", function(d) { return y(d.label); })
					.attr("width", function(d) { return Math.abs(x(d.value) - x(0)); })
					.attr("height", y.rangeBand())

				bar.enter().append('text')
					.attr("text-anchor", "middle")
					.attr("x", function(d,i) {
						return x(Math.min(0, d.value)) + (Math.abs(x(d.value) - x(0)) / 2);
					})
					.attr("y", function(d,i) {
						return y(d.label) + (y.rangeBand() / 2);
					})
					.attr("dy", ".35em")
					.text(function (d) { return d.value; })

				svg.append("g")
					.attr("class", "x axis")
					.attr("transform", "translate(0," + height + ")")
					.call(xAxis);

				svg.append("g")
					.attr("class", "y axis")
					.attr("transform", "translate(" + x(0) + ",0)")
					.call(yAxis);
		});
	}
  return chart;
}

//https://itnext.io/how-to-create-a-custom-right-click-menu-with-javascript-9c368bb58724
const normalizePozition = (mouseX, mouseY, svgPath) => {
	
	// ? compute what is the mouse position relative to the container element (scope)
	const contextMenu = document.getElementById("context-menu");
	const {
		left: scopeOffsetX,
		top: scopeOffsetY,
	} = svgPath.getBoundingClientRect();

	const scopeX = mouseX - scopeOffsetX;
	const scopeY = mouseY - scopeOffsetY;
  
	// ? check if the element will go out of bounds
	var svgPathWidth = svgPath.getBoundingClientRect().width;
	var svgPathHeight = svgPath.getBoundingClientRect().height;
	
	var menuWidth = svgPath.clientWidth;
	var menuHeight = svgPath.clientHeight;
	
	const outOfBoundsOnX = scopeX + menuWidth > svgPathWidth;
	const outOfBoundsOnY = scopeY + menuHeight > svgPathHeight;
	
	let normalizedX = mouseX;
	let normalizedY = mouseY;
  
	// ? normalzie on X
	if (outOfBoundsOnX) {
		normalizedX = scopeOffsetX + svgPath.clientWidth - contextMenu.clientWidth;
	}
	// ? normalize on Y
	if (outOfBoundsOnY) {
		normalizedY = scopeOffsetY + svgPath.clientHeight - contextMenu.clientHeight;
	}
	
	return {normalizedX, normalizedY};
};


