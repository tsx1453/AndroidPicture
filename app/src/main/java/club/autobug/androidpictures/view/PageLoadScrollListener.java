package club.autobug.androidpictures.view;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PageLoadScrollListener extends RecyclerView.OnScrollListener {

    //声明一个LinearLayoutManager
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    //当前页，从0开始
    private int currentPage = 0;

    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    public PageLoadScrollListener(StaggeredGridLayoutManager linearLayoutManager) {
        this.mStaggeredGridLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];
        if (loading) {

            if (totalItemCount > previousTotal) {
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //这里需要好好理解
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public void reset(){
        loading = true;
        currentPage = 0;
        previousTotal = 0;
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    public abstract void onLoadMore(int currentPage);

}
