package papyrus.demo

import com.google.android.gms.analytics.GoogleAnalytics
import papyrus.sleuth.Sleuth
import papyrus.sleuth.annotation.BooleanParam
import papyrus.sleuth.annotation.FormattedParam
import papyrus.sleuth.ga.GASleuthTracker
import papyrus.sleuth.ga.annotation.*


object Analytics {
    private val globalTracker = GoogleAnalytics.getInstance(App.get()).newTracker("0")
    private val stationTracker = GoogleAnalytics.getInstance(App.get()).newTracker("0")
    private val salesTracker = GoogleAnalytics.getInstance(App.get()).newTracker("0")

    val tracker: IAnalytics = Sleuth.addTracker(IMainAnalytics::class.java, GASleuthTracker(globalTracker))
            .addTracker(IMainAnalytics::class.java, GASleuthTracker(stationTracker))
            .addTracker(ISalesAnalytics::class.java, GASleuthTracker(salesTracker))
            .create(IAnalytics::class.java)


    interface IAnalytics : ISalesAnalytics, IMainAnalytics

    interface ISalesAnalytics : IScreenViewAnalytics {
        @GAEvent(category = "DoubleClick Ads", action = "DoubleClick Adhesion Ad View")
        fun dfpAdViewed()

        @GAEvent(category = "DoubleClick Ads", action = "DoubleClick Cube Ad View")
        fun cubeAdViewed()

        @GAEvent(category = "Nativo Ads", action = "Nativo Ad View")
        fun nativoAdViewed()
    }

    interface IMainAnalytics : IScreenViewAnalytics {
        @GAEvent(category = "Article", action = "Video Play Start")
        fun startVideoEvent(@CustomDimension(index = 2) @Label category: String,
                            @CustomMetric(index = 3) @BooleanParam(onTrue = "1", onFalse = "0") firstPlay: Boolean)

        @GAEvent(category = "Article", action = "Video Play Stop")
        fun stopVideoEvent(@CustomDimension(index = 2) @Label category: String, @Value percent: String)

        @GAEvent(category = "Article", action = "Web Video Play Start")
        fun startWebVideoEvent(@CustomDimension(index = 2) @Label category: String)

        @GAEvent(category = "Article", action = "Article Names")
        fun articleViewedEvent(@Label slug: String, @CustomDimension(index = 2) category: String)

        @GAEvent(category = "Article", action = "Video Names")
        fun videoViewedEvent(@Label @FormattedParam("Native: %s") slug: String, @CustomDimension(index = 2) category: String)

        @GAEvent(category = "Article", action = "Video Names")
        fun webVideoViewedEvent(@Label @FormattedParam("Web: %s") slug: String, @CustomDimension(index = 2) category: String)

        @GAEvent(category = "Livestream", action = "Watch Live Play")
        fun livestreamPlayEvent(@Label origin: String)

        @GAEvent(category = "Video Brief", action = "Push Message")
        @CustomDimension(index = 8, value = "Video Update Viewed")
        @CustomMetric(index = 4, value = "1")
        fun videoUpdateFromPushEvent(@Label title: String)

        @GAEvent(category = "Video Brief", action = "In-App Card")
        @CustomDimension(index = 8, value = "Video Update Viewed")
        @CustomMetric(index = 4, value = "1")
        fun videoUpdateFromCardEvent(@Label title: String)

        @GAEvent(category = "Video Brief", action = "Video Expired")
        fun videoUpdateExpiredEvent(@Label title: String)

        @GAEvent(category = "Video Brief", action = "Video Stop")
        fun videoUpdateStopEvent(@Label title: String, @Value time: String)

        @GAEvent(category = "Video Brief", action = "Video Complete")
        fun videoUpdateCompleteEvent(@Label title: String, @Value time: String)

        @GAEvent(category = "Video Brief", action = "Tutorial Video Play")
        @CustomDimension(index = 6, value = "Video Brief Tutorial Play")
        fun videoUpdateCoachEvent()

        @GAEvent(category = "Video Brief", action = "Video Update Notification Request")
        fun videoUpdateNotificationResponseEvent(@Label @BooleanParam(onTrue = "Yes", onFalse = "No") accepted: Boolean)

        @GAEvent(category = "Latest News Scroll", action = "Page Scrolled")
        fun latestNewsPaginatedEvent()

        @GAEvent(category = "Splash Screen", action = "Sponsorship")
        fun splashSponsorServedEvent(@Label url: String)

        @GAEvent(category = "User Satisfaction Survey", action = "Viewed")
        fun feedbackPromptShownEvent()

        @GAEvent(category = "User Satisfaction Survey")
        fun feedbackPromptPage1ResponseEvent(@Action @FormattedParam("Clicked %sSatisfied") @BooleanParam(onTrue = "", onFalse = "Not ") satisfied: Boolean)

        @GAEvent(category = "User Satisfaction Survey")
        fun feedbackPromptEmailResponseEvent(@Action @FormattedParam("Feedback Email = %s") @BooleanParam(onTrue = "Yes", onFalse = "No") sentEmail: Boolean)

        @GAEvent(category = "User Satisfaction Survey")
        fun feedbackPromptRatingResponseEvent(@Action @FormattedParam("App Store Rating = %s") @BooleanParam(onTrue = "Yes", onFalse = "No") ratedApp: Boolean)

        @GAEvent(category = "Favorite", action = "Category Screen")
        fun storySavedFromListEvent(@Label category: String)

        @GAEvent(category = "Favorite", action = "Article Screen")
        fun storySavedFromArticleEvent(@Label category: String)

        @GAEvent(category = "Social Share", action = "Native")
        fun storySharedEvent(@Label category: String)

        @GAEvent(category = "News Tips", action = "Submit")
        fun submitStoryEvent(@Label origin: String)

        @GAEvent(category = "Search", action = "Search Performed")
        fun searchPerformedEvent(@Label query: String)

        @GAEvent(category = "Share App", action = "Native")
        fun shareAppEvent()

        @GAEvent(category = "Alert", action = "Push Notification Request")
        fun pushNotificationPromptEvent(@Label @BooleanParam(onTrue = "Yes", onFalse = "No") enabled: Boolean)

        @GAEvent(category = "Alert", action = "Current Location Request")
        fun currentLocationRequestEvent(@Label @BooleanParam(onTrue = "On", onFalse = "Off") enabled: Boolean)

        @GAEvent(category = "Weather", action = "Weather Alert Viewed")
        fun weatherAlertClickedEvent(@Label title: String)

        @GAEvent(category = "Story Correction", action = "Submit")
        fun submitStoryCorrectionEvent()

        @GAEvent(category = "Radar", action = "Loop Radar")
        fun loopRadarToggleEvent(@Label @BooleanParam(onTrue = "On", onFalse = "Off") enabled: Boolean)

        @GAEvent(category = "Radar", action = "Future Radar")
        fun futureRadarToggleEvent(@Label @BooleanParam(onTrue = "On", onFalse = "Off") enabled: Boolean)

        @GAEvent(category = "Weather", action = "Module Interaction")
        @Label("Hourly Weather Scroll")
        fun hourlyWeatherScroll(@Value index: Int)

        @GAEvent(category = "Weather", action = "Module Interaction")
        @Label("Watch Video Forecast")
        fun watchVideoForecast(@Value index: Int)

        @GAEvent(category = "Weather", action = "Module Interaction")
        @Label("View More")
        fun viewMoreWeather(@Value index: Int)

        @GAEvent(category = "Weather", action = "Module Interaction")
        @Label("Weather Alerts Click")
        fun weatherAlertsClicked(@Value index: Int)

        @GAEvent(category = "Livestream", action = "Module Interaction")
        fun watchLivestreamNow(@Label @FormattedParam("Watch Now: %s") title: String, @Value index: Int)

        @GAEvent(category = "Livestream", action = "Module Interaction")
        fun reminderSetForLivestream(@Label @FormattedParam("Set Reminder: %s") title: String, @Value index: Int)

        @GAEvent(category = "News Tips", action = "Module Interaction")
        fun newsTipsClicked(@Label buttonText: String, @Value index: Int)

        @GAEvent(category = "Closings", action = "Module Interaction")
        fun closingsClicked(@Label buttonText: String, @Value index: Int)

        @GAEvent(category = "Elections", action = "Module Interaction")
        fun electionsClicked(@Label buttonText: String, @Value index: Int)


    }

    interface IScreenViewAnalytics {
        @GAScreenView("Latest News")
        fun latestNewsScreenView(@CustomDimension(index = 1) callLetters: String,
                                 @CustomDimension(index = 11) @BooleanParam(onTrue = "Enabled", onFalse = "Disabled") pushEnabled: Boolean,
                                 @CustomDimension(index = 12) @BooleanParam(onTrue = "Enabled", onFalse = "Disabled") currentLocationEnabled: Boolean,
                                 @CustomDimension(index = 13) @BooleanParam(onTrue = "Enabled", onFalse = "Disabled") videoUpdatesEnabled: Boolean)

        @GAScreenView("Top Stories")
        fun topNewsScreenView()

        @GAScreenView("Saved")
        fun savedScreenView()

        @GAScreenView("More News")
        fun moreNewsScreenView()

        @GAScreenView("Latest News Article")
        @CustomMetric(index = 1, value = "1")
        fun latestNewsArticleScreenView(@CustomDimension(index = 2) category: String,
                                        @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                        @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Top Story Article")
        @CustomMetric(index = 1, value = "1")
        fun topNewsArticleScreenView(@CustomDimension(index = 2) category: String,
                                     @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                     @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Breaking News Article")
        @CustomMetric(index = 1, value = "1")
        fun breakingNewsArticleScreenView(@CustomDimension(index = 2) category: String,
                                          @CustomDimension(index = 9) @BooleanParam isFeatured: Boolean, //Will automatically ignore. kept as param for consistency.
                                          @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Saved Article")
        @CustomMetric(index = 1, value = "1")
        fun savedArticleScreenView(@CustomDimension(index = 2) category: String,
                                   @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                   @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("More News Article")
        @CustomMetric(index = 1, value = "1")
        fun moreNewsArticleScreenView(@CustomDimension(index = 2) category: String,
                                      @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                      @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Push Article")
        @CustomMetric(index = 1, value = "1")
        fun pushArticleScreenView(@CustomDimension(index = 2) category: String,
                                  @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Search Article")
        @CustomMetric(index = 1, value = "1")
        fun searchArticleScreenView(@CustomDimension(index = 2) category: String,
                                    @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("MessageCenterArticle")
        @CustomMetric(index = 1, value = "1")
        fun messageCenterArticleScreenView(@CustomDimension(index = 2) category: String,
                                           @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Mobile Webview")
        @CustomMetric(index = 1, value = "1")
        fun mobileWebviewScreenView(@CustomDimension(index = 2) category: String,
                                    @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                    @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("AMP Webview")
        @CustomMetric(index = 1, value = "1")
        fun ampWebviewScreenView(@CustomDimension(index = 2) category: String,
                                 @CustomDimension(index = 9) @BooleanParam(onTrue = "Top Story Featured") isFeatured: Boolean,
                                 @CustomMetric(index = 2) @BooleanParam(onTrue = "1", onFalse = "0") fromSwipe: Boolean)

        @GAScreenView("Message Center")
        fun messageCenterScreenView()

        @GAScreenView("Local Weather Page")
        fun weatherScreenView()

        @GAScreenView("Radar")
        fun radarScreenView()

        @GAScreenView("Watch Live")
        fun watchLiveScreenView()

        @GAScreenView("Traffic")
        fun trafficScreenView()

        @GAScreenView("Search")
        fun searchScreenView()

        @GAScreenView("Video Brief")
        fun videoUpdateScreenView()

        @GAScreenView("Video Brief Tutorial")
        fun videoUpdateCoachScreenView()

        @GAScreenView("Weather Alerts")
        fun weatherAlertsScreenView()

        @GAScreenView("Share Story")
        fun shareStoryScreenView()

        @GAScreenView("Terms of Use")
        fun termsOfUseScreenView()

        @GAScreenView("Privacy Policy")
        fun privacyPolicyScreenView()

        @GAScreenView("About")
        fun aboutScreenView()

        @GAScreenView("Contact Us")
        fun contactUsScreenView()

        @GAScreenView("Story Correction")
        fun storyCorrectionScreenView()

        @GAScreenView("Notification Settings")
        fun alertsScreenView()
    }
}