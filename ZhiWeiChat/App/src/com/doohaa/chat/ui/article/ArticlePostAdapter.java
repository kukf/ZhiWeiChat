package com.doohaa.chat.ui.article;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ArticlePostAdapter extends FragmentPagerAdapter {


    public ArticlePostAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentImage.newInstance();
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.4f;
    }
}
