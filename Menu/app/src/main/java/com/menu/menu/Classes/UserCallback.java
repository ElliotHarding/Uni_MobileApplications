package com.menu.menu.Classes;

import java.util.concurrent.Callable;

public class UserCallback implements Callable<Void>
{
    private User user;

    public void SetUser(User u)
    {
        user = u;
    }

    @Override
    public Void call() throws Exception
    {
        return null;
    }
}
