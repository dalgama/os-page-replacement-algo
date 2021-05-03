import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Question2 {

  // FIFO Algorithm
  public int faultsFIFO (int allPages[], int page_frames) {
    
    int faults = 0;

    Queue<Integer> currentFrames = new LinkedList<>();

    for (int i = 0; i<allPages.length; i++){

      boolean checker = false;

      // Checking if the elem is in the array
      for (int element : currentFrames) {
        if (element == allPages[i]) {
            checker = true;
            break;
        }
      }
      if(checker != true){
        faults ++;
        if(page_frames == currentFrames.size()){
          int x = currentFrames.remove();
          currentFrames.add(allPages[i]);
        } else {
          currentFrames.add(allPages[i]);
        }
      }
    }
    return faults;
  }

  // LRU Algorithm
  public int faultsLRU (int allPages[], int page_frames){
    
    int faults = 0;

    HashMap<Integer, Integer> index = new HashMap<>();
    HashSet<Integer> p = new HashSet<>(page_frames);
       
      for (int i=0; i < allPages.length; i++) {
        if (p.size() < page_frames) {
          // Checking if the page is in the set
          if (!p.contains(allPages[i])) {
            faults++;
            p.add(allPages[i]);        
          }
          index.put(allPages[i], i);
        }
        // When page set is full
        else {
          // Checking if the page is in the set
          if (!p.contains(allPages[i])) {
            int max = Integer.MAX_VALUE;
            int min = Integer.MIN_VALUE;
              
            Iterator<Integer> itr = p.iterator();
              
            while (itr.hasNext()) {
              int tmp = itr.next();
              if (index.get(tmp) < max) {
                  max = index.get(tmp);
                  min = tmp;
              }
            }
            faults++;
            index.remove(min);
            p.remove(min);
            p.add(allPages[i]);
          }
          index.put(allPages[i], i);
        }
      }
    return faults;
  }

  public static void main(String[] args) {
    // Generating the number of page frames.
    Random rand = new Random(); 
    int page_frames = rand.nextInt(7 - 1) + 1;
    System.out.println("----------------------------------------------------");
    System.out.println("Number of Frames: " + Integer.toString(page_frames));

    // Creation of page numbers.
    int[] allPages = new int[13];
    for (int i = 0; i < allPages.length; i++) {
       allPages[i] = rand.nextInt(10);
    }

    // Print the array of selected pages.
    System.out.println("----------------------------------------------------");
    System.out.print("List of pages: ");
    for (int element: allPages) {
      System.out.print(Integer.toString(element) + " " );
    }
    System.out.println("\n----------------------------------------------------");

    // FIFO
    System.out.print("\n----------------------- FIFO -----------------------");
    System.out.print("\nNumber of Page Faults: ");
    Question2 elem = new Question2();
    int faultsFIFO = elem.faultsFIFO(allPages, page_frames);
    System.out.println(faultsFIFO);
    System.out.println("----------------------------------------------------");

    // LRU
    System.out.print("\n----------------------- LRU ------------------------");
    System.out.print("\nNumber of Page Faults: ");
    Question2 elem2 = new Question2();
    int faultsLRU = elem2.faultsLRU(allPages, page_frames);
    System.out.println(faultsLRU);
    System.out.println("----------------------------------------------------");
  }
}
