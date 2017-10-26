package com.smart.control.section;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author:dongpo 创建时间: 3016/9/3
 * 描述:
 * 修改:
 */
public class TextFragment3 extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_section3, container, false);
        return inflate;
    }

    @Override
    public void onClick(View v) {
    }
}
