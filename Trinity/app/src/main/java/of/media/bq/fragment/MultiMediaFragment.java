package of.media.bq.fragment;
import of.media.bq.R;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import of.media.bq.adapter.IconAdapter;
import of.meida.bq.convertPXAndDP.DensityUtil;
import of.media.bq.localInformation.App;


public class MultiMediaFragment extends Fragment {
    private List<Integer> iconIdList = new ArrayList<Integer>(){};
    private IconAdapter iconAdapter;
    private final static int iconWidth=60;//图标的宽度(单位px)
    private  final  static  int iconSpace=60;//图标的间距(单位px)
    private TextView onlineTextview;
    private TextView localTextview;
    private TableLayout topSplitLine;
    private ImageView leftTriangleImageview;
    private TableLayout bottomSplitLine;

    private final  static String TAG="trinity11";
    private final static int UPDATE_TIME=0;//更新时间
    private Fragment onlineFragment,localFragment;
    public static  boolean isExist=false;//判断多媒体是否运行

    private Timer timer;
    private FrameLayout indexFragmeLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.multi_media_fragment_item,container,false);
        isExist=true;
        initView(view);
        initListener();
        seletTab(1);//一开始选中本地
        App.sContext=getContext();
        view.post(new Runnable() {
            @Override
            public void run() {
                initLocalPosition();
            }
        });
        return view;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        isExist=false;
    }
    
    //添加除去U盘图标的ID
    private void addIconExceptUdiskIcon(){
        iconIdList.clear();
        iconIdList.add(R.drawable.gps);
        iconIdList.add(R.drawable.signal);
        iconIdList.add(R.drawable.navigation);
        iconIdList.add(R.drawable.bluetooth);
    }

    private void initView(View view) {
      
        onlineTextview = view.findViewById(R.id.online_textview);
        localTextview = view.findViewById(R.id.local_textview);
        topSplitLine = view.findViewById(R.id.top_split_line);
        leftTriangleImageview = view.findViewById(R.id.left_triangle_imageview);
        bottomSplitLine = view.findViewById(R.id.bottom_split_line);
        indexFragmeLayout = view.findViewById(R.id.indexFragmeLayout);
        
    }
    
    private void initListener() {
        
        onlineTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletTab(0);
            }
        });
        localTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletTab(1);
            }
        });
    }
    
    //i=0代表选中在线，i=1代表选中本地
    public void seletTab(int i){
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction().setCustomAnimations(R.anim.card_flip_top_in,R.anim.card_flip_top_out);
        hideOnlineAndLocalFragment(fragmentTransaction);  //隐藏在线和本地的Fragment
        switch (i){
            case 0:
                onlineTextview.setTextColor(getResources().getColor(R.color.textSelect));
                localTextview.setTextColor(getResources().getColor(R.color.textNoSelect));
                initOnlinePosition();
                if(onlineFragment==null){
                    onlineFragment=new OnlineFragment();
                    fragmentTransaction.add(R.id.indexFragmeLayout,onlineFragment);
                }else{
                    fragmentTransaction.show(onlineFragment);
                }
                
                break;
            case 1:
                onlineTextview.setTextColor(getResources().getColor(R.color.textNoSelect));
                localTextview.setTextColor(getResources().getColor(R.color.textSelect));
                initLocalPosition();
                
                if(localFragment==null){
                    localFragment=new LocalFragment();
                    fragmentTransaction.add(R.id.indexFragmeLayout,localFragment);
                }else{
                    fragmentTransaction.show(localFragment);
                }
                
                break;
            default:
                break;
        }
        
        fragmentTransaction.commit();
    }
    
    
    //隐藏在线和本地的Fragment
    public void hideOnlineAndLocalFragment(FragmentTransaction fragmentTransaction){
        
        if(onlineFragment!=null){
            fragmentTransaction.hide(onlineFragment);
        }
        if(localFragment!=null){
            fragmentTransaction.hide(localFragment);
        }
    }
    
    
    
    //初始化选中本地时 图标的位置
    public void initLocalPosition(){
        //获取屏幕高度
        final long screentHeight= DensityUtil.getWindowHeight(getContext());
        
        final int coverHeigt=4;//设置覆盖的高度
        //获取顶部RelationLayout的高度
        final int   topRelationHeight=100;
        //获取本地文字 距离屏幕顶部的距离
        final   int localTextViewScreenTopDistance=DensityUtil.getTopDistance(localTextview);
        final int leftTriangleHeight=DensityUtil.getHeight(leftTriangleImageview);//获取左三角的高度
        Log.i(TAG, "screentHeight:"+screentHeight+"\nlocalTextViewScreenTopDistance: "+localTextViewScreenTopDistance+"\ntopRelationHeight:"+topRelationHeight+"\n");
        int height=localTextViewScreenTopDistance-topRelationHeight;//顶部分割线的高度
        int leftTriangleMarginTop=height-coverHeigt;
        int bottomSplitLineMarginTop=height+leftTriangleHeight-3*coverHeigt+2;
        
        setSplitLinePosition(height+coverHeigt+2,leftTriangleMarginTop,bottomSplitLineMarginTop);
    }
    
    //初始化选中在线时 图标的位置
    public void initOnlinePosition(){
        
        final int coverHeigt=4;//设置覆盖的高度
        //获取顶部RelationLayout的高度
        final int   topRelationHeight=100;
        //获取在线文字 距离屏幕顶部的距离
        final   int onlineTextViewScreenTopDistance=DensityUtil.getTopDistance(onlineTextview);
        
        final int leftTriangleHeight=DensityUtil.getHeight(leftTriangleImageview);//获取左三角的高度
        int height=onlineTextViewScreenTopDistance-topRelationHeight;//顶部分割线的高度
        int leftTriangleMarginTop=height-coverHeigt;
        int bottomSplitLineMarginTop=height+leftTriangleHeight-3*coverHeigt+2;
        setSplitLinePosition(height+coverHeigt+2,leftTriangleMarginTop,bottomSplitLineMarginTop);
    }
    
    //设置分割线的位置
    public void setSplitLinePosition(int height,int leftTriangleMarginTop,int bottomSplitLineMarginTop){
        Log.i(TAG, "height:"+height+"\nleftTriangleMarginTop:"+leftTriangleMarginTop+"\nbottomSplitLineMarginTop:"+bottomSplitLineMarginTop);
        
        final ChangeBounds changeBounds=new ChangeBounds();
        changeBounds.setPathMotion(new ArcMotion());
        changeBounds.setDuration(500);
        TransitionManager.beginDelayedTransition((ViewGroup) topSplitLine.getParent(),changeBounds);
        
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) topSplitLine.getLayoutParams();
        layoutParams.height=height;
        topSplitLine.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams) leftTriangleImageview.getLayoutParams();
        layoutParams1.topMargin=leftTriangleMarginTop;
        leftTriangleImageview.setLayoutParams(layoutParams1);
        RelativeLayout.LayoutParams layoutParams2= (RelativeLayout.LayoutParams) bottomSplitLine.getLayoutParams();
        layoutParams2.topMargin=bottomSplitLineMarginTop;
        bottomSplitLine.setLayoutParams(layoutParams2);
        
    }
    
}
