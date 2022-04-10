package by.bsuir.relaxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static HoroscopeAPI horoscopeAPI = new HoroscopeAPI();
    public static Sign signs[] = null;

    public static Activity MAIN_ACTIVITY_CONTEXT;
    public static DatabaseHelper DB_HELPER;
    public static UserInfoDB CURR_USER_DB_INFO;

    DrawerLayout drawerLayout;
    ImageView customisedHamburger;
    TextView doExit;
    ActionBarDrawerToggle toggle;

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        ProfileFragment.FETCH_USER_NAME_FIRST_TIME = false;
        Intent toWelcomeActivity = new Intent(MainActivity.MAIN_ACTIVITY_CONTEXT, WelcomeActivity.class);
        startActivity(toWelcomeActivity);
    }

    private void getRidOfTopBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    private void setupBottomMenu() {
        BottomNavigationView bottomNavigationItemView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationItemView, navController);
    }

    private void FindDoExit(){
        doExit = findViewById(R.id.doExit);
    }

    private void FindDrawerLayout() {
        drawerLayout = findViewById(R.id.drawerLayout);
    }

    private void InstantiateToggle(){
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_string, R.string.close_string);
    }

    private void AddDrawerListenerAndSyncState(){
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void FindCustomisedHamburger(){
        customisedHamburger = findViewById(R.id.customised_hamburger);
    }

    private void SetupMainMenuNavigationView(){
        NavigationView navView = findViewById(R.id.MainMenuNavigationView);
        navView.setNavigationItemSelectedListener(menuItem->{
            switch (menuItem.getItemId()){
                case R.id.mmTutorial:{
                    Toast.makeText(MainActivity.this, "Tutorial", Toast.LENGTH_LONG).show();
                    break;
                }

                case R.id.mmHoroscope:{
                    Intent toHoroscopeActivity = new Intent(this, HoroscopeActivity.class);
                    startActivity(toHoroscopeActivity);
                    break;
                }

                case R.id.mmBodyWeightIndex:{
                    Toast.makeText(MainActivity.this, "Bodyweight", Toast.LENGTH_LONG).show();
                    break;
                }

                case R.id.mmEyeTraining:{
                    Toast.makeText(MainActivity.this, "Eye training", Toast.LENGTH_LONG).show();
                    break;
                }

                case R.id.mmDeveloperInfo:{
                    Intent toDeveloperActivity = new Intent(this, DeveloperActivity.class);
                    startActivity(toDeveloperActivity);
                    break;
                }
            }
            return true;
        });
    }

    private void downloadHoroscopeInformation(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (HoroscopeAPI.canConnect()){
                horoscopeAPI.load();
                signs = horoscopeAPI.getSigns();
            } else{
                signs = null;
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(7, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN_ACTIVITY_CONTEXT = this;

        getRidOfTopBar();

        setContentView(R.layout.activity_main);
        setupBottomMenu();

        FindDoExit();
        doExit.setOnClickListener(lambda->{
            logOut();
        });


        FindDrawerLayout();
        InstantiateToggle();
        AddDrawerListenerAndSyncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SetupMainMenuNavigationView();

        FindCustomisedHamburger();
        customisedHamburger.setOnClickListener(lambda->{
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        downloadHoroscopeInformation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}