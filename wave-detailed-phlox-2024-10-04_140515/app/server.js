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

/*const opdSchema = new mongoose.Schema({
  
  patientType : String,
  attendanceDate : String,
  diseasesType : String,
  diagonisis : String,
  diagonisis1 : String,
  diagonisis2 : String,
  age : Number,
  sex : String
  
}, {collection : 'OPD'});

const patientSchema = new mongoose.Schema({
  
  firstName : String,
  lastName : String,
  adress : String,
  city : String,
  age : Number,
  sex : String
  
}, {collection : 'Patient'});

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

const OPD = mongoose.model('OPD', opdSchema , 'OPD');

const Patient = mongoose.model('Patient', patientSchema, 'Patient');

const OPDPatientData = mongoose.model('OPDPatientData', opdPatientSchema, 'OPDPatientData');

app.post('/opd', async (req, res)=>{
  
  console.log('received data :- ', req.body);
  
  const opd = new OPD(req.body);
  
  try {
    
    await opd.save();
    
    res.status(200).send(opd);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(400).send(error);
    
  }
  
});

app.get('/opd/all', async (req, res)=>{
  
  try {
    
    const opdData = await OPD.find();
    
    console.log(opdData);
    
    res.status(201).send(opdData);
    
  } catch(error) {
    
    res.status(404).send(error);
    
    console.log(error);
    
  }
  
});

app.post('/opd/update', async (req, res) => {

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

});

// Search OPD data by ID
app.get('/opd/search', async (req, res) => {
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
});


app.post('/opd/delete', async (req, res) => {
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
});

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
    
    const deletedOPDPatientData = await OPDPatientData.deleteMany({
      
      patientId : deletedPatient._id
      
    });

    res.status(200).send({ message: 'Patient and relted field is deleted successfully', patient: deletedPatient });
    
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

    
    const updatedPatient = await Patient.findByIdAndUpdate(
      _id,
      { $set: updatedFields },  
      { new: true }             
    );

   
    if (!updatedPatient) {
      return res.status(404).send('No patient found with the provided _id.');
    }

    const newFullName = `${updatedPatient.firstName} ${updatedPatient.lastName}`;

    const result = await OPDPatientData.updateMany(
      { patientId: updatedPatient._id },    
      { $set: { fullName: newFullName } }   
    );
    
    res.status(200).send({ message: 'Patient updated successfully', patient: updatedPatient });

  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }

});

app.post('/opdpatientdata', async (req, res) => {
  
  const { patientId, opdId, diseases, diseasesType, admissionDate, status } = req.body;

  try {
    
    const patient = await Patient.findById(patientId);
    if (!patient) {
      return res.status(404).send('Patient not found');
    }

   
    const opd = await OPD.findById(opdId);
    if (!opd) {
      return res.status(404).send('OPD record not found');
    }

    
    const patNo = await getPatientPosition(patientId);

    // Get OPD's position (opdNo) in the collection
    //const opds = await OPD.find().sort({ _id: 1 });
    const opdNo = await getOpdPosition(opdId);

    // Combine first name and last name for fullName
    const fullName = `${patient.firstName} ${patient.lastName}`;

    if (patient.sex !== opd.sex) {
      return res.status(400).send(`Patient's sex (${patient.sex}) does not match OPD's required sex (${opd.sex})`);
    }

    // Check if patient's age fits within the OPD's age limit (assuming OPD has minAge and maxAge fields)
    if (patient.age < opd.minAge || patient.age > opd.maxAge) {
      return res.status(400).send(`Patient's age (${patient.age}) is outside the allowed range for OPD (${opd.minAge}-${opd.maxAge})`);
    }
    
    // Prepare OPDPatientData entry
    const opdPatientData = new OPDPatientData({
      patNo: patNo,
      patientId: patient._id,
      fullName: fullName,
      sex: patient.sex,
      age: patient.age,
      diseases: diseases,
      diseasesType: diseasesType,
      admissionDate: admissionDate,
      status: status,
      opdId: opd._id,
      opdNo: opdNo
    });

    // Save the new entry
    await opdPatientData.save();
    res.status(200).send(opdPatientData);

  } catch (error) {
    console.error(error);
    res.status(500).send('Error while saving OPD patient data');
  }
});


const getPatientPosition = async (patientId) => {
  try {
    // Get all patients without sorting
    const allPatients = await Patient.find(); // No sort applied

    // Find the index of the patient by ID
    const position = allPatients.findIndex(patient => patient._id.toString() === patientId.toString());

    // Return 1-based index if found, or null if not found
    return position !== -1 ? position + 1 : null;
  } catch (error) {
    console.log('Error in getting patient position:', error);
    throw error;
  }
};

const getOpdPosition = async (opdId) => {
  try {
    // Get all OPDs without sorting
    const allOpds = await OPD.find(); // No sort applied

    // Find the index of the OPD record by ID
    const position = allOpds.findIndex(opd => opd._id.toString() === opdId.toString());

    // Return 1-based index if found, or null if not found
    return position !== -1 ? position + 1 : null;
  } catch (error) {
    console.log('Error in getting OPD position:', error);
    throw error;
  }
};

app.get('/opdpatientdata/all', async (req, res)=>{
  
  console.log('request body :- ', req.body);
  
  try {
    
    const opdPatientData = await OPDPatientData.find();
    
    const sendingInformation = opdPatientData.map(result=>{
      
        const indexes = opdPatientData.findIndex(item=>item._id.toString() === result._id.toString() );
      
        console.log('my index :- ', indexes);
      
      return {...result.toObject(), index : indexes + 1};
      
    });
    
    console.log('finding data :- ', sendingInformation);
    
    res.status(201).send(sendingInformation);
    
  } catch(error) {
    
    console.log(error);
    
    res.status(500).send(error);
    
  }
  
});

function parseDate(dateString) {
  
  if (typeof dateString === 'undefined' || typeof dateString !== 'string') {
           console.warn('admissionDate is undefined or not a string:', dateString);
           return false; // Exclude this record
  }
  
  console.log('receiving data :- ', dateString);
  
  const [weekday, monthStr, day, time, tz, year] = dateString.toString().split(" ");
  
  // Month string to number mapping
  const monthMap = {
    Jan: 0, Feb: 1, Mar: 2, Apr: 3, May: 4, Jun: 5,
    Jul: 6, Aug: 7, Sep: 8, Oct: 9, Nov: 10, Dec: 11
  };
  
  const month = monthMap[monthStr];
  
  console.log("time :- ", time);
  
  const [hours, minutes, seconds] = time.split(':').map(Number);
  
  console.log('processing date :- ', {
    day: parseInt(day, 10),
    month,
    year: parseInt(year, 10),
    time: new Date(year, month, day, hours, minutes, seconds).getTime() // Convert to timestamp
  });
  
  return {
    day: parseInt(day, 10),
    month,
    year: parseInt(year, 10),
    time: new Date(year, month, day, hours, minutes, seconds).getTime() // Convert to timestamp
  };
}

// Function to check if a date is within the given range
function isWithinDateRange(recordDate, fromDate, toDate) {
  if (!fromDate && !toDate) return true; // No range provided, always true

  const parsedRecordDate = parseDate(recordDate); // Parse record's admissionDate

  let from = fromDate ? parseDate(fromDate) : null;
  let to = toDate ? parseDate(toDate) : null;

  const recordTimestamp = new Date(parsedRecordDate.year, parsedRecordDate.month, parsedRecordDate.day).getTime();
  const fromTimestamp = from ? new Date(from.year, from.month, from.day).getTime() : null;
  const toTimestamp = to ? new Date(to.year, to.month, to.day).getTime() : null;

  // Compare recordDate with fromDate and toDate
  const isAfterFrom = fromTimestamp ? recordTimestamp >= fromTimestamp : true;
  const isBeforeTo = toTimestamp ? recordTimestamp <= toTimestamp : true;

  return isAfterFrom && isBeforeTo;
}

app.get('/opdpatientdata/search', async (req, res) => {
  const { opdNo, patNo, diseasesType, diseases, sex, dateFrom, dateTo, ageFrom, ageTo, status, codeNo } = req.query;
  
  console.log('response receive :- ',req.query);

  let query = {}; 

  
  if (codeNo) {
    try {
      
      const index = parseInt(codeNo, 10) - 1;
      const allData = await OPDPatientData.find();
 
      if (index >= 0 && index < allData.length) {
        
        const allData = await OPDPatientData.find();
        
        //const allData = await OPDPatientData.find();
    
        const sendingInformation = allData.map(result=>{
      
        const indexes = allData.findIndex(item=>item._id.toString() === result._id.toString() );
      
        console.log('my index :- ', indexes);
      
      if(index == indexes) {    
          
         return {...result.toObject(), index : indexes + 1};
      
      }
        
    });
    
    console.log('sending :- ',sendingInformation);
    
        return res.status(200).send(sendingInformation); 
      } else {
        return res.status(404).send('No record found at the specified codeNo');
      }
    } catch (error) {
      console.error(error);
      return res.status(500).send('Error processing codeNo');
    }
  }

  
  if (opdNo) {
    
    if (opdNo) {
        const parsedOpdNo = parseInt(opdNo, 10); 
    if (!isNaN(parsedOpdNo)) {
       query.opdNo = parsedOpdNo; 
    } else {
    
    }
      
   }

  }

  if (patNo) {
    
     const parsedPatNo = parseInt(patNo, 10);
  if (!isNaN(parsedPatNo)) {
    query.patNo = parsedPatNo;
  } else {
    
  }
    
   
  }

  if (diseasesType) {
    query.diseasesType = diseasesType;
  }

  if (diseases) {
    query.diseases = diseases;
  }

  if (sex) {
    query.sex = sex;
  }
  
  const monthMap = {
        Jan: 0, Feb: 1, Mar: 2, Apr: 3, May: 4, Jun: 5,
        Jul: 6, Aug: 7, Sep: 8, Oct: 9, Nov: 10, Dec: 11
    };

  
  if (dateFrom && dateTo) {
    
      try {
            // Parse the date strings
           
        } catch (error) {
            console.error('Error parsing dates:', error);
            return res.status(400).send('Error parsing dates');
        }
    
  }

  
  if (ageFrom && ageTo) {
    query.age = { $gte: ageFrom, $lte: ageTo };
  }

  if (status) {
    query.status = status;
  }

  try {

    const results = await OPDPatientData.find(query);
    
    const allData = await OPDPatientData.find();
    
    const { dateFrom, dateTo } = req.query;
    
    //const filteredData = results;
    
    if(dateFrom && dateTo) {
      
      console.log('results :- ',results);

       
      const sendingInformation = results.map(result => {
        const indexes = allData.findIndex(item => item._id.toString() === result._id.toString() && ((!dateFrom && !dateTo) || isWithinDateRange(item.admissionDate, dateFrom, dateTo) ));
        
        if(indexes >= 0) {
        
        return { ...result.toObject(), index: indexes + 1 }; // Add index if needed
          
        }
      });
      
      console.log('sending if :- ',sendingInformation);
    
      res.status(200).send(sendingInformation);
      
    } else {
      
      const sendingInformation = results.map(result => {
        const indexes = allData.findIndex(item => item._id.toString() === result._id.toString());
        return { ...result.toObject(), index: indexes + 1 }; // Add index if needed
      });
      
      console.log('sending else :- ',sendingInformation);
    
      res.status(200).send(sendingInformation);
      
    }
    
    
    
    //console.log('sending :- ',sendingInformation);
    
    //res.status(200).send(sendingInformation);
  } catch (error) {
    console.log(error);
    res.status(500).send('Error retrieving data');
  }
});

function formatDateString(dateStr) {
  const date = new Date(dateStr);
  return !isNaN(date) ? date.toISOString().split('T')[0] : null;  
}

*/

const port = process.env.PORT || 3000;

app.listen(port, ()=>{
  
  console.log('Server is running on port ', port);
  
});
