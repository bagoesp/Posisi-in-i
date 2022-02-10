package com.bugs.posisi_in_1301.repository

import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.tools.Constants
import com.bugs.posisi_in_1301.tools.State
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SampelRepository {
    val db = Firebase.firestore.collection(Constants.COLLECTION_SAMPEL)

    // flow function firestore get data
    fun getAllSampel() = flow<State<List<Sampel>>> {
        // Emit loading state
        emit(State.loading())

        // request data
        val snapshot = db
            .orderBy("creation", Query.Direction.DESCENDING)
            .get()
            .await()

        // convert snapshot to data model
        val sampels = snapshot.toObjects(Sampel::class.java)

        // Emit success state with the data
        emit(State.success(sampels))
    }.catch {
        // If exception thrown, emit failed state along with message
        emit(State.failed(it.toString()))
    }.flowOn(Dispatchers.IO)

    // flow function to delete sampel
    fun deleteSampel(sampel: Sampel) = flow<State<Boolean>> {
        // Emit loading state
        emit(State.loading())

        // delete data process
        val id = sampel.creation.toString()
        val delSampelRef = db
            .document(id)
            .delete()
            .isSuccessful

        // Emit success
        emit(State.success(delSampelRef))
    }.catch {
        emit(State.failed(it.toString()))
    }.flowOn(Dispatchers.IO)

    // flow function to add sampel
    fun addSampel(sampel: Sampel) = flow<State<Boolean>> {
        // emit loading state
        emit(State.loading())

        // initialize the id
        val id = sampel.creation.toString()

        // add data process
        val addSampelRef = db
            .document(id)
            .set(sampel)
            .isSuccessful

        /*
        // check if the sampel has been added
        val checkSampelSnap = db
            .document(id)
            .get()
            .await()

         */

        // emit success state
        emit(State.success(addSampelRef))
    }.catch {
        emit(State.failed(it.toString()))
    }.flowOn(Dispatchers.IO)
}