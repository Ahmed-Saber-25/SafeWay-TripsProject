package com.ProjectITI.tripsproject.ui.Upcoming_Trips;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ProjectITI.tripsproject.R;
import java.util.ArrayList;
import java.util.List;

public class showNotesAdapter extends RecyclerView.Adapter<showNotesAdapter.ViewHolder> {

    List<String>NotestList;
    View mainView ;
    private Context context;


    public showNotesAdapter(List<String> NotestList, Context context) {
        this.NotestList = NotestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater in = LayoutInflater.from(parent.getContext());
        mainView = in.inflate(R.layout.notes_row_floating, parent, false);
        ViewHolder vh = new ViewHolder(mainView);
//        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row_floating,parent,false);
//        ViewHolder viewHolder=new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       // if(NotestList.get(position) != null) {
            holder.note.setText(NotestList.get(position));
       // }else{
           // holder.note.setText("there are no notes to show !! ");

       // }
    }
    public int getItemCount() {
        return NotestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public CheckBox check;
        public ViewHolder(View v) {
            super(v);
            note = v.findViewById(R.id.noteRowTextView);
            check = v.findViewById(R.id.noteRowCheckBox);


        }
    }
}
