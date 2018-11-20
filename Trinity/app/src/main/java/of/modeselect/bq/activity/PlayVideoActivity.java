package of.modeselect.bq.activity;
import of.modeselect.bq.R;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;


import of.modeselect.bq.bean.Video;
import of.modeselect.bq.service.MusicService;
import of.modeselect.bq.toast.OneToast;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends AppCompatActivity {
    private static List<Video> videoList=new ArrayList<>();
    private int position=-1;
    private VideoView videoView;
    private  boolean isPlayingMuisc=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_play_video);
        if(getIntent()!=null){
            position=getIntent().getIntExtra("position",0);
        }
        initView();
        initData();
        initListener();
        
        
    }
    
    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    private void initData() {
        if(!isCanPlay()){
            OneToast.showMessage(PlayVideoActivity.this,"当前播放列表为空");
            finish();
            return;
        }
        videoView.setVideoPath(videoList.get(position).getVideoPath());
        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//下一个视频
                nextVideo(1);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {//上一个视频
                prevVideo();
            }
        });
        //setVideoViewLayoutParams(1);//全屏
        
    }
    
    private void initListener() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextVideo(0);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                videoList.remove(position);
                nextVideo(1);
                return false;
            }
        });
    }
    
    
    /**
     * 设置videiview的全屏和窗口模式
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams(int paramsType) {
        //全屏模式
        if (1 == paramsType) {
            //设置充满整个父布局
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            //设置相对于父布局四边对齐
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        } else {
            //窗口模式
            //获取整个屏幕的宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            //设置窗口模式距离边框50
            int videoHeight = DisplayMetrics.heightPixels - 50;
            int videoWidth = DisplayMetrics.widthPixels - 50;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            //设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            //为VideoView添加属性
            videoView.setLayoutParams(LayoutParams);
        }
        
    }
    
    private void prevVideo(){
        
        saveVideoProgress();
        int size=videoList.size();
        if(!isCanPlay()){
            OneToast.showMessage(PlayVideoActivity.this,"当前播放列表为空");
            finish();
            return;
        }
        position=position==0?size-1:position-1;
        Log.i("video", "prevVideo: "+position+"//"+videoList.get(position).getProgress());
        videoView.setVideoPath(videoList.get(position).getVideoPath());
        videoView.seekTo(videoList.get(position).getProgress());
        videoView.start();
    }
    
    /**
     *
     * @param i
     * i=0时为自动播放
     * i=1是为下一个视频，不是自动播放
     */
    private void nextVideo(int i){
        if(i==1){
            saveVideoProgress();
        }else{
            if(isCanPlay()){
                Video video=videoList.get(position);
                video.setProgress(0);
                videoList.set(position,video);
                Log.i("video", "nextVideo: "+position+"//"+videoList.get(position).getProgress());
            }
        }
        
        int size=videoList.size();
        if(!isCanPlay()){
            OneToast.showMessage(PlayVideoActivity.this,"当前播放列表为空");
            finish();
            return;
        }
        position=position==size-1?0:position+1;
        videoView.setVideoPath(videoList.get(position).getVideoPath());
        videoView.seekTo(videoList.get(position).getProgress());
        videoView.start();
    }
    
    
    private void stopPlaybackVideo(){
        try{
            videoList.clear();
            videoView.pause();
            videoView.stopPlayback();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    //保存播放进度
    private void saveVideoProgress(){
        if(isCanPlay()){
            Video video=videoList.get(position);
            video.setProgress(videoView.getCurrentPosition());
            videoList.set(position,video);
        }
    }
    
    //判断是否可以播放视频
    private boolean isCanPlay(){
        if(position==-1||videoList.size()==0){
            return false;
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("trinity15", "onStart: ");
        isPlayingMuisc= MusicService.isPlaying();
        if(isPlayingMuisc){
            final Intent intent1=new Intent(PlayVideoActivity.this,MusicService.class);
            intent1.setAction(MusicService.PAUSE_ACTION);
            startService(intent1);
        }
    }
    
    @Override
    protected void onResume() {
        Log.i("trinity15", "onResume: ");
        super.onResume();
        if(!videoView.isPlaying()){
            videoView.resume();
            videoView.seekTo(videoList.get(position).getProgress());
        }
    }
    
    @Override
    protected void onPause() {
        Log.i("trinity15", "onPause: ");
        super.onPause();
        saveVideoProgress();
        if(videoView.isPlaying()){
            videoView.pause();
        }
    }
    
    @Override
    protected void onDestroy() {
        Log.i("trinity15", "onDestroy: ");
        super.onDestroy();
        stopPlaybackVideo();
        if(isPlayingMuisc){
            final    Intent intent1=new Intent(PlayVideoActivity.this,MusicService.class);
            intent1.setAction(MusicService.START_ACTION);
            startService(intent1);
        }
        
    }
    
    private void initView() {
        videoView = this.findViewById(R.id.videoView);
        
    }
    
    public static List<Video> getVideoList() {
        return videoList;
    }
    
    public static void setVideoList(List<Video> videoList) {
        PlayVideoActivity.videoList = videoList;
    }
}
