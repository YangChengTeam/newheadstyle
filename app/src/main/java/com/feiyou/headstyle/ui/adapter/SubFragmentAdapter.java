package com.feiyou.headstyle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.feiyou.headstyle.ui.fragment.CommunityFragment;
import com.feiyou.headstyle.ui.fragment.HomeFragment;
import com.feiyou.headstyle.ui.fragment.sub.FollowFragment;
import com.feiyou.headstyle.ui.fragment.sub.RecommendFragment;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;

import java.util.List;


public class SubFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> FRAGMENTS;

    public SubFragmentAdapter(FragmentManager fragmentManager,List<Fragment> fragments) {
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