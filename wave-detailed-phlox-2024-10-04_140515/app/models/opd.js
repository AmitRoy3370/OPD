const mongoose = require('mongoose');

const opdSchema = new mongoose.Schema({
  
  patientType : String,
  attendanceDate : String,
  diseasesType : String,
  diagonisis : String,
  diagonisis1 : String,
  diagonisis2 : String,
  age : Number,
  sex : String
  
}, {collection : 'OPD'});

module.exports = mongoose.model('OPD', opdSchema , 'OPD');

