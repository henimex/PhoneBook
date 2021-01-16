package com.devhen.kisiler

import android.content.ContentValues

class KisilerDao {

    fun simpleDelete(vt: VeritabaniYardimcisi, kisi_id: Int) {
        val db = vt.writableDatabase
        db.delete("kisiler", "kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }

    fun simpleAdd(vt: VeritabaniYardimcisi, kisi_ad: String, kisi_tel: String, kisi_mail: String) {
        val db = vt.writableDatabase
        val values = ContentValues()
        values.put("kisi_ad", kisi_ad)
        values.put("kisi_tel", kisi_tel)
        values.put("kisi_mail", kisi_mail)

        db.insertOrThrow("kisiler", null, values)
        db.close()
    }

    fun simpleUpdate(vt: VeritabaniYardimcisi, kisi_id: Int, kisi_ad: String, kisi_tel: String, kisi_mail: String) {
        val db = vt.writableDatabase
        val values = ContentValues()
        values.put("kisi_ad", kisi_ad)
        values.put("kisi_tel", kisi_tel)
        values.put("kisi_mail", kisi_mail)

        db.update("kisiler",values,"kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }

    fun tumTablo(vt: VeritabaniYardimcisi): ArrayList<Kisiler> {
        val db = vt.writableDatabase
        val kisilerListe = ArrayList<Kisiler>()
        val c = db.rawQuery("SELECT * FROM kisiler", null)

        while (c.moveToNext()) {
            val kisi = Kisiler(
                c.getInt(c.getColumnIndex("kisi_id")),
                c.getString(c.getColumnIndex("kisi_ad")),
                c.getString(c.getColumnIndex("kisi_tel")),
                c.getString(c.getColumnIndex("kisi_mail"))
            )
            kisilerListe.add(kisi)
        }
        return kisilerListe
    }

    fun kisiAra(vt: VeritabaniYardimcisi, aramaKelime:String): ArrayList<Kisiler> {
        val db = vt.writableDatabase
        val kisilerListe = ArrayList<Kisiler>()
        val c = db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad LIKE '%$aramaKelime%'", null)

        while (c.moveToNext()) {
            val kisi = Kisiler(
                c.getInt(c.getColumnIndex("kisi_id")),
                c.getString(c.getColumnIndex("kisi_ad")),
                c.getString(c.getColumnIndex("kisi_tel")),
                c.getString(c.getColumnIndex("kisi_mail"))
            )
            kisilerListe.add(kisi)
        }
        return kisilerListe
    }

}