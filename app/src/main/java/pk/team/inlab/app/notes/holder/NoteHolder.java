package pk.team.inlab.app.notes.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pk.team.inlab.app.notes.R;

public class NoteHolder extends RecyclerView.ViewHolder {

    private TextView mNoteTitleTextView;

    public NoteHolder(View itemView) {
        super(itemView);
        mNoteTitleTextView = itemView.findViewById(R.id.tv_item_note_title);
    }

    public void setNoteTitle(String noteTile){
        mNoteTitleTextView.setText(noteTile);
    }

}
