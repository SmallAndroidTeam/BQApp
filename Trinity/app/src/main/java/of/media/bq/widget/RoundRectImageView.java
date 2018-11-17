package of.media.bq.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by MR.XIE on 2018/10/25.
 * 自定义圆角矩形ImageView
 */
@SuppressLint("AppCompatCustomView")
public class RoundRectImageView  extends ImageView{
private Paint paint;

    public RoundRectImageView(Context context) {
        super(context,null);
        paint=new Paint();
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        paint=new Paint();
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getDrawable();
        if(drawable!=null){
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
            Bitmap b=getRoundBitmap(bitmap,45);
            final Rect rectSrc=new Rect(0,0,b.getWidth(),b.getHeight());
            final Rect rectDest=new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            canvas.drawBitmap(b,rectSrc,rectDest,paint);
        }else{
            super.onDraw(canvas);
        }
    }

    private Bitmap getRoundBitmap(Bitmap bitmap,int roundPx){
        Bitmap output=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Config.ARGB_8888);
        Canvas canvas=new Canvas(output);
        final  int color=0xff424242;
        final Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final RectF rectF=new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        int x=bitmap.getWidth();
        canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return output;
    }
}
