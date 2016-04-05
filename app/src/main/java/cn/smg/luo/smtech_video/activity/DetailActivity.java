package cn.smg.luo.smtech_video.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smg.luo.smtech_video.BaseActivity;
import cn.smg.luo.smtech_video.R;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.activity
 * @description:
 * @date 2016/3/25 16:57
 */

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Nullable @Bind(R.id.toolbar)Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail1);
        ButterKnife.bind(this);
        if(toolbar!=null) {
//        toolbar.getBackground().setAlpha(0);//toolbar透明度初始化为0
//            toolbar.setTitle("测试");
            setSupportActionBar(toolbar);
        }
        initView();
    }

    private void initView(){
        setupViewPager();
        tabLayout.addTab(tabLayout.newTab().setText("内容简介"));
        tabLayout.addTab(tabLayout.newTab().setText("作者简介"));
        tabLayout.addTab(tabLayout.newTab().setText("目录"));
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BooksFragment.newInstance(1),"内容简介");
        adapter.addFragment(BooksFragment.newInstance(2),"作者简介");
        adapter.addFragment(BooksFragment.newInstance(3),"目录");
        mViewPager.setAdapter(adapter);
    }


    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragments = new ArrayList<>();
        private final ArrayList<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
