(function (angular) {
    var MOSpecialistMember = function (QueryDAO, Mask, $timeout, GeneralUtil, $state) {
        var ctrl = this;
        ctrl.ecgChart;
        ctrl.ecgChart1;
        ctrl.ecgChart2;
        ctrl.showOptions = false

        ctrl.init = function () {
            console.log($state.params);
            ctrl.memberId = $state.params.id
            ctrl.fetchMemberDetails(ctrl.memberId)
            ctrl.fetchDatesForECG(ctrl.memberId)
        }

        ctrl.fetchMemberDetails = function (memberId) {
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_member_detail_for_specialist_role',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                if (res.result.length > 0) {
                    ctrl.memberDetails = res.result[0]
                    ctrl.memberAdditionalDetails = JSON.parse(ctrl.memberDetails.additional_info.replace(/\\/g, ''))
                }
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
            Mask.hide();
        }

        ctrl.fetchDatesForECG = function (memberId) {
            ctrl.ecgData = []
            Mask.show();
            QueryDAO.execute(
                {
                    code: 'fetch_ecg_dates_by_member',
                    parameters: {
                        memberId: Number(memberId)
                    }
                }
            ).then(function (res) {
                ctrl.ecgData = res.result
            }).catch((error) => {
                GeneralUtil.showMessageOnApiCallFailure(error);
            }).finally(function () {
                Mask.hide();
            });
        }

        ctrl.resetGraph = function () {
            Mask.show();
            ctrl.ecgChart.resetZoom();
            ctrl.ecgChart1.resetZoom();
            ctrl.ecgChart2.resetZoom();
            Mask.hide();
        }

        ctrl.printPdf = function () {
            ctrl.resetGraph();
            $timeout(function () {
                var reportPageHeight = $('#ecg-graph').innerHeight();
                var reportPageWidth = $('#ecg-graph').innerWidth();

                var pdfCanvas = $('<canvas />').attr({
                    id: "canvaspdf",
                    width: reportPageWidth,
                    height: reportPageHeight
                });

                var pdfctx = $(pdfCanvas)[0].getContext('2d');
                var pdfctxX = 0;
                var pdfctxY = 0;
                var buffer = 0;

                $("canvas").each(function (index) {
                    var canvasHeight = $(this).innerHeight();
                    var canvasWidth = $(this).innerWidth();
                    pdfctx.drawImage($(this)[0], pdfctxX, pdfctxY, canvasWidth, canvasHeight);
                    pdfctxX = 0;
                    pdfctxY += canvasHeight + buffer;

                });

                // create new pdf and add our new canvas as an image
                var pdf = new jsPDF('l', 'pt', [reportPageWidth, reportPageHeight]);
                pdf.addImage($(pdfCanvas)[0], 'PNG', 0, 0);

                // download the pdf
                pdf.save('filename.pdf');
            })

        }

        ctrl.showGraph = function () {
            if (ctrl.ecgDate != null) {
                Mask.show();
                QueryDAO.execute(
                    {
                        code: 'fetch_ecg_graph_data_by_id',
                        parameters: {
                            id: Number(ctrl.ecgDate)
                        }
                    }
                ).then(function (res) {
                    if (res.result.length > 0) {
                        ctrl.ecgGraphData = res.result[0]
                        var dataForGraph = []
                        var labels = []
                        if (ctrl.ecgGraphData.lead1_data != null) {
                            ctrl.ecgGraphData.lead1_data_array = ctrl.ecgGraphData.lead1_data.split(',')
                            ctrl.ecgGraphData.lead1_data_array.forEach((item, index) => {
                                labels.push(index)
                                dataForGraph.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.avr_data != null) {
                            ctrl.ecgGraphData.avr_data_array = ctrl.ecgGraphData.avr_data.split(',')
                            var buffer = dataForGraph.length
                            ctrl.ecgGraphData.avr_data_array.forEach((item, index) => {
                                labels.push(buffer + index)
                                dataForGraph.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v1_data != null) {
                            ctrl.ecgGraphData.v1_data_array = ctrl.ecgGraphData.v1_data.split(',')
                            var buffer = dataForGraph.length
                            ctrl.ecgGraphData.v1_data_array.forEach((item, index) => {
                                labels.push(buffer + index)
                                dataForGraph.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v4_data != null) {
                            ctrl.ecgGraphData.v4_data_array = ctrl.ecgGraphData.v4_data.split(',')
                            var buffer = dataForGraph.length
                            ctrl.ecgGraphData.v4_data_array.forEach((item, index) => {
                                labels.push(buffer + index)
                                dataForGraph.push(item)
                            })
                        }
                        var dataForGraph1 = []
                        var labels1 = []
                        if (ctrl.ecgGraphData.lead2_data != null) {
                            ctrl.ecgGraphData.lead2_data_array = ctrl.ecgGraphData.lead2_data.split(',')
                            ctrl.ecgGraphData.lead2_data_array.forEach((item, index) => {
                                labels1.push(index)
                                dataForGraph1.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.avl_data != null) {
                            ctrl.ecgGraphData.avl_data_array = ctrl.ecgGraphData.avl_data.split(',')
                            var buffer = dataForGraph1.length
                            ctrl.ecgGraphData.avl_data_array.forEach((item, index) => {
                                labels1.push(buffer + index)
                                dataForGraph1.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v2_data != null) {
                            ctrl.ecgGraphData.v2_data_array = ctrl.ecgGraphData.v2_data.split(',')
                            var buffer = dataForGraph1.length
                            ctrl.ecgGraphData.v2_data_array.forEach((item, index) => {
                                labels1.push(buffer + index)
                                dataForGraph1.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v5_data != null) {
                            ctrl.ecgGraphData.v5_data_array = ctrl.ecgGraphData.v5_data.split(',')
                            var buffer = dataForGraph1.length
                            ctrl.ecgGraphData.v5_data_array.forEach((item, index) => {
                                labels1.push(buffer + index)
                                dataForGraph1.push(item)
                            })
                        }
                        var dataForGraph2 = []
                        var labels2 = []
                        if (ctrl.ecgGraphData.lead3_data != null) {
                            ctrl.ecgGraphData.lead3_data_array = ctrl.ecgGraphData.lead3_data.split(',')
                            ctrl.ecgGraphData.lead3_data_array.forEach((item, index) => {
                                labels2.push(index)
                                dataForGraph2.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.avf_data != null) {
                            ctrl.ecgGraphData.avf_data_array = ctrl.ecgGraphData.avf_data.split(',')
                            var buffer = dataForGraph2.length
                            ctrl.ecgGraphData.avf_data_array.forEach((item, index) => {
                                labels2.push(buffer + index)
                                dataForGraph2.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v3_data != null) {
                            ctrl.ecgGraphData.v3_data_array = ctrl.ecgGraphData.v3_data.split(',')
                            var buffer = dataForGraph2.length
                            ctrl.ecgGraphData.v3_data_array.forEach((item, index) => {
                                labels2.push(buffer + index)
                                dataForGraph2.push(item)
                            })
                        }
                        if (ctrl.ecgGraphData.v6_data != null) {
                            ctrl.ecgGraphData.v6_data_array = ctrl.ecgGraphData.v6_data.split(',')
                            var buffer = dataForGraph2.length
                            ctrl.ecgGraphData.v6_data_array.forEach((item, index) => {
                                labels2.push(buffer + index)
                                dataForGraph2.push(item)
                            })
                        }
                        if (ctrl.ecgChart) {
                            ctrl.ecgChart.destroy()
                        }
                        ctrl.ecgChart = new Chart(document.getElementById('ecg'), {
                            type: 'line',
                            data: {},
                            options: {
                                responsive: true,
                                plugins: {
                                    legend: {
                                        display: false,
                                    },
                                    tooltip: {
                                        enabled: false
                                    },
                                    zoom: {
                                        pan: {
                                            enabled: true,
                                            mode: 'xy',
                                        },
                                        zoom: {
                                            enabled: true,
                                            // drag: true,
                                            mode: 'xy',
                                            wheel: {
                                                enabled: true,
                                            },
                                            pinch: {
                                                enabled: true
                                            },
                                        }
                                    },
                                },
                                scales: {
                                    x: {
                                        beginAtZero: true,
                                        ticks: {
                                            maxTicksLimit: 500,
                                            display: false,
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 5 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    },
                                    y: {
                                        // beginAtZero: true,
                                        ticks: {
                                            display: false,
                                            stepSize: 20
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 100 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    }
                                }
                            }
                        });
                        ctrl.ecgChart.data = {
                            labels: labels,
                            datasets: [{
                                data: dataForGraph,
                                lineTension: 0.5,
                                borderColor: "black",
                                borderWidth: 1,
                                pointRadius: 0
                            }]
                        }
                        ctrl.ecgChart.update();
                        if (ctrl.ecgChart1) {
                            ctrl.ecgChart1.destroy()
                        }
                        ctrl.ecgChart1 = new Chart(document.getElementById('ecg1'), {
                            type: 'line',
                            data: {},
                            options: {
                                responsive: true,
                                plugins: {
                                    legend: {
                                        display: false,
                                    },
                                    tooltip: {
                                        enabled: false
                                    },
                                    zoom: {
                                        pan: {
                                            enabled: true,
                                            mode: 'xy',
                                        },
                                        zoom: {
                                            enabled: true,
                                            // drag: true,
                                            mode: 'xy',
                                            wheel: {
                                                enabled: true,
                                            },
                                            // pinch: {
                                            //     enabled: true
                                            // },
                                        }
                                    }
                                },
                                scales: {
                                    x: {
                                        beginAtZero: true,
                                        ticks: {
                                            maxTicksLimit: 500,
                                            display: false,
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 5 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    },
                                    y: {
                                        // beginAtZero: true,
                                        ticks: {
                                            display: false,
                                            stepSize: 20
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 100 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    }
                                }
                            }
                        });
                        ctrl.ecgChart1.data = {
                            labels: labels1,
                            datasets: [{
                                data: dataForGraph1,
                                lineTension: 0.5,
                                borderColor: "black",
                                borderWidth: 1,
                                pointRadius: 0
                            }]
                        }
                        ctrl.ecgChart1.update();
                        if (ctrl.ecgChart2) {
                            ctrl.ecgChart2.destroy()
                        }
                        ctrl.ecgChart2 = new Chart(document.getElementById('ecg2'), {
                            type: 'line',
                            data: {},
                            options: {
                                responsive: true,
                                plugins: {
                                    legend: {
                                        display: false,
                                    },
                                    tooltip: {
                                        enabled: false
                                    },
                                    zoom: {
                                        pan: {
                                            enabled: true,
                                            mode: 'xy',
                                        },
                                        zoom: {
                                            enabled: true,
                                            // drag: true,
                                            mode: 'xy',
                                            wheel: {
                                                enabled: true,
                                            },
                                            pinch: {
                                                enabled: true
                                            },
                                        }
                                    },

                                },
                                scales: {
                                    x: {
                                        beginAtZero: true,
                                        ticks: {
                                            maxTicksLimit: 500,
                                            display: false,
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 5 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    },
                                    y: {
                                        // beginAtZero: true,
                                        ticks: {
                                            display: false,
                                            stepSize: 20
                                        },
                                        grid: {
                                            display: true,
                                            color: (context) => {
                                                if (context.tick.value % 100 === 0) {
                                                    return "red"
                                                } else {
                                                    return "#ff9980"
                                                }
                                            },
                                            drawBorder: true,
                                            drawOnChartArea: true
                                        }
                                    }
                                }
                            }
                        });
                        ctrl.ecgChart2.data = {
                            labels: labels2,
                            datasets: [{
                                data: dataForGraph2,
                                lineTension: 0.5,
                                borderColor: "black",
                                borderWidth: 1,
                                pointRadius: 0
                            }]
                        }
                        ctrl.ecgChart2.update();
                        Mask.hide()
                    }
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error);
                }).finally(function () {
                    Mask.hide();
                });

                ctrl.showOptions = true
            }
        }

        ctrl.init();
    };
    angular.module('imtecho.controllers').controller('MOSpecialistMember', MOSpecialistMember);
})(window.angular);