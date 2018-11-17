package of.media.bq.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

/**
 * Created by MR.XIE on 2018/10/25.
 */
public class GetThumbnail {

    //通过地址获取缩略图
    public static Bitmap getImageThumbnailByAddress(String imagePath,int width,int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,options);
        int h=options.outHeight;//获取图片的高度
        int w=options.outWidth;//获取图片的宽度
        int scaleWidth=w/width;//计算宽度缩放比
        int scaleHeight=h/height;//计算高度缩放比
        int scale=1;//初始缩放比
        if(scaleWidth<scaleHeight){//选择合适的缩放比
            scale=scaleWidth;
        }else{
            scale=scaleHeight;
        }

        if(scale<0){//判断缩放比是否符合条件
            scale=1;
        }
        options.inSampleSize=scale;
        //重新读入图片，读取缩放后的bitmap，注意这次要把inJustDecodeBounds 设为 false
        //当options.inJustDecodeBounds设置为false时，通过BitmapFactory.decodeFile去加载图片，将不会正真地返回bitmap，也就是说此时的bitmap为null。它的作用是将图片的相关信息，例如图片宽高，大小等信息带到options中，方便我们后续计算图片的宽高比。
          options.inJustDecodeBounds=false;
          bitmap=BitmapFactory.decodeFile(imagePath,options);
          //利用ThumbnailUtils来创建缩略图，这里要指定要缩放到哪个Bitmap对象
        bitmap= ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if(bitmap==null){
            bitmap=BitmapFactory.decodeFile(imagePath);
        }
        return bitmap;
    }
    //通过资源文件中的ID获取缩略图
    public static Bitmap getImageThumbnailById(Resources resources,int id, int width, int height){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap=BitmapFactory.decodeResource(resources,id,options);
        int h=options.outHeight;//获取图片的高度
        int w=options.outWidth;//获取图片的宽度
        int scaleWidth=w/width;//计算宽度缩放比
        int scaleHeight=h/height;//计算高度缩放比
        int scale=1;//初始缩放比
        if(scaleWidth<scaleHeight){//选择合适的缩放比
            scale=scaleWidth;
        }else{
            scale=scaleHeight;
        }

        if(scale<0){//判断缩放比是否符合条件
            scale=1;
        }
        options.inSampleSize=scale;
        //重新读入图片，读取缩放后的bitmap，注意这次要把inJustDecodeBounds 设为 false
        //当options.inJustDecodeBounds设置为false时，通过BitmapFactory.decodeFile去加载图片，将不会正真地返回bitmap，也就是说此时的bitmap为null。它的作用是将图片的相关信息，例如图片宽高，大小等信息带到options中，方便我们后续计算图片的宽高比。
        options.inJustDecodeBounds=false;
        bitmap=BitmapFactory.decodeResource(resources,id,options);
        //利用ThumbnailUtils来创建缩略图，这里要指定要缩放到哪个Bitmap对象
        bitmap= ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if(bitmap==null){
            bitmap=BitmapFactory.decodeResource(resources,id);
        }
        return bitmap;
    }

}
