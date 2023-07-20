//Add application level constants here
(function () {

    var serverPath = "http://" + window.location.hostname; // + ":8181";
    // var serverPath = "http://192.1.100.181:448";  //Harshit
    angular.module('imtecho')
        .constant('APP_CONFIG', {
            appName: 'TeCHO+',
            serverPath: serverPath,
            apiPath: serverPath + "/api",
            angularDateFormat: 'dd/MM/yyyy',
            angularMonthFormat: 'MMMM yyyy',
            limit: 100,
            menuTypes: ['fhs', 'training', 'manage', 'admin'],
            familyIdFormat: '**/****/***?*?*?*?*?*?*?*?*?'
        })
        .constant('REPORTS', {
            layouts: [
                { key: 'dynamicReport1', value: 'Report Layout', templateType: 'DYNAMIC_REPORTS', type: 'view' },
                { key: 'dynamicReportWithPagination', value: 'Report Layout With Pagination', templateType: 'DYNAMIC_REPORTS', type: 'view' }
            ]
        })
        .constant('LAYOUTCONFIG', {
            formLayout1: {
                detailContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                infoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }

                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                otherInfoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                otherInfoContainer2: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                titleContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }

                ],
                imageContainer: [
                    {
                        resourceCategoryItemFieldDto: {
                            fieldType: 'image',
                            fieldName: 'image',
                            displayName: 'image'
                        }
                    }
                ]

            },
            reportFormLayout: {
                detailContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                infoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }

                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                otherInfoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                otherInfoContainer2: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                titleContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }

                ],
                imageContainer: [
                    {
                        resourceCategoryItemFieldDto: {
                            fieldType: 'image',
                            fieldName: 'image',
                            displayName: 'image'
                        }
                    }
                ]

            },
            viewLayout1: {
                statusContainer: [
                    {
                        displayName: 'status',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'status',
                            displayName: 'status'
                        }
                    }
                ],
                detailContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                infoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }

                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                otherInfoContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ],
                titleContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }

                ],
                tableContainer: [
                    {
                        'displayName': 'Data',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'data',
                            displayName: 'data'
                        }
                    },
                    {
                        'displayName': 'Data',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'data',
                            displayName: 'data'
                        }
                    },
                    {
                        'displayName': 'Data2',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'data',
                            displayName: 'Data2'
                        }
                    }
                ],
                imageContainer: [
                    {
                        resourceCategoryItemFieldDto: {
                            fieldType: 'image',
                            fieldName: 'image',
                            displayName: 'image'
                        }
                    }
                ]
            },
            popupFormLayout1: {
                popupContainer: [
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    },
                    {
                        'displayName': 'LABEL',
                        displayType: 'text',
                        resourceCategoryItemFieldDto: {
                            fieldType: 'text',
                            fieldName: 'Label',
                            displayName: 'Label'
                        }
                    }
                ]
            },
            dynamicReport1: {
                "fieldsContainer": [
                    {
                        "displayName": "LABEL",
                        "displayType": "text",
                        "resourceCategoryItemFieldDto": {
                            "fieldType": "text",
                            "fieldName": "Label",
                            "displayName": "Label"
                        }
                    },
                    {
                        "displayName": "LABEL",
                        "displayType": "text",
                        "resourceCategoryItemFieldDto": {
                            "fieldType": "text",
                            "fieldName": "Label",
                            "displayName": "Label"
                        }
                    },
                    {
                        "displayName": "LABEL",
                        "displayType": "text",
                        "resourceCategoryItemFieldDto": {
                            "fieldType": "text",
                            "fieldName": "Label",
                            "displayName": "Label"
                        }
                    },
                    {
                        "displayName": "LABEL",
                        "displayType": "text",
                        "resourceCategoryItemFieldDto": {
                            "fieldType": "text",
                            "fieldName": "Label",
                            "displayName": "Label"
                        }
                    },
                    {
                        "displayName": "LABEL",
                        "displayType": "text",
                        "resourceCategoryItemFieldDto": {
                            "fieldType": "text",
                            "fieldName": "Label",
                            "displayName": "Label"
                        }
                    }
                ],
                "tableContainer": [
                    {
                        tableData: [
                            {
                                "LABEL1": "value",
                                "LABEL2": "value",
                                "LABEL3": "value",
                                "LABEL4": "value",
                            }

                        ]
                    }
                ]
            }
        })
        .constant('BLOOD_GROUP', ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-', 'N/A'])
        .constant('PREGNANCY_COMPLICATIONS', {
            APH: 'APH',
            PPH: 'PPH',
            PLPRE: 'Placenta previa',
            PRETRM: 'Pre term',
            PIH: 'PIH',
            CONVLS: 'Convulsion',
            MLPRST: 'Malpresentation',
            PRELS: 'Previous LSCS',
            TWINS: 'Twins',
            SBRTH: 'Still birth',
            P2ABO: 'Previous 2 abortions',
            KCOSCD: 'Known case of sickle cell disease',
            OTHER: 'Other',
            NONE: 'None',
            NK: 'Not known'
        })
        .constant('FOETAL_POSITIONS', {
            VERTEX: 'Vertex',
            BREECH: 'Breech',
            TRANSVERSE: 'Transverse',
            OBLIQUE: 'Oblique',
            CBMO: 'Cannot be made out'
        })
        .constant('EXPECTED_DELIVERY_PLACES', {
            SUBCENTER: 'Subcenter',
            PHC: 'PHC',
            UPHC: 'UPHC',
            CHC: 'CHC',
            SUBDISTRICTHOSP: 'Sub-District Hospital',
            DISTRICTHOSP: 'District Hospital',
            TRUSTHOSP: 'Trust Hospital',
            CHIRANJEEVIHOSP: 'Private (Chiranjeevi)',
            PRIVATEHOSP: 'Private',
            OTHER: 'Other'
        })
        .constant('HBsAg_TEST', {
            NOT_DONE: 'Not done',
            REACTIVE: 'Reactive',
            NON_REACTIVE: 'Non-reactive'
        })
        .constant('BLOOD_SUGAR_TEST', [
            { key: 'EMPTY', value: 'Done on empty stomach' },
            { key: 'NON_EMPTY', value: 'Done on non-empty stomach' },
            { key: 'NOT_DONE', value: 'Not done' }
        ])
        .constant('ALBUMIN_SUGAR', {
            0: '0',
            '+': '+',
            '++': '++',
            '+++': '+++',
            '++++': '++++'
        })
        .constant('VDRL_TEST', {
            NOT_DONE: 'Not done',
            POSITIVE: 'POSITIVE',
            NEGATIVE: 'Negative'
        })
        .constant('HIV_TEST', {
            NOT_DONE: 'Not done',
            POSITIVE: 'POSITIVE',
            NEGATIVE: 'Negative'
        })
        .constant('PLACE_OF_DEATH', {
            HOME: 'Home',
            ON_THE_WAY: 'On The Way',
            HOSP: 'Hospital'
        })
        .constant('PREGNANCY_OUTCOME', {
            LBIRTH: 'Live Birth',
            SBIRTH: 'Still Birth',
            MTP: 'MTP',
            ABORTION: 'Abortion'
        })
        .constant('IMMUNISATIONS', {
            IMMUNISATION_TT: 'TT',
            IMMUNISATION_TT_1: 'TT1',
            IMMUNISATION_TT_2: 'TT2',
            IMMUNISATION_TT_BOOSTER: 'TT_BOOSTER',
            IMMUNISATION_CORTICO_ASTEROID: 'CORTICO_ASTEROID',
            IMMUNISATION_BCG: 'BCG',
            IMMUNISATION_OPV_0: 'OPV_0',
            IMMUNISATION_OPV_1: 'OPV_1',
            IMMUNISATION_OPV_2: 'OPV_2',
            IMMUNISATION_OPV_3: 'OPV_3',
            IMMUNISATION_OPV_BOOSTER: 'OPV_BOOSTER',
            IMMUNISATION_PENTA_1: 'PENTA_1',
            IMMUNISATION_PENTA_2: 'PENTA_2',
            IMMUNISATION_PENTA_3: 'PENTA_3',
            IMMUNISATION_DPT_1: 'DPT_1',
            IMMUNISATION_DPT_2: 'DPT_2',
            IMMUNISATION_DPT_3: 'DPT_3',
            IMMUNISATION_DPT_BOOSTER: 'DPT_BOOSTER',
            IMMUNISATION_MEASLES_1: 'MEASLES_1',
            IMMUNISATION_MEASLES_2: 'MEASLES_2',
            IMMUNISATION_MEASLES_RUBELLA_1: 'MEASLES_RUBELLA_1',
            IMMUNISATION_MEASLES_RUBELLA_2: 'MEASLES_RUBELLA_2',
            IMMUNISATION_F_IPV_1_01: 'F_IPV_1_01',
            IMMUNISATION_F_IPV_2_01: 'F_IPV_2_01',
            IMMUNISATION_F_IPV_2_05: 'F_IPV_2_05',
            IMMUNISATION_HEPATITIS_B_0: 'HEPATITIS_B_0',
            IMMUNISATION_VITAMIN_K: 'VITAMIN_K',
            IMMUNISATION_VITAMIN_A: 'VITAMIN_A',
        });
}());
