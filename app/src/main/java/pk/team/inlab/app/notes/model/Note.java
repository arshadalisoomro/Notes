package pk.team.inlab.app.notes.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Note {

    private String id;
    private String noteTitle;

    public Note(){}

    public Note(String id, String noteTitle, String noteBody) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
    }

    private String noteBody;

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
}
