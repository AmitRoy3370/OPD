const express = require('express');
const mongoose = require('mongoose');
const app = express();

app.use(express.json());

const url = 'mongodb+srv://arponamitroy012:jqaPUF3LNhjpAbR7@cluster0.js0bs.mongodb.net/Students?retryWrites=true&w=majority';

mongoose.set('strictQuery', true);

mongoose.connect(url/*, {useNewUrlParser : true, useUnifiedTopology : true}*/)
.then(()=>{console.log('Connected with mongodb database')})
.catch(error=>{console.log('Database is not connected due to this error ', error)});

const opdController = require('./controllers/opdController');
const opdPatientDataController = require('./controllers/opdPatientDataController');
const patientController = require('./controllers/patientController');

app.post('/opd', opdController.addOPD);
app.get('/opd/all', opdController.getAllOPD);
app.post('/opd/update', opdController.updateOPD);
app.get('/opd/search', opdController.searchOPD);
app.post('/opd/delete', opdController.deleteOPD);

app.post('/patient', patientController.addPatient);
app.get('/patient/all', patientController.readAllPatient);
app.get('/patient/search', patientController.searchPatientByFirstName);
app.get('/patient/searchByLastName', patientController.searchPatientByLastName);
app.post('/patient/delete', patientController.deletePatient);
app.post('/patient/update', patientController.updatePatient);

app.post('/opdpatientdata', opdPatientDataController.addOPDPatientData);
app.get('/opdpatientdata/all', opdPatientDataController.readAllOPDPatientData);
app.get('/opdpatientdata/search', opdPatientDataController.searchOPDPatientData);

const port = process.env.PORT || 3000;

app.listen(port, ()=>{
  
  console.log('Server is running on port ', port);
  
});
