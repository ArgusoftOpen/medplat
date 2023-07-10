(function (angular) {
    angular.module('imtecho.service').factory('SelectizeGenerator', function ($timeout, QueryDAO, GeneralUtil) {
        var generator = {};
        /**
         *
         * @param {Array} options
         * @param {Json} selectedFile
         * @param {Function} searchApiFunction - which should return promise
         * @returns {selectize config object}
         */
        generator.generateUserSelectize = function (roleIds) {
            generator.roleIds = roleIds;
            var selectize = {};
            selectize.config = {
                valueField: 'id',
                labelField: 'firstName',
                searchField: ['_searchField'],
                create: false,
                dropdownParent: 'body',
                highlight: true,
                maxItems: 1,
                render: {
                    item: function (user, escape) {
                        var returnString = "<div>" + user.firstName + " " + user.lastName + " (" + user.userName + ")" + "</div>";
                        return returnString;
                    },
                    option: function (user, escape) {
                        var returnString = "<div>" + user.firstName + " " + user.lastName + " (" + user.userName + ")" + "</div>";
                        return returnString;
                    }
                },
                onChange: function (value) {
                    if (value === '') {
                        this.clearOptions();
                        this.refreshOptions();
                    }
                },
                onFocus: function () {
                    this.onSearchChange("");
                },
                onBlur: function () {
                    let selectize1 = this;
                    let value = this.getValue();
                    $timeout(function () {
                        if (!value) {
                            selectize1.clearOptions();
                            selectize1.refreshOptions();
                        }
                    }, 200);
                },
                load: function (query, callback) {
                    let selectize1 = this;
                    let value = this.getValue();
                    if (!value) {
                        selectize1.clearOptions();
                        selectize1.refreshOptions();
                    }
                    let promise;
                    let queryDto;
                    if (generator.roleIds != null) {
                        queryDto = {
                            code: 'role_search_for_selectize',
                            parameters: {
                                searchString: query,
                                roleIds: generator.roleIds
                            }
                        };
                    } else {
                        queryDto = {
                            code: 'user_search_for_selectize',
                            parameters: {
                                searchString: query
                            }
                        };
                    }
                    promise = QueryDAO.execute(queryDto);
                    promise.then(function (res) {
                        angular.forEach(res.result, function (result) {
                            result._searchField = query;
                        });
                        callback(res.result);
                    }, function (err) {
                        GeneralUtil.showMessageOnApiCallFailure(err);
                        callback();
                    });
                }
            };
            return selectize;
        };

        generator.getOtherInstitute = function (otherInstitutes) {
            generator.otherInstitutes = otherInstitutes;
            var selectize = {};
            selectize.config = {
                create: false,
                valueField: 'id',
                labelField: 'hierarchy',
                highlight: true,
                searchField: ['_searchField'],
                maxItems: 1,
                render: {
                    item: function (hospital, escape) {
                        var returnString = "<div>" + hospital.name + "</div>";
                        return returnString;
                    },
                    option: function (hospital, escape) {
                        var returnString = "<div>" + hospital.name + "</div>";
                        return returnString;
                    }
                },
                onChange: function (value) {
                    if (value === '') {
                        this.clearOptions();
                        this.refreshOptions();
                    }
                },
                onFocus: function () {
                    this.onSearchChange("");
                },
                onBlur: function () {
                    let selectize1 = this;
                    let value = this.getValue();
                    setTimeout(function () {
                        if (!value) {
                            selectize1.clearOptions();
                            selectize1.refreshOptions();
                        }
                    }, 200);
                },
                load: function (query, callback) {
                    let selectize1 = this;
                    let value = this.getValue();
                    if (!value) {
                        selectize1.clearOptions();
                        selectize1.refreshOptions();
                    }
                    angular.forEach(generator.otherInstitutes, function (otherInstitute) {
                        otherInstitute._searchField = query;
                    });
                    let result = generator.otherInstitutes.filter(x => {
                        if (query) {
                            return x.name === query || (x.name_in_english && x.name_in_english.search(new RegExp(query, "i")) !== -1
                                || (x.name && x.name.search(new RegExp(query, "i")) !== -1));
                        } else {
                            return x;
                        }
                    })
                    callback(result);
                }
            }
            return selectize;
        }

        /* generator.generateMultipleUserSelectize = function (searchApiFunction, queryParams) {
            var selectize = {};
            selectize.config = {
                valueField: 'id',
                labelField: 'firstName',
                searchField: ['_searchField'],
                create: false,
                dropdownParent: ' ',
                highlight: true,
                render: {
                    item: function (user, escape) {
                        var returnString = "<div>" + user.firstName + " " + user.lastName + " (" + user.userName + ")" + "</div>";
                        return returnString;
                    },
                    option: function (user, escape) {
                        var returnString = "<div>" + user.firstName + " " + user.lastName + " (" + user.userName + ")" + "</div>";
                        return returnString;
                    }
                },
                onChange: function (value) {
                    if (value === '') {
                        this.clearOptions();
                        this.refreshOptions();
                    }
                },
                onFocus: function () {
                    this.onSearchChange("");
                },
                onBlur: function () {
                    var selectize = this;
                    var value = this.getValue();
                    $timeout(function () {
                        if (!value) {
                            selectize.clearOptions();
                            selectize.refreshOptions();
                        }
                    }, 200);
                },
                load: function (query, callback) {
                    var selectize = this;
                    var value = this.getValue();
                    if (!value) {
                        selectize.clearOptions();
                        selectize.refreshOptions();
                    }
                    var promise;
                    if (angular.isFunction(searchApiFunction)) {
                        promise = searchApiFunction(query);
                    } else {
                        var apiParams = { limit: 20, search_string: query };
                        if (queryParams) {
                            angular.merge(apiParams, queryParams);
                        }

                        promise = UserDAO.search(apiParams);
                    }
                    promise.then(function (res) {
                        angular.forEach(res, function (result) {
                            result._searchField = query;
                        });
                        callback(res);
                    }, function (err) {

                        callback();
                    });
                }
            };
            return selectize;
        }; */

        return generator;
    });
})(window.angular);
