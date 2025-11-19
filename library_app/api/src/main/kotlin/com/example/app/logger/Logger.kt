package com.example.app.logger

import org.slf4j.LoggerFactory

object Logger {
    fun logAction(
        message: String,
        logLevel: LogLevel = LogLevel.INFO,
    ) {
        val logger = LoggerFactory.getLogger("AppLogger")
        when (logLevel) {
            LogLevel.INFO -> logger.info(message)
            LogLevel.DEBUG -> logger.debug(message)
            LogLevel.WARNING -> logger.warn(message)
            LogLevel.ERROR -> logger.error(message)
        }
    }
}