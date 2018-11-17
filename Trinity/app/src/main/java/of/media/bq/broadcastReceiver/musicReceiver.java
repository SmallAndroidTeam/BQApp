package of.media.bq.broadcastReceiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import of.media.bq.fragment.MultiMediaFragment;
import of.media.bq.service.MusicService;


public class musicReceiver extends BroadcastReceiver {
    public final static String TAG="bq111";
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
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG,MultiMediaFragment.isExist+"             ");
        if(intent!=null&& MultiMediaFragment.isExist){
            String action=intent.getAction();
            if(action.contentEquals(PLAY_ACTION)) {
                sendService(context,action);
                sendVoiceBroadCastReceiver(context,"com.txznet.adapter.recv","为您播放音乐");
            }
            else if(action.contentEquals(PAUSE_ACTION)){
                sendService(context,action);
                sendVoiceBroadCastReceiver(context,"com.txznet.adapter.recv","暂停播放音乐");
            }
            else if(action.contentEquals(PREV_ACTION)){
                sendService(context,PREV_ACTION);
                sendVoiceBroadCastReceiver(context,"com.txznet.adapter.recv","上一首");
            }
            else if(action.contentEquals(NEXT_ACTION)){
                sendService(context,NEXT_ACTION);
                sendVoiceBroadCastReceiver(context,"com.txznet.adapter.recv","下一首");
            }
            else if(action.contentEquals(START_ACTION)){
                sendService(context,START_ACTION);
            }
            else if(action.contentEquals(STOP_ACTION)){
                sendService(context,STOP_ACTION);
            }
        }else{
            sendVoiceBroadCastReceiver(context,intent.getAction(),"您未打开音乐");
        }
    }
    //播放语音广播
    public   void sendVoiceBroadCastReceiver(Context context,String action,String message){
        Intent intent=new Intent(action);
        intent.putExtra(action, "txz.tts.speak");
        intent.putExtra("tts",message);
        intent.setPackage("thread.ofilm.com.testtrinity");
        context.sendBroadcast(intent);
    }
    public  void sendService(Context context,String action){
        Intent intent=new Intent(context, MusicService.class);
        intent.putExtra("flag",MusicService.FLAG);
        intent.setAction(action);
        context.startService(intent);
    }
    //判断activity是否在运行
//  public boolean activityIsRunning(Context context){
//        boolean isRunning=false;
//      ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//      List<ActivityManager.RunningTaskInfo> list=am.getRunningTasks(100);
//      for(ActivityManager.RunningTaskInfo info:list){
//          if(info.topActivity.getPackageName().equals(context.getPackageName())&&info.baseActivity.getPackageName().equals(context.getPackageName())){
//             isRunning=true;
//             break;
//          }
//          continue;
//      }
//      return isRunning;
//  }
}
