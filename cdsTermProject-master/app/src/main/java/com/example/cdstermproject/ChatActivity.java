package com.example.cdstermproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    EditText InputEditText;
    ListView listView;

    ArrayList<MessageItem> messageItems=new ArrayList<>();
    ChatAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setTitle(DataSource.UserName);

        InputEditText = findViewById(R.id.et);
        listView = findViewById(R.id.listview);
        adapter = new ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 주소가 어디야!! - 구글 서비스 파일을 참조해서
        chatRef = firebaseDatabase.getReference("chat"); // 이 주소의 리얼타임 데이터 베이스에서 chat이라는 노드를 참조할래!! 라는 의미이다

        chatRef.addChildEventListener(new ChildEventListener() {
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

                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class); // 새로 추가된 값 가져오기
                /*
                파싱 기술 - 자바편
                swift or python 사용할 때는 파싱 시 구조체 or 클래스 배열 등을 활용하거나
                딕셔너리(자바에서는 해시맵)을 이용하였고, 코드 또한 직관적이라서 파싱이 쉬었다.
                그러나 자바에서는 클래스(?)를 엄격히 지켜야해서 몇가지 불편한 점이 있었는데,
                1. 자바 클래스 파일을 새로 만들어서 이동해가면서 코딩해야 하는점
                2. 사용 빈도가 낮음에도 불구하고, 새로운 파일을 생성해서 관리가 복잡해진다는 점
                3. 스위프트나 파이썬에 비해 직관성이 떨어지는 느낌을 받은점 - 이건 내가 자바랑 잘 안맞는거 같다..

                아무튼 자바를 더 공부해야할 필요가 있는데, 메시지 아이템에 담아 보내야한다.
                getValue 부분에서 String.class 도 있고 그런데 자바는 객체지향 개념인가 그래서 아무튼
                모든게 다 클래스다.



                 */
                messageItems.add(messageItem); // 메시지 입력시 메시지 아이템에 담아 리얼타임 데베로 올리기

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size() - 1); // 리스트뷰의 끝으로 스크롤 이동
                /* 이 방식의 고찰
                -iOS 에서도 테이블 뷰 사용할 떄 이런 문제를 만나는데 나는 그냥 아래 부분에 넣는게 아닌 배열을 뒤집든가
                아니면 배열의 끝을 테이블 뷰에 제일 윗 부분에 얹는 방식을 사용했었다.
                이유는 애플 디자인 문서 or UX 공부에 있어서 사용자에게 가장 최신의 정보를 상단에 올려주는 것이 더 좋다고 했기 떄문이다.
                그러나 채팅의 경우는 키보드와 맞춤이 필요하여, 스크롤 자체를 아래로 내리는 방법을 사용해야하는데,
                이 방법이 상당히 좋은 방법인 것 같다!!
                 */
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

    public void SendText(View view) {

        // 리얼타임 데베에 저장할 값들
        // 유저네임 - 학번 , 메시지내용, 프로필 이미지 - storage 구현하려면 이게 좋을듯 - 원석이 사이트로 코드 엎음.
        String userName = DataSource.UserName;
        String message = InputEditText.getText().toString();
        String pofileUrl = DataSource.profileUrl; // 스토리지 사용해야해서

        //메세지 작성 시간 문자열로..
        //Calendar calendar = Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        //String time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16


        // 시간설정 - push 사용하면 시간값을 가지고 리얼타임 데베에 기록되어 시간을 기록할 필요가 사실 없긴하나, JSON트리에서 손쉽게 확인하기 위해 이렇게 작성
        /*
        캘린더 방식도 있으나 - 개인적으로 포맷방식이 더 자유로운 느낌이라 더 애용함.
         */
        Date CurrentTime = Calendar.getInstance().getTime();
        SimpleDateFormat MyFormat = new SimpleDateFormat("HH:mm"); // 00:00 - 00시 00분 타입 HH - 0~24 mm - 0~60
        String time= MyFormat.format(new Date());

        MessageItem messageItem = new MessageItem(userName,message,time,pofileUrl); // 리얼타임 데베에 올릴 값 구현
        chatRef.push().setValue(messageItem); // 챗 노드에 push하기
        /*
        push의 경우 이전 내용을 덮어쓰지 않는다.
        다만 setValue만 사용해도 덮어쓰지 않는 스킬도 있는데, 그건 로그인시 uid 저장에 사용하였다.
         */

        InputEditText.setText(""); // 입력필드 비워주기~!

        // 키보드 editText가 우선순위가 높아 포커스를 받는데 키보드 숨기게 - 레이아웃이 포커스 갖게
        InputMethodManager keyboardHide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardHide.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}