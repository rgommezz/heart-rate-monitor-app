var mongoose = require('mongoose'),
	  Schema 	 = mongoose.Schema;

var databaseSchema = new Schema({
	day: String,
	time: String,
	values: [{
		emitting: {type: String},
		value: {type: String}
	}]
});

module.exports = mongoose.model('storageModel', databaseSchema);