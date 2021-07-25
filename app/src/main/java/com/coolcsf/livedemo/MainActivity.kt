package com.coolcsf.livedemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import com.coolcsf.livedemo.camera.LiveCamera
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private val TAG = "MainActivity"

    companion object {
        init {
            System.loadLibrary("livelib")
        }
    }

    private val mCamera by lazy {
        LiveCamera().apply { this.setPreview(surface_preview) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        surface_preview.holder.addCallback(this)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, Array(1) { Manifest.permission.CAMERA },
            200
        )
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed")
        mCamera.stopPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
        mCamera.startPreview()
    }

}
