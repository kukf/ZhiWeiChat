package com.doohaa.chat.ui.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doohaa.chat.R;
import com.doohaa.chat.ui.common.BaseFragment;


public class FragmentImage extends BaseFragment {
    public static FragmentImage newInstance() {
        return new FragmentImage();
    }

    private View rootView;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.em_fragment_image, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();
    }

    private void initComponent() {
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
    }
}
