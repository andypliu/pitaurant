/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hanfeng.pitant.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import com.hanfeng.pitant.helper.ioThread
import com.hanfeng.pitant.model.Data
import com.google.gson.Gson

/**
 * Database schema that holds the list of restaurants.
 */
@Database(
        entities = [RestaurantEntity::class],
        version = 1,
        exportSchema = false
)
abstract class RestaurantDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {

        @Volatile
        private var INSTANCE: RestaurantDatabase? = null

        fun getInstance(context: Context): RestaurantDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        RestaurantDatabase::class.java, "RestaurantEntity.db")
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                fillInDb(context.applicationContext)
                            }
                        })
                        .build()


        private fun fillInDb(context: Context) {
            // inserts in Room are executed on the current thread, so we insert in the background
            ioThread {
                val gson = Gson()
                val restaurants = gson.fromJson(RESTAURANT_JSON, Data::class.java)

                System.out.println("Number of Restaurants: ${restaurants.data.size}")

                ioThread {
                    getInstance(context).restaurantDao().insert(restaurants.data)
                    Log.d("RestaurantDB", "Inserted Data!")
                }
                System.out.println("Finished inserting data!!!")
            }
        }

        private val RESTAURANT_JSON = "{\n" +
                "  \"data\" :[\n" +
                "    {\"name\": \"珍宝家厨\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"San Francisco\", \"distance\": \"1.8 mi\", \"thumbnail\" : \"@drawable/jumbo\",\n" +
                "      \"address\" : \"1532 Noriega St. \nSan Francisco, CA 94122\", \"phone\" : \"(415) 681-1800\", \"special_description\" : \"都好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\"},\n" +
                "\n" +
                "\n" +
                "    {\"name\": \"金乐\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"San Francisco\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/jinle\",\n" +
                "      \"address\" : \"834 Washington St. \nSan Francisco, CA 94108\", \"phone\" : \"(415) 421-8102\", \"special_description\" : \"龙虾餐， 螃蟹餐\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\" },\n" +
                "    {\"name\": \"迎宾阁\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"San Francisco\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/yingbinge\",\n" +
                "      \"address\" : \"641 Jackson St. \nSan Francisco, CA 94133\", \"phone\" : \"(415) 398-8383\", \"special_description\" : \"Obama 来点过点心\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.peninsulaseafoodsf.com\"},\n" +
                "\n" +
                "    {\"name\": \"穗香酒家\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"San Francisco\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/hk_lounge\",\n" +
                "      \"address\" : \"5322 Geary Blvd. \nSan Francisco, CA 94121\", \"phone\" : \"(415) 668-8836\", \"special_description\" : \"试试吧\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\"},\n" +
                "\n" +
                "\n" +
                "    {\"name\": \"菠萝王\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"San Francisco\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/pineapple_bakery\",\n" +
                "      \"address\" : \"1915 Irving St. \nSan Francisco, CA 94122\", \"phone\" : \"(415) 702-6118\", \"special_description\" : \"便宜又新鲜的包\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\" },\n" +
                "    {\"name\": \"荔苑\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"San Francisco\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/liyuan\",\n" +
                "      \"address\" : \"2333 Irving St. \nSan Francisco, CA 94122\", \"phone\" : \"(415) 681-0488\", \"special_description\" : \"2pm - 5pm 有 \$2.99 小盘菜\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"2\", \"url\": \"NONE\"},\n" +
                "    {\"name\": \"煲仔王\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"San Francisco\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/claypot_taishan\",\n" +
                "      \"address\" : \"1000 Clement St. \nSan Francisco, CA 94118\", \"phone\" : \"(415) 668-0838\", \"special_description\" : \"台山黄鳝饭\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\"},\n" +
                "\n" +
                "\n" +
                "    {\"name\": \"杏记甜品\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Colma\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/xin_ji_sweet\",\n" +
                "      \"address\" : \"15 Colma Blvd. \nColma, CA 94014\", \"phone\" : \"(650) 822-2800\", \"special_description\" : \"好吃甜品\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.sweethoneydessert.com\"},\n" +
                "    {\"name\": \"广东烧腊小馆\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"San Francisco\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/guangdong_shaola\",\n" +
                "      \"address\" : \"2191 Irving St. \nSan Francisco, CA 94122\", \"phone\" : \"(415) 731-9948\", \"special_description\" : \"吃完卖只烧鸭回家\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\" },\n" +
                "\n" +
                "    {\"name\": \"彩蝶轩\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Millbrae\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/cai_dei_xuan\",\n" +
                "      \"address\" : \"1180 El Camino Real \nMillbrae, CA 94030\", \"phone\" : \"(650) 616-9388\", \"special_description\" : \"挺好吃的\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.zenpeninsula.com\"},\n" +
                "\n" +
                "    {\"name\": \"鲤鱼门\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Daly City\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/liyumen\",\n" +
                "      \"address\" : \"365 Gellert Blvd. \nDaly City, CA 94015\", \"phone\" : \"(650) 992-9000\", \"special_description\" : \"好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"5\", \"url\": \"http://koipalace.com\"},\n" +
                "\n" +
                "    {\"name\": \"世界美食\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"Fremont\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/worldbuffet\",\n" +
                "      \"address\" : \"6010 Stevenson Blvd. \nFremont, CA 94538\", \"phone\" : \"(510) 490-6888\", \"special_description\" : \"性价比高\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.goworldgourmet.net\"},\n" +
                "    {\"name\": \"民俗村\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"Milpitas\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/mingzhu_village\",\n" +
                "      \"address\" : \"688 Barber Ln. \nMilpitas CA 95035\", \"phone\" : \"(408) 894-7060\", \"special_description\" : \"各样野味\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"NONE\" },\n" +
                "    {\"name\": \"岭南美食坊\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Fremont\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/lingnan_street\",\n" +
                "      \"address\" : \"46356 Warm Springs Blvd \nFremont, CA 94539\", \"phone\" : \"(510) 573-4066\", \"special_description\" : \"岭南名菜\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"http://www.hkcheffremont.com\" },\n" +
                "\n" +
                "    {\"name\": \"金银岛\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Daly City\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/jinyin_island\",\n" +
                "      \"address\" : \"1300 Noriega St. \nSan Francisco, CA 94122\", \"phone\" : \"(415) 759-9118\", \"special_description\" : \"饭后来好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://goldenislandsf.com\"},\n" +
                "\n" +
                "    {\"name\": \"西贡渔港\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Sunnyvale\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/saigon_harbor\",\n" +
                "      \"address\" : \"1135 Lawrence Expy. \nSunnyvale, CA 94089\", \"phone\" : \"(408) 734-2828\", \"special_description\" : \"太好吃了\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"http://saigonharbors.com\"},\n" +
                "\n" +
                "    {\"name\": \"月星宫\", \"type\" : \"粤\", \"rating\" : \"4\", \"city\" : \"Daly City\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/moon_star\",\n" +
                "      \"address\" : \"383 Gellert Blvd \nDaly City, CA 94015\", \"phone\" : \"(650) 992-2888\", \"special_description\" : \"生日 Party\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"http://moonstarbuffet.com\" },\n" +
                "\n" +
                "    {\"name\": \"凤凰名粥\", \"type\" : \"粤\", \"rating\" : \"3\", \"city\" : \"Sunnyvale\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/mingtasty\",\n" +
                "      \"address\" : \"1129 Lawrence Expy, \nSunnyvale, CA 94089\", \"phone\" : \"(408) 734-1188\", \"special_description\" : \"各样的粥，菜也不错\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.mingtastysunnyvale.com\" },\n" +
                "    {\"name\": \"顺峰渔港\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Fremont\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/shunfeng_yugang\",\n" +
                "      \"address\" : \"43635 Boscell Rd. \nFremont, CA 94538\", \"phone\" : \"(510) 979-1368\", \"special_description\" : \"广东与各地名菜\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"NONE\" },\n" +
                "\n" +
                "    {\"name\": \"香滿樓\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Millbrea\", \"distance\": \"2.0 mi\" , \"thumbnail\" : \"@drawable/xiangmanglou\",\n" +
                "      \"address\" : \"51 E Millbrae Ave. \nMillbrae, CA 94030\", \"phone\" : \"(650) 692-6666\", \"special_description\" : \"太好吃了\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"http://www.mayflower-seafood.com/HKFL/index.htm\"},\n" +
                "    {\"name\": \"顺峰\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Millbrea\", \"distance\": \"2.5 mi\", \"thumbnail\" : \"@drawable/shunfeng\",\n" +
                "      \"address\" : \"1671 El Camino Real \nMillbrae, CA 94030\", \"phone\" : \"(650) 616-8288\", \"special_description\" : \"没什么不好吃的\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"4\", \"url\": \"NONE\" },\n" +

                "    {\"name\": \"五月花\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Milpitas\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/mayflower\",\n" +
                "      \"address\" : \"428 Barber Ln. \nMilpitas, CA 95035\", \"phone\" : \"(408) 922-2700\", \"special_description\" : \"特好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.mayflower-seafood.com/indexE.html\" },\n" +
                "\n" +
                "    {\"name\": \"半岛豪苑酒家\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"South San Francisco\", \"distance\": \"5.1 mi\", \"thumbnail\" : \"@drawable/half_island\",\n" +
                "      \"address\" : \"608 Dubuque Ave. \nSouth San Francisco \nCA 94080\", \"phone\" : \"(650) 616-8168\", \"special_description\" : \"有点贵， 不过好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"5\", \"url\": \"http://www.hlpeninsula.com\" },\n" +
                "\n" +
                "    {\"name\": \"新福临门\", \"type\" : \"粤\", \"rating\" : \"5\", \"city\" : \"Fremont\", \"distance\": \"6.9 mi\", \"thumbnail\" : \"@drawable/fulammoon\",\n" +
                "      \"address\" : \"40460 Albrae St. \nFremont, CA 94538\", \"phone\" : \"(510) 668-1333\", \"special_description\" : \"好吃，好吃\",\n" +
                "      \"description\": \"N/A\", \"price_rank\" : \"3\", \"url\": \"http://www.fulammoon.com\"}\n" +
                "\n" +
                "  ]\n" +
                "}"
    }
}
