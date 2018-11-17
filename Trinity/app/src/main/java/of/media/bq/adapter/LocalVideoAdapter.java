package of.media.bq.adapter;
import of.media.bq.R;


import android.content.Intent;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import of.media.bq.activity.PlayVideoActivity;
import of.media.bq.bean.Video;
import of.media.bq.saveData.SaveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/24.
 */
public class LocalVideoAdapter extends BaseAdapter {
    private List<Video> videoList=new ArrayList<>();
    private int position=0;//当前选中的视频下标
    public LocalVideoAdapter(List<Video> videoList) {
     this.videoList=videoList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {

        SaveData.copy(videoList,this.videoList);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        Log.i("trinity16", "getView: "+videoList.size());
        final  ViewHolder viewHolder;
        if(view==null){
           view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.local_video_adapter_item,viewGroup,false);
            RelativeLayout videRelativeLayout=view.findViewById(R.id.videRelativeLayout);
            ImageView videoThumbnail=view.findViewById(R.id.videoThumbnail);
            TextView videoNameTextView=view.findViewById(R.id.videoNameTextView);
            ImageView videoPauseImageView=view.findViewById(R.id.videoPauseImageView);
            viewHolder=new ViewHolder(videRelativeLayout,videoThumbnail,videoNameTextView,videoPauseImageView);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }

         viewHolder.videoThumbnail.setImageResource(videoList.get(i).getVideoThumbnail());
          viewHolder.videoNameTextView.setText(videoList.get(i).getVideoName());

        if(i==position){
            viewHolder.videRelativeLayout.setBackgroundResource(R.drawable.video_image_frame);
            viewHolder.videoNameTextView.setVisibility(View.VISIBLE);

        }else{
            viewHolder.videRelativeLayout.setBackground(null);
            viewHolder.videoNameTextView.setVisibility(View.GONE);
        }

        final int index=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("trinity16", "onClick: 音乐列表的长度："+videoList.size()+"//"+index+"//"+SaveData.getLocalVideoList().size());
                if(videoList.size()==0){
                    SaveData.copy(SaveData.getLocalVideoList(),videoList);
                }
                if(position==index){

                                        PlayVideoActivity.setVideoList(videoList);
                                        Intent intent=new Intent(viewGroup.getContext(),PlayVideoActivity.class);
                                        intent.putExtra("position",index);

                                       viewGroup.getContext().startActivity(intent);

                }
                else{
                    position=index;
                    TransitionSet transitionSet=new TransitionSet();
                    Fade fade=new Fade();
                    Slide slide=new Slide();
                    transitionSet.addTransition(slide).addTransition(fade);
                    TransitionManager.beginDelayedTransition((ViewGroup) view,transitionSet);
                     notifyDataSetChanged();
                }

            }
        });

        return view;
    }
    public class ViewHolder{
    RelativeLayout videRelativeLayout;
    ImageView videoThumbnail;
    TextView videoNameTextView;
    ImageView videoPauseImageView;

        public ViewHolder(RelativeLayout videRelativeLayout, ImageView videoThumbnail, TextView videoNameTextView, ImageView videoPauseImageView) {
            this.videRelativeLayout = videRelativeLayout;
            this.videoThumbnail = videoThumbnail;
            this.videoNameTextView = videoNameTextView;
            this.videoPauseImageView = videoPauseImageView;
        }

        public RelativeLayout getVideRelativeLayout() {
            return videRelativeLayout;
        }

        public ImageView getVideoThumbnail() {
            return videoThumbnail;
        }

        public TextView getVideoNameTextView() {
            return videoNameTextView;
        }

        public ImageView getVideoPauseImageView() {
            return videoPauseImageView;
        }
    }
}
