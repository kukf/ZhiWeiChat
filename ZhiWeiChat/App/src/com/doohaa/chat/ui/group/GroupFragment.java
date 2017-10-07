package com.doohaa.chat.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.doohaa.chat.Constant;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GroupFragment extends Fragment implements OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View rootView;
    private GroupAdapter groupAdapter;
    private RelativeLayout rlytNewMsg;
    private RelativeLayout rlytAdd;
    private List<Group> groups;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.em_fragment_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;

        initComponent();
        registEvent();
        fillData();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /******
     * EVENT BUS
     ******/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
        }
    }

    private void initComponent() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.group_tab);
        rlytNewMsg = (RelativeLayout) rootView.findViewById(R.id.left_layout);
        rlytAdd = (RelativeLayout) rootView.findViewById(R.id.right_layout);

        groupAdapter = new GroupAdapter(getFragmentManager());
        viewPager.setAdapter(groupAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        setUpTablayout();
    }

    private void registEvent() {
        rlytNewMsg.setOnClickListener(this);
        rlytAdd.setOnClickListener(this);
    }

    private void setUpTablayout() {
        tabLayout.getTabAt(0).setCustomView(R.layout.wallet_tab_layout).setText(R.string.topic_group);
        tabLayout.getTabAt(1).setCustomView(R.layout.wallet_tab_layout).setText(R.string.my_group).select();
        tabLayout.getTabAt(2).setCustomView(R.layout.wallet_tab_layout).setText(R.string.discover);
    }

    private void fillData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                Intent intent = new Intent(getActivity(), NewMsgAcativity.class);
                startActivity(intent);
                break;

            case R.id.right_layout:
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    private class GroupAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEM = 3;

        public GroupAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TopicFragment.newInstance();
                case 1:
                    return MyGroupFragment.newInstance();
                case 2:
                    return DiscoverFragment.newInstance();
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
