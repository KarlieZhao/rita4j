package rita.test;

import org.junit.jupiter.api.Test;

import rita.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConjugatorTests {

	@Test
	public void callPastParticiple() {
		eq(RiTa.pastParticiple("sleep"), "slept");
		eq(RiTa.pastParticiple("withhold"), "withheld");

		eq(RiTa.pastParticiple("cut"), "cut");
		eq(RiTa.pastParticiple("go"), "gone");
		eq(RiTa.pastParticiple("swim"), "swum");
		eq(RiTa.pastParticiple("would"), "would");
		eq(RiTa.pastParticiple("might"), "might");
		eq(RiTa.pastParticiple("run"), "run");
		eq(RiTa.pastParticiple("speak"), "spoken");
		eq(RiTa.pastParticiple("break"), "broken");
		eq(RiTa.pastParticiple(""), "");

		// PROBLEMS

		eq(RiTa.pastParticiple("awake"), "awoken");
		eq(RiTa.pastParticiple("become"), "became");
		eq(RiTa.pastParticiple("drink"), "drunk");
		eq(RiTa.pastParticiple("plead"), "pled");
		eq(RiTa.pastParticiple("run"), "run");
		eq(RiTa.pastParticiple("shine"), "shone");
		// or shined
		eq(RiTa.pastParticiple("shrink"), "shrunk");
		// or shrunken
		eq(RiTa.pastParticiple("stink"), "stunk");
		eq(RiTa.pastParticiple("study"), "studied");
	}

	@Test
	public void callPresentParticiple() {

		eq(RiTa.presentParticiple("sleep"), "sleeping");
		eq(RiTa.presentParticiple("withhold"), "withholding");

		eq(RiTa.presentParticiple("cut"), "cutting");
		eq(RiTa.presentParticiple("go"), "going");
		eq(RiTa.presentParticiple("run"), "running");
		eq(RiTa.presentParticiple("speak"), "speaking");
		eq(RiTa.presentParticiple("break"), "breaking");
		eq(RiTa.presentParticiple("become"), "becoming");
		eq(RiTa.presentParticiple("plead"), "pleading");
		eq(RiTa.presentParticiple("awake"), "awaking");
		eq(RiTa.presentParticiple("study"), "studying");

		eq(RiTa.presentParticiple("lie"), "lying");
		eq(RiTa.presentParticiple("swim"), "swimming");
		eq(RiTa.presentParticiple("run"), "running");
		eq(RiTa.presentParticiple("dig"), "digging");
		eq(RiTa.presentParticiple("set"), "setting");
		eq(RiTa.presentParticiple("speak"), "speaking");
		eq(RiTa.presentParticiple("bring"), "bringing");
		eq(RiTa.presentParticiple("speak"), "speaking");

		eq(RiTa.presentParticiple(""), "");
	}

	@Test
	public void callConjugateVbd() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("tense", RiTa.PAST);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.FIRST);

		String c = RiTa.conjugate("go", args);
		eq(c, "went");

		args.clear();
		args.put("tense", RiTa.PAST);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.FIRST);

		String s = RiTa.conjugate("run", args);
		eq(s, "ran");
	}

	@Test
	public void callConjugate() {


		String[] s, a;

		eq("swum", RiTa.pastParticiple("swim"));
		
		// Example of using opts
		eq(RiTa.conjugate("be", Util.opts("form", RiTa.GERUND)), "being");
		
		Map<String, Object> args = new HashMap<String, Object>();
		s = new String[] { "swim", "need", "open" };
		a = new String[] { "swims", "needs", "opens" };

		args = new HashMap<String, Object>();
		args.put("tense", RiTa.PRESENT);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);

		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("tense", RiTa.PRESENT);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);
		args.put("passive", true);

		a = new String[] { "is swum", "is needed", "is opened" };
		for (int i = 0; i < s.length; i++) {
			eq(RiTa.conjugate(s[i], args), a[i]);
		}

		/////////////////////////////////////////////////

		args.clear();
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.FIRST);
		args.put("tense", RiTa.PAST);

		eq(RiTa.conjugate("swim", args), "swam");

		s = new String[] { "swim", "need", "open", "" };
		a = new String[] { "swam", "needed", "opened", "" };

		eq(a.length, s.length);

		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("number", RiTa.PLURAL);
		args.put("person", RiTa.SECOND);
		args.put("tense", RiTa.PAST);

		a = new String[] { "swam", "needed", "opened", "" };
		eq(a.length, s.length);

		for (int i = 0; i < s.length; i++) {
			eq(RiTa.conjugate(s[i], args), a[i]);
		}

		args.clear();
		args.put("number", RiTa.PLURAL);
		args.put("person", RiTa.SECOND);
		args.put("tense", RiTa.FUTURE);

		a = new String[] { "will swim", "will need", "will open", "" };
		eq(a.length, s.length);

		for (int i = 0; i < s.length; i++) {
			eq(RiTa.conjugate(s[i], args), a[i]);
		}

		args.clear();
		args.put("tense", RiTa.PAST);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);

		a = new String[] { "swam", "needed", "opened", "" };

		eq(a.length, s.length);

		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("tense", RiTa.PAST);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);
		args.put("form", RiTa.INFINITIVE);

		a = new String[] { "to swim", "to need", "to open", "" };
		eq(a.length, s.length);
		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("tense", RiTa.PAST);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);
		args.put("passive", true);

		s = new String[] { "scorch", "burn", "hit", "" };
		a = new String[] { "was scorched", "was burned", "was hit", "" };
		eq(a.length, s.length);
		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		s = new String[] { "swim", "need", "open", "" };

		args.clear();
		args.put("tense", RiTa.PRESENT);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);
		args.put("form", RiTa.INFINITIVE);
		args.put("progressive", true);

		a = new String[] { "to be swimming", "to be needing", "to be opening", "" };
		eq(a.length, s.length);
		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("tense", RiTa.PRESENT);
		args.put("number", RiTa.SINGULAR);
		args.put("person", RiTa.THIRD);
		args.put("form", RiTa.INFINITIVE);
		args.put("perfect", true);

		a = new String[] { "to have swum", "to have needed", "to have opened", "" };
		eq(a.length, s.length);
		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

		args.clear();
		args.put("number", RiTa.PLURAL);
		args.put("person", RiTa.SECOND);
		args.put("tense", RiTa.PAST);

		eq(RiTa.conjugate("barter", args), "bartered");
		eq(RiTa.conjugate("run", args), "ran");

		s = new String[] { "compete", "complete", "eject" };
		a = new String[] { "competed", "completed", "ejected" };
		eq(a.length, s.length);
		for (int i = 0; i < s.length; i++) {
			String c = RiTa.conjugate(s[i], args);
			eq(c, a[i]);
		}

	}

	static void eq(String a, String b) {
		eq(a, b, "");
	}

	static void eq(String a, String b, String msg) {
		assertEquals(b, a, msg);
	}

	static void eq(int a, int b){
		assertEquals(b, a);
	}
}
