package com.example.loteriapractica

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loteriapractica.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var baraja = ArrayList<Cartas>()
    var control_juego = true
    var hilo = Hilo(this)
    var nuevo_Juego = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        llenarBaraja() //llenamos la baraja
        binding.btnInicio.setOnClickListener {
            try {
                if (hilo.verificar == true) {
                    /* para verificar*/
                    cancelarVerificacion()
                    return@setOnClickListener
                }
                if (control_juego) {
                    if(!nuevo_Juego) {
                        hilo.start()
                        hilo.pausarHilo()
                        cuentaRegresiva()
                        return@setOnClickListener
                    } else {
                        /* reanudamos o ejecutamos de nuevo*/
                        hilo.pausarHilo()
                        cuentaRegresiva2()
                        return@setOnClickListener
                    }
                }
                if (!control_juego) {
                    ganador()
                }
            } catch (e:Exception) { }
        }

        binding.btnPausar.setOnClickListener {
            if (hilo.estaPausado() == false) {
                hilo.pausarHilo()
            } else {
                hilo.despausarHilo()
            }
        }

        binding.btnVerificar.setOnClickListener {
            if (hilo.todasCartas()) {
                verificarCartas()
            }
        }

    }

    fun cardMostrar(tam:Int) = GlobalScope.launch { // mostrar cartas en segundo plano
        var vector = ArrayList<Cartas>()
        (0..tam-1).forEach {
            vector.add(baraja[it])
        }

        runOnUiThread {
            val adapter = CustomAdapter(vector)
            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL,false)
            binding.recyclerView.scrollToPosition(tam-1)
            binding.recyclerView.adapter = adapter
        }
    }

    fun aleatorio(lista:ArrayList<Cartas>) { // ordena el arreglo aleatorio
        lista.shuffle()
    }

    fun botonesControl() {
        aleatorio(baraja)
        binding.imagen.setImageResource(R.drawable.loteria)
        binding.btnInicio.setText("Terminar")
        binding.btnInicio.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.purple_700))
        binding.btnPausar.visibility = View.VISIBLE
        binding.btnVerificar.visibility = View.INVISIBLE
        control_juego = false
        binding.btnPausar.setText("Pausa")
    }

    fun ganador() {
        hilo.ganador()
        binding.btnInicio.setText("Jugar de nuevo")
        nuevo_Juego = true
        control_juego = true
    }

    fun verificarCartas() {
        hilo.verificar = true
        hilo.despausarHilo()
        binding.btnVerificar.visibility = View.INVISIBLE
        binding.btnInicio.setText("Cancelar")
        binding.btnPausar.setText("Pausar")
    }

    fun cancelarVerificacion() {
        hilo.cancelarHilo()
        hilo.verificar = false
        binding.btnInicio.setText("Jugar de nuevo")
    }

    //en segundo plano
    fun cuentaRegresiva() = GlobalScope.launch {
        var contador = 0
        runOnUiThread {
            binding.imagen.visibility = View.INVISIBLE
            binding.btnInicio.visibility = View.INVISIBLE
            binding.btnVerificar.visibility = View.INVISIBLE
        }
        (contador..2).forEach {
            runOnUiThread {
                binding.txtContador.text = (3-contador+1).toString()
            }
            contador++
            delay(900)
        }
        if (contador == 3) {
            runOnUiThread {
                binding.imagen.visibility = View.VISIBLE
                binding.btnInicio.visibility = View.VISIBLE
                hilo.despausarHilo()
                nuevo_Juego = true
                botonesControl()
            }
        }
    }
    //cuenta en segujndo plano
    fun cuentaRegresiva2() = GlobalScope.launch {
        var contador = 0
        runOnUiThread {
            binding.imagen.visibility = View.INVISIBLE
            binding.btnInicio.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
            binding.btnVerificar.visibility = View.INVISIBLE
        }
        (contador..2).forEach {
            runOnUiThread {
                binding.txtContador.text = (3-contador+1).toString()
            }
            contador++
            delay(900)
        }
        if (contador == 3) {
            runOnUiThread {
                binding.imagen.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.btnInicio.visibility = View.VISIBLE
                hilo.despausarHilo()
                botonesControl()

                hilo.nuevoJuego()
            }
        }
    }

    fun reproducir(pos:Int) = GlobalScope.launch {
        val contador2 = pos
        try {
            val mp = MediaPlayer.create(this@MainActivity,baraja[contador2].audio)
            mp.start()
            delay(1900)
            mp.reset()
        } catch (e: IOException) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("No se pudo reproducir el audio")
                .setMessage(e.message)
                .setNeutralButton("Aceptar", {d,i -> d.dismiss()})
                .show()
        }
    }



    fun llenarBaraja() {
        baraja.add(Cartas(R.drawable.elcazo,R.raw.elcazoaudio))
        baraja.add(Cartas(R.drawable.elpajaro,R.raw.elpajaroaudio))
        baraja.add(Cartas(R.drawable.labandera,R.raw.labanderaaudio))
        baraja.add(Cartas(R.drawable.elalacran,R.raw.elalacranaudio))
        baraja.add(Cartas(R.drawable.elcorazon,R.raw.elcorazonaudio))
        baraja.add(Cartas(R.drawable.elparaguas,R.raw.elparaguasaudio))
        baraja.add(Cartas(R.drawable.labota,R.raw.labotaaudio))
        baraja.add(Cartas(R.drawable.elapache,R.raw.elapacheaudio))
        baraja.add(Cartas(R.drawable.elcotorro,R.raw.elcotorroaudio))
        baraja.add(Cartas(R.drawable.elpescado,R.raw.elpescadoaudio))
        baraja.add(Cartas(R.drawable.labotella,R.raw.labotellaaudio))
        baraja.add(Cartas(R.drawable.elarbol,R.raw.elarbolaudio))
        baraja.add(Cartas(R.drawable.eldiablito,R.raw.eldiablitoaudio))
        baraja.add(Cartas(R.drawable.elpino,R.raw.elpinoaudio))
        baraja.add(Cartas(R.drawable.lacalavera,R.raw.lacalaveraaudio))
        baraja.add(Cartas(R.drawable.elarpa,R.raw.elarpaaudio))
        baraja.add(Cartas(R.drawable.elgallo,R.raw.elgalloaudio))
        baraja.add(Cartas(R.drawable.elsol,R.raw.elsolaudio))
        baraja.add(Cartas(R.drawable.lacampana,R.raw.lacampanaaudio))
        baraja.add(Cartas(R.drawable.elbandolon,R.raw.elbandolonaudio))
        baraja.add(Cartas(R.drawable.elgorrito,R.raw.elgorritoaudio))
        baraja.add(Cartas(R.drawable.elsoldado,R.raw.elsoldadoaudio))
        baraja.add(Cartas(R.drawable.lachalupa,R.raw.lachalupaaudio))
        baraja.add(Cartas(R.drawable.elbarril,R.raw.elbarrilaudio))
        baraja.add(Cartas(R.drawable.elmelon,R.raw.elmelonaudio))
        baraja.add(Cartas(R.drawable.eltambor,R.raw.eltamboraudio))
        baraja.add(Cartas(R.drawable.lacorona,R.raw.lacoronaaudio))
        baraja.add(Cartas(R.drawable.elborracho,R.raw.elborrachoaudio))
        baraja.add(Cartas(R.drawable.elmundo,R.raw.elmundoaudio))
        baraja.add(Cartas(R.drawable.elvaliente,R.raw.elvalienteaudio))
        baraja.add(Cartas(R.drawable.ladama,R.raw.ladamaaudio))
        baraja.add(Cartas(R.drawable.elcamaron,R.raw.elcamaronaudio))
        baraja.add(Cartas(R.drawable.elmusico,R.raw.elmusicoaudio))
        baraja.add(Cartas(R.drawable.elvenado,R.raw.elvenadoaudio))
        baraja.add(Cartas(R.drawable.laescalera,R.raw.laescaleraaudio))
        baraja.add(Cartas(R.drawable.elcantarito,R.raw.elcantaritoaudio))
        baraja.add(Cartas(R.drawable.elnegrito,R.raw.elnegritoaudio))
        baraja.add(Cartas(R.drawable.elvioloncello,R.raw.elvioloncelloaudio))
        baraja.add(Cartas(R.drawable.laestrella,R.raw.laestrellaaudio))
        baraja.add(Cartas(R.drawable.elcatrin,R.raw.elcatrinaudio))
        baraja.add(Cartas(R.drawable.elnopal,R.raw.elnopalaudio))
        baraja.add(Cartas(R.drawable.laarana,R.raw.laaranaaudio))
        baraja.add(Cartas(R.drawable.lagarza,R.raw.lagarzaaudio))
        baraja.add(Cartas(R.drawable.laluna,R.raw.lalunaaudio))
        baraja.add(Cartas(R.drawable.lamaceta,R.raw.lamacetaaudio))
        baraja.add(Cartas(R.drawable.lamano,R.raw.lamanoaudio))
        baraja.add(Cartas(R.drawable.lamuerte,R.raw.lamuerteaudio))
        baraja.add(Cartas(R.drawable.lapalma,R.raw.lapalmaaudio))
        baraja.add(Cartas(R.drawable.lapera,R.raw.laperaaudio))
        baraja.add(Cartas(R.drawable.larana,R.raw.laranaaudio))
        baraja.add(Cartas(R.drawable.larosa,R.raw.larosaaudio))
        baraja.add(Cartas(R.drawable.lasandia,R.raw.lasandiaaudio))
        baraja.add(Cartas(R.drawable.lasirena,R.raw.lasirenaaudio))
        baraja.add(Cartas(R.drawable.lasjaras,R.raw.lasjarasaudio))
    }

}