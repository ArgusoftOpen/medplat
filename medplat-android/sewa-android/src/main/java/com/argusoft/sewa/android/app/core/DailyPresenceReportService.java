package com.argusoft.sewa.android.app.core;

public interface DailyPresenceReportService {
    void removePreviousReport();
    Boolean createReport();
    void addLocations();
    Boolean closeReport();
}
