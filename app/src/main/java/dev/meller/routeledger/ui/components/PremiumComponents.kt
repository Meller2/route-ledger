package dev.meller.routeledger.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.ui.theme.LedgerBlue
import dev.meller.routeledger.ui.theme.LedgerClay
import dev.meller.routeledger.ui.theme.LedgerGold
import dev.meller.routeledger.ui.theme.LedgerGraphite
import dev.meller.routeledger.ui.theme.LedgerInk
import dev.meller.routeledger.ui.theme.LedgerLine
import dev.meller.routeledger.ui.theme.LedgerMutedInk
import dev.meller.routeledger.ui.theme.LedgerPaper
import dev.meller.routeledger.ui.theme.LedgerSage
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

private val rubFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))

fun Int.rub(): String = "${rubFormat.format(this)} ₽"
fun Double.km(): String = String.format(Locale("ru", "RU"), "%.1f км", this)

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    content: @Composable () -> Unit,
) {
    val background = if (dark) LedgerGraphite else LedgerPaper
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = background,
        tonalElevation = 0.dp,
        shadowElevation = if (dark) 0.dp else 1.dp,
        content = content,
    )
}

@Composable
fun HeroProfitCard(
    title: String,
    amount: Int,
    subtitle: String,
    profitPerHour: Int,
) {
    PremiumCard(dark = true, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(LedgerGraphite, Color(0xFF332C25), Color(0xFF1B1917)),
                    ),
                )
                .padding(24.dp),
        ) {
            Column {
                Text(title.uppercase(), color = LedgerGold, style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(12.dp))
                Text(amount.rub(), color = LedgerPaper, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(8.dp))
                Text(subtitle, color = Color(0xFFD6CCBE), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MiniPulse(value = 0.78f)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("${profitPerHour.rub()} / час", color = LedgerPaper, style = MaterialTheme.typography.titleMedium)
                        Text("Пульс смены выше среднего", color = Color(0xFFD6CCBE), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniPulse(value: Float) {
    Canvas(modifier = Modifier.size(58.dp)) {
        drawArc(
            color = Color.White.copy(alpha = 0.14f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 8f, cap = StrokeCap.Round),
        )
        drawArc(
            color = LedgerGold,
            startAngle = -90f,
            sweepAngle = 360f * value.coerceIn(0f, 1f),
            useCenter = false,
            style = Stroke(width = 8f, cap = StrokeCap.Round),
        )
    }
}

@Composable
fun MetricCard(label: String, value: String, hint: String, accent: Color = LedgerInk) {
    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Box(Modifier.size(9.dp).clip(CircleShape).background(accent))
            Spacer(Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(3.dp))
            Text(label, color = LedgerMutedInk, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(hint, color = LedgerMutedInk, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun InsightPill(text: String, accent: Color = LedgerSage) {
    Surface(shape = CircleShape, color = accent.copy(alpha = 0.18f)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = LedgerInk,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = LedgerMutedInk, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SoftLineChart(values: List<Int>, modifier: Modifier = Modifier, color: Color = LedgerBlue) {
    val maxValue = max(values.maxOrNull() ?: 1, 1)
    Canvas(modifier = modifier.fillMaxWidth().height(118.dp)) {
        val step = size.width / (values.size - 1).coerceAtLeast(1)
        val points = values.mapIndexed { index, value ->
            Offset(
                x = index * step,
                y = size.height - (value.toFloat() / maxValue.toFloat()) * size.height * 0.82f - size.height * 0.08f,
            )
        }
        points.zipWithNext().forEach { (a, b) ->
            drawLine(
                color = color.copy(alpha = 0.9f),
                start = a,
                end = b,
                strokeWidth = 8f,
                cap = StrokeCap.Round,
            )
        }
        points.forEach { point ->
            drawCircle(color = LedgerPaper, radius = 10f, center = point)
            drawCircle(color = color, radius = 6f, center = point)
        }
    }
}

@Composable
fun AreaQualityRing(score: Int, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(54.dp)) {
        drawArc(
            color = LedgerLine,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 8f, cap = StrokeCap.Round),
        )
        drawArc(
            color = if (score > 70) LedgerSage else LedgerClay,
            startAngle = -90f,
            sweepAngle = 360f * (score / 100f),
            useCenter = false,
            style = Stroke(width = 8f, cap = StrokeCap.Round),
        )
    }
}

@Composable
fun EmptyMapPreview() {
    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(22.dp)) {
            Text("Карта подключится позже", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "В MVP районы считаются без карт. В коде уже есть интерфейс MapProvider, чтобы потом добавить Google Maps или Яндекс Карты без переписывания экранов.",
                color = LedgerMutedInk,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InsightPill("геослой готов", LedgerGold)
                InsightPill("без API key", LedgerSage)
            }
        }
    }
}
