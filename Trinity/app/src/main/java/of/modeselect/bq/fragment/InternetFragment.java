package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import of.modeselect.bq.R;
import of.modeselect.bq.adapter.BrowerTitleBarAdapter;
import of.modeselect.bq.toast.OneToast;

public class InternetFragment extends Fragment implements PageFragment.UpdatePageTitle {

    private ImageView ivBrowserLeftSplitLine;
    private RelativeLayout rlBrowerRightSplit;
    private RecyclerView recyclerViewBrowserTitle;
    private BrowerTitleBarAdapter browerTitleBarAdapter;
    private ImageView ivPageAdd;
    private List<Fragment> pageFragments=new ArrayList<>();//存储所有页面的fragment

   private final static int UPDATE_PAGE_TITLE=0;//更新网页标题
    private final static int SET_ADAPTER=1;//给gridViewBrowserTitle设置适配器
    private final static int MOVE_CURRENT_TITLE=2;//移动到打开的页面的标题
    private  final static int TITLE_WIDTH=300;//标题的宽度(单位px)
    private final static int MAX_SHOW_COUNT=7;//最大显示的网页的数量
    private  final  static String NO_OPERATION_FRAGMENT="no_operation_fragment";//不要操作framgment
    @SuppressLint("HandlerLeak")
    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
          switch (msg.what){
              case UPDATE_PAGE_TITLE://更新网页标题
          if(browerTitleBarAdapter!=null){
              setRecyclerViewBrowserTitleWidth(browerTitleBarAdapter.getItemCount());
                  browerTitleBarAdapter.notifyDataSetChanged();
              recyclerViewBrowserTitle.smoothScrollToPosition(browerTitleBarAdapter.getPositon());
              if(msg.obj==null||!String.valueOf(msg.obj).contentEquals(NO_OPERATION_FRAGMENT)){//关闭的不是当前页面也就不会重新显示对应的页面了
                  showOneFragmentByIndex(browerTitleBarAdapter.getPositon());
              }

          }
              break;
              case MOVE_CURRENT_TITLE:
                  if(browerTitleBarAdapter!=null){
                      recyclerViewBrowserTitle.smoothScrollToPosition(browerTitleBarAdapter.getPositon());
                  }
                  break;
              case SET_ADAPTER://给gridViewBrowserTitle设置适配器
            if(browerTitleBarAdapter!=null){
                setRecyclerViewBrowserTitleWidth(browerTitleBarAdapter.getItemCount());
                recyclerViewBrowserTitle.setAdapter(browerTitleBarAdapter);
            }
                  break;
              default:
                  break;
          }
        }
    };

    /**
     * 设置recyclerViewBrowserTitle的宽度
     * @param count
     * count为网页的总数
     */
    private void setRecyclerViewBrowserTitleWidth(int count){

        if(count<0){
            return;
        }else if(count<=MAX_SHOW_COUNT){
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) recyclerViewBrowserTitle.getLayoutParams();
            layoutParams.width=count*TITLE_WIDTH;
            recyclerViewBrowserTitle.setLayoutParams(layoutParams);
        }else{
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) recyclerViewBrowserTitle.getLayoutParams();
            layoutParams.width=MAX_SHOW_COUNT*TITLE_WIDTH;
            recyclerViewBrowserTitle.setLayoutParams(layoutParams);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_internet,container,false);
        initView(view);
        initEvents();
         setDefaultPage();
        return view;
    }

    private void initEvents() {
        ivPageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (this){
                    PageFragment pageFragment=new PageFragment();
                    pageFragment.setUpdatePageTitle(InternetFragment.this);
                    pageFragment.setPosition(pageFragments.size());
                     pageFragments.add(pageFragment);
                    addOneFragmentByIndex(pageFragments.size()-1);
                }

            }
        });

    }




    private void initView(View view) {
        ivBrowserLeftSplitLine = view.findViewById(R.id.iv_browser_left_split_line);
        rlBrowerRightSplit = view.findViewById(R.id.rl_brower_right_split);
        recyclerViewBrowserTitle = view.findViewById(R.id.recycler_view_browser_title);
        ivPageAdd = view.findViewById(R.id.iv_page_add);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBrowserTitle.setLayoutManager(linearLayoutManager);
    }

    private void setDefaultPage() {

        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            pageFragments.clear();
          PageFragment pageFragment=new PageFragment();
          pageFragment.setUpdatePageTitle(this);
          pageFragment.setPosition(0);
           pageFragments.add(pageFragment);
          fragmentTransaction.add(R.id.framelayout_page,pageFragments.get(0));
          fragmentTransaction.commit();


    }

    //添加对应下标的fragment
    private synchronized void addOneFragmentByIndex(int index){

        if(index<0||index>(pageFragments.size()-1)){
            return;
        }
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAddFragment(fragmentTransaction);

        Fragment fragment=pageFragments.get(index);
        if(fragment!=null){
            fragmentTransaction.add(R.id.framelayout_page,fragment);
        }
        fragmentTransaction.commit();
        showOneFragmentByIndex(index);
    }

    //显示对应下标的fragment
    private void showOneFragmentByIndex(int index){

     if(index<0||index>(pageFragments.size()-1)){
         return;
     }
        final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
     hideAddFragment(fragmentTransaction);

      Fragment fragment=pageFragments.get(index);

     if(fragment!=null){
         fragmentTransaction.show(fragment);
     }
       fragmentTransaction.commit();
    }

    /**
     * 移除对应下标的fragment，同时把fragment里面对面的position的值变化一下
     * @param index
     */
 private void remoteOneFragmentByIndex(int index){
     if(index<0||index>(pageFragments.size()-1)){
         return;
     }
     final FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
     final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
    // hideAddFragment(fragmentTransaction);

     Fragment fragment=pageFragments.get(index);
     if(fragment!=null){
         fragmentTransaction.remove(fragment);
     }
     fragmentTransaction.commit();
     pageFragments.remove(index);
     for(int i=0;i<pageFragments.size();i++){
         Fragment fragment1=pageFragments.get(i);
         if(fragment1!=null){
             ((PageFragment)fragment1).setPosition(i);
         }
     }

 }

    private void hideAddFragment(FragmentTransaction fragmentTransaction){
        for(Fragment fragment:pageFragments){
            if(fragment!=null){
                fragmentTransaction.hide(fragment);
            }
        }

    }

    @Override
    public synchronized void updatePageTitle(final int position, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
             synchronized (this){
                 Log.i("Jfskjdfks", "run: "+title+"//"+position+"//"+pageFragments.size());
                if(position<0||position>(pageFragments.size()-1)){
                    return;
                }else//更新标题
                {
                    if(browerTitleBarAdapter==null){//第一次加载intent界面会出现这中情况
                        List<String> pageTitleList=new ArrayList<>();
                        pageTitleList.add(title);
                        browerTitleBarAdapter=new BrowerTitleBarAdapter();
                        browerTitleBarAdapter.setPageTitleList(pageTitleList);
                        browerTitleBarAdapter.setPositon(position);
                        mhander.sendEmptyMessage(SET_ADAPTER);

                        browerTitleBarAdapter.setSetOnItemClickListener(new BrowerTitleBarAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                browerTitleBarAdapter.setPositon(position);
                                mhander.sendEmptyMessage(UPDATE_PAGE_TITLE);
                            }

                            @Override
                            public void onDeleteButtonItemClick(View view, int position) {//点击关闭按钮
                                if(position<0||position>(pageFragments.size()-1)){
                                    return;
                                }else if(pageFragments.size()>1){
                                    int currentShowIndex=browerTitleBarAdapter.getPositon();//正在显示的标题的下标
                                    if(position==currentShowIndex){//如果关闭的是正在显示的界面
                                        browerTitleBarAdapter.remoteTitleListByIndex(position);
                                        int index=(position>=(pageFragments.size()-1))?(pageFragments.size()-2):position;
                                        remoteOneFragmentByIndex(position);//移除对应下标的fragment
                                        browerTitleBarAdapter.setPositon(index);
                                        mhander.sendEmptyMessage(UPDATE_PAGE_TITLE);
                                        mhander.sendEmptyMessage(MOVE_CURRENT_TITLE);
                                    }else{
                                        browerTitleBarAdapter.remoteTitleListByIndex(position);
                                        remoteOneFragmentByIndex(position);//移除对应下标的fragment
                                        int showIndex=(currentShowIndex<position)?currentShowIndex:currentShowIndex-1;
                                        browerTitleBarAdapter.setPositon(showIndex);
                                        mhander.obtainMessage(UPDATE_PAGE_TITLE,NO_OPERATION_FRAGMENT).sendToTarget();
                                    }

                                }else{//只剩下一个页面
                                    remoteOneFragmentByIndex(position);//移除对应下标的fragment
                                    browerTitleBarAdapter.remoteTitleListByIndex(position);
                                    browerTitleBarAdapter.setPositon(-1);
                                    mhander.sendEmptyMessage(UPDATE_PAGE_TITLE);
                                    browerTitleBarAdapter=null;
                                    setDefaultPage();//设置默认的界面
                                }


                            }
                        });

                    }else{
                        browerTitleBarAdapter.updateTitleByIndex(position,title);
                        browerTitleBarAdapter.setPositon(position);

                        mhander.sendEmptyMessage(UPDATE_PAGE_TITLE);
                        mhander.sendEmptyMessage(MOVE_CURRENT_TITLE);
                    }

                }
             }
            }
        }).start();
    }
}
