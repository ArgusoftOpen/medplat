angular.module('imtecho')
    .constant("DistrictPerformanceDashboardConstant", {
        "districtPrformanceBasciData" : [
            {
                "indicatorName": "No. of PW Registration",
                "locationElaFieldName": "expected_mother_reg",
                "locationElaTooltip": "Expected number of mother registration",
                "locationNoFieldName": "anc_reg",
                "locationNoTooltip": "Total number of mother registration",
                "locationPercentageFieldName": "per_anc_reg",
                "locationPercentageTooltip": "(Total number of mother registration * 100)/Expected number of mother registration",
                "locationRankField": "anc_reg_rank"
            },
            {
                "indicatorName": "Early ANC",
                "locationElaFieldName": "expected_pw_reg",
                "locationElaTooltip": "Expected number of pregnant women registration and its value is equals to total number of mother registration",
                "locationNoFieldName": "early_anc",
                "locationNoTooltip": "Total number of pregnant women for early anc",
                "locationPercentageFieldName": "per_early_anc",
                "locationPercentageTooltip": "(Total number of pregnant women for early anc * 100)/Total number of mother registration",
                "locationRankField": "early_anc_rank"
            },
            {
                "indicatorName": "ANC 4 Check Up Against Early ANC Registration",
                "locationElaFieldName": "expected_anc4",
                "locationElaTooltip": `Expected number of pregnant women for whom 4th ANC visit is expected to
                 take place between 36 weeks and term end and its value is equals to total number of early anc`,
                "locationNoFieldName": "anc4",
                "locationNoTooltip": "Total number of pregnant women for whom 4th ANC visit is expected to take place between 36 weeks and term end",
                "locationPercentageFieldName": "per_anc4",
                "locationPercentageTooltip": "(Total numer of PW who had 4th ANC visit * 100)/Total number of early anc",
                "locationRankField": "per_anc4_rank"
            },
            {
                "indicatorName": "No. of Anemic PW Against ANC Registration",
                "locationElaFieldName": "expected_total_anemic",
                "locationElaTooltip": "Total number of mother registration",
                "locationNoFieldName": "total_anemic",
                "locationNoTooltip": "Total number of anemic pregnant women against number of mother registration",
                "locationPercentageFieldName": "per_total_anemic",
                "locationPercentageTooltip": "(Total number of anemic PW * 100)/Total number of mother registration",
                "locationRankField": "total_anemic_rank"
            },
            {
                "indicatorName": "No. of Severe Anemic PW Identified Against Anemic PW",
                "locationElaFieldName": "expected_anemic_pw",
                "locationElaTooltip": "Total number of anemic pregnant women against number of mother registration",
                "locationNoFieldName": "total_severe_anemic",
                "locationNoTooltip": "Total number of pregnant women who had anemia during pregnancy",
                "locationPercentageFieldName": "per_total_sever_anemic",
                "locationPercentageTooltip": "(Total number of PW who had anemia * 100)/(Total number of anemic pregnant women against number of mother registration)",
                "locationRankField": "total_sever_anemic_rank"
            },
            {
                "indicatorName": "No. of Severe Anemic PW treated",
                "locationElaFieldName": "expected_severe_anemic_treated",
                "locationElaTooltip": `Expected number of pregnant women who were treated for Anemia during
                 pregnancy and its value is equals to number of pregnant women who had anemia.`,
                "locationNoFieldName": "total_severe_anemic_treated",
                "locationNoTooltip": "Total number of pregnant women who were treated for Anemia",
                "locationPercentageFieldName": "per_total_anemic_treated",
                "locationPercentageTooltip": "(Total Severe Anemic PW treated * 100)/Total Severe Anemic PW identified",
                "locationRankField": "total_anemic_treated_rank"
            },
            {
                "indicatorName": "No. of Delivery",
                "locationElaFieldName": "expected_delivery_reg",
                "locationElaTooltip": "Expected number of delivery registration",
                "locationNoFieldName": "no_of_del",
                "locationNoTooltip": "Total number of delivery registration",
                "locationPercentageFieldName": "per_no_of_del",
                "locationPercentageTooltip": "(Total number of delivery registration * 100)/Expected number of delivery registration",
                "locationRankField": "no_of_del_rank"
            },
            {
                "indicatorName": "Total Institutional Delivery",
                "locationElaFieldName": "expected_inst_del",
                "locationElaTooltip": "Expected number of institutional delivery and its value is equals to number of delivery registration",
                "locationNoFieldName": "inst_del",
                "locationNoTooltip": "Total number of institutional delivery",
                "locationPercentageFieldName": "per_inst_del",
                "locationPercentageTooltip": "(Total number of institutional delivery * 100)/Total number of delivery registration",
                "locationRankField": "inst_del_rank"
            },
            {
                "indicatorName": "Total PHI Delivery",
                "locationElaFieldName": "expected_phi_del",
                "locationElaTooltip": "Expected number of PHI(Protected Health Information) delivery and its value is equals to number of institutional delivery",
                "locationNoFieldName": "phi_del",
                "locationNoTooltip": "Total number of PHI(Protected Health Information) delivery",
                "locationPercentageFieldName": "per_phi_del",
                "locationPercentageTooltip": "(Total number of PHI delivery * 100)/Total number of institutional delivery",
                "locationRankField": "phi_del_rank"
            },
            {
                "indicatorName": "Total Home Delivery Against Delivery Registration",
                "locationElaFieldName": "expected_home_del",
                "locationElaTooltip": "Expected number of home delivery and its value is equals to number of delivery registration",
                "locationNoFieldName": "home_del",
                "locationNoTooltip": "Total number of home delivery",
                "locationPercentageFieldName": "per_home_del",
                "locationPercentageTooltip": "(Total number of home delivery * 100)/Total number of delivery registration",
                "locationRankField": "home_del_rank"
            },
            {
                "indicatorName": "Injection Corticosteroid For Pre-term Delivery",
                "locationElaFieldName": "expected_cortico_steroid",
                "locationElaTooltip": "Expected number of corticosteroid injection",
                "locationNoFieldName": "cortico_steroid",
                "locationNoTooltip": "Total number of corticosteroid injection",
                "locationPercentageFieldName": "per_cortico_steroid",
                "locationPercentageTooltip": "(Total number of corticosteroid injection * 100)/Expected number of corticosteroid injection",
                "locationRankField": "cortico_steroid_rank"
            },
            {
                "indicatorName": "Total PPIUCD At PHI",
                "locationElaFieldName": "expected_ppiucd",
                "locationElaTooltip": "Expected number of postpartum intrauterine contraceptive devices at PHI and its value is equals to number of PHI delivery",
                "locationNoFieldName": "ppiucd",
                "locationNoTooltip": "Total no of postpartum intrauterine contraceptive devices at PHI",
                "locationPercentageFieldName": "per_ppiucd",
                "locationPercentageTooltip": "(Total number of PPIUCD at PHI * 100)/Total number of PHI delivery",
                "locationRankField": "ppiucd_rank"
            },
            {
                "indicatorName": "No. of Live Birth Registration",
                "locationElaFieldName": "expected_child_reg",
                "locationElaTooltip": "Expected number of child registration and its value is equals to expected number of delivery registration",
                "locationNoFieldName": "live_birth",
                "locationNoTooltip": "Total number of child registration",
                "locationPercentageFieldName": "per_live_birth",
                "locationPercentageTooltip": "(Total number of child registration * 100)/Expected number of delivery registration",
                "locationRankField": "live_birth_rank"
            },
            {
                "indicatorName": "No. of New Born Weighed",
                "locationElaFieldName": "expected_weighed",
                "locationElaTooltip": "Expected number of new born children weighed and its value is equals to total number of child registration",
                "locationNoFieldName": "weighed",
                "locationNoTooltip": "Total number of new born children weighed",
                "locationPercentageFieldName": "per_weighed",
                "locationPercentageTooltip": "(Total number of new born children weighed * 100)/Total number of child registration",
                "locationRankField": "weighed_rank"
            },
            {
                "indicatorName": "No. of LBW Babies Identified Against Live Birth Registration",
                "locationElaFieldName": "expected_lbw",
                "locationElaTooltip": 'Total number of new born children weighed',
                "locationNoFieldName": "weighed_less_than_2_5",
                "locationNoTooltip": "Total number of low birth weight(less than 2.5) babies identified",
                "locationPercentageFieldName": "per_lbw",
                "locationPercentageTooltip": "(Total number of low birth weight(less than 2.5) babies identified * 100)/Total number of new born children weighed",
                "locationRankField": "lbw_rank"
            },
            {
                "indicatorName": "Sex Ratio At Birth",
                "locationElaFieldName": "-",
                "locationElaTooltip": null,
                "locationNoFieldName": "per_sex_ratio",
                "locationNoTooltip": "Ratio between female and male at birth and its value is equals total female * 1000/ total male",
                "locationPercentageFieldName": "-",
                "locationPercentageTooltip": null,
                "locationRankField": "sex_ratio_rank"
            },
            {
                "indicatorName": "No. of Fully Immunized Children",
                "locationElaFieldName": "expected_fully_immu",
                "locationElaTooltip": "Expected number of fully immunized children",
                "locationNoFieldName": "fully_immunized",
                "locationNoTooltip": "Total number of fully immunized children",
                "locationPercentageFieldName": "per_fully_immu",
                "locationPercentageTooltip": "(Total number of fully immunized children * 100)/Expected number of fully immunized children",
                "locationRankField": "fully_immu_rank"
            },
            {
                "indicatorName": "Child Screening For Malnutrition",
                "locationElaFieldName": "expected_child_screening_for_malnutritition",
                "locationElaTooltip": "Total number of children whose age is between 0 to 5",
                "locationNoFieldName": "total_screened_for_malnutition",
                "locationNoTooltip": "Total number of child screened for malnutrition",
                "locationPercentageFieldName": "per_total_sam_malnutrition",
                "locationPercentageTooltip": "(Total number of child screened for malnutrition * 100)/Total number of children whose age is between 0 to 5",
                "locationRankField": "per_total_sam_malnutrition_rank"
            },
            {
                "indicatorName": "SAM Identified Against Child Screened",
                "locationElaFieldName": "expected_sam",
                "locationElaTooltip": "Total number of child screened for malnutrition",
                "locationNoFieldName": "total_sam_child",
                "locationNoTooltip": "Total children who have SAM identified",
                "locationPercentageFieldName": "per_sam",
                "locationPercentageTooltip": "(Total children who have SAM identified * 100)/Total number of child screened for malnutrition",
                "locationRankField": "total_sam_rank"
            },
            {
                "indicatorName": "No. of Maternal Death Registration",
                "locationElaFieldName": "expected_maternal_death",
                "locationElaTooltip": `Expected number of women who died  while pregnant or within 42 days of termination of pregnancy
                  and its value is equals to (75 * expected number of mother registration)/100000`,
                "locationNoFieldName": "maternal_death",
                "locationNoTooltip": "Total number of women who died  while pregnant or within 42 days of termination of pregnancy",
                "locationPercentageFieldName": "-",
                "locationPercentageTooltip": null,
                "locationRankField": "maternal_death_rank"
            },
            {
                "indicatorName": "Estimated MMR",
                "locationElaFieldName": "expected_mmr",
                "locationElaTooltip": "Expected MMR and its value is equals to 75",
                "locationNoFieldName": "expected_no_mmr",
                "locationNoTooltip": "Total number of MMR(Maternal Mortality Ratio)",
                "locationPercentageFieldName": "-",
                "locationPercentageTooltip": null,
                "locationRankField": "expected_no_mmr_rank"
            },
            {
                "indicatorName": "No. of Infant Death Registration",
                "locationElaFieldName": "expected_infant_death",
                "locationElaTooltip": "Expected number of infant death registration and its value is equals to (30 * expected number of mother registration)/1000",
                "locationNoFieldName": "infant_death",
                "locationNoTooltip": "Total number of infant death registration",
                "locationPercentageFieldName": "-",
                "locationPercentageTooltip": null,
                "locationRankField": "imr_rank"
            },
            {
                "indicatorName": "Estimated IMR",
                "locationElaFieldName": "expected_imr",
                "locationElaTooltip": "Expected number of IMR(Infant Mortality Rate) and its value is equals to 30",
                "locationNoFieldName": "expected_no_imr",
                "locationNoTooltip": "Total number of IMR(Infant Mortality Rate)",
                "locationPercentageFieldName": "-",
                "locationPercentageTooltip": null,
                "locationRankField": "expected_imr_rank"
            }
        ]
    });
