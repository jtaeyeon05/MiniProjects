package lol.xodus.kspotplayer.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lyrics
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp


private val SLIDER_THUMB_WIDTH = 8.dp
private val SLIDER_THUMB_HEIGHT = 32.dp


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(innerPadding: PaddingValues = PaddingValues()) {
    val density = LocalDensity.current

    var playing by rememberSaveable { mutableStateOf(false) }
    var controlling by rememberSaveable { mutableStateOf(false) }
    var shuffle by rememberSaveable { mutableStateOf(false) }
    var repeat by rememberSaveable { mutableIntStateOf(0) }
    var favorite by rememberSaveable { mutableStateOf(false) }
    var more by rememberSaveable { mutableStateOf(false) }

    var sliderValue by rememberSaveable { mutableFloatStateOf(0.5f) }
    val sliderInteractionSource = remember { MutableInteractionSource() }

    var albumArtOffset by remember { mutableStateOf(Offset.Zero) }
    var sliderOffset by remember { mutableStateOf(Offset.Zero) }
    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
    val sliderThumbOffset by remember {
        derivedStateOf {
            with (density) {
                sliderOffset + Offset(
                    x = (sliderSize.width - SLIDER_THUMB_WIDTH.toPx()) * sliderValue + SLIDER_THUMB_WIDTH.toPx() * 0.5f,
                    y = sliderSize.height * 0.5f
                )
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.1f).compositeOver(MaterialTheme.colorScheme.primary),
                        MaterialTheme.colorScheme.primary,
                    ),
                )
            ),
        color = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        // Player
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Top Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Close"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Rounded.BarChart,
                        contentDescription = "Equalizer"
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Rounded.Cast,
                        contentDescription = "Cast"
                    )
                }
            }

            // Album Art
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp),
                contentAlignment = Alignment.Center,
            ) {
                val albumArtSize by remember { derivedStateOf { min(maxWidth, maxHeight) } }
                Surface(
                    modifier = Modifier
                        .size(albumArtSize)
                        .onGloballyPositioned { coordinates ->
                            val albumSizeHalfPx = with (density) { albumArtSize.toPx() * 0.5f }
                            albumArtOffset = coordinates.positionInWindow() + Offset(x = albumSizeHalfPx, y = albumSizeHalfPx)
                        },
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 12.dp,
                ) {
                    // TODO: Album Art
                    val colorScheme = MaterialTheme.colorScheme
                    val textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black,
                    )

                    val textMeasurer = rememberTextMeasurer()
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawRect(
                            color = colorScheme.primaryContainer
                        )
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "MUSIC",
                            topLeft = Offset(
                                x = albumArtSize.toPx() * 0.5f - textMeasurer.measure(
                                    text = "MUSIC",
                                    style = textStyle,
                                ).size.width.toFloat() * 0.5f,
                                y = albumArtSize.toPx() * 0.5f - textMeasurer.measure(
                                    text = "MUSIC",
                                    style = textStyle,
                                ).size.height.toFloat() * 0.5f
                            ),
                            style = textStyle
                        )
                    }
                }
            }

            // Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Music Title",
                        color = MaterialTheme.extendedColorScheme.contentColorHigh,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                    Text(
                        text = "Artist",
                        color = MaterialTheme.extendedColorScheme.contentColorMiddle,
                        fontSize = 16.sp,
                        maxLines = 1,
                    )
                }
                IconButton(
                    onClick = { favorite = !favorite },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.extendedColorScheme.contentColorLow,
                        contentColor = MaterialTheme.extendedColorScheme.contentColorHigh,
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (favorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                        contentDescription = "Favorite"
                    )
                }
                IconButton(
                    onClick = { more = !more },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.extendedColorScheme.contentColorLow,
                        contentColor = MaterialTheme.extendedColorScheme.contentColorHigh,
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More"
                    )
                }
            }

            // Control Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Slider(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            sliderOffset = coordinates.positionInWindow()
                        }
                        .onSizeChanged {
                            sliderSize = it
                        },
                    value = sliderValue,
                    onValueChange = {
                        controlling = true
                        sliderValue = it
                    },
                    onValueChangeFinished = {
                        controlling = false
                    },
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.extendedColorScheme.contentColorHigh,
                        inactiveTrackColor = MaterialTheme.extendedColorScheme.contentColorMiddle,
                    ),
                    interactionSource = sliderInteractionSource,
                    thumb = { sliderState ->
                        val interactions = remember { mutableStateListOf<Interaction>() }
                        LaunchedEffect(sliderInteractionSource) {
                            sliderInteractionSource.interactions.collect { interaction ->
                                when (interaction) {
                                    is PressInteraction.Press -> interactions.add(interaction)
                                    is PressInteraction.Release -> interactions.remove(interaction.press)
                                    is PressInteraction.Cancel -> interactions.remove(interaction.press)
                                    is DragInteraction.Start -> interactions.add(interaction)
                                    is DragInteraction.Stop -> interactions.remove(interaction.start)
                                    is DragInteraction.Cancel -> interactions.remove(interaction.start)
                                }
                            }
                        }

                        Spacer(
                            Modifier
                                .width(SLIDER_THUMB_WIDTH)
                                .height(SLIDER_THUMB_HEIGHT)
                                .hoverable(interactionSource = sliderInteractionSource)
                                .background(
                                    color = MaterialTheme.extendedColorScheme.contentColorHigh,
                                    shape = RoundedCornerShape(percent = 100)
                                )
                                .alpha(
                                    alpha = animateFloatAsState(
                                        targetValue = if (controlling) 0f else 1f,
                                        animationSpec = tween(100),
                                    ).value
                                )
                        )
                    },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "0:23",
                        color = MaterialTheme.extendedColorScheme.contentColorMiddle,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Start,
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.extendedColorScheme.contentColorLow,
                        contentColor = MaterialTheme.extendedColorScheme.contentColorHigh,
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = "LOSSLESS",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Text(
                        text = "4:15",
                        color = MaterialTheme.extendedColorScheme.contentColorMiddle,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.End,
                    )
                }
            }

            // Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                IconButton(
                    onClick = { shuffle = !shuffle },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (shuffle) MaterialTheme.extendedColorScheme.contentColorHigh else MaterialTheme.extendedColorScheme.contentColorMiddle
                    )
                }
                IconButton(
                    onClick = {  },
                    modifier = Modifier.size(64.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "SkipPrevious"
                    )
                }
                IconButton(
                    onClick = { playing = !playing },
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(if (playing) 36.dp else 48.dp),
                        imageVector = if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = "Play"
                    )
                }
                IconButton(
                    onClick = {  },
                    modifier = Modifier.size(64.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "SkipNext"
                    )
                }
                IconButton(
                    onClick = { if (repeat >= 2) repeat = 0 else repeat += 1 },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (repeat != 2) Icons.Rounded.Repeat else Icons.Rounded.RepeatOne,
                        contentDescription = "Repeat",
                        tint = if (repeat != 0) MaterialTheme.extendedColorScheme.contentColorHigh else MaterialTheme.extendedColorScheme.contentColorMiddle
                    )
                }
            }

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Rounded.Lyrics,
                        contentDescription = "Lyrics",
                        tint = MaterialTheme.extendedColorScheme.contentColorMiddle
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Magic",
                        tint = MaterialTheme.extendedColorScheme.contentColorMiddle
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.List,
                        contentDescription = "Tracks",
                        tint = MaterialTheme.extendedColorScheme.contentColorMiddle
                    )
                }
            }
        }

        // Overlay
        AnimatedVisibility(
            visible = controlling,
            enter = fadeIn(tween(100)),
            exit = fadeOut(tween(100)),
        ) {
            PlayerOverlay(
                albumArtOffset = albumArtOffset,
                sliderThumbOffset = sliderThumbOffset,
                sliderThumbSize = DpSize(width = SLIDER_THUMB_WIDTH, height = SLIDER_THUMB_HEIGHT),
                debugMode = favorite,
            )
        }
    }
}


@Preview(
    widthDp = 360,
    heightDp = 720,
    showBackground = true,
)
@Composable
fun PlayerScreenPreview() {
    AppTheme {
        PlayerScreen()
    }
}
