package of.modeselect.bq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import of.modeselect.bq.R;
import of.modeselect.bq.bean.NoteBean;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private List<NoteBean> noteBeanList=new ArrayList<>();
    private int id=-1;//用户选中的标题的下标
    public NoteAdapter(Context context,List<NoteBean> noteBeanList){
        this.context=context;
        this.noteBeanList.addAll(noteBeanList);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<NoteBean> getNoteBeanList() {
        return noteBeanList;
    }
    
    public void setNoteBeanList(List<NoteBean> noteBeanList) {
        this.noteBeanList.clear();
        this.noteBeanList.addAll(noteBeanList);
    }
    
    @Override
    public int getCount() {
        return noteBeanList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return noteBeanList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_note_listview_item,parent,false);
            viewHolder.imageView=convertView.findViewById(R.id.iv_spot);
            viewHolder.textView=convertView.findViewById(R.id.tv_note_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        NoteBean noteBean=noteBeanList.get(position);
        viewHolder.imageView.setImageResource(R.drawable.spot);
        if(noteBean.getNoteTitle()!=null) {
            if(noteBean.getNoteTitle().trim().contentEquals("")){
                viewHolder.textView.setText("无标题");
            }else{
                viewHolder.textView.setText(noteBean.getNoteTitle());
            }
        }else{
            viewHolder.textView.setText("无标题");
        }
        
        final Animation halfAlphaAnimation=new AlphaAnimation(0.5f,0.5f);
        halfAlphaAnimation.setDuration(0);
        halfAlphaAnimation.setFillAfter(true);
        
        final Animation fullAlphaAnimation=new AlphaAnimation(1f,1f);
        fullAlphaAnimation.setDuration(0);
        fullAlphaAnimation.setFillAfter(true);
        if(id==-1){
            viewHolder.textView.setTextColor(0x80ffffff);
            viewHolder.imageView.startAnimation(halfAlphaAnimation);
        }else if(id!=position){
            viewHolder.textView.setTextColor(0x80ffffff);
            viewHolder.imageView.startAnimation(halfAlphaAnimation);
        }else{
            viewHolder.textView.setTextColor(0xffffffff);
            viewHolder.imageView.startAnimation(fullAlphaAnimation);
        }
        
        return convertView;
    }
    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
