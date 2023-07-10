$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
    $('.cst-sidebar-nav li.has-sub').mouseover(function () {
        var submenu = $(this).children('.sub-menu');
        $(submenu).show();
    });
    $('.cst-sidebar-nav li.has-sub').mouseleave(function () {
        var submenu = $(this).children('.sub-menu');
        $(submenu).hide();
    });
    $('[data-toggle="tooltip"]').tooltip();
    $('.collapse-link').on('click', function () {
        $(this).parent().parent().toggleClass('active');
        $(this).find('i').toggleClass('ion-minus ion-plus');
    });
    $('.search').on('click', function () {
        $(this).parent().find('.text').toggleClass('active');
        $(this).parent().find('.search-box').toggleClass('active');
    });
    // Add slideDown animation to Bootstrap dropdown when expanding.
    $('.dropdown').on('show.bs.dropdown', function () {
        $(this).find('.dropdown-menu').first().stop(true, true).slideDown("fast");
    });

    // Add slideUp animation to Bootstrap dropdown when collapsing.
    $('.dropdown').on('hide.bs.dropdown', function () {
        $(this).find('.dropdown-menu').first().stop(true, true).slideUp("fast");
    });
    $('.cst-popover-toggle').on('click', function () {
        $(this).parent().find('.cst-popover').fadeToggle();
    });
    $('.close-cst-popover').on('click', function () {
        $(this).parent().parent().fadeToggle();
    });

    $('.filter-toggle').on('click', function () {
        if ($('.filter-div').hasClass('active')) {
            $('body').css("overflow", "auto");
        } else {
            $('body').css("overflow", "hidden");
        }
        $('.cst-backdrop').fadeToggle();
        $('.filter-div').toggleClass('active');
    });
    $('.close-filter').on('click', function () {
        $('.cst-backdrop').fadeToggle();
        $('.filter-div').toggleClass('active');
        $('body').css("overflow", "auto");
    });
    $('.fileUpload').on('click', function () {
        event.preventDefault();
        $(this).parent().parent().find(".myFileControl").click();
    });
    $(".header-fixed").tableHeadFixer();
    $('.collapse-btn').on('click', function () {
        $(this).find('i').toggleClass('ion-eye ion-eye-disabled');
    });
    $('.showList').on('click', function () {
        if ($(this).html() == 'Show More') {
            $(this).parent().find('.hideitem li:nth-child(3)').nextAll().show();
            $(this).html('Hide');
        } else {
            $(this).parent().find('.hideitem li:nth-child(3)').nextAll().hide();
            $(this).html('Show More');
        }
    });
    $("ul.nav-tabs a").click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
    $(".diseases-list a").click(function (e) {
        e.preventDefault();
        var showDiv= $(this).attr('href');
        $('.diseases-item').fadeOut();
        $(showDiv).fadeIn();
    });
    $(".diseases-list-action a").click(function (e) {
        e.preventDefault();
        var linkId = $(this).attr('href');
        if(linkId === '#close'){
            $('.diseases-item').fadeOut();
        }
        if(linkId === '#done'){
            $('.diseases-item').fadeOut();
        }
    });

});

$(window).on('load', function () {
    $('.hideitem li:nth-child(3)').nextAll().hide();
});

$(document).ready(function () {
    $('.clickme a').click(function () {
        $('.clickme a').removeClass('activelink');
        $(this).addClass('activelink');
        var tagid = $(this).data('tag');
        $('.list').removeClass('active').addClass('hide');
        $('#' + tagid).addClass('active').removeClass('hide');
    });
});