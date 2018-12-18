package com.prapa.seproject.pra_pa.Payment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.Bill;
import com.prapa.seproject.pra_pa.R;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private ArrayList<Bill> _billDataSet;
    private Context mContext;

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    public PaymentAdapter(ArrayList<Bill> _billDataSet, Context mContext) {
        this._billDataSet = _billDataSet;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_payment_item,
                        parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentAdapter.ViewHolder viewHolder, final int position) {
        Log.d("PAYMENT_ADAP", "onBindViewHolder: called. \n Bill : "+_billDataSet);
        try{
            Log.d("PAYMENT_ADAP","status : "+_billDataSet.get(position).getRoomString() );
            viewHolder.roomId.setText(_billDataSet.get(position).getRoomString());
            viewHolder.dateBill.setText(_billDataSet.get(position).getMonth()+"/"+_billDataSet.get(position).getYear());
            viewHolder.priceTotal.setText(""+_billDataSet.get(position).getTotal_price_bill());
            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _billDataSet.get(position).setStatus("ชำระเงินเรียบร้อย");
                    _fbfs.collection("Resident")
                            .document("USER")
                            .collection(_billDataSet.get(position).getRoomString())
                            .document(_billDataSet.get(position).getMonth()+_billDataSet.get(position).getYear())
                            .set(_billDataSet.get(position))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    viewHolder.status.setText("ชำระเรียบร้อย");
                                    viewHolder.status.setBackgroundResource(R.drawable.success_btn);
                                    Log.d("PAYMENT_ADAP", "Update data success : Room "+_billDataSet.get(position).getRoomString()
                                    +"Bill : "+_billDataSet.get(position).getMonth()+"/"+_billDataSet.get(position).getYear());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("PAYMENT_ADAP", "Update Fail : "+_billDataSet.get(position).getRecord_date());
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return _billDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout layout;
        public TextView roomId;
        public TextView priceTotal;
        public TextView dateBill;
        public Button status;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.payment_item_layout);
            roomId = itemView.findViewById(R.id.room_id_payment_status_payment_item);
            priceTotal = itemView.findViewById(R.id.price_total_payment_item);
            dateBill = itemView.findViewById(R.id.date_bill_payment_item);
            status = itemView.findViewById(R.id.status_check_payment_item);

        }
    }
}
