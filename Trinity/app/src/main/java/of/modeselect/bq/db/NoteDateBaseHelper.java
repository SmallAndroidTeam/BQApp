package of.modeselect.bq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import of.modeselect.bq.toast.OneToast;

public class NoteDateBaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_NOTE_TABLE = "create table if not exists note("
            +"id integer primary key autoincrement,"
            +"path text not null,"
            + "title text not null)";
    private Context mContext;
    public NoteDateBaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
        Log.i("fgsjafgdhajfgr", "创建note数据库成功");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("drop table if exists note");
      onCreate(db);
    }
}
