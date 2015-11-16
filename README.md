# konrad

[![Build Status](https://travis-ci.org/feedm3/konrad.svg)](https://travis-ci.org/feedm3/konrad)

konrad checks your URL's in a given intervall. If they are down he notifies you in [slack](https://slack.com/).

## Run

Download the [konrad.rar](https://github.com/feedm3/konrad/releases/tag/v0.5.0) and edit
the following properties inside the `application.properties`

1. `konrad.webhookurl` your slack webhook URL
2. `konrad.urls[0]` array of your URLs which konrad should check
3. `konrad.interval` interval (in milliseconds) in which konrad checks your URLs (default = 5000)

Start konrad with `java -jar konrad-0.5.0.jar`

## Test

To run all tests just hit

```
gradlew test
```

>  You have to set the ```konrad.webhookurl``` for the
integration tests.