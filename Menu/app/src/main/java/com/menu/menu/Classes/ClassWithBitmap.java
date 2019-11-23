package com.menu.menu.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ClassWithBitmap
{
    public Bitmap Picture;

    public String PictureToSql()
    {
        if(Picture == null)
        {
            return "null";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Picture.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        String temp = Base64.encodeToString(baos.toByteArray(), Base64.URL_SAFE);
        temp = temp.replaceAll("\n", "^");
        return temp;
    }

    public void SetPicutreFromSql(String sql)
    {
        if (sql.equals("null"))
        {
            Picture = null;
        }
        else
        {
            sql = sql.replaceAll("^","\n");

            try
            {
                sql = new String(sql.getBytes());
                byte[] decodedString = Base64.decode(sql, Base64.URL_SAFE);
                Picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            catch (Exception e)
            {
                Picture = null;
            }
        }
    }
}
