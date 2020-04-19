package com.feiyou.headstyle.ui.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.feiyou.headstyle.ui.fragment.sub.MyAtMessageFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyCommentFragment;
import com.feiyou.headstyle.ui.fragment.sub.SystemInfoFragment;


public class MyCommentFragmentAdapter extends FragmentPagerAdapter {

    private final Fragment[] FRAGMENTS = new Fragment[]{MyCommentFragment.getInstance(), MyAtMessageFragment.getInstance(),SystemInfoFragment.getInstance()};

    public MyCommentFragmentAdapter(FragmentManager fragmentManager) {
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