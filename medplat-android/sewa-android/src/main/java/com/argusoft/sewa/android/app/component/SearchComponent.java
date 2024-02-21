package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.qrscanner.QRScannerActivity;
import com.argusoft.sewa.android.app.util.CommonUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Defines methods for SearchComponent
 *
 * @author prateek
 * @since 27/01/23 6:13 PM
 */
public class SearchComponent extends LinearLayout {

    private final Context context;
    private final String label;
    private final List<String> searchTypes;
    private final SearchClick searchClick;
    private ClearClick clearClick;
    private final LocationSelection locationSelection;
    private ImageButton searchButton, clearButton;
    private ImageButton closeSearchButton;
    private String selectedType;
    private LinearLayout searchBox;
    private EditText editText;
    private DateComponent dateComponent;
    private LocationComponent locationComponent;
    private final String hint;
    private LocationMasterServiceImpl locationMasterService;
    private boolean isNearBySearch;
    //    private LocationSelectionFromDistrictComponent districtComponent;
    private Long selectedLocationId;
    LinearLayout parentLayout, buttonListLayout;

    public SearchComponent(Context context, String label, String hint, List<String> searchTypes,
                           SearchClick searchClick, LocationSelection locationSelection, LocationMasterServiceImpl locationMasterService) {
        super(context);
        this.context = context;
        this.label = label;
        this.searchTypes = searchTypes;
        this.searchClick = searchClick;
        this.locationSelection = locationSelection;
        this.hint = hint;
        this.locationMasterService = locationMasterService;
        initView();
    }

    public SearchComponent(Context context, String label, String hint, List<String> searchTypes,
                           SearchClick searchClick, ClearClick clearClick, LocationSelection locationSelection, LocationMasterServiceImpl locationMasterService) {
        super(context);
        this.context = context;
        this.label = label;
        this.searchTypes = searchTypes;
        this.searchClick = searchClick;
        this.clearClick = clearClick;
        this.locationSelection = locationSelection;
        this.hint = hint;
        this.locationMasterService = locationMasterService;
        initView();
    }

    public SearchComponent(Context context, String label, String hint, List<String> searchTypes,
                           SearchClick searchClick, ClearClick clearClick, LocationSelection locationSelection, LocationMasterServiceImpl locationMasterService, boolean isNearBySearch) {
        super(context);
        this.context = context;
        this.label = label;
        this.searchTypes = searchTypes;
        this.searchClick = searchClick;
        this.clearClick = clearClick;
        this.locationSelection = locationSelection;
        this.hint = hint;
        this.locationMasterService = locationMasterService;
        this.isNearBySearch = isNearBySearch;
        initView();
    }


    private void initView() {
        removeAllViews();
        LinearLayout linearLayout = MyStaticComponents.getLinearLayout(context, -1, VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        if (label != null) {
            MaterialTextView labelView = MyStaticComponents.generateQuestionView(null, null, context, this.label);
            linearLayout.addView(labelView);
        }

        LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.search_component_layout, null);
        linearLayout.addView(mainLayout);
        addView(linearLayout);

        ImageButton abhaQRScan = mainLayout.findViewById(R.id.abha_qr_button);
        abhaQRScan.setVisibility(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isNearBySearch ? VISIBLE : GONE);
        abhaQRScan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) context);
            intentIntegrator.setCaptureActivity(QRScannerActivity.class);
            intentIntegrator.initiateScan();
        });

        TextView searchTypeButton = mainLayout.findViewById(R.id.searchType);
        searchTypeButton.setText(UtilBean.getMyLabel("CLICK HERE to choose search type"));
        searchTypeButton.setOnClickListener(getSearchTypeClickListener());

        searchBox = mainLayout.findViewById(R.id.searchBox);

        editText = new AppCompatEditText(context);
        if (GlobalTypes.FLAVOUR_CHIP.equalsIgnoreCase(BuildConfig.FLAVOR)) {
            editText.setBackground(ContextCompat.getDrawable(context, R.drawable.chardham_edit_text_background));
            editText.setPadding(20, 20, 20, 20);
            editText.setMaxLines(2);
        } else {
            editText.setBackground(ContextCompat.getDrawable(context, R.drawable.line_background_bottom));
        }


        dateComponent = new DateComponent(context, "Select date here");
        locationComponent = new LocationComponent(context, "Select location");

        searchButton = mainLayout.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(getSearchButtonOnClickListener());

        clearButton = mainLayout.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(getClearButtonClickListener());

        if (searchTypes != null && !searchTypes.isEmpty()) {
            selectedType = searchTypes.get(0);
            searchTypeButton.setVisibility(VISIBLE);
        } else {
            searchTypeButton.setVisibility(GONE);
        }
        setSearchBoxAccordingToType();
    }

    private OnClickListener getSearchTypeClickListener() {
        return view -> {
            if (searchTypes != null && !searchTypes.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(UtilBean.getMyLabel("Choose a type of search"));
                String[] items = new String[searchTypes.size()];
                int i = 0;
                int checkedItem = 0;
                for (String type : searchTypes) {
                    items[i] = SearchTypes.getFullNameForSearchType(type);
                    if (type.equalsIgnoreCase(selectedType)) {
                        checkedItem = i;
                    }
                    i++;
                }
                builder.setSingleChoiceItems(items, checkedItem, (dialogInterface, which) -> {
                    selectedType = searchTypes.get(which);
                    setSearchBoxAccordingToType();
                    dialogInterface.dismiss();
                });
                builder.create().show();
            } else {
                SewaUtil.generateToast(context, "Sorry nothing to do here");
            }
        };
    }

    private void setSearchBoxAccordingToType() {
        if (SearchTypes.getSearchTypeText().contains(selectedType)) {
            searchBox.removeAllViews();
            searchBox.addView(editText);
            editText.setHint(UtilBean.getMyLabel(getHintInEditTextAccordingToType()));
            setKeyBoardValidationsAccordingToType();
        } else if (SearchTypes.getSearchTypeDate().contains(selectedType)) {
            searchBox.removeAllViews();
            searchBox.addView(dateComponent);
            dateComponent.setHint(UtilBean.getMyLabel(getHintInEditTextAccordingToType()));
        } else if (SearchTypes.getSearchTypeLocation().contains(selectedType)) {
            searchBox.removeAllViews();
            searchBox.addView(locationComponent);
            locationComponent.setHint(UtilBean.getMyLabel(getHintInEditTextAccordingToType()));
            openLocationSelectionDialog();
        } else {
            searchBox.removeAllViews();
            searchBox.addView(editText);
            if (hint != null) {
                editText.setHint(UtilBean.getMyLabel(hint));
            } else {
                editText.setHint(UtilBean.getMyLabel("Enter your text here"));
            }
        }
    }

    private void setKeyBoardValidationsAccordingToType() {
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[]{});

        if (SearchTypes.MOBILE_NUMBER.equalsIgnoreCase(selectedType)) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{
                    CommonUtil.getLengthInputFilter(10),
                    CommonUtil.getOnlyNumberInputFilter()
            });
        } else if (SearchTypes.ABHA_NUMBER.equalsIgnoreCase(selectedType)
                || SearchTypes.ABHA_ADDRESS.equalsIgnoreCase(selectedType)) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{
                    CommonUtil.getLengthInputFilter(14),
                    CommonUtil.getOnlyNumberInputFilter()
            });
        } else if (SearchTypes.NAME.equalsIgnoreCase(selectedType)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{
                    CommonUtil.getAlphanumericWithSpaceInputFilter(),
                    CommonUtil.getLengthInputFilter(20)
            });
        } else if (SearchTypes.FAMILY_ID.equalsIgnoreCase(selectedType)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{
                    CommonUtil.getLengthInputFilter(20),
                    CommonUtil.getFamilyIdInputFilter()
            });
        } else if (SearchTypes.MEMBER_ID.equalsIgnoreCase(selectedType)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{
                    CommonUtil.getLengthInputFilter(20),
                    CommonUtil.getMemberIdInputFilter()
            });
        }
    }

    private String getHintInEditTextAccordingToType() {
        if (selectedType.equalsIgnoreCase(SearchTypes.MEMBER_ID)) {
            return "Enter member id";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.FAMILY_ID)) {
            return "Enter family id";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.MOBILE_NUMBER)) {
            return "Enter mobile number";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.NAME)) {
            return "Enter member name";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.ABHA_NUMBER)) {
            return "Enter ABHA number";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.ABHA_ADDRESS)) {
            return "Enter ABHA address";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.DOB)) {
            return "Select date of birth";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.LMP)) {
            return "Select last menstruation period date";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.EDD)) {
            return "Select estimated delivery date";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.DELIVERY_DATE)) {
            return "Select delivery date";
        } else if (selectedType.equalsIgnoreCase(SearchTypes.LOCATION)) {
            return "Select location";
        } else {
            return "Enter your text here";
        }
    }

    private OnClickListener getSearchButtonOnClickListener() {
        return view -> {
            if (selectedType == null || SearchTypes.getSearchTypeText().contains(selectedType)) {
                if (editText.getText().toString().isEmpty()) {
                    SewaUtil.generateToast(context, "Please enter text to search");
                } else {
                    if (searchClick != null) {
                        if (editText.getText().toString().trim().length() > 2) {
                            String search = editText.getText().toString().trim().toUpperCase(Locale.ROOT);
                            if (SearchTypes.ABHA_NUMBER.equalsIgnoreCase(selectedType) && search.length() == 14) {
                                search = new StringBuilder(search).insert(2, "-").toString();
                                search = new StringBuilder(search).insert(7, "-").toString();
                                search = new StringBuilder(search).insert(12, "-").toString();
                            }
                            searchClick.onSearchClick(selectedType, search, null);
                            searchButton.setVisibility(GONE);
                            clearButton.setVisibility(VISIBLE);
                        } else {
                            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SEARCH_VALIDATION));
                        }
                    } else {
                        SewaUtil.generateToast(context, "Nothing to do here.");
                    }
                }
            } else if (SearchTypes.getSearchTypeDate().contains(selectedType)) {
                if (dateComponent.getSelectedDate() == null) {
                    SewaUtil.generateToast(context, "Please select a date to search");
                } else {
                    if (searchClick != null) {
                        searchClick.onSearchClick(selectedType, null, dateComponent.getSelectedDate());
                    } else {
                        SewaUtil.generateToast(context, "Nothing to do here.");
                    }
                }
            } else if (SearchTypes.getSearchTypeLocation().contains(selectedType)) {
                openLocationSelectionDialog();
            } else {
                SewaUtil.generateToast(context, "Please click the button to search");
            }
        };
    }

    private OnClickListener getClearButtonClickListener() {
        return view -> {
            if (selectedType == null || SearchTypes.getSearchTypeText().contains(selectedType)) {
                if (editText.getText().toString().isEmpty()) {
                    SewaUtil.generateToast(context, "Nothing to clear");
                } else {
                    if (clearClick != null) {
                        editText.setText("");
                        clearClick.onClearClick();
                        searchButton.setVisibility(VISIBLE);
                        clearButton.setVisibility(GONE);
                    } else {
                        SewaUtil.generateToast(context, "Nothing to do here.");
                    }
                }
            }
        };
    }


    public void hideSearchIcon() {
        searchButton.setVisibility(GONE);
    }

    public void addTextWatcherInEditText(TextWatcher textWatcher) {
        if (editText != null) {
            editText.addTextChangedListener(textWatcher);
            hideSearchIcon();
        }
    }

    public void changeFocusOfEditText(boolean hasFocus) {
        if (editText != null) {
            if (hasFocus)
                editText.setFocusable(true);
            else
                editText.clearFocus();
        }
    }

    public EditText getEditText() {
        return editText;
    }

    public interface SearchClick {
        void onSearchClick(String searchType, String searchText, Date date);
    }

    public interface ClearClick {
        void onClearClick();
    }

    public interface LocationSelection {
        void onLocationSelection(String searchType, String searchText, Long locationId);
    }

    public static class SearchTypes {
        private SearchTypes() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String MEMBER_ID = "MEMBER_ID";
        public static final String FAMILY_ID = "FAMILY_ID";
        public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
        public static final String NAME = "NAME";
        public static final String DOB = "DOB";
        public static final String LMP = "LMP";
        public static final String EDD = "EDD";
        public static final String DELIVERY_DATE = "DELIVERY_DATE";
        public static final String ABHA_NUMBER = "ABHA_NUMBER";
        public static final String ABHA_ADDRESS = "ABHA_ADDRESS";
        public static final String LOCATION = "LOCATION";

        protected static final List<String> SEARCH_TYPE_TEXT = new ArrayList<>();
        protected static final List<String> SEARCH_TYPE_DATE = new ArrayList<>();
        protected static final List<String> SEARCH_TYPE_LOCATION = new ArrayList<>();
        protected static final List<String> SEARCH_TYPE_BUTTON = new ArrayList<>();
        protected static final Map<String, String> FULL_NAMES = new HashMap<>();
        protected static final Map<String, Integer> ICONS = new HashMap<>();

        public static String getFullNameForSearchType(String type) {
            if (FULL_NAMES.isEmpty()) {
                FULL_NAMES.put(MEMBER_ID, "Member ID");
                FULL_NAMES.put(FAMILY_ID, "Family ID");
                FULL_NAMES.put(MOBILE_NUMBER, "Mobile Number");
                FULL_NAMES.put(NAME, "Name");
                FULL_NAMES.put(DOB, "Date of birth");
                FULL_NAMES.put(LMP, "Date of Last Menstrual Period");
                FULL_NAMES.put(EDD, "Estimated Delivery Date");
                FULL_NAMES.put(DELIVERY_DATE, "Delivery Date");
                FULL_NAMES.put(ABHA_NUMBER, "ABHA Number");
                FULL_NAMES.put(ABHA_ADDRESS, "ABHA Address");
                FULL_NAMES.put(LOCATION, "Location");
            }

            String s = FULL_NAMES.get(type);
            if (s == null) {
                return type;
            } else {
                return s;
            }
        }

//        public static Integer getIconsForSearchType(String type) {
//            if (ICONS.isEmpty()) {
//                ICONS.put(MEMBER_ID, R.drawable.ic_tab_member_id);
//                ICONS.put(FAMILY_ID, R.drawable.ic_tab_family_id);
//                ICONS.put(MOBILE_NUMBER, R.drawable.ic_tab_smartphone);
//                ICONS.put(NAME, R.drawable.ic_tab_member_name);
//                ICONS.put(DOB, R.drawable.ic_tab_dob);
//                ICONS.put(LMP, R.drawable.ic_tab_lmp_date);
//                ICONS.put(EDD, R.drawable.ic_tab_edd);
//                ICONS.put(DELIVERY_DATE, R.drawable.ic_tab_edd);
//                ICONS.put(ABHA_NUMBER, R.drawable.ic_tab_abha_card);
//                ICONS.put(ABHA_ADDRESS, R.drawable.ic_tab_abha_card);
//            }
//
//            Integer i = ICONS.get(type);
//            if (i == null) {
//                return R.drawable.ic_tab_member_id;
//            } else {
//                return i;
//            }
//        }

        public static List<String> getSearchTypeText() {
            if (SEARCH_TYPE_TEXT.isEmpty()) {
                SEARCH_TYPE_TEXT.add(MEMBER_ID);
                SEARCH_TYPE_TEXT.add(FAMILY_ID);
                SEARCH_TYPE_TEXT.add(MOBILE_NUMBER);
                SEARCH_TYPE_TEXT.add(ABHA_NUMBER);
                SEARCH_TYPE_TEXT.add(ABHA_ADDRESS);
                SEARCH_TYPE_TEXT.add(NAME);
            }
            return SEARCH_TYPE_TEXT;
        }

        public static List<String> getSearchTypeDate() {
            if (SEARCH_TYPE_DATE.isEmpty()) {
                SEARCH_TYPE_DATE.add(DOB);
                SEARCH_TYPE_DATE.add(LMP);
                SEARCH_TYPE_DATE.add(EDD);
                SEARCH_TYPE_DATE.add(DELIVERY_DATE);
            }
            return SEARCH_TYPE_DATE;
        }

        public static List<String> getSearchTypeLocation() {
            if (SEARCH_TYPE_LOCATION.isEmpty()) {
                SEARCH_TYPE_LOCATION.add(LOCATION);
            }
            return SEARCH_TYPE_LOCATION;
        }
    }

    class DateComponent extends AppCompatEditText {

        private final Context context;
        private final String hint;
        private final Calendar calendar = Calendar.getInstance();
        private Date selectedDate = null;

        public DateComponent(Context context, String hint) {
            super(context);
            this.context = context;
            this.hint = hint;
            initView();
        }

        private void initView() {
            //Set Edit Text Here
            if (hint != null) {
                setHint(hint);
            }
            setFocusable(false);
            setEnabled(true);
            setInputType(InputType.TYPE_CLASS_DATETIME);
            setBackground(ContextCompat.getDrawable(context, R.drawable.line_background_bottom));

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            setOnClickListener(getDateComponentClickListener());
        }

        private OnClickListener getDateComponentClickListener() {
            return view -> {
                final DatePickerDialog.OnDateSetListener dateListener = (view1, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectedDate = calendar.getTime();
                    updateDate();
                };

                DatePickerDialog dp = new DatePickerDialog(context, dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMaxDate(new Date().getTime());
                dp.show();
            };
        }

        private void updateDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            setText(sdf.format(calendar.getTime()));
        }

        public Date getSelectedDate() {
            return selectedDate;
        }

        public void clearData() {
            selectedDate = null;
            setText("");
            if (hint != null) {
                setHint(hint);
            }
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
    }

    class LocationComponent extends AppCompatEditText {

        private final Context context;
        private final String hint;

        public LocationComponent(Context context, String hint) {
            super(context);
            this.context = context;
            this.hint = hint;
            initView();
        }

        private void initView() {
            //Set Edit Text Here
            if (hint != null) {
                setHint(hint);
            }
            setFocusable(false);
            setEnabled(false);
            setInputType(InputType.TYPE_CLASS_DATETIME);
            setBackground(ContextCompat.getDrawable(context, R.drawable.line_background_bottom));
        }

        public void updateLocation(String selectedLocation) {
            if (selectedLocation != null)
                setText(selectedLocation);
            else
                getText().clear();
        }


    }

    private void openLocationSelectionDialog() {
//        selectedLocationId = null;
//        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
//        layoutParams.setMargins(50, 0, 50, 0);
//
//        parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
//                layoutParams);
//        parentLayout.setPadding(50, 30, 50, 30);
//        parentLayout.setBackgroundResource(R.drawable.alert_dialog);
//
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(parentLayout);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//
//        Integer level = locationMasterService.getLocationLevelByType(SewaConstants.LocationType.VILLAGE);
//        districtComponent = new LocationSelectionFromDistrictComponent(context, locationMasterService, level);
//        parentLayout.addView(districtComponent);
//
//        layoutParams.setMargins(0, 30, 0, 0);
//        buttonListLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
//                layoutParams);
//        MaterialButton ok = MyStaticComponents.getButton(context, UtilBean.getMyLabel(LabelConstants.OK), -1,
//                new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
//        buttonListLayout.addView(ok);
//        ok.setOnClickListener(view -> {
//            if (districtComponent != null && districtComponent.getSelectedLocation() != null) {
//                selectedLocationId = districtComponent.getSelectedLocation().getActualID();
//                locationComponent.updateLocation(locationMasterService.retrieveLocationHierarchyByLocationId(selectedLocationId));
//                if (locationSelection != null) {
//                    locationSelection.onLocationSelection(selectedType, null, selectedLocationId);
//                } else {
//                    SewaUtil.generateToast(context, "Nothing to do here.");
//                }
//            } else {
//                locationComponent.updateLocation(null);
//            }
//            dialog.dismiss();
//        });
//        parentLayout.addView(buttonListLayout);
//        dialog.show();
    }

}
