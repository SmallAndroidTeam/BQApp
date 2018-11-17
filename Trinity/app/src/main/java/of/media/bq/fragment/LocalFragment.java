package of.media.bq.fragment;
import of.media.bq.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import of.meida.bq.convertPXAndDP.DensityUtil;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalFragment extends Fragment implements View.OnClickListener {


    private TextView musicTextView;
    private TextView videoTextView;
    private TextView galleryTextView;
    private TableLayout slideLine;
    private Fragment localMusicFragment,localVideoFragment,localGalleryFragment;
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
        slideLine = view.findViewById(R.id.localSlideLine);
       ViewGroup.LayoutParams layoutParams=  slideLine.getLayoutParams();
       layoutParams.width= DensityUtil.getWidth(musicTextView);
       slideLine.setLayoutParams(layoutParams);
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
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction().setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_left_out,R.anim.card_flip_left_in,R.anim.card_flip_right_out);
        hideAllFragment(fragmentTransaction);
        final LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) slideLine.getLayoutParams();

    final   ChangeBounds changeBounds=new ChangeBounds();
      changeBounds.setDuration(800);
      changeBounds.setPathMotion(new ArcMotion());

       TransitionManager.beginDelayedTransition((ViewGroup) slideLine.getParent(),changeBounds);
        setTextViewColorNoSelect();
     switch (index){
         case 0:
             musicTextView.setTextColor(getResources().getColor(R.color.textSelect));
             int left=musicTextView.getLeft();
             layoutParams.leftMargin=left;
             slideLine.setLayoutParams(layoutParams);
             if(localMusicFragment==null){
                 localMusicFragment=new LocalMusicFragment();
                 fragmentTransaction.add(R.id.localFrameLayout,localMusicFragment);
             }else{
                 fragmentTransaction.show(localMusicFragment);
             }
             break;
         case  1:
             videoTextView.setTextColor(getResources().getColor(R.color.textSelect));
             int  left1=videoTextView.getLeft();
             layoutParams.leftMargin=left1;
             slideLine.setLayoutParams(layoutParams);
             if(localVideoFragment==null){
                 localVideoFragment=new LocalVideoFragment();
                 fragmentTransaction.add(R.id.localFrameLayout,localVideoFragment);

             }else{
                 fragmentTransaction.show(localVideoFragment);
             }
             break;
         case 2:
             galleryTextView.setTextColor(getResources().getColor(R.color.textSelect));
             int left2=galleryTextView.getLeft();
             layoutParams.leftMargin=left2;
             slideLine.setLayoutParams(layoutParams);
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
    //设置音乐、视频、图片的字体颜色为为选中的颜色
    public void setTextViewColorNoSelect(){
     musicTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
        videoTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
        galleryTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
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
