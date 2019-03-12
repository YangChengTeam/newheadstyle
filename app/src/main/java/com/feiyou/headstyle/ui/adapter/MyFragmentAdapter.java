package com.feiyou.headstyle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.feiyou.headstyle.ui.fragment.Home1Fragment;
import com.feiyou.headstyle.ui.fragment.CommunityFragment;
import com.feiyou.headstyle.ui.fragment.CreateFragment;
import com.feiyou.headstyle.ui.fragment.MyFragment;
import com.feiyou.headstyle.ui.fragment.TestFragment;


public class MyFragmentAdapter extends FragmentPagerAdapter {

    private final Fragment[] FRAGMENTS = new Fragment[]{new Home1Fragment(), new CommunityFragment(), new CreateFragment(), new TestFragment(), new MyFragment()};

    public MyFragmentAdapter(FragmentManager fragmentManager) {
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