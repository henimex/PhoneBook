package com.devhen.kisiler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class KisilerAdapter(private val mContex: Context, private var kisilerListe: List<Kisiler>, private val vt:VeritabaniYardimcisi)
    :RecyclerView.Adapter<KisilerAdapter.designHolder>() {

    inner class designHolder(x:View):RecyclerView.ViewHolder(x){
        var txtKisi : TextView
        var txtTel : TextView
        var txtMail : TextView
        var imgMore : ImageView

        init {
            txtKisi = x.findViewById(R.id.txtKisi)
            txtTel = x.findViewById(R.id.txtTel)
            txtMail = x.findViewById(R.id.txtMail)
            imgMore = x.findViewById(R.id.imgMore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): designHolder {
        val _desing = LayoutInflater.from(mContex).inflate(R.layout.kisi_card_tasarim,parent,false)
        return designHolder(_desing)
    }

    override fun onBindViewHolder(holder: designHolder, position: Int) {
        val kisi = kisilerListe[position]
        holder.txtKisi.text = kisi.kisi_ad
        holder.txtMail.text = kisi.kisi_mail
        holder.txtTel.text = kisi.kisi_tel

        holder.imgMore.setOnClickListener {
            val moreMenu = PopupMenu(mContex,holder.imgMore)
            moreMenu.menuInflater.inflate(R.menu.more_menu,moreMenu.menu)

            moreMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.action_sil -> {
                        Snackbar.make(holder.imgMore,"${kisi.kisi_ad} Silinsin mi ?", Snackbar.LENGTH_LONG)
                            .setTextColor(Color.WHITE)
                            .setBackgroundTint(Color.BLACK)
                            .setActionTextColor(Color.RED)
                            .setAction("EVET"){
                                KisilerDao().simpleDelete(vt,kisi.kis_id)
                                kisilerListe = KisilerDao().tumTablo(vt)
                                notifyDataSetChanged()
                            }.show()
                        true
                    }
                    R.id.action_guncelle ->{
                        kisiGuncelle(kisi)
                        true
                    }
                    else -> false
                }
            }
            moreMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return kisilerListe.size
    }

    fun kisiGuncelle(kisi:Kisiler){
        val tasarim = LayoutInflater.from(mContex).inflate(R.layout.alert_tasarimi,null)
        val edtKisiAdi = tasarim.findViewById(R.id.edtKisiAdi) as EditText
        val edtMail = tasarim.findViewById(R.id.edtMail) as EditText
        val edtTel = tasarim.findViewById(R.id.edtTel) as EditText

        edtKisiAdi.setText(kisi.kisi_ad)
        edtMail.setText(kisi.kisi_mail)
        edtTel.setText(kisi.kisi_tel)

        val ad = AlertDialog.Builder(mContex)
        ad.setTitle("Güncelleme")
        ad.setView(tasarim)
        ad.setPositiveButton("Güncelle"){dialogInterface, i ->
            val kisi_ad = edtKisiAdi.text.toString().trim()
            val kisi_tel = edtTel.text.toString().trim()
            val kisi_mail = edtMail.text.toString().trim()
            KisilerDao().simpleUpdate(vt,kisi.kis_id,kisi_ad,kisi_tel,kisi_mail)
            kisilerListe = KisilerDao().tumTablo(vt)
            notifyDataSetChanged()
        }
        ad.setNegativeButton("Iptal"){dialogInterface, i ->
            Toast.makeText(mContex,"Cancalled", Toast.LENGTH_SHORT).show()
        }
        ad.create().show()
    }

}