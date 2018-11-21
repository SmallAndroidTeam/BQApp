package of.modeselect.bq.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import of.modeselect.bq.R;

public class WorkingModeFragment extends Fragment implements View.OnClickListener {
    private  TextView tv_leftnote;
    private TextView tv_leftinternet;
    private TextView tv_rightnote;
    private TextView tv_rightinternet;
    private ImageView iv_limage;
    private ImageView iv_rimage;
    private Fragment internetFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_working_mode,container,false);
        initView(view);
        initEvents();
        setTab(0);
        initViewColor();
        return view;
    }
    
    private void setTab(int i) {
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (i){
            case 0:
                tv_rightinternet.getPaint().setFakeBoldText(true);
                tv_rightinternet.setTextColor(0xffffffff);
                tv_leftinternet.getPaint().setFakeBoldText(true);
                tv_leftinternet.setTextColor(0xffffffff);
                tv_rightnote.getPaint().setFakeBoldText(false);
                tv_rightnote.setTextColor(0x80ffffff);
                tv_leftnote.getPaint().setFakeBoldText(false);
                tv_leftnote.setTextColor(0x80ffffff);
                
                iv_limage.setImageResource(R.drawable.bg_local);
                iv_rimage.setImageResource(R.drawable.bg_rinternet);
                if(internetFragment==null){
                    internetFragment=new InternetFragment();
                    fragmentTransaction.add(R.id.workFragment,internetFragment);
                }else {
                    fragmentTransaction.show(internetFragment);
                }
                break;
            case 1:
                tv_rightinternet.getPaint().setFakeBoldText(false);
                tv_rightinternet.setTextColor(0x80ffffff);
                tv_leftinternet.getPaint().setFakeBoldText(false);
                tv_leftinternet.setTextColor(0x80ffffff);
                tv_rightnote.getPaint().setFakeBoldText(true);
                tv_rightnote.setTextColor(0xffffffff);
                tv_leftnote.getPaint().setFakeBoldText(true);
                tv_leftnote.setTextColor(0xffffffff);
                iv_rimage.setImageResource(R.drawable.bg_rnote);
                iv_limage.setImageResource(R.drawable.bg_online);
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }
    
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(internetFragment!=null){
            fragmentTransaction.hide(internetFragment);
        }
    }
    
    private void initViewColor() {
        tv_rightnote.setTextColor(getResources().getColor(R.color.dialog));
        tv_leftinternet.setTextColor(getResources().getColor(R.color.dialog));
        tv_rightinternet.setTextColor(getResources().getColor(R.color.dialog));
        tv_leftinternet.setTextColor(getResources().getColor(R.color.dialog));
    }
    
    private void initEvents() {
        tv_leftinternet.setOnClickListener(this);
        tv_leftnote.setOnClickListener(this);
        tv_rightinternet.setOnClickListener(this);
        tv_rightnote.setOnClickListener(this);
    }
    
    private void initView(View view) {
        tv_leftnote = view.findViewById(R.id.tv_leftnote);
        tv_leftinternet = view.findViewById(R.id.tv_leftinternet);
        tv_rightnote = view.findViewById(R.id.tv_rightnote);
        tv_rightinternet = view.findViewById(R.id.tv_rightinternet);
        iv_limage = view.findViewById(R.id.iv_lImage);
        iv_rimage = view.findViewById(R.id.iv_rImage);
        
    }
    
    @Override
    public void onClick(View v) {
        
        switch (v.getId()){
            case R.id.tv_leftinternet:
            case R.id.tv_rightinternet:
                setTab(0);
                
                break;
            case R.id.tv_leftnote:
            case R.id.tv_rightnote:
                setTab(1);
                
                break;
            default:
                break;
        }
    }
    
}
