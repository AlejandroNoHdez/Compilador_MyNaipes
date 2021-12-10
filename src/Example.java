class Example {
      public static void main(String args[])
          throws java.io.IOException { // This works! No need to use try{// ...}catch(IOException ex){// ...}         
  
          System.out.println("Type a letter: ");
          char letter = (char) System.in.read();
          System.out.println("You typed the letter " + letter);
      }
  }
