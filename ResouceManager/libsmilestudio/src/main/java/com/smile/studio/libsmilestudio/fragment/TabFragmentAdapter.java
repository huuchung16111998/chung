package com.smile.studio.libsmilestudio.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    public static final boolean upperCase = true;
    ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private List<String> titles;

    @Deprecated
    public TabFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles, ViewPager viewPager) {
        super(fm);
        this.viewPager = viewPager;
        this.fragments = fragments;
        this.titles = titles;
    }

    public TabFragmentAdapter(Context context, FragmentManager fm, LinkedHashMap<Integer, Fragment> mapFragments, ViewPager viewPager) {
        super(fm);
        this.viewPager = viewPager;
        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();
        for (Integer map : mapFragments.keySet()) {
            this.titles.add(context.getString(map));
            this.fragments.add(mapFragments.get(map));
        }
    }

    public TabFragmentAdapter(Context context, FragmentManager fm, LinkedHashMap<String, Fragment> mapFragments, boolean flag, ViewPager viewPager) {
        super(fm);
        this.viewPager = viewPager;
        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();
        for (String map : mapFragments.keySet()) {
            this.titles.add(map);
            this.fragments.add(mapFragments.get(map));
        }
    }

    public List<String> getTitles() {
        return titles;
    }

    public void removeTabPage(int position) {
        if (!titles.isEmpty() && position < titles.size()) {
            titles.remove(position);
            fragments.remove(position);
            notifyDataSetChanged();
        }
    }

    public void addTabPage(int position, String title, Fragment fragment) {
        titles.add(position, title);
        fragments.add(position, fragment);
        notifyDataSetChanged();
    }

    public void addTab(String title, Fragment fragment) {
        titles.add(title);
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void addTab(int position, String title, Fragment fragment) {
        if (titles.size() >= position && position >= 0) {
            titles.add(position, title);
            fragments.add(position, fragment);
            notifyDataSetChanged();
        }
    }

    public void removeTab(int position) {
        Fragment fragment = fragments.get(position);
        destroyFragmentView(viewPager, position, fragment);
        fragments.remove(fragment);
        titles.remove(position);
        notifyDataSetChanged();
    }

    public void updateTab(int position, String title, Fragment fragment) {
        addTab(position, title, fragment);
        removeTab(position - 1);
    }

    public void destroyFragmentView(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position % titles.size());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return upperCase ? titles.get(position).toUpperCase() : titles.get(position);
    }

}
