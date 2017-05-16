package com.zhihu.matisse.ui;

/**
 * Created by luoyin on 2017/5/8.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhihu.matisse.ui.MatisseActivity;
import com.zhihu.matisse.R;

import java.util.ArrayList;

import static android.R.attr.path;


/**
 * Created by luoyin on 2017/5/7.
 */



public class ProcessActivity extends Activity {
    private ImageView imageShow;
    private Bitmap bmp;                          //载入图片
    private Bitmap mbmp;                       //复制模版
    private RelativeLayout Imagelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        Imagelayout = (RelativeLayout)findViewById(R.id.Content_Layout);


        Intent intent = getIntent();
        ArrayList<Uri> path= intent.getParcelableArrayListExtra("path");

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        android.view.ViewGroup.LayoutParams pp = Imagelayout.getLayoutParams();
        pp.width=screenSize.x;
        pp.height=screenSize.x * 5 / 4;
        Imagelayout.setLayoutParams(pp);


        //自定义函数 显示图片
//        ShowPhotoByImageView(path);

    }
    /*
	 * 函数功能 显示图片
	 * 参数 String path 图片路径,源自MainActivity选择传参
	 */
    private void ShowPhotoByImageView(String path)
    {
        if (null == path) {
            Toast.makeText(this, "载入图片失败", Toast.LENGTH_SHORT).show();
            finish();
        }
		/*
		 * 问题:
		 * 获取Uri不知道getStringExtra()没对应uri参数
		 * 使用方法Uri uri=Uri.parse(path)获取路径不能显示图片
		 * mBitmap=BitmapFactory.decodeFile(path)方法不能适应大小
		 * 解决:
		 * 但我惊奇的发现decodeFile(path,opts)函数可以实现,哈哈哈
		 */
        //获取分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;    //屏幕水平分辨率
        int height = dm.heightPixels;  //屏幕垂直分辨率
        try {
            //Load up the image's dimensions not the image itself
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeFile(path,bmpFactoryOptions);
            int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
            int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
            //压缩显示
            if(heightRatio>1&&widthRatio>1) {
                if(heightRatio>widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio*2;
                }
                else {
                    bmpFactoryOptions.inSampleSize = widthRatio*2;
                }
            }
            //图像真正解码
            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(path,bmpFactoryOptions);
            mbmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
            imageShow.setImageBitmap(bmp); //显示照片
	        /*
	         * [失败] 动态设置属性
	         当设置android:scaleType="matrix"后图像显示左上角
	         设置图片居中 起点=未使用屏幕/2=(屏幕分辨率-图片宽高)/2
	         int widthCenter=imageShow.getWidth()/2-bmp.getWidth()/2;
	         int heightCenter=imageShow.getHeight()/2-bmp.getHeight()/2;
	         Matrix matrix = new Matrix();
	         matrix.postTranslate(widthCenter, heightCenter);
	         imageShow.setImageMatrix(matrix);
	         imageShow.setImageBitmap(bmp);
	         */
            //加载备份图片 绘图使用
//            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
//                    .getHeight(), bmp.getConfig());
//            canvas = new Canvas(alteredBitmap);  //画布
//            paint = new Paint(); //画刷
//            paint.setColor(Color.GREEN);
//            paint.setStrokeWidth(5);
//            paint.setTextSize(30);
//            paint.setTypeface(Typeface.DEFAULT_BOLD);  //无线粗体
//            matrix = new Matrix();
//            canvas.drawBitmap(bmp, matrix, paint);
            //imageShow.setImageBitmap(alteredBitmap);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
