package club.autobug.androidpictures.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

import androidx.room.RxRoom;
import club.autobug.androidpictures.R;
import club.autobug.androidpictures.mvp.DetailPresenter;
import club.autobug.androidpictures.mvp.DetailView;
import club.autobug.androidpictures.utils.UtilClass;
import io.reactivex.CompletableObserver;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import okhttp3.internal.Util;

public class DetailActivity extends AppCompatActivity implements DetailView {

    public String TAG = "DetailActivityDev";
    public static String DATA_ID = "DetailActivityDataId";
    public static String DATA_TAGS = "DetailActivityDataTags";

    private Toolbar toolbar;
    private String id;
    private String tags;
    private PhotoView mImageView;
    boolean showToolBar = true;
    private View actionFrame;
    private ImageView saveBtn;
    private ImageView favBtn;
    private TextView setWallPaperBtn;
    private ObjectAnimator objectAnimator;
    private DetailPresenter mDetailPresent;
    private boolean hasFaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        id = getIntent().getStringExtra(DATA_ID);
        tags = getIntent().getStringExtra(DATA_TAGS);
        Glide.with(this)
                .load("http://img5.adesk.com/" + id)
                .into(mImageView);
        mDetailPresent = new DetailPresenter(this, this, id);
//        Log.d(TAG, "DetailActivity->onCreate: " + getExternalFilesDir("Picture"));
    }

    private void initView() {
        UtilClass.setFullScreen(getWindow());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        actionFrame = findViewById(R.id.ActionFrame);
        mImageView = findViewById(R.id.imageView);
        objectAnimator = ObjectAnimator.ofFloat(actionFrame, "alpha", 1, 0);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (showToolBar) {
                    actionFrame.setVisibility(View.GONE);
                } else {
                    actionFrame.setVisibility(View.VISIBLE);
                }
                setFullScreen();
                showToolBar = !showToolBar;
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showToolBar) {
                    objectAnimator.setFloatValues(1, 0);
//                    actionFrame.setVisibility(View.GONE);
                } else {
//                    actionFrame.setVisibility(View.VISIBLE);
                    objectAnimator.setFloatValues(0, 1);
                }
                objectAnimator.start();
            }
        });
        toolbar.setPadding(0, UtilClass.getStatusBarHeight(), 0, 0);
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        layoutParams.height += UtilClass.getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        saveBtn = findViewById(R.id.saveButton);
        favBtn = findViewById(R.id.favButton);
        setWallPaperBtn = findViewById(R.id.setAsWallPaperBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilClass.checkExternalStoragePermission(DetailActivity.this)) {
                    mDetailPresent.save()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(DetailActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                    saveBtn.setClickable(false);
                }
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailPresent.fav(tags, !hasFaved)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "DetailActivity->onComplete: insert");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                hasFaved = !hasFaved;
                favBtn.setSelected(hasFaved);
            }
        });
        setWallPaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailPresent.setWallpaper();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveChecked(boolean hasSaved) {
        saveBtn.setClickable(hasSaved);
    }

    @Override
    public void favChecked(boolean hasFaved) {
//        Log.d(TAG, "DetailActivity->favChecked: " + hasFaved);
        favBtn.setSelected(hasFaved);
        this.hasFaved = hasFaved;
    }

    private void setFullScreen() {
        if (showToolBar) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }

}
