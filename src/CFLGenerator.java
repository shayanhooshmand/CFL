import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class CFLGenerator {

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


    public CFLGenerator(HashMap<String, String> g) {
        grammar = new HashMap<>();
        scribeRules(g);
    }

    public CFLGenerator(String nouns, String verbs, String preps, String articles) {
        grammar = new HashMap<>();

        HashMap<String, String> g = new HashMap<>();
        String[] nonterminals = expNonterminals();
        String[] rules = expDefaultRules();

        for(int i = 0; i < nonterminals.length; i++) {
            String rule = new String();
            switch(nonterminals[i]) {
                case NOUN :
                    rule = nouns;
                    break;
                case VERB :
                    rule = verbs;
                    break;
                case PREP :
                    rule = preps;
                    break;
                case ARTICLE :
                    rule = articles;
                    break;
                default :
                    rule = rules[i];
                    break;
            }
            g.put(nonterminals[i], rule);
        }

//        g.put(START, NOUN_PHRASE+VERB_PHRASE);
//        g.put(NOUN_PHRASE, CMPLX_NOUN+OR+CMPLX_NOUN+PREP_PHRASE);
//        g.put(VERB_PHRASE, CMPLX_VERB+OR+CMPLX_VERB+PREP_PHRASE);
//        g.put(PREP_PHRASE, PREP+CMPLX_NOUN);
//        g.put(CMPLX_NOUN, ARTICLE+NOUN);
//        g.put(CMPLX_VERB, VERB+OR+VERB+NOUN_PHRASE);
//        g.put(NOUN, nouns);
//        g.put(VERB, verbs);
//        g.put(PREP, preps);
//        g.put(ARTICLE, articles);

        scribeRules(g);
    }

    public CFLGenerator() {
        grammar = new HashMap<>();

        HashMap<String, String> g = new HashMap<>();
        String[] nonterminals = expNonterminals();
        String[] rules = expDefaultRules();

        for(int i = 0; i < nonterminals.length; i++) {
            String rule = new String();
            switch(nonterminals[i]) {
                case NOUN :
                    rule = DEFAULT_NOUNS;
                    break;
                case VERB :
                    rule = DEFAULT_VERBS;
                    break;
                case PREP :
                    rule = DEFAULT_PREPS;
                    break;
                case ARTICLE :
                    rule = DEFAULT_ARTICLES;
                    break;
                default :
                    rule = rules[i];
                    break;
            }
            g.put(nonterminals[i], rule);
        }

        scribeRules(g);
    }

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

    public String generate() {
        return generate(START);
    }

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

    public String[] expNonterminals() {
        return new String[] {START, NOUN_PHRASE, VERB_PHRASE,
                PREP_PHRASE, CMPLX_NOUN, CMPLX_VERB, NOUN, VERB,
                PREP, ARTICLE};
    }
    public String[] expDefaultRules() {
        return new String[] {NOUN_PHRASE+VERB_PHRASE,
                CMPLX_NOUN+OR+CMPLX_NOUN+PREP_PHRASE,
                CMPLX_VERB+OR+CMPLX_VERB+PREP_PHRASE,
                PREP+CMPLX_NOUN,
                ARTICLE+NOUN, VERB+OR+VERB+NOUN_PHRASE,
                DEFAULT_NOUNS, DEFAULT_VERBS, DEFAULT_PREPS,
                DEFAULT_ARTICLES};
    }

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

}