package com.example.loteriapractica

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat

//clase hilo
class Hilo(p:MainActivity) : Thread() {
    var puntero = p
    var contador = -1
    var contador2 = 0
    var verificar = false
    var todas = false
    private var ejecutar = true
    private var pausar = false


    override fun run() {
        super.run()
        while (true) {
            if (ejecutar) {
                if (!pausar) {
                    if (!verificar) {
                        if (contador == puntero.baraja.size - 1) {
                            pausarHilo()
                            terminar()
                            cartasTerminadas()
                        } else {
                            puntero.runOnUiThread {
                                todas = false
                                puntero.cardMostrar(contador+1)
                                puntero.binding.txtCartas.setText("Cartas: ${contador+1}")
                                puntero.binding.imagen.setImageResource(puntero.baraja[contador].imagen)
                                puntero.reproducir(contador)
                            }
                        }
                    }
                    if (verificar) {
                        if (contador2 == puntero.baraja.size - 1) {
                            pausarHilo()
                            terminar()
                            cartasTerminadas()
                            verificar = false
                            puntero.runOnUiThread {
                                puntero.binding.btnVerificar.visibility = View.INVISIBLE
                                puntero.binding.btnInicio.setText("Jugar otra vez")
                            }
                        } else {
                            puntero.runOnUiThread {
                                todas = false
                                puntero.cardMostrar(contador2+1)
                                puntero.binding.txtCartas.setText("Cartas: ${contador2+1}")
                                puntero.binding.imagen.setImageResource(puntero.baraja[contador2].imagen)
                                puntero.reproducir(contador2)
                            }
                        }
                        contador2++
                    }
                    contador++
                }
            } else {
                contador2 = contador-1 //  indice del juego
                contador = -1
                puntero.runOnUiThread {
                    puntero.binding.btnInicio.visibility = View.VISIBLE
                    puntero.binding.txtCartas.setText("Cartas: ${contador+1}")
                }
                ejecutar = true
                todas = true
            }
            sleep(1900L)
        }
    }

    fun pausarHilo() {
        pausar = true
        puntero.runOnUiThread {
            puntero.binding.btnPausar.setText("Reanudar")
        }
    }

    fun despausarHilo() {
        pausar = false
        puntero.runOnUiThread {
            puntero.binding.btnPausar.setText("Pausar")
        }
    }

    fun estaPausado(): Boolean {
        return pausar
    }

    fun terminar() {
        ejecutar = false
    }

    fun nuevoJuego() {
        ejecutar = true
        pausar = false
        verificar = false
    }

    fun cartasTerminadas() {
        puntero.runOnUiThread {
            AlertDialog.Builder(puntero)
                .setTitle("CARTAS TERMINADAS")
                .setMessage("Todas las cartas ya fueron reveladas.")
                .setNeutralButton("ACEPTAR", {d,i -> d.dismiss()})
                .show()
            puntero.binding.btnPausar.visibility = View.INVISIBLE // ocultar boton
            sleep(200)
        }
    }

    //todas las cartas
    fun todasCartas() : Boolean {
        return todas
    }

    fun ganador() {
        terminar()
        pausarHilo()
        puntero.runOnUiThread {
            puntero.binding.btnInicio.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(puntero,R.color.amarillo))
            puntero.binding.btnPausar.visibility = View.INVISIBLE
            puntero.binding.btnVerificar.visibility = View.VISIBLE
            if (todas) {
                AlertDialog.Builder(puntero)
                    .setTitle("Fin de las cartas")
                    .setMessage("No hay ganador.")
                    .setNeutralButton("Aceptar", {d,i -> d.dismiss()})
                    .show()
            }
            if (!todas) {
                AlertDialog.Builder(puntero)
                    .setTitle("Ganador!")
                    .setMessage("Hay un ganador.")
                    .setNeutralButton("Aceptar", {d,i -> d.dismiss()})
                    .show()
            }
        }
    }

    fun cancelarHilo() {
        pausarHilo()
        terminar()
    }
}