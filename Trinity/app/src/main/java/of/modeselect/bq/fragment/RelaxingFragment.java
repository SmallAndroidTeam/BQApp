package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;

import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.saveData.ActivityCollector;

public class RelaxingFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private TextView tv_localleft;
    private TextView tv_onlineleft;
    private TextView tv_localright;
    private TextView tv_onlineright;
    private ImageView iv_leftImage;
    private ImageView iv_rightImage;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_relaxing,container,false);
        initView(view);
        view.setOnTouchListener(this);
        initEvents();
        selectTab(1);
      return view;
    }
    
    private void selectTab(int i) {
        switch (i){
            case 0:
                iv_leftImage.setImageResource(R.drawable.bg_online);
                iv_rightImage.setImageResource(R.drawable.bg_rnote);
                break;
            case 1:
                iv_leftImage.setImageResource(R.drawable.bg_local);
                iv_rightImage.setImageResource(R.drawable.bg_rinternet);
                break;
            default:
                break;
        }
    }
    
    private void initEvents() {
        tv_localleft.setOnClickListener(this);
        tv_onlineleft.setOnClickListener(this);
        tv_localright.setOnClickListener(this);
        tv_onlineright.setOnClickListener(this);
    }
    
    private void initView(View view) {
        tv_localleft = view.findViewById(R.id.tv_localleft);
        tv_onlineleft = view.findViewById(R.id.tv_onlineleft);
        tv_localright = view.findViewById(R.id.tv_localright);
        tv_onlineright = view.findViewById(R.id.tv_onlineright);
        iv_leftImage = view.findViewById(R.id.iv_leftImage);
        iv_rightImage = view.findViewById(R.id.iv_rightImage);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_localleft:
            case R.id.tv_localright:
                selectTab(1);
                break;
            case R.id.tv_onlineleft:
            case R.id.tv_onlineright:
                selectTab(0);
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
            //通过调用MainActivity中的setFragment方法显示modeFragment
            ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(0);
          
        }
       
        return false;
    }
}
