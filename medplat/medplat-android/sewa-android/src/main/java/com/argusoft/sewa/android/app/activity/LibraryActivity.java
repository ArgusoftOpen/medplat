package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAudioPlayer;
import com.argusoft.sewa.android.app.component.MyLibraryAdapter;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.MyExpandedGridView;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.LibraryConstants;
import com.argusoft.sewa.android.app.core.impl.LibraryDownloadServiceImpl;
import com.argusoft.sewa.android.app.databean.LibraryDataBean;
import com.argusoft.sewa.android.app.databean.LibraryScreenDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 12/2/19.
 */
@EActivity
public class LibraryActivity extends MenuActivity implements AdapterView.OnItemClickListener {

    @Bean
    LibraryDownloadServiceImpl libraryDownloadService;

    private int currentLevel = 0;
    private String category = "";
    private String search = "";
    private Set<String> distinctCategory = new HashSet<>();
    private List<String> foldersInCurrentLevel = new ArrayList<>();
    private List<LibraryDataBean> libBeansInCurrentLevel = new LinkedList<>();
    private List<LibraryScreenDataBean> folders = new LinkedList<>();
    private List<LibraryScreenDataBean> videos = new LinkedList<>();
    private List<LibraryScreenDataBean> images = new LinkedList<>();
    private List<LibraryScreenDataBean> audios = new LinkedList<>();
    private List<LibraryScreenDataBean> pdfs = new LinkedList<>();
    private List<LibraryScreenDataBean> unsupported = new LinkedList<>();
    private boolean nothingToShow = false;
    private LinearLayout layout;
    private TextInputLayout searchBox;
    private Timer timer = new Timer();
    private static final long DELAY = 500;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_library);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.lib_icons);
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.LIBRARY_TITLE));

        try {
            retrieveLibraryBeans();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    public void addSearchTextBox() {
        if (searchBox == null)
            searchBox = MyStaticComponents.getEditText(this, LabelConstants.File_TAGS_TO_SEARCH, 1, 10, 1);
        if (searchBox.getEditText() != null) {
            searchBox.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //not implemented
                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before, int count) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    if (s != null && s.length() > 2) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                search = s.toString();
                                                try {
                                                    retrieveLibraryBeans();
                                                } catch (Exception e) {
                                                    Log.e(getClass().getName(), null, e);
                                                }
                                            }
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showProcessDialog();
                                                search = "";
                                                try {
                                                    retrieveLibraryBeans();
                                                } catch (Exception e) {
                                                    Log.e(getClass().getName(), null, e);
                                                }
                                                searchBox.clearFocus();
                                            }
                                        });
                                    }
                                }
                            },
                            DELAY
                    );
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //not implemented
                }
            });
        }

        if (currentLevel == 0)
            layout.addView(searchBox);
    }


    @Background
    public void retrieveLibraryBeans() {
        showProcessDialog();

        libBeansInCurrentLevel.clear();
        distinctCategory.clear();
        foldersInCurrentLevel.clear();
        nothingToShow = false;

        List<LibraryDataBean> libraryDataBeans = libraryDownloadService.retrieveLibraryBeansByCategory(category, search);

        for (LibraryDataBean libraryDataBean : libraryDataBeans) {
            if (category.equals(libraryDataBean.getCategory())) {
                libBeansInCurrentLevel.add(libraryDataBean);
            } else {
                distinctCategory.add(libraryDataBean.getCategory());
            }
        }

        sortLibraryDataBeans();
        setLibraryBeansStatic();
    }

    private void sortLibraryDataBeans() {
        List<LibraryDataBean> videoBeans = new LinkedList<>();
        List<LibraryDataBean> imageBeans = new LinkedList<>();
        List<LibraryDataBean> audioBeans = new LinkedList<>();
        List<LibraryDataBean> pdfBeans = new LinkedList<>();
        List<LibraryDataBean> unsupportedBeans = new LinkedList<>();
        for (LibraryDataBean bean : libBeansInCurrentLevel) {
            String extension = bean.getFileType();
            switch (extension) {
                case "3gp":
                case "mp4":
                    videoBeans.add(bean);
                    break;
                case "jpg":
                case "png":
                    imageBeans.add(bean);
                    break;
                case "mp3":
                    audioBeans.add(bean);
                    break;
                case "pdf":
                    pdfBeans.add(bean);
                    break;
                default:
                    unsupportedBeans.add(bean);
                    break;
            }
        }
        libBeansInCurrentLevel.clear();
        libBeansInCurrentLevel.addAll(videoBeans);
        libBeansInCurrentLevel.addAll(imageBeans);
        libBeansInCurrentLevel.addAll(audioBeans);
        libBeansInCurrentLevel.addAll(pdfBeans);
        libBeansInCurrentLevel.addAll(unsupportedBeans);
    }

    @UiThread
    public void setLibraryBeansStatic() {
        layout.removeAllViews();
        folders.clear();
        videos.clear();
        audios.clear();
        images.clear();
        pdfs.clear();
        unsupported.clear();
        addSearchTextBox();

        if (distinctCategory != null && !distinctCategory.isEmpty()) {
            for (String cat : distinctCategory) {
                if (!foldersInCurrentLevel.contains(cat.split("/")[currentLevel])) {
                    foldersInCurrentLevel.add(cat.split("/")[currentLevel]);
                }
            }
        }

        if (!foldersInCurrentLevel.isEmpty()) {
            for (String cat : foldersInCurrentLevel) {
                BitmapDrawable drawable = (BitmapDrawable) context.getDrawable(R.drawable.folder);
                if (drawable != null) {
                    folders.add(new LibraryScreenDataBean(drawable.getBitmap(), cat));
                }
            }
        }

        if (!libBeansInCurrentLevel.isEmpty()) {
            BitmapDrawable drawableAudio = (BitmapDrawable) context.getDrawable(R.drawable.play_audio);
            BitmapDrawable drawablePdf = (BitmapDrawable) context.getDrawable(R.drawable.pdf);
            BitmapDrawable drawableFile = (BitmapDrawable) context.getDrawable(R.drawable.file);

            for (LibraryDataBean bean : libBeansInCurrentLevel) {
                String extension = bean.getFileType();
                String fileName = bean.getDescription();
                String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY) + bean.getActualId() + "." + bean.getFileType();
                // If file with actualId as name does not exists, then it will check if file with fileName exist and rename it to file with actualId as name
                File file = new File(path);
                if (!(file.exists())) {
                    File file1 = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY) + bean.getFileName());
                    if (file1.exists()) {
                        file1.renameTo(file);
                    } else {
                        continue;
                    }
                }

                switch (extension) {
                    case "3gp":
                    case "mp4":
                        videos.add(new LibraryScreenDataBean(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND), fileName));
                        break;
                    case "jpg":
                    case "png":
                        images.add(new LibraryScreenDataBean(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 320, 320), fileName));
                        break;
                    case "mp3":
                        if (drawableAudio != null)
                            audios.add(new LibraryScreenDataBean(drawableAudio.getBitmap(), fileName));
                        break;
                    case "pdf":
                        if (drawablePdf != null)
                            pdfs.add(new LibraryScreenDataBean(drawablePdf.getBitmap(), fileName));
                        break;
                    default:
                        if (drawableFile != null)
                            unsupported.add(new LibraryScreenDataBean(drawableFile.getBitmap(), fileName));
                        break;
                }
            }
        }

        if (folders.isEmpty() && videos.isEmpty() && audios.isEmpty() && images.isEmpty() && pdfs.isEmpty() && unsupported.isEmpty()) {
            layout.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.NOTHING_TO_DISPLAY));
            nothingToShow = true;
        }

        if (!folders.isEmpty()) {
            addFolderView(folders, LibraryConstants.FOLDERS, 3);
        }
        if (!videos.isEmpty()) {
            addFolderView(videos, LibraryConstants.VIDEOS, 2);
        }
        if (!audios.isEmpty()) {
            addFolderView(audios, LibraryConstants.AUDIO, 1);
        }
        if (!images.isEmpty()) {
            addFolderView(images, LibraryConstants.IMAGES, 2);
        }
        if (!pdfs.isEmpty()) {
            addFolderView(pdfs, LibraryConstants.PDFS, 3);
        }
        if (!unsupported.isEmpty()) {
            addFolderView(unsupported, LibraryConstants.UNSUPPORTED_FILES, 3);
        }
        hideProcessDialog();
    }

    private int getPositionAccordingToType(String fileType, int position) {
        switch (fileType) {
            case LibraryConstants.FOLDERS:
                return position;
            case LibraryConstants.VIDEOS:
                return folders.size() + position;
            case LibraryConstants.AUDIO:
                return folders.size() + videos.size() + position;
            case LibraryConstants.IMAGES:
                return folders.size() + videos.size() + audios.size() + position;
            case LibraryConstants.PDFS:
                return folders.size() + videos.size() + audios.size() + images.size() + position;
            case LibraryConstants.UNSUPPORTED_FILES:
                return folders.size() + videos.size() + audios.size() + images.size() + pdfs.size() + position;
            default:
                return -1;
        }
    }

    private void addFolderView(List<LibraryScreenDataBean> screenDataBeans, final String header, int colCount) {
        layout.addView(getFileTypeHeader(header));

        MyLibraryAdapter adapter = new MyLibraryAdapter(context, screenDataBeans, header);
        MyExpandedGridView gridView = new MyExpandedGridView(context);
        gridView.setExpanded(true);
        gridView.setNumColumns(colCount);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setAdapter(adapter);

        AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
            int position = getPositionAccordingToType(header, i);
            if (position != -1) {
                LibraryActivity.this.onItemClick(null, null, position, -1);
            }
        };

        gridView.setOnItemClickListener(onItemClickListener);
        layout.addView(gridView);
    }

    private LinearLayout getFileTypeHeader(String fileType) {
        LinearLayout inflate = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.file_type_header, null);
        MaterialTextView textView = inflate.findViewById(R.id.fileTypeHeaderText);
        textView.setText(fileType);
        return inflate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (nothingToShow) {
            return;
        }

        if (position + 1 > foldersInCurrentLevel.size()) {
            openFileAccordingToType(position - foldersInCurrentLevel.size());
        } else {
            currentLevel++;
            if (category != null && !category.isEmpty()) {
                category = category + "/" + foldersInCurrentLevel.get(position);
            } else {
                category = foldersInCurrentLevel.get(position);
            }

            retrieveLibraryBeans();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == 0) {
            navigateToHomeScreen(false);
        } else {
            currentLevel--;
            if (category.contains("/")) {
                String[] split = category.split("/");
                String[] strings = Arrays.copyOf(split, split.length - 1);
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : strings) {
                    stringBuilder.append(string);
                    stringBuilder.append("/");
                }
                String str = stringBuilder.toString();
                category = str.substring(0, str.length() - 1);
            } else {
                category = "";
            }

            retrieveLibraryBeans();
        }
    }

    private void openFileAccordingToType(int index) {
        LibraryDataBean libraryDataBean = libBeansInCurrentLevel.get(index);

        String path = SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY) + libraryDataBean.getActualId() + "." + libraryDataBean.getFileType();
        String extension = libraryDataBean.getFileType();

        if (UtilBean.isFileExists(path)) {
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
                Intent intent = new Intent(this, CustomImageViewerActivity_.class);
                intent.putExtra("path", path);
                intent.putExtra("fileName", libraryDataBean.getDescription());
                this.startActivity(intent);
            } else if (extension.equalsIgnoreCase("pdf")) {
                Intent intent = new Intent(this, CustomPDFViewerActivity_.class);
                intent.putExtra("path", path);
                intent.putExtra("fileName", libraryDataBean.getDescription());
                this.startActivity(intent);
            } else {
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(path));
                MediaPlayer player = MediaPlayer.create(this, uri);
                if (player != null) {
                    if ((extension.equalsIgnoreCase("3gp") || extension.equalsIgnoreCase("mp4"))) {
                        Intent intent = new Intent(this, CustomVideoPlayerActivity.class);
                        intent.putExtra(GlobalTypes.DATA_MAP, libraryDataBean.getActualId() + "." + libraryDataBean.getFileType());
                        intent.putExtra("isFromAnnouncement", Boolean.TRUE);
                        intent.putExtra("fileLocation", SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY));
                        this.startActivity(intent);
                    } else {
                        MyAudioPlayer myAudioPlayer = new MyAudioPlayer(this, libraryDataBean.getFileName(), player);
                        myAudioPlayer.show();
                    }
                } else if (!GlobalTypes.SUPPORTED_EXTENSIONS.contains(extension)) {
                    SewaUtil.generateToast(this, LabelConstants.FILE_IS_NOT_SUPPORTED);
                } else {
                    SewaUtil.generateToast(this, LabelConstants.FILE_CORRUPTED);
                }
            }
        } else {
            SewaUtil.generateToast(this, LabelConstants.FILE_DOES_NOT_EXIST);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
