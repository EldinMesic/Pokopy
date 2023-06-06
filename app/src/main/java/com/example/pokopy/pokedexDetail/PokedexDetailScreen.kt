@file:Suppress("DEPRECATION")

package com.example.pokopy.pokedexDetail



import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pokopy.PokemonAPI.model.entries.EvolutionChainEntry
import com.example.pokopy.PokemonAPI.model.response.Pokemon
import com.example.pokopy.PokemonAPI.model.response.Type
import com.example.pokopy.R
import com.example.pokopy.utilities.Resource
import com.example.pokopy.utilities.parseStatToAbbr
import com.example.pokopy.utilities.parseStatToColor
import com.example.pokopy.utilities.parseStatToMax
import com.example.pokopy.utilities.parseTypeToColor
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PokedexDetailScreen(
    dominantColor : Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokedexDetailViewModel
){
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()){
        value = viewModel.getPokemonInfo(pokemonName)
    }.value
    val evolutionInfo = produceState<Resource<EvolutionChainEntry>>(initialValue = Resource.Loading()){
        value = viewModel.getEvolutionChain(pokemonName)
    }.value
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)
    ){
        PokedexDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )

        PokedexDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            evolutionInfo = evolutionInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )

        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
            .fillMaxSize()
        ){
            if(pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.front_default)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding)
                    )
                }
            }
        }

    }
}



@Composable
fun PokedexDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
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

}


@Composable
fun PokedexDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    evolutionInfo: Resource<EvolutionChainEntry>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
){
    when(pokemonInfo){
        is Resource.Success -> {
            pokemonInfo.data?.let {
                PokedexDetailSection(
                    pokemonInfo = it,
                    evolutionInfo = evolutionInfo,
                    loadingModifier = loadingModifier,
                    modifier = modifier
                        .offset(y = (-20).dp)
                )
            }
        }
        is Resource.Error -> {
            pokemonInfo.message?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = modifier
                )
            }
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }
    }
}


@Composable
fun PokedexDetailSection(
    pokemonInfo: Pokemon,
    evolutionInfo: Resource<EvolutionChainEntry>,
    loadingModifier: Modifier = Modifier,
    modifier : Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        @Suppress("DEPRECATION")
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokedexTypeSection(types = pokemonInfo.types)
        PokedexDetailDataSection(
            pokemonWeight = pokemonInfo.weight,
            pokemonHeight = pokemonInfo.height
        )
        PokedexBaseStats(pokemonInfo = pokemonInfo)
        when(evolutionInfo){
            is Resource.Success -> {
                evolutionInfo.data?.let { PokedexEvolutionChain(evolutionInfo = it) }
            }
            is Resource.Error -> {
                evolutionInfo.message?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = modifier
                    )
                }
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = loadingModifier
                )
            }
        }

    }
}




@Composable
fun PokedexTypeSection(types: List<Type>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(36.dp)
            ){
                @Suppress("DEPRECATION")
                Text(
                    text = type.type.name.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Composable
fun PokedexDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
){
    val pokemonWeightInKg = remember {
        (pokemonWeight * 100f).roundToInt() / 1000f
    }
    val pokemonHeightInMeters = remember {
        (pokemonHeight * 100f).roundToInt() / 1000f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        PokedexDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray)
        )
        PokedexDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun PokedexDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun PokedexStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
){
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val currPercent = animateFloatAsState(
        targetValue = if(animationPlayed){
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 =  true){
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.15f + currPercent.value * 0.85f)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ){
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (currPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }

}


@Composable
fun PokedexBaseStats(
    pokemonInfo: Pokemon,
    animDelayPerItem: Int = 100
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        for(i in pokemonInfo.stats.indices){
            val stat = pokemonInfo.stats[i]
            PokedexStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.base_stat,
                statMaxValue = parseStatToMax(stat),
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}


@Composable
fun PokedexEvolutionChain(
    evolutionInfo: EvolutionChainEntry
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Evolutions:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokedexEvolutions(evolutionChain = evolutionInfo)
    }
}

@Composable
fun PokedexEvolutions(
    evolutionChain: EvolutionChainEntry
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        PokedexEvolution(evolutionChain)
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (secondEvolution in evolutionChain.chain){
                PokedexEvolutionCondition(evolutionInfo = secondEvolution)
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (secondEvolution in evolutionChain.chain){
                PokedexEvolution(evolutionInfo = secondEvolution)
            }
        }
        if(evolutionChain.chain.isNotEmpty()){
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                for (thirdEvolution in evolutionChain.chain[0].chain){
                    PokedexEvolutionCondition(evolutionInfo = thirdEvolution)
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (thirdEvolution in evolutionChain.chain[0].chain){
                    PokedexEvolution(evolutionInfo = thirdEvolution)
                }
            }
        }


    }
    Spacer(modifier = Modifier.height(80.dp))

}


@Composable
fun PokedexEvolution(
    evolutionInfo: EvolutionChainEntry
){
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(evolutionInfo.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = evolutionInfo.pokemonName,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(70.dp)
        )
        Text(
            text = evolutionInfo.pokemonName.capitalize(Locale.ROOT),
            fontSize = 8.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.offset(y = (-5).dp)
        )
    }
}

@Composable
fun PokedexEvolutionCondition(
    evolutionInfo: EvolutionChainEntry
){
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = evolutionInfo.trigger,
            fontSize = 8.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.offset(y = 20.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_right_alt_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(70.dp)
        )
    }
}