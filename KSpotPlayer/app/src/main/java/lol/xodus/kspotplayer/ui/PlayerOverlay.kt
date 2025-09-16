package lol.xodus.kspotplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.tan


const val PI_F = PI.toFloat()
const val SAFE_AREA_PX = 20f  // 블러처리 대비 추가 그림 영역
const val SAFE_AREA_PX_D = -SAFE_AREA_PX  // 블러처리 대비 추가 그림 영역 (디버그용 반대)
const val SCATTER_RADIAN = PI_F * 0.15f // 확산 영역


@Composable
fun PlayerOverlay(
    albumArtOffset: Offset,
    sliderThumbOffset: Offset,
    sliderThumbSize: DpSize,
    debugMode: Boolean = false,
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val (width, height) = size.width to size.height
        val (albumX, albumY) = albumArtOffset
        val (thumbX, thumbY) = sliderThumbOffset

        val theta = atan2(y = thumbY - albumY, x = albumX - thumbX).let { if (it < 0f) it + PI_F else it }
        val (thetaLeft, thetaRight) = theta + SCATTER_RADIAN to theta - SCATTER_RADIAN

        // Marker ↓

        if (debugMode) {
            drawCircle(
                color = Color.Red,
                radius = 10f,
                center = albumArtOffset
            )
            drawCircle(
                color = Color.Red,
                radius = 10f,
                center = sliderThumbOffset
            )
        }

        // Marker ↑
        // CenterLine ↓

        if (debugMode) {
            val targetX = thumbX + (thumbY + SAFE_AREA_PX_D) / tan(theta)
            val targetY = if (theta < PI_F * 0.5f) thumbY - (width - thumbX + SAFE_AREA_PX_D) * tan(theta) else thumbY + (thumbX + SAFE_AREA_PX_D) * tan(theta)

            if (targetX >= -SAFE_AREA_PX_D && targetX <= width + SAFE_AREA_PX_D) {
                drawLine(
                    color = Color.White,
                    strokeWidth = 2f,
                    start = sliderThumbOffset,
                    end = Offset(x = targetX, y = -SAFE_AREA_PX_D),
                )
            } else {
                if (targetX > width * 0.5f) {
                    drawLine(
                        color = Color.White,
                        strokeWidth = 2f,
                        start = sliderThumbOffset,
                        end = Offset(x = width + SAFE_AREA_PX_D, y = targetY),
                    )
                } else {
                    drawLine(
                        color = Color.White,
                        strokeWidth = 2f,
                        start = sliderThumbOffset,
                        end = Offset(x = -SAFE_AREA_PX_D, y = targetY),
                    )
                }
            }
        }

        // CenterLine ↑
        // SidePower ↓
        // SidePower ↑
    }
}
