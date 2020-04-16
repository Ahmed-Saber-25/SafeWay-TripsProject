package com.ProjectITI.tripsproject.ui.Trips_History;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ProjectITI.tripsproject.Alert.Trip_alert;
import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.ProjectITI.tripsproject.Notes.showNotes;

import java.util.ArrayList;
import java.util.List;

public class RV_Trips_HistoryAdapter extends RecyclerView.Adapter<RV_Trips_HistoryAdapter.ViewHolder>  {
    private Context context;
    private List<Trip> values;
    LayoutInflater layoutInflater;
    LayoutInflater in;
    View mainView;
    View card_id;


    public RV_Trips_HistoryAdapter(Context context, List<Trip> values) {
        this.context = context;
        this.values = values;
    }


    @NonNull
    @Override
    public RV_Trips_HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        in = LayoutInflater.from(parent.getContext());
        mainView = in.inflate(R.layout.history_trips_details, parent, false);
        ViewHolder vh = new ViewHolder(mainView);
        card_id = mainView.findViewById(R.id.card_id);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RV_Trips_HistoryAdapter.ViewHolder holder, final int position) {
        holder.status.setText(values.get(position).getStatus());
        holder.tripname.setText(values.get(position).getName());
        holder.from.setText(values.get(position).getFrom());
        holder.to.setText(values.get(position).getTo());
        holder.date.setText(values.get(position).getDate());
        holder.time.setText(values.get(position).getTime());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.showDetails.getVisibility() == View.VISIBLE) {
                    slideDown(holder.showDetails);
                } else {
                    slideUp(holder.showDetails);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trip_id = values.get(position).getId();
                delete(trip_id);

            }
        });
       holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> trip_notes = new ArrayList<>();
                trip_notes = values.get(position).getNotes();
                showNotes(trip_notes);
            }
        });


    }

    @Override
    public int getItemCount() {
        return values.size();
    }


    public void showNotes(ArrayList<String> trip_notes) {
        Intent intent = new Intent(context, showNotes.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putStringArrayListExtra("data", trip_notes);
        context.startActivity(intent);
    }


    public void delete(String tripid) {
        Intent intent = new Intent(context, Trip_alert.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("trip_id", tripid);
        intent.putExtra("action", "delete");
        intent.putExtra("state","allstatus");

        context.startActivity(intent);

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tripname;
        public Button delete;
        public TextView status;
        public FrameLayout f;
        public ImageView img;
        public TextView from;
        public TextView to;
        public TextView date;
        public TextView time;
        public View showDetails;
        public Button notes;

        public ViewHolder(View v) {
            super(v);
            view = v;
            tripname = v.findViewById(R.id.history_trip_name);
            delete = v.findViewById(R.id.history_Delete);
            status = v.findViewById(R.id.history_status);
            img = v.findViewById(R.id.history_imageView_id);
            //  f = v.findViewById(R.id.fragment_details);
            from = v.findViewById(R.id.history_From_id);
            to = v.findViewById(R.id.history_To_id);
            date = v.findViewById(R.id.history_Date_id);
            time = v.findViewById(R.id.history_time_id);
            notes = v.findViewById(R.id.history_Notes);
            showDetails = v.findViewById(R.id.historyshowTripsDetails);

        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,                 // toXDelta
                500,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);


    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()*2);          // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

}
