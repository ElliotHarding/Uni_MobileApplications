package com.menu.menu.Classes;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UsersCallback implements Callable<Void>
{
    private ArrayList<User> users = null;

    public void AddUsers(ArrayList<User> u)
    {
        users = u;
    }

    @Override
    public Void call() throws Exception
    {
        return null;
    }
}
