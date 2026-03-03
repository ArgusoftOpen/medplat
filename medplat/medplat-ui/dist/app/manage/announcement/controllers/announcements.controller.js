(function () {
    function AnnouncementsController(AnnouncementDAO, PagingService) {
        var ctrl = this;
        ctrl.announcementsList = [];
        ctrl.startingPosition = 0;
        ctrl.maxSize = 50;
        ctrl.pagingService = PagingService.initialize();
        ctrl.order = "desc";
        ctrl.orderBy = "from_date";

        ctrl.toggleStatus = function (announcement) {
            AnnouncementDAO.toggleStatusById(announcement.id).then(function (response) {
                announcement.isActive = !announcement.isActive;
            });
        }

        ctrl.getAnnouncementsByCriteria = function () {
            ctrl.criteria = { limit: ctrl.pagingService.limit, offset: ctrl.pagingService.offSet, orderBy: ctrl.orderBy, order: ctrl.order };
            var announcementList = ctrl.announcementsList;
            PagingService.getNextPage(AnnouncementDAO.retrieveByCriteria, ctrl.criteria, announcementList, null).then(function (response) {
                ctrl.announcementsList = response;
            });
        }
    }
    angular.module('imtecho.controllers').controller('AnnouncementsController', AnnouncementsController);
})();
