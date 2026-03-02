//var editor;
(function () {
    var ckeditor = function ($rootScope, APP_CONFIG, AuthenticateService, $timeout) {
        if (CKEDITOR.env.ie && CKEDITOR.env.version < 9) {
            CKEDITOR.tools.enableHtml5Elements(document);
        }
        // The trick to keep the editor in the sample quite small
        // unless user specified own height.
        CKEDITOR.config.height = '100vh';
        CKEDITOR.config.width = 'auto';
        CKEDITOR.config.removePlugins = 'elementspath';
        CKEDITOR.config.resize_enabled = false;
        return {
            require: '?ngModel',
            scope: {
                placeholder: '=',
                saveFile: '&callbackFn',
                toolBarConfig: '='
            },
            link: function (scope, elm, attr, ngModel) {
                var ck;
                var toolbarConfig = ['Cut', 'Copy', 'Paste', 'Undo', 'Redo', 'Bold', 'Italic'];
                angular.extend(toolbarConfig, scope.toolBarConfig);
                var imgsrc;
                scope.apiPath = APP_CONFIG.apiPath;
                var ckeditorConfig = {
                    filebrowserUploadUrl: scope.apiPath + "/document/ckeditor?access_token=" + AuthenticateService.getToken(),
                    removeButtons: 'About',
                    tabSpaces: 4,
                    specialChars: ['"', '’', ['&custom;', 'Custom label']],
                    extraPlugins: 'uploadimage,pastefromword',
                    uploadUrl: scope.apiPath + '/document/ckeditor/drag',
                };
                var ckeditorMinimalConfig = {
                    toolbar: [
                        toolbarConfig

                    ],
                    tabSpaces: 4,
                    height: '15vh',
                    width: 'auto',
                    contentsCss: 'body {overflow-x:hidden;}',
                    MaxLength: '10'
                };
                if (toolbarConfig.length <= 0) {
                    delete ckeditorMinimalConfig.toolbar;
                }
                if (attr.onlyVisible && attr.onlyVisible === "true") {
                    ckeditorConfig.removeButtons = 'Save';
                }
                scope.apiPath = $rootScope.apiPath;
                document.domain = document.domain;
                //to remove button (placeholder_select for placeholder dropdown)
                if (attr.inline) {
                    if (attr.removeButton) {
                        ckeditorMinimalConfig.removeButtons = ckeditorMinimalConfig.removeButtons + ',' + attr.removeButton;
                    }
                }
                else {
                    if (attr.removeButton) {
                        ckeditorConfig.removeButtons = ckeditorConfig.removeButtons + ',' + attr.removeButton;
                    }
                }
                //updating placeholder
                scope.$watch('placeholder', function (newValue, oldValue) {
                    if (newValue) {
                        if (newValue.length > 0) {
                            if (attr.inline) {
                                ckeditorMinimalConfig.placeholder_select = {
                                    placeholders: scope.placeholder
                                };
                            }
                            else {
                                ckeditorConfig.placeholder_select = {
                                    placeholders: scope.placeholder
                                };
                            }
                            var currentInstance;
                            for (var i in CKEDITOR.instances) {
                                currentInstance = i;
                                break;
                            }
                            var instance = CKEDITOR.instances[currentInstance];
                            if (instance) {
                                instance.destroy(true);
                            }
                            var value = ngModel.$viewValue;
                            createEditor(value);
                        }
                        else {
                            if (oldValue && oldValue.length > 0) {
                                if (attr.inline) {
                                    ckeditorMinimalConfig.placeholder_select = {
                                        placeholders: scope.placeholder
                                    };
                                } else {
                                    ckeditorConfig.placeholder_select = {
                                        placeholders: scope.placeholder
                                    };
                                }
                                createEditor(ngModel.$viewValue);
                            }
                        }
                    }
                }, true);
                if (!ngModel) {
                    return;
                }
                ngModel.$render = function () {
                    if (ck) {
                        ck.setData(ngModel.$viewValue);
                    }
                };
                const createEditor = function (value) {
                    //before creating del other
                    if (CKEDITOR.instances) {
                        for (var ck_instance in CKEDITOR.instances) {
                            CKEDITOR.instances[ck_instance].destroy(true);
                        }
                    }
                    var wysiwygareaAvailable = isWysiwygareaAvailable();
                    if (wysiwygareaAvailable && !attr.inline) {
                        ck = CKEDITOR.replace(elm[0], ckeditorConfig);
                        if (value) {
                            ck.setData(value);
                        }
                    }
                    else {
                        ck = CKEDITOR.replace(elm[0], ckeditorMinimalConfig);
                        if (value) {
                            ck.setData(value);
                        }
                    }
                    ck.on('instanceReady', function () {
                        ck.setData(ngModel.$viewValue);
                        ck.setData(ngModel.$viewValue);
                        let editor = this;
                        editor.dataProcessor.dataFilter.addRules({
                            elements: {
                                img: function (element) {
                                    imgsrc = scope.apiPath + '/document/ckeditor/' + element.attributes.id + '?access_token=' + AuthenticateService.getToken();
                                    element.attributes['data-cke-saved-src'] = imgsrc;
                                    element.attributes.src = imgsrc;
                                    return element;
                                }
                            }
                        });
                        editor.dataProcessor.htmlFilter.addRules({
                            elements: {
                                img: function (element) {
                                    imgsrc = scope.apiPath + '/document/ckeditor/' + element.attributes.id + '?access_token=' + AuthenticateService.getToken();
                                    if (element.attributes.src !== imgsrc) {
                                        $(editor.document.getById(element.attributes.id)).attr('src', imgsrc);
                                    }
                                    return element;
                                }
                            }
                        });
                        editor.on('fileUploadRequest', function (evt) {
                            var xhr = evt.data.fileLoader.xhr;
                            xhr.setRequestHeader('Authorization', 'Bearer ' + AuthenticateService.getToken());
                        });
                        if (!attr.inline) {
                            editor.execCommand('maximize');
                        }
                        //to allow max length in ckeditor
                        //ref link: https://stackoverflow.com/questions/27231626/ckeditor-character-limitation-with-charcount-plugin
                        if (attr.maxLength) {
                            editor.on('key', function (obj) {
                                if (obj.data.keyCode === 8 || obj.data.keyCode === 46) {
                                    return true;
                                }
                                if (editor.document.getBody().getText().length >= attr.maxLength) {
                                    return false;
                                }
                                return true;
                            });
                        }
                        ck.addCommand("save", {
                            modes: { wysiwyg: 1, source: 1 },
                            exec: function () {
                                scope.saveFile({ fileContent: ck.getData() });
                            }
                        });
                    });
                    function updateModel() {
                        scope.$applyAsync(function () {
                            ngModel.$setViewValue(ck.getData());
                        });
                    }
                    ck.on('change', updateModel);
                    ck.on('key', updateModel);
                    ck.on('dataReady', updateModel);
                };
                createEditor();
            }
        };
        function isWysiwygareaAvailable() {
            // If in development mode, then the wysiwygarea must be available.
            // Split REV into two strings so builder does not replace it :D.
            if (CKEDITOR.revision === ('%RE' + 'V%')) {
                return true;
            }
            return !!CKEDITOR.plugins.get('wysiwygarea');
        }
    };
    angular.module('imtecho.directives').directive('ckeditor', ckeditor);
})();
