package pk.team.inlab.app.notes.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.List;

import pk.team.inlab.app.notes.AddNoteActivity;
import pk.team.inlab.app.notes.R;
import pk.team.inlab.app.notes.holder.NoteViewHolder;
import pk.team.inlab.app.notes.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context mContext;
    private List<Note> mNoteList;
    private FirebaseFirestore mFirebaseFirestore;
    private ProgressDialog mProgressDialog;

    public static final String NOTE_ID_EXTRA = "note_id";

    public NoteAdapter(Context mContext, List<Note> mNoteList, FirebaseFirestore mFirebaseFirestore) {
        this.mContext = mContext;
        this.mNoteList = mNoteList;
        this.mFirebaseFirestore = mFirebaseFirestore;
        this.mProgressDialog = new ProgressDialog(this.mContext);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = mNoteList.get(position);

        holder.setEditOrDeleteItemClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Show popup menu
                PopupMenu popupMenu = new PopupMenu(mContext, v, Gravity.END);
                // Menu inflate
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit_delete_item, popupMenu.getMenu());

                // code for setting icons visible on popupMenu
                try {
                    Method method = popupMenu.getMenu().getClass()
                            .getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    method.setAccessible(true);
                    method.invoke(popupMenu.getMenu(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        switch (id){
                            case R.id.menu_item_edit : {
                                Intent editNoteIntent = new Intent(mContext, AddNoteActivity.class);

                                editNoteIntent.putExtra(NOTE_ID_EXTRA, note.getId());

                                mContext.startActivity(editNoteIntent);
                                break;
                            }
                            case R.id.menu_item_delete : {

                                mProgressDialog.setTitle("Deleting note");
                                mProgressDialog.show();

                                mFirebaseFirestore.collection(mContext.getString(R.string.notes_collection))
                                        .document(mNoteList.get(position).getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(mContext, "Note deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, "Error in deleting Note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                mProgressDialog.dismiss();;
                                break;
                            }
                        }

                        return false;
                    }
                });

                return false;
            }
        });

        holder.setNoteTitle(note.getNoteTitle());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}