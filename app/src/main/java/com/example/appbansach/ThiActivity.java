package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.Adapter.BookAdapter;
import com.example.appbansach.Adapter.mhAdapter;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.mh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThiActivity extends AppCompatActivity {
    private TextView addMH;
    private RecyclerView recyclerView;

    private mhAdapter adapter;


    private List<mh> mhList;
    private String accountType;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thi);

        addMH  = findViewById(R.id.addMH);
        mDatabase = FirebaseDatabase.getInstance().getReference("mhs");

        Intent intent = getIntent();
        accountType = intent.getStringExtra("role");

        addMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThiActivity.this, addThiActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.search_viewMH);

        mhList = new ArrayList<>();

        fetchMhsFromFirebase();

        adapter = new mhAdapter(this, mhList, accountType);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    private void fetchMhsFromFirebase() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mhList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mh mh = snapshot.getValue(mh.class);
                    if (mh != null) {
                        mhList.add(mh);
                    }
                }
                adapter.notifyDataSetChanged();
                if (mhList.isEmpty()) {
                    Toast.makeText(ThiActivity.this, "Danh sách sách rỗng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ThiActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

}