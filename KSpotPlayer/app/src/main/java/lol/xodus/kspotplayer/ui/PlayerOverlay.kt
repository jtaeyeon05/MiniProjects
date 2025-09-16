package lol.xodus.kspotplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.tan


const val PI_F = PI.toFloat()
const val SAFE_AREA_PX = 40f  // 블러처리 대비 추가 그림 영역
const val SAFE_AREA_PX_D = -SAFE_AREA_PX  // 블러처리 대비 추가 그림 영역 (디버그용 반대)
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
        "SidePower" to Color.LightGray,
        "RadiusPower" to Color.Cyan,
        "Shadow" to Color.White,
        "CenterForward" to Color.Red,
        "CenterForwardPower" to Color.Yellow,
        "CenterLight" to Color.Blue,
    )

    Box {
        Canvas(
            modifier = Modifier.fillMaxSize()
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
                        color = debugColorMap["Marker"]!!,
                        radius = 10f,
                        center = albumArtOffset
                    )
                    drawCircle(
                        color = debugColorMap["Marker"]!!,
                        radius = 10f,
                        center = sliderThumbOffset
                    )
                }
            }

            // Marker ↑
            // CenterLine ↓

            run {
                if (debugMode) {
                    val targetX = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(theta)
                    val targetY = thumbY - abs(
                        ((if (theta < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(
                            theta
                        )
                    )

                    if (targetX >= -SAFE_AREA_PX_D && targetX <= width + SAFE_AREA_PX_D) {
                        drawLine(
                            color = debugColorMap["CenterLine"]!!,
                            strokeWidth = DEBUG_WIDTH,
                            start = sliderThumbOffset,
                            end = Offset(x = targetX, y = -SAFE_AREA_PX_D),
                        )
                    } else {
                        drawLine(
                            color = debugColorMap["CenterLine"]!!,
                            strokeWidth = DEBUG_WIDTH,
                            start = sliderThumbOffset,
                            end = Offset(
                                x = if (targetX > width * 0.5f) width + SAFE_AREA_PX_D else -SAFE_AREA_PX_D,
                                y = targetY
                            ),
                        )
                    }
                }
            }

            // CenterLine ↑
            // SidePower ↓

            run {
                val sidePowerRadian = PI_F * 0.03f
                // LeftSidePower
                run {
                    val left = thetaLeft + sidePowerRadian
                    val right = thetaLeft - sidePowerRadian

                    val targetXLeft = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(left)
                    val targetYLeft = thumbY - abs(((if (left < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(left))
                    val targetXRight = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(right)
                    val targetYRight = thumbY - abs(((if (right < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(right))

                    val path = Path().apply {
                        moveTo(x = thumbX, y = thumbY)

                        if (targetXLeft >= -SAFE_AREA_PX_D) {
                            lineTo(x = targetXLeft, y = -SAFE_AREA_PX_D)
                        } else {
                            lineTo(x = -SAFE_AREA_PX_D, y = targetYLeft)
                            lineTo(x = -SAFE_AREA_PX_D, y = -SAFE_AREA_PX_D)
                        }

                        if (targetXRight >= -SAFE_AREA_PX_D) {
                            lineTo(x = targetXRight, y = -SAFE_AREA_PX_D)
                        } else {
                            lineTo(x = -SAFE_AREA_PX_D, y = targetYRight)
                        }

                        lineTo(x = thumbX, y = thumbY)
                        close()
                    }

                    drawPath(
                        path = path,
                        color = extendedColorScheme.effectPowerLight,
                    )
                    if (debugMode) {
                        drawPath(
                            path = path,
                            color = debugColorMap["SidePower"]!!,
                            style = Stroke(width = DEBUG_WIDTH),
                        )
                    }
                }
                // RightSidePower
                run {
                    val left = thetaRight + sidePowerRadian
                    val right = thetaRight - sidePowerRadian

                    val targetXLeft = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(left)
                    val targetYLeft = thumbY - abs(((if (left < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(left))
                    val targetXRight = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(right)
                    val targetYRight = thumbY - abs(((if (right < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(right))

                    val path = Path().apply {
                        moveTo(x = thumbX, y = thumbY)

                        if (targetXRight <= width + SAFE_AREA_PX_D) {
                            lineTo(x = targetXRight, y = -SAFE_AREA_PX_D)
                        } else {
                            lineTo(x = width + SAFE_AREA_PX_D, y = targetYRight)
                            lineTo(x = width + SAFE_AREA_PX_D, y = -SAFE_AREA_PX_D)
                        }

                        if (targetXLeft <= width + SAFE_AREA_PX_D) {
                            lineTo(x = targetXLeft, y = -SAFE_AREA_PX_D)
                        } else {
                            lineTo(x = width + SAFE_AREA_PX_D, y = targetYLeft)
                        }

                        lineTo(x = thumbX, y = thumbY)
                        close()
                    }

                    drawPath(
                        path = path,
                        color = extendedColorScheme.effectPowerLight,
                    )
                    if (debugMode) {
                        drawPath(
                            path = path,
                            color = debugColorMap["SidePower"]!!,
                            style = Stroke(width = DEBUG_WIDTH),
                        )
                    }
                }
            }

            // SidePower ↑
            // RadiusPower ↓

            run {
                val radius = 96.dp.toPx()

                drawArc(
                    color = extendedColorScheme.effectLight,
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
                if (debugMode) {
                    drawArc(
                        color = debugColorMap["RadiusPower"]!!,
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

            // RadiusPower ↑
            // Shadow ↓

            run {
                val targetXLeft = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(thetaLeft)
                val targetYLeft = thumbY - abs(((if (thetaLeft < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(thetaLeft))
                val targetXRight = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(thetaRight)
                val targetYRight = thumbY - abs(((if (thetaRight < PI_F * 0.5f) width - thumbX else thumbX) + SAFE_AREA_PX_D) * tan(thetaRight))

                val path = Path().apply {
                    moveTo(x = thumbX, y = thumbY)

                    if (targetXLeft >= -SAFE_AREA_PX_D) {
                        lineTo(x = targetXLeft, y = -SAFE_AREA_PX_D)
                        lineTo(x = -SAFE_AREA_PX_D, y = -SAFE_AREA_PX_D)
                    } else {
                        lineTo(x = -SAFE_AREA_PX_D, y = targetYLeft)
                    }
                    lineTo(x = -SAFE_AREA_PX_D, y = height + SAFE_AREA_PX_D)
                    lineTo(x = width + SAFE_AREA_PX_D, y = height + SAFE_AREA_PX_D)

                    if (targetXRight <= width + SAFE_AREA_PX_D) {
                        lineTo(x = width + SAFE_AREA_PX_D, y = -SAFE_AREA_PX_D)
                        lineTo(x = targetXRight, y = -SAFE_AREA_PX_D)
                    } else {
                        lineTo(x = width + SAFE_AREA_PX_D, y = targetYRight)
                    }

                    lineTo(x = thumbX, y = thumbY)
                    close()
                }

                drawPath(
                    path = path,
                    color = extendedColorScheme.effectShadow,
                )
                if (debugMode) {
                    drawPath(
                        path = path,
                        color = debugColorMap["Shadow"]!!,
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
                    drawOval(
                        color = extendedColorScheme.effectPowerLight,
                        topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                        size = Size(width = width, height = height)
                    )
                    if (debugMode) {
                        drawOval(
                            color = debugColorMap["CenterForwardPower"]!!,
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
                    drawOval(
                        color = extendedColorScheme.effectLight,
                        topLeft = Offset(x = x - width * 0.5f, y = y - height * 0.5f),
                        size = Size(width = width, height = height)
                    )
                    if (debugMode) {
                        drawOval(
                            color = debugColorMap["CenterForward"]!!,
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
                val radius = sliderThumbSize.height.toPx() * 0.5f

                drawCircle(
                    color = extendedColorScheme.effectLight,
                    radius = radius,
                    center = sliderThumbOffset,
                )
                if (debugMode) {
                    drawCircle(
                        color = debugColorMap["CenterLight"]!!,
                        radius = radius,
                        center = sliderThumbOffset,
                        style = Stroke(width = DEBUG_WIDTH),
                    )
                }
            }

            // CenterLight ↑
        }

        if (debugMode) {
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
