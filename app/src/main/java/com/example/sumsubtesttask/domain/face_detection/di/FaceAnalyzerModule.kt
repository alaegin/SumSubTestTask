package com.example.sumsubtesttask.domain.face_detection.di

import com.example.sumsubtesttask.domain.face_detection.FaceDetectionAnalyzerWrapper
import com.example.sumsubtesttask.domain.face_detection.FaceDetectionAnalyzerWrapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FaceAnalyzerModule {

    @Binds
    abstract fun bindFaceDetectionAnalyzerWrapper(impl: FaceDetectionAnalyzerWrapperImpl): FaceDetectionAnalyzerWrapper
}
