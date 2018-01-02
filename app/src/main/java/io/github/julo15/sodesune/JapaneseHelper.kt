package io.github.julo15.sodesune

/**
 * Created by julianlo on 1/1/18.
 */
object JapaneseHelper {

    fun containsJapanese(text: String): Boolean = regex.containsMatchIn(text)

    fun stripYukiko(text: String): String = text.replace(bracketed, "")

    // https://gist.github.com/ryanmcgrath/982242
    val japaneseCharRegexString = """[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B"""
    val regex = japaneseCharRegexString.toRegex()
    val bracketed = """\(($japaneseCharRegexString)*\)""".toRegex()
}