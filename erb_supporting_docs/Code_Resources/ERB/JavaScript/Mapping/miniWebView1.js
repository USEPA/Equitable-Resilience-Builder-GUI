var data = data;

if(isDSTDataOn(data)){
	for (var i in data) {
		var chart = tornadoChart(data)
		d3.select("#popup")
			.datum(data[i])
			.call(chart);
	}
}

function isDSTDataOn(data){
	if(data.LCIA.length > 0){
		return true;
	}else{
		return false;
	}
}
		
function tornadoChart(data) {

	var margin = {top: 0, right: 7, bottom: 45, left: 80},
    width = 350 - margin.left - margin.right,
    height = 200 - margin.top - margin.bottom;

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



