package of.media.bq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import of.media.bq.fragment.*;

public class BluetoothPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitle = new String[]{ "拨号键盘", "通讯录", "通话记录", "未接来电"};

    public BluetoothPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int position) {
		Fragment fragment = null;
 
        switch (position) {
            case 0:
                fragment = new DialpadFragment();
                break;
            case 1:
                fragment = new ContactsFragment();
                break;
            case 2:
                fragment = new CallLogFragment();
                break;
            case 3:
                fragment = new MissedCallFragment();
                break;
            default:
                break;
        }
        return fragment;
    }
 
    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
