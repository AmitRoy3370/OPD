const mongoose = require('mongoose');

const opdPatientSchema = new mongoose.Schema({
  
      patNo: Number,
      patientId: String,
      fullName: String,
      sex: String,
      age: Number,
      diseases: String,
      diseasesType: String,
      admissionDate: String,
      status: String,
      opdId: String,
      opdNo: Number
  
}, {collection : 'OPDPatientData'});

module.exports = mongoose.model('OPDPatientData', opdPatientSchema, 'OPDPatientData');

