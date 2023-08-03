package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyScanManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.adapter.DetailOrderAdapter;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.Menu;
import com.example.otp.models.Order;
import com.example.otp.response.ResponseValue;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;
import com.example.otp.utils.NonScrollListView;
import com.example.otp.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {

    private List<Menu> detailOrders = new ArrayList<>();
    private DetailOrderAdapter orderAdapter;
    private Order pesan;
    ApiService mApiService;
    Context mContext;
    ProgressDialog progressOrder;


    NonScrollListView list;
    TextView mNamaKonsumen;
    TextView mPhoneKonsumen;
    TextView mAlamat;
    TextView mSubTotal;
    TextView mBiayaAntar;
    TextView mTotal;
    TextView mMetodeBayar;
    TextView btnInvoice;
    LinearLayout layoutCatatn;
    TextView mCatatan;
    LinearLayout layoutStatus;

    TextView statusSelesai;
    TextView statusBatal;
    TextView statusPengantaran;
    TextView statusProses;
    TextView statusMenunggu;
    TextView statusAntrian;


    View viewpb1;
    LinearLayout lyt_pb1;
    TextView pajak_pb1;
    TextView tvPajakPB1;

    SessionManager sessionManager;

    ProgressDialog progressDialog, progressSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        list = (NonScrollListView) findViewById(R.id.listview);
        mNamaKonsumen = (TextView) findViewById(R.id.tvNamaKonsumen);
        mPhoneKonsumen = (TextView) findViewById(R.id.tvPhoneKonsumen);
        mAlamat = (TextView) findViewById(R.id.tvAlamatAntar);
        mSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        mBiayaAntar = (TextView) findViewById(R.id.tvBiayaAntar);
        mTotal = (TextView) findViewById(R.id.tvTotal);
        mMetodeBayar = (TextView) findViewById(R.id.tvMetodeBayar);
        btnInvoice = (TextView) findViewById(R.id.btnInvoice);
        layoutCatatn = (LinearLayout) findViewById(R.id.layoutCatatan);
        mCatatan = (TextView) findViewById(R.id.catatan);
        layoutStatus = (LinearLayout) findViewById(R.id.layoutStatus);

        statusSelesai = (TextView) findViewById(R.id.tvStatusSelesai);
        statusBatal = (TextView) findViewById(R.id.tvStatusBatal);
        statusPengantaran = (TextView) findViewById(R.id.tvStatusPengantaran);
        statusProses = (TextView) findViewById(R.id.tvStatusProses);
        statusMenunggu = (TextView) findViewById(R.id.tvStatusMenunggu);
        statusAntrian = (TextView) findViewById(R.id.tvStatusAntrian);

        viewpb1 = (View) findViewById(R.id.viewpb1);
        lyt_pb1 = (LinearLayout) findViewById(R.id.lyt_pb1);
        pajak_pb1 = (TextView) findViewById(R.id.pajak_pb1);
        tvPajakPB1 = (TextView) findViewById(R.id.tvPajakPB1);

        mContext = this;
        sessionManager = new SessionManager(mContext);
        mApiService = ServerConfig.getAPIService();
        setListViewHeightBasedOnChildren(list);
        getIncomingIntent();
        orderAdapter = new DetailOrderAdapter(DetailOrderActivity.this, detailOrders);
        list.setAdapter(orderAdapter);

        setData();

        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressSendEmail = ProgressDialog.show(mContext, null, "Mengirim ke email...", true, false);
                sendEmail(pesan.getId());
            }
        });


    }



    private void setData() {
        String id_order = pesan.getId().toString();
        mNamaKonsumen.setText(pesan.getOrderKonsumen());
        mPhoneKonsumen.setText("+" + pesan.getOrderKonsumenPhone());
        mAlamat.setText(pesan.getOrderAlamat());
        Double subtotal = 0.0;
        for (int i = 0; i < detailOrders.size(); i++) {
            subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();
//            if (detailOrders.get(i).getMenuDiscount() == 0 || detailOrders.get(i).getMenuDiscount().toString().isEmpty()) {
//                subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();
//            } else {
//                Double harga_discount = HitungDiscount(Double.parseDouble(detailOrders.get(i).getPivot().getHarga()), detailOrders.get(i).getPivot().getDiscount());
//                subtotal += harga_discount * detailOrders.get(i).getPivot().getQty();
//            }
        }
        mSubTotal.setText(kursIndonesia(subtotal));
        mBiayaAntar.setText(kursIndonesia(Double.parseDouble(pesan.getOrderBiayaAnatar())));
        double total = subtotal + Double.parseDouble(pesan.getOrderBiayaAnatar());

        //cek pajak
//        if (pesan.getOrder_pajak_pb_satu() == 0) {
//            viewpb1.setVisibility(View.GONE);
//            lyt_pb1.setVisibility(View.GONE);
//        } else {
//            viewpb1.setVisibility(View.VISIBLE);
//            lyt_pb1.setVisibility(View.VISIBLE);
//            pajak_pb1.setText("PB1 (" + pesan.getOrder_pajak_pb_satu() + "%)");
//            double pb1 = (pesan.getOrder_pajak_pb_satu() / 100.0) * total;
//            tvPajakPB1.setText(kursIndonesia(pb1));
//            total = total + pb1;
//        }

        mTotal.setText(kursIndonesia(total));
        mMetodeBayar.setText(pesan.getOrderMetodeBayar());
        //catatan
        if (pesan.getOrderCatatan() != null) {
            layoutCatatn.setVisibility(View.VISIBLE);
            mCatatan.setText(pesan.getOrderCatatan().toString());

        }

        if (pesan.getOrderStatus().equalsIgnoreCase("selesai")) {
            statusSelesai.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.VISIBLE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("batal")) {
            statusBatal.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("pengantaran")) {
            statusPengantaran.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        } else if (pesan.getOrderStatus().equalsIgnoreCase("proses")) {
            statusProses.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        }else if (pesan.getOrderStatus().equalsIgnoreCase("antrian")) {
            statusAntrian.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        }else if (pesan.getOrderStatus().equalsIgnoreCase("menunggu")) {
            statusMenunggu.setVisibility(View.VISIBLE);
            btnInvoice.setVisibility(View.GONE);
        }
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("pesan")) {


            pesan = (Order) getIntent().getSerializableExtra("pesan");
            detailOrders = pesan.getDetailOrder();

        }
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //konfersi ke mata uang rupiah
    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;


    }

    public Double HitungDiscount(Double Harga, Integer Discount) {
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }

    private void sendEmail(Integer id_order) {
        mApiService.sendEmail(id_order).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressSendEmail.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getValue().equals("1")) {
                        Toast.makeText(mContext, "Berhasil,Cek Email Anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressSendEmail.dismiss();
            }
        });
    }

}