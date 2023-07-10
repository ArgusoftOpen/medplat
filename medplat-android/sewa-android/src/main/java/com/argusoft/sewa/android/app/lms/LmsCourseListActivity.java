package com.argusoft.sewa.android.app.lms;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.activity.LoginActivity_;
import com.argusoft.sewa.android.app.activity.MenuActivity;
import com.argusoft.sewa.android.app.core.impl.LmsDownloadServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class LmsCourseListActivity extends MenuActivity {

    @Bean
    LmsServiceImpl lmsService;
    @Bean
    LmsDownloadServiceImpl lmsDownloadService;

    private List<LmsCourseDataBean> pendingCourseDataBeans;
    private List<LmsCourseDataBean> completedCourseDataBeans;
    private List<LmsCourseDataBean> archivedCourseDataBeans;
    private LmsFragmentPagerAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_lms_course_list);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
    }

    private void initView() {
        MaterialToolbar mToolbar = findViewById(R.id.lms_toolbar);
        mToolbar.setTitle(UtilBean.getMyLabel("MY COURSES"));
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_back);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView courseImageView = findViewById(R.id.lmsCourseViewImage);
        if (BuildConfig.FLAVOR.equalsIgnoreCase(GlobalTypes.UTTARAKHAND_FLAVOR)){
            courseImageView.setImageResource(R.drawable.uttarakhand_theme);
        }
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        //Fetch List of courses
        pendingCourseDataBeans = new ArrayList<>();
        completedCourseDataBeans = new ArrayList<>();
        archivedCourseDataBeans = new ArrayList<>();
        setCourseList();
        setCourseScreen();
    }

    private void setCourseList(){
        List<LmsCourseDataBean> lmsCourseDataBeans = lmsService.retrieveCourses();
        archivedCourseDataBeans.clear();
        pendingCourseDataBeans.clear();
        completedCourseDataBeans.clear();
        for (LmsCourseDataBean course : lmsCourseDataBeans) {
            if (course.getArchived() != null && Boolean.TRUE.equals(course.getArchived())) {
                archivedCourseDataBeans.add(course);
            }else if (course.getCompletionStatus() < 100) {
                pendingCourseDataBeans.add(course);
            } else {
                completedCourseDataBeans.add(course);
            }
        }
    }


    @UiThread
    public void setCourseScreen() {
        ViewPager viewPager = findViewById(R.id.course_view_pager);
        TabLayout tabLayout = findViewById(R.id.course_tabs);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons(tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(adapter != null) {
                    setCourseList();
                    int position = tab.getPosition();
                    switch (position) {
                        case 0:
                            LmsPendingCourseListFragment lmsPendingCourseListFragment =
                                    (LmsPendingCourseListFragment) adapter.getItem(position);
                            lmsPendingCourseListFragment.resetList(pendingCourseDataBeans);
                            break;
                        case 1:
                            LmsCompletedCourseListFragment lmsCompletedCourseListFragment =
                                    (LmsCompletedCourseListFragment) adapter.getItem(position);
                            lmsCompletedCourseListFragment.resetList(completedCourseDataBeans);
                            break;
                        case 2:
                            LmsArchivedCourseListFragment lmsArchivedCourseListFragment =
                                    (LmsArchivedCourseListFragment) adapter.getItem(position);
                            lmsArchivedCourseListFragment.resetList(archivedCourseDataBeans);
                            break;

                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new LmsFragmentPagerAdaptor(getSupportFragmentManager());
        LmsPendingCourseListFragment lmsPendingCourseListFragment = LmsPendingCourseListFragment.newInstance(lmsDownloadService,lmsService, pendingCourseDataBeans);
        LmsCompletedCourseListFragment lmsCompletedCourseListFragment = LmsCompletedCourseListFragment.newInstance(lmsDownloadService,lmsService, completedCourseDataBeans);
        LmsArchivedCourseListFragment lmsArchivedCourseListFragment = LmsArchivedCourseListFragment.newInstance(lmsDownloadService,lmsService, archivedCourseDataBeans);
        adapter.addFrag(lmsPendingCourseListFragment, UtilBean.getMyLabel("Pending"));
        adapter.addFrag(lmsCompletedCourseListFragment, UtilBean.getMyLabel("Completed"));
        adapter.addFrag(lmsArchivedCourseListFragment, UtilBean.getMyLabel("Archived"));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons(TabLayout tabLayout) {
        TabLayout.Tab pending = tabLayout.getTabAt(0);
        if (pending != null) {
            pending.setIcon(R.drawable.ic_pending_course);
        }
        TabLayout.Tab completed = tabLayout.getTabAt(1);
        if (completed != null) {
            completed.setIcon(R.drawable.ic_completed_course);
        }
        TabLayout.Tab archived = tabLayout.getTabAt(2);
        if (archived != null) {
            archived.setIcon(R.drawable.ic_completed_course);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }

}
