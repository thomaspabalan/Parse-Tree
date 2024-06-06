import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

class ParseTrees {
    private final JFrame frame;
    private final JTextField text_1;
    private final JTextField text_2;
    private final JTree myTree;
    private final JLabel ambiguityLabel;
    private final JLabel ambiguitycheck;

    public ParseTrees() {
        frame = new JFrame();
        frame.setTitle("Parse Tree App | Group 2 BSCS-3B");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        // grammar side labels
        JLabel label_1 = new JLabel("Grammar:");
        label_1.setBounds(320, 50, 400, 20);
        JLabel label_2 = new JLabel("<prog> → <stmts>");
        label_2.setBounds(320, 80, 400, 20);
        JLabel label_3 = new JLabel("<stats> → <stmts>|<stmt>");
        label_3.setBounds(320, 100, 400, 20);
        JLabel label_4 = new JLabel("<stat> → <var> = <expr> | <var> = <term>");
        label_4.setBounds(320, 120, 400, 20);
        JLabel label_5 = new JLabel("<var> → a-z | A-Z");
        label_5.setBounds(320, 140, 400, 20);
        JLabel label_6 = new JLabel("<expr> → <term> | <term> + <term> | <term> - <term> ");
        label_6.setBounds(320, 160, 400, 20);
        JLabel label_7 = new JLabel("| <term> * <term> | <term> / <term> | ( <expr> )");
        label_7.setBounds(320, 180, 400, 20);
        JLabel label_8 = new JLabel("<term> → <var> | <const> | <expr> ");
        label_8.setBounds(320, 200, 400, 20);
        JLabel label_9 = new JLabel("<const> → 0|1|2|3|4|5|6|7|8|9");
        label_9.setBounds(320, 220, 400, 20);

        // expression side labels and text fields
        JLabel label_10 = new JLabel("Enter an expression:");
        label_10.setBounds(10, 10, 250, 20);
        text_1 = new JTextField();
        text_1.setBounds(130, 5, 30, 30);
        text_1.setHorizontalAlignment(JTextField.CENTER);
        JLabel label_11 = new JLabel("=");
        label_11.setBounds(165, 10, 10, 20);
        text_2 = new JTextField();
        text_2.setBounds(180, 5, 250, 30);
        text_2.setHorizontalAlignment(JTextField.CENTER);

        JButton btn_1 = new JButton("Enter");
        btn_1.setBounds(450, 5, 100, 30);
        btn_1.setFocusable(false);
        JButton btn_2 = new JButton("Clear");
        btn_2.setBounds(560, 5, 100, 30);
        btn_2.setFocusable(false);

        //Panel component for ambiguity checker
        JPanel panel = new JPanel();
        panel.setBounds(320, 270, 350, 380);
        panel.setLayout(null);
        panel.setBackground(Color.black);

        ambiguitycheck = new JLabel("The Grammar Expression is: ");
        ambiguitycheck.setBounds(10, 20, 300, 40);
        ambiguitycheck.setFont(new Font("Monospaced",Font.BOLD, 16));
        ambiguitycheck.setForeground(Color.green);

        ambiguityLabel = new JLabel("------------------------");
        ambiguityLabel.setBounds(10, 60, 300, 40);
        ambiguityLabel.setFont(new Font("Monospaced",Font.BOLD, 20));
        ambiguityLabel.setForeground(Color.white);

        panel.add(ambiguitycheck);
        panel.add(ambiguityLabel);

        //buttons action listener
        btn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String variable = text_1.getText().trim(); // Get the variable from text field 1
                String expression = text_2.getText().trim(); // Get the expression from text field 2

                if (variable.isEmpty() || expression.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Both variable and expression are required!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                // Syntax validation
                if (!isValidExpression(expression)) {
                    JOptionPane.showMessageDialog(frame, "Invalid expression syntax!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                // Check if the expression follows grammar rules
                if (!isValidGrammarExpression(expression, 2)) {
                    JOptionPane.showMessageDialog(frame, "Expression does not follow grammar rules!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                // Syntax validation for variable
                if (!isValidExpression(variable)) {
                    JOptionPane.showMessageDialog(frame, "Invalid variable syntax!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }

                // Check if the variable follows grammar rules
                if (!isValidGrammarExpression(variable, 1)) {
                    JOptionPane.showMessageDialog(frame, "Variable does not follow grammar rules!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Stop further execution
                }


                // Parse the expression and update the parse tree
                updateParseTree(variable, expression);
                JOptionPane.showMessageDialog(frame, "Expression successfully added to parse tree!", "Info",
                        JOptionPane.INFORMATION_MESSAGE);

                boolean isAmbiguous = checkAmbiguity();
                ambiguityLabel.setText(isAmbiguous ? "Ambiguous Expression" : "Unambiguous Expression");

                // disabled the button and text fields after using it.
                btn_1.setEnabled(false);
                text_1.setEnabled(false);
                text_2.setEnabled(false);
            }
        });


        btn_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear the parse tree?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Clear text fields and remove the parse tree
                    text_1.setText("");
                    text_2.setText("");

                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) myTree.getModel().getRoot();
                    root.removeAllChildren();

                    DefaultTreeModel model = new DefaultTreeModel(root);
                    myTree.setModel(model);
                    root.add(new DefaultMutableTreeNode("stmts"));
                    ambiguityLabel.setText("------------------------");
                    JOptionPane.showMessageDialog(frame, "Parse tree has been reset!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    btn_1.setEnabled(true);
                    text_1.setEnabled(true);
                    text_2.setEnabled(true);
                }
            }
        });

        // Initialize JTree for Parse tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("prog");
        root.add(new DefaultMutableTreeNode("stmts"));
        myTree = new JTree(root);

        // Create a label for indicating JTree structure
        JLabel treeLabel = new JLabel("JTree Structure");
        treeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Wrap the JTree and the label in a JPanel
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.add(treeLabel, BorderLayout.NORTH);
        treePanel.add(myTree, BorderLayout.CENTER);

        // Wrap the JPanel in a JScrollPane
        JScrollPane treeScrollPane = new JScrollPane(treePanel);
        treeScrollPane.setBounds(10, 40, 300, 610);
        frame.add(treeScrollPane);


        frame.add(label_1);
        frame.add(label_2);
        frame.add(label_3);
        frame.add(label_4);
        frame.add(label_5);
        frame.add(label_6);
        frame.add(label_7);
        frame.add(label_8);
        frame.add(label_9);
        frame.add(label_10);

        frame.add(text_1);
        frame.add(label_11);
        frame.add(text_2);
        frame.add(btn_1);
        frame.add(btn_2);
        frame.add(panel);
        frame.setVisible(true);
    }

    // expression is the inputted variable/expression, and source is which text field it came from (1 = variable, 2 = expression)
    private boolean isValidGrammarExpression(String expression, int source) {
        // Remove whitespace
        expression = expression.replace(" ", "");
        // Split the expression by operators, parentheses, and whitespace
        String[] tokens = expression.split("(?<=[-+*/()\\s])|(?=[-+*/()\\s])");

        // Check if each token follows the grammar rules
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            // If the token is not empty after trimming
            if (!token.isEmpty()) {
                // If the token is an operator or parenthesis
                if (token.matches("[-+*/()]")) {
                    // Check for consecutive operators
                    if (i > 0 && token.matches("[-+*/]") && tokens[i - 1].matches("[-+*/]")) {
                        return false; // Consecutive operators found, return false
                    }
                    // Check if an operator is at the beginning or end of the expression
                    if (token.matches("[-+*/]") && (i == 0 || i == tokens.length - 1)) {
                        return false; // Operator at the beginning or end, return false
                    }
                } else {
                    // If the token is not an operator, it should be a valid character
                    if (source == 1) { // if token came from variable text field
                        if (!token.matches("[a-zA-Z0-9]+")) {
                            return false; // Token does not follow grammar rules
                        }
                    } else { // if token came from expression text field
                        if (token.length() != 1 || !token.matches("[a-zA-Z0-9]")) {
                            return false; // Token does not follow grammar rules
                        }
                    }
                }
            }
        }
        return true;
    }


    // if user input only 1 parenthesis within the expression it will show an error message.
    private boolean isValidExpression(String expression) {
        // Count the number of opening and closing parentheses
        int openParentheses = 0;
        int closeParentheses = 0;

        for (char c : expression.toCharArray()) {
            if (c == '(') {
                openParentheses++;
            } else if (c == ')') {
                closeParentheses++;
            }
        }

        // Check if the number of opening and closing parentheses matches
        return openParentheses == closeParentheses;
    }

    private void updateParseTree(String variable, String expression) {
        DefaultMutableTreeNode varNode;
        if (expression.matches("\\d+")) {
            // If expression is a number, create a "const" node instead of "var" node
            varNode = new DefaultMutableTreeNode("const");
        } else {
            varNode = new DefaultMutableTreeNode("var");
        }
        varNode.add(new DefaultMutableTreeNode(variable));

        DefaultMutableTreeNode exprNode = new DefaultMutableTreeNode("expr");
        parseExpression(exprNode, expression);

        DefaultMutableTreeNode stmtNode = new DefaultMutableTreeNode("stmt");
        stmtNode.add(varNode);
        stmtNode.add(new DefaultMutableTreeNode("="));
        stmtNode.add(exprNode);

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) myTree.getModel().getRoot();
        ((DefaultMutableTreeNode) root.getChildAt(0)).insert(stmtNode, 0);

        myTree.repaint();
    }


    private void parseExpression(DefaultMutableTreeNode parentNode, String expression) {
        // Split the expression by operators, parentheses, and whitespace
        String[] tokens = expression.split("(?<=[-+*/()\\s])|(?=[-+*/()\\s])");

        Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
        nodeStack.push(parentNode);

        for (String token : tokens) {
            if (token.isEmpty() || token.trim().isEmpty()) {
                continue;
            }

            if (token.matches("[a-zA-Z]")) {
                DefaultMutableTreeNode termNode = new DefaultMutableTreeNode("term");
                DefaultMutableTreeNode varNode = new DefaultMutableTreeNode("var");
                varNode.add(new DefaultMutableTreeNode(token));
                termNode.add(varNode);
                nodeStack.peek().add(termNode);
            } else if (token.matches("\\d+")) {
                DefaultMutableTreeNode termNode = new DefaultMutableTreeNode("term");
                DefaultMutableTreeNode constNode = new DefaultMutableTreeNode("const");
                constNode.add(new DefaultMutableTreeNode(token));
                termNode.add(constNode);
                nodeStack.peek().add(termNode);
            } else if (token.matches("[-+*/]")) {
                nodeStack.peek().add(new DefaultMutableTreeNode(token));
            } else if (token.equals("(")) {
                DefaultMutableTreeNode exprNode = new DefaultMutableTreeNode("expr");
                DefaultMutableTreeNode openParenNode = new DefaultMutableTreeNode("(");
                nodeStack.peek().add(openParenNode);
                nodeStack.peek().add(exprNode);
                nodeStack.push(exprNode);
            } else if (token.equals(")")) {
                nodeStack.pop();
                DefaultMutableTreeNode closeParenNode = new DefaultMutableTreeNode(")");
                nodeStack.peek().add(closeParenNode);
            }
        }
    }


    private boolean checkAmbiguity() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) myTree.getModel().getRoot();
        return isAmbiguous(root);
    }

    private boolean isAmbiguous(DefaultMutableTreeNode node) {
        if (node == null || node.isLeaf()) {
            return false; // Leaf nodes or null nodes are not ambiguous
        }

        // Check for ambiguity based on grammar rules and example inputs and outputs
        if (isAmbiguousExpression(node)) {
            return true; // Ambiguity detected
        }

        // Recursively check ambiguity in child nodes
        Enumeration<?> enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration.nextElement();
            if (isAmbiguous(child)) {
                return true; // If any child is ambiguous, the whole subtree is ambiguous
            }
        }

        return false;
    }

    private boolean isAmbiguousExpression(DefaultMutableTreeNode node) {
        // Check if the node represents an expression
        if (node.getUserObject().toString().equals("expr")) {
            int termCount = countTerms(node);
            if (termCount > 2) {
                // Check if there are multiple terms, indicating potential ambiguity
                return true;
            }
        }
        return false;
    }

    private int countTerms(DefaultMutableTreeNode node) {
        int count = 0;
        int maxConsecutiveTerms = 0;
        Enumeration<?> enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration.nextElement();
            if (child.getUserObject().toString().equals("term")) {
                count++;
                if (count > maxConsecutiveTerms) {
                    maxConsecutiveTerms = count;
                }
            } else if (child.getUserObject().toString().equals("expr")) {
                // Reset count when encountering an expression within parenthesis
                return 0;
            }
        }
        return maxConsecutiveTerms;
    }
}

public class PLv2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ParseTrees();
            }
        });
    }
}
