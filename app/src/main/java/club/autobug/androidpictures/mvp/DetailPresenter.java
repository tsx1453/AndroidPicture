package club.autobug.androidpictures.mvp;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import club.autobug.androidpictures.database.AppDataBase;
import club.autobug.androidpictures.database.PictureEntity;
import io.reactivex.Completable;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DetailPresenter {

    private String TAG = "DetailPresenterDev";

    private DetailView mDetailView;
    private Context mContext;
    private String filePath = "Pictures";
    private String id;

    public DetailPresenter(DetailView mDetailView, Context context, String id) {
        this.mDetailView = mDetailView;
        this.id = id;
        mContext = context.getApplicationContext();
        checkFaved();
        checkSaved();
    }

    private void checkSaved() {
        //todo permission
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                boolean result = false;
                File rootFile = mContext.getExternalFilesDir(filePath);
                if (rootFile != null) {
                    File[] savedFiles = rootFile.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().endsWith(".png");
                        }
                    });
                    if (savedFiles != null) {
                        for (File f : savedFiles) {
                            if (TextUtils.equals(f.getName().replace(".png", ""), id)) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mDetailView.saveChecked(!aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    private void checkFaved() {
//        Log.d(TAG, "DetailPresenter->checkFaved: start");
        AppDataBase.getInstance(mContext).pictureDao()
                .getAllFavs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<PictureEntity>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<PictureEntity> pictureEntities) {
                        boolean result = false;
//                        Log.d(TAG, "DetailPresenter->onNext: " + pictureEntities.size());
                        for (PictureEntity entity : pictureEntities) {
//                            Log.d(TAG, "DetailPresenter->onSuccess: " + entity.getPictureId() + "->" + id);
                            if (TextUtils.equals(entity.getPictureId(), id)) {
                                result = true;
                                break;
                            }
                        }
                        Log.d(TAG, "DetailPresenter->favCheck " + result);
                        mDetailView.favChecked(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    public Completable save() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                FutureTarget<Bitmap> futureTarget = Glide.with(mContext)
                        .asBitmap()
                        .load("http://img5.adesk.com/" + id)
                        .submit();
                Bitmap bitmap = futureTarget.get();
                File dirPath = mContext.getExternalFilesDir(filePath);
                if (dirPath != null) {
                    File saveFile = new File(dirPath.getAbsolutePath() + File.separator + id + ".png");
                    if (!saveFile.getParentFile().exists()) {
                        saveFile.getParentFile().mkdirs();
                        saveFile.createNewFile();
                    } else {
                        saveFile.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                Log.d(TAG, "DetailPresenter->run: save");
            }
        }).subscribeOn(Schedulers.io());
    }

    public Completable fav(final String tags, final boolean faved) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (faved) {
                    AppDataBase.getInstance(mContext).pictureDao()
                            .insert(new PictureEntity(id, tags));
                } else {
                    AppDataBase.getInstance(mContext).pictureDao()
                            .deleteById(id);
                }
//                Log.d(TAG, "DetailPresenter->run: inserted");
            }
        }).subscribeOn(Schedulers.io());
    }

    public void setWallpaper() {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                FutureTarget<Bitmap> futureTarget = Glide.with(mContext.getApplicationContext())
                        .asBitmap()
                        .load("http://img5.adesk.com/" + id)
                        .submit();
                emitter.onNext(futureTarget.get());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Bitmap drawable) {
                        try {
                            WallpaperManager.getInstance(mContext).setBitmap(drawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

}
