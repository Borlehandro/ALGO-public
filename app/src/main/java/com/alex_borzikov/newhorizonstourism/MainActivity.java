package com.alex_borzikov.newhorizonstourism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.alex_borzikov.newhorizonstourism.API.ConnectionModes;
import com.alex_borzikov.newhorizonstourism.API.ServerTask;
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
    private Button showButton;
    QuestListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Create");

        MapKitFactory.setApiKey("445832db-f7b3-4d5b-ba6a-f8a60f790ba0");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);

        fragment = new QuestListFragment();

        mapView = findViewById(R.id.mapview);
        showButton = findViewById(R.id.show_button);

        showButton.setOnClickListener((View v)->{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_layout, fragment);
            fragmentTransaction.commit();
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");

        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "Restart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "On Stop Activity");
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

    public static class QuestListFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.activity_main, container, false);
            // Inflate the layout for this fragment
            ListView questList = layout.findViewById(R.id.quest_view);

            ServerTask task = new ServerTask();
            Map<String, String> params = new HashMap<>();
            params.put("mode", "GET_QUESTS_LIST");
            task.execute(params);

            ArrayList<String> questsNames = new ArrayList<>();
            ArrayList<String> questsDescriptions = new ArrayList<>();

            try {
                // TODO: Don't call get() method!
                JSONArray array = new JSONArray(task.get());
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject object = array.getJSONObject(i);
                    questsNames.add(object.getString("name"));
                    questsDescriptions.add(object.getString("short_description"));
                }

                for(String item : questsNames) {
                    Log.d("Borlehandro", item);
                }

                //ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.quest_list_layout, questsNames);
                QuestListAdapter adapter = new QuestListAdapter(getContext(),
                        R.layout.quest_list_layout, questsNames, questsDescriptions);

                questList.setAdapter(adapter);

            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }

            return layout;
        }
    }
}