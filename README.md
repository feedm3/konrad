# konrad

[![Build Status](https://travis-ci.org/feedm3/konrad.svg)](https://travis-ci.org/feedm3/konrad)

konrad checks your URL's in a given interval. If one or more is down you get a message in [slack](https://slack.com/).

## Run

Download the [konrad.rar](https://github.com/feedm3/konrad/releases/tag/v0.9.0) and edit
the following mandatory properties inside the `application.properties`

1. `konrad.webhookurl` your slack webhook URL
2. `konrad.urls[0]` array of your URLs which konrad should check

optional properties

1. `konrad.interval` interval (in milliseconds) in which konrad checks your URLs (default: 300000 (5 minutes))
2. `konrad.report-only-when-broken-urls` you only get a report when one or more urls is down (default: true)

You can than start konrad with `java -jar konrad-0.5.0.jar`

By default, konrad will check your URLs every 5 minutes and will then send you a message if a URL is down. You also
will receive a message every midnight to get a feedback that konrad is still running.


## Test

To run all tests just hit

```
gradlew test
```