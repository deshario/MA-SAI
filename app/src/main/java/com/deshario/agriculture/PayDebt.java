package com.deshario.agriculture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.deshario.agriculture.Adapters.PayDebtAdapter;
import com.deshario.agriculture.Models.Records;

import java.util.List;

/**
 * Created by Deshario on 7/8/2017.
 */

public class PayDebt extends AppCompatActivity {

    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static TextView counter;
    List<Records> debtrecords;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_main_card);
        counter = (TextView)findViewById(R.id.count);

        debtrecords = Records.getSpecificRecordsByType(1);
        counter.setText("พบ "+debtrecords.size()+" รายการ");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PayDebtAdapter(debtrecords,PayDebt.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void reload(){
        debtrecords = Records.getSpecificRecordsByType(1);
        PayDebt.counter.setText("พบ "+debtrecords.size()+" รายการ");
        mAdapter = new PayDebtAdapter(debtrecords,PayDebt.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

}
