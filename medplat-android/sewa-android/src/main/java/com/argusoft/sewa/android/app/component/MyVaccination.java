package com.argusoft.sewa.android.app.component;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.argusoft.sewa.android.app.activity.DynamicFormActivity;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author alpesh
 */
public class MyVaccination implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {

    private final Context context;
    private final LinearLayout vaccinationView;
    private final LinearLayout body;
    private QueFormBean queFormBean;
    private Map<String, MyVaccinationStatus> vaccinationsDoz;
    private List<String> vaccinations;
    private int counter;
    private int totalVaccinations;

    public MyVaccination(Context context, QueFormBean queFormBean) {
        this.context = context;
        this.queFormBean = queFormBean;

        String vaccinationsString;
        String relatedPropertyName = "";

        if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
            relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
        }

        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            relatedPropertyName += queFormBean.getLoopCounter();
        }
        vaccinationsString = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
        vaccinations = UtilBean.getListFromStringBySeparator(vaccinationsString, GlobalTypes.COMMA);

        if (vaccinations != null && vaccinations.size() > 1) {
            Collections.sort(vaccinations, UtilBean.VACCINATION_COMPARATOR);
        }
        this.queFormBean.setAnswer(null);

        vaccinationView = DynamicUtils.generateDynamicVaccinations(context);
        vaccinationView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        body = vaccinationView.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        // add new  child
        if (vaccinations != null && !vaccinations.isEmpty()) {
            queFormBean.setIsmandatory(GlobalTypes.TRUE);
            totalVaccinations = vaccinations.size();
            counter = 0;
            vaccinationsDoz = new HashMap<>();
            for (String vaccination : vaccinations) {
                MyVaccinationStatus status = new MyVaccinationStatus(context, this, vaccination, MyVaccinationStatus.GivenStatus.NULL, 0, null);
                body.addView(status.getVaccinationView());
                vaccinationsDoz.put(vaccination, status);
            }
            // show the default vaccination View
            MyVaccinationStatus status = getStatusByCounter(counter);
            if (status != null) {
                status.setVisibility(true);
            }
        } else {
            totalVaccinations = 0;
            counter = -1;
            body.addView(MyStaticComponents.generateAnswerView(context, "No vaccines are due"));
            if (DynamicFormActivity.formEngine != null) {
                DynamicFormActivity.formEngine.setBackListener(null);
                DynamicFormActivity.formEngine.setNextListener(null);
            }
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
        }

    }

    public void reSet(List<String> vaccinations) {
        if (vaccinations != null && vaccinations.size() > 1) {
            Collections.sort(vaccinations, UtilBean.VACCINATION_COMPARATOR);
        }
        this.vaccinations = vaccinations;
        queFormBean.setAnswer(null);
        counter = 0;
        if (body != null) {
            body.removeAllViews();
            if (vaccinations != null && !vaccinations.isEmpty()) {
                queFormBean.setIsmandatory(GlobalTypes.TRUE);
                totalVaccinations = vaccinations.size();
                counter = 0;
                if (vaccinationsDoz != null) {
                    vaccinationsDoz.clear();
                } else {
                    vaccinationsDoz = new HashMap<>();
                }
                for (String vaccination : vaccinations) {
                    MyVaccinationStatus status = new MyVaccinationStatus(context, this, vaccination, MyVaccinationStatus.GivenStatus.NULL, 0, null);
                    body.addView(status.getVaccinationView());
                    vaccinationsDoz.put(vaccination, status);
                }
                // show the default vaccination View
                MyVaccinationStatus status = getStatusByCounter(counter);
                if (status != null) {
                    status.setVisibility(true);
                }
            } else {
                totalVaccinations = 0;
                counter = -1;
                body.addView(MyStaticComponents.generateAnswerView(context, "No vaccines are due"));
                if (DynamicFormActivity.formEngine != null) {
                    DynamicFormActivity.formEngine.setBackListener(null);
                    DynamicFormActivity.formEngine.setNextListener(null);
                }
                queFormBean.setIsmandatory(GlobalTypes.FALSE);
            }
        }
    }

    public void showFirst() {
        MyVaccinationStatus statusByCounter = getStatusByCounter(counter);
        if (statusByCounter != null) {
            statusByCounter.setVisibility(false);
            counter = 0;
            statusByCounter = getStatusByCounter(counter);
            if (statusByCounter != null) {
                statusByCounter.setVisibility(true);
            }

        }
    }

    private MyVaccinationStatus getStatusByCounter(int count) {
        if (vaccinationsDoz != null && vaccinations != null && !vaccinationsDoz.isEmpty() && !vaccinations.isEmpty() && count > -1 && count < totalVaccinations) {
            return vaccinationsDoz.get(vaccinations.get(count));
        }
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup parent, int position) {
        MyVaccinationStatus status = getStatusByCounter(counter);
        if (status != null) {
            if (position == 0) {    // yes pressed
                if (DynamicFormActivity.formEngine != null) {
                    DynamicFormActivity.formEngine.setNextListener(this);
                }
                status.setIsTaken(MyVaccinationStatus.GivenStatus.YES);
                status.setDateOfTaken(0);
                status.showDatePicker(true);
            } else if (position == 1) {
                status.setIsTaken(MyVaccinationStatus.GivenStatus.NO);
                status.setDateOfTaken(0);
                status.showDatePicker(false);
                SharedStructureData.vaccineGivenDateMap.remove(status.getName().trim());
            }
        }
        setAnswer();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MyVaccinationStatus get = getStatusByCounter(counter);
        if (get != null) {
            if (id == DynamicUtils.ID_NEXT_BUTTON) { // next pressed
                int tempCount = counter;
                while (true) {
                    String valid = get.isValid();
                    if (valid == null) {
                        if (counter < totalVaccinations - 1) {
                            if (DynamicFormActivity.formEngine != null) {
                                DynamicFormActivity.formEngine.setBackListener(this);
                            }
                            counter++;
                            MyVaccinationStatus nextVacci = getStatusByCounter(counter);
                            if (nextVacci != null) {
                                if (nextVacci.getIsTaken().equals(MyVaccinationStatus.GivenStatus.SKIP)) {
                                    continue;
                                }
                                get.setVisibility(false);
                                nextVacci.setVisibility(true);
                            }
                        } else {
                            counter = tempCount;
                            if (DynamicFormActivity.formEngine != null) {
                                DynamicFormActivity.formEngine.setNextListener(null);
                            }
                        }
                    } else {
                        SewaUtil.generateToast(context, valid);
                    }
                    break;
                }
                setAnswer();
            } else if (id == DynamicUtils.ID_BACK_BUTTON) {
                while (true) {
                    if (counter > 0) {
                        if (DynamicFormActivity.formEngine != null) {
                            DynamicFormActivity.formEngine.setNextListener(this);
                        }
                        counter--;
                        if (counter == 0 && DynamicFormActivity.formEngine != null) {
                            DynamicFormActivity.formEngine.setBackListener(null);
                        }
                        MyVaccinationStatus preVecci = getStatusByCounter(counter);
                        if (preVecci != null && preVecci.getIsTaken().equals(MyVaccinationStatus.GivenStatus.SKIP)) {
                            continue;
                        } else {
                            get.setVisibility(false);
                            if (preVecci != null) {
                                preVecci.setVisibility(true);
                            }
                        }
                    } else {
                        if (DynamicFormActivity.formEngine != null) {
                            DynamicFormActivity.formEngine.setBackListener(null);
                        }
                    }
                    break;
                }
                setAnswer();
            } else if (id == DynamicUtils.ID_OF_CUSTOM_DATE_PICKER) {  // if date picker clicked
                DatePickerDialog dp;
                if (get.getTxtDate() != null) {
                    String[] ddmmyyy = UtilBean.split(get.getTxtDate().trim(), GlobalTypes.DATE_STRING_SEPARATOR);
                    if (ddmmyyy.length == 3) {
                        dp = new DatePickerDialog(context, this, Integer.parseInt(ddmmyyy[2]), Integer.parseInt(ddmmyyy[1]) - 1, Integer.parseInt(ddmmyyy[0]));
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        dp = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                } else {
                    Calendar calendar = Calendar.getInstance();
                    dp = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
                dp.show();
            }
        } else {
            if (DynamicFormActivity.formEngine != null) {
                DynamicFormActivity.formEngine.setBackListener(null);
                DynamicFormActivity.formEngine.setNextListener(null);
            }
        }
    }

    public int getTotalVaccinations() {
        return totalVaccinations;
    }

    @Override
    public void onDateSet(DatePicker dp, int year, int month, int date) {
        MyVaccinationStatus get = getStatusByCounter(counter);
        if (get != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, date);
            UtilBean.clearTimeFromDate(calendar);
            get.setDateOfTaken(calendar.getTimeInMillis());
            int loopcounter = 0;
            if (!queFormBean.isIgnoreLoop()) {
                loopcounter = queFormBean.getLoopCounter();
            }
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("currentVaccine", get.getName());
            editor.apply();
            String checkValidation = DynamicUtils.checkValidation(String.valueOf(get.getDateOfTaken()), loopcounter, queFormBean.getValidations());
            if (checkValidation != null) {
                get.setDateOfTaken(0);
                SewaUtil.generateToast(context, checkValidation);
                if (DynamicFormActivity.formEngine != null) {
                    DynamicFormActivity.formEngine.setNextListener(this);
                }
            } else {
                SharedStructureData.vaccineGivenDateMap.put(get.getName().trim(), calendar.getTime());
                get.setTextDate(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar.getTime()));
            }
            // fire any validation 
            setAnswer();
        }
    }

    public LinearLayout getVaccinationView() {
        return vaccinationView;
    }

    private void setAnswer() {
        boolean flag = true;
        StringBuilder finalAnswer = new StringBuilder();

        if (vaccinationsDoz != null && !vaccinationsDoz.isEmpty() && vaccinations != null && !vaccinations.isEmpty()) {
            for (String name : vaccinations) {
                MyVaccinationStatus vaccinationStatus = vaccinationsDoz.get(name);
                if (vaccinationStatus != null && vaccinationStatus.isValid() == null) {
                    if (vaccinationStatus.getAnswerString() != null) {
                        finalAnswer.append(vaccinationStatus.getAnswerString()).append("-");
                    }
                } else {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            SharedStructureData.setVaccinations(vaccinationsDoz);
            queFormBean.setAnswer(finalAnswer.toString());
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String uniqueHealthIdChild;
            Map<String, String> mapOfVaccinationGivenWithUniqueHealthId;
            if (queFormBean.getLoopCounter() == 0) {
                uniqueHealthIdChild = SharedStructureData.relatedPropertyHashTable.get("uniqueHealthIdChild");
            } else {
                uniqueHealthIdChild = SharedStructureData.relatedPropertyHashTable.get("uniqueHealthIdChild" + queFormBean.getLoopCounter());
            }
            if (uniqueHealthIdChild != null) {
                Gson gson = new Gson();
                String vaccinationMapWithUniqueHealthId = sharedPreferences.getString("vaccinationMapWithUniqueHealthId", null);
                if (vaccinationMapWithUniqueHealthId != null) {
                    mapOfVaccinationGivenWithUniqueHealthId = gson.fromJson(vaccinationMapWithUniqueHealthId, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                } else {
                    mapOfVaccinationGivenWithUniqueHealthId = new HashMap<>();
                }
                mapOfVaccinationGivenWithUniqueHealthId.put(uniqueHealthIdChild, finalAnswer.toString());
                edit.putString("vaccinationMapWithUniqueHealthId", gson.toJson(mapOfVaccinationGivenWithUniqueHealthId));
            } else {
                edit.putString("vaccinationGiven", finalAnswer.toString());
            }
            edit.apply();

            // change the listener
            for (int i = counter + 1; i < totalVaccinations; i++) {
                MyVaccinationStatus statusByCounter = getStatusByCounter(i);
                if (statusByCounter != null && !statusByCounter.getIsTaken().equals(MyVaccinationStatus.GivenStatus.SKIP)) {
                    flag = false;
                }
            }
            if (flag) {
                if (DynamicFormActivity.formEngine != null) {
                    DynamicFormActivity.formEngine.setNextListener(null);
                }
            } else {
                if (DynamicFormActivity.formEngine != null) {
                    DynamicFormActivity.formEngine.setNextListener(this);
                }
            }
        } else {
            queFormBean.setAnswer(null);
            if (DynamicFormActivity.formEngine != null) {
                DynamicFormActivity.formEngine.setNextListener(this);
            }
        }
    }
}
