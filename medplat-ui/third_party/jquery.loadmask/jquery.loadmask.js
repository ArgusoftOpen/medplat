

(function ($) {

    /**
     * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
     * Edits : Satyajit : changed method name $.fn.mask to $.fn.maskSpinner as previous function was conflicting with jquery.maskInput component
     *
     * @param label Text message that will be displayed on top of the mask besides a spinner (optional). 
     * 				If not provided only mask will be displayed without a label or a spinner.  	
     * @param delay Delay in milliseconds before element is masked (optional). If unmask() is called 
     *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
     *              mask display for quick processes.   	
     */
    $.fn.maskSpinner = function (label, delay, template, templateUrl, imagePath) {
        $(this).each(function () {
            if (delay !== undefined && delay > 0) {
                var element = $(this);
                element.data("_mask_timeout", setTimeout(function () {
                    $.maskElement(element, label, template, templateUrl, imagePath);
                }, delay));
            } else {
                $.maskElement($(this), label, template, templateUrl, imagePath);
            }
        });
    };
    /**
     * Removes mask from the element(s). Accepts both single and multiple selectors.
     * Edits : Satyajit : changed method name $.fn.unmask to $.fn.unmaskSpinner as previous function was conflicting with jquery.maskInput component
     */
    $.fn.unmaskSpinner = function () {
        $(this).each(function () {
            $.unmaskElement($(this));
        });
    };
    /**
     * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
     */
    $.fn.isMasked = function () {
        return this.hasClass("masked");
    };
    $.maskElement = function (element, label, template, templateUrl, imagePath) {

        //if this element has delayed mask scheduled then remove it and display the new one
        if (element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        if (element.isMasked()) {
            $.unmaskElement(element);
        }

        if (element.css("position") === "static") {
            element.addClass("masked-relative");
        }

        element.addClass("masked");
        var maskDiv = $('<div class="loadmask"></div>');
        //auto height fix for IE
        if (navigator.userAgent.toLowerCase().indexOf("msie") > -1) {
            maskDiv.height(element.height() + parseInt(element.css("padding-top")) + parseInt(element.css("padding-bottom")));
            maskDiv.width(element.width() + parseInt(element.css("padding-left")) + parseInt(element.css("padding-right")));
        }

        //fix for z-index bug with selects in IE6
        if (navigator.userAgent.toLowerCase().indexOf("msie 6") > -1) {
            element.find("select").addClass("masked-hidden");
        }

        element.append(maskDiv);
        var maskMsgDiv;
        if (template) {
            maskMsgDiv = $('<div class="loadmask-msg">' + template + '</div>');
            $.appendMaskDiv(maskMsgDiv, element);
        } else if (templateUrl) {
            $.ajax({
                url: templateUrl,
                success: function (data) {
                    maskMsgDiv = $('<div class="loadmask-msg">' + data + '</div>');
                    $.appendMaskDiv(maskMsgDiv, element);
                }
            });
        } else if (label && imagePath) {
            maskMsgDiv = $('<div class="loadmask-msg"><div align="center">' + label + '</div>' + '<div><img src="' + imagePath + '"></div></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        } else if (imagePath) {
            maskMsgDiv = $('<div class="loadmask-msg"><img src="' + imagePath + '"/></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        } else if (label) {
            maskMsgDiv = $('<div class="loadmask-msg">' + label + '</div>');
            $.appendMaskDiv(maskMsgDiv, element);
        } else {
            maskMsgDiv = $('<div class="loadmask-msg loadmask-msg-default" style="display:none;"></div>');
            maskMsgDiv.append('<div></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }

    };
    $.appendMaskDiv = function (maskMsgDiv, element) {
        element.append(maskMsgDiv);
//        calculate center position
//        console.log("element.height : " + element.height());
//        console.log("maskMsgDiv.height() : " + maskMsgDiv.height());
//        coconsolensole.log('maskMsgDiv.css("padding-top") : ' + maskMsgDiv.css("padding-top"));
//        console.log('maskMsgDiv.css("padding-bottom") :' + maskMsgDiv.css("padding-bottom"));
//        maskMsgDiv.css("top", Math.round(element.height() / 2 - (maskMsgDiv.height() - parseInt(maskMsgDiv.css("padding-top")) - parseInt(maskMsgDiv.css("padding-bottom"))) / 2) + "px");
//        maskMsgDiv.css("left", Math.round(element.width() / 2 - (maskMsgDiv.width() - parseInt(maskMsgDiv.css("padding-left")) - parseInt(maskMsgDiv.css("padding-right"))) / 2) + "px");
        maskMsgDiv.css("top", "50vh");
        maskMsgDiv.css("left", "calc(50vw - " + (maskMsgDiv.width() / 2) + "px)");
        maskMsgDiv.show();
    };

    $.unmaskElement = function (element) {
//if this element has delayed mask scheduled then remove it
        if (element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        element.find(".loadmask-msg,.loadmask,loadmask-msg-default").remove();
        element.removeClass("masked");
        element.removeClass("masked-relative");
        element.find("select").removeClass("masked-hidden");
    };
})(jQuery);