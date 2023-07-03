package com.example.pokopy.myCollection


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.pokopy.R
import com.example.pokopy.database.UserPokemon
import com.example.pokopy.ui.theme.RobotoCondensed


@Composable
fun MyCollection(
    navController: NavController,
    viewModel: MyCollectionViewModel
) {


    viewModel.getUserPokemon()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                contentAlignment = Alignment.TopStart,
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(16.dp, 16.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }
            Image(
                painter = painterResource(id = R.drawable.pokopy_logo),
                contentDescription = "Pokopy",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                viewModel.searchPokemonList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CollectionList(viewModel = viewModel)

        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if(isHintDisplayed){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun CollectionList(
    viewModel: MyCollectionViewModel
){
    val pokemonList by remember { viewModel.userPokemons }

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        items(pokemonList.size){
            CollectionEntry(
                entry = pokemonList[it],
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}


@Composable
fun CollectionEntry(
    entry: UserPokemon,
    modifier: Modifier = Modifier,
    viewModel: MyCollectionViewModel
){
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(3f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
    ){
        Row{
            Column(modifier = Modifier.weight(0.75f)){
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(entry.pokemonImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = entry.pokemonName,
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(0.5f)
                        )
                    },
                    success = { it ->
                        viewModel.calcDominantColor(it.result.drawable) {
                            dominantColor = it
                        }
                        SubcomposeAsyncImageContent()
                    },
                    modifier = Modifier
                        .size(110.dp)
                        .align(CenterHorizontally)
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .align(CenterVertically)) {
                Text(
                    text = "#${entry.pokemonID}",
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = entry.pokemonName,
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Caught: ${entry.count}",
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}