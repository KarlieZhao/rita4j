package rita;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class RiTa {

	protected static Lexicon _lexicon;
	protected static Analyzer analyzer = new Analyzer();
	protected static Concorder concorder = new Concorder();

	public static Map<String, Function<String, String>> addTransform(
			String name, Function<String, String> func) {
		if (func != null) {
			RiScript.transforms.put(name, func);
		}
		else {
			RiScript.transforms.remove(name);
		}
		return RiScript.transforms;
	}

	public static String[] alliterations(String word) {
		return alliterations(word, 0);
	}

	public static String[] alliterations(String word, int minWordLength) {
		return alliterations(word, Util.opts("minLength", minWordLength));
	}

	public static String[] alliterations(String word, Map<String, Object> opts) {
		return lexicon().alliterations(word, opts);
	}

	public static Map<String, String> analyze(String word) {
		return analyzer.analyze(word);
	}

	public static String articlize(String s) {
		return RiScript.articlize(s);
	}

	public static Map<String, Integer> concordance(String text) {
		return concordance(text, null);
	}

	public static Map<String, Integer> concordance(String text, Map<String, Object> opts) {
		return concorder.concordance(text, opts);
	}

	public static String conjugate(String word, Map<String, Object> opts) {
		return Conjugator.conjugate(word, opts);
	}

	public static String conjugate(String word) {
		return conjugate(word, null);
	}

	public static String env() {
		return "Java";
	}

	public static boolean hasWord(String word) {
		return lexicon().hasWord(word);
	}

	public static boolean isAbbreviation(String input) {
		return isAbbreviation(input, false);
	}

	public static boolean isAbbreviation(String input, Map<String, Object> opts) {
		return isAbbreviation(input, Util.boolOpt("ignoreCase", opts));
	}

	public static boolean isAbbreviation(String input, boolean ignoreCase) {
		if (input == null) return false;
		if (ignoreCase) input = input.substring(0, 1).toUpperCase() + input.substring(1);
		return Arrays.stream(ABRV).anyMatch(input::equals);
	}

	public static boolean isAdjective(String word) {
		return Tagger.isAdjective(word);
	}

	public static boolean isAdverb(String word) {
		return Tagger.isAdverb(word);
	}

	public static boolean isAlliteration(String word1, String word2) {
		return lexicon().isAlliteration(word1, word2, false);
	}

	public static boolean isAlliteration(String word1, String word2, boolean noLTS) {
		return lexicon().isAlliteration(word1, word2, noLTS);
	}

	public static boolean isNoun(String word) {
		return Tagger.isNoun(word);
	}

	public static boolean isPunctuation(String text) {
		return text != null && text.length() > 0 && ONLY_PUNCT.matcher(text).matches();
	}

	public static boolean isQuestion(String sentence) { // remove?
		return Arrays.stream(QUESTIONS).anyMatch(tokenize(sentence)[0].toLowerCase()::equals);
	}

	public static boolean isRhyme(String word1, String word2) {
		return lexicon().isRhyme(word1, word2, false);
	}

	public static boolean isRhyme(String word1, String word2, boolean noLTS) {
		return lexicon().isRhyme(word1, word2, noLTS);
	}

	public static boolean isStopWord(String word) {
		return Arrays.asList(RiTa.STOP_WORDS).contains(word.toLowerCase());
	}

	public static boolean isVerb(String word) {
		return Tagger.isVerb(word);
	}

	public static String[] kwic(String word) {
		return kwic(word, null);
	}

	public static String[] kwic(String word, int numWords) {
		if (concorder == null) concorder = new Concorder();
		return concorder.kwic(word, numWords);
	}

	public static String[] kwic(String word, Map<String, Object> opts) {
		if (concorder == null) concorder = new Concorder();
		return concorder.kwic(word, opts);
	}

	public static String pastParticiple(String verb) {
		return Conjugator.pastParticiple(verb);
	}

	public static String phones(String text) {
		return RiTa.phones(text, null);
	}

	public static String phones(String text, Map<String, Object> opts) {
		return analyzer.analyze(text, opts).get("phones");
	}

	public static String posInline(String text, Map<String, Object> opts) {
		return posInline(text, Util.boolOpt("simple", opts));
	}

	public static String posInline(String text) {
		return posInline(text, false);
	}

	public static String posInline(String text, boolean useSimpleTags) {
		return Tagger.tagInline(text, useSimpleTags);
	}

	public static String[] pos(String text) {
		return pos(text, false);
	}

	public static String[] pos(String text, Map<String, Object> opts) {
		return pos(text, Util.boolOpt("simple", opts));
	}

	public static String[] pos(String text, boolean useSimpleTags) {
		return Tagger.tag(text, useSimpleTags);
	}

	public static String[] pos(String[] text, Map<String, Object> opts) {
		return pos(text, Util.boolOpt("simple", opts));
	}

	public static String[] pos(String[] text) {
		return pos(text, false);
	}

	public static String[] pos(String[] text, boolean useSimpleTags) {
		return Tagger.tag(text, useSimpleTags);
	}

	public static String pluralize(String word) {
		return pluralize(word, null);
	}

	public static String pluralize(String word, Map<String, Object> opts) {
		return Inflector.pluralize(word, opts);
	}

	public static String presentParticiple(String verb) {
		return Conjugator.presentParticiple(verb);
	}

	public static float random() {
		return RandGen.random();
	}

	public static float random(float max) {
		return RandGen.random(max);
	}

	public static float random(float min, float max) {
		return RandGen.random(min, max);
	}

	public static <T> T random(T[] type) {
		return (T) RandGen.randomItem(type);
	}

	public static <T> T random(Collection<T> c) {
		return (T) RandGen.randomItem(c);
	}

	public static final float random(float[] arr) {
		return RandGen.randomItem(arr);
	}

	public static final boolean random(boolean[] arr) {
		return RandGen.randomItem(arr);
	}

	public static final int random(int[] arr) {
		return RandGen.randomItem(arr);
	}

	public static final double random(double[] arr) {
		return RandGen.randomItem(arr);
	}

	public static int[] randomOrdering(int num) {
		return RandGen.randomOrdering(num);
	}

	public static void randomSeed(int theSeed) {
		RandGen.seed(theSeed);
	}

	public static String randomWord() {
		return lexicon().randomWord(null);
	}

	public static String randomWord(Map<String, Object> opts) {
		return lexicon().randomWord(opts);
	}

	public static String randomWord(String pos) {
		return randomWord(Util.opts("pos", pos));
	}

	public static String randomWord(int syllables) {
		return randomWord(Util.opts("numSyllables", syllables));
	}

	public static String randomWord(String pos, int syllables) {
		return randomWord(Util.opts("pos", pos, "numSyllables", syllables));
	}

	public static String[] rhymes(String word) {
		return lexicon().rhymes(word);
	}

	public static String[] rhymes(String word, Map<String, Object> opts) {
		return lexicon().rhymes(word, opts);
	}

	public static String evaluate(String word) {
		return RiTa.evaluate(word, null);
	}

	public static String evaluate(String word, Map<String, Object> opts) {
		return RiScript.eval(word, opts);
	}

	public static String evaluate(String word, Map<String, Object> ctx, Map<String, Object> opts) {
		return RiScript.eval(word, ctx, opts);
	}

	public static Grammar grammar() {
		return grammar((String) null);
	}

	public static Grammar grammar(String rules) {
		return grammar(rules, null);
	}

	public static Grammar grammar(Map<String, Object> rules) {
		return grammar(rules, null);
	}

	public static Grammar grammar(Map<String, Object> rules, Map<String, Object> context) {
		return new Grammar(rules, context);
	}

	public static Grammar grammar(String rules, Map<String, Object> context) {
		return new Grammar(rules, context);
	}

	public static Markov markov(int n) {
		return markov(n, null);
	}

	public static Markov markov(int n, Map<String, Object> options) {
		return new Markov(n, options);
	}

	public static String stresses(String text) {
		return analyzer.analyze(text).get("stresses");
	}

	public static String syllables(String text) {
		return analyzer.analyze(text).get("syllables");
	}

	public static String[] soundsLike(String word) {
		return lexicon().soundsLike(word);
	}

	public static String[] spellsLike(String word) {
		return lexicon().spellsLike(word);
	}

	public static String[] search() {
		return lexicon().search(null);
	}

	public static String[] search(String word) {
		return lexicon().search(word);
	}

	public static String[] soundsLike(String word, Map<String, Object> opts) {
		return lexicon().soundsLike(word, opts);
	}

	public static String[] spellsLike(String word, Map<String, Object> opts) {
		return lexicon().spellsLike(word, opts);
	}

	public static String[] search(String word, Map<String, Object> opts) {
		return lexicon().search(word, opts);
	}

	public static String singularize(String word) {
		return singularize(word, null);
	}

	public static String singularize(String word, Map<String, Object> opts) {
		return Inflector.singularize(word, opts);
	}

	public static String[] sentences(String text) {
		return sentences(text, (Pattern) null);
	}

	public static String[] sentences(String text, Map<String, Object> opts) {
		return sentences(text, Util.strOpt("pattern", opts));
	}

	public static String[] sentences(String text, String regex) {
		return sentences(text, Pattern.compile(regex));
	}

	public static String[] sentences(String text, Pattern regex) {
		return Tokenizer.sentences(text, regex);
	}

	public static String stem(String word) {
		return Stemmer.stem(word);
	}

	public static String[] tokenize(String text) {
		return Tokenizer.tokenize(text);
	}

	public static String[] tokenize(String text, String regex) {
		return Tokenizer.tokenize(text, regex);
	}

	public static String untokenize(String[] words) {
		return Tokenizer.untokenize(words);
	}

	public static String untokenize(String[] words, String delim) {
		return Tokenizer.untokenize(words, delim);
	}

	public static String[] words() {
		return words((Pattern) null);
	}

	public static String[] words(Map<String, Object> opts) {
		return words(Util.strOpt("pattern", opts));
	}

	public static String[] words(String regex) {
		return words(Pattern.compile(regex));
	}

	public static String[] words(Pattern regex) {
		return lexicon().words(regex);
	}

	// /////////////////////////// niapi /////////////////////////////////

	public static String capitalize(String s) {
		return s == null || s.length() == 0 ? ""
				: String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
	}

	public static Lexicon lexicon() { // singleton
		if (RiTa._lexicon == null) {
			try {
				RiTa._lexicon = new Lexicon(DICT_PATH);
			} catch (Exception e) {
				throw new RiTaException("Cannot load dictionary at "
						+ DICT_PATH + " " + System.getProperty("user.dir"), e);
			}
		}
		return RiTa._lexicon;
	}


	// /////////////////////////// static /////////////////////////////////

	public static boolean SILENT = false;
	public static boolean SILENCE_LTS = true;
	public static boolean SPLIT_CONTRACTIONS = false;

	public static String PHONEME_BOUNDARY = "-";
	public static String SYLLABLE_BOUNDARY = "/";
	public static String DICT_PATH = "rita_dict.js";

	// CONSTANTS
	public static final int FIRST = 1;
	public static final int SECOND = 2;
	public static final int THIRD = 3;
	public static final int PAST = 4;
	public static final int PRESENT = 5;
	public static final int FUTURE = 6;
	public static final int SINGULAR = 7;
	public static final int PLURAL = 8;
	public static final int NORMAL = 9;
	public static final int INFINITIVE = 1;
	public static final int GERUND = 2;
	//	public static final int IMPERATIVE = 3;
	//	public static final int BARE_INFINITIVE = 4;
	//	public static final int SUBJUNCTIVE = 5;

	public static final char STRESS = '1', NOSTRESS = '0';
	public static final String VOWELS = "aeiou";
	public static final String VERSION = "2";

	public static final Pattern ONLY_PUNCT = Pattern.compile("^[^0-9A-Za-z\\s]*$");

	public static String[] ABRV = {
			"Adm.", "Capt.", "Cmdr.", "Col.", "Dr.", "Gen.", "Gov.", "Lt.", "Maj.", "Messrs.", "Mr.", "Mrs.", "Ms.",
			"Prof.", "Rep.", "Reps.", "Rev.", "Sen.", "Sens.", "Sgt.", "Sr.", "St.", "A.k.a.", "C.f.", "I.e.", "E.g.", "Vs.", "V.", "Jan.", "Feb.",
			"Mar.",
			"Apr.", "Mar.", "Jun.", "Jul.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."
	};
	public static String[] QUESTIONS = {
			"was", "what", "when", "where", "which", "why", "who", "will", "would", "who", "how", "if", "is", "could", "might", "does", "are", "have"
	};
	public static String[] STOP_WORDS = {
			"and", "a", "of", "in", "i", "you", "is", "to",
			"that", "it", "for", "on", "have", "with",
			"this", "be", "not", "are", "as", "was", "but", "or", "from",
			"my", "at", "if", "they", "your", "all", "he", "by", "one",
			"me", "what", "so", "can", "will", "do", "an", "about", "we", "just",
			"would", "there", "no", "like", "out", "his", "has", "up", "more", "who",
			"when", "don't", "some", "had", "them", "any", "their", "it's", "only",
			"which", "i'm", "been", "other", "were", "how", "then", "now",
			"her", "than", "she", "well", "also", "us", "very", "because",
			"am", "here", "could", "even", "him", "into", "our", "much",
			"too", "did", "should", "over", "want", "these", "may", "where", "most",
			"many", "those", "does", "why", "please", "off", "going", "its", "i've",
			"down", "that's", "can't", "you're", "didn't", "another", "around",
			"must", "few", "doesn't", "the", "every", "yes", "each", "maybe",
			"i'll", "away", "doing", "oh", "else", "isn't", "he's", "there's", "hi",
			"won't", "ok", "they're", "yeah", "mine", "we're", "what's", "shall",
			"she's", "hello", "okay", "here's", "less"
	};

	public static String[] PHONES = { "aa", "ae", "ah", "ao", "aw", "ay", "b", "ch", "d", "dh", "eh", "er", "ey", "f", "g", "hh", "ih", "iy", "jh", "k",
			"l", "m", "n", "ng", "ow", "oy", "p", "r", "s", "sh", "t", "th", "uh", "uw", "v", "w", "y", "z", "zh" };

	public static void main(String[] args) {
		System.out.println(RiTa.analyze("absolot"));
		System.out.println(RiTa.evaluate("( newt | ginko | salamander)"));
	}

}
