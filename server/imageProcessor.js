const path = require('path');
const multer = require('multer');

const storage = multer.diskStorage({
    destination: './public/uploads/',
    filename: (req, file, callback) =>
        callback(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname))
});

function checkFileType(file, cb) {
    const filetypes = /jpeg|jpg|png|gif/;
    const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
    const mimetype = filetypes.test(file.mimetype);
    if(mimetype && extname){
        return cb(null,true);
    } else {
        cb('Error: Images Only!');
    }
}

exports.upload = multer({
    storage: storage,
    limits:{ fileSize: 5 * 1024 * 1024 },
    fileFilter: function(req, file, cb) {
        checkFileType(file, cb);
    }
}).single('myImage');
