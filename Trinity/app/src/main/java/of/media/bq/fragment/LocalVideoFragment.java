package of.media.bq.fragment;
import of.media.bq.R;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import of.media.bq.adapter.LocalVideoAdapter;
import of.media.bq.bean.Video;
import of.media.bq.localInformation.FileManger;
import of.media.bq.saveData.SaveData;
import of.media.bq.toast.OneToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalVideoFragment extends Fragment {

    private    GridView localVideoGridView;
    private  LocalVideoAdapter localVideoAdapter;
    private  boolean isFirstLoad=true;//判断是否第一次加载

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_video_fragment,container,false);
        initView(view);
        initData();
        initListener();
        return view;
    }





    private void initData() {

        List<Video> videoList=initVideoList();
        SaveData.setLocalVideoList(videoList);
        localVideoAdapter = new LocalVideoAdapter(videoList);
        localVideoGridView.setNumColumns(4);
        localVideoGridView.setAdapter(localVideoAdapter);
    }
  private List<Video> initVideoList(){
      List<Video> videoList=new ArrayList<>();
      List<Video> videoAddress= FileManger.getInstance(getContext()).getVideos();//获取测试的视频地址

      if(videoAddress==null||videoAddress.size()<4){

          OneToast.showMessage(getContext(),"请导入至少4个mp4视频到本地");
        return videoList;
      }else{
          Video video=new Video();
          video.setVideoName("变形金刚4:绝迹重生");
          video.setVideoThumbnail(R.drawable.vp1);
          video.setVideoPath(videoAddress.get(0).getVideoPath());
          videoList.add(video);

          video=new Video();
          video.setVideoName("蜘蛛侠");
          video.setVideoThumbnail(R.drawable.vp2);
          video.setVideoPath(videoAddress.get(1).getVideoPath());
          videoList.add(video);

          video=new Video();
          video.setVideoName("复仇者联盟");
          video.setVideoThumbnail(R.drawable.vp3);
          video.setVideoPath(videoAddress.get(3).getVideoPath());
          videoList.add(video);

          video=new Video();
          video.setVideoName("钢铁侠");
          video.setVideoThumbnail(R.drawable.vp4);
          video.setVideoPath(videoAddress.get(4).getVideoPath());
          videoList.add(video);

          return  videoList;
      }

  }


    private List<String> getVideoAddress(){
        //adb push  G:\Trinity_project\Video\. /storage/emulated/0/Movies
        List<String> videoAddress=new ArrayList<>();
        String sdAddress=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//sd卡是否存在
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//sd卡存储路径
        }else {
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//本地存储路径
        }
        sdAddress+="/Movies/";

        File file=new File(sdAddress);
        if(!file.exists()||file.isFile()){
            file.mkdir();
            return null;
        }else{
            File[] files=file.listFiles();
            for(File file1:files){
                if(file1.isFile()){
                    if (file1.getAbsolutePath().endsWith(".mp4")){
                        videoAddress.add(file1.getAbsolutePath());
                    }
                }

            }
        }
        return videoAddress;
    }
    private void initView(View view) {
        localVideoGridView = view.findViewById(R.id.localVideoGridView);
    }


    private void initListener() {
//        localVideoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                OneToast.showMessage(getContext(),i+"");
//                if(localVideoAdapter.getPosition()==i){//播放视频
//                    PlayVideoActivity.setVideoList(localVideoAdapter.getVideoList());
//                    Intent intent=new Intent(getContext(),PlayVideoActivity.class);
//                    intent.putExtra("position",i);
//                    getContext().startActivity(intent);
//                }else{
//                    selectVideosByIndex(i);
//                }
//
//            }
//        });

    }



    @Override
    public void onResume() {
        super.onResume();

        if(!isFirstLoad){
           // localVideoGridView.setAdapter(localVideoAdapter);
           // localVideoAdapter.notifyDataSetChanged();
            Log.i("trinity18", "onResume: "+localVideoAdapter.getCount()+"//"+SaveData.getLocalVideoList().size());
            if(localVideoAdapter.getCount()==0){
              localVideoAdapter.setVideoList(SaveData.getLocalVideoList());
            }

        }
         isFirstLoad=false;
    }


    //选中下标为index的视频时
    public void selectVideosByIndex(int index){
        localVideoAdapter.setPosition(index);
        TransitionSet transitionSet=new TransitionSet();
        Fade fade=new Fade();
        Slide slide=new Slide();
        transitionSet.addTransition(slide).addTransition(fade);
        TransitionManager.beginDelayedTransition(localVideoGridView,transitionSet);
        localVideoAdapter.notifyDataSetChanged();

    }
}
