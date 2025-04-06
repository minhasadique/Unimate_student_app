package com.example.unimate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<SearchItem> searchResults = new ArrayList<>();
    private static final Map<String, Integer> ICON_MAP = new HashMap<>();

    static {
        ICON_MAP.put("student", R.drawable.profilelogo);
        ICON_MAP.put("course", R.drawable.searchlogoo);
        ICON_MAP.put("scholarship", R.drawable.startuplogo);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false); // Ensure correct XML file name
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchItem item = searchResults.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void setSearchResults(List<SearchItem> results) {
        if (results == null) return; // Avoid null reference issues
        this.searchResults.clear();
        this.searchResults.addAll(results);
        notifyItemRangeInserted(0, searchResults.size()); // Better than notifyDataSetChanged()
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView subtitleTextView;
        private final ImageView iconImageView;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            subtitleTextView = itemView.findViewById(R.id.subtitleTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }

        void bind(SearchItem item) {
            titleTextView.setText(item.getTitle());
            subtitleTextView.setText(item.getSubtitle());

            // Set icon dynamically
            int iconResource = ICON_MAP.getOrDefault(item.getType().toLowerCase(), R.drawable.profilelogo);
            iconImageView.setImageResource(iconResource);
        }
    }
}