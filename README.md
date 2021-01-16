Clean and simple android rss reader.

This app can be used to read the latest news from the following media outlets in Hungary:

* 444.hu
* azonnali.hu
* hang.hu


TODO:

Content:
* Add more news sources

Features:
* Add action button to view page in a browser
* Add action button to share a page
* Add pull gesture to force refresh on a given feed
* Add scroll to top by tapping on a tab button
* Add option to periodically synchronize feeds in the background (when the app is not running)

UI:
* Visually mark items that has been read before
* Visually mark new items added to top recently
* Mark feeds where there is new items on top after refresh
* Preserve article list positions after navigating
* Add visual separator that marks days and hours in lists
* Add option to show/hide article images in the lists

Consistency, optimization:
* Disable further navigation in WebView (prevent clicking on links)
* Refresh feeds only after given interval, eg. like wait 5-10 min. with a refresh
* Add check for limited/unlimited network availability (wifi/mobile network)

Planned:
* Try to honor RSS feed props like, sy:updatePeriod/sy:updateFrequency if applicable
* Show rss sources list with option to disable/enable + detailed source information like, favicon, description, copyright, author, etc.
* If the rss is eg. a podcast feed then show the podcast information in a custom format, parsing itunes:, enclosure tags
* Usage statistics with Google Analytics
