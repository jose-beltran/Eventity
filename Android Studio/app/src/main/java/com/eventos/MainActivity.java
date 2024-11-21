package com.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnMenu;
    private WebView webViewVideo1;
    private WebView webViewVideo2;
    private WebView webViewVideo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMenu = findViewById(R.id.btnMenu);
        webViewVideo1 = findViewById(R.id.webViewVideo1);
        webViewVideo2 = findViewById(R.id.webViewVideo2);
        webViewVideo3 = findViewById(R.id.webViewVideo3);

        configurarWebView(webViewVideo1, "https://www.youtube.com/watch?v=bcCFlKcD2qI&t=8020s");
        configurarWebView(webViewVideo2, "https://www.youtube.com/watch?v=7L9hhPVpYkw");
        configurarWebView(webViewVideo3, "https://www.youtube.com/embed/VIDEO_ID_3");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String name = sharedPreferences.getString("name", "Usuario");

        if (token == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnMenu);
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

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            } else if (id == R.id.action_profile) {
                                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_ciudades) {
                                Intent intent = new Intent(MainActivity.this, CiudadesActivity.class);
                                startActivity(intent);
                                return true;
                            } else if (id == R.id.action_eventos) {
                                Intent intent = new Intent(MainActivity.this, EventosActivity.class);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popup, menu);
        return true;
    }

    private void configurarWebView(WebView webView, String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webViewVideo1.canGoBack()) {
            webViewVideo1.goBack();
        } else if (webViewVideo2.canGoBack()) {
            webViewVideo2.goBack();
        } else if (webViewVideo3.canGoBack()) {
            webViewVideo3.goBack();
        } else {
            super.onBackPressed();
        }
    }
}