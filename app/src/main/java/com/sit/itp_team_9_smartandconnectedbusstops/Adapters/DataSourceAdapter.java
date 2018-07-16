package com.sit.itp_team_9_smartandconnectedbusstops.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sit.itp_team_9_smartandconnectedbusstops.Model.DataSource;
import com.sit.itp_team_9_smartandconnectedbusstops.R;

import java.util.List;

public class DataSourceAdapter extends RecyclerView.Adapter<DataSourceAdapter.DataSourceViewHolder>{

    private Context mContext;
    private List<DataSource> dataSourceList;

    public DataSourceAdapter(Context mContext, List<DataSource> dataSourceList) {
        this.mContext = mContext;
        this.dataSourceList = dataSourceList;
    }

    @NonNull
    @Override
    public DataSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.listing_data_sources, null);
        DataSourceViewHolder holder = new DataSourceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataSourceViewHolder holder, int position) {
        DataSource dataSource = dataSourceList.get(position);

        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(dataSource.getImageUrl()));
        holder.textViewTitle.setText(dataSource.getTitle());
        holder.textViewContent.setText(dataSource.getContent());
    }

    @Override
    public int getItemCount() {
        return dataSourceList.size();
    }

    class DataSourceViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewContent;

        public DataSourceViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewSourcePic);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }
}
