package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;
import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.saveData.ActivityCollector;

public class DrivingModeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_drivingmode;
    private RelativeLayout rl_returnModeFragment;
    private TextView tv_returnModeFragment;
    private TextView tv_drivingmode;
    private ImageView bg_menu;
    private Fragment singleOrMoreFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_driving_mode,container,false);
        initView(view);
        setClick();
       setTable(0);
        return view;
    }
    
    private void setTable(int i) {
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch (i){
            case 0:
                rl_returnModeFragment.setVisibility(View.INVISIBLE);
                rl_drivingmode.setVisibility(View.VISIBLE);
                tv_drivingmode.getPaint().setFakeBoldText(true);//字体加粗
                tv_drivingmode.setTextColor(0xffffffff);//100%透明度
    
                tv_returnModeFragment.getPaint().setFakeBoldText(false);
                tv_returnModeFragment.setTextColor(0x80ffffff);//50%透明度
                if(singleOrMoreFragment==null){
                    singleOrMoreFragment=new SingleOrMorePatternFragment();
                    fragmentTransaction.add(R.id.drivingFragment,singleOrMoreFragment);
                }else{
                    fragmentTransaction.show(singleOrMoreFragment);
                }
                break;
            case 1:
                rl_returnModeFragment.setVisibility(View.VISIBLE);
                rl_drivingmode.setVisibility(View.INVISIBLE);
                tv_drivingmode.getPaint().setFakeBoldText(false);//字体加粗
                tv_drivingmode.setTextColor(0x80ffffff);//100%透明度
                tv_returnModeFragment.getPaint().setFakeBoldText(true);
                tv_returnModeFragment.setTextColor(0xffffffff);//50%透明度
                if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                //通过调用MainActivity中的setFragment方法显示单人模式
                ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(4);
            }
    
                rl_returnModeFragment.setVisibility(View.INVISIBLE);
                rl_drivingmode.setVisibility(View.VISIBLE);
                tv_drivingmode.getPaint().setFakeBoldText(true);//字体加粗
                tv_drivingmode.setTextColor(0xffffffff);//100%透明度
                tv_returnModeFragment.getPaint().setFakeBoldText(false);
                tv_returnModeFragment.setTextColor(0x80ffffff);//50%透明度
                break;
                default:
                    break;
        }
        fragmentTransaction.commit();
    }
    
    private void setClick() {
        tv_drivingmode.setOnClickListener(this);
        tv_returnModeFragment.setOnClickListener(this);
    }
    
    private void initView(View view) {
        rl_drivingmode = view.findViewById(R.id.rl_drivingmode);
        rl_returnModeFragment = view.findViewById(R.id.rl_returnModeFragment);
        tv_drivingmode = view.findViewById(R.id.tv_drivingmode);
        tv_returnModeFragment = view.findViewById(R.id.tv_returnModeFragment);
        bg_menu = view.findViewById(R.id.bg_menu);
    }
    
    @Override
    public void onClick(View v) {
        Fade fade=new Fade();
        fade.setDuration(500);
        TransitionSet transitionSet=new TransitionSet().addTransition(fade);
        TransitionManager.beginDelayedTransition((ViewGroup) bg_menu.getParent(),transitionSet);
       rl_drivingmode.setVisibility(View.INVISIBLE);
       rl_returnModeFragment.setVisibility(View.INVISIBLE);
        initTextViewColor();
        switch (v.getId()){
            case R.id.tv_drivingmode:
                setTable(0);
               
                break;
            case R.id.tv_returnModeFragment:
                setTable(1);
               
                break;
        }
    }
    
    private void initTextViewColor() {
        tv_drivingmode.getPaint().setFakeBoldText(false);//字体加粗
        tv_drivingmode.setTextColor(0x80ffffff);//100%透明度
        tv_returnModeFragment.getPaint().setFakeBoldText(false);
        tv_returnModeFragment.setTextColor(0x80ffffff);//50%透明度
    }
}
