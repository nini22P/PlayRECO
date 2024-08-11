package com.github.nini22p.playreco.ui.home

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.nini22p.playreco.viewmodel.UsageViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HomeScreen(viewModel: UsageViewModel = viewModel()) {

    val context = LocalContext.current
    val usage = viewModel.usage?.observeAsState(initial = emptyList())
    val appInfo = viewModel.appInfo

    fun formatTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000 % 60
        val minutes = milliseconds / (1000 * 60) % 60
        val hours = milliseconds / (1000 * 60 * 60)
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (usage != null) {
            items(usage.value) { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onClick = {
                        Toast.makeText(
                            context,
                            "" +
                                    "${item.title} " +
                                    "${item.packageName} " +
                                    "${item.date} " +
                                    "${formatTime(item.totalTime)} " +
                                    "${item.interval} " +
                                    "${item.deviceId} " +
                                    " ",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = (appInfo.find { app -> app.packageName == item.packageName })?.title
                                ?: item.title ?: item.packageName,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                Text(item.date)
                            }
                            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                Text(formatTime(item.totalTime))
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun RefreshButton(viewModel: UsageViewModel) {
    // 记录按钮是否处于旋转状态
    var isRotating by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    // 根据 isRotating 的状态来设置旋转角度
    val rotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 500), // 500ms 完成旋转
        label = ""
    )

    FloatingActionButton(
        onClick = {
            // 触发旋转动画
            if (!isRotating) { // 防止在旋转过程中再次触发
                isRotating = true
                rotationAngle += 360f
                viewModel.fetchUsage()
            }
        },
    ) {
        Icon(
            Icons.Filled.Refresh,
            contentDescription = "Refresh",
            modifier = Modifier.graphicsLayer(rotationZ = rotation)
        )
    }

    // 使用 LaunchedEffect 确保动画结束后停止旋转
    LaunchedEffect(isRotating) {
        if (isRotating) {
            delay(500) // 延迟与动画时间一致
            isRotating = false
        }
    }
}
