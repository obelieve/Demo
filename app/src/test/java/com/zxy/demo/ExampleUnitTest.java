package com.zxy.demo;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test()
    {
        HttpRequest request = HttpRequest.get("https://www.baidu.com");
        response_print(request);
    }

    private void response_print(HttpRequest request)
    {
        System.out.println(request.code());
        System.out.println(request.body());
    }

    @Test
    public void testMultipart_formData()
    {
        String url = "http://api.im.fanwe.cn/uploads?_token=U_e720375169e100e069a36dc2d4a08dc45c99dc8c8741f&scene=social";
        HttpRequest request = HttpRequest.post(url);
        File file = new File("D:\\Downloads\\首页.png");
        System.out.println(file.getName() + " " + file.length());
        request.part("files[]", file.getName(), file);
        response_print(request);
    }
}