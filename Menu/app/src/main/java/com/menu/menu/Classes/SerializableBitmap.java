package com.menu.menu.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializableBitmap implements Serializable
{
    private Bitmap m_picture;

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

    // Converts the Bitmap into a byte array for serialization
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        m_picture.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        m_picture = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
