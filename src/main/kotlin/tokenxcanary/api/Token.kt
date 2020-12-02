package tokenxcanary.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.KtorExperimentalAPI
import tokenxcanary.config.Environment
import tokenxcanary.token.ClientAuthentication

@KtorExperimentalAPI
fun Routing.token(maskinporten: Environment.Maskinporten) {
    get("/token") {
        call.respond(HttpStatusCode.OK, ClientAuthentication(maskinporten).tokenRequest())
    }
}