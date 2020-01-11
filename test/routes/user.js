var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
router.use(bodyParser.json());
router.use(bodyParser.urlencoded({ extended: true }));

var mongoose = require ('mongoose')
mongoose.connect("mongodb://localhost:27017/test");
var db = mongoose.connection;
db.on('error', ()=>console.log('connection failed'));
db.once('open', ()=>console.log('connected'));

const userSchema = require('../models/user').schema; 
var user = mongoose.model('user', userSchema);

/* GET users listing. */
router.get('/', function(req, res, next) {
	console.log("get");
	user.find(function(err, users){
		if(err) return res.status(500).json({error: err});
		else return res.status(200).json(users);
	});
});

/* GET new user */
router.get('/null',(req, res)=>{
	console.log("get/null");
	var newUser = new user();
	newUser.save(function(err, data){
		if(err) return res.status(500).json({error: err});
		else return res.status(200).json(newUser);
	});
});

/* GET user */ 
router.get('/:id',(req, res)=>{
	console.log("get/id");
  const iId = req.params.id || '';
	if (!iId) return res.status(400).json({error: 'Incorrect id'});
	user.findOne({_id: iId}, function(err, user){
		if(err) return res.status(500).json({error: err});
		if(!user) return res.status(404).json({error: 'Unkown user'});
		else return res.status(200).json(user);
	});
});

/* DELETE */
router.delete('/:id', (req, res) => {
  const iId = req.params.id || '';
  if (!iId) return res.status(400).json({error: 'Incorrect id'});
	user.remove({_id: iId}, function(err, output){
		if(err) return res.status(500).json({error: err});
		if(output.n==0) return res.status(404).json({error: 'Unkown user'})
		return res.status(204).end();
	});
});

/* ADD user */
//router.post('/', (req, res) => {
//	newUser = new user();
//	return res.send(newUser._id)
//});

module.exports = router;
