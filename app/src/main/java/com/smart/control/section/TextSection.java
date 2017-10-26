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
 * @author:dongpo 创建时间: 9/2/2016
 * 描述:
 * 修改:
 */
public class TextSection extends Section implements View.OnClickListener {
    public TextSection(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("section", "TextSection+onCreateView_" + this);
        View inflate = inflater.inflate(R.layout.fragment_section, container, false);
        inflate.findViewById(R.id.section_start_activity).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("section", "TextSection+onAttach_" + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("section", "TextSection+onResume_" + this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("section", "TextSection+onStart_" + this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("section", "TextSection+onStop_" + this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("section", "TextSection+onDetach_" + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("section", "TextSection+onDestroy_" + this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("section", "TextSection+onPause_" + this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("section", "TextSection+onRestart_" + this);
    }

    @Override
    public void onClick(View v) {

        mAttachActivity.startActivity(new Intent(mAttachActivity, TextActivity2.class));

    }

    @Override
    public void onShow() {
        super.onShow();
        Log.d("section", "TextSection+onShow_" + this);
    }

    @Override
    public void onHide() {
        super.onHide();
        Log.d("section", "TextSection+onHide_" + this);
    }
}
