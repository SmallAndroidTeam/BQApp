package of.modeselect.bq.bean;

public class NoteBean {
   private int id;
    private String notePath;
    private String noteTitle;
    
    public NoteBean() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNotePath() {
        return notePath;
    }
    
    public void setNotePath(String notePath) {
        this.notePath = notePath;
    }
    
    public String getNoteTitle() {
        return noteTitle;
    }
    
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
}
