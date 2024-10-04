const mongoose = require('mongoose');

const patientSchema = new mongoose.Schema({
  
  firstName : String,
  lastName : String,
  adress : String,
  city : String,
  age : Number,
  sex : String
  
}, {collection : 'Patient'});

module.exports = mongoose.model('Patient', patientSchema, 'Patient');

