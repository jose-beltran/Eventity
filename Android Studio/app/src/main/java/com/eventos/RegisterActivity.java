package com.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static final String URL_REGISTRO = "http://192.168.0.7:8080/user/register";
    private EditText editTextName, editTextEmail, editTextLocation, editTextPassword;
    private Button buttonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.username_input);
        editTextEmail = findViewById(R.id.email_input);
        editTextLocation = findViewById(R.id.location_input);
        editTextPassword = findViewById(R.id.password_input);
        buttonRegistro = findViewById(R.id.register_btn);

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || location.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        User usuario = new User(name, email, location, password);
        enviarRegistro(usuario);
    }

    private void enviarRegistro(User usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Gson gson = new Gson();
        String jsonString = gson.toJson(usuario);

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_REGISTRO, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                                    // Redirigir a LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish(); // Finalizar la actividad actual
                                } else {
                                    String mensaje = response.getString("message");
                                    Toast.makeText(RegisterActivity.this, "Error: " + mensaje, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null) {
                                String errorMsg = new String(error.networkResponse.data);
                                Toast.makeText(RegisterActivity.this, "Error de red: " + errorMsg, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear el objeto JSON", Toast.LENGTH_SHORT).show();
        }
    }
}