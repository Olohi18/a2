import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Comparator;


public class GA_Simulation {

  //Attributes declaration
  private ArrayList<Individual> generation;
  private int n; // number of individuals in each generation
  private int winners; //number of individuals allowed to reproduce in each generation
  private int rounds; //number of rounds of evolution to run
  private int c_0; //initial chromosome size
  private int c_max; //maximum chromosome size
  private double chances; //chance of a mutation in each gene per roound
  private int states; //gene states possible 


    /**
     * Constructor to intialize relevant parameters and create a new generation of individuals using init.
     * @param n
     * @param winners
     * @param rounds
     * @param c_0
     * @param c_max
     * @param chances
     * @param states
     */
    public GA_Simulation(int n, int winners, int rounds, int c_0, int c_max, double chances, int states){
      this.n = n;
      this.winners = winners;
      this.rounds = rounds;
      this.c_0 = c_0;
      this.c_max = c_max;
      this.states = states;
      this.chances = chances;
      this.generation = new ArrayList<>();
      this.generation = init(this.n); //creates a new generation of individuals--- do I need this line?
    }


    /**
     * Creates n individuals and stores them in an arraylist
     * @param n
     * @return: a population of n individuals in an arraylist
     */
    public ArrayList<Individual> init(int n){
      //iterate through n times, creating a new individual and appending created individual to generation each time
        for (int i = 0;i < this.n; i++){
          Individual newCreature = new Individual(this.c_0, this.states);
          this.generation.add(newCreature);
        }

      return this.generation; //returns the arraylist
    }


    /** Sorts population by fitness score, best first 
     * @param pop: ArrayList of Individuals in the current generation
     * @return: ArrayList of Individuals sorted by fitness
    */
    public void rankPopulation(ArrayList<Individual> pop) {
        // sort population by fitness
        Comparator<Individual> ranker = new Comparator<>() {
          // this order will sort higher scores at the front
          public int compare(Individual c1, Individual c2) {
            return (int)Math.signum(c2.getFitness()-c1.getFitness());
          }
        };
        pop.sort(ranker); 
      }
 

    /**
     * generates more fit individuals from a parent generation
     * @param generation
     * @return: the new population or generation resulting from 1 round of evolution 
     */
    public ArrayList<Individual> evolve(ArrayList<Individual> generation){
      ArrayList<Individual> bestFit = new ArrayList<>(); //intializes an arraylist called bestfit that stores the winners of the natural selection process
      ArrayList<Individual> newGeneration = new ArrayList<>(); //creates a new arraylist to store the new generation formed from evolution

      //loop collects the first 15 "winners" and stores in bestFit
      for (int i = 0; i < this.winners; i++){
        bestFit.add(this.generation.get(i));
      }

      //loop creates n new individuals by crossbreeding two bestFit individuals (parents)
      for (int  i = 0; i < this.n; i++){
        int randomIndex1 = ThreadLocalRandom.current().nextInt(0, this.winners); //select a random index and stores in randomIndex1
        int randomIndex2 = ThreadLocalRandom.current().nextInt(0, this.winners); //select a random index and stores in randomIndex2

        //ensures elements chosen are different to avoid self-breeding
        while (randomIndex1 == randomIndex2){
          randomIndex1 = ThreadLocalRandom.current().nextInt(0, this.winners);
        }

        //creates a child from the chosen parents and adds it to the newGeneration
        Individual newIndividual = new Individual(bestFit.get(randomIndex1), bestFit.get(randomIndex2), this.c_max, this.chances, this.states);
        newGeneration.add(newIndividual);
      }
      return newGeneration;
    }


    /**
     * Provides vital fitness details associated with the generation passed in
     * @param generation
     */
    public void describeGeneration(ArrayList<Individual> generation){
      rankPopulation(generation); //ranks the population based on best fit
      Individual bestIndividual = generation.get(0); //gets the most fit individual
      Individual leastIndividual = generation.get(this.n-1); //gets the least fit individual
      Individual kthIndividual = generation.get(this.winners - 1); //gets the kth fit individual

      //prints data
      System.out.println("The chromosome of the best individual is " + bestIndividual.toString());
      System.out.println("---------");
      System.out.println("The best fit of the best individual is " + bestIndividual.getFitness()); //will this check for fitness again??--- yes but same chromosome, same fitness...
      System.out.println("----------------");
      System.out.println("The fit of the kth individual is " + kthIndividual.getFitness());
      System.out.println("----------------");
      System.out.println("The least fit of the least fit individual is " + leastIndividual.getFitness());
      System.out.println();

    }

    
    /**
     * Runs the entire GA_simulation experiment 
     * @return: the final population produced after "rounds" rounds of evolution
     */
    public ArrayList<Individual> run(){
      ArrayList<Individual> population = init(100); //initializes the population
      describeGeneration(population); //ranks population based on fitness and describes it

      //passes the population through "this.rounds" rounds of evolution
      for (int i = 0; i < this.rounds; i++){
          System.out.println("Round " + (i+1));
          population = evolve(population);
          describeGeneration(population);
      }  
      return population; //resulting population after rounds of evolution is completed
    }


    /**
     * Main function to perform the natural selection simulation
     * @param args
     */
    public static void main(String[] args) {
      GA_Simulation generationSample = new GA_Simulation(100, 15, 100, 8, 20, 0.01, 5); //creates a new GA instance
      GA_Simulation generation1 = new GA_Simulation(100, 5, 10, 8, 5, 0.01, 5); //creates a new GA instance for exploration
      generationSample.run();

      
      
    }
}