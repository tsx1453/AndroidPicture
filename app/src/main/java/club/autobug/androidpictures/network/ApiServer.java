package club.autobug.androidpictures.network;

import java.util.Map;

import club.autobug.androidpictures.bean.MainListDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiServer {

    @GET("vertical")
    Observable<MainListDataBean> getMixedData(@QueryMap Map<String, String> map);

    @GET("http://service.picasso.adesk.com/v1/vertical/category?adult=false&first=1")
    Observable<NavHeaderBean> getHeaderData();

    @GET("http://service.picasso.adesk.com/v1/vertical/category/{id}/vertical")
    Observable<MainListDataBean> getTypedData(@Path("id") String id, @QueryMap Map<String, String> map);

}
