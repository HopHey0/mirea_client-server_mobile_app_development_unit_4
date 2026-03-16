package com.example.pract_3_4.feature.socialNetwork.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pract_3_4.R
import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.LoadStatus
import com.example.pract_3_4.feature.socialNetwork.presentation.viewModel.PostCard
import com.example.pract_3_4.ui.theme.Pract_3_4Theme

@Composable
fun Post(
    postCard: PostCard
){
    Column(
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(modifier = Modifier.clip(CircleShape), contentDescription = null, model = postCard.profPicUrl, placeholder = painterResource(R.drawable.magnifier), error = painterResource(R.drawable.person_off_24px))
            VerticalDivider(modifier = Modifier.padding(horizontal = 5.dp))
            Column() {
                Text(text = postCard.post.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = postCard.post.body, fontSize = 12.sp, fontWeight = FontWeight.Thin)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        when (postCard.isCommentsLoading) {
            LoadStatus.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally), strokeWidth = 2.dp)
            }
            LoadStatus.Ready -> {
                Column(
                ) {
                    postCard.comments.forEach { comment ->
                        Text(text = comment.name, fontSize = 12.sp)
                        Text(text = "   ${comment.body}", fontSize = 12.sp, fontWeight = FontWeight.Thin)
                    }
                }
            }
            LoadStatus.Error -> {
                Text(modifier = Modifier.fillMaxWidth(), text = "Комментарии не найдены", textAlign = TextAlign.Center)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun PostWithCommentsPreview(){
    Pract_3_4Theme() {
        Post(PostCard(LoadStatus.Ready, PostItem(1, 1, "Title", "Short body of a topic", ""), LoadStatus.Ready, listOf(
            CommentItem(1, 1, "Jane", "Cool!"), CommentItem(2, 1, "John", "Awesome!"), CommentItem(3, 1, "Lora", "Kinda lame"))))
    }
}

@Composable
@Preview(showBackground = true)
fun PostWithoutCommentsPreview(){
    Pract_3_4Theme() {
        Post(PostCard(LoadStatus.Loading, PostItem(1, 1, "Title", "Short body of a topic", ""), LoadStatus.Error))
    }
}