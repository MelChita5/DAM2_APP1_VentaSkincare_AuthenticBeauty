package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        mAuth = FirebaseAuth.getInstance();

    }

    public void addToFirebase_BD(View view) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Map<String, Object> values = new HashMap<>();

        // Recopilamos datos ingresados
        TextView username = findViewById(R.id.RegisterName);
        TextView email = findViewById(R.id.RegisterEmail);
        TextView password = findViewById(R.id.RegisterPassword);
        TextView repeatPassword = findViewById(R.id.RegisterPassword2);

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String repeatPasswordText = repeatPassword.getText().toString();
        String emailText = email.getText().toString();

        // Validaciones

        // Si los campos estan vacios
        if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || repeatPasswordText.isEmpty()) {
            Toast.makeText(Register.this, "Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
            return;
        }

        // si las contraseñas son iguales
        if (!passwordText.equals(repeatPasswordText)) {
            Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }
        // que la contraseña sea mayor a 6 digitos
        if (passwordText.length() < 6) {
            Toast.makeText(Register.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_LONG).show();
            return;
        }
        // formato del email valido
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(Register.this, "El formato del email es inválido.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, task -> {
                    // SI es exitoso, guardamos los datos (map values) y se ingresan en Firebase users
                    if (task.isSuccessful()) {

                        values.put("name", username.getText().toString());
                        values.put("email", emailText);

                        database.collection("users").document(username.getText().toString())
                                .set(values)

                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(Register.this, "Usuario registrado exitosamente.", Toast.LENGTH_LONG).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(Register.this, "Error al ingresar datos", Toast.LENGTH_LONG).show();
                                });
                    } else {
                        // Si el correo ya esta registrado salta mensaje
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Register.this, "El correo ya está registrado.", Toast.LENGTH_LONG).show();
                        } else {
                            // Errores no registrados
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                            Toast.makeText(Register.this, "Error al crear el usuario: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public  void  changeToHome(View view) {
        startActivity(new Intent(Register.this, MainActivity.class));
    }

    public  void  changeToLogin(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }

}