package com.example.fizetsihatridfigyelmeztetalkalmazs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap.KezdolapFragment;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;

    AlertDialog.Builder dialogBuilder;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_kezdolap, R.id.nav_hozzaadas, R.id.nav_befizetettSzamlak, R.id.nav_statisztikak, R.id.nav_beallitasok)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        mAuth = FirebaseAuth.getInstance();

        BejelentkezesDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    public void BejelentkezesDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View loginPopupView = getLayoutInflater().inflate(R.layout.loginpopup, null);


        EditText editTextLoginEmail, editTextLoginJelszo;
        Button buttonBejelentkezes;
        TextView textViewElfelejtettJelszo, textViewRegisztracio;
        ProgressBar progressBarBejelentkezes;

        editTextLoginEmail = loginPopupView.findViewById(R.id.editTextLoginEmail);
        editTextLoginJelszo = loginPopupView.findViewById(R.id.editTextLoginJelszo);
        buttonBejelentkezes = loginPopupView.findViewById(R.id.buttonBejelentkezes);
        textViewElfelejtettJelszo = loginPopupView.findViewById(R.id.textViewElfelejtettJelszo);
        textViewRegisztracio = loginPopupView.findViewById(R.id.textViewRegisztracio);
        progressBarBejelentkezes = loginPopupView.findViewById(R.id.progressBarBejelentkezes);

        dialogBuilder.setView(loginPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        textViewRegisztracio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("reg", "rákattintottál a regisztrációra");
                dialog.hide();

                AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(MainActivity.this);
                final View regisztracioPopupView = getLayoutInflater().inflate(R.layout.regisztraciopopup, null);
                Dialog dialog2;

                EditText editTextRegEmail, editTextRegJelszo, editTextRegJelszo2;
                Button buttonRegisztracio;
                ProgressBar progressBarRegisztracio;

                editTextRegEmail = regisztracioPopupView.findViewById(R.id.editTextRegEmail);
                editTextRegJelszo = regisztracioPopupView.findViewById(R.id.editTextRegJelszo);
                editTextRegJelszo2 = regisztracioPopupView.findViewById(R.id.editTextRegJelszo2);
                buttonRegisztracio = regisztracioPopupView.findViewById(R.id.buttonRegisztracio);
                progressBarRegisztracio = regisztracioPopupView.findViewById(R.id.progressBarRegisztracio);


                dialogBuilder2.setView(regisztracioPopupView);
                dialog2 = dialogBuilder2.create();
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setCancelable(false);
                dialog2.show();


                buttonRegisztracio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = editTextRegEmail.getText().toString().trim();
                        String jelszo = editTextRegJelszo.getText().toString().trim();
                        String jelszo2 = editTextRegJelszo2.getText().toString().trim();

                        if (email.isEmpty())
                        {
                            editTextRegEmail.setError("E-mail cím nem lehet üres!");
                            editTextRegEmail.requestFocus();
                            return;
                        }

                        if (jelszo.isEmpty())
                        {
                            editTextRegJelszo.setError("Jelszó nem lehet üres!");
                            editTextRegJelszo.requestFocus();
                            return;
                        }

                        if (!jelszo.equals(jelszo2))
                        {
                            editTextRegJelszo.setError("Két jelszó nem egyezik!");
                            editTextRegJelszo.requestFocus();
                            return;
                        }

                        if (jelszo.length() < 6)
                        {
                            editTextRegJelszo.setError("A jelszónak legalább 6 karakter hosszúnak kell lennie!");
                            editTextRegJelszo.requestFocus();
                            return;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        {
                            editTextRegEmail.setError("E-mail formátuma nem megfelelő!");
                            editTextRegEmail.requestFocus();
                            return;
                        }

                        progressBarRegisztracio.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, jelszo)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            Felhasznalo felhasznalo = new Felhasznalo(email, jelszo);
                                            FirebaseDatabase.getInstance().getReference("Felhasznalok")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(felhasznalo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(MainActivity.this, "Felhasználó sikeresen regisztrálva", Toast.LENGTH_LONG).show();
                                                        progressBarRegisztracio.setVisibility(View.GONE);
                                                        dialog2.dismiss();
                                                        dialog.show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(MainActivity.this, "Regisztráció nem sikerült!", Toast.LENGTH_LONG).show();
                                                        progressBarRegisztracio.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this, "Regisztráció nem sikerült!", Toast.LENGTH_LONG).show();
                                            progressBarRegisztracio.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                });
            }
        });

        buttonBejelentkezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextLoginEmail.getText().toString().trim();
                String jelszo = editTextLoginJelszo.getText().toString().trim();

                if (email.isEmpty())
                {
                    editTextLoginEmail.setError("E-mail cím nem lehet üres!");
                    editTextLoginEmail.requestFocus();
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextLoginEmail.setError("E-mail formátuma nem megfelelő!");
                    editTextLoginEmail.requestFocus();
                    return;
                }

                if (jelszo.isEmpty())
                {
                    editTextLoginJelszo.setError("Jelszó nem lehet üres!");
                    editTextLoginJelszo.requestFocus();
                    return;
                }

                progressBarBejelentkezes.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, jelszo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            progressBarBejelentkezes.setVisibility(View.GONE);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Bejelentkezés sikertelen!", Toast.LENGTH_LONG).show();
                            progressBarBejelentkezes.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}