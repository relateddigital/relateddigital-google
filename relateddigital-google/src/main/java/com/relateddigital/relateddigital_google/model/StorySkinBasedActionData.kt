package com.relateddigital.relateddigital_google.model

import java.io.Serializable

class StorySkinBasedActionData : Serializable {
    var stories: List<SkinBasedStories>? = null
    var taTemplate: String? = null
    var ExtendedProps: String? = null
    val report: MailSubReport? = null
    var after: Boolean?= null
}