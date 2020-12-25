package pk.team.inlab.app.notes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pk.team.inlab.app.notes.adapter.NoteAdapter;
import pk.team.inlab.app.notes.model.Note;

public class DetailsActivity extends AppCompatActivity {

    // Firestore
    private FirebaseFirestore mFirestoreDb;

    private TextView mNoteTitleTextView;
    private TextView mNoteBodyTextView;

    String noteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Init Firestore Object
        mFirestoreDb = FirebaseFirestore.getInstance();

        mNoteTitleTextView = findViewById(R.id.tv_details_note_title);
        mNoteBodyTextView = findViewById(R.id.tv_details_note_body);

        // Get editNoteIntent String Extra
        if(getIntent().hasExtra(NoteAdapter.NOTE_ID_EXTRA)){
            noteId = getIntent().getStringExtra(NoteAdapter.NOTE_ID_EXTRA);

            mFirestoreDb.collection(getResources().getString(R.string.notes_collection))
                    .document(noteId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Note note = task.getResult().toObject(Note.class);
                                mNoteTitleTextView.setText(note.getNoteTitle());
                                mNoteBodyTextView.setText(note.getNoteBody());
                            }
                        }
                    });
        }


    }
}