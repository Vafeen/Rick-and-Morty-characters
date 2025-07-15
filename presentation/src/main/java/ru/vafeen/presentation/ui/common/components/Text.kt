package ru.vafeen.presentation.ui.common.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import ru.vafeen.presentation.ui.common.utils.suitableColor
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * A customized Text component for the primary theme.
 * Automatically uses black text color suitable for light-themed backgrounds.
 *
 * @param text The text to be displayed
 * @param modifier Optional Modifier for styling or behavior
 * @param fontSize The size of the text
 * @param fontStyle The font style (normal or italic)
 * @param fontWeight The weight of the font
 * @param fontFamily The font family to be used
 * @param letterSpacing The spacing between letters
 * @param textDecoration Text decorations like underline or strikethrough
 * @param textAlign The alignment of the text within its container
 * @param lineHeight The height of each line of text
 * @param overflow How visual overflow should be handled
 * @param softWrap Whether text should break at soft line breaks
 * @param maxLines The maximum number of lines allowed
 * @param minLines The minimum number of lines to display
 * @param onTextLayout Callback for text layout results
 * @param style The base text style to be used
 */
@Composable
internal fun ThisThemeText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = text,
    modifier = modifier,
    color = AppTheme.colors.text,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    minLines = minLines,
    onTextLayout = onTextLayout,
    style = style
)

/**
 * A customized Text component for the opposite theme.
 * Automatically uses white text color suitable for dark-themed backgrounds.
 *
 * @param text The text to be displayed
 * @param modifier Optional Modifier for styling or behavior
 * @param fontSize The size of the text
 * @param fontStyle The font style (normal or italic)
 * @param fontWeight The weight of the font
 * @param fontFamily The font family to be used
 * @param letterSpacing The spacing between letters
 * @param textDecoration Text decorations like underline or strikethrough
 * @param textAlign The alignment of the text within its container
 * @param lineHeight The height of each line of text
 * @param overflow How visual overflow should be handled
 * @param softWrap Whether text should break at soft line breaks
 * @param maxLines The maximum number of lines allowed
 * @param minLines The minimum number of lines to display
 * @param onTextLayout Callback for text layout results
 * @param style The base text style to be used
 */
@Composable
internal fun OppositeThemeText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = text,
    modifier = modifier,
    color = AppTheme.colors.background,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    minLines = minLines,
    onTextLayout = onTextLayout,
    style = style
)

/**
 * A smart Text component that automatically selects suitable text color
 * based on the provided background color for optimal contrast.
 *
 * @param text The text to be displayed
 * @param background The background color used to determine suitable text color
 * @param modifier Optional Modifier for styling or behavior
 * @param fontSize The size of the text
 * @param fontStyle The font style (normal or italic)
 * @param fontWeight The weight of the font
 * @param fontFamily The font family to be used
 * @param letterSpacing The spacing between letters
 * @param textDecoration Text decorations like underline or strikethrough
 * @param textAlign The alignment of the text within its container
 * @param lineHeight The height of each line of text
 * @param overflow How visual overflow should be handled
 * @param softWrap Whether text should break at soft line breaks
 * @param maxLines The maximum number of lines allowed
 * @param minLines The minimum number of lines to display
 * @param onTextLayout Callback for text layout results
 * @param style The base text style to be used
 */
@Composable
internal fun SuitableColorText(
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
) = Text(
    text = text,
    modifier = modifier,
    color = background.suitableColor(),
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    minLines = minLines,
    onTextLayout = onTextLayout,
    style = style
)