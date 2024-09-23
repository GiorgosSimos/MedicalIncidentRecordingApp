package com.unipi.mobile_dev.hippocratesjournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {

    private List<Incident> incidentList;

    public IncidentAdapter(List<Incident> incidentList) {
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
