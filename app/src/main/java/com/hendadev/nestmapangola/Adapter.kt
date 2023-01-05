package com.hendadev.nestmapangola

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class Adapter(private val context: Activity, private val paragem: List<Paragem>) :
    ArrayAdapter<Paragem>(context, R.layout.lista_paragem, paragem) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.lista_paragem, null, true)

        val _ID = rowView.findViewById(R.id._ID) as TextView
        val paragemNomeView = rowView.findViewById(R.id.paragemNomeView) as TextView
        val localidadeNameView = rowView.findViewById(R.id.localidadeNameView) as TextView
        val paragemImgView = rowView.findViewById(R.id.paragemImgView) as ImageView
        val rotasNameView = rowView.findViewById(R.id.rotasNameView) as TextView
        val estudoView = rowView.findViewById(R.id.estudoView) as TextView
//        val numerodeTaxiView = rowView.findViewById(R.id.estudoView) as TextView

        _ID.text = paragem[position]._ID.toString()
        paragemNomeView.text = paragem[position].nomeParagem
        localidadeNameView.text = paragem[position].localidade
        paragemImgView.setImageResource(paragem[position].imagemParagem)
        rotasNameView.text = paragem[position].rotas.toString()
//        numerodeTaxiView.text = paragem[position].numTaxiAgora
        estudoView.text = paragem[position].estado

        return rowView
    }
}