package com.ProjectITI.tripsproject.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ProjectITI.tripsproject.R;

import java.util.ArrayList;

public class showNotes extends AppCompatActivity {
    ArrayList<String> result;
    NotesAdapter adapter;
    RecyclerView recyclerView;
    TextView txt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notes);
        result = new ArrayList<String>();
        final Intent intent = getIntent();
        result = intent.getStringArrayListExtra("data");

      DisplayMetrics dm = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(dm);

      int width= dm.widthPixels;
      int height = dm.heightPixels;

      getWindow().setLayout((int)(width*.8),(int)(height*.6));

         recyclerView = findViewById(R.id.recycle_view);
         txt = findViewById(R.id.no_notes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        if(result==null || result.size() ==0 )
        {
            recyclerView.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
        }else{
            adapter = new NotesAdapter(result, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
        }
    }


}

