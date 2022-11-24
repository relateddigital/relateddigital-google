package com.relateddigital.relateddigital_google.inapp.story

import java.io.Serializable

interface StoryItemClickListener : Serializable {
    fun storyItemClicked(storyLink: String?)
}