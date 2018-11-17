package of.meida.bq.heartRate.ecgview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.trinity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By rongxinglan IN 2018/9/12
 */




public class PathView extends View {


    private float XPoint; // 原点的X坐标
    private float YPoint; // 原点的Y坐标

    private List<Integer> Data; // 数据
    private List<Integer> mList = new ArrayList<>();
    private  int index = 0;
    private Paint mPaint;

    private int screenWidth;
    private int screenHeight;
    private float  degreeSpace;


    private Path linePath = new Path();
    private Handler mHandler = new Handler();

    public PathView(Context context) {
        super(context);

        //this.mContext = context;
      //  startDraw();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

      //  this.mContext = context;
      // startDraw();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
      //  this.mContext = context;
      //  startDraw();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        this.screenWidth = width;
        this.screenHeight = height;
        setMeasuredDimension(width, height);

    }


    public void SetData(List<Integer> Data) {
        this.Data = Data;
        addData();
       startDraw();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// 重写onDraw方法

        XPoint = 0;
        YPoint = screenHeight/2;
        degreeSpace =screenWidth/Data.size();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);// 去锯齿
        mPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));// 颜色
        mPaint.setStrokeWidth(5);
        drawChart(canvas);
    }

    private void drawChart(Canvas canvas) {
        Log.i("e", "eeeeeeeeeeeeeeeeeeeeeeeee "+mList.size());
          //  float s = (float)Data.get(i) ;
            linePath.moveTo(XPoint,toY(mList.get(0)));
            for (int i = 1; i < mList.size(); i++) {
                float toPointX = XPoint + i * degreeSpace;
                float toPointY = toY(Data.get(i));
                linePath.lineTo(toPointX, toPointY);
        }
        canvas.drawPath(linePath,mPaint);
    }

    /**
     * 将数值转换为y坐标，中间大的显示心电图的区域
     */
    private float toY(int num) {
        float y;
        try {
            float a = (float) num;
            y = a/5;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }


  private void addData() {

        if(index < Data.size()){
         //   Log.i("aaa","aaaa  "+Data);

            mList.add(Data.get(index));
            invalidate();
            Log.i("o", "oooooooooooooooooooooo"+mList.size());
            Log.i("o", "oooooooooooooooooooooo"+mList.get(index));
            index++;

        }
  }
  Runnable mRunnable;
  private void startDraw() {
      Log.i("p", "pppppppppppppppppppppp");
      mRunnable = new Runnable() {
          @Override
          public void run() {
              Log.i("p", "pppppppppppppppppppppp");
              addData();
              mHandler.postDelayed(mRunnable,1000);
          }
      };
      mHandler.post(mRunnable);
  }


    //根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        else if (specMode == MeasureSpec.EXACTLY) {

        }

        return specSize;
    }

    //根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        else if (specMode == MeasureSpec.EXACTLY) {

        }
        return specSize;
    }


}


