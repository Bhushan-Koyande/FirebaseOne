package com.example.firebaseone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.util.ListenableList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mapButton;
    private Spinner spinner;

    private Location location;

    private ArrayList<ListItem> listItems;

    private GraphicsOverlay graphicsOverlay;
    private LocatorTask locator=new LocatorTask("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");
    private String TAG="";
    private String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Toast.makeText(getActivity(),"Selected an item from list twice to load results",Toast.LENGTH_LONG).show();
        graphicsOverlay=new GraphicsOverlay();
        mapButton=v.findViewById(R.id.fab);
        spinner=v.findViewById(R.id.spinner);
        mRecyclerView=v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        listItems=new ArrayList<>();
        //getAPIData();

        setupSpinner();

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new ListItemAdapter(listItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void findPlaces(String placeCategory,int imageResource){
        GeocodeParameters parameters=new GeocodeParameters();
        parameters.setMaxResults(5);
        parameters.setPreferredSearchLocation(new Point(location.getLongitude(),location.getLatitude()));

        List<String> outputAttributes=parameters.getResultAttributeNames();
        outputAttributes.add("*");

        final ListenableFuture<List<GeocodeResult>> results=locator.geocodeAsync(placeCategory,parameters);
        results.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ListenableList<Graphic> graphics = graphicsOverlay.getGraphics();
                    graphics.clear();
                    List<GeocodeResult> places = results.get();
                    for (GeocodeResult result : places) {

                        // Add a graphic representing each location with a simple marker symbol.
                        /*
                        SimpleMarkerSymbol placeSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN, 10);
                        placeSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2));
                        Graphic graphic = new Graphic(result.getDisplayLocation(), placeSymbol);
                        java.util.Map<String, Object> attributes = result.getAttributes();

                        // Store the location attributes with the graphic for later recall when this location is identified.
                        for (String key : attributes.keySet()) {
                            String value = attributes.get(key).toString();
                            graphic.getAttributes().put(key, value);
                        }
                        graphics.add(graphic);

                         */
                        Log.d(TAG,"new place starts here");
                        Log.d(TAG,result.getLabel());
                        java.util.Map<String, Object> attributes = result.getAttributes();
                        String phoneNumber;
                        String addressLine="Address : Not Available";
                        addressLine="Address : "+attributes.get("Place_addr").toString();
                        if(attributes.get("Phone")!=null){
                            phoneNumber=attributes.get("Phone").toString();
                        }else {
                            phoneNumber="no contact available";
                        }
                        listItems.add(new ListItem(imageResource,result.getLabel(),phoneNumber,addressLine));
                        Log.d(TAG,attributes.toString());
                    }
                } catch (InterruptedException | ExecutionException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void setupSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listItems.clear();
                if(adapterView.getItemAtPosition(i).toString().equals("Food")){
                    findPlaces(adapterView.getItemAtPosition(i).toString(),R.drawable.restaurant_icon);
                    mAdapter.notifyDataSetChanged();
                }
                if(adapterView.getItemAtPosition(i).toString().equals("Tourist Attraction")){
                    findPlaces(adapterView.getItemAtPosition(i).toString(),R.drawable.touristplace_icon);
                    mAdapter.notifyDataSetChanged();
                }
                if(adapterView.getItemAtPosition(i).toString().equals("ATM")){
                    findPlaces(adapterView.getItemAtPosition(i).toString(),R.drawable.atm_icon);
                    mAdapter.notifyDataSetChanged();
                }
                if(adapterView.getItemAtPosition(i).toString().equals("Hospital")){
                    findPlaces(adapterView.getItemAtPosition(i).toString(),R.drawable.doctor_icon);
                    mAdapter.notifyDataSetChanged();
                }
                if(adapterView.getItemAtPosition(i).toString().equals("Police Station")){
                    findPlaces(adapterView.getItemAtPosition(i).toString(),R.drawable.police_icon);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                findPlaces(adapterView.getItemAtPosition(0).toString(),R.drawable.restaurant_icon);
            }

        });

    }

    private void getAPIData(){
        String latString=Double.toString(location.getLatitude());
        String longString=Double.toString(location.getLongitude());
        String key="5ae2e3f221c38a28845f05b6f7b045d299ef277aac775b92c90d98e0";
        OkHttpClient client=new OkHttpClient();
        HttpUrl.Builder urlBuilder=HttpUrl.parse("http://api.opentripmap.com/0.1/en/places/radius").newBuilder();
        //urlBuilder.addQueryParameter("lang","en");
        urlBuilder.addQueryParameter("radius","3000");
        urlBuilder.addQueryParameter("lon",longString);
        urlBuilder.addQueryParameter("lat",latString);
        urlBuilder.addQueryParameter("kinds","foods,railway_stations");
        urlBuilder.addQueryParameter("format","json");
        urlBuilder.addQueryParameter("limit","25");
        urlBuilder.addQueryParameter("apikey",key);

        url=urlBuilder.build().toString();

        Request request=new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.e(TAG,e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse=response.body().string();
                Log.d(TAG,myResponse);
                try {
                    JSONArray jsonArray=new JSONArray(myResponse);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //listItems.add(new ListItem(R.drawable.restaurant_icon,jsonObject.get("name").toString(),jsonObject.get("kinds").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void getLocation(Location location){
        this.location=location;
    }

}
