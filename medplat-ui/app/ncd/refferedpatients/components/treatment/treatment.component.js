angular.module("imtecho.components")
    .component("treatment", {
        templateUrl: 'app/ncd/refferedpatients/components/treatment/treatment.html',
        bindings: {
            obj: '=',
            medicineData: '<',
            prescribedMedicine: '=',
            takeMedicine: '=',
            editData: '=',
            deleteData: '=',
            showEdit: '=',
            isSubmit: '=',
            fromMoReview: '='
        },
        controllerAs: 'ctrl',
        controller: function ($state, GeneralUtil, toaster, NcdDAO, $uibModal, Mask) {
            var ctrl = this;
            ctrl.medicineDetail = [];
            ctrl.today = new Date();
            ctrl.today.setHours(0, 0, 0, 0)
            ctrl.maxStartDate = new Date(ctrl.today.getFullYear(),ctrl.today.getMonth() + 2,ctrl.today.getDate());

            var init = function () {
                console.log(ctrl);
                ctrl.fetchPrescribedMedicines($state.params.id);
                ctrl.date = new Date()
            }

            //calculate Quantity based on the Frequency & Duration
            ctrl.calculateMedicineQuantity = function () {
                if (ctrl.medicine.duration && ctrl.medicine.frequency) {
                    ctrl.medicine.duration=Number(ctrl.medicine.duration);
                    ctrl.medicine.frequency=Number(ctrl.medicine.frequency);
                    ctrl.medicine.quantity = ctrl.medicine.duration * ctrl.medicine.frequency;
                }
            }

            //Add medicines in medicineDTO
            ctrl.addMedicine = function (form) {
                    if (ctrl.medicine.med && ctrl.medicine.frequency && ctrl.medicine.duration && ctrl.medicine.quantity
                        && ctrl.medicine.startDate && ctrl.medicine.frequency > 0 && ctrl.medicine.duration > 0 
                        && ctrl.medicine.frequency <= 5 && ctrl.medicine.duration <= 60 ) {
                        if (ctrl.medicine.quantity > ctrl.medicine.med.balanceInHand) {
                            toaster.pop('error', "Available Medicine Quantity is " + ctrl.medicine.med.balanceInHand)
                        }
                        else {
                            ctrl.obj.push({
                                medicineName: ctrl.medicine.med.medicineName,
                                medicineId: ctrl.medicine.med.medicineId,
                                startDate: ctrl.medicine.startDate,
                                frequency: ctrl.medicine.frequency,
                                duration: ctrl.medicine.duration,
                                quantity: ctrl.medicine.quantity,
                                specialInstruction: ctrl.medicine.specialInstruction
                            })
                            ctrl.medicine.med.balanceInHand -= ctrl.medicine.quantity;
                            ctrl.clearMedicineForm();
                            ctrl.isSubmit = true;
                        }
                    } else {
                        toaster.pop('error', "Please make sure medicine name and start date is selected. Also,select frequency between 1 to 5 and duration between 1 to 60.")
                    }
            }

            //clear medicine form
            ctrl.clearMedicineForm = function () {
                ctrl.medicine.med = null
                ctrl.medicine.frequency = null
                ctrl.medicine.duration = null
                ctrl.medicine.quantity = null
                ctrl.medicine.specialInstruction = null
                ctrl.medicine.startDate = null
                ctrl.medicine.isReturn = null
            }

            //Fetch prescribed medicines
            ctrl.fetchPrescribedMedicines = function (userId) {
                NcdDAO.retrievePrescribedMedicineForUser(userId).then(function (res) {
                    ctrl.prescribedMedicine = res;
                }).catch((error) => {
                    GeneralUtil.showMessageOnApiCallFailure(error)
                })
            }

            ctrl.deleteMedicine = function (index, item) {
                for(let indexForLoop in ctrl.medicineData){
                    if(ctrl.medicineData[indexForLoop]?.medicineName==item.medicineName){
                        ctrl.medicineData[indexForLoop].balanceInHand = ctrl.medicineData[indexForLoop].balanceInHand + item.quantity
                    }
                }
                ctrl.obj.splice(index, 1)
            }

            ctrl.editMedicineChange = function (item) {
                item.quantityNew = item.frequencyNew * item.durationNew
                if (item.quantityNew < item.quantity) {
                    item.isEnableReturnButton = true
                }
                else {
                    item.isEnableReturnButton = false
                }
            }

            ctrl.addMedicineToEdit = function (index, item) {
                console.log(item);
                item.quantityNew = item.frequencyNew * item.durationNew
                var med = [];
                var today = new Date()
                var diffDays = Math.round(Math.abs((today.getTime() - new Date(item.startDate).getTime()) / (24 * 60 * 60 * 1000)))
                med = ctrl.medicineData.filter(function (data) {
                    return data.medicineId === item.medicineId
                })
                if (item.durationNew > item.duration && (item.quantityNew - item.quantity) > med[0].balanceInHand) {
                    toaster.pop('error', "Available Medicine Quantity is " + med[0].balanceInHand)
                }
                else if (today > new Date(item.startDate) && item.durationNew < diffDays) {
                    toaster.pop('error', "You can not assign new duration less than " + diffDays + " days. Already " + diffDays + " days are completed for the medicine.")
                }
                else {
                    if (item.quantityNew && item.quantityNew != null && item.quantityNew > 0
                        && item.frequencyNew && item.frequencyNew != null && item.frequencyNew > 0) {
                        item.expiryDate = null
                        ctrl.editData.push({
                            id: item.id,
                            medicineName: item.medicineName,
                            medicineId: item.medicineId,
                            startDate: item.startDate,
                            isReturn: item.isReturn,
                            frequency: item.frequencyNew,
                            duration: item.durationNew,
                            quantity: item.quantityNew,
                            specialInstruction: item.specialInstruction
                        })
                        item.duration = item.durationNew
                        item.quantity = item.quantityNew
                        item.frequency = item.frequencyNew
                        item.isEdit = !item.isEdit
                    }
                    else {
                        toaster.pop('error', "Enter valid quantity / frequency")
                    }
                }
            }

            ctrl.resetFormFields = function(){
                ctrl.medicine={};
                ctrl.isSubmit = true;

            }

            ctrl.addMedicineToDelete = function (index, item) {
                let modalInstance = $uibModal.open({
                    templateUrl: 'app/ncd/refferedpatients/views/medicine-return-confirmation.modal.html',
                    windowClass: 'cst-modal',
                    backdrop: 'static',
                    size: 'md',
                    controllerAs: 'modalForConfirmation',
                    resolve: {},
                    controller: function ($uibModalInstance) {
                        let modalForConfirmation = this;
                        modalForConfirmation.isReturn = false

                        modalForConfirmation.ok = function () {
                            item.isReturn = modalForConfirmation.isReturn
                            $uibModalInstance.close();
                        }

                        modalForConfirmation.cancel = function () {
                            item.isReturn = modalForConfirmation.isReturn
                            $uibModalInstance.dismiss();
                        }
                    }
                });
                modalInstance.result
                    .then(function () {
                        ctrl.deleteData.push(
                            {
                                medicineName: item.medicineName,
                                medicineId: item.medicineId,
                                startDate: item.startDate,
                                frequency: item.frequency,
                                duration: item.duration,
                                quantity: item.quantity,
                                specialInstruction: item.specialInstruction,
                                isReturn: item.isReturn,
                                id: item.id
                            })
                        ctrl.prescribedMedicine.splice(index, 1);
                    }).catch(function() {})
            }

            init();
        }
    });
