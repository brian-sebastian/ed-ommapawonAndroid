package com.example.otp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otp.R;
import com.example.otp.models.Menu;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder>{
    private Context mContext;
    private List<Menu> menuList;
    FragmentManager fragmentManager;
    private OnItemClickListener listener;
    String path;
    MyViewHolder holder2;

    public MenuAdapter(Context mContext, List<Menu> menuList, OnItemClickListener listener) {
        super();
        this.mContext = mContext;
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        path = view.getResources().getString(R.string.pathFotoMakanan);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder2 = holder;
        final Menu data = menuList.get(position);
        holder.mNamaMenu.setText(data.getNama_makanan());

        Picasso.get()
                .load(path + data.getImage())
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageMenu);

        holder.mImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_zoom, null);
//                PhotoView photoView = mView.findViewById(R.id.imageMenu);
//                Picasso.get().load(path + data.getImage()).into(photoView);
                ImageView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(path + data.getImage()).into(photoView);
                //photoView.setImageResource(R.drawable.nature);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

//        jumlahFavorit(data.getMenuJumlahFavorit(), holder);


//        if (data.getMenuFavorit() > 0) {
//            holder.mLove.setImageResource(R.drawable.f4);
//        } else {
//            holder.mLove.setImageResource(R.drawable.f0);
//        }


        oprasional(holder, data.getMenuKetersediaan());


        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //           Toast.makeText(mContext,"you click "+ data.getMenuNama(),Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    //Menu Tersedia
                    if (data.getMenuKetersediaan() == 1) {
                        listener.onItemCliked(view, position, false);
                    } else {
                        Toast.makeText(mContext, data.getNama_makanan() + " Tidak Tesedia", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //kondisi menu tidak discount
        holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//        holder.layoutDiscount.setVisibility(View.VISIBLE);
//        holder.mDiscount.setText("-" + data.getMenuDiscount() + "%");
//        holder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//        holder.mHargaCoret.setPaintFlags(holder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getHarga())));

//        // harga dan discount
//        if (data.getMenuDiscount().toString().isEmpty() || data.getMenuDiscount() == 0 || data.getMenuDiscount() == null) {
//            //kondisi menu tidak discount
//        holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//
//        } else {
//            holder.layoutDiscount.setVisibility(View.VISIBLE);
//            holder.mDiscount.setText("-" + data.getMenuDiscount() + "%");
//            holder.mHargaCoret.setText(kursIndonesia(Double.parseDouble(data.getHarga())));
//            holder.mHargaCoret.setPaintFlags(holder.mHargaCoret.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.mHargaMenu.setText(kursIndonesia(HitungDiscount(Double.parseDouble(data.getHarga()), data.getMenuDiscount())));
//
//        }

        //long click
        holder.mParentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null){
                    listener.onItemCliked(view, position, true);
                }
                return true;
            }
        });


    }

//    private void jumlahFavorit(Integer menuJumlahFavorit, MyViewHolder holder) {
//        if (menuJumlahFavorit > 0) {
//            holder.mJmlFavorit.setText(menuJumlahFavorit.toString());
//        } else {
//            holder.mJmlFavorit.setVisibility(View.INVISIBLE);
//            holder.mLoveBlack.setVisibility(View.INVISIBLE);
//
//        }
//
//    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

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

        public MyViewHolder(@NonNull View itemView) {
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

        }
    }

    public interface OnItemClickListener {

        void onItemCliked(View view, int position, boolean isLongClick);
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

    public void favoritAt(View view, int position) {
        MyViewHolder holder = new MyViewHolder(view);
        Menu menu = menuList.get(position);
        if (menu.getMenuFavorit() == 0) {
            Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
            menu.setMenuJumlahFavorit(menu.getMenuJumlahFavorit() + 1);
            menu.setMenuFavorit(1);
            menuList.set(position, menu);
            holder.mLoveBlack.setVisibility(View.VISIBLE);
            holder.mJmlFavorit.setVisibility(View.VISIBLE);
            holder.mJmlFavorit.setText(menu.getMenuJumlahFavorit().toString());
            // notifyDataSetChanged();
        }

    }



}
