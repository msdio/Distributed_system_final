package com.example.cdstermproject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    EditText EditUserName;
    CircleImageView ivProfile;

    Uri imgUri;//선택한 프로필 이미지 경로 Uri

    boolean isFirst = true; //앱을 처음 실행한 것인가?
    boolean isChanged = false; //프로필을 변경한 적이 있는가?

    Button loginBtn;
    EditText emailField;
    EditText pwField;
    Button userlistBtn;
    Button signupBtn;

    FirebaseAuth auth = FirebaseAuth.getInstance(); // auth에 대한 정보
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); //databasereference 가져오기
    DatabaseReference mConditionRef = mDatabase.child("UserList");

    //   DatabaseReference mConditionchat = mDatabase.child("chat");

    ArrayList<USER> userList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditUserName = findViewById(R.id.Input_StuNum);
        ivProfile = findViewById(R.id.iv_profile);
        loginBtn = findViewById(R.id.loginBtn);
        emailField = findViewById(R.id.emailField);
        pwField = findViewById(R.id.pwField);
        userlistBtn = findViewById(R.id.userlistBtn);
        signupBtn = findViewById(R.id.signupBtn);

/*
        mCondition.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("useremailuid","chat은" + snapshot.getValue());
                  //  Log.d("email",mConditionRef.child(snapshot.getKey()).getEmail)
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = emailField.getText().toString().trim();
                String pwText = pwField.getText().toString().trim();

                FirebaseUser user = auth.getCurrentUser();
                //Log.d("user",user.getEmail());

               if (user == null) {
                   auth.createUserWithEmailAndPassword(emailText, pwText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Toast.makeText(MainActivity.this, "회원가입 성공 로그인 완료", Toast.LENGTH_SHORT).show();
                            loginBtn.setText("로그아웃");
                            String uid = String.valueOf(auth.getCurrentUser().getUid()); // 로그인 한 유저의 아이
                               String emailText = emailField.getText().toString().trim();
                               String message = "login";

                               USER userList = new USER(emailText,message);
                               mConditionRef.child(uid).setValue(userList);



                           } else {
                               Toast.makeText(MainActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
               else {
                   Toast.makeText(MainActivity.this, "기존의 아이디를 로그아웃하세요", Toast.LENGTH_SHORT).show();

                }
               }


        });




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이메일이랑 패스워드의 앞뒤 공백 제거
                /*
                제거하는 이유 : 사용자가 흔히 하는 실수로 앞이나 맨 뒤에 공백을 넣었는데
                이거 때문에 로그인이 안되면.. ^__^
                UX개선측면
                 */
                String emailText = emailField.getText().toString().trim();
                String pwText = pwField.getText().toString().trim();


                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this,"로그아웃 성공.", Toast.LENGTH_SHORT).show();
                   String uid = String.valueOf(auth.getCurrentUser().getUid()); // 로그인 한 유저의 아이디
                   // USER userList = new USER(emailText,"0");
                    String message = "logout";

                    USER userList = new USER(emailText,message);
                    mConditionRef.child(uid).setValue(userList);
                    auth.signOut();
                    loginBtn.setText("로그인");
                } else {

                    // No user is signed in
                    auth.signInWithEmailAndPassword(emailText,pwText)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"로그인 성공" , Toast.LENGTH_SHORT).show();
                                        loginBtn.setText("로그아웃");

                                        String uid = String.valueOf(auth.getCurrentUser().getUid()); // 로그인 한 유저의 아이디

                                        //mConditionRef.child(String.valueOf(uid)).push().setValue("1");
                                        //Write a message to the database

                                        //FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        //DatabaseReference msgRef = database.getReference(String.valueOf(uid));
                                        String message = "login";


                                        String emailText = emailField.getText().toString().trim();
                                        USER userList = new USER(emailText,message);
                                        mConditionRef.child(uid).setValue(userList);


                                        //loginState = 1; // 로그인 상태로 코드 바꿈
                                        //Intent intent = new Intent(MainActivity.this, BottomNaviActivity.class);
                                        //startActivity(intent);
                                    }else{
                                        Toast.makeText(MainActivity.this,"로그인 오류", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });

        userlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"유저리스트 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ListView01Activity.class);
                startActivity(intent);
            }
        });



        // 휴대폰 로컬에 저장된 파일 읽어오기
        DataLoadingfromLocal();
        if(DataSource.UserName !=null){
            EditUserName.setText(DataSource.UserName);
            Picasso.get().load(DataSource.profileUrl).into(ivProfile);
            isFirst=false; // 휴대폰 접속 기록 파악하기

        }
    }

    public void clickImage(View view) { // 갤러리로 가서 프로필 이미지 선택 후, 커스터마이징 가능하도록 구현
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri= data.getData();
                    // Glide는 메니페스트 파일에 퍼미션 없으면 사용 안된다. - 파베 공식문서에 따르면!!!
                    // 그러나 Picasso 라이브러리는 퍼미션 없어도 된다.!!
                    Picasso.get().load(imgUri).into(ivProfile);
                    //변경된 이미지가 있다.
                    isChanged=true;
                }
                break;
        }
    }
    public void EnterRoomBtn(View view) {

                // 변경사항없음
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "방 입장", Toast.LENGTH_SHORT).show();
                if (!isChanged && !isFirst) {
                    finish();
                } else {
                    DataSaving(); // 저장
                    Toast.makeText(getApplicationContext(), "저장_", Toast.LENGTH_SHORT).show();
                }

    }

    void DataSaving(){
        DataSource.UserName = EditUserName.getText().toString(); // 유저의 이름 가져오기 - global variable
        if(imgUri==null) return; // 만약에 어떤 이미지도 선택하지 않았다면,

        // 파베 스토리지에 올릴 이미지 명 만들기 - 유니크하게 만들려면 이게 나쁘지 않을 듯!!
        SimpleDateFormat uniqueFileName = new SimpleDateFormat("yyyyMddhhmmss"); // 년 월 일 - 이건 formatter에 대해서 찾아보면 형식 나오니까 금방임
        String fileName = uniqueFileName.format(new Date())+".png";

        //Firebase storage에 저장하기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        UploadTask uploadTask=imgRef.putFile(imgUri); // 스토리지 업로드
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // 이미지 업로드 성공 시, 파베 스토리지에서 바로 받아오게끔 구현
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // 파베 스토리지에서 다운할 수 있는 url 받아오기
                        DataSource.profileUrl= uri.toString();
                        Toast.makeText(MainActivity.this, "profile upload ok", Toast.LENGTH_SHORT).show();

                        //리얼타임 데베에 내 주소 가져오고
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference profileRef = firebaseDatabase.getReference("profiles"); // 거기에서 profile 객체 가져오기
                        // 스토리지 사용할때 자기 스토리지 주소에 cd~~ 하는걸 확인할 수 있는데 이걸 하드코딩으로 처리해도 가능하나
                        // 이렇게 코딩하면 하드코딩 없이 내 스토리지로 접근하는 cd~ 이 주소를 적어둘 수 있다.
                        // 다만 파이어베이스 특성상 스토리지도 결국은 네트워크 이용해 url을 받아오는 방식이다보니 약간 느린감은 있다.
                        // 모바일프로그래밍수업(임민규 교수님) - db쪽 보면 이 부분 나와있음!!
                        profileRef.child(DataSource.UserName).setValue(DataSource.profileUrl); // 유저네임을 key : value를 프로필 주소로
                        SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE); // 로컬에 저장
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("UserName", DataSource.UserName);
                        editor.putString("profileUrl", DataSource.profileUrl);
                        editor.commit();
                        Intent intent=new Intent(MainActivity.this, ChatActivity.class); // 저장후 채팅화면으로 변경
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
    void DataLoadingfromLocal(){ // 로컬에서 데이터 읽어오기
        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE); // 내 휴대폰 객체참조 - 모프 db관련 내용에 있다!!
        DataSource.UserName =preferences.getString("UserName", null); // 이름
        DataSource.profileUrl=preferences.getString("profileUrl", null); // 프로필 url


    }
}