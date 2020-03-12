package com.menu.menu.Classes;

import java.util.ArrayList;

public class UsersCallback extends BaseCallback
{
    protected ArrayList<User> m_users = null;
    public void AddUsers(ArrayList<User> u)
    {
        m_users = u;
    }
}
