package com.eventos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CiudadAdapter extends ArrayAdapter<City> {
    private Context context;
    private List<City> ciudades;

    public CiudadAdapter(Context context, List<City> ciudades) {
        super(context, 0, ciudades);
        this.context = context;
        this.ciudades = ciudades;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City ciudad = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_city, parent, false);
        }

        TextView tvDepartamento = convertView.findViewById(R.id.tvDepartamento);
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        ImageView ivCiudad = convertView.findViewById(R.id.ivCiudad);

        tvDepartamento.setText(ciudad.getDepartamento());
        tvNombre.setText(ciudad.getCiudad());
        Picasso.get().load(ciudad.getImagenUrl()).into(ivCiudad);

        return convertView;
    }
}