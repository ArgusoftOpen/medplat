package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyRequestStates;
import com.argusoft.sewa.android.app.databean.ChardhamEmergencyResponseStates;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prateek on 11/29/19
 */
public class PagingAdapter<T> extends ArrayAdapter<T> {

    private List<T> items;
    private int listItemResourceId;
    private MaterialTextView dateTextView;
    private MaterialTextView visitTextView;
    private MaterialTextView vaccines;
    private MaterialTextView vaccinesTitle;
    private MaterialTextView boldTextView;
    private MaterialTextView regularTextView;
    private MaterialTextView regularTextView2;
    private LinearLayout linearLayout;
    private ListItemDataBean item;
    private AdapterView.OnItemClickListener onItemClickListener;

    public PagingAdapter(Context context, int textViewResourceId, List<T> items, AdapterView.OnItemClickListener onItemClickListener) {
        super(context, textViewResourceId, items);
        this.items = new ArrayList<>();
        this.listItemResourceId = textViewResourceId;
        this.onItemClickListener = onItemClickListener;
    }

    public void addMoreItems(List<T> newItems) {
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return onItemClickListener != null;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView != null) {
            linearLayout = (LinearLayout) convertView;
        } else {
            linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(listItemResourceId, null);
        }

        switch (listItemResourceId) {
            case R.layout.listview_row_type:
                item = (ListItemDataBean) items.get(position);
                MaterialTextView text = linearLayout.findViewById(R.id.lists_text);
                text.setText(UtilBean.getMyLabel(item.getText()));
                break;

            case R.layout.listview_row_with_item:
                item = (ListItemDataBean) items.get(position);
                setBoldAndRegularText(R.id.list_text_bold, R.id.list_text_regular, R.id.list_text_separator);
                break;

            case R.layout.listview_row_for_tourist:
                item = (ListItemDataBean) items.get(position);
                setTouristRow();
                break;

            case R.layout.listview_row_emergency_request:
                item = (ListItemDataBean) items.get(position);
                setEmergencyRequestView();
                break;

            case R.layout.listview_row_notification:
                item = (ListItemDataBean) items.get(position);
                setNotificationRow();
                break;

            case R.layout.listview_row_with_date:
                item = (ListItemDataBean) items.get(position);
                setBoldAndRegularText(R.id.notification_text_bold, R.id.notification_text_regular, R.id.notification_text_separator);
                setDateAndVisit(R.id.notification_text_date, R.id.notification_text_visit);
                setVaccines(R.id.notification_text_vaccines_title, R.id.notification_text_vaccines);

                if (item.getHighlightView() != null && item.getHighlightView()) {
                    setTaskOverDueRow(R.drawable.listview_selector, R.color.colorAccent, R.color.listview_availability_text_color_selector);
                } else if (item.isDisplayEarly()) {
                    setTaskOverDueRow(R.drawable.listview_selector, R.color.colorAccent, R.color.listview_display_early_text_color_selector);
                } else if (item.isTaskOverDue()) {
                    setTaskOverDueRow(R.drawable.notification_due_selector, R.color.notificationBadgeBackground, R.color.notificationBadgeBackground);
                } else {
                    setTaskOverDueRow(R.drawable.listview_selector, R.color.colorAccent, R.color.listview_text_color_selector);
                }
                break;

            case R.layout.listview_row_with_info:
                item = (ListItemDataBean) items.get(position);
                setBoldAndRegularText(R.id.list_info_text_bold, R.id.list_info_text_regular, R.id.list_info_text_separator);
                setInfoListView();
                break;

            case R.layout.listview_row_with_two_item:
                item = (ListItemDataBean) items.get(position);
                setListViewRows();
                break;

            case R.layout.listview_row_with_three_item:
                item = (ListItemDataBean) items.get(position);
                setListView3Rows();
                break;

            case R.layout.listview_row_abha_number:
                item = (ListItemDataBean) items.get(position);
                setAbhaListViewRows();
                break;
            case R.layout.listview_row_announcement_chardham:
                item = (ListItemDataBean) items.get(position);
                setAnnouncementViewRows(position);
                break;

            default:
                String rowText = (String) items.get(position);
                MaterialTextView textView = linearLayout.findViewById(R.id.lists_text);
                textView.setText(UtilBean.getMyLabel(rowText));
                break;
        }

        return linearLayout;
    }

    private void setAnnouncementViewRows(int position) {
        MaterialTextView announcementTitle = linearLayout.findViewById(R.id.tvAnnouncementTitle);
        MaterialTextView announcementDate = linearLayout.findViewById(R.id.tvAnnouncementDate);
        AppCompatImageView announcementMedia = linearLayout.findViewById(R.id.ivPlayIcon);
        LinearLayout rootItemLayout = linearLayout.findViewById(R.id.rootItemLayout);

        announcementTitle.setText(item.getAnnouncementTitle());
        announcementDate.setText(item.getPublishDate());
        String extension;

        rootItemLayout.setOnClickListener(v -> {
            onItemClickListener.onItemClick(null, rootItemLayout, position, rootItemLayout.getId());
        });

        if (item.isHasSeen()) {
            rootItemLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_less_opacity));
        } else {
            rootItemLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.extra_light_gray));
        }

        if (item.getFileName() != null) {
            announcementMedia.setVisibility(View.VISIBLE);
            extension = item.getFileName().substring(item.getFileName().lastIndexOf(".") + 1);
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("pdf")) {
                announcementMedia.setImageResource(R.drawable.ic_eye);
            } else {
                announcementMedia.setImageResource(R.drawable.ic_play);
            }
        } else {
            announcementMedia.setVisibility(View.INVISIBLE);
        }
    }

    private void setEmergencyRequestView() {
        MaterialTextView tvHealthInfraName = linearLayout.findViewById(R.id.tvHealthInfraName);
        MaterialTextView tvRequestStatus = linearLayout.findViewById(R.id.tvRequestStatus);
        MaterialTextView tvRequestReason = linearLayout.findViewById(R.id.tvRequestReason);
        MaterialTextView reqTitle = linearLayout.findViewById(R.id.reqTitle);
        MaterialTextView tvCreatedDate = linearLayout.findViewById(R.id.tvCreatedDate);
        MaterialTextView tvCompletedDate = linearLayout.findViewById(R.id.tvCompletedDate);
        MaterialTextView tvRejectedReason = linearLayout.findViewById(R.id.tvRejectedReason);
        MaterialTextView reqContactTitle = linearLayout.findViewById(R.id.reqContactTitle);
        MaterialTextView reqContactValues = linearLayout.findViewById(R.id.reqContactValues);

        LinearLayout llRequests = linearLayout.findViewById(R.id.llRequests);
        tvHealthInfraName.setText(item.getHealthInfraName());
        tvRequestStatus.setText(item.getRequestStatus());
        tvRequestReason.setText(item.getRequestReason());
        tvCreatedDate.setText(item.getCreatedDate());
        tvCompletedDate.setText(item.getCompletedDate());
        tvRejectedReason.setText(item.getRejectReason());

        if (item.getContactNumber() != null && item.isRequestView()) {
            reqContactTitle.setVisibility(View.VISIBLE);
            reqContactValues.setVisibility(View.VISIBLE);
            reqContactValues.setText(item.getContactNumber());
        } else {
            reqContactTitle.setVisibility(View.GONE);
            reqContactValues.setVisibility(View.GONE);
        }

        if (item.getFromKiosk()) {
            if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.ONGOING.name())) {
                llRequests.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low_opacity_light_gray));
            } else {
                llRequests.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low_opacity_white));
            }
            reqTitle.setText(R.string.request_to);
        } else {
            if (item.getRequestStatus().equals(ChardhamEmergencyResponseStates.PENDING.name())) {
                llRequests.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low_opacity_light_gray));
            } else {
                llRequests.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low_opacity_white));
            }
            reqTitle.setText(R.string.request_from);
        }

        if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.COMPLETED.name())) {
            tvCompletedDate.setVisibility(View.VISIBLE);
        } else {
            tvCompletedDate.setVisibility(View.GONE);
        }


        if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.REJECTED.name())) {
            tvRejectedReason.setVisibility(View.VISIBLE);
        } else {
            tvRejectedReason.setVisibility(View.GONE);
        }


        if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.COMPLETED.name())) {
            tvRequestStatus.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_request_accepted));
        } else if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.PENDING.name())) {
            tvRequestStatus.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_pending));
        } else if (item.getRequestStatus().equals(ChardhamEmergencyRequestStates.ONGOING.name())) {
            tvRequestStatus.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_ongoing_request));
        } else {
            tvRequestStatus.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_mrp));
        }
    }

    private void setTouristRow() {
        MaterialTextView memberName = linearLayout.findViewById(R.id.tvTouristName);
        MaterialTextView memberAge = linearLayout.findViewById(R.id.tvTouristAge);
        MaterialTextView memberGender = linearLayout.findViewById(R.id.tvTouristGender);
        MaterialTextView memberUniqueId = linearLayout.findViewById(R.id.tvTouristUniqueId);
        MaterialTextView tvTrtOrScreening = linearLayout.findViewById(R.id.tvTrtOrScreening);
        View screeningView = linearLayout.findViewById(R.id.screeningStatus);
        View view = linearLayout.findViewById(R.id.view);
        MaterialTextView tvScreeningDoneAt = linearLayout.findViewById(R.id.tvScreeningDoneAt);

        memberName.setText(item.getMemberName());
        memberAge.setText(item.getMemberAge());
        memberGender.setText(item.getMemberGender());
        memberUniqueId.setText(item.getMemberId());
        tvScreeningDoneAt.setText(item.getHealthInfraName());

        if (item.getIsScreeningDone() == 1) {
            view.setVisibility(View.VISIBLE);
            tvScreeningDoneAt.setVisibility(View.GONE);
            tvTrtOrScreening.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.INVISIBLE);
            tvScreeningDoneAt.setVisibility(View.VISIBLE);
            tvTrtOrScreening.setVisibility(View.VISIBLE);
        }

        if (item != null && item.getScreeningStatus() != null) {
            if (item.getScreeningStatus().equalsIgnoreCase("TREATMENT")) {
                tvTrtOrScreening.setText(R.string.lbl_treatment);
                tvTrtOrScreening.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_ongoing_request));
            } else if (item.getScreeningStatus().equalsIgnoreCase("REGISTERED")) {
                tvTrtOrScreening.setText(R.string.lbl_registered);
                tvTrtOrScreening.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_mrp));
            } else {
                tvTrtOrScreening.setText(R.string.lbl_screening);
                tvTrtOrScreening.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_pending));
            }
        }

        if (item != null && item.getScreeningStatus() != null) {
            if (item.getScreeningStatus().equalsIgnoreCase("GREEN")) {
                screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.normal));
            } else if (item.getScreeningStatus().equalsIgnoreCase("YELLOW")) {
                screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightYellow));
            } else if (item.getScreeningStatus().equalsIgnoreCase("RED")) {
                screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.high_risk));
            } else if (item.getScreeningStatus().equalsIgnoreCase("TREATMENT")) {
                screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else {
                screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.detailsTextColor));
            }
        } else {
            screeningView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.detailsTextColor));
        }
    }

    private void setTaskOverDueRow(int layoutBackgroundResource, int textBackgroundResource, int textColor) {
        linearLayout.setBackgroundResource(layoutBackgroundResource);
        visitTextView.getBackground().setColorFilter(ContextCompat.getColor(getContext(), textBackgroundResource), PorterDuff.Mode.SRC_ATOP);
        boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        dateTextView.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        vaccinesTitle.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        vaccines.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        MaterialTextView separator = linearLayout.findViewById(R.id.notification_text_separator);
        separator.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
    }

    private void setBoldAndRegularText(int boldTextId, int regularTextId, int separatorId) {

        boldTextView = linearLayout.findViewById(boldTextId);
        boldTextView.setText(item.getBoldText());
        regularTextView = linearLayout.findViewById(regularTextId);
        regularTextView.setText(item.getRegularText());

        if (item.getHighlightView() != null && item.getHighlightView()) {
            boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_availability_text_color_selector));
            regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_availability_text_color_selector));
        } else if (item.isVerified()) {
            boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
        } else if (item.getState() != null) {
            if (item.getState().equalsIgnoreCase(FhsConstants.COMPLETED_STATE)) {
                boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
                regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            } else if (item.getState().equalsIgnoreCase(FhsConstants.INCOMPLETE_STATE)) {
                boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_screening_text_color_selector));
                regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_screening_text_color_selector));
            } else if (item.getState().equalsIgnoreCase(FhsConstants.STARTED_STATE)) {
                boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_no_screening_text_color_selector));
                regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_no_screening_text_color_selector));
            } else if (item.getState().equalsIgnoreCase(FhsConstants.NOT_STARTED_STATE)) {
                boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_display_early_text_color_selector));
                regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_display_early_text_color_selector));
            }
        } else {
            boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
            regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
        }

        if (item.getBoldText() == null || item.getRegularText() == null) {
            linearLayout.findViewById(separatorId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(separatorId).setVisibility(View.VISIBLE);
        }

        if (item.getBoldText() == null) {
            linearLayout.findViewById(boldTextId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(boldTextId).setVisibility(View.VISIBLE);
        }

        if (item.getRegularText() == null) {
            linearLayout.findViewById(regularTextId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(regularTextId).setVisibility(View.VISIBLE);
        }
    }

    private void setDateAndVisit(int dateTextId, int visitTextId) {
        dateTextView = linearLayout.findViewById(dateTextId);
        dateTextView.setText(item.getDate());
        visitTextView = linearLayout.findViewById(visitTextId);
        visitTextView.setText(item.getVisit());

        if (item.getDate() == null) {
            linearLayout.findViewById(R.id.notification_text_date).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.notification_text_date).setVisibility(View.VISIBLE);
        }

        if (item.getVisit() == null) {
            linearLayout.findViewById(R.id.notification_text_visit).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.notification_text_visit).setVisibility(View.VISIBLE);
        }
    }

    private void setVaccines(int titleId, int vaccinesTextId) {
        vaccines = linearLayout.findViewById(vaccinesTextId);
        vaccines.setText(item.getVaccines());
        vaccinesTitle = linearLayout.findViewById(titleId);
        vaccinesTitle.setText(item.getVaccinesTitle());

        if (item.getVaccines() != null) {
            linearLayout.findViewById(R.id.notification_text_vaccines_title).setVisibility(View.VISIBLE);
            linearLayout.findViewById(R.id.notification_text_vaccines).setVisibility(View.VISIBLE);
        } else {
            linearLayout.findViewById(R.id.notification_text_vaccines_title).setVisibility(View.GONE);
            linearLayout.findViewById(R.id.notification_text_vaccines).setVisibility(View.GONE);
        }
    }

    private void setInfoListView() {
        MaterialTextView infoTextView = linearLayout.findViewById(R.id.list_info_text);
        if (item.getInfo() != null) {
            infoTextView.setText(item.getInfo());
            linearLayout.findViewById(R.id.list_info_text_separator).setVisibility(View.VISIBLE);
            linearLayout.findViewById(R.id.list_info_text).setVisibility(View.VISIBLE);
        } else {
            linearLayout.findViewById(R.id.list_info_text).setVisibility(View.GONE);
        }
    }

    private void setListViewRows() {
        MaterialTextView idTextView = linearLayout.findViewById(R.id.list_text_title);
        idTextView.setText(item.getFamilyId());

        if (item.getFamilyId() == null) {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.VISIBLE);
        }

        MaterialTextView memberIdTextView = linearLayout.findViewById(R.id.list_first_text_bold);
        memberIdTextView.setText(item.getMemberId());
        MaterialTextView memberName = linearLayout.findViewById(R.id.list_first_text_regular);
        memberName.setText(item.getMemberName());
        if (item.getMemberId() == null || item.getMemberName() == null) {
            linearLayout.findViewById(R.id.list_first_text_separator).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.list_first_text_separator).setVisibility(View.VISIBLE);
        }

        if (item.getHighlightView() != null && item.getHighlightView()) {
            memberName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_availability_text_color_selector));
            memberIdTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_availability_text_color_selector));
        } else if (item.isVerified()) {
            memberName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            memberIdTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
        } else {
            memberName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
            memberIdTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
        }
        setBoldAndRegularText(R.id.list_second_text_bold, R.id.list_second_text_regular, R.id.list_second_text_separator);
    }

    private void setAbhaListViewRows() {
        MaterialTextView idTextView = linearLayout.findViewById(R.id.list_text_title);
        idTextView.setText(item.getMemberId());

        if (item.getMemberId() == null) {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.VISIBLE);
        }

        MaterialTextView memberIdTextView = linearLayout.findViewById(R.id.list_first_text_bold);
        memberIdTextView.setText(item.getRegularText());
        AppCompatImageView abhaNumberImageView = linearLayout.findViewById(R.id.abhaNumberImageView);
        if (item.isHealthId()) {
            abhaNumberImageView.setVisibility(View.VISIBLE);
        } else {
            abhaNumberImageView.setVisibility(View.GONE);
        }
    }

    private void setNotificationRow() {
        MaterialTextView notificationType = linearLayout.findViewById(R.id.notification_text);
        notificationType.setText(item.getNotificationText());
        if (item.getNotificationCount() != null) {
            MaterialTextView notificationCount = linearLayout.findViewById(R.id.notification_count);
            notificationCount.setText(UtilBean.getMyLabel(item.getNotificationCount()));
            linearLayout.findViewById(R.id.notification_badge).setVisibility(View.VISIBLE);
        } else {
            linearLayout.findViewById(R.id.notification_badge).setVisibility(View.INVISIBLE);
        }
    }

    private void setListView3Rows() {
        MaterialTextView idTextView = linearLayout.findViewById(R.id.list_text_title);
        idTextView.setText(item.getFamilyId());

        if (item.getFamilyId() == null) {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.list_text_title).setVisibility(View.VISIBLE);
        }

        MaterialTextView memberIdTextView = linearLayout.findViewById(R.id.list_first_text_bold);
        memberIdTextView.setText(item.getMemberId());


        MaterialTextView memberName = linearLayout.findViewById(R.id.list_first_text_regular);
        memberName.setText(item.getMemberName());
        if (item.getMemberId() == null || item.getMemberName() == null) {
            linearLayout.findViewById(R.id.list_first_text_separator).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(R.id.list_first_text_separator).setVisibility(View.VISIBLE);
        }

        if (item.isVerified()) {
            memberName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            memberIdTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
        } else {
            memberName.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
            memberIdTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
        }
        setBoldAnd2RegularText(R.id.list_second_text_bold, R.id.list_second_text_regular, R.id.list_third_text_regular,R.id.list_second_text_separator);
    }

    private void setBoldAnd2RegularText(int boldTextId, int regularTextId,int regularTextId2, int separatorId) {

        boldTextView = linearLayout.findViewById(boldTextId);
        boldTextView.setText(item.getBoldText());
        regularTextView = linearLayout.findViewById(regularTextId);
        regularTextView.setText(item.getRegularText());
        regularTextView2 = linearLayout.findViewById(regularTextId2);
        regularTextView2.setText(UtilBean.getMyLabel(LabelConstants.PENDING_MEMBERS) + " : " + Integer.toString(item.getPendingAbhaCount()));

        if (item.isVerified()) {
            boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
            regularTextView2.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_verified_text_color_selector));
        } else {
            boldTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
            regularTextView.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));
            regularTextView2.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.listview_text_color_selector));

        }

        if (item.getBoldText() == null || item.getRegularText() == null) {
            linearLayout.findViewById(separatorId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(separatorId).setVisibility(View.VISIBLE);
        }

        if (item.getBoldText() == null) {
            linearLayout.findViewById(boldTextId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(boldTextId).setVisibility(View.VISIBLE);
        }

        if (item.getRegularText() == null) {
            linearLayout.findViewById(regularTextId).setVisibility(View.GONE);
        } else {
            linearLayout.findViewById(regularTextId).setVisibility(View.VISIBLE);
        }
    }


}
