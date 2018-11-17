package of.media.bq.fragment;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import of.media.bq.R;
import of.media.bq.activity.MainActivity;
import of.media.bq.saveData.ActivityCollector;

public class SingleModeFragment extends Fragment implements View.OnClickListener {
    private TextView multiMediaButton;
    private TextView bluetoothTv;
    private TextView heartrateTv;
    private TextView interiorviewTv;
    private TextView outsideviewTv;
    private TextView carweichatTv;
    private TextView returnTv;
    private RelativeLayout multiMediaRelativeLayout;
    private RelativeLayout blueToothRelativeLayout;
    private RelativeLayout heartRateRelativeLayout;
    private RelativeLayout interiorViewRelativeLayout;
    private RelativeLayout outsideViewRelativeLayout;
    private RelativeLayout carWeiChatRelativeLayout;
    private RelativeLayout returnRelativeLayout;
    private Fragment multiMediaFragment;
    private Fragment bluetoothFragment;
    private Fragment carWeiChatFragment;
    private Fragment heartRateFragment;
    private Fragment interiorCarFragment;
    private Fragment outsideCarFragment;
    private ImageView bgmenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.single_mode_fragment,container,false);
        initView(view);
        initEvents();
        initShowFragment();
        return view;
    }
    
    
    private void initShowFragment() {
        multiMediaButton.callOnClick();
    }
    
    private void initEvents() {
        multiMediaButton.setOnClickListener(this);
        bluetoothTv.setOnClickListener(this);
        heartrateTv.setOnClickListener(this);
        interiorviewTv.setOnClickListener(this);
        outsideviewTv.setOnClickListener(this);
        returnTv.setOnClickListener(this);
        carweichatTv.setOnClickListener(this);
        
    }
    private void initView(View view) {
        multiMediaButton = view.findViewById(R.id.tv_multimedia);
        bluetoothTv=view.findViewById(R.id.tv_bluetooth);
        heartrateTv=view.findViewById(R.id.tv_heartrate);
        interiorviewTv=view.findViewById(R.id.tv_interiorview);
        outsideviewTv=view.findViewById(R.id.tv_outsideview);
        returnTv=view.findViewById(R.id.tv_return);
        carweichatTv=view.findViewById(R.id.tv_carweichat);
        blueToothRelativeLayout=view.findViewById(R.id.rl_bluetooth);
        heartRateRelativeLayout=view.findViewById(R.id.rl_heartrate);
        interiorViewRelativeLayout=view.findViewById(R.id.rl_interiorview);
        outsideViewRelativeLayout=view.findViewById(R.id.rl_outsideview);
        multiMediaRelativeLayout=view.findViewById(R.id.rl_multimedia);
        returnRelativeLayout=view.findViewById(R.id.rl_return);
        carWeiChatRelativeLayout=view.findViewById(R.id.rl_carweichat);
        bgmenu = view.findViewById(R.id.bgmenu);
    }
    
    //设置view透明度变化
    private void setTextViewAlphaChange(View view){
        Animation animation=new AlphaAnimation(0.5f,1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }
    
    @Override
    public void onClick(View v) {
        Fade fade=new Fade();
        fade.setDuration(500);
        TransitionSet transitionSet=new TransitionSet().addTransition(fade);
        TransitionManager.beginDelayedTransition((ViewGroup) bgmenu.getParent(),transitionSet);
        
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        initTextViewColor();
        
        
        switch (v.getId()){
            case R.id.tv_multimedia:
                String text=multiMediaButton.getText().toString();
                int i=text.length();
                setTextViewAlphaChange(multiMediaButton);
                multiMediaButton.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.VISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(multiMediaFragment==null){
                    multiMediaFragment=new MultiMediaFragment();
                    fragmentTransaction.add(R.id.singleModeFragment,multiMediaFragment);
                }else{
                    fragmentTransaction.show(multiMediaFragment);
                }
                break;
            case R.id.tv_bluetooth:
                setTextViewAlphaChange(bluetoothTv);
                bluetoothTv.setTextColor(getResources().getColor(R.color.textSelect));
                blueToothRelativeLayout.setVisibility(View.VISIBLE);
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(bluetoothFragment ==null){
                    bluetoothFragment =new BluetoothFragment();
                    fragmentTransaction.add(R.id.singleModeFragment, bluetoothFragment);
                }else{
                    fragmentTransaction.show(bluetoothFragment);
                }
                break;
            case R.id.tv_heartrate:
                setTextViewAlphaChange(heartrateTv);
                heartrateTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.VISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(heartRateFragment==null){
                    heartRateFragment=new HeartRateFragment();
                    fragmentTransaction.add(R.id.singleModeFragment,heartRateFragment);
                }else{
                    fragmentTransaction.show(heartRateFragment);
                }
                break;
            case R.id.tv_interiorview:
                setTextViewAlphaChange(interiorviewTv);
                interiorviewTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.VISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(interiorCarFragment==null){
                    interiorCarFragment=new InteriorViewFragment();
                    fragmentTransaction.add(R.id.singleModeFragment,interiorCarFragment);
                }else{
                    fragmentTransaction.show(interiorCarFragment);
                }
                break;
            case R.id.tv_outsideview:
                setTextViewAlphaChange(outsideviewTv);
                outsideviewTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.VISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(outsideCarFragment==null){
                    outsideCarFragment=new OutsideViewFragment();
                    fragmentTransaction.add(R.id.singleModeFragment,outsideCarFragment);
                }else{
                    fragmentTransaction.show(outsideCarFragment);
                }
                break;
            case R.id.tv_carweichat:
                setTextViewAlphaChange(carweichatTv);
                carweichatTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.VISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(carWeiChatFragment==null){
                    carWeiChatFragment=new CarWeiChatFragment();
                    fragmentTransaction.add(R.id.singleModeFragment,carWeiChatFragment);
                }else{
                    fragmentTransaction.show(carWeiChatFragment);
                }
                break;
            case R.id.tv_return:
                setTextViewAlphaChange(returnTv);
                returnTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                blueToothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.VISIBLE);
                if(ActivityCollector.getActivityByIndex(0)!=null&&(ActivityCollector.getActivityByIndex(0) instanceof MainActivity)){
                    //通过调用MainActivity中的setFragment方法显示modeFragment
                    ((MainActivity) Objects.requireNonNull(ActivityCollector.getActivityByIndex(0))).setFragment(0);
                }
                
                break;
            
            default:
                break;
        }
        fragmentTransaction.commit();
    }
    
    //隐藏所有的fragment
    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(multiMediaFragment!=null){
            fragmentTransaction.hide(multiMediaFragment);
        }
        if(bluetoothFragment !=null){
            fragmentTransaction.hide(bluetoothFragment);
        }
        if(carWeiChatFragment!=null){
            fragmentTransaction.hide(carWeiChatFragment);
        }
        if(heartRateFragment!=null){
            fragmentTransaction.hide(heartRateFragment);
        }
        if(interiorCarFragment!=null){
            fragmentTransaction.hide(interiorCarFragment);
        }
        if(outsideCarFragment!=null){
            fragmentTransaction.hide(outsideCarFragment);
        }
    }
    
    private void initTextViewColor(){
        multiMediaButton.setTextColor(getResources().getColor(R.color.textNoSelect));
        bluetoothTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        heartrateTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        interiorviewTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        outsideviewTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        carweichatTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        returnTv.setTextColor(getResources().getColor(R.color.textNoSelect));
    }
    
}
