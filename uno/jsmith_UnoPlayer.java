package uno;

import java.util.List;
//In the simulation, my name is 'James'. 
public class jsmith_UnoPlayer implements UnoPlayer {
    
    //ranks and colors listed in http://nifty.stanford.edu/2012/davies-uno/description.html
    
    //implements ranks 
    UnoPlayer.Rank skip = UnoPlayer.Rank.SKIP; 
    UnoPlayer.Rank drawTwo = UnoPlayer.Rank.DRAW_TWO; 
    UnoPlayer.Rank wildDrawFour = UnoPlayer.Rank.WILD_D4; 
    UnoPlayer.Rank reverse = UnoPlayer.Rank.REVERSE; 
    UnoPlayer.Rank wild = UnoPlayer.Rank.WILD; 

    //implements colors 
    UnoPlayer.Color red = UnoPlayer.Color.RED; 
    UnoPlayer.Color blue = UnoPlayer.Color.BLUE; 
    UnoPlayer.Color green = UnoPlayer.Color.GREEN; 
    UnoPlayer.Color yellow = UnoPlayer.Color.YELLOW; 
    UnoPlayer.Color wildColor = UnoPlayer.Color.NONE; 
    
    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card. 
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can 
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    
    public int play(List<Card> hand, Card upCard, Color calledColor, GameState state) {
        
        //data for card on top 
        int cardNumber = upCard.getNumber(); 
        UnoPlayer.Rank cardRank = upCard.getRank(); 
        UnoPlayer.Color cardColor = upCard.getColor(); 
        
        
        int redCount = 0; 
        int blueCount = 0; 
        int greenCount = 0; 
        int yellowCount = 0; 
        
        int actionCards = 0; 
        int regularCards = 0; 
        
        UnoPlayer.Color lowestCardColor = null; 
        UnoPlayer.Color highestCardColor = null; 
        
        //arrays 
        List<Card> cardHistory = state.getPlayedCards(); 
        int[] totalScore = state.getTotalScoreOfUpcomingPlayers(); 
        int[] cardCount = state.getNumCardsInHandsOfUpcomingPlayers(); 


        
                for(int i = 0; i < cardHistory.size(); i++) { 
            //count for action cards
            if (cardHistory.get(i).getRank() != Rank.NUMBER) { 
                actionCards++; 
            }
            //count for regular cards
            else 
                regularCards++;
        }
        
        //frequency of each color in personal deck
        for (int i = 0; i < hand.size(); i++) { 
            if(hand.get(i).getColor() == red) { 
                redCount++; 
            }
            if(hand.get(i).getColor() == blue) { 
                blueCount++; 
            }
            if(hand.get(i).getColor() == green) { 
                greenCount++; 
            }
            if(hand.get(i).getColor() == yellow) { 
                yellowCount++; 
            }
        }

        int playerHighestScore = 0; 
        int highestScore = totalScore[0]; 
        
        for(int score = 0; score < totalScore.length; score++) { 
            if (totalScore[score] > highestScore) 
                playerHighestScore = score; 
        }
        
        
        //lowest color frequency in my hand 
            //red
        if ( ! (redCount > blueCount && redCount > greenCount && redCount > yellowCount))
            lowestCardColor = red; 
            //blue
        else if ( ! (blueCount > redCount && blueCount > greenCount && blueCount > yellowCount))
            lowestCardColor = blue;   
        //green 
        else if ( ! (greenCount > redCount && greenCount > blueCount && greenCount > yellowCount))
            lowestCardColor = green;   
        else 
            lowestCardColor = yellow; 
        
        
        //highest color frequency in my hand 
        if (redCount > blueCount && redCount > greenCount && redCount > yellowCount)
            highestCardColor = red; 
            //blue
        else if (blueCount > redCount && blueCount > greenCount && blueCount > yellowCount)
            highestCardColor = blue;   
            //green 
        else if (greenCount > redCount && greenCount > blueCount && greenCount > yellowCount)
            highestCardColor = green;   
        else 
            highestCardColor = yellow; 
            
        
        //playing in order of most useful cards in deck - (i) highest point cards - wild and draw four, (ii) most common color card, (iii) any wild card, (iv) if color/number matches

        for (int i = 0; i < hand.size(); i++) { 
           
            if (hand.get(i).getRank() == wild || hand.get(i).getRank() == wildDrawFour) 
                return i;   
            if (highestCardColor == calledColor) { 
                if (hand.get(i).getColor() == highestCardColor)
                    return i; 
            }
            
            if(hand.get(i).getRank() == skip || hand.get(i).getRank() == reverse || hand.get(i).getRank() == drawTwo) { 
                if(hand.get(i).getRank() == cardRank || hand.get(i).getColor() == cardColor)
                    return i; 
            }
            //play color if it matches 
            if (hand.get(i).getColor() == calledColor) 
                return i;  
            if(cardNumber == hand.get(i).getNumber() && cardNumber != -1)
                return i; 
                
            //get rid of the least common card 
            if(lowestCardColor == cardColor) {
                if(hand.get(i).getColor() == lowestCardColor) 
                    return i; 
            }

            //color if it matches the upcard 
            if(hand.get(i).getColor() == cardColor) 
                return i; 
                    
        }
        
        return -1; 
    }


    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to 
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand) {

        int numRed = 0; 
        int numBlue = 0; 
        int numGreen = 0; 
        int numYellow = 0; 
        
        for (int i = 0; i < hand.size(); i++){
            if(hand.get(i).getColor() == red) { 
                numRed++; 
            }
            if(hand.get(i).getColor() == blue) { 
                numBlue++; 
            }
            if(hand.get(i).getColor() == green) { 
                numGreen++; 
            }
            if(hand.get(i).getColor() == yellow) { 
                numYellow++; 
            }
        }
        
        if(numRed > numYellow && numRed > numBlue && numRed > numGreen) {
            return red; 
        }
        else if (numBlue > numYellow && numBlue > numRed && numBlue > numGreen) {
            return blue; 
        }
        else if (numGreen > numRed && numGreen > numBlue && numGreen > numYellow) { 
            return green; 
        }
        else 
            return yellow; 
        
    }
    
}



