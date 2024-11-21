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

public class EventoAdapter extends ArrayAdapter<Evento> {
    private Context context;
    private List<Evento> eventos;

    public EventoAdapter(Context context, List<Evento> eventos) {
        super(context, 0, eventos);
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento evento = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvCiudad = convertView.findViewById(R.id.tvCiudad);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecio);
        TextView tvDireccion = convertView.findViewById(R.id.tvDireccion);
        ImageView ivEvento = convertView.findViewById(R.id.ivEvento);

        tvNombre.setText(evento.getNombre());
        tvCiudad.setText(evento.getCiudad());
        tvPrecio.setText(evento.getPrecio());
        tvDireccion.setText(evento.getDireccion());
        Picasso.get().load(evento.getImagenurl()).into(ivEvento);

        return convertView;
    }
}
