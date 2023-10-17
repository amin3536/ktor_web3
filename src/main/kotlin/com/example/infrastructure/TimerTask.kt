package com.example.infrastructure

import io.ktor.util.logging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

class TimerTask internal constructor(
    name: String,
    private val delay: Duration = Duration.ZERO,
    private val repeat: Duration? = null,
    private val coroutineScope: CoroutineScope = GlobalScope,
    action: suspend () -> Unit
) {
    private val log = KtorSimpleLogger("om.example.infrastructure")
    private val keepRunning = AtomicBoolean(true)
    private var job: Job? = null
    private val tryAction = suspend {
        try {
            action()
        } catch (e: Throwable) {
            log.warn("$name timer action failed: $action")
        }
    }

    fun start() {
        job = coroutineScope.launch {
            delay(delay)
            if (repeat != null) {
                while (keepRunning.get()) {
                    tryAction()
                    delay(repeat)
                }
            } else {
                if (keepRunning.get()) {
                    tryAction()
                }
            }
        }
    }

    /**
     * Initiates an orderly shutdown, where if the timer task is currently running,
     * we will let it finish, but not run it again.
     * Invocation has no additional effect if already shut down.
     */
    fun shutdown() {
        keepRunning.set(false)
    }

    /**
     * Immediately stops the timer task, even if the job is currently running,
     * by cancelling the underlying Koros Job.
     */
    fun cancel() {
        shutdown()
        job?.cancel("cancel() called")
    }

    companion object {
        /**
         * Runs the given `action` after the given `delay`,
         * once the `action` completes, waits the `repeat` duration
         * and runs again, until `shutdown` is called.
         *
         * if action() throws an exception, it will be swallowed and a warning will be logged.
         */
        fun start(
            name: String,
            delay: Duration = Duration.ZERO,
            repeat: Duration? = null,
            coroutineScope: CoroutineScope = GlobalScope,
            action: suspend () -> Unit
        ): TimerTask =
            TimerTask(name, delay, repeat, coroutineScope, action).also { it.start() }
    }
}