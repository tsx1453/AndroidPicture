package club.autobug.androidpictures.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import androidx.core.content.ContextCompat;
import club.autobug.androidpictures.R;
import club.autobug.androidpictures.database.PictureEntity;
import club.autobug.androidpictures.utils.Preferences;
import club.autobug.androidpictures.view.TagLayout;

public class UnlikeManageActivity extends AppCompatActivity {

    private List<String> tagList;
    private List<String> deleteList;
    private TagLayout tagLayout;
    private FloatingActionButton fab;
    boolean flagAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlike_manage);
        init();
    }

    private void init() {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tagList = new ArrayList<>(Preferences.getUnlike(this));
        tagList.remove("");
        deleteList = new ArrayList<>();
        tagLayout = findViewById(R.id.tagLayout);
        tagLayout.addTags(tagList);
        tagLayout.setOnTagItemClickListener(new TagLayout.onTagItemClickListener() {
            @Override
            public void onClick(List<String> selectedList) {
                if (selectedList.size() > 0) {
                    flagAdd = false;
                } else {
                    flagAdd = true;
                }
                deleteList.clear();
                deleteList.addAll(selectedList);
                changeIcon();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (flagAdd) {
                    final EditText editText = new EditText(UnlikeManageActivity.this);
                    editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    new AlertDialog.Builder(UnlikeManageActivity.this)
                            .setTitle("添加标签")
                            .setView(editText)
                            .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String t = editText.getText().toString();
                                    if (t.length() > 0) {
                                        tagList.add(t);
                                        tagLayout.addTags(Collections.singletonList(t));
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    tagLayout.deleteTags(deleteList);
                    for (String i : deleteList) {
                        tagList.remove(i);
                    }
                    deleteList.clear();
                    flagAdd = true;
                    changeIcon();
                }
            }
        });

    }

    private void changeIcon() {
        if (flagAdd) {
            fab.setImageResource(R.drawable.ic_add_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_delete_black_24dp);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Preferences.setUnlike(this, PictureEntity.getTags(tagList));
    }

}
