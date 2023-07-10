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
        lazyLoadConstantsPath: require('./bower.json').appPath || 'app' + '/config.lazyload.js'
    };
    // Define the configuration for all the tasks
    grunt.initConfig({
        // Project settings
        ngconstant: {
            options: {
                space: '  ',
                wrap: '"use strict";\n\n {%= __ngModule %}',
                name: 'config',
                dest: '<%= config.app %>/config.js',
            },
            medplat: {
                options: {
                    dest: '<%= config.app %>/config.js',
                },
                constants: {
                    ENV: {
                        implementation: 'medplat'
                    }
                }
            },
            telangana: {
                options: {
                    dest: '<%= config.app %>/config.js',
                },
                constants: {
                    ENV: {
                        implementation: 'telangana'
                    }
                }
            },
            sewa_rural: {
                options: {
                    dest: '<%= config.app %>/config.js',
                },
                constants: {
                    ENV: {
                        implementation: 'sewa_rural'
                    }
                }
            },
            uttarakhand: {
                options: {
                    dest: '<%= config.app %>/config.js',
                },
                constants: {
                    ENV: {
                        implementation: 'uttarakhand'
                    }
                }
            }
        },
        config: config,
        bower: {
            install: {
                options: {
                    targetDir: 'bower_components'
                }
                //just run 'grunt bower:install' and you'll see files from your Bower packages in bower_components directory
            }
        },
        // Watches files for changes and runs tasks based on the changed files
        watch: {
            js: {
                files: ['<%= config.app %>/**/*.js'],
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                //                tasks: ["jshint"]
            },
            styles: {
                files: ['<%= config.app %>/styles/{,*/}*.css', './styles/less/*.less'],
                tasks: ['less']
            },
            gruntfile: {
                files: ['Gruntfile.js']
            },
            livereload: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    '<%= config.app %>/{,*/}*.html',
                    '<%= config.app %>/{,*/}*.*'
                ],
                tasks: ["less"]
            },
            mocks: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                tasks: ['concat:mockservices'],
                files: [
                    '<%= config.app %>/<%= config.mockservices %>/{,*/}*.js'
                ]
            }
        },
        // The actual grunt server settings
        connect: {
            options: {
                port: 9090,
                // Change this to '0.0.0.0' to access the server from outside.
                hostname: '0.0.0.0',
                livereload: 35730
            },
            livereload: {
                options: {
                    open: true
                }
            },
            dist: {
                options: {
                    open: true,
                    base: '<%= config.dist %>'
                }
            }
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= config.dist %>'
                    ]
                }]
            }
        },
        // ng-annotate tries to make the code safe for minification automatically
        // by using the Angular long form for dependency injection.
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    src: '<%= config.dist %>/<%= config.app %>/**/*.js',
                    dest: ''
                }]
            }
        },
        // Performs rewrites based on filerev and the useminPrepare configuration
        usemin: {
            html: ['<%= config.dist %>/index.html'],
        },
        // The following *-min tasks will produce minified files in the dist folder
        // By default, your `index.html`'s <!-- Usemin block --> will take care of
        // minification. These next options are pre-configured if you do not wish
        // to use the Usemin blocks.
        cssmin: {
            dist: {
                files: {
                    '<%= config.dist %>/styles/app.min.css': [
                        '<%= config.dist %>/styles/{,*/}*.css'
                    ]
                }
            }
        },
        htmlmin: {
            dist: {
                options: {
                    collapseWhitespace: true,
                    conservativeCollapse: true,
                    collapseBooleanAttributes: true,
                    removeCommentsFromCDATA: true,
                    removeOptionalTags: true
                },
                files: [{
                    expand: true,
                    src: '<%= config.dist %>/<%= config.app %>/**/*.html',
                    dest: ''
                }]
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
        uglify: {
            generated: {
                files: [
                    {
                        dest: '<%= config.dist %>/scripts/app.min.js',
                        src: ['<%= config.dist %>/scripts/app.js']
                    },
                    {
                        expand: true,
                        src: '<%= config.dist %>/<%= config.app %>/**/*.js',
                        dest: ''
                    }

                ]
            }
        },
        copy: {
            main: {
                files: [
                    // includes files within path                    
                    { expand: true, src: ['**', '!**/node_modules/**', '!**/bower.json', '!**/Gruntfile.js', '!**/package.json'], dest: '<%= config.dist %>' }
                ],
            },
        },
        jshint: {
            all: ['<%= config.app %>/**/*.js'],
            options: {
                //                "asi": true,
                //                "force": true,
                "eqnull": true,
                "eqeqeq": true,
                "curly": true,
                //                "undef": true,
                "unused": "vars",
                "elision": true,
                //                "globals": {
                //                    "angular": true,
                //                    "document": true,
                //                    "$": true,
                //                    "setTimeout": true,
                //                    "LocalServer": true,
                //                    "jQuery": true,
                //                    "window":true,
                //                    "console":true
                //                }
            }
        },
        less: {
            development: {
                options: {
                    paths: ['./styles/less']
                },
                files: {
                    './styles/css/main.css': './styles/css/main.less'
                }
            }
        }
    });
    grunt.registerTask('build', [
        'bower:install',
        'clean',
        'copy',
        'concat:generated',
        'ngAnnotate',
        'cssmin',
        'uglify:generated',
        'usemin',
        'htmlmin'
    ]);

    grunt.registerTask('medplat', ['ngconstant:medplat'])

    grunt.registerTask('telangana', ['ngconstant:telangana'])

    grunt.registerTask('sewa_rural', ['ngconstant:sewa_rural'])

    grunt.registerTask('uttarakhand', ['ngconstant:uttarakhand'])

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

    var getControllerContent = function (componentName) {
        return "(function () {\n"
            + "\tfunction " + componentName + "Controller() {\n"
            + "\t}\n"
            + "\tangular.module('" + config.name + ".controllers').controller('" + componentName + "Controller', " + componentName + "Controller);\n"
            + "})();";
    };
    var getServiceContent = function (componentName) {
        return "(function() {"
            + "\n\tfunction " + componentName + "DAO($resource) {"
            + "\n\t\tvar api = $resource('', {},"
            + "\n\t\t{"
            + "\n\t\t});"
            + "\n\treturn {"
            + "\n\t};"
            + "\n\t}"
            + "\n\tangular.module('" + config.name + ".service').factory('" + componentName + "DAO', " + componentName + "DAO);"
            + "\n})();";
    };
    var getFilterContent = function (componentName) {
        return "(function () {"
            + "\n\tfunction " + componentName + "() {"
            + "\n\t\treturn function (text) {"
            + "\n\t};"
            + "\n\t}"
            + "\n\tangular.module('" + config.name + ".filters').filter('" + componentName + "', " + componentName + ");"
            + "\n})();";
    };
    var getDirectiveContent = function (componentName) {
        return "(function () {"
            + "\n\tvar " + componentName + " = function () {"
            + "\n\treturn {"
            + "\n\t\trestrict: 'AE',"
            + "\n\t\tscope: {},"
            + "\n\t\tlink: function (scope, element, attrs) {"
            + "\n\t\t}"
            + "\n\t};"
            + "\n\t};"
            + "\n\tangular.module('" + config.name + ".directives').directive('" + componentName + "', " + componentName + ");"
            + "\n})();";
    };
    var addConstantForLazyLoad = function (key, path) {
        var fileContent = grunt.file.read(config.lazyLoadConstantsPath).trim();
        var startMarker = "//LazyLoad Constants start";
        var endMarker = "//LazyLoad Constants end";
        var startIndex = fileContent.indexOf(startMarker);
        var endIndex = fileContent.indexOf(endMarker);
        var beforeConstant = fileContent.substring(0, startIndex);
        var afterConstant = fileContent.substring(endIndex);
        var constantConfig = fileContent.substring(startIndex, endIndex).trim();
        if (constantConfig.lastIndexOf(",") + 1 != constantConfig.length) {
            constantConfig += ",";
        }
        constantConfig += '\n\t\t\'' + key + '\':[\'' + path + "']";
        grunt.file.write(config.lazyLoadConstantsPath, beforeConstant + constantConfig + '\n\t\t' + afterConstant);
    };

    grunt.registerTask('module', 'Create controllers,services,views folder for module', function (moduleName, subModuleName, name) {

        grunt.file.mkdir(config.app + '/' + moduleName + "/" + subModuleName);
        var componentName = subModuleName;
        if (name != null) {
            componentName = name;
        }
        grunt.task.run([
            'controller:' + moduleName + ":" + subModuleName + ":" + componentName
            //            'service:' + moduleName + ":" + subModuleName + ":" + componentName
        ]);
        grunt.file.write(config.app + '/' + moduleName + "/" + subModuleName + "/" + subModuleName + ".html", '');
    });

    grunt.registerTask('controller', 'Create controllers for module', function (moduleName, subModuleName, name) {
        var key = name.toLowerCase() + '.controller';
        var path = config.app + '/' + moduleName + "/" + subModuleName + "/" + subModuleName.toLowerCase() + ".controller.js";
        addConstantForLazyLoad(key, path);
        grunt.file.write(path, getControllerContent(name));
    });
    grunt.registerTask('service', 'Create services for module', function (moduleName, name) {
        var key = name.toLowerCase() + '.service';
        var path = config.app + '/' + moduleName + "/common/" + name.toLowerCase() + ".service.js";
        addConstantForLazyLoad(key, path);
        grunt.file.write(path, getServiceContent(name));
    });
    grunt.registerTask('filter', 'Create filter for module', function (moduleName, name) {
        var key = name.toLowerCase() + '.filter';
        var path = config.app + '/' + moduleName + "/common/" + name.toLowerCase() + ".filter.js";
        addConstantForLazyLoad(key, path);
        grunt.file.write(path, getFilterContent(name));
    });
    grunt.registerTask('directive', 'Create directive for module', function (moduleName, name) {
        var key = name.toLowerCase() + '.directive';
        var path = config.app + '/' + moduleName + "/common/" + name.toLowerCase() + ".directive.js";
        addConstantForLazyLoad(key, path);
        grunt.file.write(path, getDirectiveContent(name));
    });
    grunt.registerTask('default', [
        'build'
    ]);



};
