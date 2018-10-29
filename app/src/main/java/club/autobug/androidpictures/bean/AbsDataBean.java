package club.autobug.androidpictures.bean;

import java.util.List;

abstract public class AbsDataBean {

    public abstract String getOriginalImageUrl();

    public abstract String getThumbnailUrl();

    public abstract List<String> getTags();

    public abstract String getId();

}
