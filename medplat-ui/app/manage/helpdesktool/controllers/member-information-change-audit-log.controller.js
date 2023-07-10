(function () {
    function MemberInfoChangeAuditLogController
        (GeneralUtil, Mask, QueryDAO, DrTechoDAO, toaster) {
        var ctrl = this;

        ctrl.search = '';

        ctrl.listOfMemberAuditLogs = [];
        ctrl.pagingService = {
            offSet: 0,
            limit: 100,
            index: 0,
            allRetrieved: false,
            pagingRetrivalOn: false
        };

        ctrl.columnName = [
            { column: 'lmp_date', name: 'Lmp Date', display: true },
            { column: 'given_on', name: 'Immunisation Date', display: true },
            { column: 'service_date', name: 'Service Date', display: true },
            { column: 'state', name: 'Wpd Revert Service Record', display: false },
            { column: 'place_of_birth', name: 'Place Of Birth', display: true },
            { column: 'health_infrastructure_id', name: 'Delivery Place', display: true },
            { column: 'dob', name: 'Date Of Birth', display: true },
            { column: 'gender', name: 'Gender', display: true },
            { column: 'reg_date', name: 'Preg. Registration Date', display: true },
            { column: 'date_of_delivery', name: 'Date Of Delivery', display: true }
        ]

        ctrl.filteredColumn = ctrl.columnName[0];

        var retrieveAll = function () {
            if (!ctrl.pagingService.pagingRetrivalOn && !ctrl.pagingService.allRetrieved && ctrl.filteredColumn) {
                ctrl.pagingService.pagingRetrivalOn = true;
                setOffsetLimit();
                let queryDto;
                queryDto = {
                    code: 'member_information_change_audit_log',
                    parameters: {
                        limit: ctrl.pagingService.limit,
                        offset: ctrl.pagingService.offSet,
                        column: ctrl.filteredColumn.column,
                        search_text: ctrl.search
                    }
                };
                Mask.show();
                QueryDAO.execute(queryDto).then(function (resForData) {
                    if (resForData.result && (resForData.result.length === 0 || resForData.result.length < ctrl.pagingService.limit)) {
                        ctrl.pagingService.allRetrieved = true;
                    } else {
                        ctrl.pagingService.allRetrieved = false;
                    }
                    ctrl.listOfMemberAuditLogs = ctrl.listOfMemberAuditLogs.concat(resForData.result);
                }, function (err) {
                    GeneralUtil.showMessageOnApiCallFailure(err);
                    ctrl.pagingService.allRetrieved = true;
                }).finally(function () {
                    ctrl.pagingService.pagingRetrivalOn = false;
                    Mask.hide();
                });
            }
        };

        ctrl.searchData = function (reset) {
            if (reset) {
                ctrl.pagingService.index = 0;
                ctrl.pagingService.allRetrieved = false;
                ctrl.pagingService.pagingRetrivalOn = false;
                ctrl.listOfMemberAuditLogs = [];
            }
            retrieveAll();
        };

        var setOffsetLimit = function () {
            ctrl.pagingService.limit = 100;
            ctrl.pagingService.offSet = ctrl.pagingService.index * 100;
            ctrl.pagingService.index = ctrl.pagingService.index + 1;
        };

        var init = function () {
        }

        ctrl.downloadFile = function (id, title) {
            DrTechoDAO.downloadFile(id)
                .then(function (res) {
                    toaster.pop('success', 'File downloaded successfully!');
                    if (res.data !== null && navigator.msSaveBlob) {
                        return navigator.msSaveBlob(new Blob([res.data], { type: "application/octet-stream;charset=UTF-8'" }));
                    }
                    let a = $("<a style='display: none;'/>");
                    let url = window.URL.createObjectURL(new Blob([res.data], { type: "application/octet-stream;charset=UTF-8'" }));
                    a.attr("href", url);
                    a.attr("download", title);
                    $("body").append(a);
                    a[0].click();
                    window.URL.revokeObjectURL(url);
                    a.remove();
                }, function (err) {
                    if (err.status === 404) {
                        toaster.pop({
                            type: 'error',
                            title: 'Operation unsuccessful!',
                            body: 'File not found!'
                        });
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: 'Operation unsuccessful!',
                            body: 'Something went wrong, try again later!'
                        });
                    }
                })
        };
        init();
    }
    angular.module('imtecho.controllers').controller('MemberInfoChangeAuditLogController', MemberInfoChangeAuditLogController);
})();