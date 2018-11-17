package of.media.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import of.media.bq.R;

public class BelowFragment  extends Fragment {
    private Fragment modeFragment,singleModeFragment,relaxingFragment;
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
        final FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
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
                if(singleModeFragment==null){
                    singleModeFragment=new SingleModeFragment();
                    fragmentTransaction.add(R.id.mainFragment,singleModeFragment);
                }else{
                    fragmentTransaction.show(singleModeFragment);
                }
                break;
            case 2:
                if(relaxingFragment!=null) {
                    relaxingFragment = new RelaxingFragment();
                    fragmentTransaction.add(R.id.downFragment,relaxingFragment);
                }else {
                    fragmentTransaction.show(relaxingFragment);
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
        if(singleModeFragment!=null){
            fragmentTransaction.hide(singleModeFragment);
        }
        if(relaxingFragment!=null){
            fragmentTransaction.hide(relaxingFragment);
        }
    }
}
