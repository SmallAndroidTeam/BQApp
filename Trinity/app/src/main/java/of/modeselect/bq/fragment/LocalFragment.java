package of.modeselect.bq.fragment;
import of.modeselect.bq.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import of.modeselect.bq.convertPXAndDP.DensityUtil;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalFragment extends Fragment implements View.OnClickListener {


    private TextView musicTextView;
    private TextView videoTextView;
    private TextView galleryTextView;

    private Fragment localMusicFragment,localVideoFragment,localGalleryFragment;
    private ImageView localMusicUnderline;
    private ImageView localVideoUnderline;
    private ImageView localGalleryUnderline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_fragment,container,false);
        initView(view);
        initListener();
           setTab(0);
        return view;
    }


    private void initListener() {
        musicTextView.setOnClickListener(this);
        videoTextView.setOnClickListener(this);
        galleryTextView.setOnClickListener(this);
    }

    private void initView(View view) {
        musicTextView = view.findViewById(R.id.localMusicTextView);
        videoTextView = view.findViewById(R.id.localVideoTextView);
        galleryTextView = view.findViewById(R.id.localGalleryTextView);
        localMusicUnderline = view.findViewById(R.id.localMusicUnderline);
        localVideoUnderline = view.findViewById(R.id.localVideoUnderline);
        localGalleryUnderline = view.findViewById(R.id.localGalleryUnderline);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.localMusicTextView:
               setTab(0);
                break;
            case R.id.localVideoTextView:
                setTab(1);
                break;
            case R.id.localGalleryTextView:
                setTab(2);
                default:
                    break;
        }
    }

    //选中音乐、视频、图片对应的下滑线的位置变化，且切换viwePage
    //index=0：选中音乐
    //index=1：选中视频
    //index=2：选中图片
    public void setTab(int index){
        final      FragmentManager fragmentManager= this.getFragmentManager();
        assert fragmentManager != null;
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction().setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_left_out,R.anim.card_flip_left_in,R.anim.card_flip_right_out);
        hideAllFragment(fragmentTransaction);
        setTextViewColorAndShowUnderLineByIndex(index);
     switch (index){
         case 0:
             musicTextView.setTextColor(getResources().getColor(R.color.textSelect));
             if(localMusicFragment==null){
                 localMusicFragment=new LocalMusicFragment();
                 fragmentTransaction.add(R.id.localFrameLayout,localMusicFragment);
             }else{
                 fragmentTransaction.show(localMusicFragment);
             }
             break;
         case  1:
             videoTextView.setTextColor(getResources().getColor(R.color.textSelect));
             if(localVideoFragment==null){
                 localVideoFragment=new LocalVideoFragment();
                 fragmentTransaction.add(R.id.localFrameLayout,localVideoFragment);

             }else{
                 fragmentTransaction.show(localVideoFragment);
             }
             break;
         case 2:
             galleryTextView.setTextColor(getResources().getColor(R.color.textSelect));
             if(localGalleryFragment==null){
                 localGalleryFragment=new LocalGalleryFragment();
                 fragmentTransaction.add(R.id.localFrameLayout,localGalleryFragment);
             }else{
                 fragmentTransaction.show(localGalleryFragment);
             }
             break;
             default:
                 break;
     }
     fragmentTransaction.commit();
    }

    /**
     *
     * @param index
     * index=0;选中音乐时
     * index=1;选中视频时
     * index=2;选中图片时
     */
    public void setTextViewColorAndShowUnderLineByIndex(int index){
     musicTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
        videoTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
        galleryTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
        final Fade fade=new Fade();
        TransitionManager.beginDelayedTransition((ViewGroup) localMusicUnderline.getParent(),fade);
        TransitionManager.beginDelayedTransition((ViewGroup) localVideoUnderline.getParent(),fade);
        TransitionManager.beginDelayedTransition((ViewGroup) localGalleryUnderline.getParent(),fade);
        localMusicUnderline.setVisibility(View.INVISIBLE);
        localVideoUnderline.setVisibility(View.INVISIBLE);
        localGalleryUnderline.setVisibility(View.INVISIBLE);
        switch (index){
            case 0:
                musicTextView.setTextColor(0xffffffff);//100%透明度，颜色为#ffffff
                videoTextView.setTextColor(0x80ffffff);//50%透明度，颜色为#ffffff
                galleryTextView.setTextColor(0x80ffffff);
                localMusicUnderline.setVisibility(View.VISIBLE);
                break;
            case 1:
                musicTextView.setTextColor(0x80ffffff);
                videoTextView.setTextColor(0xffffffff);
                galleryTextView.setTextColor(0x80ffffff);
                localVideoUnderline.setVisibility(View.VISIBLE);
                break;
            case 2:
                musicTextView.setTextColor(0x80ffffff);
                videoTextView.setTextColor(0x80ffffff);
                galleryTextView.setTextColor(0xffffffff);
                localGalleryUnderline.setVisibility(View.VISIBLE);
                break;
                default:
                    break;
        }
    }

    public void hideAllFragment(FragmentTransaction fragmentTransaction){
      if(localMusicFragment!=null){
          fragmentTransaction.hide(localMusicFragment);
      }
      if(localVideoFragment!=null){
          fragmentTransaction.hide(localVideoFragment);
      }
      if(localGalleryFragment!=null){
          fragmentTransaction.hide(localGalleryFragment);
      }
    }
}
