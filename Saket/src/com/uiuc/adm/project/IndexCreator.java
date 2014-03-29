package com.uiuc.adm.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import com.uiuc.adm.btree.BTree;
import com.uiuc.adm.btree.DBAbstract;
import com.uiuc.adm.btree.DBMaker;

public class IndexCreator {
	
	public static final String testFolder = "D:\\testdb";
	DBAbstract db;
    BTree tree;
	
	String input="", output="";
	
	int low=Integer.MAX_VALUE, high=Integer.MIN_VALUE;
	String attribute_name = "creative_id", datatype="int";
	
	BufferedWriter bw = null;
	IndexCreator(String input, String output) throws IOException
	{
		this.input = input;
		this.output = output;
		try {
			bw = new BufferedWriter(new FileWriter(new File(output), false));
			bw.write("");
		
			bw.close();
			bw = new BufferedWriter(new FileWriter(new File(output), true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Method which sets the ball rolling
		createIndex();
	}
	
	void createIndex() throws IOException
	{
		
		instantiateBTree();
		//read dataset and create BTree
		AddToBTree();
		//display the btree created
		displayBTree();
		//some testing of retrievals, by value, ranges etc
		testRetrieve();
		//Write metadata to file 
		writeMetadata();
		//Write btree to file
		serializeBTreeToFile();
		//read btree back from file
		deserializeBTreeFromFile();
		//some testing with btree read from file
		testRetrieve();
		
		db.close();
	}
	
	

	

	//write meta data like attribute name, attribute type, size
	private void writeMetadata() 
	{
		System.out.println("writing meta data");
		try {
			bw.write(attribute_name+","+datatype+","+low+","+high+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	void instantiateBTree() throws IOException
	{
		System.out.println("instantiating btree");
        db = newDBCache();
        tree = BTree.createInstance(db);
	}
	
	//reads each tuple and creates btree, with attribute_value as key
	//and an arraylist of offsets as value
	private void AddToBTree() throws IOException {
		try {
			//int lineNo = 0;
			
			Scanner in = new Scanner(new File(input));
			
			RandomAccessFile aFile=new RandomAccessFile(input, "rw");
			
			while (in.hasNext())
			{
				//lineNo++;
				String line= in.nextLine();
				
				long offset = aFile.getFilePointer();
				aFile.readLine();
				
				
				//System.out.println(offset+" "+l+" "+l.length());
				String[] array = line.split(",");
			
				
				//insert in Btree the key and value (indexed attribute and offset)
				//insertIntoBTree(array[6], lineNo);
					
				if (Integer.parseInt(array[6])<low) low=Integer.parseInt(array[6]);
				if (Integer.parseInt(array[6])>high) high=Integer.parseInt(array[6]);
				
				if (tree.get(array[6])==null)
				{
					ArrayList<Long> value = new ArrayList<Long>();
					value.add(offset);
					tree.insert(array[6], value, false);
				}
				else
				{
					ArrayList<Long> value = (ArrayList<Long>) tree.get(array[6]);
					value.add(offset);
					tree.insert(array[6], value, true);
				}
			}
			//aFile.seek(525);
			//System.out.println(aFile.readLine());
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayBTree() throws IOException {
		BTree.BTreeTupleBrowser browser = tree.browse();
        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
        while (browser.getNext(tuple)) 
        {
        	System.out.println(tuple.key+" "+tuple.value);
        }	
	}
	
	private void serializeBTreeToFile() throws IOException 
	{
		BTree.BTreeTupleBrowser browser = tree.browse();
        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
        bw = new BufferedWriter(new FileWriter(new File(output), true));
        while (browser.getNext(tuple)) 
        {
        	//System.out.println(tuple.key+" "+tuple.value);
        	bw.write(tuple.key+"");
        	ArrayList<Long> tmp = (ArrayList<Long>) tuple.value;
        	for (Long i : tmp)
        	{
        		bw.write(","+i);
        	}
        	bw.write("\n");
        }	
        bw.close();
		
	}

	private void deserializeBTreeFromFile() throws IOException 
	{
		Scanner in = new Scanner(new File(output));
		String line = null;
		String attribute_name, datatype;
		int low, high;
		
		
		tree.clear();
		
		if (in.hasNext())
		{
			line = in.nextLine();
			String[] array = line.split(",");
			attribute_name = array[0];
			datatype = array[1];
			low = Integer.parseInt(array[2]); high = Integer.parseInt(array[3]);
			
			while (in.hasNext())
			{
				line=in.nextLine();
				String[] node = line.split(",");
				ArrayList<Long> value = new ArrayList<Long>();
				
				for (int i=1; i<node.length; i++)
				{
					value.add(Long.parseLong(node[i]));
				}
				tree.insert(node[0], value, false);
			}
			
		}
		
	}
	
	void testRetrieve() throws IOException
	{
		ArrayList<Long> values = null;
		
		values = retrieveIndex("105078044", 1);
		displayResults(values);
		values = retrieveIndex("104622301", 1);
		displayResults(values);
		values = retrieveIndex("105078044", 2);
		displayResults(values);
	//	System.out.println("here");
		values = retrieveIndex("105078044", 3);
		displayResults(values);
		
		values = retrieveIndex("103890355", "105717602", 1);
		displayResults(values);
		
	}
	
	void displayResults(ArrayList<Long> values)
	{
		for (Long i : values)
			System.out.print(i+" ");
		System.out.println();
	}


	/*
	 * 
	 * Mode 1 : return equal to key1
	 * Mode 2 : return greater than key1
	 * Mode 3 : return less than key1
	 */
	@SuppressWarnings("unchecked")
	ArrayList<Long> retrieveIndex(String key, int mode) throws IOException
	{
		ArrayList<Long> result = null;
		switch(mode)
		{
			case 1:
			{
				result =  (ArrayList<Long>) tree.get(key);
				break;
			}
				
			case 2:
			{
				BTree.BTreeTupleBrowser browser = tree.browse(key, false);
		        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
		        result = new ArrayList<Long>();
		        while (browser.getNext(tuple)) 
		        {
		        	ArrayList<Long> tmp = (ArrayList<Long>) tuple.value;
		        	for (Long i : tmp)
		        	{
		        		result.add(i);
		        	}
		        	//System.out.println(tuple.key+" "+tuple.value);
		        }
		        break;
			}
			case 3:
			{
				BTree.BTreeTupleBrowser browser = tree.browse();
		        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
		        result = new ArrayList<Long>();
		        while (browser.getNext(tuple)) 
		        {
		        	if (((String)tuple.key).compareTo(key)>=0) break;
		        	ArrayList<Long> tmp = (ArrayList<Long>) tuple.value;
		        	for (Long i : tmp)
		        	{
		        		result.add(i);
		        	}
		        }
		        break;
			}
		}
		return result;
	}
	
	
	ArrayList<Long> retrieveIndex(String key1, String key2, int mode) throws IOException
	{
		ArrayList<Long> result = new ArrayList<Long>();
		
		//start at key1
		BTree.BTreeTupleBrowser browser = tree.browse(key1, false);
        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
        while (browser.getNext(tuple)) 
        {
        	//stop at key2
        	if (((String)tuple.key).compareTo(key2)>=0) break;
        	
        	ArrayList<Long> tmp = (ArrayList<Long>) tuple.value;
        	
        	for (Long i : tmp)
        	{
        		result.add(i);
        	}
        	//System.out.println(tuple.key+" "+tuple.value);
        }
		return result;
	}
	
	
	
	 static public String newTestFile() 
	 {
		 return testFolder + File.separator + "test" + System.nanoTime();
	 }
	
	static public DBAbstract newDBCache() throws IOException {
     return (DBAbstract) DBMaker.openFile(newTestFile()).make();
 }
}
