package com.example.cdstermproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    ArrayList<MessageItem> messageItems;
    LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<MessageItem> messageItems, LayoutInflater layoutInflater) {
        // 어댑터 연결하기
        /*
        어댑터 iOS로 치면 delegate 및 dataSource와 비슷하다
         */
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        MessageItem item = messageItems.get(position); // 현재 보여줄 메시지 아이템 포지션을 생성
        View ViewOfItem = null; // 리사이클 뷰 사용하지 않겠다

        /*
        내 메시지인지 아니면 상대방의 메시지인지 확인이 필요하다.
         */
        if(item.getName().equals(DataSource.UserName)){  // 유저 네임이 일치하여 나의 메시지이면
            ViewOfItem= layoutInflater.inflate(R.layout.my_msgbox,viewGroup,false); // 왼쪽에 표시되는 메시지 박스 사용
        }else{
            ViewOfItem= layoutInflater.inflate(R.layout.other_msgbox,viewGroup,false); // 오른쪽에 표시되는 메시지 박스 사용
        }

        // 만들어진 아이템 뷰에 대하여 값을 설정해줘야함
        /*
        iOS로 따지자면 커스텀하는 방식으로 상당이지만 자바에서도 이 개념을 비슷하게 차용된다.
         */

        CircleImageView CircleImageView = ViewOfItem.findViewById(R.id.CircleImageView);
        TextView textViewName = ViewOfItem.findViewById(R.id.TextView_name);
        TextView textViewMsg = ViewOfItem.findViewById(R.id.TextView_Msg);
        TextView textViewTime = ViewOfItem.findViewById(R.id.TextView_Time);

        textViewName.setText(item.getName());
        textViewMsg.setText(item.getMessage());
        textViewTime.setText(item.getTime());

        Glide.with(ViewOfItem).load(item.getPofileUrl()).into(CircleImageView);

        return ViewOfItem;
    }
}