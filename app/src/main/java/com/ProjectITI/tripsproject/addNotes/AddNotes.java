package com.ProjectITI.tripsproject.addNotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.ProjectITI.tripsproject.ui.Trips_History.RV_Trips_HistoryAdapter;

import java.util.ArrayList;
import java.util.List;


public class AddNotes extends AppCompatActivity {

    TextView note ;
        private LinearLayout parentLinearLayout;
    static RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    static addNoteAdapter mAdapter;
    static ArrayList<String> newData = new ArrayList<>();
    String trip_id;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_notes);
            parentLinearLayout = findViewById(R.id.parent_linear_layout);

            note = findViewById(R.id.notes_text);
            recyclerView = findViewById(R.id.note_recycle_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            Intent intent = getIntent();
             trip_id = intent.getStringExtra("tripId");
            newData = intent.getStringArrayListExtra("notes");
            if(newData != null)
            {
                mAdapter = new addNoteAdapter(getApplicationContext(), newData);
                recyclerView.setAdapter(mAdapter);
            }
            Log.i("tag","trip id  : "+trip_id);

        }
        public void onAddNote(View v) {
            String getNote  = note.getText().toString();
            if(getNote.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Empty Note",Toast.LENGTH_LONG).show();
            }else {
                newData.add(getNote);
                mAdapter = new addNoteAdapter(getApplicationContext(), newData);
                recyclerView.setAdapter(mAdapter);
                note.setText("");
            }
        }

        public void onClickSubmit(View v) {
            Log.i("tag", String.valueOf(newData));
            TripDao tripDao = new TripDao();
            tripDao.addNotes(trip_id , newData);
            finish();
        }


    }
