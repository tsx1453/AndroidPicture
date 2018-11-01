package club.autobug.androidpictures.mvp;

import java.util.List;

import club.autobug.androidpictures.AppApplication;
import club.autobug.androidpictures.bean.MainListDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;
import club.autobug.androidpictures.database.AppDataBase;
import club.autobug.androidpictures.database.PictureEntity;
import club.autobug.androidpictures.network.NetWorkManager;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPageDataModel {

    void getMainListMixedData(final LoadListener listener, int count, int skip, boolean hotOrNew) {
        NetWorkManager.getInstance().getMainListMixedData(count, 0, skip, hotOrNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainListDataBean>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(MainListDataBean bean) {
                        if (listener != null) {
                            if (bean != null && bean.getRes() != null &&
                                    bean.getRes().getVertical().size() > 0 &&
                                    bean.getRes().getVertical().get(0) != null) {
                                listener.onLoaded(bean.getRes().getVertical());
                            }
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

    void getHeaderData(final LoadListener listener) {
        NetWorkManager.getInstance().getHeaderData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NavHeaderBean>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(NavHeaderBean navHeaderBean) {
                        if (listener != null) {
                            if (navHeaderBean != null && navHeaderBean.getRes() != null &&
                                    navHeaderBean.getRes().getCategory().size() > 0 &&
                                    navHeaderBean.getRes().getCategory().get(0) != null) {
                                listener.onLoaded(navHeaderBean.getRes().getCategory());
                            }
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

    void getMainListTypedData(final LoadListener listener, String id, int count, int skip, boolean hotOrNew) {
        NetWorkManager.getInstance().getMainListTypedData(
                id, count, 0, skip, hotOrNew
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainListDataBean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(MainListDataBean mainListDataBean) {
                        if (listener != null) {
                            if (mainListDataBean != null && mainListDataBean.getRes() != null &&
                                    mainListDataBean.getRes().getVertical().size() > 0 &&
                                    mainListDataBean.getRes().getVertical().get(0) != null) {
                                listener.onLoaded(mainListDataBean.getRes().getVertical());
                            }
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

    void getFavData(final LoadListener listener) {
        AppDataBase.getInstance(AppApplication.applicationContext)
                .pictureDao().getAllFavs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<PictureEntity>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<PictureEntity> pictureEntities) {
                        if (listener != null) {
                            listener.onLoaded(pictureEntities);
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

    interface LoadListener<T> {
        void onLoaded(List<T> bean);
    }

}
