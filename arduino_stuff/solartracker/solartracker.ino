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

//If type not specified analog, it is digital.
int victorPin = 3;
int tempSensorPin = 0, tempSensorPin2 = 1;  //Analog
int encoderPin1 = 4, encoderPin2 = 5;
int moistureSensorPin = 2; //Analog
int currentSensorPin1 = 3, currentSensorPin2 = 4;  //Analog
int windSpeedSensorPin = -1 //Pin to be determined

int victorValue = 90;
int tempSensorValue;
byte victorDirection = 0;
byte protectionStatus = 0;

byte receiveBuffer[3];
byte sendBuffer[8];
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
      
      if(receiveBuffer[1] == 0)//If protected mode not enabled...
      {
        rotateTo(receiveBuffer[0]);
      }
      else
      {
       rotateTo(0);//Close the panels. 
      }
      
      /*victorValue = int(receiveBuffer[1]);
      victorDirection = receiveBuffer[2];
      Serial.println(victorValue);
      if(victorDirection == 0x1)
      {
        victorValue = victorValue * -1;
      }
      victorValue = victorValue + 100;
      victorValue = int(victorValue * 0.9);
      Serial.println(victorValue);
      victor.write(victorValue);*/
      
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

void sendData()
{
  sendBuffer[0] = protectionStatus;
  sendBuffer[1] = encoderPosition();
  sendBuffer[2] = processWindSpeedSensorData(analogRead(windSpeedSensorPin));
  sendBuffer[3] = processTemperatureSensorData(analogRead(tempSensorPin));
  sendBuffer[4] = processTemperatureSensorData(analogRead(tempSensorPin2));
  sendBuffer[5] = processMoistureSensorData(analogRead(moistureSensorPin));
  sendBuffer[6] = processCurrentSensorData(analogRead(currentSensorPin1));
  sendBuffer[7] = processCurrentSensorData(analogRead(currentSensorPin2));
  adk.SndData(sLength, sendBuffer);
}

void rotateTo(byte angle)
{
 if(angle > encoderPosition())//If the solar panels are below the target...
 {
  while(angle > encoderPosition)
   {
     victor.write(120);//...rotate them to the target while they are lower than the target.
   }
   vitor.write(90);//90 stops the talon.
 }
 else if(angle < encoderPosition)//If the solar panels are above the target...
 {
   while(angle < encoderPosition)
   {
     victor.write(60);//...rotate them to the target while they are above the target.
   }
   vitor.write(90);//90 stops the talon.
 }
 else victor.write(90);
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

byte endcoderPosition()
{
 //Todo stub 
}

byte processMoistureSensorData(int data)
{
 //Todo stub 
}

byte processCurrentSensorData(int data)
{
 //Todo stub 
}

byte processWindSpeedSensorData(int data)
{
  //Todo stub
}
