package com.example.pract_3_4.feature.repoSearcher.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.ui.theme.Pract_3_4Theme

@Composable
fun RepoRow(
    item: RepoItem
){
    Row (
        modifier = Modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .background(Color.LightGray)
            .padding(horizontal = 5.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.weight(3f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.fullName,
                fontSize = 28.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Stars: " + item.starsCount.toString(),
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = item.language,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RepoRowPreview(){
    Pract_3_4Theme() {
        RepoRow(RepoItem(1, "Sample", "Sample description, nothing special", 10000, "Kotlin"))
    }
}