package com.example.bodyguardapp;

import android.Manifest;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
public class firstScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirestoreRecyclerAdapter adapter;
    private SimpleLocation location;

    //facebook and login
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public ArrayList<String> mBTDevicesNames = new ArrayList<>();
    String sessionType;
    //    private static final String TAG = "MainActivity";
    private boolean isRegesstered = false, session = false;
    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtName, txtEmail;
    BluetoothAdapter mBluetoothAdapter;
    private CallbackManager callbackManager;
    //Personal data
    public TextView PersonalHightText, PersonalWightText, PersonalAgeText, PersonalPhoneText, selectGender;
    public EditText PersonalHightEdit, PersonalWightEdit, PersonalAgeEdit, PersonalPhoneEdit;
    public Button PersonalSubmit;
    public RadioGroup gender;

    // GUI Components
    private TextView mBluetoothStatus;
    private TextView mReadBuffer;
    private Button mScanBtn;
    private Button mOffBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;

    private final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler; // Our main handler that will receive callback notifications
    private firstScreen.ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private FloatingActionButton fab = null;


    //stopWatch
    TextView WatchTextView;
    TextView CalTextView, Cal, Heart, CPM, CPM2;
    TextView heartBeatTextView;
    Button stopSession;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler WatchHandler;
    int Seconds, Minutes, MilliSeconds;
    TextView genderText;
    //choice activity
    Button runActivity, walkActivity, danceActivity, bikeActivity;
    double MET;
    //SOS activity
    Button emrButton;
    TextView emrView;
    EditText emrEdit;
    ArrayList<String> emrContacts = new ArrayList<String>();
    //History
    RecyclerView historyView;
    TextView historyText;
    //firbase
    private MenuItem item;
    private Map<String, Object> keys = new HashMap<>();
    private Map<String, Object> Histkeys = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    boolean monitor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                1);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
        //stopWatch
        WatchTextView = (TextView) findViewById(R.id.WatchtextView);
        CalTextView = (TextView) findViewById(R.id.CalTextView);
        Heart = (TextView) findViewById(R.id.BPM);
        Cal = (TextView) findViewById(R.id.CalView);
        CPM2 = (TextView) findViewById(R.id.CalPerM);
        CPM = (TextView) findViewById(R.id.CPM);
        heartBeatTextView = (TextView) findViewById(R.id.heartBeatTextView);
        stopSession = (Button) findViewById(R.id.StopButton);
        walkActivity = (Button) findViewById(R.id.ActivityWalk);
        runActivity = (Button) findViewById(R.id.ActivityRun);
        danceActivity = (Button) findViewById(R.id.ActivityDancing);
        bikeActivity = (Button) findViewById(R.id.ActivityBicycle);
        WatchHandler = new Handler();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //only show session acticity or stop session and show main
                if (isRegesstered) {
                    if (!session) {
                        fab.setImageResource(android.R.drawable.ic_media_pause);
                        StartTime = SystemClock.uptimeMillis();
                        WatchHandler.postDelayed(runnable, 0);
                        CalTextView.setVisibility(View.VISIBLE);
                        CPM.setVisibility(View.VISIBLE);
                        CPM2.setVisibility(View.VISIBLE);
                        Heart.setVisibility(View.VISIBLE);
                        Cal.setVisibility(View.VISIBLE);
                        stopSession.setVisibility(View.VISIBLE);
                        heartBeatTextView.setVisibility(View.VISIBLE);
                        session = true;
                    } else {
                        fab.setImageResource(android.R.drawable.ic_media_play);
                        TimeBuff += MillisecondTime;
                        WatchHandler.removeCallbacks(runnable);
                        session = false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "insert Personal Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab.hide();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        loginButton = navigationView.getHeaderView(0).findViewById(R.id.login_button);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        circleImageView = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);
        loadUserProfile(AccessToken.getCurrentAccessToken());

        //PersonalData
        PersonalAgeText = (TextView) findViewById(R.id.PersonalAge);
        PersonalHightText = (TextView) findViewById(R.id.PersonalHight);
        PersonalWightText = (TextView) findViewById(R.id.PersonalWight);
        PersonalAgeEdit = (EditText) findViewById(R.id.PersonalAgeText);
        PersonalHightEdit = (EditText) findViewById(R.id.PersonalHightText);
        PersonalWightEdit = (EditText) findViewById(R.id.PersonalWightText);
        PersonalSubmit = (Button) findViewById(R.id.PersonalButton);
        PersonalPhoneEdit = (EditText) findViewById(R.id.PersonalPhoneText);
        PersonalPhoneText = (TextView) findViewById(R.id.PersonalPhone);
        gender = (RadioGroup) findViewById(R.id.radioGroup);
        selectGender = (TextView) findViewById(R.id.CalView);
        emrButton = (Button) findViewById(R.id.EmrButton);
        emrEdit = (EditText) findViewById(R.id.EmrEdit);
        emrView = (TextView) findViewById(R.id.EmgContact);
        genderText = (TextView) findViewById(R.id.textView);
        historyText = (TextView) findViewById(R.id.historyText);
        historyView = (RecyclerView) findViewById(R.id.historyView);
        PersonalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRegesstered = true;
                PersonalAgeEdit.setVisibility(View.INVISIBLE);
                PersonalAgeText.setVisibility(View.INVISIBLE);
                PersonalPhoneEdit.setVisibility(View.INVISIBLE);
                PersonalPhoneText.setVisibility(View.INVISIBLE);
                gender.setVisibility(View.INVISIBLE);
                selectGender.setVisibility(View.INVISIBLE);
                PersonalHightEdit.setVisibility(View.INVISIBLE);
                PersonalHightText.setVisibility(View.INVISIBLE);
                PersonalWightEdit.setVisibility(View.INVISIBLE);
                PersonalWightText.setVisibility(View.INVISIBLE);
                PersonalSubmit.setVisibility(View.INVISIBLE);
                genderText.setVisibility(View.INVISIBLE);
                docRef = db.collection(txtName.getText().toString()).document("personalData");
                keys.put("Hight :", PersonalHightEdit.getText().toString());
                keys.put("Wight :", PersonalWightEdit.getText().toString());
                keys.put("Age :", PersonalAgeEdit.getText().toString());
                keys.put("Trainer :", PersonalPhoneEdit.getText().toString());
                int selectedID = gender.getCheckedRadioButtonId();
                RadioButton selectedGender = findViewById(selectedID);
                keys.put("gender", selectedGender.getText().toString());
                docRef.set(keys);
                mBluetoothStatus.setVisibility(View.VISIBLE);
                mReadBuffer.setVisibility(View.VISIBLE);
                mScanBtn.setVisibility(View.VISIBLE);
                mOffBtn.setVisibility(View.VISIBLE);
                mDiscoverBtn.setVisibility(View.VISIBLE);
                mListPairedDevicesBtn.setVisibility(View.VISIBLE);
                mDevicesListView.setVisibility(View.VISIBLE);
                mBluetoothStatus.setVisibility(View.INVISIBLE);
                mReadBuffer.setVisibility(View.INVISIBLE);
            }
        });

        mBluetoothStatus = (TextView) findViewById(R.id.bluetoothStatus);
        mReadBuffer = (TextView) findViewById(R.id.readBuffer);
        mScanBtn = (Button) findViewById(R.id.scan);
        mOffBtn = (Button) findViewById(R.id.off);
        mDiscoverBtn = (Button) findViewById(R.id.discover);
        mListPairedDevicesBtn = (Button) findViewById(R.id.PairedBtn);

        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = (ListView) findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Ask for location permission if not already allowed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        walkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MET = 3.5;
                fab.show();
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                WatchTextView.setText("00:00:00");
                TimeBuff += MillisecondTime;
                WatchHandler.removeCallbacks(runnable);
                WatchTextView.setVisibility(View.VISIBLE);
                CalTextView.setVisibility(View.VISIBLE);
                CPM.setVisibility(View.VISIBLE);
                CPM2.setVisibility(View.VISIBLE);
                Heart.setVisibility(View.VISIBLE);
                Cal.setVisibility(View.VISIBLE);
                heartBeatTextView.setVisibility(View.VISIBLE);
                stopSession.setVisibility(View.VISIBLE);
                walkActivity.setVisibility(View.INVISIBLE);
                runActivity.setVisibility(View.INVISIBLE);
                danceActivity.setVisibility(View.INVISIBLE);
                bikeActivity.setVisibility(View.INVISIBLE);
                sessionType = "Walk";
                WatchTextView.setText("00:00:00");

            }
        });

        runActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MET = 11;
                fab.show();
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                WatchTextView.setText("00:00:00");
                TimeBuff += MillisecondTime;
                WatchHandler.removeCallbacks(runnable);
                WatchTextView.setVisibility(View.VISIBLE);
                CalTextView.setVisibility(View.VISIBLE);
                CPM.setVisibility(View.VISIBLE);
                CPM2.setVisibility(View.VISIBLE);
                Heart.setVisibility(View.VISIBLE);
                Cal.setVisibility(View.VISIBLE);
                heartBeatTextView.setVisibility(View.VISIBLE);
                stopSession.setVisibility(View.VISIBLE);
                walkActivity.setVisibility(View.INVISIBLE);
                runActivity.setVisibility(View.INVISIBLE);
                danceActivity.setVisibility(View.INVISIBLE);
                bikeActivity.setVisibility(View.INVISIBLE);
                sessionType = "run";
                WatchTextView.setText("00:00:00");

            }
        });
        danceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MET = 5;
                fab.show();
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                WatchTextView.setText("00:00:00");
                TimeBuff += MillisecondTime;
                WatchHandler.removeCallbacks(runnable);
                WatchTextView.setVisibility(View.VISIBLE);
                CalTextView.setVisibility(View.VISIBLE);
                CPM.setVisibility(View.VISIBLE);
                CPM2.setVisibility(View.VISIBLE);
                Heart.setVisibility(View.VISIBLE);
                Cal.setVisibility(View.VISIBLE);
                heartBeatTextView.setVisibility(View.VISIBLE);
                stopSession.setVisibility(View.VISIBLE);
                walkActivity.setVisibility(View.INVISIBLE);
                runActivity.setVisibility(View.INVISIBLE);
                danceActivity.setVisibility(View.INVISIBLE);
                bikeActivity.setVisibility(View.INVISIBLE);
                sessionType = "Dance";
                WatchTextView.setText("00:00:00");

            }
        });
        bikeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MET = 10;
                fab.show();
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                WatchTextView.setText("00:00:00");
                TimeBuff += MillisecondTime;
                WatchHandler.removeCallbacks(runnable);
                WatchTextView.setVisibility(View.VISIBLE);
                CalTextView.setVisibility(View.VISIBLE);
                CPM.setVisibility(View.VISIBLE);
                CPM2.setVisibility(View.VISIBLE);
                Heart.setVisibility(View.VISIBLE);
                Cal.setVisibility(View.VISIBLE);
                heartBeatTextView.setVisibility(View.VISIBLE);
                stopSession.setVisibility(View.VISIBLE);
                walkActivity.setVisibility(View.INVISIBLE);
                runActivity.setVisibility(View.INVISIBLE);
                danceActivity.setVisibility(View.INVISIBLE);
                bikeActivity.setVisibility(View.INVISIBLE);
                sessionType = "bike";
                WatchTextView.setText("00:00:00");

            }
        });
        stopSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                WatchTextView.setVisibility(View.INVISIBLE);
                CalTextView.setVisibility(View.INVISIBLE);
                CPM.setVisibility(View.INVISIBLE);
                CPM2.setVisibility(View.INVISIBLE);
                Heart.setVisibility(View.INVISIBLE);
                Cal.setVisibility(View.INVISIBLE);
                heartBeatTextView.setVisibility(View.INVISIBLE);
                stopSession.setVisibility(View.INVISIBLE);
                walkActivity.setVisibility(View.VISIBLE);
                runActivity.setVisibility(View.VISIBLE);
                danceActivity.setVisibility(View.VISIBLE);
                bikeActivity.setVisibility(View.VISIBLE);
                // db update
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                docRef = db.collection(txtName.getText().toString()).document(currentDateandTime);
                Histkeys.put("time", WatchTextView.getText().toString());
                Histkeys.put("cals", CalTextView.getText().toString());
                Histkeys.put("Session Type", sessionType);
                docRef.set(Histkeys);
                //send Data to Trainer
                SmsManager smgr = SmsManager.getDefault();
                String message = " trainee Name : " + txtName.getText().toString() + "\n Time of training : " + WatchTextView.getText().toString() + "\n Session Type : " + sessionType;
                smgr.sendTextMessage(PersonalPhoneEdit.getText().toString(), null, message, null, null);
                //stop clock
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                WatchTextView.setText("00:00:00");
                WatchHandler.postDelayed(runnable, 0);
                heartBeatTextView.setText("0");
                CalTextView.setText("0");
                CPM.setText("0");
                session = false;
            }
        });

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mReadBuffer.setText(readMessage);
                    boolean readable = true;
                    if (session) {
                        int val = Integer.parseInt(PersonalWightEdit.getText().toString());
                        double cals = 0.175 * MET * Minutes * val;
                        String str = Double.toString(cals);
                        CalTextView.setText(str);//todo::Check if coorect
                        heartBeatTextView.setText(mReadBuffer.getText());
                        String str2 = Double.toString(0.175 * MET * val);
                        CPM.setText(str2);
                    }
                    //todo:: monitor heart
                    long pastTime = 0;
                    int j = 0;
                    String buffer = mReadBuffer.getText().toString();
                    for (int i = 0; i < buffer.length(); i++) {
                        if (buffer.charAt(i) < '0' || buffer.charAt(i) > '9') {
                            j = i;
                            break;
                        }
                    }
                    buffer = buffer.substring(0, j);
                    if (readable && buffer!= "") {
                        if (Integer.parseInt(buffer) <= 40 && !monitor) {
                            pastTime = System.currentTimeMillis();
                            monitor = true;
                        }
                        if (Integer.parseInt(buffer) > 40) {
                            monitor = false;
                        }
                        location = new SimpleLocation(getBaseContext());
                        location.beginUpdates();
                        final double latitude = location.getLatitude();
                        final double longitude = location.getLongitude();
                        long test = System.currentTimeMillis();
                        if (test >= (pastTime + 15 * 1000) && monitor) { //multiply by 1000 to get milliseconds
                            SmsManager smgr = SmsManager.getDefault();
                            String message =txtName.getText().toString() +  "\n EMERGENCY at :" + "\n latitude : " + Double.toString(latitude) + "\n longitude : " + Double.toString(longitude);
                            for (int i = 0; i < emrContacts.size(); i++) {
                                smgr.sendTextMessage(emrContacts.get(i), null, message, null, null);
                            }
                        }

                    }
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        mBluetoothStatus.setText("Connected to Device: " + (String) (msg.obj));
//                        mBluetoothStatus.setVisibility(View.INVISIBLE);
//                        mReadBuffer.setVisibility(View.INVISIBLE);
                        mScanBtn.setVisibility(View.INVISIBLE);
                        mOffBtn.setVisibility(View.INVISIBLE);
                        mDiscoverBtn.setVisibility(View.INVISIBLE);
                        mListPairedDevicesBtn.setVisibility(View.INVISIBLE);
                        mDevicesListView.setVisibility(View.INVISIBLE);
                        walkActivity.setVisibility(View.VISIBLE);
                        runActivity.setVisibility(View.VISIBLE);
                        danceActivity.setVisibility(View.VISIBLE);
                        bikeActivity.setVisibility(View.VISIBLE);
                        mBluetoothStatus.setVisibility(View.INVISIBLE);
                        mReadBuffer.setVisibility(View.INVISIBLE);
                    } else
                        mBluetoothStatus.setText("Connection Failed");
                }
            }
        };

        if (mBTArrayAdapter == null) {
            // Device does not support Bluetooth
            mBluetoothStatus.setText("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);
                }
            });

            mOffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOff(v);
                }
            });

            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPairedDevices(v);
                }
            });

            mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discover(v);
                }
            });
        }
        emrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emrContacts.add(emrEdit.getText().toString());
                emrEdit.setText("");
            }
        });

//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists())
//                {
//                    try
//                    {
//                        if(documentSnapshot..contains(txtName.toString())) {
//                            isRegesstered = true;
//                            PersonalAgeEdit.setVisibility(View.INVISIBLE);
//                            PersonalAgeText.setVisibility(View.INVISIBLE);
//                            PersonalPhoneEdit.setVisibility(View.INVISIBLE);
//                            PersonalPhoneText.setVisibility(View.INVISIBLE);
//                            gender.setVisibility(View.INVISIBLE);
//                            selectGender.setVisibility(View.INVISIBLE);
//                            PersonalHightEdit.setVisibility(View.INVISIBLE);
//                            PersonalHightText.setVisibility(View.INVISIBLE);
//                            PersonalWightEdit.setVisibility(View.INVISIBLE);
//                            PersonalWightText.setVisibility(View.INVISIBLE);
//                            PersonalSubmit.setVisibility(View.INVISIBLE);
//                            mBluetoothStatus.setVisibility(View.VISIBLE);
//                            mReadBuffer.setVisibility(View.VISIBLE);
//                            mScanBtn.setVisibility(View.VISIBLE);
//                            mOffBtn.setVisibility(View.VISIBLE);
//                            mDiscoverBtn.setVisibility(View.VISIBLE);
//                            mListPairedDevicesBtn.setVisibility(View.VISIBLE);
//                            mDevicesListView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    catch (NumberFormatException e)
//                    {
//                    }
//                }
//                else
//                {
//                }
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_screen, menu);
        getMenuInflater().inflate(R.menu.bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_bluetooth && isRegesstered) {
            mBluetoothStatus.setVisibility(View.INVISIBLE);
            mReadBuffer.setVisibility(View.INVISIBLE);
            if (mScanBtn.getVisibility() == View.INVISIBLE) {
                mBluetoothStatus.setVisibility(View.INVISIBLE);
                mReadBuffer.setVisibility(View.INVISIBLE);
                PersonalAgeEdit.setVisibility(View.INVISIBLE);
                PersonalAgeText.setVisibility(View.INVISIBLE);
                PersonalPhoneEdit.setVisibility(View.INVISIBLE);
                PersonalPhoneText.setVisibility(View.INVISIBLE);
                gender.setVisibility(View.INVISIBLE);
                selectGender.setVisibility(View.INVISIBLE);
                PersonalHightEdit.setVisibility(View.INVISIBLE);
                PersonalHightText.setVisibility(View.INVISIBLE);
                PersonalWightEdit.setVisibility(View.INVISIBLE);
                PersonalWightText.setVisibility(View.INVISIBLE);
                WatchTextView.setVisibility(View.INVISIBLE);
                CalTextView.setVisibility(View.INVISIBLE);
                CPM.setVisibility(View.INVISIBLE);
                CPM2.setVisibility(View.INVISIBLE);
                Heart.setVisibility(View.INVISIBLE);
                Cal.setVisibility(View.INVISIBLE);
                heartBeatTextView.setVisibility(View.INVISIBLE);
                stopSession.setVisibility(View.INVISIBLE);
                walkActivity.setVisibility(View.INVISIBLE);
                runActivity.setVisibility(View.INVISIBLE);
                danceActivity.setVisibility(View.INVISIBLE);
                bikeActivity.setVisibility(View.INVISIBLE);
                PersonalSubmit.setVisibility(View.INVISIBLE);
                mBluetoothStatus.setVisibility(View.VISIBLE);
                mReadBuffer.setVisibility(View.VISIBLE);
                mScanBtn.setVisibility(View.VISIBLE);
                mOffBtn.setVisibility(View.VISIBLE);
                mDiscoverBtn.setVisibility(View.VISIBLE);
                mListPairedDevicesBtn.setVisibility(View.VISIBLE);
                mDevicesListView.setVisibility(View.VISIBLE);
                emrEdit.setVisibility(View.INVISIBLE);
                emrView.setVisibility(View.INVISIBLE);
                emrButton.setVisibility(View.INVISIBLE);
                historyView.setVisibility(View.INVISIBLE);
                historyText.setVisibility(View.INVISIBLE);
                fab.hide();
                //Make every thing else INVISABLE :: todo
            } else {
//                mBluetoothStatus.setVisibility(View.INVISIBLE);
//                mReadBuffer.setVisibility(View.INVISIBLE);
                mScanBtn.setVisibility(View.INVISIBLE);
                mOffBtn.setVisibility(View.INVISIBLE);
                mDiscoverBtn.setVisibility(View.INVISIBLE);
                mListPairedDevicesBtn.setVisibility(View.INVISIBLE);
                mDevicesListView.setVisibility(View.INVISIBLE);
                fab.show();
            }
            emrEdit.setVisibility(View.INVISIBLE);
            emrView.setVisibility(View.INVISIBLE);
            emrButton.setVisibility(View.INVISIBLE);
        }
        if (id == R.id.action_bluetooth && !isRegesstered) {
            Toast.makeText(getApplicationContext(), "insert Personal Data", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.item = item;
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home && isRegesstered) {
            walkActivity.setVisibility(View.VISIBLE);
            runActivity.setVisibility(View.VISIBLE);
            danceActivity.setVisibility(View.VISIBLE);
            bikeActivity.setVisibility(View.VISIBLE);
            emrEdit.setVisibility(View.INVISIBLE);
            historyView.setVisibility(View.INVISIBLE);
            historyText.setVisibility(View.INVISIBLE);
            emrView.setVisibility(View.INVISIBLE);
            emrButton.setVisibility(View.INVISIBLE);
            mBluetoothStatus.setVisibility(View.INVISIBLE);
            mReadBuffer.setVisibility(View.INVISIBLE);
            mScanBtn.setVisibility(View.INVISIBLE);
            mOffBtn.setVisibility(View.INVISIBLE);
            mDiscoverBtn.setVisibility(View.INVISIBLE);
            mListPairedDevicesBtn.setVisibility(View.INVISIBLE);
            mDevicesListView.setVisibility(View.INVISIBLE);
        } else if (id == R.id.History && isRegesstered) {
            mBluetoothStatus.setVisibility(View.INVISIBLE);
            mReadBuffer.setVisibility(View.INVISIBLE);
            mScanBtn.setVisibility(View.INVISIBLE);
            mOffBtn.setVisibility(View.INVISIBLE);
            mDiscoverBtn.setVisibility(View.INVISIBLE);
            mListPairedDevicesBtn.setVisibility(View.INVISIBLE);
            historyView.setVisibility(View.VISIBLE);
            historyText.setVisibility(View.VISIBLE);
            emrEdit.setVisibility(View.INVISIBLE);
            emrView.setVisibility(View.INVISIBLE);
            emrButton.setVisibility(View.INVISIBLE);
            PersonalAgeEdit.setVisibility(View.INVISIBLE);
            PersonalAgeText.setVisibility(View.INVISIBLE);
            PersonalPhoneEdit.setVisibility(View.INVISIBLE);
            PersonalPhoneText.setVisibility(View.INVISIBLE);
            gender.setVisibility(View.INVISIBLE);
            selectGender.setVisibility(View.INVISIBLE);
            PersonalHightEdit.setVisibility(View.INVISIBLE);
            PersonalHightText.setVisibility(View.INVISIBLE);
            PersonalWightEdit.setVisibility(View.INVISIBLE);
            PersonalWightText.setVisibility(View.INVISIBLE);
            PersonalSubmit.setVisibility(View.INVISIBLE);
            walkActivity.setVisibility(View.INVISIBLE);
            runActivity.setVisibility(View.INVISIBLE);
            danceActivity.setVisibility(View.INVISIBLE);
            bikeActivity.setVisibility(View.INVISIBLE);
        } else if (id == R.id.SOS && isRegesstered) {
            emrEdit.setVisibility(View.VISIBLE);
            emrView.setVisibility(View.VISIBLE);
            emrButton.setVisibility(View.VISIBLE);
            PersonalAgeEdit.setVisibility(View.INVISIBLE);
            PersonalAgeText.setVisibility(View.INVISIBLE);
            PersonalPhoneEdit.setVisibility(View.INVISIBLE);
            PersonalPhoneText.setVisibility(View.INVISIBLE);
            gender.setVisibility(View.INVISIBLE);
            selectGender.setVisibility(View.INVISIBLE);
            PersonalHightEdit.setVisibility(View.INVISIBLE);
            PersonalHightText.setVisibility(View.INVISIBLE);
            PersonalWightEdit.setVisibility(View.INVISIBLE);
            PersonalWightText.setVisibility(View.INVISIBLE);
            PersonalSubmit.setVisibility(View.INVISIBLE);
            mBluetoothStatus.setVisibility(View.INVISIBLE);
            mReadBuffer.setVisibility(View.INVISIBLE);
            mScanBtn.setVisibility(View.INVISIBLE);
            mOffBtn.setVisibility(View.INVISIBLE);
            mDiscoverBtn.setVisibility(View.INVISIBLE);
            mListPairedDevicesBtn.setVisibility(View.INVISIBLE);
            mDevicesListView.setVisibility(View.INVISIBLE);
            WatchTextView.setVisibility(View.INVISIBLE);
            CalTextView.setVisibility(View.INVISIBLE);
            CPM.setVisibility(View.INVISIBLE);
            CPM2.setVisibility(View.INVISIBLE);
            Heart.setVisibility(View.INVISIBLE);
            Cal.setVisibility(View.INVISIBLE);
            heartBeatTextView.setVisibility(View.INVISIBLE);
            stopSession.setVisibility(View.INVISIBLE);
            walkActivity.setVisibility(View.INVISIBLE);
            runActivity.setVisibility(View.INVISIBLE);
            danceActivity.setVisibility(View.INVISIBLE);
            bikeActivity.setVisibility(View.INVISIBLE);
            historyView.setVisibility(View.INVISIBLE);
            historyText.setVisibility(View.INVISIBLE);
            fab.hide();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                mBluetoothStatus.setText("Enabled");
            } else
                mBluetoothStatus.setText("Disabled");
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    txtEmail.setText(email);
                    txtName.setText(first_name + " " + last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(firstScreen.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMainActivity);
            }
        }
    };

    private void bluetoothOn(View view) {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothStatus.setText("Bluetooth enabled");
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
//        // Check which request we're responding to
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//                mBluetoothStatus.setText("Enabled");
//            }
//            else
//                mBluetoothStatus.setText("Disabled");
//        }
//    }

    private void bluetoothOff(View view) {
        mBTAdapter.disable(); // turn off
        mBluetoothStatus.setText("Bluetooth disabled");
        Toast.makeText(getApplicationContext(), "Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void discover(View view) {
        // Check if the device is already discovering
        if (mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
        } else {
            if (mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(View view) {
        mBTArrayAdapter.clear();
        mPairedDevices = mBTAdapter.getBondedDevices();
        if (mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            mBluetoothStatus.setText("Connecting...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread() {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();

                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fail == false) {
                        mConnectedThread = new firstScreen.ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            WatchTextView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            WatchHandler.postDelayed(this, 0);
        }

    };

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}