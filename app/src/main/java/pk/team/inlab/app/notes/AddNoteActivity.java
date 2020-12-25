package pk.team.inlab.app.notes;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pk.team.inlab.app.notes.model.Note;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private Button mAddNoteButton;

    // Firestore
    private FirebaseFirestore mFirestoreDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mContext = this;

        mTitleEditText = findViewById(R.id.tiet_note_title);
        mBodyEditText = findViewById(R.id.tiet_note_body);
        mAddNoteButton = findViewById(R.id.btn_add_note);

        // Init Firestore Object
        mFirestoreDb = FirebaseFirestore.getInstance();

        // Click
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for saving data to
                String noteTitle = mTitleEditText.getText().toString();
                String noteBody = mBodyEditText.getText().toString();

                // Generate unique id for each document/record
                String id = UUID.randomUUID().toString();

                saveToFirestore(new Note(id, noteTitle, noteBody, new Date()));

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




