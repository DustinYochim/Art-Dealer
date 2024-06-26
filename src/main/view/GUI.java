/**
 * Author: Dustin Yochim (unless otherwise specified)
 * Created: 02/2024
 * Description: This class handles the graphical user-interface for the app. The GUI features 3 main screens:
 the Welcome screen, the main Game window, and the Goodbye screen. It uses Java's swing library. Most things
 used in this class were learnt from https://youtu.be/Kmgo00avvEw?si=5o_pSH_PQPu56Ss3
 */

package main.view;

import main.model.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.sound.sampled.*;

/**
 * The graphical user-interface for the application.
 */
public class GUI {

    /******************************************** Data Attributes  ****************************************************/
    private final JFrame frame; // this is the main game frame
    private JPanel cardPanel; // this Panel will hold the user's cards
    private JPanel previousCards; // this panel holds a history of the previous card selections
    private JLabel roundLabel; // this panel is used to display the current round information
    private ActionListener startButtonListener;
    private ActionListener howToPlayButtonListener;
    private ActionListener dealButtonListener;
    private ActionListener quitButtonListener;
    private ActionListener backButtonListener;

    private final Deck deck; // a deck of 52 Cards

    // Global font/style declarations
    private final Font bigFont = new Font("Serif", Font.BOLD, 20);
    private final Font regFont = new Font("Serif", Font.BOLD, 14);
    private final Font titleFont = new Font("Serif", Font.BOLD, 64);
    private final Font buttonFont = new Font("Serif", Font.PLAIN, 14);
    private final Color bg = new Color(53,101,77);
    private final Color txt = new Color(255, 255, 255);

    /******************************************************************************************************************/

    /**
     * The constructor is used to create the frame that is used throughout the game.
     */
    public GUI(Deck deck) {
        frame = new JFrame("Art Dealer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLocationRelativeTo(null);
        this.deck = deck;
    }

    /**
     * Starts the game by showing the welcome screen.
     */
    public void startApp() {
        showWelcomeScreen();
    }

    /**
     * Shows the welcome screen containing a title, logo, instructions for playing the game,
     * and a button for continuing.
     */
    public void showWelcomeScreen() {
        frame.getContentPane().removeAll();
        JPanel welcomeScreenPanel = new JPanel();
        welcomeScreenPanel.setLayout(new BorderLayout());
        welcomeScreenPanel.setBackground(new Color(53,101,77));

        // Description Section
        // JLabel gameDescriptionLabel = getjLabel(); // a method below that returns the content of the label
        // // gameDescriptionLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        // welcomeScreenPanel.add(gameDescriptionLabel, BorderLayout.NORTH);

        // Welcome, Section
        // JLabel welcomeMessageLabel = new JLabel("Welcome to Art Dealer!");
        JLabel welcomeMessageLabel = new JLabel();
        welcomeMessageLabel.setFont(titleFont);
        welcomeMessageLabel.setForeground(Color.white);

        ImageIcon cardLogo =
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/resources/ad_logo.png")));
        welcomeMessageLabel.setHorizontalTextPosition(JLabel.CENTER);
        welcomeMessageLabel.setVerticalTextPosition(JLabel.TOP);
        welcomeMessageLabel.setIcon(cardLogo);
        welcomeMessageLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeScreenPanel.add(welcomeMessageLabel, BorderLayout.CENTER);

        // Button Section
        JButton startButton = new JButton("Start Game");
        startButton.setFont(buttonFont);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(bg);
        buttonPanel.add(startButton);

        JButton howToPlayButton = new JButton("How to Play");
        howToPlayButton.setFont(buttonFont);
        buttonPanel.add(howToPlayButton);

        // JButton quitButton = new JButton("Quit");
        // quitButton.setFont(buttonFont);
        // buttonPanel.add(quitButton);

        welcomeScreenPanel.add(buttonPanel, BorderLayout.SOUTH);

        welcomeScreenPanel.setBorder(new EmptyBorder(10, 10, 40, 10));

        // Instead of handling events in the GUI, the flow is returned to the GameController
        startButton.addActionListener(e -> {
            if (startButtonListener != null) {
                startButtonListener.actionPerformed(e);
            }
        });

        howToPlayButton.addActionListener(e -> {
            if (howToPlayButtonListener != null) {
                howToPlayButtonListener.actionPerformed(e);
            }
        });

        // quitButton.addActionListener(e -> {
        //     if (quitButtonListener != null) {
        //         quitButtonListener.actionPerformed(e);
        //     }
        // });

        /*
        once everything is added to the welcomeScreenPanel, add it to the main frame
         */
        frame.getContentPane().add(welcomeScreenPanel);
        frame.setVisible(true);
    }

    /**
     * @return The instructions for playing the game.
     */
    private static JLabel getInstructionsjLabel() {
        JLabel descriptionLabel = new JLabel("<html>" +
                 "<body style=color:white; font-family: serif>"
                + "<h2>Introduction</h2>"
                + "<p>Welcome to Art Dealer, the thrilling card game where you go head-to-head against the computer, " +
                "known as the \"Art Dealer,\" in a battle of wits and pattern recognition. Your mission? Unravel the " +
                "mystery behind the dealer's card selection pattern and outsmart them at their own game.</p>" + "<br>"
                + "<p>In each round, you'll be presented with a challenge: pick four cards that you believe the Art " +
                "Dealer will also choose. But beware, the dealer's selections are shrouded in secrecy, and it's up to" +
                " you to decipher the pattern behind their choices.<p>" + "<br>"
                + "Successfully match the dealer's picks and earn a point. Rack up two points, and you'll advance to the next round, where a new and even more perplexing pattern awaits."
                + "Do you have what it takes to outmaneuver the Art Dealer and emerge victorious in this captivating " +
                "game of strategy and deduction? It's time to find out in Art Dealer!" + "<br>"
                + "<h3>Gameplay Instructions:</h3>"
                + "<ol>"
                + "<li><strong>Selection Process:</strong>"
                + "<ul>"
                + "<li>You will pick four cards, one by one.</li>"
                + "<li>Each time you make a selection, the program will display the chosen card immediately.</li>"
                + "<li>If you attempt to pick a card that has already been chosen in the current round, you'll receive an error message and be asked to choose another card.</li>"
                + "<li>If you attempt to pick the same 4 cards more than once in a round you will be forced to pick " +
                "your cards again.</li>"
                + "</ul>"
                + "</li>"
                + "<li><strong>Round Structure:</strong>"
                + "<ul>"
                + "<li>After you pick four cards, the Art Dealer will make its selection based on a predetermined pattern.</li>"
                + "<li>The Art Dealer's selections will be displayed to you with a red border, indicating which cards" +
                " it has chosen.</li>"
                +"<li>In order to progress in the rounds you must match the pattern twice.</li>"
                + "<li>You can choose to continue playing more rounds or stop the program altogether.</li>"
                + "</ul>"
                + "</li>"
                + "<li><strong>History Display:</strong>"
                + "<ul>"
                + "<li>All selected cards, along with an indication of which cards were chosen by the Art Dealer, will be added to the history.</li>"
                + "<li>Cards denoted with a * were chosen by the dealer.</li>"
                +"<li>You can view the history to keep track of your progress.</li>"
                + "</ul>"
                + "</li>"
                + "</ol>"
                + "<p>Enjoy playing \"The Art Dealer\" game!</p>"
                + "</body>"
                + "</html>");
        descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
        return descriptionLabel;
    }

    /**
     * Takes in the current round number and updates the round display
     * @param roundNumber the current round of the game
     */
    public void updateRoundNumber(int roundNumber, int currentWins, int requiredWins) {
        roundLabel.setText("Round " + roundNumber + " : " + currentWins + "/" + requiredWins);
        roundLabel.repaint();
        roundLabel.revalidate();
    }


    /**
     * Creates and displays the main game window.
     */
    public void showGameScreen(int roundNumber, int currentWins, int requiredWins) {

        // Main Panel for the Game Screen
        JPanel gameScreenPanel = new JPanel(new BorderLayout());
        gameScreenPanel.setBackground(bg);
        gameScreenPanel.setForeground(txt);

        /***************** Round Panel ********************************************************************************/
        JPanel roundPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        roundPanel.setBackground(bg);
        roundPanel.setForeground(txt);
        roundLabel = new JLabel("Round " + roundNumber + " : " + currentWins + "/" + requiredWins);
        roundLabel.setFont(bigFont);
        roundLabel.setForeground(txt);
        roundPanel.add(roundLabel);
        roundPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        /**************************************************************************************************************/

        /******************* Main area - includes Card display and Round Information **********************************/
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(roundPanel, BorderLayout.NORTH);

        // Card Panel
        JPanel cardPanelAndHeading = new JPanel(new BorderLayout());
        cardPanelAndHeading.setForeground(txt);
        cardPanelAndHeading.setBackground(bg);

        JLabel cardPanelHeading = new JLabel("Your Hand");
        cardPanelHeading.setFont(bigFont);
        cardPanelHeading.setForeground(txt);
        cardPanelHeading.setBackground(bg);
        cardPanelHeading.setBorder(new EmptyBorder(20, 0, 0, 0));
        cardPanelHeading.setHorizontalAlignment(SwingConstants.CENTER);

        cardPanelAndHeading.add(cardPanelHeading, BorderLayout.NORTH);

        cardPanel = new JPanel(new FlowLayout());
        cardPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        cardPanel.setBackground(bg);
        cardPanel.setForeground(txt);
        cardPanelAndHeading.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(cardPanelAndHeading, BorderLayout.CENTER);
        gameScreenPanel.add(mainPanel, BorderLayout.CENTER);
        /*************************************************************************************************************/

        /************************************* Previous Cards *********************************************************/
        previousCards = new JPanel();
        previousCards.setForeground(txt);
        previousCards.setBackground(bg);
        JLabel heading = new JLabel("Previous Hands");
        heading.setBackground(bg);
        heading.setForeground(txt);
        heading.setFont(bigFont);
        previousCards.add(heading);
        previousCards.setLayout(new BoxLayout(previousCards, BoxLayout.Y_AXIS));
        previousCards.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        previousCards.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK), // Right border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
        ));
        JScrollPane previousCardsScroll = new JScrollPane(previousCards);
        previousCardsScroll.setBackground(bg);
        previousCardsScroll.setForeground(txt);
        previousCardsScroll.setFont(regFont);
        previousCardsScroll.setBorder(null);
        gameScreenPanel.add(previousCardsScroll, BorderLayout.WEST);
        /*************************************************************************************************************/

        /***************************************** Button Panel *******************************************************/
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bg);
        JButton dealButton = new JButton("Pick Cards");
        dealButton.setFont(buttonFont);
        buttonPanel.add(dealButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(buttonFont);
        buttonPanel.add(quitButton);

        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK), // Right border
                BorderFactory.createEmptyBorder(0, 0, 20, 0) // Padding
        ));

        /*************************************************************************************************************/

        /*
        Once again, the action listeners for the buttons return flow to the game controller rather than handling them
         directly in the GUI class.
         */
        dealButton.addActionListener(e -> {
            if (dealButtonListener != null) {
                dealButtonListener.actionPerformed(e);
            }
        });

        quitButton.addActionListener(e -> {
            if (quitButtonListener != null) {
                quitButtonListener.actionPerformed(e);
            }
        });

        gameScreenPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll(); // clear components from previous screens
        frame.getContentPane().add(gameScreenPanel); // Add new components defined in gameScreenPanel
        frame.revalidate(); // Re-validate the frame
        frame.repaint(); // Repaint the frame
    }

    /**
     * Clears the card display
     */
    public void clearCardPanel() {
        cardPanel.removeAll();
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Displays a user's "hand" of cards on the screen.
     * @param hand A hand of cards.
     */
    public void displayHand(Hand hand) {
        cardPanel.removeAll(); // remove previous cards
        // loop through the hand and display each card
        for (Card card : hand.getHand()) {
            String imagePath = "/main/resources/PlayingCards/" + card.getImageFilePath();
            // System.out.println(imagePath);
            ImageIcon cardImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
            // https://docs.oracle.com/javase/8/docs/api/java/awt/Image.html
            Image scaledImage = cardImage.getImage().getScaledInstance(90, 140, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(resizedIcon);

            // Check if the card was chosen by the dealer
            if (card.getChosenByDealer()) {
                // Add a border to the card if chosen by the dealer
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            } else {
                // Otherwise, add an empty border
                imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }

            cardPanel.add(imageLabel);
        }
        cardPanel.revalidate();
        cardPanel.repaint();

    }

    /**
     * Displays the log of "hands" on the screen.
     * @param cards A "hand" of cards formatted for logging.
     */
    public void displayPrevious(String cards){
        // Authored by Ellis Twiggs Jr
        JLabel label = new JLabel(cards);
        label.setForeground(txt);
        label.setBorder(BorderFactory.createEmptyBorder());
        previousCards.add(label);
        previousCards.revalidate();
        previousCards.repaint();
    }

    /**
     * Display's a window showing the dealer's selections, only used on pattern 9.
     * @param message A string of text representing the dealer's choice of cards.
     */
    public void announceSelectionPatternNine(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * announces a win, and gives info about required wins to advance
     * @param currentWins the users current wins
     * @param requiredWins the required wins to advance to the next round
     */
    public void announceWin(int currentWins, int requiredWins) {
        String message;

        if (currentWins < requiredWins) {
            message = "Congratulations! You won this time!\n";
            message += "You have " + currentWins + " out of " + requiredWins + " wins required to move on to the next round.";
        } else {
            message = "You've beat this round. Let's see if you can figure out this next one.";
        }
        JOptionPane.showMessageDialog(frame, message);
    }

    // Authored by Ellis Twiggs Jr
    /**
     * Display's a window, allowing the user to select which cards to be added to his hand.
     * @return An array of cards to be added to the user's hand.
     */
public Hand displayChoice() {
    // Arrays for the suits and ranks
    String[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
    String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN",
            "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
    Hand userHand = new Hand();

    for (int i = 0; i < 4; i++) {
        JComboBox<String> suitComboBox = new JComboBox<>(suits);
        JComboBox<String> rankComboBox = new JComboBox<>(ranks);



        // Create a JPanel to hold the JComboBoxes and labels
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select a Rank:"));
        panel.add(rankComboBox);
        panel.add(new JLabel("Select a Suit:"));
        panel.add(suitComboBox);

        // Show the pop-up window with the drop-down menus
        int result = JOptionPane.showConfirmDialog(null, panel, "Select a Card",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Check if the user made a selection
        if (result == JOptionPane.OK_OPTION) {
            // Get the selected suit and rank
            String selectedRank = (String) rankComboBox.getSelectedItem();
            String selectedSuit = (String) suitComboBox.getSelectedItem();
            // Convert the string to the Suit enum
            // https://www.geeksforgeeks.org/converting-a-string-to-an-enum-in-java/
            try {
                Suit suitToEnum = Suit.valueOf(selectedSuit);
                Rank rankToEnum = Rank.valueOf(selectedRank);
                // https://www.w3schools.com/java/java_classes.asp
                Card card = deck.getCard(rankToEnum, suitToEnum);
                // Check if the card is already selected
                if (!userHand.getHand().contains(card)) {
                    // Add the selected card to the hand
                    userHand.addCard(card);
                    displayHand(userHand);

                } else {
                    // Display a message or handle the case where the same card is selected again
                    JOptionPane.showMessageDialog(null, "You already selected this card. Please pick a new card.");
                    i--; // Decrement the loop counter to prompt the user for the same position again
                }

                // Add the selected card to the list
            } catch (IllegalArgumentException e) {
                System.out.println("There was an error " + e);
                JOptionPane.showMessageDialog(null, "Invalid selection. Please try again.");
                i--;
            }
        } else {
            System.out.println("User canceled the selection.");
            JOptionPane.showMessageDialog(null, "User canceled the selection.");
            // Clearing the selected cards
            userHand.clear();
            displayHand(userHand);
            return null;
        }
    }
    return userHand;
}

    /*
    The program's action listeners are defined in the game controller so that they can return the flow to the
    controller upon events. The below methods take in the listener declared in the game controller and apply them to
    the buttons in the GUI.
     */

    /**
     * Applies a listener to the start button.
     * @param listener The start button action listener.
     */
    public void addStartButtonListener (ActionListener listener){
        startButtonListener = listener;
    }

    /**
     * Applies a listener to the deal button.
     * @param listener The deal button action listener.
     */
    public void addDealButtonListener (ActionListener listener){
        dealButtonListener = listener;
    }

    /**
     * Applies a listener to the quit button.
     * @param listener The quit button action listener.
     */
    public void addQuitButtonListener (ActionListener listener){
        quitButtonListener = listener;
    }

    public void addHowToPlayButtonListener (ActionListener listener){
        howToPlayButtonListener = listener;
    }


    /**
     * Shows a goodbye screen for 5 seconds before terminating the program.
     */
    public void showGoodbyeScreen () {
        // Goodbye Panel Setup
        JPanel goodbyeScreenPanel = new JPanel();
        goodbyeScreenPanel.setBackground(bg);
        goodbyeScreenPanel.setForeground(txt);
        goodbyeScreenPanel.setLayout(new BorderLayout());


        // Goodbye label setup
        JLabel goodbyeMessageLabel = new JLabel("Thanks for playing");
        goodbyeMessageLabel.setFont(titleFont);
        goodbyeMessageLabel.setBackground(bg);
        goodbyeMessageLabel.setForeground(txt);
        goodbyeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        goodbyeScreenPanel.add(goodbyeMessageLabel, BorderLayout.CENTER);
        ImageIcon cardLogo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/main/resources/ad_logo.png")));
        goodbyeMessageLabel.setHorizontalTextPosition(JLabel.CENTER);
        goodbyeMessageLabel.setVerticalTextPosition(JLabel.TOP);
        goodbyeMessageLabel.setIcon(cardLogo);
        goodbyeMessageLabel.setHorizontalAlignment(JLabel.CENTER);

        goodbyeScreenPanel.add(goodbyeMessageLabel, BorderLayout.CENTER);


        frame.getContentPane().removeAll(); // remove previous content from the frame
        frame.getContentPane().add(goodbyeScreenPanel);
        frame.setVisible(true);

        // Adds a 5-second exitTimer for the goodbye screen to show before terminating the program
        Timer exitTimer = new Timer(5000, e -> System.exit(0));
        exitTimer.setRepeats(false);
        exitTimer.start();
    }


    /**
     * Warn user that they have used this hand in this round already
     */
    public void showSameHandWarning() {
        String message = "Nice try. You'll have to select a unique hand to win this round.";
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * @return the user's choice of restarting (1) or quitting (2)
     */
    public int displayRestartOption() {
        String[] options = {"Restart", "Quit"};
        int choice = JOptionPane.showOptionDialog(frame,
                "You won the game! Do you want to restart?",
                "Congratulations!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return choice + 1; // Returning 1 for "Restart" and 2 for "Quit"
    }

    /**
     * play's a sound effect, used when the user wins a round
     */
    public void playVictorySound()  {
        try {
            AudioInputStream sound =
                    AudioSystem.getAudioInputStream(Objects.requireNonNull(GUI.class.getResourceAsStream(
                            "/main/resources/sound/victory.wav")));

            Clip c = AudioSystem.getClip();

            c.open(sound);
            c.start();
            Thread.sleep(c.getMicrosecondLength() / 1000);

            c.close();
            sound.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * shows a new window containing game instructions
     */
    public void showInstructionsScreen() {
        frame.revalidate();
        frame.repaint();
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BorderLayout());
        instructionsPanel.setBackground(bg);
        instructionsPanel.setForeground(Color.white);

        JLabel gameDescriptionLabel = getInstructionsjLabel(); // a method below that returns the content of the label
        gameDescriptionLabel.setForeground(txt);
        // // gameDescriptionLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        instructionsPanel.add(gameDescriptionLabel, BorderLayout.CENTER);

        JPanel instructionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionsButtonPanel.setBackground(bg);

        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        instructionsButtonPanel.add(backButton);

        instructionsPanel.add(instructionsButtonPanel, BorderLayout.SOUTH);

        instructionsPanel.setBorder(new EmptyBorder(10, 10, 40, 10));

        // Instead of handling events in the GUI, the flow is returned to the GameController

        backButton.addActionListener(e -> {
            if (backButtonListener != null) {
                backButtonListener.actionPerformed(e);
            }
        });

        /*
        once everything is added to the welcomeScreenPanel, add it to the main frame
         */
        frame.getContentPane().removeAll();
        frame.getContentPane().add(instructionsPanel);
        frame.setVisible(true);
    }


    public void addBackButtonListener(ActionListener listener) { backButtonListener = listener;}
}
