package com.relateddigital.relateddigital_google.model

import com.google.gson.annotations.SerializedName
import com.relateddigital.relateddigital_google.model.GiftBox
import java.io.Serializable

class ActionResponse : Serializable {
    @SerializedName("FavoriteAttributeAction")
    var mFavoriteAttributeAction: List<FavoriteAttributeAction>? = null

    @SerializedName("MailSubscriptionForm")
    var mMailSubscriptionForm: List<MailSubscriptionForm>? = null

    @SerializedName("SpinToWin")
    var mSpinToWinList: List<SpinToWin>? = null

    @SerializedName("ShakeToWin")
    var mShakeToWinList: List<ShakeToWin>? = null

    @SerializedName("ScratchToWin")
    var mScratchToWinList: List<ScratchToWin>? = null

    @SerializedName("ProductStatNotifier")
    var mProductStatNotifierList: List<ProductStatNotifier>? = null

    @SerializedName("Story")
    var mStory: List<BannerStory>? = null

    @SerializedName("Drawer")
    var mDrawer: List<Drawer>? = null

    @SerializedName("GiftRain")
    var mGiftRain: List<GiftRain>? = null

    @SerializedName("GiftBox")
    var mGiftBox: List<GiftBox>? = null

    @SerializedName("FindToWin")
    var mFindToWin: List<FindToWin>? = null

    @SerializedName("AppBanner")
    var mAppBanner: List<AppBanner>? = null

    @SerializedName("VERSION")
    var version: Int? = null

    @SerializedName("capping")
    var capping: String? = null

    var favoriteAttributeAction: List<FavoriteAttributeAction>?
        get() = mFavoriteAttributeAction
        set(favoriteAttributeAction) {
            mFavoriteAttributeAction = favoriteAttributeAction
        }
    var mailSubscriptionForm: List<MailSubscriptionForm>?
        get() = mMailSubscriptionForm
        set(mailSubscriptionForm) {
            mMailSubscriptionForm = mailSubscriptionForm
        }
    var spinToWinList: List<SpinToWin>?
        get() = mSpinToWinList
        set(spinToWinList) {
            mSpinToWinList = spinToWinList
        }
    var shakeToWinList: List<ShakeToWin>?
        get() = mShakeToWinList
        set(ShakeToWin) {
            mShakeToWinList = shakeToWinList
        }
    var scratchToWinList: List<ScratchToWin>?
        get() = mScratchToWinList
        set(scratchToWinList) {
            mScratchToWinList = scratchToWinList
        }

    var productStatNotifierList: List<ProductStatNotifier>?
        get() = mProductStatNotifierList
        set(productStatNotifierList) {
            mProductStatNotifierList = productStatNotifierList
        }

    var story: List<BannerStory>?
        get() = mStory
        set(story) {
            mStory = story
        }

    var drawer: List<Drawer>?
        get() = mDrawer
        set(story) {
            mDrawer = drawer
        }
}