Linda Tang
COMS3134 Final Capstone Project 

A program that finds the n most common words in a document, using an
implemented BSTMap, AVLTreeMap, or MyHashMap.


I expect that the HashMap will have the best performance, and the BSTMap will have the worst performance.
This is because when retrieving entries in a Hashmap, it takes an average time complexity of Theta(n) to retrieve them, which is the
fastest time complexity out of all three data structures.
We know that it typically takes longer to traverse a regular BST than an AVL, because AVLs self balance themselves and
thus shorten the amount of iterations it takes to reach the desired entry.

These are the results from running all three data structures on the given Bible.txt:

java CommonWordFinder Bible.txt hash 20000  0.84s user 0.12s system 159% cpu 0.597 total
java CommonWordFinder Bible.txt bst 20000  1.23s user 0.19s system 117% cpu 1.205 total
java CommonWordFinder Bible.txt avl 20000  0.97s user 0.14s system 130% cpu 0.847 total

As we can see, the HashMap took the least amount of time of .597s, while BSTMap took the longest time of 1.205s,
which aligns with our predictions on the program's execution speed.