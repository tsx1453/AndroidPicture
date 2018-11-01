package club.autobug.androidpictures.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import club.autobug.androidpictures.database.PictureEntity;

public class Preferences {

    private static final String SP_NAME = "autobug.sp";
    private static final String SP_UNLIKE = "autobug.sp.unlike";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSP(context).edit();
    }

    public static void saveUnlike(Context context, String tags) {
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<String> set = new HashSet<>(PictureEntity.getTagList(tags));
        set.addAll(getUnlike(context));
        for (String i : set) {
            stringBuilder.append(i);
            stringBuilder.append(",");
        }
        getEditor(context).putString(SP_UNLIKE, stringBuilder.toString()).apply();
    }

    public static void setUnlike(Context context, String tags) {
        getEditor(context).putString(SP_UNLIKE, tags).apply();
    }

    public static Set<String> getUnlike(Context context) {
        return new HashSet<>(PictureEntity.getTagList(getSP(context).getString(SP_UNLIKE, "")));
    }

}
