package rita;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lexicon {

	private static int MAP_SIZE = 30000;
	private static String EA[] = { };
	private static String DELIM = ":";

	public Map<String, String[]> dict; // data

	public Lexicon(String filePath) {
		List<String> lines = loadJSON(filePath);
		if (lines == null || lines.size() < 2) {
			throw new RiTaException("Problem parsing lexicon files");
		}
		populateDict(lines);
	}

	public static List<String> loadJSON(String file) {

		if (file == null) {
			throw new RiTaException("No dictionary path specified!");
		}

		InputStream is = RiTa.class.getResourceAsStream(file);
		if (is == null) {
			throw new RiTaException("Unable to load lexicon from: " + file);
		}

		try {
			return new BufferedReader(new InputStreamReader(is))
					.lines().collect(Collectors.toList());
		} catch (Exception e) {
			throw new RiTaException("Unable to read lines from: " + file, e);
		}
	}

	public String[] alliterations(String theWord, Map<String, Object> opts) {

		if (theWord == null || theWord.length() < 2) return EA;

		opts = this.parseArgs(opts);

		int limit = Util.intOpt("limit", opts);
		boolean silent = Util.boolOpt("silent", opts);
		String targetPos = Util.strOpt("targetPos", opts);

		// only allow consonant inputs
		if (Util.contains(RiTa.VOWELS, theWord.charAt(0))) {
			if (!silent && !RiTa.SILENT) console.warn(
					"Expected a word starting with a consonant,"
							+ " but got '" + theWord + "'");
			return EA;
		}

		String fss = this.firstStressedSyl(theWord);
		if (fss == null) return EA;

		String phone = this.firstPhone(fss);
		ArrayList<String> result = new ArrayList<>();

		// make sure we parsed first phoneme
		if (phone == null) {
			if (!silent && !RiTa.SILENT) {
				console.warn("Failed parsing first phone in '" + theWord + "'");
			}
			return EA;
		}

		String[] words = words();
		if ((boolean) opts.get("shuffle")) {
			words = RandGen.shuffle(words);
		}
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] rdata = dict.get(word);
			// check word length and syllables 
			if (word.equals(theWord) || !this.checkCriteria(word, rdata, opts)) {
				continue;
			}
			if (targetPos.length() > 0) {
				word = this.matchPos(word, rdata, opts, false);
				if (word == null) continue;
				if (!word.equals(words[i])) rdata = dict.get(word);
			}

			// TODO: use 'rdata', phones here

			// if new word is not in dictionary
			//String phones = rdata != null ? rdata[0] : this.rawPhones(word);

			String c2 = this.firstPhone(this.firstStressedSyl(word));
			if (phone.equals(c2)) result.add(word);
			if (result.size() >= limit) break;
		}

		return result.toArray(new String[Math.min(result.size(), limit)]);
	}
	
	public boolean hasWord(String word) {
		return hasWord(word, null); 
	}

	public boolean hasWord(String word, Map<String, Object> opts) {
		
		if (word == null || word.length() == 0) {
			return false;
		}
		
		boolean exists = this.dict.containsKey(word.toLowerCase());
		boolean noDerivations = Util.boolOpt("noDerivations", opts);
		
		if (noDerivations || exists) return exists;
		
		// plural?
		String sing = RiTa.singularize(word);
		if (this.dict.containsKey(sing.toLowerCase()) && Arrays.asList(RiTa.tagger.allTags(sing)).contains("nn")) return true;
		
		// TODO: need a Conjugator.unconjugate() ?
		String unconj = Conjugator.unconjugate(word);
		return (this.dict.containsKey(unconj) && Arrays.asList(RiTa.tagger.allTags(unconj)).contains("vb"));
	}

	public boolean isAlliteration(String word1, String word2, boolean noLts) {
		if (word1 == null || word2 == null) return false;
		if (word1.length() == 0 || word2.length() == 0) return false;

		String c1 = firstPhone(firstStressedSyl(word1, noLts));
		String c2 = firstPhone(firstStressedSyl(word2, noLts));

		return c1.length() > 0 && c2.length() > 0
				&& !RiTa.isVowel(c1.charAt(0)) && c1.equals(c2);
	}

	public boolean isRhyme(String word1, String word2, boolean noLts) {

		if (word1 == null || word2 == null) return false;
		if (word1.toUpperCase().equals(word2.toUpperCase())) return false;

		String phones1 = rawPhones(word1, noLts);
		String phones2 = rawPhones(word2, noLts);
		if (phones1 == null || phones2 == null) return false;
		if (phones2.equals(phones1)) return false;

		String p1 = lastStressedVowelPhonemeToEnd(word1, noLts);
		String p2 = lastStressedVowelPhonemeToEnd(word2, noLts);

		return p1 != null && p2 != null && p1.equals(p2);
	}

	private String matchPos(String word, String[] rdata,
			Map<String, Object> opts, boolean strict) {

		String pos = (String) opts.get("pos");
		String targetPos = (String) opts.get("targetPos");
		int numSyllables = (int) opts.get("numSyllables");
		boolean pluralize = (boolean) opts.get("pluralize");
		boolean conjugate = (boolean) opts.get("conjugate");

		String[] posArr = rdata[1].split(" ");
		if (strict && !targetPos.equals(posArr[0]) ||
				!Arrays.asList(posArr).contains(targetPos)) {
			return null;
		}

		// we've matched our pos, pluralize or inflect if needed
		String result = word;
		if (pluralize) {
			if (word.endsWith("ness") || word.endsWith("ism")) return null;
			result = RiTa.pluralize(word);
		}
		else if (conjugate) { // inflect
			result = reconjugate(word, pos);
		}

		// verify we haven't changed syllable count
		if (!result.equals(word) && numSyllables > 0) {
			boolean tmp = RiTa.SILENCE_LTS;
			RiTa.SILENCE_LTS = true;
			// TODO: use rdata here if possible
			int num = RiTa.syllables(result).split(RiTa.SYLLABLE_BOUNDARY).length;
			RiTa.SILENCE_LTS = tmp;
			// reject if syllable count has changed
			if (num != numSyllables) return null;
		}

		return result;
	}

	private String reconjugate(String word, String pos) {
		switch (pos) {
		/*  VBD 	Verb, past tense
		    VBG 	Verb, gerund or present participle
		    VBN 	Verb, past participle
		    VBP 	Verb, non-3rd person singular present
		    VBZ 	Verb, 3rd person singular present */
		case "vbd":
			return RiTa.conjugate(word, S1P_PAST);
		case "vbg":
			return RiTa.presentPart(word);
		case "vbn":
			return RiTa.pastPart(word);
		case "vbp":
			return word;// RiTa.conjugate(word); // no args
		case "vbz":
			return RiTa.conjugate(word, S3P_PRES);
		default:
			throw new RiTaException("Unexpected pos: " + pos);
		}
	}

	private static final Map<String, Object> S1P_PAST = RiTa.opts(
			"number", RiTa.SINGULAR,
			"person", RiTa.FIRST,
			"tense", RiTa.PAST);

	private static final Map<String, Object> S3P_PRES = RiTa.opts(
			"number", RiTa.SINGULAR,
			"person", RiTa.THIRD,
			"tense", RiTa.PRESENT);

	private boolean isMassNoun(String w, String pos) { // ?
		return w.endsWith("ness")
				|| w.endsWith("ism")
				|| pos.indexOf("vbg") > 0
				|| Util.contains(Util.MASS_NOUNS, w);
	}

	private boolean checkCriteria(String word, String[] rdata, Map<String, Object> opts) {

		int minLength = (int) opts.get("minLength");
		int maxLength = (int) opts.get("maxLength");
		int numSyllables = (int) opts.get("numSyllables");

		// check word length
		if (word.length() > maxLength) return false;
		if (word.length() < minLength) return false;

		// match numSyllables if supplied
		if (numSyllables > 0) {
			int syls = rdata[0].split(" ").length;
			if (numSyllables != syls) return false;
		}
		return true;
	}

	public String randomWord() {
		return randomWord((String) null, null);
	}

	public String randomWord(String pattern) {
		return randomWord(pattern, null);
	}

	public String randomWord(Map<String, Object> opts) {

		return this.randomWord((String) null, opts);
	}

	public String randomWord(Pattern pattern) {
		return this.randomWord(pattern, null);
	}

	// takes Pattern or String or null
	public String randomWord(Object pattern, Map<String, Object> opts) {
		if (pattern == null && opts != null && opts.get("regex") != null) {
			pattern = opts.get("regex");
		}

		if (opts == null) opts = new HashMap<String, Object>();
		// delegate to search {limit=1, shuffle=true, strictPos=true} 
		opts.put("limit", 1); 
		opts.put("shuffle", true);
		opts.put("strictPos", true);

		String[] result = pattern instanceof Pattern // java is ugly
				? this.search((Pattern) pattern, opts)
				: this.search((String) pattern, opts);

		// relax our pos constraints if we got nothing
		if (result.length < 1 && opts.get("pos") != null) {
			opts.put("strictPos", false);
			result = pattern instanceof Pattern 
			? this.search((Pattern) pattern, opts)
			: this.search((String) pattern, opts);
		}
		
		if (result == null || result.length < 1) {
			opts.clear();
			throw new RiTaException("No random word with options: " + opts);
		}
		return result[0]; // limit is 1
	}

	public String[] search() {
		return search((String) null, null);
	}

	public String[] search(Map<String, Object> opts) {
		return search((String) null, opts);
	}

	public String[] search(String pattern) {
		return search(pattern, null);
	}

	public String[] search(String pattern, Map<String, Object> opts) {

		Pattern re = null;
		if (pattern == null) {
			pattern = Util.strOpt("regex", opts);
		}
		if ("stresses".equals(Util.strOpt("type", opts)) && RE.test("^\\^?[01]+\\$?$", pattern)) {
			pattern = pattern.replaceAll("([01])(?=([01]))", "$1/");
		}
		if (pattern != null) re = Pattern.compile(pattern);
		return search(re, opts);
	}

	public String[] search(Pattern regex, Map<String, Object> opts) {

		String[] words = words();
		if (regex == null && (opts == null || opts.size() < 1)) return words;

		String type = Util.strOpt("type", opts, "");

		boolean tmp = RiTa.SILENCE_LTS;
		RiTa.SILENCE_LTS = true;

		opts = this.parseArgs(opts);

		int limit = (int) opts.get("limit");
		String tpos = (String) opts.get(TARGET_POS);
		List<String> result = new ArrayList<String>();

		int len = words.length;
		int start = 0;
		if ((boolean) opts.get("shuffle")) {
			start = (int) (Math.floor(RiTa.random(len)));
			words = RandGen.shuffle(words);
		}

		for (int i = 0; i < len; i++) {
			int idx = (start + i) % len;
			String word = words[idx], data[] = dict.get(word);

			if (!this.checkCriteria(word, data, opts)) continue;

			if (tpos.length() > 0) {
				word = matchPos(word, data, opts, limit == 1);
				// word = matchPos(word, data, opts, limit == 1 || (boolean) opts.get("strictPos"));
				if (word == null) continue;
				// Note: we may have changed the word here (e.g. via conjugation)
				// and it is also may no longer be in the dictionary
				if (!word.equals(words[idx])) data = dict.get(word);
			}

			if (regex != null) {
				if (type.equals(STRESSES)) {
					//String stresses = RiTa.analyzer.analyzeWord(word)[1];
					String phones = data != null ? data[0] : this.rawPhones(word);
					String stresses = RiTa.analyzer.phonesToStress(phones);
					if (RE.test(regex, stresses)) result.add(word);
				}
				else if (type.equals(PHONES)) {
					String phones = data != null ? data[0] : this.rawPhones(word);
					phones = phones.replaceAll(STRESS, "").replaceAll(SPC, Analyzer.DELIM);
					if (RE.test(regex, phones)) result.add(word);
				}
				else {
					if (RE.test(regex, word)) result.add(word); // match letters
				}
			}
			else {
				result.add(word); // no regex
			}

			if (result.size() >= limit) break;
		}

		RiTa.SILENCE_LTS = tmp;

		return result.toArray(new String[result.size()]);
	}

	public String[] searchOld(String regex, Map<String, Object> opts) {

		String[] words = words();
		if (regex == null) return words;

		String type = Util.strOpt("type", opts, "");
		int limit = Util.intOpt("limit", opts);

		if (type.equals("stresses")) {
			// if we have a stress string without slashes
			// add them to the regex pattern
			if (RE.test("^[01]+$", regex)) {
				regex = String.join("/", regex.split(""));
			}
		}
		if (RE.test("^/.*/$", regex)) {
			regex = regex.substring(1, regex.length() - 1);
		}

		Pattern re = Pattern.compile(regex);
		opts = this.parseArgs(opts);
		//console.log(opts);

		ArrayList<String> result = new ArrayList<String>();
		boolean tmp = RiTa.SILENCE_LTS;
		RiTa.SILENCE_LTS = true;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] rdata = dict.get(word);

			if (!this.checkCriteria(word, rdata, opts)) continue;
			if (((String) opts.get("targetPos")).length() > 0) {
				word = matchPos(word, rdata, opts, false);
				if (word == null) continue;
				if (!word.equals(words[i])) rdata = dict.get(word);
			}

			// TODO: use 'data' here if possible

			// if new word is not in dictionary
			//String phones = rdata != null ? rdata[0] : this.rawPhones(word);

			if (type.equals("stresses")) {
				String stresses = RiTa.analyzer.analyzeWord(word)[1];
				if (RE.test(re, stresses)) result.add(word);
			}
			if (type.equals("phones")) {
				String phones = RiTa.analyzer.analyzeWord(word)[0];
				if (RE.test(re, phones)) result.add(word);
			}
			else {
				if (RE.test(re, word)) result.add(word);
			}
			if (result.size() >= limit) break;
		}

		RiTa.SILENCE_LTS = tmp;
		return result.toArray(new String[Math.min(result.size(), limit)]);
	}

	public String[] soundsLike(String word) {
		return this.soundsLike(word, null);
	}

	public String[] spellsLike(String word) {
		return this.spellsLike(word, null);
	}

	public String[] soundsLike(String word, Map<String, Object> opts) {
		String[] result = EA;
		if (word != null && word.length() > 0) {
			if (opts == null) opts = new HashMap<>();
			opts.put("type", "sound");
			result = Util.boolOpt("matchSpelling", opts)
					? this.similarBySoundAndLetter(word, opts)
					: this.similarByType(word, opts);
		}
		return result;
	}

	public String[] spellsLike(String word, Map<String, Object> opts) {
		String[] result = EA;
		if (word != null && word.length() > 0) {
			if (opts == null) opts = new HashMap<>();
			opts.put("type", "letter");
			result = this.similarByType(word, opts);
		}
		return result;
	}

	public String[] similarBySoundAndLetter(String word, Map<String, Object> opts) {

		opts.put("type", "letter");
		String[] simLetter = similarByType(word, opts);
		if (simLetter.length < 1) return EA;

		opts.put("type", "sound");
		String[] simSound = similarByType(word, opts);
		if (simSound.length < 1) return EA;

		String[] result = intersect(simSound, simLetter);
		return Arrays.copyOfRange(result, 0,
				Math.min(result.length, Util.intOpt("limit", opts)));
	}

	public String[] rhymes(String theWord) {
		return this.rhymes(theWord, null);
	}

	public String[] rhymes(String theWord, Map<String, Object> opts) {

		opts = this.parseArgs(opts);

		int limit = Util.intOpt("limit", opts);
		String tpos = Util.strOpt("targetPos", opts);

		if (theWord == null || theWord.length() < 2) return EA;

		String phone = this.lastStressedPhoneToEnd(theWord);
		if (phone == null) return EA;

		String[] words = words();
		if ((boolean) opts.get("shuffle")) {
			words = RandGen.shuffle(words);
		}
		List<String> result = new ArrayList<>();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] rdata = dict.get(word);

			// check word length and syllables 
			if (word.equals(theWord) || !this.checkCriteria(word, rdata, opts)) {
				continue;
			}

			if (tpos.length() > 0) {
				word = this.matchPos(word, rdata, opts, false);
				if (word == null) continue;
				if (!word.equals(words[i])) rdata = dict.get(word);
			}

			// if new word is not in dictionary
			String phones = rdata != null ? rdata[0] : this.rawPhones(word);

			// check for the rhyme
			if (phones.endsWith(phone)) result.add(word);
			if (result.size() >= limit) break;
		}

		return result.toArray(new String[Math.min(result.size(), limit)]);
	}

	public String[] similarByType(String theWord, Map<String, Object> opts) {
		opts = this.parseArgs(opts);

		String type = Util.strOpt("type", opts);
		boolean sound = type.equals("sound");
		int limit = Util.intOpt("limit", opts);
		int minDist = Util.intOpt("minDistance", opts);
		String tpos = Util.strOpt("targetPos", opts);

		String input = theWord.toLowerCase();
		String variations = input + "||" + input + "s||" + input + "es";
		String[] phonesA = null, words = words();

		if (sound) {
			phonesA = this.toPhoneArray(this.rawPhones(input));
			if (phonesA == null) return EA;
		}

		ArrayList<String> result = new ArrayList<String>();
		int minVal = Integer.MAX_VALUE;

		if ((boolean) opts.get("shuffle")) {
			words = RandGen.shuffle(words);
		}

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] rdata = dict.get(word);
			if (!this.checkCriteria(word, rdata, opts)) continue;
			if (variations.contains(word)) continue;

			if (tpos.length() > 0) {
				word = this.matchPos(word, rdata, opts, false);
				if (word == null) continue;
				if (!word.equals(words[i])) rdata = dict.get(word);
			}

			// if new word is not in dictionary
			String phones = rdata != null ? rdata[0] : this.rawPhones(word);

			int med = -1;
			String[] phonesB;
			if (sound) {
				phonesB = phones.replaceAll("1", "").replaceAll(" ", "-").split("-");
				med = minEditDist(phonesA, phonesB);
			}
			else {
				med = minEditDist(input, word);
			}

			// found something even closer
			if (med >= minDist && med < minVal) {
				minVal = med;
				result = new ArrayList<String>();
				result.add(word);
			}
			// another best to add
			else if (med == minVal && result.size() < limit) {
				result.add(word);
			}
		}

		int count = Math.min(result.size(), limit);
		return result.subList(0, count).toArray(new String[count]);
	}

	public String[] toPhoneArray(String raw) {
		ArrayList<String> result = new ArrayList<String>();
		String sofar = "";
		for (int i = 0; i < raw.length(); i++) {
			if (raw.charAt(i) == ' ' || raw.charAt(i) == '-') {
				result.add(sofar);
				sofar = "";
			}
			else if (raw.charAt(i) != '1' && raw.charAt(i) != '0') {
				sofar += raw.charAt(i);
			}
		}
		result.add(sofar);
		return result.toArray(new String[result.size()]);
	}

	public String[] words() {
		Set<String> keys = dict.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	public String[] words(Pattern regex) {
		Set<String> keys = dict.keySet();
		if (regex == null) return keys.toArray(new String[keys.size()]);
		return keys.stream()
				.filter(word -> regex.matcher(word).matches())
				.toArray(String[]::new);
	}

	//////////////////////////////////////////////////////////////////////

	public static String[] intersect(String[] a, String[] b) {
		// https://stackoverflow.com/questions/17863319/java-find-intersection-of-two-arrays
		Set<String> s1 = new HashSet<String>(Arrays.asList(a));
		s1.retainAll(Arrays.asList(b));
		String[] result = s1.toArray(new String[s1.size()]);
		Arrays.sort(result);
		return result;
	}

	public String posData(String word) {
		String[] rdata = lookupRaw(word);
		return (rdata != null && rdata.length == 2)
				? rdata[1].replaceAll("\\]", "")
				: null;
	}

	public String rawPhones(String word) {
		return this.rawPhones(word, false);
	}

	public String rawPhones(String word, boolean noLts) {

		String[] rdata = lookupRaw(word);
		if (rdata != null && rdata.length != 0) {
			return rdata[0];
		}

		if (!noLts) { // check lts otherwise
			String[] phones = RiTa.analyzer.computePhones(word);
			if (phones != null && phones.length > 0) {
				return Util.syllabifyPhones(phones).trim(); // TODO: why?
				//.replaceAll("\\[", "").replaceAll("'", "");
			}
		}
		return null;
	}

	String[] posArr(String word) {
		String pl = posData(word);
		return (pl != null) ? pl.split(" ") : new String[0];
	}

	private String[] lookupRaw(String word) {
		String[] result = null;
		if (word != null && word.length() > 0) {
			word = word.toLowerCase();
			if (dict != null) {
				result = dict.get(word);
			}
		}
		return result != null ? result : EA;
	}

	private void populateDict(List<String> lines) {

		dict = new LinkedHashMap<String, String[]>(MAP_SIZE);

		for (int i = 1; i < lines.size() - 1; i++) // ignore JS prefix/suffix
		{
			String line = lines.get(i).replaceAll("[\"\\[\\]]", "");
			String[] parts = line.split(DELIM);
			if (parts == null || parts.length != 2) {
				throw new RiTaException("Illegal entry: " + line);
			}

			dict.put(parts[0].trim(), parts[1].split(","));
			//if (i < 10) System.out.println(parts[0].trim()
			// +": "+parts[1].split(",")[0]);
		}
	}

	private String lastStressedPhoneToEnd(String word) {
		return lastStressedPhoneToEnd(word, false);
	}

	private String lastStressedPhoneToEnd(String word, boolean noLts) {

		if (word == null || word.length() == 0) return null;

		String raw = rawPhones(word, noLts);
		if (raw == null || raw.length() == 0) return null;
		int idx = raw.lastIndexOf(RiTa.STRESS);//?
		//if (idx < 0) return null;
		if (idx >= 0) {
			char c = raw.charAt(--idx);
			while (c != '-' && c != ' ') {
				if (--idx < 0) return raw; // single-stressed syllable
				c = raw.charAt(idx);
			}
		}//fix to #75 from js
		return raw.substring(idx + 1);
	}

	private String lastStressedVowelPhonemeToEnd(String word, boolean noLts) {

		if (word == null || word.length() == 0) return null;

		String raw = lastStressedPhoneToEnd(word, noLts);
		if (raw == null || raw.length() == 0) return null;

		String[] syllables = raw.split(" ");
		String lastSyllable = syllables[syllables.length - 1];
		lastSyllable = lastSyllable.replace("[^a-z-1 ]", "");

		int idx = -1;
		for (int i = 0; i < lastSyllable.length(); i++) {
			char c = lastSyllable.charAt(i);
			if (RiTa.VOWELS.contains(Character.toString(c))) {
				idx = i;
				break;
			}
		}

		return lastSyllable.substring(idx);
	}

	private String firstStressedSyl(String word) {
		return firstStressedSyl(word, false);
	}

	private String firstStressedSyl(String word, boolean noLts) {

		String raw = rawPhones(word, noLts);
		if (raw == null || raw.equals("")) return null;
		raw = raw.trim();
		int idx = raw.indexOf(RiTa.STRESS);
		if (idx < 0) return null;
		char c = raw.charAt(--idx);
		while (c != ' ') {
			if (--idx < 0) {
				// single-stressed syllable
				idx = 0;
				break;
			}
			c = raw.charAt(idx);
		}
		String firstToEnd = idx == 0 ? raw : raw.substring(idx).trim();
		idx = firstToEnd.indexOf(" ");
		return idx < 0 ? firstToEnd : firstToEnd.substring(0, idx);
	}

	private String firstPhone(String rawPhones) {
		if (rawPhones != null && rawPhones.length() > 0) {
			String[] phones = rawPhones.split(RiTa.PHONEME_BOUNDARY);
			if (phones != null) return phones[0];
		}
		return null;
	}

	private static int minEditDist(String source, String target) {
		return minEditDist(source.split(""), target.split(""));
	}

	private static int minEditDist(String[] source, String[] target) {

		int i, j, cost;
		String sI; // ith character of s
		String tJ; // jth character of t

		int[][] matrix = new int[source.length + 1][target.length + 1];

		// Step 1 ----------------------------------------------

		for (i = 0; i <= source.length; i++) {
			// System.out.println(i);
			matrix[i][0] = i;
		}

		for (j = 0; j <= target.length; j++) {
			matrix[0][j] = j;
		}

		// Step 2 ----------------------------------------------

		for (i = 1; i <= source.length; i++) {
			sI = source[i - 1];

			// Step 3 --------------------------------------------

			for (j = 1; j <= target.length; j++) {
				tJ = target[j - 1];

				// Step 4 ------------------------------------------

				cost = (sI.equals(tJ)) ? 0 : 1;

				// Step 5 ------------------------------------------
				matrix[i][j] = Math.min(Math.min(matrix[i - 1][j] + 1,
						matrix[i][j - 1] + 1), matrix[i - 1][j - 1] + cost);
			}
		}

		// Step 6 ----------------------------------------------
		return matrix[source.length][target.length];
	}

	private Map<String, Object> parseArgs(Map<String, Object> opts) {
		String tpos = Util.strOpt("pos", opts, "");
		boolean pluralize = false;
		boolean conjugate = false;
		if (tpos.length() > 0) {
			pluralize = (tpos.equals("nns"));
			conjugate = (tpos.charAt(0) == 'v' && tpos.length() > 2);
			if (tpos.charAt(0) == 'n') tpos = "nn";
			else if (tpos.charAt(0) == 'v') tpos = "vb";
			else if (tpos.equals("r")) tpos = "rb";
			else if (tpos.equals("a")) tpos = "jj";
		}
		if (opts == null) opts = new HashMap<>();

		// if limit==1 (eg, for randomWord) then default minLength = 4;
		int limit = Util.intOpt("limit", opts, 10);
		int defMinLen = limit > 1 ? 3 : 4;

		opts.put("limit", limit);
		opts.put("pluralize", pluralize);
		opts.put("conjugate", conjugate);
		opts.put("targetPos", tpos);
		opts.put("maxLength", Util.intOpt("maxLength", opts));
		opts.put("minLength", Util.intOpt("minLength", opts, defMinLen));
		opts.put("numSyllables", Util.intOpt("numSyllables", opts, 0));
		opts.put("minDistance", Util.intOpt("minDistance", opts, 1));
		opts.put("shuffle", Util.boolOpt("shuffle", opts));
		return opts;
	}

	private static final String TARGET_POS = "targetPos", STRESSES = "stresses";
	private static final String PHONES = "phones", STRESS = "1", SPC = " ";

	public static void main(String[] args) throws Exception {
		//Lexicon lex = new Lexicon(RiTa.DICT_PATH);
		console.log(RiTa.lexicon().randomWord());
		//		System.out.println(lex.dict.get("dog")[0]);
		//		System.out.println(lex.dict.get("dog")[1]);
		//		System.out.println(lex._rawPhones("dog"));
		//System.out.println(lex._rawPhones("absolot"));
		//		Pattern re = Pattern.compile("phant");
		//		System.out.println(RE.test(re, "elephantine"));
	}

}
