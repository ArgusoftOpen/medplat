package com.argusoft.sewa.android.app.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.List;

public class DistrictHealthInfraComponent extends LinearLayout {

    private final Context context;
    private final QueFormBean queFormBean;
    private HealthInfrastructureBean selectedInfra = null;

    public DistrictHealthInfraComponent(Context context) {
        super(context);
        this.context = context;
        this.queFormBean = null;
        initView();
    }

    public DistrictHealthInfraComponent(Context context, QueFormBean queFormBean) {
        super(context);
        this.context = context;
        this.queFormBean = queFormBean;
        initView();
    }

    private void initView() {
        LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context,
                IdConstants.HEALTH_INFRA_MAIN_LAYOUT_ID, VERTICAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        List<HealthInfrastructureBean> infraList =
                    SharedStructureData.healthInfrastructureService.retrieveHealthInfraListByLocationList(
                        SharedStructureData.locationMasterService.getLocationIdsInsideDistrictOfUser());

        String[] arrayOfOptions = new String[infraList.size() + 1];
        if (!infraList.isEmpty()) {
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            int count = 1;
            for (HealthInfrastructureBean bean : infraList) {
                arrayOfOptions[count] = bean.getName();
                count++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_HEALTH_INFRASTRUCTURE_AVAILABLE);
        }

        Spinner infraTypeSelectionSpinner =
                MyStaticComponents.getSpinner(
                        context, arrayOfOptions, 0,
                        IdConstants.HEALTH_INFRA_TYPE_SELECTION_SPINNER_ID);
        infraTypeSelectionSpinner.setOnItemSelectedListener(getInfraSelectionListener(infraList));

        mainLayout.addView(infraTypeSelectionSpinner);
        addView(mainLayout);
    }

    private AdapterView.OnItemSelectedListener getInfraSelectionListener(List<HealthInfrastructureBean> infraList) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setSelectedInfra(null);
                    return;
                }
                setSelectedInfra(infraList.get(position - 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Not to do anything
            }
        };
    }

    private void setSelectedInfra(HealthInfrastructureBean infra) {
        this.selectedInfra = infra;
        SharedStructureData.selectedHealthInfra = this.selectedInfra;
        if (queFormBean != null) {
            if (this.selectedInfra != null) {
                queFormBean.setAnswer(this.selectedInfra.getActualId());
            } else {
                queFormBean.setAnswer(null);
            }
        }
    }

    public HealthInfrastructureBean getSelectedInfra() {
        return this.selectedInfra;
    }
}
