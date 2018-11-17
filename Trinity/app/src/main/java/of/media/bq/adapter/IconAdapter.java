package of.media.bq.adapter;
import of.media.bq.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.XIE on 2018/10/22.
 */
public class IconAdapter extends BaseAdapter {
    List<Integer> iconIdList=new ArrayList<>();

    public IconAdapter(List<Integer> iconIdList) {
        this.iconIdList = iconIdList;
    }

    public IconAdapter() {
    }

    public void setIconIdList(List<Integer> iconIdList) {
        this.iconIdList = iconIdList;
    }

    @Override
    public int getCount() {
        return iconIdList.size();
    }

    @Override
    public Object getItem(int i) {
        return iconIdList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       final ViewHolder viewHolder;
        if(view==null){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.icon_gridview_item,viewGroup,false);
            ImageView icon=view.findViewById(R.id.system_state_icon);
            viewHolder=new ViewHolder(icon);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageResource(iconIdList.get(i));
        return view;
    }
    public class  ViewHolder{
        ImageView icon;

        public ViewHolder(ImageView icon) {
            this.icon = icon;
        }

        public ImageView getIcon() {
            return icon;
        }
    }
}
