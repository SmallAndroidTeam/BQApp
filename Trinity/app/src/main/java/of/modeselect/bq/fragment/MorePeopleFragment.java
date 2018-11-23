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

import of.modeselect.bq.R;
import of.modeselect.bq.heartRate.fragment.heartFragment;

public class MorePeopleFragment extends Fragment {
    private Fragment heartfragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_more_people,container,false);
        getFragment();
        return view;
    }
    
    private void getFragment() {
        final FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(heartfragment==null){
            heartfragment=new heartFragment();
            fragmentTransaction.add(R.id.heartRateFragment,heartfragment);
            
        }else {
            fragmentTransaction.show(heartfragment);
        }
        fragmentTransaction.commit();
    }
    
   
}
