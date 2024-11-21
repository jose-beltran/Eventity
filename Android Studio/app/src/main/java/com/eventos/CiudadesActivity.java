package com.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CiudadesActivity extends AppCompatActivity {
    private ImageButton btnMenu;
    private ListView listViewCiudades;
    private CiudadAdapter ciudadAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        btnMenu = findViewById(R.id.btnMenu);
        listViewCiudades = findViewById(R.id.listViewCiudades);
        requestQueue = Volley.newRequestQueue(this);

        // Verificar el token JWT
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            // Redirigir al login si no hay token
            Intent intent = new Intent(CiudadesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(CiudadesActivity.this, v);
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

                                Intent intent = new Intent(CiudadesActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            } else if (id == R.id.action_profile) {
                                Intent intent = new Intent(CiudadesActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_ciudades) {
                                Intent intent = new Intent(CiudadesActivity.this, CiudadesActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_eventos) {
                                Intent intent = new Intent(CiudadesActivity.this, EventosActivity.class);
                                startActivity(intent);
                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            cargarCiudades(token);
        }
    }

    private void cargarCiudades(String token) {
        String url = "http://192.168.0.7:8080/ciudades";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<City> ciudades = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String departamento = jsonObject.getString("departamento");
                                String ciudad = jsonObject.getString("ciudad");
                                String imagenUrl = jsonObject.getString("imagenurl");
                                ciudades.add(new City(departamento, ciudad, imagenUrl));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ciudadAdapter = new CiudadAdapter(CiudadesActivity.this, ciudades);
                        listViewCiudades.setAdapter(ciudadAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            // Redirigir al login si el token ha expirado
                            Toast.makeText(CiudadesActivity.this, "Token expirado. Por favor, inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CiudadesActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CiudadesActivity.this, "Error al cargar ciudades: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
        requestQueue.add(jsonArrayRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popup, menu);
        return true;
    }
}