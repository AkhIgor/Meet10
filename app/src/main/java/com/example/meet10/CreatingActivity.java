package com.example.meet10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreatingActivity extends AppCompatActivity {

    private TextView name;
    private TextView content;
    private FloatingActionButton fab;
    private NoteDao noteDao;

    private String date;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating);

        name = (TextView) findViewById(R.id.nameText);
        content = (TextView) findViewById(R.id.infoText);

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());
        date = format.format(new Date());

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                note = new Note(name.getText().toString(), date, content.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataBase db = MyRoom.getInstance().getDatabase();
                        noteDao = db.getNoteDAO();
                        noteDao.addItem(note);
                        //db.close();
                    }
                }).start();

                //Intent main = new Intent(CreatingActivity.this, MainActivity.class);
                MainActivity.NoteList.add(note);
                //startActivity(main);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CreatingActivity.this);
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
