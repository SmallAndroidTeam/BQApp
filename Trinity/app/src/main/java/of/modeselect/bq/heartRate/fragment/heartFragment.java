package of.modeselect.bq.heartRate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import of.modeselect.bq.R;
import of.modeselect.bq.fragment.HeartRateFragment;
/**
 * Create By rongxinglan IN 2018/11/8
 */
public class heartFragment extends Fragment implements  View.OnClickListener{

    private TextView mCurrent;
    private TextView mWeek;
    private TextView mMonth;
    private TextView testAgain;

    public Fragment currentFragment;
    public Fragment MonthFragment;
    private Fragment WeekFragment;
    private Fragment HeartRateFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.chart, null);
        View view=inflater.inflate(R.layout.chart_layout,container,false);
        initView(view);
        initListener();
        initShowFragment();
        return view;
    }

    private void initListener() {
        mCurrent.setOnClickListener(this);
        mWeek.setOnClickListener(this);
        mMonth.setOnClickListener(this);
        testAgain.setOnClickListener(this);
    }

    private void initView(View view){
      mCurrent=view.findViewById(R.id.current);
      mWeek=view.findViewById(R.id.week);
      mMonth=view.findViewById(R.id.month);
      testAgain =view.findViewById(R.id.Test_again);
  }
    private void initShowFragment() {
        mCurrent.callOnClick();
        mMonth.callOnClick();
    }

    @Override
    public void onClick(View v){
        final FragmentManager fragmentManager = this.getFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (v.getId()){
            case R.id.current:
                if(currentFragment==null){
                    currentFragment=new CurrentFragment();
                    fragmentTransaction.add(R.id.main_fragment,currentFragment);
                }else{
                    fragmentTransaction.show(currentFragment);
                }
                mCurrent.setBackgroundResource(R.drawable.chart_select);
                mWeek.setBackgroundResource(R.drawable.bg_week);
                mMonth.setBackgroundResource(R.drawable.bg_month);
                break;
            case R.id.week:
                if(WeekFragment==null){
                    WeekFragment=new WeekFragment();
                    fragmentTransaction.add(R.id.main_fragment,WeekFragment);
                }else{
                    fragmentTransaction.show(WeekFragment);
                }
                mWeek.setBackgroundResource(R.drawable.chart_select);
                mCurrent.setBackgroundResource(R.drawable.bg_now);
                mMonth.setBackgroundResource(R.drawable.bg_month);
                break;
            case R.id.month:
                if(MonthFragment==null){
                    MonthFragment=new MonthFragment();
                    fragmentTransaction.add(R.id.main_fragment,MonthFragment);
                }else{
                    fragmentTransaction.show(MonthFragment);
                }
                mMonth.setBackgroundResource(R.drawable.chart_select);
                mCurrent.setBackgroundResource(R.drawable.bg_now);
                mWeek.setBackgroundResource(R.drawable.bg_week);
                break;
            case R.id.Test_again:
//                Intent intent = new Intent(getContext(),MainActivity.class);
//                startActivity(intent);
                if(HeartRateFragment==null){
                    HeartRateFragment=new HeartRateFragment();
                    fragmentTransaction.add(R.id.mainFragment,HeartRateFragment);
                }else{
                    fragmentTransaction.show(HeartRateFragment);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(currentFragment!=null){
            fragmentTransaction.hide(currentFragment);
        }
        if(MonthFragment!=null){
            fragmentTransaction.hide(MonthFragment);
        }
        if(WeekFragment!=null){
            fragmentTransaction.hide(WeekFragment);
        }
        if(HeartRateFragment!=null){
            fragmentTransaction.hide(HeartRateFragment);
        }
    }
}
