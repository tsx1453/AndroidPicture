package club.autobug.androidpictures.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface PictureDao {

    @Query("select * from pictureentity")
    Maybe<List<PictureEntity>> getAllFavs();

    @Insert
    void insert(PictureEntity... pictureEntities);

    @Delete
    void delete(PictureEntity pictureEntity);

    @Query("delete from pictureentity")
    void deleteAll();

    @Query("delete from pictureentity where pictureId = :id")
    void deleteById(String id);
}
