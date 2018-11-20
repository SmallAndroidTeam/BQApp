package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Objects;

import of.modeselect.bq.R;
import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.saveData.ActivityCollector;


public class SingleOrMorePatternFragment extends Fragment implements View.OnClickListener {
    
    private RelativeLayout moreModeLayout;
    private RelativeLayout singleModeLayout;
    private ImageView singleModeFlame;
    private ImageView moreModeFlame;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_singer_or_more_pattern,container,false);
       initview(view);
       initEvents();
       return view;
    }
    
    private void initEvents() {
        singleModeLayout.setOnClickListener(this);
        moreModeLayout.setOnClickListener(this);
    }
    
    private void initview(View view) {
        singleModeLayout = view.findViewById(R.id.singleModeLayout);
        moreModeLayout = view.findViewById(R.id.moreModeLayout);
        singleModeFlame = view.findViewById(R.id.singleModeFlame);
        moreModeFlame = view.findViewById(R.id.moreModeFlame);
    }
    
    private  void addAnimation(View view){
        Fade fade=new Fade();
        fade.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(),fade);
    }
    
    @Override
    public void onClick(View v) {
        addAnimation(moreModeFlame);
        addAnimation(singleModeFlame);
    switch (v.getId()){
        case R.id.singleModeLayout:
            moreModeFlame.setVisibility(View.INVISIBLE);
            singleModeFlame.setVisibility(View.VISIBLE);
//            if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
//                //通过调用MainActivity中的setFragment方法显示单人模式
//                ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(1);
//            }
            break;
        case R.id.moreModeLayout:
            moreModeFlame.setVisibility(View.VISIBLE);
            singleModeFlame.setVisibility(View.INVISIBLE);
            break;
            default:
                break;
    }
    }
}
