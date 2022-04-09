package by.bsuir.relaxapp;

import static by.bsuir.relaxapp.MainActivity.CURR_USER_DB_INFO;
import static by.bsuir.relaxapp.MainActivity.DB_HELPER;
import static by.bsuir.relaxapp.MainActivity.MAIN_ACTIVITY_CONTEXT;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfileFragment extends Fragment {

    private EditText
            BodyWeightEditText,
            HeightEditText,
            SystolicEditText,
            DiastolicEditText,
            AgeEditText;

    private Spinner horoscopeSpinner;

    private MaterialButton
            bodyWeightOKButton,
            heightOKButton,
            bloodPressureOKButton,
            ageOKButton,
            zodiacOKButton;

    private GridView photoGalleryGrid;


    public static boolean FETCH_USER_NAME_FIRST_TIME = true;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String fullName;
    private String email;

    private ProgressBar progressBar;

    private String userID;

    private TextView userNameTextView;
    private TextView userEmailTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ProfileFragment() {    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void insertNewUserIntoDatabase(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_id, userID);
        contentValues.put(DatabaseHelper.KEY_weight, CURR_USER_DB_INFO.WEIGHT);
        contentValues.put(DatabaseHelper.KEY_height, CURR_USER_DB_INFO.HEIGHT);
        contentValues.put(DatabaseHelper.KEY_sysPressure, CURR_USER_DB_INFO.SYS_PRESSURE);
        contentValues.put(DatabaseHelper.KEY_diaPressure, CURR_USER_DB_INFO.DIA_PRESSURE);
        contentValues.put(DatabaseHelper.KEY_age, CURR_USER_DB_INFO.AGE);
        contentValues.put(DatabaseHelper.KEY_zodiac, CURR_USER_DB_INFO.ZODIAC);

        sQlitedatabase.insert(DatabaseHelper.TABLE_USERS, null, contentValues);
        DB_HELPER.close();
    }

    private void getEditTextDataFromDatabase(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();
        Cursor cursor = sQlitedatabase.query(
                DatabaseHelper.TABLE_USERS,
                new String[] {
                        DatabaseHelper.KEY_weight,
                        DatabaseHelper.KEY_height,
                        DatabaseHelper.KEY_sysPressure,
                        DatabaseHelper.KEY_diaPressure,
                        DatabaseHelper.KEY_age,
                        DatabaseHelper.KEY_zodiac},
                DatabaseHelper.KEY_id + " = '" + userID + "'",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            int weightInd = cursor.getColumnIndex(DatabaseHelper.KEY_weight);
            int heightInd = cursor.getColumnIndex(DatabaseHelper.KEY_height);
            int sysPressureInd = cursor.getColumnIndex(DatabaseHelper.KEY_sysPressure);
            int diaPressureInd = cursor.getColumnIndex(DatabaseHelper.KEY_diaPressure);
            int ageInd = cursor.getColumnIndex(DatabaseHelper.KEY_age);
            int zodiacInd = cursor.getColumnIndex(DatabaseHelper.KEY_zodiac);

            CURR_USER_DB_INFO = new UserInfoDB(
                        cursor.getInt(weightInd),
                        cursor.getInt(heightInd),
                        cursor.getInt(sysPressureInd),
                        cursor.getInt(diaPressureInd),
                        cursor.getInt(ageInd),
                        cursor.getInt(zodiacInd)
                );
        } else {
            CURR_USER_DB_INFO = new UserInfoDB();
            insertNewUserIntoDatabase();
        }

        Log.e("DB", CURR_USER_DB_INFO.toString());
        DB_HELPER.close();
    }

    private void fillEditTexts(){
        if (CURR_USER_DB_INFO.WEIGHT != -1){
            BodyWeightEditText.setText(String.valueOf(CURR_USER_DB_INFO.WEIGHT));
        }
        if (CURR_USER_DB_INFO.HEIGHT != -1){
            HeightEditText.setText(String.valueOf(CURR_USER_DB_INFO.HEIGHT));
        }
        if (CURR_USER_DB_INFO.SYS_PRESSURE != -1){
            SystolicEditText.setText(String.valueOf(CURR_USER_DB_INFO.SYS_PRESSURE));
        }
        if (CURR_USER_DB_INFO.DIA_PRESSURE != -1){
            DiastolicEditText.setText(String.valueOf(CURR_USER_DB_INFO.DIA_PRESSURE ));
        }
        if (CURR_USER_DB_INFO.AGE != -1){
            AgeEditText.setText(String.valueOf(CURR_USER_DB_INFO.AGE));
        }
    }

    private void findAllOKButtons(View view){
        bodyWeightOKButton = view.findViewById(R.id.BodyWeightOKButton);
        heightOKButton = view.findViewById(R.id.HeightOKButton);
        bloodPressureOKButton = view.findViewById(R.id.BloodPressureOKButton);
        ageOKButton = view.findViewById(R.id.AgeOKButton);
        zodiacOKButton = view.findViewById(R.id.HoroscopeOKButton);
    }

    private void findAllEditTexts(View view) {
        BodyWeightEditText = view.findViewById(R.id.BodyWeightEditText);
        HeightEditText = view.findViewById(R.id.HeightEditText);
        SystolicEditText = view.findViewById(R.id.SystolicEditText);
        DiastolicEditText = view.findViewById(R.id.DiastolicEditText);
        AgeEditText = view.findViewById(R.id.AgeEditText);
    }

    private void findHoroscopeSpinner(View view){
        horoscopeSpinner = view.findViewById(R.id.HoroscopeSpinner);
    }

    private void findPhotoGalleryGrid(View view){
        photoGalleryGrid = view.findViewById(R.id.photoGalleryGrid);
    }

    private boolean weightCorrect(){
        String weightStr = BodyWeightEditText.getText().toString().trim();
        try{
            int weight = Integer.parseInt(weightStr);

            if (weight < 20 || weight > 350) return false;

            CURR_USER_DB_INFO.WEIGHT = weight;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void updateWeight(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_weight, CURR_USER_DB_INFO.WEIGHT);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    private boolean heightCorrect(){
        String heightStr = HeightEditText.getText().toString().trim();
        try{
            int height = Integer.parseInt(heightStr);

            if (height < 50 || height > 290) return false;

            CURR_USER_DB_INFO.HEIGHT = height;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void updateHeight(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_height, CURR_USER_DB_INFO.HEIGHT);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    private boolean sysPressureCorrect(){
        String sysStr = SystolicEditText.getText().toString().trim();
        try{
            int sys = Integer.parseInt(sysStr);

            if (sys < 100 || sys > 210) return false;

            CURR_USER_DB_INFO.SYS_PRESSURE = sys;
        } catch (NumberFormatException e) {
            return false;
        }


        return true;
    }

    private void updateSysPressure(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_sysPressure, CURR_USER_DB_INFO.SYS_PRESSURE);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    private boolean diaPressureCorrect(){
        String diaStr = DiastolicEditText.getText().toString().trim();
        try{
            int dia = Integer.parseInt(diaStr);

            if (dia < 60 || dia > 150) return false;

            CURR_USER_DB_INFO.DIA_PRESSURE= dia;
        } catch (NumberFormatException e) {
            return false;
        }


        return true;
    }

    private void updateDiaPressure(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_diaPressure, CURR_USER_DB_INFO.DIA_PRESSURE);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    private void updateZodiac(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_zodiac, CURR_USER_DB_INFO.ZODIAC);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    private boolean ageCorrect(){
        String ageStr = AgeEditText.getText().toString().trim();
        try{
            int age = Integer.parseInt(ageStr);

            if (age < 1 || age > 190) return false;

            CURR_USER_DB_INFO.AGE = age;
        } catch (NumberFormatException e) {
            return false;
        }


        return true;
    }

    private void updateAge(){
        DB_HELPER = new DatabaseHelper(MainActivity.MAIN_ACTIVITY_CONTEXT);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_age, CURR_USER_DB_INFO.AGE);
        String where = DatabaseHelper.KEY_id + " = '" + userID + "'";
        sQlitedatabase.update(DatabaseHelper.TABLE_USERS, contentValues, where, null);

        DB_HELPER.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = view.findViewById(R.id.progressBarProfileFragment);


        findAllOKButtons(view);
        findAllEditTexts(view);
        findHoroscopeSpinner(view);
        findPhotoGalleryGrid(view);

        bodyWeightOKButton.setOnClickListener(lambda->{
            if (weightCorrect()) {
                updateWeight();
            } else{
                BodyWeightEditText.setError("20<=Вес<=350!");
                BodyWeightEditText.requestFocus();
            }
        });
        heightOKButton.setOnClickListener(lambda->{
            if (heightCorrect()){
                updateHeight();
            } else{
                HeightEditText.setError("50<=Рост<=290!");
                HeightEditText.requestFocus();
            }
        });
        bloodPressureOKButton.setOnClickListener(lambda->{
            if (sysPressureCorrect()){
                updateSysPressure();
            } else{
                SystolicEditText.setError("100<=Сист.<=210!");
                SystolicEditText.requestFocus();
            }

            if (diaPressureCorrect()){
                updateDiaPressure();
            } else{
                DiastolicEditText.setError("60<=Диаст.<=150!");
                DiastolicEditText.requestFocus();
            }
        });
        ageOKButton.setOnClickListener(lambda->{
            if (ageCorrect()){
                updateAge();
            } else{
                AgeEditText.setError("1<=Возраст<=190!");
                AgeEditText.requestFocus();
            }
        });
        zodiacOKButton.setOnClickListener(lambda->{
            CURR_USER_DB_INFO.ZODIAC = horoscopeSpinner.getSelectedItemPosition();
            updateZodiac();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.MAIN_ACTIVITY_CONTEXT,
                R.array.horoscope_options_array, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horoscopeSpinner.setAdapter(adapter);


        userNameTextView = view.findViewById(R.id.UserNameTextView);
        userEmailTextView = view.findViewById(R.id.UserEmailTextView);
        if (FETCH_USER_NAME_FIRST_TIME) {
            progressBar.setVisibility(View.VISIBLE);
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            Log.e("ID", userID);

            reference.child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            if (userProfile != null) {
                                fullName = userProfile.name;
                                email = userProfile.email;

                                userNameTextView.setText(fullName);
                                userEmailTextView.setText(email);
                                //!ВНИМАНИЕ НАЧИНАЕТСЯ ЧТЕНИЕ ИЗ БД
                                {
                                    getEditTextDataFromDatabase();
                                    fillEditTexts();
                                    if (CURR_USER_DB_INFO.ZODIAC != -1)
                                    horoscopeSpinner.setSelection(CURR_USER_DB_INFO.ZODIAC);
                                }
                                //!ВНИМАНИЕ
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.MAIN_ACTIVITY_CONTEXT, "Something wrong!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            FETCH_USER_NAME_FIRST_TIME = false;
        } else {
            userNameTextView.setText(fullName);
            userEmailTextView.setText(email);
        }



        ProfileImageGridAdapter photoAdapter = new ProfileImageGridAdapter(MainActivity.MAIN_ACTIVITY_CONTEXT, shit);
        photoGalleryGrid.setAdapter(photoAdapter);
        photoGalleryGrid.setOnItemClickListener((adapterView, view1, i, l) -> {
            int item_pos = shit[i];
            showDialogBox(i);
        });

        return view;
    }

    int shit[] = { R.drawable.add_image};

    public void showDialogBox(int itemId){
        Dialog dialog = new Dialog(MAIN_ACTIVITY_CONTEXT, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.gallery_item_dialog_box);

        ImageView imgView = dialog.findViewById(R.id.ItemImage);
        Button deleteItemBtn = dialog.findViewById(R.id.deleteItemButton);
        Button closeItemBtn = dialog.findViewById(R.id.closeItemButton);

        imgView.setImageResource(shit[itemId]);
        closeItemBtn.setOnClickListener(lambda->{
            dialog.dismiss();
        });

        dialog.show();
    }
}