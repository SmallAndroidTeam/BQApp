package of.media.bq.bean;

import android.graphics.Bitmap;

/**
 * Created by MR.XIE on 2018/10/25.
 */
public class Gallery {
    private Bitmap thumbnail;//缩略图
    private  int galleryAddress;//图片的路径（测试用的是资源文件)

    public Gallery() {
    }

    public Gallery(int galleryAddress) {
        this.galleryAddress = galleryAddress;
    }

    public Gallery(Bitmap thumbnail, int galleryAddress) {
        this.thumbnail = thumbnail;
        this.galleryAddress = galleryAddress;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getGalleryAddress() {
        return galleryAddress;
    }

    public void setGalleryAddress(int galleryAddress) {
        this.galleryAddress = galleryAddress;
    }
}
