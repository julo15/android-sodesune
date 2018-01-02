package io.github.julo15.sodesune

import org.junit.Assert
import org.junit.Test

/**
 * Created by julianlo on 1/1/18.
 */
class JapaneseHelperTest {
    @Test
    fun containsJapanese() {
        Assert.assertTrue(JapaneseHelper.containsJapanese("あ"))
        Assert.assertTrue(JapaneseHelper.containsJapanese("aあ"))
        Assert.assertTrue(JapaneseHelper.containsJapanese("あa"))
        Assert.assertTrue(JapaneseHelper.containsJapanese("ア"))
        Assert.assertTrue(JapaneseHelper.containsJapanese("a　あ"))
    }

    @Test
    fun doesNotContainJapanese() {
        Assert.assertFalse(JapaneseHelper.containsJapanese(""))
        Assert.assertFalse(JapaneseHelper.containsJapanese("a"))
        Assert.assertFalse(JapaneseHelper.containsJapanese(" "))
        Assert.assertFalse(JapaneseHelper.containsJapanese("()"))
    }

    @Test
    fun `bracketed characters`() {
        Assert.assertTrue(JapaneseHelper.bracketed.containsMatchIn("(ア)"))
        Assert.assertFalse(JapaneseHelper.bracketed.containsMatchIn("ア)"))
        Assert.assertFalse(JapaneseHelper.bracketed.containsMatchIn("あいう"))

        Assert.assertEquals("あい", JapaneseHelper.stripYukiko("あ(う)い"))
        Assert.assertEquals("あいき", JapaneseHelper.stripYukiko("あ(う)い(さ)き"))
    }
}