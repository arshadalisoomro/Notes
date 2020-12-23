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

    FirebaseFirestore firestoreDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mContext = this;

        mTitleEditText = findViewById(R.id.tiet_note_title);
        mBodyEditText = findViewById(R.id.tiet_note_body);
        mAddNoteButton = findViewById(R.id.btn_add_note);

        mProgressDialog = new ProgressDialog(mContext);

        // Init Firestore Object
        firestoreDb = FirebaseFirestore.getInstance();

        // Click
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for saving data to
                String id = UUID.randomUUID().toString();
                String noteTitle = mTitleEditText.getText().toString();
                String noteBody = mBodyEditText.getText().toString();

                saveToFirestore(new Note(id, noteTitle, noteBody));

            }
        });

    }

    private void saveToFirestore(Note note) {
        mProgressDialog.setTitle("Saving note");
        mProgressDialog.show();

        Map<String, Object> noteDoc = new HashMap<>();

        noteDoc.put("id", note.getId());
        noteDoc.put("noteTitle", note.getNoteTitle());
        noteDoc.put("noteBody", note.getNoteBody());

        // Firestore
        firestoreDb.collection("Notes").document(note.getId()).set(noteDoc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // This method is called when data is saved
                        mProgressDialog.dismiss();

                        // Empty the EditTexts
                        mTitleEditText.setText("");
                        mBodyEditText.setText("");

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




