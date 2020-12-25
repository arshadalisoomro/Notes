package pk.team.inlab.app.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import pk.team.inlab.app.notes.adapter.NoteAdapter;
import pk.team.inlab.app.notes.model.Note;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter mNoteAdapter;
    private List<Note> mNoteList;
    private RecyclerView mRecyclerView;

    // Required objects
    private FirebaseFirestore mFirestoreDb;
    private CollectionReference mNotesCollectionReference;
    private ListenerRegistration mNotesListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init required objects
        mFirestoreDb = FirebaseFirestore.getInstance();
        mNotesCollectionReference = mFirestoreDb.collection(getString(R.string.notes_collection));

        mNoteList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.rv_notes);

        mNoteAdapter = new NoteAdapter(MainActivity.this, mNoteList, mFirestoreDb);
        mRecyclerView.setAdapter(mNoteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotesListenerRegistration = mNotesCollectionReference
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null){

                    // Empty list
                    mNoteList.clear();

                   for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                       Note note = doc.toObject(Note.class);
                       note.setId(doc.getId());
                       mNoteList.add(note);
                   }

                   // Notify data set changed in adapter
                    mNoteAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}