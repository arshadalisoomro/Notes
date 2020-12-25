package pk.team.inlab.app.notes.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pk.team.inlab.app.notes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout mLinearLayout;
    private TextView mNoteTitleTextView;

    public NoteViewHolder(View itemView) {
        super(itemView);
        mNoteTitleTextView = itemView.findViewById(R.id.tv_item_note_title);
        mLinearLayout = itemView.findViewById(R.id.ll_item_note);
    }

    public void setEditOrDeleteItemClickListener(View.OnLongClickListener onLongClickListener){
        mLinearLayout.setOnLongClickListener(onLongClickListener);
    }

    public void setNoteTitle(String noteTile){
        mNoteTitleTextView.setText(noteTile);
    }

}
