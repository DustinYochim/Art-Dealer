/*
 Art Dealer was written by Dustin Yochim, Ellis Twiggs Jr, Jonathan Schweighauser, Adam Loepker, and Yosef Alqufidi
 in Spring 2024 Semester as part of the Introduction to Software Profession class at the University of Missouri - Saint
 Louis.

 This Java program was an attempt to design a program following the Model View Controller design pattern
 discussed here https://www.geeksforgeeks.org/mvc-design-pattern/. It contains the classes: Card for a single playing
  card represented using the Rank and Suit enums, Deck for a collection of 52 Cards, and Hand as a collection of 4
  Cards. The Deck and Hand classes use an ArrayList as their central Data Structure to enable easy shuffling and
  dealing of cards.

  In order to run the program, clone the repository from https://github.com/DustinYochim/Art-Dealer down to your
  local machine. Then open the program in your favorite IDE (we developed using IntelliJ). Once you have the program
  open in your IDE, in order to build the program you may have to select the Main class. The "Main" class of this
  project is App.java. Once you have the program open in your IDE and have selected App.java as the "Main" class.
  Click the "Run" option in your IDE to start the App!

 Homework 1
 The program lets you deal out 4 cards where it will show you your hand, and log your hand in a separate file, you
 can continue to draw cards or quit the program.

 Homework 2
 Now the program lets you pick your own cards, rather than the cards being randomly dealt to you. This iteration
 also adds the history of your previous hands to the display on the game screen.

 Homework 3
 The program now displays each card as soon as the user makes a selection, following a sequential rhythm: first card
 selected is immediately displayed, second card selected is displayed next, and so on until the fourth card is
 displayed. Once displayed, a card remains visible until the current round ends. If the user attempts to select a card
 that has already been chosen in the current round, the program issues a polite error message and prompts for a
 different card. After the user selects four cards, the program "picks" a subset of those cards based on the
 Art Dealer's pattern. The Art Dealer (program) selects any red cards (hearts or diamonds) and avoids selecting
 any black cards (spades or clubs). The selected cards are displayed visibly to the user, and the program pauses
 until the user indicates to stop.
 */

package main;

import main.controller.GameController;
import main.model.Deck;
import main.view.GUI;

/**
 * App is the starting point of the program and where the card Deck, GUI, and GameController will be initialized.
 */
public class App {
    public static void main(String[] args) {
        Deck deck = new Deck(); // initialize deck
        GUI gui = new GUI(deck); // initialize GUI
        GameController controller = new GameController(deck, gui); // controller will need deck and GUI to control flow
        gui.startApp(); // this starts the GUI
    }
}
