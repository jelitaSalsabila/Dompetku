package com.example.rplrus10.dompetku;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<keluar> implements View.OnClickListener {
    private ArrayList<keluar> arraylist;
    Context mContext;
    @Override
    public void onClick(View v) {
    }
    static class ViewHolder {
        TextView tanggal , keluar,keterangan;
    }
    public CustomAdapter(ArrayList<keluar> arraylist, Context context) {
        super(context, R.layout.row_item, arraylist);
        this.arraylist = arraylist;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        keluar keluar = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.tanggal = (TextView) convertView.findViewById(R.id.tanggal);
            viewHolder.keluar = (TextView) convertView.findViewById(R.id.keluar);
            viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);


            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.tanggal.setText(keluar.getTanggal());
        viewHolder.keluar.setText(keluar.getKeluar());
        viewHolder.keterangan.setText(keluar.getKeterangan());
        return convertView;
    }
}