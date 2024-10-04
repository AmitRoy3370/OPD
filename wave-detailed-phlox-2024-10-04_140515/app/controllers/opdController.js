const OPD = require('../models/opd');

const OPDPatientData = require('../models/opdPatientData');

const Patient = require('../models/patient');

exports.addOPD = async (req, res)=>{
  
  console.log('received data :- ', req.body);
  
  const opd = new OPD(req.body);
  
  try {
    
    await opd.save();
    
    res.status(200).send(opd);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(400).send(error);
    
  }
  
};

exports.getAllOPD = async (req, res)=>{
  
  try {
    
    const opdData = await OPD.find();
    
    console.log(opdData);
    
    res.status(201).send(opdData);
    
  } catch(error) {
    
    res.status(404).send(error);
    
    console.log(error);
    
  }
  
};

exports.updateOPD = async (req, res) => {

  console.log('request :- ', req.body);

  const { _id, updatedFields } = req.body;  

  try {
    
    if (!_id) {
      return res.status(400).send('OPD _id is required to identify the patient.');
    }

    
    if (!updatedFields || Object.keys(updatedFields).length === 0) {
      return res.status(400).send('No fields provided to update.');
    }

    
    const updatedPatient = await OPD.findByIdAndUpdate(
      _id,
      { $set: updatedFields },  
      { new: true }             
    );

   
    if (!updatedPatient) {
      return res.status(404).send('No OPD found with the provided _id.');
    }

    //const newFullName = `${updatedPatient.firstName} ${updatedPatient.lastName}`;

    const result = await OPDPatientData.updateMany(
      { opdId: updatedPatient._id },    
      { $set: { diseasesType: updatedPatient.diseasesType,
                status : updatedPatient.patientType } }   
    );
    
    res.status(200).send({ message: 'OPD updated successfully', opd: updatedPatient });

  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }

};

// Search OPD data by ID
exports.searchOPD = async (req, res) => {
  const { _id } = req.query;  

  try {
    
    console.log('_id :- ', req.query);
    
    const opdData = await OPD.findById(_id);
    
    console.log('sending opd data :- ', opdData);

    
    if (!opdData) {
      return res.status(404).send('No OPD found with the provided _id.');
    }

    
    res.status(200).send(opdData);

  } catch (error) {
    console.log(error);
    res.status(500).send('Error retrieving OPD data.');
  }
};

exports.deleteOPD = async (req, res) => {
  const { _id } = req.body;  

  try {
    
    if (!_id) {
      return res.status(400).send('OPD _id is required to delete the record.');
    }
    
    const deletedOPD = await OPD.findByIdAndDelete(_id);
   
    if (!deletedOPD) {
      return res.status(404).send('No OPD found with the provided _id.');
    }
    
    const deletedPatients = await OPDPatientData.deleteMany({ opdId: _id });

    res.status(200).send({ 
      message: 'OPD and related OPDPatientData records deleted successfully', 
      deletedOPD: deletedOPD,
      deletedPatients: deletedPatients.deletedCount 
    });

  } catch (error) {
    console.log(error);
    res.status(500).send('Error deleting OPD record.');
  }
};
