package rita;


public class Tagger
{

	public static final String[] ADJ = {"jj", "jjr", "jjs"};
	public static final String[] ADV = {"rb", "rbr", "rbs", "rp"};
	public static final String[] NOUNS = {"nn", "nns", "nnp", "nnps"};
	public static final String[] VERBS = {"vb", "vbd", "vbg", "vbn", "vbp", "vbz"};
	
  public static boolean isAdjective(String word)
  {
	  return checkType(word, ADJ);
  }

  public static boolean isAdverb(String word)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public static boolean isNoun(String word)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public static boolean isVerb(String word)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public static String tagInline(String text, boolean useSimpleTags)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public static String[] tag(String text, boolean useSimpleTags)
  {
    // TODO Auto-generated method stub
    return null;
  }

  private static boolean checkType(String word, String[] tagArray) {
/*
	    if (word != null) {

	      if (word.length() == 0) return false;

	      if (word.indexOf(" ") < 0) {

	        String[] psa = RiTa._lexicon()._posArr(word);

	        if (RiTa.LEX_WARN && psa.length < 1 && this.size() <= 1000) {
	          warn(RiTa.LEX_WARN);
	          RiTa.LEX_WARN = 0; // only once
	        }

	        return psa.filter(p => tagArray.indexOf(p) > -1).length > 0;
	      }

	      throw Error("checkType() expects single word, found: '" + word + "'");
	    }
	    	    */
	    return (Boolean) null;

	  }
	  

}
