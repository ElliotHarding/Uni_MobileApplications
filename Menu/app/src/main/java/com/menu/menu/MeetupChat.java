package com.menu.menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.menu.menu.Classes.BaseCallback;
import com.menu.menu.Classes.DatabaseCommunicator;
import com.menu.menu.Classes.LocalSettings;
import com.menu.menu.Classes.Order;
import com.menu.menu.Classes.OrdersCallback;
import com.menu.menu.Classes.User;
import com.menu.menu.Classes.UsersCallback;

import java.util.ArrayList;

public class MeetupChat extends AppCompatActivity
{
    private User m_eater = null;
    private User m_chef = null;
    private boolean m_bThisIsChef = false;
    private boolean m_bTakeaway = false;

    private DatabaseCommunicator m_dbComms = new DatabaseCommunicator();
    private TextView txt_addressLine1;
    private TextView txt_addressLine2;
    private TextView txt_addressLine3;
    private TextView txt_postCode;
    private TextView txt_meetingName;
    private TextView txt_state;
    private EditText input_message;
    private ListView listView_messages;
    private ProgressBar m_progressBar;
    private String m_orderId;
    private ArrayList<String> m_messages = new ArrayList<>();
    private Thread m_updateThread = null;
    private boolean m_bContinueUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_chat);

        txt_addressLine1 = findViewById(R.id.txt_addressLine1);
        txt_addressLine2 = findViewById(R.id.txt_addressLine2);
        txt_addressLine3 = findViewById(R.id.txt_addressLine3);
        txt_postCode = findViewById(R.id.txt_addressPostCode);
        txt_meetingName = findViewById(R.id.txt_meetingName);
        input_message = findViewById(R.id.txt_messageInput);
        listView_messages = findViewById(R.id.listView_messages);
        m_progressBar = findViewById(R.id.progressBar);

        txt_meetingName.setVisibility(View.INVISIBLE);
        txt_addressLine1.setVisibility(View.INVISIBLE);
        txt_addressLine2.setVisibility(View.INVISIBLE);
        txt_addressLine3.setVisibility(View.INVISIBLE);
        txt_postCode.setVisibility(View.INVISIBLE);
        txt_state.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("orderId"))
        {
            m_orderId = extras.getString("orderId");

            GetOrderInfoCallback goic = new GetOrderInfoCallback();
            goic.SetMessage("Select * FROM " + m_dbComms.m_orderTable + " WHERE id = '" + m_orderId + "';");
            m_dbComms.RequestOrderData(goic);

            m_progressBar.startNestedScroll(1);
            m_progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            SetError("Data not found! Check internet?");
        }

        findViewById(R.id.btn_postMessage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(m_eater == null)
                    return;

                //Get message & add to list
                String message = (LocalSettings.GetLocalUser().getId() == m_eater.getId() ? "e -z- " : "c -z- ") + input_message.getText().toString();
                m_messages.add(message);

                //Create temp order object to utilize sql function for uploading
                Order tempOrder = new Order();
                tempOrder.setMessages(m_messages);

                //Upload new message list
                AddMessageCallback amcb = new AddMessageCallback();
                amcb.SetMessage("UPDATE " + m_dbComms.m_orderTable + " SET messages='" + tempOrder.getMessages_sql() + "' WHERE id='" + m_orderId + "';");
                m_dbComms.GenericUpload(amcb);

                //Clear message input box
                input_message.setText("");
            }
        });

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MarkCompleteCallback mcc = new MarkCompleteCallback();
                mcc.SetMessage("DELETE FROM " + m_dbComms.m_orderTable + " WHERE id='" + m_orderId + "';");
                m_dbComms.GenericUpload(mcc);
            }
        });

        //Update messages every second
        m_bContinueUpdate = true;
        m_updateThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    while (!isInterrupted() && m_bContinueUpdate)
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                UpdateMessages();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
    }

    @Override
    public void onBackPressed()
    {
        m_bContinueUpdate = false;
        m_updateThread = null;
        super.onBackPressed();
    }

    @Override
    protected void onStop()
    {
        m_bContinueUpdate = false;
        m_updateThread = null;
        super.onStop();
    }

    private void UpdateMessages()
    {
        UpdateMessagesCallback umcb = new UpdateMessagesCallback();
        umcb.SetMessage("SELECT * FROM " + m_dbComms.m_orderTable + " WHERE id='" + m_orderId + "';");
        m_dbComms.RequestOrderData(umcb);
    }

    private class MarkCompleteCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_message.equals("null"))
            {
                m_bContinueUpdate = false;
                m_updateThread = null;
                startActivity(new Intent(MeetupChat.this, MainHub.class));
            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Failed. Check internet?");
                    }
                });
            }
            return null;
        }
    }

    private class AddMessageCallback extends BaseCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(!m_message.equals("null"))
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Failed to add message. Check internet?");
                    }
                });
            }
            return null;
        }
    }

    private class UpdateMessagesCallback extends OrdersCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_orders != null && !m_orders.isEmpty())
                    {
                        m_messages = m_orders.get(0).getMessages();
                        if(m_messages.get(0).equals("null"))
                            m_messages.remove(0);

                        listView_messages.setAdapter(new MessageListViewAdapter(getApplicationContext(), m_messages));
                    }
                    else
                    {
                        SetError("Failed to update chat! Check internet?");
                    }
                }
            });
            return null;
        }
    }

    private class GetOrderInfoCallback extends OrdersCallback
    {
        @Override
        public Void call() throws Exception
        {
            if(m_orders != null && !m_orders.isEmpty())
            {
                OtherUserInfoCallback ouic = new OtherUserInfoCallback();
                if(m_orders.get(0).getEaterId().equals(LocalSettings.GetLocalUser().getId()))
                {
                    m_eater = LocalSettings.GetLocalUser();
                    ouic.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE id='" + m_orders.get(0).getChefId() +"';");
                }
                else
                {
                    m_chef = LocalSettings.GetLocalUser();
                    ouic.SetMessage("SELECT * FROM " + m_dbComms.m_userTable + " WHERE id='" + m_orders.get(0).getEaterId() +"';");
                }

                m_dbComms.RequestUserData(ouic);
            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SetError("Data not found! Check internet?");
                    }
                });

                m_progressBar.setVisibility(View.INVISIBLE);
                m_progressBar.stopNestedScroll();
            }
            return null;
        }
    }

    private class OtherUserInfoCallback extends UsersCallback
    {
        @Override
        public Void call() throws Exception
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(m_users != null && !m_users.isEmpty())
                    {
                        if(m_eater == null)
                        {
                            m_eater = m_users.get(0);

                            txt_meetingName.setText("Meeting: " + m_eater.getUsername());
                            txt_meetingName.setVisibility(View.VISIBLE);

                            if(m_bTakeaway)
                            {
                                txt_addressLine1.setText(m_eater.getAddressLine1());
                                txt_addressLine2.setText(m_eater.getAddressLine2());
                                txt_addressLine3.setText(m_eater.getAddressLine3());
                                txt_postCode.setText(m_eater.getAddressPostCode());

                                txt_addressLine1.setVisibility(View.VISIBLE);
                                txt_addressLine2.setVisibility(View.VISIBLE);
                                txt_addressLine3.setVisibility(View.VISIBLE);
                                txt_postCode.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            m_chef = m_users.get(0);

                            txt_meetingName.setText("Meeting: " + m_chef.getUsername());
                            txt_meetingName.setVisibility(View.VISIBLE);

                            if(!m_bTakeaway)
                            {
                                txt_addressLine1.setText(m_chef.getAddressLine1());
                                txt_addressLine2.setText(m_chef.getAddressLine2());
                                txt_addressLine3.setText(m_chef.getAddressLine3());
                                txt_postCode.setText(m_chef.getAddressPostCode());

                                txt_addressLine1.setVisibility(View.VISIBLE);
                                txt_addressLine2.setVisibility(View.VISIBLE);
                                txt_addressLine3.setVisibility(View.VISIBLE);
                                txt_postCode.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                txt_state.setText("On route");
                                txt_state.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else
                    {
                        SetError("Data not found! Check internet?");
                    }
                    m_progressBar.setVisibility(View.INVISIBLE);
                    m_progressBar.stopNestedScroll();
                    m_bContinueUpdate = true;
                    m_updateThread.start();
                }
            });
            return null;
        }
    }

    public class MessageListViewAdapter extends ArrayAdapter<String>
    {
        public ArrayList<String> m_messageListRef;

        public MessageListViewAdapter(Context c, ArrayList<String> messageListRef)
        {
            super(c, R.layout.layout_message_eater, messageListRef);
            m_messageListRef = messageListRef;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            String message = m_messageListRef.get(position);

            String[] messageAndType = message.split(" -z- ");

            View itemView = convertView;
            if (itemView == null)
            {
                itemView = (LayoutInflater.from(getContext())).inflate(messageAndType[0].equals("c") ? R.layout.layout_message_chef : R.layout.layout_message_eater, parent, false);
            }

            //Subject text
            TextView subjectText;
            if(messageAndType[0].equals("c"))
            {
                subjectText = itemView.findViewById(R.id.txt_chefMessage);
            }
            else
            {
                subjectText = itemView.findViewById(R.id.txt_eaterMessage);
            }
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
