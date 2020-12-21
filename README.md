![Java CI with Maven](https://github.com/dhowe/rita/workflows/Java%20CI%20with%20Maven/badge.svg) <a href="http://www.gnu.org/licenses/gpl-3.0.en.html"><img src="https://img.shields.io/badge/license-GPL-orange.svg" alt="npm version"></a> <a href="https://www.npmjs.com/package/rita"><img src="https://img.shields.io/npm/v/rita.svg" alt="npm version"></a>

## RiTa: generative language tools for Java

<a href="http://rednoise.org/rita"><img height="80" src="http://rednoise.org/rita/img/RiTa-logo3.png"/></a>

RiTa is a toolkit for natural language and generative literature. It is implemented in Java and JavaScript, with a common API for both, and it is free/libre/open-source via the GPL license.

###

* For JavaScript, see this [repo](https://github.com/dhowe/ritajs) or use it with [npm](https://www.npmjs.com/package/rita)  or [unpkg](https://unpkg.com/browse/rita/dist/) !
* A simple [Processing example](#in-processing) 
* A simple [Java example](#a-simple-example-java)
* For [Developers](#developing)

### Installation

* Via [github packages](https://github.com/dhowe/rita/packages/)
* Via [maven central](https://search.maven.org/artifact/org.rednoise/rita)
* Or directly in maven:

```xml
<dependency>
  <groupId>org.rednoise</groupId>
  <artifactId>rita</artifactId>
  <version>2.0.2</version>
</dependency>
```


## API

  <table cellspacing="0" cellpadding="0" border="0">
   <tr>
    <th colspan=2>RiTa&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </th>
    <th>RiTa.Markov</th>
    <th> &nbsp; RiTa.Grammar &nbsp; </th>
   </tr>
   <tr>
<td>
    <a href="https://rednoise.org/rita/reference/RiTa/addTransform/index.html">RiTa.addTransform()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/alliterations/index.html">RiTa.alliterations()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/analyze/index.html">RiTa.analyze()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/concordance/index.html">RiTa.concordance()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/conjugate/index.html">RiTa.conjugate()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/evaluate/index.html">RiTa.evaluate()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/hasWord/index.html">RiTa.hasWord()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isAbbreviation/index.html">RiTa.isAbbreviation()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isAdjective/index.html">RiTa.isAdjective()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isAdverb/index.html">RiTa.isAdverb()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isAlliteration/index.html">RiTa.isAlliteration()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isNoun/index.html">RiTa.isNoun()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isPunctuation/index.html">RiTa.isPunctuation()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isQuestion/index.html">RiTa.isQuestion()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isRhyme/index.html">RiTa.isRhyme()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isStopWord/index.html">RiTa.isStopWord()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/isVerb/index.html">RiTa.isVerb()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/kwic/index.html">RiTa.kwic()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/pastParticiple/index.html">RiTa.pastParticiple()</a><br/>
   </td>
   <td>
   <a href="https://rednoise.org/rita/reference/RiTa/phones/index.html">RiTa.phones()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/pos/index.html">RiTa.pos()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/posInline/index.html">RiTa.posInline()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/presentParticiple/index.html">RiTa.presentParticiple()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/pluralize/index.html">RiTa.pluralize()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/randomOrdering/index.html">RiTa.randomOrdering()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/randomSeed/index.html">RiTa.randomSeed()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/randomWord/index.html">RiTa.randomWord()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/rhymes/index.html">RiTa.rhymes()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/search/index.html">RiTa.search()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/sentences/index.html">RiTa.sentences()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/singularize/index.html">RiTa.singularize()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/soundsLike/index.html">RiTa.soundsLike()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/spellsLike/index.html">RiTa.spellsLike()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/stem/index.html">RiTa.stem()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/stresses/index.html">RiTa.stresses()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/syllables/index.html">RiTa.syllables()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/tokenize/index.html">RiTa.tokenize()</a><br/>
    <a href="https://rednoise.org/rita/reference/RiTa/untokenize/index.html">RiTa.untokenize()</a><br/>
   </td>
   <td>
    <a href="https://rednoise.org/rita/reference/Markov/addText/index.html">addText()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/completions/index.html">completions()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/generate/index.html">generate()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/probability/index.html">probability()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/probabilities/index.html">probabilities()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/size/index.html">size()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/toString/index.html">toString()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/toJSON/index.html">toJSON()</a><br/>
    <a href="https://rednoise.org/rita/reference/Markov/fromJSON/index.html">fromJSON()</a><br/>
    <br/><br/><br/><br/><br/><br/><br/><br/><br/>
   </td>
   <td>
    <a href="https://rednoise.org/rita/reference/Grammar/addRule/index.html">addRule()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/addRules/index.html">addRules()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/expand/index.html">expand()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/removeRule/index.html">removeRule()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/toJSON/index.html">toJSON()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/toString/index.html">toString()</a><br/>
    <a href="https://rednoise.org/rita/reference/Grammar/fromJSON/index.html">fromJSON()</a><br/>
    <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
   </td>
 </tr>
</table>
&nbsp;

## RiTaScript

RiTaScript can be used as part of any grammar (via RiTa.Grammar) or can be run directly using <a href="https://rednoise.org/rita/reference/RiTa/evaluate/index.html">RiTa.evaluate()</a>


### Choice
Select a random choice from a group of options:
```
The weather was (sad | gloomy | depressed).  ->  "The weather was gloomy." 
I'm (very | super | really) glad to ((meet | know) you | learn about you).  ->  "I'm very glad to know you." 
```
<br>

Use the seq() transform to output the Choice options in a linear sequence:
```
The weather was (sad | gloomy | depressed).seq()  ->  
  0) "The weather was sad" 
  1) "The weather was gloomy" 
  2) "The weather was depressed" 
  3) "The weather was sad" 
  ...
```

Use the rseq() transform to output the Choice options in a randomized, non-repeating sequence:
```
The weather was (sad | gloomy | depressed).rseq()  ->  
  0) "The weather was depressed" 
  1) "The weather was gloomy" 
  2) "The weather was sad" 
  3) "The weather was gloomy" 
  ...
```

Use the norep() transform to ensure outputs never repeats:
```
The weather was (sad | gloomy | depressed).norep()  ->  
  0) "The weather was depressed" 
  1) "The weather was gloomy" 
  2) "The weather was depressed" 
  3) "The weather was sad" 
  4) "The weather was gloomy" 
  ...
```

### Weighted Choice
Assign probabilities to choice selection

```
The weather was (sad | gloomy [2] | depressed[4]).  ->  "The weather was depressed." 
```

### Assignment
Basic assignments do not have output, they simply create or update a variable to be used elsewhere (variables in JavaScript may also be used when passed in via the scripts 'context')

```
$desc=wet and cold
The weather was $desc  ->  "The weather was wet and cold" 
```

### Inline Assignment

Inline assignments allow one to easily set a variable, output it, and refer to it later:

```
Jane was from [$place=(New York | Berlin | Shanghai)]. 
$place is cold and wet. 
     ->  "Jane was from Berlin. Berlin is cold and wet."

$place=(New York | Berlin | Shanghai)
$place is cold and wet in winter. 
     ->  "Berlin is cold and wet in the winter."
    
In [$place=(New York | Berlin | Shanghai)] it is cold and wet in winter. 
     ->  "In Berlin it is cold and wet in the winter."
```


### Transforms
Allow for modification of variables, choices, and raw text. RiScript comes with a number of useful transforms enabled (including pluralize(), capitalize(), and articlize()), which can be nested to create complex expressions. User-defined transforms can be added using RiTa.addTransform() or by passing a transform function as part of the script's 'context'.
```
How many (tooth | menu | child).pluralize() do you have?
How many (tooth | menu | child).pluralize().toUpper() do you have?
He grew up to be $animal.articlize().
He grew up to be (anteater).articlize().
He grew up to be (anteater).articlize().myCustomTransform().
```



<!--
### Choice

// Resolves choice without repeating
How many (tooth | menu | child).norepeat() do you have?

// Resolves choice in sequence
How many (tooth | menu | child).seq() do you have?

| | | 
|-|-|
| The weather was (sad &#124; gloomy &#124; depressed). | "The weather was depressed." |
| I'm (very &#124; super &#124; really) glad to ((meet &#124; know) you &#124; learn about you). | "I'm very glad to know you." |


### Weighted Choice
| | | 
|-|-|
| The weather was (sad &#124; gloomy [2] &#124; depressed[4]). | "The weather was gloomy." |

### Assignment

Basic assignments do not have output, they simply create/update a symbol
| | | 
|-|-|
|$desc=wet and cold||
|The weather was $desc|"The weather was wet and cold"|

### Inline Assignment

Inline assignments create/modify a symbol _and_ output its contents

| | | 
|-|-|
| `Jane was from [$place=(New York | Berlin | Shanghai)]. $place is cold and wet.` | `Jane was from Berlin. Berlin is cold and wet.` |
| `$place=(New York | Berlin | Shanghai)`<br/>`$place is cold and wet in winter.` | `Berlin is cold and wet in the winter.` |
| `In [$place=(New York | Berlin | Shanghai)] it is cold and wet in winter.` | `In Berlin it is cold and wet in the winter.` |


```
Jane was from [$place=(New York | Berlin | Shanghai)]. 
$place is cold and wet in the winter.

$place=(New York | Berlin | Shanghai) 
$place is cold and wet in the winter.

$place=(New York | Berlin | Shanghai) is cold and wet in the winter.

In [$place=(New York | Berlin | Shanghai)], it is cold and wet in winter.

In [$place=(New York | Berlin | Shanghai) it is cold and wet in winter].

```

### Symbols
Variables (or symbols) can be defined in RiScript or in JavaScript (and passed in via the 'context' argument)
```
$desc=dark and gloomy
The weather was $desc
```
&nbsp;&nbsp;&nbsp;&nbsp;or 
```
/* 'desc' defined in JS */
The weather was $desc
```
-->
### Conditionals
Allow for conditional execution, based on the values of one or more variables
```
// 'desc' can be defined in JS or RS */
{desc='party'} The party was happening
{desc='party', user=$john} The party was happening and John was wearing $John.color.
```
<!--
### Conditionals: If-else

```
{adj='positive'} The party was happening :: The party was not happening.
```
&nbsp;&nbsp;&nbsp;&nbsp;or 
```
{adj='positive'} The party was happening.
{adj!='positive'} The party was not happening.
```
<!--
### Labels
```
#Opening {
 The Fellow will be expected to teach one course. Apart from focusing on their own research and \
 teaching one course, the Fellow will be expected to give a presentation of their scholarship at the \
 Institute. The Fellow will also be expected to participate in the intellectual life of the community.
}

$Opening=(
 The Fellow will be expected to teach one course. Apart from focusing on their own research and \
 teaching one course, the Fellow will be expected to give a presentation of their scholarship at the \
 Institute. The Fellow will also be expected to participate in the intellectual life of the community.
)
```
-->

&nbsp;


### Developing
```sh
$ git clone https://github.com/dhowe/rita.git
$ cd rita
$ mvn install      # when done, you should see "BUILD SUCCESS"
```
The project requires a minimum version of Java 8 and Maven 3.6 to build.

### Eclipse
1. Do steps above under **Building**
2. In eclipse, File->Import->Maven->Existing Maven Projects and select your 'rita' folder
3. Right-click on project, and select 'Run-as' -> 'Maven install' or 'JUnit tests'

<br/>

## A Simple Example (Java)

&nbsp; &nbsp; For JavaScript examples, go [here](https://github.com/dhowe/ritajs#a-simple-sketch)

1. Create a new Java project in Eclipse (or your IDE of choice)
2. Download [rita.jar](http://rednoise.org/rita/download/rita.jar) and add it to the build path for the project. In eclipse: 'Project' > 'Properties' > 'Java Build Path' > 'Libraries' > 'Add External JARs...'
3. Create and run a new class, SimpleExample.java, with the following code:
```Java
import rita.*;

public class SimpleExample {

  public static void main(String[] args) {

    System.out.println(RiTa.analyze("The elephant took a bite!"));
  }
}
```

## In Processing

To install:

1. Open Processing and select 'Sketch' menu > 'Import Library...' > 'Add Library...'
2. Search for 'RiTa' and then install it

Create an example sketch as follows (and/or see the included examples):
```java
import rita.*;
import java.util.*;

void setup() {

  size(600, 200);
  background(50);
  textSize(20);
  noStroke();

  Map data = RiTa.analyze("The elephant took a bite!");

  float y = 15;
  for (Iterator it = data.keySet().iterator(); it.hasNext();) {
    String key = (String) it.next();
    text(key + ": " + data.get(key), 25, y += 26);
  }
}
```


## Contributors

### Code Contributors

This project exists only because of the people who contribute. Thank you!
<a href="https://github.com/dhowe/RiTa/graphs/contributors"><img src="https://opencollective.com/RiTa/contributors.svg?width=890&button=false" /></a>

### Financial Contributors
<a href="https://opencollective.com/rita/donate" target="_blank">
  <img src="https://opencollective.com/rita/contribute/button@2x.png?color=blue" width=300 />
</a>

