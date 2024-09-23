package com.unipi.mobile_dev.hippocratesjournal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {

    private List<Incident> incidentList;
    private Context context;

    public IncidentAdapter(Context context, List<Incident> incidentList) {
        this.context = context;
        this.incidentList = incidentList;
    }

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout for each item_incident (Giving a look to the card views)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent,false);
        return new IncidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.IncidentViewHolder holder, int position) {
        // assigning values to the views we created in the item_incident layout file based on the position of recycler view
        Incident incident = incidentList.get(position);
        holder.textViewName.setText(incident.getName());
        holder.textViewDiagnosis.setText(incident.getDiagnosis());
        holder.textViewDoe.setText(incident.getDateOfExamination());

        //Set onClickListener for each item
        holder.itemView.setOnClickListener(v -> {
            // Create intent to open DetailActivity
            Intent intent = new Intent(context, DetailedIncidentActivity.class);

            // Pass incident details to the new activity
            intent.putExtra("incidentId", incident.getKey());
            intent.putExtra("name", incident.getName());
            intent.putExtra("dob", incident.getDateOfBirth());
            intent.putExtra("doe", incident.getDateOfExamination());
            intent.putExtra("gender", incident.getGender());
            intent.putExtra("symptoms", incident.getSymptoms());
            intent.putExtra("diagnosis", incident.getDiagnosis());
            intent.putExtra("prescription", incident.getPrescription());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        //to know the number of items (incidents) that must be displayed
        return incidentList.size();
    }

    public static class IncidentViewHolder extends RecyclerView.ViewHolder{
        //Declare your views
        TextView textViewName, textViewDiagnosis, textViewDoe;

        public IncidentViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize your views
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDiagnosis = itemView.findViewById(R.id.textViewDiagnosis);
            textViewDoe = itemView.findViewById(R.id.textViewDoe);

        }
    }
}
