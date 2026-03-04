package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.core.ImmunisationService;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.EBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

@EBean(scope = EBean.Scope.Singleton)
public class ImmunisationServiceImpl implements ImmunisationService {

    @Override
    public Set<String> getDueImmunisationsForChild(Date dateOfBirth, String givenImmunisations) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar firstJuly = Calendar.getInstance();
        firstJuly.set(Calendar.YEAR, 2019);
        firstJuly.set(Calendar.MONTH, 6);
        firstJuly.set(Calendar.DATE, 1);
        firstJuly.set(Calendar.HOUR_OF_DAY, 0);
        firstJuly.set(Calendar.MINUTE, 0);
        firstJuly.set(Calendar.SECOND, 0);
        firstJuly.set(Calendar.MILLISECOND, 0);

        Set<String> vaccinationSet = new LinkedHashSet<>();
        Map<String, Date> vaccineGivenDateMap = new HashMap<>();
        int ageInWeeks = UtilBean.getNumberOfWeeks(dateOfBirth, new Date());
        int ageInDays = UtilBean.getNumberOfDays(dateOfBirth, new Date());
        int ageInMonths = UtilBean.getNumberOfMonths(dateOfBirth, new Date());

        if (SharedStructureData.formType != null && SharedStructureData.formType.equals(FormConstants.TECHO_FHW_WPD)) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.OPV_0);
        }

        if (ageInDays <= 1) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
        }

        if (ageInDays <= 15) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
        }

        if (ageInMonths <= 12) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
        }

        if (ageInWeeks >= 6 && ageInWeeks < 10) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
        } else if (ageInWeeks >= 6 && ageInWeeks < 14) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_2);
        } else if (ageInWeeks >= 6 && ageInMonths < 9) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_2);
            vaccinationSet.add(RchConstants.PENTA_3);
            vaccinationSet.add(RchConstants.OPV_3);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_3);
            vaccinationSet.add(RchConstants.F_IPV_2_01);
            vaccinationSet.add(RchConstants.F_IPV_2_05);
        } else if (ageInWeeks >= 6 && ageInMonths < 10) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_2);
            vaccinationSet.add(RchConstants.PENTA_3);
            vaccinationSet.add(RchConstants.OPV_3);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_3);
            vaccinationSet.add(RchConstants.F_IPV_2_01);
            vaccinationSet.add(RchConstants.F_IPV_2_05);
            vaccinationSet.add(RchConstants.VITAMIN_A);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_1);
        } else if (ageInWeeks >= 6 && ageInMonths < 16) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.OPV_3);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.PENTA_3);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_2);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_3);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.F_IPV_2_01);
            vaccinationSet.add(RchConstants.F_IPV_2_05);
            vaccinationSet.add(RchConstants.VITAMIN_A);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_1);
        } else if (ageInWeeks >= 6 && ageInMonths < 24) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.OPV_3);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.PENTA_3);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_2);
            vaccinationSet.add(RchConstants.ROTA_VIRUS_3);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.F_IPV_2_01);
            vaccinationSet.add(RchConstants.F_IPV_2_05);
            vaccinationSet.add(RchConstants.VITAMIN_A);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_1);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_2);
            vaccinationSet.add(RchConstants.OPV_BOOSTER);
            vaccinationSet.add(RchConstants.DPT_BOOSTER);
        } else if (ageInWeeks >= 6) {
            vaccinationSet.add(RchConstants.HEPATITIS_B_0);
            vaccinationSet.add(RchConstants.VITAMIN_K);
            vaccinationSet.add(RchConstants.OPV_0);
            vaccinationSet.add(RchConstants.BCG);
            vaccinationSet.add(RchConstants.OPV_1);
            vaccinationSet.add(RchConstants.OPV_2);
            vaccinationSet.add(RchConstants.OPV_3);
            vaccinationSet.add(RchConstants.PENTA_1);
            vaccinationSet.add(RchConstants.PENTA_2);
            vaccinationSet.add(RchConstants.PENTA_3);
            vaccinationSet.add(RchConstants.F_IPV_1_01);
            vaccinationSet.add(RchConstants.F_IPV_2_01);
            vaccinationSet.add(RchConstants.F_IPV_2_05);
            vaccinationSet.add(RchConstants.VITAMIN_A);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_1);
            vaccinationSet.add(RchConstants.MEASLES_RUBELLA_2);
            vaccinationSet.add(RchConstants.OPV_BOOSTER);
            vaccinationSet.add(RchConstants.DPT_BOOSTER);
        }

        boolean anyPentaDoseGiven = givenImmunisations != null &&
                (givenImmunisations.contains(RchConstants.PENTA_1) ||
                        givenImmunisations.contains(RchConstants.PENTA_2) ||
                        givenImmunisations.contains(RchConstants.PENTA_3));

        if (ageInMonths >= 12 && ageInMonths <= 84 &&
                (givenImmunisations == null || !anyPentaDoseGiven)) {
            vaccinationSet.add(RchConstants.DPT_1);
            if (ageInMonths >= 13) {
                vaccinationSet.add(RchConstants.DPT_2);
            }
            if (ageInMonths >= 14) {
                vaccinationSet.add(RchConstants.DPT_3);
            }
        }

        if (givenImmunisations != null && givenImmunisations.length() > 0) {
            StringTokenizer vaccineTokenizer = new StringTokenizer(givenImmunisations, ",");
            while (vaccineTokenizer.hasMoreElements()) {
                String[] vaccine = vaccineTokenizer.nextToken().split("#");
                String givenVaccineName = vaccine[0].trim();
                try {
                    if (givenVaccineName.equals(RchConstants.VITAMIN_A)) {
                        Date givenDate = vaccineGivenDateMap.get(givenVaccineName);
                        if (givenDate == null || givenDate.before(sdf.parse(vaccine[1]))) {
                            vaccineGivenDateMap.put(givenVaccineName, sdf.parse(vaccine[1]));
                        }
                    } else {
                        vaccinationSet.remove(givenVaccineName);
                        vaccineGivenDateMap.put(givenVaccineName, sdf.parse(vaccine[1]));
                    }

                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (vaccinationSet.contains(RchConstants.DPT_BOOSTER)) {
            if (givenImmunisations == null) {
                vaccinationSet.remove(RchConstants.DPT_BOOSTER);
            } else {
                if (!(givenImmunisations.contains(RchConstants.PENTA_3)
                        || givenImmunisations.contains(RchConstants.DPT_3))) {
                    vaccinationSet.remove(RchConstants.DPT_BOOSTER);
                }
                if (givenImmunisations.contains(RchConstants.PENTA_3) &&
                        UtilBean.getNumberOfMonths(vaccineGivenDateMap.get(RchConstants.PENTA_3), new Date()) < 6) {
                    vaccinationSet.remove(RchConstants.DPT_BOOSTER);
                }
                if (givenImmunisations.contains(RchConstants.DPT_3) &&
                        UtilBean.getNumberOfMonths(vaccineGivenDateMap.get(RchConstants.DPT_3), new Date()) < 6) {
                    vaccinationSet.remove(RchConstants.DPT_BOOSTER);
                }
            }
        }

        if (vaccinationSet.contains(RchConstants.OPV_BOOSTER)) {
            if (givenImmunisations == null) {
                vaccinationSet.remove(RchConstants.OPV_BOOSTER);
            } else {
                if (!givenImmunisations.contains(RchConstants.OPV_3)) {
                    vaccinationSet.remove(RchConstants.OPV_BOOSTER);
                }
                if (givenImmunisations.contains(RchConstants.OPV_3) &&
                        UtilBean.getNumberOfMonths(vaccineGivenDateMap.get(RchConstants.OPV_3), new Date()) < 6) {
                    vaccinationSet.remove(RchConstants.OPV_BOOSTER);
                }
            }
        }

        if (vaccinationSet.contains(RchConstants.F_IPV_2_05)
                && givenImmunisations != null
                && givenImmunisations.contains(RchConstants.F_IPV_1_01)) {
            vaccinationSet.remove(RchConstants.F_IPV_2_05);
        }

        if (vaccinationSet.contains(RchConstants.VITAMIN_A)
                && givenImmunisations != null
                && vaccineGivenDateMap.containsKey(RchConstants.VITAMIN_A)
                && UtilBean.getNumberOfMonths(vaccineGivenDateMap.get(RchConstants.VITAMIN_A), new Date()) < 4) {
            vaccinationSet.remove(RchConstants.VITAMIN_A);
        }

        boolean measles1Given = givenImmunisations != null &&
                (givenImmunisations.contains(RchConstants.MEASLES_RUBELLA_1) ||
                        givenImmunisations.contains(RchConstants.MEASLES_1));
        if (vaccinationSet.contains(RchConstants.MEASLES_RUBELLA_1) && measles1Given) {
            vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_1);
        }

        if (vaccinationSet.contains(RchConstants.MEASLES_RUBELLA_2)) {
            if (givenImmunisations != null &&
                    (givenImmunisations.contains(RchConstants.MEASLES_RUBELLA_2)
                            || givenImmunisations.contains(RchConstants.MEASLES_2))) {
                vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_2);
                vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_1);
            } else if (givenImmunisations != null && !measles1Given) {
                vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_2);
                vaccinationSet.add(RchConstants.MEASLES_RUBELLA_1);
            } else if (givenImmunisations != null && givenImmunisations.contains(RchConstants.MEASLES_1)
                    && UtilBean.getNumberOfWeeks(vaccineGivenDateMap.get(RchConstants.MEASLES_1), new Date()) < 4) {
                vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_2);
            } else if (givenImmunisations != null && givenImmunisations.contains(RchConstants.MEASLES_RUBELLA_1)
                    && UtilBean.getNumberOfWeeks(vaccineGivenDateMap.get(RchConstants.MEASLES_RUBELLA_1), new Date()) < 4) {
                vaccinationSet.remove(RchConstants.MEASLES_RUBELLA_2);
            }
        }


        if (dateOfBirth.before(firstJuly.getTime())) {
            int firstJulyAgeMonths = UtilBean.getNumberOfMonths(dateOfBirth, firstJuly.getTime());
            if (firstJulyAgeMonths >= 12 ||
                    (givenImmunisations != null &&
                            !givenImmunisations.contains(RchConstants.ROTA_VIRUS_1) &&
                            anyPentaDoseGiven)) {
                vaccinationSet.remove(RchConstants.ROTA_VIRUS_1);
                vaccinationSet.remove(RchConstants.ROTA_VIRUS_2);
                vaccinationSet.remove(RchConstants.ROTA_VIRUS_3);
            }
        } else if (UtilBean.getNumberOfMonths(dateOfBirth, new Date()) > 12 &&
                (givenImmunisations == null ||
                        !givenImmunisations.contains(RchConstants.ROTA_VIRUS_1))) {
            vaccinationSet.remove(RchConstants.ROTA_VIRUS_1);
            vaccinationSet.remove(RchConstants.ROTA_VIRUS_2);
            vaccinationSet.remove(RchConstants.ROTA_VIRUS_3);
        }

        if (!vaccinationSet.isEmpty()) {
            List<String> vaccines = new ArrayList<>(vaccinationSet);
            Collections.sort(vaccines, UtilBean.VACCINATION_COMPARATOR);
            return new LinkedHashSet<>(vaccines);
        } else {
            return new LinkedHashSet<>();
        }
    }

    @Override
    public String vaccinationValidationForChild(Date dob, Date givenDate, String currentVaccine, Map<String, Date> vaccineGivenDateMap) {
        if (currentVaccine == null) {
            return null;
        }
        if (givenDate == null) {
            return LabelConstants.PLEASE_SELECT_A_DATE;
        }
        dob = UtilBean.clearTimeFromDate(dob);
        givenDate = UtilBean.clearTimeFromDate(givenDate);

        Calendar from = Calendar.getInstance();
        from.setTime(dob);
        Calendar to = Calendar.getInstance();
        to.setTime(dob);
        Date tmpDate;
        Date tmpDate2;
        switch (currentVaccine) {
            case RchConstants.HEPATITIS_B_0:
            case RchConstants.VITAMIN_K:
                to.add(Calendar.DATE, 1);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.OPV_0:
                to.add(Calendar.DATE, 15);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.BCG:
                to.add(Calendar.YEAR, 1);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.OPV_1:
                from.add(Calendar.DATE, 42);
                to.add(Calendar.MONTH, 58);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.OPV_2:
                tmpDate = vaccineGivenDateMap.get(RchConstants.OPV_1);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 70);
                to.add(Calendar.MONTH, 59);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.OPV_3:
                tmpDate = vaccineGivenDateMap.get(RchConstants.OPV_2);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 98);
                to.add(Calendar.MONTH, 60);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.ROTA_VIRUS_1:
            case RchConstants.F_IPV_1_01:
                from.add(Calendar.DATE, 42);
                to.add(Calendar.MONTH, 12);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.ROTA_VIRUS_2:
                tmpDate = vaccineGivenDateMap.get(RchConstants.ROTA_VIRUS_1);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 70);
                to.add(Calendar.MONTH, 23);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.ROTA_VIRUS_3:
                tmpDate = vaccineGivenDateMap.get(RchConstants.ROTA_VIRUS_2);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 98);
                to.add(Calendar.MONTH, 24);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.PENTA_1:
                if (vaccineGivenDateMap.containsKey(RchConstants.DPT_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }
                from.add(Calendar.DATE, 42);
                to.add(Calendar.MONTH, 12);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.PENTA_2:
                if (vaccineGivenDateMap.containsKey(RchConstants.DPT_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                tmpDate = vaccineGivenDateMap.get(RchConstants.PENTA_1);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 70);
                to.add(Calendar.MONTH, 59);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.PENTA_3:
                if (vaccineGivenDateMap.containsKey(RchConstants.DPT_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.DPT_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                tmpDate = vaccineGivenDateMap.get(RchConstants.PENTA_2);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 98);
                to.add(Calendar.MONTH, 60);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.DPT_1:
                if (vaccineGivenDateMap.containsKey(RchConstants.PENTA_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }
                from.add(Calendar.MONTH, 12);
                to.add(Calendar.MONTH, 82);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.DPT_2:
                if (vaccineGivenDateMap.containsKey(RchConstants.PENTA_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                tmpDate = vaccineGivenDateMap.get(RchConstants.DPT_1);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 13);
                to.add(Calendar.MONTH, 83);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.DPT_3:
                if (vaccineGivenDateMap.containsKey(RchConstants.PENTA_1)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_2)
                        || vaccineGivenDateMap.containsKey(RchConstants.PENTA_3)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                tmpDate = vaccineGivenDateMap.get(RchConstants.DPT_2);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 14);
                to.add(Calendar.MONTH, 84);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;

            case RchConstants.F_IPV_2_01:
                tmpDate = vaccineGivenDateMap.get(RchConstants.F_IPV_1_01);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 98);
                to.add(Calendar.MONTH, 12);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.F_IPV_2_05:
                if (vaccineGivenDateMap.containsKey(RchConstants.F_IPV_1_01)
                        || vaccineGivenDateMap.containsKey(RchConstants.F_IPV_2_01)) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                Date opv3Date2 = vaccineGivenDateMap.get(RchConstants.OPV_3);
                if (opv3Date2 == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (UtilBean.getNumberOfMonths(dob, givenDate) > 11) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                Calendar givenDateCalender2 = Calendar.getInstance();
                givenDateCalender2.setTime(givenDate);
                Calendar onlyDate2 = UtilBean.clearTimeFromDate(givenDateCalender2);
                if (opv3Date2.after(onlyDate2.getTime()) || opv3Date2.before(onlyDate2.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.DATE, 98);
                to.add(Calendar.MONTH, 12);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.VITAMIN_A:
                tmpDate = vaccineGivenDateMap.get(RchConstants.VITAMIN_A);

                if (tmpDate != null && tmpDate.after(givenDate)) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                from.add(Calendar.MONTH, 8);
                to.add(Calendar.MONTH, 60);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.OPV_BOOSTER:
                tmpDate = vaccineGivenDateMap.get(RchConstants.OPV_3);
                if (tmpDate == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (UtilBean.getNumberOfMonths(tmpDate, givenDate) < 6) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.DPT_BOOSTER:
                tmpDate = vaccineGivenDateMap.get(RchConstants.DPT_3);
                tmpDate2 = vaccineGivenDateMap.get(RchConstants.PENTA_3);
                if (tmpDate == null
                        && tmpDate2 == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate != null && UtilBean.getNumberOfMonths(tmpDate, givenDate) < 6) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (tmpDate2 != null && UtilBean.getNumberOfMonths(tmpDate2, givenDate) < 6) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.MEASLES_RUBELLA_1:

                from.add(Calendar.MONTH, 9);
                to.add(Calendar.MONTH, 59);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            case RchConstants.MEASLES_RUBELLA_2:
                tmpDate = vaccineGivenDateMap.get(RchConstants.MEASLES_RUBELLA_1);
                tmpDate2 = vaccineGivenDateMap.get(RchConstants.MEASLES_1);
                if (tmpDate == null
                        && tmpDate2 == null) {
                    return UtilBean.getMyLabel(LabelConstants.VACCINATION_IS_NOT_VALID);
                }

                if (tmpDate != null && UtilBean.getNumberOfDays(tmpDate, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                if (tmpDate2 != null && UtilBean.getNumberOfDays(tmpDate2, givenDate) <= 28) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }

                from.add(Calendar.MONTH, 16);
                to.add(Calendar.MONTH, 60);
                if (givenDate.after(to.getTime()) || givenDate.before(from.getTime())) {
                    return LabelConstants.VACCINATION_DATE_IS_NOT_VALID;
                }
                break;
            default:
        }
        return null;
    }

    @Override
    public boolean isImmunisationMissed(Date dateOfBirth, String immunisation) {
        Calendar instance = Calendar.getInstance();
        Date currentDate = instance.getTime();
        instance.setTime(dateOfBirth);
        Date lbw = new Date();
        Date ubw = new Date();

        switch (immunisation) {
            case RchConstants.HEPATITIS_B_0:
            case RchConstants.VITAMIN_K:
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, 1);
                ubw = instance.getTime();
                break;


            case RchConstants.BCG:
                lbw = instance.getTime();
                instance.add(Calendar.YEAR, 1);
                ubw = instance.getTime();
                break;

            case RchConstants.OPV_0:
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, 15);
                ubw = instance.getTime();
                break;

            case RchConstants.OPV_1:
                instance.add(Calendar.DAY_OF_WEEK, 42);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -42);
                instance.add(Calendar.MONTH, 58);
                ubw = instance.getTime();
                break;

            case RchConstants.OPV_2:
            case RchConstants.PENTA_2:
                instance.add(Calendar.DAY_OF_WEEK, 70);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -70);
                instance.add(Calendar.MONTH, 59);
                ubw = instance.getTime();
                break;

            case RchConstants.OPV_3:
            case RchConstants.PENTA_3:
                instance.add(Calendar.DAY_OF_WEEK, 98);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -98);
                instance.add(Calendar.MONTH, 60);
                ubw = instance.getTime();
                break;

            case RchConstants.ROTA_VIRUS_1:
            case RchConstants.PENTA_1:
            case RchConstants.F_IPV_1_01:
                instance.add(Calendar.DAY_OF_WEEK, 42);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -42);
                instance.add(Calendar.MONTH, 12);
                ubw = instance.getTime();
                break;

            case RchConstants.ROTA_VIRUS_2:
                instance.add(Calendar.DAY_OF_WEEK, 70);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -70);
                instance.add(Calendar.MONTH, 23);
                ubw = instance.getTime();
                break;

            case RchConstants.ROTA_VIRUS_3:
                instance.add(Calendar.DAY_OF_WEEK, 98);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -98);
                instance.add(Calendar.MONTH, 24);
                ubw = instance.getTime();
                break;

            case RchConstants.DPT_1:
                instance.add(Calendar.MONTH, 12);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -12);
                instance.add(Calendar.MONTH, 82);
                ubw = instance.getTime();
                break;

            case RchConstants.DPT_2:
                instance.add(Calendar.MONTH, 13);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -13);
                instance.add(Calendar.MONTH, 83);
                ubw = instance.getTime();
                break;

            case RchConstants.DPT_3:
                instance.add(Calendar.MONTH, 14);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -14);
                instance.add(Calendar.MONTH, 84);
                ubw = instance.getTime();
                break;

            case RchConstants.F_IPV_2_01:
            case RchConstants.F_IPV_2_05:
                instance.add(Calendar.DAY_OF_WEEK, 98);
                lbw = instance.getTime();
                instance.add(Calendar.DAY_OF_WEEK, -98);
                instance.add(Calendar.MONTH, 12);
                ubw = instance.getTime();
                break;

            case RchConstants.VITAMIN_A:
                instance.add(Calendar.MONTH, 8);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -8);
                instance.add(Calendar.MONTH, 60);
                ubw = instance.getTime();
                break;

            case RchConstants.OPV_BOOSTER:
            case RchConstants.DPT_BOOSTER:
                instance.add(Calendar.MONTH, 16);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -16);
                ubw = instance.getTime();
                break;

            case RchConstants.MEASLES_RUBELLA_1:
                instance.add(Calendar.MONTH, 9);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -9);
                instance.add(Calendar.MONTH, 59);
                ubw = instance.getTime();
                break;

            case RchConstants.MEASLES_RUBELLA_2:
                instance.add(Calendar.MONTH, 16);
                lbw = instance.getTime();
                instance.add(Calendar.MONTH, -16);
                instance.add(Calendar.MONTH, 60);
                ubw = instance.getTime();
                break;
            default:
        }

        return !lbw.before(currentDate) || !ubw.after(currentDate);
    }
}
