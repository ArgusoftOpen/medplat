package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.databean.WorkLogScreenDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alpeshkyada
 */
public class MyWorkLogAdapter extends BaseAdapter {

    private Context context;
    private List<WorkLogScreenDataBean> myLogs;
    private Map<Integer, View> adapterView;

    public MyWorkLogAdapter(Context context, List<WorkLogScreenDataBean> myLogs) {
        this.context = context;
        this.adapterView = new HashMap<>();
        this.myLogs = myLogs;
    }

    @Override
    public int getCount() {
        if (myLogs != null) {
            return myLogs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (myLogs != null) {
            return myLogs.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = adapterView.get(position);
        if (view == null && myLogs != null && !myLogs.isEmpty()) {
            final WorkLogScreenDataBean log = myLogs.get(position);

            LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.worklog_list_view, null);
            ImageView status = layout.findViewById(R.id.work_log_status_image);
            if (log.getImage() != -1) {
                status.setImageResource(log.getImage());
                status.setVisibility(View.VISIBLE);
            } else {
                status.setVisibility(View.GONE);
            }

            TextView date = layout.findViewById(R.id.work_log_date);
            if (log.getDate() != null) {
                date.setText(log.getDate());
                date.setVisibility(View.VISIBLE);
            } else {
                date.setVisibility(View.GONE);
            }

            TextView name = layout.findViewById(R.id.work_log_name);
            if (log.getName() != null) {
                name.setText(log.getName());
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }

            TextView task = layout.findViewById(R.id.work_log_task);
            if (log.getTask() != null) {
                task.setText(log.getTask());
                task.setVisibility(View.VISIBLE);
            } else {
                task.setVisibility(View.GONE);
            }

            final View.OnClickListener listener = v -> {
                if (SharedStructureData.workLogAlert != null) {
                    SharedStructureData.workLogAlert.dismiss();
                }
            };

            ImageView info = layout.findViewById(R.id.work_log_info);
            if (log.getMessage() != null) {
                info.setVisibility(View.VISIBLE);
                layout.setOnClickListener(v -> {
                    SharedStructureData.workLogAlert = new MyAlertDialog(context,
                            log.getMessage(), listener, DynamicUtils.BUTTON_OK);
                    SharedStructureData.workLogAlert.show();
                });
            } else {
                info.setVisibility(View.GONE);
            }

            adapterView.put(position, layout);
            return layout;
        }
        return view;
    }
}
