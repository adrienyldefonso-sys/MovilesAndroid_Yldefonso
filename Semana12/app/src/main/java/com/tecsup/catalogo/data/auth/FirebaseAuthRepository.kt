
package com.tecsup.catalogo.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(
        correo: String,
        clave: String
    ): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave)
            .await().user!!.uid

        Result.success(leerSesion(uid, correo))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null
        return leerSesion(u.uid, u.email ?: "")
    }

    override fun cerrarSesion() = auth.signOut()

    // El rol se lee de Firestore (usuarios/{uid}, campo "rol").
    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios")
            .document(uid)
            .get()
            .await()

        // LOG TEMPORAL PARA COMPROBAR EL ROL
        Log.d(
            "ROL",
            "uid=$uid existe=${doc.exists()} rol=${doc.getString("rol")}"
        )

        val rol = if (doc.getString("rol") == "admin") {
            Rol.ADMIN
        } else {
            Rol.USUARIO
        }

        return Sesion(uid, correo, rol)
    }
}
