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
import of.media.bq.saveData.ActivityCollector;

public class ModeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rlSelectionPattern;
    private RelativeLayout rlSingleOrMorePattern;
    private RelativeLayout rlReturnHome;
    private TextView tvSelectionPattern;
    private TextView tvSingleOrMorePattern;
    private TextView tvReturnHome;
    private Fragment singleOrMorePatternFragment;
    private Fragment selectionPatternFragment;
    private ImageView bgmenu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mode_fragment_item,container,false);
        initView(view);
        initEvent();
        selectFragmemt(0);
        return  view;
    }
    
    /**
     *
     * @param type
     * type=0: 选中selectionPatternFragment
     * type=1:选中singleOrMorePatternFragment
     */
    private  void selectFragmemt(int type){
     switch (type){
         case 0:
             tvSelectionPattern.callOnClick();
             break;
         case 1:
             tvSingleOrMorePattern.callOnClick();
             break;
             default:
                 break;
     }
    }
    private void initEvent() {
        tvReturnHome.setOnClickListener(this);
        tvSelectionPattern.setOnClickListener(this);
        tvSingleOrMorePattern.setOnClickListener(this);
    }
    private void initView(View view) {
        rlSelectionPattern=view.findViewById(R.id.rl_selectionpattern);
        rlSingleOrMorePattern=view.findViewById(R.id.rl_singleormoreselection);
        rlReturnHome=view.findViewById(R.id.rl_returnhome);
        tvReturnHome=view.findViewById(R.id.tv_returnhome);
        tvSelectionPattern=view.findViewById(R.id.tv_selectionpattern);
        tvSingleOrMorePattern=view.findViewById(R.id.tv_singleormorepattern);
        bgmenu = view.findViewById(R.id.bgmenu);
    }
    
    //设置view透明度变化
    private void setTextViewAlphaChange(View view){
        Animation animation=new AlphaAnimation(0.1f,1.0f);
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
    
        switch (v.getId()) {
            case R.id.tv_selectionpattern:
                setTextViewAlphaChange(tvSelectionPattern);
                rlSelectionPattern.setVisibility(View.VISIBLE);
                rlSingleOrMorePattern.setVisibility(View.INVISIBLE);
                rlReturnHome.setVisibility(View.INVISIBLE);
                if(selectionPatternFragment==null){
                    selectionPatternFragment=new SelectionPatternFragment();
                    fragmentTransaction.add(R.id.modeFragment,selectionPatternFragment);
                }else{
                    fragmentTransaction.show(selectionPatternFragment);
                }
                break;
        
            case R.id.tv_singleormorepattern:
                setTextViewAlphaChange(tvSingleOrMorePattern);
                rlReturnHome.setVisibility(View.INVISIBLE);
                rlSingleOrMorePattern.setVisibility(View.VISIBLE);
                rlSelectionPattern.setVisibility(View.INVISIBLE);
                if(singleOrMorePatternFragment==null){
                    singleOrMorePatternFragment=new SingleOrMorePatternFragment();
                    fragmentTransaction.add(R.id.modeFragment,singleOrMorePatternFragment);
                }else{
                    fragmentTransaction.show(singleOrMorePatternFragment);
                }
                break;
            case R.id.tv_returnhome:
                setTextViewAlphaChange(tvReturnHome);
                rlSelectionPattern.setVisibility(View.INVISIBLE);
                rlSingleOrMorePattern.setVisibility(View.INVISIBLE);
                rlReturnHome.setVisibility(View.VISIBLE);
    
                ActivityCollector.finishAll();
                
                break;
        }
        fragmentTransaction.commit();
    }
    //隐藏所有的fragment
    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(selectionPatternFragment!=null){
            fragmentTransaction.hide(selectionPatternFragment);
        }
        if(singleOrMorePatternFragment!=null){
            fragmentTransaction.hide(singleOrMorePatternFragment);
        }
    }
    private void initTextViewColor(){
        tvSingleOrMorePattern.setTextColor(getResources().getColor(R.color.textNoSelect));
        tvSelectionPattern.setTextColor(getResources().getColor(R.color.textNoSelect));
        tvReturnHome.setTextColor(getResources().getColor(R.color.textNoSelect));
        
    }
}
