var myapp = angular.module('imtecho.constant');

myapp.constant("WrongActionType", {
    WRONG_DELIVERY:{
        key:"MARK_AS_FALSE_DELIVERY", 
        value:"Revert Service Record",
        note:"Please note that Mark As Wrong Delivery will delete all the delivery related data like Delivery Registration, live birth or still birth, PNC notification and any other related data for the registered delivery. Option can not be revert once done successfully."
    },
    ARCHIVE_MEMBER:{
        key:"MARK_MEMBER_AS_ARCHIVE",
        value:"Mark Member As Archive",
        note:"Please note that when you mark member as archive it remove all pending services, notifications and reports"
    }
});
