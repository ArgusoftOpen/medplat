package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.EActivity;

@EActivity
public class AboutActivity extends MenuActivity {

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.ABOUT_TITLE));
        setSubTitle(null);

        String versionName = LabelConstants.VERSION + " : " + BuildConfig.VERSION_NAME;
        TextView version = findViewById(R.id.version);
        version.setText(versionName);

        ImageView ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setImageResource(R.drawable.medplat_argus);

        String releaseDate = LabelConstants.RELEASE_DATE + " : " + BuildConfig.RELEASE_DATE;
        TextView release = findViewById(R.id.releaseDate);
        release.setText(releaseDate);

        TextView link = findViewById(R.id.link);
        if (Boolean.TRUE.equals(SewaUtil.isUserInTraining)) {
            link.setText(BuildConfig.BASE_URL_TRAINING);
        } else {
            link.setText(BuildConfig.BASE_URL);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            navigateToHomeScreen(false);
        }
        return true;
    }

    private void openPhone(String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile, null));
        startActivity(intent);
    }

    public void composeEmail(String[] addresses) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Spannable colorized(final String text, final String word, final int argb) {
        final Spannable spannable = new SpannableString(text);
        int substringStart = 0;
        int start;
        while ((start = text.indexOf(word, substringStart)) >= 0) {
            spannable.setSpan(
                    new ForegroundColorSpan(argb), start, start + word.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            substringStart = start + word.length();
        }
        return spannable;
    }
}