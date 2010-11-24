function MultiPhrase_Object(ParamText, ParamBookSearchInfoObject)
{
  this.mSearchText     = ParamText;
  this.mWords          = new Array();
  this.mPhrases        = new Array()
  this.mPhraseObjects  = new Array();
  this.mMatchRecords   = new Array();
  this.mBookSearchInfo = ParamBookSearchInfoObject;

  // Define functions for this Object
  //
  this.fGetWords             = MultiPhrase_GetWords;
  this.fGetPhrases           = MultiPhrase_GetPhrases;
  this.fPhraseCount          = MultiPhrase_PhraseCount;
  this.fGetMatches           = MultiPhrase_GetMatchingPhrases;
  this.fResetMatches         = MultiPhrase_ResetMatches;
  this.fCheckForMatch        = MultiPhrase_CheckForMatch;
  this.fParse                = MultiPhrase_Parse;
  this.t                     = MultiPhrase_Test;
  this.fTest                 = MultiPhrase_Test;
}

/******************** Accessors ********************/
// Returns all the search words that are not part of
// phrases and that are valid search terms for the book
//
function MultiPhrase_GetWords()
{
  return this.mWords;
}

// Returns all the phrases that for the current search text
// This includes all stop words.  The phrases are as they were
// typed in by the user
//
function MultiPhrase_GetPhrases()
{
  return this.mPhrases;
}

// Returns the count of valid phrases
// If this return value is 0, then no valid phrases exist
//
function MultiPhrase_PhraseCount()
{
  return this.mPhraseObjects.length;
}

// Returns an Array of Phrases that have matched
//
function MultiPhrase_GetMatchingPhrases()
{
  var matchArray = new Array();
  var index;

  for(index = 0; index < this.mPhraseObjects.length; ++index)
  {
    if(this.mPhraseObjects[index].fIsMatch())
    {
      matchArray[matchArray.length] = this.mPhraseObjects[index];
    }
  }

  return matchArray;
}
/***************** End Accessors ******************/

// Calls ParseWordsAnd Phrases to construct the mWords and mPhrases
// Array
//
function MultiPhrase_Parse()
{
  var WordsAndPhrases;
  var Index;
  var Words;
  var PhraseIndex;
  var Phrase;
  var PhraseObject;

  WordsAndPhrases = WWHStringUtilities_ParseWordsAndPhrases(this.mSearchText);

  for(Index = 0; Index < WordsAndPhrases.length; ++Index)
  {
    Words = WWHStringUtilities_ParseWordsAndPhrases(WordsAndPhrases[Index]);

    if(Words.length == 1)
    {
      if(this.mBookSearchInfo.fValidSearchWord(Words[0]))
      {
        this.mWords[this.mWords.length] = Words[0];
      }
    }
    else if(Words.length > 1)
    {
      // The entry is a phrase, construct the phrase here
      //
      Phrase = "";
      this.mPhrases[this.mPhrases.length] = WordsAndPhrases[Index];
      for(PhraseIndex = 0; PhraseIndex < Words.length; ++PhraseIndex)
      {
        if(this.mBookSearchInfo.fValidSearchWord(Words[PhraseIndex]))
        {
          Phrase += Words[PhraseIndex] + " ";
        }
      }
      if(Phrase.length > 0)
      {
        PhraseObject = new Phrase_Object(Phrase.substring(0, Phrase.length - 1));
        PhraseObject.fParse();
        this.mPhraseObjects[this.mPhraseObjects.length] = PhraseObject;
      }
    }
  }
}

// Tests all the phrases in mPhrases for matches on the
// Parameter Words
//
function MultiPhrase_Test(ParamFirst, ParamSecond)
{
  var index;
  var currentPhrase;

  for(index = 0; index < this.mPhraseObjects.length; ++index)
  {
    this.mPhraseObjects[index].fTestPhrase(ParamFirst, ParamSecond);
  }
}

// After the word pair file runs its tests on the multi phrase object
// this method will be called to see if all the phrases matched.  If they
// did then the book and file index will be recorded and stored.
//
function MultiPhrase_CheckForMatch(ParamBookIndex, ParamFileIndex)
{
  var result = false;
  var matchingPhrases = new Array();

  matchingPhrases = this.fGetMatches();

  if(this.mPhraseObjects.length > 0 &&
     matchingPhrases.length > 0 &&
     matchingPhrases.length == this.mPhraseObjects.length)
  {
    result = true;
  }

  return result;
}

// This function will be called between each pair file test to
// reset the matches from the previous pair file test.  The
// matches have been recorded so no information is lost
//
function MultiPhrase_ResetMatches()
{
  var index;
  for(index = 0; index < this.mPhraseObjects.length; ++index)
  {
    this.mPhraseObjects[index].fResetMatches();
  }
}