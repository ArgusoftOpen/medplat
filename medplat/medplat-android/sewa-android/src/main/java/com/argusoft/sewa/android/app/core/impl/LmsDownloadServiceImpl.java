package com.argusoft.sewa.android.app.core.impl;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.argusoft.sewa.android.app.databean.LmsOptionFeedbackDataBean;
import com.argusoft.sewa.android.app.databean.LmsScenarioConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsSimulationAudioDataBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.LmsDownloadService;
import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.argusoft.sewa.android.app.databean.LmsLessonDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionBankDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionOptionDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuestionSetDataBean;
import com.argusoft.sewa.android.app.databean.LmsQuizCompletionMessage;
import com.argusoft.sewa.android.app.databean.LmsQuizConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsSectionConfigDataBean;
import com.argusoft.sewa.android.app.databean.LmsTopicDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FileDownloadBean;
import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class LmsDownloadServiceImpl implements LmsDownloadService {

    @Bean
    LmsServiceImpl lmsService;

    @RootContext
    Context context;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LmsCourseBean, Integer> lmsCourseBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<FileDownloadBean, Integer> fileDownloadBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

    private boolean isFileNotExistsOnLocal(Long mediaId, String fileName) {
        File downloadedFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS_TEMP), fileName);
        if (downloadedFile.exists()) {
            moveFile(mediaId, downloadedFile, fileName);
        }
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS), fileName);
        return !file.exists();
    }

    private boolean isFileExistsOnLocal(String fileName) {
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS), fileName);
        return file.exists();
    }

    private static void clearSharedPref(Context context) {
        SharedPreferences downloadPref = context.getSharedPreferences(
                SewaConstants.LMS_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.clear();
        edit.apply();
    }

    private void deleteFile(Long mediaId, String fileName) {
        Log.e(getClass().getSimpleName(), "deleteFile called for File : " + fileName);
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS_TEMP), fileName);
        if (file.exists()) {
            file.delete();
        }
        clearSharedPref(context);
        try {
            List<FileDownloadBean> fileDownloadBeans = fileDownloadBeanDao.queryBuilder().where().eq("mediaId", mediaId).query();
            if (fileDownloadBeans != null && !fileDownloadBeans.isEmpty()) {
                for (FileDownloadBean fileDownloadBean : fileDownloadBeans) {
                    fileDownloadBean.setNoOfAttempt(fileDownloadBean.getNoOfAttempt() + 1);
                    fileDownloadBeanDao.update(fileDownloadBean);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void deleteLmsFile(Long mediaId, String fileName) {
        Log.e(getClass().getSimpleName(), "deleteLmsFile called for File : " + fileName);
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS), fileName);
        if (file.exists()) {
            file.delete();
        }
        file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS_TEMP), fileName);
        if (file.exists()) {
            file.delete();
        }
        clearSharedPref(context);
        try {
            List<FileDownloadBean> fileDownloadBeans = fileDownloadBeanDao.queryBuilder().where().eq("mediaId", mediaId).query();
            if (fileDownloadBeans != null && !fileDownloadBeans.isEmpty()) {
                for (FileDownloadBean fileDownloadBean : fileDownloadBeans) {
                    fileDownloadBean.setNoOfAttempt(fileDownloadBean.getNoOfAttempt() + 1);
                    fileDownloadBeanDao.update(fileDownloadBean);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void moveFile(Long mediaId, File downloadedFile, String fileName) {
        downloadedFile.renameTo(new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS), fileName));
        Log.i(getClass().getSimpleName(), "#### File moved to " + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS));
        clearSharedPref(context);
        try {
            DeleteBuilder<FileDownloadBean, Integer> deleteBuilder = fileDownloadBeanDao.deleteBuilder();
            deleteBuilder.where().eq("mediaId", mediaId);
            deleteBuilder.delete();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void startFileDownloading(Context context, Long mediaId, String mediaName) {
        List<VersionBean> versionBeans = null;
        try {
            versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, GlobalTypes.VERSION_LMS_FILE_DOWNLOAD_URL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String fileDownloadUrl = WSConstants.REST_TECHO_SERVICE_URL;
        if (versionBeans != null && !versionBeans.isEmpty()) {
            fileDownloadUrl = versionBeans.get(0).getValue() + "api/mobile/";
        }
        String downloadPath = fileDownloadUrl + "getfileById?id=" + mediaId;
        Log.i(getClass().getSimpleName(), "##### Download URL : " + downloadPath);
        final Uri downloadUri = Uri.parse(downloadPath);
        String encode = mediaName;
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri fileUri = Uri.parse("file://" + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS_TEMP) + encode);

        Log.i(getClass().getSimpleName(), "************Downloading started ***********");
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setDestinationUri(fileUri);
        request.setTitle(UtilBean.getMyLabel("TeCHO+") + " - " + UtilBean.getMyLabel("Downloading LMS files"));
        request.setDescription(mediaName);
        long enqueueId = dm.enqueue(request);

        SharedPreferences downloadPref = context.getSharedPreferences(SewaConstants.LMS_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.putLong(FieldNameConstants.DOWNLOAD_ID, enqueueId);
        edit.putLong(FieldNameConstants.MEDIA_ID, mediaId);
        edit.putString(FieldNameConstants.FILE_NAME, mediaName);

        edit.apply();
        Log.i(getClass().getSimpleName(), "#### Registering the Broadcast");
        registerBroadcastReceiver(context);
    }

    private void registerBroadcastReceiver(Context context) {
        BroadcastReceiver receiver;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(getClass().getSimpleName(), "#### Received the Download Broadcast");
                String action = intent.getAction();

                if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i(getClass().getSimpleName(), "#### Broadcast Receiver: DOWNLOAD NOT COMPLETED");
                    return;
                }

                Log.i(getClass().getSimpleName(), "#### Broadcast Receiver: DOWNLOAD COMPLETED.");
                SharedPreferences prefs = context.getSharedPreferences(SewaConstants.LMS_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
                long downloadId = prefs.getLong(FieldNameConstants.DOWNLOAD_ID, 0);
                Long mediaId = prefs.getLong(FieldNameConstants.MEDIA_ID, 0);
                String fileName = prefs.getString(FieldNameConstants.FILE_NAME, null);

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (downloadId != id) {
                    Log.i(getClass().getSimpleName(), "#### DOWNLOAD IDs ARE NOT SAME.");
                    return;
                }

                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));

                if (!cursor.moveToFirst()) {
                    return;
                }

                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED:
                        Log.i(getClass().getSimpleName(), "#### Download failed, Deleting all the files");
                        deleteFile(mediaId, fileName);
                        startDownloading(context);
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        String encode = fileName;
                        try {
                            encode = URLEncoder.encode(encode, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        File downloadedFile = new File(URI.create("file://" + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS_TEMP) + encode));
                        if (downloadedFile.exists()) {
                            Log.i(getClass().getSimpleName(), "#### Downloaded successful : " + fileName);
                            moveFile(mediaId, downloadedFile, fileName);
                        }
                        startDownloading(context);
                        break;
                    default:
                }
                cursor.close();
            }
        };

        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public long getCourseSize(LmsCourseDataBean lmsCourseDataBean) {
        long courseMediaSize = 0;
        for (LmsTopicDataBean topic : lmsCourseDataBean.getTopics()) {
            if (topic.getTopicMedias() == null || topic.getTopicMedias().isEmpty()) {
                continue;
            }
            for (LmsLessonDataBean media : topic.getTopicMedias()) {
                courseMediaSize += media.getSize() != null ? media.getSize() : 0;
            }
        }
        return courseMediaSize;
    }

    //free space available in device(internal storage)
    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return (availableBlocks * blockSize);
    }


    @Override
    public void addLmsMediaFilesToDownloadList() {
        try {
            //First delete previous store not downloaded files of LMS
            DeleteBuilder<FileDownloadBean, Integer> deleteBuilder = fileDownloadBeanDao.deleteBuilder();
            deleteBuilder.where().like("filename", "LMS_MEDIA%");
            deleteBuilder.delete();
            List<FileDownloadBean> fileDownloadBeans = new ArrayList<>();
            List<LmsCourseBean> lmsCourseBeans = lmsCourseBeanDao.queryForAll();
            for (LmsCourseBean lmsCourseBean : lmsCourseBeans) {
                LmsCourseDataBean course = new LmsCourseDataBean(lmsCourseBean);
                lmsService.makeCourseDownloadable(course.getCourseId(), this.getCourseSize(course) <= this.getAvailableInternalMemorySize());
                course.setMediaDownloaded(this.getCourseSize(course) <= this.getAvailableInternalMemorySize());
                if ((course.getArchived() == null || Boolean.FALSE.equals(course.getArchived())) && course.getMediaDownloaded()) {
                    addCourseTestConfigMediasForDownload(fileDownloadBeans, course.getTestConfigJson());
                    addQuestionSetFilesForDownload(fileDownloadBeans, course.getQuestionSet());
                    if (course.getCourseImage() != null && course.getCourseImage().getMediaId() != null
                            && isFileNotExistsOnLocal(
                            course.getCourseImage().getMediaId(),
                            UtilBean.getLMSFileName(course.getCourseImage().getMediaId(), course.getCourseImage().getMediaName()))) {
                        fileDownloadBeans.add(new FileDownloadBean(
                                course.getCourseImage().getMediaId(),
                                UtilBean.getLMSFileName(course.getCourseImage().getMediaId(), course.getCourseImage().getMediaName()),
                                getExtensionFromFileName(course.getCourseImage().getMediaExtension(), course.getCourseImage().getMediaName()),
                                0, false));
                    }
                    if (course.getTopics() == null || course.getTopics().isEmpty()) {
                        continue;
                    }
//                int count = 0;
                    for (LmsTopicDataBean topic : course.getTopics()) {
                        addQuestionSetFilesForDownload(fileDownloadBeans, topic.getQuestionSet());
                        if (topic.getTopicMedias() == null || topic.getTopicMedias().isEmpty()) {
                            continue;
                        }
                        for (LmsLessonDataBean media : topic.getTopicMedias()) {
                            addQuestionSetFilesForDownload(fileDownloadBeans, media.getQuestionSet());
//                        if (count <= 1) {
                            addMediaFilesForDownload(fileDownloadBeans, media);
//                            count++;
//                        }
                        }
                    }
                }
            }
            if (!fileDownloadBeans.isEmpty()) {
                fileDownloadBeanDao.create(fileDownloadBeans);
            }
            startDownloading(context);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    private void addMediaFilesForDownload(List<FileDownloadBean> fileDownloadBeans, LmsLessonDataBean media) throws SQLException {
        String fileName = UtilBean.getLMSFileName(media.getMediaId(), media.getMediaFileName());
        if (media.getMediaId() != null && isFileNotExistsOnLocal(media.getMediaId(), fileName)) {
            Log.i(getClass().getSimpleName(), "LMS File does not Exists on Local : " + media.getMediaFileName());
            fileDownloadBeans.add(new FileDownloadBean(
                    media.getMediaId(),
                    fileName,
                    getExtensionFromFileName(media.getMediaExtension(), media.getMediaFileName()),
                    0, false));
        }
        String transcriptFileName = UtilBean.getLMSFileName(media.getTranscriptFileId(), media.getTranscriptFileName());
        if (media.getTranscriptFileId() != null && isFileNotExistsOnLocal(media.getTranscriptFileId(), transcriptFileName)) {
            Log.i(getClass().getSimpleName(), "LMS transcript File does not Exists on Local : " + media.getMediaFileName());
            fileDownloadBeans.add(new FileDownloadBean(
                    media.getTranscriptFileId(),
                    transcriptFileName,
                    getExtensionFromFileName(media.getTranscriptFileExtension(), media.getTranscriptFileExtension()),
                    0, false));
        }
    }


    private void addCourseTestConfigMediasForDownload(List<FileDownloadBean> fileDownloadBeans, Map<Integer, LmsQuizConfigDataBean> testConfigJson) {
        if (testConfigJson != null && !testConfigJson.isEmpty()) {
            for (Map.Entry<Integer, LmsQuizConfigDataBean> entry : testConfigJson.entrySet()) {
                LmsQuizConfigDataBean lmsQuizConfigDataBean = entry.getValue();
                if (lmsQuizConfigDataBean.getQuizCompletionMessage() != null) {
                    LmsQuizCompletionMessage quizCompletionMessage = lmsQuizConfigDataBean.getQuizCompletionMessage();
                    String fileName = UtilBean.getLMSFileName(quizCompletionMessage.getMediaId(), quizCompletionMessage.getMediaName());
                    if (quizCompletionMessage.getMediaId() != null && isFileNotExistsOnLocal(quizCompletionMessage.getMediaId(), fileName)) {
                        fileDownloadBeans.add(new FileDownloadBean(
                                quizCompletionMessage.getMediaId(),
                                fileName,
                                getExtensionFromFileName(quizCompletionMessage.getMediaExtension(), quizCompletionMessage.getMediaName()),
                                0, false));
                    }
                }
            }
        }
    }

    private void addQuestionSetFilesForDownload(List<FileDownloadBean> fileDownloadBeans, List<LmsQuestionSetDataBean> questionSetDataBeans) {
        if (questionSetDataBeans == null || questionSetDataBeans.isEmpty()) {
            return;
        }
        for (LmsQuestionSetDataBean questionSet : questionSetDataBeans) {
            if (questionSet.getQuestionBank() == null || questionSet.getQuestionBank().isEmpty()) {
                continue;
            }
            for (LmsQuestionBankDataBean questionBank : questionSet.getQuestionBank()) {
                if (questionBank.getConfigJson() == null || questionBank.getConfigJson().isEmpty()) {
                    continue;
                }

                List<LmsSectionConfigDataBean> questionConfigs = new Gson().fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsSectionConfigDataBean>>() {
                }.getType());

                List<LmsScenarioConfigDataBean> scenarioConfigs = new Gson().fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsScenarioConfigDataBean>>() {
                }.getType());

                for (LmsSectionConfigDataBean sectionConfig : questionConfigs) {
                    if (sectionConfig.getQuestions() != null && !sectionConfig.getQuestions().isEmpty()) {
                        for (LmsQuestionConfigDataBean questionConfig : sectionConfig.getQuestions()) {
                            addQuestionConfigFilesForDownload(fileDownloadBeans, questionConfig);
                        }
                    }
                }

                //For downloading  media for scenario
                for (LmsScenarioConfigDataBean sectionConfig : scenarioConfigs) {
                    addScenarioQuestionConfigFilesForDownload(fileDownloadBeans, sectionConfig);
                    addScenarioAudioFilesForDownload(fileDownloadBeans, sectionConfig.getAudio());
                }

                //For downloading questions and options media for scenario
                for (LmsScenarioConfigDataBean sectionConfig : scenarioConfigs) {
                    if (sectionConfig.getScenarioQuestions() != null && !sectionConfig.getScenarioQuestions().isEmpty()) {
                        for (LmsQuestionConfigDataBean questionConfig : sectionConfig.getScenarioQuestions()) {
                            addQuestionConfigFilesForDownload(fileDownloadBeans, questionConfig);
                        }
                    }
                }

                //For downloading option feedback media for correct and incorrect answers
                for (LmsScenarioConfigDataBean sectionConfig : scenarioConfigs) {
                    if (sectionConfig.getScenarioQuestions() != null && !sectionConfig.getScenarioQuestions().isEmpty()) {
                        for (LmsQuestionConfigDataBean questionConfig : sectionConfig.getScenarioQuestions()) {
                            if (questionConfig.getOptions() != null && !questionConfig.getOptions().isEmpty()) {
                                for (LmsQuestionOptionDataBean optionDataBean : questionConfig.getOptions()) {
                                    addOptionFeedbackMediaForScenarioForDownload(fileDownloadBeans, optionDataBean.getOptionFeedback());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addQuestionConfigFilesForDownload(List<FileDownloadBean> fileDownloadBeans, LmsQuestionConfigDataBean questionConfig) {
        String fileName = UtilBean.getLMSFileName(questionConfig.getMediaId(), questionConfig.getMediaName());
        if (questionConfig.getMediaId() != null && isFileNotExistsOnLocal(questionConfig.getMediaId(), fileName)) {
            fileDownloadBeans.add(new FileDownloadBean(
                    questionConfig.getMediaId(),
                    fileName,
                    getExtensionFromFileName(questionConfig.getMediaExtension(), questionConfig.getMediaName()),
                    0, false));
        }

        addQuestionOptionMediaForDownload(fileDownloadBeans, questionConfig.getOptions());
        addQuestionOptionMediaForDownload(fileDownloadBeans, questionConfig.getLhs());
        addQuestionOptionMediaForDownload(fileDownloadBeans, questionConfig.getRhs());
    }

    private void addScenarioQuestionConfigFilesForDownload(List<FileDownloadBean> fileDownloadBeans, LmsScenarioConfigDataBean questionConfig) {
        String fileName = UtilBean.getLMSFileName(questionConfig.getMediaId(), questionConfig.getMediaName());
        if (questionConfig.getMediaId() != null && isFileNotExistsOnLocal(questionConfig.getMediaId(), fileName)) {
            fileDownloadBeans.add(new FileDownloadBean(
                    questionConfig.getMediaId(),
                    fileName,
                    getExtensionFromFileName(questionConfig.getMediaExtension(), questionConfig.getMediaName()),
                    0, false));
        }
    }

    private void addScenarioAudioFilesForDownload(List<FileDownloadBean> fileDownloadBeans, LmsSimulationAudioDataBean simulationAudioDataBean) {
        if (simulationAudioDataBean != null){
            String fileName = UtilBean.getLMSFileName(simulationAudioDataBean.getMediaId(), simulationAudioDataBean.getMediaName());
            if (simulationAudioDataBean.getMediaId() != null && isFileNotExistsOnLocal(simulationAudioDataBean.getMediaId(), fileName)) {
                fileDownloadBeans.add(new FileDownloadBean(
                        simulationAudioDataBean.getMediaId(),
                        fileName,
                        getExtensionFromFileName(simulationAudioDataBean.getMediaExtension(), simulationAudioDataBean.getMediaName()),
                        0, false));
            }
        }
    }

    private void addOptionFeedbackMediaForScenarioForDownload(List<FileDownloadBean> fileDownloadBeans, LmsOptionFeedbackDataBean optionFeedbackDataBean) {
        String fileName = UtilBean.getLMSFileName(optionFeedbackDataBean.getMediaId(), optionFeedbackDataBean.getMediaName());
        if (optionFeedbackDataBean.getMediaId() != null && isFileNotExistsOnLocal(optionFeedbackDataBean.getMediaId(), fileName)) {
            fileDownloadBeans.add(new FileDownloadBean(
                    optionFeedbackDataBean.getMediaId(),
                    fileName,
                    getExtensionFromFileName(optionFeedbackDataBean.getMediaExtension(), optionFeedbackDataBean.getMediaName()),
                    0, false));
        }
    }

    private void addQuestionOptionMediaForDownload(List<FileDownloadBean> fileDownloadBeans, List<LmsQuestionOptionDataBean> options) {
        if (options != null && !options.isEmpty()) {
            for (LmsQuestionOptionDataBean option : options) {
                String fileName = UtilBean.getLMSFileName(option.getMediaId(), option.getMediaName());
                if (option.getMediaId() != null && isFileNotExistsOnLocal(option.getMediaId(), fileName)) {
                    fileDownloadBeans.add(new FileDownloadBean(
                            option.getMediaId(),
                            fileName,
                            getExtensionFromFileName(option.getMediaExtension(), option.getMediaName()),
                            0, false));
                }
            }
        }
    }

    private void startDownloading(Context context) {
        try {
            Where<FileDownloadBean, Integer> where = fileDownloadBeanDao.queryBuilder().where();
            FileDownloadBean fileDownloadBean = where.and(
                    where.lt("noOfAttempt", 3),
                    where.eq("isDownloaded", false)
            ).queryForFirst();

            if (fileDownloadBean != null) {
                startFileDownloading(context, fileDownloadBean.getMediaId(), fileDownloadBean.getFileName());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static String getExtensionFromFileName(String extension, String fileName) {
        if (extension != null) {
            return extension;
        }
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public void addCourseMediaToDownload(Integer courseId) {
        try {
            LmsCourseBean lmsCourseBean = lmsCourseBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            if (lmsCourseBean != null) {
                LmsCourseDataBean course = new LmsCourseDataBean(lmsCourseBean);

                //First delete previous store not downloaded files of LMS
                DeleteBuilder<FileDownloadBean, Integer> deleteBuilder = fileDownloadBeanDao.deleteBuilder();
                deleteBuilder.where().like("filename", "LMS_MEDIA%");
                deleteBuilder.delete();

                List<FileDownloadBean> fileDownloadBeans = new ArrayList<>();

                lmsService.makeCourseDownloadable(course.getCourseId(), this.getCourseSize(course) <= this.getAvailableInternalMemorySize());
                course.setMediaDownloaded(this.getCourseSize(course) <= this.getAvailableInternalMemorySize());
                if ((course.getArchived() == null || Boolean.FALSE.equals(course.getArchived())) && course.getMediaDownloaded()) {
                    addCourseTestConfigMediasForDownload(fileDownloadBeans, course.getTestConfigJson());
                    addQuestionSetFilesForDownload(fileDownloadBeans, course.getQuestionSet());
                    if (course.getCourseImage() != null && course.getCourseImage().getMediaId() != null
                            && isFileNotExistsOnLocal(
                            course.getCourseImage().getMediaId(),
                            UtilBean.getLMSFileName(course.getCourseImage().getMediaId(), course.getCourseImage().getMediaName()))) {
                        fileDownloadBeans.add(new FileDownloadBean(
                                course.getCourseImage().getMediaId(),
                                UtilBean.getLMSFileName(course.getCourseImage().getMediaId(), course.getCourseImage().getMediaName()),
                                getExtensionFromFileName(course.getCourseImage().getMediaExtension(), course.getCourseImage().getMediaName()),
                                0, false));
                    }
                    if (course.getTopics() == null || course.getTopics().isEmpty()) {

                    } else {

                        for (LmsTopicDataBean topic : course.getTopics()) {
                            addQuestionSetFilesForDownload(fileDownloadBeans, topic.getQuestionSet());
                            if (topic.getTopicMedias() == null || topic.getTopicMedias().isEmpty()) {
                                continue;
                            }
                            for (LmsLessonDataBean media : topic.getTopicMedias()) {
                                addQuestionSetFilesForDownload(fileDownloadBeans, media.getQuestionSet());
                                addMediaFilesForDownload(fileDownloadBeans, media);
                            }
                        }
                    }
                }

                if (!fileDownloadBeans.isEmpty()) {
                    fileDownloadBeanDao.create(fileDownloadBeans);
                }
                startDownloading(context);

            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public void deleteCourseMedia(Integer courseId) {
        try {
            LmsCourseBean course = lmsCourseBeanDao.queryBuilder().where().eq(FieldNameConstants.COURSE_ID, courseId).queryForFirst();
            if (course != null) {
                LmsCourseDataBean lmsCourseDataBean = new LmsCourseDataBean(course);

                Map<Integer, LmsQuizConfigDataBean> testConfigJson = lmsCourseDataBean.getTestConfigJson();
                List<LmsQuestionSetDataBean> questionSetDataBeans = lmsCourseDataBean.getQuestionSet();
                if (testConfigJson != null && !testConfigJson.isEmpty()) {
                    for (Map.Entry<Integer, LmsQuizConfigDataBean> entry : testConfigJson.entrySet()) {
                        LmsQuizConfigDataBean lmsQuizConfigDataBean = entry.getValue();
                        if (lmsQuizConfigDataBean.getQuizCompletionMessage() != null) {
                            LmsQuizCompletionMessage quizCompletionMessage = lmsQuizConfigDataBean.getQuizCompletionMessage();
                            String fileName = UtilBean.getLMSFileName(quizCompletionMessage.getMediaId(), quizCompletionMessage.getMediaName());
                            if (quizCompletionMessage.getMediaId() != null && isFileExistsOnLocal(fileName)) {
                                deleteLmsFile(quizCompletionMessage.getMediaId(), fileName);
                            }
                        }
                    }
                }

                deleteQuestionSetFiles(questionSetDataBeans);

                if (lmsCourseDataBean.getTopics() == null || lmsCourseDataBean.getTopics().isEmpty()) {
                    return;
                }

                for (LmsTopicDataBean topic : lmsCourseDataBean.getTopics()) {
                    deleteQuestionSetFiles(topic.getQuestionSet());
                    if (topic.getTopicMedias() == null || topic.getTopicMedias().isEmpty()) {
                        continue;
                    }
                    for (LmsLessonDataBean media : topic.getTopicMedias()) {
                        deleteQuestionSetFiles(media.getQuestionSet());
                        deleteMediaFiles(media);
                    }
                }
            }

        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    private void deleteMediaFiles(LmsLessonDataBean media) {
        String fileName = UtilBean.getLMSFileName(media.getMediaId(), media.getMediaFileName());
        if (media.getMediaId() != null && isFileExistsOnLocal(fileName)) {
            Log.i(getClass().getSimpleName(), "LMS File does Exists on Local : " + media.getMediaFileName());
            deleteLmsFile(media.getMediaId(), fileName);
        }
        String transcriptFileName = UtilBean.getLMSFileName(media.getTranscriptFileId(), media.getTranscriptFileName());
        if (media.getTranscriptFileId() != null && isFileExistsOnLocal(transcriptFileName)) {
            Log.i(getClass().getSimpleName(), "LMS transcript File does Exists on Local : " + media.getMediaFileName());
            deleteLmsFile(media.getTranscriptFileId(), fileName);
        }
    }

    private void deleteQuestionSetFiles(List<LmsQuestionSetDataBean> questionSetDataBeans) {
        if (questionSetDataBeans == null || questionSetDataBeans.isEmpty()) {
            return;
        }
        for (LmsQuestionSetDataBean questionSet : questionSetDataBeans) {
            if (questionSet.getQuestionBank() == null || questionSet.getQuestionBank().isEmpty()) {
                continue;
            }
            for (LmsQuestionBankDataBean questionBank : questionSet.getQuestionBank()) {
                if (questionBank.getConfigJson() == null || questionBank.getConfigJson().isEmpty()) {
                    continue;
                }
                List<LmsSectionConfigDataBean> questionConfigs = new Gson().fromJson(questionBank.getConfigJson(), new TypeToken<List<LmsSectionConfigDataBean>>() {
                }.getType());
                for (LmsSectionConfigDataBean sectionConfig : questionConfigs) {
                    if (sectionConfig.getQuestions() != null && !sectionConfig.getQuestions().isEmpty()) {
                        for (LmsQuestionConfigDataBean questionConfig : sectionConfig.getQuestions()) {
                            deleteQuestionConfigFiles(questionConfig);
                        }
                    }
                }
            }
        }
    }

    private void deleteQuestionConfigFiles(LmsQuestionConfigDataBean questionConfig) {
        String fileName = UtilBean.getLMSFileName(questionConfig.getMediaId(), questionConfig.getMediaName());
        if (questionConfig.getMediaId() != null && isFileExistsOnLocal(fileName)) {
            deleteLmsFile(questionConfig.getMediaId(), fileName);
        }

        deleteQuestionOptionMedia(questionConfig.getOptions());
        deleteQuestionOptionMedia(questionConfig.getLhs());
        deleteQuestionOptionMedia(questionConfig.getRhs());
    }

    private void deleteQuestionOptionMedia(List<LmsQuestionOptionDataBean> options) {
        if (options != null && !options.isEmpty()) {
            for (LmsQuestionOptionDataBean option : options) {
                String fileName = UtilBean.getLMSFileName(option.getMediaId(), option.getMediaName());
                if (option.getMediaId() != null && isFileExistsOnLocal(fileName)) {
                    deleteLmsFile(option.getMediaId(), fileName);
                }
            }
        }
    }
}
