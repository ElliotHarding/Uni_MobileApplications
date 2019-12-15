package com.menu.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BasketItem;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Meal;
import com.menu.menu.Classes.OrdersCallback;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

import java.util.ArrayList;

public class MeetupChat extends AppCompatActivity
{
    private User m_eater = null;
    private User m_chef = null;
    private boolean m_bForChef = false;
    private boolean m_bTakeaway = false;
    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private ArrayList<String> m_messages = new ArrayList<>();
    private TextView txt_addressLine1;
    private TextView txt_addressLine2;
    private TextView txt_addressLine3;
    private TextView txt_postCode;
    private TextView txt_meetingName;
    private TextView txt_state;
    private EditText input_message;
    private ListView listView_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_chat);

        txt_addressLine1 = findViewById(R.id.txt_addressLine1);
        txt_addressLine2 = findViewById(R.id.txt_addressLine2);
        txt_addressLine3 = findViewById(R.id.txt_addressLine3);
        txt_postCode = findViewById(R.id.txt_postCode);
        txt_meetingName = findViewById(R.id.txt_meetingName);
        txt_state = findViewById(R.id.txt_state);
        input_message = findViewById(R.id.txt_messageInput);
        listView_messages = findViewById(R.id.listView_messages);

        findViewById(R.id.btn_postMessage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String message = (LocalSettings.GetLocalUser().getId() == m_eater.getId() ? "e" : "c") + input_message.getText().toString();
                m_messages.add(message);
                listView_messages.setAdapter(new MessageListViewAdapter(getApplicationContext(), m_messages));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("orderId") && extras.containsKey("forChef") && extras.containsKey("isTakeaway"))
        {
            String orderId = extras.getString("orderId");
            m_bForChef = extras.getBoolean("forChef");
            m_bTakeaway = extras.getBoolean("isTakeaway");
        }
        else
        {
            SetError("Data not found! Check internet?");
        }
    }

    private class GetOrderInfoCallback extends OrdersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_orders != null && !m_orders.isEmpty())
            {
                String[] names = m_orders.get(0).getId().split(",");

                OtherUserInfoCallback ouic = new OtherUserInfoCallback();

                if(names[0].equals(LocalSettings.GetLocalUser().getId()))
                {
                    m_eater = LocalSettings.GetLocalUser();
                    ouic.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE id='" + names[1] +"';");
                }
                else
                {
                    m_chef = LocalSettings.GetLocalUser();
                    ouic.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE id='" + names[0] +"';");
                }

                m_dbComms.RequestUserData(ouic);
            }
            else
            {
                SetError("Data not found! Check internet?");
            }
            return null;
        }
    }

    private class OtherUserInfoCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_users != null && !m_users.isEmpty())
            {
                if(m_eater == null)
                {
                    m_eater = m_users.get(0);

                    txt_meetingName.setText("Meeting: " + m_eater.getUsername());

                    if(m_bTakeaway)
                    {
                        txt_addressLine1.setText(m_eater.getAddressLine1());
                        txt_addressLine2.setText(m_eater.getAddressLine2());
                        txt_addressLine3.setText(m_eater.getAddressLine3());
                        txt_postCode.setText(m_eater.getAddressPostCode());
                    }
                }
                else
                {
                    m_chef = m_users.get(0);

                    txt_meetingName.setText("Meeting: " + m_chef.getUsername());

                    if(!m_bTakeaway)
                    {
                        txt_addressLine1.setText(m_chef.getAddressLine1());
                        txt_addressLine2.setText(m_chef.getAddressLine2());
                        txt_addressLine3.setText(m_chef.getAddressLine3());
                        txt_postCode.setText(m_chef.getAddressPostCode());
                    }
                    else
                    {
                        txt_state.setText("On route");
                    }
                }
            }
            else
            {
                SetError("Data not found! Check internet?");
            }
            return null;
        }
    }

    public class MessageListViewAdapter extends ArrayAdapter<String>
    {
        ArrayList<String> m_messageListRef;

        public MessageListViewAdapter(Context c, ArrayList<String> messageListRef)
        {
            super(c, R.layout.layout_meal_info, messageListRef);
            m_messageListRef = messageListRef;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            String message = m_messageListRef.get(position);

            String[] messageAndType = message.split("$");

            View itemView = convertView;
            if (itemView == null)
            {
                itemView = (LayoutInflater.from(getContext())).inflate(messageAndType[0].equals("c") ? R.layout.chef_message : R.layout.eater_message, parent, false);
            }

            //Subject text
            TextView subjectText = itemView.findViewById(R.id.textView);
            subjectText.setText(messageAndType[1]);

            return itemView;
        }
    }


    private void SetError(String errorString)
    {
        Toast t = Toast.makeText(MeetupChat.this, errorString, Toast.LENGTH_LONG);
        t.show();
    }
}
