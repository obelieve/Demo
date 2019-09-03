package com.zxy.demo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.zxy.demo.algorithm.AStarAlgorithm;
import com.zxy.demo.floor.FloorInfo;
import com.zxy.demo.floor.FloorMap;
import com.zxy.demo.floor.FloorProcessor;
import com.zxy.demo.utils.RouteMapUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 显示路线ImageView
 * Created by zxy on 2019/09/02.
 */
@SuppressLint("AppCompatCustomView")
public class RouteImageView extends ImageView {

    /**
     * 需要分段显示路线时，所需的间隔时间
     */
    private static final int ROUTE_DELAY_TIME = 500;
    private Gson mGson = new Gson();
    private Disposable mDisposable;

    public RouteImageView(Context context) {
        super(context);
    }

    public RouteImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RouteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 显示路线
     *
     * @param assetsImagePath assets目录下的原图
     * @param assetsJsonPath  assets目录下的原图json信息
     * @param assetsDataPath  assets目录下的原图二维数组映射信息
     * @param startPosition   起点
     * @param endPosition     终点
     */
    public void setRouteImageBitmap(final String assetsImagePath, final String assetsJsonPath, final String assetsDataPath, final String startPosition, final String endPosition) {
        mDisposable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                try {
                    Bitmap bitmap = RouteMapUtil.getBitmap(RouteMapUtil.getAssetsFile(assetsImagePath));
                    emitter.onNext(bitmap);
                    if (TextUtils.isEmpty(startPosition) || TextUtils.isEmpty(endPosition) || startPosition.equals(endPosition)) {
                        emitter.onComplete();
                        return;
                    }
                    FloorInfo floorInfo = mGson.fromJson(new BufferedReader(new InputStreamReader(RouteMapUtil.getAssetsFile(assetsJsonPath))), FloorInfo.class);
                    FloorProcessor floorProcessor = new FloorProcessor(floorInfo);
                    FloorMap floorMap = new FloorMap(FloorProcessor.surfaceFromFile(RouteMapUtil.getAssetsFile(assetsDataPath)));
                    floorProcessor.setFloorMap(floorMap);
                    List<AStarAlgorithm.Node> list = floorProcessor.genFloorRouteNodes(startPosition, endPosition);

                    if (list.size() >= 2) {
                        int startX = list.get(0).X;
                        int startY = list.get(0).Y;
                        int endX = list.get(list.size() - 1).X;
                        int endY = list.get(list.size() - 1).Y;
                        int distance = Math.abs(endY - startY) + Math.abs(endX - startX);
                        final int DEF_DISTANCE = (floorInfo.getWidth() + floorInfo.getHeight()) / floorInfo.getZoom_size() / 10;//设置默认每秒显示的距离
                        if (DEF_DISTANCE == 0 || distance < 2 * DEF_DISTANCE || list.size() == 2) {//直接显示起始点路线
                            Bitmap tempBitmap = floorProcessor.genFloorRouteBitmap(bitmap, list);
                            bitmap.recycle();
                            bitmap = tempBitmap;
                            emitter.onNext(bitmap);
                        } else {//间隔一段时间，显示一段路线
                            int split = distance / DEF_DISTANCE;
                            int increase = list.size() / split;
                            int curIndex = 0;
                            RouteTrace routeTrace = RouteTrace.START;
                            Bitmap tempBitmap = null;
                            while (curIndex < list.size()) {
                                switch (routeTrace) {
                                    case START:
                                        curIndex += increase;
                                        tempBitmap = RouteMapUtil.drawSurfaceStartRouting(bitmap, list.subList(0, curIndex), floorInfo.getZoom_size());
                                        bitmap.recycle();
                                        bitmap = tempBitmap;
                                        emitter.onNext(bitmap);
                                        if (curIndex + increase >= list.size()) {
                                            routeTrace = RouteTrace.WHOLE;
                                        } else {
                                            routeTrace = RouteTrace.START;
                                        }
                                        Thread.sleep(ROUTE_DELAY_TIME);
                                        break;
                                    case WHOLE:
                                        curIndex += increase;
                                        tempBitmap = RouteMapUtil.drawSurfaceWholeRouting(bitmap, list, floorInfo.getZoom_size());
                                        bitmap.recycle();
                                        bitmap = tempBitmap;
                                        emitter.onNext(bitmap);
                                        break;
                                }
                            }
                        }
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    e.printStackTrace();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * 路线轨迹枚举
     */
    private enum RouteTrace {
        /**
         * 完整路线
         */
        WHOLE,
        /**
         * 从起点到中间某个点的路线
         */
        START
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
