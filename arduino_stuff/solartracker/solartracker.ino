#include <Servo.h>

#include <adk.h>
#include <Usb.h>

USB Usb;
ADK adk(&Usb,"SolarProjectTeam",
"DemoKit",
"DemoKit",
"1.0",
"http://www.android.com",
"0000000012345678");

Servo victor;

int victorPin = 4;
int tempSensorPin = 0;
int victorValue = 90;
int tempSensorValue;
byte victorDirection = 0;

byte receiveBuffer[3];
byte sendBuffer[1];
uint16_t length;
uint16_t sLength;

byte rcode;

void setup()
{
  Serial.begin(9600);
  if(Usb.Init() == -1)
  { 
    Serial.println("USB not initialized.");
    while(true);
  }
  Serial.println("USB initialized.");
  Serial.println(sizeof(receiveBuffer));

  pinMode(connectedLed, OUTPUT);
  pinMode(controlledLed, OUTPUT);
  victor.attach(victorPin);
}

void loop()
{
  Usb.Task();
  if(adk.isReady())
  {
    Serial.println("ADK is ready!"); 
    digitalWrite(connectedLed, HIGH);
    //length = 1;
    length = sizeof(receiveBuffer);
    sLength = sizeof(sendBuffer);
    Serial.println(length);
    
    rcode = adk.RcvData(&length, receiveBuffer);
    if (rcode == 0)
    {
      Serial.println("Data received successfully!");
      Serial.println(receiveBuffer[0]);
      if (receiveBuffer[0] == byte(0))
      {
        Serial.println("Turning on LED.");
        digitalWrite(controlledLed, HIGH);
      }
      else
      {
        Serial.println("Turning off LED. ");
        digitalWrite(controlledLed, LOW); 
      }
      
      victorValue = int(receiveBuffer[1]);
      victorDirection = receiveBuffer[2];
      Serial.println(victorValue);
      if(victorDirection == 0x1)
      {
        victorValue = victorValue * -1;
      }
      victorValue = victorValue + 100;
      victorValue = int(victorValue * 0.9);
      Serial.println(victorValue);
      victor.write(victorValue);
      
    }
    
    tempSensorValue = analogRead(tempSensorPin);
    sendBuffer[0] = processTemperatureSensorData(tempSensorValue);
    //sendBuffer[0] = 0x01;
    
    adk.SndData(sLength, sendBuffer);
  }
  else
  {
    Serial.println("ADK not ready.");
    digitalWrite(connectedLed, LOW); 
  }
  delay(100);
}

byte processTemperatureSensorData(int temp)
{
  temp = temp * 5000;  //
  temp = temp / 1023;  //Gets millivolts from arduino analog units
  temp = (temp - 500) / 10; //Converts millivolts to degrees c.
  Serial.println("temparature" + temp);
  
  byte tempByte = temp + 40; /* The temperature sensor has a range of
                                40 to 150 degrees C.  The byte data
                                type has a range of 0 to 255.  This 
                                conversion ensures that temp fits into
                                tempByte. */
  return tempByte;
}
