package com.argusoft.sewa.android.app.core;

import android.content.Context;

public interface LmsDownloadService {

    void startFileDownloading(Context context, Long mediaId, String mediaName);

    void addLmsMediaFilesToDownloadList();

    void addCourseMediaToDownload(Integer courseId);

    void deleteCourseMedia(Integer courseId);


}
