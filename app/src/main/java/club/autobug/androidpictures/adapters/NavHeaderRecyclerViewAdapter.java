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

public class NavHeaderRecyclerViewAdapter extends RecyclerView.Adapter<NavHeaderRecyclerViewAdapter.NavViewHolder> {

    private String TAG = "NavHeaderRecyclerViewAdapterDev";

    private Context mContext;
    private List<NavHeaderBean.ResBean.CategoryBean> mList;
    private ItemClickListener mListener;

    public NavHeaderRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<NavHeaderBean.ResBean.CategoryBean> mList) {
        this.mList.addAll(mList);
        this.notifyDataSetChanged();
//        Log.d(TAG, "NavHeaderRecyclerViewAdapter->setmList: " + mList.size());
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        Log.d(TAG, "NavHeaderRecyclerViewAdapter->onCreateViewHolder");
        return new NavViewHolder(LayoutInflater.from(mContext).inflate(R.layout.header_class_list_item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder navViewHolder, int i) {
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

    static class NavViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        TextView title;

        NavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.headerText);
            background = itemView.findViewById(R.id.headerBackImg);
        }
    }

    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onItemClick(String id, String name);
    }

}
