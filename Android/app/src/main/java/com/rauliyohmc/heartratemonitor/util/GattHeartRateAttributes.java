package com.rauliyohmc.heartratemonitor.util;

import java.util.HashMap;

/**
 * Created by rauliyohmc on 07/03/15.
 */
public class GattHeartRateAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    // Heart Rate Service UUIDs
    public static String UUID_HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";
    public static String UUID_HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String UUID_BODY_SENSOR_LOCATION = "00002a38-0000-1000-8000-00805f9b34fb";
    public static String UUID_HEART_RATE_CONTROL_POINT = "00002a39-0000-1000-8000-00805f9b34fb";
    // Descriptor for enabling notification on HEART_RATE_MEASUREMENT characteristic
    public static String UUID_CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    // Battery Service
    public static String UUID_BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    public static String UUID_BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";


    static {

        /**
         * Heart Rate device GATT services
         */
        attributes.put(UUID_HEART_RATE_SERVICE, "Servicio cardiovascular");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Servicio de info del dispositivo");
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Acceso genérico");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Atributo general");
        attributes.put(UUID_BATTERY_SERVICE, "Servicio de batería");


        /**
         * Heart Rate device GATT characteristics
         */

        // Generic Access
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Nombre del dispositivo");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Apariencia");
        attributes.put("00002a02-0000-1000-8000-00805f9b34fb", "Peripheral Privacy Flag");
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred ConParam");


        // Heart Rate Service
        attributes.put(UUID_HEART_RATE_MEASUREMENT, "Medida del ritmo cardíaco");
        attributes.put(UUID_BODY_SENSOR_LOCATION, "Ubicación del sensor");
        attributes.put(UUID_HEART_RATE_CONTROL_POINT, "Punto de control");

        // Device Information Service
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Nombre del manufacturador");
        attributes.put("00002a24-0000-1000-8000-00805f9b34fb", "Numero de modelo");
        attributes.put("00002a26-0000-1000-8000-00805f9b34fb", "Revisión del firmware");
        attributes.put("00002a27-0000-1000-8000-00805f9b34fb", "Revisión del hardware");
        attributes.put("00002a2a-0000-1000-8000-00805f9b34fb", "Lista de datos de registro");
        attributes.put("00002a50-0000-1000-8000-00805f9b34fb", "PnP ID");


        // Battery Service
        attributes.put(UUID_BATTERY_LEVEL, "Nivel de batería");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
