package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.argusoft.sewa.android.app.activity.AbhaNumberActivity_;
import com.argusoft.sewa.android.app.activity.AnnouncementActivity_;
import com.argusoft.sewa.android.app.activity.AssignFamilyActivity_;
import com.argusoft.sewa.android.app.activity.DailyNutritionActivity_;
import com.argusoft.sewa.android.app.activity.DataQualityActivity_;
import com.argusoft.sewa.android.app.activity.FHSActivity_;
import com.argusoft.sewa.android.app.activity.FHSAshaActivity_;
import com.argusoft.sewa.android.app.activity.FHSAwwActivity_;
import com.argusoft.sewa.android.app.activity.HeadToToeScreeningActivity_;
import com.argusoft.sewa.android.app.activity.HighRiskAshaActivity_;
import com.argusoft.sewa.android.app.activity.HighRiskPregnancyActivity_;
import com.argusoft.sewa.android.app.activity.IDSP2Activity_;
import com.argusoft.sewa.android.app.activity.LibraryActivity_;
import com.argusoft.sewa.android.app.activity.LmsProgressReportActivity_;
import com.argusoft.sewa.android.app.activity.MobileNumberValidationActivity_;
import com.argusoft.sewa.android.app.activity.MyPeopleActivity_;
import com.argusoft.sewa.android.app.activity.MyPeopleAshaActivity_;
import com.argusoft.sewa.android.app.activity.MyPeopleAwwActivity_;
import com.argusoft.sewa.android.app.activity.NCDRegisterFHWActivity_;
import com.argusoft.sewa.android.app.activity.NCDScreeningAshaActivity_;
import com.argusoft.sewa.android.app.activity.NPCBScreening_;
import com.argusoft.sewa.android.app.activity.NcdConfirmationFhwActivity_;
import com.argusoft.sewa.android.app.activity.NcdRegisterAshaActivity_;
import com.argusoft.sewa.android.app.activity.NcdScreeningFhwActivity_;
import com.argusoft.sewa.android.app.activity.NcdWeeklyVisitFhwActivity_;
import com.argusoft.sewa.android.app.activity.NotificationActivity_;
import com.argusoft.sewa.android.app.activity.NotificationAshaActivity_;
import com.argusoft.sewa.android.app.activity.NotificationAwwActivity_;
import com.argusoft.sewa.android.app.activity.OPDFacilityActivity_;
import com.argusoft.sewa.android.app.activity.PhoneNumberVerificationActivity_;
import com.argusoft.sewa.android.app.activity.TakeHomeRation_;
import com.argusoft.sewa.android.app.activity.WorkLogActivity_;
import com.argusoft.sewa.android.app.activity.WorkRegisterActivity_;
import com.argusoft.sewa.android.app.activity.WorkRegisterAshaActivity_;
import com.argusoft.sewa.android.app.activity.WorkStatusActivity_;
import com.argusoft.sewa.android.app.constants.MenuConstants;
import com.argusoft.sewa.android.app.lms.LmsCourseListActivity_;

public class MenuClickListener implements View.OnClickListener {

    private Context context;
    private String selectedMenu;

    public MenuClickListener(Context context, String selectedMenu) {
        this.context = context;
        this.selectedMenu = selectedMenu;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (selectedMenu) {
            case MenuConstants.FHW_CFHC:
                intent = new Intent(context, FHSActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_DATA_QUALITY:
                intent = new Intent(context, DataQualityActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_SURVEILLANCE:
                intent = new Intent(context, IDSP2Activity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_ASSIGN_FAMILY:
                intent = new Intent(context, AssignFamilyActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_MY_PEOPLE:
                intent = new Intent(context, MyPeopleActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_MOBILE_VERIFICATION:
                intent = new Intent(context, MobileNumberValidationActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_NOTIFICATION:
                intent = new Intent(context, NotificationActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_HIGH_RISK_WOMEN_AND_CHILD:
                intent = new Intent(context, HighRiskPregnancyActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_NCD_SCREENING:
                intent = new Intent(context, NcdScreeningFhwActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_NCD_REGISTER:
                intent = new Intent(context, NCDRegisterFHWActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_WORK_REGISTER:
                intent = new Intent(context, WorkRegisterActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_WORK_STATUS:
                intent = new Intent(context, WorkStatusActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.LIBRARY:
                intent = new Intent(context, LibraryActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ANNOUNCEMENTS:
                intent = new Intent(context, AnnouncementActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.WORK_LOG:
                intent = new Intent(context, WorkLogActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_FHS:
                intent = new Intent(context, FHSAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_MY_PEOPLE:
                intent = new Intent(context, MyPeopleAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_NOTIFICATION:
                intent = new Intent(context, NotificationAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_HIGH_RISK_BENEFICIARIES:
                intent = new Intent(context, HighRiskAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_CBAC_ENTRY:
                intent = new Intent(context, NCDScreeningAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_NCD_REGISTER:
                intent = new Intent(context, NcdRegisterAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_NPCB_SCREENING:
                intent = new Intent(context, NPCBScreening_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ASHA_WORK_REGISTER:
                intent = new Intent(context, WorkRegisterAshaActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.HEAD_TO_TOE_SCREENING:
                intent = new Intent(context, HeadToToeScreeningActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.OPD_FACILITY:
                intent = new Intent(context, OPDFacilityActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.AWW_FHS:
                intent = new Intent(context, FHSAwwActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.AWW_MY_PEOPLE:
                intent = new Intent(context, MyPeopleAwwActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.AWW_NOTIFICATION:
                intent = new Intent(context, NotificationAwwActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.DAILY_NUTRITION:
                intent = new Intent(context, DailyNutritionActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.TAKE_HOME_RATION:
                intent = new Intent(context, TakeHomeRation_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHSR_PHONE_NUMBER_VERIFICATION:
                intent = new Intent(context, PhoneNumberVerificationActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.LEARNING_MANAGEMENT_SYSTEM:
                intent = new Intent(context, LmsCourseListActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.LMS_PROGRESS_REPORT:
                intent = new Intent(context, LmsProgressReportActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.ABHA_NUMBER:
                intent = new Intent(context, AbhaNumberActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_NCD_CONFIRMATION:
                intent = new Intent(context, NcdConfirmationFhwActivity_.class);
                context.startActivity(intent);
                break;
            case MenuConstants.FHW_NCD_WEEKLY_VISIT:
                intent = new Intent(context, NcdWeeklyVisitFhwActivity_.class);
                context.startActivity(intent);
                break;
            default:
        }
    }
}
