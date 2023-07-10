package com.argusoft.sewa.android.app.lms;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.databean.LmsViewedMediaDataBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class LmsCourseListAdaptor extends RecyclerView.Adapter<LmsCourseListAdaptor.ViewHolder> {

    private final Context context;
    private List<LmsCourseScreenDataBean> list;
    private final Map<Integer, View> adapterView;
    private final boolean showProgress, archiveOption;
    private LmsServiceImpl lmsService;

    public LmsCourseListAdaptor(Context context, List<LmsCourseScreenDataBean> list,
                                boolean showProgress, boolean archiveOption, LmsServiceImpl lmsService) {
        this.context = context;
        this.list = list;
        this.showProgress = showProgress;
        this.archiveOption = archiveOption;
        this.adapterView = new HashMap<>();
        this.lmsService = lmsService;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public List<LmsCourseScreenDataBean> getData() {
        return list;
    }

    public void updateList(List<LmsCourseScreenDataBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_lms_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LmsCourseScreenDataBean course = list.get(position);
        holder.icon_menu.setVisibility(archiveOption ? View.VISIBLE : View.GONE);
        if(course.getCompletionStatus() < 100) {
            holder.notification_badge.setVisibility(isCourseContentUpdated(course.getCourseId()) ? View.VISIBLE : View.GONE);
        }else {
            holder.notification_badge.setVisibility(View.GONE);
        }
        if (course.getImage() != null) {
            holder.courseImage.setImageBitmap(course.getImage());
        }

        if (course.getCourseTitle() != null) {
            holder.title.setText(course.getCourseTitle());
        }

        if (course.getCourseTitle() != null) {
            String text = String.format("Lessons: %s, Quizzes: %s", course.getTopicSize(), course.getQuestionSetSize());
            holder.numberOfLessonsAndQuizzes.setText(text);
        }


        if (course.getCourseDesc() != null) {
            holder.desc.setText(course.getCourseDesc());
        }


        if (showProgress) {
            holder.progressLayout.setVisibility(View.VISIBLE);
            holder.progressText.setText(course.getCompletionStatus() + "%");
            holder.progressBar.setProgress(course.getCompletionStatus());
            if (course.getCompletionStatus() < 60) {
                holder.progressBar.getProgressDrawable().setColorFilter(
                        Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            } else if (course.getCompletionStatus() >= 60 && course.getCompletionStatus() < 80) {
                holder.progressBar.getProgressDrawable().setColorFilter(
                        Color.rgb(255, 165, 0), android.graphics.PorterDuff.Mode.SRC_IN);
            } else if (course.getCompletionStatus() >= 80) {
                holder.progressBar.getProgressDrawable().setColorFilter(
                        Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.progressLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public abstract void onAdapterItemClick(int position);
    public abstract void onMenuClick(int position, View view);

    private boolean isCourseContentUpdated(Integer courseId){
        try {
            LmsCourseDataBean lmsCourseDataBean = lmsService.retrieveCourseByCourseId(courseId);
            LmsViewedMediaDataBean lmsViewedMediaDataBean = lmsService.getViewedCourseById(courseId);
            if(lmsViewedMediaDataBean != null && lmsCourseDataBean != null) {
                if ((lmsCourseDataBean.getQuestionSet() != null && lmsViewedMediaDataBean.getQuestionSet() == null) ||
                        (lmsCourseDataBean.getTopics() != null && lmsViewedMediaDataBean.getTopics() == null)) {
                    return true;
                } else if(lmsCourseDataBean.getQuestionSet() != null && lmsViewedMediaDataBean.getQuestionSet() != null &&
                        (!lmsViewedMediaDataBean.getQuestionSet().containsAll(lmsCourseDataBean.getQuestionSet()) ||
                                !lmsCourseDataBean.getQuestionSet().containsAll(lmsViewedMediaDataBean.getQuestionSet()))){
                    return true;
                } else if (lmsCourseDataBean.getTopics() != null && lmsViewedMediaDataBean.getTopics() != null){
                    if(!lmsViewedMediaDataBean.getTopics().containsAll(lmsCourseDataBean.getTopics()) ||
                            !lmsCourseDataBean.getTopics().containsAll(lmsViewedMediaDataBean.getTopics())) {
                        return true;
                    }
                    for (LmsTopicDataBean lmsTopicDataBean : lmsCourseDataBean.getTopics()) {
                        for (LmsTopicDataBean lmsTopicDataBean1 : lmsViewedMediaDataBean.getTopics()) {
                            if (Objects.equals(lmsTopicDataBean.getTopicId(), lmsTopicDataBean1.getTopicId())) {
                                if ((lmsTopicDataBean1.getQuestionSet() == null && lmsTopicDataBean.getQuestionSet() != null) ||
                                        (lmsTopicDataBean1.getTopicMedias() == null && lmsTopicDataBean.getTopicMedias() != null)) {
                                    return true;
                                } else if(lmsTopicDataBean1.getQuestionSet() != null && lmsTopicDataBean.getQuestionSet() != null &&
                                        (!lmsTopicDataBean1.getQuestionSet().containsAll(lmsTopicDataBean.getQuestionSet()) ||
                                                !lmsTopicDataBean.getQuestionSet().containsAll(lmsTopicDataBean1.getQuestionSet()))){
                                    return true;
                                }
                                if (lmsTopicDataBean1.getTopicMedias() != null && lmsTopicDataBean.getTopicMedias() != null) {
                                    if(!lmsTopicDataBean1.getTopicMedias().containsAll(lmsTopicDataBean.getTopicMedias()) ||
                                            !lmsTopicDataBean.getTopicMedias().containsAll(lmsTopicDataBean1.getTopicMedias())){
                                        return true;
                                    }
                                    for (LmsLessonDataBean lmsLessonDataBean : lmsTopicDataBean.getTopicMedias()) {
                                        for (LmsLessonDataBean lmsLessonDataBean1 : lmsTopicDataBean1.getTopicMedias()) {
                                            if (Objects.equals(lmsLessonDataBean.getActualId(), lmsLessonDataBean1.getActualId())){
                                                if(lmsLessonDataBean1.getQuestionSet() == null && lmsLessonDataBean.getQuestionSet() != null) {
                                                    return true;
                                                }else if(lmsLessonDataBean1.getQuestionSet() != null && lmsLessonDataBean.getQuestionSet() != null &&
                                                        (!lmsLessonDataBean1.getQuestionSet().containsAll(lmsLessonDataBean.getQuestionSet()) ||
                                                                !lmsLessonDataBean.getQuestionSet().containsAll(lmsLessonDataBean1.getQuestionSet()))){
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        ImageView notification_badge, icon_menu;
        TextView title;
        TextView numberOfLessonsAndQuizzes;
        TextView desc;
        LinearLayout progressLayout;
        TextView progressText;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            notification_badge = itemView.findViewById(R.id.notification_badge);
            icon_menu = itemView.findViewById(R.id.icon_menu);
            title = itemView.findViewById(R.id.course_title);
            numberOfLessonsAndQuizzes = itemView.findViewById(R.id.course_lessons_and_quizzes);
            desc = itemView.findViewById(R.id.course_desc);
            progressLayout = itemView.findViewById(R.id.course_progress_layout);
            progressText = itemView.findViewById(R.id.course_progress_text);
            progressBar = itemView.findViewById(R.id.course_progress_bar);
            icon_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenuClick(getAdapterPosition(), v);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdapterItemClick(getAdapterPosition());
                }
            });
        }
    }
}