package com.leemyeongyun.todays_quote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initData()
    }

    private fun initData() {//기본적으로 12시간 fetch에 제한이 걸림
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(//비동기로 지정
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 //서버에서 block 하지않는 이상, 앱이 들어올때마다 곧바로 fetch
            }
        )
        //비동기적이기때문에, 리스너 따로 등록
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {//성공했다면
                val quotes = parseQuotesJson(remoteConfig.getString("quotes")) // 명언 가져옴
                val isNameRevealed = remoteConfig.getBoolean(("is_name_revealed")) // 이름 가져옴

                displayQuotesPager(quotes, isNameRevealed)


            }
        }

    }

    //json 파싱함수
    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json) //일단 json string으로 json array를 만듬
        var jsonList = emptyList<JSONObject>() //반복을 돌리기 위해, json array를 list에 추가
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject((index))  //array 크기만큼, index로 받아 가져옴
            jsonObject?.let { // null이 아니라면,
                jsonList = jsonList + it //뒤에 한개씩 json object가 추가됨

            }
        }

        //jsonList를 다시 QuoteList로 변환
        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name"))
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        viewPager.adapter = QuotePagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        ) //지금은 빈 리스트 가져오기
    }
}