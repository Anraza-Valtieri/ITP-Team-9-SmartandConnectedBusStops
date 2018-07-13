
package com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONLTABusRoute {
    private static final String TAG = JSONLTABusRoute.class.getSimpleName();
    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    private static Map<String, LinkedList<Value>> busRouteMap = new HashMap<>();

    private String getOdataMetadata() {
        return odataMetadata;
    }

    private void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    private List<Value> getValue() {
        return value;
    }

    private void setValue(List<Value> value) {
        this.value = value;
    }

    public Map<String, LinkedList<Value>> getBusRouteMap() {
        return busRouteMap;
    }

    @Override
    public String toString() {
        return "JSONLTABusRoute{" +
                "value=" + value +
                ", busRouteMap=" + busRouteMap +
                '}';
    }

    public void createMap(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                for(Value entry : getValue()){
                    Log.d(TAG, "doInBackground: Got entry "+entry.toString() );
                    String busNo = entry.getServiceNo();
                    Value data = entry;
                    if(busRouteMap.get(busNo) != null){ // We have data of this bus
                        LinkedList<Value> oldData = busRouteMap.get(busNo);
                        oldData.add(data);
                        busRouteMap.put(busNo, oldData);
//                        Log.d(TAG, "doInBackground: createMap != null"+data.toString() );
                    }else{
                        LinkedList<Value> listData = new LinkedList<>();
                        listData.add(data);
                        busRouteMap.put(busNo, listData);
//                        Log.d(TAG, "doInBackground: createMap == null"+data.toString() );
                    }
                }
//                Log.d(TAG, "doInBackground: createMap "+busRouteMap.toString());
                return null;
            }
        };
        asyncTask.execute();
    }

}
