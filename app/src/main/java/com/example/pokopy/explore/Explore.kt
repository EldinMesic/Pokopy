package com.example.pokopy.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.pokopy.PokemonAPI.model.entries.PokedexListEntry
import com.example.pokopy.R
import com.example.pokopy.ui.theme.RobotoCondensed


@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel
){

    if(!viewModel.hasLoaded.value) {
        viewModel.startTimer()
    }

    val timer by remember{ viewModel.timer }

    val isLoading by remember{ viewModel.isLoading }
    val canExplore by remember{ viewModel.canExplore }
    val isExploring by remember{ viewModel.isExploring }

    val caughtPokemon by remember { viewModel.caughtPokemon }



    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ExploreTopSection(navController)

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 20.dp)
            ) {
                if(isLoading){
                    Surface(
                        color = Color.Transparent,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(2f)
                    ){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier  = Modifier
                                .padding(
                                    start = 30.dp,
                                    end = 30.dp,
                                    top = 110.dp,
                                    bottom = 110.dp
                                )
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Gray.copy(0.8f))
                                .size(40.dp, 40.dp)

                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 10.dp,
                                modifier = Modifier
                                    .size(80.dp)
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Loading. Please Wait...",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                            )

                        }
                    }
                }

                if(canExplore){
                    Button(
                        onClick = { viewModel.catchPokemon() },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "Catch Pokemon",
                            fontSize = 32.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))



                }else if(isExploring){
                    Text(
                        text = "You found ${caughtPokemon.pokemonName}!\nClick to catch it!",
                        fontFamily = RobotoCondensed,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CaughtPokemonEntry(entry = caughtPokemon,viewModel = viewModel )

                }

                else{
                    Text(
                        textAlign = TextAlign.Center,
                        text = "You can Explore again in:\n${viewModel.parseSecondsToTime(timer)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }


            }

        }
    }

}


@Composable
fun CaughtPokemonEntry(
    entry: PokedexListEntry,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel
){
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                viewModel.isExploring.value = false
            }
    ){
        Column{
            Text(
                text = "#${entry.number}",
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
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
                success = {
                    viewModel.calcDominantColor(it.result.drawable){
                        dominantColor = it
                    }
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun ExploreTopSection(
    navController: NavController
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_wave_line),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.background),
            modifier = Modifier.fillMaxSize()
        )
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ){
            Image(
                painter = painterResource(id = R.drawable.pokopy_logo),
                contentDescription = "Pokopy",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}