package of.modeselect.bq.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import of.modeselect.bq.R;

/**
 * Created by MR.XIE on 2018/11/21.
 */
public class BrowerTitleBarAdapter extends RecyclerView.Adapter<BrowerTitleBarAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> pageTitleList=new ArrayList<>();
    private int positon=-1;
     private OnItemClickListener onItemClickListener;

    public synchronized void updateTitleByIndex(int index,String title){//更新标题
        Log.i("Jfksjdkf", "updateTitleByIndex: "+index+"//"+title+"//"+pageTitleList.size());
       if(index>=0&&index<pageTitleList.size()){
           pageTitleList.set(index,title);
       }else{
           pageTitleList.add(index,title);
       }
    }

    public void setSetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void remoteTitleListByIndex(int index){//移除一个标题
     if(index<0||index>(pageTitleList.size()-1)){
         return;
     }
     pageTitleList.remove(index);
    }
    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public List<String> getPageTitleList() {
        return pageTitleList;
    }

    public void setPageTitleList(List<String> pageTitleList) {
        this.pageTitleList.clear();
        this.pageTitleList.addAll(pageTitleList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.intent_recycler_item_brower_title_bar,viewGroup,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       viewHolder.tvPageTitle.setText(pageTitleList.get(i));
        viewHolder.rlPageTitle.setTag(i);
        viewHolder.ivClosePage.setTag(i);
        final Animation noSelectAnimation=new AlphaAnimation(0.5f,0.5f);
        noSelectAnimation.setFillAfter(true);
        noSelectAnimation.setDuration(0);
        final Animation selectAnimation=new AlphaAnimation(1f,1f);
        selectAnimation.setFillAfter(true);
        selectAnimation.setDuration(0);
       if(positon==-1){
           viewHolder.tvPageTitle.setTextColor(0x80ffffff);//50%透明度
           viewHolder.ivClosePage.startAnimation(noSelectAnimation);
       }else if (positon!=i){
           viewHolder.tvPageTitle.setTextColor(0x80ffffff);
           viewHolder.ivClosePage.startAnimation(noSelectAnimation);
       }else{
           viewHolder.tvPageTitle.setTextColor(0xffffffff);//100%透明度
           viewHolder.ivClosePage.startAnimation(selectAnimation);
       }
    }

    @Override
    public int getItemCount() {
        return pageTitleList.size();
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.rl_page_title:
               if(onItemClickListener!=null){
                   onItemClickListener.onItemClick(view, (Integer) view.getTag());
               }
               break;
           case R.id.iv_close_page:
               if(onItemClickListener!=null){
                   onItemClickListener.onDeleteButtonItemClick(view, (Integer) view.getTag());
               }
               break;
       }

    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView tvPageTitle;
        ImageView ivClosePage;
        RelativeLayout rlPageTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPageTitle=itemView.findViewById(R.id.tv_page_title);
            ivClosePage=itemView.findViewById(R.id.iv_close_page);
            rlPageTitle=itemView.findViewById(R.id.rl_page_title);
            rlPageTitle.setOnClickListener(BrowerTitleBarAdapter.this);
            ivClosePage.setOnClickListener(BrowerTitleBarAdapter.this);
        }
    }
  public   interface  OnItemClickListener{
        void onItemClick(View view,int position);//点击标题
        void onDeleteButtonItemClick(View view,int position);//点击关闭按钮
    }

}
