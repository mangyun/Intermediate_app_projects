package com.leemyeongyun.todays_quote

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>,
) : RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHoler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHoler(
            LayoutInflater.from(parent.context) //부모의 context 가져옴
                .inflate(R.layout.item_quote, parent, false)
        )

    override fun getItemCount() = quotes.size

    //이름과 명언을 binding하는 함수
    class QuoteViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)

        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote) {
            quoteTextView.text = quote.quote
            nameTextView.text = quote.name
        }
    }

    override fun onBindViewHolder(holder: QuoteViewHoler, position: Int) {
        holder.bind(quotes[position]) //해당 위치의 명언을 가져옴
    }
}