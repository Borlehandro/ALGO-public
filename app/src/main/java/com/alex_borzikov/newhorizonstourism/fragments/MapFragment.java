package com.alex_borzikov.newhorizonstourism.fragments;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alex_borzikov.newhorizonstourism.MainViewModel;
import com.alex_borzikov.newhorizonstourism.R;
import com.alex_borzikov.newhorizonstourism.activities.CodeScanActivity;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
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
import com.yandex.mapkit.location.LocationManagerUtils;
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
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class MapFragment extends Fragment implements Session.RouteListener {

    private static final String TAG = "Borlehandro";

    private MapView mapView;

    // Todo you need more action buttons
    private FloatingActionButton anchorButton, showButton, codeScanButton;

    private UserLocationLayer userLocationLayer;

    private LocationManager locationManager;
    private MapObjectCollection mapObjects;
    private PedestrianRouter router;

    private PolylineMapObject lastLine;

    private MainViewModel viewModel;

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

            LinkedList<PointInfoItem> currentPointsQueue = viewModel.getPointsQueue().getValue();

            if (currentPointsQueue != null && currentPointsQueue.size() > 0) {

                Log.d(TAG, "onLocationUpdated: Try to build drivingRouter with it `|` ");

                List<RequestPoint> points = new ArrayList<>();
                points.add(new RequestPoint(location.getPosition(), RequestPointType.WAYPOINT, null));

                points.add(new RequestPoint(new Point(currentPointsQueue.peek().getLocationX(),
                        currentPointsQueue.peek().getLocationY()), RequestPointType.WAYPOINT, null));

                router.requestRoutes(points, new TimeOptions(), MapFragment.this);

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
                    getApplicationContext(), R.drawable.user_arrow),
                    new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                            .setRotationType(RotationType.ROTATE)
                            .setZIndex(1f)
                            .setScale(0.5f));

            CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

            pinIcon.setIcon(
                    "pin",
                    ImageProvider.fromResource(getApplicationContext(), R.drawable.user_arrow),
                    new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                            .setRotationType(RotationType.ROTATE)
                            .setZIndex(1f)
                            .setScale(0.5f)
            );

            userLocationView.getAccuracyCircle().setFillColor(ContextCompat.getColor(getActivity(),
                    R.color.colorLocationArea));

        }

        @Override
        public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

        }

        @Override
        public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

        }
    };

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

        router = TransportFactory.getInstance().createPedestrianRouter();

        showButton = fragment.findViewById(R.id.showButton);
        codeScanButton = fragment.findViewById(R.id.scanCodeButton);
        anchorButton = fragment.findViewById(R.id.anchorActionButton);


        if (!focused)
            locationManager.requestSingleUpdate(locationListener);

        showButton.setOnClickListener((View v) -> {

            viewModel.setShowOpened(true);
            v.setVisibility(View.INVISIBLE);

        });

        codeScanButton.setOnClickListener((View v) -> {

            startActivity(new Intent(getApplicationContext(), CodeScanActivity.class));

        });

        anchorButton.setOnClickListener((View v) -> {

            Log.d(TAG, "onCreate: Anchor button enabled");

            mapView.getMap().move(
                    new CameraPosition(LocationManagerUtils.getLastKnownLocation().getPosition(),
                            18.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 3),
                    null);
        });

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        Log.d(TAG, "onActivityCreated: " + viewModel.getUserInfo().getValue().getLanguage());

        viewModel.getShowOpened().observe(getViewLifecycleOwner(), (opened) -> {
            if (opened)
                Log.d(TAG, "onActivityCreated: OPENED IN MAP");
            else {
                showButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "onActivityCreated: NOT OPEN IN MAP");
            }
        });

        viewModel.getNeedPointsQueue().observe(getViewLifecycleOwner(), need -> {
            if(need) {

                LinkedList<PointInfoItem> currentPointsQueue = viewModel.getPointsQueue().getValue();

                List<RequestPoint> points = new ArrayList<>();

                points.add(new RequestPoint(
                        LocationManagerUtils.getLastKnownLocation().getPosition(),
                        RequestPointType.WAYPOINT, null));

                points.addAll(currentPointsQueue.stream()
                        .map(i -> new RequestPoint(new Point(i.getLocationX(), i.getLocationY()),
                                RequestPointType.WAYPOINT, null))
                        .collect(Collectors.toList()));

//                points.add(new RequestPoint(new Point(currentPointsQueue.peek().getLocationX(),
//                        currentPointsQueue.peek().getLocationY()), RequestPointType.WAYPOINT,
//                        null));

                Log.d(TAG, "onActivityCreated: " + points.toString());

                for (int i=0; i<points.size()-1; ++i) {

                    Log.d(TAG, "onActivityCreated: way " +
                            points.get(i).getPoint().getLatitude() + ";" + points.get(i).getPoint().getLongitude()
                            + " to " + points.get(i+1).getPoint().getLatitude() + ";" + points.get(i+1).getPoint().getLongitude());

                    router.requestRoutes(Arrays.asList(points.get(i), points.get(i+1)),
                            new TimeOptions(), MapFragment.this);
                }
            }
        });

        viewModel.getQuestStarted().observe(getViewLifecycleOwner(), (started) -> {
            viewModel.setShowOpened(false);
            viewModel.setNeedPointsQueue(false);
            viewModel.setQuestFinished(false);

            mapObjects.clear();

            Log.d(TAG, "onCreate: FIRST POINT IN "
                    + viewModel.getPointsQueue().getValue().get(0).getLocationX() + ";"
                    + viewModel.getPointsQueue().getValue().get(0).getLocationY());

            LinkedList<PointInfoItem> currentPointsQueue = viewModel.getPointsQueue().getValue();

            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + currentPointsQueue.get(0).getName());

            // Todo set normal value
            locationManager.subscribeForLocationUpdates(0.0d, 100, 0.0d, false,
                    FilteringMode.OFF, locationListener);
        });

        viewModel.getQuestFinished().observe(getViewLifecycleOwner(), (finished) -> {
            if (finished) {
                locationManager.unsubscribe(locationListener);
                mapObjects.clear();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMasstransitRoutes(List<Route> routes) {

        if (routes.size() > 0) {
            for (Section section : routes.get(0).getSections()) {

                PolylineMapObject polylineMapObject = mapObjects.addPolyline(SubpolylineHelper.subpolyline(
                                routes.get(0).getGeometry(), section.getGeometry()));

                polylineMapObject.setStrokeColor(ContextCompat.getColor(getActivity(),
                        R.color.colorWay));

                // Todo MAKE IT BETTER!
                if(!viewModel.getNeedPointsQueue().getValue()) {
                    if (lastLine != null)
                        mapObjects.remove(lastLine);

                    lastLine = polylineMapObject;
                }
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

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        locationManager.resume();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onPause() {
        locationManager.suspend();
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}