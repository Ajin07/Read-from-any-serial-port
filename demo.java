/* This is for reading data from serial port using java. Need to setup rxtx libraries for proper run. Use netbeans*/ 

import java.io.*;
import java.util.*;
import gnu.io.*; // for rxtxSerial library
import java.sql.*;
 public class Demo implements Runnable, SerialPortEventListener {
   static CommPortIdentifier portId;
   static CommPortIdentifier saveportId;
   static Enumeration        portList;
   InputStream           inputStream;
   SerialPort           serialPort;
   Thread           readThread;
 
   
   static OutputStream      outputStream;
   static boolean        outputBufferEmptyFlag = false;
 
   public static void main(String[] args) {
      boolean           portFound = false;
      String           defaultPort;
      

       
      // determine the name of the serial port on several operating systems
      String osname = System.getProperty("os.name","").toLowerCase();
     
         defaultPort = "COM3";
      
           
      if (args.length > 0) {
         defaultPort = args[0];
      } 
 
      //System.out.println("Set default port to "+defaultPort);
       
        // parse ports and if the default port is found, initialized the reader
      portList = CommPortIdentifier.getPortIdentifiers();
      while (portList.hasMoreElements()) {
         portId = (CommPortIdentifier) portList.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(defaultPort)) {
              // System.out.println("Found port: "+defaultPort);
               portFound = true;
               // init reader thread
               Demo reader = new Demo();
            } 
         } 
          
      } 
      if (!portFound) {
         System.out.println("port " + defaultPort + " not found.");
      } 
       
   } 
 
 
 
   public Demo() {
      // initalize serial port
      try {
         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
      } catch (PortInUseException e) {}
    
      try {
         inputStream = serialPort.getInputStream();
      } catch (IOException e) {}
    
      try {
         serialPort.addEventListener(this);
      } catch (TooManyListenersException e) {}
       
      // activate the DATA_AVAILABLE notifier
      serialPort.notifyOnDataAvailable(true);
    
      try {
         // set port parameters
         serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, 
                     SerialPort.STOPBITS_1, 
                     SerialPort.PARITY_NONE);
      } catch (UnsupportedCommOperationException e) {}
       
      // start the read thread
      readThread = new Thread(this);
      readThread.start();
       
   }
 
   public void run() {
      // first thing in the thread, we initialize the write operation
     // initwritetoport();
      try {
         while (true) {
            // write string to port, the serialEvent will read it
            //writetoport();
            Thread.sleep(1000);
         }
      } catch (InterruptedException e) {}
   } 
   private static String deleteCharAt(String strValue, int index) {
		return strValue.substring(0, index) + strValue.substring(index + 1);}
 
   public void serialEvent(SerialPortEvent event) {
       //System.out.println(".............."+event.getEventType());
      switch (event.getEventType()) {
      case SerialPortEvent.BI:
          System.out.println("BI");
      case SerialPortEvent.OE:
          System.out.println("OE");
      case SerialPortEvent.FE:
          System.out.println("FE");
      case SerialPortEvent.PE:
          System.out.println("PE");
      case SerialPortEvent.CD:
          System.out.println("CD");
      case SerialPortEvent.CTS:
          System.out.println("CTS");
      case SerialPortEvent.DSR:
          System.out.println("DSR");
      case SerialPortEvent.RI:
          System.out.println("RI");
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
          System.out.println("OUTPUT_BUFFER_EMPTY");
          break;
      case SerialPortEvent.DATA_AVAILABLE:
         // we get here if data has been received
         byte[] readBuffer = new byte[20];
         try {
            // read data
            while (inputStream.available() > 0) {
               int numBytes = inputStream.read(readBuffer);
            } 
            // print data
            String result  = new String(readBuffer);
            // remove all unknown characters            
            String newString = result.replaceAll("[^a-zA-Z0-9]","");
            System.out.println(""+newString);
          
         } catch (IOException e) {}
    
         break;
      }
   } 
 
}