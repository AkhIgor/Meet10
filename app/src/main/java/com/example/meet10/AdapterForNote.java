package com.example.meet10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Игорь on 06.07.2018.
 */

public class AdapterForNote extends RecyclerView.Adapter<AdapterForNote.ViewHolder> {

    public interface INotesAdapterCallback {
        void openEditor(String text, String color, long id, int position);
        void delete(long id);
    }

    INotesAdapterCallback callback;

    private List<Note> ListNote;
    private Context context;
    private MainActivity.OnPositionListener listener;

    public AdapterForNote(List<Note> listNote, Context context, MainActivity.OnPositionListener listener) {
        ListNote = listNote;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = ListNote.get(position);

        holder.Name.setText(note.getName());
        holder.Date.setText(note.getDate());
        holder.Content.setText(note.getInfo());
        holder.id = note.getID();

        setSettings(holder);
    }

    @Override
    public int getItemCount() {
        try {
            return ListNote.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void setSettings(ViewHolder holder) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
        String textSize = preferences.getString("text_size", "14");
        holder.Name.setTextSize(Float.parseFloat(textSize));
        holder.Date.setTextSize(Float.parseFloat(textSize));
        holder.Content.setTextSize(Float.parseFloat(textSize));

        String textStyle = preferences.getString("text_style", "Обычный");
        int typeFace = Typeface.NORMAL;
        if (textStyle.contains("Полужирный")) {
            typeFace += Typeface.BOLD;
        }
        if (textStyle.contains("Курсив")) {
            typeFace += Typeface.ITALIC;
        }
        holder.Name.setTypeface(null, typeFace);
        holder.Date.setTypeface(null, typeFace);
        holder.Content.setTypeface(null, typeFace);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView Date;
        public TextView Content;
        public ImageButton EditButton;
        public ImageButton DeleteButton;

        private long id;
        private Note note;
        private DataBase db;
        private NoteDao noteDao;

        public ViewHolder(final View itemView) {
            super(itemView);

            Name = (TextView) itemView.findViewById(R.id.nameText);
            Date = (TextView) itemView.findViewById(R.id.dateText);
            Content = (TextView) itemView.findViewById(R.id.infoText);
            EditButton = (ImageButton) itemView.findViewById(R.id.openButton);
            DeleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);

            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(context, getAdapterPosition(), id, Name.getText().toString(), Content.getText().toString());
                }
                //                @Override
//                public void onClick(View v) {
//                    Intent editNote = new Intent(context, EditActivity.class);
//                    editNote.putExtra("ID", id);
//                    editNote.putExtra("Name", Name.getText().toString());
//                    editNote.putExtra("Content", Content.getText().toString());
//                    context.startActivity(editNote);
            });

            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db = MyRoom.getInstance().getDatabase();
                            noteDao = db.getNoteDAO();
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            note = new Note(id, Name.getText().toString(), Date.getText().toString(), Content.getText().toString());
                            noteDao.deleteItem(note);
                        }
                    }).start();

                    ListNote.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

    }
}
