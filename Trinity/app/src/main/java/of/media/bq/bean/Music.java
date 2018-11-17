package of.media.bq.bean;

/**
 * Created by MR.XIE on 2018/10/25.
 */
public class Music {
    private int id; // 音乐id
    private String title; // 音乐标题
    private String uri; // 音乐路径
    private int length; // 长度
    private String image; // icon
    private int albumImageId;
    private String artist; // 艺术家
    private String lrcpath;//歌词路径
    private Integer progress=0;//播放进度
    public Music() {
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public int getAlbumImageId() {
        return albumImageId;
    }

    public void setAlbumImageId(int albumImageId) {
        this.albumImageId = albumImageId;
    }

    public String getLrcpath() {
        return lrcpath;
    }

    public void setLrcpath(String lrcpath) {
        this.lrcpath = lrcpath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
