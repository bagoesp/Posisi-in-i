package com.bugs.posisi_in_1301.algorithm

import com.bugs.posisi_in_1301.model.DataMasukan
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.model.SampelKnn
import kotlin.math.sqrt

class Knn(
    val listSampel: MutableList<Sampel>,
    val dataMasukan: DataMasukan,
    val k: Int
) {

    fun resultPosisi() : String {
        val finalList = mutableListOf<SampelKnn>()

        for (sampel in listSampel) {
            val jarak = euclidean(sampel, dataMasukan)
            finalList.add(SampelKnn(sampel, jarak))
        }

        val finalResult = finalList.sortedBy { it.jarak }
            .take(k)
            .groupingBy { it.sampel.label }
            .eachCount()
            .maxByOrNull { it.value }!!.key

        return finalResult!!
    }

    /*
        Fungsi untuk menghitung jarak euclidean antara data sampel terhadap data masukan
     */

    fun euclidean(sampel: Sampel, masukan: DataMasukan) : Float {
        val ap1 = pangkat(sampel.ap1!! - masukan.ap1!!)
        val ap2 = pangkat(sampel.ap2!! - masukan.ap2!!)
        val ap3 = pangkat(sampel.ap3!! - masukan.ap3!!)

        // jumlahkan keseluruhan nilai
        val jumlahAp = ap1 + ap2 + ap3

        // cari akar dari jumlah
        val euclidean = sqrt(jumlahAp.toFloat())

        // kembalikan nilai euclidean
        return euclidean
    }

    // fungsi untuk memangkatkan nilai
    fun pangkat(nilai: Int) : Int {
        return nilai * nilai
    }
}