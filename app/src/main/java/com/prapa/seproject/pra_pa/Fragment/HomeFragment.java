package com.prapa.seproject.pra_pa.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.ShowBill.ShowBillFragment;
import com.prapa.seproject.pra_pa.User;

public class HomeFragment extends Fragment {

    private FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();
    private User user;
    private SharedPreferences _spfr;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String role = _spfr.getString("role","");

        try {
            if(role.equals("")){
                initLogin();
                Log.d("HOME", "Login");
            }else {
                nextToPage(role);
                Log.d("HOME", "Next page auto");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initLogin(){
        Button _loginBtn = getView().findViewById(R.id.login_btn);
        _loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _username = ((EditText)(getView().findViewById(R.id.username_home))).getText().toString();
                String _password = ((EditText)(getView().findViewById(R.id.password_home))).getText().toString();
                try{
                    if(_username.isEmpty() || _password.isEmpty()){
                        Toast.makeText(getActivity(), "กรุณาใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                        Log.d("HOME", "Empty");
                    }else{
                        getDataFromDb(_username, _password);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("HOME", "Error : "+e.getMessage());
                }

            }
        });
    }

    private void getDataFromDb(final String username, final String password){

        _fbfs.collection("User_Pass").document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("HOME", "... "+documentSnapshot.toObject(User.class));
                        if(documentSnapshot.toObject(User.class) == null){
                            Toast.makeText(getActivity(), "ไม่พบข้อมูล : "+username, Toast.LENGTH_SHORT).show();
                            Log.d("HOME", "not found username : "+username);
                        }else{
                            user = documentSnapshot.toObject(User.class);
                            checkAuthen(password);
                            Log.d("HOME", "getData success... user : "+user.getUsername());

                            //SharedPreferences
                            SharedPreferences.Editor editor = _spfr.edit();
                            editor.putString("room_id", user.getRoom_id());
                            editor.putString("role", user.getRole());
                            editor.commit();
                            Log.d("HOME", "save on device : "+_spfr.getString("room_id", "not found"));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "ไม่พบข้อมูล : "+username, Toast.LENGTH_SHORT).show();
                Log.d("HOME", "not found username : "+username);
            }
        });
    }

    private void checkAuthen(String password){
        Log.d("HOME", "Check Authen");
        if(password.equals(user.getPassword())){
            nextToPage(user.getRole());
        }else{
            Toast.makeText(getActivity(), "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            Log.d("HOME", "password is wrong");
        }
    }

    private void nextToPage(String role){
        if(role.equals("RESIDENT")){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new ShowBillFragment())
                    .addToBackStack(null).commit();
        } else if (role.equals("STAFF")) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new ChoosePlanFragment())
                    .addToBackStack(null).commit();
        } else if (role.equals("LEGAL")){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new EditUnitFragment())
                    .addToBackStack(null).commit();
        }
        Toast.makeText(getActivity(), "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();
        Log.d("HOME", "เข้าสู่ระบบสำเร็จ go to : "+role);
    }

}
