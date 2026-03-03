(function () {
    'use strict';
    var app = angular.module('imtecho');
    app.service('I18nService', [function () {
        var current = 'en';
        var dict = {
            en: {
                SEND: 'Send',
                CLEAR: 'Clear',
                AI_ASSISTANT: 'AI Assistant',
                TYPE_MSG: 'Type your message or issue...',
                ATTACH: 'Attach'
            },
            hi: {
                SEND: 'भेजें',
                CLEAR: 'साफ़ करें',
                AI_ASSISTANT: 'एआई सहायक',
                TYPE_MSG: 'अपना संदेश या समस्या लिखें...',
                ATTACH: 'जोड़ें'
            },
            gu: {
                SEND: 'પাঠવો',
                CLEAR: 'સાફ કરો',
                AI_ASSISTANT: 'એઆઈ સહાયક',
                TYPE_MSG: 'તમારો સંદેશ અથવા સમસ્યા ટાઇપ કરો...',
                ATTACH: 'જોડો'
            }
        };

        this.set = function (lang) { current = lang || 'en'; };
        this.get = function () { return current; };
        this.t = function (key) { return (dict[current] && dict[current][key]) || dict['en'][key] || key; };
    }]);
}());
