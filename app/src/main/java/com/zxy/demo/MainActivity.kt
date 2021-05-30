package com.zxy.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            content()
        }
    }



    @Composable
    fun content(){
        var state by remember { mutableStateOf(0) }
        val tabTitle = listOf("Tab1","Tab2","Tab3")
        val contentList:MutableList<String> = mutableListOf()
        for(item in 0..30){
            contentList.add("i=$item 选项卡 $state")
        }
        Column(modifier = Modifier.padding(16.dp)){
            Image(painter = painterResource(id = R.drawable.ic_load_error), contentDescription = "",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Android",style = MaterialTheme.typography.h6,
            maxLines = 2,overflow = TextOverflow.Ellipsis)
            Text("Studio",style = MaterialTheme.typography.body2)
            Text("Jetpack Compose",style = MaterialTheme.typography.body2)
            val list:MutableList<String> = mutableListOf()
            for(item in 0..100){
                list.add("i=$item")
            }
            TabRow(selectedTabIndex = state) {
                tabTitle.forEachIndexed { index, title ->
                    Tab(text = { Text(title,color = Color.White)},
                    selected = state == index,
                    onClick = {
                        state = index
                    })
                }
            }
            lazyColumns(contentList = contentList)
        }
    }

    @Composable
    fun lazyColumns(contentList:List<String>){
        LazyColumn {
            itemsIndexed(contentList) { index,content ->
                Text(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), text = content)
            }
        }
    }

//    @Preview
//    @Composable
//    fun previewContent(){
//        content()
//    }
}
