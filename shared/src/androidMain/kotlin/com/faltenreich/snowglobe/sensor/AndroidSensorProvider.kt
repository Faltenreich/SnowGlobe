package com.faltenreich.snowglobe.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AndroidSensorProvider(context: Context) : SensorProvider {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _acceleration = MutableStateFlow(Acceleration.Zero)
    override val acceleration = _acceleration

    private val listener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            event?.takeIf { it.values.size >= 3 } ?: return
            val acceleration = Acceleration(
                x = event.values[0],
                y = event.values[1],
                z = event.values[2],
            )
            _acceleration.update { acceleration }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    override fun start() {
        println("Starting sensor observation")
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stop() {
        println("Stopping sensor observation")
        sensorManager.unregisterListener(listener, sensor)
    }
}