(function () {
    function DashboardController(Mask, QueryDAO, GeneralUtil ,DASHBOARDConst, $http) {
        var dashboardCtrl = this;
        dashboardCtrl.duration = DASHBOARDConst.duration;
        dashboardCtrl.indicators = DASHBOARDConst.indicators;
        dashboardCtrl.order = "asc";
        dashboardCtrl.orderBy = "";
        // dashboardCtrl.pagingService = PagingService.initialize();
        dashboardCtrl.selectedFilterIndicator = dashboardCtrl.indicators[0];
        var NAME_MAPPING_MAP = {
            'Jam Kandorna': 'Jamkandorna',
            'Vinchhiya': 'Vinchchiya',
            'Kotda Sangani': 'Kotada sangani',
            'Gir Somnath': 'Gir Somnath',
            'Kachchh': 'Kutch',
            'Mahesana': 'mehsana'
        };

        var CORPORATION = [
            {
                name: "Rajkot Corporation",
                location: {
                    latitude: 22.2757,
                    longitude: 70.8070
                }
            },
            {
                name: "Bhavnagar Corporation",
                location: {
                    latitude: 21.7716,
                    longitude: 72.1458
                }
            },
            {
                name: "Ganghinagar Corporation",
                location: {
                    latitude: 23.237560,
                    longitude: 72.647781
                }
            },
            {
                name: "Junagadh Corporation",
                location: {
                    latitude: 21.5190,
                    longitude: 70.4598
                }
            },
            {
                name: "Surat Corporation",
                location: {
                    latitude: 21.170240,
                    longitude: 72.831062
                }
            },
            {
                name: "Vadodara Corporation",
                location: {
                    latitude: 22.310696,
                    longitude: 73.192635
                }
            },
            {
                name: "Ahmedabad Corporation",
                location: {
                    latitude: 23.033863,
                    longitude: 72.585022
                }
            },
            {
                name: "Jamnagar Corporation",
                location: {
                    latitude: 22.4687,
                    longitude: 70.0674
                }
            }
        ];

        dashboardCtrl.chartOptions = {
            legend: {
                display: true
            },
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: '(% of population cover)'
                    }
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: '(Location)'
                    }
                }]
            },
            plugins: {
                labels: {
                    render: function () {
                        return '';
                    }
                }
            },
            responsive: true,
            maintainAspectRatio: false,
        };

        dashboardCtrl.pieChartData = {};
        dashboardCtrl.pieChartData.labels = [];
        dashboardCtrl.pieChartData.countData = [];
        dashboardCtrl.barChartData = {};
        dashboardCtrl.barChartData.labels = [];
        dashboardCtrl.series = ['IDSP Population coverage in % till date'];
        dashboardCtrl.barChartData.countData = [];

        dashboardCtrl.pieChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                display: true
            },
            plugins: {
                labels: [{
                    render: function (args) {
                        return '';
                    },
                    position: 'outside',
                }]
            }
        };

        dashboardCtrl.lineChartOptions = {
            legend: { display: true },
        };

        dashboardCtrl.selectedIndicator = "Population Indicators";
        dashboardCtrl.onChangeIndicator = function (indicator) {
            dashboardCtrl.selectedIndicator = indicator;
            dashboardCtrl.createScale(indicator.id);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis1', 30, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis2', 45, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis3', 40, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis4', 30, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis5', 45, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis6', 40, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis7', 30, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis8', 45, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis9', 40, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis10', 40, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis11', 40, 50);
            dashboardCtrl.createCustomAxis(indicator.id + 'Axis12', 40, 50);
        }

        dashboardCtrl.init = function () {
            dashboardCtrl.getChartData();
            dashboardCtrl.createLollipop();
            dashboardCtrl.createLineChart();
            dashboardCtrl.createParallelChart();
            //dashboardCtrl.createScale();
        };

        dashboardCtrl.lineChart = {};
        dashboardCtrl.lineChart.labels = ["January", "February", "March", "April", "May", "June", "July"];
        dashboardCtrl.lineChart.series = ['Sex Ratio', 'Sex Ration at Birth', 'MMR', 'IMR'];


        dashboardCtrl.createCustomAxis = function (id, scale1, scale2) {
            var margin = { top: 10, right: 30, bottom: 30, left: 0 },
                width = 150 - margin.left - margin.right,
                height = 50 - margin.top - margin.bottom;

            if (!!d3.select("#" + id)) {
                d3.select("#" + id).select("svg").remove();
            }

            var svg = d3.select("#" + id)
                .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom);

            // Create the scale
            var xscale = d3.scaleLinear()
                .domain([30, 50])         // This is what is written on the Axis: from 0 to 100
                .range([30, width]);

            var x_axis = d3.axisBottom()
                .scale(xscale).tickSize(8)
                .ticks(2);
            // Draw the axis
            svg
                .append("g")
                .attr('transform', "translate(" + margin.left + "," + margin.top + ")")      // This controls the vertical position of the Axis
                .call(x_axis);

            // Add 1 circle for the group B:
            svg
                .append("circle")
                .attr("cx", xscale(scale1))
                .attr("cy", 10)
                .attr("r", 8)
                .style("fill", "yellow")
                .attr("stroke", "yellow")

            svg
                .append("circle")
                .attr("cx", xscale(scale2))
                .attr("cy", 10)
                .attr("r", 8)
                .style("fill", "blue")
                .attr("stroke", "blue")

            svg.append('line')
                .style("stroke", "#ff8c00")
                .style("stroke-width", 10)
                .attr("x1", xscale(scale1 + 2))
                .attr("y1", margin.top)
                .attr("x2", xscale(scale2 - 2))
                .attr("y2", margin.top);
        }

        dashboardCtrl.createScale = function (id) {

            var data = [
                {
                    "name": "4.97",
                    "value": 4.97
                },
                {
                    "name": "-2.20",
                    "value": -2.20
                },
                {
                    "name": "3",
                    "value": 3
                },

            ];

            if (!!d3.select("#" + id)) {
                d3.select("#" + id).select("svg").remove();
            }

            var margin = { top: 10, right: 30, bottom: 30, left: 40 },
                width = 200 - margin.left - margin.right,
                height = 200 - margin.top - margin.bottom;
            var x = d3.scaleLinear()
                .domain(d3.extent(data, d => d.value))
                .range([0, width]);

            var y = d3.scaleBand()
                .domain(data.map(d => d.name))
                .rangeRound([0, height])
                .padding(0.2);

            var xAxis = d3.axisBottom(x);

            var yAxis = d3.axisLeft(y)
                .tickSize(0);

            var svg = d3.select("#" + id)
                .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom);

            var chart = svg.append('g')
                .attr('transform', "translate(" + margin.left + "," + margin.top + ")");

            var bar = chart.selectAll('.bar')
                .data(data)
                .enter().append('rect')
                .attr('class', d => `bar ${d.value < 0 ? 'negative' : 'positive'}`)
                .attr('x', d => x(Math.min(0, d.value)))
                .attr('y', d => y(d.name))
                .attr('width', d => Math.abs(x(d.value) - x(0)))
                .attr('height', y.bandwidth());

            chart.append('g')
                .attr('class', 'axis y')
                .attr('transform', `translate(${x(0)}, 0)`)
                .call(yAxis);
        }

        dashboardCtrl.createLineChart = function () {
            dashboardCtrl.lineChart.data = [
                [10, 20, 5, 1, 80, 200, 500],
                [100, 9, 5, 80, 60, 2, 10],
                [100, 98, 54, 8, 600, 20, 50],
                [10, 500, 800, 15, 60, 2, 200],
            ];

            var data = [
                {
                    name: "Gandhinagar",
                    values: [
                        { date: "2000", price: "100" },
                        { date: "2001", price: "110" },
                        { date: "2002", price: "145" },
                        { date: "2003", price: "241" },
                        { date: "2004", price: "101" },
                        { date: "2005", price: "90" },
                        { date: "2006", price: "10" },
                        { date: "2007", price: "35" },
                        { date: "2008", price: "21" },
                        { date: "2009", price: "201" }
                    ]
                },
                {
                    name: "Vadodara",
                    values: [
                        { date: "2000", price: "200" },
                        { date: "2001", price: "120" },
                        { date: "2002", price: "33" },
                        { date: "2003", price: "21" },
                        { date: "2004", price: "51" },
                        { date: "2005", price: "190" },
                        { date: "2006", price: "120" },
                        { date: "2007", price: "85" },
                        { date: "2008", price: "221" },
                        { date: "2009", price: "101" }
                    ]
                },
                {
                    name: "Morbi",
                    values: [
                        { date: "2000", price: "50" },
                        { date: "2001", price: "10" },
                        { date: "2002", price: "5" },
                        { date: "2003", price: "71" },
                        { date: "2004", price: "20" },
                        { date: "2005", price: "9" },
                        { date: "2006", price: "220" },
                        { date: "2007", price: "235" },
                        { date: "2008", price: "61" },
                        { date: "2009", price: "10" }
                    ]
                }
            ];
            var margin = { top: 10, right: 30, bottom: 90, left: 40 },
                width = 500 - margin.left - margin.right,
                height = 200 - margin.top - margin.bottom;
            var duration = 250;

            var lineOpacity = "0.25";
            var lineOpacityHover = "0.85";
            var otherLinesOpacityHover = "0.1";
            var lineStroke = "1.5px";
            var lineStrokeHover = "2.5px";

            var circleOpacity = '0.85';
            var circleOpacityOnLineHover = "0.25"
            var circleRadius = 3;
            var circleRadiusHover = 6;


            /* Format Data */
            var parseDate = d3.timeParse("%Y");
            data.forEach(function (d) {
                d.values.forEach(function (d) {
                    d.date = parseDate(d.date);
                    d.price = +d.price;
                });
            });


            /* Scale */
            var xScale = d3.scaleTime()
                .domain(d3.extent(data[0].values, d => d.date))
                .range([0, width]);

            var yScale = d3.scaleLinear()
                .domain([0, d3.max(data[0].values, d => d.price)])
                .range([height, 0]);

            var color = d3.scaleOrdinal(d3.schemeCategory10);

            /* Add SVG */
            var svg = d3.select("#lineChart").append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .append('g')
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


            /* Add line into SVG */
            var line = d3.line()
                .x(d => xScale(d.date))
                .y(d => yScale(d.price));

            let lines = svg.append('g')
                .attr('class', 'lines');

            lines.selectAll('.line-group')
                .data(data).enter()
                .append('g')
                .attr('class', 'line-group')
                .on("mouseover", function (d, i) {
                    svg.append("text")
                        .attr("class", "title-text")
                        .style("fill", color(i))
                        .text(d.name)
                        .attr("text-anchor", "middle")
                        .attr("x", (width) / 2)
                        .attr("y", 5);
                })
                .on("mouseout", function (d) {
                    svg.select(".title-text").remove();
                })
                .append('path')
                .attr('class', 'line')
                .attr('d', d => line(d.values))
                .style('stroke', (d, i) => color(i))
                .style('opacity', lineOpacity)
                .on("mouseover", function (d) {
                    d3.selectAll('.line')
                        .style('opacity', otherLinesOpacityHover);
                    d3.selectAll('.circle')
                        .style('opacity', circleOpacityOnLineHover);
                    d3.select(this)
                        .style('opacity', lineOpacityHover)
                        .style("stroke-width", lineStrokeHover)
                        .style("cursor", "pointer");
                })
                .on("mouseout", function (d) {
                    d3.selectAll(".line")
                        .style('opacity', lineOpacity);
                    d3.selectAll('.circle')
                        .style('opacity', circleOpacity);
                    d3.select(this)
                        .style("stroke-width", lineStroke)
                        .style("cursor", "none");
                });


            /* Add circles in the line */
            lines.selectAll("circle-group")
                .data(data).enter()
                .append("g")
                .style("fill", (d, i) => color(i))
                .selectAll("circle")
                .data(d => d.values).enter()
                .append("g")
                .attr("class", "circle")
                .on("mouseover", function (d) {
                    d3.select(this)
                        .style("cursor", "pointer")
                        .append("text")
                        .attr("class", "text")
                        .text(`${d.price}`)
                        .attr("x", d => xScale(d.date) + 5)
                        .attr("y", d => yScale(d.price) - 10);
                })
                .on("mouseout", function (d) {
                    d3.select(this)
                        .style("cursor", "none")
                        .transition()
                        .duration(duration)
                        .selectAll(".text").remove();
                })
                .append("circle")
                .attr("cx", d => xScale(d.date))
                .attr("cy", d => yScale(d.price))
                .attr("r", circleRadius)
                .style('opacity', circleOpacity)
                .on("mouseover", function (d) {
                    d3.select(this)
                        .transition()
                        .duration(duration)
                        .attr("r", circleRadiusHover);
                })
                .on("mouseout", function (d) {
                    d3.select(this)
                        .transition()
                        .duration(duration)
                        .attr("r", circleRadius);
                });


            /* Add Axis into SVG */
            var xAxis = d3.axisBottom(xScale).ticks(5);
            var yAxis = d3.axisLeft(yScale).ticks(5);

            svg.append("g")
                .attr("class", "x axis")
                .attr("transform", `translate(0, ${height})`)
                .call(xAxis);

            svg.append("g")
                .attr("class", "y axis")
                .call(yAxis)
                .append('text')
                .attr("y", 15)
                .attr("transform", "rotate(-90)")
                .attr("fill", "#000")
                .text("Total values");
            svg.append("circle").attr("cx", 0).attr("cy", 160).attr("r", 6).style("fill", "orange")
            svg.append("circle").attr("cx", 220).attr("cy", 160).attr("r", 6).style("fill", "blue")
            svg.append("circle").attr("cx", 0).attr("cy", 180).attr("r", 6).style("fill", "green")
            svg.append("text").attr("x", 10).attr("y", 160).text("Sex Ratio of Total Population	").style("font-size", "15px").attr("alignment-baseline", "middle")
            svg.append("text").attr("x", 230).attr("y", 160).text("Sex Ratio at Birth").style("font-size", "15px").attr("alignment-baseline", "middle")
            svg.append("text").attr("x", 10).attr("y", 180).text("Females above 6 who attended school (%)").style("font-size", "15px").attr("alignment-baseline", "middle")

        }

        dashboardCtrl.createParallelChart = function () {

            const data = [
                {
                    "territory": "Morabi",
                    "quarter": "Q1 2013",
                    "profit": 41119.6
                },
                {
                    "territory": "Morabi",
                    "quarter": "Q2 2013",
                    "profit": 36771.95
                },
                {
                    "territory": "Gandhinagar",
                    "quarter": "Q1 2013",
                    "profit": 121434.25
                },
                {
                    "territory": "Gandhinagar",
                    "quarter": "Q2 2013",
                    "profit": 159530.8
                },
                {
                    "territory": "Baroda",
                    "quarter": "Q1 2013",
                    "profit": 295160.15
                },
                {
                    "territory": "Baroda",
                    "quarter": "Q2 2013",
                    "profit": 444106.95
                },
                {
                    "territory": "Ahmedabad",
                    "quarter": "Q1 2013",
                    "profit": 556678.6
                },
                {
                    "territory": "Ahmedabad",
                    "quarter": "Q2 2013",
                    "profit": 684290.8
                },
            ];

            const chartData = [
                [
                    {
                        "rank": 8,
                        "profit": 41119.6,
                        "next": {
                            "rank": 8,
                            "profit": 36771.95,
                            "next": null
                        }
                    },
                    {
                        "rank": 8,
                        "profit": 36771.95,
                        "next": null
                    }
                ],
                [{
                    "rank": 7,
                    "profit": 121434.25,
                    "next": {
                        "rank": 6,
                        "profit": 159530.8,
                        "next": null
                    }
                },
                {
                    "rank": 6,
                    "profit": 159530.8,
                    "next": null
                }
                ],
                [
                    {
                        "rank": 3,
                        "profit": 295160.15,
                        "next": {
                            "rank": 1,
                            "profit": 444106.95,
                            "next": null
                        }
                    },
                    {
                        "rank": 1,
                        "profit": 444106.95,
                        "next": null
                    }

                ],
                [
                    {
                        "rank": 0,
                        "profit": 556678.6,
                        "next": {
                            "rank": 0,
                            "profit": 684290.8,
                            "next": null
                        }
                    },
                    {
                        "rank": 0,
                        "profit": 684290.8,
                        "next": null
                    }
                ],
            ];
            // set the dimensions and margins of the graph
            let margin = { top: 30, right: 70, bottom: 30, left: 70 },
                width = 500 - margin.left - margin.right,
                height = 250 - margin.top - margin.bottom;

            let padding = 25;

            let quarters = ["Base Year Rank", "Reference Year Rank"];
            // let y = d3.scalePoint()
            //     .range([margin.top, height - margin.bottom - padding]);


            let ranking = [
                {
                    "territory": "Southeast",
                    "first": 0,
                    "last": 0
                },
                {
                    "territory": "Mideast",
                    "first": 1,
                    "last": 2
                },
                {
                    "territory": "Southwest",
                    "first": 2,
                    "last": 3
                },
                {
                    "territory": "Baroda",
                    "first": 3,
                    "last": 4
                },
                {
                    "territory": "Great Lakes",
                    "first": 4,
                    "last": 1
                },
                {
                    "territory": "Far West",
                    "first": 5,
                    "last": 5
                },
                {
                    "territory": "New England",
                    "first": 6,
                    "last": 7
                },
                {
                    "territory": "Gandhinagar",
                    "first": 7,
                    "last": 6
                },
                {
                    "territory": "Morabi",
                    "first": 8,
                    "last": 8
                }
            ]

            let left = ["Ahmedabad", "Baroda", "Gandhinagar", "Morabi"];
            let right = ["Ahmedabad", "Baroda", "Gandhinagar", "Morabi"];

            const territories = ["Morabi", "Gandhinagar", "Baroda", "Ahmedabad"];
            const seq = (start, length) => Array.apply(null, { length: length }).map((d, i) => i + start);

            const color = d3.scaleOrdinal(d3.schemeTableau10)
                .domain(seq(0, ranking.length));

            const bx = d3.scalePoint()
                .domain(seq(0, quarters.length))
                .range([0, width - margin.left - margin.right - padding * 2]);

            const by = d3.scalePoint()
                .domain(seq(0, ranking.length))
                .range([margin.top, height - margin.bottom - padding])

            const toCurrency = (num) => d3.format("$,.2f")(num)

            const title = g => g.append("title")
                .text((d, i) => `${d.territory} - ${quarters[i]}\nRank: ${d.profit.rank + 1}\nProfit: ${toCurrency(d.profit.profit)}`)

            function highlight(e, d) {
                this.parentNode.appendChild(this);
                series.filter(s => s !== d)
                    .transition().duration(500)
                    .attr("fill", "#ddd").attr("stroke", "#ddd");
                markTick(leftY, 0);
                markTick(rightY, quarters.length - 1);

                function markTick(axis, pos) {
                    axis.selectAll(".tick text").filter((s, i) => i === d[pos].rank)
                        .transition().duration(500)
                        .attr("font-weight", "bold")
                        .attr("fill", color(d[0].rank));
                }
            }

            function restore() {
                series.transition().duration(500)
                    .attr("fill", s => color(s[0].rank)).attr("stroke", s => color(s[0].rank));
                restoreTicks(leftY);
                restoreTicks(rightY);

                function restoreTicks(axis) {
                    axis.selectAll(".tick text")
                        .transition().duration(500)
                        .attr("font-weight", "normal").attr("fill", "black");
                }
            }


            const svg = d3.select("#charParallel")
                .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .attr("cursor", "default")
                .attr("viewBox", [0, 0, width, height]);

            svg.append("g")
                .attr("transform", `translate(${margin.left + 25},0)`)
                .selectAll("path")
                .data(seq(0, quarters.length))
                .join("path")
                .attr("stroke", "#ccc")
                .attr("stroke-width", 2)
                .attr("stroke-dasharray", "5,5")
                .attr("d", d => d3.line()([[bx(d), 0], [bx(d), height - margin.bottom]]));

            const series = svg.selectAll(".series")
                .data(chartData)
                .join("g")
                .attr("class", "series")
                .attr("opacity", 1)
                .attr("fill", d => color(d[0].rank))
                .attr("stroke", d => color(d[0].rank))
                .attr("transform", `translate(${margin.left + padding},0)`)
            //    .on("mouseover", highlight)
            //  .on("mouseout", restore);

            series.selectAll("path")
                .data(d => d)
                .join("path")
                .attr("stroke-width", d3.scaleOrdinal()
                    .domain(["default"])
                    .range([5]))
                .attr("d", (d, i) => {
                    if (d.next)
                        return d3.line()([[bx(i), by(d.rank)], [bx(i + 1), by(d.next.rank)]]);
                });

            const bumps = series.selectAll("g")
                .data((d, i) => d.map(v => ({ territory: territories[i], profit: v, first: d[0].rank })))
                .join("g")
                .attr("transform", (d, i) => `translate(${bx(i)},${by(d.profit.rank)})`)
                // .call(g => g.append("title").text((d, i) => `${d.territory} - ${quarters[i]}\n${d.profit.profit}`));
                .call(title);

            bumps.append("circle").attr("r", 5);
            bumps.append("text")
                .attr("dy", "0.35em")
                .attr("fill", "white")
                .attr("stroke", "none")
                .attr("text-anchor", "middle")
                .style("font-weight", "bold")
                .style("font-size", "5px")
                .text(d => d.profit.rank + 1);

            const drawAxis = (g, x, y, axis, domain) => {
                g.attr("transform", `translate(${x},${y})`)
                    .call(axis)
                    .selectAll(".tick text")
                    .attr("font-size", "8px");

                if (!domain) g.select(".domain").remove();
            }

            const ax = d3.scalePoint()
                .domain(quarters)
                .range([margin.left + padding, width - margin.right - padding]);

            const y = d3.scalePoint()
                .range([margin.top, height - margin.bottom - padding]);

            svg.append("g").call(g => drawAxis(g, 0, height - margin.top - margin.bottom + 13, d3.axisBottom(ax), true));
            var leftY = svg.append("g").call(g => drawAxis(g, margin.left, 0, d3.axisLeft(y.domain(left))));
            var rightY = svg.append("g").call(g => drawAxis(g, width - margin.right, 0, d3.axisRight(y.domain(right))));

            return svg.node();

        }

        dashboardCtrl.createLollipop = function () {

            var margin = { top: 10, right: 30, bottom: 90, left: 40 },
                width = 500 - margin.left - margin.right,
                height = 250 - margin.top - margin.bottom;
            // append the svg object to the body of the page
            var svg = d3.select("#my_dataviz")
                .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform",
                    "translate(" + margin.left + "," + margin.top + ")");

            // Parse the Data
            var data = [
                {
                    Country: "Narmada",
                    Value1: 80,
                    Value2: 60,
                    Value3: 70,
                },
                {
                    Country: "Surendranagar",
                    Value1: 55,
                    Value2: 45,
                    Value3: 60,
                },
                {
                    Country: "Navsari",
                    Value1: 55,
                    Value2: 45,
                    Value3: 60,
                },
                {
                    Country: "Porbandar",
                    Value1: 55,
                    Value2: 45,
                    Value3: 60,
                },
                {
                    Country: "Gandhinagar",
                    Value1: 55,
                    Value2: 45,
                    Value3: 10,
                },
                {
                    Country: "Vadodara",
                    Value1: 25,
                    Value2: 45,
                    Value3: 60,
                },
                {
                    Country: "Kutch",
                    Value1: 55,
                    Value2: 45,
                    Value3: 60,
                }
            ]
            // X axis
            var x = d3.scaleBand()
                .range([0, width])
                .domain(data.map(function (d) { return d.Country; }))
                .padding(1);
            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(d3.axisBottom(x))
                .selectAll("text")
                .attr("transform", "translate(-10,0)rotate(-45)")
                .style("text-anchor", "end");

            // Add Y axis
            var y = d3.scaleLinear()
                .domain([0, 100])
                .range([height, 0]);
            svg.append("g")
                .call(d3.axisLeft(y));

            // Lines
            svg.selectAll("myline")
                .data(data)
                .enter()
                .append("line")
                .attr("x1", function (d) { return x(d.Country); })
                .attr("x2", function (d) { return x(d.Country); })
                .attr("y1", function (d) { return y(d.Value); })
                .attr("y2", y(0))
                .attr("stroke", "grey")

            // Circles
            svg.selectAll("mycircle")
                .data(data)
                .enter()
                .append("circle")
                .attr("cx", function (d) { return x(d.Country); })
                .attr("cy", function (d) { return y(d.Value1); })
                .attr("r", "6")
                .style("fill", "yellow")
                .attr("stroke", "yellow")
            svg.selectAll("mycircle")
                .data(data)
                .enter()
                .append("circle")
                .attr("cx", function (d) { return x(d.Country); })
                .attr("cy", function (d) { return y(d.Value2); })
                .attr("r", "6")
                .style("fill", "blue")
                .attr("stroke", "blue")
            svg.selectAll("mycircle")
                .data(data)
                .enter()
                .append("circle")
                .attr("cx", function (d) { return x(d.Country); })
                .attr("cy", function (d) { return y(d.Value3); })
                .attr("r", "6")
                .style("fill", "orange")
                .attr("stroke", "orange")
            svg.append("circle").attr("cx", 0).attr("cy", 230).attr("r", 6).style("fill", "orange")
            svg.append("circle").attr("cx", 120).attr("cy", 230).attr("r", 6).style("fill", "yellow")
            svg.append("circle").attr("cx", 260).attr("cy", 230).attr("r", 6).style("fill", "blue")
            svg.append("text").attr("x", 10).attr("y", 230).text("Composite").style("font-size", "15px").attr("alignment-baseline", "middle")
            svg.append("text").attr("x", 130).attr("y", 230).text("State Average").style("font-size", "15px").attr("alignment-baseline", "middle")
            svg.append("text").attr("x", 270).attr("y", 230).text("Location Value").style("font-size", "15px").attr("alignment-baseline", "middle")


        }

        dashboardCtrl.getChartData = function () {
            dashboardCtrl.stateData1 = [{ "x_axis_label": "Bharuch", "series_label": 97.27, "color": "Green" }, { "x_axis_label": "Narmada", "series_label": 98.58, "color": "yellow" }, { "x_axis_label": "Valsad", "series_label": 98.11, "color": "Red" }, { "x_axis_label": "Gandhinagar", "series_label": 97.33, "color": "Yellow" }, { "x_axis_label": "Mahesana", "series_label": 99.70, "color": "Green" }, { "x_axis_label": "Gandhinagar Corporation", "series_label": 96.72, "color": "Green" }, { "x_axis_label": "Surendranagar", "series_label": 99.80, "color": "Red" }, { "x_axis_label": "Rajkot", "series_label": 97.76, "color": "yellow" }, { "x_axis_label": "Rajkot Corporation", "series_label": 96.07, "color": "Green" }, { "x_axis_label": "Patan", "series_label": 97.77, "color": "yellow" }, { "x_axis_label": "Mahisagar", "series_label": 97.00, "color": "Red" }, { "x_axis_label": "Navsari", "series_label": 100.55, "color": "Green" }, { "x_axis_label": "Panchmahal", "series_label": 98.23, "color": "Green" }, { "x_axis_label": "Kachchh", "series_label": 99.54, "color": "Green" }, { "x_axis_label": "Kheda", "series_label": 98.46, "color": "Red" }, { "x_axis_label": "Dang", "series_label": 96.12, "color": "Green" }, { "x_axis_label": "Porbandar", "series_label": 94.13, "color": "Yellow" }, { "x_axis_label": "Tapi", "series_label": 98.53, "color": "Red" }, { "x_axis_label": "Botad", "series_label": 97.01, "color": "Green" }, { "x_axis_label": "Morbi", "series_label": 99.86, "color": "Green" }, { "x_axis_label": "Chhota Udepur", "series_label": 98.85, "color": "Green" }, { "x_axis_label": "Arvalli", "series_label": 97.97, "color": "Yellow" }, { "x_axis_label": "Banaskantha", "series_label": 97.42, "color": "Red" }, { "x_axis_label": "Ahmedabad", "series_label": 98.02, "color": "Green" }, { "x_axis_label": "Ahmedabad Corporation", "series_label": 94.16, "color": "Yellow" }, { "x_axis_label": "Jamnagar", "series_label": 98.01, "color": "Green" }, { "x_axis_label": "Jamnagar Corporation", "series_label": 96.01, "color": "Red" }, { "x_axis_label": "Junagadh Corporation", "series_label": 93.57, "color": "Yellow" }, { "x_axis_label": "Junagadh", "series_label": 98.21, "color": "Green" }, { "x_axis_label": "Surat", "series_label": 97.58, "color": "Red" }, { "x_axis_label": "Surat Corporation", "series_label": 99.59, "color": "Green" }, { "x_axis_label": "Amreli", "series_label": 96.82, "color": "Green" }, { "x_axis_label": "Bhavnagar", "series_label": 97.14, "color": "Green" }, { "x_axis_label": "Vadodara", "series_label": 97.18, "color": "Yellow" }, { "x_axis_label": "Devbhumi Dwarka", "series_label": 101.21, "color": "Green" }, { "x_axis_label": "Bhavnagar Corporation", "series_label": 92.31, "color": "Yellow" }, { "x_axis_label": "Anand", "series_label": 97.73, "color": "Green" }, { "x_axis_label": "Gir Somnath", "series_label": 97.67, "color": "Red" }, { "x_axis_label": "Vadodara Corporation", "series_label": 96.70, "color": "Green" }, { "x_axis_label": "Sabarkantha", "series_label": 98.00, "color": "Green" }, { "x_axis_label": "Dahod", "series_label": 97.84, "color": "Yellow" }];
            $http.get('Gujarat-Topo.json').then(function (response) {
                dashboardCtrl.data = response.data;
                setBaseGrapth(dashboardCtrl.data);
            });
        };

        //Generating State Map

        var width = 236, height = 250;
        var projection = d3.geoMercator().scale(2);

        var path = d3.geoPath()
            .projection(projection)
            .pointRadius(2);
        dashboardCtrl.randomMapId = "chart123";
        document.getElementById(dashboardCtrl.randomMapId).innerHTML = "";   // clear previous chart

        var svg = d3.select("#" + dashboardCtrl.randomMapId).append("svg")
            .attr("width", width)
            .attr("height", height);

        var g = svg.append("g");

        dashboardCtrl.mapCode = "polygons";
        dashboardCtrl.mapPropertyName = "District";

        function setBaseGrapth(data) {
            var boundary = centerZoom(data);
            var subunits = drawSubUnits(data);
            //colorSubunits(subunits);
            // drawSubUnitLabels(data);
            // drawPlaces(data);
            // drawOuterBoundary(data, boundary);
            // setDataPoint();
        }

        function centerZoom(data) {
            var o = topojson.mesh(data, data.objects[dashboardCtrl.mapCode], function (a, b) {
                return a === b;
            });
            projection
                .scale(1)
                .translate([0, 0]);
            var b = path.bounds(o),
                s = 1 / Math.max((b[1][0] - b[0][0]) / width, (b[1][1] - b[0][1]) / height),
                t = [(width - s * (b[1][0] + b[0][0])) / 2, (height - s * (b[1][1] + b[0][1])) / 2];
            var p = projection
                .scale(s)
                .translate([(width - s * (b[1][0] + b[0][0])) / 2, (height - s * (b[1][1] + b[0][1])) / 2]);
            return o;
        }
        function getOpacity(scaledMin, scaledMax, num) {
            var max = Math.max.apply(Math, num);
            var min = Math.min.apply(Math, num);
            return num.map(num => (scaledMax - scaledMin) * (num - min) / (max - min) + scaledMin);
        }
        function updateOpacity(field) {
            let opacity = [];
            let index = 0;
            for (let d of dashboardCtrl.stateData1) {
                opacity[index] = index;
                index++;
            }
            opacity = getOpacity(0.1, 1, opacity);
            index = 0;
            for (let d of dashboardCtrl.stateData1) {
                d.opacity = opacity[index];
                index++;
            }
        }

        function drawSubUnits(data) {
            var tooltip = d3.select("body").append("div")
                .attr("class", "covid-19-tooltip")
                .style("opacity", 0);
            updateOpacity();

            var subunits = g.selectAll(".subunit")
                .data(topojson.feature(data, data.objects[dashboardCtrl.mapCode]).features)
                .enter().append("path")
                .attr("class", "subunit")
                .attr("d", path)
                .style("opacity", function (d) {
                    for (let stateData of dashboardCtrl.stateData1) {
                        if (d.properties[dashboardCtrl.mapPropertyName] && d.properties[dashboardCtrl.mapPropertyName].toLowerCase() == getMappingName(stateData.x_axis_label).toLowerCase()) {
                            if (stateData.opacity == 0) {
                                return 0.1;
                            } else {
                                return stateData.opacity;
                            }
                        }
                    }
                })
                .style("fill", function (d) {
                    // console.info(d.properties.ac_name);
                    for (let stateData of dashboardCtrl.stateData1) {
                        if (d.properties[dashboardCtrl.mapPropertyName] && d.properties[dashboardCtrl.mapPropertyName].toLowerCase() == getMappingName(stateData.x_axis_label).toLowerCase()) {
                            return stateData.color;
                        }
                    }
                })
                .style("stroke", "#fff")
                .style("stroke-width", "1px")
                .on("click", function (d) {
                    console.log(d.properties.District)
                    let districtname = d.properties.District
                    let element = dashboardCtrl.stateData1.find(state => districtname == state.x_axis_label);
                    // me.onSelectLocation(element)
                })
                .on('mouseover', function (d) {
                    element = dashboardCtrl.stateData1.find(state => d.properties.District == state.x_axis_label);
                    if (!!element) {
                        tooltip.transition()
                            .duration(200)
                            .style("opacity", .9);
                        tooltip.html(d.properties.District + ":" + element.series_label + "%")
                            .style("left", (d3.event.pageX) + "px")
                            .style("top", (d3.event.pageY - 28) + "px");
                    }
                })
                .on('mouseout', function () {
                    tooltip.transition()
                        .duration(500)
                        .style("opacity", 0);
                });
            // Set corporation
            g.selectAll()
                .data(CORPORATION)
                .enter().append("circle")
                .attr("r", 4)
                .style("stroke", "white")
                .attr("transform", function (d) {
                    return "translate(" + projection([
                        d.location.longitude,
                        d.location.latitude
                    ]) + ")";
                }).style("fill", function (d) {
                    for (let stateData of dashboardCtrl.stateData1) {
                        if (d.name.toLowerCase() == getMappingName(stateData.x_axis_label).toLowerCase()) {
                            return stateData.color;
                        }
                    }
                });
            return subunits;
        }

        function drawSubUnitLabels(data) {
            g.selectAll(".subunit-label")
                .data(topojson.feature(data, data.objects.polygons).features)
                .enter().append("text")
                .attr("class", "subunit-label")
                .attr("transform", function (d) {
                    return "translate(" + path.centroid(d) + ")";
                })
                .attr("dy", ".35em")
                .attr("text-anchor", "middle")
                .style("font-size", ".5em")
                .style("text-shadow", "0px 0px 2px #fff")
                .style("text-transform", "uppercase")

                .text(function (d) {
                    //return d.properties.st_nm;
                    return d.properties.District;
                });
        }

        function getMappingName(name) {
            return NAME_MAPPING_MAP[name] || name;
        }

        dashboardCtrl.toggleFilter = function () {
            if (angular.element('.filter-div').hasClass('active')) {
                angular.element('body').css("overflow", "auto");
            } else {
                angular.element('body').css("overflow", "hidden");
            }
            angular.element('.cst-backdrop').fadeToggle();
            angular.element('.filter-div').toggleClass('active');
        };

        dashboardCtrl.fetchUsers = function (id, filter) {
            // dashboardCtrl.pagingService.resetOffSetAndVariables();
            isPrintClicked = false;
            if (id === 'click') {
                dashboardCtrl.toggleFilter();
            } else {
                dashboardCtrl.filterId = id;
                dashboardCtrl.filter = filter;
                $('#search').blur();
            }
            dashboardCtrl.getUsersByCriteria(id, filter);
        };

        // dashboardCtrl.getUsersByCriteria = function (reset) {
        // dashboardCtrl.criteria = { roleid: null, locationId: null, searchString: null, orderby: null, order: dashboardCtrl.order, limit: 100, offset: 100,status: null};
        //     dashboardCtrl.criteria.orderby = dashboardCtrl.orderBy;
        //     var userList = dashboardCtrl.usersList;
        //     if (!reset) {
        //         userList = [];
        //         // dashboardCtrl.pagingService.resetOffSetAndVariables();
        //         isPrintClicked = false;
        //     }
        //     commonCriteria();
        //     if (dashboardCtrl.getFromState) {
        //         if ($state.current.userSelectedRoleId != null) {
        //             dashboardCtrl.criteria.roleid = $state.current.userSelectedRoleId;
        //             dashboardCtrl.getFromState = false;
        //         }
        //         if ($state.current.userSelectedLocationId) {
        //             dashboardCtrl.criteria.locationId = $state.current.userSelectedLocationId;
        //             dashboardCtrl.getFromState = false;
        //         }
        //         if ($state.current.userSearched != null && $state.current.userSearched.length > 1) {
        //             dashboardCtrl.searchText = $state.current.userSearched;
        //             dashboardCtrl.criteria.searchString = $state.current.userSearched;
        //             dashboardCtrl.getFromState = false;
        //         }
        //     }
        //     if (!isPrintClicked) {
        //         Mask.show();
        //         PagingService.getNextPage(QueryDAO.retrieveByCriteria, dashboardCtrl.criteria, userList, null).then(function (res) {
        //             dashboardCtrl.usersList = res;
        //             angular.forEach(dashboardCtrl.usersList, function (user) {
        //                 if (user.title) {
        //                     user.fullName = user.title + ' ' + user.firstName + ' ' + user.lastName;
        //                 } else {
        //                     user.fullName = user.firstName + ' ' + user.lastName;
        //                 }
        //                 if (typeof (user.areaOfIntervention) === 'string') {
        //                     user.areaOfInterventionToDisplay = $sce.trustAsHtml(user.areaOfIntervention);
        //                 } else if (!user.areaOfIntervention) {
        //                     user.areaOfInterventionToDisplay = $sce.trustAsHtml('N.A');
        //                 }
        //             });

        //         }, GeneralUtil.showMessageOnApiCallFailure).finally(function () {
        //             Mask.hide();
        //             if (dashboardCtrl.filter === 'search') {
        //                 $('#search').focus();
        //             }
        //         });
        //     }
        // };

        function commonCriteria() {
            if (dashboardCtrl.filter && dashboardCtrl.filterId) {
                if (dashboardCtrl.filter === 'sort') {
                    dashboardCtrl.criteria.orderby = dashboardCtrl.filterId;
                    dashboardCtrl.orderBy = dashboardCtrl.filterId;
                    if (dashboardCtrl.order === 'asc') {
                        dashboardCtrl.order = 'desc';
                        dashboardCtrl.criteria.order = 'desc';
                    } else {
                        dashboardCtrl.order = 'asc';
                        dashboardCtrl.criteria.order = 'asc';
                    }
                }
                if (dashboardCtrl.searchText && dashboardCtrl.searchText.length > 1) {
                    dashboardCtrl.criteria.searchString = dashboardCtrl.searchText;
                }
            }
            if (dashboardCtrl.selectedLocation && dashboardCtrl.selectedLocation.finalSelected) {
                var level = "level" + dashboardCtrl.selectedLocation.finalSelected.level;
                if (dashboardCtrl.selectedLocation[level] !== null) {
                    dashboardCtrl.criteria.locationId = dashboardCtrl.selectedLocation[level].id;
                } else {
                    if (dashboardCtrl.selectedLocation.finalSelected.level - 1 > 0) {
                        level = "level" + (dashboardCtrl.selectedLocation.finalSelected.level - 1);
                        dashboardCtrl.criteria.locationId = dashboardCtrl.selectedLocation[level].id;
                    }
                }
            }

            if (dashboardCtrl.selectedRole) {
                dashboardCtrl.criteria.roleid = dashboardCtrl.selectedRole.id;
            }
            if (dashboardCtrl.selectedStatus) {
                dashboardCtrl.criteria.status = dashboardCtrl.selectedStatus;
            }

        }

        dashboardCtrl.init();
        dashboardCtrl.onChangeIndicator(dashboardCtrl.indicators[0]);
    }
    angular.module('imtecho.controllers').controller('DashboardController', DashboardController);
})();
