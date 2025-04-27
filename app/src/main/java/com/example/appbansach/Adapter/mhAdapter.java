package com.example.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.R;
import com.example.appbansach.UpdateThiActivity;
import com.example.appbansach.modle.mh;
import com.example.appbansach.updateBookActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class mhAdapter extends RecyclerView.Adapter<mhAdapter.ViewHolder> {

    private List<mh> mhList;
    private Context mContext;
    private String accountType;

    public mhAdapter(Context context, List<mh> mhList, String accountType) {
        this.mContext = context;
        this.mhList = mhList;
        this.accountType = accountType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtMHName;
        public TextView txtMota;
        public TextView txtGia;
        public ImageView action;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txtMHName = itemView.findViewById(R.id.txtMHName);
            txtMota = itemView.findViewById(R.id.txtMota);
            txtGia = itemView.findViewById(R.id.txtGia);
            action = itemView.findViewById(R.id.action);

            action.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showOptionsDialog(mContext, mhList.get(position));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View mhView = inflater.inflate(R.layout.mh, parent, false);

        return new ViewHolder(mhView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mh item = mhList.get(position);
        double donGia = item.getGia();

        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedDonGia = vnFormat.format(donGia);

        holder.txtMHName.setText(item.getTenHang());
        holder.txtMota.setText(item.getMoTa());
        holder.txtGia.setText(formattedDonGia);

        if ("admin".equals(accountType)) {
            holder.action.setVisibility(View.VISIBLE);
        } else if ("user".equals(accountType)) {
            holder.action.setVisibility(View.GONE);
        }
    }

    private void showOptionsDialog(Context context, mh item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn tác vụ");
        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    Intent intent = new Intent(context, UpdateThiActivity.class);
                    intent.putExtra("item_id", item.getMaHang());
                    context.startActivity(intent);
                    break;
//                case 1:
//                    showDeleteConfirmationDialog(context, item);
//                    break;
            }
        });
        builder.create().show();
    }

    private void showDeleteConfirmationDialog(Context context, mh item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa  không?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            deleteItem(item);
        });
        builder.setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }

    private void deleteItem(mh item) {
        int position = mhList.indexOf(item);
        mhList.remove(position);
        notifyItemRemoved(position);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mhs");
        mDatabase.child(item.getMaHang()).removeValue();
    }

    @Override
    public int getItemCount() {
        return mhList.size();
    }
}
