package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
            List<Music>  musicList=addData();
            if(musicList.size()>0){
                MusicService.initMusicService(musicList,0);
                setMusicAlbumPosition(0);
            }else{
                OneToast.showMessage(getContext(),"请导入至少5首mp3歌到本地");
                setMusicAlbumPosition(-1);
            }
            musicTitle.setText(MusicService.getMusicTitle(0));
            singerName.setText(MusicService.getMusicArtist(0));
        }

    }
    private List<Music> addData() {
        MusicUtils.initMusicList();
        List<Music>  musicList=MusicUtils.sMusicList;
        List<Music> testMusicList=new ArrayList<>();
        final Integer[] musicAlbumIDs=new Integer[]{R.drawable.mp2,R.drawable.mp5,R.drawable.mp3,R.drawable.mp1,R.drawable.mp4};
        // adb push G:\Trinity_project\music /storage/emulated/0/Music



//        if(musicList==null){
//            return  testMusicList;
//        }else{
//            Music music=null;
//            for(int i=0;i<4;i++)
//            {    music=musicList.get(i);
//                music.setAlbumImageId(musicAlbumIDs[i]);
//                testMusicList.add(music);
//            }
//            return  testMusicList;
//        }



        if(musicList==null||musicList.size()<5){
            return  testMusicList;
        }else{
            Music music=null;
            for(int i=0;i<musicAlbumIDs.length;i++)
            {    music=musicList.get(i);
                music.setAlbumImageId(musicAlbumIDs[i]);
                testMusicList.add(music);
            }
            return  testMusicList;
        }
    }

    private List<String> getMusicAddress(){
        List<String> musicAddress=new ArrayList<>();
        String sdAddress=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//sd卡是否存在
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//sd卡存储路径
        }else {
            sdAddress=Environment.getExternalStorageDirectory().getAbsolutePath();//本地存储路径
        }
        sdAddress+="/Music/music";
        File file=new File(sdAddress);
        if(!file.exists()||file.isFile()){
            return null;
        }else{
            File[] files=file.listFiles();
            for(File file1:files){
                if (file1.getAbsolutePath().endsWith(".mp3")){
                    musicAddress.add(file1.getAbsolutePath());
                }
            }
        }
        return musicAddress;
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
        Animation animationAlpha=new AlphaAnimation(1f,1f);
        Animation animationAlpha1=new AlphaAnimation(0.5f,0.5f);
        animationAlpha.setDuration(0);
        animationAlpha.setFillAfter(true);
        animationAlpha1.setFillAfter(true);
        animationAlpha1.setDuration(0);
        currentMusicImageview.startAnimation(animationAlpha);
        firstMusicImageview.startAnimation(animationAlpha1);
        prevMusicImageview.startAnimation(animationAlpha1);
        nextMusicImageview.startAnimation(animationAlpha1);
        lastMusicImageview.startAnimation(animationAlpha1);

        if(position==-1){//没有音乐
            OneToast.showMessage(getContext(),"没有音乐");
            currentMusicImageview.setVisibility(View.VISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            prevMusicImageview.setVisibility(View.INVISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            lastMusicImageview.setVisibility(View.INVISIBLE);

        }else if(position>MusicService.getMusicSize()-1){//下标超过最大值不执行任何操作

        }
        else if(MusicService.getMusicSize()==1){

            currentMusicImageview.setVisibility(View.VISIBLE);
            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            prevMusicImageview.setVisibility(View.INVISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            currentMusicImageview.setImageResource(MusicService.getMusicList().get(position).getAlbumImageId());
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
        }else if(MusicService.getMusicSize()==2){

            lastMusicImageview.setVisibility(View.INVISIBLE);
            firstMusicImageview.setVisibility(View.INVISIBLE);
            currentMusicImageview.setVisibility(View.VISIBLE);
            currentMusicImageview.setImageResource(MusicService.getMusicList().get(position).getAlbumImageId());
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
            prevMusicImageview.setVisibility(View.VISIBLE);
            nextMusicImageview.setVisibility(View.INVISIBLE);
            int preIndex=position<=0?1:position-1;
            prevMusicImageview.setImageResource(MusicService.getMusicList().get(preIndex).getAlbumImageId());

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
            prevMusicImageview.setImageResource(MusicService.getMusicList().get(prevPostion).getAlbumImageId());
            currentMusicImageview.setImageResource(MusicService.getMusicList().get(position).getAlbumImageId());

            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);
            nextMusicImageview.setImageResource(MusicService.getMusicList().get(nextPostion).getAlbumImageId());
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
            firstMusicImageview.setImageResource(MusicService.getMusicList().get(firstPositon).getAlbumImageId());
            prevMusicImageview.setImageResource(MusicService.getMusicList().get(prevPostion).getAlbumImageId());
            currentMusicImageview.setImageResource(MusicService.getMusicList().get(position).getAlbumImageId());
            nextMusicImageview.setImageResource(MusicService.getMusicList().get(nextPostion).getAlbumImageId());
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
            firstMusicImageview.setImageResource(MusicService.getMusicList().get(firstPositon).getAlbumImageId());
            prevMusicImageview.setImageResource(MusicService.getMusicList().get(prevPostion).getAlbumImageId());
            currentMusicImageview.setImageResource(MusicService.getMusicList().get(position).getAlbumImageId());
            nextMusicImageview.setImageResource(MusicService.getMusicList().get(nextPostion).getAlbumImageId());
            lastMusicImageview.setImageResource(MusicService.getMusicList().get(lastPosition).getAlbumImageId());
            final AnimationSet animationSet= (AnimationSet) AnimationUtils.loadAnimation(getContext(),R.anim.music_album_change);
            currentMusicImageview.startAnimation(animationSet);

        }
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
