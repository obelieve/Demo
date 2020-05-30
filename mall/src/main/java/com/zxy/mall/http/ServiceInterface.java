package com.zxy.mall.http;

import com.zxy.frame.net.BaseResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zxy on 2019/08/06.
 */
public interface ServiceInterface {

    String BASE_URL = "http://api.dev.2048.com";
    String GET_TAB = "/api/get_tab";
    String SQUARE_POST = "/api/square_post";
    String VERSION_CHECK = "/api/version_check";

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("RANGE") String downParam, @Url String fileUrl);

//    @POST(GET_TAB)
//    Call<ResponseBody> get_tab();
//
//    /**
//     * 社区广场列表
//     *
//     * @param page
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(SQUARE_POST)
//    Observable<BaseResponse<SquarePostEntity>> square_post(@Field("page") int page);
//
//    /**
//     * 版本更新
//     *
//     * @return
//     */
//    @POST(VERSION_CHECK)
//    Observable<BaseResponse<VersionUpdateEntity>> version_check();

}
