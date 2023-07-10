angular.module('imtecho')
    .constant("outPatientTreatmentConstants", {
        outPatientTreatmentSearchTitle: "Out Patient Treatment Search",
        outPatientTreatmentTitle: "Out Patient Treatment",
        outPatientTreatmentBeneficiaryTitle: "Beneficiary Details",
        outPatientTreatmentHealthDetailsTitle: "Health Details",
        outPatientTreatmentFormDisplayTitle: "Current Encounter",

        formFieldServiceDate: "Service Date",
        formFieldHealthInfrastructure: "Health Infrastructure",
        formFieldProvisionalDiagnosis: "Provisional Diagnosis",
        formFieldAdvisedLabTestCategories: "Advised Lab Test Categories",
        formFieldAdvisedLabTests: "Advised Lab Tests",
        formFieldMedicinesGivenOn: "Medicines Given On",
        formFieldInstructions: "Instructions"
    }).constant("status", {
        pending: "PENDING",
        completed: "COMPLETED"
    })
