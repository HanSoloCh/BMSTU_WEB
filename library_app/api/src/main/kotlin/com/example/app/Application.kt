package com.example.app

import com.example.app.di.appModule
import com.example.app.plugin.configureAuthentication
import com.example.app.plugin.configureLog
import com.example.app.plugin.configureSerialization
import com.example.app.plugin.configureStatusPages
import com.example.app.route.v1.version1
import com.example.app.route.v2.version2
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) = EngineMain.main(args)


fun Application.module() {
    configureLog()
    configureSerialization()
    configureAuthentication()
    configureStatusPages()

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
    }
    install(Koin) {
        slf4jLogger()
        modules(appModule(environment.config))
    }
    routing {
        route("/api") {
            version1()
            version2()
        }
    }
}
