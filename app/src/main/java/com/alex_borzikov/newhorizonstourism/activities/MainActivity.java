package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
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
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

public class MainActivity extends AppCompatActivity
        implements DrivingSession.DrivingRouteListener {

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

    private LocationManager locationManager;


    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationUpdated(@NonNull Location location) {
            Log.d(TAG, "onLocationUpdated: " + location.getPosition().getLatitude() + ";"
                    + location.getPosition().getLongitude());
        }

        @Override
        public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
            Log.d(TAG, "onLocationStatusUpdated: ");

        }
    };

    private final UserLocationObjectListener userLocationObjectListener
            = new UserLocationObjectListener() {
        @Override
        public void onObjectAdded(@NonNull UserLocationView userLocationView) {
            userLocationLayer.setAnchor(
                    new PointF((float) (mapView.getWidth() * 0.5),
                            (float) (mapView.getHeight() * 0.5)),

                    new PointF((float) (mapView.getWidth() * 0.5),
                            (float) (mapView.getHeight() * 0.83)));

            userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                    getApplicationContext(), R.drawable.user_arrow));

            CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

            pinIcon.setIcon(
                    "icon",
                    ImageProvider.fromResource(getApplicationContext(), R.drawable.search_result),
                    new IconStyle().setAnchor(new PointF(0f, 0f))
                            .setRotationType(RotationType.ROTATE)
                            .setZIndex(0f)
                            .setScale(1f)
            );

            // 2020-03-14 17:52:57.385

            pinIcon.setIcon(
                    "pin",
                    ImageProvider.fromResource(getApplicationContext(), R.drawable.mark),
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey("445832db-f7b3-4d5b-ba6a-f8a60f790ba0");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);

        MapKit mapKit = MapKitFactory.getInstance();


        locationManager = mapKit.createLocationManager();

        locationManager.subscribeForLocationUpdates(0.0d, 0, 0.0d, false,
                FilteringMode.OFF, locationListener);

        mapView = findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(false);

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(userLocationObjectListener);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

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

        if (currentPointsQueue != null)
            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + currentPointsQueue.get(0).getName());

        showButton.setOnClickListener((View v) -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_layout, fragment);
            fragmentTransaction.commit();
        });

        codeScanButton.setOnClickListener((View v) -> {
            startActivity(new Intent(getApplicationContext(), CodeScanActivity.class));
        });

        // Check permissions
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "onCreate: COARSE DENIED!");

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Toast.makeText(getApplicationContext(), "You should allow COARSE",
                        Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_RESULT);
        }*/
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        locationManager.resume();
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
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "On restart");

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
        Log.d(TAG, "onStart: ");
        super.onStart();
        mapView.onStart();
        // locationManager.resume();
        MapKitFactory.getInstance().onStart();
    }


    @Override
    protected void onPause() {
        locationManager.suspend();
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


    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        for (DrivingRoute route : list) {
            mapObjects.addPolyline(route.getGeometry());
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {

        String errorMessage = "Error message";
        if (error instanceof RemoteError) {
            errorMessage = "Error message";
        } else if (error instanceof NetworkError) {
            errorMessage = "Error message";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

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

    /*private void submitRequest(Point start, Point end) {

        Log.d(TAG, "submitRequest: start: " + start.getLatitude() + ";" + start.getLongitude());

        DrivingOptions options = new DrivingOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(
                start,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(
                end,
                RequestPointType.WAYPOINT,
                null));
        drivingSession = drivingRouter.requestRoutes(requestPoints, options, this);
    }*/

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