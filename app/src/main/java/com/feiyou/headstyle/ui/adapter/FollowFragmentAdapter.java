package com.feiyou.headstyle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.feiyou.headstyle.ui.fragment.sub.FollowFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyFriendsFragment;
import com.feiyou.headstyle.ui.fragment.sub.RecommendFragment;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;


public class FollowFragmentAdapter extends FragmentPagerAdapter {

    private final Fragment[] FRAGMENTS = new Fragment[]{MyFriendsFragment.newInstance(1), MyFriendsFragment.newInstance(2)};

    public FollowFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS[position];
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return FRAGMENTS.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}