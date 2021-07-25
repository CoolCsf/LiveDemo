package com.coolcsf.livedemo.camera

import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.getNumberOfCameras
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.coolcsf.livedemo.jni.LiveLibJni


/**
 * 1. 创建camera对象
 * 2. 设置camera配置
 * 3. 回调数据到jni
 *
 * 注意点：1. thread管理 2. 设备起停管理
 */
class LiveCamera() : Camera.PreviewCallback {
    private val TAG = "LiveCamera"
    private val mWidth = 640
    private val mHeight = 360
    private lateinit var mCamera: Camera
    private lateinit var mParameters: Camera.Parameters
    private lateinit var mPreview: SurfaceView
    fun setPreview(preview: SurfaceView) {
        this.mPreview = preview
    }

    private fun initCamera() {
        mCamera = Camera.open()
        initParameters()
        mCamera.setPreviewCallback(this)
        mCamera.setPreviewDisplay(mPreview.holder)
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        Log.d(TAG, "onPreviewFrame$data")
        data?.let {
            LiveLibJni.sendCameraData(it)
        }
    }

    fun startPreview() {
        initCamera()
        mCamera.startPreview()
    }

    fun stopPreview() {
        mCamera.stopPreview()
        releasePreview()
    }

    private fun releasePreview() {
        mCamera.stopFaceDetection();
        mCamera.setPreviewCallback(null);
        mCamera.release()
    }

    private fun initParameters() {
        mParameters = mCamera.parameters
        mParameters.previewFormat = ImageFormat.NV21 //default
        if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mParameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (isSupportFocus(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mParameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }

        //设置预览图片大小
        setPreviewSize()
        //设置图片大小
        setPictureSize()
        setCameraDisplayOrientation()
        mCamera.parameters = mParameters
    }

    private fun isSupportFocus(focusMode: String): Boolean {
        return mParameters.supportedFocusModes.contains(focusMode)
    }

    private fun setPictureSize() {
        mParameters.setPictureSize(mWidth, mHeight)
    }

    private fun setPreviewSize() {
        mParameters.setPreviewSize(mWidth, mHeight)
    }

    private fun setCameraDisplayOrientation() {
        val cameraInfo = CameraInfo()
        Camera.getCameraInfo(CameraInfo.CAMERA_FACING_BACK, cameraInfo)
        val rotation: Int = 90
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        //cameraInfo.orientation 图像传感方向
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (cameraInfo.orientation - degrees + 360) % 360
        }
        //相机预览方向
        mCamera.setDisplayOrientation(result)
    }
}