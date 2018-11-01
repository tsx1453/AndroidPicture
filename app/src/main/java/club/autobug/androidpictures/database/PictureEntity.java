package club.autobug.androidpictures.database;

import java.util.Arrays;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import club.autobug.androidpictures.bean.AbsDataBean;

@Entity
public class PictureEntity extends AbsDataBean {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String pictureId;

    @ColumnInfo
    private String tags;

    public PictureEntity(String pictureId, String tags) {
        this.pictureId = pictureId;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Ignore
    public static List<String> getTagList(String tags) {
        return Arrays.asList(tags.split(","));
    }

    @Ignore
    public static String getTags(List<String> list) {
        if (list.size() < 1) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : list) {
            stringBuilder.append(i);
            stringBuilder.append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    @Override
    public String getOriginalImageUrl() {
        return null;
    }

    @Override
    public String getThumbnailUrl() {
        return "http://img5.adesk.com/" + getPictureId();
    }

    @Override
    public List<String> getPictureTags() {
        return getTagList(tags);
    }
}
