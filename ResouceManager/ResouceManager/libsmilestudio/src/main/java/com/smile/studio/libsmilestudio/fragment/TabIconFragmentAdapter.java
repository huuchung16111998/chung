package com.smile.studio.libsmilestudio.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class TabIconFragmentAdapter extends FragmentPagerAdapter {

    public static final boolean upperCase = true;
    private Context context;
    private ArrayList<Fragment> fragments;
    private List<Integer> resourceIcon;

    public TabIconFragmentAdapter(Context context, FragmentManager fm, LinkedHashMap<Integer, Fragment> mapFragments) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<Fragment>();
        resourceIcon = new ArrayList<Integer>();
        for (Integer map : mapFragments.keySet()) {
            this.resourceIcon.add(map);
            this.fragments.add(mapFragments.get(map));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position % resourceIcon.size());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = context.getResources().getDrawable(resourceIcon.get(position));
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
