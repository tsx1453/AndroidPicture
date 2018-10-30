package club.autobug.androidpictures.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PictureEntity.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract PictureDao pictureDao();

    private static volatile AppDataBase Instance;

    public static AppDataBase getInstance(Context context) {
        if (Instance == null) {
            synchronized (AppDataBase.class) {
                if (Instance == null) {
                    Instance = Room.databaseBuilder(context.getApplicationContext()
                            , AppDataBase.class, "PictureFav.db")
                            .build();
                }
            }
        }
        return Instance;
    }

}
