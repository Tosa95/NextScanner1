package tosatto.nextscanner.hardwarecom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;


public class SerialControl implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	/*private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};*/
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	//private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	//private static final int DATA_RATE = 9600;

	public SerialControl(int dataRate, int timeOut, String[] portNames)
	{
		initialize(dataRate, timeOut, portNames);
	}
	
	//Initializes the connection
	public void initialize(int dataRate, int timeOut, String[] portNames) {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		System.out.println(portEnum.hasMoreElements());
		
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
			for (String portName : portNames) {
				if (currPortId.getName().equals(portName)) {
					System.out.println("Port " + portName + " found");
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					timeOut);

			// set port parameters
			serialPort.setSerialPortParams(dataRate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			System.out.println("AAA");
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
			System.out.println(e.toString());
		}
	}

	//Closes the connection
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				//String inputLine=input.readLine();
				//System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	//Accende il laser
	public boolean laserON ()
	{
		PrintStream pStr = new PrintStream(output);

		pStr.print('^');
		
		try {
			String inputLine=input.readLine();
			
			if (inputLine.equals("OK"))
			{
				System.out.println("LaserON");
			}
			else
			{
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean laserOFF ()
	{
		PrintStream pStr = new PrintStream(output);

		pStr.print('v');
		
		try {
			String inputLine=input.readLine();
			
			if (inputLine.equals("OK"))
			{
				System.out.println("LaserOFF");
			}
			else
			{
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean step ()
	{
		PrintStream pStr = new PrintStream(output);

		pStr.print('s');
		
		try {
			String inputLine=input.readLine();
			
			if (inputLine.equals("OK"))
			{
				System.out.println("step");
			}
			else
			{
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
}