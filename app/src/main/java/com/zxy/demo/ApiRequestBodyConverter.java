package com.zxy.demo;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class ApiRequestBodyConverter implements Converter<File, RequestBody> {
    @Nullable
    @Override
    public RequestBody convert(File value) throws IOException {
        RequestBody bodyParams = RequestBody.create(value,MediaType.parse("image/png"));
        return bodyParams;
    }
}
