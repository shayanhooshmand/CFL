import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class MyGUI {

    private JFrame frame;
    private JPanel intro, activity, advanced;
    private String userNouns, userVerbs, userPreps, userArticles;
    private CFLGenerator grammar;


    public MyGUI() {

        grammar = new CFLGenerator();
        frame = new JFrame();

        createIntroPanel();
        createActivityPanel();
        createAdvancedDialog();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Create Language!");
        frame.pack();
        frame.setVisible(true);

    }

    private void createIntroPanel() {
        intro = new JPanel();

        intro.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        intro.setLayout(new GridLayout(0, 1));

        JLabel introText = new JLabel("Introductory Information", JLabel.CENTER);
        intro.add(introText);
        frame.add(intro, BorderLayout.PAGE_START);

    }

    private void createActivityPanel() {
        activity = new JPanel();

        activity.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        activity.setLayout(new BoxLayout(activity, BoxLayout.PAGE_AXIS));

        JLabel nounText = new JLabel("Nouns:");
        JLabel verbText = new JLabel("Verbs:");
        JLabel prepText = new JLabel("Prepositions:");
        JLabel articlesText = new JLabel("Articles");

        String[] defaultWords = grammar.expDefaultRules();
        JTextField nouns = new JTextField(defaultWords[6], 30);
        JTextField verbs = new JTextField(defaultWords[7], 30);
        JTextField prepositions = new JTextField(defaultWords[8], 30);
        JTextField articles = new JTextField(defaultWords[9], 30);
        JButton enter = new JButton("Generate a sentence in my CFL!");
        JLabel sentence = new JLabel();
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userNouns = nouns.getText();
                userVerbs = verbs.getText();
                userPreps = prepositions.getText();
                userArticles = articles.getText();
                grammar = new CFLGenerator(userNouns, userVerbs, userPreps, userArticles);
                sentence.setText(grammar.generate());

            }
        });


        activity.add(nounText);
        activity.add(nouns);
        activity.add(verbText);
        activity.add(verbs);
        activity.add(prepText);
        activity.add(prepositions);
        activity.add(articlesText);
        activity.add(articles);
        activity.add(enter);
        activity.add(sentence);

        frame.add(activity, BorderLayout.CENTER);
    }

    private void createAdvancedDialog() {

        JButton adv = new JButton("Advanced");
        adv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel advDialog = new JPanel();
                advDialog.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
                advDialog.setLayout(new BoxLayout(advDialog, BoxLayout.PAGE_AXIS));

                String[] nonterminalNames = grammar.expNonterminals();
                String[] defaultRules = grammar.expDefaultRules();

                JTextField[] inputs = new JTextField[nonterminalNames.length];
                for(int i = 0; i < inputs.length; i++) {
                    inputs[i] = new JTextField(defaultRules[i], 30);

                    JPanel currentPanel = new JPanel();
                    currentPanel.add(new JLabel(nonterminalNames[i]));
                    currentPanel.add(new JLabel("-->"));
                    currentPanel.add(inputs[i]);
                    advDialog.add(currentPanel);
                }

                int result = JOptionPane.showConfirmDialog(frame, advDialog, "Define your grammar", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) {
                    HashMap<String, String> userRules = new HashMap<>();
                    for(int i = 0; i < inputs.length; i++) {
                        userRules.put(nonterminalNames[i], inputs[i].getText());
                    }
                    grammar = new CFLGenerator(userRules);
                }
            }
        });

        advanced = new JPanel();
        advanced.add(adv);
        frame.add(advanced, BorderLayout.PAGE_END);

    }

}
