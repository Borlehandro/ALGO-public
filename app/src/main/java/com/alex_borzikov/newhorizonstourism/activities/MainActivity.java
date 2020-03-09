package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.alex_borzikov.newhorizonstourism.adapters.QuestListAdapter;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {

    private static final String TAG = "Borlehandro";

    private String userName;
    private String password;
    public String language;

    private int userId;
    String pointId;

    public static String pointCode;
    public static List<PointInfoItem> currentPointsQueue;

    private MapView mapView;
    private Button showButton, codeScanButton;
    QuestListFragment fragment;

    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Create");

        MapKitFactory.setApiKey("445832db-f7b3-4d5b-ba6a-f8a60f790ba0");
        MapKitFactory.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(new Point(52.5391, 85.2230), 14, 0, 0));

        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(this);

        fragment = new QuestListFragment();

        showButton = findViewById(R.id.showButton);
        codeScanButton = findViewById(R.id.scanCodeButton);

        // TODO Send password safety
        userId = getIntent().getIntExtra("userId", 0);
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        language = getIntent().getStringExtra("language");

        Log.d(TAG, "user " + userName);
        Log.d(TAG, "id " + userId);
        Log.d(TAG, "password " + password);
        Log.d(TAG, "lang " + language);

        if(currentPointsQueue!=null)
            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + currentPointsQueue.get(0).getName());

        showButton.setOnClickListener((View v) -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_layout, fragment);
            fragmentTransaction.commit();
        });

        codeScanButton.setOnClickListener((View v) -> {
            startActivity(new Intent(getApplicationContext(), CodeScanActivity.class));
        });
    }



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        mapView.getMap().move(
                new CameraPosition(new Point(52.539303, 85.223677), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");

        mapView.getMap().move(
                new CameraPosition(new Point(52.539303, 85.223677), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Restart");

        // Todo Make code using better
        if (pointCode != null) {
            Toast.makeText(getApplicationContext(), "Get code: " + pointCode, Toast.LENGTH_LONG).show();

            InfoTask codeTask = new InfoTask();

            Map<String, String> codeParams = new HashMap<>();
            codeParams.put("mode", "CHECK_CODE");
            codeParams.put("code", pointCode);

            codeTask.execute(codeParams);

            try {
                pointId = codeTask.get();
                Log.d(TAG, "onRestart: get pointId: " + pointId);
                // Todo check pointId == currentQuestPoint
                Intent toPoint = new Intent(getApplicationContext(), PointActivity.class);
                toPoint.putExtra("pointId", pointId);
                toPoint.putExtra("language", language);
                // It can not repeat!
                pointCode = null;
                startActivity(toPoint);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        Log.d(TAG, "On Stop Activity");
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    // Todo Use it for location listening
    private void updateLocation() {
        Log.d(TAG, "updateLocation");
        LocationManager manager = MapKitFactory.getInstance().createLocationManager();
        Log.d(TAG, manager.toString());
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                Log.i(TAG, "UPDATED");
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
                Log.i(TAG, "STATUS UPDATED");
            }
        };

        Log.d(TAG, listener.toString());
        manager.requestSingleUpdate(listener);
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        pinIcon.setIcon(
                "icon",
                ImageProvider.fromResource(this, R.drawable.icon),
                new IconStyle().setAnchor(new PointF(0f, 0f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(0f)
                        .setScale(1f)
        );

        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.search_result),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE);

    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

    public static class QuestListFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.activity_main, container, false);

            // Inflate the layout for this fragment
            ListView questList = layout.findViewById(R.id.quest_view);

            InfoTask getListTask = new InfoTask();
            Map<String, String> questListParams = new HashMap<>();
            questListParams.put("mode", "GET_QUESTS_LIST");
            questListParams.put("language", ((MainActivity) getActivity()).language);

            getListTask.execute(questListParams);

            try {
                // TODO: Don't call get() method!

                String result = getListTask.get();

                Log.d(TAG, "Activity get " + result);

                List<QuestListItem> parsingResult = JsonParser.parseQuestList(result);

                List<String> questsNames = parsingResult.stream().map(QuestListItem::getName)
                        .collect(Collectors.toList());

                List<String> questsDescriptions = parsingResult.stream()
                        .map(QuestListItem::getDescriptionShort).collect(Collectors.toList());

                List<Integer> questsId = parsingResult.stream().map(QuestListItem::getId)
                        .collect(Collectors.toList());

                for (String item : questsNames) {
                    Log.d(TAG, item);
                }

                //ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.quest_list_layout, questsNames);
                QuestListAdapter adapter = new QuestListAdapter(getContext(),
                        R.layout.quest_list_layout, questsNames, questsDescriptions);

                questList.setAdapter(adapter);

                questList.setOnItemClickListener((AdapterView<?> parent, View view,
                                                  int position, long id) -> {

                    Log.d(TAG, "Click on " + position);

                    Intent toQuestInfo = new Intent(getActivity().getApplicationContext(),
                            QuestActivity.class);

                    Log.d(TAG, "onCreateView: get ID:" + questsId.get(position));

                    toQuestInfo.putExtra("language", ((MainActivity) getActivity()).language);
                    toQuestInfo.putExtra("questId", String.valueOf(questsId.get(position)));

                    startActivityForResult(toQuestInfo, 1);

                });

            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }

            return layout;
        }
    }

    /**
     * Interesting code for animation
     */

    // slide the view from below itself to the current position
    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,  // fromXDelta
                0,  // toXDelta
                view.getHeight(),  // fromYDelta
                0);  // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,  // fromXDelta
                0,  // toXDelta
                0,  // fromYDelta
                view.getHeight());  // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


}