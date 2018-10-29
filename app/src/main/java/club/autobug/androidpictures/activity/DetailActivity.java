package club.autobug.androidpictures.activity;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

import club.autobug.androidpictures.R;
import club.autobug.androidpictures.utils.UtilClass;

public class DetailActivity extends AppCompatActivity {

    public static String DATA_ID = "DetailActivityDataId";

    private Toolbar toolbar;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        id = getIntent().getStringExtra(DATA_ID);
    }

    private void initView() {
        UtilClass.setFullScreen(getWindow());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
//        AppBarLayout appBarLayout = findViewById(R.id.AppBarLayout);
//        appBarLayout.setBackgroundColor(Color.TRANSPARENT);
//        appBarLayout.setBackground(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
