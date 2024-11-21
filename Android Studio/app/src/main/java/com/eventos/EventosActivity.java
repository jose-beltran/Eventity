package com.eventos;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity {
    private ImageButton btnMenu;
    private ListView listViewEventos;
    private EventoAdapter eventoAdapter;
    private RequestQueue requestQueue;
    private Button btnPublicarEvento;
    private Button btnRefreshEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        btnMenu = findViewById(R.id.btnMenu);
        listViewEventos = findViewById(R.id.listViewEventos);
        btnPublicarEvento = findViewById(R.id.btnAddEvent);
        btnRefreshEvents = findViewById(R.id.btnRefreshEvents);

        requestQueue = Volley.newRequestQueue(this);

        // Verificar el token JWT
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            // Redirigir al login si no hay token
            Intent intent = new Intent(EventosActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(EventosActivity.this, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.action_logout) {
                                // Cerrar sesión
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("token");
                                editor.remove("name");
                                editor.apply();

                                // Redirigir al login
                                Intent intent = new Intent(EventosActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            } else if (id == R.id.action_profile) {
                                // Navegar a UserProfileActivity
                                Intent intent = new Intent(EventosActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_ciudades) {
                                // Navegar a CiudadesActivity
                                Intent intent = new Intent(EventosActivity.this, CiudadesActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_eventos) {
                                // Navegar a EventosActivity
                                Intent intent = new Intent(EventosActivity.this, EventosActivity.class);
                                startActivity(intent);
                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        cargarEventos();
        btnPublicarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarEvento();
            }
        });

        btnRefreshEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarEventos();
            }
        });
    }

    private void cargarEventos() {
        String url = "http://192.168.0.7:8080/eventos";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("EventosActivity", "Respuesta recibida: " + response.toString());
                        List<Evento> eventos = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                String ciudad = jsonObject.getString("ciudad");
                                String precio = jsonObject.getString("precio");
                                String direccion = jsonObject.getString("direccion");
                                String imagenurl = jsonObject.getString("imagenurl");
                                eventos.add(new Evento(nombre, ciudad, precio, direccion, imagenurl));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("EventosActivity", "Error al parsear JSON: " + e.getMessage());
                            }
                        }
                        if (eventos.isEmpty()) {
                            Toast.makeText(EventosActivity.this, "No se encontraron eventos.", Toast.LENGTH_SHORT).show();
                        }
                        eventoAdapter = new EventoAdapter(EventosActivity.this, eventos);
                        listViewEventos.setAdapter(eventoAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventosActivity.this, "Error al cargar eventos: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("VolleyError", error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void publicarEvento() {
        // Inflar el layout del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_event, null);

        // Obtener referencias a los elementos del diálogo
        TextInputEditText editTextNombreEvento = dialogView.findViewById(R.id.editTextNombreEvento);
        TextInputEditText editTextCiudadEvento = dialogView.findViewById(R.id.editTextCiudadEvento);
        TextInputEditText editTextPrecioEvento = dialogView.findViewById(R.id.editTextPrecioEvento);
        TextInputEditText editTextDireccionEvento = dialogView.findViewById(R.id.editTextDireccionEvento);
        TextInputEditText editTextImagenurlEvento = dialogView.findViewById(R.id.editTextImagenurlEvento);
        MaterialButton btnAddEvent = dialogView.findViewById(R.id.btnPostEvent);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EventosActivity.this);
        dialogBuilder.setView(dialogView);

        AlertDialog dialog = dialogBuilder.create();

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener los datos del formulario
                String nombreEvento = editTextNombreEvento.getText().toString().trim();
                String ciudadEvento = editTextCiudadEvento.getText().toString().trim();
                String precioEvento = editTextPrecioEvento.getText().toString().trim();
                String direccionEvento = editTextDireccionEvento.getText().toString().trim();
                String imagenurlEvento = editTextImagenurlEvento.getText().toString().trim();

                if (nombreEvento.isEmpty() || ciudadEvento.isEmpty() || precioEvento.isEmpty() ||
                        direccionEvento.isEmpty() || imagenurlEvento.isEmpty()) {
                    Toast.makeText(EventosActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://192.168.0.7:8080/eventos";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nombre", nombreEvento);
                    jsonObject.put("ciudad", ciudadEvento);
                    jsonObject.put("precio", precioEvento);
                    jsonObject.put("direccion", direccionEvento);
                    jsonObject.put("imagenurl", imagenurlEvento);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Crear la solicitud POST con JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(EventosActivity.this, "Evento añadido exitosamente!", Toast.LENGTH_SHORT).show();
                                cargarEventos();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String errorMessage = "Error al añadir el evento.";
                                if (error.networkResponse != null) {
                                    errorMessage += " Código: " + error.networkResponse.statusCode;
                                    try {
                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                        JSONObject data = new JSONObject(responseBody);
                                        errorMessage += ", Mensaje: " + data.optString("message");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast.makeText(EventosActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.e("VolleyError", error.toString());
                            }
                        });

                requestQueue.add(jsonObjectRequest);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}