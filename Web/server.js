var express 			= require('express'),
	app 				= express(),
	socketio 			= require('socket.io'),
	bodyParser 			= require('body-parser'),
	http 				= require('http'),
	mongoose			= require('mongoose'),
	port                = process.env.PORT || 3000,
	heartRateController = require('./server/controllers/heartRate-controller');

// introduce your own MongoDB deployment URI, local or remote
var uri = "yourMongoDBUri";

// Database connection
mongoose.connect(uri, function (err, res){
	if (err) console.log('ERROR: Connecting to the DB: ' + err);
	else console.log('Connection to DB established successfully');
});

// Serve static content for the app from the “public” directory in the application directory:
app.use(express.static(__dirname + '/public'));
app.use('/bower_components',  express.static(__dirname + '/bower_components'));
app.use('/node_modules',  express.static(__dirname + '/node_modules'));

// Middleware to handle properly POST requests
app.use(bodyParser());

// Attach Socket.io
var server = http.createServer(app);
var io = socketio.listen(server);
app.set('socketio', io);
app.set('server', server);


// Application routes
app.get('/', function (req, res) {
	res.sendFile(__dirname + '/public/index.html');
});

//Post method to send heart rate values to the server
app.post('/api/heartRate', heartRateController.postHeartRateValue);
app.post('/api/location', heartRateController.postLocation);


app.get('server').listen(port, function(){
	console.log('Server ready, listening in port 3000');
});
