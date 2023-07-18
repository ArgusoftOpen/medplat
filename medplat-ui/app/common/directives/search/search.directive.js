(function () {
    var search = function (QueryDAO, SEARCH_TERM) {
        return {
            restrict: 'E',
            scope: {
                terms: "<?",
                memberType: "<?",
                searchFn: "&",
                closeFn: "&",
                searchForm: "=",
                search: "=",
                selectedLocation: "=?",
                selectedLocationId: "=?"
            },
            templateUrl: 'app/common/directives/search/search-template.html',
            link: function($scope, element, attrs) {
                var init = function () {

                    if (!$scope.memberType) {
                        $scope.memberType = [];
                    }

                    $scope.selectedIndex = null;

                    $scope.baseTerms = [
                        { name: SEARCH_TERM.memberId, value: 'member id', order: 1, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.familyId, value: 'family id', order: 2, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.mobileNumber, value: 'mobile number', order: 3, config: { minlength: 10, maxlength: 10 }, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.orgUnit, value: 'organization unit', order: 4, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.villageName, value: 'village name', order: 5, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.name, value: 'name', order: 6, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.lmp, value: 'lmp', order: 7, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.edd, value: 'edd', order: 8, config: {}, searchFor: $scope.memberType },
                        // { name: SEARCH_TERM.abhaNumber, value: 'abha number', order: 9, config: {}, searchFor: $scope.memberType },
                        // { name: SEARCH_TERM.abhaAddress, value: 'abha address', order: 10, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.maaVatsalya, value: 'maaVatsalya', order: 9, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.dob, value: 'dob', order: 10, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.pmjay, value: 'pmjay', order: 11, config: {}, searchFor: $scope.memberType },
                        { name: SEARCH_TERM.ration, value: 'ration', order: 12, config: {}, searchFor: $scope.memberType },
                    ];

                    if (!$scope.terms) {
                        $scope.terms = [];
                    }

                    if (!$scope.searchForm) {
                        $scope.searchForm = {};
                    }

                    if (!$scope.selectedLocation) {
                        $scope.selectedLocation = {}
                    }

                    if (!$scope.selectedLocationId) {
                        $scope.selectedLocationId = ''
                    }

                    $scope.filteredLabels = [];
                    
                    if ($scope.terms.length > 0) {
                        $scope.filterTerms();
                    } 

                    if ($scope.terms.length === 0 || $scope.filteredLabels.length === 0) {
                        $scope.filteredLabels = $scope.baseTerms;
                    }

                    if ($scope.filteredLabels.length > 1) {
                        $scope.filteredLabels = $scope.filteredLabels.sort($scope.compareOrder)
                    }

                }

                $scope.locationSelectizeWpd = {
                    create: false,
                    valueField: 'id',
                    labelField: 'hierarchy',
                    dropdownParent: 'body',
                    highlight: true,
                    searchField: ['_searchField'],
                    maxItems: 1,
                    render: {
                        item: function (location, escape) {
                            var returnString = "<div>" + location.hierarchy + "</div>";
                            return returnString;
                        },
                        option: function (location, escape) {
                            var returnString = "<div>" + location.hierarchy + "</div>";
                            return returnString;
                        }
                    },
                    onFocus: function () {
                        this.onSearchChange("");
                    },
                    onBlur: function () {
                        var selectize = this;
                        var value = this.getValue();
                        setTimeout(function () {
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
                        var queryDto = {
                            code: 'location_search_for_web',
                            parameters: {
                                locationString: query,
                            }
                        };
                        if (query !== null && query !== '') {
                            promise = QueryDAO.execute(queryDto);
                            promise.then(function (res) {
                                angular.forEach(res.result, function (result) {
                                    result._searchField = query;
                                });
                                callback(res.result);
                            }, function () {
                                callback();
                            });
                        }
                    }
                }

                $scope.$watch('selectedLocation', function(newValue) {
                    $scope.selectedLocationId = newValue['locationId'];
                }, true)

                $scope.$watch('search.searchFor', function(newValue) {
                    if (newValue != undefined) {
                        $scope.filteredLabels = [];
                        if ($scope.terms.length === 0) {
                            $scope.filteredLabels = $scope.baseTerms;
                        } else {
                            $scope.filterTerms();
                        }
                        $scope.filteredLabels = $scope.filteredLabels.filter(label => label.searchFor.includes($scope.search.searchFor))
                        if ($scope.filteredLabels.length > 1) {
                            $scope.filteredLabels.sort($scope.compareOrder)
                        } 
                        $scope.search.searchBy = null
                        $scope.resetSearchString() 
                    }
                }, true)

                $scope.$watch('search.searchBy', function(newValue) {
                    if (newValue != undefined && newValue != null) {
                        $scope.selectedIndex = $scope.filteredLabels.map(label => label.value).indexOf(newValue);
                    } else {
                        $scope.selectedIndex = null;
                    }
                })

                $scope.compareOrder = (a, b) => {
                    if (a.order < b.order) {
                        return -1
                    } else if (a.order > b.order) {
                        return 1
                    } else {
                        return 0
                    }
                }

                $scope.filterTerms = () => {
                    $scope.baseTerms.forEach(baseTerm => {
                        $scope.terms.forEach(term => {
                            if (baseTerm.name === term.name) {
                                if (term.order != undefined) {
                                    baseTerm.order = term.order;
                                }
                                if (term.config != undefined) {
                                    baseTerm.config = term.config;
                                }
                                if (term.searchFor != undefined) {
                                    baseTerm.searchFor = term.searchFor;
                                }
                                $scope.filteredLabels.push(baseTerm);
                            }
                        });
                    });
                }

                $scope.resetSearchString = () => {
                    if ($scope.search.searchString != null) {
                        $scope.search.searchString = null;
                    }
                    $scope.search.familyMobileNumber = null;
                    $scope.selectedLocation = {};
                    $scope.searchForm.$setPristine()
                };

                $scope.validateAbhaNumber = (healthIdNumber) => {
                    if (healthIdNumber != undefined) {
                        let newValue = healthIdNumber.split('-').join('').slice(2);
                        let finalValue = healthIdNumber.slice(0, 2);

                        if (newValue.length > 0) {
                            newValue = newValue.match(new RegExp('.{1,4}', 'g')).join('-');
                            finalValue = finalValue + '-' + newValue;
                            if (finalValue.length >= 17) {
                                $scope.search.searchString = healthIdNumber.slice(0, 17);
                                $scope.searchForm['abhaNumber'].$setValidity("healthid", true);
                            } else {
                                $scope.search.searchString = finalValue;
                                $scope.searchForm['abhaNumber'].$setValidity("healthid", false);
                            }
                        }
                    }
                }

                init();
            }
        }
    };

    angular.module('imtecho.directives').directive('search', search);
})();