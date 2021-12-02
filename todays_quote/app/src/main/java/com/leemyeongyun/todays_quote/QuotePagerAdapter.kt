package com.leemyeongyun.todays_quote

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean,
) : RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHoler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHoler(
            LayoutInflater.from(parent.context) //부모의 context 가져옴
                .inflate(R.layout.item_quote, parent, false)
        )

    override fun onBindViewHolder(holder: QuoteViewHoler, position: Int) {
        val actualPosition = position % quotes.size //실제 위치에서 명언 갯수로 나누게 되면 해당위치의 값을 넣을 수 있음
        holder.bind(quotes[actualPosition], isNameRevealed) //해당 위치의 명언을 가져옴
    }

    override fun getItemCount() = Int.MAX_VALUE //무한 전환을 위해서

    //이름과 명언을 binding하는 함수
    class QuoteViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)

        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote, isNameRevealed: Boolean) {
            quoteTextView.text = "\"${quote.quote}\"" //""로 감싸 출력

            if (isNameRevealed) {
                nameTextView.text = "- ${quote.name}"
                nameTextView.visibility = View.VISIBLE //recycle뷰라 재사용하지 않으면, 안 보일수도 있음
            } else
                nameTextView.visibility = View.GONE //아니라면 사라짐
        }


    }
}