package com.menu.menu.Classes;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalSettings
{
    private static User m_localUser = null;

    public static void LoadSettings(Context context)
    {
        m_localUser = new User();

        SharedPreferences userSettings = context.getSharedPreferences("UserInfo", 0);

        m_localUser.setUsername(userSettings.getString("Username", ""));
        m_localUser.setPassword(userSettings.getString("Password", ""));
    }

    public static void UpdateLocalUser(User u)
    {
        m_localUser = u;
    }

    public static User GetLocalUser()
    {
        return m_localUser;
    }

    public static boolean IsLoginSaved()
    {
        return (m_localUser != null || (m_localUser.getPassword() != null && m_localUser.getUsername() != null));
    }

    public static void SaveLoginDetails(String user, String pass, Context context)
    {
        SharedPreferences userSettings = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("Username",user);
        editor.putString("Password",pass);
        editor.commit();
    }

}
