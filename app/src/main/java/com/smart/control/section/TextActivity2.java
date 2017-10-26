package com.smart.control.section;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * @author:dongpo 创建时间: 2016/9/3
 * 描述:
 * 修改:
 */
public class TextActivity2 extends SectionActivity {
    @Override
    protected void onActivityCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_section2);
        Log.d("section", "TextActivity2-onCreate");
        getSectionManager().add(R.id.single_fl_container, new TextSection2(this), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextActivity2-onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextActivity2-onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextActivity2-onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextActivity2-onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextActivity2-onPause");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("section", "TextActivity2-onRestart");
    }


}
