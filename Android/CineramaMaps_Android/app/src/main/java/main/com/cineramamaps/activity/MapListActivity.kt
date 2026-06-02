package main.com.cineramamaps.activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.gson.Gson
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import main.com.cineramamaps.R
import main.com.cineramamaps.activity.ui.theme.CineramaMaps_AndroidTheme
import main.com.cineramamaps.activity.ui.theme.cairoFamily
import main.com.cineramamaps.model.PlaceDetail
import main.com.cineramamaps.model.PlacesImage
import main.com.cineramamaps.model.Tag

object ListViewContentWrapper {
    var _places: MutableState<List<PlaceDetail>?> = mutableStateOf(null)
    val places: List<PlaceDetail>? get() = _places.value
    private var _tags: MutableState<List<Tag>?> = mutableStateOf(null)
    val tags: List<Tag>? get() = _tags.value
    private var _images: MutableState<List<PlacesImage>?> = mutableStateOf(null)
    val images: List<PlacesImage>? get() = _images.value

    private var onFavoriteClick: ((String, (Boolean) -> Unit) -> Unit)? = null

    @JvmStatic
    fun setOnFavoriteClick(callback: OnFavoriteClick?) {
        onFavoriteClick = if (callback != null) {
            { placeId, toggle ->
                callback.onClick(placeId, toggle)
            }
        } else {
            { _, cb -> cb(false) }
        }
    }
    @JvmStatic
    fun setRestaurantContent(composeView: ComposeView) {
        composeView.setContent {
            CineramaMaps_AndroidTheme {
                ListViewContent(place = _places.value, tags = _tags.value, image = _images.value, onFavoriteClick = onFavoriteClick)
            }
        }
    }

    @JvmStatic
    fun updatePlaces(context: Context, newPlaces: List<PlaceDetail>) {
        _places.value = newPlaces
    }



    @JvmStatic
    fun updateTags(newTags: List<Tag>) {
        _tags.value = newTags
    }
    @JvmStatic
    fun updateImage(newImage: MutableList<PlacesImage>) {
        _images.value = newImage
    }
}

//class MapListActivity : ComponentActivity() {
//    lateinit var session: SessionManager
//    var successData by mutableStateOf<PlacesBean?>(null)
//    @OptIn(ExperimentalMaterial3Api::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            CineramaMaps_AndroidTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column(
//                        modifier = Modifier.padding(innerPadding)
//                    ) {
//                        ListViewContent(
//                            place = ListViewContentWrapper.places,
//                            tags = ListViewContentWrapper.tags,
//                            image = ListViewContentWrapper.images,
//                            isFav = currentUserFavourite ?: false,
//                        )
//                    }
//                }
//            }
//        }
//    }
//    private fun favorite(placeId: String, toggleHeartIcon: (Boolean) -> Unit) {
//        ApiCall.get().Create().addTofavorite(
//            session.getUserID() ?: "",
//            placeId
//        ).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    try {
//                        val responseData = response.body()!!.string()
//                        val `object` = JSONObject(responseData)
//
//                        if (`object`.getString("status") == "1") {
//                            val isFavorite = !`object`.getString("result")
//                                .equals("Removed From Favorites", ignoreCase = true)
//                            toggleHeartIcon(isFavorite)
//
//                            // Update the corresponding place in your list
//                            successData?.result?.placeDetails?.find { it.id == placeId }
//                                ?.isCurrentUserFavorite = isFavorite
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(
//                    this@MapListActivity,
//                    "Error: " + t.message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//    }
//
//    }
@Composable
fun ListViewContent(
    place: List<PlaceDetail>?,
    tags: List<Tag>?,
    image: List<PlacesImage>?,
    onFavoriteClick: ((String, (Boolean) -> Unit) -> Unit)?
) {
    val configuration = LocalConfiguration.current
    val layoutDirection = if (configuration.layoutDirection == android.util.LayoutDirection.RTL) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    var selectedIndex by remember { mutableStateOf(-1) } // -1 = All, -2 = Favorites
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Popular") }

    // ✅ Updated filtering logic (supports Favorite)
    val filteredPlaces = remember(place, tags, selectedIndex) {
        when {
            selectedIndex == -1 || place == null -> place
            selectedIndex == -2 -> place.filter { it.isCurrentUserFavorite == true }.toMutableStateList()
            tags != null && selectedIndex in tags.indices -> {
                val selectedTag = tags[selectedIndex]
                place.filter { placeDetail ->
                    placeDetail.tagDetails?.any { it.id == selectedTag.id } == true
                }
            }
            else -> place
        }
    }?.let { it as? SnapshotStateList<PlaceDetail> ?: it.toMutableStateList() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {
        val horizontalScrollState = rememberScrollState()

        // 🔹 Tags + Favorite filter row
        val currentLocale = LocalConfiguration.current.locales[0]
        Row(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(top = 8.dp)
        ) {
            // 🟠 "All" Button
            val isAllSelected = selectedIndex == -1
            OutlinedButton(
                onClick = { selectedIndex = -1 },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, if (isAllSelected) Color(0xFFFF7D36) else Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFF7D36),
                    contentColor = if (isAllSelected) Color.White else Color.Black
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(38.dp)
            ) {
                Text(
                    text = if (currentLocale.language == "ar") "الكل" else "All",
                    fontSize = 14.sp,
                    fontFamily = cairoFamily
                )
            }

            val isFavoriteSelected = selectedIndex == -2 && filteredPlaces!!.any { it.isCurrentUserFavorite }
            OutlinedButton(
                onClick = { selectedIndex = -2 },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, if (isFavoriteSelected) Color(0xFFFF7D36) else Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFF7D36),
                    contentColor = if (isFavoriteSelected) Color.White else Color.Black
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(38.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = if (isFavoriteSelected)
                            R.drawable.ic_fav_heart
                        else
                            R.drawable.ic_heart
                    ),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 4.dp, top = 4.dp)
                )
                Text(
                    text = if (currentLocale.language == "ar") "المفضلة" else "Favorite",
                    fontSize = 14.sp,
                    fontFamily = cairoFamily
                )
            }

            // 🏷️ Tag Buttons
            tags?.forEachIndexed { index, tag ->
                val isSelected = selectedIndex == index
                OutlinedButton(
                    onClick = { selectedIndex = index },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(
                        1.dp,
                        Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36")),
                        contentColor = if (isSelected) Color.White else Color.Black
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(38.dp)
                ) {
                    Text(
                        text = if (currentLocale.language == "ar") tag.tagNameAr else tag.tagName,
                        fontSize = 14.sp,
                        fontFamily = cairoFamily,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        // 🔹 Sort Dropdown Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.Nearby_restaurant),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = cairoFamily,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            val options = listOf(
                stringResource(id = R.string.Popular),
                stringResource(id = R.string.Name),
                stringResource(id = R.string.Oldest),
                stringResource(id = R.string.Distance)
            )

            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { expanded = true }
                ) {
                    Text(
                        text = "Sort by : ",
                        fontSize = 14.sp,
                        fontFamily = cairoFamily,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = selectedOption,
                        fontSize = 14.sp,
                        fontFamily = cairoFamily,
                        color = Color(0xFFFF5722),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrowdown),
                        contentDescription = "Dropdown arrow",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 16.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(162.dp)
                        .background(Color.White)
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = option
                                expanded = false
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        fontFamily = cairoFamily,
                                        modifier = Modifier.weight(1f)
                                    )
                                    val iconRes = if (option == selectedOption)
                                        R.drawable.ic_selectedring
                                    else
                                        R.drawable.ic_unselectedring
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )
                        if (index < options.lastIndex) Divider()
                    }
                }
            }
        }

        // 🔹 Places List
        LazyColumn {
            if (filteredPlaces != null) {
                if (filteredPlaces.isEmpty()) {
                    item {
                        Text(
                            text = "No places match this filter",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(filteredPlaces.size) { index ->
                        val context = LocalContext.current
                        RestaurantCard(
                            place = filteredPlaces[index],
                            images = image,
                            onFavoriteClick = { placeId, toggleHeartIcon ->
                                if (onFavoriteClick != null) {
                                    onFavoriteClick(placeId) { success ->
                                        val currentIndex = filteredPlaces.indexOfFirst { it.id == placeId }
                                        if (currentIndex != -1) {
                                            val place = filteredPlaces[currentIndex]

                                            // ✅ Toggle locally (UI immediate feedback)
                                            place.isCurrentUserFavorite = !place.isCurrentUserFavorite

                                            // ✅ Revert if API failed
                                            if (!success) {
                                                place.isCurrentUserFavorite = !place.isCurrentUserFavorite
                                            }

                                            // ✅ If we are showing Favorites tab, remove unfavorited items
                                            if (selectedIndex == -2 && !place.isCurrentUserFavorite) {
                                                filteredPlaces.removeAt(currentIndex)
                                            }
                                        }
                                    }
                                }
                            },
                            onClick = {
                                val intent = Intent(context, DetailsActivity::class.java).apply {
                                    putExtra("id", filteredPlaces[index].id)
                                    putExtra("placeName", filteredPlaces[index].placeName)
                                    putExtra("placeNameAr", filteredPlaces[index].placeNameAr)
                                    putExtra("description", filteredPlaces[index].description)
                                    putExtra("descriptionAr", filteredPlaces[index].descriptionAr)
                                    putExtra("address", filteredPlaces[index].address)
                                    putExtra("distance", filteredPlaces[index].distance)
                                    putExtra("lat", filteredPlaces[index].lat)
                                    putExtra("lng", filteredPlaces[index].lon)
                                    putExtra("currentUserFavourite", filteredPlaces[index].isCurrentUserFavorite)
                                    putExtra("totalfavplace", filteredPlaces[index].total_fav_place)
                                    putExtra("totalunfavplace", filteredPlaces[index].total_unfav_place)
                                    putExtra("suggestedTime", filteredPlaces[index].suggestedTime)
                                    putExtra("advice", filteredPlaces[index].advice)
                                    putExtra("videoLinkEn", filteredPlaces[index].video_link_en)
                                    putExtra("videoLinkAr", filteredPlaces[index].video_link_ar)
                                    putExtra("adviceArabic", filteredPlaces[index].adviceArabic)
                                    putExtra("promoCodeAndDiscount", filteredPlaces[index].promoCodeAndDiscount)
                                    putExtra("promoCodePercentage", filteredPlaces[index].promoCodePercentage)
                                    putExtra("icon", filteredPlaces[index].icon)
                                    putExtra("avgRating", filteredPlaces[index].rating)



                                    val gson = Gson()
                                    putExtra("placeDetails", gson.toJson(filteredPlaces[index].tagDetails))
                                    putExtra("images", gson.toJson(image))
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun RestaurantCard(place: PlaceDetail,images: List<PlacesImage>?, onFavoriteClick: (String, (Boolean) -> Unit) -> Unit,onClick: () -> Unit) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val context = LocalContext.current
    val currentLocale = LocalConfiguration.current.locales[0]
    var isFavorite by remember(place.id, place.isCurrentUserFavorite) {
        mutableStateOf(place.isCurrentUserFavorite)
    }
    val placeImage = remember(images, place.id) {
        println("All images: ${images?.joinToString { it.placeId }}")
        println("Looking for place: ${place.id}")
        images?.firstOrNull {
            println("Checking image with placeId: ${it.placeId}")
            it.placeId.toString() == place.id.toString()
        }?.also {
            println("Found matching image: ${it.image}")
        }
    }
    var rating: Float
    try {
        rating = place.rating.toFloat()
    }catch (e:Exception) {
        rating = 0.0F
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFE7E7E9),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (placeImage != null && placeImage.image.isNotBlank()) {
                    AsyncImage(
                        model = placeImage.image,
                        contentDescription = place.placeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomEnd = 4.dp,
                                    bottomStart = 4.dp
                                )
                            ),
                        placeholder = painterResource(R.drawable.ic_placeholder),
                        error = painterResource(R.drawable.ic_placeholder),
                        onError = { result ->
                            println("Failed to load image: $result")
                        }
                    )
                } else {
                    println("No image found for place ${place.id}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 10.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.Gray, shape = CircleShape)
                            .clickable {
                                isFavorite = !isFavorite
                                place.isCurrentUserFavorite = isFavorite

                                val toastMessage = if (isFavorite) {
                                    if (currentLocale.language == "ar") "تمت الإضافة إلى المفضلة" else "Added to Favorites"
                                } else {
                                    if (currentLocale.language == "ar") "تمت الإزالة من المفضلة" else "Removed from Favorites"
                                }
                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

                                onFavoriteClick(place.id) { success ->
                                    if (!success) {
                                        isFavorite = !isFavorite
                                        place.isCurrentUserFavorite = isFavorite
                                    }
                                }
                            }

                    ) {
                        Image(
                            painter = painterResource(
                                id = if (isFavorite) R.drawable.ic_fav else R.drawable.ic_heart
                            ),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 6.dp)
                        .size(40.dp)
                        .background(color = Color(0xFFFF7D36), shape = CircleShape)
                        .align(Alignment.BottomEnd)
                )
                {

                    Log.d("Iconnns", "RestaurantCard: ${place.icon}")

                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        model = ImageRequest.Builder(context)
                            .data(place.icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentScale = ContentScale.Fit
                    )
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_restaurant),
//                        contentDescription = "",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.size(24.dp),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )
                }
            }
            val currentLocale = LocalConfiguration.current.locales[0]
            Text(
                text = if (currentLocale.language == "ar") place.placeNameAr else place.placeName,
                fontSize = 18.sp,
                fontFamily = cairoFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            var favStatus by remember {
                mutableStateOf(
                    when {
                        place.total_fav_place == "1" -> "Like"
                        place.total_unfav_place == "1" -> "Dislike"
                        else -> ""
                    }
                )
            }

            var totalFavPlace by remember { mutableStateOf(if (place.total_fav_place == "1") "1" else "0") }
            var totalUnfavPlace by remember { mutableStateOf(if (place.total_unfav_place == "1") "1" else "0") }
            Row(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_thumb_up),
                    contentDescription = "Like",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            favStatus = when (favStatus) {
                                "Like" -> {
                                    totalFavPlace = "0"
                                    ""
                                }

                                "Dislike" -> {
                                    totalUnfavPlace = "0"
                                    totalFavPlace = "1"
                                    "Like"
                                }

                                else -> {
                                    totalFavPlace = "1"
                                    "Like"
                                }
                            }
                        },
                    colorFilter = ColorFilter.tint(
                        if (favStatus == "Like") Color(0xFFFF7D36) else Color.Gray
                    )
                )

                // Like Count - only show if count is "1"
                if (totalFavPlace == "1") {
                    Text(
                        text = totalFavPlace,
                        fontFamily = cairoFamily,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "|",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )

                // Dislike Button
                Image(
                    painter = painterResource(id = R.drawable.ic_thumb_down),
                    contentDescription = "Dislike",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            favStatus = when (favStatus) {
                                "Dislike" -> {
                                    totalUnfavPlace = "0"
                                    ""
                                }

                                "Like" -> {
                                    totalFavPlace = "0"
                                    totalUnfavPlace = "1"
                                    "Dislike"
                                }

                                else -> {
                                    totalUnfavPlace = "1"
                                    "Dislike"
                                }
                            }
                        },
                    colorFilter = ColorFilter.tint(
                        if (favStatus == "Dislike") Color(0xFFFF7D36) else Color.Gray
                    )
                )

                // Dislike Count - only show if count is "1"
                if (totalUnfavPlace == "1") {
                    Text(
                        text = totalUnfavPlace,
                        fontFamily = cairoFamily,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.padding(end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->   // Always show 3 stars
                        val starColor = if (index < rating.toInt()) {
                            Color(0xFFE25E16)     // Selected stars
                        } else {
                            Color.LightGray // Unselected stars
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    starColor,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Star",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_locate),
                    contentDescription = "locate",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = place.address,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = cairoFamily,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f),
                    style = LocalTextStyle.current.copy(textDirection = TextDirection.ContentOrRtl)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(1.dp)
                        .height(14.dp)
                        .background(Color.Gray)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_routing),
                    contentDescription = "routing",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "${place.distance} km",
                    fontFamily = cairoFamily,
                    maxLines = 1,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
            }
            Text(
                text = buildAnnotatedString {
                    val htmlText = if (currentLocale.language == "ar") place.descriptionAr else place.description
                    append(
                        AnnotatedString(
                            HtmlCompat.fromHtml(
                                htmlText,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            ).toString()
                        )
                    )
                },
                fontSize = 14.sp,
                fontFamily = cairoFamily,
                maxLines = 4,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                overflow = TextOverflow.Ellipsis
            )
            val horizontalScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                place.tagDetails?.forEachIndexed { index, tag ->
                    val backgroundColor =
                        Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))


//                val textColor = if (isSelected) Color.White else Color(android.graphics.Color.parseColor(tag.colorCode ?: "#FF7D36"))

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(32.dp)
                    ) {
                        // Show tag icon if available


                        // You would need to load the icon here - this is a placeholder
                        // For example, using Coil or another image loading library
                        // Icon(Icons.Default.Tag, contentDescription = null)

                        val currentLocale = LocalConfiguration.current.locales[0]
                        Text(
                            text = if (currentLocale.language == "ar") tag.tagNameAr else tag.tagName,
                            fontSize = 14.sp,
                            fontFamily = cairoFamily,
                            modifier = Modifier
                        )

                    }
                }
            }
        }
    }
}
