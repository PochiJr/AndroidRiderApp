package com.example.android.androidriderapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.android.androidriderapp.Common.Common;
import com.example.android.androidriderapp.Model.Rider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    private final static int PERMISSION = 1000;


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Si da error copio to lo de aqui a attachBaseContext
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.rider_info_tbl);

        // Inicializar la View
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        // Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muestra la Cardview con todas las perrerías que hemos incluido en showRegisterDialog();
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muestra la Cardview con todas las perrerías que hemos incluido en showSignInDialog();
                showSignInDialog();
            }
        });

    }

    private void showSignInDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // Título de la Cardview
        dialog.setTitle("INICIAR SESIÓN");
        // Semitítulo de la Cardview
        dialog.setMessage("Introduzca su email para iniciar sesión");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        // Ponemos dos nuevos botones, uno para confirmar el inicio de sesión y otro para cancelarlo
        dialog.setPositiveButton("INICIAR SESIÓN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                // Hacemos que desaparezca el botón de iniciar sesión mientras esté cargando
                btnSignIn.setEnabled(false);

                // Comprobar validación (Ver si el usuario ha introducido los datos)
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Por favor, introduzca su email",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Por favor, introduzca su contraseña",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "La contraseña no puede contener menos de 6 " +
                            "caracteres", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                // Añadimos los puntos giratorios de carga
                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();


                // Iniciar sesión
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),
                        edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Hacemos desaparecer los puntitos giratorios
                                waitingDialog.dismiss();

                                startActivity(new Intent(MainActivity.this,Home.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hacemos desaparecer los puntitos giratorios
                                waitingDialog.dismiss();

                                Snackbar.make(rootLayout, "Fallo al iniciar sesión" + e.getMessage(),
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                                // Activamos de nuevo el botón si la carga es fallida
                                btnSignIn.setEnabled(true);
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        dialog.show();

    }

    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // Título de la Cardiew
        dialog.setTitle("REGISTRARSE");
        // Semitítulo de la Cardview
        dialog.setMessage("Introduzca su email para registrarse");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        // Ponemos el mensaje previo en la Cardview
        dialog.setView(register_layout);

        // Ponemos dos nuevos botones, uno para confirmar el registro y otro para cancelarlo
        dialog.setPositiveButton("REGISTRARSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                // Comprobar validación (Ver si el usuario ha introducido los datos)
                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Por favor, introduzca su email",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Por favor, introduzca su contraseña",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (edtPassword.getText().toString().length() < 6){
                    Snackbar.make(rootLayout,"La contraseña no puede contener menos de 6 " +
                            "caracteres", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())){
                    Snackbar.make(rootLayout,"Por favor, introduzca su teléfono",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                // Registrar a un nuevo usuario
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),
                        edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Guardar y crear usuario en la base de datos (db)
                                Rider rider = new Rider();
                                rider.setEmail(edtEmail.getText().toString());
                                rider.setPassword(edtPassword.getText().toString());
                                rider.setName(edtName.getText().toString());
                                rider.setPhone(edtPhone.getText().toString());

                                // Usamos el UID (User ID) como key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(rider)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Snackbar.make(rootLayout,"Se ha registrado con éxito",
                                                        Snackbar.LENGTH_SHORT)
                                                        .show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout,"Ha ocurrido un fallo durante su registro "+ e.getMessage(),
                                                        Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Ha ocurrido un fallo durante su registro "+ e.getMessage(),
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }
}
