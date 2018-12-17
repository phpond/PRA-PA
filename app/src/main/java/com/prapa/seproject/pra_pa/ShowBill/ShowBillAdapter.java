package com.prapa.seproject.pra_pa.ShowBill;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;

import java.util.ArrayList;

public class ShowBillAdapter extends RecyclerView.Adapter<ShowBillAdapter.ViewHolder> {

    private ArrayList<Bill> _billDataSet;
    private Context mContext;

    public ShowBillAdapter(ArrayList<Bill> _billDataSet, Context mContext) {
        this._billDataSet = _billDataSet;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShowBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_showbill,
                        parent, false);

        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowBillAdapter.ViewHolder viewHolder, int position) {
        Log.d("SHOWBILL_ADAP", "onBindViewHolder: called. \n Bill : "+_billDataSet);
        try{
            viewHolder.totalPrice.setText(""+_billDataSet.get(position).getTotal_price_bill());
            viewHolder.room.setText(""+_billDataSet.get(position).getRoomString());
            viewHolder.waterMeter.setText(""+_billDataSet.get(position).getWater_bill());
            viewHolder.dateRecord.setText(""+_billDataSet.get(position).getRecord_date());
            viewHolder.billDate.setText(""+_billDataSet.get(position).getMonth()+"/"+_billDataSet.get(position).getYear());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return _billDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CardView parentLayout;
        public TextView totalPrice;
        public TextView room;
        public TextView waterMeter;
        public TextView dateRecord;
        public TextView billDate;

        public ViewHolder(View itemView){
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            totalPrice = itemView.findViewById(R.id.price_total_show_bill);
            room = itemView.findViewById(R.id.room_id_show_bill);
            waterMeter = itemView.findViewById(R.id.water_meter_show_bill);
            dateRecord = itemView.findViewById(R.id.date_record_show_bill);
            billDate = itemView.findViewById(R.id.bill_show_bill);

        }
    }
}
