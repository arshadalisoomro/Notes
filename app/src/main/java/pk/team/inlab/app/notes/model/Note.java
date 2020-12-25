package pk.team.inlab.app.notes.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Note {

    private String id;
    private String noteTitle;
    private String noteBody;
    @ServerTimestamp
    private Date timestamp;

    public Note(){}

    public Note(String id, String noteTitle, String noteBody, Date timestamp) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
