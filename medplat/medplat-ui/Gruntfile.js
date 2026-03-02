'use strict';
module.exports = function (grunt) {

    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    // Configurable paths for the application
    var config = {
        app: require('./bower.json').appPath || 'app',
        name: require('./bower.json').name || 'app',
        mockservices: 'mockservices',
        dist: 'dist',
        lazyLoadConstantsPath: (require('./bower.json').appPath || 'app') + '/config.lazyload.js'
    };

    // Define the configuration for all the tasks
    grunt.initConfig({
        config: config,

        ngconstant: {
            options: {
                space: '  ',
                wrap: '"use strict";\n\n {%= __ngModule %}',
                name: 'config',
                dest: '<%= config.app %>/config.js',
            },
            medplat: { constants: { ENV: { implementation: 'medplat' } } },
            telangana: { constants: { ENV: { implementation: 'telangana' } } },
            sewa_rural: { constants: { ENV: { implementation: 'sewa_rural' } } },
            uttarakhand: { constants: { ENV: { implementation: 'uttarakhand' } } }
        },

        bower: {
            install: {
                options: {
                    targetDir: 'bower_components'
                }
            }
        },

        watch: {
            js: {
                files: ['<%= config.app %>/**/*.js'],
                options: { livereload: '<%= connect.options.livereload %>' }
            },
            styles: {
                files: ['<%= config.app %>/styles/{,*/}*.css', './styles/less/*.less'],
                tasks: ['less']
            },
            gruntfile: { files: ['Gruntfile.js'] },
            livereload: {
                options: { livereload: '<%= connect.options.livereload %>' },
                files: [
                    '<%= config.app %>/{,*/}*.html',
                    '<%= config.app %>/{,*/}*.*'
                ],
                tasks: ["less"]
            },
            mocks: {
                options: { livereload: '<%= connect.options.livereload %>' },
                tasks: ['concat:mockservices'],
                files: ['<%= config.app %>/<%= config.mockservices %>/{,*/}*.js']
            }
        },

        connect: {
            options: {
                port: 9090,
                hostname: '0.0.0.0',
                livereload: 35730
            },
            livereload: { options: { open: true } },
            dist: { options: { open: true, base: '<%= config.dist %>' } }
        },

        clean: {
            dist: { files: [{ dot: true, src: ['.tmp', '<%= config.dist %>'] }] }
        },

        usemin: {
            html: ['<%= config.dist %>/index.html'],
        },

        cssmin: {
            dist: {
                files: { '<%= config.dist %>/styles/app.min.css': ['<%= config.dist %>/styles/{,*/}*.css'] }
            }
        },

        concat: {
            generated: {
                files: [
                    {
                        dest: '<%= config.dist %>/scripts/app.js',
                        src: [
                            'bower_components/angular/angular.js',
                            'bower_components/angular-resource/angular-resource.js',
                            'bower_components/angular-ui-router/release/angular-ui-router.js',
                            'bower_components/oclazyload/dist/ocLazyLoad.js'
                        ]
                    }
                ]
            },
            mockservices: {
                files: [
                    {
                        dest: '<%= config.app %>/mocks.js',
                        src: '<%= config.app %>/<%= config.mockservices %>/*.js',
                    }
                ]
            }
        },

        copy: {
            main: {
                files: [
                    { expand: true, src: ['**', '!**/node_modules/**', '!**/bower_components/**', '!**/.git/**', '!**/dist/**', '!**/bower.json', '!**/Gruntfile.js', '!**/package.json'], dest: '<%= config.dist %>' },
                    { expand: true, src: ['bower_components/**'], dest: '<%= config.dist %>' }
                ]
            }
        },

        jshint: {
            all: ['<%= config.app %>/**/*.js'],
            options: {
                eqnull: true,
                eqeqeq: true,
                curly: true,
                unused: "vars",
                elision: true
            }
        },

        less: {
            development: {
                options: { paths: ['./styles/less'] },
                files: { './styles/css/main.css': './styles/css/main.less' }
            }
        }
    });

    // ========================
    // TASKS
    // ========================

    // Build task
    grunt.registerTask('build', [
        'bower:install',
        'clean',
        'copy',
        'concat:generated',
        'cssmin',
        'usemin'
    ]);

    grunt.registerTask('medplat', ['ngconstant:medplat']);
    grunt.registerTask('telangana', ['ngconstant:telangana']);
    grunt.registerTask('sewa_rural', ['ngconstant:sewa_rural']);
    grunt.registerTask('uttarakhand', ['ngconstant:uttarakhand']);

    grunt.registerTask('serve', 'Compile then start a connect web server', function (target) {
        if (target === 'dist') {
            return grunt.task.run(['build', 'connect:dist:keepalive']);
        }
        grunt.task.run([
            'bower:install',
            'concat:mockservices',
            'less',
            'connect:livereload',
            'watch'
        ]);
    });

    grunt.registerTask('default', ['build']);
};