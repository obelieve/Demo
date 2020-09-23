package com.github.obelieve.net;


import com.github.obelieve.bean.UploadTokenEntity;
import com.github.obelieve.bean.VersionUpdateEntity;
import com.github.obelieve.community.BuildConfig;
import com.github.obelieve.community.bean.BBSUserInfoEntity;
import com.github.obelieve.community.bean.BBSUserTrendsEntity;
import com.github.obelieve.community.bean.BBSUserZanEntity;
import com.github.obelieve.community.bean.CommentDetailsEntity;
import com.github.obelieve.community.bean.CommentListEntity;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReplyListEntity;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.bean.UpdateDetailEntity;
import com.github.obelieve.login.entity.SendSMSEntity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.main.UserNavInfoEntity;
import com.github.obelieve.me.bean.CenterMenuEntity;
import com.zxy.frame.net.ApiBaseResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Admin
 * on 2020/8/13
 */
public interface ServiceInterface {

    String BASE_URL = BuildConfig.BASE_URL;

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Header("RANGE") String downParam, @Url String fileUrl);

    /**
     * 获取用户个人信息接口（彩经版本）
     *
     * @return
     */
    @POST(UrlConst.USER_NAV_INFO)
    Observable<ApiBaseResponse<UserNavInfoEntity>> userNavInfo();

    @POST(UrlConst.CENTER_MENU)
    Observable<ApiBaseResponse<CenterMenuEntity>> centerMenuU();

    /**
     * 等级权益 - 每月金币领取
     *
     * @return
     */
    @POST(UrlConst.LEVEL_MONTH_COIN_DRAW)
    Observable<ApiBaseResponse<String>> levelMonthCoinDraw();

//
//    /**
//     * 金币中心
//     *
//     * @return
//     */
//    @POST(UrlConst.COIN_INDEX)
//    Observable<ApiBaseResponse<CoinIndexEntity>> coinIndex();
//
//    /**
//     * 金币明细
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(UrlConst.COIN_FLOW)
//    Observable<ApiBaseResponse<CoinFlowEntity>> coinFlow(@Field("month") String month);
//
//
//    /**
//     * 等级权益 - 每月金币领取
//     *
//     * @return
//     */
//    @POST(UrlConst.LEVEL_MONTH_COIN_DRAW)
//    Observable<ApiBaseResponse<String>> levelMonthCoinDraw();
//
//    /**
//     * 我的等级
//     *
//     * @return
//     */
//    @POST(UrlConst.LEVEL_INDEX)
//    Observable<ApiBaseResponse<LevelIndexEntity>> levelIndex();
//
//    /**
//     * 成长值明细
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(UrlConst.LEVEL_FLOW)
//    Observable<ApiBaseResponse<LevelFlowEntity>> levelFlow(@Field("page") int page);
//
//    /**
//     * 比赛赛况
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(UrlConst.MATCH_LIVE)
//    Observable<ApiBaseResponse<MatchLiveEntity>> matchLive(@Field("match_id") String match_id, @Field("count") Integer count);//count 可选 null
//
//    /**
//     * 分析
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(UrlConst.GET_MATCH_ANALYSIS_WITH_GOAL)
//    Observable<ApiBaseResponse<GetMatchAnalysisWithGoalEntity>> getMatchAnalysisWithGoal(@Field("match_id") String match_id, @Field("day_number") String day_number);
//
//    /**
//     * 金币充值页面列表接口
//     *
//     * @return
//     */
//    @POST(UrlConst.RECHARGE_LIST)
//    Observable<ApiBaseResponse<RechargeListEntity>> rechargeList();


    /********************* 社区 *********************/
    /**
     * 社区广场列表接口（搜索共用）
     *
     * @param page
     * @param keyword
     * @return
     */
    @POST(UrlConst.SQUARE_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<SquareListsEntity>> squarePost(@Field("page") int page, @Field("keyword") String keyword);

    /**
     * 社区精选列表接口
     *
     * @param page
     * @return
     */
    @POST(UrlConst.GOOD_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<SquareListsEntity>> goodPost(@Field("page") int page);

    /**
     * 帖子详情
     *
     * @param post_id
     * @return
     */
    @POST(UrlConst.POST_DETAIL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<UpdateDetailEntity>> postDetail(@Field("post_id") int post_id);

    /**
     * 帖子评论
     *
     * @param post_id
     * @return
     */
    @POST(UrlConst.COMMENT_LIST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<CommentListEntity>> commentList(@Field("post_id") int post_id, @Field("page") int page, @Field("sort") String sort, @Field("from_cid") Integer from_cid);

    /**
     * 点赞（取消）帖子\评论\回复接口
     *
     * @param post_id
     * @param comment_id int	否	评论\回复ID
     * @return
     */
    @POST(UrlConst.POST_ZAN_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<PraiseEntity>> zanPost(@Field("post_id") int post_id, @Field("comment_id") Integer comment_id);

    /**
     * 发表评论 （帖子进行回复）
     *
     * @param post_id
     * @param content
     * @return
     */
    @POST(UrlConst.SEND_COMMENT)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> sendPostComment(@Field("post_id") int post_id, @Field("content") String content);

    /**
     * 发表评论 (评论进行回复)
     *
     * @param post_id
     * @param content
     * @return
     */
    @POST(UrlConst.SEND_COMMENT)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> sendPostComment(@Field("post_id") int post_id, @Field("content") String content, @Field("comment_id") int comment_id);

    /**
     * 发表评论 （对回复进行回复）
     *
     * @param post_id
     * @param content
     * @return
     */
    @POST(UrlConst.SEND_COMMENT)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> sendPostComment(@Field("post_id") int post_id, @Field("content") String content, @Field("comment_id") int comment_id,
                                                        @Field("to_uid") int to_uid, @Field("to_uname") String to_uname, @Field("to_rid") int to_rid);

    /**
     * 删除帖子\评论\回复接口
     *
     * @param p_id   帖子ID\评论ID\回复ID
     * @param p_type post-帖子；comment-评论；reply-回复
     */
    @POST(UrlConst.DEL_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> delPost(@Field("p_id") int p_id, @Field("p_type") String p_type);


    /**
     * 获取举报类型接口
     */
    @POST(UrlConst.GET_REPORT_TYPE)
    Observable<ApiBaseResponse<ReportTypeEntity>> getReportType();


    /**
     * 举报帖子\评论\回复接口
     *
     * @param p_id   帖子ID\评论ID\回复ID
     * @param rt_id  举报类型ID
     * @param p_type post-帖子；comment-评论；reply-回复
     */
    @POST(UrlConst.REPORT_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> reportPost(@Field("p_id") int p_id, @Field("rt_id") int rt_id, @Field("p_type") String p_type);


    /**
     * 获取我的消息下 评论详情接口
     *
     * @param comment_id int	是	评论ID
     */
    @POST(UrlConst.POST_GET_COMMENT)
    @FormUrlEncoded
    Observable<ApiBaseResponse<CommentDetailsEntity>> getComment(@Field("comment_id") int comment_id);


    /**
     * 评论下回复列表接口
     *
     * @param comment_id int	是	评论ID
     * @param page       页码，默认1
     * @param sort       排序方式，zan_num - 热度；created_at - 时间；默认热度
     * @param from_cid
     */
    @POST(UrlConst.GET_REPLY_LIST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<ReplyListEntity>> getReplyList(@Field("comment_id") int comment_id, @Field("page") int page, @Field("sort") String sort, @Field("from_cid") Integer from_cid);

    /**
     * 客户端错误收集接口
     *
     * @param log
     */
    @POST(UrlConst.ERROR_LOG)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> errorLog(@Field("log") String log);


    /**
     * 七牛上传凭证接口
     */
    @POST(UrlConst.QINIU_TOKEN_URL)
    Observable<ApiBaseResponse<UploadTokenEntity>> uploadToken();

    /**
     * 发布帖子
     *
     * @param content     帖子文字内容
     * @param media       string	否	媒体的七牛地址（多个用英文逗号拼接“,”），当content为空时，此字段不能为空
     * @param media_type  string	否	媒体类型；image-图片，video-视频；默认image
     * @param media_scale string	否	图片原始宽高（100,200）
     * @return
     */
    @POST(UrlConst.SEND_POST)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> sendPost(@Field("content") String content, @Field("media") String media,
                                                 @Field("media_type") String media_type, @Field("media_scale") String media_scale);

    /** 登录 **/

    /**
     * 发送验证码
     *
     * @param username
     * @param sms_type
     * @param open_type
     * @param open_id
     * @param validate_code
     * @return
     */
    @POST(UrlConst.SMS_URL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<SendSMSEntity>> sendSMS(@Field("username") String username, @Field("sms_type") String sms_type,
                                                       @Field("open_type") String open_type, @Field("open_id") String open_id, @Field("validate_code") String validate_code);

    /**
     * 登录注册
     *
     * @param username
     * @param code
     * @param umeng_token
     * @return
     */
    @POST(UrlConst.USER_LOGIN_URL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<UserEntity>> login(@Field("username") String username, @Field("code") String code, @Field("umeng_token") String umeng_token);


    /**
     * 第三方登录注册
     *
     * @param third_type
     * @param open_id
     * @param access_token
     * @return
     */
    @POST(UrlConst.USER_THIRD_LOGIN_URL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<UserEntity>> thridLogin(@Field("third_type") String third_type, @Field("open_id") String open_id,
                                                       @Field("access_token") String access_token, @Field("umeng_token") String umeng_token);


    /**
     * 第三方绑定手机
     *
     * @param username
     * @param code
     * @param open_type
     * @param open_id
     * @param umeng_token
     * @return
     */
    @POST(UrlConst.USER_BIND_PHONE_URL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<UserEntity>> thridBindPhone(@Field("username") String username, @Field("code") String code,
                                                           @Field("open_type") String open_type, @Field("open_id") String open_id,
                                                           @Field("umeng_token") String umeng_token);


    /**
     * 解除手机号码绑定
     *
     * @param code
     * @return
     */
    @POST(UrlConst.UN_BIND_MOBILE)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> unbindMobile(@Field("code") String code);

    /**
     * 手机号码绑定
     *
     * @param code
     * @return
     */
    @POST(UrlConst.BIND_MOBILE)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> bindMobile(@Field("mobile") String mobile, @Field("code") String code);

    /**
     * 账号注销
     *
     * @param code
     * @return
     */
    @POST(UrlConst.USER_CANCEL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> cancelAccount(@Field("mobile") String mobile, @Field("code") String code);

    /**
     * 退出登录
     * @return
     */
    @POST(UrlConst.USER_LOGINOUT_URL)
    Observable<ApiBaseResponse<String>> logout();

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST(UrlConst.USER_INFO_URL)
    Observable<ApiBaseResponse<UserEntity>> getUserInfo();

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST(UrlConst.USER_UPDATE_INFO_URL)
    @FormUrlEncoded
    Observable<ApiBaseResponse<String>> updateUserInfo(@Field("nickname")String nickname, @Field("sex")String sex, @Field("city")String city,
                                                       @Field("birthday")String birthday, @Field("avatar")String avatar);


    /**个人主页**/

    /**
     * 个人主页基础信息接口
     *
     * @param user_id
     * @return
     */
    @POST(UrlConst.BBS_USER_INFO)
    @FormUrlEncoded
    Observable<ApiBaseResponse<BBSUserInfoEntity>> bbsUserInfo(@Field("user_id") int user_id);

    /**
     * 个人主页动态列表接口
     *
     * @param user_id
     * @return
     */
    @POST(UrlConst.BBS_USER_TRENDS)
    @FormUrlEncoded
    Observable<ApiBaseResponse<BBSUserTrendsEntity>> bbsUserTrends(@Field("user_id") int user_id, @Field("page") int page);

    /**
     * 个人主页点赞列表接口
     *
     * @param user_id
     * @return
     */
    @POST(UrlConst.BBS_USER_ZAN)
    @FormUrlEncoded
    Observable<ApiBaseResponse<BBSUserZanEntity>> bbsUserZan(@Field("user_id") int user_id, @Field("page") int page);

    /**
     * 版本更新
     *
     * @return
     */
    @POST(UrlConst.VERSION_CHECK_URL)
    Observable<ApiBaseResponse<VersionUpdateEntity>> versionUpdate();
}
