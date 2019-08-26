package com.feiyou.headstyle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.feiyou.headstyle.ui.fragment.CashDetailFragment;
import com.feiyou.headstyle.ui.fragment.GoldDetailFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyAtMessageFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyCommentFragment;
import com.feiyou.headstyle.ui.fragment.sub.SystemInfoFragment;


public class GoldAndCashAdapter extends FragmentPagerAdapter {

    private final Fragment[] FRAGMENTS = new Fragment[]{GoldDetailFragment.getInstance(), CashDetailFragment.getInstance()};

    public GoldAndCashAdapter(FragmentManager fragmentManager) {
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