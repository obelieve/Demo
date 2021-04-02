package com.hencoder.hencoderpracticedraw2;

import android.os.Bundle;

import android.view.Menu;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zxy.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.PorterDuff.Mode （人名-数字图像合成技术  Thomas Porter 和 Tom Duff 发表于 1984）
 * 2.着色器Mode:
 * {@link android.graphics.Shader.TileMode.CLAMP} //超出部分，选边缘颜色
 * {@link android.graphics.Shader.TileMode.REPEAT} //超出部分，选重复颜色
 * {@link android.graphics.Shader.TileMode.MIRROR} //超出部分，选镜像颜色
 * 3.图片合成Mode: {@link android.graphics.ComposeShader} (dst-目标图像,src-源图像)
 * {@link android.graphics.PorterDuff.Mode.SRC_OVER} //默认 src覆盖在目标上面
 *
 */
public class MainActivity2 extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();

    {
        pageModels.add(new PageModel(R.layout.sample_linear_gradient, R.string.title_linear_gradient, R.layout.practice_linear_gradient));
        pageModels.add(new PageModel(R.layout.sample_radial_gradient, R.string.title_radial_gradient, R.layout.practice_radial_gradient));
        pageModels.add(new PageModel(R.layout.sample_sweep_gradient, R.string.title_sweep_gradient, R.layout.practice_sweep_gradient));
        pageModels.add(new PageModel(R.layout.sample_bitmap_shader, R.string.title_bitmap_shader, R.layout.practice_bitmap_shader));
        pageModels.add(new PageModel(R.layout.sample_compose_shader, R.string.title_compose_shader, R.layout.practice_compose_shader));
        pageModels.add(new PageModel(R.layout.sample_lighting_color_filter, R.string.title_lighting_color_filter, R.layout.practice_lighting_color_filter));
        pageModels.add(new PageModel(R.layout.sample_color_mask_color_filter, R.string.title_color_matrix_color_filter, R.layout.practice_color_matrix_color_filter));
        pageModels.add(new PageModel(R.layout.sample_xfermode, R.string.title_xfermode, R.layout.practice_xfermode));
        pageModels.add(new PageModel(R.layout.sample_stroke_cap, R.string.title_stroke_cap, R.layout.practice_stroke_cap));
        pageModels.add(new PageModel(R.layout.sample_stroke_join, R.string.title_stroke_join, R.layout.practice_stroke_join));
        pageModels.add(new PageModel(R.layout.sample_stroke_miter, R.string.title_stroke_miter, R.layout.practice_stroke_miter));
        pageModels.add(new PageModel(R.layout.sample_path_effect, R.string.title_path_effect, R.layout.practice_path_effect));
        pageModels.add(new PageModel(R.layout.sample_shadow_layer, R.string.title_shader_layer, R.layout.practice_shadow_layer));
        pageModels.add(new PageModel(R.layout.sample_mask_filter, R.string.title_mask_filter, R.layout.practice_mask_filter));
        pageModels.add(new PageModel(R.layout.sample_fill_path, R.string.title_fill_path, R.layout.practice_fill_path));
        pageModels.add(new PageModel(R.layout.sample_text_path, R.string.title_text_path, R.layout.practice_text_path));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return PageFragment.newInstance(pageModel.sampleLayoutRes, pageModel.practiceLayoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class PageModel {
        @LayoutRes int sampleLayoutRes;
        @StringRes int titleRes;
        @LayoutRes int practiceLayoutRes;

        PageModel(@LayoutRes int sampleLayoutRes, @StringRes int titleRes, @LayoutRes int practiceLayoutRes) {
            this.sampleLayoutRes = sampleLayoutRes;
            this.titleRes = titleRes;
            this.practiceLayoutRes = practiceLayoutRes;
        }
    }
}
