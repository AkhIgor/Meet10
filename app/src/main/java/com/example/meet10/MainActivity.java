package com.example.meet10;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DataBase db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Note> NoteList;
    private NoteDao noteDao;

    private static final int CREATING = 1;
    private static final int CHANGING = 2;

    private Context context = this;

    protected int position;

    private long ID;
    private String Name, Date, Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder
                        (getApplicationContext(),
                                DataBase.class,
                                "Calculator_db")
                        .fallbackToDestructiveMigration()
                        .build();

                noteDao = db.getNoteDAO();
                NoteList = noteDao.getNotes();
            }
        }).start();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNote = new Intent(MainActivity.this, CreatingActivity.class);
                startActivityForResult(createNote, CREATING);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new AdapterForNote(NoteList, this, new PositionListener());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Intent intent = getIntent();
            switch (requestCode) {
                case CREATING:
                {
                    long id = 0;
                    if(NoteList.size() != 0)
                        id = NoteList.get(NoteList.size() - 1).getID();

                    Name = data.getStringExtra("Name");
                    Date = data.getStringExtra("Date");
                    Content = data.getStringExtra("Content");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Note note = new Note(Name, Date, Content);
                            noteDao.addItem(note);
                        }
                    }).start();

                    Note note = new Note( id, data.getStringExtra("Name"), data.getStringExtra("Date"), data.getStringExtra("Content"));
                    NoteList.add(note);
                    adapter.notifyDataSetChanged();

                    break;
                }
                case CHANGING:

                    ID = data.getLongExtra("ID" , 0);
                    Name = data.getStringExtra("Name");
                    Date = data.getStringExtra("Date");
                    Content = data.getStringExtra("Content");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Note note = new Note(ID, Name, Date,Content);
                            noteDao.updateItem(note);
                        }
                    }).start();

                    Note note = new Note(data.getIntExtra("ID", 0), data.getStringExtra("Name"), data.getStringExtra("Date"), data.getStringExtra("Content"));
                    NoteList.set(position, note);

                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);

        return super.onOptionsItemSelected(item);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public interface OnPositionListener{
        void onClick(Context context, int position, long id, String name, String content);
    }

    private class PositionListener implements OnPositionListener {

        Context context;

        @Override
        public void onClick(Context context, int position, long id, String name, String content) {
            this.context = context;
            Intent intent = new Intent(context, EditActivity.class);
            setPosition(position);
            intent.putExtra("ID", id);
            intent.putExtra("Name", name);
            intent.putExtra("Content", content);
            startActivityForResult(intent, CHANGING);
        }
    }
}
