import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;



public class Dictionary {
    private AVLTree<String> avlTree;


    public Dictionary(String s) {
        avlTree = new AVLTree<>();
        avlTree.insertAVL(s);
    }


    public Dictionary() {
        avlTree = new AVLTree<>();
    }


    public Dictionary(File f) {
        avlTree = new AVLTree<>();
        loadDictionaryFromFile(f);
    }

    public void addWord(String s)  {
        if (avlTree.isInTree(s)) {
            System.out.println("Word already exists in the dictionary");
        }
        avlTree.insertAVL(s);
    }

    public boolean findWord(String s) {
        if (!avlTree.isInTree(s)) {
            System.out.println("Word not found in the dictionary");
        }else{
            System.out.println("Word found in the dictionary");
        }
        return avlTree.isInTree(s);
    }

    public void deleteWord(String s) {
        if (!avlTree.isInTree(s)) {
            System.out.println("Word not found to delete");
        }
        avlTree.deleteAVL(s);
    }

    public Queue<String> findSimilar(String s) {
        Queue<String> similarWords = new Queue<>();
        findSimilarWords(avlTree.root, s, similarWords);
        return similarWords;
    }

    private void findSimilarWords(BSTNode<String> node, String s, Queue<String> similarWords) {
        Stack<BSTNode<String>> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            BSTNode<String> current = stack.pop();

            if (current != null) {
                if (isSimilar(current.el, s)) {
                    similarWords.enqueue(current.el);
                }

                // Push the right and left children onto the stack
                stack.push(current.right);
                stack.push(current.left);
            }
        }
    }

    private  boolean isSimilar(String word1, String word2) {
        int differences = 0;
        int maxDifferences =1;

        int index1 = 0;
        int index2 = 0;
        if (word1.equals(word2))
            return false;

        while (index1 < word1.length() && index2 < word2.length()) {
            if (word1.charAt(index1) != word2.charAt(index2)) {
                differences++;


                if (differences > maxDifferences) {
                    return false;
                }


                if (word1.length() > word2.length()) {
                    index1++;
                } else if (word1.length() < word2.length()) {
                    index2++;
                } else {

                    index1++;
                    index2++;
                }
            } else {

                index1++;
                index2++;
            }
        }


        if(Math.abs(word1.length() - word2.length())<=1)
            return true;
        else
            return false;


    }




    public void saveDictionaryToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            saveWordsToFile(avlTree.root, writer);
            System.out.println("Dictionary saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving dictionary to file: " + e.getMessage());
        }
    }

    private void saveWordsToFile(BSTNode<String> node, FileWriter writer) throws IOException {
        if (node != null) {
            saveWordsToFile(node.left, writer);
            writer.write(node.el + "\n");
            saveWordsToFile(node.right, writer);
        }
    }

    private void loadDictionaryFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                avlTree.insertAVL(line.trim());
            }
            System.out.println("Dictionary loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading dictionary from file: " + e.getMessage());
        }
    }
    
    public class DictionaryMenu {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            Dictionary dictionary = new Dictionary();
            
            while (true) {
                System.out.println("\nDictionary Menu:");
                System.out.println("1. Load Dictionary from File");
                System.out.println("2. Save Dictionary to File");
                System.out.println("3. Add Word");
                System.out.println("4. Delete Word");
                System.out.println("5. Find Word");
                System.out.println("6. Find Similar Words");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
    
                switch (choice) {
                    case 1:
                        System.out.print("Enter file name to load dictionary: ");
                        String loadFileName = scanner.nextLine();
                        File loadFile = new File(loadFileName);
                        dictionary.loadDictionaryFromFile(loadFile);
                        break;
    
                    case 2:
                        System.out.print("Enter file name to save dictionary: ");
                        String saveFileName = scanner.nextLine();
                        dictionary.saveDictionaryToFile(saveFileName);
                        break;
    
                    case 3:
                        System.out.print("Enter the word to add: ");
                        String addWord = scanner.nextLine();
                        dictionary.addWord(addWord);
                        break;
    
                    case 4:
                        System.out.print("Enter the word to delete: ");
                        String deleteWord = scanner.nextLine();
                        dictionary.deleteWord(deleteWord);
                        break;
    
                    case 5:
                        System.out.print("Enter the word to find: ");
                        String findWord = scanner.nextLine();
                        dictionary.findWord(findWord);
                        break;
    
                    case 6:
                        System.out.print("Enter the word to find similar words: ");
                        String similarWord = scanner.nextLine();
                        Queue<String> similarWords = dictionary.findSimilar(similarWord);
                        System.out.println("Similar words: " + similarWords);
                        break;
    
                    case 7:
                        System.out.println("Exiting the program. Goodbye!");
                        scanner.close();
                        return;
    
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        }
    }
    
}
