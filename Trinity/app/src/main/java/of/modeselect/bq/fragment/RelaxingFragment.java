package of.modeselect.bq.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;

import of.modeselect.bq.activity.MainActivity;
import of.modeselect.bq.saveData.ActivityCollector;

public class RelaxingFragment extends Fragment implements View.OnClickListener {


    private TextView leftOnlinebutton;
    private TextView leftLocalbutton;
    private ImageView leftMenuLiner;
    private ImageView rightMenuLiner;
    private TextView rightOnlinebutton;
    private TextView rightLocalbutton;
    private Fragment localFragment,onlineFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_relaxing,container,false);
        initView(view);
        initEvents();
        selectTab(0);
      return view;
    }

    /**
     *
     * @param i
     * i==0,选中本地
     * i==1,选中在线
     */
    private void selectTab(int i) {
        switch (i){
            case 0:
                leftLocalbutton.callOnClick();
                break;
            case 1:
                leftOnlinebutton.callOnClick();
                break;
                default:
                    break;
        }
    }
    
    private void initEvents() {
        leftOnlinebutton.setOnClickListener(this);
        leftLocalbutton.setOnClickListener(this);
        rightOnlinebutton.setOnClickListener(this);
        rightLocalbutton.setOnClickListener(this);
    }
    
    private void initView(View view) {
        leftOnlinebutton = view.findViewById(R.id.leftOnlinebutton);
        leftLocalbutton = view.findViewById(R.id.leftLocalbutton);
        leftMenuLiner = view.findViewById(R.id.leftMenuLiner);
        rightMenuLiner = view.findViewById(R.id.rightMenuLiner);
        rightOnlinebutton = view.findViewById(R.id.rightOnlinebutton);
        rightLocalbutton = view.findViewById(R.id.rightLocalbutton);
    }
    
    @Override
    public void onClick(View v) {
        Fade fade=new Fade();
//        leftMenuLiner.setVisibility(View.INVISIBLE);
//        rightMenuLiner.setVisibility(View.INVISIBLE);
//        TransitionManager.beginDelayedTransition((ViewGroup) leftMenuLiner.getParent(),fade);
//        TransitionManager.beginDelayedTransition((ViewGroup) rightMenuLiner.getParent(),fade);

        TransitionManager.beginDelayedTransition((ViewGroup) leftOnlinebutton.getParent(),fade);
        TransitionManager.beginDelayedTransition((ViewGroup) rightOnlinebutton.getParent(),fade);

        final FragmentManager fragmentManage= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
         final FragmentTransaction fragmentTransaction=fragmentManage.beginTransaction();
         fragmentTransaction.setCustomAnimations(R.anim.card_flip_top_in,R.anim.card_flip_top_out);
         hideAllFragment(fragmentTransaction);
        switch (v.getId()){
            case R.id.leftOnlinebutton:
            case R.id.rightOnlinebutton:
                leftOnlinebutton.getPaint().setFakeBoldText(true);//字体加粗
                leftOnlinebutton.setTextColor(0xffffffff);

                leftLocalbutton.getPaint().setFakeBoldText(false);
                leftLocalbutton.setTextColor(0x80ffffff);

                rightOnlinebutton.getPaint().setFakeBoldText(true);//字体加粗
                rightOnlinebutton.setTextColor(0xffffffff);

                rightLocalbutton.getPaint().setFakeBoldText(false);
                rightLocalbutton.setTextColor(0x80ffffff);

                leftMenuLiner.setImageResource(R.drawable.bg_online);
                rightMenuLiner.setImageResource(R.drawable.bg_rnote);

                RelativeLayout.LayoutParams leftMenuLinerLayoutParams= (RelativeLayout.LayoutParams) leftMenuLiner.getLayoutParams();
                leftMenuLinerLayoutParams.bottomMargin=60;
                leftMenuLiner.setLayoutParams(leftMenuLinerLayoutParams);

                RelativeLayout.LayoutParams rightMenuLinerLayoutParams= (RelativeLayout.LayoutParams) rightMenuLiner.getLayoutParams();
                rightMenuLinerLayoutParams.bottomMargin=60;
                rightMenuLiner.setLayoutParams(rightMenuLinerLayoutParams);

                if(onlineFragment==null){
                    onlineFragment=new OnlineFragment();
                    fragmentTransaction.add(R.id.multimediaLayout,onlineFragment);
                }else{
                    fragmentTransaction.show(onlineFragment);
                }
                break;
            case R.id.leftLocalbutton:
            case R.id.rightLocalbutton:

                leftLocalbutton.getPaint().setFakeBoldText(true);//字体加粗
                leftLocalbutton.setTextColor(0xffffffff);//100%透明度

                leftOnlinebutton.getPaint().setFakeBoldText(false);
                leftOnlinebutton.setTextColor(0x80ffffff);//50%透明度

                rightLocalbutton.getPaint().setFakeBoldText(true);//字体加粗
                rightLocalbutton.setTextColor(0xffffffff);

                rightOnlinebutton.getPaint().setFakeBoldText(false);
                rightOnlinebutton.setTextColor(0x80ffffff);

                leftMenuLiner.setImageResource(R.drawable.bg_local);
                rightMenuLiner.setImageResource(R.drawable.bg_rinternet);

                RelativeLayout.LayoutParams leftMenuLinerLayoutParams1= (RelativeLayout.LayoutParams) leftMenuLiner.getLayoutParams();
                leftMenuLinerLayoutParams1.bottomMargin=0;
                leftMenuLiner.setLayoutParams(leftMenuLinerLayoutParams1);

                RelativeLayout.LayoutParams rightMenuLinerLayoutParams1= (RelativeLayout.LayoutParams) rightMenuLiner.getLayoutParams();
                rightMenuLinerLayoutParams1.bottomMargin=0;
                rightMenuLiner.setLayoutParams(rightMenuLinerLayoutParams1);


                if(localFragment==null){
                    localFragment=new LocalFragment();
                    fragmentTransaction.add(R.id.multimediaLayout,localFragment);
                }else{
                    fragmentTransaction.show(localFragment);
                }
                break;
            default:
                break;
        }
//        leftMenuLiner.setVisibility(View.VISIBLE);
//        rightMenuLiner.setVisibility(View.VISIBLE);
        fragmentTransaction.commit();
    }

    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(localFragment!=null){
            fragmentTransaction.hide(localFragment);
        }

        if(onlineFragment!=null){
            fragmentTransaction.hide(onlineFragment);
        }
    }



}
