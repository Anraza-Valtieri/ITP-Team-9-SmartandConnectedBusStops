
package com.sit.itp_team_9_smartandconnectedbusstops.BusRoutes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

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

    private Map<String, LinkedList<Value>> busRouteMap = new HashMap<>();

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
                List<Value> list = getValue();
                Iterator<Value> iterator = list.iterator();
                while(iterator.hasNext()){
                    String busNo = iterator.next().getServiceNo();
                    Value data = iterator.next();
//                    String data = iterator.next().getBusStopCode();
//                    Log.d(TAG, "createMap: adding "+busNo+ " with "+data.toString());
                    if(busRouteMap.get(busNo) != null){
                        LinkedList<Value> oldData = busRouteMap.get(busNo);
                        oldData.add(data);
                    }else{
                        LinkedList<Value> listData = new LinkedList<>();
                        listData.add(data);
                        busRouteMap.put(busNo, listData);
                    }

                }
                return null;
            }
        };
        asyncTask.execute();
    }

}
