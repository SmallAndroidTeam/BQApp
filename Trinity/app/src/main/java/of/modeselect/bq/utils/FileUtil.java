package of.modeselect.bq.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import of.modeselect.bq.db.NoteDataBaseOperator;
import of.modeselect.bq.localInformation.App;

public class FileUtil {
    private static final String notePathPrefix="note_";
    private static final String notePathSuffix=".txt";
    private static String basePath;//保存便签的固定目录
    private  int noteId=0;
    private static  FileUtil fileUtil;
    private String readNoteStatus="";
    private Context context;
    private   static   NoteDataBaseOperator  noteDataBaseOperator;
    public  static FileUtil getInstance(){
        if(fileUtil==null){
            fileUtil=new FileUtil();
            if(noteDataBaseOperator==null)
            noteDataBaseOperator=new NoteDataBaseOperator(App.sContext);
        }
        return fileUtil;
    }
    
    public int getNoteId() {
        return noteId;
    }
    
    /**
     * 获取保存便签的固定目录
     * @return
     */
    public  boolean getBasePath(){
    String path=null;
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        path = Environment.getExternalStorageDirectory() + "/Note/";
    }else{
        path= Environment.getExternalStoragePublicDirectory("0")+"/Note/";
    }
    File file=new File(path);
    if(!file.exists()){
     if(file.mkdirs()){
         basePath=path;
         return true;
     }else {
         basePath=null;
         return  false;
     }
    }
    basePath=path;
    return  true;
  }
    
    /**
     * 获取下一个文件的ID
     * @return
     */
  private int getNextID(){

      return noteDataBaseOperator.getNextId();
  }
    
    /**
     * 创建新的笔记并且保存内容
     * @param content
     * @return
     */
  public boolean saveNote(String content){
      
      String title=null;
    if(!getBasePath()){//创建目录失败
      return  false;
    }
    if(content==null){
        return false;
    }
      noteId=getNextID();
      String path=basePath+notePathPrefix+noteId+notePathSuffix;
      File file;
      while (new File(path).exists()){
          noteId++;
          path=basePath+notePathPrefix+noteId+notePathSuffix;
      }
      file=new File(path);
      BufferedWriter bufferedWriter=null;
      try {
          if(file.createNewFile())
          {
              
              String[] lines=content.split("\n");
              bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false)));
              for(int i=0;i<lines.length;i++) {
                  if(i==0){
                      title=lines[0];
                  }
               String line=lines[i];
                 // Log.i("bq1212",i+"----------"+line);
               bufferedWriter.write(line+"\n");
              }
             bufferedWriter.flush();
              noteDataBaseOperator.saveNote(noteId,path,title);
            return true;
          }else{
              return false;
          }
      } catch (IOException e) {
          e.printStackTrace();
          //如果文件创建成功但是保存数据时失败，就会删除此文件
          return false;
      }finally {
          if(bufferedWriter!=null){
              try {
                  bufferedWriter.close();
                //  Log.i("fgsjafgdhajfgr",path+"//"+title);
                
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
  }
    
    /**
     * 删除一个便签通过对应的本地存储路径
     * @param notePath
     */
  public void deleteOneNoteByNotePath(String notePath){
  File file=new File(notePath);
  if(file.exists()){
      file.delete();//删除本地文件
  }
  noteDataBaseOperator.deleteOneNoteByNotePath(notePath);//删除数据库
  }
    
    /**
     * 更新便签内容
     * @param notePath
     * @param content
     * @param title
     * @return
     */
  
  public boolean updateNoteStatusByNotePath(String notePath,String content,String title) {
     if(notePath==null||content==null||title==null){
         return false;
     }
       File   file=new File(notePath);
       if(!file.exists()){
           try {
               if(file.createNewFile()){
    
                   BufferedWriter bufferedWriter=null;
                   try {
                       String newTitle="";
                       String[] lines=content.split("\n");
                       bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false)));
                       for(int i=0;i<lines.length;i++) {
                           if(i==0){
                               newTitle=lines[0];
                           }
                           String line=lines[i];
                           // Log.i("bq1212",i+"----------"+line);
                           bufferedWriter.write(line+"\n");
                       }
                       bufferedWriter.flush();
                       if(!newTitle.trim().contentEquals(title.trim())){
                           return noteDataBaseOperator.updateNoteTitleByNotePath(notePath,newTitle);
                       }
                       return true;
                   } catch (IOException e) {
                       e.printStackTrace();
                       //如果文件创建成功但是保存数据时失败，就会删除此文件
                       return false;
                   }finally {
                       if(bufferedWriter!=null){
                           try {
                               bufferedWriter.close();
                               //  Log.i("fgsjafgdhajfgr",path+"//"+title);
                
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               }else{
                   return false;
               }
           } catch (IOException e) {
               e.printStackTrace();
               return false;
           }
       }else{
           BufferedWriter bufferedWriter=null;
           try {
                   String newTitle="";
                   String[] lines=content.split("\n");
                   bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false)));
                   for(int i=0;i<lines.length;i++) {
                       if(i==0){
                           newTitle=lines[0];
                       }
                       String line=lines[i];
                       // Log.i("bq1212",i+"----------"+line);
                       bufferedWriter.write(line+"\n");
                   }
                   bufferedWriter.flush();
                   if(!newTitle.trim().contentEquals(title.trim())){
                     return noteDataBaseOperator.updateNoteTitleByNotePath(notePath,newTitle);
                   }
                   return true;
           } catch (IOException e) {
               e.printStackTrace();
               //如果文件创建成功但是保存数据时失败，就会删除此文件
               return false;
           }finally {
               if(bufferedWriter!=null){
                   try {
                       bufferedWriter.close();
                       //  Log.i("fgsjafgdhajfgr",path+"//"+title);
                
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
    
       }
       
  }
  
    public String getReadNoteStatus() {
        return readNoteStatus;
    }
    
    /**\
     * 读取便签内容
     * @param path
     * @return
     */
  public boolean readNote(String path){
      readNoteStatus="";
      File file=new File(path);
      if(!file.exists()){
          return false;
      }else {
          BufferedReader bufferedReader=null;
          try {
              bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
              String line;
              while((line=bufferedReader.readLine())!=null){
                  readNoteStatus+=line+"\n";
              }
              return true;
          } catch (Exception e) {
              e.printStackTrace();
              return false;
          }
          finally {
              if(bufferedReader!=null){
                  try {
                      bufferedReader.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }
      }
      
  }
  
  
}
