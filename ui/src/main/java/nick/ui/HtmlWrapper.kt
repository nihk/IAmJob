package nick.ui

import android.os.Build
import android.text.Html
import android.text.Spanned
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HtmlWrapper @Inject constructor() {

    fun fromHtml(text: String): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(text)
    }
}