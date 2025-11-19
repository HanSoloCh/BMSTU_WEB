package com.example.app.plugin

import com.example.app.logger.LogLevel
import com.example.app.logger.Logger
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping

fun Application.configureLog() {
    environment.monitor.subscribe(ApplicationStarting) {
        Logger.logAction("Application starting", LogLevel.INFO)
    }

    environment.monitor.subscribe(ApplicationStarted) {
        Logger.logAction("Application started", LogLevel.INFO)
    }

    environment.monitor.subscribe(ApplicationStopping) {
        Logger.logAction("Application stopping", LogLevel.WARNING)
    }

    environment.monitor.subscribe(ApplicationStopped) {
        Logger.logAction("Application stopped", LogLevel.WARNING)
    }
}