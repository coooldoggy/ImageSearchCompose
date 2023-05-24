package com.coooldoggy.imagesearchcompose.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.coooldoggy.imagesearchcompose.util.MultipleEventsCutter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BasicTextField(
    value: String,
    valueChangeListener: (String) -> Unit,
    delayedValueChangeListener: ((String) -> Unit)? = null,
    onValueChangeDelay: Long = 0L,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    hintText: String = "",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(42.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: (@Composable (innerTextField: @Composable () -> Unit) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    border: BorderStroke = BorderStroke(width = 1.5.dp, brush = SolidColor(Color(0xFFF2F3F5))),
    backgroundShape: Shape = RoundedCornerShape(6.dp),
    leftContent: (@Composable () -> Unit)? = null,
    rightContent: (@Composable () -> Unit)? = null,
    elevation: Int = 0,
    textSelectionColors: TextSelectionColors = TextSelectionColors(
        handleColor = Color(0xFF202429),
        backgroundColor = Color(0xFF989CA1),
    ),
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    contentVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    hintColor: Color = Color(0xFFCFD2D7),
) {
    Card(
        modifier = modifier,
        border = border,
        shape = backgroundShape,
        backgroundColor = backgroundColor,
        elevation = elevation.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = contentVerticalAlignment,
        ) {
            leftContent?.let {
                leftContent()
            }

            MultipleEventsCutter(onValueChangeDelay) { _cutter ->
                Row(modifier = Modifier.weight(1f)) {
                    androidx.compose.foundation.text.BasicTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = value,
                        onValueChange = {
                            valueChangeListener.invoke(it)
                            _cutter.processEvent {
                                delayedValueChangeListener?.invoke(it)
                            }
                        },
                        enabled = enabled,
                        readOnly = readOnly,
                        textStyle = textStyle.copy(
                            color = textColor,
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                        ),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        singleLine = singleLine,
                        maxLines = maxLines,
                        visualTransformation = visualTransformation,
                        onTextLayout = onTextLayout,
                        interactionSource = interactionSource,
                        cursorBrush = cursorBrush,
                        decorationBox = decorationBox ?: { innerTextField ->
                            TextFieldDefaults.TextFieldDecorationBox(
                                value = value,
                                innerTextField = innerTextField,
                                enabled = enabled,
                                singleLine = singleLine,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = MutableInteractionSource(),
                                placeholder = {
                                    Text(
                                        text = hintText,
                                        fontSize = fontSize,
                                        color = hintColor,
                                    )
                                },
                                contentPadding = contentPadding,
                            )
                        },
                    )
                }
            }

            rightContent?.let {
                rightContent()
            }
        }
    }
}
