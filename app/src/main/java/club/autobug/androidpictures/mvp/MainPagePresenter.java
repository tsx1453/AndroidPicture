package club.autobug.androidpictures.mvp;

import android.util.Log;

import java.util.List;

import club.autobug.androidpictures.bean.AbsDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;

public class MainPagePresenter<T extends AbsDataBean> {

    private String TAG = "MainPagePresenterDev";

    private MainPageView<T> view;
    private MainPageDataModel model;

    public MainPagePresenter(MainPageView<T> view) {
        this.view = view;
        model = new MainPageDataModel();
    }

    public void loadMainListMixedData(int limit, int skip, boolean hotOrNew, final boolean clear) {
        model.getMainListMixedData(new MainPageDataModel.LoadListener<T>() {
            @Override
            public void onLoaded(List<T> list) {
                view.onLoadCompleted(list, clear);
            }
        }, limit, skip, hotOrNew);
    }

    public void loadHeaderData() {
        model.getHeaderData(new MainPageDataModel.LoadListener<NavHeaderBean.ResBean.CategoryBean>() {
            @Override
            public void onLoaded(List<NavHeaderBean.ResBean.CategoryBean> bean) {
//                Log.d(TAG, "MainPagePresenter->onLoaded: " + bean.size());
                view.onHeaderDataLoadCompleted(bean);
            }
        });
    }

    public void loadMainListTypedData(String id, int limit, int skip, boolean hotOrNew, final boolean clear) {
        model.getMainListTypedData(new MainPageDataModel.LoadListener<T>() {
            @Override
            public void onLoaded(List<T> list) {
                view.onLoadCompleted(list, clear);
            }
        }, id, limit, skip, hotOrNew);
    }

    public void loadFavData() {
        model.getFavData(new MainPageDataModel.LoadListener<T>() {
            @Override
            public void onLoaded(List<T> bean) {
                Log.d(TAG, "MainPagePresenter->onLoaded: " + bean.size());
                view.onLoadCompleted(bean, true);
            }
        });
    }


}
