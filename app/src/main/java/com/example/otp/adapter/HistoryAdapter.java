package com.example.otp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otp.R;
import com.example.otp.activities.DetailOrderActivity;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.Menu;
import com.example.otp.models.Order;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<Order> pesanList;
    private List<Menu> detailpesanList;
    ApiService mApiService;


    public HistoryAdapter(Context ctx, List<Order> pesanList) {
        this.pesanList = pesanList;
        this.mContext = ctx;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_order, parent, false);
        HistoryAdapter.HistoryViewHolder holder = new HistoryAdapter.HistoryViewHolder(view);
        mApiService = ServerConfig.getAPIService();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        final Order pesan = pesanList.get(position);
        holder.mNoOrder.setText("Order :" + " #" + pesan.getId());
        holder.mNamaResto.setText(pesan.getOrderRestoran());
        // String status = pesan.getPesanStatus().substring(0,1).toUpperCase() + pesan.getPesanStatus().substring(1).toLowerCase();
        holder.mStatus.setText(pesan.getOrderStatus());
        holder.mTanggal.setText(pesan.getCreatedAt().substring(0, 16));
        detailpesanList = pesan.getDetailOrder();
        double total = 0;
        for (int i = 0; i < detailpesanList.size(); i++) {
            total += Double.parseDouble(detailpesanList.get(i).getPivot().getHarga()) * detailpesanList.get(i).getPivot().getQty();
        }

        holder.mTotal.setText(kursIndonesia(total + Double.parseDouble(pesan.getOrderBiayaAnatar())));


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Anda memilih " + pesan.getOrderKonsumen(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailOrderActivity.class);
                intent.putExtra("pesan", (Serializable) pesan);

                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return pesanList.size();
    }

    //convert time string to milis date
    public long timeStringToMilis(String strDate) {

        SimpleDateFormat tgl = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        long milliseconds = 0;
        Date date = null;
        try {
            date = tgl.parse(strDate);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView mNamaResto;
        TextView mStatus;
        TextView mTanggal;
        LinearLayout mParentLayout;
        TextView mTotal;
        TextView mNoOrder;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mNamaResto = (TextView) itemView.findViewById(R.id.tvNamaResto);
            mStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            mTanggal = (TextView) itemView.findViewById(R.id.tvWaktu);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
            mTotal = (TextView) itemView.findViewById(R.id.tvTotal);
            mNoOrder = (TextView) itemView.findViewById(R.id.tvNoOrder);
        }
    }
}
