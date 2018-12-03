package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import of.modeselect.bq.R;
import of.modeselect.bq.bean.Music;
import of.modeselect.bq.localInformation.MusicIconLoader;
import of.modeselect.bq.localInformation.MusicUtils;
import of.modeselect.bq.service.MusicService;
import of.modeselect.bq.toast.OneToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by MR.XIE on 2018/10/23.
 */
public class LocalMusicFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, MusicService.Control {


    private final  static String TAG="trinity12";
    private final static int UPDATE_PROGRESS=0;//更新进度条
    private final static int UPDATE_UI=1;//更新播放界面的UI信息
    private ImageView prevMusicImageview;
    private ImageView currentMusicImageview;
    private ImageView nextMusicImageview;
    private ImageView prevImageView;
    private ImageView playImageView;
    private ImageView nextImageView;
    private SeekBar musicSeekbar;
    private TextView musicTitle;
    private TextView singerName;

    @SuppressLint("HandlerLeak")
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PROGRESS:
                    musicSeekbar.setMax(MusicService.getMusicDuration());
                    musicSeekbar.setProgress(MusicService.getMusicCurrentPosition());
                    sendEmptyMessageDelayed(UPDATE_PROGRESS,100);
                    break;
                case UPDATE_UI:
                    setMusicAlbumPosition(MusicService.getCurrentPosition());
                    if(MusicService.isPlaying()){
                        playImageView.setImageResource(R.drawable.play_imageview);
                    }else{
                        playImageView.setImageResource(R.drawable.pause_imageview);
                    }
                    musicTitle.setText(MusicService.getMusicTitle(MusicService.getCurrentPosition()));
                    singerName.setText(MusicService.getMusicArtist(MusicService.getCurrentPosition()));
                    mhandler.sendEmptyMessage(UPDATE_PROGRESS);
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView firstMusicImageview;
    private ImageView lastMusicImageview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local_music_fragment,container,false);
        initView(view);
        initListener(view);
        initData();
        MusicService.setmControl(this);
        return view;
    }

    private void initData() {
        if(MusicService.isCanPlay()){//如果前台服务存在
            mhandler.sendEmptyMessage(UPDATE_UI);
        }else{
            MusicUtils.initMusicList();
            List<Music>  musicList=MusicUtils.sMusicList;
            
            if(musicList.size()>0){
                MusicService.initMusicService(musicList,0);
                setMusicAlbumPosition(0);
            }else{
                OneToast.showMessage(getContext(),"当前无本地歌曲");
                setMusicAlbumPosition(-1);
            }
            musicTitle.setText(MusicService.getMusicTitle(0));
            singerName.setText(MusicService.getMusicArtist(0));
        }

    }

    private void initView(View view) {
        firstMusicImageview = view.findViewById(R.id.firstMusicImageview);
        prevMusicImageview = view.findViewById(R.id.prevMusicImageview);
        currentMusicImageview = view.findViewById(R.id.currentMusicImageview);
        nextMusicImageview = view.findViewById(R.id.nextMusicImageview);
        lastMusicImageview = view.findViewById(R.id.lastMusicImageview);
        prevImageView = view.findViewById(R.id.prevImageView);
        playImageView = view.findViewById(R.id.playImageView);
        nextImageView = view.findViewById(R.id.nextImageView);
        musicSeekbar = view.findViewById(R.id.musicSeekbar);
        musicTitle = view.findViewById(R.id.musicTitle);
        singerName = view.findViewById(R.id.singerName);
    }

    private void initListener(View view) {
        view.setOnTouchListener(this);

        firstMusicImageview.setOnTouchListener(this);
        lastMusicImageview.setOnTouchListener(this);

        prevMusicImageview.setOnTouchListener(this);
        nextMusicImageview.setOnTouchListener(this);

        prevImageView.setOnClickListener(this);
        playImageView.setOnClickListener(this);
        nextImageView.setOnClickListener(this);
        musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mhandler.removeMessages(UPDATE_PROGRESS);
                if(!MusicService.isExistMusics()){
                    musicSeekbar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                MusicService.setMusicCurrentPosition(musicSeekbar.getProgress());
                mhandler.sendEmptyMessage(UPDATE_PROGRESS);
            }
        });

    }



    private  float downX=0;//按下时距离屏幕左边的距离
    private final  static float MIN_HORIZONTAL_SLIDE_DISTANCE=100;//最小的水平滑动距离
    private boolean isSLideComplete=false;//滑动是否完成
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final   int size=MusicService.getMusicSize();
        if(size==0){
            return  false;
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSLideComplete=false;
                downX = motionEvent.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float move_X = motionEvent.getX() - downX;
                if (Math.abs(move_X) >= MIN_HORIZONTAL_SLIDE_DISTANCE&&!isSLideComplete) {//平移一个位置
                    Log.i(TAG, "onTouch: ");
                    if(move_X>0){//向右平移
                        prevImageView.callOnClick();
                    }else{//向左平移

                        nextImageView.callOnClick();
                    }

                    isSLideComplete=true;
                    return true;
                }
                Log.i(TAG, "水平移动：" + move_X);
                break;
            case MotionEvent.ACTION_UP:
                float move_X1 = motionEvent.getX() - downX;
                if(Math.abs(move_X1)<=1){//单击事件
                    switch (view.getId()){
                        case R.id.prevMusicImageview:
                            prevImageView.callOnClick();
                            break;
                        case R.id.nextMusicImageview:
                            nextImageView.callOnClick();
                            break;
                        case R.id.firstMusicImageview:
                            prevImageView.callOnClick();
                            prevImageView.callOnClick();
                            break;
                        case R.id.lastMusicImageview:
                            nextImageView.callOnClick();
                            nextImageView.callOnClick();
                            break;
                        default:
                            break;
                    }
                }

                isSLideComplete=true;
                Log.i(TAG, "onTouch: 离开");
                break;
        }
        return false;
    }

    //根据下标设置图片专辑显示的图片
    public void setMusicAlbumPosition(int position){

        if(position==-1){//没有音乐
            OneToast.showMessage(getContext(),"没有音乐");
            currentMusicImageview.setVisibility(View.VISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            prevMusicImageview.setVisibility(View.INVISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            lastMusicImageview.setVisibility(View.INVISIBLE);
            setMusicAlbumAlpha();
            setMusicAlbum(currentMusicImageview,null);
        }else if(position>MusicService.getMusicSize()-1){//下标超过最大值不执行任何操作
        }
        else if(MusicService.getMusicSize()==1){
            currentMusicImageview.setVisibility(View.VISIBLE);
            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            prevMusicImageview.setVisibility(View.INVISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            setMusicAlbumAlpha();
            setMusicAlbum(currentMusicImageview,MusicService.getMusicList().get(position).getImage());
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
        }else if(MusicService.getMusicSize()==2){
            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            currentMusicImageview.setVisibility(View.VISIBLE);
            prevMusicImageview.setVisibility(View.VISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            setMusicAlbum(currentMusicImageview,MusicService.getMusicList().get(position).getImage());
            int preIndex=position<=0?1:position-1;
            setMusicAlbum(prevMusicImageview,MusicService.getMusicList().get(preIndex).getImage());
            setMusicAlbumAlpha();
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);


        }else if(MusicService.getMusicSize()==3)
        {
            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            currentMusicImageview.setVisibility(View.VISIBLE);
            prevMusicImageview.setVisibility(View.VISIBLE);
            nextMusicImageview.setVisibility(View.VISIBLE);

            int size=MusicService.getMusicSize();
            int prevPostion=position==0?size-1:position-1;
            int nextPostion=(position>=(size-1))?0:position+1;
            setMusicAlbum(prevMusicImageview,MusicService.getMusicList().get(prevPostion).getImage());
            setMusicAlbum(currentMusicImageview,MusicService.getMusicList().get(position).getImage());
            setMusicAlbum(nextMusicImageview,MusicService.getMusicList().get(nextPostion).getImage());
            setMusicAlbumAlpha();
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);

        }else if(MusicService.getMusicSize()==4){
            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.VISIBLE);
            currentMusicImageview.setVisibility(View.VISIBLE);
            prevMusicImageview.setVisibility(View.VISIBLE);
            nextMusicImageview.setVisibility(View.VISIBLE);
            int size=MusicService.getMusicSize();
            int prevPostion=position==0?size-1:position-1;
            int nextPostion=(position>=(size-1))?0:position+1;
            int firstPositon=prevPostion==0?(size-1):prevPostion-1;


            setMusicAlbum(firstMusicImageview,MusicService.getMusicList().get(firstPositon).getImage());
            setMusicAlbum(prevMusicImageview,MusicService.getMusicList().get(prevPostion).getImage());
            setMusicAlbum(currentMusicImageview,MusicService.getMusicList().get(position).getImage());
            setMusicAlbum(nextMusicImageview,MusicService.getMusicList().get(nextPostion).getImage());

            setMusicAlbumAlpha();
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
        }else{
            lastMusicImageview.setVisibility(View.VISIBLE);
            firstMusicImageview.setVisibility(View.VISIBLE);
            currentMusicImageview.setVisibility(View.VISIBLE);
            prevMusicImageview.setVisibility(View.VISIBLE);
            nextMusicImageview.setVisibility(View.VISIBLE);
            int size=MusicService.getMusicSize();
            int prevPostion=position==0?size-1:position-1;
            int nextPostion=(position>=(size-1))?0:position+1;
            int firstPositon=prevPostion==0?(size-1):prevPostion-1;
            int lastPosition=nextPostion>=(size-1)?0:nextPostion+1;



            setMusicAlbum(firstMusicImageview,MusicService.getMusicList().get(firstPositon).getImage());
            setMusicAlbum(prevMusicImageview,MusicService.getMusicList().get(prevPostion).getImage());
            setMusicAlbum(currentMusicImageview,MusicService.getMusicList().get(position).getImage());
            setMusicAlbum(nextMusicImageview,MusicService.getMusicList().get(nextPostion).getImage());
            setMusicAlbum(lastMusicImageview,MusicService.getMusicList().get(lastPosition).getImage());

            setMusicAlbumAlpha();
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
        }

    }

    /**
     * 设置音乐的专辑图片
     * @param imageView
     * @param albumAddress
     */
    private void setMusicAlbum(ImageView imageView,String albumAddress){

        if(albumAddress==null){
            imageView.setImageResource(R.drawable.play_page_default_cover);
        }else{
            Bitmap bitmap= MusicIconLoader.getInstance().load(albumAddress);
            if(bitmap==null){
                imageView.setImageResource(R.drawable.play_page_default_cover);
            }else{
                imageView.setImageBitmap(bitmap);
            }
        }

    }


    /**
     * 设置专辑图片的透明度
     */
    private void setMusicAlbumAlpha(){

        Animation animationAlpha=new AlphaAnimation(1f,1f);
        Animation animationAlpha1=new AlphaAnimation(0.5f,0.5f);
        animationAlpha.setDuration(0);
        animationAlpha.setFillAfter(true);
        animationAlpha1.setFillAfter(true);
        animationAlpha1.setDuration(0);
        if(currentMusicImageview.getVisibility()==View.VISIBLE)
            currentMusicImageview.startAnimation(animationAlpha);

        if(firstMusicImageview.getVisibility()==View.VISIBLE)
            firstMusicImageview.startAnimation(animationAlpha1);

        if(prevMusicImageview.getVisibility()==View.VISIBLE)
            prevMusicImageview.startAnimation(animationAlpha1);

        if(nextMusicImageview.getVisibility()==View.VISIBLE)
            nextMusicImageview.startAnimation(animationAlpha1);

        if(lastMusicImageview.getVisibility()==View.VISIBLE)
            lastMusicImageview.startAnimation(animationAlpha1);

    }





    @Override
    public void onClick(View view) {
        if(!MusicService.isExistMusics()){
            OneToast.showMessage(getContext(),"当前没有音乐");
            return;
        }
        switch (view.getId()){
            case R.id.prevImageView:
                int currentPosition1=MusicService.getCurrentPosition();
                // currentPosition1 = currentPosition1 == 0 ? (MusicService.getMusicSize() - 1 ): currentPosition1 - 1;
                final    Intent intent1=new Intent(getContext(),MusicService.class);
                intent1.setAction(MusicService.PREV_ACTION);
                Objects.requireNonNull(getActivity()).startService(intent1);
                break;
            case R.id.playImageView:
                final    Intent intent=new Intent(getContext(),MusicService.class);
                intent.setAction(MusicService.TOGGLEPAUSE_ACTION);
                Objects.requireNonNull(getActivity()).startService(intent);
                break;
            case R.id.nextImageView:
                int currentPosition=MusicService.getCurrentPosition();
                final    Intent intent2=new Intent(getContext(),MusicService.class);
                intent2.setAction(MusicService.NEXT_ACTION);
                Objects.requireNonNull(getActivity()).startService(intent2);
                //  currentPosition =( currentPosition >= (MusicService.getMusicSize() - 1 ))? 0 : currentPosition + 1;
                break;
            default:
                break;
        }
    }

    @Override
    public void playButton(int index) {
        switch (index){
            case 0://暂停
                if(playImageView!=null){
                    //暂停
                    playImageView.setImageResource(R.drawable.pause_imageview);
                    mhandler.removeMessages(UPDATE_PROGRESS);
                }
                break;
            case 1://播放
                if(playImageView!=null){
                    //播放
                    playImageView.setImageResource(R.drawable.play_imageview);
                    mhandler.sendEmptyMessage(UPDATE_PROGRESS);
                }
                break;
        }
    }
    @Override
    public void updateUI() {
        mhandler.sendEmptyMessage(UPDATE_UI);
    }
}
