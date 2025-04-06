package com.example.unimate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StartupAdapter extends RecyclerView.Adapter<StartupAdapter.ViewHolder> {
    private List<Startup> startups;

    public StartupAdapter(List<Startup> startups) {
        this.startups = startups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.startup_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Startup startup = startups.get(position);

        holder.startupTitle.setText(startup.getTitle()); // ✅ Corrected field name
        holder.startupDescription.setText(startup.getDescription());
        holder.startupAuthor.setText(startup.getAuthor());
        holder.startupRoles.setText(startup.getRoles());
        holder.startupContact.setText(startup.getContact());
    }

    @Override
    public int getItemCount() {
        return startups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startupTitle, startupDescription, startupAuthor, startupRoles, startupContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startupTitle = itemView.findViewById(R.id.startupTitle);
            startupDescription = itemView.findViewById(R.id.startupDescription);
            startupAuthor = itemView.findViewById(R.id.startupAuthor);
            startupRoles = itemView.findViewById(R.id.startupRoles);
            startupContact = itemView.findViewById(R.id.startupContact);
        }
    }
}
