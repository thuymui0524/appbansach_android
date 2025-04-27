package com.example.appbansach;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbansach.modle.mh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addThiActivity extends AppCompatActivity {

    private EditText etMaHang, etTenHang, etMoTa, etGia, etSoLuong;
    private TextView btnAddProduct;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_thi);

        mDatabase = FirebaseDatabase.getInstance().getReference("mhs");

        etMaHang = findViewById(R.id.etMaHang);
        etTenHang = findViewById(R.id.etTenHang);
        etMoTa = findViewById(R.id.etMoTa);
        etGia = findViewById(R.id.etGia);
        etSoLuong = findViewById(R.id.etSoLuong);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void addProduct() {
        String maHang = etMaHang.getText().toString().trim();
        String tenHang = etTenHang.getText().toString().trim();
        String moTa = etMoTa.getText().toString().trim();
        String giaStr = etGia.getText().toString().trim();
        String soLuongStr = etSoLuong.getText().toString().trim();

        if (maHang.isEmpty() || tenHang.isEmpty() || moTa.isEmpty() || giaStr.isEmpty() || soLuongStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền vào tất cả các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        double gia;
        int soLuong;

        try {
            gia = Double.parseDouble(giaStr);
            soLuong = Integer.parseInt(soLuongStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem sản phẩm đã tồn tại chưa
        mDatabase.child(maHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(addThiActivity.this, "Mã hàng đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    mh newProduct = new mh(maHang, tenHang, moTa, gia, soLuong);

                    mDatabase.child(maHang).setValue(newProduct).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(addThiActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            etMaHang.setText("");
                            etTenHang.setText("");
                            etMoTa.setText("");
                            etGia.setText("");
                            etSoLuong.setText("");
                        } else {
                            Toast.makeText(addThiActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(addThiActivity.this, "Lỗi cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
