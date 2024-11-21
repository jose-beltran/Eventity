package com.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private EditText editTextName, editTextEmail;
    private Button btnSave;
    private RequestQueue requestQueue;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnMenu = findViewById(R.id.btnMenu);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSave = findViewById(R.id.btnSave);

        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        if (token == null) {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(UserProfileActivity.this, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.action_logout) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.remove("name");
                                editor.apply();

                                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            } else if (id == R.id.action_profile) {
                                Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_ciudades) {
                                Intent intent = new Intent(UserProfileActivity.this, CiudadesActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_eventos) {
                                Intent intent = new Intent(UserProfileActivity.this, EventosActivity.class);
                                startActivity(intent);
                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            cargarInformacionUsuario();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarInformacionUsuario();
            }
        });
    }

    private void cargarInformacionUsuario() {
        String url = "http://192.168.0.7:8080/user/profile";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            String email = response.getString("email");

                            editTextName.setText(name);
                            editTextEmail.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this, "Error al cargar la información del usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            // Redirigir al login si el token ha expirado
                            Toast.makeText(UserProfileActivity.this, "Token expirado. Por favor, inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Error al cargar la información del usuario: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void actualizarInformacionUsuario() {
        String url = "http://192.168.0.7:8080/user/profile";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", editTextName.getText().toString());
            jsonObject.put("email", editTextEmail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UserProfileActivity.this, "Información actualizada exitosamente", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserProfileActivity.this, "Error al actualizar la información: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}