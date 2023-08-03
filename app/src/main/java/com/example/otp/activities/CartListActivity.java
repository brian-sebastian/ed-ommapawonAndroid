package com.example.otp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.adapter.CartAdapter;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.CartList;
import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;
import com.example.otp.response.ResponseOneRumahMakan;
import com.example.otp.response.ResponseValue;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.DatabaseHelper;
import com.example.otp.utils.NonScrollListView;
import com.example.otp.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity implements CartAdapter.Clicklistener{

    private static final String TAG = "Cart List";

    DatabaseHelper myDb;
    Context mContext;
    SessionManager sessionManager;
    ApiService mApiService;
    ProgressDialog progressDialog, progressOrder, progressResto;

    NonScrollListView list;
    ScrollView scCart;
    TextView mSubTotal;
    TextView mBiayaAntar;
    TextView mTotal;
    TextView mNamaKonsumen;
    TextView mPhoneKonsumen;
    TextView mAlamatAntar;
    EditText mCatatanAlamat;
    TextView btnOrder;
    RadioGroup mMetodeBayar;
    RadioButton cashButton;
    RadioButton mBtnTransfer;
    TextView mBtnCatatan;
    TextView mSaldo;
    View message;
    ImageView img_msg;
    TextView title_msg;
    TextView sub_title_msg;
    ImageView btnBack;
    ImageView btnResto;
    View viewpb1;
    LinearLayout lyt_pb1;
    TextView pajak_pb1;
    TextView tvPajakPB1;

    private ArrayList<CartList> cartList;
    private ArrayList<CartList> cartListTemp;
    private List<Menu> menuList = new ArrayList<>();
    CartAdapter.Clicklistener listener;
    private CartAdapter adapter;
    RumahMakan resto;

    double SubTotal, Total;
    HashMap<String, String> user, location;
    String konsumen_id, konsumen_nama, konsumen_phone, id_resto, tempMsgTarifAntar;
    Float hargaFloat;
    int jmlMenuTidakTersedia = 0, jmlCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        list = (NonScrollListView) findViewById(R.id.listview);
        scCart = (ScrollView) findViewById(R.id.scCart);
        mSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        mBiayaAntar = (TextView) findViewById(R.id.tvBiayaAntar);
        mTotal = (TextView) findViewById(R.id.tvTotal);
        mNamaKonsumen = (TextView) findViewById(R.id.tvNamaKonsumen);
        mPhoneKonsumen = (TextView) findViewById(R.id.tvPhoneKonsumen);
        mAlamatAntar = (TextView) findViewById(R.id.tvAlamatAntar);
        mCatatanAlamat = (EditText) findViewById(R.id.etCatatanAlamat);
        btnOrder = (TextView) findViewById(R.id.btnOrder);
        mMetodeBayar = (RadioGroup) findViewById(R.id.rgMetodeBayar);
        cashButton = (RadioButton) findViewById(R.id.rbCash);
        mBtnTransfer = (RadioButton) findViewById(R.id.rbTransfer);
        mBtnCatatan = (TextView) findViewById(R.id.btnCatatan);
        message = (View) findViewById(R.id.error);
        img_msg = (ImageView) findViewById(R.id.img_msg);
        title_msg = (TextView) findViewById(R.id.title_msg);
        sub_title_msg = (TextView) findViewById(R.id.sub_title_msg);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnResto = (ImageView) findViewById(R.id.btnResto);
        viewpb1 = (View) findViewById(R.id.viewpb1);
        lyt_pb1 = (LinearLayout) findViewById(R.id.lyt_pb1);
        pajak_pb1 = (TextView) findViewById(R.id.pajak_pb1);
        tvPajakPB1 = (TextView) findViewById(R.id.tvPajakPB1);

        mContext = this;
        mApiService = ServerConfig.getAPIService();
        myDb = new DatabaseHelper(mContext);
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        location = sessionManager.getLocation();
        progressDialog = new ProgressDialog(mContext);


        setListViewHeightBasedOnChildren(list);
        cartList = new ArrayList<CartList>();
        cartListTemp = new ArrayList<CartList>();
        listener = this;
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        cekCartList();

        //Button Resto on Click
        btnResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressResto = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                if (jmlCart == 0) {
                    progressResto.dismiss();
                    Toast.makeText(mContext, "Anda Tidak Memiliki Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + id_resto, Toast.LENGTH_SHORT).show();
                    mApiService.getRestoranByID(id_resto).enqueue(new Callback<ResponseOneRumahMakan>() {
                        @Override
                        public void onResponse(Call<ResponseOneRumahMakan> call, Response<ResponseOneRumahMakan> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getValue().equals("1")) {
                                    RumahMakan restoran = response.body().getData();
                                    progressResto.dismiss();
                                    Intent intent = new Intent(mContext, MenuActivity.class);
                                    intent.putExtra("Resto", restoran);
                                    mContext.startActivity(intent);
                                } else {
                                    progressResto.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseOneRumahMakan> call, Throwable t) {
                            progressResto.dismiss();

                        }
                    });
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //btn cttn click
        mBtnCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnCatatan.setVisibility(View.GONE);
                mCatatanAlamat.setVisibility(View.VISIBLE);
            }
        });
        //Button order click
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Sub total =" + String.valueOf(SubTotal) + "Total = " + String.valueOf(Total), Toast.LENGTH_SHORT).show();

                    alertKonfirmasi();
                }
            });
    }

    //get data cart from sqlite
    private void cekCartList() {
        //Mengambil data dari Sqlite
        Cursor res = myDb.getAllCart();
        jmlCart = res.getCount();
        //kondisi tidak ada cart
        if (jmlCart == 0) {
           progressDialog.dismiss();
            emptyCart();
            Toast.makeText(mContext, "anda tidak memiliki cart", Toast.LENGTH_SHORT).show();
            //kondisi ada cart
        } else {
            // res.moveToFirst();//Memulai Cursor pada Posisi Awal
            Toast.makeText(mContext, "anda memiliki cart", Toast.LENGTH_SHORT).show();
            while (res.moveToNext()) {

                Integer id = Integer.valueOf(res.getString(0));
                id_resto = res.getString(1);
                String id_menu = res.getString(2);
                String harga = (res.getString(3));
                Integer qty = Integer.valueOf(res.getString(4));
                String catatan = res.getString(5);
                String nama_resto = res.getString(6);

                CartList temp = new CartList(id, id_resto, id_menu, harga, qty, catatan, nama_resto, "", 0 );
                cartListTemp.add(temp);
            }
            get_restoran();


        }
    }


    @Override
    public void itemDeleted(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        SubTotal = SubTotal - (Float.parseFloat(cart.getHarga()) * cart.getQty());


        int msg = myDb.deleteCart(cart.getId());
        adapter.removeAt(position);
        //cartList.remove(position);
        SetData();

        if (cartList.size() == 0) {
            message.setVisibility(View.VISIBLE);
            img_msg.setImageResource(R.drawable.msg_cart);
            title_msg.setText("Ayo Pesan Makanan Anda Sekarang");
            sub_title_msg.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesaan Anda \n");
            scCart.setVisibility(View.GONE);
        }
    }


    @Override
    public void itemMin(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        int qty = cart.getQty();
        if (qty > 1) {
            String id_menu = cart.getId_menu();
            qty -= 1;
            boolean updete = myDb.UpdateCart(qty, id_menu);
            if (updete) {
                SubTotal -= Float.parseFloat(cart.getHarga());
                SetData();
            }
        }

    }

    @Override
    public void itemPlus(View view, int position) {
        CartList cart = (CartList) cartList.get(position);
        int qty = cart.getQty();
        String id_menu = cart.getId_menu();
        qty += 1;
        boolean updete = myDb.UpdateCart(qty, id_menu);
        if (updete) {
            SubTotal += Float.parseFloat(cart.getHarga());
            SetData();
        }

    }
    private double getBiayaAntar() {

        if (resto.getRestoran_delivery().equals("gratis")) {
            tempMsgTarifAntar = resto.getRestoran_delivery().toString();
            return 0;
        } else {
            tempMsgTarifAntar = resto.getRestoran_delivery() + " " + kursIndonesia(Double.parseDouble(resto.getRestoran_delivery_tarif()));
            return Double.valueOf(resto.getRestoran_delivery_tarif());
        }

    }


    //get restoran by id
    public void get_restoran() {
        mApiService.cekCart(location.get(SessionManager.LAT), location.get(SessionManager.LANG), id_resto, user.get(SessionManager.ID).toString()).enqueue(new Callback<ResponseOneRumahMakan>() {
            @Override
            public void onResponse(Call<ResponseOneRumahMakan> call, Response<ResponseOneRumahMakan> response) {
                //    get restoran

                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        resto = response.body().getData();
                        menuList = response.body().getMenu();
                        setCartList();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "gagal dapat resto", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseOneRumahMakan> call, Throwable t) {
                progressDialog.dismiss();
                message.setVisibility(View.VISIBLE);
                img_msg.setImageResource(R.drawable.msg_no_connection);
                title_msg.setText("Opss.. Gagal terhubung kesever");
                sub_title_msg.setText("Priksa kembali koneksi internet Anda");
                scCart.setVisibility(View.GONE);

            }
        });
    }

    //set cart , update menu;
    public void setCartList() {

        for (int i = 0; i < cartListTemp.size(); i++) {
            CartList cart = cartListTemp.get(i);
            Boolean itemDelete = true;

            for (int j = 0; j < menuList.size(); j++) {

                Menu item = menuList.get(j);

                if (cart.getId_menu().equals(item.getId().toString())) {
                    itemDelete = false;

                    //kondisi menu tidak discount
                        hargaFloat = Float.parseFloat(item.getHarga());
                        SubTotal += (hargaFloat * cart.getQty());
                        Toast.makeText(mContext, "tidak terdeteksi diskon", Toast.LENGTH_SHORT).show();
                   CartList temp = new CartList(cart.getId(), cart.getId_resto(), cart.getId_menu(), item.getHarga(), cart.getQty(), cart.getCatatan(), item.getNama_makanan(), item.getImage(), item.getMenuKetersediaan());

                    cartList.add(temp);
                }
            }
            if (itemDelete) {
                //hapus menu yang telah di delete
                int msg = myDb.deleteCart(cart.getId());
            }
        }
        if (cartList.isEmpty() || cartList.size() < 1) {
            emptyCart();
            progressDialog.dismiss();
        } else {

            SetData();
            adapter = new CartAdapter(CartListActivity.this, cartList, listener);
            list.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
    //set data
    private void SetData() {
        Total = getBiayaAntar() + SubTotal;
        konsumen_id = user.get(SessionManager.ID).toString();
        konsumen_nama = String.valueOf(user.get(SessionManager.NAMA_KONSUMEN).toUpperCase());
        konsumen_phone = String.valueOf(user.get(SessionManager.NOMOR_TELPON));
        mSubTotal.setText(kursIndonesia(SubTotal));
        mBiayaAntar.setText(tempMsgTarifAntar);
        mNamaKonsumen.setText(konsumen_nama);
        mPhoneKonsumen.setText("+" + konsumen_phone);
        mTotal.setText(kursIndonesia(Total));
        mAlamatAntar.setText(location.get(SessionManager.ALAMAT));
        cashButton.setChecked(true);
        checkKondisi();
    }
    public void checkKondisi() {

        cekKetersedian();
        if (Double.parseDouble(resto.getRestoran_distace()) > resto.getJarak_pesanan()) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Lokasi Antar Diluar Jarak Pengantaran");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else if (SubTotal < Double.parseDouble(resto.getRestoran_delivery_minimum())) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Pesanan Anda dibawah Minimum \n Minimum Subtotal " + kursIndonesia(Double.parseDouble(resto.getRestoran_delivery_minimum())));
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else if (jmlMenuTidakTersedia > 0) {
            btnOrder.setEnabled(false);
            btnOrder.setText("Terdapat Item yang Tidak Tersedia");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorhint));
        } else {
            btnOrder.setEnabled(true);
            btnOrder.setText("Pesanan Sekarang");
            btnOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));

        }
    }

    public void alertKonfirmasi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Konfirmasi Pesanan");
        builder.setMessage("Pesanan anda akan diterima oleh " + resto.getRestoran_nama().toString().toUpperCase() + " ,total pembayaran " + kursIndonesia(Total));
        builder.setCancelable(false);
        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressOrder = new ProgressDialog(mContext);
                progressOrder = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                alertAntrian();
            }
        });
        builder.setNegativeButton("Periksa Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });
        alert.show();
    }

    public void alertAntrian(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Antrian");
        builder.setMessage("Pesanan anda sedang dalam antrian mohon tunggu dalam waktu 7 menit dan akan kami segera proses");
        builder.setCancelable(false);
        builder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressOrder = new ProgressDialog(mContext);
                progressOrder = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                sendOrder();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(mContext, CartListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });
        alert.show();
    }

    //send order to restoran and insert database
    private void sendOrder() {

        int selectId = mMetodeBayar.getCheckedRadioButtonId();
        cashButton = (RadioButton) findViewById(selectId);
        String metBayar = cashButton.getText().toString();
        String title = konsumen_nama;
        String message = all_order();
        String id_konsumen = user.get(SessionManager.ID).toString();
        final String id_restoran = id_resto;
        String pesan_lat = location.get(SessionManager.LAT);
        String pesan_long = location.get(SessionManager.LANG);
        String pesan_alamat = location.get(SessionManager.ALAMAT);
        String pesan_catatan = mCatatanAlamat.getText().toString();
        String jarak_antar = resto.getRestoran_distace().toString();
        String pesan_biaya_antar = Double.toString(getBiayaAntar());
        String pesan_metode_bayar;
        if (metBayar.equals("Transfer - BRI ")) {
            pesan_metode_bayar = "transfer";
        } else {
            pesan_metode_bayar = "cod";
        }
        ArrayList<String> menu = new ArrayList<String>();
        ArrayList<String> harga = new ArrayList<String>();
        ArrayList<String> qty = new ArrayList<String>();
        ArrayList<String> catatan = new ArrayList<String>();

        for (int i = 0; i < cartList.size(); i++) {
            menu.add(cartList.get(i).getId_menu());
            harga.add(cartList.get(i).getHarga());
            qty.add(String.valueOf(cartList.get(i).getQty()));
            catatan.add(cartList.get(i).getCatatan());
        }

        mApiService.createOrder(title, message, id_konsumen, id_restoran, pesan_lat, pesan_long, pesan_alamat, pesan_catatan, pesan_metode_bayar, jarak_antar, pesan_biaya_antar, menu, qty, harga, catatan).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressOrder.dismiss();
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    String id = response.body().getId();
                    if (value.equals("1")) {
                        Toast.makeText(mContext, "Berhasil Mengirim Pesanan ", Toast.LENGTH_SHORT).show();
                        //kosongkan cart dari db
                        myDb.deleteAll();
                        if(pesan_metode_bayar.equals("transfer")){
                            alertTransfer(kursIndonesia(Total));
                        }else{
                            intentSucess(id);
                        }
                    } else if (value.equals("0")) {
                        Toast.makeText(mContext, (R.string.error1), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.error2), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressOrder.dismiss();
                Toast.makeText(mContext, R.string.lostconnection, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    //    untuk membuat list view anti scroll view
    public static void setListViewHeightBasedOnChildren(ListView listView) {
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


    //set mata unag format rupiah
    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    public String all_order() {
        String pesanan = "";
        String sperase = ",";
        for (int i = 0; i < cartList.size(); i++) {
            if (i == cartList.size() - 1) {
                sperase = ".";
            }
            pesanan += cartList.get(i).getNama_makanan() + " " + cartList.get(i).getQty() + sperase;
        }
        return pesanan;
    }

    //tampilkan jika tidak ada cart list
    private void emptyCart() {
        message.setVisibility(View.VISIBLE);
        img_msg.setImageResource(R.drawable.msg_cart);
        title_msg.setText("Ayo Pesan Makanan Anda Sekarang");
        sub_title_msg.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesaan Anda \n");
        scCart.setVisibility(View.GONE);
    }

    public void cekKetersedian() {
        int x = 0;
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getKetersediaan() == 0) {
                x++;
            }
        }
        jmlMenuTidakTersedia = x;

    }

    public void intentSucess(String id) {
        Intent intent = new Intent(mContext, SuccessOrderActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void alertTransfer(String Total) {
        Intent intent = new Intent(mContext, TransferActivity.class);
        intent.putExtra("Total", Total);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}