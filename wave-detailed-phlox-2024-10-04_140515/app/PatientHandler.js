const express = require('express');
const mongoose = require('mongoose');
const app = express();

const patientSchema = new mongoose.Schema({
  
  firstName : String,
  lastName : String,
  adress : String,
  city : String,
  age : Number,
  sex : String
  
}, {collection : 'Patient'});

const Patient = mongoose.model('Patient', patientSchema, 'Patient');

app.post('/patient', async (req, res)=>{
  
  console.log('received data ;- ', req.body);
  
  try {
    
    const patient = new Patient(req.body);
    
    await patient.save();
    
    res.status(200).send(patient);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(400).send(error);
    
  }
  
});

app.get('/patient/all', async(req, res)=>{
  
  try {
    
    const patient = await Patient.find();
    
    console.log(patient);
    
    res.status(201).send(patient);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(404).send(error);
    
  }
  
});

app.get('/patient/search', async (req, res)=>{
  
  const {firstName} = req.query;
  
  try {
    
    if(!firstName) {
      
      return res.status(400).send('firstName query parameter is required');
      
    }
    
    const patient = await Patient.find({firstName : new RegExp(firstName, 'i')});
    
    res.status(200).send(patient);
    
    console.log(patient);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(500).send(error);
    
  }
  
});


app.get('/patient/searchByLastName', async (req, res)=>{
  
  const {lastName} = req.query;
  
  try {
    
    if(!lastName) {
      
      return res.status(400).send('lastName query parameter is required');
      
    }
    
    const patient = await Patient.find({lastName : new RegExp(lastName, 'i')});
    
    res.status(200).send(patient);
    
    console.log(patient);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(500).send(error);
    
  }
  
});

app.post('/patient/delete', async (req, res)=>{

  console.log('request :- ',req.body);
  
  const {firstName, lastName, adress, city, age, sex} = req.body;
  
  try {
    
    if(!firstName || !lastName || !sex || !adress || !city || !age) {
      
      return res.status(400).send('all parameters need to required.');
      
    }
    
    const deletedPatient = await Patient.findOneAndDelete({
            firstName: firstName,
            lastName: lastName,
            sex: sex,
            adress: adress,
            city: city,
            age: age
    });

    if (!deletedPatient) {
        
      return res.status(404).send('No matching patient found to delete');
    
    }

    res.status(200).send({ message: 'Patient deleted successfully', patient: deletedPatient });
    
  } catch(error) {
    
    console.log(error);
    
    res.status(500).send(error);
    
  }
  
});

app.post('/patient/update', async (req, res) => {

  console.log('request :- ', req.body);

  const { _id, updatedFields } = req.body;  // Extract _id and updated fields from the request

  try {
    // Check if _id is provided
    if (!_id) {
      return res.status(400).send('Patient _id is required to identify the patient.');
    }

    // Check if there are fields to update
    if (!updatedFields || Object.keys(updatedFields).length === 0) {
      return res.status(400).send('No fields provided to update.');
    }

    // Find the patient by _id and update the fields
    const updatedPatient = await Patient.findByIdAndUpdate(
      _id,
      { $set: updatedFields },  // Fields to update
      { new: true }             // Return the updated document
    );

    // If no patient was found with the given _id
    if (!updatedPatient) {
      return res.status(404).send('No patient found with the provided _id.');
    }

    // Return success with the updated patient data
    res.status(200).send({ message: 'Patient updated successfully', patient: updatedPatient });

  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }

});

