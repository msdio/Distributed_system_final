package com.example.cdstermproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    ArrayList<user_login> UserList=  new ArrayList<>();

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public user_login getItem(int position) {
        return UserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView user_email = (TextView)convertView.findViewById(R.id.user_email);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.on_off_button);

        user_login UserList1 = getItem(position);

        user_email.setText(UserList1.getName());
        imageView.setImageDrawable(UserList1.getPoster());



        return convertView;

    }

    public void addItem(String name, Drawable imageView) {

        user_login UserList1 = new user_login();

        /* MyItem에 아이템을 setting한다. */
        UserList1.setName(name);
        UserList1.setPoster(imageView);

        /* mItems에 MyItem을 추가한다. */
        UserList.add(UserList1);

    }

}

//            android:src="@drawable/online"