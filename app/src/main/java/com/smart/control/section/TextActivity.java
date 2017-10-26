package com.smart.control.section;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:dongpo 创建时间: 9/2/2016
 * 描述:
 * 修改:
 */
public class TextActivity extends SectionActivity {

    @Bind(R.id.section_add)
    Button mSectionAdd;
    @Bind(R.id.section_remove)
    Button mSectionRemove;
    @Bind(R.id.section_replace)
    Button mSectionReplace;
    @Bind(R.id.section_show)
    Button mSectionShow;
    @Bind(R.id.section_hide)
    Button mSectionHide;
    private Section mSection;
    private int mCount;
    private TextSection mSection1;

    @Override
    protected void onActivityCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_section);
        ButterKnife.bind(this);
        Log.d("section", "TextActivity-onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextActivity-onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextActivity-onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextActivity-onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextActivity-onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextActivity-onPause");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("section", "TextActivity-onRestart");
    }


    @OnClick({R.id.section_add, R.id.section_remove, R.id.section_replace, R.id.section_hide, R.id.section_show})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.section_add:
                if (mCount % 2 == 0) {
                    mSection = new TextSection(this);
                } else {
                    mSection = new TextSection2(this);
                }
                mCount++;
                mSection.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out, R.anim.activity_left_out, R.anim.activity_right_in);
                getSectionManager().add(R.id.single_fl_container, mSection, true);
                break;
            case R.id.section_remove:
                getSectionManager().remove(R.id.single_fl_container, mSection);
                break;
            case R.id.section_replace:
                getSectionManager().replace(R.id.single_fl_container, new TextSection3(this), false);
                break;
            case R.id.section_hide:
                getSectionManager().hide(mSection);
                break;
            case R.id.section_show:
                getSectionManager().show(mSection);
                break;
        }
    }

    @OnClick({R.id.section_add2, R.id.section_remove2, R.id.section_replace2, R.id.section_show2, R.id.section_hide2})
    public void onClick2(View view) {
        switch (view.getId()) {
            case R.id.section_add2:
                mSection1 = new TextSection(this);
                mSection1.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out, R.anim.activity_left_out, R.anim.activity_right_in);
                getSectionManager().add(R.id.single_fl_container_bottom, mSection1, false);
                break;
            case R.id.section_remove2:
                getSectionManager().remove(R.id.single_fl_container_bottom, mSection1);
                break;
            case R.id.section_replace2:
                getSectionManager().replace(R.id.single_fl_container_bottom, new TextSection3(this), false);
                break;
            case R.id.section_show2:
                getSectionManager().show(mSection1, true);
                break;
            case R.id.section_hide2:
                getSectionManager().hide(mSection1);
                break;
        }
    }
}
