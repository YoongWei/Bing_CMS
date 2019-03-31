package com.ntu.cz3003.CMS.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ntu.cz3003.CMS.R;
import com.ntu.cz3003.CMS.models.Incident;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import static com.ntu.cz3003.CMS.Constants.CMS_PREFIX;
import static com.ntu.cz3003.CMS.Constants.CMS_PREFIX_NUMBER_FORMAT;
import static com.ntu.cz3003.CMS.Constants.CMS_STATUS_OPEN;
import static com.ntu.cz3003.CMS.Constants.CMS_STATUS_PENDING;
import static com.ntu.cz3003.CMS.Constants.CMS_STATUS_RESERVED;
import static com.ntu.cz3003.CMS.Constants.REQUEST_CODE_IMAGE_OPEN;

/**
 MapActivity class displays map , waste location and handling submit request, make
 reservation activity.
 @author ILoveNTU
 @version 2.1
 @since 2019-01-15
 */

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MapsActivity";
    private static final int DEFAULT_ZOOM = 11;
    private static final int MY_LOCATION_ZOOM = 17;
    private static final LatLng DEFAULT_SINGAPORE_LOCATION = new LatLng(1.369067, 103.810828);

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private final DocumentReference incidentCounterDocument = db.collection("counters").document("incident_counter");
    private int incidentCounter;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseUser firebaseUser;
    private GoogleMap mMap;
    private Location mLastLocation;

    private FloatingActionButton myLocationButton;
    private FloatingActionButton toggleSubmitBottomSheetButton;
    private BottomSheetBehavior submitFormBottomSheetBehavior;
    private BottomSheetBehavior incidentDetailBottomSheetBehavior;
    private LinearLayout submitFormBottomSheet;
    private LinearLayout incidentDetailBottomSheet;

    //Submit Form BottomSheet
    private Button submitRequestButton;
    private Spinner typeCategory;
    private EditText titleInput;
    private Uri selectedImage;
    private ImageView uploadImageButton;
    private ImageView showImage;
    private ProgressBar submitProgressBar;
    private TextView locationInput;
    private EditText locationDescriptionInput;
    private EditText descriptionInput;

    private TextView titleTextView;
    private TextView incidentTypeTextView;
    private TextView addressTextView;
    private TextView locationDescriptionTextView;
    private TextView incidentDescriptionTextView;
    private TextView reportDateTextView;

    //Incident Detail BottomSheet
    private TextView titleTextView;
    private TextView remarksTextView;
    private TextView statusTextView;
    private TextView requesterNameTextView;
    private ImageView incidentImageView;
    private TextView addressTextView;
    private TextView submitDateView;
    private TextView addressDescriptionTextView;

    //Navigation Drawer
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userProfileImageView;

    private HashMap<String, Marker> mapMarkerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mapMarkerManager = new HashMap<String, Marker>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initUI();
    }

    private void initUI() {
        findViews();
        initButtonListener();
        loadCategoryIntoSpinner();
        loadUserIntoNavigation();
    }

    private void findViews() {
        myLocationButton = findViewById(R.id.myLocationButton);
        toggleSubmitBottomSheetButton = findViewById(R.id.toggleBottomSheetButton);
        submitFormBottomSheet = findViewById(R.id.submitFormBottomSheet);
        incidentDetailBottomSheet = findViewById(R.id.incidentDetailBottomSheet);
        submitFormBottomSheetBehavior = BottomSheetBehavior.from(submitFormBottomSheet);
        incidentDetailBottomSheetBehavior = BottomSheetBehavior.from(incidentDetailBottomSheet);

        submitRequestButton = submitFormBottomSheet.findViewById(R.id.submitRequestButton);
        typeCategory = submitFormBottomSheet.findViewById(R.id.typeCategory);
        titleInput = submitFormBottomSheet.findViewById(R.id.titleInput);
        uploadImageButton = submitFormBottomSheet.findViewById(R.id.uploadImageButton);
        showImage = submitFormBottomSheet.findViewById(R.id.showImage);
        submitProgressBar = submitFormBottomSheet.findViewById(R.id.submitRequestProgressBar);
        descriptionInput = submitFormBottomSheet.findViewById(R.id.descriptionInput);
        locationDescriptionInput = submitFormBottomSheet.findViewById(R.id.locationDescriptionInput);
        locationInput = submitFormBottomSheet.findViewById(R.id.locationInput);

        titleTextView = incidentDetailBottomSheet.findViewById(R.id.titleTextView);
        incidentTypeTextView = incidentDetailBottomSheet.findViewById(R.id.incidentTypeTextView);
        addressTextView = incidentDetailBottomSheet.findViewById(R.id.addressTextView);
        locationDescriptionTextView = incidentDetailBottomSheet.findViewById(R.id.locationDescriptionTextView);
        incidentDescriptionTextView = incidentDetailBottomSheet.findViewById(R.id.incidentDescriptionTextView);
        reportDateTextView = incidentDetailBottomSheet.findViewById(R.id.reportDateTextView);

        remarksTextView = incidentDetailBottomSheet.findViewById(R.id.remarksTextView);
        requesterNameTextView = incidentDetailBottomSheet.findViewById(R.id.requesterNameTextView);
        incidentImageView = incidentDetailBottomSheet.findViewById(R.id.incidentImageView);
        addressTextView = incidentDetailBottomSheet.findViewById(R.id.addressTextView);
        submitDateView = incidentDetailBottomSheet.findViewById(R.id.submitDateView);
        addressDescriptionTextView = incidentDetailBottomSheet.findViewById(R.id.addressDescriptionTextView);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        View hView = navigationView.getHeaderView(0);
        userNameTextView = hView.findViewById(R.id.userNameTextView);
        userEmailTextView = hView.findViewById(R.id.userEmailTextView);
        userProfileImageView = hView.findViewById(R.id.userProfileImageView);
    }

    private void loadUserIntoNavigation() {
        userNameTextView.setText(firebaseUser.getDisplayName());
        userEmailTextView.setText(firebaseUser.getEmail());
        Picasso.get().load(firebaseUser.getPhotoUrl()).into(userProfileImageView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap = googleMap;
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.night_map));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setOnMarkerClickListener(this);

        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastLocation = task.getResult();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_SINGAPORE_LOCATION, DEFAULT_ZOOM);
                    mMap.moveCamera(cameraUpdate);
                }
            }
        });

        subscribeIncident();
    }

    private void initButtonListener() {
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCameraToCurrentLocation();
            }
        });

        toggleSubmitBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitFormBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    submitFormBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            mLastLocation = task.getResult();
                            GeoPoint geoPoint = new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            locationInput.setText(getAddressName(geoPoint));
                        }
                    });

                } else {
                    submitFormBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        submitRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProgressBar.bringToFront();
                submitProgressBar.setVisibility(View.VISIBLE);
                submitIncident();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_IMAGE_OPEN);
            }
        });

    }

    private void subscribeIncident() {
        final CollectionReference incidentsCollection = db.collection("incidents");

        incidentsCollection
                .whereEqualTo("status", CMS_STATUS_OPEN).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Incident Incident = document.toObject(Incident.class);
                        Incident.setId(document.getId());
                            LatLng latlng = new LatLng(Incident.getLocation().getLatitude(), Incident.getLocation().getLongitude());
                            Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(Incident.getType()));
                            marker.setTag(Incident);
                            mapMarkerManager.put(Incident.getId(), marker);

                    }
                }
            }
        });

        incidentCounterDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                incidentCounter = Integer.parseInt(documentSnapshot.get("lastInsertedId").toString());
            }
        });

        incidentsCollection
                .whereEqualTo("status", CMS_STATUS_OPEN)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            Incident Incident = dc.getDocument().toObject(Incident.class);
                            Incident.setId(dc.getDocument().getId());
                            LatLng latlng = new LatLng(Incident.getLocation().getLatitude(), Incident.getLocation().getLongitude());

                            Marker existingMarker = mapMarkerManager.get(Incident.getId());

                            switch (dc.getType()) {
                                case ADDED:
                                    if (existingMarker == null) {
                                        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(Incident.getType()));
                                        marker.setTag(Incident);
                                        mapMarkerManager.put(Incident.getId(), marker);
                                    }

                                    break;
                                case REMOVED:
                                    if (existingMarker != null) {
                                        existingMarker.remove();
                                        mapMarkerManager.remove(Incident.getId());
                                    }
                                    break;
                                case MODIFIED:
                                    if (Incident.getStatus().equals(CMS_STATUS_OPEN) || (Incident.getStatus().equals(CMS_STATUS_RESERVED) )) {
                                        if (existingMarker != null) {
                                            existingMarker.setPosition(latlng);
                                            existingMarker.setTitle(Incident.getType());
                                            existingMarker.setTag(Incident);

                                            mapMarkerManager.put(Incident.getId(), existingMarker);
                                        }
                                        else {
                                            Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(Incident.getType()));
                                            marker.setTag(Incident);
                                            mapMarkerManager.put(Incident.getId(), marker);
                                        }
                                    }
                                    else {
                                        if (existingMarker != null) {
                                            existingMarker.remove();
                                            mapMarkerManager.remove(Incident.getId());
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

    private void loadCategoryIntoSpinner() {
        String[] categories = new String[]{"Flood", "Fire Incident", "Terrorist Attack", "Chemical Leaking", "Tsunami", "Typhoon"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        typeCategory.setAdapter(adapter);
    }

    private void animateCameraToCurrentLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastLocation = task.getResult();
                    LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, MY_LOCATION_ZOOM);
                    mMap.animateCamera(cameraUpdate);
                    myLocationButton.setColorFilter(Color.argb(255,88,150,228));
                }
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (submitFormBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                Rect buttonRect = new Rect();
                submitFormBottomSheet.getGlobalVisibleRect(outRect);
                toggleSubmitBottomSheetButton.getGlobalVisibleRect(buttonRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()) && !buttonRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    submitFormBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            else if (incidentDetailBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                incidentDetailBottomSheet.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    incidentDetailBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        }
        myLocationButton.setColorFilter(Color.argb(255,0,0,0));
        return super.dispatchTouchEvent(event);
    }

    private void submitIncident() {
		incidentCounter++;
        Map<String, Integer> data = new HashMap<>();
        data.put("lastInsertedId", incidentCounter);
        incidentCounterDocument.set(data);
        final StorageReference fileReference = mStorageRef.child("images/" + System.currentTimeMillis() + "." + getFileExtension(selectedImage));
        UploadTask uploadTask = fileReference.putFile(selectedImage);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GeoPoint geoPoint = new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                Incident incident = new Incident();
                incident.setcreatedAt(new Date());
                incident.setcreatedBy(firebaseUser.getUid());
                incident.setDescription(descriptionInput.getText().toString());
                incident.setIncidentNo(CMS_PREFIX + String.format(CMS_PREFIX_NUMBER_FORMAT, incidentCounter));
                incident.setLocation(geoPoint);
                incident.setLocationDescription(locationDescriptionInput.getText().toString());
                incident.setStatus(CMS_STATUS_PENDING);
                incident.setTitle(titleInput.getText().toString());
                incident.setType(typeCategory.getSelectedItem().toString());
                incident.setupdatedAt(new Date());

                db.collection("Incident").document().set(incident)

                final GeoPoint geoPoint = new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                Date submitDate = Calendar.getInstance().getTime();

                Incidents incidents = new Incidents(firebaseUser.getUid(),submitDate, firebaseUser.getUid(), descriptionInput.getText().toString(), geoPoint, locationDescriptionInput.getText().toString(), titleInput.getText().toString()
                , typeCategory.getSelectedItem().toString(), "open", uri.toString());

                db.collection("incidents").document().set(incidents)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MapsActivity.this, "Incident added", Toast.LENGTH_SHORT).show();
                                submitProgressBar.setVisibility(View.INVISIBLE);
                                submitFormBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                submitProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MapsActivity.this, "Unable to add", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private String getAddressName(GeoPoint geoPoint)
    {
        String myAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(),geoPoint.getLongitude(),1);
            myAddress = addresses.get(0).getAddressLine(0);
        }catch (IOException e){
            e.printStackTrace();
        }
        return myAddress;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (submitFormBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            submitFormBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else if (incidentDetailBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            incidentDetailBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.side_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_request) {
            Intent i = new Intent(getApplicationContext(),IncidentActivity.class);
            i.putExtra("FROM_ACTIVITY", "MapsActivity");
            startActivity(i);
        }
        else if (id == R.id.nav_logout) {
            Intent i = new Intent(getApplicationContext(),LogoutActivity.class);
            i.putExtra("FROM_ACTIVITY", "MapsActivity");
            startActivity(i);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() instanceof Incident) {
            Incident incident = (Incident) marker.getTag();

            if (incidentDetailBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
        if (marker.getTag() instanceof Incidents) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Incidents incident = (Incidents) marker.getTag();

            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    mLastLocation = task.getResult();
                }
            });

            if (incidentDetailBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                titleTextView.setText(incident.getTitle());
                addressTextView.setText(getAddressName(incident.getLocation()));
                remarksTextView.setText(incident.getDescription());
                submitDateView.setText(dateFormat.format(incident.getcreatedAt()));
                addressDescriptionTextView.setText(incident.getLocationDescription());

                db.collection("User").document(incident.getRequesterUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = documentSnapshot.toObject(User.class);
                                requesterNameTextView.setText(user.getName());
                            }
                        });

                Picasso.get().load(incident.getImageUri()).into(incidentImageView);
                incidentDetailBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else {
                incidentDetailBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_IMAGE_OPEN: {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    selectedImage = data.getData();
                    Toast.makeText(MapsActivity.this, selectedImage.toString(), Toast.LENGTH_SHORT).show();
                    Picasso.get().load(selectedImage).into(showImage);
                }
            }
        }
    }

}