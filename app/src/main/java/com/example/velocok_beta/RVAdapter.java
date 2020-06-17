package com.example.velocok_beta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocok_beta.database.DB_Path;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PathViewHolder> {
    public static class PathViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView date;
        TextView speed_1;
        TextView speed_2;
        TextView speed_3;
        TextView title;

        PathViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.Card1);
            date = itemView.findViewById(R.id.pathDate);
            speed_1 = itemView.findViewById(R.id.settore_1_val);
            speed_2 = itemView.findViewById(R.id.settore_2_val);
            speed_3 = itemView.findViewById(R.id.settore_3_val);
            title = itemView.findViewById(R.id.pathTitle);
        }
    }

    List<DB_Path> paths;

    RVAdapter(List<DB_Path> paths) {
        this.paths = paths;
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_card, parent, false);
        PathViewHolder cvh = new PathViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PathViewHolder holder, int position) {
        DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        String dateText = "default";
        try {
            date = inputFormat.parse(paths.get(position).getDate());
            dateText = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(dateText);
        holder.speed_1.setText(String.format(Locale.getDefault(), "%.2f", paths.get(position).getSpeeds().get(0)));
        holder.speed_2.setText(String.format(Locale.getDefault(), "%.2f", paths.get(position).getSpeeds().get(1)));
        holder.speed_3.setText(String.format(Locale.getDefault(), "%.2f", paths.get(position).getSpeeds().get(2)));
        holder.title.setText(paths.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setNewPaths(List<DB_Path> newPaths) {
        this.paths = newPaths;
        notifyDataSetChanged();
    }


}
