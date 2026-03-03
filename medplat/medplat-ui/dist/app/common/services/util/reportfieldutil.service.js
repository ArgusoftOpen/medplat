(function (angular) {
    angular.module('imtecho.service').factory('ReportFieldUtil', function () {
        var reportFieldUtil = {};
        reportFieldUtil.extractDataFromField = function (containers, data) {
            data = {};
            _.each(containers, function (containerFields) {
                _.each(containerFields, function (field) {
                    if (field.value) {
                        if (field.fieldType === 'date' || field.fieldType === 'onlyMonth') {
                            data[field.fieldName] = (new Date(moment(field.value).format("MM/DD/YYYY")));
                        } else if (field.fieldType === 'dateNTime') {
                            data[field.fieldName] = (new Date(moment(field.value, "MM/DD/YYYY HH:mm:ss").format("YYYY-MM-DD HH:mm:ss")));
                        } else if (field.fieldType === 'dateRangePicker') {
                            data[field.fieldName] = { startDate: field.value.startDate };
                            data[field.fieldName].endDate = (new Date(moment(field.value.endDate, "MM/DD/YYYY HH:mm:ss").format("YYYY-MM-DD HH:mm:ss")));
                        }
                        else {
                            data[field.fieldName] = field.value;
                        }
                    }
                });
            });
            return data;
        };

        //data to send
        reportFieldUtil.extractFieldValueFromData = function (containers, data) {
            _.each(containers, function (containerFields) {
                _.each(containerFields, function (field) {
                    _.each(data, function (value, key) {
                        if (field.fieldName === key) {
                            if (field.fieldType === 'date' && field.value) {
                                field.value = moment(new Date(moment(value).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                            } else if (field.fieldType === 'dateNTime') {
                                field.value = moment((moment((value)).format("YYYY-MM-DD HH:mm:ss")), "YYYY-MM-DD HH:mm:ss").format("YYYY/MM/DD HH:mm:ss");
                            } else if (field.fieldType === 'onlyMonth') {
                                field.value = moment(new Date(moment(value).format("YYYY-MM-DD"))).format("MM/DD/YYYY");
                            }
                            else {
                                field.value = value;
                            }
                        }
                    });
                });
            });
            return containers;
        };

        reportFieldUtil.generateTableData = function (tableData, containers) {
            var thead = [], tbody = [];
            if (tableData && tableData.length > 0) {
                let tableColumns = _.keys(tableData[0]);
                _.each(tableColumns, function (column) {
                    var headPushed = false;
                    _.each(containers.tableFieldContainer, function (tableField) {
                        if (column == tableField.fieldName) {
                            thead.push(angular.copy(tableField));
                            headPushed = true
                        }
                    });
                    if (!headPushed) {
                        thead.push({ fieldName: column })
                    }
                });
                _.each(tableData, function (rowData) {
                    var rowObject = {};
                    _.each(_.keys(rowData), function (key) {
                        var columnData = _.find(thead, function (headObject) {
                            return headObject.fieldName === key;
                        });
                        if (columnData) {
                            if (columnData.customStyle) {
                                var rowFields = columnData.customStyle.match(/@(.*?)@/g);
                                if (rowFields) {
                                    _.each(rowFields, function (field) {
                                        columnData.customStyle = columnData.customStyle.replace(new RegExp(field), "row['" + field.substring(1, field.length - 1) + "'].displayValue");
                                    });
                                }
                            }
                            rowObject[key] = angular.copy(columnData);
                        }
                        angular.extend(rowObject[key], { displayValue: rowData[key] });
                    });
                    tbody.push(rowObject);
                });
            }
            return {
                header: thead, body: tbody
            };
        };
        return reportFieldUtil;
    });
})(window.angular);
