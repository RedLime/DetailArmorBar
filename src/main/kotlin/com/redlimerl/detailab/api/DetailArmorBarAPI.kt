@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.redlimerl.detailab.api

object DetailArmorBarAPI {

    /**
     * Create the ArmorBarBuilder
     */
    @JvmStatic
    fun customArmorBarBuilder(): ArmorBarBuilder {
        return ArmorBarBuilder()
    }

    /**
     * Create the ItemBarBuilder
     */
    @JvmStatic
    fun customItemBarBuilder(): ItemBarBuilder {
        return ItemBarBuilder()
    }
}
