package of.modeselect.bq.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.MediaController;
import android.widget.RemoteViews;

import of.modeselect.bq.R;
import of.modeselect.bq.bean.Music;
import of.modeselect.bq.localInformation.MusicIconLoader;
import of.modeselect.bq.toast.OneToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MusicService extends Service {
    public final static String TOGGLEPAUSE_ACTION="music.toggle";//切换播放状态
    public final  static String STOP_ACTION="music.stop";//停止播放
    public final  static String PLAY_ACTION="music.play";//播放
    public final  static String PREV_ACTION="music.prev";//上一首
    public final  static String NEXT_ACTION="music.next";//下一首
    public final  static String PAUSE_ACTION="music.pause";//暂停
    public final  static String START_ACTION="music.continue";//继续播放
    public final static String OPENMUSIC_ACTION="music.open";//打开音乐
    public final static String CLOSEMUSIC_ACTION="music.close";//关闭音乐
    public final static String RANDOM_ACTION="music.random";//随机
    public final static String LOOPALL_ACTION="music.loop.all";//顺序播放
    public final static String LOOPSINGLE_ACTION="music.loop.single";//单曲循环
    public final static String LOOPRANDOM_ACTION="music.loop.random";//随机播放
    public final static String LISTOPEN_ACTION="music.list.open";//打开音乐列表
    public final static String LISTCLOSE_ACTION="music.list.close";//关闭音乐列表
    public final static String FAVOUR_ACTION="music.favour";//收藏
    public final static String UNFAVOUR_ACTION="music.unfavour";//取消收藏
    public final static String FAVOUROPEN_ACTION="music.favour.open";//打开收藏列表
    public final static String UNFAVOURCLOSE_ACTION="music.unfavour.close";//关闭收藏列表
    public final  static String SEND_MUSIC_SERVICE_FLAG="UserOperation";//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
    private  static  MediaPlayer mMediaPlayer;
    private  static List<Music> musicList=new ArrayList<>();//存储播放的音乐列表
    private static int currentPosition=-1;//设置默认的播放下标
    private static Control mControl;
    private static boolean ForegroundIsExist=false;//判断前台服务是否存在
    private static final int NotificationId=1002;
    private static Notification mNotification;
    private static NotificationManager notificationManager;
    private RemoteViews widgetRemoteViews;
    public static List<Music> getMusicList() {
        return musicList;
    }
    
    public static void setmControl(Control mControl) {
        MusicService.mControl = mControl;
    }
    
    public static void setMusicList(List<Music> musicList) {
        MusicService.musicList = musicList;
    }
    
    public static int getCurrentPosition() {
        return currentPosition;
    }
    public final static String FLAG="autoplay";//自动播放的标志
    public final static String TAG="bq111";
    public static void setCurrentPosition(int currentPosition) {
        MusicService.currentPosition = currentPosition;
    }

    private  final BroadcastReceiver foregroundIntentReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null&&intent.getAction()!=null){
                handleCommandIntent(intent,1);
            }
        }
    };

    //初始化服务
    public static void initMusicService(List<Music> list, int Position){
        if(list==null||list.size()==0||Position<0||Position>list.size()-1){
            return;
        }
        if(mMediaPlayer==null){
            mMediaPlayer=new MediaPlayer();
        }
        musicList.clear();
        musicList.addAll(list);
        currentPosition=Position;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(musicList.get(currentPosition).getUri());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public MusicService() {
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        if(mMediaPlayer!=null){
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    
                    int position=currentPosition;
                    if(mControl!=null){
                        mControl.autoPlay();
                    }else{
                        nextMusic(FLAG,null);
                    }
                }
            });
        }

        widgetRemoteViews=new RemoteViews(getPackageName(),R.layout.notification);
        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //注册前台服务广播
        final  IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(TOGGLEPAUSE_ACTION);
        intentFilter.addAction(PREV_ACTION);
        intentFilter.addAction(NEXT_ACTION);
        intentFilter.addAction(STOP_ACTION);
        registerReceiver(foregroundIntentReceiver,intentFilter);
    }
    

    //初始化前台服务
    private  boolean initNotification(){
        if(isCanPlay()){
            widgetRemoteViews.setTextViewText(R.id.widget_content,musicList.get(currentPosition).getTitle());//设置歌名
            if(musicList.get(currentPosition).getImage()!=null){//如果音乐专辑图片存在
                Bitmap bitmap= MusicIconLoader.getInstance().load(musicList.get(currentPosition).getImage());
                if(bitmap!=null){
                    widgetRemoteViews.setImageViewBitmap(R.id.widget_image,bitmap);
                }else{
                    widgetRemoteViews.setImageViewResource(R.id.widget_image,R.drawable.mp1);
                }
            }else{
                widgetRemoteViews.setImageViewResource(R.id.widget_image,R.drawable.mp1);
            }
            if(mMediaPlayer.isPlaying()){
                widgetRemoteViews.setImageViewResource(R.id.widget_play,R.drawable.widget_play_selector);
            }else{
                widgetRemoteViews.setImageViewResource(R.id.widget_play,R.drawable.pause_imageview);
            }
            return true;
        }else{
            return false;
        }
    }

    private Notification getmNotification(){

        final Context ForegroundContext=this;
        if(!initNotification()){//如果初始化服务失败
            return null;
        }

        //设置前台绑定事件
         Intent toggleIntent=new Intent(TOGGLEPAUSE_ACTION);
        PendingIntent togglePItent=PendingIntent.getBroadcast(ForegroundContext,0,toggleIntent,0);
        widgetRemoteViews.setOnClickPendingIntent(R.id.widget_play,togglePItent);

        Intent nextIntent=new Intent(NEXT_ACTION);
        PendingIntent nextPIntent=PendingIntent.getBroadcast(ForegroundContext,0,nextIntent,0);
        widgetRemoteViews.setOnClickPendingIntent(R.id.widget_next,nextPIntent);

        Intent prevIntent=new Intent(PREV_ACTION);
        PendingIntent prevPIntent=PendingIntent.getBroadcast(ForegroundContext,0,prevIntent,0);
        widgetRemoteViews.setOnClickPendingIntent(R.id.widget_pre,prevPIntent);

        Intent stopIntent=new Intent(STOP_ACTION);
        PendingIntent stopPIntent=PendingIntent.getBroadcast(ForegroundContext,0,stopIntent,0);
        widgetRemoteViews.setOnClickPendingIntent(R.id.audio_stop,stopPIntent);


        final  Intent nowPlayingIntent=new Intent(Intent.ACTION_MAIN);
         nowPlayingIntent.addCategory(Intent.CATEGORY_LAUNCHER);
          nowPlayingIntent.setComponent(new ComponentName(getPackageName(),"of.media.bq.activity.MainActivity"));
          nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent clickIntent=PendingIntent.getActivity(ForegroundContext,0,nowPlayingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
         if(mNotification==null){
             if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){//SDK版本大于26
                 String id="channel_2";
                 String description="144";
                 int improtance=NotificationManager.IMPORTANCE_LOW;
                 NotificationChannel channel=new NotificationChannel(id,description,improtance);
                 channel.enableLights(true);
                 channel.enableVibration(true);
                 notificationManager.createNotificationChannel(channel);
                 mNotification=new Notification.Builder(ForegroundContext,id).setContent(widgetRemoteViews).setSmallIcon(R.drawable.ic_notification)
                         .setContentIntent(clickIntent).setWhen(System.currentTimeMillis()).setAutoCancel(false).build();
             }else{
                 NotificationCompat.Builder builder=new NotificationCompat.Builder(ForegroundContext).setContent(widgetRemoteViews).setSmallIcon(R.drawable.ic_notification)
                         .setContentIntent(clickIntent).setWhen(System.currentTimeMillis()).setAutoCancel(false);
                 mNotification=builder.build();
             }
         }else{
             mNotification.contentView=widgetRemoteViews;
         }
       return  mNotification;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        if(intent==null){
            return super.onStartCommand(intent, flags, startId);
        }
        if(!isExistMusics()){
            OneToast.showMessage(MusicService.this,"当前没有音乐");
        }else{
            String action=intent.getAction();
            assert action != null;
            if(!ForegroundIsExist)//如果前台服务不存在
            { new Thread(new Runnable() {
                @Override
                public void run() {
                    if(getmNotification()!=null){
                        startForeground(NotificationId,getmNotification());
                        ForegroundIsExist=true;
                    }
                }
            }).start();
            }
//            if(action.contentEquals(PLAY_ACTION)){
//                if(isCanPlay()){
//                    String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
//                    playMusic(send_music_service_flag);
//                }
//
//            }else if(action.contentEquals(PREV_ACTION)){
//                String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
//                prevMusic(send_music_service_flag);
//            }else if(action.contentEquals(NEXT_ACTION)){
//                String flag=intent.getStringExtra("flag");//通过flag判断是否为自动播放
//                String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
//                nextMusic(flag,send_music_service_flag);
//            }else if(action.contentEquals(PAUSE_ACTION)){
//                String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
//                pauseMusic(send_music_service_flag);
//            }else if(action.contentEquals(START_ACTION)){
//                String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
//                startMusic(send_music_service_flag);
//            }

            handleCommandIntent(intent,0);
        }
        
        return super.onStartCommand(intent, flags, startId);
    }


    private void  handleCommandIntent(Intent intent,int type){//type=0为onStartCommand函数处理的消息，type=1为处理前台服务接收的广播

        if(!isCanPlay()){//如果不能满足播放条件
            return;
        }
        if(intent==null||intent.getAction()==null){
            return;
        }
        String action=intent.getAction();
       if(action.contentEquals(TOGGLEPAUSE_ACTION)){
       if(isPlaying()){
           pauseMusic(null);
       }else{
           startMusic(null);
       }
           NotificationChange(TOGGLEPAUSE_ACTION);//改变前台服务中的音乐状态信息
       }else if(action.contentEquals(PREV_ACTION)){
           if(type==0){
               String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
               prevMusic(send_music_service_flag);
           }else if(type==1){
               prevMusic(null);
           }
           NotificationChange(PREV_ACTION);//改变前台服务中的音乐状态信息
       }else  if(action.contentEquals(NEXT_ACTION)){
           if(type==0){
               String flag=intent.getStringExtra("flag");//通过flag判断是否为自动播放
               String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
               nextMusic(flag,send_music_service_flag);
           }else if(type==1){
               nextMusic(null,null);
           }
           NotificationChange(NEXT_ACTION);//改变前台服务中的音乐状态信息
       }else if(action.contentEquals(STOP_ACTION)){
           stopMusic();
           ForegroundIsExist=false;
           mNotification=null;
           stopForeground(true);
       }else if(action.contentEquals(PLAY_ACTION)){
           if(type==0){
               if(isCanPlay()){
                   String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
                   playMusic(send_music_service_flag);
               }
           }
           NotificationChange(TOGGLEPAUSE_ACTION);//改变前台服务中的音乐状态信息
       }else if(action.contentEquals(PAUSE_ACTION)){
        if(type==0){
            String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
            pauseMusic(send_music_service_flag);
        }
           NotificationChange(TOGGLEPAUSE_ACTION);//改变前台服务中的音乐状态信息
       }else if(action.contentEquals(START_ACTION)){
           if(type==0){
               String send_music_service_flag=intent.getStringExtra("send_music_service_flag");//目的是为了判断是通过操作播放界面而实现的音乐动作还是通过广播实现的
               startMusic(send_music_service_flag);
           }
           NotificationChange(TOGGLEPAUSE_ACTION);//改变前台服务中的音乐状态信息
       }

        Log.i("jfsjfkasjkfj", "handleCommandIntent: "+action);

    }


   private  void NotificationChange(final String what){
        if(what==null){
            return;
        }
       if(!isCanPlay()){//如果不能满足播放条件
           return;
       }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!ForegroundIsExist){
                    return;
                }else if(mNotification==null){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(mNotification!=null||!ForegroundIsExist){
                                NotificationChange(what);
                                this.cancel();
                            }
                        }
                    },0,100);
                    return;
                }

                if(TOGGLEPAUSE_ACTION.contentEquals(what)){
                    if(isPlaying()){
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_play_selector);
                    }else{
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_pause_selector);
                    }
                }else if(NEXT_ACTION.contentEquals(what)||PREV_ACTION.contentEquals(what)){
                    mNotification.contentView.setTextViewText(R.id.widget_content,musicList.get(currentPosition).getTitle());//设置歌名
                    if(musicList.get(currentPosition).getImage()!=null){//如果音乐专辑图片存在
                        Bitmap bitmap= MusicIconLoader.getInstance().load(musicList.get(currentPosition).getImage());
                        if(bitmap!=null){
                            mNotification.contentView.setImageViewBitmap(R.id.widget_image,bitmap);
                        }else{
                            mNotification.contentView.setImageViewResource(R.id.widget_image,R.drawable.mp1);
                        }
                    }else{
                        mNotification.contentView.setImageViewResource(R.id.widget_image,R.drawable.mp1);
                    }
                    if(isPlaying()){
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_play_selector);
                    }else{
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_pause_selector);
                    }
                }else if(STOP_ACTION.contentEquals(what)){
                    if(isPlaying()){
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_play_selector);
                    }else{
                        mNotification.contentView.setImageViewResource(R.id.widget_play,R.drawable.widget_pause_selector);
                    }
                }
                notificationManager.notify(NotificationId,mNotification);
            }
        }).start();
   }




    private void pauseMusic(String send_music_service_flag){
        if(isPlaying()){
            mMediaPlayer.pause();
            sendIntent(getApplicationContext(),PAUSE_ACTION);
            saveMusicProgress();
            if(mControl!=null){
                mControl.playButton(0);
            }
        }
    }
    
    
    
    private void stopMusic(){
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            sendIntent(getApplicationContext(),STOP_ACTION);//改为停止广播
            saveMusicProgress();
            if(mControl!=null){
                mControl.playButton(0);
            }
        }
    }
    
    private  void startMusic(String send_music_service_flag){
        if(mMediaPlayer!=null){
            mMediaPlayer.start();
            sendIntent(getApplicationContext(),START_ACTION);
            if(mControl!=null){
                mControl.playButton(1);
            }
        }
    }
    
    private  void playMusic(String send_music_service_flag){
        
        if(isPlaying()){
            if(mControl!=null){
                mControl.playButton(0);
            }
            mMediaPlayer.pause();
            sendIntent(getApplicationContext(),PAUSE_ACTION);
            saveMusicProgress();
        }else{
            if(mControl!=null){
                mControl.playButton(1);
            }
            mMediaPlayer.start();
            sendIntent(getApplicationContext(),START_ACTION);
        }
        
    }
    
    //判断是否满足播放条件
    public boolean isCanPlay(){
        if(musicList.size()==0||currentPosition<0||currentPosition>musicList.size()&&mMediaPlayer!=null){
            return false;
        }else {
            return true;
        }
    }
    
    private void prevMusic(String send_music_service_flag){
        
        if(isCanPlay()) {
            saveMusicProgress();
            currentPosition = currentPosition == 0 ? (getMusicSize() - 1) :( currentPosition - 1);
            Log.i("trinity12", "prevMusic: "+musicList.get(currentPosition).getProgress()+"//"+currentPosition);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(musicList.get(currentPosition).getUri());
                mMediaPlayer.prepare();
                mMediaPlayer.seekTo(musicList.get(currentPosition).getProgress());
                mMediaPlayer.start();
                sendIntent(getApplicationContext(),PREV_ACTION);
                if(mControl!=null){
                    mControl.playButton(1);
                }
                if(send_music_service_flag==null||!send_music_service_flag.contentEquals(SEND_MUSIC_SERVICE_FLAG)){//是通过广播发送的服务
                    if(mControl!=null){
                        mControl.updateUI();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private  void nextMusic(String flag,String send_music_service_flag){
        
        if(isCanPlay()) {
            Log.i("trinity12", "nextMusic:"+flag);
            if(flag==null||!flag.contentEquals(FLAG)){//如果是普通的下一首
                saveMusicProgress();
            }else{//如果是自动播放会设置当前的音乐保存进度为0
                Music music=musicList.get(currentPosition);
                music.setProgress(0);
                musicList.set(currentPosition,music);
            }
            
            currentPosition = (currentPosition >= (getMusicSize() - 1)) ? 0 : currentPosition + 1;
            
            Log.i("trinity12", "nextMusic: "+musicList.get(currentPosition).getProgress()+"//"+currentPosition);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(musicList.get(currentPosition).getUri());
                mMediaPlayer.prepare();
                mMediaPlayer.seekTo(musicList.get(currentPosition).getProgress());
                mMediaPlayer.start();
                sendIntent(getApplicationContext(),NEXT_ACTION);
                if(mControl!=null){
                    mControl.playButton(1);
                }
                if(send_music_service_flag==null||!send_music_service_flag.contentEquals(SEND_MUSIC_SERVICE_FLAG)){//是通过广播发送的服务
                    if(mControl!=null){
                        mControl.updateUI();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //保存当前播放歌曲的进度
    private void saveMusicProgress(){
        if(isCanPlay()){
            Music music=musicList.get(currentPosition);
            music.setProgress(mMediaPlayer.getCurrentPosition());
            musicList.set(currentPosition,music);
        }
        
    }
    //判断音乐列表是否为空
    public static boolean isExistMusics(){
        return musicList.size()!=0;
    }
    
    //获取播放列表的音乐个数
    public  static  int getMusicSize(){
        return  musicList.size();
    }
    
    //获取音乐标题
    public  static String getMusicTitle(int index){
        if(musicList.size()==0){
            return "暂无";
        }else{
            return musicList.get(index).getTitle();
        }
    }
    //获取歌手名
    public  static String getMusicArtist(int index){
        if(musicList.size()==0){
            return "暂无";
        }else{
            return musicList.get(index).getArtist();
        }
    }
    
    //判断音乐是否正在播放
    public static boolean isPlaying(){
        if(mMediaPlayer==null){
            return false;
        }
        return mMediaPlayer.isPlaying();
    }
    
    //获取当前播放歌曲的进度
    public static int getMusicCurrentPosition(){
        if(mMediaPlayer==null){
            return 0;
        }else{
            return   mMediaPlayer.getCurrentPosition();
        }
    }
    //获取当前播放歌曲的长度
    public static int getMusicDuration(){
        if(mMediaPlayer!=null){
            return mMediaPlayer.getDuration();
        }else{
            return 0;
        }
    }
    //设置当前播放音乐的进度
    public  static void setMusicCurrentPosition(int time){
        if(mMediaPlayer!=null){
            mMediaPlayer.seekTo(time);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
            mControl=null;
        }
    }
    public interface Control{
        void playButton(int index);//控制播放按钮的形状(0暂停，1,播放）
        void autoPlay();//自动播放
        void updateUI();//更新播放界面的信息
    }
    
    
    //发送对应action的广播
    public static void sendIntent(Context context,String action){
        Intent intent=new Intent(action);
        intent.setPackage("thread.ofilm.com.testtrinity");
        context.sendBroadcast(intent);
    }
}
