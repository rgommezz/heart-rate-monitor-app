# heart-rate-monitor
Master Thesis project, a heart rate monitoring system, composed of an Android and Web applications working together as a whole.

The Android application acts as a bridge, providing an interface to connect your personal heart rate commercial sensor through Bluetooth 4.0 Low Energy. The application will receive data from the sensor and will parse it appropiately, in order to transfer it to a web server by HTTP POST requests. It will also send location information periodically to track down the user.

The web server will communicate with the browser via Web Socket connection so as to send real-time heart rate and location values.

The web client will be able to monitor those values as well as showing a graph with the latest heart rate samples. It will also allow the user to view the current location of the person being monitorised.

Notification system included to notifiy via email when certain heart rate conditions take place.

MongoDB implemented to store every heart rate sample received and hence making possible data mining.

# Author

[Raúl Gómez Acuña](https://www.linkedin.com/in/gomezaraul)

# Director

[Álvaro Durán Martínez](http://www.ic.uma.es/contenidos/ficha_personal.action?id=766)

# Documentation

Full documentation is available together with LaTeX source (in spanish) under `/doc/` directory.

# Screenshots

![alt text](https://github.com/rauliyohmc/heart-rate-monitor-app/blob/master/doc/graphs/AndroidEscaneo.png)
![alt text](https://github.com/rauliyohmc/heart-rate-monitor-app/blob/master/doc/graphs/AndroidConectado.png)
![alt text](https://github.com/rauliyohmc/heart-rate-monitor-app/blob/master/doc/graphs/AndroidEmitiendo2.png)
![alt text](https://github.com/rauliyohmc/heart-rate-monitor-app/blob/master/doc/graphs/AndroidNotificaciones.png)

