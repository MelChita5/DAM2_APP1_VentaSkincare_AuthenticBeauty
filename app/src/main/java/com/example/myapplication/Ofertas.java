package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ofertas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ofertas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ofertas1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public  void  changeToHome(View view) {
        startActivity(new Intent(Ofertas.this, MainActivity.class));
    }

    public  void  changeToCarrito(View view) {
        startActivity(new Intent(Ofertas.this, Carrito.class));
    }

    public  void  changeToLogin(View view) {
        startActivity(new Intent(Ofertas.this, Login.class));
    }
}