package com.argusoft.sewa.android.app.datastructure;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.component.MedicineDetailComponent;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyListInColorComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.MyVaccination;
import com.argusoft.sewa.android.app.component.listeners.CheckBoxChangeListener;
import com.argusoft.sewa.android.app.component.listeners.TextChangeListener;
import com.argusoft.sewa.android.app.component.listeners.TextFocusChangeListener;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class FormGenerator {

    private FormGenerator() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * this is generate the View of question with all requirement element
     */
    public static void generateQuestion(QueFormBean queFormBean, Context context) {

        LinearLayout questionUIFrame = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        questionUIFrame.setOrientation(LinearLayout.VERTICAL);
        questionUIFrame.setLayoutParams(layoutParams);

        queFormBean.setTitleView(MyStaticComponents.generateTitleView(context, queFormBean.getTitle()));
        queFormBean.setSubTitleView(MyStaticComponents.generateSubTitleView(context, queFormBean.getSubtitle()));
        queFormBean.setInstructionsView(MyStaticComponents.generateInstructionView(context, queFormBean.getInstruction()));
        boolean isBold = true;
        if (queFormBean.getFormulas() != null) {
            for (FormulaTagBean formulaTagBean : queFormBean.getFormulas()) {
                if (formulaTagBean.getFormulavalue().contains("notBoldQuestion")){
                    isBold = false;
                    break;
                }
            }
        }
        if (!isBold){
            queFormBean.setQuestionView(MyStaticComponents.generateAnswerView(context, queFormBean.getQuestion()));
        } else {
            queFormBean.setQuestionView(MyStaticComponents.generateQuestionView(queFormBean.getHelpvideofield(), queFormBean.getHint(), context, queFormBean.getQuestion()));
        }

        generateQuestionTypeView(queFormBean, context);

        if (queFormBean.getSubform() != null && queFormBean.getSubform().trim().length() > 0 && !queFormBean.getSubform().trim().equalsIgnoreCase("null")) {

            String[] mediaDisplay = UtilBean.split(queFormBean.getSubform(), GlobalTypes.COMMA);

            if (mediaDisplay.length > 0) {
                for (String media : mediaDisplay) {
                    String[] keyMap = UtilBean.split(media, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (keyMap[0].equalsIgnoreCase("hasMultiMedia")) {
                        List<FieldValueMobDataBean> dataSources = UtilBean.getDataMapValues(keyMap[1]);
                        if (dataSources != null && !dataSources.isEmpty()) {
                            String fileFormat = dataSources.get(0).getValue();
                            fileFormat = fileFormat.substring(fileFormat.lastIndexOf(".") + 1);
                            if (fileFormat.equalsIgnoreCase("gif") || fileFormat.equalsIgnoreCase("jpg") || fileFormat.equalsIgnoreCase("png") || fileFormat.equalsIgnoreCase("psd") || fileFormat.equalsIgnoreCase("tif") || fileFormat.equalsIgnoreCase("jpeg")) {
                                questionUIFrame.addView(getImage(context, dataSources));
                            } else if (fileFormat.equalsIgnoreCase("amr") || fileFormat.equalsIgnoreCase("mp3") || fileFormat.equalsIgnoreCase("wma")) {
                                questionUIFrame.addView(getAudio(context, queFormBean, keyMap[1]));
                            } else if (fileFormat.equalsIgnoreCase("mp4") || fileFormat.equalsIgnoreCase("avi") || fileFormat.equalsIgnoreCase("mpg") || fileFormat.equalsIgnoreCase("3gp")) {
                                if (queFormBean.getType().equals(GlobalTypes.SHOW_VIDEO_MANDATORY)) {
                                    questionUIFrame.addView(getMandatoryVideo(context, keyMap[1], queFormBean.getId()));
                                } else {
                                    questionUIFrame.addView(getVideo(context, keyMap[1]));
                                }
                            }
                        } else {
                            // no media found application
                            questionUIFrame.addView(MyStaticComponents.generateQuestionView(null, null, context, GlobalTypes.NO_MEDIA_FOUND));
                        }
                    }
                    ////image and audio code here////
                }
            }
        }

        if (queFormBean.getTitleView() != null) {
            questionUIFrame.addView(queFormBean.getTitleView());
        }

        if (queFormBean.getSubTitleView() != null) {
            questionUIFrame.addView(queFormBean.getSubTitleView());
        }

        if (queFormBean.getInstructionsView() != null) {
            questionUIFrame.addView(queFormBean.getInstructionsView());
        }

        if (queFormBean.getQuestionView() != null) {
            questionUIFrame.addView(queFormBean.getQuestionView());
        }

        if (queFormBean.getQuestionTypeView() != null) {
            if (queFormBean.getType().equalsIgnoreCase(GlobalTypes.LIST_IN_COLOR)) {
                MyListInColorComponent component = (MyListInColorComponent) queFormBean.getQuestionTypeView();
                questionUIFrame.addView(component.getListView());
                if (queFormBean.getMandatorymessage() != null && queFormBean.getMandatorymessage().trim().length() > 0 && !queFormBean.getMandatorymessage().trim().equalsIgnoreCase("null")) {
                    questionUIFrame.addView(MyStaticComponents.generateInstructionView(context, queFormBean.getMandatorymessage()));
                }
            } else {
                questionUIFrame.addView((View) queFormBean.getQuestionTypeView());
            }
        }
        queFormBean.setQuestionUIFrame(questionUIFrame);
    }

    private static View getImage(Context context, List<FieldValueMobDataBean> dataSources) {
        if (dataSources != null && !dataSources.isEmpty()) {
            String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED) + dataSources.get(0).getValue();
            if (UtilBean.isFileExists(path)) {
                ImageView imageView = MyStaticComponents.getImageView(context, -1, -1, null);
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, UtilBean.getDpsAccordingScreenHeight(context, 40)));
                imageView.setPadding(0, 0, 0, 0);
                return imageView;
            }
        }
        return MyStaticComponents.generateQuestionView(null, null, context, GlobalTypes.NO_MEDIA_FOUND);
    }

    private static View getAudio(Context context, QueFormBean queFormBean, String dataMap) {
        return MyDynamicComponents.getAudioPlayer(context, queFormBean, dataMap, -1);
    }

    private static View getVideo(Context context, String dataMap) {
        return MyStaticComponents.getVideoShow(context, dataMap, -1);
    }

    private static View getMandatoryVideo(Context context, String dataMap, Integer questionId) {
        return MyStaticComponents.getMandatoryVideoShow(context, dataMap, -1, questionId);
    }

    /**
     * this will generate answer view according to question type
     */
    private static void generateQuestionTypeView(QueFormBean queFormBean, Context context) {
        String queType = queFormBean.getType();
        int questionId;

        if (queFormBean.getIshidden().equalsIgnoreCase(GlobalTypes.TRUE)) {
            DynamicUtils.applyFormulaForHiddenQuestion(queFormBean, true);
        } else {
            DynamicUtils.applyFormula(queFormBean, true);
        }
        if (queType.equalsIgnoreCase(GlobalTypes.COMBO) || queType.equalsIgnoreCase(GlobalTypes.COMBO_BOX_DYNAMIC_SELECT)) {
            Spinner spinner = MyDynamicComponents.getSpinner(context, queFormBean, -1);
            queFormBean.setQuestionTypeView(spinner); // to add component into question bean
        } else if (queType.equalsIgnoreCase(GlobalTypes.RADIOBUTTON)) {
            boolean isHorizontal = false;
            List<OptionTagBean> options = queFormBean.getOptions();
            if (options != null && options.size() == 2 && options.get(0).getValue().trim().length() < 8) {
                isHorizontal = true;
            }
            if (queFormBean.getId() == 9997 || queFormBean.getId() == 9998) {
                RadioGroup radioGroup = MyDynamicComponents.getRadioGroup(context, queFormBean, -1, false);
                queFormBean.setQuestionTypeView(radioGroup);
            } else {
                RadioGroup radioGroup = MyDynamicComponents.getRadioGroup(context, queFormBean, -1, isHorizontal);
                queFormBean.setQuestionTypeView(radioGroup);
            }
        } else if (queType.equalsIgnoreCase(GlobalTypes.CUSTOM_DATE_BOX)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getCustomDatePicker(queFormBean, context));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CUSTOM_TIME_PICKER)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getCustomTimePicker(queFormBean, context));
        } else if (queType.equalsIgnoreCase(GlobalTypes.AGE_BOX)) {
            queFormBean.setQuestionTypeView(MyStaticComponents.getAgeBox(context, -1, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.AGE_BOX_DISPLAY)) {
            String defaultValue = GlobalTypes.NOT_AVAILABLE;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null && defaultValue.trim().length() > 0 && !defaultValue.equalsIgnoreCase("null")) {
                    queFormBean.setAnswer(defaultValue);
                    String[] split = UtilBean.split(defaultValue, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (split.length == 3) {
                        defaultValue = UtilBean.getAgeDisplay(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    }
                }
            }
            queFormBean.setQuestionTypeView(MyStaticComponents.generateAnswerView(context, defaultValue));
        } else if (queType.equalsIgnoreCase(GlobalTypes.TEXT_BOX_WITH_AUDIO)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getAudioTextBox(context, queFormBean, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.TEXT_AREA)) {
            TextInputLayout editText = MyStaticComponents.getEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), -1);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue);
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHARDHAM_TEXT_AREA)) {
            TextInputLayout editText = MyStaticComponents.getChardhamEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), -1);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue);
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
        } else if (queType.equalsIgnoreCase(GlobalTypes.NUMBER_TEXT_BOX_COMPONENT)) {
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            TextInputLayout editText = MyStaticComponents.getEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), inputType);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue);
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
        } else if (queType.equalsIgnoreCase(GlobalTypes.TEXT_BOX)) {
            int inputType = -1;
            List<ValidationTagBean> validations = queFormBean.getValidations();
            if (validations != null && !validations.isEmpty()) {
                for (ValidationTagBean validation : validations) {
                    if (validation.getMethod().equalsIgnoreCase(FormulaConstants.NUMERIC) || validation.getMethod().equalsIgnoreCase(FormulaConstants.GREATER_THAN_0)) {
                        inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.PERSON_NAME)) {
                        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.ONLY_NUMBERS)) {
                        inputType = InputType.TYPE_CLASS_NUMBER;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.ALPHA_NUMERIC_WITH_CAPS)) {
                        inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                        break;
                    }
                }
            }
            TextInputLayout editText = MyStaticComponents.getEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), inputType);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue);
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
            // for set hint
            DynamicUtils.applyFormula(queFormBean, false);
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHARDHAM_TEXT_BOX)) {
            int inputType = -1;
            List<ValidationTagBean> validations = queFormBean.getValidations();
            if (validations != null && !validations.isEmpty()) {
                for (ValidationTagBean validation : validations) {
                    if (validation.getMethod().equalsIgnoreCase(FormulaConstants.NUMERIC) || validation.getMethod().equalsIgnoreCase(FormulaConstants.GREATER_THAN_0)) {
                        inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.PERSON_NAME)) {
                        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.ONLY_NUMBERS)) {
                        inputType = InputType.TYPE_CLASS_NUMBER;
                        break;
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.ALPHA_NUMERIC_WITH_CAPS)) {
                        inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                        break;
                    }
                }
            }
            TextInputLayout editText = MyStaticComponents.getChardhamEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), inputType);
            editText.setPadding(0, 0, 0, 40);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue);
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
            // for set hint
            DynamicUtils.applyFormula(queFormBean, false);
        } else if (queType.equalsIgnoreCase(GlobalTypes.TEXT_BOX_CHANGE_LISTENER)) {
            int inputType = -1;
            List<ValidationTagBean> validations = queFormBean.getValidations();
            if (validations != null && !validations.isEmpty()) {
                for (ValidationTagBean validation : validations) {
                    if (validation.getMethod().equalsIgnoreCase(FormulaConstants.NUMERIC) ||
                            validation.getMethod().equalsIgnoreCase(FormulaConstants.GREATER_THAN_0) ||
                            validation.getMethod().equalsIgnoreCase(FormulaConstants.ONLY_NUMBERS)) {
                        inputType = InputType.TYPE_CLASS_NUMBER;
                        break;
                    }
                }
            }
            TextInputLayout editText = MyStaticComponents.getEditText(context, queFormBean.getQuestion(), -1, queFormBean.getLength(), inputType);
            editText.setPadding(0, -20, 0, 60);
            if (editText.getEditText() != null) {
                editText.getEditText().setOnFocusChangeListener(new TextFocusChangeListener(context, queFormBean));
                editText.getEditText().addTextChangedListener(new TextChangeListener(editText.getEditText()));
            }
            String defaultValue;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    queFormBean.setAnswer(defaultValue + "----"); //DON'T DELETE THE APPEND OF '----' IN THE STRING ANSWER. IT IS FOR VALIDATION PURPOSE
                    editText.getEditText().setText(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(editText);
            // for set hint
            DynamicUtils.applyFormula(queFormBean, false);
        } else if (queType.equalsIgnoreCase(GlobalTypes.QR_SCAN)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getQRScannerView(context, queFormBean, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.PHOTO_PICKER)) {
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
            queFormBean.setQuestionTypeView(MyDynamicComponents.getPhotoPicker(queFormBean, context, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.SHOW_VIDEO)) {
            queFormBean.setQuestionTypeView(MyStaticComponents.getVideoShow(context, queFormBean.getDatamap(), -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.SHOW_VIDEO_MANDATORY)) {
            if (queFormBean.getLoopCounter() == 0) {
                SharedStructureData.videoShownMap.put(queFormBean.getId(), Boolean.FALSE);
                queFormBean.setQuestionTypeView(MyStaticComponents.getMandatoryVideoShow(context, queFormBean.getDatamap(), -1, queFormBean.getId()));
            } else {
                questionId = DynamicUtils.getLoopId(queFormBean.getId(), queFormBean.getLoopCounter());
                SharedStructureData.videoShownMap.put(questionId, Boolean.FALSE);
                queFormBean.setQuestionTypeView(MyStaticComponents.getMandatoryVideoShow(context, queFormBean.getDatamap(), -1, questionId));
            }
        } else if (queType.equalsIgnoreCase(GlobalTypes.WEIGHT_BOX)) {
            boolean isCombo = true;
            boolean isDateShow = false;
            if (queFormBean.getDatamap() != null && queFormBean.getDatamap().trim().length() > 0) {
                String[] dataMap = UtilBean.split(queFormBean.getDatamap().trim(), GlobalTypes.KEY_VALUE_SEPARATOR);
                if (dataMap.length > 0) {
                    isCombo = !dataMap[0].trim().equalsIgnoreCase("TF");
                }
                if (dataMap.length > 1) {
                    isDateShow = dataMap[1].trim().equalsIgnoreCase(GlobalTypes.TRUE);
                }
            }
            String checkBoxLabel = null;
            if (queFormBean.getSubform() != null && queFormBean.getSubform().trim().length() > 0) {
                checkBoxLabel = queFormBean.getSubform().trim();
            }
            queFormBean.setQuestionTypeView(MyDynamicComponents.getWeightBox(context, queFormBean, -1, isCombo, isDateShow, checkBoxLabel));
        } else if (queType.equalsIgnoreCase(GlobalTypes.WEIGHT_BOX_DISPLAY)) {
            String defaultValue = GlobalTypes.NOT_AVAILABLE;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue == null || defaultValue.trim().length() == 0 || defaultValue.trim().equalsIgnoreCase("null")) {
                    defaultValue = GlobalTypes.NOT_AVAILABLE;
                } else {
                    queFormBean.setAnswer(defaultValue);
                }
            }
            queFormBean.setQuestionTypeView(MyStaticComponents.generateAnswerView(context, UtilBean.setWeightDisplay(defaultValue)));
        } else if (queType.equalsIgnoreCase(GlobalTypes.TEMPERATURE_BOX)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getTemperatureBox(context, queFormBean, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.LABEL_FORMULA)) {
            String relatedPropertyName = null;
            String defaultValue = GlobalTypes.NOT_AVAILABLE;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue == null || defaultValue.trim().length() == 0 || defaultValue.trim().equalsIgnoreCase("null")) {
                    defaultValue = GlobalTypes.NOT_AVAILABLE;
                } else {
                    queFormBean.setAnswer(defaultValue);
                }
            }
            MaterialTextView labelFormula;
            if (relatedPropertyName != null && relatedPropertyName.equalsIgnoreCase(GlobalTypes.RELATED_PROPERTY_FOR_MALNUTRITION_GRADE)) {
                labelFormula = MyStaticComponents.getMaterialTextView(context, defaultValue, -1, -1, false);
                if (labelFormula != null) {
                    labelFormula.setPadding(30, 0, 0, 0);
                }
            } else {
                labelFormula = MyStaticComponents.generateAnswerView(context, defaultValue);
                if (BuildConfig.FLAVOR.equalsIgnoreCase(GlobalTypes.UTTARAKHAND_FLAVOR)) {
                    labelFormula.setPadding(0, 0, 0, 40);
                }
            }
            queFormBean.setQuestionTypeView(labelFormula);
            if (queFormBean.getLoopCounter() == 0) {
                SharedStructureData.currentMemberUHId = SharedStructureData.relatedPropertyHashTable.get("uniqueHealthId");
            } else {
                SharedStructureData.currentMemberUHId = SharedStructureData.relatedPropertyHashTable.get("uniqueHealthId" + queFormBean.getLoopCounter());
            }
            DynamicUtils.applyFormula(queFormBean, true);
        } else if (queType.equalsIgnoreCase(GlobalTypes.MULTI_SELECT)) {
            CheckBoxChangeListener checkBoxChangeListener = new CheckBoxChangeListener(queFormBean, true, false);
            LinearLayout multiSelect = MyDynamicComponents.getCheckBoxInGroup(context, queFormBean, -1, checkBoxChangeListener);
            queFormBean.setQuestionTypeView(multiSelect);
        } else if (queType.equalsIgnoreCase(GlobalTypes.SEARCHABLE_MULTI_SELECT)) {
            CheckBoxChangeListener checkBoxChangeListener = new CheckBoxChangeListener(queFormBean, true, false);
            LinearLayout multiSelect = MyDynamicComponents.getChardhamCheckBoxInGroup(context, queFormBean, -1, checkBoxChangeListener);
            queFormBean.setQuestionTypeView(multiSelect);
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHARDHAM_CHIP_GROUP)) {
            LinearLayout multiSelect = MyDynamicComponents.getChardhamChipGroup(context, queFormBean, -1);
            multiSelect.setPadding(0, 0, 0, 40);
            queFormBean.setQuestionTypeView(multiSelect);
        } else if (queType.equalsIgnoreCase(GlobalTypes.SINGLE_CHECK_BOX)) {
            String label;
            List<OptionDataBean> optionTagBeans = UtilBean.getOptionsOrDataMap(queFormBean, false);
            if (!optionTagBeans.isEmpty()) {
                OptionDataBean option = optionTagBeans.get(0);
                label = option.getValue();
            } else {
                label = queFormBean.getQuestion();
            }
            boolean isChecked;
            String isMandatory = queFormBean.getIsmandatory();
            isChecked = isMandatory != null && isMandatory.equalsIgnoreCase(GlobalTypes.TRUE);
            MaterialCheckBox checkBox = MyStaticComponents.getCheckBox(context, label, -1, isChecked);
            checkBox.setOnCheckedChangeListener(new CheckBoxChangeListener(queFormBean, false, isChecked));
            //setting default checked
            if (queFormBean.getRelatedpropertyname() != null) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                String defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                if (defaultValue != null) {
                    checkBox.setChecked(false);
                    if (defaultValue.equals("T")) {
                        checkBox.setChecked(true);
                    }
                }
            }
            queFormBean.setQuestionTypeView(checkBox);
        } else if (queType.equalsIgnoreCase(GlobalTypes.BLOOD_PRESSURE_MEASUREMENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getBloodPressureBox(context, queFormBean, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHARDHAM_BLOOD_PRESSURE_MEASUREMENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getChardhamBloodPressureBox(context, queFormBean, -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.PLAY_AUDIO)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getAudioPlayer(context, queFormBean, queFormBean.getDatamap(), -1));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CALL)) {
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
            String defaultValue = null;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
            }
            if (defaultValue == null) {
                defaultValue = "108";
            }
            queFormBean.setQuestionTypeView(MyStaticComponents.getCallView(context, UtilBean.getMyLabel(defaultValue), -1, Gravity.CENTER_HORIZONTAL));
        } else if (queType.equalsIgnoreCase(GlobalTypes.LABEL)) {
            queFormBean.setIsmandatory(GlobalTypes.FALSE);
        } else if (queType.equalsIgnoreCase(GlobalTypes.LIST_IN_COLOR)) {
            String beneficiaryId = null;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String relatedPropertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    relatedPropertyName += queFormBean.getLoopCounter();
                }
                beneficiaryId = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
            }
            if (beneficiaryId != null && beneficiaryId.trim().length() > 0 && !beneficiaryId.trim().equalsIgnoreCase("null")) {
                if (queFormBean.getDatamap() != null && queFormBean.getDatamap().trim().equalsIgnoreCase(GlobalTypes.TRUE)) {
                    // direct map is given
                    queFormBean.setQuestionTypeView(new MyListInColorComponent(context, SharedStructureData.getListOfLIC()));
                }
            } else {
                queFormBean.setQuestionTypeView(new MyListInColorComponent(context, null));
            }
        } else if (queType.equalsIgnoreCase(GlobalTypes.VACCINATIONS_TYPE)) {
            PageFormBean pageFromNext = DynamicUtils.getPageFromNext(DynamicUtils.getLoopId(queFormBean));
            if (pageFromNext != null) {
                pageFromNext.setIsVaccinations(true);
                pageFromNext.setMyVaccination(new MyVaccination(context, queFormBean));
                queFormBean.setQuestionTypeView(pageFromNext.getMyVaccination().getVaccinationView());
            }
        } else if (queType.equalsIgnoreCase(GlobalTypes.SIMPLE_RADIO_DATE)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getRadioButtonDate(context, queFormBean, null, queFormBean.getDatamap()));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHECKBOX_TEXT_BOX)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getCheckBoxWithTextBox(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.MEMBERS_LIST_COMPONENT)) {
            queFormBean.setQuestionTypeView(UtilBean.getMembersListForDisplay(context, null));
        } else if (queType.equalsIgnoreCase(GlobalTypes.MEMBER_FULL_NAME_COMPONENT)) {
            queFormBean.setQuestionTypeView(UtilBean.getMemberFullNameForDisplay(context));
        } else if (queType.equalsIgnoreCase(GlobalTypes.MOTHER_CHILD_RELATIONSHIP)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getMotherChildRelationshipView(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.ORAL_SCREENING_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getOralScreeningComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.BREAST_SCREENING_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getBreastScreeningComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CERVICAL_SCREENING_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getCervicalScreeningComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.BMI_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getBMIComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.HEALTH_INFRASTRUCTURE_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getHealthInfrastructureComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHARDHAM_HEALTH_INFRASTRUCTURE_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getChardhamHealthInfrastructureComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.HUSBAND_WIFE_RELATIONSHIP)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getHusbandWifeRelationshipView(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.IMMUNISATION_GIVEN_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getImmunisationGivenComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.CHILD_GROWTH_CHART_COMPONENT)) {
            String gender = SharedStructureData.relatedPropertyHashTable.get("gender");
            boolean isBoy = gender == null || !gender.equals(GlobalTypes.FEMALE);
            queFormBean.setQuestionTypeView(MyDynamicComponents.getChildGrowthChart(isBoy, context));
        } else if (queType.equalsIgnoreCase(GlobalTypes.SCHOOL_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getSchoolComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.OTP_BASED_VERIFICATION_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getOTPBasedVerificationComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.FORM_SUBMISSION_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getFormSubmissionComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.MEMBER_DETAILS_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getMemberDetailsComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.OPENRDTREADER_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getORDTComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.HEALTH_ID_MANAGEMENT_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getHealthIdManagementView(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.SET_DRUG_COMPONENT)) {
            queFormBean.setQuestionTypeView(new MedicineDetailComponent(context, queFormBean));
        } else if (queType.equalsIgnoreCase(GlobalTypes.HEALTH_ADVISORY_CHARDHAM_COMPONENT)) {
            queFormBean.setQuestionTypeView(MyDynamicComponents.getHealthAdvisoryChardhamComponent(context, queFormBean));
        } else {
            Log.i("FormGenerator", "###########################" + queType + "########## not handled");
        }
    }
}
