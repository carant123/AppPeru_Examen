package com.example.peruapptest.presentation.executor

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}