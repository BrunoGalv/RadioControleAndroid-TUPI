package br.com.devmaker.rbn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.devmaker.rbn.fragment.AoVivoFragment;
import br.com.devmaker.rbn.fragment.CanaisFragment;

/**
 * Created by leodegeus on 30/01/2018.
 */

public class MenuAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    AoVivoFragment tab1;
    CanaisFragment tab2;

    public MenuAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null) {
                    tab1 = new AoVivoFragment();
                }

                return tab1;
            case 1:
                if (tab2 == null) {
                    tab2 = new CanaisFragment();
                }

                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}