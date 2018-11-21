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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;
import of.modeselect.bq.saveData.ActivityCollector;

public class ModeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rlSelectionPattern;
    
    private RelativeLayout rlReturnHome;
    private TextView tvSelectionPattern;
    
    private TextView tvReturnHome;
    
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
                tvReturnHome.callOnClick();
                break;
            default:
                break;
        }
    }
    private void initEvent() {
        tvReturnHome.setOnClickListener(this);
        tvSelectionPattern.setOnClickListener(this);
        
    }
    private void initView(View view) {
        rlSelectionPattern=view.findViewById(R.id.rl_selectionpattern);
        
        rlReturnHome=view.findViewById(R.id.rl_returnhome);
        tvReturnHome=view.findViewById(R.id.tv_returnhome);
        tvSelectionPattern=view.findViewById(R.id.tv_selectionpattern);
        
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
                tvSelectionPattern.setTextColor(getResources().getColor(R.color.textSelect));
                rlSelectionPattern.setVisibility(View.VISIBLE);
                
                rlReturnHome.setVisibility(View.INVISIBLE);
                if(selectionPatternFragment==null){
                    selectionPatternFragment=new SelectionPatternFragment();
                    fragmentTransaction.add(R.id.modeFragment,selectionPatternFragment);
                }else{
                    fragmentTransaction.show(selectionPatternFragment);
                }
                break;
            
            case R.id.tv_returnhome:
                setTextViewAlphaChange(tvReturnHome);
                tvReturnHome.setTextColor(getResources().getColor(R.color.textSelect));
                rlSelectionPattern.setVisibility(View.INVISIBLE);
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
        
    }
    private void initTextViewColor(){
        
        tvSelectionPattern.setTextColor(getResources().getColor(R.color.textNoSelect));
        tvReturnHome.setTextColor(getResources().getColor(R.color.textNoSelect));
        
    }
}
