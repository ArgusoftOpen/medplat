package com.argusoft.sewa.android.app.core.impl;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.core.DailyPresenceReportService;
import com.argusoft.sewa.android.app.databean.CurrentLocationDto;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.DailyPresenceReport;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.service.GPSTracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class DailyPresenceReportServiceImpl implements DailyPresenceReportService {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<DailyPresenceReport, Integer> dailyPresenceReportBeanDao;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaServiceRestClientImpl restClient;

    @Override
    public void removePreviousReport() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            DeleteBuilder<DailyPresenceReport, Integer> deleteBuilder = dailyPresenceReportBeanDao.deleteBuilder();
            deleteBuilder.where().lt("startTime", calendar.getTime());
            deleteBuilder.delete();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public Boolean createReport() {
        try {
            Gson gson = new Gson();
            Date today = new Date();
            SharedStructureData.gps.getLocation();
            CurrentLocationDto location = new CurrentLocationDto(today, String.valueOf(GPSTracker.latitude), String.valueOf(GPSTracker.longitude));

            List<CurrentLocationDto> currentLocationDtos = new ArrayList<>();
            currentLocationDtos.add(location);

            Integer attendanceId = restClient.markAttendanceForTheDay(gson.toJson(currentLocationDtos));

            if (attendanceId != null && attendanceId > 0) {
                dailyPresenceReportBeanDao.deleteBuilder().delete();
                DailyPresenceReport dailyPresenceReport = new DailyPresenceReport();
                dailyPresenceReport.setStartTime(today);
                dailyPresenceReport.setLocations(gson.toJson(currentLocationDtos));
                dailyPresenceReport.setAttendanceId(attendanceId);
                dailyPresenceReportBeanDao.create(dailyPresenceReport);
                return true;
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return false;
    }

    @Override
    public void addLocations() {
        List<DailyPresenceReport> dailyPresenceReports;
        try {
            dailyPresenceReports = dailyPresenceReportBeanDao.queryForAll();
            if (dailyPresenceReports != null && !dailyPresenceReports.isEmpty() && dailyPresenceReports.get(0).getEndTime() == null) {
                DailyPresenceReport dailyPresenceReport = dailyPresenceReports.get(0);

                Gson gson = new Gson();
                List<CurrentLocationDto> currentLocationDtos = gson.fromJson(dailyPresenceReport.getLocations(), new TypeToken<List<CurrentLocationDto>>() {
                }.getType());

                SharedStructureData.gps.getLocation();
                CurrentLocationDto location = new CurrentLocationDto(new Date(), String.valueOf(GPSTracker.latitude), String.valueOf(GPSTracker.longitude));
                currentLocationDtos.add(location);
                dailyPresenceReport.setLocations(new Gson().toJson(currentLocationDtos));
                dailyPresenceReportBeanDao.update(dailyPresenceReport);
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public Boolean closeReport() {
        List<DailyPresenceReport> dailyPresenceReports;
        try {
            dailyPresenceReports = dailyPresenceReportBeanDao.queryForAll();
            if (dailyPresenceReports != null && !dailyPresenceReports.isEmpty() && dailyPresenceReports.get(0).getEndTime() == null) {
                DailyPresenceReport dailyPresenceReport = dailyPresenceReports.get(0);
                Date today = new Date();

                Gson gson = new Gson();
                List<CurrentLocationDto> currentLocationDtos = gson.fromJson(dailyPresenceReport.getLocations(), new TypeToken<List<CurrentLocationDto>>() {
                }.getType());

                SharedStructureData.gps.getLocation();
                CurrentLocationDto location = new CurrentLocationDto(today, String.valueOf(GPSTracker.latitude), String.valueOf(GPSTracker.longitude));
                currentLocationDtos.add(location);

                dailyPresenceReport.setEndTime(today);
                dailyPresenceReport.setLocations(new Gson().toJson(currentLocationDtos));
                dailyPresenceReportBeanDao.update(dailyPresenceReport);

                restClient.storeAttendanceForTheDay(dailyPresenceReport.getLocations(), dailyPresenceReport.getAttendanceId());

                SharedStructureData.CHO_ATTENDANCE_IS_MARKED_PRESENCE = false;

                dailyPresenceReportBeanDao.deleteBuilder().delete();
                return true;
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return false;
    }
}
