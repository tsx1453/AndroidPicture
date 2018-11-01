package club.autobug.androidpictures.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import club.autobug.androidpictures.R;
import club.autobug.androidpictures.bean.NavHeaderBean;

public class NavHeaderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "NavHeaderRecyclerViewAdapterDev";
    private int TYPE_NORMAL = 1;
    private int TYPE_HEADER = 2;

    private Context mContext;
    private List<NavHeaderBean.ResBean.CategoryBean> mList;
    private ItemClickListener mListener;

    public NavHeaderRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        mList.add(null);
    }

    public void setmList(List<NavHeaderBean.ResBean.CategoryBean> mList) {
        this.mList.addAll(mList);
        this.notifyDataSetChanged();
//        Log.d(TAG, "NavHeaderRecyclerViewAdapter->setmList: " + mList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        Log.d(TAG, "NavHeaderRecyclerViewAdapter->onCreateViewHolder");
        if (i == TYPE_NORMAL) {
            return new NavViewHolder(LayoutInflater.from(mContext).inflate(R.layout.header_class_list_item_layout, viewGroup, false));
        } else {
            return new HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.header_class_list_header_layout, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            onBindNorMalViewHolder((NavViewHolder) holder, position);
        } else {
            onBindHeaderViewHolder((HeaderViewHolder) holder);
        }
    }

    private void onBindHeaderViewHolder(HeaderViewHolder holder) {
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onHeaderClick(false);
                }
            }
        });
        holder.downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onHeaderClick(true);
                }
            }
        });
    }

    private void onBindNorMalViewHolder(NavViewHolder navViewHolder, int i) {
        Glide.with(mContext)
                .load(mList.get(i).getCover())
                .into(navViewHolder.background);
        navViewHolder.title.setText(mList.get(i).getName());
        final NavHeaderBean.ResBean.CategoryBean bean = mList.get(i);
//        Log.d(TAG, "NavHeaderRecyclerViewAdapter->onBindViewHolder: " + navViewHolder.title.getText().toString());
        navViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(bean.getId(), bean.getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) == null) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    static class NavViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        TextView title;

        NavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.headerText);
            background = itemView.findViewById(R.id.headerBackImg);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView downBtn;
        ImageView favBtn;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            downBtn = itemView.findViewById(R.id.downloadBtn);
            favBtn = itemView.findViewById(R.id.favBtn);
        }
    }

    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onItemClick(String id, String name);

        void onHeaderClick(boolean isDownload);
    }

}
