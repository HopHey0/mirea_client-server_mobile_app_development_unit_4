package com.example.pract_3_4.feature.repoSearcher.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pract_3_4.feature.repoSearcher.presentation.ui.component.RepoRow
import com.example.pract_3_4.feature.repoSearcher.presentation.viewModel.RepoListViewModel

@Composable
fun RepoList(
    repoListViewModel: RepoListViewModel,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
){
    val repos = repoListViewModel.searchRes.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    val isLoading = repoListViewModel.isLoading.collectAsState()
    Column {
        TopBarRepoList(onClickButton)
        TextField(
            modifier = Modifier.fillMaxWidth()
                .padding(all = 5.dp)
                .background(Color.LightGray),
            value = text,
            onValueChange = {
                text = it
                repoListViewModel.searchRepos(text)
                            },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") }
        )
        if (!isLoading.value) {
            LazyColumn(
                modifier = modifier
                    .padding(horizontal = 15.dp),
            ) {
                items(repos.value) { item ->
                    RepoRow(item)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                }
            }
        } else {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
//                Icon(
//                    modifier = modifier.fillMaxSize(),
//                    imageVector = Icons.Filled.Search,
//                    contentDescription = "PlaceHolder")
                CircularProgressIndicator(modifier = Modifier.fillMaxSize().padding(all = 10.dp), strokeWidth = 5.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarRepoList(
    onClickButton: () -> Unit = { }
){
    TopAppBar(
        title = {
            Text(
                text = "Pseudo Repo Searcher"
            )
        },
        actions = {
            IconButton(
                modifier = Modifier,
                onClick = { onClickButton() }) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = "Go to Social net"
                )
            }
        }
    )
}