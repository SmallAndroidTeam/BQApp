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
public class OnlineFragment extends Fragment implements View.OnClickListener {
    private TextView musicTextView;
    private TextView videoTextView;
    private TextView galleryTextView;
    private TableLayout slideLine;
    private Fragment onlineMusicFragment,onlineVideoFragment,onlineGalleryFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.online_fragment,container,false);
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
        musicTextView = view.findViewById(R.id.onlineMusicTextView);
        videoTextView = view.findViewById(R.id.onlineVideoTextView);
        galleryTextView = view.findViewById(R.id.onlineGalleryTextView);
        slideLine = view.findViewById(R.id.onlineSlideLine);
        ViewGroup.LayoutParams layoutParams=  slideLine.getLayoutParams();
        layoutParams.width= DensityUtil.getWidth(musicTextView);
        slideLine.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onlineMusicTextView:
                setTab(0);
                break;
            case R.id.onlineVideoTextView:
                setTab(1);
                break;
            case R.id.onlineGalleryTextView:
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
        final LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) slideLine.getLayoutParams();

        final ChangeBounds changeBounds=new ChangeBounds();
        changeBounds.setDuration(500);
        changeBounds.setPathMotion(new ArcMotion());
        TransitionManager.beginDelayedTransition((ViewGroup) slideLine.getParent(),changeBounds);
        setTextViewColorNoSelect();

        final  FragmentManager fragmentManager=getFragmentManager();
       final   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
       hideAllFragment(fragmentTransaction);
        switch (index){
            case 0:
                musicTextView.setTextColor(getResources().getColor(R.color.textSelect));
                int left=musicTextView.getLeft();
                layoutParams.leftMargin=left;
                slideLine.setLayoutParams(layoutParams);
                  if(onlineMusicFragment==null){
                      onlineMusicFragment=new OnlineMusicFragment();
                      fragmentTransaction.add(R.id.onlineFrameLayout,onlineMusicFragment);
                  }else{
                      fragmentTransaction.show(onlineMusicFragment);
                  }
                break;
            case  1:
                videoTextView.setTextColor(getResources().getColor(R.color.textSelect));
                int  left1=videoTextView.getLeft();
                layoutParams.leftMargin=left1;
                slideLine.setLayoutParams(layoutParams);
                if(onlineVideoFragment==null){
                    onlineVideoFragment=new OnlineVideoFragment();
                    fragmentTransaction.add(R.id.onlineFrameLayout,onlineVideoFragment);
                }else{
                    fragmentTransaction.show(onlineVideoFragment);
                }
                break;
            case 2:
                galleryTextView.setTextColor(getResources().getColor(R.color.textSelect));
                int left2=galleryTextView.getLeft();
                layoutParams.leftMargin=left2;
                slideLine.setLayoutParams(layoutParams);
                if(onlineGalleryFragment==null){
                    onlineGalleryFragment=new OnlineGalleryFragment();
                    fragmentTransaction.add(R.id.onlineFrameLayout,onlineGalleryFragment);
                }else{
                    fragmentTransaction.show(onlineGalleryFragment);
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
        if(onlineMusicFragment!=null){
            fragmentTransaction.hide(onlineMusicFragment);
        }
        if(onlineVideoFragment!=null){
            fragmentTransaction.hide(onlineVideoFragment);
        }
        if(onlineGalleryFragment!=null){
            fragmentTransaction.hide(onlineGalleryFragment);
        }
    }
}
