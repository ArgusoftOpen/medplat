angular.module('imtecho')
    .constant("SicklecellConstants", {
        sicklecellSearchTitle: "Sickle Cell Screening Search",
        sicklecellFormDisplayTitle: "Member Details",
        sicklecellFormTitle: "Sickle Cell Screening",

        formFieldIsSickleCellAnemiaTestDone: "Is Sickle Cell Anemia test done?",
        formFieldDttTestResult: "DTT Test Result",
        formFieldIsHplcTestDone: "Is HPLC Test Done",
        formFieldHplcTestResult: "HPLC Test Result",

        formValueDisplayPositive: "Positive",
        formValueDisplayNegative: "Negative",

        formValuePositive: "POSITIVE",
        formValueNegative: "NEGATIVE",

        YES: "Yes",
        NO: "No",
    }).constant("HplcTestValues", {
        NEGATIVE: "Negative",
        SICKLE_DISEASE: "Sickle Disease",
        SICKLE_THALASSEMIA: "Sickle Thalassemia",
        BETA_THALASSEMIA: "Beta Thalassemia",
        SICKLE_CELL_TRAIT: "Sickle Cell Trait",
        OTHER: "Other",
    })
