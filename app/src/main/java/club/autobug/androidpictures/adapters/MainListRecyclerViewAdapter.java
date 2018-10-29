package club.autobug.androidpictures.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import club.autobug.androidpictures.R;
import club.autobug.androidpictures.bean.AbsDataBean;
import club.autobug.androidpictures.mvp.MainPageView;
import club.autobug.androidpictures.utils.UtilClass;

public class MainListRecyclerViewAdapter<T extends AbsDataBean> extends RecyclerView.Adapter<MainListRecyclerViewAdapter.AppViewHolder> {

    private final String TAG = "RecyclerViewAdapterDev";

    private int itemWidth = 0;
    private Context mContext;
    private List<T> mList;
    private RequestOptions requestOptions;
    private RecyclerView mRecyclerView;
    private MainListItemClickListener mainListItemClickListener;

    public MainListRecyclerViewAdapter(Context mContext, int cols, RecyclerView recyclerView) {
        this.mContext = mContext;
        mRecyclerView = recyclerView;
        mList = new ArrayList<>();
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);
        itemWidth = (Resources.getSystem().getDisplayMetrics().widthPixels - UtilClass.dp2px(20)) / cols;

    }

    public void setMainListItemClickListener(MainListItemClickListener mainListItemClickListener) {
        this.mainListItemClickListener = mainListItemClickListener;
    }

    public void addData(List<T> list, boolean clear) {
        if (clear) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
        if (clear) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    @NonNull
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.picture_list_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull MainListRecyclerViewAdapter.AppViewHolder appViewHolder, final int i) {
        if (mList != null && mList.get(i) != null) {
            ViewGroup.LayoutParams layoutParams = appViewHolder.imageView.getLayoutParams();
            layoutParams.height = (int) (54f / 35f * itemWidth);
            appViewHolder.imageView.setLayoutParams(layoutParams);
            Glide.with(mContext)
                    .asBitmap()
                    .load(mList.get(i).getThumbnailUrl())
                    .apply(requestOptions)
                    .into(appViewHolder.imageView);
            appViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mainListItemClickListener != null) {
                        mainListItemClickListener.onCLicked(mList.get(i));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View clickView;

        AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            clickView = itemView;
        }
    }

    public interface MainListItemClickListener {
        void onCLicked(AbsDataBean absDataBean);
    }
}
