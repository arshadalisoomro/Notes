package pk.team.inlab.app.notes.model;

public class Note {

    private String noteTitle;
    private String noteBody;

    public Note(String noteTitle, String noteBody) {
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
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
