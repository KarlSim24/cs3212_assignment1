import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.debug.*;
import java.lang.Object;
import java.util.Enumeration;
import java.util.ArrayList;

import edu.gwu.algtest.ComparableKeyValuePair;
import edu.gwu.algtest.TreeNode;


class keyEnumerator implements Enumeration {
	ArrayList<java.lang.Comparable> keyArray;
	int arrIdx;

	public keyEnumerator (ArrayList<java.lang.Comparable> keyArray){
		this.keyArray = keyArray;
		arrIdx = 0;
	}

        //Enumeration Interface
        public boolean hasMoreElements(){
	    if(arrIdx >= keyArray.size()){
		    return false;
	    }
	    return true;
        }

        public java.lang.Comparable nextElement(){
	    java.lang.Comparable keyToReturn = keyArray.get(arrIdx);
	    arrIdx++;
	    return keyToReturn;
        }
}
class valEnumerator implements Enumeration {
        ArrayList<java.lang.Object> valArray;
	int arrIdx;

        public valEnumerator (ArrayList<java.lang.Object> valArray){
                this.valArray = valArray;
		arrIdx = 0;
        }

        //Enumeration Interface
        public boolean hasMoreElements(){
            if(arrIdx >= valArray.size()){
                    return false;
            }
            return true;	    
        }

        public java.lang.Object nextElement(){
            java.lang.Object valToReturn = valArray.get(arrIdx);
            arrIdx++;
            return valToReturn;        
	}
}



public class MyBinarySearchTree implements TreeSearchAlgorithm {
    
    //Algorithm Interface
    public java.lang.String getName(){
        return "Karl Simon's MyBinarySearchTree";
    }
    public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop){
        return;
    }

    public static edu.gwu.algtest.TreeNode root;
    public static int maxSize;
    public static int curTreeSize; //size of tree
    
    public static ArrayList<java.lang.Comparable> keyArray;
    public static ArrayList<java.lang.Object> valArray;
    
    //SearchAlgorithm Interface
    public void initialize(int maxSize){
        MyBinarySearchTree.maxSize = maxSize;
	curTreeSize = 0;
	keyArray = new ArrayList<java.lang.Comparable>();
	valArray = new ArrayList<java.lang.Object>();
	root = null; //initialize new root
        return;
    }

    public int getCurrentSize(){
        return curTreeSize;
    }

    public java.util.Enumeration getKeys(){
	return new keyEnumerator(keyArray);
    }

    public java.util.Enumeration getValues(){
	return new valEnumerator(valArray);
    }

    //OrderedSearchAlgorithm Interface
    public java.lang.Object insert(java.lang.Comparable key, java.lang.Object value){
	if(curTreeSize > maxSize){
		return null; //tree too large
	}
	if(root != null){//if root exists
            //check if key already exists
            edu.gwu.algtest.ComparableKeyValuePair searchNode = search(key);
            if(searchNode != null){ //if key exists, update value
                java.lang.Object oldVal = searchNode.value;
		valArray.remove(oldVal);
		valArray.add(value);
                searchNode.value = value; //update value
                return oldVal;
            }
            //node doesn't already exist, so search for place to insert
            recursiveInsert(root, key, value);
            //here, new TreeNode is inserted
            return null;            
        }
        else {
            //create root
            root = new edu.gwu.algtest.TreeNode(key, value);
	    //System.out.println("created root");
	    curTreeSize++;
	    keyArray.add(key);
	    valArray.add(value);
            return null;
        }

    }

    public void recursiveInsert(edu.gwu.algtest.TreeNode curNode, java.lang.Comparable key, java.lang.Object value){
        if(key.compareTo(curNode.key) < 0){ //check left branch
            if(curNode.left != null){ //go left
                recursiveInsert(curNode.left, key, value);
            }
            else{ //insert here
                edu.gwu.algtest.TreeNode insertNode = new edu.gwu.algtest.TreeNode(key, value);
		curNode.left = insertNode;
		insertNode.parent = curNode;
		curTreeSize++;
		keyArray.add(key);
		valArray.add(value);
                return;
            }
        }
        else{ //check right branch
            if(curNode.right != null){ //go right
                recursiveInsert(curNode.right, key, value);
            }
            else{ //insert here
                edu.gwu.algtest.TreeNode insertNode = new edu.gwu.algtest.TreeNode(key, value);
		curNode.right = insertNode;
		insertNode.parent = curNode;
		curTreeSize++;
                keyArray.add(key);
                valArray.add(value);		
                return;
            }
        }
    }



    public edu.gwu.algtest.ComparableKeyValuePair search(java.lang.Comparable key){
        //start at root
        edu.gwu.algtest.TreeNode curNode = root;
        while(curNode != null){
            //if node already exits, update KVP of treenode
            if(key.compareTo(curNode.key) == 0){
                return curNode;
            }
            else if(key.compareTo(curNode.key) < 0){
                curNode = curNode.left;
            }
            else{
                curNode = curNode.right;
            }
        }
        return null; //key not in tree
    }

    public edu.gwu.algtest.ComparableKeyValuePair minimum(){
        //go left as far as possible
        edu.gwu.algtest.TreeNode curNode = root;
	//printTree(root);
	//System.out.println("tree size = " + curTreeSize);
        while(curNode != null){
            if(curNode.left != null){
                curNode = curNode.left;
            }
            else{
                return curNode;
            }
        }
        return null;
    }

    public edu.gwu.algtest.ComparableKeyValuePair maximum(){
        //go right as far as possible
        edu.gwu.algtest.TreeNode curNode = root;
        while(curNode != null){
            if(curNode.right != null){
                curNode = curNode.right;
            }
            else{
                return curNode;
            }
        }
        return null;
    }

    public java.lang.Object delete(java.lang.Comparable key){
	 //search for node with node.key == key;
	 edu.gwu.algtest.TreeNode curNode = root; //QUESTION: how to directly use search method?
         while(curNode != null){
             //if node already exits, update KVP of treenode
             if(key.compareTo(curNode.key) == 0){
                 break;
             }
             else if(key.compareTo(curNode.key) < 0){
                 curNode = curNode.right;
             }
             else{
                 curNode = curNode.right;
             }
         }

        if(curNode != null){ //we found node for given key
		//CASE1: no children
		if(curNode.left == curNode.right){
			if(curNode.parent != null){
				if(curNode.parent.right == curNode){ //curNode is right child
					curNode.parent.right = null;
					return curNode.value;
				}	
				else{ //curNode is left child
					curNode.parent.left = null;
					return curNode.value;
				}	
			}
			else{ //node is root, so remove root
				root = null;
				return curNode.value;
			}
		}	
		//CASE2a: node has left child
		else if(curNode.left != null && curNode.right == null) {
			if(curNode.parent != null){
                                if(curNode.parent.right == curNode){ //curNode is right child
                                        curNode.parent.right = curNode.left;
                                        return curNode.value;
                                }
                                else{ //curNode is left child
                                        curNode.parent.left = curNode.left;
                                        return curNode.value;
                                }
			}
			else { //curNode is root
				root = curNode.left;
				return curNode.value;
			}
		}
		//CASE2b: node has right child
		else if(curNode.right != null && curNode.left == null){
                        if(curNode.parent != null){
                                if(curNode.parent.right == curNode){ //curNode is right child
                                        curNode.parent.right = curNode.right;
                                        return curNode.value;
                                }
                                else{ //curNode is left child
                                        curNode.parent.left = curNode.right;
                                        return curNode.value;
                                }
                        }
                        else { //curNode is root
                                root = curNode.right;
                                return curNode.value;
                        }
		}
		//CASE3: node has two children
		else{
			//find curNode's successor (search())
			java.lang.Comparable sucKey = successor(curNode.key);
			//suc can't be null (because node has two children)
			 edu.gwu.algtest.TreeNode sucNode = root; //QUESTION: how to use search method
		         while(sucNode != null){
		             //if node already exits, update KVP of treenode
		            if(sucKey.compareTo(sucNode.key) == 0){
		                 break;
		             }
		             else if(sucKey.compareTo(sucNode.key) < 0){
		                 sucNode = sucNode.right;
		             }
		             else{
                 		sucNode = sucNode.right;
             		     }	
		         }
			 //sucNode is set
             //TODO: finish implementing CASE3
			return null;
		}

	}
	
	return null; //key not found
    }

    public java.lang.Comparable successor(java.lang.Comparable key){
	 //search for node
         //QUESTION: how to use search() method instead?
	 edu.gwu.algtest.TreeNode curNode = root;
         while(curNode != null){
             //if node already exits, update KVP of treenode
             if(key.compareTo(curNode.key) == 0){
                 break;
             }
             else if(key.compareTo(curNode.key) < 0){
                 curNode = curNode.right;
             }
             else{
                 curNode = curNode.right;
             }
         }

	 if(curNode != null){
		if(curNode.right != null){ //1. right child exists
			curNode = curNode.right; //go right child (make it root)
		
        		while(curNode != null){ //search for minimum
            			if(curNode.left != null){
                			curNode = curNode.left;
            			}
            			else{
                			return curNode.key;
            			}	
        		}	
		}
		else if(curNode.parent == null){ //2.1 node is root
			return null;
		}
		else { //2.2 node is not root
			//go up right links until reach left link
			while(curNode.parent != null){
                                if(curNode.parent.left != null){
                                        return curNode.parent.key; //reached successor
                                }
				//keep going up links
				curNode = curNode.parent;
			}
			return null;
		}
	 }
	 else{ //key not in tree
		 return null;
	 }
	 return null;
    }

    public java.lang.Comparable predecessor(java.lang.Comparable key){
       	//search for node
         
         //QUESTION: how to use search() method instead?
         edu.gwu.algtest.TreeNode curNode = root;
         while(curNode != null){
             //if node already exits, update KVP of treenode
             if(key.compareTo(curNode.key) == 0){
                 break;
             }
             else if(key.compareTo(curNode.key) < 0){
                 curNode = curNode.right;
             }
             else{
                 curNode = curNode.right;
             }
         }

	
	if(curNode != null){
                if(curNode.left != null){ //1. left child exists
                        curNode = curNode.left; //go left child (make it root)

                        while(curNode != null){ //search for minimum
                                if(curNode.right != null){
                                        curNode = curNode.right;
                                }
                                else{
                                        return curNode.key;
                                }
                        }
                }
                else if(curNode.parent == null){ //2.1 node is root
                        return null;
                }
                else { //2.2 node is not root
                        //go up left links until reach right link
                        while(curNode.parent != null){
                                if(curNode.parent.right != null){
                                        return curNode.parent.key; //reached predecessor
                                }
                                //keep going up links
                                curNode = curNode.parent;
                        }
                        return null;
                }
         }
         else{ //key not in tree
                 return null;
         }
	 return null;
    }

    //TreeSearchAlgorithm
    public edu.gwu.algtest.TreeNode getRoot(){
        return root;
    }
	
    public void printTree(edu.gwu.algtest.TreeNode curNode){
	if(curNode == null){
		return;
	}
	// System.out.println(curNode);

	printTree(curNode.left);
	printTree(curNode.right);
    }

}
