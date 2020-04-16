package com.ProjectITI.tripsproject.ui.Upcoming_Trips;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.ProjectITI.tripsproject.AddTrip.AddTrip;
import com.ProjectITI.tripsproject.Alert.Trip_alert;
import com.ProjectITI.tripsproject.Model.Trip;
import com.ProjectITI.tripsproject.Model.TripDao;
import com.ProjectITI.tripsproject.R;
import com.ProjectITI.tripsproject.TimePickerFragment;
import com.ProjectITI.tripsproject.addNotes.AddNotes;
import com.ProjectITI.tripsproject.updateTrip.UpdateTripData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.ProjectITI.tripsproject.ShowAlertDialog.floatingIntent;
import static com.ProjectITI.tripsproject.ui.Upcoming_Trips.HomeFragment.DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>  {
  //  private upcomingContract.PresenterInterface upcomingPresenter = new upcomingPresenter(this);
    private final Context context;
    private List<Trip> values;
    private Trip trip;

    showNotesAdapter adapter;
    RecyclerView recyclerView;




    public RecycleViewAdapter(Context context, List<Trip> values) {
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater in = LayoutInflater.from(parent.getContext());
        View v = in.inflate(R.layout.upcoming_trips_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewAdapter.ViewHolder holder, final int position) {
        if (values.get(position).isWait())
        {
            String text = "\nWaiting";
            String text2 =  values.get(position).getName()+text;


            Spannable spannable = new SpannableString(text2);

            spannable.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.colorPrimaryLight)), values.get(position).getName().length(), text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tripname.setText(spannable, TextView.BufferType.SPANNABLE);
        }
        else
        {
            holder.tripname.setText(values.get(position).getName());
        }

        holder.from.setText(values.get(position).getFrom());
        holder.to.setText(values.get(position).getTo());
        holder.data.setText(values.get(position).getDate());
        holder.time.setText(values.get(position).getTime());




        holder.spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                List<String> trip_notes;
                trip_notes = values.get(position).getNotes();
                PopupMenu popup = new PopupMenu(context, holder.spinner);
                for (int i = 0; i < trip_notes.size(); i++) {
                    popup.getMenu().add(Menu.NONE, 1, Menu.NONE, trip_notes.get(i));
                }
                popup.show();

            }
        });
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Notes:
                                String id = values.get(position).getId();
                                ArrayList<String> notes = values.get(position).getNotes();
                                Notes(id, notes);
                                break;

                            case R.id.Cancel:
                                String tripId = values.get(position).getId();
                                cancel(tripId);
                                break;

                            case R.id.Delete:
                                String trip_id = values.get(position).getId();
                                delete(trip_id);
                                break;

                            case R.id.Edit:
                                Intent edit = new Intent(view.getContext(), UpdateTripData.class);
                                edit.putExtra("tripName", values.get(position).getName());
                                edit.putExtra("startPoint", values.get(position).getFrom());
                                edit.putExtra("endPoint", values.get(position).getTo());
                                edit.putExtra("date", values.get(position).getDate());
                                edit.putExtra("time", values.get(position).getTime());
                                edit.putExtra("type", values.get(position).getType());
                                edit.putExtra("repeat", values.get(position).getRepeat());
                                edit.putExtra("id", values.get(position).getId());
                                (view.getContext()).startActivity(edit);
                                break;

                        }
                        return false;
                    }
                });
                popup.show();

            }
        });

        holder.start_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean add2request = false;
                TripDao tripDao  = new TripDao();

                int trip_position = position;

                String source = values.get(position).getFrom();
                String des = values.get(position).getTo();

                String repeat = values.get(position).getRepeat();
                String name = values.get(position).getName();
                String startPoint = values.get(position).getFrom();
                String endPoint = values.get(position).getTo();
                String startDate = values.get(position).getDate();
                String time = values.get(position).getTime();
                String type = values.get(position).getType();
                ArrayList<String> notes = values.get(position).getNotes();

                // No Repeat, Repeat Daily, Repeat Weekly, Repeat Monthly
                if (repeat.equals("Repeat Weekly") || repeat.equals("Repeat Daily") || repeat.equals("Repeat Monthly")) {

                    Trip new_trip = new Trip(name, startPoint, endPoint, time, startDate, "upcoming", type, repeat,false);
                    addNewTrip_Reapet(repeat, new_trip, notes);
                    add2request = true;
                }
                //One Way Trip, Round Trip
                if (type.equals("Round Trip")) {

                    Trip new_trip = new Trip(name, startPoint, endPoint, time, startDate, "upcoming", type, repeat,true);
                    addNewTrip_RoundTrip(new_trip,notes,add2request);
                }
                String tripId = values.get(trip_position).getId();
                TripDao tripDao2 = new TripDao();
                tripDao2.DoneTrip(tripId);

                TripDao tr = new TripDao();
                if (floatingIntent != null)
                {
                    context.stopService(floatingIntent);
                }
                floatingIntent = new Intent(context, FloatingWidgetService.class);
                //newIntent.putExtra("position", values.get(position).getId());
                floatingIntent.putStringArrayListExtra("notes", notes);
                context.startService(floatingIntent);

                //((Activity)context).finish();

               // homeFragment.getActivity().getFragmentManager().popBackStack();
                gotToMap(source, des);

            }
        });
    }

    private void addNewTrip_RoundTrip(Trip trip, ArrayList<String> notes,boolean add) {
        String start = trip.getTo();
        String end = trip.getFrom();
        trip.setRepeat("No Repeat");
        trip.setStatus("upcoming");
        trip.setType("One Way Trip");
        trip.setFrom(start);
        trip.setTo(end);
        trip.setWait(true);
        trip.setName(trip.getName()+" - BACK");
        TripDao tripDao = new TripDao();
        tripDao.AddTrip(trip, notes,null,add);



        /*
        Intent intent = new Intent(context,showCalenderandTimepicker.class);
        intent.putExtra("tripName", trip.getName());
        intent.putExtra("endPoint", trip.getTo());
        intent.putExtra("date", trip.getDate());
        intent.putExtra("time", trip.getTime());
        intent.putExtra("type", trip.getType());
        intent.putExtra("repeat", trip.getRepeat());
        intent.putExtra("startPoint", trip.getFrom());

        intent.putStringArrayListExtra("notes", notes);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

*/
    }


    private void addNewTrip_Reapet(String repeat, Trip trip, ArrayList<String> notes) {
        String newDate = "";
        Date trip_date;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            trip_date = df.parse(trip.getDate());
            Log.i("tag", "new Date  : " + trip_date.toString());
            cal.setTime(trip_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }



        //Log.i("tag", "day " + day + "/" + Month + "/" + year);

        if (repeat.equals("Repeat Weekly")) {
            cal.add(Calendar.WEEK_OF_YEAR,1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
           /*
            int days = day + 7;
            if(days>31)
            {
                int num = day + 7 - 31 ;
                int newMonth = Month + 1;
                if(newMonth >12)
                {
                    newDate = num + "/" + 1 + "/" + year+1;
                }else{
                    newDate = num + "/" + newMonth + "/" + year;
                }

            }else{
                newDate = (day + 7) + "/" + Month + "/" + year;
            }*/
            trip.setDate(newDate);

        } else if (repeat.equals("Repeat Daily")) {

            cal.add(Calendar.DAY_OF_MONTH,1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
            /*
            int num = day +1;
            if(num >31)
            {
                int newMonth = Month + 1;
                if(newMonth >12)
                {
                    newDate = 1 + "/" + 1 + "/" + year+1;

                }else{
                    newDate = 1 + "/" + newMonth + "/" + year;
                }
            }else{
                newDate = (day + 1) + "/" + Month + "/" + year;
            }*/
            trip.setDate(newDate);


        } else if (repeat.equals("Repeat Monthly")) {
            cal.add(Calendar.MONTH,1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int Month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            newDate = day + "/" + Month + "/" + year;
            /*
            int newMonth = Month+1;
            if(newMonth >12)
            {
                newDate = day + "/" + 1 + "/" + year+1;
            }else{
                newDate = day + "/" + (Month + 1) + "/" + year;
            }*/
            trip.setDate(newDate);
        }
        // Trip trip = trip_name, start, end, time, date, status, trip_type, trip_repeat);
        ArrayList<String> notes2 = new ArrayList<>();
        TripDao tripDao = new TripDao();
        tripDao.AddTrip(trip, notes,cal,false);
        // upcomingPresenter.getAllData();

    }
    @Override
    public int getItemCount() {
        return values.size();
    }


    public void cancel(String tripId) {

        Intent cancel = new Intent(context, Trip_alert.class);
        cancel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cancel.putExtra("trip_id", tripId);
        cancel.putExtra("action", "cancel");
        context.startActivity(cancel);
    }


    public void Notes(String tripid, ArrayList<String> notes) {

        Intent note = new Intent(context, AddNotes.class);
        note.putExtra("tripId", tripid);
        note.putExtra("notes", notes);
        note.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(note);

    }



    public void delete(String tripid) {

        Intent delete = new Intent(context, Trip_alert.class);
        delete.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        delete.putExtra("trip_id", tripid);
        delete.putExtra("action", "delete");
        delete.putExtra("state", "upcoming");
        context.startActivity(delete);

    }


    public void gotToMap(String source, String destiaion) {
        //If permission is granted start floating widget service
        String uri = "http://maps.google.com/maps?f=d&hl=en" + "&daddr=" + destiaion;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tripname;
        public TextView from;
        public TextView to;
        public TextView data;
        public TextView time;
        public ImageView img;
        public TextView spinner;
        public Button start_trip;
        public TextView buttonViewOption;
        public CardView c;

        public ViewHolder(View v) {
            super(v);
            view = v;
            tripname = v.findViewById(R.id.TripName_id);
            from = v.findViewById(R.id.From_id);
            to = v.findViewById(R.id.To_id);
            data = v.findViewById(R.id.Date_id);
            time = v.findViewById(R.id.time_id);
            img = v.findViewById(R.id.imageView_id);
            c = v.findViewById(R.id.card_id);
            spinner = v.findViewById(R.id.showNotes);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            start_trip = v.findViewById(R.id.start);

        }
    }

}
