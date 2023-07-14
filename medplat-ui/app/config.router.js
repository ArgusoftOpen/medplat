(function () {
    var as = angular.module("imtecho");
    as.config(['$stateProvider', '$urlRouterProvider', 'LZ_CONFIG',
        function ($stateProvider, $urlRouterProvider, LZ_CONFIG) {
            // For unmatched routes
            $urlRouterProvider.otherwise('/');
            // Application routes
            $stateProvider
                .state('login', {
                    url: '/',
                    title: 'Login',
                    templateUrl: 'app/login/views/login.html',
                    controller: 'LoginController as login',
                    resolve: load([
                        'login.controller',
                        'bootstrap',
                        'formvalidation.directive',
                        'generalutil.service',
                        'emptylabel.filter'
                    ])
                })
                .state('logindownload', {
                    url: '/download',
                    title: 'Login Download',
                    templateUrl: 'app/login/views/login-download.html',
                    controller: 'LoginDownloadController as login',
                    resolve: load([
                        'login-download.controller',
                        'bootstrap',
                        'formvalidation.directive'
                    ])
                })
                .state('info', {
                    url: '/info',
                    title: 'App Info',
                    templateUrl: 'app/login/views/info.html',
                    controller: 'InfoController as info',
                    resolve: load([
                        'info.controller',
                        'formvalidation.directive',
                        'mobilenumber.filter',
                        'auto-focus.directive',
                        'limitTo.directive',
                        'info.service'
                    ])
                })
                .state('resetpassword', {
                    url: '/forgotpassword',
                    title: 'Forgot Password',
                    templateUrl: 'app/login/views/forget-password.html',
                    controller: 'ForgetPasswordController as forgetpass',
                    resolve: load([
                        'user.service',
                        'forget-password.controller',
                        'bootstrap',
                        'formvalidation.directive'
                    ])
                })
                // .state('healthidcardotp', {
                //     url: '/healthidcardotp/:id',
                //     title: 'Health ID Card Download',
                //     templateUrl: 'app/manage/ndhm/healthid/views/health-id-card-otp.html',
                //     controller: 'HealthIdCardOTPController as healthIDCardOtpCtrl',
                //     resolve: load([
                //         'health-id-card-otp.controller',
                //         'formvalidation.directive',
                //         'ndhm-health-id-create-service',
                //         'generalutil.service',
                //         'numbers-only.directive'
                //     ])
                // })
                .state('techo', {
                    url: "/techo",
                    abstract: true,
                    templateUrl: 'app/common/views/layout.html',
                    controller: 'LayoutController as layout',
                    resolve: load([
                        'bootstrap',
                        'uibootstrap',
                        'layout.controller',
                        'roles.service',
                        'titlecase.filter',
                        'datesuffix.filter',
                        'formvalidation.directive',
                        'generalutil.service',
                        'chosen',
                        'confirmation.modal',
                        'data-persist.service',
                        'tableFixer',
                        'customjs',
                        'alert.modal',
                        // 'ndhm.consent.request.modal',
                        // 'ndhm.link.privious.record.modal',
                        // 'ndhm-cretae-and-print-health-id.modal',
                        'numbers-only.directive',
                        'location.directive',
                        // 'wellness.directive',
                        // 'discharge-summary.directive',
                        // 'observation.directive',
                        // 'diagnostic-report.directive',
                        // 'medication-request.directive',
                        // 'appointment.directive',
                        // 'care-plan.directive',
                        // 'allergy-intolerance.directive',
                        // 'op-consultation.directive',
                        // 'immunization-record.directive',
                        // 'presciption.directive',
                        // 'health-document-record.directive',
                        // 'immunization.directive',
                        // 'document-reference-directive',
                        // 'consent-list-directive',
                        // 'organization.directive',
                        // 'condition.directive',
                        // 'procedure.directive',
                        'location.service',
                        'printthis',
                        'firePath',
                        'isolateform.directive',
                        'emptylabel.filter',
                        'update-password.modal.controller',
                        'user.service',
                        'dynamicview.directive',
                        'map-to-array.filter',
                        'selectize',
                        'trustashtml',
                        'datePicker.directive',
                        'alphabetonly.directive',
                        'removespaces.directive',
                        'yes-or-no.filter',
                        'gender.filter',
                        'sd-score.filter',
                        'query.service',
                        'locationName.filter',
                        'ngFlow',
                        'unsafe.filter',
                        'daterangepicker.directive',
                        'limitTo.directive',
                        'a-redirect',
                        'sort.directive',
                        'update-user-profile.controller',
                        'role.service',
                        'user.constants',
                        'update-password.modal.controller',
                        'infrastructure.directive',
                        'selectize.generator',
                        'techo-form-field-directive',
                        'condition-render-directive',
                        'generate-dynamic-template.directives',
                        'listvalue.modal',
                        'search.directive',
                        'search.constants'
                    ])
                })
                .state('techo.dashboard', {
                    url: '/dashboard',
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.admin', {
                    url: "/admin",
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.manage', {
                    url: "/manage",
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.fieldsupportofficer', {
                    url: "/fso",
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.fieldsupportofficer.absentusers', {
                    url: '/absentusers',
                    title: 'Manage absent users',
                    templateUrl: 'app/fieldsupportofficer/views/manageAbsentUsers.html',
                    controller: 'ManageAbsentUsersController as manageabsentuserscontroller',
                    resolve: load([
                        'manageabsentusers.controller',
                        'paging.service'
                    ])
                })
                .state('techo.training', {
                    url: "/training",
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.manage.menu', {
                    url: "/menu",
                    title: 'Manage Menu',
                    templateUrl: 'app/admin/menufeature/views/menu-feature-config.html',
                    controller: 'MenuConfigController as menuconfig',
                    resolve: load([
                        'menu-feature-config.controller',
                        'menu-config.service',
                        'user.service',
                        'role.service',
                        'selectize.generator'
                    ])
                })
                .state('techo.manage.user', {
                    url: '/user/:id',
                    title: 'Manage user',
                    templateUrl: 'app/manage/users/views/manage-user.html',
                    controller: 'ManageUserController as usercontroller',
                    resolve: load([
                        'manage-user.controller',
                        'user.service',
                        'role.service',
                        'user.constants',
                        'update-password.modal.controller',
                        'limitTo.directive'
                    ])
                })
                .state('techo.manage.users', {
                    url: '/users',
                    title: 'List of Users',
                    templateUrl: 'app/manage/users/views/users.html',
                    controller: 'UsersController as userscontroller',
                    resolve: load([
                        'users.controller',
                        'user.service',
                        'languagename.filter',
                        'statecapitalize.filter',
                        'user.constants',
                        'ngInfiniteScroll',
                        'paging.service',
                        'role.service',
                        'alasql'
                    ])
                })
                .state('techo.manage.announcement', {
                    url: '/announcement/:id',
                    title: 'Add announcement',
                    templateUrl: 'app/manage/announcement/views/manage-announcement.html',
                    controller: 'ManageAnnouncementController as ctrl',
                    resolve: load([
                        'manage-announcement.controller',
                        'announcement.service',
                    ])
                })
                // .state('techo.manage.announcements', {
                //     url: '/announcements',
                //     title: 'List of announcements',
                //     templateUrl: 'app/manage/announcement/views/announcements.html',
                //     controller: 'AnnouncementsController as announcementscontroller',
                //     resolve: load([
                //         'announcements.controller',
                //         'announcement.service',
                //         'paging.service'
                //     ])
                // })
                .state('techo.manage.memberinformation', {
                    url: '/memberinformation/:uniqueHealthId',
                    title: 'Member Information',
                    templateUrl: 'app/manage/views/member-information.html',
                    controller: 'MemberInfoController as memberInfo',
                    params: {
                        uniqueHealthId: null
                    },
                    resolve: load([
                        'memberinfo.controller',
                        'anganwadi.service',
                        'authentication.service'
                    ])
                })
                .state('techo.manage.familyinformation', {
                    url: '/familyinformation/:familyId',
                    title: 'Family Information',
                    templateUrl: 'app/manage/views/family-information.html',
                    controller: 'FamilyInfoController as familyInfo',
                    params: {
                        familyId: null
                    },
                    resolve: load([
                        'familyinfo.controller',
                        'query.controller',
                        'query.service'
                    ])
                })
                .state('techo.manage.userinformation', {
                    url: '/userinformation',
                    title: 'User Information',
                    templateUrl: 'app/manage/userinformation/views/user-information.html',
                    controller: 'UserInfoController as userInfo',
                    resolve: load([
                        'user-information.controller',
                        'query.controller',
                        'query.service'
                    ])
                })
                .state('techo.manage.memberserviceregister', {
                    url: '/serviceregister',
                    title: 'Member Service Register',
                    templateUrl: 'app/manage/memberserviceregister/views/member-service-register.html',
                    controller: 'MemberServiceRegisterController as memberServiceInfo',
                    resolve: load([
                        'member-service-register.controller',
                        'member-service-register.constant',
                        'member-lmp-service-register.controller',
                        'member-anc-service-register.controller',
                        'member-child-service-register.controller',
                        'member-wpd-service-register.controller',
                        'member-pnc-service-register.controller',
                        'query.controller',
                        'query.service',
                        'paging.service',
                        'alasql',
                    ])
                })
                // .state('techo.manage.searchfeature', {
                //     url: '/searchFeature/:uniqueHealthId',
                //     title: 'Helpdesk Tool - Search',
                //     templateUrl: 'app/manage/helpdesktool/views/help-desk-search.html',
                //     controller: 'SearchFeatureController as searchInfo',
                //     params: { familyId: { value: null } },
                //     resolve: load([
                //         'help-desk-search.controller',
                //         'help-desk-edit.modal.controller',
                //         'query.controller',
                //         'anganwadi.service',
                //         'query.service',
                //         'timeline-config.service',
                //         'help-desk.constant',
                //         'authentication.service'
                //     ])
                // })
                .state('techo.manage.mobilemangementinfo', {
                    url: '/mobileManagementInfo',
                    title: 'Mobile management',
                    templateUrl: 'app/manage/mobileManagement/views/mobile-management.html',
                    controller: 'MobileManagementController as mobileManageInfo',
                    resolve: load([
                        'help-desk-search.controller',
                        'query.controller',
                        'anganwadi.service',
                        'query.service',
                        'authentication.service',
                        'mobile-management.controller'
                    ])
                })
                .state('techo.manage.mobilelibrary', {
                    url: '/mobileLibrary',
                    title: 'Mobile Library',
                    templateUrl: 'app/manage/mobilelibrary/views/mobile-library.html',
                    controller: 'MobileLibraryController as mobileLibrary',
                    resolve: load([
                        'help-desk-search.controller',
                        'query.controller',
                        'anganwadi.service',
                        'query.service',
                        'authentication.service',
                        'mobile-library.controller',
                        'role.service',
                        'mobile-library.service'
                    ])
                })
                .state('techo.manage.usergeoservices', {
                    url: '/viewGeoServices',
                    title: 'View Geo Services',
                    templateUrl: 'app/manage/geoservices/views/view-user-geo-services.html',
                    controller: 'ViewGeoServicesController as viewGeoServices',
                    resolve: load([
                        'help-desk-search.controller',
                        'query.controller',
                        'anganwadi.service',
                        'query.service',
                        'authentication.service',
                        'view-user-geo-services.controller',
                        'role.service',
                        'mobile-library.service',
                    ])
                })
                .state('techo.manage.servicedeliverylocation', {
                    url: '/servicedeliverylocation',
                    title: 'Place of Service Delivery',
                    templateUrl: 'app/manage/servicedeliverylocation/views/service-delivery-location.html',
                    controller: 'ServiceDeliveryLocationController as serviceDeliveryLocation',
                    resolve: load([
                        'help-desk-search.controller',
                        'query.controller',
                        'anganwadi.service',
                        'query.service',
                        'authentication.service',
                        'service-delivery-location.controller',
                        'service-delivery-line-list.controller',
                        'location.service',
                        'role.service',
                        'mobile-library.service',
                    ])
                })
                // .state('techo.manage.rchregister', {
                //     url: '/rchregister',
                //     title: 'Rch register',
                //     templateUrl: 'app/manage/rchregister/views/rch-register.html',
                //     controller: 'RchRegisterController as rchRegister',
                //     resolve: load([
                //         'rch-register.controller',
                //         'member-service-register.constant',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'rch-register.service',
                //         'report.service'
                //     ])
                // })
                .state('techo.manage.uploadDocument', {
                    url: '/uploadDocument',
                    title: 'Upload Document',
                    templateUrl: 'app/manage/uploaddocument/views/upload-document.html',
                    controller: 'UploadDocumentController as uploadDocument',
                    resolve: load([
                        'help-desk-search.controller',
                        'query.controller',
                        'anganwadi.service',
                        'query.service',
                        'authentication.service',
                        'mobileLibrary.controller',
                        'role.service',
                        'mobile-library.service',
                        'upload-document.controller'
                    ])
                })
                .state('techo.manage.pncSearch', {
                    url: '/pnc/search',
                    title: 'PNC Institution Form',
                    templateUrl: 'app/manage/facilitydataentry/pnc/views/pnc-search.html',
                    controller: 'PncSearchController as pncsearchcontroller',
                    resolve: load([
                        'pnc-search.controller',
                        'pnc.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anganwadi.service'
                    ])
                })
                .state('techo.manage.pnc', {
                    url: '/pnc/:id',
                    title: 'Manage PNC visit',
                    templateUrl: 'app/manage/facilitydataentry/pnc/views/manage-pnc.html',
                    controller: 'ManagePncController as managepnccontroller',
                    resolve: load([
                        'manage-pnc.controller',
                        'pnc.service',
                        'anganwadi.service',
                        'selectize.generator'
                    ])
                })
                .state('techo.manage.fpChangeSearch', {
                    url: '/fpchange/search',
                    title: 'Family Planning Service Visit',
                    templateUrl: 'app/manage/facilitydataentry/familyplanning/views/family-planning-search.html',
                    controller: 'FpChangeSearch as fpchangesearch',
                    resolve: load([
                        'family-planning-search.controller',
                        'iucd-removal.modal.controller',
                        'change-family-planning-method.modal.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging-for-query-builder.service',
                        'familyplanning.filter'
                    ])
                })
                .state('techo.manage.childServiceSearch', {
                    url: '/childservice/search',
                    title: 'Child Service Visit',
                    templateUrl: 'app/manage/facilitydataentry/childservice/views/child-service-search.html',
                    controller: 'ChildServiceSearchController as childservicesearchcontroller',
                    resolve: load([
                        'child-service-search.controller',
                        'child-service.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anganwadi.service'
                    ])
                })
                .state('techo.manage.childservice', {
                    url: '/childservice/:id',
                    title: 'Manage Child Service Visit',
                    templateUrl: 'app/manage/facilitydataentry/childservice/views/manage-child-service.html',
                    controller: 'ManageChildServiceController as managechildservicecontroller',
                    resolve: load([
                        'manage-child-service.controller',
                        'child-service.service',
                        'anganwadi.service',
                        'selectize.generator'
                    ])
                })
                .state('techo.manage.wpdSearch', {
                    url: '/wpd/search',
                    title: 'Institutional Delivery Reg. Form',
                    templateUrl: 'app/manage/facilitydataentry/wpd/views/wpd-search.html',
                    controller: 'WpdSearchController as queform',
                    resolve: load([
                        'wpd-search.controller',
                        'wpd.service',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        // 'ndhm-health-id-util-service',
                        // 'ndhm-health-id.constants'
                    ])
                })
                .state('techo.manage.wpd', {
                    url: '/wpd/:id',
                    title: 'Manage WPD',
                    templateUrl: 'app/manage/facilitydataentry/wpd/views/manage-wpd.html',
                    controller: 'ManageWpdController as managewpdcontroller',
                    resolve: load([
                        'manage-wpd.controller',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service',
                        'wpd.service',
                        'selectize.generator',
                        // 'ndhm-hip-util-service'
                    ])
                })
                // .state('techo.manage.sicklecellSearch', {
                //     url: '/sicklecell/search',
                //     title: 'Sickle Cell Screening',
                //     templateUrl: 'app/manage/facilitydataentry/sicklecell/views/sicklecell-search.html',
                //     controller: 'SicklecellSearchController as sicklecellsearch',
                //     resolve: load([
                //         'sicklecell-search.controller',
                //         'sicklecell.constants',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //     ])
                // })
                .state('techo.manage.outPatientTreatmentSearch', {
                    url: '/outpatienttreatment/search',
                    title: 'Out-Patient Treatment (OPD)',
                    templateUrl: 'app/manage/facilitydataentry/outpatienttreatment/views/out-patient-treatment-search.html',
                    controller: 'OutPatientTreatmentSearch as outPatientTreatmentSearch',
                    resolve: load([
                        'out-patient-treatment-search.controller',
                        'out-patients.constants',
                        'out-patient.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service'
                    ])
                })
                .state('techo.manage.optTreatmentForm', {
                    url: '/opt/treatmentform/:id',
                    title: 'OPT Treatment Form',
                    templateUrl: 'app/manage/facilitydataentry/outpatienttreatment/views/out-patient-treatment.html',
                    controller: 'OutPatientTreatment as outPatientTreatment',
                    resolve: load([
                        'out-patient-treatment.controller',
                        'out-patients.constants',
                        'out-patient.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service'
                    ])
                })
                .state('techo.manage.optMedicinesForm', {
                    url: '/opt/medicinesform/:id',
                    title: 'OPT Medicines Form',
                    templateUrl: 'app/manage/facilitydataentry/outpatienttreatment/views/out-patient-treatment.html',
                    controller: 'OutPatientTreatment as outPatientTreatment',
                    resolve: load([
                        'out-patient-treatment.controller',
                        'out-patients.constants',
                        'out-patient.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service'
                    ])
                })
                .state('techo.manage.sicklecell', {
                    url: '/sicklecell/:id',
                    title: 'Sickle Cell Screening',
                    templateUrl: 'app/manage/facilitydataentry/sicklecell/views/sicklecell.html',
                    controller: 'SicklecellController as sicklecell',
                    resolve: load([
                        'sicklecell.controller',
                        'sicklecell.service',
                        'sicklecell.constants',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                    ])
                })
                .state('techo.manage.pregregedit', {
                    url: '/pregnancyregistration/edit',
                    title: 'Pregnancy Registration Verification',
                    templateUrl: 'app/manage/pregnancyregistration/views/preg-reg-edit.html',
                    controller: 'PregnancyRegistration as pregreg',
                    resolve: load([
                        'preg-reg-edit.controller',
                        'preg-reg-edit-modal.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                    ])
                })
                .state('techo.manage.markchiranjeevi', {
                    url: '/markchiranjeevi',
                    title: 'Chiranjeevi Deliveries Updation',
                    templateUrl: 'app/manage/views/mark-chiranjeevi.html',
                    controller: 'MarkChiranjeevi as markchiranjeevi',
                    resolve: load([
                        'mark-chiranjeevi.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                    ])
                })
                .state('techo.manage.healthInfrastructureApproval', {
                    url: '/approvehealthinfrastructure',
                    title: 'Health Infrastructure Approval',
                    templateUrl: 'app/admin/drtecho/views/healthinfrastructure-approval.html',
                    controller: 'HealthInfrastructureApproval as healthinfrastructureapproval',
                    resolve: load([
                        'drtecho.constants',
                        'healthinfrastructure-approval.controller',
                        'drtecho.service',
                        'healthinfrastructure-detail-modal',
                        'healthinfrastructure-approval-modal',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'reverseIterate.filter',
                    ])
                })
                .state('techo.manage.drtechoUserApproval', {
                    url: '/drtechouserapproval',
                    title: 'Dr. Techo User Approval',
                    templateUrl: 'app/admin/drtecho/views/user-approval.html',
                    controller: 'UserApproval as userapproval',
                    resolve: load([
                        'drtecho.constants',
                        'user-approval.controller',
                        'user-detail-modal',
                        'user-approval-modal',
                        'drtecho.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'query.service',
                        'reverseIterate.filter',
                    ])
                })
                // .state('techo.manage.ncdreport', {
                //     url: '/ncdreport',
                //     title: 'NCD Location Wise Report',
                //     templateUrl: 'app/manage/views/ncd-report.html',
                //     controller: 'NcdReport as ncdreport',
                //     resolve: load([
                //         'ncd-report.controller'
                //     ])
                // })
                .state('techo.manage.npcblist', {
                    url: '/npcb',
                    title: 'National Programme For Control of Blindness',
                    templateUrl: 'app/manage/npcb/views/npcb-list.html',
                    controller: 'NpcbList as npcblist',
                    resolve: load([
                        'npcb-list.controller',
                        'npcb-spectacles-updation.modal.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                    ])
                })
                .state('techo.manage.npcbexamine', {
                    url: '/npcb/examine/:action/:id',
                    title: 'NPCB Examination',
                    templateUrl: 'app/manage/npcb/views/npcb-examine.html',
                    controller: 'NpcbExamine as npcbexamine',
                    resolve: load([
                        'npcb-examine.controller',
                        'npcb.service',
                        'authentication.service',
                    ])
                })
                // .state('techo.manage.cerebralpalsysearch', {
                //     url: '/cerebralpalsy/search',
                //     title: 'Suspected CP List',
                //     templateUrl: 'app/manage/suspectedcp/views/cerebral-palsy-search.html',
                //     controller: 'CerebralPalsySearchController as cerebralpalsysearch',
                //     resolve: load([
                //         'cerebral-palsy-search.controller',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'anganwadi.service'
                //     ])
                // })
                .state('techo.manage.cerebralpalsy', {
                    url: '/cerebralpalsy/:id',
                    title: 'Suspected CP Child Details',
                    templateUrl: 'app/manage/suspectedcp/views/cerebral-palsy.html',
                    controller: 'CerebralPalsyController as cerebralpalsy',
                    resolve: load([
                        'cerebral-palsy.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anganwadi.service'
                    ])
                })
                // .state('techo.manage.childscreeninglist', {
                //     url: '/childscreening',
                //     title: 'Child Screening List',
                //     templateUrl: 'app/manage/childscreening/views/child-screening-list.html',
                //     controller: 'ChildScreeningListController as childscreeninglist',
                //     resolve: load([
                //         'child-screening-list.controller',
                //         'ngInfiniteScroll',
                //         'child-screening.service',
                //         'paging.service'
                //     ])
                // })
                // .state('techo.manage.childscreening', {
                //     url: '/childscreening/:action/:id',
                //     title: '',
                //     templateUrl: 'app/manage/childscreening/views/child-screening.html',
                //     controller: 'ChildScreeningController as childscreening',
                //     resolve: load([
                //         'child-screening.controller',
                //         'child-screening.service',
                //         // 'ndhm-hip.service',
                //         // 'ndhm-hip.constants',
                //         // 'ndhm-health-id-capture-service',
                //         // 'ndhm-health-id-create-service'
                //     ])
                // })
                .state('techo.manage.laboratorytests', {
                    url: '/laboratorytests/:id',
                    title: 'Laboratory Tests Update',
                    templateUrl: 'app/manage/childscreening/views/laboratory-tests.html',
                    controller: 'LaboratoryTestsController as laboratorytests',
                    resolve: load([
                        'laboratory-tests.controller',
                        'child-screening.service'
                    ])
                })
                .state('techo.manage.medicines', {
                    url: '/medicines/:id',
                    title: 'Medicines given as per protocol',
                    templateUrl: 'app/manage/childscreening/views/medicines.html',
                    controller: 'MedicinesController as medicinescontroller',
                    resolve: load([
                        'medicines.controller',
                        'child-screening.service'
                    ])
                })
                .state('techo.manage.role', {
                    url: '/roles',
                    title: 'Manage Role',
                    templateUrl: 'app/admin/role/views/role.html',
                    controller: 'RoleController as rolecontroller',
                    resolve: load([
                        'roles.controller',
                        'menu-config.service',
                        'role.service',
                        'role.modal.controller',
                        'user.constants',
                        'statecapitalize.filter'
                    ])
                })
                // .state('techo.manage.notification', {
                //     url: '/notification',
                //     title: 'Notification configuration',
                //     templateUrl: 'app/admin/applicationmanagement/notification/views/notification.html',
                //     controller: 'NotificationController as notificationcontroller',
                //     resolve: load([
                //         'notification.controller',
                //         'notification.service',
                //         'statecapitalize.filter',
                //         'selectize.generator',
                //         'syncWithServerService'
                //     ])
                // })
                .state('techo.manage.addNotificationConfiguration', {
                    url: '/admin/applicationmanagement/notification/add',
                    title: 'Add Notification configuration',
                    templateUrl: 'app/admin/applicationmanagement/notification/views/manage-notification.html',
                    controller: 'AddNotificationConfigurationController as addnotificationcontroller',
                    resolve: load([
                        'notification.controller',
                        'manage-notification.controller',
                        'notification.service',
                        'statecapitalize.filter'
                    ])
                })
                .state('techo.training.scheduled', {
                    url: '/all',
                    title: 'Training Schedule',
                    templateUrl: 'app/training/trainingschedule/views/scheduled-training.html',
                    controller: 'ScheduledTrainingCtrl as scheduledTraining',
                    resolve: load([
                        'scheduled-training.controller',
                        'training-schedule.service',
                        'topicCoverage.service',
                        'reschedule-training.modal.controller',
                        'common.service',
                        'constant.service',
                        'course.service',
                        'paging-for-query-builder.service'
                    ])
                })
                .state('techo.training.schedule', {
                    url: '/schedule',
                    title: 'Schedule Training',
                    templateUrl: 'app/training/trainingschedule/views/training-schedule.html',
                    controller: 'TrainingScheduleCtrl as trainingSchedule',
                    resolve: load([
                        'training-schedule.controller',
                        'training-schedule.service',
                        'course.service',
                        'role.service',
                        'datePicker.directive',
                        'attendee-selection.modal.controller'
                    ])
                })
                .state('techo.home', {
                    url: "/home",
                    template: '<ui-view></ui-view>'
                })
                .state('techo.dashboard.fhs', {
                    url: '/fhs',
                    title: 'FHS Dashboard',
                    templateUrl: 'app/fhs/dashboard/views/fhs-dashboard.html',
                    controller: 'FhsDashboardController as fhsdashboard',
                    resolve: load([
                        'fhs-dashboard.controller',
                        'fhs-dashboard.service',
                        'alasql',
                        // 'ngmap'
                    ])
                })
                // .state('techo.dashboard.ncdscreeningdashboard', {
                //     url: '/ncdscreening',
                //     title: 'NCD Screening Dashboard',
                //     templateUrl: 'app/ncd/dashboard/views/ncd-screening-dashboard.html',
                //     controller: 'NcdScreeningDashboardController as ncdScreeningDashboard',
                //     resolve: load([
                //         'ncd-screening-dashboard.controller',
                //     ])
                // })
                .state('techo.dashboard.fhsreport', {
                    url: '/fhsreport/:locationId/:userId',
                    title: 'FHS Report',
                    templateUrl: 'app/fhs/dashboard/views/fhs-report.html',
                    controller: 'FhsReportController as fhsreport',
                    params: {
                        userId: null
                    },
                    resolve: load([
                        'fhs-report.controller',
                        'fhs-dashboard.service'
                    ])
                })
                .state('techo.dashboard.webtasks', {
                    url: '/webtasks/:id',
                    title: '',
                    templateUrl: 'app/manage/webtasks/views/webtasks.html',
                    controller: 'WebTasksController as webtasks',
                    resolve: load([
                        'webtasks.controller',
                        'webtasks.service',
                        'paging.service',
                        // 'mo-verification-for-child-screening.modal.controller',
                        'mo-maternal-death-verification.modal.controller',
                        // 'child-screening.service',
                        'anganwadi.service',
                        'alasql'
                    ])
                })
                // .state('techo.training.dashboard', {
                //     url: '/dashboard',
                //     title: 'My Training Dashboard',
                //     templateUrl: 'app/training/mytrainingdashboard/views/dashboard.html',
                //     controller: 'DashboardCtrl as dashboard',
                //     resolve: load([
                //         'dashboard.controller',
                //         'training-schedule.service',
                //         'topicCoverage.service',
                //         'common.service',
                //         'ngInfiniteScroll',
                //         'paging.service'
                //     ])
                // })
                .state('techo.training.dashboardDetails', {
                    url: '/dashboarddetails/:trainerId/:trainingId/:trainingDate',
                    title: 'Training Dashboard Details',
                    templateUrl: 'app/training/mytrainingdashboard/views/dashboard-details.html',
                    controller: 'DashboardDetailsCtrl as dashboardDetails',
                    resolve: load([
                        'dashboard-details.controller',
                        'training-schedule.service',
                        'attendance.service',
                        'topicCoverage.service',
                        'reason-topic.modal.controller',
                        'reason-attendance.modal.controller'
                    ])
                })
                .state('techo.training.traineeStatus', {
                    url: '/traineestatus?{queryParams:json}',
                    params: { queryParams: null },
                    title: 'Trainee Status',
                    templateUrl: 'app/training/traineestatus/views/trainee-status.html',
                    controller: 'TraineeStatusCtrl as traineeStatusCtrl',
                    resolve: load([
                        'trainee-status.controller',
                        'training-schedule.service',
                        'common.service'
                    ])
                })
                .state('techo.training.editTraineeStatus', {
                    url: '/edittraineestatus/:trainingId',
                    title: 'Mark Training Completion',
                    templateUrl: 'app/training/traineestatus/views/edit-trainee-status.html',
                    controller: 'EditTraineeStatusCtrl as editTraineeStatusCtrl',
                    params: {
                        paramOne: { flag: false }
                    },
                    resolve: load([
                        'edit-trainee-status.controller',
                        'training-schedule.service'
                    ])
                })
                // .state('techo.manage.fhsrverification', {
                //     url: '/fhsrverification',
                //     title: 'MO CFHC Re-Verification',
                //     templateUrl: 'app/fhs/mocfhcreverification/views/mo-cfhc-reverification.html',
                //     controller: 'FhsrVerificationController as fhsrVerificationCtrl',
                //     resolve: load([
                //         'mo-cfhc-reverification.controller',
                //         'verification.service',
                //         'location-type.constants',
                //         'verification.constants',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service'
                //     ])
                // })
                // .state('techo.manage.moverification', {
                //     url: '/moverification',
                //     title: 'Medical Officer Verification - Level 2',
                //     templateUrl: 'app/fhs/moreverificationlevel2/views/mo-verification.html',
                //     controller: 'MoVerificationController as moVerificationCtrl',
                //     resolve: load([
                //         'mo-verification.controller',
                //         'verification.service',
                //         'location-type.constants',
                //         'verification.constants',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service'
                //     ])
                // })
                // .state('techo.dashboard.gvkverification', {
                //     url: '/gvkverification',
                //     title: 'Call Center Verification',
                //     templateUrl: 'app/fhs/callcenterverification/views/gvk-verification.html',
                //     controller: 'GvkVerificationController as gvkreverification',
                //     resolve: load([
                //         'gvk-verification.controller',
                //         'gvk-verification.service',
                //         'gvk-verification.constant',
                //         'mobilenumber.filter',
                //         'auto-focus.directive',
                //         'paging.service',
                //         'ngInfiniteScroll',
                //         'search-members.controller',
                //         'migration.service',
                //         'ago-from-date.filter'
                //     ])
                // })
                .state('techo.dashboard.gvkCallEffectivenessReports', {
                    url: '/gvkCallEffectivenessReports',
                    templateUrl: 'app/manage/gvkcallcenterreport/calleffectivessreprot/views/gvk-call-effectiveness-reports.html',
                    title: 'GVK : Call Effectiveness Report',
                    controller: 'gvkCallEffectivenessReportsController as gvkCallEffectivenessReports',
                    resolve: load([
                        'gvk-call-effectiveness-reports.controller',
                        'gvk-verification.constant',
                        'mobilenumber.filter',
                        'report.service',
                        'query.service',
                        'auto-focus.directive',
                        'paging.service',
                        'ngInfiniteScroll',
                        'search-members.controller',
                        'migration.service',
                        'ago-from-date.filter'
                    ])
                })
                .state('techo.dashboard.cccverification', {
                    url: '/cccverification',
                    title: 'Command And Control Center',
                    templateUrl: 'app/fhs/commandandcontrolcenter/views/ccc-verification.html',
                    controller: 'CCCVerificationController as cccverification',
                    resolve: load([
                        'ccc-verification.controller',
                        'ccc-verification.service',
                        'ccc-verification.constant',
                        'ccc-overdue-services-member-display.controller',
                        'child-service.service',
                        'mobilenumber.filter',
                        'auto-focus.directive',
                        'paging.service',
                        'ngInfiniteScroll',
                        'search-members.controller',
                        'migration.service',
                        'gvk-verification.service',
                        'ago-from-date.filter'
                    ])
                })
                // .state('techo.manage.uploadlocation', {
                //     url: '/uploadlocation',
                //     title: 'Upload Location',
                //     templateUrl: 'app/manage/uploadlocation/views/upload-location.html',
                //     controller: 'UploadLocationController as uploadLocationCtrl',
                //     resolve: load([
                //         'upload-location.controller',
                //         'upload-location.service'
                //     ])
                // })
                .state('techo.report', {
                    url: '/report',
                    template: '<ui-view></ui-view>',
                    abstract: true
                })
                .state('techo.report.config', {
                    url: '/config/:id',
                    title: 'Report Config',
                    templateUrl: 'app/admin/report/views/report-layout.html',
                    controller: 'ReportLayoutController as rl',
                    resolve: load([
                        'report-layout.controller',
                        'report.service',
                        'group.service',
                        'selectize.generator',
                        'user.service',
                        'jq-sortable.directive'
                    ])
                })
                .state('techo.report.view', {
                    url: '/view/:id?from_link&type&{queryParams:json}',
                    params: {
                        queryParams: null
                    },
                    templateUrl: 'app/admin/report/views/report-view.html',
                    controller: 'ReportViewController as rv',
                    resolve: load([
                        'report.service',
                        'dynamicview.directive',
                        'report-view.controller',
                        'ngInfiniteScroll',
                        'paging.service',
                        'reportfieldutil.service',
                        'alasql',
                        'selectize.generator',
                        'user.service'
                    ])
                })
                .state('techo.report.all', {
                    url: '/all',
                    title: 'List of Reports',
                    templateUrl: 'app/admin/report/views/reports.html',
                    controller: 'ReportController as report',
                    resolve: load([
                        'report.service',
                        'reports.controller',
                        'group.service',
                        'paging.service',
                        'syncWithServerService'
                    ])
                })
                // .state('techo.report.familyreport', {
                //     url: '/familyreport',
                //     title: 'Family Report',
                //     templateUrl: 'app/manage/familyreport/views/family-report-view.html',
                //     controller: 'FamilyReportViewController as familyreport',
                //     resolve: load([
                //         'report.service',
                //         'family-report-view.controller',
                //         'paging.service',
                //         'ngInfiniteScroll'
                //     ])
                // })
                .state('techo.report.groups', {
                    url: '/groups',
                    title: 'Groups',
                    templateUrl: 'app/admin/report/views/report-group.html',
                    controller: 'ReportGroupController as reportGroup',
                    resolve: load([
                        'report.service',
                        'report-group.controller',
                        'group.service',
                        'report-group.modal.controller'

                    ])
                })
                .state('techo.manage.districtperformancedashboard', {
                    url: '/districtperformancedashboard',
                    title: 'District Factsheet',
                    templateUrl: 'app/manage/districtperformancedashboard/views/district-performance-dashboard.html',
                    controller: 'DistrictPerformanceDashboardController as districtPerformanceDashboardCtrl',
                    resolve: load([
                        'districtPerformanceDashboardCtrl.controller',
                        'number-format.filter',
                        'show-tooltip',
                        'district-performance-dashboard.service',
                        'district-performance-dashboard.constants'
                    ])
                })
                .state('techo.manage.courselist', {
                    url: '/courselist',
                    title: 'Course',
                    templateUrl: 'app/training/courselist/views/course-list.html',
                    controller: 'CourseListController as courselistcontroller',
                    resolve: load([
                        'course.service',
                        'course-list.controller'
                    ])
                })
                .state('techo.manage.course', {
                    url: '/course/:id',
                    title: 'Manage Course',
                    templateUrl: 'app/training/courselist/views/course.html',
                    controller: 'CourseController as coursecontroller',
                    resolve: load([
                        'course.controller',
                        'update-course.modal.controller',
                        'update-module.modal.controller',
                        'timeline-config.service',
                        'authentication.service',
                        'course.service',
                        'course.constant',
                        'auto-focus.directive',
                        'role.service',
                        'constant.service',
                        'drtecho.service',
                        'sohElementConfiguration.service'
                    ])
                })
                .state('techo.training.questionsetlist', {
                    url: '/question-set-list/:refId/:refType',
                    title: 'Question Set Configuration',
                    templateUrl: 'app/training/questionconfiguration/views/question-set-list.html',
                    controller: 'QuestionSetListController as questionSetListController',
                    resolve: load([
                        'question-list.controller',
                        'course.service'
                    ])
                })
                .state('techo.training.questionset', {
                    url: '/question-set/:id/:refId/:refType',
                    title: 'Manage Question Set Configuration',
                    templateUrl: 'app/training/questionconfiguration/views/question-set.html',
                    controller: 'QuestionSetController as questionSetController',
                    resolve: load([
                        'question-set.controller',
                        'course.service'
                    ])
                })
                .state('techo.training.question', {
                    url: '/question/:id/:refId/:refType',
                    title: 'Manage Question Configuration',
                    templateUrl: 'app/training/questionconfiguration/views/configure-question.html',
                    controller: 'ConfigureQuestionController as configureQuestionController',
                    resolve: load([
                        'question.controller',
                        'course.service',
                        'reverseIterate.filter',
                        'statecapitalize.filter',
                        'drtecho.service',
                        'sohElementConfiguration.service',
                    ])
                })
                .state('techo.manage.lmsdashboard', {
                    url: '/lmsdashboard',
                    title: 'LMS Dashboard',
                    templateUrl: 'app/training/dashboard/views/lms-dashboard.html',
                    controller: 'LMSDashboardController as lms',
                    resolve: load([
                        'lms-dashboard.controller',
                        'paging-for-query-builder.service'
                    ])
                })
                .state('techo.notification', {
                    url: '/event',
                    abstract: true,
                    template: '<ui-view></ui-view>'
                })
                .state('techo.notification.config', {
                    url: '/config/:id',
                    title: 'Event Configuration',
                    templateUrl: 'app/admin/applicationmanagement/event/views/manage-event-config.html',
                    controller: 'EventConfigurationController as eventConfig',
                    resolve: load([
                        'manage-event-config.controller',
                        'mobile-type-event-config.modal.controller',
                        'event-config.service',
                        'ckeditor.directive',
                        'notification.service',
                        'form.service',
                        'jq-sortable.directive',
                        'push-notification.service'
                    ])
                })
                .state('techo.notification.all', {
                    url: '/all',
                    title: 'Events Configuration',
                    templateUrl: 'app/admin/applicationmanagement/event/views/event-configs.html',
                    controller: 'EventConfigurationsController as eventConfigs',
                    resolve: load([
                        'event-configs.controller',
                        'event-config.service',
                        'notification.service',
                        'syncWithServerService',
                        'event-exception.modal.controller'
                    ])
                })
                // .state('techo.manage.anganwadi', {
                //     url: '/anganwadilist',
                //     title: 'List of Anganwadis',
                //     templateUrl: 'app/manage/anganwadi/views/anganwadi.html',
                //     controller: 'AnganwadiListController as anganwadilistController',
                //     resolve: load([
                //         'anganwadi.service',
                //         'anganwadi.controller',
                //         'anganwadi.constant',
                //         'paging.service',
                //         'ngInfiniteScroll',
                //     ])
                // })
                // .state('techo.manage.edit-anganwadi', {
                //     url: '/anganwadi/:id',
                //     title: 'Manage Location',
                //     templateUrl: 'app/manage/anganwadi/views/manage-anganwadi.html',
                //     controller: 'AnganwadiController as anganwadiController',
                //     resolve: load([
                //         'anganwadi.service',
                //         'manage-anganwadi.controller',
                //         'anganwadi.constant',
                //         'numbers-only.directive',
                //         'limitTo.directive'
                //     ])
                // })
                .state('techo.manage.location', {
                    url: '/location',
                    title: 'Manage Location',
                    templateUrl: 'app/manage/locations/views/location.html',
                    controller: 'LocationController as locationctrl',
                    resolve: load([
                        'location.controller',
                        'location.service',
                        'edit-location.controller'
                    ])
                })
                .state('techo.manage.edit-location', {
                    url: '/editlocation/:id',
                    title: 'Edit Location',
                    templateUrl: 'app/manage/locations/views/edit-location.html',
                    controller: 'EditLocationController as editlocationctrl',
                    resolve: load([
                        'edit-location.controller',
                        'location.service'
                    ]),
                    params: { location: null }
                })
                .state('techo.manage.add-location', {
                    url: '/addlocation',
                    title: 'Add Location',
                    templateUrl: 'app/manage/locations/views/add-location.html',
                    controller: 'AddLocationController as addlocationctrl',
                    resolve: load([
                        'add-location.controller',
                        'location.service',
                        'auto-focus.directive'
                    ])
                })
                .state('techo.manage.query', {
                    url: '/query',
                    title: 'Query master',
                    templateUrl: 'app/admin/applicationmanagement/querybuilder/views/query.html',
                    controller: 'QueryController as querycontroller',
                    resolve: load([
                        'query.controller',
                        'query.service',
                        'syncWithServerService'
                    ])
                })
                .state('techo.querymanagement', {
                    url: '/querymanagement',
                    title: 'Query Management Tool',
                    templateUrl: 'app/admin/applicationmanagement/querymanagement/views/query-management.html',
                    controller: 'QueryManagement as querymanagement',
                    resolve: load([
                        'query.controller',
                        'query.service',
                        'query-management.controller',
                        'query-management.service',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'report.service',
                        'paging.service'
                    ])
                })
                .state('techo.manage.translator-label', {
                    url: '/translatorLabel',
                    title: 'Labels Translation',
                    templateUrl: 'app/admin/labelstranslation/views/label-translator.html',
                    controller: 'TranslatorLabelController as translatorLabel',
                    resolve: load([
                        'label-translator.controller',
                        'internationalization.service',
                        'paging.service',
                        'ngInfiniteScroll',
                    ])
                })
                .state('techo.manage.valuesnmultimedia', {
                    url: '/valuesnmultimedia',
                    title: 'Values And Multimedia',
                    templateUrl: 'app/admin/valuesandmultimedia/views/values-and-multi-media.html',
                    controller: 'ValueNMultimediaController as vnm',
                    resolve: load([
                        'values-and-multi-media.controller',
                        'multi-media-player.controller'
                    ])
                })
                // .state('techo.manage.duplicateMemberVerification', {
                //     url: '/duplicateMemberVerification',
                //     title: 'Duplicate Member Verification',
                //     templateUrl: 'app/manage/duplicatememberverification/views/duplicate-member-verification.html',
                //     controller: 'DuplicateMemberVerificationController as dmv',
                //     resolve: load([
                //         'duplicate-member-verification.controller',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service'
                //     ])
                // })
                .state('techo.manage.familymoving', {
                    url: '/familymoving',
                    title: 'Family Moving',
                    templateUrl: 'app/manage/familymoving/views/family-moving.html',
                    controller: 'FamilyMovingController as fm',
                    resolve: load([
                        'family-moving.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anganwadi.service'
                    ])
                })
                // .state('techo.manage.verifiedfamilymoving', {
                //     url: '/verifiedfamilymoving',
                //     title: 'Verified Family Moving',
                //     templateUrl: 'app/manage/familymoving/views/family-moving.html',
                //     controller: 'FamilyMovingController as fm',
                //     resolve: load([
                //         'family-moving.controller',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'anganwadi.service'
                //     ])
                // })
                // .state('techo.manage.fhsworkbargraph', {
                //     url: '/fhsworkprogress',
                //     title: 'FHS WORK PROGRESS',
                //     templateUrl: 'app/manage/fhsworkprogress/views/fhs-verification-work-graph.html',
                //     controller: 'FhsWorkProgress as fwp',
                //     resolve: load([
                //         'fhs-verification-work-graph.controller'
                //     ])
                // })
                // .state('techo.manage.rchdatamigration', {
                //     url: '/rchdatamigration',
                //     title: 'RCH DATA MIGRATION',
                //     templateUrl: 'app/manage/views/rchDataMigration.html',
                //     controller: 'RchDataMigrationCtrl as rdm',
                //     resolve: load([
                //         'rchdatamigration.controller'
                //     ])
                // })
                .state('techo.manage.createsync', {
                    url: '/createsync',
                    title: 'Create RCH Data Sync Request',
                    templateUrl: 'app/manage/views/rchDataMigrationCreation.html',
                    controller: 'RchDataMigrationCreationCtrl as rdmc',
                    resolve: load([
                        'rchdatamigrationcreation.controller',
                        'rchdatamigrationcreationmodal.controller'
                    ]),
                    params: { userId: null }
                })
                .state('techo.manage.ancformquestions', {
                    url: '/ancformquestions/:id',
                    templateUrl: 'app/manage/facilitydataentry/anc/views/anc-form-questions.html',
                    controller: 'AncFormQuestionsController as ancque',
                    title: 'ANC Service Visit',
                    resolve: load([
                        'anc-form-questions.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anc-form-questions.service',
                        'selectize.generator',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service',
                        'selectize.generator',
                        // 'ndhm-hip-util-service'
                    ])
                })
                .state('techo.manage.ancformquestionsdynamic', {
                    url: '/ancformquestionsdynamic/:id',
                    templateUrl: 'app/manage/facilitydataentry/anc/dynamic/views/anc-form-questions.html',
                    controller: 'AncFormQuestionsController as ctrl',
                    title: 'ANC Service Visit',
                    resolve: load([
                        'anc-form-questions-dynamic.controller',
                        'authentication.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        'anc-form-questions.service',
                        'selectize.generator',
                        // 'ndhm-hip.service',
                        // 'ndhm-hip.constants',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-create-service',
                        'selectize.generator',
                        // 'ndhm-hip-util-service'
                    ])
                })
                .state('techo.manage.pncdynamic', {
                    url: '/pncdynamic/:id',
                    templateUrl: 'app/manage/facilitydataentry/pnc/dynamic/views/manage-pnc.html',
                    controller: 'ManagePncController as ctrl',
                    title: 'PNC Service Visit',
                    resolve: load([
                        'manage-pnc-dynamic.controller',
                        'pnc.service',
                        // 'anganwadi.service'
                    ])
                })
                .state('techo.manage.childservicedynamic', {
                    url: '/childservicedynamic/:id',
                    templateUrl: 'app/manage/facilitydataentry/childservice/dynamic/views/manage-child-service.html',
                    controller: 'ManageChildServiceController as ctrl',
                    title: 'Child Service Visit',
                    resolve: load([
                        'manage-child-service-dynamic.controller',
                        'child-service.service',
                    ])
                })
                .state('techo.manage.sicklecelldynamic', {
                    url: '/sicklecelldynamic/:id',
                    title: 'Sickle Cell Screening',
                    templateUrl: 'app/manage/facilitydataentry/sicklecell/dynamic/views/sicklecell.html',
                    controller: 'SicklecellController as ctrl',
                    resolve: load([
                        'sicklecell-dynamic.controller',
                        'sicklecell.service',
                    ])
                })
                // .state('techo.manage.facilityPerformancedynamic', {
                //     url: '/facilityperformancedynamic',
                //     title: 'Facility Performance',
                //     templateUrl: 'app/manage/facilitydataentry/facilityperformance/dynamic/views/facility-performance.html',
                //     controller: 'FacilityPerformanceController as ctrl',
                //     resolve: load([
                //         'facility-performance-dynamic.controller',
                //         'facility-performance.service'
                //     ])
                // })
                .state('techo.manage.medicinesdynamic', {
                    url: '/medicinesdynamic/:id',
                    title: 'Medicines given as per protocol',
                    templateUrl: 'app/manage/childscreening/dynamic/views/medicines.html',
                    controller: 'MedicinesController as ctrl',
                    resolve: load([
                        'medicines-dynamic.controller',
                        'child-screening.service'
                    ])
                })
                .state('techo.manage.laboratorytestsdynamic', {
                    url: '/laboratorytestsdynamic/:id',
                    title: 'Laboratory Tests Update',
                    templateUrl: 'app/manage/childscreening/dynamic/views/laboratory-tests.html',
                    controller: 'LaboratoryTestsController as ctrl',
                    resolve: load([
                        'laboratory-tests-dynamic.controller',
                        'child-screening.service'
                    ])
                })
                // .state('techo.manage.childscreeningdynamic', {
                //     url: '/childscreeningdynamic/:action/:id',
                //     title: '',
                //     templateUrl: 'app/manage/childscreening/dynamic/views/child-screening.html',
                //     controller: 'ChildScreeningController as ctrl',
                //     resolve: load([
                //         'child-screening-dynamic.controller',
                //         'child-screening.service',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                .state('techo.manage.managelocationtypedynamic', {
                    url: '/managelocationtypedynamic/:id',
                    title: 'Manage Location Type',
                    templateUrl: 'app/manage/locationtype/dynamic/views/manage-location-type.html',
                    controller: 'ManageLocationTypeController as ctrl',
                    resolve: load([
                        'manage-location-type-dynamic.controller',
                        'query.service',
                        'location.service',
                    ])
                })
                // .state('techo.manage.staffsmsconfigdynamic', {
                //     url: '/staffsmsconfigdynamic/:id',
                //     title: '',
                //     templateUrl: 'app/manage/staffsmsconfig/dynamic/views/manage-staff-sms-config.html',
                //     controller: 'ManageStaffSmsConfigController as ctrl',
                //     resolve: load([
                //         'manage-staff-sms-config-dynamic.controller',
                //         'staff-sms-config.service',
                //         'user.service',
                //         'role.service'
                //     ])
                // })
                .state('techo.manage.npcbexaminedynamic', {
                    url: '/npcb/examinedynamic/:action/:id',
                    title: 'NPCB Examination',
                    templateUrl: 'app/manage/npcb/dynamic/views/npcb-examine.html',
                    controller: 'NpcbExamine as ctrl',
                    resolve: load([
                        'npcb-examine-dynamic.controller',
                        'npcb.service',
                        'staff-sms-config.service',
                    ])
                })
                .state('techo.manage.healthinfrastructures', {
                    url: '/healthinfrastructures',
                    templateUrl: 'app/manage/healthfacilitymapping/views/health-infrastructure.html',
                    title: 'Health Facility Mapping',
                    controller: 'HealthInfrastructureController as healthInfrastructure',
                    resolve: load([
                        'health-infrastructure.controller',
                        // 'hpr-creation-service',
                        // 'hfr-search-service',
                        // 'hpr-profile-service',
                        'facility-linking.modal.controller',
                        'query.service',
                        'ngInfiniteScroll',
                        'hospital-type-display-name.filter',
                        'health-infrastructure-service',
                        'paging-for-query-builder.service'
                    ])
                })
                .state('techo.manage.healthinfrastructure', {
                    url: '/healthinfrastructure/:id',
                    title: 'Health Infrastructure',
                    templateUrl: 'app/manage/healthfacilitymapping/views/manage-health-infrastructure.html',
                    controller: 'ManageHealthInfrastructure as hi',
                    resolve: load([
                        'manage-health-infrastructure.controller',
                        // 'hpr-creation-service',
                        // 'hfr-search-service',
                        // 'hpr-profile-service',
                        'facility-linking.modal.controller',
                        'query.service',
                        'location.service',
                        // 'hfr-search-service',
                        'health-infrastructure-service'
                    ])
                })
                // .state('techo.manage.covid19healthinfrastructures', {
                //     url: '/covid-19/healthinfrastructures',
                //     templateUrl: 'app/manage/healthfacilitymapping/views/health-infrastructure.html',
                //     title: 'COVID-19 : Manage Health Infrastructure',
                //     controller: 'HealthInfrastructureController as healthInfrastructure',
                //     resolve: load([
                //         'health-infrastructure.controller',
                //         'hpr-creation-service',
                //         'hfr-search-service',
                //         'hpr-profile-service',
                //         'facility-linking.modal.controller',
                //         'query.service',
                //         'ngInfiniteScroll',
                //         'hospital-type-display-name.filter',
                //         'health-infrastructure-service'
                //     ])
                // })
                // .state('techo.manage.covid19healthinfrastructure', {
                //     url: '/covid-19/healthinfrastructure/:id',
                //     title: 'COVID-19 : Manage Health Infrastructure',
                //     templateUrl: 'app/manage/healthfacilitymapping/views/manage-health-infrastructure.html',
                //     controller: 'ManageHealthInfrastructure as hi',
                //     resolve: load([
                //         'manage-health-infrastructure.controller',
                //         'hpr-creation-service',
                //         'hfr-search-service',
                //         'hpr-profile-service',
                //         'facility-linking.modal.controller',
                //         'query.service',
                //         'location.service',
                //         'hfr-search-service',
                //         'health-infrastructure-service'
                //     ])
                // })
                // .state('techo.manage.createAbhaNumber', {
                //     url: '/createAbhaNumber',
                //     templateUrl: 'app/manage/ndhm/healthid/views/abha-number-creation.html',
                //     title: 'Create And Link Abha Number',
                //     controller: 'AbhaNumberCreationController as ctrl',
                //     resolve: load([
                //         'ndhm-health-id.constants',
                //         'ndhm-health-id-create-service',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-util-service',
                //         'abha-create.controller',
                //         // 'anganwadi.service'
                //     ])
                // })
                // .state('techo.manage.loginHpId', {
                //     url: '/loginHpId/:id',
                //     templateUrl: 'app/manage/ndhm/hpr/views/hpr-login.html',
                //     title: 'Login Healthcare Professional ID',
                //     controller: 'HPRLoginController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-creation-service',
                //         'hpr-profile-service',
                //         'hpr-util-service',
                //         'hpr-auth-service',
                //         'hpr-forgot-service',
                //         'hpr-search-service',
                //         'update-password.modal.controller',
                //         'forgot-healthId.modal.controller',
                //         'reset-password.modal.controller',
                //         'hpr-login.controller'
                //     ])
                // })
                // .state('techo.manage.profileHpId', {
                //     url: '/profileHpId',
                //     templateUrl: 'app/manage/ndhm/hpr/views/hpr-profile.html',
                //     title: 'Profile Healthcare Professional ID',
                //     controller: 'HPRProfileController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-creation-service',
                //         'hpr-profile-service',
                //         'hpr-util-service',
                //         'update-password.modal.controller',
                //         'hpr-profile.controller'
                //     ])
                // })
                // .state('techo.manage.forgotHpId', {
                //     url: '/forgotHpId',
                //     templateUrl: 'app/manage/ndhm/hpr/views/hpr-forgot.html',
                //     title: 'Forgot Healthcare Professional ID',
                //     controller: 'HPRForgotController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-forgot-service',
                //         'hpr-forgot.controller'
                //     ])
                // })
                // .state('techo.manage.createHpId', {
                //     url: '/createHpId/:id',
                //     templateUrl: 'app/manage/ndhm/hpr/views/hp-id-creation.html',
                //     title: 'Create Healthcare Professional ID',
                //     controller: 'HpIdCreationController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-creation-service',
                //         'hpr-profile-service',
                //         'hpr-search-service',
                //         'hpr-util-service',
                //         'hpr-creation-controller'
                //     ])
                // })
                // .state('techo.manage.loginHfrId', {
                //     url: '/loginHfrId',
                //     templateUrl: 'app/manage/ndhm/hfr/views/hfr-login.html',
                //     title: 'Login Health Facility Registry',
                //     controller: 'HFRLoginController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-creation-service',
                //         'hpr-profile-service',
                //         'hpr-util-service',
                //         'hpr-auth-service',
                //         'hpr-forgot-service',
                //         'hpr-search-service',
                        
                //         'update-password.modal.controller',
                //         'forgot-healthId.modal.controller',
                //         'reset-password.modal.controller',
                //         'hfr-login.controller'
                //     ])
                // })
                // .state('techo.manage.dashboardHfrId', {
                //     url: '/dashboardHfrId',
                //     templateUrl: 'app/manage/ndhm/hfr/views/hfr-dashboard.html',
                //     title: 'DashBoard Health Facility Registry',
                //     controller: 'HFRDashboardController as ctrl',
                //     resolve: load([
                //         'hfr-constants',
                //         'hfr-onboarding-service',
                //         'hfr-utility-service',
                //         'hfr-search-service',
                //         'hfr-hprlinkage-service',
                //         'hfr-dashboard.controller'
                //     ])
                // })
                // .state('techo.manage.basicHfrId', {
                //     url: '/basicHfrId/:id',
                //     templateUrl: 'app/manage/ndhm/hfr/views/hfr-basic.html',
                //     title: 'Health Facility Registry',
                //     controller: 'HFRBasicController as ctrl',
                //     resolve: load([
                //         'hpr-constants',
                //         'hpr-creation-service',
                //         'hpr-profile-service',
                //         'hpr-util-service',
                //         'hpr-auth-service',
                //         'hpr-forgot-service',
                //         'hpr-search-service',
                //         'hfr-constants',
                //         'hfr-onboarding-service',
                //         'hfr-utility-service',
                //         'hfr-search-service',
                //         'health-infrastructure-service',
                //         'login-hfr.modal.controller',
                //         'query.service',
                //         'hfr-basic.controller'
                //     ])
                // })
                // .state('techo.ncd', {
                //     url: '/ncd',
                //     template: '<ui-view></ui-view>',
                //     abstract: true
                // })
                // .state('techo.ncd.druginventory', {
                //     url: 'druginventory/:id?type',
                //     title: 'Drug Inventory',
                //     templateUrl: 'app/ncd/druginventory/views/drug-inventory.modal.html',
                //     controller: 'NcdDrugInventoryController as ncddi',
                //     resolve: load([
                //         'ncd-drug-inventory.controller',
                //         'ncd.service',
                //     ])
                // })
                // .state('techo.ncd.moreviewscreen', {
                //     url: '/moreviewscreen/:id',
                //     title: 'MBBS MO Review Screen',
                //     templateUrl: 'app/ncd/MBBSMOReviewScreen/views/ncd-mbbsRS.modal.html',
                //     controller: 'NcdMOReviewScreenController as ncdrs',
                //     resolve: load([
                //         'ncd-moReview-screeen.controller',
                //         'ncd.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'alasql',
                //         'treatment.component',
                //         'date-infra.component'
                //     ])
                // })
                // .state('techo.ncd.moreview', {
                //     url: '/moreview',
                //     title: 'MO Review members',
                //     templateUrl: 'app/ncd/MOReview/views/ncd-mo-review-screen.html',
                //     controller: 'NcdMOReviewController as ctrl',
                //     resolve: load([
                //         'ncd-mo-review.controller',
                //         'ncd.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'alasql'
                //     ])
                // })
                // .state('techo.ncd.moreviewmember', {
                //     url: '/moreview/:id',
                //     title: 'MO Review',
                //     templateUrl: 'app/ncd/MOReview/views/ncd-mo-review-member.html',
                //     controller: 'NcdMOReviewMemberController as ctrl',
                //     resolve: load([
                //         'ncd-mo-review-member.controller',
                //         'ncd.service',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component'
                //     ])
                // })
                // .state('techo.ncd.moreviewfollowup', {
                //     url: '/moreviewfollowup',
                //     title: 'MO Review followup',
                //     templateUrl: 'app/ncd/MOReviewFollowup/views/ncd-mo-review-followup-screen.html',
                //     controller: 'NcdMOReviewFollowupController as ctrl',
                //     resolve: load([
                //         'ncd-mo-review-followup.controller',
                //         'ncd.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'alasql'
                //     ])
                // })
                // .state('techo.ncd.moreviewfollowupmember', {
                //     url: '/moreviewfollowup/:id',
                //     title: 'MO Review followup',
                //     templateUrl: 'app/ncd/MOReviewFollowup/views/ncd-mo-review-followup-member.html',
                //     controller: 'NcdMOReviewFollowupMemberController as ctrl',
                //     resolve: load([
                //         'ncd-mo-review-followup-member.controller',
                //         'ncd.service',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component'
                //     ])
                // })
                // .state('techo.ncd.moreviewpatientsummary', {
                //     url: 'moreviewpatientsummary/:id?type',
                //     title: 'MBBS MO Review Patient Summary',
                //     templateUrl: 'app/ncd/MBBSMOReviewScreen/views/ncd-mbbsmor-patientsummary.modal.html',
                //     controller: 'NcdMOReviewPatientSummaryController as ncdps',
                //     resolve: load([
                //         'ncd-moReview-patientsummary.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component'
                //     ])
                // })
                // .state('techo.ncd.followupscreen', {
                //     url: '/followupscreen/:id?type',
                //     title: 'Consultant Followup Screen',
                //     templateUrl: 'app/ncd/Consultantfollowupscreen/views/con-followup-screen.modal.html',
                //     controller: 'NcdConsultantfollowupScreenController as ncdfs',
                //     resolve: load([
                //         'ncd-followup-screeen.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component'
                //     ])
                // })
                // .state('techo.ncd.followupscreenlisting', {
                //     url: 'followupscreenlisting/:id?type',
                //     title: 'Consultant Followup Screen members',
                //     templateUrl: 'app/ncd/Consultantfollowupscreen/views/ncd-members-fs.modal.html',
                //     controller: 'NcdCFSListingController as ncdfsl',
                //     resolve: load([
                //         'ncd-member-listing-fs.controller',
                //         'ncd.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'alasql'
                //     ])
                // })
                // .state('techo.ncd.memberdetails', {
                //     url: '/member/:id?type',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-main-view.html',
                //     controller: 'NcdMemberDetailController as ncdmd',
                //     abstract: true,
                //     resolve: load([
                //         'ncd-member-detail.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component',
                //         'ncd.constant',
                //         'date-infra.component',
                //         'treatment.component'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.initialassessment', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-initial-assessment.html',
                //     controller: 'NcdInitialAssessment as ctrl',
                //     resolve: load([
                //         'ncd-initial-assessment.controller',
                //         'ncd-member-detail.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service',
                //         'ncd.service',
                //         'referral.component',
                //         'ncd.constant',
                //         'date-infra.component',
                //         'treatment.component'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.hypertension', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-hypertension.html',
                //     controller: 'NcdHypertension as ctrl',
                //     resolve: load([
                //         'ncd-hypertension.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.diabetes', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-diabetes.html',
                //     controller: 'NcdDiabetes as ctrl',
                //     resolve: load([
                //         'ncd-diabetes.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.oralcancer', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-oral-cancer.html',
                //     controller: 'NcdOralCancer as ctrl',
                //     resolve: load([
                //         'ncd-oral-cancer.controller'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.breastcancer', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-breast-cancer.html',
                //     controller: 'NcdBreastCancer as ctrl',
                //     resolve: load([
                //         'ncd-breast-cancer.controller'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.cervicalcancer', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-cervical-cancer.html',
                //     controller: 'NcdCervicalCancer as ctrl',
                //     resolve: load([
                //         'ncd-cervical-cancer.controller'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.mentalhealth', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-mental-health.html',
                //     controller: 'NcdMentalHealth as ctrl',
                //     resolve: load([
                //         'ncd-mental-health.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.general', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-general.html',
                //     controller: 'NcdGeneral as ctrl',
                //     resolve: load([
                //         'ncd-general.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                // .state('techo.ncd.memberdetails.investigation', {
                //     url: '',
                //     title: 'NCD Member Detail',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-investigation.html',
                //     controller: 'NcdInvestigation as ctrl',
                //     resolve: load([
                //         'ncd-investigation.controller',
                //         'ndhm-hip.service',
                //         'ndhm-hip.constants',
                //         'ndhm-health-id-capture-service',
                //         'ndhm-health-id-create-service'
                //     ])
                // })
                // .state('techo.ncd.members', {
                //     url: '/members?type',
                //     title: 'Referred Patients',
                //     templateUrl: 'app/ncd/refferedpatients/views/ncd-members.html',
                //     controller: 'NcdMembersController as mlisting',
                //     resolve: load([
                //         'ncd-members.controller',
                //         'ncd.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //         'alasql',
                //         'titlecase.filter'
                //     ])
                // })
                // .state('techo.ncd.patientSummary', {
                //     url: '/patientSummary/:id',
                //     title: 'Patient Summary',
                //     templateUrl: 'app/ncd/PatientSummary/views/ncd-patient-summary.html',
                //     controller: 'NcdPatientSummary as ctrl',
                //     resolve: load([
                //         'ncd-patient-summary.controller',
                //         'ncd.service'
                //     ])
                // })
                .state('techo.manage.ancSearch', {
                    url: '/ancsearch',
                    title: 'ANC Service Visit',
                    templateUrl: 'app/manage/facilitydataentry/anc/views/anc-search.html',
                    controller: 'ANCSearchController as queform',
                    resolve: load([
                        'anc-search.controller',
                        'anc-form-questions.service',
                        'wpd.service',
                        'ngInfiniteScroll',
                        'paging.service',
                        // 'ndhm-health-id.constants',
                        // 'ndhm-health-id-create-service',
                        // 'ndhm-health-id-capture-service',
                        // 'ndhm-health-id-util-service',
                        // 'ndhm-hiu-controller',
                        // 'ndhm-hip.constants',
                        // 'ndhm-hip-util-service',
                        // 'ndhm-hiu.constants',
                        // 'ndhm-hiu.service',
                        // 'ndhm-hip.service',
                        // 'ndhm-hiu-health-record.service'
                    ])
                })
                // .state('techo.manage.servermanage', {
                //     url: '/servermanage',
                //     title: 'Server management',
                //     templateUrl: 'app/admin/applicationmanagement/servermanagement/views/server-management.html',
                //     controller: 'ServerManagementController as servermanagecontroller',
                //     resolve: load([
                //         'server-management.controller',
                //         'servermanage.service'
                //     ])
                // })
                .state('techo.manage.migrations', {
                    url: '/migrations?selectedIndex&locationId',
                    params: {
                        selectedIndex: {
                            value: '0'
                        },
                        locationId: {
                            value: null
                        }
                    },
                    title: 'Manage Migrations',
                    templateUrl: 'app/manage/membermigration/views/migrations.html',
                    controller: 'MigrationsController as migrations',
                    resolve: load([
                        'migrations.controller',
                        'ngInfiniteScroll',
                        'migration.service',
                        'search-members.controller',
                        'paging.service'
                    ])
                })
                .state('techo.manage.migrationInfomation', {
                    url: '/memberInformation/:memberId?migrationId&locationId',
                    title: 'Manage Migration',
                    params: {
                        locationId: {
                            value: undefined
                        }
                    },
                    templateUrl: 'app/manage/membermigration/views/manage-similar-members.html',
                    controller: 'ManageSimilarMembersController as sm',
                    resolve: load([
                        'ngInfiniteScroll',
                        'mark-as-lfu.modal.controller',
                        'manage-similar-members.controller',
                        'migration.service'
                    ])
                })
                .state('techo.manage.facilityPerformance', {
                    url: '/facilityperformance',
                    title: 'Facility Performance',
                    templateUrl: 'app/manage/facilitydataentry/facilityperformance/views/facility-performance.html',
                    controller: 'FacilityPerformanceController as facilityPerformanceController',
                    resolve: load([
                        'facility-performance.controller',
                        'facility-performance.service'
                    ])
                })
                .state('techo.manage.managevolunteers', {
                    url: '/managevolunteers',
                    title: 'Manage Volunteers',
                    templateUrl: 'app/manage/views/manage-volunteers.html',
                    controller: 'ManageVolunteersController as managevolunteerscontroller',
                    resolve: load([
                        'manage-volunteers.controller',
                        'manage-volunteers.service'
                    ])
                })
                .state('techo.manage.manage-widgets', {
                    url: '/widgets',
                    title: 'Manage Widgets',
                    templateUrl: 'app/admin/applicationmanagement/widgetmanagement/views/widget-management.html',
                    controller: 'ManageWidgetsController as manageWidgetsController',
                    resolve: load([
                        'widget-management.controller',
                        'notification.service',
                        'statecapitalize.filter',
                        'selectize.generator'
                    ])
                })
                .state('techo.manage.expectedTarget', {
                    url: '/expectedtarget',
                    title: 'Location Wise Expected Target',
                    templateUrl: 'app/manage/administration/locationwiseexpectedtarget/views/expected-target.html',
                    controller: 'ExpectedTargetController as expectedTargetController',
                    resolve: load([
                        'expected-target.controller',
                        'expected-target.service',
                        'alasql'
                    ])
                })
                .state('techo.manage.searchMembers', {
                    url: '/searchMembers/:migrationId',
                    title: 'Search Members',
                    templateUrl: 'app/common/views/search-members.html',
                    controller: 'SearchMembersController as searchMembers',
                    resolve: load([
                        'ngInfiniteScroll',
                        'search-members.controller',
                        'migration.service',
                        'paging.service'
                    ])
                })
                // .state('techo.manage.userhealthapproval', {
                //     url: '/userhealthapproval',
                //     title: 'State Of Health User Approval',
                //     templateUrl: 'app/manage/sohuserapproval/views/soh-user-approval.html',
                //     controller: 'UserHealthApprovalsController as uha',
                //     resolve: load([
                //         'soh-user-approval.controller',
                //         'soh-user-approve.modal.controller',
                //         'soh-user-approval.service',
                //         'soh-user-disapprove.modal.controller'
                //     ])
                // })
                // .state('techo.manage.timelineconfig', {
                //     url: '/timelineconfig/:id',
                //     title: 'Add Timeline',
                //     templateUrl: 'app/admin/mytecho/timelineconfig/views/manage-timeline-config.html',
                //     controller: 'ManageTimelineConfigController as manageTimelineConfigController',
                //     resolve: load([
                //         'manage-timeline-config.controller',
                //         'timeline-config.service',
                //         'query.service',
                //         'announcement.service',
                //     ])
                // })
                // .state('techo.manage.timelineconfigs', {
                //     url: '/timelineconfigs',
                //     title: 'List of Timelines',
                //     templateUrl: 'app/admin/mytecho/timelineconfig/views/timeline-config.html',
                //     controller: 'TimelineConfigcontroller as timelineConfigcontroller',
                //     resolve: load([
                //         'timeline-config.controller',
                //         'timeline-config.service',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.cardconfig', {
                //     url: '/cardconfig/:id',
                //     title: 'Add Card',
                //     templateUrl: 'app/admin/mytecho/timelineconfig/views/manage-timeline-config.html',
                //     controller: 'ManageTimelineConfigController as manageTimelineConfigController',
                //     resolve: load([
                //         'manage-timeline-config.controller',
                //         'timeline-config.service',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.cardconfigs', {
                //     url: '/cardconfigs',
                //     title: 'List of Cards',
                //     templateUrl: 'app/admin/mytecho/timelineconfig/views/timeline-config.html',
                //     controller: 'TimelineConfigcontroller as timelineConfigcontroller',
                //     resolve: load([
                //         'timeline-config.controller',
                //         'timeline-config.service',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.tipoftheday', {
                //     url: '/tipoftheday/:id',
                //     title: 'Add Tip Of The Day',
                //     templateUrl: 'app/admin/mytecho/tipoftheday/views/manage-tip-of-the-day.html',
                //     controller: 'ManageTipOfTheDayController as manageTipOfTheDayController',
                //     resolve: load([
                //         'manage-tip-of-the-day.controller',
                //         'timeline-config.service',
                //         'query.service',
                //         'tip-of-the-day.constant'
                //     ])
                // })
                // .state('techo.manage.tipsoftheday', {
                //     url: '/tipsoftheday',
                //     title: 'Tips Of The Day',
                //     templateUrl: 'app/admin/mytecho/tipoftheday/views/tip-of-the-day.html',
                //     controller: 'TipOfTheDayController as tipOfTheDayController',
                //     resolve: load([
                //         'tip-of-the-day.controller',
                //         'timeline-config.service',
                //         'query.service',
                //         'tip-of-the-day.constant'
                //     ])
                // })
                // .state('techo.manage.contentPreviewConfig', {
                //     url: '/contentpreviewconfig',
                //     title: 'Content Preview Config',
                //     templateUrl: 'app/admin/mytecho/contentpreviewconfig/views/content-preview-config.html',
                //     controller: 'ContentPreviewConfig as contentPreviewConfig',
                //     resolve: load([
                //         'contentPreviewConfig.controller',
                //         'contentPreviewConfig.service',
                //     ])
                // })
                .state('techo.manage.queryexcelsheetgenerator', {
                    url: '/queryexcelsheetgenerator',
                    title: 'Generate Excel Sheet',
                    templateUrl: 'app/admin/applicationmanagement/queryexcelsheetgenerator/views/query-excel-sheet-generator.html',
                    controller: 'ExcelGenereatorController as excelGenereatorController',
                    resolve: load([
                        'query-excel-sheet-generator.controller',
                        'query.service',
                        'report.service',
                    ])
                })
                // .state('techo.manage.faq', {
                //     url: '/faq/:id',
                //     title: 'Add Question',
                //     templateUrl: 'app/admin/mytecho/faq/views/manage-faq-view.html',
                //     controller: 'ManageFAQViewController as addQuestion',
                //     resolve: load([
                //         'manage-faq-view.controller',
                //         'faq.service'
                //     ])
                // })
                // .state('techo.manage.faqs', {
                //     url: '/faqs',
                //     title: 'Frequently Asked Questions',
                //     templateUrl: 'app/admin/mytecho/faq/views/faq-view.html',
                //     controller: 'FAQViewController as faqView',
                //     resolve: load([
                //         'faq-view.controller',
                //         'faq.service'
                //     ])
                // })
                // .state('techo.manage.staffsmsconfig', {
                //     url: '/staffsmsconfig/:id',
                //     title: 'Add Staff Sms Config',
                //     templateUrl: 'app/manage/staffsmsconfig/views/manage-staff-sms-config.html',
                //     controller: 'ManageStaffSmsConfigController as staffSmsConfig',
                //     resolve: load([
                //         'user.service',
                //         'manage-staff-sms-config.controller',
                //         'role.service',
                //         'authentication.service',
                //         'query.service',
                //         'staff-sms-config.service',
                //         'dynamicview.directive',
                //         'staff-sms-config.constant'
                //     ])
                // })
                // .state('techo.manage.staffsmsconfigs', {
                //     url: '/staffsmsconfigs',
                //     title: 'List Of Staff Sms',
                //     templateUrl: 'app/manage/staffsmsconfig/views/staff-sms-config.html',
                //     controller: 'StaffSmsController as staffSmsConf',
                //     resolve: load([
                //         'staff-sms-config.controller',
                //         'authentication.service',
                //         'staff-sms-config.service',
                //         'user-list.modal.controller'
                //     ])
                // })
                .state('techo.manage.team', {
                    url: '/team/:id',
                    title: 'Add Team',
                    templateUrl: 'app/manage/teamconfig/views/manage-team.html',
                    controller: 'ManageTeamController as teamController',
                    resolve: load([
                        'manage-team.controller',
                        'user.service',
                        'role.service',
                        'authentication.service',
                        'query.service',
                        'selectize.generator',
                        'team-config.service'
                    ])
                })
                .state('techo.manage.teams', {
                    url: '/teams',
                    title: 'Manage Teams',
                    templateUrl: 'app/manage/teamconfig/views/team-list.html',
                    controller: 'TeamListController as teamCtrl',
                    resolve: load([
                        'team-config.service',
                        'team-list.controller',
                        'authentication.service'
                    ])
                })
                .state('techo.manage.teamtype', {
                    url: '/teamtype/:id',
                    title: 'Add Team Type',
                    templateUrl: 'app/admin/teamtypeconfig/views/manage-team-type-config.html',
                    controller: 'ManageTeamTypeConfigController as ctrl',
                    resolve: load([
                        'manage-team-type-config.controller',
                        'role.service',
                        'query.service',
                        'team-type-config.service'
                    ])
                })
                .state('techo.manage.teamtypes', {
                    url: '/teamtypes',
                    title: 'Team Types',
                    templateUrl: 'app/admin/teamtypeconfig/views/team-type-config.html',
                    controller: 'TeamTypeConfigListController as ctrl',
                    resolve: load([
                        'team-type-config.service',
                        'query.service',
                        'team-type-config.controller',
                        'authentication.service'
                    ])
                })
                .state('techo.manage.touristscreening', {
                    url: '/chardham/touristscreening/:id',
                    title: 'Tourist Screening',
                    templateUrl: 'app/manage/chardham/views/tourist-screening.html',
                    controller: 'TouristScreeningController as ctrl',
                    resolve: load([
                        'tourist-screening.controller'
                    ])
                })
                .state('techo.manage.systemconfigs', {
                    url: 'systemconfigs',
                    title: 'System Config List',
                    templateUrl: 'app/admin/applicationmanagement/systemconfig/views/system-config.html',
                    controller: 'SystemConfigListController as ctrl',
                    resolve: load([
                        'system-config.controller',
                        'query.service'
                    ])
                })
                .state('techo.manage.systemconfig', {
                    url: '/admin/applicationmanagement/systemconfig/:key',
                    title: 'Manage System Config',
                    templateUrl: 'app/admin/applicationmanagement/systemconfig/views/manage-system-config.html',
                    controller: 'ManageSystemConfigController as ctrl',
                    resolve: load([
                        'manage-system-config.controller',
                        'query.service'
                    ])
                })
                .state('techo.manage.dynamicform', {
                    url: '/dynamicform/:formId',
                    title: 'Dynamic Form Builder',
                    templateUrl: 'app/admin/dynamicformbuilder/views/dynamic-form.html',
                    controller: 'DynamicFormBuilderController as dFormCtrl',
                    resolve: load([
                        'dynamic-form.controller',
                        'query.service'
                    ])
                })
                .state('techo.manage.dynamicformconfig', {
                    url: '/dynamicformconfig/:formConfigId/:formId',
                    title: 'Dynamic Form Builder',
                    templateUrl: 'app/admin/dynamicformbuilder/views/manage-dynamic-form.html',
                    controller: 'ManageDynamicFormBuilderController as mdFormCtrl',
                    resolve: load([
                        'manage-dynamic-form.controller',
                        'query.service'
                    ])
                })
                .state('techo.dashboard.callcenter', {
                    url: '/callcenter',
                    title: 'Call Center Dashboard',
                    templateUrl: 'app/admin/gvkcallcenter/dashboard/views/call-center-dashboard.html',
                    controller: 'CallCenterDashBoardController as callCenterDashBoardController',
                    resolve: load([
                        'call-center-dashboard.controller',
                        'call-center-dashboard.constant'
                    ])
                })
                .state('techo.dashboard.gvksuccessfulcalldashboard', {
                    url: '/gvksuccessfulcalldashboard',
                    title: 'GVK Successful Call Dashboard',
                    templateUrl: 'app/admin/gvkcallcenter/dashboard/views/callCenterDashBoard.html',
                    controller: 'CallCenterDashBoardController as callCenterDashBoardController',
                    resolve: load([
                        'callCenterDashBoard.controller',
                        'callCenterDashBoard.constant'
                    ])
                })
                // .state('techo.manage.configureRbskDefects', {
                //     url: '/configureRbskDefects',
                //     title: 'RBSK Defects Configuration',
                //     templateUrl: 'app/admin/rbsk/views/defects-list.html',
                //     controller: 'ConfigureRbskDefects as defectsConfiguration',
                //     resolve: load([
                //         'configureRbskDefects.controller',
                //         'paging.service',
                //         'rbsk.service',
                //         'manageDefect.constants'
                //     ])
                // })
                // .state('techo.manage.defect', {
                //     url: '/defect/:id',
                //     title: 'Manage Defect',
                //     templateUrl: 'app/admin/rbsk/views/defect-manage.html',
                //     controller: 'ManageDefect as manageDefect',
                //     resolve: load([
                //         'manageDefect.controller',
                //         'paging.service',
                //         'rbsk.service',
                //         'reverseIterate.filter',
                //         'stabilizationModal.controller',
                //         'manageDefect.constants',
                //         'drtecho.service'
                //     ])
                // })
                .state('techo.manage.schools', {
                    url: '/schools',
                    templateUrl: 'app/manage/schools/views/schools.html',
                    title: 'Manage Schools',
                    controller: 'SchoolController as schoolCtrl',
                    resolve: load([
                        'school.controller',
                        'query.service',
                        'ngInfiniteScroll'
                    ])
                })
                .state('techo.manage.school', {
                    url: '/school/:id',
                    templateUrl: 'app/manage/schools/views/manage-school.html',
                    title: 'Manage Schools',
                    controller: 'ManageSchoolController as manageSchoolCtrl',
                    resolve: load([
                        'manage-school.controller',
                        'query.service'
                    ])
                })
                // .state('techo.manage.sohElementConfiguration', {
                //     url: '/sohelementconfiguration?selectedTab',
                //     params: {
                //         selectedTab: {
                //             value: '0'
                //         }
                //     },
                //     title: 'SoH Element Configuration',
                //     templateUrl: 'app/admin/soh/views/element-configuration.html',
                //     controller: 'SohElementConfiguration as sohElementConfiguration',
                //     resolve: load([
                //         'sohElementConfiguration.controller',
                //         'sohElementConfiguration.service',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.manageSohElementConfiguration', {
                //     url: '/sohelementconfiguration/manage/:id',
                //     title: 'Manage SoH Element Configuration',
                //     templateUrl: 'app/admin/soh/views/manage-element-configuration.html',
                //     controller: 'ManageSohElementConfiguration as manageSohElementConfiguration',
                //     resolve: load([
                //         'manageSohElementConfiguration.controller',
                //         'sohElementConfiguration.service',
                //         'query.service',
                //         'reverseIterate.filter',
                //         'sohelement.constants',
                //         'drtecho.service'
                //     ])
                // })
                // .state('techo.manage.manageSohChartConfiguration', {
                //     url: '/sohchartconfiguration/manage/:id',
                //     title: 'Manage SoH Chart Configuration',
                //     templateUrl: 'app/admin/soh/views/manage-chart-configuration.html',
                //     controller: 'ManageSohChartConfiguration as ctrl',
                //     resolve: load([
                //         'manageSohChartConfiguration.controller',
                //         'sohElementConfiguration.service',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.manageSohElementModuleConfiguration', {
                //     url: '/sohelementmoduleconfiguration/manage/:id',
                //     title: 'Manage SoH Element Module Configuration',
                //     templateUrl: 'app/admin/soh/views/manage-element-module-configuration.html',
                //     controller: 'ManageSohElementModuleConfiguration as ctrl',
                //     resolve: load([
                //         'manageSohElementModuleConfiguration.controller',
                //         'sohElementConfiguration.service',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.sohApp', {
                //     url: '/sohApplication',
                //     title: 'State Of Health Application',
                //     templateUrl: 'app/admin/soh/views/soh-application.html',
                //     controller: 'SohApp as ctrl',
                //     resolve: load([
                //         'sohApp.controller'
                //     ])
                // })
                // .state('techo.manage.wards', {
                //     url: '/wards',
                //     templateUrl: 'app/manage/rchdatapush/wards/views/wards.html',
                //     title: 'Manage Location Wards',
                //     controller: 'WardController as ctrl',
                //     resolve: load([
                //         'ward.controller',
                //         'query.service',
                //         'ngInfiniteScroll'
                //     ])
                // })
                // .state('techo.manage.ward', {
                //     url: '/ward/:id',
                //     templateUrl: 'app/manage/rchdatapush/wards/views/manage-wards.html',
                //     title: 'Manage Location Wards',
                //     controller: 'ManageWardController as ctrl',
                //     resolve: load([
                //         'manage-ward.controller',
                //         'query.service',
                //         'location.service',
                //     ])
                // })
                // .state('techo.manage.rchLocations', {
                //     url: '/rchLocation',
                //     title: 'RCH Locations',
                //     templateUrl: 'app/manage/rchdatapush/rchlocations/views/rch-location.html',
                //     controller: 'RchLocationController as ctrl',
                //     resolve: load([
                //         'rch-location.controller',
                //         'query.service',
                //         'ngInfiniteScroll',
                //     ])
                // })
                .state('techo.manage.featureUsage', {
                    url: '/featureusage',
                    templateUrl: 'app/admin/featureusage/views/feature-usage-new.html',
                    title: 'Feature Usage Analytics',
                    controller: 'FeatureUsageController as ctrl',
                    resolve: load([
                        'feature-usage.controller',
                        'query.service',
                        'roles.service',
                        'feature-usage.constant'
                    ])
                })
                .state('techo.manage.featureSync', {
                    url: '/applicationmanagement/featureSync',
                    templateUrl: 'app/admin/applicationmanagement/featuresync/views/feature-sync.html',
                    title: 'Manage Feature Syncing',
                    controller: 'FeatureSyncController as featureCtrl',
                    resolve: load([
                        'feature-sync.controller',
                        'paging.service',
                        'ngInfiniteScroll',
                    ])
                })
                .state('techo.manage.syncServerManage', {
                    url: '/applicationmanagement/syncServerManage',
                    templateUrl: 'app/admin/applicationmanagement/syncServerManage/views/sync-server-manage.html',
                    title: 'Sync Server Manage',
                    controller: 'syncServerManageController as syncServerCtrl',
                    resolve: load([
                        'syncServerManage.controller',
                    ])
                })
                .state('techo.manage.manageOpdLabTest', {
                    url: '/labTestList',
                    title: 'OPD Lab Test List',
                    templateUrl: 'app/admin/manageopd/opd/views/lab-test-list.html',
                    controller: 'LabTestList as labTestConfiguration',
                    resolve: load([
                        'labTestList.controller',
                        'paging.service'
                    ])
                })
                .state('techo.manage.opdLabTest', {
                    url: '/manageLabTest/:id',
                    title: 'Manage OPD Lab Test',
                    templateUrl: 'app/admin/manageopd/opd/views/lab-test-manage.html',
                    controller: 'LabTestManage as manageLabTest',
                    resolve: load([
                        'manageLabTest.controller',
                        'paging.service',
                        'labTest.service'
                    ])
                })
                // .state('techo.manage.dtcInterface', {
                //     url: '/covid-19/dtcInterface',
                //     title: 'COVID-19 - DTC Interface',
                //     templateUrl: 'app/manage/covid19/dtcinterface/views/dtc-interface.html',
                //     controller: 'DtcInterface as ctrl',
                //     resolve: load([
                //         'dtc-interface.controller',
                //         'query.service',
                //         'ngInfiniteScroll',
                //     ])
                // })
                // .state('techo.manage.searchcovidtechomembers', {
                //     url: '/covid-19/search',
                //     title: 'Search Techo Members',
                //     templateUrl: 'app/manage/covid19/hospitaladmission/views/search-covid-member.html',
                //     controller: 'SearchCovidController as searchcovid',
                //     resolve: load([
                //         'search-covid-member.controller',
                //         'authentication.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //     ])
                // })
                // .state('techo.manage.covidtravellers', {
                //     url: '/covid-19/covidtravellers',
                //     title: 'Covid Travellers Screening',
                //     templateUrl: 'app/manage/covid19/travellerscreening/views/traveller-screening.html',
                //     controller: 'CovidTravellersScreening as covidscreening',
                //     resolve: load([
                //         'traveller-screening.controller',
                //         'covid.service',
                //         'authentication.service'
                //     ])
                // })
                // .state('techo.manage.covidcases', {
                //     url: '/covid-19/covidcases',
                //     title: 'COVID-19 Contact Tracing',
                //     templateUrl: 'app/manage/covid19/contacttracing/views/contact-tracing.html',
                //     controller: 'CovidCasesController as covidCasesCtrl',
                //     resolve: load([
                //         'contact-tracing.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.manageCovidCases', {
                //     url: '/covid-19/manageCovidCases/:id',
                //     title: 'Manage COVID-19 Cases',
                //     templateUrl: 'app/manage/covid19/contacttracing/views/manage-contact-tracing.html',
                //     controller: 'ManageCovidCasesController as manageCovidCasesCtrl',
                //     resolve: load([
                //         'manage-contact-tracing.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.manageCovidTravelHistory', {
                //     url: '/covid-19/managecovidtravelhistory/:contactId',
                //     title: 'Manage COVID-19 Travel History',
                //     templateUrl: 'app/manage/covid19/contacttracing/views/manage-travel-history.html',
                //     controller: 'ManageCovidTravelHistoryController as manageCovidTravelHistoryCtrl',
                //     resolve: load([
                //         'manage-travel-history.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.covidAdmission', {
                //     url: '/covid-19/covidadmission',
                //     templateUrl: 'app/manage/covid19/hospitaladmission/views/admission.html',
                //     title: 'Hospital Admission',
                //     controller: 'CovidTravellersAdmission as covidAdmissionctrl',
                //     resolve: load([
                //         'admission.controller',
                //         'query.service',
                //         'covid.service',
                //         'daily-checkup.modal.controller',
                //         'paging.service',
                //         'ngInfiniteScroll',
                //         'discharge.modal.controller',
                //         'refer-in-admit.modal.controller'
                //     ])
                // })
                // .state('techo.manage.covidLabTestEntry', {
                //     url: '/covid-19/covidlabtestentry',
                //     templateUrl: 'app/manage/covid19/opdlabtest/views/opd-lab-test.html',
                //     title: 'OPD Lab Test Entry',
                //     controller: 'CovidLabTestEntry as covidLabTestEntryCtrl',
                //     resolve: load([
                //         'opd-lab-test.controller',
                //         'query.service',
                //         'covid.service',
                //         'paging.service',
                //         'ngInfiniteScroll',
                //     ])
                // })
                // .state('techo.manage.labTestAdmin', {
                //     url: '/covid-19/labtestadmin',
                //     templateUrl: 'app/manage/covid19/labtestadmin/views/lab-test-admin.html',
                //     title: 'COVID-19 Lab Test Admin',
                //     controller: 'LabTestAdminController as lbtCtrl',
                //     resolve: load([
                //         'lab-test-admin.controller',
                //         'edit-admission.modal.controller',
                //         'edit-opd-lab-test.modal.controller',
                //         'trasfer-hospital.modal.controller',
                //         'query.service',
                //         'covid.service',
                //         'paging.service',
                //     ])
                // })
                // .state('techo.manage.104calls', {
                //     url: '/covid-19/104calls',
                //     title: '104 Calls',
                //     templateUrl: 'app/manage/covid19/104call/views/104-calls.html',
                //     controller: 'Calls104Controller as calls104Ctrl',
                //     resolve: load([
                //         '104-calls.controller',
                //         'query.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //     ])
                // })
                // .state('techo.manage.manage104Calls', {
                //     url: '/covid-19/manage104calls/:id',
                //     title: 'Manage 104 Calls',
                //     templateUrl: 'app/manage/covid19/104call/views/manage-104-calls.html',
                //     controller: 'Manage104CallsController as manage104CallsCtrl',
                //     resolve: load([
                //         'manage-104-calls.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.emodashboard', {
                //     url: '/covid-19/emodashboard',
                //     title: 'Covid2019 Suspect List',
                //     templateUrl: 'app/manage/covid19/suspectedlist/views/suspected-list.html',
                //     controller: 'EmoDashboardController as emo',
                //     resolve: load([
                //         'suspected-list.controller',
                //         'covid.service',
                //         'query.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //     ])
                // })
                // .state('techo.manage.labtest', {
                //     url: '/covid-19/labtest',
                //     title: 'Lab',
                //     templateUrl: 'app/manage/covid19/labtest/views/labtest-dashboard.html',
                //     controller: 'LabTestController as labtest',
                //     resolve: load([
                //         'labtest-dashboard.controller',
                //         'covid.service',
                //         'query.service',
                //         'ngInfiniteScroll',
                //         'paging.service',
                //     ])
                // })
                // .state('techo.manage.labinfrastructure', {
                //     url: '/covid-19/labinfrastructure',
                //     title: 'Manage Lab Infrastructure',
                //     templateUrl: 'app/manage/covid19/labinfrastructure/views/lab-infras.html',
                //     controller: 'Labinfrastructures as labinfrastructures',
                //     resolve: load([
                //         'lab-infras.controller',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.updatelabinfrastructure', {
                //     url: '/covid-19/labinfrastructure/:id',
                //     title: 'Manage Lab Infrastructure',
                //     templateUrl: 'app/manage/covid19/labinfrastructure/views/update-lab-infras.html',
                //     controller: 'Labinfrastructure as labinfrastructure',
                //     resolve: load([
                //         'update-lab-infras.controller',
                //         'query.service',
                //     ])
                // })
                .state('techo.manage.idspFormS', {
                    url: '/idsp/form-s',
                    title: 'IDSP Form S',
                    templateUrl: 'app/manage/idsp/views/form-s.html',
                    controller: 'IDSPFormS as ctrl',
                    resolve: load([
                        'idspFormS.controller',
                        'query.service',
                        'idsp.service',
                        'idspFormS.constants'
                    ])
                })
                .state('techo.manage.systemcode', {
                    url: '/systemcode',
                    title: 'System Codes',
                    templateUrl: 'app/manage/systemcode/views/manage-codes.html',
                    controller: 'SystemCodeController as ctrl',
                    resolve: load([
                        'manage-codes.controller',
                        'query.service',
                        'servermanage.service',
                        'manage-codes.service',
                        'edit-code.modal.controller'
                    ])
                })
                // .state('techo.manage.covid19Dashboard', {
                //     url: '/covid-19/dashboard',
                //     title: 'COVID-19 Rapid Response Dashboard',
                //     templateUrl: 'app/manage/covid19/rapidresponsedashboard/views/rapid-response-dashboard.html',
                //     controller: 'Covid19DashboardController as covid19Dashboardctrl',
                //     resolve: load([
                //         'rapid-response-dashboard.controller',
                //         'query.service',
                //     ])
                // })
                // .state('techo.manage.symptomaticMembersHeatmap', {
                //     url: '/covid-19/symptomaticMembersHeatmap',
                //     title: "Spatial Distribution Of Cases",
                //     templateUrl: 'app/manage/covid19/symptomaticmembersheatmap/views/symptomatic-members-heatmap.html',
                //     controller: 'SymptomaticMembersHeatmapController as heatMapCtrl',
                //     resolve: load([
                //         'symptomatic-members-heatmap.controller',
                //         'query.service',
                //         'alasql'
                //     ])
                // })
                .state('techo.manage.chardhamHeatMap', {
                    url: '/chardham/heatmap',
                    title: 'Chardham Heat Map',
                    templateUrl: 'app/manage/chardham/views/chardham-heat-map.html',
                    controller: 'ChardhamHeatMapController as ctrl',
                    resolve: load([
                        'chardham-heatmap.controller',
                        'query.service',
                        'alasql'
                    ])
                })
                // .state('techo.manage.covid19AdmissionReport', {
                //     url: '/covid-19/form-s/:id',
                //     title: 'Patient Case Sheet',
                //     templateUrl: 'app/manage/covid19/hospitaladmission/views/admission-report.html',
                //     controller: 'Covid19AdmissionReport as ctrl',
                //     resolve: load([
                //         'admission-report.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.locationClusterManagement', {
                //     url: '/covid-19/locationClusterManagement',
                //     title: 'COVID 19 Cluster Management',
                //     templateUrl: 'app/manage/covid19/locationcluster/views/location-cluster.html',
                //     controller: 'LocationClusterMangementCtrl as locationClusterMangementCtrl',
                //     resolve: load([
                //         'location-cluster.controller',
                //         'query.service'
                //     ])
                // })
                // .state('techo.manage.manageLocationClusterManagement', {
                //     url: '/covid-19/manageLocationClusterManagement/:id',
                //     title: 'COVID 19 Cluster Management',
                //     templateUrl: 'app/manage/covid19/locationcluster/views/manage-location-cluster.html',
                //     controller: 'ManagelocationClusterMangement as manageLocationClusterMangementCtrl',
                //     resolve: load([
                //         'manage-location-cluster.controller',
                //         'query.service',
                //         'user.service'
                //     ])
                // })
                .state('techo.manage.updateFamilyAreaList', {
                    url: '/update-family-area',
                    title: 'Update Family Area List',
                    templateUrl: 'app/manage/administration/updatefamilyarea/views/update-family-area-list.html',
                    controller: 'UpdateFamilyAreaList as ctrl',
                    resolve: load([
                        'update-family-area-list.controller',
                        'query.service',
                        'ngInfiniteScroll',
                    ])
                })
                .state('techo.admin.systemConstraints', {
                    url: '/system-constraints?selectedTab',
                    params: {
                        selectedTab: {
                            value: 'manage-form-configs'
                        }
                    },
                    title: 'Form Configurator',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/system-constraints.html',
                    controller: 'SystemConstraints as ctrl',
                    resolve: load([
                        'system-constraints.controller',
                        'system-constraint.service',
                        'statecapitalize.filter',
                    ])
                })
                .state('techo.admin.manageSystemConstraintForm', {
                    url: '/system-constraint/form/:uuid',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/manage-system-constraint-form.html',
                    title: 'Manage Form',
                    controller: 'ManageSystemConstraintForm as ctrl',
                    resolve: load([
                        'manage-system-constraint-form.controller',
                        'system-constraint.service',
                    ])
                })
                .state('techo.admin.manageSystemConstraintForms', {
                    url: '/system-constraint/forms?menuConfigId',
                    params: {
                        menuConfigId: {
                            value: null
                        }
                    },
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/manage-system-constraint-form.html',
                    title: 'Manage Forms',
                    controller: 'ManageSystemConstraintForm as ctrl',
                    resolve: load([
                        'manage-system-constraint-form.controller',
                        'system-constraint.service',
                    ])
                })
                .state('techo.admin.configureDynamicTemplate', {
                    url: '/system-constraint/form/dynamic-template/:uuid',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/configure-dynamic-template.html',
                    title: 'Configure Dynamic Template',
                    controller: 'ConfigureDynamicTemplate as ctrl',
                    resolve: load([
                        'configure-dynamic-template.controller',
                        'configure-dynamic-template.directives',
                        'system-constraint.service',
                    ])
                })
                .state('techo.admin.configureMobileTemplate', {
                    url: '/system-constraint/form/mobile-template/:uuid',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/configure-mobile-template.html',
                    title: 'Configure Mobile Template',
                    controller: 'ConfigureMobileTemplate as ctrl',
                    resolve: load([
                        'configure-mobile-template.controller',
                        'system-constraint.service',
                        'dndLists'
                    ])
                })
                .state('techo.admin.manageSystemConstraintStandard', {
                    url: '/system-constraint/standard/:id',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/manage-system-constraint-standard.html',
                    title: 'Manage Form Standard',
                    controller: 'ManageSystemConstraintStandard as ctrl',
                    resolve: load([
                        'manage-system-constraint-standard.controller',
                        'system-constraint.service',
                    ])
                })
                .state('techo.admin.manageSystemConstraintStandardField', {
                    url: '/system-constraint/standard-field/:uuid',
                    templateUrl: 'app/admin/applicationmanagement/managesystemconstraints/views/manage-system-constraint-standard-field.html',
                    title: 'Manage Form Standard Field',
                    controller: 'ManageSystemConstraintStandardField as ctrl',
                    resolve: load([
                        'manage-system-constraint-standard-field.controller',
                        'system-constraint.service',
                    ])
                })
                .state('techo.manage.healthInfrastructureType', {
                    url: '/health-infrastructure-type/:id',
                    title: 'Health Infrastructure Type Mapping',
                    templateUrl: 'app/manage/healthfacilitymapping/views/manage-health-infrastructure-type.html',
                    controller: 'ManageHealthInfrastructureType as ctrl',
                    resolve: load([
                        'manage-health-infrastructure-type.controller',
                        'query.service',
                        'location.service',
                        'update-health-infrastructure-mapping.controller'
                    ])
                })
                .state('techo.manage.memberInfoChangeAuditLog', {
                    url: '/member-info-change-audit-log',
                    title: 'Member Details Audit Log',
                    templateUrl: 'app/manage/helpdesktool/views/member-information-change-audit-log.html',
                    controller: 'MemberInfoChangeAuditLogController as ctrl',
                    resolve: load([
                        'member-information-change-audit-log.controller',
                        'query.service',
                        'drtecho.service',
                        'ngInfiniteScroll',
                    ])
                })
                .state('techo.manage.reportoffline', {
                    url: '/report-offline',
                    title: 'Download Report Offline',
                    templateUrl: 'app/manage/reportoffline/views/report-offline.html',
                    controller: 'ReportOfflineCtrl as reportOfflineCtrl',
                    resolve: load([
                        'report-offline.controller',
                        'report-offline.constant',
                        'report.service',
                        'servermanage.service'
                    ])
                }).state('techo.manage.performancedashboard', {
                    url: '/dashboardpoc',
                    title: 'Performance Dashboard',
                    templateUrl: 'app/manage/performancedashboard/views/performance-dashboard.html',
                    controller: 'DashboardController as dashboardCtrl',
                    resolve: load([
                        'performance-dashboard.controller',
                        'performance-dashboard.constant',
                        'query.service',
                        'location.service',
                    ])
                })
                .state('techo.manage.mobileFeatureManagement', {
                    url: '/mobile-feature-management',
                    title: 'Mobile Feature Management',
                    templateUrl: 'app/manage/mobileFeatureManagement/views/mobile-feature-management.html',
                    controller: 'MobileFeatureManagementController as ctrl',
                    resolve: load([
                        'mobile-feature-management.controller',
                        'query.controller',
                        'query.service',
                        'authentication.service',
                        'mobile-feature.modal.controller',
                    ])
                })
                .state('techo.manage.mobileMenuManagement', {
                    url: '/mobile-menu-management',
                    title: 'Mobile Menu Management',
                    templateUrl: 'app/manage/mobileMenuManagement/views/mobile-menu-management.html',
                    controller: 'MobileMenuManagementController as ctrl',
                    resolve: load([
                        'mobile-menu-management.controller',
                        'query.controller',
                        'query.service',
                        'authentication.service',
                    ])
                })
                .state('techo.manage.mobileMenuConfig', {
                    url: '/mobile-menu-config/:id',
                    title: 'Mobile Menu Configuration',
                    templateUrl: 'app/manage/mobileMenuManagement/views/mobile-menu-config.html',
                    controller: 'MobileMenuConfigController as ctrl',
                    resolve: load([
                        'mobile-menu-config.controller',
                        'query.controller',
                        'query.service',
                        'authentication.service',
                        'dndLists',
                        'update-feature.modal.controller',
                    ])
                })
                .state('techo.manage.locationtype', {
                    url: '/locationtype',
                    title: 'Location Type',
                    templateUrl: 'app/manage/locationtype/views/location-type.html',
                    controller: 'LocationTypeController as locationTypeCtrl',
                    resolve: load([
                        'location-type.controller',
                        'query.service',
                        'location.service',
                    ])
                })
                .state('techo.manage.managelocationtype', {
                    url: '/managelocationtype/:id',
                    title: 'Manage Location Type',
                    templateUrl: 'app/manage/locationtype/views/manage-location-type.html',
                    controller: 'ManageLocationTypeController as manageLocationTypeCtrl',
                    resolve: load([
                        'manage-location-type.controller',
                        'query.service',
                        'location.service',
                    ])
                }).state('techo.manage.uploaduser', {
                    url: '/uploadUser',
                    title: 'Upload User',
                    templateUrl: 'app/manage/uploaduser/views/upload-user.html',
                    controller: 'UploadUserController as ctrl',
                    resolve: load([
                        'upload-user.controller',
                        'location.service',
                        'upload-user.service',
                        'roles.service',
                        'authentication.service',
                    ])
                })
                // .state('techo.manage.pushnotificationtype', {
                //     url: '/push/type',
                //     title: 'List of Push Notification Type',
                //     templateUrl: 'app/admin/pushnotification/views/push-type.html',
                //     controller: 'PushNotificationTypeController as ctrl',
                //     resolve: load([
                //         'push-type.controller',
                //         'push-notification.service'
                //     ])
                // })
                .state('techo.manage.managepushnotificationtype', {
                    url: '/push/type/:id',
                    title: 'Manage Push Notification Type',
                    templateUrl: 'app/admin/pushnotification/views/manage-push-type.html',
                    controller: 'ManagePushNotificationTypeController as ctrl',
                    resolve: load([
                        'manage-push-type.controller',
                        'push-notification.service',
                        'drtecho.service'
                    ])
                }).state('techo.manage.pushnotification', {
                    url: '/push',
                    title: 'List of Push Notification',
                    templateUrl: 'app/admin/pushnotification/views/push-notification.html',
                    controller: 'PushNotificationController as ctrl',
                    resolve: load([
                        'push-notification.controller',
                        'push-notification.service',
                        'paging.service'
                    ])
                }).state('techo.manage.managepushnotification', {
                    url: '/push/:id',
                    title: 'Manage Push Notification',
                    templateUrl: 'app/admin/pushnotification/views/manage-push-notification.html',
                    controller: 'ManagePushNotificationController as ctrl',
                    resolve: load([
                        'manage-push-notification.controller',
                        'push-notification.service',
                        'push-notification.constant'
                    ])
                })
                .state('techo.manage.smstype', {
                    url: '/sms-type-list',
                    title: 'SMS Type List',
                    templateUrl: 'app/manage/smstype/views/sms-type-list.html',
                    controller: 'SmsTypeListController as smsTypeListController',
                    resolve: load([
                        'sms-type-list.controller',
                        'sms-type.service'
                    ])
                })
                .state('techo.manage.smstypemanage', {
                    url: '/sms-type/:type',
                    title: 'Manage SMS Type',
                    templateUrl: 'app/manage/smstype/views/sms-type-manage.html',
                    controller: 'SmsTypeManageController as smsTypeManageController',
                    resolve: load([
                        'sms-type.controller',
                        'sms-type.service'
                    ])
                })
                .state('techo.admin.analyticsdashboard', {
                    url: '/analyticsdashboard',
                    templateUrl: 'app/admin/analyticsdashboard/views/analytics-dashboard.html',
                    title: 'Analytics Dashboard',
                    controller: 'AnalyticsDashbordController as analyticsdashboard',
                    resolve: load([
                        'analytics-dashboard.controller',
                        'query.service',
                        'paging-for-query-builder.service'
                    ])
                })
                // .state('techo.ncd.familyqrcode', {
                //     url: '/familyqrcode',
                //     templateUrl: 'app/ncd/FamilyQRCodeGeneration/views/family-qr-code-generation.html',
                //     title: 'Family QR Code Generation',
                //     controller: 'FamilyQRCodeGenerationController as ctrl',
                //     resolve: load([
                //         'family-qr-code-generation.controller',
                //         'family-qr-code.service',
                //         'authentication.service'
                //     ])
                // })
                // .state('techo.manage.lmsdashboardv2', {
                //     url: '/lmsdashboardv2',
                //     title: 'LMS Dashboard V2',
                //     templateUrl: 'app/training/dashboard/views/lms-dashboard-v2.html',
                //     controller: 'LMSDashboardControllerV2 as lms',
                //     resolve: load([
                //         'lms-dashboard-v2.controller',
                //         'paging-for-query-builder.service',
                //         'authentication.service',
                //         'user.service',
                //         'role.service'
                //     ])
                // });

            function load(srcs, callback, fetchUser, fetchConstant) {
                var depObj = {
                    deps: ['$ocLazyLoad', '$q', '$http', '$templateCache',
                        function ($ocLazyLoad, $q, $http, $templateCache) {
                            var deferred = $q.defer();
                            var promise = false;
                            srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                            if (!promise) {
                                promise = deferred.promise;
                            }
                            angular.forEach(srcs, function (src) {
                                promise = promise.then(function () {
                                    if (LZ_CONFIG[src]) {
                                        var srcArray = [];
                                        var promiseArray = [];
                                        var srcArrayFromConstants = LZ_CONFIG[src];
                                        srcArrayFromConstants = angular.isArray(srcArrayFromConstants) ? _.flatten(srcArrayFromConstants) : [srcArrayFromConstants];
                                        angular.forEach(srcArrayFromConstants, function (srcString) {
                                            if (srcString.startsWith('$templateCache:')) {
                                                srcString = srcString.replace('$templateCache:', '');
                                                if (!$templateCache.get(srcString)) {
                                                    var templatePromise = $http.get(srcString).then(function (response) {
                                                        $templateCache.put(srcString, response.data);
                                                    });
                                                    promiseArray.push(templatePromise);
                                                }
                                            } else {
                                                srcArray.push(srcString);
                                            }
                                        });
                                        promiseArray.push($ocLazyLoad.load(srcArray));
                                        return $q.all(promiseArray);
                                    } else {
                                        return $ocLazyLoad.load(src);
                                    }
                                });
                            });
                            deferred.resolve();
                            return callback ? promise.then(function () {
                                return callback();
                            }) : promise;
                        }]
                };
                if (fetchUser != null && fetchUser) {
                    depObj.User = ['AuthenticateService', '$rootScope',
                        function (AuthenticateService, $rootScope) {
                            return AuthenticateService.getLoggedInUser().catch(function () {
                                $rootScope.logOut();
                            });
                        }];
                }
                /* if (fetchConstant !== null && fetchConstant) {
                    depObj.Constant = ['ConstantMapUtilService', 'CONSTANTMAP', "CONSTANTSET", function (ConstantMapUtilService, CONSTANTSET) {
                        if (!CONSTANTSET.isSet) {
                            ConstantMapUtilService.reload().then(function () {
                                CONSTANTSET.isSet = true;
                            });
                        }
                    }];
                } */
                return depObj;
            }
        }]);
    as.run(function ($rootScope) {
        $rootScope.getGender = function (gender) {
            let g;
            switch (gender) {
                case 'M':
                    g = 'Male';
                    break;
                case 'F':
                    g = 'Female'
                    break;
                case 'T':
                    g = 'Transgender'
                    break;
            }
            return g;
        }
    })
}());
