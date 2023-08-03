package com.example.otp.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.otp.R;
import com.example.otp.activities.MenuActivity;
import com.example.otp.models.RumahMakan;
import com.example.otp.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RumahMakantAdapter extends RecyclerView.Adapter<RumahMakantAdapter.RumahMakanViewHolder> implements Filterable {

    private List<RumahMakan> mArrayList;
    private List<RumahMakan> mFilteredList;
    private Context mContext;
    SessionManager sessionManager;
    HashMap<String, String> loca;
    String strDistance;
    View view;

    public RumahMakantAdapter(Context context, List<RumahMakan> rumahMakanList) {
        this.mArrayList = rumahMakanList;
        this.mFilteredList = rumahMakanList;
        this.mContext = context;
        sessionManager = new SessionManager(mContext);
        loca = sessionManager.getLocation();
    }



    @NonNull
    @Override
    public RumahMakanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_restoran, parent, false);
        RumahMakanViewHolder holder = new RumahMakanViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RumahMakanViewHolder holder, final int position) {
        final RumahMakan data = mFilteredList.get(position);

       holder.txtNamaResto.setText(data.getRestoran_nama());
        if (data.getRestoran_delivery().equals("gratis")) {
            holder.txtTarifDelivery.setText("Gratis");
            holder.txtTarifDelivery.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.txtTarifDelivery.setText(kursIndonesia(Double.parseDouble(data.getRestoran_delivery_tarif())));
            holder.txtTarifDelivery.setTextColor(ContextCompat.getColor(mContext, R.color.textSub));
        }

        holder.txtMinimum.setText(kursIndonesia(Double.parseDouble(data.getRestoran_delivery_minimum())));
        holder.tvJumlahPesan.setText(data.getRestoran_delivery().toString() + " Pesanan");
        String Deskripsi = data.getRestoran_deskripsi().substring(0, 1).toUpperCase() + data.getRestoran_deskripsi().substring(1);
        holder.tvDeskripsi.setText(Deskripsi);
        holder.tvJarak.setText(satuan_jarak(data.getRestoran_distace()));

        oprasional(holder, data.getRestoran_oprasional());

        holder.imgRumahmakan.setImageResource(R.drawable.logo);


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Anda Memilih " + data.getRestoran_nama(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MenuActivity.class);
                intent.putExtra("Resto", data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<RumahMakan> filteredList = new ArrayList<>();

                    for (RumahMakan restoranverdion : mArrayList) {

                        if (restoranverdion.getRestoran_nama().toLowerCase().contains(charString)) {
                            filteredList.add(restoranverdion);
                        }

                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<RumahMakan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public class RumahMakanViewHolder extends RecyclerView.ViewHolder {


        TextView txtNamaResto;
        TextView txtTarifDelivery;
        TextView txtMinimum;
        LinearLayout mParentLayout;
        TextView tvJumlahPesan;
        TextView tvOptasional;
        TextView tvDeskripsi;
        TextView tvJarak;
        ImageView imgRumahmakan;

        public RumahMakanViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNamaResto = (TextView) itemView.findViewById(R.id.tvNamaResto);
            txtTarifDelivery = (TextView) itemView.findViewById(R.id.tvTarif);
            txtMinimum = (TextView) itemView.findViewById(R.id.tvMin);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
            tvJumlahPesan = (TextView) itemView.findViewById(R.id.tvJumlah_pesan);
            tvOptasional = (TextView) itemView.findViewById(R.id.tvOprasional);
            tvDeskripsi = (TextView) itemView.findViewById(R.id.tvDeskripsi_resto);
            tvJarak = (TextView) itemView.findViewById(R.id.tvJarak);
            imgRumahmakan = (ImageView) itemView.findViewById(R.id.imageRestoran);

        }
    }

    public void oprasional(RumahMakanViewHolder holder, Integer code) {

        if (code == 1) {
            holder.tvOptasional.setText("Buka");
            //holder.tvOptasional.setBackground(ContextCompat.getDrawable(mContext,R.drawable.rounded_corner_green));
            holder.tvOptasional.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            holder.tvOptasional.setText("Tutup");
            holder.tvOptasional.setBackgroundResource(R.drawable.rounded_corner_red);
        }
    }

    public void hitung_jarak(String lokasi_resto) {
        String[] locResto = lokasi_resto.split(",");
        double lat1 = Double.parseDouble(loca.get(SessionManager.LAT));
        double lng1 = Double.parseDouble(loca.get(SessionManager.LANG));
        double lat2 = Double.parseDouble(locResto[0]);
        double lng2 = Double.parseDouble(locResto[1]);
        Location asal = new Location("point A");
        asal.setLatitude(lat1);
        asal.setLongitude(lng1);
        Location tujuan = new Location("point B");
        tujuan.setLatitude(lat2);
        tujuan.setLongitude(lng2);

        double distance = (double) Math.floor(asal.distanceTo(tujuan) / 1000 * 100) / 100;

        if (distance < 1) {
            strDistance = String.valueOf(distance * 1000) + " m";
        } else {
            strDistance = String.valueOf(distance) + " km";
        }


    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    private String satuan_jarak(String jarak) {
        String jarakStr = null;
        if (Double.parseDouble(jarak) < 1) {
            jarakStr = Double.parseDouble(jarak) * 1000 + " m";
        } else {
            jarakStr = jarak + " Km";
        }
        return jarakStr;
    }
}
