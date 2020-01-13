package com.alex_borzikov.newhorizonstourism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Borlehandro";

    private MapView mapView;
    private ListView questList;

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("445832db-f7b3-4d5b-ba6a-f8a60f790ba0");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);
        questList = findViewById(R.id.quest_view);
        mapView = findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        ServerTask task = new ServerTask();
        task.execute();

        ArrayList<String> questsNames = new ArrayList<>();

        try {
            // TODO: Don't call get() method!
            JSONArray array = new JSONArray(task.get());
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                questsNames.add(object.getString("name"));
            }

            for(String item : questsNames) {
                Log.d(TAG, item);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, questsNames);
            questList.setAdapter(adapter);

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    // Todo Use it for location listening
    private void updateLocation(){
        Log.d(TAG,"updateLocation");
        LocationManager manager = MapKitFactory.getInstance().createLocationManager();
        Log.d(TAG,manager.toString());
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                Log.i(TAG,"UPDATED");
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
                Log.i(TAG,"STATUS UPDATED");
            }
        };

        Log.d(TAG,listener.toString());
        manager.requestSingleUpdate(listener);
    }
}