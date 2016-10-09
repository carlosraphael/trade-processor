package loadtesting

import java.util.Date
import java.math.BigDecimal;

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.Random

/**
 * Basic Gatling simulation that injects json ->
 * 
 * {
    "userId" : 134256,
    "currencyFrom" : "EUR",
    "currencyTo" : "GBP",
    "amountSell" : 1000.0,
    "amountBuy" : 747.1,
    "rate" : 0.7471,
    "timePlaced" : "24-JAN-15 10:27:44",
    "originatingCountry" : "FR"
  }
 *
 * -> through http on:
 * 	- /consumption/trade <-
 * 	-	/consumption/trades (gatling batch not implemented yet)
 */
class TradeSimulation extends Simulation {

  val userIdFeeder = Iterator.continually(Map("userId" -> Random.nextInt(Integer.MAX_VALUE)))
  val amountSellFeeder = Iterator.continually(Map("amountSell" -> BigDecimal.valueOf(Random.nextInt(10000))))
  val amountBuyFeeder = Iterator.continually(Map("amountBuy" -> BigDecimal.valueOf(Random.nextInt(10000))))
  val timeFeeder = Iterator.continually(Map("timePlaced" -> DateTime.now().toString("dd-MMM-yy hh:mm:ss")))

  val httpConf = http
    .baseURL(System.getProperty("trade.hostname", "http://localhost:8080"))
		.header("Authorization", "Bearer " + System.getProperty("oauth2.token"))
    .contentTypeHeader("application/json")
    .acceptHeader("application/json")

  val tradeLoadTesting = scenario("Trade load testing")
    .feed(userIdFeeder).feed(amountSellFeeder)
    .feed(amountBuyFeeder).feed(timeFeeder)
    .exec(http("Trade random injection")
    .post("/consumption/trade")
    .body(StringBody(
      """{
         |"userId" : ${userId},
         |"currencyFrom" : "EUR",
         |"currencyTo" : "GBP",
         |"amountSell" : ${amountSell},
         |"amountBuy" : ${amountBuy},
         |"rate" : 0.7471,
         |"timePlaced" : "${timePlaced}",
         |"originatingCountry" : "FR"
         |}""".stripMargin))
    .check(status.is(202)))

  setUp(tradeLoadTesting.inject(rampUsersPerSec(1) to 20 during(1 minute)).protocols(httpConf))
}
