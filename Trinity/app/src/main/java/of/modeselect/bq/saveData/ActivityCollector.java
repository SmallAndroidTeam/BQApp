package of.modeselect.bq.saveData;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理activity
 */
public class ActivityCollector {
    
    private static List<Activity> activityList=new ArrayList<>();//首先会保存mainActivity
    
    public static Activity getActivityByIndex(int index){
        if(index<-1||index>activityList.size()-1){
            return null;
        }
        else{
            return activityList.get(index);
        }
    }
    
    public  static  void addActivity(Activity activity){
        activityList.add(activity);
    }
    public static void remoteActivity(Activity activity){
        activityList.remove(activity);
    }
    
    public  static void finishAll(){
        for(Activity activity:activityList){
            activity.finish();
        }
    }
}
