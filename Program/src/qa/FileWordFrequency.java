package qa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * @author Sabiha S. Asha
 * 
 */

public class FileWordFrequency {
	private static final int MYTHREADS = 30;
 
	static TreeMap<String, TreeMap<String, Integer>> wordFileCount= new TreeMap<String, TreeMap<String, Integer>>();
	
	public static void main(String args[]) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);
		File selectedFile=null;
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = jfc.getSelectedFile();
			System.out.println("Input File selected: "+selectedFile.getAbsolutePath());
		}
		
		System.out.println("Now iterating each file mentioned in the input file...");
		int fcount=0;
		
		try (FileReader reader = new FileReader(selectedFile);
			BufferedReader br = new BufferedReader(reader)) {

			// read line by line
	        String line;
	        while ((line = br.readLine()) != null) {
		        	fcount++;
		        System.out.println("Name of the file#"+fcount+": "+line); 
				
		        Runnable worker = new MyRunnable(line, selectedFile);
		        executor.execute(worker);
            }
	        
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {	 
			}
			System.out.println("\nFinished all threads");
						
			System.out.println("Do you want to select a word and know more? Type yes or no.");
			Scanner in = new Scanner ( System.in );
			switch ( in.next() ) {
				case "yes":
					System.out.println ( "You picked yes. Plz input a word to get more details on that." );
					Scanner sin = new Scanner ( System.in );
					String sword= in.next().trim();
					System.out.println("You have selected the search word: "+sword);
					sin.close();
					int tcount=0;
					//now loop through the Treemap
					Iterator hmIterator = wordFileCount.entrySet().iterator(); 
					while (hmIterator.hasNext()) { 
			            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
			            
			            TreeMap <String, Integer> tmap = (TreeMap<String, Integer>) mapElement.getValue();		            
			            Iterator itr = tmap.entrySet().iterator(); 
			            
			            while(itr.hasNext()) 
			            { 
			                 Map.Entry<String, Integer> entry = (Entry<String, Integer>) itr.next(); 
			                 
			                 if (entry.getKey().equals(sword)){
			                		 System.out.println("Filename "+mapElement.getKey()+" with count: "+ entry.getValue());
			                		 tcount=tcount+entry.getValue();
			                		 break;
			                 }
			            } 
			            	   	
			    	}
			            
			        System.out.println("Total count in all files for the word "+sword+" is "+tcount);
					break;
				case "no":
					System.out.println ( "You picked no. Program will exit now." );
					break;
			}
			in.close();
						
		}		
		
    }
 
	public static class MyRunnable implements Runnable {
		private final String filename;
		private final File inputFile;
 
		MyRunnable(String filename, File inputFile) {
			this.filename = filename;
			this.inputFile = inputFile;
		}
 
		@Override
		public void run () {
			
			File parentFolder = new File(inputFile.getParent());
	        //System.out.println(parentFolder.getPath());
	        File b = new File(parentFolder, filename);
	            
	        Scanner input = null;
			try {
				input = new Scanner(b);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
            // count occurrences
            TreeMap<String, Integer> wordCounts = new TreeMap<String, Integer>();
            while (input.hasNext()) {
                String next = input.next().toLowerCase();
                if (!wordCounts.containsKey(next)) {
                    wordCounts.put(next, 1);
                } else {
                    wordCounts.put(next, wordCounts.get(next) + 1);
                }
            }
            wordFileCount.put(filename, wordCounts);

            System.out.println("Total words found: " + wordCounts.size());
            for (String word : wordCounts.keySet()) {
                int count = wordCounts.get(word);
                
                System.out.println(word + "\t" + count);
                
            }
            System.out.println("****************************");
		}
	}
}
