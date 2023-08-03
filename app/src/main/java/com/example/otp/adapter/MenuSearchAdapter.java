package com.example.otp.adapter;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otp.R;
import com.example.otp.activities.MenuActivity;
import com.example.otp.config.ServerConfig;
import com.example.otp.fragment.SearchMenuFragment;
import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;
import com.example.otp.response.ResponseOneRumahMakan;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSearchAdapter extends RecyclerView.Adapter<MenuSearchAdapter.MyViewHolder> {
    private List<Menu> menuList;

    private Context mContext;
    FragmentManager fragmentManager;
    ApiService mApiService;
    View view;

    public MenuSearchAdapter(Context mContext, List<Menu> data) {
        super();
        this.menuList = data;

        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        mApiService = ServerConfig.getAPIService();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Menu data = menuList.get(position);
        holder.mNamaMenu.setText(data.getNama_makanan());
        final String path = view.getResources().getString(R.string.pathFotoMakanan);


        Picasso.get()
                .load(view.getResources().getString(R.string.pathFotoMakanan) + data.getImage())
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageMenu);

        holder.mImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_zoom, null);
                ImageView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(path + data.getImage()).into(photoView);
                //photoView.setImageResource(R.drawable.nature);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


//        jumlahFavorit(data.getMenuJumlahFavorit(), holder);


        oprasional(holder, data.getMenuKetersediaan());

        //kondisi menu tidak discount
        holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getHarga())));


//        // harga dan discount
//        if (data.getMenuDiscount().toString().isEmpty() || data.getMenuDiscount() == 0 || data.getMenuDiscount() == null) {
//            //kondisi menu tidak discount
//            holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//        } else {
//            holder.layoutDiscount.setVisibility(View.VISIBLE);
//            holder.mDiscount.setText("-" + data.getMenuDiscount() + "%");
//            holder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//            holder.mHargaCoret.setPaintFlags(holder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.mHargaMenu.setText(kursIndonesia(HitungDiscount(Double.parseDouble(data.getHarga()), data.getMenuDiscount())));
//
//        }

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //           Toast.makeText(mContext,"you click "+ data.getMenuNama(),Toast.LENGTH_SHORT).show();

                if (data.getMenuKetersediaan() == 1) {

                    getResto(data.getId_restoran().toString(), data.getId().toString());


                } else {
                    Toast.makeText(mContext, data.getNama_makanan() + " Tidak Tesedia", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void getResto(final String id, final String id_menu) {
        mApiService.getRestoranByID(id).enqueue(new Callback<ResponseOneRumahMakan>() {
            @Override
            public void onResponse(Call<ResponseOneRumahMakan> call, Response<ResponseOneRumahMakan> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equalsIgnoreCase("1")) {
                        RumahMakan data = response.body().getData();
                        Intent intent = new Intent(mContext, MenuActivity.class);
                        intent.putExtra("Resto", data);
                        mContext.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOneRumahMakan> call, Throwable t) {

            }
        });
    }

    private void jumlahFavorit(Integer menuJumlahFavorit, MyViewHolder holder) {
        if (menuJumlahFavorit > 0) {
            holder.mJmlFavorit.setText(menuJumlahFavorit.toString());
        } else {
            holder.mJmlFavorit.setVisibility(View.INVISIBLE);
            holder.mLoveBlack.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView mNamaMenu;
        TextView mHargaMenu;
        CardView mParentLayout;
        TextView mKetersediaan;
        ImageView mLove;
        ImageView mImageMenu;
        TextView mJmlFavorit;
        ImageView mLoveBlack;
//        TextView mHargaCoret;
//        TextView mDiscount;
//        LinearLayout layoutDiscount;

        public MyViewHolder(final View itemView) {
            super(itemView);
            mNamaMenu = (TextView) itemView.findViewById(R.id.tvNamaMenu);
            mHargaMenu = (TextView) itemView.findViewById(R.id.tvHargaMenu);
            mParentLayout = (CardView) itemView.findViewById(R.id.parentLayout);
            mKetersediaan = (TextView) itemView.findViewById(R.id.tvKetersedian);
//            mLove = (ImageView) itemView.findViewById(R.id.imgLove);
            mImageMenu = (ImageView) itemView.findViewById(R.id.imageMenu);
//            mJmlFavorit = (TextView) itemView.findViewById(R.id.tvJmlFavorit);
//            mLoveBlack = (ImageView) itemView.findViewById(R.id.imgLoveBlack);
//            mHargaCoret = (TextView) itemView.findViewById(R.id.tvHargaCoret);
//            mDiscount = (TextView) itemView.findViewById(R.id.tvDiscount);
//            layoutDiscount = (LinearLayout) itemView.findViewById(R.id.layoutDiscount);
//            ButterKnife.bind(this, itemView);
        }
    }


    public void oprasional(MyViewHolder holder, Integer code) {

        if (code == 1) {
            holder.mKetersediaan.setText("Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.mKetersediaan.setText("Tidak Tersedia");
            holder.mKetersediaan.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }

    public String kursIndonesia(double nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }

    public Double HitungDiscount(Double Harga, Integer Discount) {
        int discount = Discount / 100;
        double harga_potongan = ((Discount / 100.00) * Harga);
        return Harga - harga_potongan;
    }


}
