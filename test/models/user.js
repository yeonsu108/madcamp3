const mongoose = require ('mongoose');
const wishSchema = require('./wish').schema;
const Schema = mongoose.Schema;
/**
 * Create Schema
 * https://stackoverflow.com/questions/43024285/embedding-schemas-is-giving-error/43024503
 */
const userSchema = new Schema({
    wishList: [{
        type: wishSchema
    }]
});
module.exports = mongoose.model ('user', userSchema);
