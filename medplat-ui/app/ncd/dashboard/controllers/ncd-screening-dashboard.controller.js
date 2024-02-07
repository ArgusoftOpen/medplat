(function () {
    function NcdScreeningDashboardController(QueryDAO) {
        var ncdScreeningDashboard = this;
        ncdScreeningDashboard.lineChart = {};
        ncdScreeningDashboard.lineChart.bars = [];
        ncdScreeningDashboard.lineChart.colors = [];
        ncdScreeningDashboard.basketColors = ['#803690', '#00ADF9', '#46BFBD', '#FDB45C', '#6600ff', '#86b300', '#ff0000', '#000099', '#00cccc'];
        ncdScreeningDashboard.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left',
                        ticks: {
                            beginAtZero: true,
                            userCallback: function (label, index, labels) {
                                if (Math.floor(label) === label) {
                                    return label;
                                }
                            },
                        }
                    }
                ]
            }
        };
        ncdScreeningDashboard.lineChart.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
        ncdScreeningDashboard.lineChart.series = ['Series A'];
        ncdScreeningDashboard.lineChart.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                ]
            }
        };
        ncdScreeningDashboard.init = function () {
            QueryDAO.execute({
                code: 'ncd_current_count'
            }).then(function (res) {
                ncdScreeningDashboard.baskets = [];
                ncdScreeningDashboard.labels = [];
                ncdScreeningDashboard.bars = [];
                ncdScreeningDashboard.colors = [];
                ncdScreeningDashboard.basketNames = (Object.keys(res.result[0]));
                ncdScreeningDashboard.basketNames.forEach(function (basketName, index) {
                    var color = ncdScreeningDashboard.basketColors[index]
                    ncdScreeningDashboard.baskets.push({
                        name: basketName,
                        count: res.result[0][basketName],
                        colorCode: color
                    })
                    ncdScreeningDashboard.labels.push(basketName);
                    ncdScreeningDashboard.bars.push(res.result[0][basketName])
                    ncdScreeningDashboard.colors.push(color);
                });
            })
        }
        ncdScreeningDashboard.init();
    }
    angular.module('imtecho.controllers').controller('NcdScreeningDashboardController', NcdScreeningDashboardController);
})();
