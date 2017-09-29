package com.ygip.ipbase_android.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.utils.PhotoBitmapUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LockyLuo on 2017/9/28.
 */

public class BitmapUtils {
    static String tag = "BitmapUtils";

    public static Bitmap getSmallerImageBitmap(Activity ac, Uri uri) {
        String path = FileUtils.Uri2FilePath(ac, uri);
        Bitmap bitmap = compressImage(decodeSampledBitmapFromFile(path, 1000, 1000));
        return bitmap;
    }

    public static File getSmallerImageFile(Activity ac, Uri uri) {
        String path = FileUtils.Uri2FilePath(ac, uri);
        Bitmap bitmap = compressImage(decodeSampledBitmapFromFile(path, 1000, 1000));
        String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        return saveBitmapFile(bitmap, fileName);
    }

    public static File saveBitmapFile(Bitmap bitmap, String fileName) {
        String dir=Environment.getExternalStorageDirectory() + "/ipbase/";
        File dirP=new File(dir);
        if(!dirP.exists())
            dirP.mkdir();
        File file = new File( dir+ fileName);//将要保存图片的路径
        if (file.exists())
            file.delete();

        try {
            FileOutputStream bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri, int doCompress) throws IOException {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d(tag, "Max memory is " + maxMemory + "KB");
        String path = FileUtils.Uri2FilePath(ac, uri);

        Bitmap bitmap = null;
        int degree = PhotoBitmapUtils.readPictureDegree(path);
        Log.d(tag, "degree " + degree);
        int w = 512;
        int h = 512;

        try {
            bitmap = decodeSampledBitmapFromFile(path, w, h);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        bitmap = PhotoBitmapUtils.rotaingImageView(degree, bitmap);


        if (doCompress == 0) {
            return bitmap;
        } else {
            return compressImage(bitmap);//再进行质量压缩
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 800) {  //循环判断如果压缩后图片是否大于?kb,大于继续压缩
            Log.d(tag, "compressImage " + options);
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据给定的宽度和高度动态计算图片压缩比率
     *
     * @param options   Bitmap配置文件
     * @param reqWidth  需要压缩到的宽度
     * @param reqHeight 需要压缩到的高度
     * @return 压缩比
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

        }

        return inSampleSize;
    }

    /**
     * 将图片根据压缩比压缩成固定宽高的Bitmap，实际解析的图片大小可能和#reqWidth、#reqHeight不一样。
     *
     * @param imgPath   图片地址
     * @param reqWidth  需要压缩到的宽度
     * @param reqHeight 需要压缩到的高度
     * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String imgPath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.d(tag, "----------------------------------------");
        Log.d(tag, "decodeSampledBitmapFromFile inSampleSize:" + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

}
