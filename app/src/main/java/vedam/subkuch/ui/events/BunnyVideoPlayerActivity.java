package vedam.subkuch.ui.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

/** Plays an enrolled Learn lesson using Bunny Stream's native Android player. */
public class BunnyVideoPlayerActivity extends BaseActivity {

    private static final String EXTRA_VIDEO_URL = "extra_bunny_video_url";
    private static final String EXTRA_TITLE = "extra_lesson_title";

    public static Intent newIntent(Context context, String videoUrl, String title) {
        return new Intent(context, BunnyVideoPlayerActivity.class)
                .putExtra(EXTRA_VIDEO_URL, videoUrl)
                .putExtra(EXTRA_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunny_video_player);
        setToolbarBackButton();

        String videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        setTitle(TextUtils.isEmpty(title) ? getString(R.string.learn) : title);

        if (!isValidPlaybackUrl(videoUrl)) {
            showError(R.string.learn_video_unavailable);
            return;
        }

        WebView player = findViewById(R.id.bunny_video_player);
        WebSettings settings = player.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        player.setWebChromeClient(new WebChromeClient());
        player.loadUrl(videoUrl);
    }

    /** API topic responses use https://player.mediadelivery.net/play/{libraryId}/{videoId}. */
    private static boolean isValidPlaybackUrl(String value) {
        if (TextUtils.isEmpty(value)) return false;
        Uri uri = Uri.parse(value);
        if (!"https".equalsIgnoreCase(uri.getScheme())
                || !"player.mediadelivery.net".equalsIgnoreCase(uri.getHost())) {
            return false;
        }
        java.util.List<String> segments = uri.getPathSegments();
        for (int index = 0; index + 2 < segments.size(); index++) {
            if ("play".equalsIgnoreCase(segments.get(index))) {
                String libraryId = segments.get(index + 1);
                String videoId = segments.get(index + 2);
                if (!TextUtils.isEmpty(libraryId) && !TextUtils.isEmpty(videoId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showError(int stringRes) {
        findViewById(R.id.bunny_video_player).setVisibility(View.GONE);
        TextView error = findViewById(R.id.tv_bunny_player_error);
        error.setText(stringRes);
        error.setVisibility(View.VISIBLE);
        Toast.makeText(this, stringRes, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        WebView player = findViewById(R.id.bunny_video_player);
        if (player != null) {
            player.stopLoading();
            player.destroy();
        }
        super.onDestroy();
    }
}
