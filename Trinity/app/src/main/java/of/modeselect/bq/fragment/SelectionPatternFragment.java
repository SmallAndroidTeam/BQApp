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
    
    private TextView workTextView;
    private TextView steamTextView;
    private TextView relaxTextView;
    private TextView driveTextView;
  
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
        
        workTextView = view.findViewById(R.id.tv_workmode);
        relaxTextView = view.findViewById(R.id.tv_relaxmode);
        driveTextView = view.findViewById(R.id.tv_drivemode);
        steamTextView = view.findViewById(R.id.tv_steammode);

    }
    private void setTextViewAlphaChange(TextView view){
        view.getPaint().setFakeBoldText(true);//字体加粗
        view.setTextColor(0xffffffff);//100%透明度
    }
    
    private  void addAnimation(View view){
        Fade fade=new Fade();
      fade.setDuration(100);
        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(),fade);
    }
    @Override
    public void onClick(View v) {
        addAnimation(choose_work);
        addAnimation(choose_relax);
        addAnimation(choose_steer);
        addAnimation(choose_steam);
        initTextViewColor();
        switch(v.getId()){
            case R.id.iv_relax:
                setTextViewAlphaChange(relaxTextView);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                if(choose_relax.getVisibility()==View.VISIBLE){
                    Log.i("selectionPattern",
                            String.valueOf("//"+ ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)));
                    if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                        //通过调用MainActivity中的setFragment方法显示modeFragment
                        ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(1);
                }
                }else {
                    choose_relax.setVisibility(View.VISIBLE);
                    
                    workTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    workTextView.setTextColor(0x80ffffff);//50%透明度
                    relaxTextView.getPaint().setFakeBoldText(true);//字体加粗
                    relaxTextView.setTextColor(0xffffffff);//100%透明度
                    steamTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    steamTextView.setTextColor(0x80ffffff);//50%透明度
                    driveTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    driveTextView.setTextColor(0x80ffffff);//50%透明度
                }

                break;
            case R.id.iv_work:
                setTextViewAlphaChange(workTextView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                
                if(choose_work.getVisibility()==View.VISIBLE){
                    if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                        //通过调用MainActivity中的setFragment方法显示modeFragment
                        ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(2);
                    }
                }else {
                    choose_work.setVisibility(View.VISIBLE);
                    
                    workTextView.getPaint().setFakeBoldText(true);//字体不加粗
                    workTextView.setTextColor(0xffffffff);//50%透明度
                    relaxTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    relaxTextView.setTextColor(0x80ffffff);//50%透明度
                    steamTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    steamTextView.setTextColor(0x80ffffff);//50%透明度
                    driveTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    driveTextView.setTextColor(0x80ffffff);//50%透明度
                }
              
                break;
            case R.id.iv_steamingmedia:
               // setTextViewAlphaChange(steamTextView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                choose_steer.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_steeringwheel:
                 setTextViewAlphaChange(driveTextView);
                choose_relax.setVisibility(View.INVISIBLE);
                choose_work.setVisibility(View.INVISIBLE);
                choose_steam.setVisibility(View.INVISIBLE);
                
                if(choose_steer.getVisibility()==View.VISIBLE){
                    if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                        //通过调用MainActivity中的setFragment方法显示DrivingFragment
                        ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(3);
                    }
                }else {
                    choose_steer.setVisibility(View.VISIBLE);
                    
                    workTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    workTextView.setTextColor(0x80ffffff);//50%透明度
                    relaxTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    relaxTextView.setTextColor(0x80ffffff);//50%透明度
                    steamTextView.getPaint().setFakeBoldText(false);//字体不加粗
                    steamTextView.setTextColor(0x80ffffff);//50%透明度
                    driveTextView.getPaint().setFakeBoldText(true);//字体加粗
                    driveTextView.setTextColor(0xffffffff);//100%透明度
                }
             
                break;
        }
    }
    private void initTextViewColor() {
        workTextView.getPaint().setFakeBoldText(false);//字体不加粗
        workTextView.setTextColor(0x80ffffff);//50%透明度
        relaxTextView.getPaint().setFakeBoldText(false);//字体不加粗
        relaxTextView.setTextColor(0x80ffffff);//50%透明度
        steamTextView.getPaint().setFakeBoldText(false);//字体不加粗
        steamTextView.setTextColor(0x80ffffff);//50%透明度
        driveTextView.getPaint().setFakeBoldText(false);//字体不加粗
        driveTextView.setTextColor(0x80ffffff);//50%透明度
    }
}
