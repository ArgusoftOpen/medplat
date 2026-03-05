(function () {
    'use strict';

    angular.module('imtecho').service('DashboardService', DashboardService);

    function DashboardService($q, $http, GeneralUtil) {
        this.getDashboardData = function () {
            var deferred = $q.defer();

            // Mock data for demonstration - replace with actual API call
            var mockData = {
                summary: {
                    totalUsers: 1250,
                    activeUsers: 890,
                    totalRecords: 15678,
                    todayRecords: 234,
                    lastUpdate: new Date()
                },
                recentActivities: [
                    {
                        id: 1,
                        type: 'user_login',
                        description: 'User John Doe logged in',
                        timestamp: new Date(Date.now() - 3600000).toISOString()
                    },
                    {
                        id: 2,
                        type: 'record_created',
                        description: 'New patient record created',
                        timestamp: new Date(Date.now() - 7200000).toISOString()
                    },
                    {
                        id: 3,
                        type: 'data_sync',
                        description: 'Data synchronization completed',
                        timestamp: new Date(Date.now() - 10800000).toISOString()
                    }
                ],
                quickStats: [
                    {
                        title: 'Total Patients',
                        value: 3456,
                        icon: 'fa-users',
                        color: 'blue',
                        trend: '+12%'
                    },
                    {
                        title: 'Appointments Today',
                        value: 89,
                        icon: 'fa-calendar',
                        color: 'green',
                        trend: '+5%'
                    },
                    {
                        title: 'Pending Tasks',
                        value: 23,
                        icon: 'fa-tasks',
                        color: 'orange',
                        trend: '-8%'
                    },
                    {
                        title: 'System Health',
                        value: '98%',
                        icon: 'fa-heartbeat',
                        color: 'green',
                        trend: 'Stable'
                    }
                ],
                modules: [
                    {
                        name: 'Administration',
                        key: 'admin',
                        description: 'System administration and configuration',
                        icon: 'fa-cog',
                        users: 45
                    },
                    {
                        name: 'Manage',
                        key: 'manage',
                        description: 'Data management and records',
                        icon: 'fa-database',
                        users: 234
                    },
                    {
                        name: 'NCD',
                        key: 'ncd',
                        description: 'Non-communicable diseases management',
                        icon: 'fa-heart',
                        users: 156
                    },
                    {
                        name: 'Training',
                        key: 'training',
                        description: 'Training and education modules',
                        icon: 'fa-graduation-cap',
                        users: 78
                    }
                ]
            };

            // Simulate API delay
            setTimeout(function () {
                deferred.resolve({
                    data: mockData
                });
            }, 500);

            return deferred.promise;
        };

        this.getSystemHealth = function () {
            var deferred = $q.defer();
            
            // Mock system health data
            var healthData = {
                status: 'healthy',
                uptime: '99.9%',
                lastBackup: new Date().toISOString(),
                databaseStatus: 'connected',
                apiStatus: 'operational'
            };

            setTimeout(function () {
                deferred.resolve({
                    data: healthData
                });
            }, 300);

            return deferred.promise;
        };
    }
})();
