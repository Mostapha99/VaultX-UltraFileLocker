package com.vaultx.core.security

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.vaultx.data.database.entities.AttemptType
import com.vaultx.data.database.entities.IntruderLogEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Date
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntruderDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var imageCapture: ImageCapture? = null
    
    companion object {
        private const val TAG = "IntruderDetector"
        private const val MAX_FAILED_ATTEMPTS = 3
        private const val ATTEMPT_WINDOW_MINUTES = 5
    }
    
    private var failedAttempts = 0
    private var lastAttemptTime = 0L
    
    fun recordFailedAttempt(
        attemptType: AttemptType,
        enteredValue: String? = null,
        lifecycleOwner: LifecycleOwner? = null,
        onIntruderDetected: (IntruderLogEntity) -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        
        // Reset counter if attempts are outside the time window
        if (currentTime - lastAttemptTime > ATTEMPT_WINDOW_MINUTES * 60 * 1000) {
            failedAttempts = 0
        }
        
        failedAttempts++
        lastAttemptTime = currentTime
        
        val deviceInfo = getDeviceInfo()
        
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            // Trigger intruder detection
            val logId = UUID.randomUUID().toString()
            
            if (lifecycleOwner != null && isFrontCameraAvailable()) {
                captureSelfie(logId) { selfieImagePath ->
                    val intruderLog = IntruderLogEntity(
                        id = logId,
                        timestamp = Date(currentTime),
                        attemptType = AttemptType.MULTIPLE_ATTEMPTS,
                        enteredValue = enteredValue,
                        deviceInfo = deviceInfo,
                        selfieImagePath = selfieImagePath
                    )
                    onIntruderDetected(intruderLog)
                }
            } else {
                val intruderLog = IntruderLogEntity(
                    id = logId,
                    timestamp = Date(currentTime),
                    attemptType = AttemptType.MULTIPLE_ATTEMPTS,
                    enteredValue = enteredValue,
                    deviceInfo = deviceInfo,
                    selfieImagePath = null
                )
                onIntruderDetected(intruderLog)
            }
            
            // Reset counter after detection
            failedAttempts = 0
        } else {
            // Record individual failed attempt
            val logId = UUID.randomUUID().toString()
            val intruderLog = IntruderLogEntity(
                id = logId,
                timestamp = Date(currentTime),
                attemptType = attemptType,
                enteredValue = enteredValue,
                deviceInfo = deviceInfo,
                selfieImagePath = null
            )
            onIntruderDetected(intruderLog)
        }
    }
    
    private fun isFrontCameraAvailable(): Boolean {
        return try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIds = cameraManager.cameraIdList
            
            for (cameraId in cameraIds) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    return true
                }
            }
            false
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Error checking front camera availability", e)
            false
        }
    }
    
    private fun captureSelfie(logId: String, onImageCaptured: (String?) -> Unit) {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                
                imageCapture = ImageCapture.Builder().build()
                
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        context as LifecycleOwner,
                        cameraSelector,
                        imageCapture
                    )
                    
                    val outputDirectory = File(context.filesDir, "intruder_selfies")
                    if (!outputDirectory.exists()) {
                        outputDirectory.mkdirs()
                    }
                    
                    val photoFile = File(outputDirectory, "intruder_$logId.jpg")
                    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    
                    imageCapture?.takePicture(
                        outputFileOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                Log.d(TAG, "Intruder selfie captured: ${photoFile.absolutePath}")
                                onImageCaptured(photoFile.absolutePath)
                            }
                            
                            override fun onError(exception: ImageCaptureException) {
                                Log.e(TAG, "Failed to capture intruder selfie", exception)
                                onImageCaptured(null)
                            }
                        }
                    )
                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                    onImageCaptured(null)
                }
            }, ContextCompat.getMainExecutor(context))
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up camera for selfie capture", e)
            onImageCaptured(null)
        }
    }
    
    private fun getDeviceInfo(): String {
        return buildString {
            append("Device: ${android.os.Build.DEVICE}\n")
            append("Model: ${android.os.Build.MODEL}\n")
            append("Manufacturer: ${android.os.Build.MANUFACTURER}\n")
            append("Android Version: ${android.os.Build.VERSION.RELEASE}\n")
            append("SDK: ${android.os.Build.VERSION.SDK_INT}\n")
            append("Time: ${Date()}")
        }
    }
    
    fun resetFailedAttempts() {
        failedAttempts = 0
        lastAttemptTime = 0L
    }
    
    fun getFailedAttemptsCount(): Int = failedAttempts
    
    fun cleanup() {
        cameraExecutor.shutdown()
    }
}

