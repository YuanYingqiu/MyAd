package com.example.qq.myad;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mImgUrl;
    private ViewPager vpImageContainer;
    private LinearLayout llDotContainer;
    private ImageView ivRedDot;
    private int mDotWidth;



    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentItem = vpImageContainer.getCurrentItem();
            if(currentItem < mImgUrl.size()-1)
                vpImageContainer.setCurrentItem(currentItem+1);
            else {
                vpImageContainer.setCurrentItem(0);
            }

            mHandler.sendEmptyMessageDelayed(10000,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vpImageContainer = (ViewPager) findViewById(R.id.vp_image_container);
        llDotContainer = (LinearLayout) findViewById(R.id.ll_gray_dot);
        ivRedDot = (ImageView) findViewById(R.id.iv_red_dot);


        mImgUrl = new ArrayList<>();
        mImgUrl.add("http://pic2.zhimg.com/3c2a6727f946d9475aa00aa4489be8f9.jpg");
        mImgUrl.add("http://pic4.zhimg.com/8e41e46a5c52687251127b17787cba87.jpg");
        mImgUrl.add("http://pic4.zhimg.com/000f9a9d80501705542f3cd7b59aaa97.jpg");
        mImgUrl.add("http://pic1.zhimg.com/d4fa3b2f9589628bb7feddac0d3ac474.jpg");
        mImgUrl.add("http://pic3.zhimg.com/fa05bca4894c35e2a9c8632cbcbf024e.jpg");

        initDotAndNewsImages();

        initDotWidth();

        initViewPagerEvent();



    }

    private void initDotWidth() {

        ivRedDot.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivRedDot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else {
                    ivRedDot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                if(llDotContainer.getChildCount()>1){
                    mDotWidth = llDotContainer.getChildAt(1).getLeft()-llDotContainer.getChildAt(0).getLeft();
                }
            }
        });
    }

    private void initViewPagerEvent() {

        PagerAdapter adapter = new MyAdCirclePlayPagerAdapter(MainActivity.this,mImgUrl);
        vpImageContainer.setAdapter(adapter);

        vpImageContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int redDotLeftMargin = (int) (mDotWidth*positionOffset+position*mDotWidth);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedDot.getLayoutParams();
                params.leftMargin = redDotLeftMargin;
                ivRedDot.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mHandler.sendEmptyMessageDelayed(10000,1000);

    }

    private void initDotAndNewsImages() {

        int leftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,3,getResources().getDisplayMetrics());

        for (int i = 0; i < mImgUrl.size(); i++) {
            initDot(i,leftMargin);
        }
    }

    private void initDot(int i,int leftMargin){

        ImageView view = new ImageView(MainActivity.this);
        view.setImageResource(R.drawable.shape_gray_2dot);
        //要写在这里面 不然会出现奇奇怪怪的bug
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if( i > 0 ){
            params.leftMargin = leftMargin;
        }
        llDotContainer.addView(view,params);

    }






    class MyAdCirclePlayPagerAdapter extends PagerAdapter{
        private List<String> mSubImgUrl;
        private List<ImageView> mSubImgs;
        public MyAdCirclePlayPagerAdapter(Context context, List<String> imgUrl) {
            this.mSubImgUrl = imgUrl;

            mSubImgs = new ArrayList<>();
            for (int i= 0 ; i < mSubImgUrl.size() ; i++){
                ImageView iv = new ImageView(context);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mSubImgs.add(iv);
            }
        }


        @Override
        public int getCount() {
            return mSubImgUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView ivNews = mSubImgs.get(position);
            Picasso.with(MainActivity.this).load(mSubImgUrl.get(position)).into(ivNews);
            container.addView(ivNews);
            return ivNews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
