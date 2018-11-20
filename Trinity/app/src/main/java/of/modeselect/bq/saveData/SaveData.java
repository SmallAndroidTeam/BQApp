package of.modeselect.bq.saveData;

import of.modeselect.bq.bean.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/29.
 */
public class SaveData {
    private  static List<Video> localVideoList=new ArrayList<>();//本地视频列表

    public static List<Video> getLocalVideoList() {
        return localVideoList;
    }

    public static void setLocalVideoList(List<Video> localVideoList1) {
        SaveData.localVideoList.clear();
        copy(localVideoList1,localVideoList);
    }

    public static void copy(List src,List dest){
        for (int i = 0 ;i < src.size() ; i++) {
            Object obj = src.get(i);
            if (obj instanceof List){
                dest.add(new ArrayList());
                copy((List)obj,(List)((List)dest).get(i));
            }else{
                dest.add(obj);
            }
        }

    }
}
