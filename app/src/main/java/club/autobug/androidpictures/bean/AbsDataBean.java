package club.autobug.androidpictures.bean;

import java.util.List;

abstract public class AbsDataBean {

    public abstract String getOriginalImageUrl();

    public abstract String getThumbnailUrl();

    public abstract List<String> getPictureTags();

    public abstract String getPictureId();

}
