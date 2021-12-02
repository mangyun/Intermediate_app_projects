package com.leemyeongyun.todays_quote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    //transformer로 전환효과
    private fun initViews() {
        viewPager.setPageTransformer { page, position -> //kotlin이라 lambda로 가능
            when {
                position.absoluteValue >= 0.5F -> { //절댓값으로 표현한, 화면 좌우 0.5칸 밖인경우
                    page.alpha = 0F //투명하게
                }
                position == 0F -> { //화면중앙이라면
                    page.alpha = 1F //완전히 보이기
                }
                else -> { //화면 중앙에서 좌우 0.5칸 안인경우
                    page.alpha = 1F - 2 * position.absoluteValue

                }
            }
        }
    }

    private fun initData() {//기본적으로 12시간 fetch에 제한이 걸림
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(//비동기로 지정
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 //서버에서 block 하지않는 이상, 앱이 들어올때마다 곧바로 fetch
            }
        )
        //비동기적이기때문에, 리스너 따로 등록,
        remoteConfig.fetchAndActivate().addOnCompleteListener { //remoteConfig가 fetch를 완료하는것
            progressBar.visibility = View.GONE
            if (it.isSuccessful) {//성공했다면
                val quotes = parseQuotesJson(remoteConfig.getString("quotes")) // 명언 가져옴
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed") // 이름 가져옴

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
        val adapter = QuotePagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter = adapter
        //부드럽게 넘어가는 스크롤은 안되게, maxValue값의 반만큼 좌우로 뷰갯수 생성
        viewPager.setCurrentItem(adapter.itemCount / 2, false)
    }
}