package of.modeselect.bq.fragment;
import of.modeselect.bq.R;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import of.modeselect.bq.adapter.LocalGalleryAdapter;
import of.modeselect.bq.bean.Gallery;
import of.modeselect.bq.convertPXAndDP.DensityUtil;
import of.modeselect.bq.toast.OneToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalGalleryFragment extends Fragment {
    private final  static String TAG="trinity12";
    private GridView localGalleryGridView;
    private LocalGalleryAdapter localGalleryAdapter;
    private ImageView galleryImageview;
    private int position=-1;
    private  float fromDegress=0;//旋转开始的角度


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_gallery_fragment,container,false);
        
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        localGalleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                galleryDialog().show();
                galleryImageview.setImageResource(localGalleryAdapter.getGalleryList().get(i).getGalleryAddress());
                final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
                galleryImageview.startAnimation(animationSet);
            }
        });
    }



    private Dialog galleryDialog(){
        final  View view=LayoutInflater.from(getContext()).inflate(R.layout.gallery_dialog,null);
        galleryImageview = view.findViewById(R.id.galleryImageview);
        ImageView prevImageView = view.findViewById(R.id.prevImageView);
        ImageView nextImageView= view.findViewById(R.id.nextImageView);
        ImageView enlarge=view.findViewById(R.id.enlarge);
        ImageView smaller=view.findViewById(R.id.smaller);
        ImageView rotation=view.findViewById(R.id.rotation);
        final Dialog dialog=new Dialog(getContext(),R.style.MyDialog);
        dialog.setCancelable(true);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        WindowManager.LayoutParams lp=window.getAttributes();
//        lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width= DensityUtil.getWindowWidth(getContext())/2;
        lp.height= (int) (DensityUtil.getWindowHeight(getContext())/1.5);
        window.setAttributes(lp);

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                OneToast.showMessage(getContext(),"触摸了");
//                return false;
//            }
//        });
          final int size=localGalleryAdapter.getCount();
        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //重新设置图片的大小
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,300);
               layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                galleryImageview.setLayoutParams(layoutParams);
                fromDegress=0;
                if(position==size-1){
                    OneToast.showMessage(getContext(),"已经是最后一张了");
                }else{
                    position=position==size-1?0:position+1;
                    galleryImageview.setImageBitmap(BitmapFactory.decodeResource(getResources(),localGalleryAdapter.getGalleryList().get(position).getGalleryAddress()));
                    final AnimationSet animationSet= ( AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.next_gallery_image);
                    galleryImageview.startAnimation(animationSet);
                }


            }
        });
        prevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,300);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                galleryImageview.setLayoutParams(layoutParams);

                fromDegress=0;
                if(position==0){
                    OneToast.showMessage(getContext(),"已经是第一张了");
                } else{
                    position= position - 1;
                    galleryImageview.setImageResource(localGalleryAdapter.getGalleryList().get(position).getGalleryAddress());
                    final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.prev_gallery_image);
                    galleryImageview.startAnimation(animationSet);
                }

            }
        });

        //放大
        enlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmapScale(1.2f,1.2f);

            }
        });
        //缩小
        smaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapScale(0.8f,0.8f);
            }
        });
        //循转
        rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapRotate(90);
            }
        });
        return  dialog;
    }


     private void bitmapScale(float x,float y){
//          Bitmap baseBitmap=((BitmapDrawable)galleryImageview.getDrawable()).getBitmap();
//         if(baseBitmap!=null){
//             Bitmap afterBitmap=Bitmap.createBitmap((int)(baseBitmap.getWidth()*x),(int)(baseBitmap.getHeight()*y),baseBitmap.getConfig());
//
//             Paint paint=new Paint();
//             //消除锯齿
//             paint.setAntiAlias(true);
//             paint.setFilterBitmap(true);
//             Canvas canvas=new Canvas(afterBitmap);
//
//             Matrix matrix=new Matrix();
//             matrix.setScale(x,y);
//             canvas.drawBitmap(baseBitmap,matrix, paint);
//             galleryImageview.setImageBitmap(afterBitmap);
//         }
            if(galleryImageview.getWidth()*x<DensityUtil.dip2px(getContext(),90)){
                return;
            }
            Animation scaleAnimation=new ScaleAnimation(1,1*x,1,1*y,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
           scaleAnimation.setDuration(500);
           scaleAnimation.setFillAfter(true);
           scaleAnimation.setRepeatCount(0);
           scaleAnimation.setStartOffset(0);
           scaleAnimation.setInterpolator(getContext(),android.R.anim.decelerate_interpolator);
           galleryImageview.startAnimation(scaleAnimation);

         RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) galleryImageview.getLayoutParams();
         layoutParams.width= (int) (galleryImageview.getWidth()*1*x);
         layoutParams.height= (int) (galleryImageview.getHeight()*1*y);
         galleryImageview.setLayoutParams(layoutParams);

     }


     //图片旋转(defress为旋转的角度)
    private void bitmapRotate(float defrees){
        Bitmap baseBitmap=((BitmapDrawable)(galleryImageview.getDrawable())).getBitmap();
        if(baseBitmap!=null){
            Bitmap afterBitmap=null;
                afterBitmap=Bitmap.createBitmap((int)(baseBitmap.getWidth()),(int)(baseBitmap.getHeight()),baseBitmap.getConfig());
            Paint paint=new Paint();
            //消除锯齿
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            Canvas canvas=new Canvas(afterBitmap);
            Matrix matrix=new Matrix();
            matrix.setRotate(defrees,baseBitmap.getWidth()/2.0f,baseBitmap.getHeight()/2.0f);
            canvas.drawBitmap(baseBitmap,matrix,paint);
            galleryImageview.setImageBitmap(afterBitmap);
        }
//        Animation rotateAnimation=new RotateAnimation(fromDegress,fromDegress+defrees,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        rotateAnimation.setDuration(300);
//        rotateAnimation.setFillAfter(true);
//        rotateAnimation.setInterpolator(getContext(),android.R.anim.accelerate_decelerate_interpolator);
//        galleryImageview.startAnimation(rotateAnimation);
//        fromDegress=(fromDegress+defrees)%360;
        imageRotate();

    }

    //图片旋转
    private void imageRotate(){
        Animation rotateAnimation=new RotateAnimation(fromDegress-90,fromDegress,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        galleryImageview.startAnimation(rotateAnimation);
    }



    private void initView(View view) {
        localGalleryGridView = view.findViewById(R.id.localGalleryGridView);
    }
    private void initData() {
        List<Gallery> galleryList=addData();
        localGalleryAdapter = new LocalGalleryAdapter(getContext(),galleryList);
        localGalleryGridView.setNumColumns(10);
        localGalleryGridView.setHorizontalSpacing(20);
        localGalleryGridView.setVerticalSpacing(20);
        localGalleryGridView.setAdapter(localGalleryAdapter);
    }
    private List<Gallery> addData(){
        List<Gallery> galleryList=new ArrayList<>();
        final Integer[] galleryIDs=new Integer[]{R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8
        ,R.drawable.p9,R.drawable.p10};
        Gallery gallery=null;
        for(Integer id:galleryIDs){
            gallery=new Gallery(id);
            galleryList.add(gallery);
        }
        return galleryList;
    }
}
