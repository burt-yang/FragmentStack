package com.smart.control.section;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author:dongpo 创建时间: 3016/9/3
 * 描述:
 * 修改:
 */
public class TextSection3 extends Section implements View.OnClickListener {

    public TextSection3(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("section", "TextSection3+onCreateView3_"+this);
        View inflate = inflater.inflate(R.layout.fragment_section3, container, false);
        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("section", "TextSection3+onAttach3_"+this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextSection3+onResume3_"+this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextSection3+onStart3_"+this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextSection3+onStop3_"+this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("section", "TextSection3+onDetch3_"+this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextSection3+onDestroy3_"+this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextSection3+onPause3_"+this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("section", "TextSection3+onRestart3_"+this);
    }

    @Override
    public void onClick(View v) {

        mAttachActivity.startActivity(new Intent(mAttachActivity, TextActivity.class));

    }
}
