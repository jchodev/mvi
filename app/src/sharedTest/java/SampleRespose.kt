import com.jerry.mvi.data.model.Product
import com.jerry.mvi.data.model.ProductListResponse


fun getHoodieListResponse() : ProductListResponse {
    return ProductListResponse(
        products = listOf(
            Product(
                id = "Hoodie1",
                name = "HoodieName1",
                price = "HoodiePrice1",
                image = "HoodieImage1",
            ),
            Product(
                id = "Hoodie2",
                name = "HoodieName2",
                price = "HoodiePrice2",
                image = "HoodieImage2",
            ),
        ),
        title = "Hoodie",
        product_count = 2,
    )
}

fun getSneakerListResponse() : ProductListResponse {
    return ProductListResponse(
        products = listOf(
            Product(
                id = "Sneaker1",
                name = "SneakerName1",
                price = "SneakerPrice1",
                image = "SneakerImage1",
            ),
            Product(
                id = "Sneaker2",
                name = "SneakerName2",
                price = "SneakerPrice2",
                image = "SneakerImage2",
            ),
            Product(
                id = "Sneaker3",
                name = "SneakerName3",
                price = "SneakerPrice3",
                image = "SneakerImage3",
            ),
        ),
        title = "Sneaker",
        product_count = 3,
    )
}