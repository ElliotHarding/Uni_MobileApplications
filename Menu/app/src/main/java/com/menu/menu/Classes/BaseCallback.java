package com.menu.menu.Classes;

import java.util.concurrent.Callable;

public class BaseCallback implements Callable<Void>
{
    protected String m_message = null;
    public void SetMessage(String message)
    {
        m_message = message;
    }
    String GetMessage()
    {
        return m_message;
    }

    @Override
    public Void call() throws Exception
    {
        return null;
    }
}
