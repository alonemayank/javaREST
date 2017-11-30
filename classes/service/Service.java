package service;

import javax.ws.rs.core.*;
import javax.ws.rs.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import DBSingleton.*;
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
import business.*;

@Path("Services")

public class Service{
   
   business b1 = new business();
   DBSingleton dbSingleton;
   
   public void Service(){
   dbSingleton = b1.initialize();
   }
   
   
   //Initialize the database
   @Context
   private UriInfo context;

   @GET
   @Produces("application/xml")
   public String initialize(){
      
         dbSingleton = b1.initialize();
         String temp11="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><AppointmentList><intro>Welcome to the LAMS Appointment Service</intro><wadl>"+this.context.getBaseUri().toString()+"application.wadl</wadl></AppointmentList>";
         return temp11;
         }
   //Get all appointments
   
   @Path("Appointments")
   @GET
   @Produces("application/xml")
   @Consumes("text/plain")      
   public String getAllAppointments(){
        DBSingleton dbSingleton = DBSingleton.getInstance();
         String result= b1.getAllAppointmentsBusiness(dbSingleton);
   
         return result;
         }
   
   //Get appointment based on appointment ID
   @Path("Appointments/{id}")
   @GET
   @Produces("application/xml")
   @Consumes("text/plain")
   public String getAppointment(@PathParam("id") String aptid){
   DBSingleton dbSingleton = DBSingleton.getInstance();
   String result= b1.getAppointmentBusiness(aptid,dbSingleton);
   return result;
   }
   
   //Add appointment based on XML
   @Path("Appointments")
   @PUT
   @Produces("application/xml")
   @Consumes({"text/xml","application/xml"})
   public String addAppointment(String xml){
   DBSingleton dbSingleton = DBSingleton.getInstance();
   String result= b1.addAppointmentBusiness(xml,dbSingleton);
   return result;
   }
   
  
   //Add new patient
   @Path("AddPatient")
   @PUT
   @Produces("application/xml")
   @Consumes({"text/xml","application/xml"})
   public String addPatient(String xml){
   DBSingleton dbSingleton = DBSingleton.getInstance();

      String result="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><status>Please Check Your Input</status>";
      String id="";
         String name="";
         String address="";
         char insurance='y';
         String dob="";
         String phId="";
       try{
       
        DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder= dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();
        //id
        NodeList nlist= doc.getElementsByTagName("id");
        id = nlist.item(0).getTextContent();
        //name
        nlist= doc.getElementsByTagName("name");
        name = nlist.item(0).getTextContent();
        //address
        nlist= doc.getElementsByTagName("address");
        address = nlist.item(0).getTextContent();
        //insurance
        nlist= doc.getElementsByTagName("insurance");
        insurance = nlist.item(0).getTextContent().charAt(0);
        //System.out.println(insurance);
        //dob
        nlist= doc.getElementsByTagName("dob");
        dob = nlist.item(0).getTextContent();
        //phId
        nlist= doc.getElementsByTagName("phId");
        phId = nlist.item(0).getTextContent(); 
        //Send result
        //result = b1.addPatientBusiness(id,name,address,insurance,dob,phId,dbSingleton);
        result = b1.addPatientBusiness("855","XYZ","address",'y',"1962-12-19","20",dbSingleton);
               
        }
        catch(Exception e){
        
        System.out.println(e);
        }
        return result; 
   
   }
   
   
   //Add new Physician
   @Path("AddPhysician")
   @PUT
   @Produces("application/xml")
   @Consumes({"text/xml","application/xml"})

   public String addPhysician(String xml){
   DBSingleton dbSingleton = DBSingleton.getInstance();

   String id=""; 
   String name="";
   String result="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><status>Please check your input</status>";
   try{
        DocumentBuilderFactory dbFactory= DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder= dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();
        //id
        NodeList nlist= doc.getElementsByTagName("id");
        id = nlist.item(0).getTextContent();
        //name
        nlist= doc.getElementsByTagName("name");
        name = nlist.item(0).getTextContent();
         
        //Send result
        result = b1.addPhysicianBusiness(id, name,dbSingleton );
        }
        catch(Exception e){
        
        System.out.println(e);
        }

      return result;
   }
   
}