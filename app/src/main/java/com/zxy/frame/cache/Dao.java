package com.zxy.frame.cache;

import com.zxy.frame.json.JsonUtil;
import com.zxy.frame.utility.UContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Dao
{
    public static final String DIR;

    static
    {
        DIR = UContext.getContext().getExternalFilesDir("") + File.separator + "cache";
        File file = new File(DIR);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }

    public static <T> void insertOrUpdate(String json, Class<T> clazz)
    {
        File file = new File(DIR, clazz.getSimpleName());
        if (file.exists())
        {
            file.delete();
        } else
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(file);
            writer.write(json);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally
        {
            try
            {
                if (writer != null)
                    writer.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static <T> void insertOrUpdate(T model, Class<T> clazz)
    {
        insertOrUpdate(JsonUtil.parseObject(model), clazz);
    }

    public static <T> boolean delete(Class<T> clazz)
    {
        File file = new File(DIR, clazz.getSimpleName());
        if (file.exists())
        {
            return file.delete();
        }
        return true;
    }

    public static <T> T query(Class<T> clazz)
    {
        T model = JsonUtil.parseJson(queryForJson(clazz), clazz);
        return model;
    }

    public static <T> String queryForJson(Class<T> clazz)
    {
        StringBuilder builder = new StringBuilder();
        File file = new File(DIR, clazz.getSimpleName());
        if (file.exists())
        {
            BufferedReader reader = null;
            try
            {
                reader = new BufferedReader(new FileReader(file));
                String string = null;
                if ((string = reader.readLine()) != null)
                {
                    builder.append(string);
                    builder.append("\n");
                }
            } catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            } finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return builder.toString();
    }
}
