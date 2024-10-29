package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
    }

    // Cuando la actividad se muestra a la pantalla
    public void onStart() {
        super.onStart();
        // verifica la concectividad del usuario
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // si hay un usario conectado, se recarga
        if(currentUser != null){
            reload();
        }
    }

    public void loginUser(View view) {
        // Obtenemos los valores
        EditText email = findViewById(R.id.ingresarEmail);
        EditText password = findViewById(R.id.ingresarPassword);
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        // Verifica si los campos estan vacios
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(Login.this, "Por favor, ingrese ambos campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Login","Email: " + emailText);

        // Utilizo en Firebase el Authentication por la proteccion de datos
        // Usando el email y contraseña
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // si es exitoso obtenemos el usuario desde Firebase
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // si falla muestra mensaje de error, y registra el error
                            String errorMessage = "La autenticación falló. Verifique sus datos.";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("LoginError", "Error al iniciar sesión", task.getException());
                            // no se cambia de pantalla
                            updateUI(null);
                        }
                    }
                });



    }

    private void updateUI(FirebaseUser user) {
        // Dirige a la pantalla principal
        if (user != null) {
            // Redirigir a otra actividad si el login es exitoso, por ejemplo:
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            // cierra la actividad actual
            finish();
        }
    }

    // recarga la actividad
    private void reload() { }

    public  void  changeToHome(View view) {
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    public  void  changeToRegister(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}

