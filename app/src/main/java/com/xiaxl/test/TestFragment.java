package com.xiaxl.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaxveliang on 2017/11/9.
 * <p>
 * 专辑详情
 * <p>
 * 为什么做成RecycleView?????
 * <p>
 * 为了规避这个问题：
 * http://blog.csdn.net/bigggfish/article/details/53585783
 */
public class TestFragment extends Fragment {


    public static TestFragment newInstance() {
        //
        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        //
        return fragment;
    }


    private View mRootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.test_frag, container, false);

        return mRootView;
    }


}
