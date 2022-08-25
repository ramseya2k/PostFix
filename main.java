import java.io.*;
import java.util.*;
public class Main
{
    public static Queuez validOperators = new Queuez('+', '-', '*', '-', '^');
    public static void main(String[] args) throws FileNotFoundException
    {
        Queuez infixExpression = new Queuez();
        readFileToQueue("input.txt", infixExpression);
        infixExpression = infixToPostfix(infixExpression);
        System.out.println(infixExpression.toString());
        System.out.println(postfixToResult(infixExpression));
    }

    public static int postfixToResult(Queuez infixExpression) // converts postfix method to a result
    {
        StackzInt stak = new StackzInt();
        while(!infixExpression.isEmpty())
        {
            char token = infixExpression.dequeue(); // token takes away a char from the queue in the parameter
            if(Character.isDigit(token)) // if token is an operand, put it in the stack
            {
                stak.push(Integer.parseInt(String.valueOf(token)));;
            }
            else if(validOperators.contains(token)) // if token is an operand, pop the stack
            {
                int R = stak.pop();
                int L = stak.pop();
                switch(token) // evaluate
                {
                    case('+'):
                        stak.push(L+R);
                        break;
                    case('-'):
                        stak.push(L-R);
                        break;
                    case('*'):
                        stak.push(L*R);
                        break;
                    case('/'):
                        stak.push(L/R);
                        break;
                    default:
                        throw new RuntimeException("Invalid token, only +,-,*,/ are allowed");
                }
            }
        }
        return stak.pop();
    }
    public static Queuez infixToPostfix(Queuez infixExpression)  // this method converts the infix expression to a postfix
    {
        Queuez postfixExpression = new Queuez();
        Stackz operators = new Stackz();
        while (!infixExpression.isEmpty()) // loops each and every single character until it reaches the end
        {
            char token = infixExpression.dequeue();
            if (Character.isDigit(token)) // if character is digit put it on the new queue
            {
                postfixExpression.enqueue(token);
            }
            else if (validOperators.contains(token)) // if the queue contains a token, evaluate its precedence
            {
                while (!operators.isEmpty()
                        && precedenceOf(operators.peek()) >= precedenceOf(token))
                {
                    postfixExpression.enqueue(operators.pop());
                }
                operators.push(token);
            }
            else if (token == '(') // if token is a '(' put it in the stack
            {
                operators.push(token);
            }
            else if (token == ')') // if token is a ')', pop until it reaches the '('
            {
                while (!operators.isEmpty() && operators.peek() != '(')
                {
                    postfixExpression.enqueue(operators.pop());
                }
                operators.pop(); // remove parenthesis
            }
            else // if something is wrogn throw a RuntimeException
            {
                throw new RuntimeException("Invalid token!");
            }
        }
        while (!operators.isEmpty())
        {
            postfixExpression.enqueue(operators.pop());
        }
        return postfixExpression;
    }

    public static int precedenceOf(char operator) // evaluates precedence of operator
    {
        if (operator == '+' || operator == '-')
        {
            return 2;
        }
        else if (operator == '*' || operator == '/')
        {
            return 3;
        }
        else if (operator == '(')
        {
            return -1;
        }
        throw new RuntimeException("Invalid token, only operators have precedence");
    }
    public static void readFileToQueue(String fileName, Queuez queue) throws FileNotFoundException // imports .txt file into the queue
    {
        try (Scanner file = new Scanner(new File(fileName)))
        {
            while (file.hasNextLine())
            {
                for (char c : file.nextLine().toCharArray())
                {
                    queue.enqueue(c);
                }
            }
        }
    }
}
class Node {
    private char op;
    private int num;
    private Node top;

    Node() {
        top = null;
    }

    Node(char op) // Constructor
    {
        this.op = op;
    }
    Node(int num) // Constructor
    {
        this.num = num;
    }

    public Node getNext() // return the next stack
    {
        return top;
    }

    public void setNext(Node n) // pointer function
    {
        top = n;
    }

    public char getValue() // returns value
    {
        return this.op;
    }
    public int getValueInt() // returns value
    {
        return this.num;
    }
}

class Queuez
{
    private Node front;
    private Node rear;

    public Queuez(char ... values)
    {
        for(char c: values)
        {
            enqueue(c);
        }
    }

    public void enqueue(char c)
    {
        Node newNode = new Node(c);
        newNode.setNext(null);
        if (front == null)
        {
            front = rear = newNode;
        }
        else
        {
            rear.setNext(newNode);
            rear = newNode;
        }
    }

    public char dequeue()
    {
        if (front != null)
        {
            char value  = front.getValue();
            front = front.getNext();
            return value;
        }

        if (front == null)
        {
            rear = null;
        }
        throw new RuntimeException("Queue is empty");
    }

    public Character peek()
    {
        if(front == null)
        {
            return null;
        }
        return front.getValue();
    }

    public boolean contains(char c)
    {
        Node current = front; // temp node
        while (current != null)
        {
            if(current.getValue() == c)
            {
                return true;
            }
            current = current.getNext(); // traverses
        }
        return false;
    }

    public boolean isEmpty()
    {
        return front == null;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Node current = front;
        while (current != null)
        {
            sb.append(current.getValue());
            current = current.getNext();
        }
        return sb.toString();
    }
}
class Stackz
{
    private Node top;
    Stackz()
    {
        top=null;
    }
    public void push(char op)
    {
        Node newNode = new Node(op); // pushes binary to the stack
        newNode.setNext(top); // pointer
        top=newNode;
    }
    public char pop()
    {
        char temp = top.getValue();
        top=top.getNext();
        return temp;
    }
    public boolean isEmpty()
    {
        return top == null;
    }
    public String toString()
    {
        Node curr = top;
        String result = "";
        while(curr!=null)
        {
            result += curr.getValue();
            System.out.print(result);
            curr=curr.getNext();
        }
        return result;
    }
    public char peek() // Returns the top of the stack
    {
        return top.getValue();
    }
}

class StackzInt
{
    private Node top;
    StackzInt()
    {
        top=null;
    }
    public void push(int value)
    {
        Node newNode = new Node(value); // pushes binary to the stack
        newNode.setNext(top); // pointer
        top=newNode;
    }
    public int pop()
    {
        int temp = top.getValueInt();
        top=top.getNext();
        return temp;
    }
    public boolean isEmpty()
    {
        return top == null;
    }
    public String toString()
    {
        Node curr = top;
        String result = "";
        while(curr!=null)
        {
            result += curr.getValueInt();
            System.out.print(result);
            curr=curr.getNext();
        }
        return result;
    }
    public int peek() // Returns the top of the stack
    {
        return top.getValueInt();
    }
}
