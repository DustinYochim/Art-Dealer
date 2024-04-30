/*
 Art Dealer was written in Java using IntelliJ by Dustin Yochim, Ellis Twiggs Jr, Jonathan Schweighauser, Adam Loepker,
 and Yosef Alqufidi in Spring 2024 Semester as part of the Introduction to Software Profession class at the
 University of Missouri - Saint Louis.

  It contains the classes: Card for a single playing card represented using the Rank and Suit enums,
  Deck for a  collection of 52 Cards, and Hand as a collection of 4 Cards.

  The Deck and Hand classes use an ArrayList as their central Data Structure to enable easy shuffling and
  dealing of cards.

  In order to run the program, clone the repository from https://github.com/DustinYochim/Art-Dealer down to your
  local machine. Then open the program in your favorite IDE (we developed using IntelliJ). Once you have the program
  open in your IDE, in order to build the program you may have to select the Main class. The "Main" class of this
  project is App.java. Once you have the program open in your IDE and have selected App.java as the "Main" class.
  Click the "Run" option in your IDE to start the App!
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
