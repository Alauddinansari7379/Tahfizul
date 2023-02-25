package com.amtech.tahfizulquranonline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.fragment.CompletedAssignFragment;
import com.amtech.tahfizulquranonline.fragment.DashboardFragment;
import com.amtech.tahfizulquranonline.fragment.MaulimFeedbackFragment;
import com.amtech.tahfizulquranonline.fragment.MaulimProfileFragment;
import com.amtech.tahfizulquranonline.fragment.NewAssignment;
import com.amtech.tahfizulquranonline.fragment.PendingAssignFragment;
import com.amtech.tahfizulquranonline.fragment.ReportFragment;
import com.amtech.tahfizulquranonline.fragment.TalibDashboardFragment;
import com.amtech.tahfizulquranonline.fragment.TalibIlmDetailsFragment;
import com.amtech.tahfizulquranonline.fragment.TalibPendingAssignment;
import com.amtech.tahfizulquranonline.fragment.ViewFeedbackFragment;
import com.amtech.tahfizulquranonline.utils.Constant;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import static com.amtech.tahfizulquranonline.utils.AppConstants.currentUserName;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private Dialog dialog;
    private static final String TAG = "MainActivity";
    private AlertDialog permissionDialog;
    private TextView maulimNameTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Constant.mainActivity=this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.actionbar_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View view1 = navigationView.getHeaderView(0);
        maulimNameTxt = view1.findViewById(R.id.maulim_name_txt);
        if (currentUserName!=null && !currentUserName.isEmpty()) {
            maulimNameTxt.setText(currentUserName);
        }
        ImageView closeBtn = view1.findViewById(R.id.close_drwr);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        String fragName = getIntent().getStringExtra("frag");
        if(fragName != null)
        {
            if(fragName.equals("pending_assignment"))
            {
                PendingAssignFragment pendingFragment = new PendingAssignFragment();
                setMyFragment(pendingFragment);
            }
            else if(fragName.equals("report_fragment"))
            {
                ReportFragment reportFragment = new ReportFragment();
                setMyFragment(reportFragment);
            }
            else if(fragName.equals("complete_assignment"))
            {
                CompletedAssignFragment completedAssignFragment = new CompletedAssignFragment();
                setMyFragment(completedAssignFragment);
            }
        }
        else
        {
            DashboardFragment dashboardFragment = new DashboardFragment();
            setMyFragment(dashboardFragment);
        }
        initLogoutDialog();
        checkPermissions();
    }
    private void initLogoutDialog()
    {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bground));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextView okBtn = dialog.findViewById(R.id.ok_btn);
        TextView canBtn = dialog.findViewById(R.id.can_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                //String rememberMe = pref.getString("remember", "noValue");
                editor.clear();
//                if(rememberMe.equals("YES"))
//                {
//                    editor.putString("login", "0");
//                }
//                else if(rememberMe.equals("NO"))
//                {
//                    editor.clear();
//                }
                editor.apply();
                dialog.dismiss();
                currentUserName = "";
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }
        });
        canBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void setMyFragment(Fragment fragment) {
        //get current fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
    @SuppressLint("NonConstantResourceId")
    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.dash:
                fragment = new DashboardFragment();
                toolbar.setTitle("Dashboard");
                break;
            case R.id.maulim_profile:
                fragment = new MaulimProfileFragment();
                toolbar.setTitle("Profile");
                break;
            case R.id.maulim_timeslots:
                fragment = new DashboardFragment();
                toolbar.setTitle("Class TimeSlots");
                break;
//            case R.id.assignments:
////                fragment = new BestDealFragment();
////                toolbar.setTitle(R.string.best_deal);
//                break;
            case R.id.new_assignment:
                fragment = new NewAssignment();
                toolbar.setTitle("New Assignment");
//                toolbar.setTitle(R.string.login);
                break;
            case R.id.pending_assignments:
                fragment = new PendingAssignFragment();
                toolbar.setTitle("Pending Assignment");
//                toolbar.setTitle(R.string.register);
                break;
            case R.id.completed_assignments:
                fragment = new CompletedAssignFragment();
                toolbar.setTitle("Completed Assignment");
//                toolbar.setTitle(R.string.customers);
                break;
//            case R.id.mou_class_recs:
////                fragment = new HomeFragment();
////                toolbar.setTitle(R.string.customers);
//                break;
            case R.id.mou_talibe_rep:
                fragment = new ReportFragment();
                toolbar.setTitle("Report");
                break;
            case R.id.mou_talibe_details:
                fragment = new TalibIlmDetailsFragment();
                toolbar.setTitle("My Talib Ilm");
                break;
            case R.id.view_feedback:
                fragment = new MaulimFeedbackFragment();
                toolbar.setTitle("View Feedbacks");
                break;
            case R.id.maulim_logout:
                dialog.show();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //calling the method display selected screen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//            DashboardFragment cartFragment = new DashboardFragment();
//            setMyFragment(cartFragment);
//            //dialog.show();
//            return true;
//        }
//        if (id == R.id.action_notification) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if(fragment instanceof DashboardFragment)
            {
                super.onBackPressed();
            }
            else
            {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
//            super.onBackPressed();
        }
    }
    public void checkPermissions()
    {
        Log.i(TAG, "checkPermissions is called: ");
        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted())
                        {
                            //Toast.makeText(MainActivity.this, "All permissions granted!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(report.isAnyPermissionPermanentlyDenied())
                            {
                                showGoToAppSettingsDialog();
                            }
                            else
                            {
                                showPermissionWarningDialog();
                            }
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public void showPermissionWarningDialog()
    {
        permissionDialog = new AlertDialog.Builder(this)
                .setTitle("PERMISSIONS REQUIRED")
                .setMessage("Give permissions for proper app functionality")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermissions();
                    }
                }).show();
    }
    public void showGoToAppSettingsDialog()
    {
        permissionDialog = new AlertDialog.Builder(this)
                .setTitle("PERMISSIONS PERMANENTLY DENIED")
                .setMessage("Go to app settings to enable permissions")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    }
                }).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
        checkPermissions();
    }
public void viewSelect()
{
    navigationView.getMenu().getItem(4).setChecked(true);
    Fragment fragment  = new PendingAssignFragment();
    toolbar.setTitle("Pending Assignment");
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.content_frame, fragment);
    ft.commit();
}
}