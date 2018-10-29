package club.autobug.androidpictures.mvp;

import java.util.List;

import club.autobug.androidpictures.bean.AbsDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;

public interface MainPageView<T extends AbsDataBean> {

    void startLoadData(int num, int skip);

    void onLoadCompleted(List<T> bean,boolean clear);

    void onHeaderDataLoadCompleted(List<NavHeaderBean.ResBean.CategoryBean> list);

}
