package com.menu.menu.Classes;

import com.menu.menu.Login;

public class DatabaseCommunicator
{
    public enum LoginOption
    {
        Username,
        Email
    }

    public boolean TryLogin(String usernameOrEmail, String pass, LoginOption option)
    {
        String statement = "SELECT * FROM USERS WHERE Password = " + pass + " AND ";

        switch (option)
        {
            case Username:
                statement += "Username = ";
                break;
            case Email:
                statement += "Email = ";
                break;
        }

        statement += usernameOrEmail;

        User u = GetUser(statement);
        if (u == null)
            return false;

        u.LoggedIn = true;

        return UpdateUser(u);
    }

    public User GetUserViaUsername(String username)
    {
        return GetUser("Select * FROM USERS WHERE id = " + username);
    }

    public User GetUserViaEmail(String email)
    {
        return GetUser("Select * FROM USERS WHERE id = " + email);
    }

    public boolean UpdateUser(User u)
    {
        return true;
    }

    public boolean AddUser(User user)
    {
        return true;
    }

    private User GetUser(String selectStatement)
    {
        return new User();
    }



}
