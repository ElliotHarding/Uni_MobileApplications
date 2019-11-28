package com.menu.menu.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ClassWithBitmap
{
    public Bitmap m_picture;

    public String pictureToSql()
    {
        if(m_picture == null)
        {
            return "null";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m_picture.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        String temp = Base64.encodeToString(baos.toByteArray(), Base64.URL_SAFE);
        temp = temp.replaceAll("\n", "^");
        return temp;
    }

    public void setPicutreFromSql(String sql)
    {
        if (sql.equals("null"))
        {
            m_picture = null;
        }
        else
        {
            sql = sql.replaceAll("^","\n");

            try
            {
                sql = new String(sql.getBytes());
                byte[] decodedString = Base64.decode(sql, Base64.URL_SAFE);
                m_picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            catch (Exception e)
            {
                m_picture = null;
            }
        }
    }

    public Bitmap getPicture()
    {
        return m_picture;
    }

    public void setPicture(Bitmap b)
    {
        m_picture = b;
    }
}
