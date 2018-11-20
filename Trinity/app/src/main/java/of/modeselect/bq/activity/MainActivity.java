package of.modeselect.bq.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import of.modeselect.bq.R;
import of.modeselect.bq.fragment.BelowFragment;

import of.modeselect.bq.fragment.RelaxingFragment;

import of.modeselect.bq.fragment.WorkingModeFragment;
import of.modeselect.bq.saveData.ActivityCollector;

public class MainActivity extends FragmentActivity {
    private Fragment belowFragment,relaxingFragment,workingModeFragment;
    private TextView currentTimeTextView;
    private final static int UPDATE_TIME=0;//更新时间
    private Timer timer;
    public static TextView visibleTextView;
    @SuppressLint("HandlerLeak")
    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TIME:
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
                    currentTimeTextView.setText(simpleDateFormat.format(new Date()));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        currentTimeTextView=findViewById(R.id.tv_currenttime);
        
        visibleTextView = findViewById(R.id.invisible_textview);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mhander.sendEmptyMessage(UPDATE_TIME);
            }
        },0,100);
        setFragment(0);
        ActivityCollector.addActivity(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remoteActivity(this);
    }
    /**
     *
     * @param type
     * type=0:显示belowFragment
     * type=1:显示relaxingFragment
     */
    public void setFragment(int type){
        final FragmentManager fragmentManager=getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        setTextInisible();
        switch (type){
            case 0:
                if(belowFragment==null){
                    belowFragment=new BelowFragment();
                    fragmentTransaction.add(R.id.downFragment,belowFragment);
                }else{
                    fragmentTransaction.show(belowFragment);
                }
                break;
            case 1:
                if(relaxingFragment==null) {
                    relaxingFragment = new RelaxingFragment();
                    fragmentTransaction.add(R.id.downFragment,relaxingFragment);
                    setTextVisible();
                }else {
                    setTextVisible();
                    fragmentTransaction.show(relaxingFragment);
                }
                break;
            case 3:
                if(workingModeFragment==null) {
                    workingModeFragment = new WorkingModeFragment();
                    fragmentTransaction.add(R.id.downFragment,workingModeFragment);
                    setTextVisible();
                }else {
                    setTextVisible();
                    fragmentTransaction.show(workingModeFragment);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
        
    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(belowFragment!=null){
            fragmentTransaction.hide(belowFragment);
        }
        if(relaxingFragment!=null){
            fragmentTransaction.hide(relaxingFragment);
        }
    }
 
    public  static void setTextVisible(){
        visibleTextView.setVisibility(View.VISIBLE);
        
    }
    public  static void setTextInisible(){
        visibleTextView.setVisibility(View.INVISIBLE);
    }
   
}
