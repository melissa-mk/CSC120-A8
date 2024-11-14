import java.util.Scanner;
import java.util.Stack;

public class Birdie implements Contract {
    String[] directions={"forward", "backward", "left", "right"};
    String[] oppositeDirections={"backward", "forward", "right", "left"};

    String name;
    int health=10; // indicates the bird's health status based on meals, exertion, or rest
    Stack<String> history=new Stack<>(); // keeps track of the Birdie's action to facilitate the action reversal

    /**
     * constructor that adds the bird's name
     * @param name of the bird object
     */
    public Birdie(String name){
        this.name=name;
    }

    /**
     * explicit default constructor (no parameters)
     */
    public Birdie(){}

    /**
     * changes the Birdie's name from the default to a user-defined name
     * @param name new name of the bird
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     * method assigns each direction with its reverse
     * @param direction the user wants to move the bird in
     * @return the reverse of the input direction
     */
    public String getReverse(String direction){
        for(int i=0; i<directions.length; i++){
            if(directions[i].equals(direction)){
                return oppositeDirections[i];
            }
        }
        return null;
    }

    /**
     * the method enables a bird to hold something by its beak
     * @param item an item being grabbed
     */
    @Override
    public void grab(String item) {
        System.out.println("Yay I got the " + item);
        history.push("Grabbed an item.");
    }

    /**
     * this method enables a bird to drop the item being held
     * @param item an item being dropped
     * @return the dropped item
     */
    @Override
    public String drop(String item) {
        history.push("Dropped an item.");
        return "Oops I dropped the " + item + "!";
    }

    /**
     * the method enables a bird to stare at and observe the item being held
     * @param item an item being observed
     */
    @Override
    public void examine(String item) {
        System.out.println("Hmm, the " + item + " looks interesting 👀");
    }

    /**
     * @param item an item being used
     */
    @Override
    public void use(String item) {
        System.out.println("CAWWWW!!! I'm trying to use the " + item + "!");
        history.push("Using this item.");
    }

    /**
     * validates user input on the direction field
     * @param direction the user wants to move the bird in
     * @return true if the direction is among the predefined set, false otherwise
     */
    public boolean isValidDirection(String direction){
        for (int i = 0; i < directions.length; i++) {
            if(direction.equals(directions[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * @param direction, forward/backward/left/right
     * @return true if the bird can walk, false if the bird is out of health/energy
     */
    @Override
    public boolean walk(String direction) {
        if(isValidDirection(direction)) {
            if (health > 0) {
                System.out.println("Walking " + direction);
                health--;
                history.push("Abouta walk " + getReverse(direction));
                return true;
            } else {
                System.out.println("You don't have enough energy to walk! Take a meal first");
                return false;
            }
        }else {
            throw new RuntimeException("Please use one of the valid directions.");
        }
    }

    /**
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return true if the bird is flying, false if the bird is at (0,0)
     */
    @Override
    public boolean fly(int x, int y) {
        if(health>=2) {
            history.push("I'm FLYINGGG!!!");
            return x != 0 || y != 0;
        }else {
            return false;
        }
    }

    /**
     * bird gets tired and its health slightly deteriorates
     * @return the new health level
     */
    @Override
    public Number shrink() {
        if(!fly(0,0)) {
            history.push("I just lost 2 energy bars!");
            return health-=2;
        } else {
            history.push("I just lost 1 energy bar!");
            return health--;
        }
    }

    /**
     * feeds the bird and increases its health
     * @return the numerical health status
     */
    @Override
    public Number grow() {
        System.out.println("Such a nutritious meal 🥹");
        history.push("I just had a meal!");
        return health+=2;
    }

    /**
     * bird stops flying and gets its health slightly increased
     *
     */
    @Override
    public void rest() {
        fly(0,0);
        history.push("Just regained 1 energy bar after I rested!");
        health++;
    }

    /**
     * this method checks the top element of the stack and checks what method it is indicating then proceeds to reverse its action
     */
    @Override
    public void undo() {
        try {
            if(!history.empty()) {
                String lastAction = history.pop();
                if (lastAction.equals( "Grabbed an item.")) {
                    System.out.print(" Never mind, I just dropped the item.");
                } else if (lastAction.equals( "Dropped an item.")) {
                    System.out.print(" Whew! I just picked up the item.");
                } else if (lastAction.equals( "Using this item.")) {
                    System.out.print(" I'm not using the item anymore.");
                } else if (lastAction.startsWith("Abouta walk ")) {
                    switch (lastAction) {
                        case "Abouta walk forward" -> System.out.println("Moving forward");
                        case "Abouta walk backward" -> System.out.println("Moving backward");
                        case "Abouta walk left" -> System.out.println("To the left, to the left");
                        case "Abouta walk right" -> System.out.println("To the right, to the right");
                    }
                } else if (lastAction.equals( "I'm FLYINGGG!!!")) {
                    fly(0, 0);
                    System.out.print(" I'm feeling a bit queasy, so I'm not flying anymore.");
                } else if (lastAction.equals( "I just lost 2 energy bars!")) {
                    health += 2;
                    System.out.print(" I'm feeling better, so I can fly again.");
                } else if (lastAction.equals( "I just lost 1 energy bar")) {
                    health++;
                    System.out.print(" I'm feeling just a bit better.");
                } else if (lastAction.equals( "I just had a meal!")) {
                    System.out.print(" Not a good meal...falling sick.");
                    health -= 2;
                } else if (lastAction.equals( "Just regained 1 energy bar after I rested!")) {
                    System.out.print(" Never mind, I actually need a nap.");
                    health--;
                }
            }
        }catch(RuntimeException e){
            System.out.println("Your stack might be empty!");
        }
    }

    /**
     * provides a list of methods that a Birdie can implement
     */
    public void showOptions(){
        System.out.println("Welcome to Birdie! Here's what "+ name+ " can do:\n1. Grab an item\n2. Drop an item\n3. Examine an item\n4. Use an item\n5. Walk in a direction [forward/backward/left/right]\n6. Fly to a point\n7. Shrink\n8. Grow\n9. Rest\n10. Undo\n11. Change " + name + "'s name");
    }

    public static void main(String[] args) {
        Birdie birdie = new Birdie("Larry");
        System.out.println("What do you want to name your birdie?");

        Scanner sc = new Scanner(System.in);
        birdie.setName(sc.nextLine());
        birdie.showOptions();
        birdie.walk("forward");
        birdie.undo();
        birdie.walk("left");
        birdie.undo();
        birdie.walk("right");
        birdie.undo();
        birdie.walk("backward");
        birdie.undo();

        birdie.grow();
        System.out.println(birdie.health);
    }
}
