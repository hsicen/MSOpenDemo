package me.hsicen.msopendemo.ui

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.hsicen.msopendemo.WeViewModel
import me.hsicen.msopendemo.ui.theme.WeComposeTheme
import kotlin.math.roundToInt

/**
 * @author: hsicen
 * @date: 2022/4/27 14:11
 * @email: codinghuang@163.com
 * description: Office 365 Api 测试
 *
 */
@Composable
fun OfficePage(act: Activity, modifier: Modifier = Modifier) {
  val viewModel: WeViewModel = viewModel()
  var refresh by remember { mutableStateOf(1) }

  Column(
    modifier
      .background(WeComposeTheme.colors.background)
      .fillMaxSize()
  ) {

    // 顶部标题栏
    WeTopBar("Office 365 Api 测试")

    LazyColumn {
      item {
        val mUser by remember(refresh) { mutableStateOf(viewModel.mUser) }
        Button(modifier = modifier.padding(16.dp), onClick = {
          viewModel.signIn(act)
          refresh += 1
        }) {
          Column {
            Text("登录")
            Spacer(modifier.size(16.dp))
            Text("User info:")
            Text("name: ${mUser?.displayName}")
            Text("mail: ${mUser?.mail}")
            Text("zone: ${mUser?.mailboxSettings?.timeZone}")
          }
        }
      }

      item {
        Button(modifier = modifier.padding(16.dp), onClick = {
          viewModel.signOut()
          refresh += 1
        }) {
          Column {
            Text("注销")
          }
        }
      }

      item {
        val mList by remember(refresh) { mutableStateOf(viewModel.mEventList) }
        Button(modifier = modifier.padding(16.dp), onClick = {
          viewModel.queryEvent()
          refresh += 1
        }) {
          Column {
            Text("查询日程")
            Text("日程信息条数: ${mList.size}条")
          }
        }
      }

      items(viewModel.mEventList) { event ->
        Log.d("hsc", "刷新日程列表 $refresh")
        Text("标题：${event.subject}")
        Text("内容：${event.body}")
        Text("开始时间：${event.start}")
        Text("结束时间：${event.end}")
        Text("参与人：${event.attendees}")

        Spacer(modifier.size(4.dp))
      }

      item {
        Button(modifier = modifier.padding(16.dp), onClick = {
          viewModel.createEvent()
          refresh += 1
        }) {
          Column {
            Text("创建日程")
          }
        }
      }
    }
  }
}

fun Modifier.offsetPercent(offsetPercentX: Float = 0f, offsetPercentY: Float = 0f) =
  this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) {
      val offsetX = (offsetPercentX * placeable.width).roundToInt()
      val offsetY = (offsetPercentY * placeable.height).roundToInt()
      placeable.placeRelative(offsetX, offsetY)
    }
  }