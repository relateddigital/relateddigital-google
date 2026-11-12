package com.relateddigital.relateddigital_google

import com.relateddigital.relateddigital_google.model.Element
import com.relateddigital.relateddigital_google.model.RelatedDigitalModel
import com.relateddigital.relateddigital_google.push.PushNotificationManager
import com.relateddigital.relateddigital_google.push.carousel.CarouselBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class CrashFixesUnitTest {

    private fun element(url: String?): Element {
        return Element().apply { this.url = url }
    }

    @Test
    fun carouselElementUrl_validId_returnsUrl() {
        val elements = listOf(element("https://a"), element("https://b"))
        assertEquals("https://a", CarouselBuilder.getElementUrl(elements, "1"))
        assertEquals("https://b", CarouselBuilder.getElementUrl(elements, "2"))
    }

    @Test
    fun carouselElementUrl_invalidInput_returnsNull() {
        val elements = listOf(element("https://a"))
        assertNull(CarouselBuilder.getElementUrl(elements, null))
        assertNull(CarouselBuilder.getElementUrl(elements, ""))
        assertNull(CarouselBuilder.getElementUrl(elements, "abc"))
        assertNull(CarouselBuilder.getElementUrl(elements, "0"))
        assertNull(CarouselBuilder.getElementUrl(elements, "5"))
        assertNull(CarouselBuilder.getElementUrl(null, "1"))
    }

    @Test
    fun actionIconId_parsesOnlyNumericValues() {
        assertEquals(2131230850, PushNotificationManager.parseActionIconId("2131230850"))
        assertEquals(12, PushNotificationManager.parseActionIconId(" 12 "))
        assertNull(PushNotificationManager.parseActionIconId(null))
        assertNull(PushNotificationManager.parseActionIconId(""))
        assertNull(PushNotificationManager.parseActionIconId("0"))
        assertNull(PushNotificationManager.parseActionIconId("ic_launcher"))
        assertNull(PushNotificationManager.parseActionIconId("https://example.com/icon.png"))
    }

    @Test
    fun modelExtra_returnsSnapshot() {
        val model = RelatedDigitalModel(
            organizationId = "", profileId = "", dataSource = "", appVersion = "",
            pushPermissionStatus = "", osVersion = "", sdkVersion = "", deviceType = "",
            deviceName = "", carrier = "", identifierForVendor = "", local = "",
            cookieId = null, userAgent = "", visitorData = "", visitData = ""
        )
        model.addWithoutSaving("key1", "value1")

        val snapshot = model.getExtra()
        val iterator = snapshot.keys.iterator()
        model.addWithoutSaving("key2", "value2")
        iterator.next()

        assertEquals(1, snapshot.size)
        assertFalse(snapshot.containsKey("key2"))
        assertEquals(2, model.getExtra().size)
    }
}
