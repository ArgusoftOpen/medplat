(function () {
    'use strict';
    angular.module('imtecho.controllers')
        .controller('LayoutController', LayoutController);

    function LayoutController($filter, $state, AuthenticateService, $uibModal, $interval, Mask, UserDAO, GeneralUtil) {
        var layout = this;
        layout.currentState = $state;
        function init() {
            layout.userDetail;
            layout.userObj = {};
            layout.allMenu = [];
            layout.showSearch = false;
            [layout.imagesPath, layout.logoImages] = GeneralUtil.getLogoImages();
            AuthenticateService.getLoggedInUser().then(function (res) {
                layout.userDetail = res.data;
                layout.showSearch = true;
                layout.menuList = layout.userDetail.features;
                angular.forEach(layout.menuList, function (value, key) {
                    layout.withGroups = [];
                    layout.withoutGroups = [];
                    layout.withGroups = $filter('filter')(value, { isGroup: '!!' });
                    layout.withGroups = $filter('orderBy')(layout.withGroups, 'displayOrder');
                    layout.withoutGroups = $filter('filter')(value, { isGroup: '!' });
                    layout.withoutGroups = $filter('orderBy')(layout.withoutGroups, 'displayOrder');
                    if (!!layout.showSearch) {
                        // Generating Menu array For search
                        layout.allMenu = layout.allMenu.concat(layout.withoutGroups.map(function (val) {
                            val.groupName = key;
                            return val;
                        }));
                        layout.withGroups.map(function (val) {
                            var subgropus = val.subGroups.map(function (sgroup) {
                                sgroup.subGroupName = val.name;
                                sgroup.groupName = key;
                                return sgroup;
                            });
                            layout.allMenu = layout.allMenu.concat(subgropus);
                        });
                    }
                    //Final sorted array
                    layout.withGroups.push.apply(layout.withGroups, layout.withoutGroups);
                    layout.menuList[key] = layout.withGroups;
                });
            });
            var intervalPromise = $interval(function () {
                if ($('.cst-sidebar-nav li.has-sub').length > 0) {
                    parentHover();
                    $interval.cancel(intervalPromise);
                }
            }, 1);
        }

        layout.redirect = function (state) {
            layout.search = '';
            $state.go(state);
        };

        layout.redirectToState = function (state) {
            if (state !== null && state !== undefined && state !== '')
                $state.go(state);
        };

        layout.changePassword = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/manage/users/views/update-password.modal.html',
                controller: 'ResetPasswordModalController',
                controllerAs: 'resetPassword',
                windowClass: 'cst-modal',
                size: 'md',
                resolve: {
                    user: function () {
                        var user = {
                            changePassword: true, showCancel: true,
                            userName: layout.userDetail.userName
                        };
                        return user;
                    }
                }
            });
            modalInstance.result.then(function () { }, function () { });
        };

        layout.logout = function () {
            AuthenticateService.logOut();
            $state.go("login");
        };

        var menuTimeOut;
        var isTimerRunning = false;
        var lastMouseoutElement;
        var parentHover = function () {
            $('.position-wrapper.child').mouseover(function () {
                clearTimeout(menuTimeOut);
                isTimerRunning = false;
            });

            $('body').click(function () {
                clearCloseLastMenu();
            });

            $('.cst-sidebar-nav li.has-sub').mouseover(function () {
                clearCloseLastMenu();
                var submenu = $(this).children('.position-wrapper');
                var anchor = $(this).children('a');
                $(anchor).addClass('add-arrow');
                var pos = $(this).position();
                var posTop = pos.top;
                var posLeft = pos.left;
                var itemWidth = Math.round($(this).outerWidth());
                var itemHeight = submenu.height();
                var curPos = $(this).offset().top
                var winHeight = $(window).height();
                var maxHeight = 0;
                posLeft += itemWidth;
                if ($(submenu).hasClass('child')) {
                    if (winHeight - 100 <= itemHeight) {
                        if (curPos - posTop == 60) {
                            posTop = 0;
                        } else {
                            posTop = 0 - (curPos - posTop) + 60
                        }
                        maxHeight = winHeight - 100;

                    } else if (itemHeight - (winHeight - 40 - posTop - 60) > 0) {
                        posTop = posTop - (itemHeight - (winHeight - 40 - posTop - 60));
                    }
                } else {
                    if (winHeight - 100 <= itemHeight) {
                        posTop = 60;
                        maxHeight = winHeight - 100;
                    } else if (itemHeight - (winHeight - 40 - posTop) > 0) {
                        posTop = posTop - (itemHeight - (winHeight - 40 - posTop));
                    }
                }

                /* if (winHeight - 100 <= itemHeight) {
                    if ($(submenu).hasClass('child')) {
                        posTop = 0;
                    } else {
                        posTop = 60;
                    }
                    maxHeight = winHeight - 100;
                } else if (itemHeight - (winHeight - 40 - posTop) > 0) {
                    if (itemHeight - (winHeight - 40 - curPos) > 0) {
                        posTop = posTop - (itemHeight - (winHeight - 40 - posTop))
                    }
                    maxHeight = itemHeight;
                }
                if ($(this).parent().closest('nav').hasClass('cst-sidebar-nav'))
                    menuHeight = winHeight - posTop + $(window).scrollTop() - 40;
                else {
                    menuHeight = winHeight - $(this).offset().top + $(window).scrollTop() - 40;
                } */

                $(submenu).css({
                    'top': posTop + 'px',
                    'left': posLeft + 'px',
                });
                if (maxHeight > 0) {
                    $(submenu).children('.sub-menu').css({
                        'height': maxHeight + 'px'
                    })
                }
                $(submenu).show();
            });

            $('.cst-sidebar-nav li.has-sub').mouseleave(function () {
                var self = $(this);
                isTimerRunning = true;
                lastMouseoutElement = $(this);
                clearTimeout(menuTimeOut);
                menuTimeOut = setTimeout(function () {
                    var submenu = self.find('.position-wrapper');
                    $(submenu).removeAttr('style');
                    var anchor = self.find('a');
                    $(anchor).removeClass('add-arrow');
                    isTimerRunning = false;
                }, 3000);
            });
        };

        var clearCloseLastMenu = function () {
            if (isTimerRunning) {
                clearTimeout(menuTimeOut);
                var submenu = lastMouseoutElement.find('.position-wrapper');
                $(submenu).removeAttr('style');
                var anchor = lastMouseoutElement.find('a');
                $(anchor).removeClass('add-arrow');
                isTimerRunning = false;
                layout.search = '';
            }
        }

        layout.navigateToState = function (state) {
            $state.go(state.state.name, state.params);
        };

        layout.updateProfile = function () {
            Mask.show();
            layout.getUser(layout.userDetail.id).then(function () {
                var modalInstance = $uibModal.open({
                    templateUrl: 'app/common/views/update-user-profile.html',
                    controller: 'UpdateUserProfileController',
                    controllerAs: 'updateProfile',
                    size: 'xl',
                    backdrop: 'static',
                    resolve: {
                        updateUserObj: function () {
                            return layout.userObj;
                        }
                    }
                });
                modalInstance.result.then(function (res) {
                    if (!(res === 'cancel' || res === 'escape key press')) {
                        throw res;
                    }
                }, function () { });
            });
            Mask.hide();
        };

        layout.getUser = function (id) {
            Mask.show();
            return UserDAO.retrieveById(id).then(function (res) {
                layout.userObj = res;
            }).finally(function () {
                Mask.hide();
            });
        };

        init();
    }
})();
