package com.doohaa.chat.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.doohaa.chat.R;
import com.doohaa.chat.ui.article.ArticleListActivity;
import com.doohaa.chat.ui.article.ArticlePostActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.group.GroupDetailActivity;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * Created by sunshixiong on 5/11/16.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private WalletAdapter walletAdapter;
    private EaseTitleBar easeTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_fragment_wallet);
        initComponent();
        registEvent();
    }

    private void initComponent() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.wallet_tab);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);

        walletAdapter = new WalletAdapter(getSupportFragmentManager());
        viewPager.setAdapter(walletAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        setUpTablayout();
    }

    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
    }

    private void setUpTablayout() {
        tabLayout.getTabAt(0).setCustomView(R.layout.wallet_tab_layout).setText(R.string.wallet_tab_bulletin);
        tabLayout.getTabAt(1).setCustomView(R.layout.wallet_tab_layout).setText(R.string.wallet_tab_deal).select();
        tabLayout.getTabAt(2).setCustomView(R.layout.wallet_tab_layout).setText(R.string.wallet_tab_asset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }

    private class WalletAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEM = 3;

        public WalletAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BulletinFragment.newInstance();
                case 1:
                    return DealFragment.newInstance();
                case 2:
                    return AssetFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEM;
        }
    }
}
