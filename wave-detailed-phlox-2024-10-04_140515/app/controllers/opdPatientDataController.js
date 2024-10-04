const Patient = require('../models/patient');

const OPD = require('../models/opd');

const OPDPatientData = require('../models/opdPatientData');

exports.addOPDPatientData = async (req, res) => {
  
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
};

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

exports.readAllOPDPatientData = async (req, res)=>{
  
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
  
};

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

exports.searchOPDPatientData = async (req, res) => {
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
    
    /*const sendingInformation = filteredData.map(result => {
      const indexes = allData.findIndex(item => item._id.toString() === result._id.toString());
      return { ...result.toObject(), index: indexes + 1 }; // Add index if needed
    });*/
    
    //console.log('sending :- ',sendingInformation);
    
    //res.status(200).send(sendingInformation);
  } catch (error) {
    console.log(error);
    res.status(500).send('Error retrieving data');
  }
};

function formatDateString(dateStr) {
  const date = new Date(dateStr);
  return !isNaN(date) ? date.toISOString().split('T')[0] : null;  
}
