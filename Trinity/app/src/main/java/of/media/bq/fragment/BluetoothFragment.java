package of.media.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import of.media.bq.R;
import of.media.bq.adapter.BluetoothPagerAdapter;

public class BluetoothFragment extends Fragment{

    private static final String TAG = "BT.BluetoothFragment";

    private TabLayout bluetoothTabLayout;
    private ViewPager bluetoothViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bluetooth, container, false);
        bluetoothTabLayout = view.findViewById(R.id.bluetoothTabLayout);
        bluetoothViewPager = view.findViewById(R.id.bluetoothViewPager);
        BluetoothPagerAdapter adapter = new BluetoothPagerAdapter(getFragmentManager());
        bluetoothViewPager.setAdapter(adapter);
        bluetoothTabLayout.setupWithViewPager(bluetoothViewPager);

        updateFragment(true);

        return view;
    }

    /**
     * Updates the content fragment of this Activity based on the state of the bluetooth.
     */
    private void updateFragment(boolean bluetoothEnabled) {
        Log.d(TAG, "updateBluetoothFragment, bluetooth is enabled(" + bluetoothEnabled + ")");

        if (bluetoothEnabled) {
            // Bluetooth connected

        } else {
            // Bluetooth not connected
        }
    }

}
