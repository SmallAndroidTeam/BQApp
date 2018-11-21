package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import of.modeselect.bq.R;

public class BelowFragment  extends Fragment {
    private Fragment modeFragment,drivingModeFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_below,container,false);
        setFragment(0);
        return view;
    }
    /**
     *
     * @param type
     * type=0:显示modeFragment
     * type=1:显示singleModeFragment
     */
    public void setFragment(int type){
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (type){
            case 0:
                if(modeFragment==null){
                    modeFragment=new ModeFragment();
                    fragmentTransaction.add(R.id.mainFragment,modeFragment);
                }else{
                    fragmentTransaction.show(modeFragment);
                }
                
                break;
            case 1:
                if(drivingModeFragment==null){
                    drivingModeFragment=new DrivingModeFragment();
                    fragmentTransaction.add(R.id.mainFragment,drivingModeFragment);
                }else{
                    fragmentTransaction.show(drivingModeFragment);
                }
                
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
        
    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(modeFragment!=null){
            fragmentTransaction.hide(modeFragment);
        }
        if(drivingModeFragment!=null){
            fragmentTransaction.hide(drivingModeFragment);
        }
    }
}
