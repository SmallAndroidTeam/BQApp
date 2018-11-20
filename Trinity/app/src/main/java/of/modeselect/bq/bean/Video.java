package of.modeselect.bq.bean;

import android.graphics.Bitmap;

/**
 * Created by MR.XIE on 2018/10/24.
 */
public class Video {

    private int videoThumbnail;
    private String videoName;
    private int vdieoId=0;
    private String videoPath=null;//播放路径(如果视频已下载则此为本地路径)
    private String resolution=null;//分辨率
    private long size=0;//大小
    private long date=0;//添加时间
    private long duration=0;//时长
    private String thumbnailPath=null;//缩略图的网络地址
    private Bitmap Thumbnail=null;//缩略图
    private Integer progress=0;//播放进度(最大值为1000）
    private String networkVideoAddress=null;//如果是本地视频则为空


    public Video(String videoPath, String videoName, String resolution, long size, long date, long duration, Bitmap thumbnail, Integer progress) {
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
        Thumbnail = thumbnail;
        this.progress = progress;
    }

    public Video(int vdieoId, String videoPath, String videoName, String resolution, long size, long date, long duration) {
        this.vdieoId = vdieoId;
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.resolution = resolution;
        this.size = size;
        this.date = date;
        this.duration = duration;
    }


    public int getVdieoId() {
        return vdieoId;
    }

    public void setVdieoId(int vdieoId) {
        this.vdieoId = vdieoId;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public Bitmap getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getNetworkVideoAddress() {
        return networkVideoAddress;
    }

    public void setNetworkVideoAddress(String networkVideoAddress) {
        this.networkVideoAddress = networkVideoAddress;
    }

    public Video() {
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(int videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
