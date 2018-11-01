package club.autobug.androidpictures.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import club.autobug.androidpictures.database.PictureEntity;
import club.autobug.androidpictures.utils.JsonDataFilter;
import club.autobug.androidpictures.utils.Preferences;
import club.autobug.androidpictures.utils.UtilClass;
import club.autobug.androidpictures.view.TagLayout;

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
            notifyDataSetChanged();
        }
        mList.addAll(list);
        new JsonDataFilter<T>(mList, mContext).runFilter();
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
            appViewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (i >= mList.size()) {
                        return false;
                    }
                    final TagLayout tagLayout = new TagLayout(mContext, mList.get(i).getPictureTags());
                    tagLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    tagLayout.addTags(mList.get(i).getPictureTags());
//                    tagLayout.requestLayout();
//                    tagLayout.setBackgroundColor(Color.RED);
//                    Log.d(TAG, "MainListRecyclerViewAdapter->onLongClick: "+tagLayout.getHeight());
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("选择不想再看到的标签")
                            .setView(tagLayout)
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Preferences.saveUnlike(mContext, PictureEntity.getTags(tagLayout.getSelectedTags()));
                                    mList.remove(i);
                                    notifyItemRemoved(i);
                                    mRecyclerView.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    }, 500);
                                }
                            })
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    tagLayout.destory();
                                }
                            })
                            .setCancelable(true).create();
                    tagLayout.requestLayout();
                    dialog.show();
                    return true;
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
