package of.media.bq.adapter;
import of.media.bq.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import of.media.bq.bean.Gallery;
import of.media.bq.bean.GetThumbnail;
import of.meida.bq.convertPXAndDP.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/25.
 */
public class LocalGalleryAdapter extends BaseAdapter{
  private Context context;
   private List<Gallery> galleryList=new ArrayList<>();
    public final static int GALLERY_WIDTH=113;//图片的宽度(单位dp）
    public final static int GALLERY_Height=113;//图片的高度(单位dp）
    public LocalGalleryAdapter(Context context, List<Gallery> galleryList) {
        this.context = context;
        this.galleryList = galleryList;
    }

    public List<Gallery> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<Gallery> galleryList) {
        this.galleryList = galleryList;
    }

    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public Object getItem(int i) {
        return galleryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view==null){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.local_gallery_adapter_item,viewGroup,false);
            ImageView galleryImageview=view.findViewById(R.id.galleryImageview);
            viewHolder=new ViewHolder(galleryImageview);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
          Gallery gallery=galleryList.get(i);
        if(gallery.getThumbnail()==null){
            gallery.setThumbnail(GetThumbnail.getImageThumbnailById(context.getResources(),galleryList.get(i).getGalleryAddress(),
                    DensityUtil.dip2px(context,GALLERY_WIDTH), DensityUtil.dip2px(context,GALLERY_Height)));
            galleryList.set(i,gallery);
        }
        viewHolder.galleryImageview.setImageBitmap(gallery.getThumbnail());
        return view;
    }
    public class  ViewHolder{
        ImageView galleryImageview;

        public ViewHolder(ImageView galleryImageview) {
            this.galleryImageview = galleryImageview;
        }

        public ImageView getGalleryImageview() {
            return galleryImageview;
        }
    }
}
