package business;

import DBSingleton.*;
import java.util.*;
import components.data.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.text.*;
import java.lang.*;
import java.time.*;


import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.InputSource;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class business {
     
      // Initialize objects
      
      DBSingleton dbSingleton;
      Patient patient = null;
      Phlebotomist phleb = null;
      PSC psc = null;
      
      //===========Initialize database==============
      
     public DBSingleton initialize(){
         dbSingleton = DBSingleton.getInstance();
         dbSingleton.db.initialLoad("LAMS");
         
         return dbSingleton; 
         
     }
     
            
     //=============Get all appointments==================
      
     public String getAllAppointmentsBusiness(DBSingleton dbSingleton) {
        // System.out.println(dbSingleton);
        List<Object> objs = dbSingleton.db.getData("Appointment", "");
        
        String tempS= fetchAllAppointments(objs);
        return (tempS);        
        
        }

        
        //============ Get appointment based on Appointment ID
        
        public String getAppointmentBusiness(String aptid,DBSingleton dbSingleton){
        
        String aid = "id='"+aptid+"'";
        
        List<Object> objs = dbSingleton.db.getData("Appointment", aid);
        
        String tempS2= fetchAllAppointments(objs);

        return (tempS2);        
        
        }


        //============= Get single patient appointment===========
        
        public List<Object> getPatientAppointment(String aptid, DBSingleton dbSingleton){
        
        String aid = "patientid='"+aptid+"'";
        
        List<Object> obj = dbSingleton.db.getData("Appointment", aid);
        
        //System.out.println(obj);
        return (obj);        
        
        }
        
        //============ Query on appointment database========
        
        public List<Object> getQAppointment(String query, DBSingleton dbSingleton){
        
                
        List<Object> obj = dbSingleton.db.getData("Appointment", query);
        
        //System.out.println(obj);
        return (obj);        
        
        }
        
        //========== Convert object to XML String ==============

        public String fetchAllAppointments(List<Object> o1){
        
        String XMLString="";
        
        String ParentNode = "<AppointmentList></AppointmentList>";

        
        try{
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         
                  
         Document doc = dBuilder.parse(new InputSource(new StringReader(ParentNode)));
         
         
         //Optional but recommended
         doc.getDocumentElement().normalize();
         
         //Root Element
         Element element = doc.getDocumentElement();

        
        String date = "";
        String id="";
        String time="";
        String pid="";
        String pname="";
        String Address="";
        String pInsurance="";
        String pDob="";
        String phbmtId="";
        String phbmtName="";
        String pscId="";
        String pscName="";
        String AppId="";
        String dxCode="";
        String labTestId="";

        
        for(Object o2: o1){
        id=((Appointment)o2).getId();
        date=((Appointment)o2).getApptdate().toString();
        time=(((Appointment)o2).getAppttime()).toString();
        pname=(((Appointment)o2).getPatientid()).getName();
        pid=(((Appointment)o2).getPatientid()).getId();
         
         Address = (((Appointment)o2).getPatientid()).getAddress();
         pInsurance=Character.toString((((Appointment)o2).getPatientid()).getInsurance());
         pDob=((((Appointment)o2).getPatientid()).getDateofbirth()).toString();
         
         //phlebotomist
         phbmtId=(((Appointment)o2).getPhlebid()).getId();
         phbmtName=(((Appointment)o2).getPhlebid()).getName();
         
         
         //PSC
         
         pscId=(((Appointment)o2).getPscid()).getId();
         pscName=(((Appointment)o2).getPscid()).getName();
         
         //For later use
         patient = ((Appointment)o2).getPatientid();
         phleb = ((Appointment)o2).getPhlebid();
         psc = ((Appointment)o2).getPscid();

         
       //Print all to test
         
       /*
       System.out.println(id);
       System.out.println(date);
       System.out.println(time);
       System.out.println(pid);
       System.out.println(pname);
       System.out.println(Address);
       System.out.println(pInsurance);
       System.out.println(pDob);
       System.out.println(phbmtId);
       System.out.println(phbmtName);
       System.out.println(pscId); 
       System.out.println(pscName);
       
       */

         
         // Create Parent Nodes and Append Child to it
         
         Element appointmentNode = doc.createElement("appointment");
         appointmentNode.setAttribute("date",date);
         appointmentNode.setAttribute("time",time);
         appointmentNode.setAttribute("id",id);
         
         
         //Patient Element
         Element patientNode = doc.createElement("patient");
         patientNode.setAttribute("pid",pid);
         
         //Name
         Element patientNnode= doc.createElement("name");
         patientNode.appendChild(patientNnode);
         
         Text patientNtext= doc.createTextNode(pname);
         patientNnode.appendChild(patientNtext);
         
         //Address
         Element pAddress= doc.createElement("address");
         patientNode.appendChild(pAddress);
         
         Text patientAddress= doc.createTextNode(Address);
         pAddress.appendChild(patientAddress);
         
         //Insurance
         Element pInsuranceNode= doc.createElement("insurance");
         patientNode.appendChild(pInsuranceNode);
         
         Text pInsuranceText= doc.createTextNode(pInsurance);
         pInsuranceNode.appendChild(pInsuranceText);
         
         //DOB
         
         Element dobNode= doc.createElement("dob");
         patientNode.appendChild(dobNode);
         
         Text dobText= doc.createTextNode(pDob);
         dobNode.appendChild(dobText);

         //Append it to Appointment Node
         appointmentNode.appendChild(patientNode);
               
         //Phlebotomist Node with id attribute
         Element phlebotomistNode = doc.createElement("phlebotomist");
         phlebotomistNode.setAttribute("id",phbmtId);
               
         //Name and Value to PHBMT
         Element phlebotomistName = doc.createElement("name");
         phlebotomistNode.appendChild(phlebotomistName);
         
         Text phbmtNameText = doc.createTextNode(phbmtName);
         phlebotomistName.appendChild(phbmtNameText);
               
         //Phlebotomist append to Appointment node
         appointmentNode.appendChild(phlebotomistNode);
         
         
         //PSC Node with id attribute
         Element pscNode = doc.createElement("psc");
         pscNode.setAttribute("id",pscId);
               
         //Name and Value to PSC
         Element PSCName = doc.createElement("name");
         pscNode.appendChild(PSCName);
         
         Text pscNameText = doc.createTextNode(pscName);
         PSCName.appendChild(pscNameText);
               
         //PSC append to Appointment node
         appointmentNode.appendChild(pscNode);
         
         //create allLabTests node
         Element allLabTestsNode = doc.createElement("allLabTests");

         
       //==========Loop for Lab Tests===============//
       List<AppointmentLabTest> alt = ((Appointment)o2).getAppointmentLabTestCollection();
       for(int i=0;i<alt.size();i++){
       labTestId=alt.get(i).getAppointmentLabTestPK().getLabtestid();
       AppId=alt.get(i).getAppointmentLabTestPK().getApptid();
       dxCode=alt.get(i).getAppointmentLabTestPK().getDxcode();
        
      Element altNode = doc.createElement("appointmentLabTest");
      altNode.setAttribute("appointmentId",AppId);
      altNode.setAttribute("dxcode",dxCode);
      altNode.setAttribute("labTestId",labTestId);
      
      //APPEND TO PARENT ALT
      allLabTestsNode.appendChild(altNode);
      
       
       
       }//Loop ends here
       
       //alt append to Appointment node
       appointmentNode.appendChild(allLabTestsNode);
       
       //Append all to Root Element
        element.appendChild(appointmentNode);
        
        }//Outer Loop ends here
       
       
       //Now finalize to write all XML into string
       
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
      
      transformer.setOutputProperty(OutputKeys.INDENT,"yes");
      DOMSource source = new DOMSource(doc);
      
      // to a string
      StringWriter writer = new StringWriter();
      StreamResult result2= new StreamResult(writer);
      transformer.transform(source,result2);
      
      XMLString= writer.toString();
       
       
       //Print final output
       /*
       System.out.println();       
       System.out.println(XMLString);
       System.out.println();    
       */

       }
       catch(Exception e){
       System.out.println(e);
       e.printStackTrace();
       }
  
      return XMLString; 
       }//fetch all appointment ends here return XML String
       
             
      //===========Return value by tag name==================
      
      public String returnTagValue(String XML, String tagName){
      String result="";
      try{
        DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder= dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(XML)));
        doc.getDocumentElement().normalize();
        NodeList nlist= doc.getElementsByTagName(tagName);
        result = nlist.item(0).getTextContent();         
        }
        catch(Exception e){
        
        System.out.println(e);
        }   
        return result;
      }

      //==============Add a new appointment===================
        public String addAppointmentBusiness(String xml,DBSingleton dbSingleton){
        
        String result="";
        Random rand=new Random();
        int randomNum = rand.nextInt((10000 - 1000) + 1) + 1000;
        
        String aptId=Integer.toString(randomNum);
        //System.out.println(aptId);
        
        String aptDate=returnTagValue(xml,"date");
       // System.out.println(aptDate);
       
        String aptTime=returnTagValue(xml,"time");
       // System.out.println(aptTime);
       
        String PatientId=returnTagValue(xml,"patientId");
       // System.out.println(PatientId);
       
        String physianId=returnTagValue(xml,"physicianId");
       // System.out.println(physianId);
       
        String pscid=returnTagValue(xml,"pscId");
       // System.out.println("PSC : "+pscid);
       
        String phlebotomistId=returnTagValue(xml,"phlebotomistId");
       // System.out.println("PHB :"+phlebotomistId);
       
       
        
        //Check date and time validity
        int Flag=1;
        boolean valid=true,valid2=true;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("kk:mm:ss");
          
         Date date =null;
         Date time=null;
         try{
          
         date = formatter.parse(aptDate);
         time = formatter2.parse(aptTime);
         
         String temptime1="08:00:00";
         String temptime2="17:00:00";
         
         Date time1=formatter2.parse(temptime1);
         Date time2=formatter2.parse(temptime2);
         
         //Check if request falls between 8am to 5pm
         //System.out.println(time.after(time1));
         //System.out.println(time.before(time2));
         if(time.after(time1) && time.before(time2)){
         
         }
         else{
         
         Flag=0;
         }
                
         valid=aptDate.equals(formatter.format(date));
         
                  
         valid2=aptTime.equals(formatter2.format(time));
         
         if((valid==false) || (valid2==false)){
         Flag=0;
         }

         }catch(ParseException e){
         e.printStackTrace();
         }
         
        //Check if All Id's exist
        try{
        patient=getPatientObject(PatientId,dbSingleton);
        //System.out.println(patient);
        
        phleb=getPhlebObject(phlebotomistId,dbSingleton);
        //System.out.println(phleb);

        psc=getPscObject(pscid,dbSingleton);
        //System.out.println(psc);
        }
        catch(Exception e){
        //System.out.println("Invalid id");
        Flag=0;
        //System.out.println(e);
        }
        
        //Check for duplicate
        String Pdate="",Ptime="";
        List<Object> PObj1= getPatientAppointment(PatientId,dbSingleton);
        for(Object PObj : PObj1){
        Pdate=((Appointment)PObj).getApptdate().toString();
        Ptime=(((Appointment)PObj).getAppttime()).toString();
        }
        
        /* Debugging
        System.out.println("***************");
        System.out.println(Pdate);
        System.out.println(aptDate);
        System.out.println(Ptime);
        System.out.println(aptTime);
        System.out.println("***************");
        */
        
        
        if(Pdate.equals(aptDate) && Ptime.equals(aptTime)){
        
        Flag=0;
        
        }
        
       //Check availability for Appointment
       
       //Setting Up the interval
       String newTime1="",newTime2="";
       try{
       SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
       SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
       Date d1 = df1.parse(aptTime);
       Date d2 = df2.parse(aptTime);
       Calendar cal1 = Calendar.getInstance();
       Calendar cal2 = Calendar.getInstance();
       cal1.setTime(d1);
       cal2.setTime(d2);
       cal1.add(Calendar.MINUTE, -30);
       cal2.add(Calendar.MINUTE, 30);
       newTime1 = df1.format(cal1.getTime());
       newTime2 = df2.format(cal2.getTime());
       }
       catch(Exception e){
       System.out.println("Error in Setting up time interval : "+e);
       }
       
       //Fetching all appointments for same requirement to check for availability
       SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
       String d1="",d2="",dToTest="";
       d1 = newTime1;
       d2 = newTime2;
       
       //String query="pscid='"+pscid+"'"+"AND phlebid='"+phlebotomistId+"'"+" AND apptdate='"+aptDate+"'";
       
       String query="phlebid='"+phlebotomistId+"'"+" AND apptdate='"+aptDate+"'";

      
       List<Object> CObj1= getQAppointment(query, dbSingleton);
       for(Object CObj : CObj1){
       
       Ptime=(((Appointment)CObj).getAppttime()).toString();
       
       //Check if time intersect within 30 minutes
             
       dToTest = Ptime;
       boolean isSplit = false, isWithin = false;
   
       Date dt1 = null, dt2 = null,  dt3 = null;
       try{
       dt1 = sdf.parse(d1);
       dt2 = sdf.parse(d2);
       dt3 = sdf.parse(dToTest);
   
       isSplit = (dt2.compareTo(dt1) < 0);
       
       //System.out.println("[split]: " +isSplit);
   
       if (isSplit)
       {
           isWithin = (dt3.after(dt1) || dt3.before(dt2));
       }
       else
       {
           isWithin = (dt3.after(dt1) && dt3.before(dt2));
       }
   
       //System.out.println("Is time within interval? " +isWithin);
       
       //If found within 30 minutes of appointments set Flag to 0
       if(isWithin==true){
       
       Flag=0;
       }
       
              
               
       
       
       }
       catch(Exception e){
       System.out.println("Error in 30 Minute Block :"+e);
       }
    
       }

       
       
        
       /*If Date and Time and ID's are valid Process
       // from here. Additionally if everything
       // is fine process the apointment.
       */
       
        if(Flag != 0){
                             
        Appointment newAppt = new Appointment(aptId,java.sql.Date.valueOf(aptDate),java.sql.Time.valueOf(aptTime));
        
        //extra steps here due to persistence api and join, need to create objects in list
        List<AppointmentLabTest> tests = new ArrayList<AppointmentLabTest>();
        
        //Lab Test Logic
        try{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document = db.parse(new InputSource(new StringReader(xml)));
        NodeList nodeList = document.getElementsByTagName("test");
        
        String temp1="",temp2="";
         
         
         for(int x=0;x<nodeList.getLength(); x++) {
           temp1=((Element)nodeList.item(x)).getAttribute("id");
           temp2=((Element)nodeList.item(x)).getAttribute("dxcode");
           
           //System.out.println(temp1);
           //System.out.println(temp2);
           
           AppointmentLabTest test = new AppointmentLabTest(aptId,temp1,temp2);
           
           String temp3="code='"+temp2+"'";
           //System.out.println(temp3);
           
           test.setDiagnosis((Diagnosis)dbSingleton.db.getData("Diagnosis", temp3).get(0));
           temp3="id='"+temp1+"'";
           
           //System.out.println(temp3);
           
           test.setLabTest((LabTest)dbSingleton.db.getData("LabTest",temp3).get(0));
           tests.add(test);
           
        }
        
        // Lab test logic ends
        
        //Added here
        newAppt.setAppointmentLabTestCollection(tests);
        
            
        newAppt.setPatientid(patient);
        
        newAppt.setPhlebid(phleb);
        
        newAppt.setPscid(psc);

        boolean good = dbSingleton.db.addData(newAppt);
        
        //System.out.println(good);
        }
        catch(Exception e){
        System.out.println("Error in setting up appointment: "+e);
        }
        
        String sTemp= getAppointmentBusiness(aptId,dbSingleton);
        return sTemp;
        }
        //If date not valid report error
        else{
        
        String error= errorXML();
        return error;
        }
        
        }//Add appointment ends here
        
        
        
        //========================get patient based on id=======================
         public Patient getPatientObject(String PId, DBSingleton dbS){
         String temp = "id='"+PId+"'";
         List<Object> tempO = dbS.db.getData("Patient", temp);
            
         Patient object = (Patient)tempO.get(0);
         return object;
         }     
         
         //==================phleb==================

         public Phlebotomist getPhlebObject(String PId, DBSingleton dbS){
         String temp = "id='"+PId+"'";
         List<Object> tempO = dbS.db.getData("Phlebotomist", temp);
            
         Phlebotomist object = (Phlebotomist)tempO.get(0);
         return object;
         }     
         
         //================psc========================

         public PSC getPscObject(String PId, DBSingleton dbS){
         String temp = "id='"+PId+"'";
         List<Object> tempO = dbS.db.getData("PSC", temp);
            
         PSC object = (PSC)tempO.get(0);
         return object;
         }     
         
         
         //================Error reporting XML function=====================
         public String errorXML(){
         String XMLString="";
         String ParentNode = "<AppointmentList></AppointmentList>";

        
        try{
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         
                  
         Document doc = dBuilder.parse(new InputSource(new StringReader(ParentNode)));
         
         
         //Optional but recommended
         doc.getDocumentElement().normalize();
         
         //Root Element
         Element element = doc.getDocumentElement();
         
         Element errorNode = doc.createElement("error");
         element.appendChild(errorNode);
         Text errorText = doc.createTextNode("ERROR:Appointment is not available");
         errorNode.appendChild(errorText);
         
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
         
         transformer.setOutputProperty(OutputKeys.INDENT,"yes");
         DOMSource source = new DOMSource(doc);
         
         // to a string
         StringWriter writer = new StringWriter();
         StreamResult result2= new StreamResult(writer);
         transformer.transform(source,result2);
         
         XMLString= writer.toString();
   
         
         
         }
         catch(Exception e){
         System.out.println(e);
         }

         return XMLString;
         
         }//ErrorXML ends
         
         //Adding Patient into database
         
        public String addPatientBusiness(String id, String name, String address, char insurance, String dob, String phId,DBSingleton dbSingleton){
        
        String aid2 = "id='"+id+"'";
        
        Object obj2= dbSingleton.db.getData("Patient",aid2);
        String check=obj2.toString();
        //System.out.println(check);
        
        //Check if patient already exist
        if(check=="[]"){

        String result="";
        Patient newObj = new Patient(id,name,address,insurance,java.sql.Date.valueOf(dob));
        
        // Add Physician Details into database
        //Check if Physician ID is correct
        String aid3 = "id='"+phId+"'";
        
        List<Object> obj3= dbSingleton.db.getData("Physician",aid3);
        check=obj3.toString();
        //System.out.println(check);
        
        String phName=((Physician)obj3.get(0)).getName();
        if(check!="[]"){
        
        

        newObj.setPhysician(new Physician(phId,phName));
        dbSingleton.db.addData(newObj);
        
        String aid = "id='"+id+"'";
        Object objs = dbSingleton.db.getData("Patient", aid);
        
         //System.out.println(objs);
         result = objs.toString();
        

         return result;
         }
         
         else{
         
         return "Wrong Physician details";
         }// Physician Check Ends
         
         }//Patient Check Ends
         
         else{
         
         return "Patient ID already exist";
         }
         
        }//Add patient ends
        
        //=================Add Physician =================
        public String addPhysicianBusiness(String id, String name,DBSingleton dbSingleton){
         
        String result="";
        Physician newObj=null;
        
        String aid2 = "id='"+id+"'";
        
        Object obj2= dbSingleton.db.getData("Physician",aid2);
        String check=obj2.toString();
        //System.out.println(check);
        
        if(check=="[]"){
               
        newObj = new Physician(id,name);
        
        // Add Physician Details into database
        
        dbSingleton.db.addData(newObj);
        
        String aid = "id='"+id+"'";
        Object objs = dbSingleton.db.getData("Physician", aid);
        
        //System.out.println(objs);
        result = objs.toString();
        

        return result;
        }
        
        else{
        return "Physician ID already exist";
        }
         
        }//Add physician ends
    
        }