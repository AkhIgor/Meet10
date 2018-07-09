package com.example.meet10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private TextView name;
    private TextView content;
    private FloatingActionButton fab;
    private NoteDao noteDao;

    public static long id;
    private Note note;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = (TextView) findViewById(R.id.nameText);
        content = (TextView) findViewById(R.id.contentText);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        Intent intent = getIntent();
        try {
            name.setText(intent.getStringExtra("Name"));
            content.setText(intent.getStringExtra("Content"));
            id = intent.getLongExtra("ID", 1);
        } catch (NullPointerException ignored) {}

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBase db = MyRoom.getInstance().getDatabase();
                noteDao = db.getNoteDAO();

                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
                date = format.format(new Date());
            }
        }).start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = new Note(id, name.getText().toString(), date, content.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        noteDao.updateItem(note);
                        MainActivity.NoteList = noteDao.getNotes();
                        //db.close();
                    }
                }).start();

//                Intent main = new Intent(EditActivity.this, MainActivity.class);
//                startActivity(main);
//                MainActivity.NoteList.remove(id);
//                MainActivity.NoteList.add((int) id, note);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditActivity.this);
        String textSize = preferences.getString("text_size", "14");
        name.setTextSize(Float.parseFloat(textSize));
        content.setTextSize(Float.parseFloat(textSize));

        String textStyle = preferences.getString("text_style", "Обычный");
        int typeFace = Typeface.NORMAL;
        if (textStyle.contains("Полужирный")) {
            typeFace += Typeface.BOLD;
        }
        if (textStyle.contains("Курсив")) {
            typeFace += Typeface.ITALIC;
        }
        name.setTypeface(null, typeFace);
        content.setTypeface(null, typeFace);
    }
}
