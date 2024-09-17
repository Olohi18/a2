import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Hashtable;

public class Individual {

    /**
     * Chromosome stores the individual's genetic data as an ArrayList of characters
     * Each character represents a gene
     */
    ArrayList<Character> chromosome; 
    Integer c_0; //c_0 is the initial chromosome size
    Integer num_letters; //num_letters is the number of possible gene states

    
    /**
     * Chooses a letter at random, in the range from A to the number of states indicated
     * @param num_letters The number of letters available to choose from (number of possible states)
     * @return The letter as a Character
     */
    private Character randomLetter(int num_letters) {
        return Character.valueOf((char)(65+ThreadLocalRandom.current().nextInt(num_letters)));
      }
    
    /** 
     * Method to determine whether a given gene will mutate based on the parameter ***m***
     * @param m the mutation rate
     * @return true if a number randomly chosen between 0 and 1 is less than ***m***, else false
    */
    private Boolean doesMutate(double m){ //changed parameter to double
        float randomNum = ThreadLocalRandom.current().nextInt(0, 1);
        return randomNum < m;
    }

    /**
     * Expresses the individual's chromosome as a String, for display purposes
     * @return The chromosome as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder(chromosome.size());
        for(Character ch: chromosome) {
          builder.append(ch);
        }
        return builder.toString();
      }

    /** 
     * Inital constructor to generate initial population members
     * @param c_0 The initial chromosome size
     * @param num_letters The number of letters available to choose from
     */
    public Individual(int c_0, int num_letters) {
      this.chromosome = new ArrayList<>(); //initializes an empty arraylist to store the chromosome's genes
      //populates the chromosome with genes, such that length of chromosome is c_0
      for (int i = 0; i < c_0; i++){
        this.chromosome.add(randomLetter(num_letters));
      }
    }

     /**
      * Second constructor to create parents and offspring chromosomes
      * @param parent1 The first parent chromosome
      * @param parent2 The second parent chromosome
      * @param c_max The maximum chromosome size
      * @param m The chances per round of mutation in each gene
      */
    public Individual(Individual parent1, Individual parent2, int c_max, double m, int num_letters) { 

      int prefix = ThreadLocalRandom.current().nextInt(1,5); //determines the length of the prefix
      int suffix = ThreadLocalRandom.current().nextInt(1, 5); //determines the length of the suffix
      this.chromosome = new ArrayList<>(); //creates a new arraylist for the child chromosome

      //adds the first prefix elements from parent1 to the new (child) chromosome
      for (int i=0; i <= prefix; i++){
        this.chromosome.add(parent1.chromosome.get(i)); //what if we make the child so that it doesn't exceed cmax in the first place
      }

      //adds the last suffix elements from parent2 to the new (child) chromosone
      for (int i = parent2.chromosome.size() - suffix; i < parent2.chromosome.size(); i++){
        this.chromosome.add(parent2.chromosome.get(i)); 
      }

      //ensures length of created chromosome is at most c_max: not a necessary step given how I implemented the program, though
      while (this.chromosome.size() > c_max){
        this.chromosome.removeLast();
      }

      //mutates genes in chromosome based on the probability of mutation, m
      for (int i = 0; i < this.chromosome.size(); i++){
        if (doesMutate(m)){
          char random = randomLetter(num_letters); //set random to a random number

          //continue setting random to another letter until random equals a letter that is not the original letter
          while (random != this.chromosome.get(i)){
            random = randomLetter(num_letters); }
          
          this.chromosome.set(i, random);}    
      }
  }

    /**
     * Calculates the fitness score of each chromosome
     * @return The fitness score as an int
     */
    public int getFitness() {
        //initialize fitness score to 0
        int fitness = 0;

        //initialize two pointers, start and end
        int start = 0;
        int end = this.chromosome.size() - 1;

        //iterate through arraylist using start and end pointers to compare elements at ith and n-ith position, where n is the length of the chromosome
        while (start <= end){

          Character startElement = this.chromosome.get(start);
          Character endElement = this.chromosome.get(end);

          //compare elements pointed to by both pointers
          if (startElement.equals(endElement)){
            fitness ++;
          }
          else{
            fitness --;
          }

          start ++; //increment start pointer
          end --; //decrement end pointer
        }


        //iterate through array list again using start pointer to compare adjacent elements
        while (start < this.chromosome.size()-1){
          
          Character current = this.chromosome.get(start);
          Character nextElem = this.chromosome.get(start + 1);
          
          //compare elements pointed to by start pointer and start+1
          if (current.equals(nextElem)){
            fitness --;
          }

          start ++; //increment start pointer
        }

      return fitness; //returns fitness score               
      }
    
    public static void main(String[] args) {
      Individual olohi = new Individual(8, 5);
      Individual husband = new Individual(8, 5);
      Individual jedidiah = new Individual(olohi, husband, 8, 2.0, 5);
      System.out.println(olohi.toString() + " and " + husband.toString() + " are parents of " + jedidiah.toString());
      System.out.println("The fitness of olohi: " + olohi.getFitness() + " and husband: " + husband.getFitness() + " and jeddy: " + jedidiah.getFitness());

    }
}
