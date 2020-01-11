var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var wishSchema = new Schema({
	url:{
		type: String
	},
	lat:{
		type: String
	},
	lng:{
		type: String
	}
});

module.exports = mongoose.model('wish', wishSchema);
