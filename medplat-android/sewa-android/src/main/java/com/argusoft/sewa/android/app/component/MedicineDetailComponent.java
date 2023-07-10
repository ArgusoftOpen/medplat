package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;


public class MedicineDetailComponent extends LinearLayout {

    private final Context context;
    private final QueFormBean queFormBean;

    private Integer medicineCount = null;
    private boolean isAdditional = false;
    private MedicineListAdapter adapter;

    // dataMap (medicineCount-isNew / drugs-isNew)
    public MedicineDetailComponent(Context context, QueFormBean queFormBean) {
        super(context);
        this.context = context;
        this.queFormBean = queFormBean;
        setDataForMedicine();
        setMainLayout();
    }

    private void setDataForMedicine() {

        String datamap = queFormBean.getDatamap();

        if (datamap == null || datamap.isEmpty()) {
            return;
        }

        String[] split = datamap.split("-");

        if (split.length == 2) {
            String totalMedicine = SharedStructureData.relatedPropertyHashTable.get(split[0]);
            if (totalMedicine != null) {
                medicineCount = Integer.valueOf(totalMedicine);
            }
            isAdditional = split[1].equals("1");
        }
    }

    private void setMainLayout() {
        String noMedicineLabel = "No medicine found";
        LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        if (medicineCount != null) {
            mainLayout.addView(getBodyLayout());
        } else {
            mainLayout.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(noMedicineLabel)));
        }
        addView(mainLayout);
    }

    private LinearLayout getBodyLayout() {
        LinearLayout bodyLayout = MyStaticComponents.getLinearLayout(context, -1, VERTICAL, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        getMedicineDetailLayout(bodyLayout);
        return bodyLayout;
    }

    private void getMedicineDetailLayout(LinearLayout bodyLayout) {

        if (isAdditional) {
            removeAllViews();
            if (SharedStructureData.additionalMedicineList != null && !SharedStructureData.additionalMedicineList.isEmpty()) {
                adapter = new MedicineListAdapter(context, SharedStructureData.additionalMedicineList, true, queFormBean, SharedStructureData.ncdService, null);
            }
        } else {
            removeAllViews();
            MaterialTextView textView = new MaterialTextView(context);
            textView.setText(UtilBean.getMyLabel("** Light pink background indicates expired dosage."));
            textView.setTextSize(12);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            bodyLayout.addView(textView);
            if (SharedStructureData.prescribedMedicineList != null && !SharedStructureData.prescribedMedicineList.isEmpty()) {
                adapter = new MedicineListAdapter(context, SharedStructureData.prescribedMedicineList, false, queFormBean, SharedStructureData.ncdService, SharedStructureData.prescribedMedicineMap);
            }
        }

        RecyclerView medicineListView = new RecyclerView(context);
        medicineListView.setLayoutManager(new LinearLayoutManager(context));
        medicineListView.setFocusable(false);
        medicineListView.setNestedScrollingEnabled(true);
        medicineListView.setAdapter(adapter);
        RecyclerView.LayoutParams params = new
                RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
        medicineListView.setLayoutParams(params);
        bodyLayout.addView(medicineListView);
    }

}
