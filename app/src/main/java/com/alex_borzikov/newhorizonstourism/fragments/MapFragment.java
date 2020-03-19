package com.alex_borzikov.newhorizonstourism.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.activities.CodeScanActivity;
import com.alex_borzikov.newhorizonstourism.activities.MainActivity;
import com.alex_borzikov.newhorizonstourism.activities.PointActivity;
import com.alex_borzikov.newhorizonstourism.api.InfoTask;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class MapFragment extends Fragment implements Session.RouteListener {

    private static final String ARG_LANG = "language";

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

    MainViewModel viewModel;

    private boolean focused;

    BottomSheetBehavior sheetBehavior;

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

                mtRouter.requestRoutes(points, options, MapFragment.this);

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

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LANG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            language = getArguments().getString(ARG_LANG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MapKitFactory.setApiKey("445832db-f7b3-4d5b-ba6a-f8a60f790ba0");

        MapKitFactory.initialize(getActivity());

        View fragment = inflater.inflate(R.layout.fragment_map, container, false);

        Log.d(TAG, "onCreate: ");

        TransportFactory.initialize(getApplicationContext());

        MapKit mapKit = MapKitFactory.getInstance();

        mapView = fragment.findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(false);

        mapView.setNoninteractive(false);

        locationManager = mapKit.createLocationManager();

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(userLocationObjectListener);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        mtRouter = TransportFactory.getInstance().createMasstransitRouter();

        showButton = fragment.findViewById(R.id.showButton);
        codeScanButton = fragment.findViewById(R.id.scanCodeButton);
        anchorButton = fragment.findViewById(R.id.anchorActionButton);

        if (!focused && currentPointsQueue == null)
            locationManager.requestSingleUpdate(locationListener);

        if (currentPointsQueue != null) {
            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + currentPointsQueue.get(0).getName());

            // Todo set normal value
            locationManager.subscribeForLocationUpdates(0.0d, 100, 0.0d, false,
                    FilteringMode.OFF, locationListener);
        }

        showButton.setOnClickListener((View v) -> {

            // Todo Check view in the model and set bottom sheet param

            viewModel.setShowOpened(true);
            v.setVisibility(View.INVISIBLE);

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

//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.add(R.id.main_layout, fragment);
//            fragmentTransaction.commit();
        });

        codeScanButton.setOnClickListener((View v) ->
                startActivity(new Intent(getApplicationContext(), CodeScanActivity.class)));

        anchorButton.setOnClickListener((View v) -> {

            //Todo  Just focus in user! Not set anchor!

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

        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ok ?
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        Log.d(TAG, "onActivityCreated: " + viewModel.getUserInfo().getValue().getLanguage());

        viewModel.getShowOpened().observe(getViewLifecycleOwner(), (opened) -> {
            if (opened)
                Log.d(TAG, "onActivityCreated: OPENED IN MAP");
            else
                Log.d(TAG, "onActivityCreated: NOT OPEN IN MAP");
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        locationManager.resume();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onPause() {
        locationManager.suspend();
        super.onPause();
        Log.d(TAG, "Pause");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "On Stop Activity");
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}