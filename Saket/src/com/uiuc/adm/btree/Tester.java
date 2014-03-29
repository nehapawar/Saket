package com.uiuc.adm.btree;



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.concurrent.locks.Lock;

import com.uiuc.adm.btree.BTree.BTreeTuple;
import com.uiuc.adm.btree.DBAbstract;
import com.uiuc.adm.btree.DBMaker;

public class Tester {
	//public static final String testFolder = System.getProperty("java.io.tmpdir", ".") + "_testdb";
	public static final String testFolder = "D:\\testdb";
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		/*DBAbstract db;
        BTree<String, String> tree;
        //byte[] test, test0, test1, test2, test3;
       // byte[] value1, value2;
        String one="one", two="two", three="three", four="four", five="five";
        String a="1", b="2", c="3", d="4", e="5";

        Comparator<String> comparator = new Comparator<String>() {
			
			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				//return o1.compareTo(o2)
				return o1.compareTo(o2);
			}
		};
        db = newDBCache();
        tree = BTree.createInstance(db, comparator,null,null,true);
        tree.insert(one, a, false);
        tree.insert(two, b, false);
        
        String result;
        result =  tree.get(three);
        System.out.println("three : "+result);
        if (result != null) {
            throw new Error("three shouldn't be found");
        }
        result =  tree.get(one);
        System.out.println("one : "+result);
        if (result == null || comparator.compare(result, a)!=0) 
        {
            throw new Error("Invalid value for one: " + result);
        }

        result = tree.get(two);
        System.out.println("two : "+result);
        if (result == null || comparator.compare(result, b)!=0) 
        {
            throw new Error("Invalid value for two: " + result);
        }

        result = tree.get(four);
        System.out.println("four : "+result);
        if (result != null) {
            throw new Error("four shouldn't be found");
        }

        db.close();*/
		
		
		
		
		
		
		
		String one="one", two="two", three="three", four="four", five="five";
        //String a="1", b="2", c="3", d="4", e="5";
		DBAbstract db;
        BTree tree;
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        ArrayList<Integer> c = new ArrayList<Integer>();
        ArrayList<Integer> hold = null;
        a.add(1); a.add(2);
        b.add(10); b.add(20); b.add(30);


        db = newDBCache();
        tree = BTree.createInstance(db);

        // insert differnt objects and retrieve them
        tree.insert(one, a, false);
        hold = (ArrayList<Integer>) tree.get(one);
        System.out.println("printing one");
        for (Integer i : hold)
        {
        	System.out.print(i+" ");
        }
        System.out.println();
        
        
        tree.insert(two, b, false);
        hold = (ArrayList<Integer>) tree.get(two);
        System.out.println("printing two");
        for (Integer i : hold)
        {
        	System.out.print(i+" ");
        }
        System.out.println();
        
        
        tree.insert(three, c, false);
        hold = (ArrayList<Integer>) tree.get(three);
        System.out.println("printing three");
        for (Integer i : hold)
        {
        	System.out.print(i+" ");
        }
        System.out.println();
       
        
        if (tree.get(two)==null)
        {
        	ArrayList<Integer> d = new ArrayList<Integer>();
        	d.add(20);
        	tree.insert(two, d, false);
        }
        else
        {
        	ArrayList<Integer> tmp = (ArrayList<Integer>) tree.get(two);
        	tmp.add(27);
        	tree.insert(two, tmp, true);
        }
        hold = (ArrayList<Integer>) tree.get(two);
        System.out.println("printing two again");
        for (Integer i : hold)
        {
        	System.out.print(i+" ");
        }
        System.out.println();
        
        
        
        if (tree.get(four)==null)
        {
        	ArrayList<Integer> d = new ArrayList<Integer>();
        	d.add(20);
        	tree.insert(four, d, false);
        }
        else
        {
        	ArrayList<Integer> tmp = (ArrayList<Integer>) tree.get(four);
        	tmp.add(27);
        	tree.insert(four, tmp, true);
        }
        hold = (ArrayList<Integer>) tree.get(four);
        System.out.println("printing four");
        for (Integer i : hold)
        {
        	System.out.print(i+" ");
        }
        System.out.println();
        //tree.insert(two, "value3", false);
        //tree.insert("myownobject", new ObjectTT(new Integer(234)), false);

       /* if ("value1".equals(tree.get("test1")))
        	System.out.println("yes!");
        if ("value2".equals(tree.get("test2")))
        	System.out.println("oh yeah!");
        if ((new Integer(1)).equals(tree.get("one")))
        	System.out.println("bingo!");
        if ((new Long(2)).equals(tree.get("two")))
        	System.out.println("all clear!");*/
        
        /*tree.remove("test1");
        System.out.println(tree.get("test1"));*/
        
        
       // System.out.println(tree.get("test2"));
        
        
        /*BTreeTuple tupe = new BTreeTuple();
        tree.browse("test2", true).getNext(tupe);
        System.out.println(tupe.key+" "+tupe.value);
        
        tree.browse().getNext(tupe);
        System.out.println(tupe.key+" "+tupe.value);
        tree.browse().getNext(tupe);
        System.out.println(tupe.key+" "+tupe.value);*/
        
        
        BTree.BTreeTupleBrowser browser = tree.browse();
        BTree.BTreeTuple tuple = new BTree.BTreeTuple();
        while (browser.getNext(tuple)) 
        {
        	System.out.println(tuple.key+" "+tuple.value);
        }
        
        //System.out.println(tree.findGreaterOrEqual("test").key);

        db.close();
		
		
		
		
        System.out.println("done");
	}
	
	 static public String newTestFile() {
	        return testFolder + File.separator + "test" + System.nanoTime();
	    }
	
	static public DBAbstract newDBCache() throws IOException {
        return (DBAbstract) DBMaker.openFile(newTestFile()).make();
    }
	

}
