#include <SoftwareSerial.h>
#define pinRX 0
#define pinTX 1
#define pinLeft A0
#define pinRight A1
#define pinUp A2
#define pinDown A3
bool pauseGameBool = true; // used to indicate running or closed game 
const char DELIMITER = '~'; // only append it to anything you're sending 
const char LEFT = '1', RIGHT = '2' , UP = '3', DOWN = '4';
const char PAUSE = '5'; // const sent from android to indicate pause/stop
const char START = '6'; // const sent from android to indicate start
const char QUIT = '7';

SoftwareSerial btConnection(pinRX, pinTX); // RX, TX
char recvChar ; 
int wrongCounter = 0;
void setup() {
  pinMode(pinRX,INPUT); // set receive pin on the board to input
  pinMode(pinTX,OUTPUT); // set the transmitter pin on the board to output
  pinMode(13, OUTPUT); //enable the pin for the red LED
  pinMode(10, OUTPUT);  //enable the pin for the GREEN LED
  digitalWrite(13, LOW); // TURN OFF THE RED PIN at the start
  digitalWrite(10, LOW); // TURN OFF THE GREEN PIN at the start
  Serial.begin(9600); // SET the speed at which the board should send chars (PORT)
  btConnection.begin(9600); // SET the speed at which the board should receive chars (PORT) 
}

void loop(){  
  delay(350);
  if(btConnection.available() && pauseGameBool){ // if a char is received and the game is in pause mode
    recvChar = btConnection.read(); 
    if(recvChar == START) { // if the received char is the start Signal
      pauseGameBool = false;
      lightUpCorrect(); // LIGHT UP Both leds to indicate a start (Game started)
      lightUpWrong();
    }
  }

  if(!pauseGameBool){ // if the game isn't in pause mode
   if(btConnection.available()){ // while there is a new char
    recvChar = btConnection.read(); 
    if(recvChar == PAUSE) { // if the Received Char is Pause Signal
      pauseGameBool = true;
    }
    
  ////////////// LEFT CHAR CODE ///////////////////////
      while(recvChar == LEFT){ // if the received char is RIGHT
           if(analogRead(pinLeft) > 10){
              lightUpCorrect();
              break;
            }
         if(btConnection.available()){ // if new char is received delay for a flicker and break (mal72t4 tdos)
            lightUpWrong();
            wrongCounter++;
            delay(200);
            break;
         }
     }
  ////////////// RIGHT CHAR CODE ///////////////////////
     while(recvChar == RIGHT){ // if the received char is RIGHT
            if(analogRead(pinRight) > 10){
              lightUpCorrect();
              break;
            }
         if(btConnection.available()){ // if new char is received delay for a flicker and break
            lightUpWrong();
            wrongCounter++;
            delay(200);
            break;
         }
     }
/* ////////////// UP CHAR CODE ///////////////////////
     while(recvChar == UP){ // if the received char is RIGHT
            if(analogRead(pinUp) > 10){
              lightUpCorrect();
              break;
            }
         if(btConnection.available()){ // if new char is received delay for a flicker and break
            lightUpWrong();
            delay(200);
            break;
         }
     }
 ////////////// DOWN CHAR CODE ///////////////////////
     while(recvChar == DOWN){ // if the received char is RIGHT
            if(analogRead(pinDown) > 10){
              lightUpCorrect();
              break;
            }
         if(btConnection.available()){ // if new char is received delay for a flicker and break
            lightUpWrong();
            delay(200);
            break;
         }
     }*/
      if(wrongCounter >= 3){
        quitGame();  
      }
    }
   }
}

void sendToPhone(char c){
  Serial.print(c);
  Serial.print(DELIMITER);
}

void lightUpWrong(){
   digitalWrite(13, HIGH);
   delay(100);
   digitalWrite(13, LOW);
}

void lightUpCorrect(){
   digitalWrite(10, HIGH);
   delay(100);
   digitalWrite(10, LOW);
}

void lightForWait(){
  digitalWrite(13, HIGH);
  digitalWrite(10, HIGH);
}

void quitGame(){
  sendToPhone(QUIT);
  pauseGameBool = true;
   wrongCounter = 0;
}



