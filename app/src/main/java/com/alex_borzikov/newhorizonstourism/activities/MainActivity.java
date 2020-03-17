package com.alex_borzikov.newhorizonstourism.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.alex_borzikov.newhorizonstourism.adapters.QuestListAdapter;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.adapters.TabAdapter;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;

import com.alex_borzikov.newhorizonstourism.fragments.BottomTabFragment;
import com.alex_borzikov.newhorizonstourism.fragments.QuestListTabFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.tabs.TabLayout;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
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
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.MasstransitOptions;
import com.yandex.mapkit.transport.masstransit.MasstransitRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.SectionMetadata;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.mapkit.transport.masstransit.Transport;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

public class MainActivity extends AppCompatActivity implements Session.RouteListener {

    private static final String TAG = "Borlehandro";

    private String userName;
    private String password;
    public String language;

    private int userId;
    String pointId;

    public static String pointCode;
    public static LinkedList<PointInfoItem> currentPointsQueue;

    private MapView mapView;

    // Todo you need more action buttons
    private Button showButton, codeScanButton;
    private FloatingActionButton anchorButton;

    BottomTabFragment fragment;

    private UserLocationLayer userLocationLayer;

    private LocationManager locationManager;
    private MapObjectCollection mapObjects;
    private MasstransitRouter mtRouter;

    private PolylineMapObject lastLine;

    private boolean focused;

    // Todo Stop after quest finish
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationUpdated(@NonNull Location location) {

            Log.d(TAG, "onLocationUpdated: " + location.getPosition().getLatitude() + ";"
                    + location.getPosition().getLongitude());

            if (!focused) {

                Log.d(TAG, "onLocationUpdated: Wanna focus");

                mapView.getMap().move(
                        new CameraPosition(location.getPosition(), 18.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 5),
                        null);

                focused = true;
            }

            if (currentPointsQueue != null && currentPointsQueue.size() > 0) {

                Log.d(TAG, "onLocationUpdated: Try to build drivingRouter with it `|` ");

                // Todo Make it pedestrians only!
                MasstransitOptions options = new MasstransitOptions(
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new TimeOptions());

                List<RequestPoint> points = new ArrayList<RequestPoint>();
                points.add(new RequestPoint(location.getPosition(), RequestPointType.WAYPOINT, null));

                points.add(new RequestPoint(new Point(currentPointsQueue.peek().getLocationX(),
                        currentPointsQueue.peek().getLocationY()), RequestPointType.WAYPOINT, null));

                mtRouter.requestRoutes(points, options, MainActivity.this);

            }
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
        TransportFactory.initialize(this);

        MapKit mapKit = MapKitFactory.getInstance();

        mapView = findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(false);

        mapView.setNoninteractive(false);

        locationManager = mapKit.createLocationManager();

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(userLocationObjectListener);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        mtRouter = TransportFactory.getInstance().createMasstransitRouter();

        // fragment = new QuestListFragment();

        showButton = findViewById(R.id.showButton);
        codeScanButton = findViewById(R.id.scanCodeButton);
        anchorButton = findViewById(R.id.anchorActionButton);

        // TODO Send password safety
        userId = getIntent().getIntExtra("userId", 0);
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        language = getIntent().getStringExtra("language");

        Log.d(TAG, "user " + userName);
        Log.d(TAG, "id " + userId);
        Log.d(TAG, "password " + password);
        Log.d(TAG, "lang " + language);

        if (!focused && currentPointsQueue == null)
            locationManager.requestSingleUpdate(locationListener);

        if (currentPointsQueue != null) {
            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + currentPointsQueue.get(0).getName());

            // Todo set normal value
            locationManager.subscribeForLocationUpdates(0.0d, 100, 0.0d, false,
                    FilteringMode.OFF, locationListener);
        }

        showButton.setOnClickListener((View v) -> {

//            BottomTabFragment fragment = BottomTabFragment.newInstance();
//            fragment.show(getSupportFragmentManager(), "bottom_sheet_fragment");

//            BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
//
//            View dialogView = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.buttom_sheet_layout, (LinearLayout)findViewById(R.id.bottomSheetContainer)
//            );
//
//            dialog.setContentView(dialogView);
//
//            TabAdapter tabAdapter = new TabAdapter(dialog.get);
//            TabLayout tabLayout = dialog.findViewById(R.id.tabLayout);
//            ViewPager viewPager = dialog.findViewById(R.id.viewPager);
//
//            Log.w(TAG, "onCreate: View pager " + viewPager);
//
//            tabAdapter.addFragment(new QuestListTabFragment(), "Tab 1");
//
//            viewPager.setAdapter(tabAdapter);
//            tabLayout.setupWithViewPager(viewPager);
//
//            Log.w(TAG, "onCreate: Set adapter and pager");
//
//            dialog.show();

            Log.w(TAG, "Shown!");

            // Check it is movable
//            if(mapView.getScreenshot()==null)
//                Log.w(TAG, "onCreate: IT'S NOT MOVABLE");
//            else Log.w(TAG, "onCreate: IT'S MOVABLE");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_layout, fragment);
            fragmentTransaction.commit();
        });

        codeScanButton.setOnClickListener((View v) ->
                startActivity(new Intent(getApplicationContext(), CodeScanActivity.class)));

        anchorButton.setOnClickListener((View v) -> {
            if (!userLocationLayer.isAnchorEnabled()) {

                Log.d(TAG, "onCreate: Anchor button enabled");

                userLocationLayer.setAnchor(
                        new PointF((float) (mapView.getWidth() * 0.5),
                                (float) (mapView.getHeight() * 0.5)),

                        new PointF((float) (mapView.getWidth() * 0.5),
                                (float) (mapView.getHeight() * 0.83)));

                anchorButton.setImageResource(android.R.drawable.ic_menu_compass);

            } else {
                userLocationLayer.resetAnchor();
                anchorButton.setImageResource(android.R.drawable.ic_menu_mylocation);
            }
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
        Log.d(TAG, "onResumeFragments: ");
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "On restart");
        super.onRestart();

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
    public void onMasstransitRoutes(List<Route> routes) {
        // In this example we consider first alternative only
        if (routes.size() > 0) {
            for (Section section : routes.get(0).getSections()) {
                drawSection(
                        section.getMetadata().getData(),
                        SubpolylineHelper.subpolyline(
                                routes.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }

    @Override
    public void onMasstransitRoutesError(Error error) {
        String errorMessage = "unknown_error_message";
        if (error instanceof RemoteError) {
            errorMessage = "remote_error_message";
        } else if (error instanceof NetworkError) {
            errorMessage = "network_error_message";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void drawSection(SectionMetadata.SectionData data,
                             Polyline geometry) {
        // Draw a section polyline on a map
        // Set its color depending on the information which the section contains
        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);

        // Masstransit route section defines exactly one on the following
        // 1. Wait until public transport unit arrives
        // 2. Walk
        // 3. Transfer to a nearby stop (typically transfer to a connected
        //    underground station)
        // 4. Ride on a public transport
        // Check the corresponding object for null to get to know which
        // kind of section it is
        if (data.getTransports() != null) {
            // A ride on a public transport section contains information about
            // all known public transport lines which can be used to travel from
            // the start of the section to the end of the section without transfers
            // along a similar geometry
            for (Transport transport : data.getTransports()) {
                // Some public transport lines may have a color associated with them
                // Typically this is the case of underground lines
                if (transport.getLine().getStyle() != null) {
                    polylineMapObject.setStrokeColor(
                            // The color is in RRGGBB 24-bit format
                            // Convert it to AARRGGBB 32-bit format, set alpha to 255 (opaque)
                            transport.getLine().getStyle().getColor() | 0xFF000000
                    );
                    // return;
                }
            }
            // Let us draw bus lines in green and tramway lines in red
            // Draw any other public transport lines in blue
            HashSet<String> knownVehicleTypes = new HashSet<>();
            knownVehicleTypes.add("bus");
            knownVehicleTypes.add("tramway");
            for (Transport transport : data.getTransports()) {
                String sectionVehicleType = getVehicleType(transport, knownVehicleTypes);
                if (sectionVehicleType.equals("bus")) {
                    polylineMapObject.setStrokeColor(0xFF00FF00);  // Green
                    // return;
                } else if (sectionVehicleType.equals("tramway")) {
                    polylineMapObject.setStrokeColor(0xFFFF0000);  // Red
                    // return;
                }
            }
            polylineMapObject.setStrokeColor(0xFF0000FF);  // Blue
        } else {
            // This is not a public transport ride section
            // In this example let us draw it in black
            polylineMapObject.setStrokeColor(0xFF000000);  // Black
        }

        // Todo MAKE IT BETTER!
        if (lastLine != null)
            mapObjects.remove(lastLine);

        lastLine = polylineMapObject;
    }

    private String getVehicleType(Transport transport, HashSet<String> knownVehicleTypes) {
        // A public transport line may have a few 'vehicle types' associated with it
        // These vehicle types are sorted from more specific (say, 'histroic_tram')
        // to more common (say, 'tramway').
        // Your application does not know the list of all vehicle types that occur in the data
        // (because this list is expanding over time), therefore to get the vehicle type of
        // a public line you should iterate from the more specific ones to more common ones
        // until you get a vehicle type which you can process
        // Some examples of vehicle types:
        // "bus", "minibus", "trolleybus", "tramway", "underground", "railway"
        for (String type : transport.getLine().getVehicleTypes()) {
            if (knownVehicleTypes.contains(type)) {
                return type;
            }
        }
        return null;
    }

//    public static class QuestListFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            Log.d(TAG, "onCreateView: ");
//
//            View layout = inflater.inflate(R.layout.activity_main, container, false);
//
//            // Inflate the layout for this fragment
//
//            return layout;
//        }
//    }

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