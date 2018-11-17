package of.meida.bq.convertPXAndDP;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

/**
 * 转化dp和px
 * Created by MR.XIE on 2018/8/18.
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context,float deValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(deValue*scale+0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context,float deValue) {
        final  float scale=context.getResources().getDisplayMetrics().density;
        return (int)(deValue/scale+0.5f);

    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getWindowWidth(Context context){
        int width=((Activity)context).getResources().getDisplayMetrics().widthPixels;
        return width;
    }
    public static int getWindowHeight(Context context){
        int height=((Activity)context).getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /*
     * 获取控件宽
     */
    public static int getWidth(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }
    /*
     * 获取控件高
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

    //获取下划线的宽度
    public static int getTableLayoutWidth(TableLayout tableLayout){
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) tableLayout.getLayoutParams();
        return   layoutParams.width;
    }

    //获取LinearLayout布局的高度
    public static int getLinearLayoutHeight(LinearLayout linearLayout){
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        return layoutParams.height;
    }

    //获取ReationLayout布局的高度（父布局为LinearLayout)
    public static int getRelativeLayoutHeight(RelativeLayout relativeLayout){
       ViewGroup.LayoutParams layoutParams=relativeLayout.getLayoutParams();
        return layoutParams.height;
    }
    //获取控件在屏幕中距离顶部的距离
    public static int getTopDistance(View v){
        int[] location=new int[2];
        v.getLocationOnScreen(location);
        int x=location[0];
        int y=location[1];
        return y;
    }

}
