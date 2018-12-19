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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prapa.seproject.pra_pa.GeneratePassword;
import com.prapa.seproject.pra_pa.R;
import com.prapa.seproject.pra_pa.User;

public class SearchUsernameFragment extends Fragment {

    FirebaseFirestore _fbfs = FirebaseFirestore.getInstance();

    String numberRoom;
    TextView usernameStr, pwdStr;

    private SharedPreferences _spfr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_username, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _spfr = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);

        initLogout();
        initBackBtn();
        initSearch();
        rePassword();
    }

    private String numberUpper;
    private void initSearch(){
        ImageView _btnSearch = getView().findViewById(R.id.show_u_btn_submit);
        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberRoom = ((EditText) getView().findViewById(R.id.show_u_number_room)).getText().toString();
                if (numberRoom.isEmpty()){
                    Log.e("ShowUsernamePwd", "NumberRoom is Empty");
                }
                else{
                    numberUpper = numberRoom.toUpperCase();
                    getDataFromFireBase(numberUpper);
                    Log.e("ShowUsernamePwd", "NumberRoom is "+numberUpper);
                }
            }
        });
    }

    private void rePassword(){
        usernameStr = getView().findViewById(R.id.show_u_username);
        pwdStr = getView().findViewById(R.id.show_u_pwd);
        Button _rePass = getView().findViewById(R.id.re_password_btn_search_user);
        _rePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String _username = usernameStr.getText().toString();
                    String _pass = pwdStr.getText().toString();
                    Log.d("ShowUsernamePwd", "username : "+_username +" password : "+_pass);
                    if(_username.equals("-") || _pass.equals("-")){
                        Toast.makeText(getContext(),"ข้อมูลผู้ใช้ว่าง",Toast.LENGTH_LONG).show();
                        Log.d("ShowUsernamePwd", "Empty");
                    }else {
                        final String newPwd = GeneratePassword.generatePassword();
                        User user = new User(numberRoom, _username, newPwd, "RESIDENT");
                        _fbfs.collection("User_Pass")
                                .document("Room"+numberUpper)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pwdStr.setText(newPwd);
                                        Log.d("ShowUsernamePwd", "set password success \n New password: "+newPwd);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("ShowUsernamePwd", "set password failed");
                                    }
                                });
                    }

                }catch (Exception e){
                    Log.e("ShowUsernamePwd",""+e.getMessage()) ;
                }
            }
        });

    }

    private void getDataFromFireBase(String _numberRoom){
        _fbfs.collection("User_Pass")
                .document("Room"+_numberRoom)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("ShowUsernamePwd", "... "+documentSnapshot.toObject(User.class));
                        if(documentSnapshot.toObject(User.class) == null){
                            SetUsernamePassword("-","-");
                            Toast.makeText(getContext(),"ไม่มีเลขห้องนี้",Toast.LENGTH_LONG).show();
                            Log.e("ShowUsernamePwd","Unknown Error") ;
                        }else{
                            User users = documentSnapshot.toObject(User.class);
                            SetUsernamePassword(users.getUsername(),users.getPassword());
                            Log.e("ShowUsernamePwd"," ROOM : "+documentSnapshot.toObject(User.class).getRoom_id()) ;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ShowUsernamePwd","Unknown Error") ;
            }
        });
    }

    private void SetUsernamePassword(String name, String pwd){
        usernameStr = getView().findViewById(R.id.show_u_username);
        pwdStr = getView().findViewById(R.id.show_u_pwd);

        Log.e("ShowUsernamePwd","Show Username & Password") ;
        usernameStr.setText(name);
        pwdStr.setText(pwd);
    }

    private void initLogout(){
        ImageView _logout = getView().findViewById(R.id.logout_search_user);
        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = _spfr.edit();
                editor.clear();
                editor.commit();
                Log.d("ShowUsernamePwd", _spfr.getString("role", "not found"));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new HomeFragment())
                        .addToBackStack(null).commit();
                Log.d("ShowUsernamePwd", "Logout --> Home");
            }
        });
    }

    private void initBackBtn() {
        ImageView _backBtn = getView().findViewById(R.id.back_btn_search_user);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .popBackStack();
            }
        });

    }
}


