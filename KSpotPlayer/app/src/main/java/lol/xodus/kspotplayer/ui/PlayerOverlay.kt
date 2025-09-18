package lol.xodus.kspotplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan


const val PI_F = PI.toFloat()
const val SAFE_AREA_PX = 40f  // 블러처리 대비 추가 그림 영역
const val DEBUG_WIDTH = 4f // 디버그 선 굵기
const val SCATTER_RADIAN = PI_F * 0.15f // 확산 영역


@Composable
fun PlayerOverlay(
    albumArtOffset: Offset,
    sliderThumbOffset: Offset,
    sliderThumbSize: DpSize,
    debugMode: Boolean = false,
) {
    val extendedColorScheme = MaterialTheme.extendedColorScheme
    val debugColorMap = mapOf(
        "Marker" to Color.Green,
        "CenterLine" to Color.Gray,
        "RadialPower" to Color.Cyan,
        "SidePower" to Color.LightGray,
        "Shadow" to Color.White,
        "CenterForwardPower" to Color.Yellow,
        "CenterForward" to Color.Red,
        "CenterLight" to Color.Blue,
    )

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (sliderThumbOffset.x - (sliderThumbSize.height + 10.dp).toPx() * 0.5f).roundToInt(),
                            y = (sliderThumbOffset.y - (sliderThumbSize.height + 10.dp).toPx() * 0.5f).roundToInt()
                        )
                    }
                    .align(Alignment.TopStart)
                    .size(sliderThumbSize.height + 10.dp)
                    .background(
                        color = MaterialTheme.extendedColorScheme.effectLight1,
                        shape = CircleShape
                    )
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        ) {
            drawEffect(
                albumArtOffset = albumArtOffset,
                sliderThumbOffset = sliderThumbOffset,
                sliderThumbSize = sliderThumbSize,
                safeArea = SAFE_AREA_PX,
                extendedColorScheme = extendedColorScheme,
                debugMode = false,
                debugColorMap = debugColorMap
            )
        }

        if (debugMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawEffect(
                    albumArtOffset = albumArtOffset,
                    sliderThumbOffset = sliderThumbOffset,
                    sliderThumbSize = sliderThumbSize,
                    safeArea = SAFE_AREA_PX,
                    extendedColorScheme = extendedColorScheme,
                    debugMode = true,
                    debugColorMap = debugColorMap
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(with (LocalDensity.current) { (SAFE_AREA_PX * 1.5f).toDp() }),
                horizontalAlignment = Alignment.End,
            ) {
                for ((key, value) in debugColorMap) {
                    Text(
                        text = key,
                        color = value
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawEffect(
    albumArtOffset: Offset,
    sliderThumbOffset: Offset,
    sliderThumbSize: DpSize,
    safeArea: Float,
    extendedColorScheme: ExtendedColorScheme,
    debugMode: Boolean = false,
    debugColorMap: Map<String, Color> = mapOf()
) {
    val (width, height) = size.width to size.height
    val (albumX, albumY) = albumArtOffset
    val (thumbX, thumbY) = sliderThumbOffset

    val theta = if (albumX == albumY) 0f else atan2(
        y = thumbY - albumY,
        x = albumX - thumbX
    ).let { if (it < 0f) it + PI_F else it }
    val (thetaLeft, thetaRight) = theta + SCATTER_RADIAN to theta - SCATTER_RADIAN

    // Marker ↓

    run {
        if (debugMode) {
            drawCircle(
                color = debugColorMap.getOrDefault("Marker", Color.White),
                radius = 10f,
                center = albumArtOffset
            )
            drawCircle(
                color = debugColorMap.getOrDefault("Marker", Color.White),
                radius = 10f,
                center = sliderThumbOffset
            )
        }
    }

    // Marker ↑
    // CenterLine ↓

    run {
        if (debugMode) {
            val targetX = thumbX + (thumbY + safeArea) / tan(theta)
            val targetY = thumbY - abs(
                ((if (theta < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(
                    theta
                )
            )

            if (targetX >= -safeArea && targetX <= width + safeArea) {
                drawLine(
                    color = debugColorMap.getOrDefault("CenterLine", Color.White),
                    strokeWidth = DEBUG_WIDTH,
                    start = sliderThumbOffset,
                    end = Offset(x = targetX, y = -safeArea),
                )
            } else {
                drawLine(
                    color = debugColorMap.getOrDefault("CenterLine", Color.White),
                    strokeWidth = DEBUG_WIDTH,
                    start = sliderThumbOffset,
                    end = Offset(
                        x = if (targetX > width * 0.5f) width + safeArea else -safeArea,
                        y = targetY
                    ),
                )
            }
        }
    }

    // CenterLine ↑
    // RadialPower ↓

    run {
        val radius = 96.dp.toPx()

        if (!debugMode) {
            drawArc(
                color = extendedColorScheme.effectPowerLight,
                startAngle = -thetaLeft * 180f / PI_F,
                sweepAngle = SCATTER_RADIAN * 2f * 180f / PI_F,
                useCenter = true,
                topLeft = Offset(
                    x = thumbX - radius,
                    y = thumbY - radius
                ),
                size = Size(
                    width = radius * 2f,
                    height = radius * 2f,
                ),
            )
        } else {
            drawArc(
                color = debugColorMap.getOrDefault("RadialPower", Color.White),
                startAngle = -thetaLeft * 180f / PI_F,
                sweepAngle = SCATTER_RADIAN * 2f * 180f / PI_F,
                useCenter = true,
                topLeft = Offset(
                    x = thumbX - radius,
                    y = thumbY - radius
                ),
                size = Size(
                    width = radius * 2f,
                    height = radius * 2f,
                ),
                style = Stroke(width = DEBUG_WIDTH),
            )
        }
    }

    // RadialPower ↑
    // SidePower ↓

    run {
        val sidePowerRadian = PI_F * 0.02f
        // LeftSidePower
        run {
            val left = thetaLeft + sidePowerRadian
            val right = thetaLeft - sidePowerRadian

            val targetXLeft = thumbX + (thumbY + safeArea) / tan(left)
            val targetYLeft = thumbY - abs(((if (left < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(left))
            val targetXRight = thumbX + (thumbY + safeArea) / tan(right)
            val targetYRight = thumbY - abs(((if (right < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(right))

            val path = Path().apply {
                moveTo(x = thumbX, y = thumbY)

                if (targetXLeft >= -safeArea) {
                    lineTo(x = targetXLeft, y = -safeArea)
                } else {
                    lineTo(x = -safeArea, y = targetYLeft)
                    lineTo(x = -safeArea, y = -safeArea)
                }

                if (targetXRight >= -safeArea) {
                    lineTo(x = targetXRight, y = -safeArea)
                } else {
                    lineTo(x = -safeArea, y = targetYRight)
                }

                lineTo(x = thumbX, y = thumbY)
                close()
            }

            if (!debugMode) {
                drawPath(
                    path = path,
                    color = extendedColorScheme.effectPowerLight,
                )
            } else {
                drawPath(
                    path = path,
                    color = debugColorMap.getOrDefault("SidePower", Color.White),
                    style = Stroke(width = DEBUG_WIDTH),
                )
            }
        }
        // RightSidePower
        run {
            val left = thetaRight + sidePowerRadian
            val right = thetaRight - sidePowerRadian

            val targetXLeft = thumbX + (thumbY + safeArea) / tan(left)
            val targetYLeft = thumbY - abs(((if (left < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(left))
            val targetXRight = thumbX + (thumbY + safeArea) / tan(right)
            val targetYRight = thumbY - abs(((if (right < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(right))

            val path = Path().apply {
                moveTo(x = thumbX, y = thumbY)

                if (targetXRight <= width + safeArea) {
                    lineTo(x = targetXRight, y = -safeArea)
                } else {
                    lineTo(x = width + safeArea, y = targetYRight)
                    lineTo(x = width + safeArea, y = -safeArea)
                }

                if (targetXLeft <= width + safeArea) {
                    lineTo(x = targetXLeft, y = -safeArea)
                } else {
                    lineTo(x = width + safeArea, y = targetYLeft)
                }

                lineTo(x = thumbX, y = thumbY)
                close()
            }

            if (!debugMode) {
                drawPath(
                    path = path,
                    color = extendedColorScheme.effectPowerLight,
                )
            } else {
                drawPath(
                    path = path,
                    color = debugColorMap.getOrDefault("SidePower", Color.White),
                    style = Stroke(width = DEBUG_WIDTH),
                )
            }
        }
    }

    // SidePower ↑
    // Shadow ↓

    run {
        val targetXLeft = thumbX + (thumbY + safeArea) / tan(thetaLeft)
        val targetYLeft = thumbY - abs(((if (thetaLeft < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(thetaLeft))
        val targetXRight = thumbX + (thumbY + safeArea) / tan(thetaRight)
        val targetYRight = thumbY - abs(((if (thetaRight < PI_F * 0.5f) width - thumbX else thumbX) + safeArea) * tan(thetaRight))

        val path = Path().apply {
            moveTo(x = thumbX, y = thumbY)

            if (targetXLeft >= -safeArea) {
                lineTo(x = targetXLeft, y = -safeArea)
                lineTo(x = -safeArea, y = -safeArea)
            } else {
                lineTo(x = -safeArea, y = targetYLeft)
            }
            lineTo(x = -safeArea, y = height + safeArea)
            lineTo(x = width + safeArea, y = height + safeArea)

            if (targetXRight <= width + safeArea) {
                lineTo(x = width + safeArea, y = -safeArea)
                lineTo(x = targetXRight, y = -safeArea)
            } else {
                lineTo(x = width + safeArea, y = targetYRight)
            }

            lineTo(x = thumbX, y = thumbY)
            close()
        }

        if (!debugMode) {
            drawPath(
                path = path,
                color = extendedColorScheme.effectShadow1,
            )
        } else {
            drawPath(
                path = path,
                color = debugColorMap.getOrDefault("Shadow", Color.White),
                style = Stroke(width = DEBUG_WIDTH),
            )
        }
    }

    // Shadow ↑
    // CenterForwardPower ↓

    run {
        val width = 32.dp.toPx()
        val height = 128.dp.toPx()
        val diagonal = hypot(x = width * 0.5f, y = height * 0.5f)

        val x = thumbX + diagonal * cos(theta)
        val y = thumbY - diagonal * sin(theta)

        rotateRad(
            radians = PI_F / 2 - theta,
            pivot = Offset(x = x, y = y)
        ) {
            if (!debugMode) {
                drawOval(
                    color = extendedColorScheme.effectPowerLight,
                    topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                    size = Size(width = width, height = height)
                )
            } else {
                drawOval(
                    color = debugColorMap.getOrDefault("CenterForwardPower", Color.White),
                    topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                    size = Size(width = width, height = height),
                    style = Stroke(width = DEBUG_WIDTH),
                )
            }
        }
    }

    // CenterForwardPower ↑
    // CenterForward ↓

    run {
        val width = 24.dp.toPx()
        val height = 96.dp.toPx()
        val diagonal = hypot(x = width * 0.5f, y = height * 0.5f)

        val x = thumbX + diagonal * cos(theta)
        val y = thumbY - diagonal * sin(theta)

        rotateRad(
            radians = PI_F / 2 - theta,
            pivot = Offset(x = x, y = y)
        ) {
            if (!debugMode) {
                drawOval(
                    color = extendedColorScheme.effectLight2,
                    topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                    size = Size(width = width, height = height)
                )
            } else {
                drawOval(
                    color = debugColorMap.getOrDefault("CenterForward", Color.White),
                    topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                    size = Size(width = width, height = height),
                    style = Stroke(width = DEBUG_WIDTH),
                )
            }
        }
    }

    // CenterForward ↑
    // CenterLight ↓

    run {
        val radius = sliderThumbSize.height.toPx() * 0.75f

        if (!debugMode) {
            drawCircle(
                color = extendedColorScheme.effectLight1,
                radius = radius,
                center = sliderThumbOffset,
            )
        } else {
            drawCircle(
                color = debugColorMap.getOrDefault("CenterLight", Color.White),
                radius = radius,
                center = sliderThumbOffset,
                style = Stroke(width = DEBUG_WIDTH),
            )
        }
    }

    // CenterLight ↑
}
