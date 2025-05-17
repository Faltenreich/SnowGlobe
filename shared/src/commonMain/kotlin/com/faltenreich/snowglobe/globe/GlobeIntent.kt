package com.faltenreich.snowglobe.globe

sealed interface GlobeIntent {

    data object Toggle : GlobeIntent
}