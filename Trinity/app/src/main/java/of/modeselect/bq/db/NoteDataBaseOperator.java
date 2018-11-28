package of.modeselect.bq.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import of.modeselect.bq.bean.NoteBean;

public class NoteDataBaseOperator {
    private NoteDateBaseHelper dbHelper;
    private SQLiteDatabase db;
    public NoteDataBaseOperator(Context context) {
        dbHelper = new NoteDateBaseHelper(context, "note", null, 1);
        db = dbHelper.getWritableDatabase();
    }
    //保存便笺
    public void saveNote(int id,String path,String title) {
        db.execSQL("insert into note(id,path,title) values(?,?,?)",
                new Object[] {id,path,title});
    }
    //取消便笺
    public void deleteNote(String path){
            db.execSQL("delete from note where path=?", new String[] { path });
    }
    // 查询所有便笺
    public List<NoteBean> queryAllNote() {
        ArrayList<NoteBean> noteBeans = new ArrayList<NoteBean>();
        Cursor c = db.rawQuery("select * from note", null);
        if(c.moveToFirst()){
            do{
                NoteBean noteBean = new NoteBean();
                noteBean.setNotePath(c.getString(c.getColumnIndex("path")));
                noteBean.setId(c.getInt(c.getColumnIndex("id")));
                noteBean.setNoteTitle(c.getString(c.getColumnIndex("title")));
                noteBeans.add(noteBean);
            }  while (c.moveToNext());
        }
        c.close();
        return noteBeans;
    }
    
    /**
     * 获取下一个数据的id
     * @return
     */
    public  int getNextId(){
        Cursor cursor=db.rawQuery("select * from note order by id desc",null);
        if(cursor.moveToFirst()){
         return cursor.getInt(cursor.getColumnIndex("id"))+1;
        }else{
            return 1;
        }
    }
    
    /**
     * 删除一条便签记录通过便签本地的路径
     * @param notePath
     * @return
     */
    public boolean deleteOneNoteByNotePath(String notePath){
        db.beginTransaction();
        try{
            db.execSQL("delete from note where path = ?",new Object[]{notePath});
            db.setTransactionSuccessful();
            return true;
        }catch (Exception e){
        return false;
        }finally {
            db.endTransaction();
        }
    }
    
    /**
     * 更新便签的标题通过便签的路径
     * @param notePath
     * @param title
     * @return
     */
    public boolean updateNoteTitleByNotePath(String notePath,String title){
        db.beginTransaction();
        try{
            db.execSQL("update note set title = ? where path = ?",new Object[]{title,notePath});
            db.setTransactionSuccessful();
            return true;
        }catch (Exception e){
        return  false;
        }finally {
            db.endTransaction();
        }
    }
    
    // 判断信息是否已经存在
    public boolean Dataexist(String id1,String path,String title) {
        String Query = "Select id from note where id=?and path=? and notecontent=?";
        Cursor cursor = db.rawQuery(Query, new String[] { id1,path,title});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
