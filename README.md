# heart-rate-monitor
Master Thesis project, a heart rate monitoring system, composed of an Android and Web applications working together as a whole.

The Android application acts as a bridge, providing an interface to connect your personal heart rate Bluetooth 4.0 sensor,receive data from it and transfer it to a personalised web server. It will also send location info.

The web server will communicate with the browser via Web Socket connection to send real-time heart rate and location values.

The web client will be able to monitor that value as well as showing a graph with the latest values.
Notification system included to notified via email when certain conditions take place.

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

