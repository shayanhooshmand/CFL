/**
 *
 * This class represents a Context-Free Language generator object,
 * which is complete with its own grammar rules following a CFL language
 * structure, various methods to update the rules and words in the grammar,
 * and a method to generate random sentences.
 *
 * @author Shayan Hooshmand
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class CFLGenerator {

    /**
     *
     * Constructs a CFL Generator with default grammar rules
     * and words.
     *
     */
    public CFLGenerator() {
        grammar = new HashMap<>();

        HashMap<String, String> g = new HashMap<>();
        String[] nonterminals = expNonterminals();
        String[] rules = expDefaultRules();

        for(int i = 0; i < nonterminals.length; i++)
            g.put(nonterminals[i], rules[i]);

        scribeRules(g);
    }

    // *************************************************
    // PUBLIC METHODS
    // *************************************************

    /**
     *
     * Updates the grammar rules
     *
     * @param g the new rules for the grammar
     */

    public void updateRules(HashMap<String, String> g) {
        scribeRules(g);
    }

    /**
     *
     * Updates the words in the grammar
     *
     * @param nouns list of nouns delimited by commas to go into grammar
     * @param verbs list of verbs delimited by commas to go into grammar
     * @param preps list of preps delimited by commas to go into grammar
     * @param articles list of articles delimited by commas to go into grammar
     */
    public void updateWords(String nouns, String verbs, String preps, String articles) {
        HashMap<String, String> g = new HashMap<>();
        g.put(NOUN, nouns);
        g.put(VERB, verbs);
        g.put(PREP, preps);
        g.put(ARTICLE, articles);

        scribeRules(g);
    }

    /**
     *
     * Public method to generate a random sentence
     *
     * @return the random sentence generated based on current grammar rules
     */
    public String generate() {
        return generate(START);
    }

    /**
     *
     * Gives access to users to the strings representing the nonterminals
     * in our grammar rule.
     *
     * @return an array of the strings of nonterminal names
     */
    public String[] expNonterminals() {
        return new String[] {START, NOUN_PHRASE, VERB_PHRASE,
                PREP_PHRASE, CMPLX_NOUN, CMPLX_VERB, NOUN, VERB,
                PREP, ARTICLE};
    }

    /**
     *
     * Gives access to users to the default grammar rules for each nonterminal
     *
     * @return a string array of every default grammar rule
     */
    public String[] expDefaultRules() {
        return new String[] {NOUN_PHRASE+VERB_PHRASE,
                CMPLX_NOUN+OR+CMPLX_NOUN+PREP_PHRASE,
                CMPLX_VERB+OR+CMPLX_VERB+PREP_PHRASE,
                PREP+CMPLX_NOUN,
                ARTICLE+NOUN, VERB+OR+VERB+NOUN_PHRASE,
                DEFAULT_NOUNS, DEFAULT_VERBS, DEFAULT_PREPS,
                DEFAULT_ARTICLES};
    }

    /**
     *
     * Overrides toString to show a CFLGenerator object's grammar
     * rules when printed
     *
     * @return a string representation of the CFLGenerator's grammar
     * rules
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String nonterminal : grammar.keySet()) {
            sb.append(nonterminal + ":");
            for(LinkedList<String> rule : grammar.get(nonterminal)) {
                for(String variable : rule) {
                    sb.append(variable + "+");
                }
                sb.append(",");
            }
            sb.append("/n");
        }

        return sb.toString();
    }

    // *************************************************
    // PRIVATE METHODS
    // *************************************************

    /**
     *
     * Performs a rewrite of the rules of the grammar
     *
     * @param g New grammar rules to overwrite or simply join old ones
     */
    private void scribeRules(HashMap<String, String> g) {
        for(String nonterminal : g.keySet()) {
            grammar.put(nonterminal, new ArrayList<>());

            String rules = g.get(nonterminal).trim();
            String[] rulesArray = rules.split(",");
            for(String rule : rulesArray) {
                grammar.get(nonterminal).add(parseRule(rule));
            }
        }
    }

    /**
     *
     * Helper method for generating a random sentence recursively
     *
     * @param variable
     * @return the string or substring that is generated
     */
    private String generate(String variable) {
        //If this is a terminal string
        if(!grammar.containsKey(variable)) {
            return variable + " ";
        }

        //Otherwise, recurse
        StringBuilder sb = new StringBuilder();
        ArrayList<LinkedList<String>> options = grammar.get(variable);
        Random r = new Random();
        LinkedList<String> choice = options.get(r.nextInt(options.size()));
        for(String replacer : choice)
            sb.append(generate(replacer));

        return sb.toString();
    }

    /**
     *
     * Parses a string representing the RHS of a grammar rule into
     * a LinkedList of strings, each being a single nonterminal/terminal
     *
     * @param rule the string representing the RHS of a rule
     * @return the rule parsed into a LinkedList
     */
    private LinkedList<String> parseRule(String rule) {
        rule = rule.trim();
        LinkedList<String> parsedRule = new LinkedList<>();

        int count = 0;
        int start = 0;
        while(count < rule.length()) {
            boolean isNonterminal = (rule.charAt(count) == '<');
            while(!isNonterminal && count++ < rule.length()) {
                if((count == rule.length()) || rule.charAt(count) == '<') {
                    parsedRule.add(rule.substring(start, count));
                    start = count;
                    isNonterminal = true;
                }
            }
            while(isNonterminal && count < rule.length()) {
                if(rule.charAt(count++) == '>') {
                    parsedRule.add(rule.substring(start, count));
                    start = count;
                    isNonterminal = false;
                }
            }
        }

        return parsedRule;
    }

    // *************************************************
    // FIELDS
    // *************************************************

    private HashMap<String, ArrayList<LinkedList<String>>> grammar;
    public final String START = "<start>";
    public final String NOUN_PHRASE = "<noun-phrase>";
    public final String VERB_PHRASE = "<verb-phrase>";
    public final String PREP_PHRASE = "<prep-phrase>";
    public final String CMPLX_NOUN = "<cmplx-noun>";
    public final String CMPLX_VERB = "<cmplx-verb>";
    public final String ARTICLE = "<article>";
    public final String NOUN = "<noun>";
    public final String VERB = "<verb>";
    public final String PREP = "<prep>";
    public final String OR = ",";
    private final String DEFAULT_NOUNS = "girl, dog, boy";
    private final String DEFAULT_VERBS = "eats, attacks, launches";
    private final String DEFAULT_PREPS = "around, over, under";
    private final String DEFAULT_ARTICLES = "the, a";

}