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
const wishSchema = require('../models/wish').schema; 
var wish = mongoose.model('wish', wishSchema);

/* GET wish listing. */
router.get('/:id',(req, res)=>{
  const id = req.params.id || '';
	if (!id) return res.status(400).json({error: 'Incorrect id'});
	user.findOne({_id: id}, function(err, user){
		if(err) return res.status(500).json({error: err});
		if(!user) return res.status(404).json({error: 'Unkown user'});
		else return res.status(200).json(user.wishList);
	});
});

/* DELETE */
router.delete('/:id/:url', (req, res) => {
  const id = req.params.id || '';
  if (!id) return res.status(400).json({error: 'Incorrect id'});
  const url = req.params.url || '';
  if (!url) return res.status(400).json({error: 'Incorrect url'});
	user.findOne({_id: id}, function(err, user){
		if(err) return res.status(500).json({error: err});
		if(!user) return res.status(404).json({error: 'Unkown user'});
		else {
			user.wishList = user.wishList.fillter(function (n){
			  return n.url != url;
		 	});
			user.save();
			return res.status(204).end();
		}
	});
});

/* ADD wish */
router.post('/:id', (req, res) => {
  const iWish = JSON.parse(req.body.wish) || []; 
	//iWish = iWish[0];  //if not work, add this code and input wish set jsonarray
  const id = req.params.id || ''; 
  if (!id.length) return res.status(400).json({error: 'Incorrenct id'});
  userModel.findOne({_id: id}, function(err, user){
    if(!user) return res.status(404).json({error: 'Unkown user'});
    if(err) return res.status(500).json({error: err});
    else{
			var check=0;
			for(var i=0, wish; wish = user.wishList[i]; i++){
				if(wish.url == iWish.url) check += 1;
			}   
			if(! check) user.wishList.push(iWish);
      user.save();
      return res.status(201).json(user.wishList || '');
    }   
  }); 
});

module.exports = router;
