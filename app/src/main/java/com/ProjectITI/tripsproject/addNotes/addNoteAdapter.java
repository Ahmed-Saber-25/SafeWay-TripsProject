package com.ProjectITI.tripsproject.addNotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.R;

import java.util.List;

public class addNoteAdapter extends RecyclerView.Adapter<addNoteAdapter.ViewHolder> {

    private Context context;
    private List<String> values;
    View mainView;

    public addNoteAdapter(Context context, List<String> values) {
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater in = LayoutInflater.from(parent.getContext());
        mainView = in.inflate(R.layout.field, parent, false);
        addNoteAdapter.ViewHolder vh = new addNoteAdapter.ViewHolder(mainView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.note.setText(values.get(position).toString());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.remove(values.get(position));
              notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public Button delete;
        public ViewHolder(View v) {
            super(v);
            note = v.findViewById(R.id.txt_note);
            delete = v.findViewById(R.id.delete_button);


        }
    }
}
