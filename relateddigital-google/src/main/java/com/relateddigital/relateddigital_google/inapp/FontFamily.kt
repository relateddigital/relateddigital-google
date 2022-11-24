package com.relateddigital.relateddigital_google.inapp

enum class FontFamily {
    Monospace {
        override fun toString(): String {
            return "monospace"
        }
    },
    SansSerif {
        override fun toString(): String {
            return "sansserif"
        }
    },
    Serif {
        override fun toString(): String {
            return "serif"
        }
    },
    Default {
        override fun toString(): String {
            return "default"
        }
    }
}