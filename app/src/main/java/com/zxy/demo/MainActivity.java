package com.zxy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    @ViewInject(R.id.iv)
    ImageView mIv;
    @ViewInject(R.id.btn)
    Button mBtn;
    @ViewInject(R.id.tv)
    TextView mTv;

    boolean mDbOrHttp;//点击切换使用 database or http
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewInject();
        imageLoader();
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (mDbOrHttp)
        {
            mDbOrHttp = false;
            database();
        } else
        {
            mDbOrHttp = true;
            http();
        }
    }


    /**
     * 初始化xUtils3
     */
    private void init()
    {
        x.Ext.init(getApplication());
        x.Ext.setDebug(false);
    }

    private void viewInject()
    {
        x.view().inject(this);
    }

    private void imageLoader()
    {
        x.image().bind(mIv, "http://i3.sinaimg.cn/home/2013/0331/U586P30DT20130331093840.png");
    }

    /**
     * TableA static class
     */
    private void database()
    {
        long startT = System.currentTimeMillis();
        List<TableA> list = new ArrayList<>();
        DbManager dbManager = x.getDb(new DbManager.DaoConfig().setDbDir(getExternalFilesDir(null)).setDbName("a.db").setDbVersion(1));
        try
        {
            TableA tableA = new TableA();
            for (int i = 1; i < 2; i++)
            {
                tableA.setId(i);
                tableA.setName("1111");
                list.add(tableA);
            }
            dbManager.save(list);

            tableA.setId(2);
            tableA.setName("2222");
            dbManager.update(tableA);

            tableA.setId(3);
            tableA.setName("33");
            dbManager.update(tableA);

            dbManager.deleteById(TableA.class, 1);

            list = dbManager.selector(TableA.class).findAll();
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("耗时：" + (System.currentTimeMillis() - startT) + "ms\n");
        builder.append("list:" + list + "\n");
        mTv.setText(builder.toString());
    }

    private void http()
    {
        RequestParams params = new RequestParams("https://www.douban.com");
        x.http().get(params, new Callback.CommonCallback<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                LogUtil.i();
                mTv.setText(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                LogUtil.i();
            }

            @Override
            public void onCancelled(CancelledException cex)
            {
                LogUtil.i();
            }

            @Override
            public void onFinished()
            {
                LogUtil.i();
            }
        });
    }

    @Table(name = "tableA")
    public static class TableA
    {

        @Column(name = "id", isId = true)
        private int id;
        @Column(name = "name")
        private String name;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "TableA{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


}
