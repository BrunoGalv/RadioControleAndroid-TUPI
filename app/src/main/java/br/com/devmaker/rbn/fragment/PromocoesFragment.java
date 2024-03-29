package br.com.devmaker.rbn.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.dialog.EscolherDialogFragment;
import br.com.devmaker.rbn.util.CacheData;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PromocoesFragment extends Fragment {

    private static String TAG = EscolherDialogFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public PromocoesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CacheData cacheData = new CacheData(getContext());
        View rootView = inflater.inflate(R.layout.fragment_promocoes, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#8d939b"), Color.parseColor(cacheData.getString("color")));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(cacheData.getString("color")));

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        PromocoesStatusFragment vigentes = new PromocoesStatusFragment();
        Bundle argsVigentes = new Bundle();
        argsVigentes.putString("status", "vigentes");
        vigentes.setArguments(argsVigentes);
        adapter.addFragment(vigentes, "Vigentes");
        PromocoesStatusFragment encerradas = new PromocoesStatusFragment();
        Bundle argsEncerradas = new Bundle();
        argsEncerradas.putString("status", "encerradas");
        encerradas.setArguments(argsEncerradas);
        adapter.addFragment(encerradas, "Encerradas");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

