package com.sit.itp_team_9_smartandconnectedbusstops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sit.itp_team_9_smartandconnectedbusstops.Adapters.DataSourceAdapter;
import com.sit.itp_team_9_smartandconnectedbusstops.Model.DataSource;

import java.util.ArrayList;
import java.util.List;

public class DataSourcesActivity extends AppCompatActivity {

    private Button mButtonBack;
    RecyclerView recyclerView;
    DataSourceAdapter dataSourceAdapter;

    List<DataSource> dataSourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sources);

        mButtonBack = findViewById(R.id.btnBack);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dataSourceList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewDataSource);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataSourceList.add(new DataSource(R.drawable.logo_googlemaps, "Google Maps", "Provide display of maps, location critical information and navigation routes"));
        dataSourceList.add(new DataSource(R.drawable.logo_twitter, "Twitter", "Provide updates on train services"));
        dataSourceList.add(new DataSource(R.drawable.logo_datagov, "Data Gov", "Provide weather forecast, PSI Level and UV Index information"));
        dataSourceList.add(new DataSource(R.drawable.logo_datamall, "LTA DataMall", "Provide timely bus timing information for different bus services in Singapore"));

        dataSourceAdapter = new DataSourceAdapter(this, dataSourceList);
        recyclerView.setAdapter(dataSourceAdapter);
    }
}
