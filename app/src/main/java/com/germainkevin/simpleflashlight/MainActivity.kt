package com.germainkevin.simpleflashlight

import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    //ON, Le flash est allumé
    //OFF, Le flash est éteint
    private enum class FlashLightState { ON, OFF }

    private var mFlashLightState = FlashLightState.OFF

    private val mCameraManager: CameraManager by lazy {
        getSystemService(CAMERA_SERVICE) as CameraManager
    }

    // Verifie si l'appareil ou est installee cette application
    // detient un " front facing camera"
    private val isFlashAvailable by lazy {
        applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    // what camera [front/back]
    private var mCameraId: String? = null

    private lateinit var mFlashStateTv: TextView // texte qui montre si la torche est allumee ou non

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFlashStateTv = findViewById(R.id.mFlashStateTv)
        findViewById<FloatingActionButton>(R.id.mPowerButton).setOnClickListener {
            if (canFlashBeUsed) toggleTorchState() else Toast.makeText(
                applicationContext,
                "Cet appareil n'a pas de torche",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val canFlashBeUsed: Boolean
        get() {
            return if (isFlashAvailable) {
                mCameraId = mCameraManager.cameraIdList[0]
                true
            } else false
        }

    private fun toggleTorchState() {
        mCameraId?.let {
            if (mFlashLightState == FlashLightState.ON) {
                mCameraManager.setTorchMode(it, false)
                mFlashLightState = FlashLightState.OFF
                mFlashStateTv.text = getString(R.string.flash_is_off)
            } else {
                mCameraManager.setTorchMode(it, true)
                mFlashLightState = FlashLightState.ON
                mFlashStateTv.text = getString(R.string.flash_is_on)
            }
        }
    }
}