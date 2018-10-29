package club.autobug.androidpictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;


import club.autobug.androidpictures.activity.DetailActivity;
import club.autobug.androidpictures.bean.AbsDataBean;
import club.autobug.androidpictures.bean.NavHeaderBean;
import club.autobug.androidpictures.mvp.MainPagePresenter;
import club.autobug.androidpictures.mvp.MainPageView;
import club.autobug.androidpictures.adapters.MainListRecyclerViewAdapter;
import club.autobug.androidpictures.adapters.NavHeaderRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainPageView<AbsDataBean>,
        NavHeaderRecyclerViewAdapter.ItemClickListener, MainListRecyclerViewAdapter.MainListItemClickListener {

    private final String TAG = "MainActivityDev";
    protected RecyclerView mRecyclerView;
    protected RecyclerView mHeaderRecyclerView;
    private MainListRecyclerViewAdapter<AbsDataBean> mAdapter;
    private NavHeaderRecyclerViewAdapter mHeaderAdapter;
    protected MainPagePresenter<AbsDataBean> mPresenter;
    private boolean mixedData = true;
    private boolean isGoUp = true;
    private FloatingActionButton mFab;
    private boolean isHot = true;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MainListRecyclerViewAdapter<>(this, 3, mRecyclerView);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new MainPagePresenter<>(this);
        mPresenter.loadMainListMixedData(30, 0, isHot, false);

        mHeaderRecyclerView = navigationView.getHeaderView(0).findViewById(R.id.headerRecyclerView);
        mHeaderAdapter = new NavHeaderRecyclerViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mHeaderRecyclerView.setLayoutManager(layoutManager);
        mHeaderRecyclerView.setAdapter(mHeaderAdapter);
        mHeaderAdapter.setmListener(this);

        mPresenter.loadHeaderData();

        mFab = findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isGoUp = mRecyclerView.canScrollVertically(-1);
                changeFabIcon();
                if (mixedData && !isGoUp) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGoUp) {
                    mRecyclerView.scrollToPosition(0);
                } else {
                    mPresenter.loadMainListMixedData(30, 0, true, true);
                    mixedData = true;
                    setTitle("推荐");
                    mFab.hide();
                }
            }
        });
        mFab.hide();
        mAdapter.setMainListItemClickListener(this);
    }

    private void changeFabIcon() {
        mFab.hide();
        if (isGoUp) {
            mFab.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_home_black_24dp);
        }
        mFab.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_sort) {
            isHot = !isHot;
            if (isHot) {
                Toast.makeText(this, "当前加载热度最高图片", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "当前加载最新图片", Toast.LENGTH_SHORT).show();
            }
            reloadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadData() {
        if (mixedData) {
            mPresenter.loadMainListMixedData(30, 0, isHot, true);
        } else {
            mPresenter.loadMainListTypedData(id, 30, 0, isHot, true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startLoadData(int num, int skip) {

    }

    @Override
    public void onLoadCompleted(List<AbsDataBean> bean, boolean clear) {
        if (mAdapter != null) {
            mAdapter.addData(bean, clear);
        }
    }

    @Override
    public void onHeaderDataLoadCompleted(List<NavHeaderBean.ResBean.CategoryBean> list) {
        if (mHeaderAdapter != null) {
            mHeaderAdapter.setmList(list);
        }
    }

    @Override
    public void onItemClick(String id, String name) {
        mPresenter.loadMainListTypedData(id, 30, 0, isHot, true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        this.id = id;
        mixedData = false;
        setTitle(name);
        mFab.show();
    }

    @Override
    public void onCLicked(AbsDataBean absDataBean) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.DATA_ID, absDataBean.getId());
        startActivity(intent);
    }
}
