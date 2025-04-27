package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.mh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateThiActivity extends AppCompatActivity {

    private  String  mhId;
    private DatabaseReference mDatabase;
    private EditText etMaHang, etTenHang, etMoTa, etGia, etSoLuong;
   private TextView btnUpdateProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_thi);
        etMaHang = findViewById(R.id.etMaHang);
        etTenHang = findViewById(R.id.etTenHang);
        etMoTa = findViewById(R.id.etMoTa);
        etGia = findViewById(R.id.etGia);
        etSoLuong = findViewById(R.id.etSoLuong);
        btnUpdateProduct= findViewById(R.id.btnUpdateProduct);
        
        etMaHang.setEnabled(false);
        etMaHang.setFocusable(false);
        Intent intent = getIntent();
        if (intent != null) {
            mhId = intent.getStringExtra("item_id");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fetchMhDetails();

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMHToFirebase();
            }
        });
    }
    private void fetchMhDetails() {
        if (mhId != null) {
            mDatabase.child("mhs").child(mhId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        mh mh = snapshot.getValue(mh.class);
                        if (mh != null) {
                            etMaHang.setText(mh.getMaHang());
                            etTenHang.setText(mh.getTenHang());
                            etMoTa.setText(mh.getMoTa());
                            etGia.setText(String.valueOf(mh.getGia()));
                            etSoLuong.setText(String.valueOf(mh.getSoLuong()));
                        }
                    } else {
                        Toast.makeText(UpdateThiActivity.this, "Không tìm thấy ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UpdateThiActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateMHToFirebase() {
        String MaHang = etMaHang.getText().toString().trim();
        String TenHang = etTenHang.getText().toString().trim();
        String MoTa = etMoTa.getText().toString().trim();
        String Gia = etGia.getText().toString().trim();
        String SoLuong = etSoLuong.getText().toString().trim();


        if (MaHang.isEmpty() || TenHang.isEmpty() || MoTa.isEmpty() || Gia.isEmpty() ||
                SoLuong.isEmpty() ) {
            Toast.makeText(UpdateThiActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double donGia;
        int soLuong;
        try {
            donGia = Double.parseDouble(Gia);
            soLuong = Integer.parseInt(SoLuong);
        } catch (NumberFormatException e) {
            Toast.makeText(UpdateThiActivity.this, "Đơn giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        mh mh = new mh(MaHang, TenHang, MoTa, donGia, soLuong);

        mDatabase.child("mhs").child(mhId).setValue(mh)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateThiActivity.this, "Cập nhật  thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    etMaHang.setText("");
                    etTenHang.setText("");
                    etMoTa.setText("");
                    etGia.setText("");
                    etSoLuong.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateThiActivity.this, "Cập nhật  thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}