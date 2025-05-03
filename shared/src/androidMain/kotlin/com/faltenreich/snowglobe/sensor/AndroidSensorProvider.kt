package com.faltenreich.snowglobe.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AndroidSensorProvider(context: Context) : SensorProvider {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private val _data = MutableStateFlow(SensorData.Zero)
    override val data = _data

    private val listener = object : TriggerEventListener() {
        override fun onTrigger(triggerEvent: TriggerEvent?) {
            triggerEvent?.takeIf { it.values.size >= 3 } ?: return
            _data.update {
                SensorData(
                    x = triggerEvent.values[0],
                    y = triggerEvent.values[1],
                    z = triggerEvent.values[2],
                )
            }
        }
    }

    override fun start() {
        sensorManager.requestTriggerSensor(listener, sensor)
    }

    override fun stop() {
        sensorManager.cancelTriggerSensor(listener, sensor)
    }
}