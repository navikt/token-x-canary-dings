package tokenxcanary

import com.nimbusds.jwt.JWT
import io.ktor.util.KtorExperimentalAPI
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldBeEqualTo
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test
import tokenxcanary.redis.Cache
import tokenxcanary.token.Authentication
import java.time.Instant
import java.util.Date

@KtorExperimentalAPI
class AuthenticationTest {

    @Test
    fun `Generate, sign and Validate JWT`() {
        withMockOAuth2Server {
            val environment = testEnvironment(this)
            Cache(environment)
            val assertion = Authentication(environment.maskinporten).assertion(environment.maskinporten.scopes)
            val parsedToken = parse(assertion)
            val claimsSet = parsedToken.jwtClaimsSet
            parsedToken `should be instance of` JWT::class
            claimsSet.getStringClaim("scope") shouldBeEqualTo environment.maskinporten.scopes
            claimsSet.getStringClaim("iss") shouldBeEqualTo environment.maskinporten.clientId
            claimsSet.getStringListClaim("aud").singleOrNull() shouldBeEqualTo this.issuerUrl("maskinporten").toString()
            val now = Date.from(Instant.now())
            MatcherAssert.assertThat("Claim `iat` is not greater", now.after(Date(claimsSet.getDateClaim("iat").time)))
            MatcherAssert.assertThat("Claim `exp` is not less", now.before(Date(claimsSet.getDateClaim("exp").time)))
        }
    }
}
