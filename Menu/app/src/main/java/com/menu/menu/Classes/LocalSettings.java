package com.menu.menu.Classes;

public class LocalSettings
{
    public static User LocalUser = null;

    public static void LoadSettings()
    {

    }

    public static void UpdateLocalUser(User u)
    {
        LocalUser = u;
    }

    public static boolean IsLoginSaved()
    {
        return (LocalUser.Password != null && LocalUser.Username != null);
    }
}
