package com.example.fizetsihatridfigyelmeztetalkalmazs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;

    AlertDialog.Builder dialogBuilder;
    Dialog dialog;

    DataBaseHelper dataBaseHelper;
    List<String> listBeallitasok = new ArrayList<>();

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
        dataBaseHelper = new DataBaseHelper(MainActivity.this);

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

//        Bitmap map=takeScreenShot(new KezdolapFragment().getActivity());
//        Bitmap fast=fastblur(map, 10);
//        final Drawable draw=new BitmapDrawable(getResources(),fast);
//        dialog.getWindow().setBackgroundDrawable(draw);

        dialog.show();

        textViewRegisztracio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("reg", "rákattintottál a regisztrációra");
                dialog.cancel();

                AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(MainActivity.this);
                final View regisztracioPopupView = getLayoutInflater().inflate(R.layout.regisztraciopopup, null);
                Dialog dialog2;

                EditText editTextRegEmail, editTextRegJelszo, editTextRegJelszo2;
                Button buttonRegisztracio;
                ProgressBar progressBarRegisztracio;
                ImageView imageViewVissza;

                editTextRegEmail = regisztracioPopupView.findViewById(R.id.editTextRegEmail);
                editTextRegJelszo = regisztracioPopupView.findViewById(R.id.editTextRegJelszo);
                editTextRegJelszo2 = regisztracioPopupView.findViewById(R.id.editTextRegJelszo2);
                buttonRegisztracio = regisztracioPopupView.findViewById(R.id.buttonRegisztracio);
                progressBarRegisztracio = regisztracioPopupView.findViewById(R.id.progressBarRegisztracio);
                imageViewVissza = regisztracioPopupView.findViewById(R.id.imageViewVissza);


                dialogBuilder2.setView(regisztracioPopupView);
                dialog2 = dialogBuilder2.create();
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setCancelable(false);
                dialog2.show();


                imageViewVissza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.cancel();
                        dialog.show();
                    }
                });

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
                            editTextRegEmail.setError("E-mail formátum nem megfelelő!");
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
                                                        Toast.makeText(MainActivity.this, "Felhasználó sikeresen regisztrálva.", Toast.LENGTH_LONG).show();
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

        textViewElfelejtettJelszo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextLoginEmail.getText().toString().trim();

                if (email.isEmpty())
                {
                    editTextLoginEmail.setError("Kérem írja be az e-mail címét!");
                    editTextLoginEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextLoginEmail.setError("E-mail formátum nem megfelelő!");
                    editTextLoginEmail.requestFocus();
                    return;
                }

                progressBarBejelentkezes.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Jelszó visszaállító email elküldve!", Toast.LENGTH_LONG).show();
                            progressBarBejelentkezes.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Hiba történt, próbálja újra!", Toast.LENGTH_LONG).show();
                            progressBarBejelentkezes.setVisibility(View.INVISIBLE);
                        }
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
                    return;
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified())
                            {
                                Toast.makeText(MainActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                progressBarBejelentkezes.setVisibility(View.GONE);
                                listBeallitasok = dataBaseHelper.AdatbazisbolBeallitasokLekerese(mAuth.getCurrentUser().getEmail());
                                if (listBeallitasok.size() == 0) {
                                    dataBaseHelper.AlapBeallitasokHozzaadasa();
                                }
                            }
                            else
                            {
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Erősítse meg az e-mail címét!", Toast.LENGTH_LONG).show();
                                progressBarBejelentkezes.setVisibility(View.GONE);
                            }
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

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();


        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

}