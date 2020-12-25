package pk.team.inlab.app.notes;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pk.team.inlab.app.notes.adapter.NoteAdapter;
import pk.team.inlab.app.notes.model.Note;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private Button mAddNoteButton;

    // Firestore
    private FirebaseFirestore mFirestoreDb;

    private String editNoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();

        mContext = this;
        mProgressDialog = new ProgressDialog(mContext);


        mTitleEditText = findViewById(R.id.tiet_note_title);
        mBodyEditText = findViewById(R.id.tiet_note_body);
        mAddNoteButton = findViewById(R.id.btn_add_note);

        // Init Firestore Object
        mFirestoreDb = FirebaseFirestore.getInstance();

        // Get editNoteIntent String Extra
        if(getIntent().hasExtra(NoteAdapter.NOTE_ID_EXTRA)){
            // Set title of ActionBar
            actionBar.setTitle(getResources().getString(R.string.update_note));
            mAddNoteButton.setText(getResources().getString(R.string.update_note));

            editNoteId = getIntent().getStringExtra(NoteAdapter.NOTE_ID_EXTRA);
            // Edit note logic
            mFirestoreDb.collection(getResources().getString(R.string.notes_collection))
                    .document(editNoteId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Note note = task.getResult().toObject(Note.class);
                                mTitleEditText.setText(note.getNoteTitle());
                                mBodyEditText.setText(note.getNoteBody());
                            }
                        }
                    });
        } else {
            actionBar.setTitle("Add note");
        }

        // Click
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for saving data to
                String id;
                String noteTitle = "";
                String noteBody = "";

                if (editNoteId.isEmpty()){
                    // Generate unique id for each document/record
                    id = UUID.randomUUID().toString();
                } else {
                    id = editNoteId;
                }

                if (TextUtils.isEmpty(mTitleEditText.getText().toString())){
                    mTitleEditText.setError("Note title must not be empty");
                } else {
                    noteTitle = mTitleEditText.getText().toString();
                }

                if (TextUtils.isEmpty(mBodyEditText.getText().toString())){
                    mBodyEditText.setError("Note body must not be empty");
                } else {
                    noteBody = mBodyEditText.getText().toString();
                }

                if (!TextUtils.isEmpty(mTitleEditText.getText().toString())
                || !TextUtils.isEmpty(mBodyEditText.getText().toString())){
                    // Save Update
                    saveToFirestore(new Note(id, noteTitle, noteBody, new Date()));
                }

            }
        });

    }

    private void saveToFirestore(Note note) {
        mProgressDialog.setTitle("Saving note");
        mProgressDialog.show();

        Map<String, Object> noteDoc = new HashMap<>();

        noteDoc.put("noteTitle", note.getNoteTitle());
        noteDoc.put("noteBody", note.getNoteBody());
        noteDoc.put("timestamp", note.getTimestamp());

        // Firestore
        mFirestoreDb.collection(getString(R.string.notes_collection)).document(note.getId()).set(noteDoc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // This method is called when data is saved
                        mProgressDialog.dismiss();

                        // Finish Activity
                        AddNoteActivity.this.finish();

                        Toast.makeText(AddNoteActivity.this, "Note saved.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // This method will be called if any error
                        e.printStackTrace();
                    }
                });
    }
}




