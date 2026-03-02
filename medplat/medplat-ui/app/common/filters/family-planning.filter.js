(function () {
    let techoFilters = angular.module("imtecho.filters");
    techoFilters.filter('familyplanning', function () {
        return (input) => {
            if (input) {
                switch (input) {
                    case 'FMLSTR':
                        return 'FEMALE STERILIZATION';
                    case 'MLSTR':
                        return 'MALE STERILIZATION';
                    case 'IUCD5':
                        return 'IUCD- 5 YEARS';
                    case 'IUCD10':
                        return 'IUCD- 10 YEARS';
                    case 'CONDOM':
                        return 'CONDOM';
                    case 'ORALPILLS':
                        return 'ORAL PILLS';
                    case 'CHHAYA':
                        return 'CHHAYA';
                    case 'ANTARA':
                        return 'ANTARA';
                    case 'CONTRA':
                        return 'EMERGENCY CONTRACEPTIVE PILLS';
                    case 'PPIUCD':
                        return 'PPIUCD';
                    case 'PAIUCD':
                        return 'PAIUCD';
                    case 'PPTL':
                        return 'Post Parterm TL';
                    case 'PATL':
                        return 'Post Abortion TL';
                    case 'LAPAROSCOPIC_TL':
                        return 'Laparoscopic TL';
                    case 'ABDOMINAL_TL':
                        return 'Abdominal TL';
                    case 'VASECTOMY_FOR_HUSBAND':
                        return 'Vasectomy (for Husband)';
                    default:
                        return 'N.A'
                }
            } else {
                return 'N.A';
            }
        };
    });
})();
