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
 * @author:dongpo 创建时间: 2016/9/3
 * 描述:
 * 修改:
 */
public class TextSection2 extends Section implements View.OnClickListener {

    public TextSection2(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("section", "TextSection2+onCreateView2_"+this);
        View inflate = inflater.inflate(R.layout.fragment_section2, container, false);
        inflate.findViewById(R.id.section_start_activity).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("section", "TextSection2+onAttach2_"+this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextSection2+onResume2_"+this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextSection2+onStart2_"+this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextSection2+onStop2_"+this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("section", "TextSection2+onDetch2_"+this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextSection2+onDestroy2_"+this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextSection2+onPause2_"+this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("section", "TextSection2+onRestart2_"+this);
    }

    @Override
    public void onClick(View v) {

        mAttachActivity.startActivity(new Intent(mAttachActivity, TextActivity.class));

    }
}
