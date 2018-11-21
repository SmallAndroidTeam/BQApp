package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import of.modeselect.bq.R;

import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.saveData.ActivityCollector;


public class SelectionPatternFragment extends Fragment implements View.OnClickListener{
    
    private ImageView workImageView;
    private ImageView relaxImageView;
    private ImageView steeringImageView;
    private ImageView steamingImageView;
    private ImageView choose_relax;
    private ImageView choose_work;
    private ImageView choose_steam;
    private ImageView choose_steer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_selection_pattern,container,false);
        init(view);
        setListener();
        return view;
    }
    
    private void setListener() {
        workImageView.setOnClickListener(this);
        relaxImageView.setOnClickListener(this);
        steamingImageView.setOnClickListener(this);
        steeringImageView.setOnClickListener(this);
    }
    
    private void init(View view) {
        
        workImageView=view.findViewById(R.id.iv_work);
        relaxImageView=view.findViewById(R.id.iv_relax);
        steeringImageView=view.findViewById(R.id.iv_steeringwheel);
        steamingImageView=view.findViewById(R.id.iv_steamingmedia);
        
        choose_work = view.findViewById(R.id.choose_work);
        choose_relax = view.findViewById(R.id.choose_relax);
        choose_steer = view.findViewById(R.id.choose_steer);
        choose_steam = view.findViewById(R.id.choose_steam);
        
    }
    private void setTextViewAlphaChange(View view){
        Animation animation=new AlphaAnimation(0.5f,1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }
    
    private  void addAnimation(View view){
        Fade fade=new Fade();
        fade.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(),fade);
    }
    @Override
    public void onClick(View v) {
        
        addAnimation(choose_work);
        addAnimation(choose_relax);
        addAnimation(choose_steer);
        addAnimation(choose_steam);
        switch(v.getId()){
            case R.id.iv_relax:
                setTextViewAlphaChange(relaxImageView);
                choose_relax.setVisibility(View.VISIBLE);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                
                Log.i("selectionPattern",
                        String.valueOf("//"+ ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)));
                if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                    //通过调用MainActivity中的setFragment方法显示modeFragment
                    ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(1);
                }
//                 Intent intent=new Intent(getActivity(), RelaxingActivity.class);
//                 startActivity(intent);
                break;
            case R.id.iv_work:
                setTextViewAlphaChange(workImageView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_work.setVisibility(View.VISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                    //通过调用MainActivity中的setFragment方法显示modeFragment
                    ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(2);
                }
                break;
            case R.id.iv_steamingmedia:
                setTextViewAlphaChange(steamingImageView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.VISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_steeringwheel:
                setTextViewAlphaChange(steeringImageView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.VISIBLE);
                if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                    //通过调用MainActivity中的setFragment方法显示DrivingFragment
                    ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(3);
                }
                break;
        }
    }
}
