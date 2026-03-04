package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.LibraryDataBean;

import java.util.List;

/**
 * Created by prateek on 12/2/19.
 */
public interface LibraryDownloadService {

    void deleteFile(String fileName, Long actualId);

    void retrieveNotDownloadedAndStartDownload();

    List<LibraryDataBean> retrieveLibraryBeansByCategory(String category, String search);
}
