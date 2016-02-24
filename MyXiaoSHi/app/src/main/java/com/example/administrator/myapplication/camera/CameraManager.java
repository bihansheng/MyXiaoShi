/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.administrator.myapplication.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.example.administrator.myapplication.DisplayUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @lixiangxiang
 *  相机管理类
 * 包括初始化
 * 拍照
 * 保存截取后的图片
 *
 */
public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();
    public CameraSizeComparator sizeComparator = new CameraSizeComparator();

    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private AutoFocusManager autoFocusManager;

    private boolean initialized;
    private boolean previewing;
    private int requestedCameraId = -1;
    private Activity activity;
    private ImageView imPicTake;
    /**
     * Preview frames are delivered here, which we pass on to the registered
     * handler. Make sure to clear the handler so it will only receive one
     * message.
     */
    private final PreviewCallback previewCallback;
    private boolean isPreviewing = true;
    private PictureDealCallBack pictureDealCallBack;


    public CameraManager(Context context, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
        previewCallback = new PreviewCallback(configManager);
    }

    public void setDealBack(PictureDealCallBack pictureDealCallBack){
        this.pictureDealCallBack =pictureDealCallBack;
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder The surface object which the camera will draw preview frames
     *               into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public synchronized void openDriver(SurfaceHolder holder, float previewRate) throws IOException {
        Camera theCamera = camera;
        if (theCamera == null) {

            if (requestedCameraId >= 0) {
                theCamera = OpenCameraInterface.open(requestedCameraId);
            } else {
                theCamera = OpenCameraInterface.open();
            }

            if (theCamera == null) {
                throw new IOException();
            }
            camera = theCamera;
        }
        theCamera.setPreviewDisplay(holder);

        if (!initialized) {
            initialized = true;
            configManager.initFromCameraParameters(theCamera);
        }
        //相机参数配置
        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save
        parameters.setPictureFormat(PixelFormat.JPEG);//
        printSupportPictureSize(parameters);
        printSupportPreviewSize(parameters);
        Size pictureSize = getPropPictureSize(
                parameters.getSupportedPictureSizes(), previewRate, 800);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        Size previewSize = getPropPreviewSize(
                parameters.getSupportedPreviewSizes(), previewRate, 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPreviewFrameRate(1);
        theCamera.setDisplayOrientation(90);
        printSupportFocusMode(parameters);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        theCamera.setParameters(parameters);
        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();//
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        isPreviewing = true;
        parameters = theCamera.getParameters(); //
        Log.i(TAG, "PreviewSize--With = " + parameters.getPreviewSize().width
                + "Height = " + parameters.getPreviewSize().height);
        Log.i(TAG, "PictureSize--With = " + parameters.getPictureSize().width
                + "Height = " + parameters.getPictureSize().height);
        // these,
        // temporarily
        try {
            configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

    }

    /**
     * 打印获取焦点模式
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i(TAG, "focusModes--" + mode);
        }
    }

    public boolean equalRate(Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    public Size getPropPreviewSize(List<Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, th)) {
                Log.i(TAG, "PreviewSize:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//
        }
        return list.get(i);
    }

    /**
     * 获取最佳适应屏幕的照片
     * @param list
     * @param th  宽高比  按照屏幕宽高 来确定图片
     * @param minWidth  最低宽度
     * @return
     */
    public Size getPropPictureSize(List<Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);
        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, th)) {
                Log.i(TAG, "PictureSize : w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//
        }
        return list.get(i);
    }


    public void printSupportPictureSize(Camera.Parameters params) {
        List<Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Size size = pictureSizes.get(i);
            Log.i(TAG, "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    /**
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Size size = previewSizes.get(i);
            Log.i(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
        }

    }


    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
            // Make sure to clear these each time we close the camera, so that
            // any scanning rect
            // requested by intent is forgotten.
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public synchronized void startPreview() {
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(context, camera);
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data
     * will arrive as byte[] in the message.obj field, with width and height
     * encoded as message.arg1 and message.arg2, respectively.
     *
     * @param handler The handler to send the message to.
     * @param message The what field of the message to be sent.
     */
    public synchronized void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = camera;
        if (theCamera != null && previewing) {
            previewCallback.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(previewCallback);
        }
    }

    /**
     * Allows third party apps to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means
     *                 "no preference".
     */
    public synchronized void setManualCameraId(int cameraId) {
        requestedCameraId = cameraId;
    }

    /**
     * 获取相机分辨率
     *
     * @return
     */
    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    public Size getPreviewSize() {
        if (null != camera) {
            return camera.getParameters().getPreviewSize();
        }
        return null;
    }

    /**
     * 执行拍摄
     * @param imPicTake
     */
    public void doTakePicture(ImageView imPicTake) {
        this.imPicTake = imPicTake;
        if (isPreviewing && (camera != null)) {
            camera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /**
     * 拍照声音的设置
     */
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()

    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };

    /**
     * 拍照后的回调
     */
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()

    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap bitmap = null;
            if (null != data) {
                camera.stopPreview();
                bitmap = dealByte(data, context);
                isPreviewing = false;
                int x = bitmap.getWidth();
                int y = bitmap.getHeight();
                Log.i("svn", "宽以及高" + bitmap.getWidth() + "**" + bitmap.getWidth());
            }
            if (null != bitmap) {
                saveBitmap(bitmap);
            }
            isPreviewing = true;
        }

        /**
         * 将图片按照屏幕上选定的范围来裁剪图片
         * @param data 原始图片数据
         * @param context
         * @return
         */
        private Bitmap dealByte(byte[] data, Context context) {

            int screenW = DisplayUtil.getWidth(activity);//拍照activity ,在初始化 CameraManager 时 传入
            int screenH = DisplayUtil.getHeight(activity);

            Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.i("Bitmap", "原图：" + b.getWidth() + "*" + b.getHeight() + "图片长度》》》" + data.length);
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            Log.i("Bitmap", "屏幕：" + screenW + "*" + screenH);
            Log.i("Bitmap", "状态栏：" + statusBarHeight);

            Matrix matrix = new Matrix();
            matrix.postRotate((float) 90.0);
            Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);//将图片旋转90 度，因为拍照后的图片默认是横置的
            Log.i("Bitmap", "旋转后图：" + rotaBitmap.getWidth() + "*" + rotaBitmap.getHeight());

            int width = imPicTake.getWidth();
            int height = imPicTake.getHeight();
            int[] location = new int[2];
            imPicTake.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            Log.i("Bitmap", "xy：" + x + "*" + y);

            Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, screenW, screenH - statusBarHeight, true);//将图片缩放至屏幕宽高，除去状态栏的高度
            Log.i("Bitmap", "缩放后的图片：" + sizeBitmap.getWidth() + "*" + sizeBitmap.getHeight());
            Bitmap rectBitmap = Bitmap.createBitmap(sizeBitmap, x, y - statusBarHeight, width, height);//按照指定的形状对图片剪切
            return rectBitmap;
        }

        public Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) rotateDegree);
            Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
            return rotaBitmap;
        }

    };

    /**保存bitmap
     * @param b
     */
    public  void saveBitmap(Bitmap b){
        String path = IMAGE_DIRECTORY;
        Log.i(TAG, "saveBitmap:jpegName = " + path);
        try {
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            b.recycle();//回收bitmap
            pictureDealCallBack.dealCallBackListenter(path);
            Log.i(TAG, "saveBitmap");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:");
            e.printStackTrace();
        }

    }

    public class CameraSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 创建图片目录
     */
    public String FILE_Directory, IMAGE_DIRECTORY;

    public String createDirectory(Context context,String path) {
        FILE_Directory = Environment.getExternalStorageDirectory().getPath() + File.separator
                + context.getPackageName();
        //图片存放目录
        IMAGE_DIRECTORY = FILE_Directory + "/image/" +path;
        File directorys  = new File(FILE_Directory + "/image/");
        if (!directorys.exists()){
            directorys.mkdirs();
        }
        return IMAGE_DIRECTORY;
    }

    public interface PictureDealCallBack{
        void dealCallBackListenter(String path);
    }

}
