/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.cameraxextensions.model

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LensFacing
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureLatencyEstimate
import androidx.camera.extensions.ExtensionMode

/**
 * Defines the current UI state of the camera during pre-capture.
 * The state encapsulates the available camera extensions, the available camera lenses to toggle,
 * the current camera lens, the current extension mode, and the state of the camera.
 */
data class CameraUiState(
    val cameraState: CameraState = CameraState.NOT_READY,
    val availableExtensions: List<Int> = emptyList(),
    val availableCameraLens: List<Int> = listOf(LENS_FACING_BACK),
    @LensFacing val cameraLens: Int = LENS_FACING_BACK,
    @ExtensionMode.Mode val extensionMode: Int = ExtensionMode.NONE,
    val realtimeCaptureLatencyEstimate: ImageCaptureLatencyEstimate =
        ImageCaptureLatencyEstimate.UNDEFINED_IMAGE_CAPTURE_LATENCY
)

/**
 * Defines the current state of the camera.
 */
enum class CameraState {
    /**
     * Camera hasn't been initialized.
     */
    NOT_READY,

    /**
     * Camera is open and presenting a preview stream.
     */
    READY,

    /**
     * Camera is open and preview stream is currently running.
     * Updates during this period are from camera state operations.
     */
    PREVIEW_ACTIVE,

    /**
     * Camera is initialized but the preview has been stopped.
     */
    PREVIEW_STOPPED
}

/**
 * Defines the various states during post-capture.
 */
sealed class CaptureState {
    /**
     * Capture capability isn't ready. This could be because the camera isn't initialized, or the
     * camera is changing the lens, or the camera is switching to a new extension mode.
     */
    object CaptureNotReady : CaptureState()

    /**
     * Capture capability is ready.
     */
    object CaptureReady : CaptureState()

    /**
     * Capture process has started.
     */
    object CaptureStarted : CaptureState()

    /**
     * Capture postview is ready
     */
    data class CapturePostview(val bitmap: Bitmap): CaptureState()

    /**
     * Capture process progress updated with the [progress] value
     */
    data class CaptureProcessProgress(val progress: Int): CaptureState()

    /**
     * Capture completed successfully.
     */
    data class CaptureFinished(
        val outputResults: ImageCapture.OutputFileResults,
        val isProcessProgressSupported: Boolean
    ) : CaptureState()

    /**
     * Capture failed with an error.
     */
    data class CaptureFailed(val exception: Exception) : CaptureState()
}
