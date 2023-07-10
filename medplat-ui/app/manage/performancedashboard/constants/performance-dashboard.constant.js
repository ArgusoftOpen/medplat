angular.module('imtecho').constant("DASHBOARDConst", {
    duration: [
        {
            key: 'LAST_365_DAYS',
            value: 'Last 365 days'
        }, {
            key: '2021-2022',
            value: 'Current Financial Year'
        }],
    indicators: [
        {
            key: "Population Indicators",
            id: "populationScale"
        },
        {
            key: "Infant & Child Mortality",
            id: "infantScale"
        },
        {
            key: "Family Planning and Unmet Needs",
            id: "familyScale"
        },
        {
            key: "Maternal & Child Health",
            id: "maternalScale"
        },
        {
            key: "Feeding Practice, Nutrition & Anemia",
            id: "feedingScale"
        },
        // {
        //     key: "NCD",
        //     id: "ncdScale"
        // },

    ]
});
