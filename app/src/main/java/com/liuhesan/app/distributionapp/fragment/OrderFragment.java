package com.liuhesan.app.distributionapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;

import java.util.ArrayList;
import java.util.List;

import shanyao.tabpagerindictor.TabPageIndicator;

/**
 * Created by Tao on 2016/12/10.
 */

public class OrderFragment extends Fragment {
    private View view;
    private Toolbar mToolbar;
    private TabPageIndicator indicator;
    private ViewPager viewPager;
    private BasePagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        initView();
        return view;
    }
    private void initView() {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.order_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "扫描", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new BasePagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        setTabPagerIndicator();
    }

    private void setTabPagerIndicator() {
        indicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
        indicator.setIndicatorColor(Color.parseColor("#28AAE3"));// 设置底部导航线的颜色
        indicator.setDividerColor(Color.parseColor("#ffffff"));// 设置分割线的颜色
        indicator.setDividerPadding(10);//设置
        indicator.setTextColorSelected(Color.parseColor("#28AAE3"));// 设置tab标题选中的颜色
        indicator.setTextColor(Color.parseColor("#979696"));// 设置tab标题未被选中的颜色
        indicator.setTextSize(28);// 设置字体大小
        indicator.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private class BasePagerAdapter extends FragmentPagerAdapter {
        String[] titles;

        public BasePagerAdapter(FragmentManager fm) {
            super(fm);
            this.titles = getResources().getStringArray(R.array.order_titles);
        }



        @Override
        public Fragment getItem(int position) {
            List<Fragment> fragments = new ArrayList<>();

            ReseizeOrderFragment  reseizeOrderFragment = new ReseizeOrderFragment();
            TakingGoodsFragment takingGoodsFragment = new TakingGoodsFragment();
            DistributingFragment distributingFragment = new DistributingFragment();
            MoreFragment moreFragment = new MoreFragment();
            fragments.add(reseizeOrderFragment);
            fragments.add(takingGoodsFragment);
            fragments.add(distributingFragment);
            fragments.add(moreFragment);

            FragmentFactory fragmentFactory = new FragmentFactory(fragments);

            return fragmentFactory.createForNoExpand(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
