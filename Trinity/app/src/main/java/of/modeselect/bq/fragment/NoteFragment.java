package of.modeselect.bq.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import of.modeselect.bq.db.NoteDataBaseOperator;
import of.modeselect.bq.utils.FileUtil;
import of.modeselect.bq.R;
import of.modeselect.bq.adapter.NoteAdapter;
import of.modeselect.bq.bean.NoteBean;
import of.modeselect.bq.toast.OneToast;

public class NoteFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lvNote;
    private EditText evWrite;
    private ImageView ivAddNote;
    private NoteAdapter noteAdapter;
    private LinearLayout llsave;
    private LinearLayout llcancle;
    private String saveNoteStatus="";//保存笔记开始的状态数据
    private final static int SAVE_SUCCESS=1;//保存数据成功
    private final static int SHOW_NOTE_STATUS=2;//显示便签的数据
    private final static int UPDATE_NOTE_STATUS=3;//更新适配器中的数据
    private final static int SAVE_FAIL=4;//保存数据失败
    private NoteDataBaseOperator noteDataBaseOperator;
    @SuppressLint("HandlerLeak")
    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SAVE_SUCCESS:
                    OneToast.showMessage(getContext(),"保存成功");
                    break;
                case SAVE_FAIL:
                    OneToast.showMessage(getContext(),"保存失败");
                    break;
                case SHOW_NOTE_STATUS:
                    if(msg.obj!=null){
                        String content= (String) msg.obj;
                        evWrite.setText(content);
                        saveNoteStatus=content;
                    }
                    break;
                case UPDATE_NOTE_STATUS:
                    noteAdapter.notifyDataSetChanged();
                    break;
                    default:
                        break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note,container,false);
        initView(view);
        setClick();
        return view;
    }
    
    private void setClick() {
        ivAddNote.setOnClickListener(this);
        llsave.setOnClickListener(this);
        llcancle.setOnClickListener(this);
        lvNote.setOnItemClickListener(this);
    }
    
    private void initView(View view) {
        lvNote = view.findViewById(R.id.lv_note);
        ivAddNote = view.findViewById(R.id.iv_add_note);
        evWrite = view.findViewById(R.id.ev_write);
        llsave=view.findViewById(R.id.ll_save);
        llcancle=view.findViewById(R.id.ll_cancle);
        
        noteDataBaseOperator=new NoteDataBaseOperator(getContext());
        noteAdapter=new NoteAdapter(getContext(),noteDataBaseOperator.queryAllNote());
        lvNote.setAdapter(noteAdapter);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_note:
                noteAdapter.setId(-1);
                mhander.sendEmptyMessage(UPDATE_NOTE_STATUS);
                evWrite.setFocusable(true);
                evWrite.setFocusableInTouchMode(true);
                evWrite.requestFocus();
                evWrite.setText("");
           // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            break;
            case R.id.ll_cancle:
                final String currentEt=evWrite.getText().toString().trim();
                if(noteAdapter.getId()<0&&currentEt.contentEquals("")){
                    OneToast.showMessage(getContext(),"内容为空");
                    return;
                }
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext()).setCancelable(false).setTitle("提醒")
                        .setMessage("是否确认删除？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                         
                            }
                        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("kfsjkdf", "onClick: "+noteAdapter.getId());
                                if(noteAdapter.getId()<0){
                                    evWrite.setText("");
                                }else{
                                    if(noteAdapter.getId()<(noteAdapter.getNoteBeanList().size())){
                                  FileUtil.getInstance().deleteOneNoteByNotePath(noteAdapter.getNoteBeanList().get(noteAdapter.getId()).getNotePath());
                                  OneToast.showMessage(getContext(),"删除成功");
                                 
                                  evWrite.setText("");
                                  saveNoteStatus="";
                                   new Thread(new Runnable() {
                                       @Override
                                       public void run() {
                                        synchronized (this){
                                            noteAdapter.setNoteBeanList(noteDataBaseOperator.queryAllNote());
                                            noteAdapter.setId(-1);
                                            mhander.sendEmptyMessage(UPDATE_NOTE_STATUS);
                                        }
                                       }
                                   }).start();
                                    }
                                }
                              
                            }
                        });
                alertDialog.show();
                break;
            case R.id.ll_save:
                final String content=evWrite.getText().toString().trim();
                if(noteAdapter.getId()<0){//新建的标签，点击直接保存
                    if(content.contentEquals("")){
                        OneToast.showMessage(getContext(),"内容为空");
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this){
                                if(FileUtil.getInstance().saveNote(content)){
                                    saveNoteStatus=content;
                                     mhander.sendEmptyMessage(SAVE_SUCCESS);
                                    noteAdapter.setNoteBeanList(noteDataBaseOperator.queryAllNote());
                                     noteAdapter.setId(noteAdapter.getCount()-1);
                                     mhander.sendEmptyMessage(UPDATE_NOTE_STATUS);
                                    Log.i("kfsjkdf", "run: "+noteAdapter.getId()+"//"+noteAdapter.getCount());
                         
                                }
                            }
                        }
                    }).start();
                }else {
                    if(content.contentEquals(saveNoteStatus)){
                        OneToast.showMessage(getContext(),"已保存");
                    }else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(noteAdapter.getId()<noteAdapter.getNoteBeanList().size()){
                                    if(FileUtil.getInstance().updateNoteStatusByNotePath(noteAdapter.getNoteBeanList().get(noteAdapter.getId()).getNotePath(),content,noteAdapter.getNoteBeanList().get(noteAdapter.getId()).getNoteTitle())){
                                                  mhander.sendEmptyMessage(SAVE_SUCCESS);
                                        noteAdapter.setNoteBeanList(noteDataBaseOperator.queryAllNote());
                                        mhander.sendEmptyMessage(UPDATE_NOTE_STATUS);
                                        saveNoteStatus=content;
                                    }else{
                                        mhander.sendEmptyMessage(SAVE_FAIL);
                                    }
                                }
                            }
                        }).start();
                     
                    }
                }
                break;
                default:
                    break;
        }
        
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
     
         new Thread(new Runnable() {
             @Override
             public void run() {
                 synchronized (this){
                     final String path=noteAdapter.getNoteBeanList().get(position).getNotePath();
                 
                     noteAdapter.setId(position);
                     mhander.sendEmptyMessage(UPDATE_NOTE_STATUS);
                     FileUtil.getInstance().readNote(path);
                     mhander.obtainMessage(SHOW_NOTE_STATUS,FileUtil.getInstance().getReadNoteStatus()).sendToTarget();
                 }
               
             }
         }).start();
    }
    /**
     File file = new File(FileUtil.getBasePath()+noteBeans.size()+"/txt");
     try {
     if(!file.exists()){
     file.createNewFile();
     }
     BufferedWriter bufferedWriter=new BufferedWriter();
     bufferedWriter.write();
     RandomAccessFile raf = new RandomAccessFile(file, "rwd");
     raf.seek(file.length());
     raf.write(content.getBytes());
     raf.close();
     }catch (IOException e) {
     e.printStackTrace();
     }
     */
}
