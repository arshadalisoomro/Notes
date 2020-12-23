package pk.team.inlab.app.notes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pk.team.inlab.app.notes.R;
import pk.team.inlab.app.notes.holder.NoteHolder;
import pk.team.inlab.app.notes.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    private Context mContext;
    private List<Note> mNoteList;

    public NoteAdapter(Context mContext, List<Note> mNoteList) {
        this.mContext = mContext;
        this.mNoteList = mNoteList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = mNoteList.get(position);

        holder.setNoteTitle(note.getNoteTitle());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}