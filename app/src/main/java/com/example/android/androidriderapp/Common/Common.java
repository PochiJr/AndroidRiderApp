package com.example.android.androidriderapp.Common;

import com.example.android.androidriderapp.Remote.FCMClient;
import com.example.android.androidriderapp.Remote.IFCMService;

/**
 * Created by jesus on 04/07/2018.
 */

public class Common {
     public static final String driver_tbl = "Drivers"; // Aquí se almacena la ubicación de los conductores
    public static final String driver_info_tbl = "DriversInformation"; // Aquí se almacenan los datos de los conductores
    public static final String rider_info_tbl = "RidersInformation"; // Aquí se almacenan los datos de los pasajeros
    public static final String pickup_request_tbl = "PickupRequest"; // Aquí se almacenan los datos de las peticiones de recogida
    public static final String token_tbl = "Tokens";

    public static final String fcmURL = "https://fcm.googleapis.com/";

    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }
}
