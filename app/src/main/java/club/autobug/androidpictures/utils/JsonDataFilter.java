package club.autobug.androidpictures.utils;

import android.content.Context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import club.autobug.androidpictures.bean.AbsDataBean;

public class JsonDataFilter<T extends AbsDataBean> {

    private List<T> mList;
    private Set<String> unLikeList;

    public JsonDataFilter(List<T> mList, Context context) {
        this.mList = mList;
        unLikeList = Preferences.getUnlike(context.getApplicationContext());
    }

    public void runFilter() {
        Iterator<T> iterator = mList.iterator();
        while (iterator.hasNext()) {
            if (check(iterator.next().getPictureTags())) {
                iterator.remove();
            }
        }
    }

    private boolean check(List<String> tags) {
        Set<String> set = new HashSet<>(unLikeList);
        set.retainAll(tags);
        return set.size() > 0;
    }
}
