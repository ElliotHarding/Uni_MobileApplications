package com.menu.menu.Classes;

import java.util.ArrayList;

public class UsersCallback extends BaseCallback
{
    private ArrayList<User> users = null;
    public void AddUsers(ArrayList<User> u)
    {
        users = u;
    }
}
