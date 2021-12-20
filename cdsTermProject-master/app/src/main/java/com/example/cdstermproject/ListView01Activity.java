package com.example.cdstermproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListView01Activity extends AppCompatActivity {

    int flag;

    ListView listView;
    ListAdapter listAdapter  = new ListAdapter();
    String message = "message";
    String name = "name";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); //databasereference 가져오기
    DatabaseReference mConditionRef = mDatabase.child("UserList");
    DatabaseReference mConditionUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view01);

        listView = findViewById(R.id.listView1);
        listView.setAdapter(listAdapter);


        mConditionRef.addChildEventListener(new ChildEventListener() {
            /// 리얼타임 데베에서 정보 읽어오기 -
            /*
            정보를 읽어오는 방법에는 다양한 메소드가 있다 - 파베 공식문서 참고
            크게 3가지 정도가 있는데
            1. 한번 읽기 - 아마 get() 메소드 사용하는걸로 기억함
            2. addChildEventListener - 이건 업데이트 된 거 이외에 모든 정보를 다 읽어온다. - 데이터가 크면 단점이지만 그럼에도 불구하고 사용한 이유가 있다.
            3. 업데이트 된 내용만 읽기 - 이것도 메소드 따로 있었는데 잘 기억 안남 공식 문서 보기

            2번을 사용한 이유는 여러명의 유저가 서로 다른 시간에 접속하고 이미 메시지가 작성되어 있으면 그 유저는 그 메시지를 볼 수 없다.
            사실 카카오톡 같은 경우 그룹이 만들어진 상황에서 새로운 유저가 접속하면 새유저는 이전의 내용을 볼 수 없으나,
            우리 앱의 경우 실시간 + 게시판의 성격을 동시차용하기를 바랬기 때문에 이렇게 변경하였다.

            - 추가로 카카오톡의 '삭제된 메시지입니다' 로 이미 보내놓고 삭제하는 기능도 구현을 생각하고 있음
            - 구현 방법 - 메시지 삭제시 리얼타임 데베에서 메시지 내용을 바꾸는게 아니라 메시지 삭제 코드를 따로 두어
            메시지 삭제 코드가 들어있는 메시지를 가림 표시
            이렇게 구현하면, 메시지 복구 가능하며, 나에게만 가림 or 모두에게서 삭제 구현 가능하게 설계할 수 있다 - 이건 나중에 해봐야지^_^
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String uid = String.valueOf(dataSnapshot.getKey());
                mConditionUid =mConditionRef.child(uid);
                /* *********************************************************** */
                mConditionUid.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(message.equals(dataSnapshot.getKey())) {
                            String login = "login";
                            if(login.equals(dataSnapshot.getValue())) {

                                flag =1;

                            }
                            else {
                                flag =0;
                            }

                        }

                        else if (name.equals(dataSnapshot.getKey())) {

                            String text = ""+dataSnapshot.getValue();
                            Log.d("usertag",text);
                            //userList.add(new user_login("hi",R.drawable.online));
                            if (flag ==1) {
                                listAdapter.addItem(text, ContextCompat.getDrawable(getApplicationContext(), R.drawable.online));
                            }
                            else if (flag == 0){
                                listAdapter.addItem(text, ContextCompat.getDrawable(getApplicationContext(),R.drawable.offline ));

                            }
                            listAdapter.notifyDataSetChanged();

                            //userList.add(new USER(, "미션임파서블","15세 이상관람가"));

                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            /* ********************************* */


            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }



}
