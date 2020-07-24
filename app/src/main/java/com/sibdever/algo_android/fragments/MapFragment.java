package com.sibdever.algo_android.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sibdever.algo_android.MainViewModel;
import com.sibdever.algo_android.R;
import com.sibdever.algo_android.activities.CodeScanActivity;
import com.sibdever.algo_android.data.Point;
import com.sibdever.algo_android.data.ShortPoint;
import com.sibdever.algo_android.dialogs.WithoutRouteDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
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
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class MapFragment extends Fragment implements Session.RouteListener {

    private static final String TAG = "Borlehandro";

    private MapView mapView;

    private FloatingActionButton anchorButton, codeScanButton;
    private ExtendedFloatingActionButton showButton;

    private UserLocationLayer userLocationLayer;
    private PlacemarkMapObject mark;

    private LocationManager locationManager;
    private MapObjectCollection mapObjects;
    private PedestrianRouter router;

    private PolylineMapObject lastLine;

    private MainViewModel viewModel;

    private boolean focused, withoutRouters;

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationUpdated(@NonNull Location location) {

            //

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

            // LinkedList<ShortPoint> currentPointsQueue = viewModel.getPointsQueue().getValue();
            ShortPoint nextPoint = viewModel.getNextPoint().getValue();

            if (nextPoint != null) {

                // Manage user routing in quest

                Log.d(TAG, "onLocationUpdated: Try to build router with it `|` ");

                List<RequestPoint> points = new ArrayList<>();
                points.add(new RequestPoint(location.getPosition(), RequestPointType.WAYPOINT, null));

                points.add(new RequestPoint(new com.yandex.mapkit.geometry.Point(nextPoint.getLatitude(),
                        nextPoint.getLongitude()), RequestPointType.WAYPOINT, null));

                if (mark != null)
                    mapObjects.remove(mark);

                mark = mapObjects.addPlacemark(
                        new com.yandex.mapkit.geometry.Point(nextPoint.getLatitude(),
                                nextPoint.getLongitude()));

                mark.setOpacity(0.9f);
                mark.setIcon(ImageProvider.fromResource(getActivity(), R.drawable.placemark_mini));
                mark.setDraggable(true);

                if (withoutRouters) {
                    mapView.getMap().move(
                            new CameraPosition(
                                    new com.yandex.mapkit.geometry.Point(nextPoint.getLatitude(),
                                            nextPoint.getLongitude()),
                                    18.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 5),
                            null);
                }

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
                    getApplicationContext(), R.drawable.location_mark),
                    new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                            .setRotationType(RotationType.ROTATE)
                            .setZIndex(1f)
                            .setScale(0.2f));

            CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

            pinIcon.setIcon(
                    "pin",
                    ImageProvider.fromResource(getApplicationContext(),
                            R.drawable.location_mark),
                    new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                            .setRotationType(RotationType.ROTATE)
                            .setZIndex(1f)
                            .setScale(0.2f)
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

        // Todo encrypt config.properties or use another secure method

        try(InputStream input = getContext().getAssets().open("config.properties")) {

            Properties property = new Properties();
            property.load(input);

            String key = property.getProperty("api_key");

            Log.e(TAG, "Get key: " + key);
            MapKitFactory.setApiKey(key);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            getActivity().finish();
        }

        MapKitFactory.initialize(getActivity());

        View fragment = inflater.inflate(R.layout.fragment_map, container, false);

        Log.d(TAG, "onCreate: ");

        TransportFactory.initialize(getApplicationContext());

        MapKit mapKit = MapKitFactory.getInstance();

        mapView = fragment.findViewById(R.id.mapview);

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
        codeScanButton.setVisibility(View.INVISIBLE);

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

        Resources res = getResources();

        String language = res.getConfiguration().getLocales().get(0).getLanguage();
        Log.w(TAG, "onActivityCreated: " + language);

        viewModel.getShowOpened().observe(getViewLifecycleOwner(), (opened) -> {
            if (opened)
                Log.d(TAG, "onActivityCreated: OPENED IN MAP");
            else {
                showButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "onActivityCreated: NOT OPEN IN MAP");
            }
        });

        viewModel.getBottomSheetState().observe(getViewLifecycleOwner(), state -> {
            if (state == MainViewModel.BottomStates.POINTS_QUEUE_IN_PROCESS) {

                // Manage points queue

                showButton.setText(getActivity().getString(R.string.pointsListHeader));

                LinkedList<ShortPoint> currentPointsQueue = viewModel.getPointsQueue().getValue();

                List<RequestPoint> points = new ArrayList<>();

                points.add(new RequestPoint(
                        LocationManagerUtils.getLastKnownLocation().getPosition(),
                        RequestPointType.WAYPOINT, null));

                points.addAll(currentPointsQueue.stream()
                        .map(i -> new RequestPoint(new com.yandex.mapkit.geometry.Point(i.getLatitude(), i.getLongitude()),
                                RequestPointType.WAYPOINT, null))
                        .collect(Collectors.toList()));

                Log.d(TAG, "onActivityCreated: " + points.toString());

                withoutRouters = false;

                for (int i = 0; i < points.size() - 1; ++i) {

                    PlacemarkMapObject mark = mapObjects.addPlacemark(points.get(i + 1).getPoint());
                    mark.setOpacity(0.9f);
                    mark.setIcon(ImageProvider.fromResource(getActivity(), R.drawable.placemark_mini));
                    mark.setDraggable(true);

                    Log.d(TAG, "onActivityCreated: way " +
                            points.get(i).getPoint().getLatitude() + ";" + points.get(i).getPoint().getLongitude()
                            + " to " + points.get(i + 1).getPoint().getLatitude() + ";" + points.get(i + 1).getPoint().getLongitude());

                    router.requestRoutes(Arrays.asList(points.get(i), points.get(i + 1)),
                            new TimeOptions(), MapFragment.this);
                }
            }
        });

        viewModel.getQuestStarted().observe(getViewLifecycleOwner(), (started) -> {
            viewModel.setShowOpened(false);
            viewModel.setBottomSheetState(MainViewModel.BottomStates.POINTS_QUEUE_COMPLETED);
            viewModel.setQuestFinished(false);

            codeScanButton.setVisibility(View.VISIBLE);

            mapObjects.clear();

            Log.d(TAG, "onCreate: FIRST POINT IN "
                    + viewModel.getPointsQueue().getValue().get(0).getLatitude() + ";"
                    + viewModel.getPointsQueue().getValue().get(0).getLongitude());

            Log.d(TAG, "onCreate: GET POINTS QUEUE: " + viewModel.getNextPoint().getValue().getName());

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

        viewModel.getBottomSheetState().observe(getViewLifecycleOwner(), (state) -> {
            if (state == MainViewModel.BottomStates.QUEST_DESCRIPTION_STATE)
                showButton.setText(R.string.descriptionText);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMasstransitRoutes(List<Route> routes) {

        if (routes.size() > 0) {
            Log.w(TAG, "onMasstransitRoutes: SIZE IS > 0 ");
            for (Section section : routes.get(0).getSections()) {

                Log.w(TAG, "onMasstransitRoutes: TRY DRAW IT");

                Log.w(TAG, "onMasstransitRoutes: SECTION " + section.getGeometry().toString());

                PolylineMapObject polylineMapObject = mapObjects.addPolyline(SubpolylineHelper.subpolyline(
                        routes.get(0).getGeometry(), section.getGeometry()));

                Log.w(TAG, "onMasstransitRoutes: POLYLINE " + polylineMapObject);

                polylineMapObject.setStrokeColor(ContextCompat.getColor(getActivity(), R.color.colorWay));

                // Todo MAKE IT BETTER!
                if (!(viewModel.getBottomSheetState().getValue() == MainViewModel.BottomStates.POINTS_QUEUE_IN_PROCESS)) {
                    if (lastLine != null)
                        mapObjects.remove(lastLine);

                    lastLine = polylineMapObject;
                }
            }
        } else if (!withoutRouters) {
            withoutRouters = true;
            Log.w(TAG, "onMasstransitRoutes: SIZE IS 0");

            Log.w(TAG, "onActivityCreated: WITHOUT ROUTERS ");

            mapView.getMap().move(
                    new CameraPosition(new com.yandex.mapkit.geometry.Point(
                            viewModel.getPointsQueue().getValue().peek().getLatitude(),
                            viewModel.getPointsQueue().getValue().peek().getLongitude()),
                            20.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.LINEAR, 0),
                    null);

            WithoutRouteDialog dialog = new WithoutRouteDialog();
            dialog.show(getActivity().getSupportFragmentManager(), "withoutRoute");

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

        Log.e(TAG, errorMessage);
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

        if (viewModel.getBottomSheetState().getValue() == MainViewModel.BottomStates.POINTS_QUEUE_START
                || viewModel.getBottomSheetState().getValue() == MainViewModel.BottomStates.POINTS_QUEUE_IN_PROCESS
                || viewModel.getBottomSheetState().getValue() == MainViewModel.BottomStates.POINTS_QUEUE_COMPLETED)

            showButton.setText(getActivity().getString(R.string.pointsListHeader));

        else if (viewModel.getBottomSheetState().getValue() == MainViewModel.BottomStates.QUEST_DESCRIPTION_STATE)

            showButton.setText(getActivity().getString(R.string.descriptionText));

        else
            showButton.setText(R.string.questListHeader);

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