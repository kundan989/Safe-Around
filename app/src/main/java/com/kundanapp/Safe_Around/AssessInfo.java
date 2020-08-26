package com.kundanapp.Safe_Around;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;

import java.util.Collections;

public class AssessInfo extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button yes1,yes2,yes3,no1,no2,no3,submitbt;
    boolean isclicked1=false;
    boolean isclicked2=false;
    boolean isclicked3=false;
    LinearLayout tab;
    TextView mob;
    TextView mobd;
    TextView mark;
    int yes=0;
    int login=0;
    String phoneno;
    private static final int RC_SIGN_IN = 101;
    TextView note;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_info);
        setTitle("Assess Yourself");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        yes1 = findViewById(R.id.buttonYES);
        yes2 = findViewById(R.id.buttonYES2);
        tab = findViewById(R.id.logininfo);
        yes3 = findViewById(R.id.buttonYES3);
        mob= findViewById(R.id.mobno);
        mark= findViewById(R.id.mark);
        no1 = findViewById(R.id.buttonNO);
        no2 = findViewById(R.id.buttonNO2);
        no3 = findViewById(R.id.buttonNO3);
        submitbt = findViewById(R.id.submit);
        mobd = findViewById(R.id.drawermob);
        note=findViewById(R.id.note);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            phoneno=user.getPhoneNumber();
            editor.putString("number", phoneno);
            editor.commit();
            login=1;
            boolean val = pref.getBoolean("track", false);
            if(val)
            {
                mark.setTextColor(Color.parseColor("#D50000"));
                mark.setText("Marked +ve");
            }
            tab.setVisibility(View.VISIBLE);
            mob.setText(phoneno);
        }
        else
        {
            login=0;
            editor.putBoolean("login", false);
            editor.commit();
        }

        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked1=true;
                yes1.setBackgroundColor(Color.RED);
                no1.setBackgroundColor(Color.parseColor("#161625"));
            }
        });

        no1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked1=true;
               no1.setBackgroundColor(Color.RED);
               yes1.setBackgroundColor(Color.parseColor("#161625"));
            }
        });

        yes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked2=true;
                yes2.setBackgroundColor(Color.RED);
                no2.setBackgroundColor(Color.parseColor("#161625"));
            }
        });

        no2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked2=true;
                no2.setBackgroundColor(Color.RED);
                yes2.setBackgroundColor(Color.parseColor("#161625"));
            }
        });

        yes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked3=true;
                yes=1;
                yes3.setBackgroundColor(Color.RED);
                no3.setBackgroundColor(Color.parseColor("#161625"));
                if(login==0)
                    note.setVisibility(View.VISIBLE);
            }
        });

        no3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isclicked3=true;
                yes=0;
                no3.setBackgroundColor(Color.RED);
                yes3.setBackgroundColor(Color.parseColor("#161625"));
//                LinearLayout rl1 = (LinearLayout) findViewById(R.id.feverlayout);
                note.setVisibility(View.GONE);

            }
        });

        submitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ((isclicked1==true && isclicked2==true && isclicked3==true))
                {
                    if (yes == 1 && login == 0) {
                        doPhoneLogin();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        phoneno=user.getPhoneNumber();
                        editor.putString("number", phoneno);
                        editor.putBoolean("track", true);
                        editor.putBoolean("login", true);
                        editor.commit();
                        finish();
                    } else if (yes == 0 && login == 1) {
                        editor.putBoolean("track", false);
                        editor.putBoolean("login", true);
                        editor.commit();
                        finish();
                    } else if (yes == 1 && login == 1) {
                        editor.putBoolean("track", true);
                        editor.putBoolean("login", true);
                        editor.commit();
                        finish();
                    } else if (yes == 0 && login == 0) {
                        editor.putBoolean("track", false);
                        editor.putBoolean("login", false);
                        editor.commit();
                        finish();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Answer all the Questions Above", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void doPhoneLogin() {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
//                editor.putBoolean("track", true);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                phoneno=user.getPhoneNumber();
                editor.putString("number", phoneno);
                editor.putBoolean("login", true);
                editor.commit();
//                showAlertDialog(user);
            } else {
                /**
                 *   Sign in failed. If response is null the user canceled the
                 *   sign-in flow using the back button. Otherwise check
                 *   response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(getBaseContext(), "Phone Auth Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void showAlertDialog(FirebaseUser user) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                AssessInfo.this);
        // Set Title
        mAlertDialog.setTitle("Successfully Signed In");
        // Set Message
        mAlertDialog.setMessage(" Phone Number is " + user.getPhoneNumber());
        mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mAlertDialog.create();
        // Showing Alert Message
        mAlertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.loginmenu) {
            if(login==0)
            doPhoneLogin();
            else
                Toast.makeText(getBaseContext(), "You are already login.", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}