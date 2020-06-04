package com.example.peruapptest.presentation.executor

import io.reactivex.Scheduler
import java.util.concurrent.Executor

interface ThreadExecutor : Executor{
    val scheduler: Scheduler
}