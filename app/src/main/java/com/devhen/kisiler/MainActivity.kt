package com.devhen.kisiler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    private lateinit var kisilerListe:ArrayList<Kisiler>
    private lateinit var adapter: KisilerAdapter
    private lateinit var vt: VeritabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Kişi Listesi"
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        vt = VeritabaniYardimcisi(this)
        tumKisileriListele()

        fab.setOnClickListener {
            alertGoster()
        }
    }

    private fun tumKisileriListele() {
        kisilerListe = KisilerDao().tumTablo(vt)
        adapter= KisilerAdapter(this,kisilerListe,vt)
        rv.adapter=adapter
    }

    private fun kisiAra(arananKelime:String) {
        kisilerListe = KisilerDao().kisiAra(vt,arananKelime)
        adapter= KisilerAdapter(this,kisilerListe,vt)
        rv.adapter=adapter
    }

    fun alertGoster(){
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarimi,null)
        val edtKisiAdi = tasarim.findViewById(R.id.edtKisiAdi) as EditText
        val edtMail = tasarim.findViewById(R.id.edtMail) as EditText
        val edtTel = tasarim.findViewById(R.id.edtTel) as EditText

        val ad = AlertDialog.Builder(this)
        ad.setTitle("Yeni Kişi Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){dialogInterface, i ->
            val kisi_ad = edtKisiAdi.text.toString().trim()
            val kisi_tel = edtTel.text.toString().trim()
            val kisi_mail = edtMail.text.toString().trim()

            KisilerDao().simpleAdd(vt,kisi_ad,kisi_tel,kisi_mail)
            tumKisileriListele()
        }
        ad.setNegativeButton("Iptal"){dialogInterface, i ->
            Toast.makeText(this,"Cancalled",Toast.LENGTH_SHORT).show()
        }
        ad.create().show()
    }

    /**
     * SEARCH BAR KODLARI. Classı SearchView.OnQueryTextListener inherit etmeyi unutma (androidx sürümü ile )
     * Not tasarıma view class eklerken ordaki androidx sürümü ile aynı olan inheritance ı burda aktif edecegiz.
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu,menu)
        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        kisiAra(query.toString())
        Log.e("Gonderilen Arama",query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        kisiAra(newText.toString())
        Log.e("HarfGirdikçe",newText.toString())
        return true
    }
}