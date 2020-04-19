package com.feiyou.headstyle.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


public class SubFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> FRAGMENTS;

    public SubFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.FRAGMENTS = fragments;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return FRAGMENTS.size();
    }

    //@Override
    //public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    //}
}