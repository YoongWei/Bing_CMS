package com.ntu.cz3003.CMS.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntu.cz3003.CMS.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IncidentsAdapter extends RecyclerView.Adapter<IncidentsAdapter.MyViewHolder> {
    private List<Incident> incidentsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, createdAt, description;
        public ImageView incidentsImage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            createdAt = (TextView) view.findViewById(R.id.createdAt);
            description = (TextView) view.findViewById(R.id.description);
            incidentsImage = (ImageView) view.findViewById(R.id.incident_image);
        }
    }


    public IncidentsAdapter(List<Incident> incidentsList) {
        this.incidentsList = incidentsList;
    }

    @Override
    public IncidentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incidents_list, parent, false);

        return new IncidentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IncidentsAdapter.MyViewHolder holder, int position) {
        Incident incidents = incidentsList.get(position);
        holder.title.setText(incidents.getTitle());
        holder.createdAt.setText(incidents.getCreatedAt().toString());
        holder.description.setText(incidents.getDescription());
        //holder.geopoints.setText(address);
        Picasso.get().load(incidents.getImageUri()).into(holder.incidentsImage);
    }

    @Override
    public int getItemCount() {
        return incidentsList.size();
    }
}
