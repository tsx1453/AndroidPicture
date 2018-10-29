package club.autobug.androidpictures.network;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import club.autobug.androidpictures.bean.MainListDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkManager {
    private final String HOST = "http://service.picasso.adesk.com/v1/vertical/";
    private final String TAG = "RetrofitManagerDev";
    private volatile static NetWorkManager ourInstance;

    public static NetWorkManager getInstance() {
        if (ourInstance == null) {
            synchronized (NetWorkManager.class) {
                if (ourInstance == null) {
                    ourInstance = new NetWorkManager();
                }
            }
        }
        return ourInstance;
    }


    private Retrofit retrofit;
    private ApiServer apiServer;

    private NetWorkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.d(TAG, "NetWorkManager->intercept: " + request.url().toString());
                Response response = chain.proceed(request);
                MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, content))
                        .build();
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }

    public Observable<MainListDataBean> getMainListMixedData(int limit, int first, int skip, boolean hotOrnew) {
        Map<String, String> prams = new HashMap<>();
        prams.put("limit", String.valueOf(limit));
        prams.put("adult", "false");
        prams.put("first", String.valueOf(first));
        prams.put("skip", String.valueOf(skip));
        prams.put("order", hotOrnew ? "hot" : "new");
        return apiServer.getMixedData(prams);
    }

    public Observable<NavHeaderBean> getHeaderData() {
        return apiServer.getHeaderData();
    }

    public Observable<MainListDataBean> getMainListTypedData(String id, int limit, int first, int skip, boolean hotOrnew) {
        Map<String, String> prams = new HashMap<>();
        prams.put("limit", String.valueOf(limit));
        prams.put("adult", "false");
        prams.put("first", String.valueOf(first));
        prams.put("skip", String.valueOf(skip));
        prams.put("order", hotOrnew ? "hot" : "new");
        return apiServer.getTypedData(id, prams);
    }
}
