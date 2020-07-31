package com.zxy.demo.test.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseFragment;
import com.zxy.utility.SystemUtil;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;
import cn.zhouchaoyuan.excelpanel.ExcelPanel;

/**
 * Created by Admin
 * on 2020/7/31
 */
public class UITestFragment extends BaseFragment {

    @BindView(R.id.rv_num_percent)
    RecyclerView rvNumPercent;
    @BindView(R.id.rv_num_content)
    RecyclerView rvNumContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.rv_num_tag)
    RecyclerView rvNumTag;
    @BindView(R.id.rv_num_tag2)
    RecyclerView rvNumTag2;
    @BindView(R.id.rv_number_percent_value_left)
    RecyclerView rvNumberPercentValueLeft;
    @BindView(R.id.rv_number_percent_value_right)
    RecyclerView rvNumberPercentValueRight;
    @BindView(R.id.rv_skip)
    RecyclerView rvSkip;
    @BindView(R.id.ep_table)
    ExcelPanel epTable;
    @BindView(R.id.ep_table_dragon_tiger)
    ExcelPanel epTableDragonTiger;
    @BindView(R.id.rv_dragon_tiger)
    RecyclerView rvDragonTiger;
    @BindView(R.id.rv_big_small)
    RecyclerView rvBigSmall;
    @BindView(R.id.rv_road_bead)
    RecyclerView rvRoadBead;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.tl_tab2)
    TabLayout tlTab2;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.rv_lottery_data)
    RecyclerView rvLotteryData;

    UIAdapter mUIAdapter;
    NumContentAdapter mNumContentAdapter;
    NumberTagAdapter mNumberTagAdapter;
    NumberTagAdapter mNumberTagAdapter2;
    NumberPercentValueLeftAdapter mNumberPercentValueLeftAdapter;
    NumberPercentValueRightAdapter mNumberPercentValueRightAdapter;
    SkipNumberAdapter mSkipNumberAdapter;
    ExcelPanelAdapter mExcelPanelAdapter;
    ExcelPanelDragonTigerAdapter mExcelPanelDragonTigerAdapter;
    DragonTigerAdapter mDragonTigerAdapter;
    BigSmallAdapter mBigSmallAdapter;
    RoadBeadAdapter mRoadBeadAdapter;
    PlanAnalysisLotteryDataAdapter mPlanAnalysisLotteryDataAdapter;
    Activity mActivity;

    @Override
    public int layoutId() {
        return R.layout.fragment_ui_test;
    }

    @Override
    protected void initView() {
        mActivity = getActivity();
        mUIAdapter = new UIAdapter(mActivity);
        rvNumPercent.setAdapter(mUIAdapter);
        mUIAdapter.getDataHolder().setList(Arrays.asList(
                new NumberPercentViewHolder.Data("01,02,03,04,05", 30, Color.parseColor("#F53F3F")),
                new NumberPercentViewHolder.Data("01,02,03,04,05", 30, Color.parseColor("#FF8800")),
                new NumberPercentViewHolder.Data("01,02,03,04,05", 97, Color.parseColor("#0077FF"))));
        mNumContentAdapter = new NumContentAdapter(mActivity);
        rvNumContent.setAdapter(mNumContentAdapter);
        mNumContentAdapter.getDataHolder().setList(Arrays.asList(
                new NumberContentViewHolder.Data(PlanConst.ColorValue.HOT, "热号", Arrays.asList("1", "22", "33", "44", "55")),
                new NumberContentViewHolder.Data(PlanConst.ColorValue.WARM, "温号", Arrays.asList("11", "99", "88", "77", "66")),
                new NumberContentViewHolder.Data(PlanConst.ColorValue.COLD, "冷号", Arrays.asList("11", "22", "22", "32", "12", "22", "1", "2", "34", "55", "23", "22", "1", "2", "34", "55"))));
        mNumberTagAdapter = new NumberTagAdapter(mActivity);
        rvNumTag.setLayoutManager(new GridLayoutManager(mActivity, 3));
        rvNumTag.setAdapter(mNumberTagAdapter);
        mNumberTagAdapter.getDataHolder().setList(Arrays.asList(
                new NumberTagViewHolder.Data(PlanConst.ColorValue.HOT.getTextColor(), "热号：  3次"),
                new NumberTagViewHolder.Data(PlanConst.ColorValue.WARM.getTextColor(), "温号：2-3次"),
                new NumberTagViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "冷号：  2次")));
        mNumberTagAdapter2 = new NumberTagAdapter(mActivity);
        rvNumTag2.setLayoutManager(new LinearLayoutManager(mActivity));
        rvNumTag2.setAdapter(mNumberTagAdapter2);
        mNumberTagAdapter2.getDataHolder().setList(Arrays.asList(
                new NumberTagViewHolder.Data(PlanConst.ColorValue.HOT.getTextColor(), "预警号：最大遗漏-2<当前遗漏 "),
                new NumberTagViewHolder.Data(PlanConst.ColorValue.WARM.getTextColor(), "黄金号：平均遗漏-2≤当前遗漏≤平均遗漏+2"),
                new NumberTagViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "观察号：\n当前遗漏<平均遗漏-2 或 平均遗漏+2<当前遗漏\n≤最大遗漏-2")));

        mNumberPercentValueRightAdapter = new NumberPercentValueRightAdapter(mActivity);
        rvNumberPercentValueRight.setLayoutManager(new LinearLayoutManager(mActivity));
        rvNumberPercentValueRight.setAdapter(mNumberPercentValueRightAdapter);
        mNumberPercentValueRightAdapter.getDataHolder().setList(Arrays.asList(
                new NumberPercentValueRightViewHolder.Data("比率为2:1:2", "30.0%", PlanConst.ColorValue.HOT.getTextColor()),
                new NumberPercentValueRightViewHolder.Data("比率为1:1:3", "20.0%", PlanConst.ColorValue.WARM.getTextColor()),
                new NumberPercentValueRightViewHolder.Data("比率为2:1:0", "16.7%", PlanConst.ColorValue.COLD.getTextColor())));

        mNumberPercentValueLeftAdapter = new NumberPercentValueLeftAdapter(mActivity);
        rvNumberPercentValueLeft.setLayoutManager(new LinearLayoutManager(mActivity));
        rvNumberPercentValueLeft.setAdapter(mNumberPercentValueLeftAdapter);
        mNumberPercentValueLeftAdapter.getDataHolder().setList(Arrays.asList(
                new NumberPercentValueLeftViewHolder.Data("的选号属于“预警号”", "30.0%", PlanConst.ColorValue.HOT.getTextColor()),
                new NumberPercentValueLeftViewHolder.Data("的选号属于“黄金号”", "20.0%", PlanConst.ColorValue.WARM.getTextColor()),
                new NumberPercentValueLeftViewHolder.Data("的选号属于“观察号”", "16.7%", PlanConst.ColorValue.COLD.getTextColor())));


        mSkipNumberAdapter = new SkipNumberAdapter(mActivity);
        rvSkip.setLayoutManager(new LinearLayoutManager(mActivity));
        rvSkip.setAdapter(mSkipNumberAdapter);
        rvSkip.addItemDecoration(new RecyclerView.ItemDecoration() {
            int padding;

            {
                padding = SystemUtil.dp2px(10);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = padding;
            }
        });
        mSkipNumberAdapter.getDataHolder().setList(Arrays.asList(
                new SkipNumberViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "01", 3, 4, 20),
                new SkipNumberViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "02", 10, 6, 20),
                new SkipNumberViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "03", 8, 10, 18),
                new SkipNumberViewHolder.Data(PlanConst.ColorValue.COLD.getTextColor(), "04", 3, 3, 15),
                new SkipNumberViewHolder.Data(PlanConst.ColorValue.WARM.getTextColor(), "05", 20, 20, 20)
        ));
        mExcelPanelAdapter = new ExcelPanelAdapter(mActivity);
        epTable.setAdapter(mExcelPanelAdapter);
        epTable.setOnLoadMoreListener(new ExcelPanel.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onLoadHistory() {

            }
        });
        mExcelPanelAdapter.setExcelPanel(epTable);

        mExcelPanelDragonTigerAdapter = new ExcelPanelDragonTigerAdapter(mActivity);
        epTableDragonTiger.setAdapter(mExcelPanelDragonTigerAdapter);
        epTableDragonTiger.setOnLoadMoreListener(new ExcelPanel.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onLoadHistory() {

            }
        });
        mExcelPanelDragonTigerAdapter.setExcelPanel(epTableDragonTiger);

        mExcelPanelAdapter.setAllData(
                Arrays.asList("吉吉计划", "巴博斯计划", "施龙计划", "AMG计划"),
                Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13"),
                Arrays.asList(
                        Arrays.asList(
                                new ExcelPanelAdapter.CellData("01", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("02", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("03", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("04", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("05", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("06", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("07", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("08", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("09", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("10", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("11", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("12", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("13", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes())),

                        Arrays.asList(
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("05", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("06", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("08", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("09", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("10", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("11", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("12", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("13", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes())),
                        Arrays.asList(
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("02", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("03", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("05", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("06", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("08", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("09", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("10", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("11", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("12", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("13", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes())),
                        Arrays.asList(
                                new ExcelPanelAdapter.CellData("01", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.HOT.getTextColor(), PlanConst.ColorValue.HOT.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("04", PlanConst.ColorValue.WARM.getTextColor(), PlanConst.ColorValue.WARM.getBgRes()),
                                new ExcelPanelAdapter.CellData("05", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("07", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("08", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("09", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("10", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("11", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("12", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()),
                                new ExcelPanelAdapter.CellData("13", PlanConst.ColorValue.COLD.getTextColor(), PlanConst.ColorValue.COLD.getBgRes()))
                ));

        mExcelPanelDragonTigerAdapter.setAllData(Arrays.asList("吉吉计划", "巴博斯计划", "施龙计划", "AMG计划"),
                Arrays.asList("8龙", "10虎"),
                Arrays.asList(
                        Arrays.asList(
                                new ExcelPanelDragonTigerAdapter.CellData("龙", PlanConst.ColorValue.DRAGON.getTextColor(), PlanConst.ColorValue.DRAGON.getBgRes()),
                                new ExcelPanelDragonTigerAdapter.CellData("虎", PlanConst.ColorValue.TIGER.getTextColor(), PlanConst.ColorValue.TIGER.getBgRes())),
                        Arrays.asList(
                                new ExcelPanelDragonTigerAdapter.CellData("龙", PlanConst.ColorValue.DRAGON.getTextColor(), PlanConst.ColorValue.DRAGON.getBgRes()),
                                new ExcelPanelDragonTigerAdapter.CellData("", PlanConst.ColorValue.NO_DRAGON_TIGER.getTextColor(), PlanConst.ColorValue.NO_DRAGON_TIGER.getBgRes())),
                        Arrays.asList(
                                new ExcelPanelDragonTigerAdapter.CellData("", PlanConst.ColorValue.NO_DRAGON_TIGER.getTextColor(), PlanConst.ColorValue.NO_DRAGON_TIGER.getBgRes()),
                                new ExcelPanelDragonTigerAdapter.CellData("虎", PlanConst.ColorValue.TIGER.getTextColor(), PlanConst.ColorValue.TIGER.getBgRes())),
                        Arrays.asList(
                                new ExcelPanelDragonTigerAdapter.CellData("", PlanConst.ColorValue.NO_DRAGON_TIGER.getTextColor(), PlanConst.ColorValue.NO_DRAGON_TIGER.getBgRes()),
                                new ExcelPanelDragonTigerAdapter.CellData("虎", PlanConst.ColorValue.TIGER.getTextColor(), PlanConst.ColorValue.TIGER.getBgRes()))));

        mDragonTigerAdapter = new DragonTigerAdapter(mActivity);
        LinearLayoutManager dragonTigerLM = new LinearLayoutManager(mActivity);
        dragonTigerLM.setOrientation(RecyclerView.HORIZONTAL);
        rvDragonTiger.setLayoutManager(dragonTigerLM);
        rvDragonTiger.setAdapter(mDragonTigerAdapter);
        mDragonTigerAdapter.getDataHolder().setList(Arrays.asList(
                new DragonTigerAdapter.Data(true, "龙"),
                new DragonTigerAdapter.Data(false, "龙虎龙"),
                new DragonTigerAdapter.Data(false, "虎虎"),
                new DragonTigerAdapter.Data(false, ""),
                new DragonTigerAdapter.Data(false, "龙")
        ));

        mBigSmallAdapter = new BigSmallAdapter(mActivity);
        LinearLayoutManager bigSmallLM = new LinearLayoutManager(mActivity);
        bigSmallLM.setOrientation(RecyclerView.HORIZONTAL);
        rvBigSmall.setLayoutManager(bigSmallLM);
        rvBigSmall.setAdapter(mBigSmallAdapter);
        mBigSmallAdapter.getDataHolder().setList(Arrays.asList(
                new BigSmallAdapter.Data("小"),
                new BigSmallAdapter.Data("大大大"),
                new BigSmallAdapter.Data("小小"),
                new BigSmallAdapter.Data(""),
                new BigSmallAdapter.Data("大")
        ));
        mRoadBeadAdapter = new RoadBeadAdapter(mActivity);
        rvRoadBead.setLayoutManager(new LinearLayoutManager(mActivity));
        rvRoadBead.addItemDecoration(new RecyclerView.ItemDecoration() {
            int margin;

            {
                margin = SystemUtil.dp2px(24);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = margin;
            }
        });
        rvRoadBead.setAdapter(mRoadBeadAdapter);
        mRoadBeadAdapter.getDataHolder().setList(Arrays.asList(
                new RoadBeadViewHolder.Data("总共", 23, 23, 23, 23),
                new RoadBeadViewHolder.Data("当前", 2, 23, 2, 23),
                new RoadBeadViewHolder.Data("最大", 6, 23, 6, 23),
                new RoadBeadViewHolder.Data("平均", 3, 23, 3, 23)
        ));
        vpContent.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new Fragment();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "号码分布";
            }
        });

        PlanAnalysisTypeTabLayoutHelper tabLayoutHelper = new PlanAnalysisTypeTabLayoutHelper();
        tabLayoutHelper.init(vpContent, tlTab, Arrays.asList("号码分布", "冷热温分析", "遗漏分析"), 0);
        PlanAnalysisDigitTabLayoutHelper tabLayoutHelper1 = new PlanAnalysisDigitTabLayoutHelper();
        tabLayoutHelper1.init(vpContent, tlTab2, Arrays.asList("万位", "千位", "百位"), 0);

        mPlanAnalysisLotteryDataAdapter = new PlanAnalysisLotteryDataAdapter(mActivity);
        rvLotteryData.setAdapter(mPlanAnalysisLotteryDataAdapter);
        rvLotteryData.setLayoutManager(new LinearLayoutManager(mActivity));
        rvLotteryData.addItemDecoration(new RecyclerView.ItemDecoration() {
            int padding;

            {
                padding = SystemUtil.dp2px(10);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = padding;
            }
        });
        mPlanAnalysisLotteryDataAdapter.getDataHolder().setList(Arrays.asList(

                new PlanAnalysisLotteryDataViewHolder.Data("三码（推荐关注3-5期）  进行中 1/5", Arrays.asList(
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 1, Arrays.asList("10", "11", "12", "13", "14", "15")),
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 0, Arrays.asList("10", "11", "12", "13", "14", "15")),
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 0, Arrays.asList("10", "11", "12", "13", "14", "15"))),
                        Arrays.asList(new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")),
                                new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")),
                                new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")))),

                new PlanAnalysisLotteryDataViewHolder.Data("三码（推荐关注3-5期）  进行中 1/5", Arrays.asList(
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 1, Arrays.asList("10", "11", "12", "13", "14", "15")),
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 0, Arrays.asList("10", "11", "12", "13", "14", "15")),
                        new PlanAnalysisLotteryDataViewHolder.Data.LotteryDataBean("0005期", "中奖", 0, Arrays.asList("10", "11", "12", "13", "14", "15"))),
                        Arrays.asList(new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")),
                                new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")),
                                new PlanAnalysisLotteryDataViewHolder.Data.NumberDataBean("千位", Arrays.asList("1", "2", "3", "4", "5")))
                )));
    }

    public static class UIAdapter extends BaseRecyclerViewAdapter<NumberPercentViewHolder.Data> {


        public UIAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new NumberPercentViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }

    }

    public static class NumContentAdapter extends BaseRecyclerViewAdapter<NumberContentViewHolder.Data> {


        public NumContentAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new NumberContentViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


    }

    public static class NumberTagAdapter extends BaseRecyclerViewAdapter<NumberTagViewHolder.Data> {

        public NumberTagAdapter(Activity mActivity) {
            super(mActivity);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new NumberTagViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


    }

    public static class NumberPercentValueLeftAdapter extends BaseRecyclerViewAdapter<NumberPercentValueLeftViewHolder.Data> {

        public NumberPercentValueLeftAdapter(Activity mActivity) {
            super(mActivity);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new NumberPercentValueLeftViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }
    }

    public static class SkipNumberAdapter extends BaseRecyclerViewAdapter<SkipNumberViewHolder.Data> {

        public SkipNumberAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new SkipNumberViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


    }

    public static class NumberPercentValueRightAdapter extends BaseRecyclerViewAdapter<NumberPercentValueRightViewHolder.Data> {

        public NumberPercentValueRightAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new NumberPercentValueRightViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


    }

    public static class ExcelPanelAdapter extends BaseExcelPanelAdapter<String, String, ExcelPanelAdapter.CellData> {

        private Context mContext;

        public ExcelPanelAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
            return new ExcelCellViewHolder(parent);
        }

        @Override
        public void onBindCellViewHolder(RecyclerView.ViewHolder viewHolder, int verticalPosition, int horizontalPosition) {
            if (viewHolder instanceof ExcelCellViewHolder) {
                ExcelCellViewHolder holder = (ExcelCellViewHolder) viewHolder;
                CellData cellData = getMajorItem(verticalPosition, horizontalPosition);
                holder.bind(cellData);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
            return new ExcelViewTopHolder(parent);
        }

        @Override
        public void onBindTopViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof ExcelViewTopHolder) {
                ExcelViewTopHolder holder = (ExcelViewTopHolder) viewHolder;
                String str = getTopItem(position);
                holder.bind(str);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
            return new ExcelLeftViewHolder(parent);
        }

        @Override
        public void onBindLeftViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof ExcelLeftViewHolder) {
                ExcelLeftViewHolder holder = (ExcelLeftViewHolder) viewHolder;
                String str = getLeftItem(position);
                holder.bind(str);
            }
        }

        @Override
        public View onCreateTopLeftView() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_excel_top_left, null);
            TextView tv = view.findViewById(R.id.tv_content);
            tv.setText("计划/冷热温");
            return view;
        }

        public static class ExcelCellViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelCellViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_cell, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(CellData cellData) {
                tvContent.setText(cellData.getNumber());
                tvContent.setTextColor(cellData.getTextColor());
                tvContent.setBackgroundResource(cellData.getBgRes());
            }
        }

        public static class ExcelViewTopHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelViewTopHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_top, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String content) {
                tvContent.setText(content);
            }
        }

        public static class ExcelLeftViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelLeftViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_left, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String content) {
                tvContent.setText(content);
            }
        }

        public static class CellData {
            private String number;
            private int textColor;
            private int bgRes;

            public CellData() {
            }

            public CellData(String number, int textColor, int bgRes) {
                this.number = number;
                this.textColor = textColor;
                this.bgRes = bgRes;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public int getTextColor() {
                return textColor;
            }

            public void setTextColor(int textColor) {
                this.textColor = textColor;
            }

            public int getBgRes() {
                return bgRes;
            }

            public void setBgRes(int bgRes) {
                this.bgRes = bgRes;
            }

            @Override
            public String toString() {
                return "CellData{" +
                        "number='" + number + '\'' +
                        ", textColor=" + textColor +
                        ", bgRes=" + bgRes +
                        '}';
            }
        }
    }

    public static class ExcelPanelDragonTigerAdapter extends BaseExcelPanelAdapter<String, String, ExcelPanelDragonTigerAdapter.CellData> {

        private Context mContext;

        public ExcelPanelDragonTigerAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
            return new ExcelCellViewHolder(parent);
        }

        @Override
        public void onBindCellViewHolder(RecyclerView.ViewHolder viewHolder, int verticalPosition, int horizontalPosition) {
            if (viewHolder instanceof ExcelCellViewHolder) {
                ExcelCellViewHolder holder = (ExcelCellViewHolder) viewHolder;
                CellData cellData = getMajorItem(verticalPosition, horizontalPosition);
                holder.bind(cellData);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
            return new ExcelViewTopHolder(parent);
        }

        @Override
        public void onBindTopViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof ExcelViewTopHolder) {
                ExcelViewTopHolder holder = (ExcelViewTopHolder) viewHolder;
                String str = getTopItem(position);
                holder.bind(str);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
            return new ExcelLeftViewHolder(parent);
        }

        @Override
        public void onBindLeftViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof ExcelLeftViewHolder) {
                ExcelLeftViewHolder holder = (ExcelLeftViewHolder) viewHolder;
                String str = getLeftItem(position);
                holder.bind(str);
            }
        }

        @Override
        public View onCreateTopLeftView() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_excel_top_left, null);
            TextView tv = view.findViewById(R.id.tv_content);
            tv.setText("计划/冷热温");
            return view;
        }

        public static class ExcelCellViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelCellViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_dragon_tiger_cell, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(CellData cellData) {
                tvContent.setText(cellData.getNumber());
                tvContent.setTextColor(cellData.getTextColor());
                tvContent.setBackgroundResource(cellData.getBgRes());
            }
        }

        public static class ExcelViewTopHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelViewTopHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_dragon_tiger_top, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String content) {
                tvContent.setText(content);
            }
        }

        public static class ExcelLeftViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_content)
            TextView tvContent;

            public ExcelLeftViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_excel_dragon_tiger_left, parent, false));
                ButterKnife.bind(this, itemView);
            }

            public void bind(String content) {
                tvContent.setText(content);
            }
        }

        public static class CellData {
            private String number;
            private int textColor;
            private int bgRes;

            public CellData() {
            }

            public CellData(String number, int textColor, int bgRes) {
                this.number = number;
                this.textColor = textColor;
                this.bgRes = bgRes;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public int getTextColor() {
                return textColor;
            }

            public void setTextColor(int textColor) {
                this.textColor = textColor;
            }

            public int getBgRes() {
                return bgRes;
            }

            public void setBgRes(int bgRes) {
                this.bgRes = bgRes;
            }

            @Override
            public String toString() {
                return "CellData{" +
                        "number='" + number + '\'' +
                        ", textColor=" + textColor +
                        ", bgRes=" + bgRes +
                        '}';
            }
        }
    }

    public static class DragonTigerAdapter extends BaseRecyclerViewAdapter<DragonTigerAdapter.Data> {

        private int mCurPosition = 0;

        public DragonTigerAdapter(Activity mActivity) {
            super(mActivity);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new DragonTigerViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }

        public class DragonTigerViewHolder extends BaseViewHolder<Data> {

            @BindView(R.id.fl_content)
            FrameLayout flContent;
            @BindView(R.id.tv_selected)
            TextView tvSelected;
            @BindView(R.id.ll_content)
            LinearLayout llContent;

            Activity mActivity;

            public DragonTigerViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_dragon_tiger);
                mActivity = (Activity) parent.getContext();
            }

            @Override
            public void bind(Data data) {
                int position = getDataHolder().getList().indexOf(data);
                flContent.setSelected(data.selected);
                tvSelected.setVisibility(data.selected ? View.VISIBLE : View.INVISIBLE);
                llContent.setBackgroundColor(position % 2 == 0 ? mActivity.getResources().getColor(R.color.color_black_F7F7F7) : mActivity.getResources().getColor(R.color.white));
                llContent.removeAllViews();
                if (!TextUtils.isEmpty(data.getData())) {
                    String[] strings = data.getData().split("");
                    for (String s : strings) {
                        TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.viewholder_single_text_item, llContent, false);
                        tv.setText(s);
                        llContent.addView(tv);
                    }
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurPosition != position) {
                            if (mCurPosition >= 0 && mCurPosition < getDataHolder().getList().size()) {
                                getDataHolder().getList().get(mCurPosition).selected = false;
                            }
                            getDataHolder().getList().get(position).selected = true;
                            mCurPosition = position;
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        public static class Data {

            private boolean selected;
            private String data;

            public Data() {
            }

            public Data(boolean selected, String data) {
                this.selected = selected;
                this.data = data;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }
    }

    public static class BigSmallAdapter extends BaseRecyclerViewAdapter<BigSmallAdapter.Data> {


        public BigSmallAdapter(Activity mActivity) {
            super(mActivity);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new BigSmallViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


        public class BigSmallViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Data> {

            @BindView(R.id.fl_content)
            FrameLayout flContent;
            @BindView(R.id.ll_content)
            LinearLayout llContent;

            Activity mActivity;

            public BigSmallViewHolder(ViewGroup parent) {
                super(parent, R.layout.viewholder_big_small);
                mActivity = (Activity) parent.getContext();
            }

            @Override
            public void bind(Data data) {
                int position = getDataHolder().getList().indexOf(data);
                llContent.setBackgroundColor(position % 2 == 0 ? mActivity.getResources().getColor(R.color.color_black_F7F7F7) : mActivity.getResources().getColor(R.color.white));
                llContent.removeAllViews();
                if (!TextUtils.isEmpty(data.getData())) {
                    String[] strings = data.getData().split("");
                    for (String s : strings) {
                        TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.viewholder_single_text_item, llContent, false);
                        tv.setText(s);
                        llContent.addView(tv);
                    }
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        public static class Data {

            private String data;

            public Data() {
            }

            public Data(String data) {
                this.data = data;
            }


            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }
    }

    public static class RoadBeadAdapter extends BaseRecyclerViewAdapter<RoadBeadViewHolder.Data> {

        public RoadBeadAdapter(Activity mActivity) {
            super(mActivity);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RoadBeadViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


    }

    public static class PlanAnalysisLotteryDataAdapter extends BaseRecyclerViewAdapter<PlanAnalysisLotteryDataViewHolder.Data> {

        public PlanAnalysisLotteryDataAdapter(Activity mActivity) {
            super(mActivity);
        }


        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new PlanAnalysisLotteryDataViewHolder(parent);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }
    }
}
